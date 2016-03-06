package nyc.pleasure.partner;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nyc.pleasure.partner.auth.AuthActivity;
import nyc.pleasure.partner.auth.LoginEmailActivity;
import nyc.pleasure.partner.event.EventBrowseActivity;
import nyc.pleasure.partner.event.EventCreateActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private ViewHolder viewHolderRef;

    public static class ViewHolder {
        public final TextView textViewLoginStatus;
        public final TextView textViewLogin;
        public final TextView textViewEventBrowse;
//        public final TextView textViewEventCreate;


        public ViewHolder(View view) {
            textViewLoginStatus = (TextView) view.findViewById(R.id.login_status);
            textViewLogin = (TextView) view.findViewById(R.id.text_view_login);
            textViewEventBrowse = (TextView) view.findViewById(R.id.text_view_event_browse);
//            textViewEventCreate = (TextView) view.findViewById(R.id.text_view_event_create);
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
        viewHolderRef.textViewEventBrowse.setText(R.string.action_event_browse);
        viewHolderRef.textViewEventBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseEvent();
            }
        });


        /**
         *   VIEW MESSAGES
         */
/*
        viewHolderRef.textViewEventCreate.setText(R.string.action_event_create);
        viewHolderRef.textViewEventCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
            }
        });
*/

        return rootView;
    }


    @Override
    public void onStart() {

        /**
         *   LOGIN STATUS.
         */

        String userDisplayId = PreferenceUtility.getLoggedInUserId(this.getContext());
        String userDisplayName = PreferenceUtility.getLoggedInUserDisplayName(this.getContext());

        if(userDisplayId == null || userDisplayId.length() < 1) {
            // NOT LOGIN YET.
            viewHolderRef.textViewLoginStatus.setText("Connect with others through Interesting Events. Login to start the Fun. ");
            viewHolderRef.textViewLogin.setVisibility(View.VISIBLE);
            viewHolderRef.textViewEventBrowse.setVisibility(View.GONE);
//            viewHolderRef.textViewEventCreate.setVisibility(View.GONE);
        } else {
            // Use easy to read DISPLAY NAME instead of ID.
            viewHolderRef.textViewLoginStatus.setText("Welcome back " + userDisplayName + " !");
            viewHolderRef.textViewLogin.setVisibility(View.GONE);
            viewHolderRef.textViewEventBrowse.setVisibility(View.VISIBLE);
//            viewHolderRef.textViewEventCreate.setVisibility(View.VISIBLE);
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
        Intent intent = new Intent(this.getActivity(), LoginEmailActivity.class);
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

    private void createEvent() {
        Intent intent = new Intent(this.getActivity(), EventCreateActivity.class);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
