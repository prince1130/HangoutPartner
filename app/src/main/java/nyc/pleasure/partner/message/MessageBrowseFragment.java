package nyc.pleasure.partner.message;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import nyc.pleasure.partner.PreferenceUtility;
import nyc.pleasure.partner.R;
import nyc.pleasure.partner.adapter.ChatListAdapter;
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
    private ChatListAdapter mMessageListAdapter;

    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseUtility = FirebaseUtility.getInstance(getResources());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message_browse, container, false);         // Inflate the layout for this fragment

        mListView = (ListView) rootView.findViewById(R.id.listview_message);

        String userId = PreferenceUtility.getLoggedInUserId(this.getContext());
        mMessageListAdapter = new ChatListAdapter(mFirebaseUtility.getMessageReference().limit(50), this.getActivity(), R.layout.list_item_chat, userId);
        mListView.setAdapter(mMessageListAdapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMessageListAdapter.cleanup();
    }


}
