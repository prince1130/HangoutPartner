package nyc.pleasure.partner.event;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import nyc.pleasure.partner.MainActivity;
import nyc.pleasure.partner.PreferenceUtility;
import nyc.pleasure.partner.R;
import nyc.pleasure.partner.adapter.EventListAdapter;
import nyc.pleasure.partner.firebase.FirebaseUtility;
import nyc.pleasure.partner.obj.FunEvent;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventBrowseFragment extends Fragment {

    public static final String LOG_TAG = EventBrowseFragment.class.getSimpleName();


    /////////////////////////////////////////////////////////////////////////////////////
////    PERSISTENCE
/////////////////////////////////////////////////////////////////////////////////////
    /* A reference to the Firebase */
    private FirebaseUtility mFirebaseUtility;
    private EventListAdapter mEventListAdapter;

    private ListView mListView;

/////////////////////////////////////////////////////////////////////////////////////
////    LIFECYCLE FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////

    public EventBrowseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseUtility = FirebaseUtility.getInstance(getResources());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_event_browse, container, false);

        mListView = (ListView) rootView.findViewById(R.id.listview_event);
        Button doneButton = (Button) rootView.findViewById(R.id.buttonEventDone);
        Button createButton = (Button) rootView.findViewById(R.id.buttonEventCreate);


        String userId = PreferenceUtility.getLoggedInUserId(this.getContext());
        mEventListAdapter = new EventListAdapter(mFirebaseUtility.getEventReference().limit(50), this.getActivity(), R.layout.list_item_event, userId);
        mListView.setAdapter(mEventListAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                FunEvent event = (FunEvent) adapterView.getItemAtPosition(position);
                if (event != null) {
                    ((Callback) getActivity()).onItemSelected(event.getEventId());
                }

            }
        });


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToMain();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goEventCreate();
            }
        });


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
        void onFragmentInteraction(Uri uri);
    }

    public interface Callback {
        public void onItemSelected(String eventId);
    }

/////////////////////////////////////////////////////////////////////////////////////
////    HELPER FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////

    private void goBackToMain() {
        Intent intent = new Intent(this.getActivity(), MainActivity.class);
        startActivity(intent);
    }

    private void goEventCreate() {
        Intent intent = new Intent(this.getActivity(), EventCreateActivity.class);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
