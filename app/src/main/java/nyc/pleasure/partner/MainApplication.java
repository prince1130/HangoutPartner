package nyc.pleasure.partner;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;

/**
 * Created by Chien on 12/20/2015.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(this);
//        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }
}
