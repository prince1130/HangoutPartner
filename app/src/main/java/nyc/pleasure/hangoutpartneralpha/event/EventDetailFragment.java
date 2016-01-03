package nyc.pleasure.hangoutpartneralpha.event;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

import nyc.pleasure.hangoutpartneralpha.R;
import nyc.pleasure.hangoutpartneralpha.firebase.FirebaseUtility;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailFragment extends Fragment {

    public static final String LOG_TAG = EventDetailFragment.class.getSimpleName();

    /////////////////////////////////////////////////////////////////////////////////////
////    VIEW
/////////////////////////////////////////////////////////////////////////////////////
    private ViewHolder viewHolderRef;

    ////  editTextTitle   editTextEventDate  editTextEventTime   editTextLocation   editTextEventDetail   buttonEventSave   buttonEventCancel
    public static class ViewHolder {
        public final TextView editTextTitle;
        public final TextView editTextEventDate;
        public final TextView editTextEventTime;
        public final TextView editTextLocation;
        public final TextView editTextEventDetail;
        public final Button buttonEventSave;
        public final Button buttonEventCancel;

        public ViewHolder(View view) {
            editTextTitle = (TextView) view.findViewById(R.id.editTextTitle);
            editTextEventDate = (TextView) view.findViewById(R.id.editTextEventDate);
            editTextEventTime = (TextView) view.findViewById(R.id.editTextEventTime);
            editTextLocation = (TextView) view.findViewById(R.id.editTextLocation);
            editTextEventDetail = (TextView) view.findViewById(R.id.editTextEventDetail);
            buttonEventSave = (Button) view.findViewById(R.id.buttonEventSave);
            buttonEventCancel = (Button) view.findViewById(R.id.buttonEventCancel);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////
////    PERSISTENCE
/////////////////////////////////////////////////////////////////////////////////////
    /* A reference to the Firebase */
    private FirebaseUtility mFirebaseUtility;

/////////////////////////////////////////////////////////////////////////////////////
////    LIFECYCLE FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////

    public EventDetailFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_event_detail, container, false);
        viewHolderRef = new ViewHolder(rootView);


        return rootView;
    }


/////////////////////////////////////////////////////////////////////////////////////
////    HELPER FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////



}
