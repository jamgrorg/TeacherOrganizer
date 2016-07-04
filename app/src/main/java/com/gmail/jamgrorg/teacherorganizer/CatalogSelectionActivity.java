package com.gmail.jamgrorg.teacherorganizer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gmail.jamgrorg.teacherorganizer.databaseUtils.DatabaseHelper;

public class CatalogSelectionActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static String catalogType;
    private static SimpleCursorAdapter catalogSimpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog_selection);

        Bundle bundle = getIntent().getExtras();
        catalogType = bundle.getString("catalogType");

        String[] from = new String[0];
        switch (catalogType) {
            case "lessonType":
                setTitle(R.string.detail_lesson_type_title_text);
                from = new String[] {DatabaseHelper.TITLE_LESSON_TYPE_COLUMN};
                break;
            case "subjects":
                setTitle(R.string.detail_subjects_title_text);
                from = new String[] {DatabaseHelper.TITLE_SUBJECTS_COLUMN};
                break;
            case "groups":
                setTitle(R.string.detail_group_title_text);
                from = new String[] {DatabaseHelper.GROUP_STUDENTS_COLUMN};
                break;
            default:
                break;
        }

        int[] to = new int[] {R.id.catalog_list_item_text};

        catalogSimpleCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.catalog_list_item, null, from, to, 0);

        ListView catalogSelectionList = (ListView) findViewById(R.id.catalog_selection_list);
        try {
            catalogSelectionList.setAdapter(catalogSimpleCursorAdapter);
        } catch (NullPointerException e) {}

        catalogSelectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor;
                switch (catalogType) {
                    case "lessonType":
                        cursor = MainActivity.databaseHelper.getLessonTypeRecordById(id);
                        catalogSimpleCursorAdapter.swapCursor(cursor);
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {
                            if (cursor.getInt(0) == id) {
                                Intent intent = new Intent();
                                intent.putExtra("lessonType", cursor.getString(1));
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                            cursor.moveToNext();
                        }
                        break;
                    case "subjects":
                        cursor = MainActivity.databaseHelper.getSubjectsRecordById(id);
                        catalogSimpleCursorAdapter.swapCursor(cursor);
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {
                            if (cursor.getInt(0) == id) {
                                Intent intent = new Intent();
                                intent.putExtra("subject", cursor.getString(1));
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                            cursor.moveToNext();
                        }
                        break;
                    case "groups":
                        cursor = MainActivity.databaseHelper.getGroupsRecordsById(id);
                        catalogSimpleCursorAdapter.swapCursor(cursor);
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {
                            if (cursor.getInt(0) == id) {
                                Intent intent = new Intent();
                                intent.putExtra("group", cursor.getString(4));
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                            cursor.moveToNext();
                        }
                        break;
                }
            }
        });

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new catalogCursorLoader(getApplicationContext(), MainActivity.databaseHelper);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        catalogSimpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    static class catalogCursorLoader extends CursorLoader {

        final DatabaseHelper databaseHelper;

        public catalogCursorLoader(Context context, DatabaseHelper databaseHelper) {
            super(context);
            this.databaseHelper = databaseHelper;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = null;
            switch (catalogType) {
                case "lessonType":
                    cursor = databaseHelper.getAllLessonTypeRecords();
                    break;
                case "subjects":
                    cursor = databaseHelper.getAllSubjectsRecords();
                    break;
                case "groups":
                    cursor = databaseHelper.getAllGroupsRecords();
                    break;
            }

            return cursor;
        }
    }


}
