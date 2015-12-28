package nyc.pleasure.hangoutpartneralpha.event;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nyc.pleasure.hangoutpartneralpha.R;

public class EventBrowseActivity extends AppCompatActivity
        implements EventBrowseFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_browse);

        if(savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            EventBrowseFragment fragment = new EventBrowseFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.event_browse_container, fragment).commit();


        }

    }

    public void onFragmentInteraction(Uri uri) {

    }

}
