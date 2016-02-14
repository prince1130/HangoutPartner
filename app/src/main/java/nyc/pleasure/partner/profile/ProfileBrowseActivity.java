package nyc.pleasure.partner.profile;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nyc.pleasure.partner.R;

public class ProfileBrowseActivity extends AppCompatActivity
    implements ProfileBrowseFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_browse);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        if(savedInstanceState == null) {
            // Create the detail fragment and add it to the activity             // using a fragment transaction.
            ProfileBrowseFragment pbf = new ProfileBrowseFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.profile_browse_container, pbf).commit();
        }

    }

    public void onFragmentInteraction(Uri uri) {

    }

}
