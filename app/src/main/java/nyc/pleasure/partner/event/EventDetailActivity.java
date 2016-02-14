package nyc.pleasure.partner.event;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import nyc.pleasure.partner.OptionsMenuUtility;
import nyc.pleasure.partner.R;
import nyc.pleasure.partner.PreferenceUtility;
import nyc.pleasure.partner.auth.AuthActivity;
import nyc.pleasure.partner.chat.ChatActivity;
import nyc.pleasure.partner.profile.ProfileUpdateActivity;

public class EventDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        if(savedInstanceState == null) {
            // Create the detail fragment and add it to the activity  using a fragment transaction.
            EventDetailFragment fragment = new EventDetailFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.event_detail_container, fragment).commit();

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
            OptionsMenuUtility.viewMyProfile(this);
            return true;
        } else if(id == R.id.action_message) {
            OptionsMenuUtility.viewMyMessage(this);
            return true;
        } else if(id == R.id.action_logout) {
            OptionsMenuUtility.doLogout(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//// CLICK ACTION FUNCTIONS
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
