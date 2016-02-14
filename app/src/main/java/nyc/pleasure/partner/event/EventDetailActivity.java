package nyc.pleasure.partner.event;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nyc.pleasure.partner.R;

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
}
