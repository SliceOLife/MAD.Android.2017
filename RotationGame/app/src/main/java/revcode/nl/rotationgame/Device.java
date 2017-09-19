package revcode.nl.rotationgame;

/**
 * Created by jordi on 9/19/17.
 */

public class Device {
    public String deviceIdentifier;
    public String deviceName;
    public DeviceType deviceType;
    public DeviceState deviceState;

    public Device(String identifier, String name, DeviceType type, DeviceState state) {
        this.deviceIdentifier = identifier;
        this.deviceName = name;
        this.deviceState = state;
    }

    public enum DeviceType {
        IPHONE,
        ANDROID
    }

    public enum DeviceState {
        PORTRAIT,
        LANDSCAPE
    }
}
