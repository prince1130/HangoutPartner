package nyc.pleasure.partner.event;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import nyc.pleasure.partner.PreferenceUtility;
import nyc.pleasure.partner.profile.ProfileUpdateActivity;
import nyc.pleasure.partner.R;
import nyc.pleasure.partner.auth.AuthActivity;
import nyc.pleasure.partner.chat.ChatActivity;

public class EventBrowseActivity extends AppCompatActivity
        implements EventBrowseFragment.OnFragmentInteractionListener, EventBrowseFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_browse);

        if(savedInstanceState == null) {
            // Create the detail fragment and add it to the activity using a fragment transaction.
            EventBrowseFragment fragment = new EventBrowseFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.event_browse_container, fragment).commit();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String userId = PreferenceUtility.getLoggedInUserId(this);

        if(userId != null && userId.length() > 0) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        } else {
            // NOT LOGIN YET. Don't display Menu.
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_account) {
            viewAccount();
            return true;
        } else if(id == R.id.action_message) {
            viewMessage();
            return true;
        } else if(id == R.id.action_logout) {
            doLogout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//// CLICK ACTION FUNCTIONS
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void viewAccount() {
        Intent intent = new Intent(this, ProfileUpdateActivity.class);
        if(intent.resolveActivity(this.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void viewMessage() {
        Intent intent = new Intent(this, ChatActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void doLogout() {
        /// Need to destroy preference saved earlier.
        PreferenceUtility.clearPreference(this);
        Intent intent = new Intent(this, AuthActivity.class);
        if(intent.resolveActivity(this.getPackageManager()) != null) {
            startActivity(intent);
        }
    }


/////////////////////////////////////////////////////////////////////////////////////
////    CALLBACK FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////

    public void onFragmentInteraction(Uri uri) {
        Intent intent = new Intent(this, EventDetailActivity.class).setData(uri);
        startActivity(intent);
    }

    public void onItemSelected(String eventId) {
        PreferenceUtility.setSelectedEventId(this, eventId);
        Intent intent = new Intent(this, EventDetailActivity.class);
        startActivity(intent);
    }


}
