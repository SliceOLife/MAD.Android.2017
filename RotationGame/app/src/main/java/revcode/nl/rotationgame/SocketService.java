package revcode.nl.rotationgame;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.squareup.otto.Subscribe;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import revcode.nl.rotationgame.events.DeviceRotationEvent;
import revcode.nl.rotationgame.events.DisplaySyncEvent;


/**
 * Created by Jordi on 9/20/2017.
 */

public class SocketService extends IntentService {

    private static final String TAG = "SocketService";
    private Socket mSocket;
    private static final String BASE_URL = "https://rotations.jordify.nl";
    private DeviceInfo ownDevice;

    private List<DeviceInfo> connectedDevices = new ArrayList<>();

    public SocketService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        Context mContext = getApplicationContext();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        try {
            mSocket = IO.socket(BASE_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        initSocket();
        GlobalBus.getInstance().register(this);
        return START_REDELIVER_INTENT;
    }

    private void initSocket() {
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on("rotation change", onRemoteRotationChanged);
        mSocket.on("device connected", onDeviceConnected);
        mSocket.on("device disconnected", onDeviceDisconnected);

        mSocket.connect();

        // Send device handshake to server
        deviceHandshake();
    }

    public void deviceHandshake() {
        Log.d(TAG, String.valueOf(getResources().getConfiguration().orientation));
        ownDevice = new DeviceInfo(Build.MODEL, DeviceInfo.DeviceType.ANDROID, DeviceInfo.DeviceState.PORTRAIT);
        if (mSocket.connected()) {
            mSocket.emit("connect device", ownDevice.toJson());
        }
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(mSocket.connected()) {
                Log.d(TAG, String.format("Connected to: %s", BASE_URL));
            }
        }
    };

    private Emitter.Listener onDeviceConnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            DeviceInfo deviceInstance = DeviceInfo.fromJson(args[0].toString());
            connectedDevices.add(deviceInstance);
            Log.i(TAG, String.format("Got a new connection: %s", args[0].toString()));
            syncDeviceStates();
        }
    };

    private Emitter.Listener onDeviceDisconnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            // Yes, this is a crappy way of removing a connected device.
            DeviceInfo deviceInstance = DeviceInfo.fromJson(args[0].toString());
            Log.i(TAG, String.format("Device %s disconnected", deviceInstance.deviceName));

            for(DeviceInfo device: connectedDevices) {
                if(Objects.equals(device.deviceName, deviceInstance.deviceName)) {
                    connectedDevices.remove(deviceInstance);
                }
            }
        }
    };

    private Emitter.Listener onRemoteRotationChanged = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            DeviceInfo deviceInstance = DeviceInfo.fromJson(args[0].toString());
            Log.i(TAG, String.format("Got a remote orientation change: %s", args[0].toString()));
            updateDeviceState(deviceInstance);
        }
    };

    public void onDestroy() {
        super.onDestroy();

        mSocket.emit("device disconnect", ownDevice.toJson());
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off("rotation change", onRemoteRotationChanged);
        mSocket.off("device connected", onDeviceConnected);
        mSocket.off("device disconnected", onDeviceDisconnected);

        // Unregister ourselves from the global event bus
        GlobalBus.getInstance().unregister(this);
    }

    /**
     * This method updates an already tracked remote device with their new
     * orientation state, it then fires off the syndDeviceStates method.
     *
     * {@link SocketService#syncDeviceStates()}
     * @param deviceInfo
     */
    public void updateDeviceState(DeviceInfo deviceInfo) {
        // Yes, this is a crappy way of updating a remote device state.
        for(DeviceInfo connectedDevice: connectedDevices) {
            if(Objects.equals(connectedDevice.deviceName, deviceInfo.deviceName)) {
                if(connectedDevice.deviceState != deviceInfo.deviceState) {
                    // Device states are not equal, update our list and fire to the event bus
                    int index = connectedDevices.indexOf(connectedDevice);
                    connectedDevices.set(index, deviceInfo);
                }
            }else{
                connectedDevices.add(deviceInfo);
            }
        }

        // We updated a remote devices orientation so we should re-sync orientation state
        syncDeviceStates();
    }

    /**
     * This method evaluates all the remote device orientation states
     * with our own orientation state which we bundle into a DisplaySyncEvent
     * and posts it to the global event bus.
     *
     * @see DisplaySyncEvent
     */
    public void syncDeviceStates() {
        boolean syncState = false;
        for(DeviceInfo connectedDevice: connectedDevices) {
            if(connectedDevice.deviceState == ownDevice.deviceState) {
                syncState = true;
            }else {
                syncState = false;
            }
        }

        if(syncState) {
            GlobalBus.getInstance().post(new DisplaySyncEvent(true));
        }else{
            GlobalBus.getInstance().post(new DisplaySyncEvent(false));
        }
    }

    /**
     * This method subscribes to rotation changes happening on this device that
     * were posted on the global event bus by the main activity.
     *
     * @param event
     */
    @Subscribe
    public void rotationChanged(DeviceRotationEvent event) {
        Log.d(TAG, "Device rotation event: " + event.getDeviceState().toString());

        // Update our own device and emit the change
        ownDevice.setDeviceState(event.getDeviceState());
        mSocket.emit("rotation change", ownDevice.toJson());

        // Because we updated our own rotation, we need to re-sync orientation states
        syncDeviceStates();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");
    }
}
