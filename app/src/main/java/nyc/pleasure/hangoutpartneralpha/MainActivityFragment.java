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
import nyc.pleasure.hangoutpartneralpha.event.EventBrowseActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private ViewHolder viewHolderRef;

    public static class ViewHolder {
        public final TextView textViewLoginStatus;
        public final TextView textViewLogin;
        public final TextView textViewEvent;
        public final TextView textViewMessage;


        public ViewHolder(View view) {
            textViewLoginStatus = (TextView) view.findViewById(R.id.login_status);
            textViewLogin = (TextView) view.findViewById(R.id.text_view_login);
            textViewEvent = (TextView) view.findViewById(R.id.text_view_event);
            textViewMessage = (TextView) view.findViewById(R.id.text_view_message);
        }
    }

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        viewHolderRef = new ViewHolder(rootView);
//        rootView.setTag(viewHolderRef);

        /**
         *   LOGIN
         */
        viewHolderRef.textViewLogin.setText(R.string.action_login);
        viewHolderRef.textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });


        /**
         *   BROWSE EVENTS
         */
        viewHolderRef.textViewEvent.setText(R.string.action_event);
        viewHolderRef.textViewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseEvent();
            }
        });


        /**
         *   VIEW MESSAGES
         */
        viewHolderRef.textViewMessage.setText(R.string.action_message);
        viewHolderRef.textViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMessage();
            }
        });

        return rootView;
    }


    @Override
    public void onStart() {

        /**
         *   LOGIN STATUS.
         */

        String userDisplayId = Utility.getLoggedInUserId(this.getContext());
        String userDisplayName = Utility.getLoggedInUserDisplayName(this.getContext());

        if(userDisplayId == null || userDisplayId.length() < 1) {
            // NOT LOGIN YET.
            viewHolderRef.textViewLoginStatus.setText("Connect with others through Interesting Events. Login to start the Fun. ");
            viewHolderRef.textViewLogin.setVisibility(View.VISIBLE);
            viewHolderRef.textViewEvent.setVisibility(View.GONE);
            viewHolderRef.textViewMessage.setVisibility(View.GONE);
        } else {
            // Use easy to read DISPLAY NAME instead of ID.
            viewHolderRef.textViewLoginStatus.setText("Welcome back !");
            viewHolderRef.textViewLogin.setVisibility(View.GONE);
            viewHolderRef.textViewEvent.setVisibility(View.VISIBLE);
            viewHolderRef.textViewMessage.setVisibility(View.VISIBLE);
        }

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

    private void doLogin() {
        Intent intent = new Intent(this.getActivity(), AuthActivity.class);
        if(intent.resolveActivity(this.getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void browseEvent() {
        Intent intent = new Intent(this.getActivity(), EventBrowseActivity.class);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void viewMessage() {
        Intent intent = new Intent(this.getActivity(), ChatActivity.class);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}
