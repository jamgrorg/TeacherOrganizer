package com.gmail.jamgrorg.teacherorganizer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gmail.jamgrorg.teacherorganizer.importExportUtils.ImportExportDialogActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DetailJournalActivity extends AppCompatActivity {

    private static TableLayout tableMainColumn;
    private static TableLayout tableContent;
    private static TableLayout tableRatingColumn;
    private static String group = "";
    private static String subject = "";
    private static String lessonType = "";
    private static String journalType = "";
    private static Cursor journalDatesCursor = null;
    private static Cursor journalStudentsCursor = null;
    private static String[] marks;
    public static String[][] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_journal);

        Bundle bundle = getIntent().getExtras();
        group = bundle.getString("group");
        subject = bundle.getString("subject");
        lessonType = bundle.getString("lesson_type");
        journalType = bundle.getString("journalType");

        ScrollView scrollView = (ScrollView) findViewById(R.id.journalScrollView);
        try {
            scrollView.setHorizontalScrollBarEnabled(false);
        } catch (NullPointerException e) {}
        scrollView.setVerticalScrollBarEnabled(false);

        journalDatesCursor = MainActivity.databaseHelper.getJournalDatesRecords(lessonType, group, subject);
        String[] dates = new String[(Integer) journalDatesCursor.getCount()];
        int j = 0;
        while (journalDatesCursor.moveToNext()) {
            dates[j] = journalDatesCursor.getString(1);
            j++;
        }

        journalStudentsCursor = MainActivity.databaseHelper.getStudentsRecordsByGroup(group);
        String[] students = new String[(Integer) journalStudentsCursor.getCount()];
        int i = 0;
        while (journalStudentsCursor.moveToNext()) {
            students[i] = journalStudentsCursor.getString(1)
                    + " " + journalStudentsCursor.getString(2)
                    + " " + journalStudentsCursor.getString(3);
            i++;
        }
        if (journalType.equals("all")) {
            initializeTables(journalStudentsCursor.getCount(), journalDatesCursor.getCount(), students, dates);
        } else {
            initializeTables(journalStudentsCursor.getCount(), 1, students,
                    new String[] {String.valueOf(new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime()))});
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (journalType.equals("all")) {
                    getTablesData(journalStudentsCursor.getCount(), journalDatesCursor.getCount());
                } else {
                    getTablesData(journalStudentsCursor.getCount(), 1);
                }
            }
        });

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {}
    }

    private void initializeTables(final int rowCount, int collCount, final String[] students, String[] dates) {
        tableMainColumn = (TableLayout) findViewById(R.id.tableMainColumn);
        tableContent = (TableLayout) findViewById(R.id.tableContent);

        for (int i = 0 ; i < rowCount + 1; i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tr.setMinimumHeight(60);
            if (i % 2 == 0) {
                tr.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            } else {
                tr.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            tr.setGravity(Gravity.CENTER);

            final TextView tv = new TextView(this);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setTextSize(12);
            if (i == 0) {
                tv.setText(R.string.journal_students_dates_column_title);
            } else {
                tv.setText(students[i - 1]);
            }
            tv.setTextColor(Color.WHITE);
            tv.setPadding(20,2,20,2);

            tr.addView(tv);
            tableMainColumn.addView(tr);
        }

        for (int i = 0; i < rowCount + 1; i++) {
            final TableRow tr = new TableRow(this);
            tr.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tr.setMinimumHeight(60);
            tr.setGravity(Gravity.CENTER);

            if (i == 0) {
                tr.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }

            for (int j = 0; j < collCount; j++) {
                final TextView tv = new TextView(this);
                tv.setMinimumHeight(60);
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(12);
                if (i == 0) {
                    tv.setTextColor(Color.WHITE);
                    tv.setText(dates[j]);
                } else {
                    if ((i + j) % 2 != 0) {
                        tv.setBackgroundColor(getResources().getColor(R.color.md_blue_grey_100));
                    }
                    tv.setTextColor(Color.BLACK);
                    Cursor journalMarksCursor = MainActivity.databaseHelper.getJournalMarksRecords(
                            dates[j], lessonType, group, subject, students[i - 1]);
                    while (journalMarksCursor.moveToNext()) {
                        tv.setText(journalMarksCursor.getString(6));
                    }
                    if (journalType.equals("new")) {
                        final int finalI = i;
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final NumberPicker numberPicker = new NumberPicker(v.getContext());
                                marks = getResources().getStringArray(R.array.marks);
                                numberPicker.setMinValue(0);
                                numberPicker.setMaxValue(marks.length - 1);
                                numberPicker.setDisplayedValues(marks);

                                new AlertDialog.Builder(v.getContext()).setTitle(R.string.journal_select_mark_dialog_title).
                                        setView(numberPicker).setPositiveButton(android.R.string.yes,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (tv.getText().toString().trim().length() == 0) {
                                                    MainActivity.databaseHelper.insertJournalRecord(
                                                            String.valueOf(new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime())),
                                                            lessonType, group, subject, students[finalI - 1], marks[numberPicker.getValue()]);
                                                } else {
                                                    MainActivity.databaseHelper.updateJournalRecord(
                                                            String.valueOf(new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime())),
                                                            lessonType, group, subject, students[finalI - 1], marks[numberPicker.getValue()]);
                                                }
                                                tv.setText(marks[numberPicker.getValue()]);

                                                initializeRatingTable(rowCount, students);
                                            }
                                        }).create().show();
                            }
                        });

                        tv.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

                                MainActivity.databaseHelper.deleteJournalRecord(
                                        String.valueOf(new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime())),
                                        lessonType, group, subject, students[finalI - 1]);
                                tv.setText("");

                                initializeRatingTable(rowCount, students);

                                return true;
                            }
                        });
                    }
                }
                tv.setPadding(20,2,20,2);

                tr.addView(tv);
            }
            tableContent.addView(tr);
        }

        initializeRatingTable(rowCount, students);
    }

    private void initializeRatingTable(int rowCount, final String[] students) {
        tableRatingColumn = (TableLayout) findViewById(R.id.tableRatingColumn);

        try {
            tableRatingColumn.removeAllViews();
        } catch (NullPointerException e) {}

        for (int i = 0 ; i < rowCount + 1; i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tr.setMinimumHeight(60);
            tr.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            tr.setGravity(Gravity.CENTER);

            final TextView tv = new TextView(this);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(12);
            if (i == 0) {
                tv.setText(R.string.journal_student_rating_column_title);
            } else {
                int tvMaxLenght = 4;
                tv.setFilters(new InputFilter[] {new InputFilter.LengthFilter(tvMaxLenght)});
                tv.setText(getStudentRating(group, subject, students[i - 1]));
            }
            tv.setTextColor(Color.WHITE);
            tv.setPadding(20,2,20,2);

            tr.addView(tv);
            tableRatingColumn.addView(tr);
        }
    }

    private String getStudentRating(String group,
                                    String subject, String student) {
        Cursor cursor = MainActivity.databaseHelper.getJournalStudentMarks(
                group, subject, student);
        double studentRating = 0;
        while (cursor.moveToNext()) {
            if ((!cursor.getString(0).equals("-") && (cursor.getString(0).length() != 0))) {
                studentRating += Double.parseDouble(cursor.getString(0));
            }
        }

        double sRating;
        if (studentRating != 0) {
            sRating = studentRating / cursor.getCount();
        } else {
            sRating = 0;
        }

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        double studentRatingCoefficient = Double.parseDouble(settings.getString("rating", "0.5"));

        return String.valueOf(sRating * studentRatingCoefficient);
    }

    private void getTablesData(int rowCount, int collCount) {
        data = new String[collCount + 2][rowCount + 1];


        for (int i = 0; i < rowCount + 1; i++) {
            TableRow tableRow = (TableRow) tableMainColumn.getChildAt(i);
            TextView textView = (TextView) tableRow.getChildAt(0);
            data[0][i] = textView.getText().toString().trim();
        }

        for (int i = 0; i < rowCount + 1; i++) {
            for (int j = 1; j < collCount + 1; j++) {
                TableRow tableRow = (TableRow) tableContent.getChildAt(i);
                TextView textView = (TextView) tableRow.getChildAt(j - 1);
                data[j][i] = textView.getText().toString().trim();
            }
        }

        for (int i = 0; i < rowCount + 1; i++) {
            TableRow tableRow = (TableRow) tableRatingColumn.getChildAt(i);
            TextView textView = (TextView) tableRow.getChildAt(0);
            data[collCount + 1][i] = textView.getText().toString().trim();
        }

        Intent intent = new Intent(getApplicationContext(), ImportExportDialogActivity.class);
        intent.putExtra("importExportType", "export");
        intent.putExtra("isJournal", true);
        intent.putExtra("rowCount", rowCount + 1);
        intent.putExtra("collCount", collCount + 2);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItemSettings = menu.findItem(R.id.action_settings);
        menuItemSettings.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
