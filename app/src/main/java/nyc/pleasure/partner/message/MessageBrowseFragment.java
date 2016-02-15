package nyc.pleasure.partner.message;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.Firebase;

import nyc.pleasure.partner.PreferenceUtility;
import nyc.pleasure.partner.R;
import nyc.pleasure.partner.adapter.MessageListAdapter;
import nyc.pleasure.partner.firebase.FirebaseUtility;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageBrowseFragment extends Fragment {

    public static final String LOG_TAG = MessageBrowseFragment.class.getSimpleName();

/////////////////////////////////////////////////////////////////////////////////////
////    PERSISTENCE
/////////////////////////////////////////////////////////////////////////////////////
    /* A reference to the Firebase */
    private FirebaseUtility mFirebaseUtility;
    private Firebase mFirebaseUserRef = null;
    private MessageListAdapter mMessageListAdapter;

    private ListView mListView;
    private String myUserId = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myUserId = PreferenceUtility.getLoggedInUserId(this.getContext());
        mFirebaseUtility = FirebaseUtility.getInstance(getResources());
        mFirebaseUserRef = mFirebaseUtility.getUserReference().child(myUserId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message_browse, container, false);         // Inflate the layout for this fragment

        mListView = (ListView) rootView.findViewById(R.id.listview_message);

        mMessageListAdapter = new MessageListAdapter(mFirebaseUserRef.child("chatMessages").limit(50), this.getActivity(), R.layout.list_item_message, myUserId);
        mListView.setAdapter(mMessageListAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String msgBucketId = (String)adapterView.getItemAtPosition(position);
                if (msgBucketId != null) {
                    ((Callback) getActivity()).onItemSelected(msgBucketId);
                }

            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMessageListAdapter.cleanup();
    }

/////////////////////////////////////////////////////////////////////////////////////
////    CALLBACK FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    public interface Callback {
        public void onItemSelected(String msgBucketId);
    }

}
