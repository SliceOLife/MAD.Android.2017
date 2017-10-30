package jordi.pw.huecontrol.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

/**
 * Created by jordi on 10/27/17.
 */

public class Bulb implements Parcelable {
    private static final String TAG = "Bulb";

    private String endpoint;
    private String name;
    private boolean state;

    private Integer hue;
    private Integer brightness;
    private Integer saturation;


    public Bulb(String endpoint, String identifier, Integer id) {
        this.endpoint = String.format("http://%s/api/%s/lights/%d/state/", endpoint, identifier, id);
        Log.d(TAG, String.format("I'm bulb %s, with endpoint: %s", id, this.endpoint));
    }

    public void toggle() {
        // Execute a request and toggle state
        execRequest(String.format("{\"on\":%s}", Boolean.toString(!state)));
        state = !state;
    }

    public void updateHue(Integer hue) {
        // Execute a request and set hue
        execRequest(String.format("{\"hue\":%d}", hue));
        this.hue = hue;
    }

    public void updateBrightness(Integer brightness) {
        // Exec request
        execRequest(String.format("{\"bri\":%d}", brightness));
        this.brightness = brightness;
    }

    public void updateSaturation(Integer saturation) {
        // Exec request
        execRequest(String.format("{\"sat\":%d}", saturation));
        this.saturation = saturation;
    }

    protected Bulb(Parcel in) {
        endpoint = in.readString();
        name = in.readString();
        state = in.readByte() != 0x00;
        hue = in.readByte() == 0x00 ? null : in.readInt();
        brightness = in.readByte() == 0x00 ? null : in.readInt();
        saturation = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(endpoint);
        dest.writeString(name);
        dest.writeByte((byte) (state ? 0x01 : 0x00));
        if (hue == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(hue);
        }
        if (brightness == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(brightness);
        }
        if (saturation == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(saturation);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Bulb> CREATOR = new Parcelable.Creator<Bulb>() {
        @Override
        public Bulb createFromParcel(Parcel in) {
            return new Bulb(in);
        }

        @Override
        public Bulb[] newArray(int size) {
            return new Bulb[size];
        }
    };

    private void execRequest(String requestBody) {
        AndroidNetworking.put(endpoint)
                .addStringBody(requestBody)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        // Log?
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, anError.getMessage());
                    }
                });
    }

    public String getName() {
        return name;
    }

    public boolean isState() {
        return state;
    }

    public Integer getHue() {
        return hue;
    }

    public Integer getBrightness() {
        return brightness;
    }

    public Integer getSaturation() {
        return saturation;
    }

    public void setSaturation(Integer saturation) { this.saturation = saturation; }

    public void setBrightness(Integer brightness) { this.brightness = brightness; }

    public void setHue(Integer hue) { this.hue = hue; }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}