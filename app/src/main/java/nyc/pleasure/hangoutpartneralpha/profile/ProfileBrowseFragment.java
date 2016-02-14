package nyc.pleasure.hangoutpartneralpha.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import nyc.pleasure.hangoutpartneralpha.MediaUploadActivity;
import nyc.pleasure.hangoutpartneralpha.R;
import nyc.pleasure.hangoutpartneralpha.chat.ChatActivity;
import nyc.pleasure.hangoutpartneralpha.event.EventBrowseActivity;
import nyc.pleasure.hangoutpartneralpha.firebase.FirebaseUtility;
import nyc.pleasure.hangoutpartneralpha.obj.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileBrowseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ProfileBrowseFragment extends Fragment {

    public static final String LOG_TAG = ProfileBrowseFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

/////////////////////////////////////////////////////////////////////////////////////
////    VIEW
/////////////////////////////////////////////////////////////////////////////////////
    private ViewHolder viewHolderRef;

    public static class ViewHolder {
        public final EditText editTextDisplayName;
        public final Spinner spinnerGender;
        public final EditText editTextDescriptionMe;
        public final TextView textViewAcctId;

        public final Button buttonMedia;
        public final Button buttonMessage;
        public final Button buttonDone;

        public ViewHolder(View view) {

            editTextDisplayName = (EditText) view.findViewById(R.id.editTextDisplayName);
            spinnerGender = (Spinner) view.findViewById(R.id.spinnerGender);
            editTextDescriptionMe = (EditText) view.findViewById(R.id.editTextDescriptionMe);
            textViewAcctId = (TextView) view.findViewById(R.id.textViewAcctId);

            buttonMedia = (Button) view.findViewById(R.id.buttonMedia);
            buttonMessage = (Button) view.findViewById(R.id.buttonMessage);
            buttonDone = (Button) view.findViewById(R.id.buttonDone);
        }
    }

    private ArrayAdapter<CharSequence> genderAdapter = null;

/////////////////////////////////////////////////////////////////////////////////////
////    PERSISTENCE
/////////////////////////////////////////////////////////////////////////////////////

    /* A reference to the Firebase */
    private Firebase mFirebaseUserRef;
    private String selectedUserId;

/////////////////////////////////////////////////////////////////////////////////////
////    LIFECYCLE FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////

    public ProfileBrowseFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent =  getActivity().getIntent();
        Uri data = intent.getData();
         //// Before comes here. the source Activity have to let us know the id with    Intent intent = new Intent(this, ProfileBrowseActivity.class).putExtra("selectedProfileId", userId);
        selectedUserId = intent.getStringExtra("selectedProfileId");
        mFirebaseUserRef = FirebaseUtility.getInstance(getResources()).getUserReference().child(selectedUserId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_profile_browse, container, false);
        viewHolderRef = new ViewHolder(rootView);

        genderAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        viewHolderRef.spinnerGender.setAdapter(genderAdapter);


        viewHolderRef.buttonMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseMedia();
            }
        });

        viewHolderRef.buttonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        viewHolderRef.buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//// CLICK ACTION FUNCTIONS
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void browseMedia() {
        Intent mediaIntent = new Intent(this.getActivity(), MediaUploadActivity.class);
        mediaIntent.putExtra("selectedProfileId", selectedUserId);
        if (mediaIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
            startActivity(mediaIntent);
        }
    }

    private void sendMessage() {
        Intent messageIntent = new Intent(this.getActivity(), ChatActivity.class);
        messageIntent.putExtra("selectedProfileId", selectedUserId);
        if (messageIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
            startActivity(messageIntent);
        }
    }

    private void goBack() {
        Intent intent = new Intent(this.getActivity(), EventBrowseActivity.class);
        startActivity(intent);
    }

/////////////////////////////////////////////////////////////////////////////////////
////    CALLBACK FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////




/////////////////////////////////////////////////////////////////////////////////////
////    HELPER FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////

    private void updateView(ViewHolder viewHolder, User user) {
        viewHolder.editTextDisplayName.setText(user.getDisplayName());

        int genPosition = genderAdapter.getPosition(user.getGender());
        viewHolder.spinnerGender.setSelection(genPosition);

        viewHolder.editTextDescriptionMe.setText(user.getDescriptionMe());
        viewHolder.textViewAcctId.setText(user.getUserId());
    }


}
