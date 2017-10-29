package jordi.pw.huecontrol.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import jordi.pw.huecontrol.Interfaces.BulbsLoadedCallback;

/**
 * Created by jordi on 10/27/17.
 */

public class Bridge implements Parcelable {
    private static final String TAG = "Bridge";

    private String name;
    private String endpoint;
    private String identifier;

    private ArrayList<Bulb> bulbs;

    public Bridge(String name, String endpoint, String identifier) {
        this.name = name;
        this.endpoint = endpoint;
        this.identifier = identifier;

        this.bulbs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ArrayList<Bulb> getBulbs() {
        return bulbs;
    }

    protected Bridge(Parcel in) {
        name = in.readString();
        endpoint = in.readString();
        identifier = in.readString();
        if (in.readByte() == 0x01) {
            bulbs = new ArrayList<Bulb>();
            in.readList(bulbs, Bulb.class.getClassLoader());
        } else {
            bulbs = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(endpoint);
        dest.writeString(identifier);
        if (bulbs == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(bulbs);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Bridge> CREATOR = new Parcelable.Creator<Bridge>() {
        @Override
        public Bridge createFromParcel(Parcel in) {
            return new Bridge(in);
        }

        @Override
        public Bridge[] newArray(int size) {
            return new Bridge[size];
        }
    };

    private void parseBulbs(JSONObject response) throws JSONException {
        for (Iterator<String> iterator = response.keys(); iterator.hasNext();) {
            // Create a new bulb
            String cur = iterator.next();
            Bulb bulb = new Bulb(this.endpoint, this.identifier, Integer.valueOf(cur));
            bulb.setName(response.getJSONObject(cur).getString("name"));
            bulb.setState(response.getJSONObject(cur).getJSONObject("state").getBoolean("on"));

            JSONObject stateObj = response.getJSONObject(cur).getJSONObject("state");
            bulb.setHue(stateObj.getInt("hue"));
            bulb.setBrightness(stateObj.getInt("bri"));
            bulb.setSaturation(stateObj.getInt("sat"));

            // Add bulb to list
            bulbs.add(bulb);
        }
    }


    public void getAvailableBulbs(final BulbsLoadedCallback callback) throws Exception {
        final String bulbEndpoint = String.format("http://%s/api/%s/lights/", endpoint, identifier);

        // Request bulbs from endpoint
        AndroidNetworking.get(bulbEndpoint)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        // Parse bulbs and call our callback
                        try {
                            parseBulbs(response);
                        } catch (JSONException e) {
                            // Error during JSON parsing
                            Log.d(TAG, e.getMessage());
                        }
                        callback.onBulbsLoaded();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, anError.getMessage());
                    }
                });
    }
}