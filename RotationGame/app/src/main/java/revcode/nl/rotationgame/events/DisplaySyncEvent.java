package revcode.nl.rotationgame.events;

/**
 * Created by Jordi on 9/20/2017.
 */

public class DisplaySyncEvent {
    private boolean remoteDeviceOrientationsInSync;

    public DisplaySyncEvent(Boolean devicesInSync) {
        this.remoteDeviceOrientationsInSync = devicesInSync;
    }

    public boolean areRemoteDeviceOrientationsInSync() {
        return remoteDeviceOrientationsInSync;
    }
}
