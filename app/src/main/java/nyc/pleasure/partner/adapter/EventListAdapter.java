package nyc.pleasure.partner.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;

import java.util.Calendar;

import nyc.pleasure.partner.R;
import nyc.pleasure.partner.obj.FunEvent;

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
        ((TextView) view.findViewById(R.id.textView_location)).setText(event.getLocationName());
        ((TextView) view.findViewById(R.id.textView_date)).setText(getDateString(event.getStartTime()));
        ((TextView) view.findViewById(R.id.textView_event_id)).setText(event.getEventId());

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

}
