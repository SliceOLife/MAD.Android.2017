package revcode.nl.rotationgame;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import revcode.nl.rotationgame.events.DeviceRotationEvent;
import revcode.nl.rotationgame.events.DisplaySyncEvent;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "RotationActivity";

    // List of connected clients
    private List<DeviceInfo> connectedClients = new ArrayList<>();
    private Bus busInstance = GlobalBus.getInstance();

    private TextView tvSyncState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get UI element
        tvSyncState = (TextView)findViewById(R.id.tvSyncState);

        // Register ourselves with the global event bus.
        GlobalBus.getInstance().register(this);

        // Decrease orientation integer by one as enums start at 0.
        int orientationState = getResources().getConfiguration().orientation - 1;

        // Post our current orientation state over the event bus
        busInstance.post(new DeviceRotationEvent(DeviceInfo.DeviceState.values()[orientationState]));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister from the global event bus.
        GlobalBus.getInstance().unregister(this);
    }


    /**
     * This method handles a display sync event and updates the display color if necessary.
     * @param event
     */
    @Subscribe
    public void syncDisplayState(DisplaySyncEvent event) {
        if(event.areRemoteDeviceOrientationsInSync()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                    tvSyncState.setText("SYNC OK");
                }
            });
        }else if(!event.areRemoteDeviceOrientationsInSync()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                    tvSyncState.setText("SYNC NOT OK");
                }
            });
        }
    }
}
