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

import java.util.Calendar;

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
        public final TextView textViewEventPartner;
        public final TextView textViewEventDetail;
        public final TextView textViewEventId;
        public final Button buttonEventBack;
        public final Button buttonEventContact;

        public ViewHolder(View view) {
            textViewTitle = (TextView) view.findViewById(R.id.editTextTitle);
            textViewEventDate = (TextView) view.findViewById(R.id.editTextEventDate);
            textViewEventTime = (TextView) view.findViewById(R.id.editTextEventTime);
            textViewLocation = (TextView) view.findViewById(R.id.editTextLocation);
            textViewEventPartner = (TextView) view.findViewById(R.id.textViewEventPartner);
            textViewEventDetail = (TextView) view.findViewById(R.id.editTextEventDetail);
            textViewEventId = (TextView) view.findViewById(R.id.textViewEventId);
            buttonEventBack = (Button) view.findViewById(R.id.buttonEventBack);
            buttonEventContact = (Button) view.findViewById(R.id.buttonEventContact);
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

        viewHolderRef.buttonEventContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactEventHost();
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

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//// CLICK ACTION FUNCTIONS
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void goBackToEventBrowse() {
        Intent intent = new Intent(this.getActivity(), EventBrowseActivity.class);
        startActivity(intent);
    }

    private void contactEventHost() {
        Intent intent = new Intent(this.getActivity(), ChatActivity.class);
        startActivity(intent);
    }


/////////////////////////////////////////////////////////////////////////////////////
////    HELPER FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////

    private void updateView(ViewHolder viewHolder, FunEvent event) {
        viewHolder.textViewTitle.setText(event.getTitle());
        viewHolder.textViewEventDate.setText(getDateString(event.getStartTime()));
        viewHolder.textViewEventTime.setText(getTimeString(event.getStartTime()));
        viewHolder.textViewLocation.setText(event.getLocation());
        viewHolder.textViewEventPartner.setText(event.getCreaterUserDisplayName());
        viewHolder.textViewEventDetail.setText(event.getDetail());

        viewHolder.textViewEventId.setText(event.getEventId());
    }


    private String getDateString(Long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        Integer year = c.get(Calendar.YEAR);
        Integer month = c.get(Calendar.MONTH) + 1;
        Integer day = c.get(Calendar.DAY_OF_MONTH);

        String result = year.toString();
        if(month < 10) {
            result = result + "-0" + month;
        } else {
            result = result + "-" + month ;
        }
        if(day < 10) {
            result = result + "-0" + day;
        } else {
            result = result + "-" + day ;
        }
        return result;
    }

    private String getTimeString(Long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        Integer hour = c.get(Calendar.HOUR_OF_DAY);
        Integer min = c.get(Calendar.MINUTE);

        String result = null;
        if(hour < 10) {
            result = "0" + hour ;
        } else {
            result = "" + hour ;
        }
        if(min < 10) {
            result = result + ":0" + min ;
        } else {
            result = result + ":" + min ;
        }
        return result;
    }


}
