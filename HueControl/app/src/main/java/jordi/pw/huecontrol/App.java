package jordi.pw.huecontrol;

import android.app.Application;
import android.content.Context;

import com.androidnetworking.AndroidNetworking;

/**
 * Created by jordi on 10/28/17.
 */
public class App extends Application {
    private static App mInstance;
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        this.setAppContext(getApplicationContext());
        AndroidNetworking.initialize(getApplicationContext());
    }

    public static App getInstance(){
        return mInstance;
    }
    public static Context getAppContext() {
        return mAppContext;
    }
    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
    }
}