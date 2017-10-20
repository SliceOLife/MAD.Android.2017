package revcode.nl.rotationgame;

import com.google.gson.Gson;

/**
 * Created by jordi on 9/19/17.
 */

public class DeviceInfo {
    public String deviceName;
    public DeviceType deviceType;
    public DeviceState deviceState;

    public DeviceInfo(String name, DeviceType type, DeviceState state) {
        this.deviceName = name;
        this.deviceType = type;
        this.deviceState = state;
    }

    public void setDeviceState(DeviceState state) {
        this.deviceState = state;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static DeviceInfo fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, DeviceInfo.class);
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
