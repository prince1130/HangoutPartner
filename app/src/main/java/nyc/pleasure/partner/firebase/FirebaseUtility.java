package nyc.pleasure.partner.firebase;

import android.content.res.Resources;
import android.util.Log;

import com.firebase.client.Firebase;

import nyc.pleasure.partner.R;

/**
 * Created by Chien on 1/2/2016.
 */
public class FirebaseUtility {

    public static final String LOG_TAG = FirebaseUtility.class.getSimpleName();

    /* A reference to the Firebase */
    private static String FIREBASE_URL = null;
    private static Firebase mFirebaseRootRef = null;
    private static Firebase mFirebaseUserRef = null;
    private static Firebase mFirebaseEventRef = null;
    private static Firebase mFirebaseMessageRef = null;

    private static Integer lock = 0;
    private static FirebaseUtility anInstance = null;

    private FirebaseUtility(Resources res) {
        if(FIREBASE_URL == null) {
            FIREBASE_URL = res.getString(R.string.firebase_url);
        }

        Log.i(LOG_TAG, " FIREBASE_URL " + FIREBASE_URL);
        // Setup our Firebase mFirebaseRef
        mFirebaseRootRef = new Firebase(FIREBASE_URL).getRoot();
        mFirebaseUserRef = new Firebase(FIREBASE_URL).child("user");
        mFirebaseEventRef = new Firebase(FIREBASE_URL).child("event");
        mFirebaseMessageRef = new Firebase(FIREBASE_URL).child("message");
    }

    public static FirebaseUtility getInstance(Resources res) {
        synchronized (lock) {
            if(anInstance == null) {
                anInstance = new FirebaseUtility(res);
            }
        }
        return anInstance;
    }

    public Firebase getRootReference() {
        return mFirebaseRootRef;
    }

    public Firebase getUserReference() {
        return mFirebaseUserRef;
    }

    public Firebase getEventReference() {
        return mFirebaseEventRef;
    }

    public Firebase getMessageReference() { return mFirebaseMessageRef; }

}
