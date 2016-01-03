package nyc.pleasure.hangoutpartneralpha.event;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import nyc.pleasure.hangoutpartneralpha.MainActivity;
import nyc.pleasure.hangoutpartneralpha.R;
import nyc.pleasure.hangoutpartneralpha.chat.ChatActivity;
import nyc.pleasure.hangoutpartneralpha.firebase.FirebaseUtility;
import nyc.pleasure.hangoutpartneralpha.obj.FunEvent;
import nyc.pleasure.hangoutpartneralpha.obj.User;

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
        public final TextView textViewTitle;
        public final TextView textViewEventDate;
        public final TextView textViewEventTime;
        public final TextView textViewLocation;
        public final TextView textViewEventDetail;
        public final TextView textViewEventId;
        public final Button buttonEventBack;
        public final Button buttonEventMessage;

        public ViewHolder(View view) {
            textViewTitle = (TextView) view.findViewById(R.id.editTextTitle);
            textViewEventDate = (TextView) view.findViewById(R.id.editTextEventDate);
            textViewEventTime = (TextView) view.findViewById(R.id.editTextEventTime);
            textViewLocation = (TextView) view.findViewById(R.id.editTextLocation);
            textViewEventDetail = (TextView) view.findViewById(R.id.editTextEventDetail);
            textViewEventId = (TextView) view.findViewById(R.id.textViewEventId);
            buttonEventBack = (Button) view.findViewById(R.id.buttonEventBack);
            buttonEventMessage = (Button) view.findViewById(R.id.buttonEventMessage);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////
////    PERSISTENCE
/////////////////////////////////////////////////////////////////////////////////////
    /* A reference to the Firebase */
    private Firebase mFirebaseEventRef;
    private ValueEventListener mFirebaseEventValueListener = null;
    private String mEventId;

/////////////////////////////////////////////////////////////////////////////////////
////    LIFECYCLE FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////

    public EventDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent =  getActivity().getIntent();
        Uri data = intent.getData();
        mEventId = intent.getStringExtra("selectedEventId");
        mFirebaseEventRef = FirebaseUtility.getInstance(getResources()).getEventReference().child(mEventId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_event_detail, container, false);
        viewHolderRef = new ViewHolder(rootView);

        viewHolderRef.buttonEventBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToEventBrowse();
            }
        });

        viewHolderRef.buttonEventMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatWithEventHost();
            }
        });


        mFirebaseEventValueListener = mFirebaseEventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(LOG_TAG, "onDataChange. Rebuild UI with latest data. " + dataSnapshot.toString());
                FunEvent currentEvent = dataSnapshot.getValue(FunEvent.class);
                if (currentEvent != null) { //// This user was found.
                    updateView(viewHolderRef, currentEvent);
                } else { //// No such event stored. Shouldn't be here.

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return rootView;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mFirebaseEventRef.removeEventListener(mFirebaseEventValueListener);
    }

/////////////////////////////////////////////////////////////////////////////////////
////    HELPER FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////

    private void goBackToEventBrowse() {
        Intent intent = new Intent(this.getActivity(), EventBrowseActivity.class);
        startActivity(intent);
    }

    private void chatWithEventHost() {
        Intent intent = new Intent(this.getActivity(), ChatActivity.class);
        startActivity(intent);
    }

    private void updateView(ViewHolder viewHolder, FunEvent event) {
        viewHolder.textViewTitle.setText(event.getTitle());
        viewHolder.textViewEventDate.setText(parseDate(event.getEndTime()));
        viewHolder.textViewEventTime.setText(parseTime(event.getEndTime()));
        viewHolder.textViewLocation.setText(event.getLocation());
        viewHolder.textViewEventDetail.setText(event.getDetail());

        viewHolder.textViewEventId.setText(event.getEventId());
    }

    private String parseDate(Long time) {
        return "2015-01-01";
    }
    private String parseTime(Long time) {
        return "11:11:11";
    }


}
