package nyc.pleasure.partner.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;


/**
 * Created by Chien on 12/27/2015.
 */
public class EventContract {

    // The "Content authority" is a name for the entire content provider, similar to the relationship between a domain name and its website.
    //  A convenient string to use for the content authority is the package name for the app, which is guaranteed to be unique on the device.
    public static final String CONTENT_AUTHORITY = "nyc.pleasure.partner";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact  the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_USER = "user";
    public static final String PATH_EVENT = "event";


    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /* Inner class that defines the table contents of the user table */
    public static final class UserEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        // Table name
        public static final String TABLE_NAME = "user";

        // The user id is what will be sent to web service as the user query as the location query.
        public static final String COLUMN_USER_ID = "user_id";

        // Human readable location string, provided by the API.  Because for styling,
        // "Charles Lee" is more recognizable than user45678.
        public static final String COLUMN_DISPLAY_NAME = "display_name";

        // A few useful Columns
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_INTRODUCTION = "introduction";

        public static Uri buildUserUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


    /* Inner class that defines the table contents of the event table */
    public static final class EventEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EVENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENT;

        public static final String TABLE_NAME = "event";

        public static final String COLUMN_EVENT_ID = "event_id";

        // Column with the foreign key into the user table.
        public static final String COLUMN_CREATER_KEY = "creater_id";

        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_END_TIME = "end_time";
        public static final String COLUMN_LOCATION = "location";

        // Short description and long description of the event.
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DETAIL = "detail";

        // A few useful Columns

        /////////////////////////////////////////////////////////////////////////////////
        //// URI BUILDER
        /////////////////////////////////////////////////////////////////////////////////
        public static Uri buildEventUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildEventByUserUri(long date, long userId) {
            long normalizedDate = normalizeDate(date);
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(normalizedDate))
                    .appendQueryParameter(COLUMN_CREATER_KEY, Long.toString(userId)).build();
        }

        public static Uri buildEventAfterDateUri(long date) {
            long normalizedDate = normalizeDate(date);
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(normalizedDate)).build();
        }

        /////////////////////////////////////////////////////////////////////////////////
        //// VARIABLE GETTER
        /////////////////////////////////////////////////////////////////////////////////
        public static long getDateFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }

        public static long getUserIdFromUri(Uri uri) {
            String userId = uri.getQueryParameter(COLUMN_CREATER_KEY);
            if (null != userId && userId.length() > 0)
                return Long.parseLong(userId);
            else
                return 0;
        }

    }


}
