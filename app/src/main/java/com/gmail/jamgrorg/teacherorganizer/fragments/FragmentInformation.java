package com.gmail.jamgrorg.teacherorganizer.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.gmail.jamgrorg.teacherorganizer.MainActivity;
import com.gmail.jamgrorg.teacherorganizer.R;
import com.gmail.jamgrorg.teacherorganizer.databaseUtils.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentInformation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentInformation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInformation extends Fragment implements
        android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    private static SimpleCursorAdapter infoNotesSimpleCursorAdapter;
    private static SimpleCursorAdapter infoTTOCSimpleCursorAdapter;
    private static ListView infoNotesList;
    private static ListView infoTTOCList;
    private static TextView infoNotesTitle;
    private static TextView infoTTOCTitle;

    public FragmentInformation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentInformation.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentInformation newInstance(String param1, String param2) {
        FragmentInformation fragment = new FragmentInformation();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_information, container, false);

        MainActivity.currFragment = "informationFragment";

        getActivity().invalidateOptionsMenu();

        infoNotesTitle = (TextView) view.findViewById(R.id.infoNotesTitle);
        infoTTOCTitle = (TextView) view.findViewById(R.id.infoTTOCTitle);

        String[] noteFrom = new String[]{DatabaseHelper.TITLE_NOTE_COLUMN,
                DatabaseHelper.BODY_NOTE_COLUMN};
        int[] noteTo = new int[]{R.id.note_item_title, R.id.note_item_body};

        infoNotesSimpleCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.note_list_item, null, noteFrom, noteTo, 0);
        infoNotesList = (ListView) view.findViewById(R.id.infoNotesList);
        infoNotesList.setAdapter(infoNotesSimpleCursorAdapter);

        getLoaderManager().initLoader(0, null, this);

        String[] ttocFrom = new String[]{DatabaseHelper.NUM_TTOC_COLUMN, DatabaseHelper.DATE_TTOC_COLUMN,
                DatabaseHelper.START_TIME_TTOC_COLUMN, DatabaseHelper.END_TIME_TTOC_COLUMN,
                DatabaseHelper.TYPE_TTOC_COLUMN, DatabaseHelper.SUBJECT_TTOC_COLUMN,
                DatabaseHelper.GROUP_TTOC_COLUMN, DatabaseHelper.LECTURE_HALL_TTOC_COLUMN};
        int[] ttocTo = new int[]{R.id.ttoc_num_field, R.id.ttoc_date_field, R.id.ttoc_start_time_field,
                R.id.ttoc_end_time_field, R.id.ttoc_type_field, R.id.ttoc_subject_field,
                R.id.ttoc_group_field, R.id.ttoc_class_field};

        infoTTOCSimpleCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.ttoc_list_item, null, ttocFrom, ttocTo, 0);
        infoTTOCList = (ListView) view.findViewById(R.id.infoTTOCList);
        infoTTOCList.setAdapter(infoTTOCSimpleCursorAdapter);

        getLoaderManager().initLoader(1, null, this);

        return view;
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
        switch (id) {
            case 0:
                return new infoNoteCursorLoader(getActivity(), MainActivity.databaseHelper);
            case 1:
                return new infoTTOCCursorLoader(getActivity(), MainActivity.databaseHelper);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case 0:
                infoNotesSimpleCursorAdapter.swapCursor(data);
                break;
            case 1:
                infoTTOCSimpleCursorAdapter.swapCursor(data);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    static class infoNoteCursorLoader extends CursorLoader {

        final DatabaseHelper databaseHelper;

        public infoNoteCursorLoader(Context context, DatabaseHelper databaseHelper) {
            super(context);
            this.databaseHelper = databaseHelper;
        }

        @Override
        public Cursor loadInBackground() {

            return databaseHelper.getLastThreeNote();
        }
    }

    static class infoTTOCCursorLoader extends CursorLoader {

        final DatabaseHelper databaseHelper;

        public infoTTOCCursorLoader(Context context, DatabaseHelper databaseHelper) {
            super(context);
            this.databaseHelper = databaseHelper;
        }

        @Override
        public Cursor loadInBackground() {

            Calendar infoCalendar = Calendar.getInstance();
            SimpleDateFormat infoSimpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

            return databaseHelper.getTTOCRecordByDate(infoSimpleDateFormat.format(infoCalendar.getTime()));
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

        if (MainActivity.fabAdd.isShown()) {
            MainActivity.fabAdd.hide();
        }
        if (MainActivity.fabShowWholeJournal.isShown()) {
            MainActivity.fabShowWholeJournal.hide();
        }

        getLoaderManager().getLoader(0).forceLoad();
        getLoaderManager().getLoader(1).forceLoad();

        Cursor infoNoteCOrsor = MainActivity.databaseHelper.getLastThreeNote();
        infoNotesSimpleCursorAdapter.swapCursor(infoNoteCOrsor);
        Cursor infoTTOCCursore = MainActivity.databaseHelper.getTTOCRecordByDate(
                new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime()));
        infoTTOCSimpleCursorAdapter.swapCursor(infoTTOCCursore);

        if ((infoNotesSimpleCursorAdapter.getCount() <= 0) &&
                (infoTTOCSimpleCursorAdapter.getCount() <= 0)) {
            infoNotesTitle.setText(R.string.infoNotesTTOCEmptyText);
            infoNotesTitle.setGravity(Gravity.CENTER);
            infoNotesTitle.setVisibility(View.VISIBLE);
            infoTTOCTitle.setVisibility(View.GONE);
        } else if ((infoNotesSimpleCursorAdapter.getCount() <= 0) &&
                (infoTTOCSimpleCursorAdapter.getCount() > 0)) {
            infoNotesTitle.setText(R.string.infoNotesEmptyText);
            infoNotesTitle.setGravity(Gravity.CENTER);
            infoNotesTitle.setVisibility(View.VISIBLE);
            infoTTOCTitle.setText(R.string.infoTTOCTitle);
            infoTTOCTitle.setGravity(Gravity.START);
            infoTTOCTitle.setVisibility(View.VISIBLE);
        } else if ((infoNotesSimpleCursorAdapter.getCount() > 0) &&
                (infoTTOCSimpleCursorAdapter.getCount() <= 0)) {
            infoTTOCTitle.setText(R.string.infoTTOCEmptyText);
            infoTTOCTitle.setGravity(Gravity.CENTER);
            infoTTOCTitle.setVisibility(View.VISIBLE);
            infoNotesTitle.setText(R.string.infoNotesTitle);
            infoNotesTitle.setGravity(Gravity.START);
            infoNotesTitle.setVisibility(View.VISIBLE);
        }
    }
}
