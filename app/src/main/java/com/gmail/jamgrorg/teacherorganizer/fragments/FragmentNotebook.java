package com.gmail.jamgrorg.teacherorganizer.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import com.gmail.jamgrorg.teacherorganizer.DetailNoteDialogActivity;
import com.gmail.jamgrorg.teacherorganizer.MainActivity;
import com.gmail.jamgrorg.teacherorganizer.R;
import com.gmail.jamgrorg.teacherorganizer.databaseUtils.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentNotebook.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentNotebook#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNotebook extends Fragment implements
        android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    private static SimpleCursorAdapter notesSimpleCursorAdapter;
    private static ListView notesListView;
    public static boolean isNoteSaved = false;

    private static final int CM_DELETE_ID = 1;

    public FragmentNotebook() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentNotebook.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentNotebook newInstance(String param1, String param2) {
        FragmentNotebook fragment = new FragmentNotebook();
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
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_notebook, container, false);

        View view = inflater.inflate(R.layout.fragment_notebook, container, false);

        MainActivity.currFragment = "notebookFragment";

        getActivity().invalidateOptionsMenu();

        String[] from = new String[]{DatabaseHelper.TITLE_NOTE_COLUMN,
                DatabaseHelper.BODY_NOTE_COLUMN, DatabaseHelper.IS_NOTIFICATION_NOTE_COLUMN,
                DatabaseHelper.ALARM_DATE_NOTE_COLUMN, DatabaseHelper.ALARM_TIME_NOTE_COLUMN,
                DatabaseHelper.ALARM_TYPE_NOTE_COLUMN};
        int[] to = new int[]{R.id.note_item_title, R.id.note_item_body};  // Поправить!!!!!!!!

        notesSimpleCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.note_list_item, null, from, to, 0);
        notesListView = (ListView) view.findViewById(R.id.notes_list);
        notesListView.setAdapter(notesSimpleCursorAdapter);
        notesListView.setTextFilterEnabled(true);

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetailNoteDialogActivity.class);
                intent.putExtra("newRecord", false);
                Cursor cursor = MainActivity.databaseHelper.getAllNoteRecords();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    if (cursor.getInt(0) == id) {
                        intent.putExtra("id", id);
                        intent.putExtra("title", cursor.getString(1));
                        intent.putExtra("body", cursor.getString(2));
                        intent.putExtra("isNotification", cursor.getInt(3));
                        intent.putExtra("alarmDate", cursor.getString(4));
                        intent.putExtra("alarmTime", cursor.getString(5));
                        intent.putExtra("alarmType", cursor.getInt(6));
                    }
                    cursor.moveToNext();
                }

                startActivity(intent);
            }
        });

        registerForContextMenu(notesListView);

        getLoaderManager().initLoader(0, null, this);

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

    public void searchNoteRecord(String searchText) {
        Cursor cursor =
                MainActivity.databaseHelper.getNotesRecordByText(searchText);

        notesSimpleCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new NotesCursorLoader(getActivity(), MainActivity.databaseHelper);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        notesSimpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    static class NotesCursorLoader extends CursorLoader {

        final DatabaseHelper databaseHelper;

        public NotesCursorLoader(Context context, DatabaseHelper databaseHelper) {
            super(context);
            this.databaseHelper = databaseHelper;
        }

        @Override
        public Cursor loadInBackground() {

            return databaseHelper.getAllNoteRecords();
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
                Intent intent = new Intent(getContext(), DetailNoteDialogActivity.class);
                intent.putExtra("newRecord", true);
                startActivity(intent);
            }
        });

        searchNoteRecord(MainActivity.searchView.getQuery().toString().trim());
        if (isNoteSaved) {

            Snackbar snackbar = Snackbar.make(getView(),
                    R.string.record_save_confirmation_popup,
                    Snackbar.LENGTH_SHORT);
            View view = snackbar.getView();
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            snackbar.show();

            isNoteSaved = false;
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
            MainActivity.databaseHelper.deleteNoteRecord(adapterContextMenuInfo.id);
            //DetailNoteDialogActivity.notificationActivity.cancelNotification(getContext()); // Отменяем уведомление

            Snackbar snackbar = Snackbar.make(getView(),
                    R.string.record_delete_confirmation_popup,
                    Snackbar.LENGTH_SHORT);
            View view = snackbar.getView();
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            snackbar.show();

            searchNoteRecord(MainActivity.searchView.getQuery().toString().trim());

            return true;
        }

        return super.onContextItemSelected(item);
    }
}
