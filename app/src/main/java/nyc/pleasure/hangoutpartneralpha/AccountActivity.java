package nyc.pleasure.hangoutpartneralpha;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        if(savedInstanceState == null) {
            // Create the detail fragment and add it to the activity             // using a fragment transaction.
            AccountFragment fragment = new AccountFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.account_container, fragment).commit();
        }

    }

}
