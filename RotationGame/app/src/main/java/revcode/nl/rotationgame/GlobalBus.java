package revcode.nl.rotationgame;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by Jordi on 9/20/2017.
 */

public class GlobalBus {
    private static Bus mBus;

    public static Bus getInstance() {
        if(null == mBus) {
            mBus = new Bus(ThreadEnforcer.ANY);
        }
        return mBus;
    }
}
