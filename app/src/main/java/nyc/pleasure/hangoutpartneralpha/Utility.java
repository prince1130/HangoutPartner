package nyc.pleasure.hangoutpartneralpha;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Chien on 12/28/2015.
 */
public class Utility {

    public static String getCurrentUserName(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String defaultValue = "";
        return sharedPref.getString(context.getString(R.string.preference_user_name), defaultValue);
    }

    public static void setCurrentUserName(Context context, String userName) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.preference_user_name), userName);
        editor.commit();
    }

}
