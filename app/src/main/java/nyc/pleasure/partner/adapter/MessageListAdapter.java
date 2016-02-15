package nyc.pleasure.partner.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.Query;

import nyc.pleasure.partner.R;
import nyc.pleasure.partner.obj.ChatMessage;

/**
 * Created by Chien on 12/22/2015.
 */
public class MessageListAdapter extends FirebaseListAdapter<String> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String mUsername;

    public MessageListAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, String.class, layout, activity);
        this.mUsername = mUsername;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mInflater.inflate(mLayout, viewGroup, false);
        }
        populateView(view, mKeys.get(i));
        return view;
    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param otherUserId An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, String otherUserId) {
        TextView textView_user_messaged = (TextView) view.findViewById(R.id.textView_user_messaged);
        textView_user_messaged.setText(otherUserId);
    }
}

