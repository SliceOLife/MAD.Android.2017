package revcode.nl.rotationgame;

import android.app.Application;
import android.content.Intent;

/**
 * Created by Jordi on 9/20/2017.
 */

public class RotationApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Intent intent = new Intent(this, SocketService.class);
        startService(intent);
    }
}
