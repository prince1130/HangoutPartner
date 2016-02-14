package nyc.pleasure.partner;

import android.app.Activity;
import android.content.Intent;

import nyc.pleasure.partner.auth.AuthActivity;
import nyc.pleasure.partner.message.MessageBrowseActivity;
import nyc.pleasure.partner.profile.ProfileUpdateActivity;

/**
 * Created by Chien on 2/14/2016.
 */
public class OptionsMenuUtility {

    public static void viewMyProfile(Activity activity) {
        Intent intent = new Intent(activity, ProfileUpdateActivity.class);
        if(intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    public static void viewMyMessage(Activity activity) {
        Intent intent = new Intent(activity, MessageBrowseActivity.class);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    public static void doLogout(Activity activity) {
        /// Need to destroy preference saved earlier.
        PreferenceUtility.clearPreference(activity);
        Intent intent = new Intent(activity, AuthActivity.class);
        if(intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

}
