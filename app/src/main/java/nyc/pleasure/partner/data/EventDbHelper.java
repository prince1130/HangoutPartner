package nyc.pleasure.partner.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import nyc.pleasure.partner.data.EventContract.EventEntry;
import nyc.pleasure.partner.data.EventContract.UserEntry;

/**
 * Created by Chien on 12/27/2015.
 */
public class EventDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "hangout.db";

    private static final String SQL_DROP_EVENT = "DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME;
    private static final String SQL_DROP_USER = "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;

    private static final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
            UserEntry._ID + " INTEGER PRIMARY KEY, " +
            UserEntry.COLUMN_USER_ID + " INTEGER UNIQUE NOT NULL, " +
            UserEntry.COLUMN_DISPLAY_NAME + " TEXT UNIQUE NOT NULL, " +
            UserEntry.COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
            UserEntry.COLUMN_GENDER + " TEXT NOT NULL, " +
            UserEntry.COLUMN_AGE + " REAL NOT NULL, " +
            UserEntry.COLUMN_INTRODUCTION + " TEXT NOT NULL " +
            " );";

    final String SQL_CREATE_EVENT_TABLE = "CREATE TABLE " + EventEntry.TABLE_NAME + " (" +
            // Why AutoIncrement here, and not above? Unique keys will be auto-generated in either case.
            //  But for events, it's reasonable to assume the user will want information
            // for a certain date and all dates *following*, so the events should be sorted accordingly.
            EventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

            EventEntry.COLUMN_EVENT_ID + " INTEGER UNIQUE NOT NULL, " +
            EventEntry.COLUMN_CREATER_KEY + " TEXT NOT NULL, " +  // the ID of the USER created this EVENT
            EventEntry.COLUMN_START_TIME + " REAL NOT NULL, " +
            EventEntry.COLUMN_LOCATION + " TEXT NOT NULL, " +
            EventEntry.COLUMN_TITLE + " TEXT NOT NULL," +
            EventEntry.COLUMN_DETAIL + " TEXT NOT NULL, " +

            // Set up the location column as a foreign key to location table.
            " FOREIGN KEY (" + EventEntry.COLUMN_CREATER_KEY + ") REFERENCES " +
            UserEntry.TABLE_NAME + " (" + UserEntry._ID + "), " +

            // To assure the application have just one weather entry per day
            // per location, it's created a UNIQUE constraint with REPLACE strategy
            " UNIQUE (" + EventEntry.COLUMN_START_TIME + ", " +
            EventEntry.COLUMN_CREATER_KEY + ") ON CONFLICT REPLACE);";



    public EventDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE); // create USER very first.
        sqLiteDatabase.execSQL(SQL_CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DROP_EVENT); // drop EVENT first before drop USER.
        sqLiteDatabase.execSQL(SQL_DROP_USER); // drop USER at the end.
        onCreate(sqLiteDatabase);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}
