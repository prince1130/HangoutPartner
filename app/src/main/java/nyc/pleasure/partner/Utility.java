package nyc.pleasure.partner;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Chien on 12/28/2015.
 */
public class Utility {

    public static String getLoggedInUserId(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String defaultValue = null;
        return sharedPref.getString(context.getString(R.string.preference_user_id), defaultValue);
    }

    public static void setLoggedInUserId(Context context, String userId) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.preference_user_id), userId);
        editor.commit();
    }


    public static String getLoggedInUserDisplayName(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String defaultValue = null;
        return sharedPref.getString(context.getString(R.string.preference_display_name), defaultValue);
    }

    public static void setLoggedInUserDisplayName(Context context, String userId) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.preference_display_name), userId);
        editor.commit();
    }

    public static void clearPreference(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }

}
