package nyc.pleasure.hangoutpartneralpha.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Chien on 12/28/2015.
 */
public class HangoutSyncService extends Service {

    public final String LOG_TAG = HangoutSyncService.class.getSimpleName();

    private static final Object sSyncAdapterLock = new Object();
    private static HangoutSyncAdapter sHangoutSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d( LOG_TAG, "onCreate - HangoutSyncService");
        synchronized (sSyncAdapterLock) {
            if (sHangoutSyncAdapter == null) {
                sHangoutSyncAdapter = new HangoutSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sHangoutSyncAdapter.getSyncAdapterBinder();
    }

}
