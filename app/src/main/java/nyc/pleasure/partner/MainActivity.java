package nyc.pleasure.partner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import nyc.pleasure.partner.auth.AuthActivity;
import nyc.pleasure.partner.chat.ChatActivity;
import nyc.pleasure.partner.profile.ProfileUpdateActivity;
import nyc.pleasure.partner.sync.HangoutSyncAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        HangoutSyncAdapter.initializeSyncAdapter(this);

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String userId = Utility.getLoggedInUserId(this);

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

    @Override
    public void onStart() {
        super.onStart();
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
        Utility.clearPreference(this);
        Intent intent = new Intent(this, AuthActivity.class);
        if(intent.resolveActivity(this.getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    /*
    private void test() {
        super.onCreate(null);
        super.onCreateView(null, null, null);
        super.onStart();
        super.onStop();
        super.onDestroyView();
        super.onDestroy();
    }
    */

}
