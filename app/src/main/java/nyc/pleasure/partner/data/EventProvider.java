package nyc.pleasure.partner.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Chien on 12/27/2015.
 */
public class EventProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private EventDbHelper mOpenHelper;

    static final int EVENT_ALL = 100;
    static final int EVENT_AFTER_TIME = 101;
    static final int EVENT_AFTER_TIME_AND_USER = 102;
    static final int USER_ALL = 201;

    private static final SQLiteQueryBuilder sEventByUserIdQueryBuilder = new SQLiteQueryBuilder();
    static {
        sEventByUserIdQueryBuilder.setTables(
                EventContract.EventEntry.TABLE_NAME + " INNER JOIN " +
                        EventContract.UserEntry.TABLE_NAME +
                        " ON " + EventContract.EventEntry.TABLE_NAME +
                        "." + EventContract.EventEntry.COLUMN_CREATER_KEY +
                        " = " + EventContract.UserEntry.TABLE_NAME +
                        "." + EventContract.UserEntry.COLUMN_USER_ID

        );
    }


    //location.location_setting = ?
    private static final String sEventAfterTimeSelection =
            EventContract.EventEntry.TABLE_NAME + "." + EventContract.EventEntry.COLUMN_START_TIME + " > ? ";

    private static final String sEventAfterTimeAndUserSelection =
            EventContract.EventEntry.TABLE_NAME + "." + EventContract.EventEntry.COLUMN_START_TIME + " > ? AND "
                    + EventContract.UserEntry.TABLE_NAME + "." +  EventContract.UserEntry.COLUMN_USER_ID + " = ? " ;

    @Override
    public boolean onCreate() {
        mOpenHelper = new EventDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case EVENT_ALL:
                return EventContract.EventEntry.CONTENT_ITEM_TYPE;
            case EVENT_AFTER_TIME:
                return EventContract.EventEntry.CONTENT_TYPE;
            case EVENT_AFTER_TIME_AND_USER:
                return EventContract.EventEntry.CONTENT_TYPE;
            case USER_ALL:
                return EventContract.UserEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri = null;

        switch (match) {
            case EVENT_ALL: {
                long _id = db.insert(EventContract.EventEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = EventContract.EventEntry.buildEventUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case USER_ALL: {
                long _id = db.insert(EventContract.UserEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = EventContract.UserEntry.buildUserUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case EVENT_ALL:
                rowsUpdated = db.update(EventContract.EventEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case USER_ALL:
                rowsUpdated = db.update(EventContract.UserEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case EVENT_ALL:
                rowsDeleted = db.delete(EventContract.EventEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case USER_ALL:
                rowsDeleted = db.delete(EventContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "event all"
            case EVENT_ALL: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        EventContract.EventEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "event that happen after certain date /*/*"
            case EVENT_AFTER_TIME: {
                retCursor = getEventAfterTime(uri, projection, sortOrder);
                break;
            }
            // "event that created by certain user and happen after certain time "
            case EVENT_AFTER_TIME_AND_USER : {
                retCursor = getEventAfterTimeAndUser(uri, projection, sortOrder);
                break;
            }

            // "user specific/*"
            case USER_ALL: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        EventContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    private Cursor getEventAfterTime(Uri uri, String[] projection, String sortOrder) {
        long time = EventContract.EventEntry.getDateFromUri(uri);

        return sEventByUserIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sEventAfterTimeSelection,
                new String[]{Long.toString(time)},
                null,
                null,
                sortOrder
        );
    }


    private Cursor getEventAfterTimeAndUser(Uri uri, String[] projection, String sortOrder) {
        long time = EventContract.EventEntry.getDateFromUri(uri);
        long userId = EventContract.EventEntry.getUserIdFromUri(uri);

        return sEventByUserIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sEventAfterTimeAndUserSelection,
                new String[]{Long.toString(time), Long.toString(userId)},
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = EventContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, EventContract.PATH_EVENT, EVENT_ALL);
        matcher.addURI(authority, EventContract.PATH_EVENT + "/*", EVENT_AFTER_TIME);
        matcher.addURI(authority, EventContract.PATH_EVENT + "/*/*", EVENT_AFTER_TIME_AND_USER);
        matcher.addURI(authority, EventContract.PATH_USER, USER_ALL);
        return matcher;
    }


}
