package nyc.pleasure.hangoutpartneralpha.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/**
 * Created by Chien on 12/29/2015.
 */
public class HangoutAuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private HangoutAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new HangoutAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }

}
