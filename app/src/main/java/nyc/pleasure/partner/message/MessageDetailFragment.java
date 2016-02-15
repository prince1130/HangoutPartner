package nyc.pleasure.partner.message;


import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

import nyc.pleasure.partner.PreferenceUtility;
import nyc.pleasure.partner.R;
import nyc.pleasure.partner.adapter.ChatListAdapter;
import nyc.pleasure.partner.firebase.FirebaseUtility;
import nyc.pleasure.partner.obj.ChatMessage;
import nyc.pleasure.partner.obj.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageDetailFragment extends Fragment {

    public static final String LOG_TAG = MessageDetailFragment.class.getSimpleName();

 /////////////////////////////////////////////////////////////////////////////////////
////    VIEW
/////////////////////////////////////////////////////////////////////////////////////
    private ViewHolder viewHolderRef;

    public static class ViewHolder {
        public final ListView mListView;
        public ViewHolder(View view) {
            mListView = (ListView) view.findViewById(R.id.list_view_message);
        }
    }

/////////////////////////////////////////////////////////////////////////////////////
////    PERSISTENCE
/////////////////////////////////////////////////////////////////////////////////////
    /* A reference to the Firebase */
    private FirebaseUtility mFirebaseUtility;
    private Firebase mFirebaseUserRef = null;
    private Firebase mFirebaseMsgRef = null; // Point to the Message Bucket that contains all messages for these 2 members.
    private ChatListAdapter mChatListAdapter;

    private String myUserId = null;
    private String theirUserId = null;
    private String msgBucketId = null;

/////////////////////////////////////////////////////////////////////////////////////
////    LIFECYCLE FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////

    public MessageDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myUserId = PreferenceUtility.getLoggedInUserId(this.getContext());
        theirUserId = PreferenceUtility.getSelectedUserId(this.getContext());
        mFirebaseUtility = FirebaseUtility.getInstance(getResources());
        mFirebaseUserRef = mFirebaseUtility.getUserReference().child(myUserId);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_message_detail, container, false);
        viewHolderRef = new ViewHolder(rootView);

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        EditText inputText = (EditText) rootView.findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        rootView.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });


        getMessageBucketId();

        return rootView;
    }


    private void getMessageBucketId() {

        mFirebaseUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                msgBucketId = null; // Make sure there is no value stored before we go retrieve it.
                Log.d(LOG_TAG, "onDataChange. Rebuild UI with latest data. " + dataSnapshot.toString());
                User currentUser = dataSnapshot.getValue(User.class);
                if (currentUser != null) { //// This user was found.
                    Map<String, String> messageMap = currentUser.getChatMessages();
                    if(messageMap != null) {
                        msgBucketId = messageMap.get(theirUserId);
                    }

                }
                if(msgBucketId == null) {
                    msgBucketId = "" + System.currentTimeMillis();
                    addFirebaseMsgBucketId(msgBucketId);
                }

                initalizeView(msgBucketId);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void initalizeView(String messageBucketId) {
        mFirebaseMsgRef = mFirebaseUtility.getMessageReference().child(messageBucketId);

        mChatListAdapter = new ChatListAdapter(mFirebaseMsgRef.limit(50), this.getActivity(), R.layout.list_item_chat, myUserId);

        viewHolderRef.mListView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                viewHolderRef.mListView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

    }


    private void addFirebaseMsgBucketId(String messageBucketId) {
        /// Insert to Both Users on sending and receiving sides..
        mFirebaseUtility.getUserReference().child(myUserId).child("chatMessages").child(theirUserId).setValue(messageBucketId);
        mFirebaseUtility.getUserReference().child(theirUserId).child("chatMessages").child(myUserId).setValue(messageBucketId);
    }

    private void sendMessage() {
        EditText inputText = (EditText) this.getActivity().findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            ChatMessage chat = new ChatMessage();
            chat.setContent(input);
            chat.setUserIdFrom(myUserId);
            chat.setUserIdTo(theirUserId);
            chat.setTime(System.currentTimeMillis());
            chat.setMsgId(chat.getTime().toString());
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseMsgRef.child(chat.getMsgId()).setValue(chat); // message -> bucketid -> msgId -> Msg
            inputText.setText("");
        }
    }

}
