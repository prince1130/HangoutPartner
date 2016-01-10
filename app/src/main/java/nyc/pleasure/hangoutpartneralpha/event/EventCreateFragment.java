package nyc.pleasure.hangoutpartneralpha.event;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import nyc.pleasure.hangoutpartneralpha.R;
import nyc.pleasure.hangoutpartneralpha.Utility;
import nyc.pleasure.hangoutpartneralpha.firebase.FirebaseUtility;
import nyc.pleasure.hangoutpartneralpha.obj.FunEvent;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventCreateFragment extends Fragment
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final String LOG_TAG = EventCreateFragment.class.getSimpleName();

/////////////////////////////////////////////////////////////////////////////////////
////    VIEW
/////////////////////////////////////////////////////////////////////////////////////
    private ViewHolder viewHolderRef;

    ////  editTextTitle   editTextEventDate  editTextEventTime   editTextLocation   editTextEventDetail   buttonEventSave   buttonEventCancel
    public static class ViewHolder {
        public final EditText editTextTitle;
        public final TextView textViewEventDate;
        public final TextView textViewEventTime;
        public final Button buttonEventDate;
        public final Button buttonEventTime;
        public final EditText editTextLocation;
        public final EditText editTextEventDetail;
        public final Button buttonEventSave;
        public final Button buttonEventCancel;

        public ViewHolder(View view) {
            editTextTitle = (EditText) view.findViewById(R.id.editTextTitle);
            textViewEventDate = (TextView) view.findViewById(R.id.textViewEventDate);
            textViewEventTime = (TextView) view.findViewById(R.id.textViewEventTime);
            buttonEventDate = (Button) view.findViewById(R.id.buttonEventDate);
            buttonEventTime = (Button) view.findViewById(R.id.buttonEventTime);
            editTextLocation = (EditText) view.findViewById(R.id.editTextLocation);
            editTextEventDetail = (EditText) view.findViewById(R.id.editTextEventDetail);
            buttonEventSave = (Button) view.findViewById(R.id.buttonEventSave);
            buttonEventCancel = (Button) view.findViewById(R.id.buttonEventCancel);
        }
    }

    private final static Integer DEFAULT_YEAR = 2016;
    private final static Integer DEFAULT_MONTH = 12;
    private final static Integer DEFAULT_DAY = 31;
    private final static Integer DEFAULT_HOUR = 18;
    private final static Integer DEFAULT_MINUTE = 30;

    private Integer selectedYear = DEFAULT_YEAR;
    private Integer selectedMonth = DEFAULT_MONTH;
    private Integer selectedDay = DEFAULT_DAY;
    private Integer selectedHour = DEFAULT_HOUR;
    private Integer selectedMin = DEFAULT_MINUTE;

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

        viewHolderRef.textViewEventDate.setText(DEFAULT_YEAR + "-" + DEFAULT_MONTH + "-" + DEFAULT_DAY);
        viewHolderRef.textViewEventTime.setText(DEFAULT_HOUR + ":" + DEFAULT_MINUTE);

        viewHolderRef.buttonEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        viewHolderRef.buttonEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        viewHolderRef.buttonEventSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doEventCreate(viewHolderRef);
            }
        });

        viewHolderRef.buttonEventCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBrowseEvent();
            }
        });

        return rootView;
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//// CLICK ACTION FUNCTIONS
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////  editTextTitle   editTextEventDate  editTextEventTime   editTextLocation   editTextEventDetail   buttonEventSave   buttonEventCancel

    private void doEventCreate(ViewHolder viewHolder) {
        FunEvent event = new FunEvent();
        event.setCreatedTime(System.currentTimeMillis());
        event.setCreaterUserId(Utility.getLoggedInUserId(this.getContext()));
        event.setCreaterUserDisplayName(Utility.getLoggedInUserDisplayName(this.getContext()));
        event.setTitle(viewHolder.editTextTitle.getText().toString());
        event.setLocation(viewHolder.editTextLocation.getText().toString());
        event.setDetail(viewHolder.editTextEventDetail.getText().toString());

        event.setStartTime(convertTime(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMin));

        Long eventId = event.getCreatedTime();
        event.setEventId(eventId.toString());
        mFirebaseUtility.getEventReference().child(eventId.toString()).setValue(event);

        goBrowseEvent();
    }


    private void goBrowseEvent() {
        Intent intent = new Intent(this.getActivity(), EventBrowseActivity.class);
        startActivity(intent);
    }

/////////////////////////////////////////////////////////////////////////////////////
////    CALLBACK FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////


    public void showDatePickerDialog(View v) {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, DEFAULT_YEAR, (DEFAULT_MONTH - 1), DEFAULT_DAY);
        dialog.show();
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the time chosen by the user
        month++;
        viewHolderRef.textViewEventDate.setText(year + "-" + month + "-" + day);
        selectedYear = year;
        selectedMonth = month;
        selectedDay = day;
    }


    public void showTimePickerDialog(View v) {
        TimePickerDialog dialog = new TimePickerDialog(getContext(), this, DEFAULT_HOUR, DEFAULT_MINUTE, true);
        dialog.show();
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        viewHolderRef.textViewEventTime.setText(hourOfDay + ":" + minute);
        selectedHour = hourOfDay;
        selectedMin = minute;
    }


/////////////////////////////////////////////////////////////////////////////////////
////    HELPER FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////


    private long convertTime(int year, int month, int day, int hour, int min) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, min);
        return cal.getTimeInMillis();
    }



}
