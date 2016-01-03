package nyc.pleasure.hangoutpartneralpha.event;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.firebase.client.Firebase;


import nyc.pleasure.hangoutpartneralpha.MainActivity;
import nyc.pleasure.hangoutpartneralpha.R;
import nyc.pleasure.hangoutpartneralpha.Utility;
import nyc.pleasure.hangoutpartneralpha.adapter.EventListAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventBrowseFragment extends Fragment {

    public static final String LOG_TAG = EventBrowseFragment.class.getSimpleName();


    /////////////////////////////////////////////////////////////////////////////////////
////    PERSISTENCE
/////////////////////////////////////////////////////////////////////////////////////
    /* A reference to the Firebase */
    private static String FIREBASE_URL = null;
    private Firebase mFirebaseRootRef = null;
    private Firebase mFirebaseEventRef = null;
//    private ValueEventListener mFirebaseEventValueListener = null;
    private EventListAdapter mEventListAdapter;


/////////////////////////////////////////////////////////////////////////////////////
////    LIFECYCLE FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////

    public EventBrowseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FIREBASE_URL = getResources().getString(R.string.firebase_url);
        Log.i(LOG_TAG, " FIREBASE_URL " + FIREBASE_URL);
        // Setup our Firebase mFirebaseRef
        mFirebaseRootRef = new Firebase(FIREBASE_URL).getRoot();
        mFirebaseEventRef = new Firebase(FIREBASE_URL).child("event");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_event_browse, container, false);

        final ListView listView = (ListView) rootView.findViewById(R.id.listview_event);

        String userId = Utility.getLoggedInUserId(this.getContext());
        mEventListAdapter = new EventListAdapter(mFirebaseEventRef.limit(50), this.getActivity(), R.layout.list_item_event, userId);
        listView.setAdapter(mEventListAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mEventListAdapter.cleanup();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


/////////////////////////////////////////////////////////////////////////////////////
////    HELPER FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////


    private void goBackToMain() {
        Intent afterAuthenticatedIntent = new Intent(this.getActivity(), MainActivity.class);
        startActivity(afterAuthenticatedIntent);
    }


}
