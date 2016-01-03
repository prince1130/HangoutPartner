package nyc.pleasure.hangoutpartneralpha.event;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import nyc.pleasure.hangoutpartneralpha.MainActivity;
import nyc.pleasure.hangoutpartneralpha.R;
import nyc.pleasure.hangoutpartneralpha.Utility;
import nyc.pleasure.hangoutpartneralpha.firebase.FirebaseUtility;
import nyc.pleasure.hangoutpartneralpha.obj.FunEvent;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventCreateFragment extends Fragment {

    public static final String LOG_TAG = EventCreateFragment.class.getSimpleName();

/////////////////////////////////////////////////////////////////////////////////////
////    VIEW
/////////////////////////////////////////////////////////////////////////////////////
    private ViewHolder viewHolderRef;

    ////  editTextTitle   editTextEventDate  editTextEventTime   editTextLocation   editTextEventDetail   buttonEventSave   buttonEventCancel
    public static class ViewHolder {
        public final EditText editTextTitle;
        public final EditText editTextEventDate;
        public final EditText editTextEventTime;
        public final EditText editTextLocation;
        public final EditText editTextEventDetail;
        public final Button buttonEventSave;
        public final Button buttonEventCancel;

        public ViewHolder(View view) {
            editTextTitle = (EditText) view.findViewById(R.id.editTextTitle);
            editTextEventDate = (EditText) view.findViewById(R.id.editTextEventDate);
            editTextEventTime = (EditText) view.findViewById(R.id.editTextEventTime);
            editTextLocation = (EditText) view.findViewById(R.id.editTextLocation);
            editTextEventDetail = (EditText) view.findViewById(R.id.editTextEventDetail);
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

    public EventCreateFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_event_create, container, false);
        viewHolderRef = new ViewHolder(rootView);
//        rootView.setTag(viewHolderRef);

        viewHolderRef.buttonEventSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doEventCreate(viewHolderRef);
            }
        });

        viewHolderRef.buttonEventCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToMain();
            }
        });

        return rootView;
    }


/////////////////////////////////////////////////////////////////////////////////////
////    HELPER FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////

    ////  editTextTitle   editTextEventDate  editTextEventTime   editTextLocation   editTextEventDetail   buttonEventSave   buttonEventCancel

    private void doEventCreate(ViewHolder viewHolder) {
        FunEvent event = new FunEvent();
        event.setCreatedTime(System.currentTimeMillis());
        event.setCreaterUserId(Utility.getLoggedInUserId(this.getContext()));
        event.setTitle(viewHolder.editTextTitle.getText().toString());
        event.setLocation(viewHolder.editTextLocation.getText().toString());
        event.setDetail(viewHolder.editTextEventDetail.getText().toString());

        String date = viewHolder.editTextEventDate.getText().toString();
        String time = viewHolder.editTextEventTime.getText().toString();
        event.setStartTime(convertTime(date, time));

        Long eventId = event.getCreatedTime();
        mFirebaseUtility.getEventReference().child(eventId.toString()).setValue(event);

        goBackToMain();
    }

    private long convertTime(String date, String time) {
        return System.currentTimeMillis();
    }

    private void goBackToMain() {
        Intent afterAuthenticatedIntent = new Intent(this.getActivity(), MainActivity.class);
        startActivity(afterAuthenticatedIntent);
    }

}
