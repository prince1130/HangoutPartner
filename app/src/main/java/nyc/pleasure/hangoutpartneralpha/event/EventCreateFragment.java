package nyc.pleasure.hangoutpartneralpha.event;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

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

    int PLACE_PICKER_REQUEST = 10;

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
        public final TextView textViewLocationName;
        public final TextView textViewLocationAddress;
        public final Button buttonEventLocation;
        public final EditText editTextEventDetail;
        public final Button buttonEventSave;
        public final Button buttonEventCancel;

        public ViewHolder(View view) {
            editTextTitle = (EditText) view.findViewById(R.id.editTextTitle);
            textViewEventDate = (TextView) view.findViewById(R.id.textViewEventDate);
            textViewEventTime = (TextView) view.findViewById(R.id.textViewEventTime);
            buttonEventDate = (Button) view.findViewById(R.id.buttonEventDate);
            buttonEventTime = (Button) view.findViewById(R.id.buttonEventTime);
            textViewLocationName = (TextView) view.findViewById(R.id.textViewLocationName);
            textViewLocationAddress = (TextView) view.findViewById(R.id.textViewLocationAddress);
            buttonEventLocation = (Button) view.findViewById(R.id.buttonEventLocation);
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

        viewHolderRef.buttonEventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickLocation();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == this.getActivity().RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this.getActivity());
                LatLng placeLL = place.getLatLng();
                String placeAdd = String.valueOf(place.getAddress());
                String placeName = String.valueOf(place.getName());

                viewHolderRef.textViewLocationName.setText(placeName);
                viewHolderRef.textViewLocationAddress.setText(placeAdd);

                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this.getActivity(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }




//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//// CLICK ACTION FUNCTIONS
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void pickLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            Intent intent = builder.build(this.getActivity());
            if (intent.resolveActivity(this.getActivity().getPackageManager()) != null) {
                startActivityForResult(intent, PLACE_PICKER_REQUEST);
            }
        } catch (GooglePlayServicesRepairableException ex) {
            Log.e(LOG_TAG, "pickLocation() " + ex);
        } catch (GooglePlayServicesNotAvailableException ex) {
            Log.e(LOG_TAG, "pickLocation() " + ex);
        }
    }

    private void doEventCreate(ViewHolder viewHolder) {
        FunEvent event = new FunEvent();
        event.setCreatedTime(System.currentTimeMillis());
        event.setCreaterUserId(Utility.getLoggedInUserId(this.getContext()));
        event.setCreaterUserDisplayName(Utility.getLoggedInUserDisplayName(this.getContext()));
        event.setTitle(viewHolder.editTextTitle.getText().toString());
        event.setDetail(viewHolder.editTextEventDetail.getText().toString());

        event.setStartTime(convertTime(selectedYear, selectedMonth - 1, selectedDay, selectedHour, selectedMin));
        event.setLocationName(viewHolder.textViewLocationName.getText().toString());
        event.setLocationAddress(viewHolder.textViewLocationAddress.getText().toString());

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
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, selectedYear, selectedMonth, selectedDay);
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
