package revcode.nl.rotationgame.events;

import revcode.nl.rotationgame.DeviceInfo;

/**
 * Created by Jordi on 9/20/2017.
 */

public class DeviceRotationEvent {
    private DeviceInfo.DeviceState deviceState;

    public DeviceRotationEvent(DeviceInfo.DeviceState deviceState) {
        this.deviceState = deviceState;
    }

    public DeviceInfo.DeviceState getDeviceState() {
        return this.deviceState;
    }
}
