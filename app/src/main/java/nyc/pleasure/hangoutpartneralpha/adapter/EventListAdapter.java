package nyc.pleasure.hangoutpartneralpha.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;

import nyc.pleasure.hangoutpartneralpha.R;
import nyc.pleasure.hangoutpartneralpha.obj.FunEvent;

/**
 * Created by Chien on 12/21/2015.
 */
public class EventListAdapter  extends FirebaseListAdapter<FunEvent> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String mUsername;

    public EventListAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, FunEvent.class, layout, activity);
        this.mUsername = mUsername;
    }

    /**
     * Bind an instance of the <code>FunEvent</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>FunEvent</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param event An instance representing the current state of a event message
     */
    @Override
    protected void populateView(View view, FunEvent event) {
        // Map a Chat object to an entry in our listview
/*
        String author = event.getCreaterUserId();
        TextView authorText = (TextView) view.findViewById(R.id.event_creater);
        authorText.setText(author + ": ");
        // If the message was sent by this user, color it differently
        if (author != null && author.equals(mUsername)) {
            authorText.setTextColor(Color.RED);
        } else {
            authorText.setTextColor(Color.BLUE);
        }
        */
        ////  editTextTitle   editTextEventDate  editTextEventTime   editTextLocation   editTextEventDetail
        ((TextView) view.findViewById(R.id.textView_title)).setText(event.getTitle());
        ((TextView) view.findViewById(R.id.textView_location)).setText(event.getLocation());
        ((TextView) view.findViewById(R.id.textView_time)).setText(converTime(event.getStartTime()));

    }

    private String converTime(Long time) {
        return "Jan 25, 2016";
    }

}
