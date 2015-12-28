package nyc.pleasure.hangoutpartneralpha.event;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import nyc.pleasure.hangoutpartneralpha.R;

/**
 * Created by Chien on 12/26/2015.
 */
public class EventBrowseAdapter extends CursorAdapter {


    /**
     * Cache of the children views for a event list item.
     */
    public static class ViewHolder {
        public final TextView titleView;
        public final TextView locationView;
        public final TextView timeView;
        public final TextView createrView;

        public ViewHolder(View view) {
            titleView = (TextView) view.findViewById(R.id.event_title);
            locationView = (TextView) view.findViewById(R.id.event_location);
            timeView = (TextView) view.findViewById(R.id.event_time);
            createrView = (TextView) view.findViewById(R.id.event_creater);
        }
    }

    public EventBrowseAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.titleView.setText(cursor.getString(EventBrowseFragment.COL_EVENT_TITLE));
        viewHolder.locationView.setText(cursor.getString(EventBrowseFragment.COL_EVENT_LOCATION));
        viewHolder.timeView.setText(cursor.getString(EventBrowseFragment.COL_EVENT_DATE));
        viewHolder.createrView.setText(cursor.getString(EventBrowseFragment.COL_USER_DISPLAY));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = R.layout.list_item_event;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

}
