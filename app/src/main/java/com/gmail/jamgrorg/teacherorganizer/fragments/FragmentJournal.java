package com.gmail.jamgrorg.teacherorganizer.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gmail.jamgrorg.teacherorganizer.CatalogSelectionActivity;
import com.gmail.jamgrorg.teacherorganizer.DetailJournalActivity;
import com.gmail.jamgrorg.teacherorganizer.MainActivity;
import com.gmail.jamgrorg.teacherorganizer.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentJournal.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentJournal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentJournal extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static String subject = "";
    private static String group = "";
    private static String lessonType = "";
    private static EditText journalSubjectField;
    private static EditText journalLessonTypeField;
    private static EditText journalGroupField;

    private OnFragmentInteractionListener mListener;

    public FragmentJournal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentJournal.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentJournal newInstance(String param1, String param2) {
        FragmentJournal fragment = new FragmentJournal();
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_journal, container, false);

        MainActivity.currFragment = "journalFragment";

        getActivity().invalidateOptionsMenu();

        journalSubjectField = (EditText) view.findViewById(R.id.journal_subject_field);
        journalSubjectField.setKeyListener(null);
        journalSubjectField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CatalogSelectionActivity.class);
                intent.putExtra("catalogType", "subjects");
                startActivityForResult(intent, 4);
            }
        });

        journalLessonTypeField = (EditText) view.findViewById(R.id.journal_lesson_type_field);
        journalLessonTypeField.setKeyListener(null);
        journalLessonTypeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CatalogSelectionActivity.class);
                intent.putExtra("catalogType", "lessonType");
                startActivityForResult(intent, 5);
            }
        });

        journalGroupField = (EditText) view.findViewById(R.id.journal_group_field);
        journalGroupField.setKeyListener(null);
        journalGroupField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CatalogSelectionActivity.class);
                intent.putExtra("catalogType", "groups");
                startActivityForResult(intent, 6);
            }
        });

        disableFABAdd();
        disableFABShowWholeJournal();

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

    private void checkFields() {
        if ((journalSubjectField.getText().toString().trim().length() == 0) ||
                (journalLessonTypeField.getText().toString().trim().length() == 0) ||
                (journalGroupField.getText().toString().trim().length() == 0)) {
            disableFABAdd();
            disableFABShowWholeJournal();
        } else {
            enableFABAdd();
            Cursor cursor = MainActivity.databaseHelper.getJournalDatesRecords(
                    journalLessonTypeField.getText().toString().trim(),
                    journalGroupField.getText().toString().trim(),
                    journalSubjectField.getText().toString().trim());
            if (cursor.getCount() > 0) {
                enableFABShowWholeJournal();
            } else {
                disableFABShowWholeJournal();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void enableFABAdd() {
        MainActivity.fabAdd.setBackgroundTintList(
                ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        MainActivity.fabAdd.setEnabled(true);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void enableFABShowWholeJournal() {
        MainActivity.fabShowWholeJournal.setBackgroundTintList(
                ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
        MainActivity.fabShowWholeJournal.setEnabled(true);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void disableFABAdd() {
        MainActivity.fabAdd.setBackgroundTintList(
                ColorStateList.valueOf(getResources().getColor(R.color.mdtp_dark_gray)));
        MainActivity.fabAdd.setEnabled(false);
        MainActivity.fabShowWholeJournal.setBackgroundTintList(
                ColorStateList.valueOf(getResources().getColor(R.color.mdtp_dark_gray)));
        MainActivity.fabShowWholeJournal.setEnabled(false);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void disableFABShowWholeJournal() {
        MainActivity.fabShowWholeJournal.setBackgroundTintList(
                ColorStateList.valueOf(getResources().getColor(R.color.mdtp_dark_gray)));
        MainActivity.fabShowWholeJournal.setEnabled(false);
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

        MainActivity.fabAdd.setImageResource(R.drawable.ic_playlist_plus_white_48dp);
        MainActivity.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                subject = journalSubjectField.getText().toString().trim();
                group = journalGroupField.getText().toString().trim();
                lessonType = journalLessonTypeField.getText().toString().trim();

                Intent intent = new Intent(getContext(), DetailJournalActivity.class);
                intent.putExtra("group", group);
                intent.putExtra("subject", subject);
                intent.putExtra("lesson_type", lessonType);
                intent.putExtra("journalType", "new");
                startActivity(intent);
            }
        });


        if (!MainActivity.fabShowWholeJournal.isShown()) {
            MainActivity.fabShowWholeJournal.show();
        }
        MainActivity.fabShowWholeJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                subject = journalSubjectField.getText().toString().trim();
                group = journalGroupField.getText().toString().trim();
                lessonType = journalLessonTypeField.getText().toString().trim();

                Intent intent = new Intent(getContext(), DetailJournalActivity.class);
                intent.putExtra("group", group);
                intent.putExtra("subject", subject);
                intent.putExtra("lesson_type", lessonType);
                intent.putExtra("journalType", "all");
                Cursor cursor = MainActivity.databaseHelper.getJournalDatesRecords(lessonType, group, subject);
                if (cursor.getCount() <= 0) {

                    Snackbar snackbar = Snackbar.make(MainActivity.fabAdd,
                            R.string.no_journal_popup,
                            Snackbar.LENGTH_SHORT);
                    View view1 = snackbar.getView();
                    view1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    snackbar.show();
                } else {
                    startActivity(intent);
                }

            }
        });

        checkFields();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (MainActivity.fabShowWholeJournal.isShown()) {
            MainActivity.fabShowWholeJournal.hide();
        }

        enableFABAdd();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        } else {
            switch (requestCode) {
                case 4:
                    journalSubjectField.setText(data.getStringExtra("subject"));
                    break;
                case 5:
                    journalLessonTypeField.setText(data.getStringExtra("lessonType"));
                    break;
                case 6:
                    journalGroupField.setText(data.getStringExtra("group"));
                default:
                    break;
            }
        }
    }
}
