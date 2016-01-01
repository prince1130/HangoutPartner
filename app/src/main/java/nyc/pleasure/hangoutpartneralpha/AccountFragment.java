package nyc.pleasure.hangoutpartneralpha;


import android.content.Intent;
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

import nyc.pleasure.hangoutpartneralpha.obj.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    public static final String LOG_TAG = AccountFragment.class.getSimpleName();

/////////////////////////////////////////////////////////////////////////////////////
////    VIEW
/////////////////////////////////////////////////////////////////////////////////////
    private ViewHolder viewHolderRef;

    public static class ViewHolder {
        public final EditText editTextFirstName;
        public final EditText editTextLastName;
        public final EditText editTextDisplayName;
        public final EditText editTextGender;
        public final EditText editTextEmail;
        public final TextView textViewAcctId;
        public final Button buttonSave;
        public final Button buttonCancel;

        public ViewHolder(View view) {
            editTextFirstName = (EditText) view.findViewById(R.id.editTextFirstName);
            editTextLastName = (EditText) view.findViewById(R.id.editTextLastName);
            editTextDisplayName = (EditText) view.findViewById(R.id.editTextDisplayName);
            editTextGender = (EditText) view.findViewById(R.id.editTextGender);
            editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
            textViewAcctId = (TextView) view.findViewById(R.id.textViewAcctId);
            buttonSave = (Button) view.findViewById(R.id.buttonSave);
            buttonCancel = (Button) view.findViewById(R.id.buttonCancel);
        }
    }

/////////////////////////////////////////////////////////////////////////////////////
////    PERSISTENCE
/////////////////////////////////////////////////////////////////////////////////////
    /* A reference to the Firebase */
    private static String FIREBASE_URL = null;
    private Firebase mFirebaseRootRef = null;
    private Firebase mFirebaseUserRef = null;
    private ValueEventListener mFirebaseUserValueListener = null;


    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FIREBASE_URL = getResources().getString(R.string.firebase_url);
        Log.i(LOG_TAG, " FIREBASE_URL " + FIREBASE_URL);
        // Setup our Firebase mFirebaseRef
        mFirebaseRootRef = new Firebase(FIREBASE_URL).getRoot();

        String userId = Utility.getLoggedInUserId(this.getContext());
        mFirebaseUserRef = new Firebase(FIREBASE_URL).child("user").child(userId);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        viewHolderRef = new ViewHolder(rootView);
//        rootView.setTag(viewHolderRef);

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
        mFirebaseUserValueListener = mFirebaseUserRef.addValueEventListener(new ValueEventListener() {
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
        mFirebaseUserRef.removeEventListener(mFirebaseUserValueListener);
    }



    private void updateView(ViewHolder viewHolder, User user) {
        viewHolder.editTextFirstName.setText(user.getFirstName());
        viewHolder.editTextLastName.setText(user.getLastName());
        viewHolder.editTextDisplayName.setText(user.getDisplayName());
        viewHolder.editTextEmail.setText(user.getEmail());
        viewHolder.editTextGender.setText(user.getGender());

        viewHolder.textViewAcctId.setText(user.getUserId());
    }

    private void doAccountSave(ViewHolder viewHolder) {
        String userId = Utility.getLoggedInUserId(this.getContext());
        Utility.setLoggedInUserDisplayName(this.getContext(), viewHolder.editTextDisplayName.getText().toString());

        User user = new User();
        user.setUserId(userId);
        user.setFirstName(viewHolder.editTextFirstName.getText().toString());
        user.setLastName(viewHolder.editTextLastName.getText().toString());
        user.setDisplayName(viewHolder.editTextDisplayName.getText().toString());
        user.setEmail(viewHolder.editTextEmail.getText().toString());
        user.setGender(viewHolder.editTextGender.getText().toString());

        mFirebaseUserRef.setValue(user);
        goBackToMain();
    }

    private void goBackToMain() {
        Intent afterAuthenticatedIntent = new Intent(this.getActivity(), MainActivity.class);
        startActivity(afterAuthenticatedIntent);
    }


}
