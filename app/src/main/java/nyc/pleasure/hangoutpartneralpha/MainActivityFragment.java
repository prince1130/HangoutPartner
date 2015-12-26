package nyc.pleasure.hangoutpartneralpha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nyc.pleasure.hangoutpartneralpha.auth.AuthActivity;
import nyc.pleasure.hangoutpartneralpha.chat.ChatActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private FragmentActivity fragAct;
    private ViewHolder viewHolderRef;

    public static class ViewHolder {
        public final TextView textViewAccount;
        public final TextView textViewMessage;
        public final TextView textViewEvent;
        public final TextView textViewLoginStatus;

        public ViewHolder(View view) {
            textViewAccount = (TextView) view.findViewById(R.id.text_view_account);
            textViewMessage = (TextView) view.findViewById(R.id.text_view_message);
            textViewEvent = (TextView) view.findViewById(R.id.text_view_event);
            textViewLoginStatus = (TextView) view.findViewById(R.id.login_status);
        }
    }

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        viewHolderRef = new ViewHolder(rootView);
        rootView.setTag(viewHolderRef);

        viewHolderRef.textViewAccount.setText(R.string.action_account);
        viewHolderRef.textViewMessage.setText(R.string.action_message);
        viewHolderRef.textViewEvent.setText(R.string.action_event);

        fragAct = this.getActivity();

        viewHolderRef.textViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragAct, ChatActivity.class);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }

        });

        return rootView;
    }

    public void onStart() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String defaultValue = "";
        String userName = sharedPref.getString(getString(R.string.preference_user_name), defaultValue);
        viewHolderRef.textViewLoginStatus.setText("Welcome " + userName + " !");

        super.onStart();
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
