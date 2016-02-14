package nyc.pleasure.hangoutpartneralpha;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Calendar;

import nyc.pleasure.hangoutpartneralpha.firebase.FirebaseUtility;
import nyc.pleasure.hangoutpartneralpha.obj.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment
        implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    public static final String LOG_TAG = AccountFragment.class.getSimpleName();

/////////////////////////////////////////////////////////////////////////////////////
////    VIEW
/////////////////////////////////////////////////////////////////////////////////////
    private ViewHolder viewHolderRef;

    public static class ViewHolder {
        public final EditText editTextEmail;
        public final EditText editTextDisplayName;
        public final EditText editTextFirstName;
        public final EditText editTextLastName;

        public final Spinner spinnerGender;
        public final Button buttonBirthDate;
        public final EditText editTextDescriptionMe;

        public final TextView textViewAcctId;
        public final Button buttonMedia;
        public final Button buttonSave;
        public final Button buttonCancel;

        public ViewHolder(View view) {
            editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
            editTextDisplayName = (EditText) view.findViewById(R.id.editTextDisplayName);
            editTextFirstName = (EditText) view.findViewById(R.id.editTextFirstName);
            editTextLastName = (EditText) view.findViewById(R.id.editTextLastName);

            spinnerGender = (Spinner) view.findViewById(R.id.spinnerGender);
            buttonBirthDate = (Button) view.findViewById(R.id.buttonBirthDate);
            editTextDescriptionMe = (EditText) view.findViewById(R.id.editTextDescriptionMe);

            textViewAcctId = (TextView) view.findViewById(R.id.textViewAcctId);
            buttonMedia = (Button) view.findViewById(R.id.buttonMedia);
            buttonSave = (Button) view.findViewById(R.id.buttonSave);
            buttonCancel = (Button) view.findViewById(R.id.buttonCancel);
        }
    }

    private ArrayAdapter<CharSequence> genderAdapter = null;

    private final static Integer DEFAULT_YEAR = 2016;
    private final static Integer DEFAULT_MONTH = 12;
    private final static Integer DEFAULT_DAY = 31;

    private Integer selectedYear = DEFAULT_YEAR;
    private Integer selectedMonth = DEFAULT_MONTH;
    private Integer selectedDay = DEFAULT_DAY;

/////////////////////////////////////////////////////////////////////////////////////
////    PERSISTENCE
/////////////////////////////////////////////////////////////////////////////////////

    /* A reference to the Firebase */
    private Firebase mFirebaseUserRef;
//    private ValueEventListener mFirebaseUserValueListener = null;
//    private String mUserId;

/////////////////////////////////////////////////////////////////////////////////////
////    LIFECYCLE FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup our Firebase mFirebaseRef
        String mUserId = Utility.getLoggedInUserId(this.getContext());
        mFirebaseUserRef = FirebaseUtility.getInstance(getResources()).getUserReference().child(mUserId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        viewHolderRef = new ViewHolder(rootView);
//        rootView.setTag(viewHolderRef);


        // Create an ArrayAdapter using the string array and a default spinner layout
        genderAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        viewHolderRef.spinnerGender.setAdapter(genderAdapter);
        viewHolderRef.spinnerGender.setOnItemSelectedListener(this);


        viewHolderRef.buttonBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        viewHolderRef.buttonMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageMedia();
            }
        });

        viewHolderRef.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAccountSave(viewHolderRef);
            }
        });

        viewHolderRef.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToMain();
            }
        });


        //// IF THE USER DATA EXIST ALREADY. RETRIEVE THEM AND POPULATE THE UI WITH EXISTING DATA.
        //// OTHERWISE, DO NOT POPULATE ANYTHING.
        mFirebaseUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(LOG_TAG, "onDataChange. Rebuild UI with latest data. " + dataSnapshot.toString());
                User currentUser = dataSnapshot.getValue(User.class);
                if (currentUser != null) { //// This user was found.
                    updateView(viewHolderRef, currentUser);
                } else { //// No such user stored.

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
//        mFirebaseUserRef.removeEventListener(mFirebaseUserValueListener);
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//// CLICK ACTION FUNCTIONS
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void doAccountSave(ViewHolder viewHolder) {
        String userId = Utility.getLoggedInUserId(this.getContext());
        Utility.setLoggedInUserDisplayName(this.getContext(), viewHolder.editTextDisplayName.getText().toString());

        User user = new User();
        user.setUserId(userId);
        user.setEmail(viewHolder.editTextEmail.getText().toString());
        user.setDisplayName(viewHolder.editTextDisplayName.getText().toString());
        user.setFirstName(viewHolder.editTextFirstName.getText().toString());
        user.setLastName(viewHolder.editTextLastName.getText().toString());

        String gen = (String)viewHolder.spinnerGender.getSelectedItem();
        user.setGender(gen);
        user.setBirthDate(convertTime(selectedYear, selectedMonth - 1, selectedDay, 0, 0));

        user.setDescriptionMe(viewHolder.editTextDescriptionMe.getText().toString());

        mFirebaseUserRef.setValue(user);
        goBackToMain();
    }

    private void manageMedia() {
        Intent mediaIntent = new Intent(this.getActivity(), MediaUploadActivity.class);
        startActivity(mediaIntent);
    }

    private void goBackToMain() {
        Intent mainIntent = new Intent(this.getActivity(), MainActivity.class);
        startActivity(mainIntent);
    }

    public void showDatePickerDialog(View v) {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, selectedYear, selectedMonth, selectedDay);
        dialog.show();
    }


/////////////////////////////////////////////////////////////////////////////////////
////    CALLBACK FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the time chosen by the user
        month++;
        viewHolderRef.buttonBirthDate.setText(year + "-" + month + "-" + day);
        selectedYear = year;
        selectedMonth = month;
        selectedDay = day;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

/////////////////////////////////////////////////////////////////////////////////////
////    HELPER FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////

    private void updateView(ViewHolder viewHolder, User user) {
        viewHolder.editTextEmail.setText(user.getEmail());
        viewHolder.editTextDisplayName.setText(user.getDisplayName());
        viewHolder.editTextFirstName.setText(user.getFirstName());
        viewHolder.editTextLastName.setText(user.getLastName());

        int genPosition = genderAdapter.getPosition(user.getGender());
        viewHolder.spinnerGender.setSelection(genPosition);

        Calendar c = Calendar.getInstance();
        if(user.getBirthDate() == null) {
            user.setBirthDate(convertTime(DEFAULT_YEAR, DEFAULT_MONTH - 1, DEFAULT_DAY, 0 , 0));
        }
        c.setTimeInMillis(user.getBirthDate());
        viewHolderRef.buttonBirthDate.setText(c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1)+ "-" + c.get(Calendar.DAY_OF_MONTH));

        viewHolder.editTextDescriptionMe.setText(user.getDescriptionMe());

        viewHolder.textViewAcctId.setText(user.getUserId());
    }

    private long convertTime(int year, int month, int day, int hour, int min) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, min);
        return cal.getTimeInMillis();
    }

}
