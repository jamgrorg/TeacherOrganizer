package com.gmail.jamgrorg.teacherorganizer.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gmail.jamgrorg.teacherorganizer.DetailTTOCDialogActivity;
import com.gmail.jamgrorg.teacherorganizer.MainActivity;
import com.gmail.jamgrorg.teacherorganizer.R;
import com.gmail.jamgrorg.teacherorganizer.databaseUtils.DatabaseHelper;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentTimetable_of_classes.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentTimetable_of_classes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTimetable_of_classes extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    private static SimpleCursorAdapter ttocSimpleCursorAdapter;
    private static ListView ttocListView;
    public static boolean isTTOCSaved = false;

    private static int recordCount;

    private static final int CM_DELETE_ID = 1;

    public FragmentTimetable_of_classes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTimetable_of_classes.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTimetable_of_classes newInstance(String param1, String param2) {
        FragmentTimetable_of_classes fragment = new FragmentTimetable_of_classes();
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
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timetable_of_classes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity.currFragment = "ttocFragment";

        getActivity().invalidateOptionsMenu();

        MainActivity.ttocCalendarWidget.setVisibility(View.VISIBLE);

        MainActivity.ttocCalendarWidget.setCurrentDate(Calendar.getInstance().getTime());
        MainActivity.ttocCalendarWidget.setSelectedDate(Calendar.getInstance().getTime());

        MainActivity.ttocCalendarWidget.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull final CalendarDay date, boolean selected) {
                getLoaderManager().getLoader(0).forceLoad();
            }
        });

        String[] from = new String[]{DatabaseHelper.NUM_TTOC_COLUMN, DatabaseHelper.DATE_TTOC_COLUMN,
                DatabaseHelper.START_TIME_TTOC_COLUMN, DatabaseHelper.END_TIME_TTOC_COLUMN,
                DatabaseHelper.TYPE_TTOC_COLUMN, DatabaseHelper.SUBJECT_TTOC_COLUMN,
                DatabaseHelper.GROUP_TTOC_COLUMN, DatabaseHelper.LECTURE_HALL_TTOC_COLUMN};
        int[] to = new int[]{R.id.ttoc_num_field, R.id.ttoc_date_field, R.id.ttoc_start_time_field,
                R.id.ttoc_end_time_field, R.id.ttoc_type_field, R.id.ttoc_subject_field,
                R.id.ttoc_group_field, R.id.ttoc_class_field};

        ttocSimpleCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.ttoc_list_item, null, from, to, 0);
        ttocListView = (ListView) view.findViewById(R.id.ttoc_list);
        ttocListView.setAdapter(ttocSimpleCursorAdapter);

        ttocListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetailTTOCDialogActivity.class);
                intent.putExtra("newRecord", false);
                Cursor cursor = MainActivity.databaseHelper.getAllTTOCRecords();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    if (cursor.getInt(0) == id) {
                        intent.putExtra("id", id);
                        intent.putExtra("date", cursor.getString(1));
                        intent.putExtra("num", cursor.getInt(2));
                        intent.putExtra("startTime", cursor.getString(3));
                        intent.putExtra("endTime", cursor.getString(4));
                        intent.putExtra("type", cursor.getString(5));
                        intent.putExtra("subject", cursor.getString(6));
                        intent.putExtra("group", cursor.getString(7));
                        intent.putExtra("lesson_hall", cursor.getString(8));
                    }
                    cursor.moveToNext();
                }

                startActivity(intent);
            }
        });

        registerForContextMenu(ttocListView);
        getLoaderManager().initLoader(0, null, this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new TTOCCursorLoader(getActivity(), MainActivity.databaseHelper);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        ttocSimpleCursorAdapter.swapCursor(data);

        recordCount = data.getCount();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    static class TTOCCursorLoader extends CursorLoader {

        final DatabaseHelper databaseHelper;

        public TTOCCursorLoader(Context context, DatabaseHelper databaseHelper) {
            super(context);
            this.databaseHelper = databaseHelper;
        }

        @Override
        public Cursor loadInBackground() {

            return databaseHelper.getTTOCRecordByDate(
                    new SimpleDateFormat("dd.MM.yyyy").format(MainActivity.ttocCalendarWidget.getSelectedDate().getDate())
            );
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.fabAdd.setImageResource(R.drawable.ic_plus_circle_outline_white_48dp);
        if (!MainActivity.fabAdd.isShown()) {
            MainActivity.fabAdd.show();
        }
        MainActivity.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetailTTOCDialogActivity.class);
                intent.putExtra("newRecord", true);
                intent.putExtra("date",
                        new SimpleDateFormat("dd.MM.yyyy").format(
                                MainActivity.ttocCalendarWidget.getSelectedDate().
                                        getDate()));
                intent.putExtra("recordCount", FragmentTimetable_of_classes.recordCount);
                startActivity(intent);
            }
        });

        getLoaderManager().getLoader(0).forceLoad();
        if (isTTOCSaved) {

            Snackbar snackbar = Snackbar.make(getView(),
                    R.string.record_save_confirmation_popup,
                    Snackbar.LENGTH_SHORT);
            View view = snackbar.getView();
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            snackbar.show();

            isTTOCSaved = false;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, R.string.cm_delete_button);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            MainActivity.databaseHelper.deleteTTOCRecord(adapterContextMenuInfo.id);

            Snackbar snackbar = Snackbar.make(getView(),
                    R.string.record_delete_confirmation_popup,
                    Snackbar.LENGTH_SHORT);
            View view = snackbar.getView();
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            snackbar.show();

            getLoaderManager().getLoader(0).forceLoad();
            return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.ttocCalendarWidget.setVisibility(View.GONE);
    }
}
