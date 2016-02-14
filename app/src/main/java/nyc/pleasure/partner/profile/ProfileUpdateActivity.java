package nyc.pleasure.partner.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nyc.pleasure.partner.R;

public class ProfileUpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

        if(savedInstanceState == null) {
            // Create the detail fragment and add it to the activity             // using a fragment transaction.
            ProfileUpdateFragment fragment = new ProfileUpdateFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.account_container, fragment).commit();
        }

    }

}
