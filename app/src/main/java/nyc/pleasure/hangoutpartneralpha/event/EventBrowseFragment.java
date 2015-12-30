package nyc.pleasure.hangoutpartneralpha.event;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import nyc.pleasure.hangoutpartneralpha.R;
import nyc.pleasure.hangoutpartneralpha.data.EventContract;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventBrowseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventBrowseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventBrowseFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = EventBrowseFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EventBrowseAdapter mEventAdapter;
    private ListView mEventListView;

    private static final int EVENT_LOADER = 1;

    private static final String[] EVENT_COLUMNS = {
            EventContract.EventEntry.TABLE_NAME + "." + EventContract.EventEntry._ID,
            EventContract.EventEntry.COLUMN_EVENT_ID,
            EventContract.EventEntry.COLUMN_START_TIME,
            EventContract.EventEntry.COLUMN_LOCATION,
            EventContract.EventEntry.COLUMN_TITLE,
            EventContract.EventEntry.COLUMN_DETAIL,
            EventContract.UserEntry.COLUMN_USER_ID,
            EventContract.UserEntry.COLUMN_DISPLAY_NAME,
            EventContract.UserEntry.COLUMN_EMAIL,
            EventContract.UserEntry.COLUMN_GENDER,
            EventContract.UserEntry.COLUMN_AGE
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_EVENT_ID = 0;
    static final int COL_EVENT_ID2 = 1;
    static final int COL_EVENT_DATE = 2;
    static final int COL_EVENT_LOCATION = 3;
    static final int COL_EVENT_TITLE = 4;
    static final int COL_EVENT_DETAIL = 5;
    static final int COL_USER_ID = 6;
    static final int COL_USER_DISPLAY = 7;
    static final int COL_USER_EMAIL = 8;
    static final int COL_USER_GENDER = 9;
    static final int COL_USER_AGE = 10;

    public EventBrowseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventBrowseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventBrowseFragment newInstance(String param1, String param2) {
        EventBrowseFragment fragment = new EventBrowseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getLoaderManager().initLoader(EVENT_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_event_browse, container, false);

        mEventAdapter = new EventBrowseAdapter(getActivity(), null, 0);

        mEventListView = (ListView) rootView.findViewById(R.id.listview_event);
        mEventListView.setAdapter(mEventAdapter);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void onEventChanged() {
        updateEvent();
        getLoaderManager().restartLoader(EVENT_LOADER, null, this);
    }

    private void updateEvent() {
//        HangoutSyncAdapter.syncImmediately(getActivity());
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////    LOADER
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortOrder = EventContract.EventEntry.COLUMN_START_TIME + " ASC";
        Uri eventListUri = EventContract.EventEntry.buildEventAfterDateUri(System.currentTimeMillis());

        return new CursorLoader(getActivity(),
                eventListUri,
                EVENT_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mEventAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mEventAdapter.swapCursor(null);
    }

}
