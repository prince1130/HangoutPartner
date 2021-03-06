package nyc.pleasure.partner.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;

import nyc.pleasure.partner.R;
import nyc.pleasure.partner.obj.ChatMessage;

/**
 * Created by Chien on 12/22/2015.
 */
public class ChatListAdapter extends FirebaseListAdapter<ChatMessage> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String mUsername;

    public ChatListAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, ChatMessage.class, layout, activity);
        this.mUsername = mUsername;
    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param chatMsg An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, ChatMessage chatMsg) {
        // Map a Chat object to an entry in our listview
        String author = chatMsg.getUserIdFrom();
        TextView authorText = (TextView) view.findViewById(R.id.author_chat);
        authorText.setText(author + ": ");
        // If the message was sent by this user, color it differently
        if (author != null && author.equals(mUsername)) {
            authorText.setTextColor(Color.RED);
        } else {
            authorText.setTextColor(Color.BLUE);
        }
        ((TextView) view.findViewById(R.id.content_chat)).setText(chatMsg.getContent());
    }
}

