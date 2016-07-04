package com.gmail.jamgrorg.teacherorganizer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gmail.jamgrorg.teacherorganizer.databaseUtils.DatabaseHelper;

public class CatalogActivity extends AppCompatActivity implements
        android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CM_DELETE_ID = 1;
    private static SimpleCursorAdapter catalogSimpleCursorAdapter;
    public static boolean catalogRecordSaved = false;
    private static String catalogType;
    private static String[] from = new String[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        try {
            catalogType = bundle.getString("catalogType");
        } catch (Exception e) {}

        ListView catalogList = (ListView) findViewById(R.id.catalog_list);
        registerForContextMenu(catalogList);

        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (catalogType.equals("groups")) {
                    intent = new Intent(getApplicationContext(), DetailCatalogDialogActivity.class);
                    catalogType = "students";
                } else {
                    intent = new Intent(getApplicationContext(), DetailCatalogDialogActivity.class);
                }

                intent.putExtra("newRecord", true);
                intent.putExtra("catalogType", catalogType);
                startActivity(intent);
            }
        });


        switch (catalogType) {
            case "lessonType":
                setTitle(R.string.title_activity_lesson_type_catalog);
                from = new String[]{DatabaseHelper.TITLE_LESSON_TYPE_COLUMN};
                break;
            case "subjects":
                setTitle(R.string.title_activity_subjects_catalog);
                from = new String[]{DatabaseHelper.TITLE_SUBJECTS_COLUMN};
                break;
            case "groups":
                setTitle(R.string.title_activity_groups_catalog);
                from = new String[]{DatabaseHelper.GROUP_STUDENTS_COLUMN};
                break;
            default:
                break;
        }


        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {}

        int[] to = new int[] {R.id.catalog_list_item_text};

        catalogSimpleCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.catalog_list_item, null, from, to, 0);

        catalogList.setAdapter(catalogSimpleCursorAdapter);

        catalogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetailCatalogDialogActivity.class);
                Cursor cursor = null;
                switch (catalogType) {
                    case "lessonType":
                        cursor = MainActivity.databaseHelper.getAllLessonTypeRecords();
                        break;
                    case "subjects":
                        cursor = MainActivity.databaseHelper.getAllSubjectsRecords();
                        break;
                    case "groups":
                        intent = new Intent(getApplicationContext(), CatalogStudentsActivity.class);
                        catalogType = "students";
                        cursor = MainActivity.databaseHelper.getAllGroupsRecords();
                        break;
                }
                intent.putExtra("catalogType", catalogType);
                intent.putExtra("newRecord", false);
                try {
                    cursor.moveToFirst();
                } catch (NullPointerException e) {}
                while (!cursor.isAfterLast()) {
                    if (cursor.getInt(0) == id) {
                        intent.putExtra("id", id);
                        if (catalogType.equals("students")) {
                            intent.putExtra("group", cursor.getString(4));
                        } else {
                            intent.putExtra("title", cursor.getString(1));
                        }
                    }
                    cursor.moveToNext();
                }
                startActivity(intent);

            }
        });

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (catalogRecordSaved) {

            Snackbar snackbar = Snackbar.make(findViewById(R.id.catalog_view),
                    R.string.record_save_confirmation_popup,
                    Snackbar.LENGTH_SHORT);
            View view = snackbar.getView();
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            snackbar.show();

            catalogRecordSaved = false;
        }

        if (catalogType.equals("students")) {
            catalogType = "groups";
        }

        getSupportLoaderManager().getLoader(0).forceLoad();

    }

    @Override
    protected void onStop() {
        super.onStop();
        switch (catalogType) {
            case "lessonType":

                break;
            case "subjects":

                break;

            case "groups":
                catalogType = "groups";
                break;
            default:
                break;
        }
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new lessonTypeCursorLoader(getApplicationContext(), MainActivity.databaseHelper);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        try {
            catalogSimpleCursorAdapter.swapCursor(data);
        } catch (Exception e) {}
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    static class lessonTypeCursorLoader extends CursorLoader {

        final DatabaseHelper databaseHelper;

        public lessonTypeCursorLoader(Context context, DatabaseHelper databaseHelper) {
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
                    cursor = MainActivity.databaseHelper.getAllGroupsRecords();
                    break;
            }

            return cursor;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 1, R.string.cm_delete_button);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            switch (catalogType) {
                case "lessonType": {
                    MainActivity.databaseHelper.deleteLessonTypeRecord(adapterContextMenuInfo.id);

                    Snackbar snackbar = Snackbar.make(findViewById(R.id.catalog_view),
                            R.string.record_delete_confirmation_popup,
                            Snackbar.LENGTH_SHORT);
                    View view = snackbar.getView();
                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    snackbar.show();

                    break;
                }
                case "subjects": {
                    MainActivity.databaseHelper.deleteSubjectsRecord(adapterContextMenuInfo.id);

                    Snackbar snackbar = Snackbar.make(findViewById(R.id.catalog_view),
                            R.string.record_delete_confirmation_popup,
                            Snackbar.LENGTH_SHORT);
                    View view = snackbar.getView();
                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    snackbar.show();

                    break;
                }
                case "groups":
                    final Cursor cursor = MainActivity.databaseHelper.getGroupsRecordsById(adapterContextMenuInfo.id);
                    catalogSimpleCursorAdapter.swapCursor(cursor);
                    new AlertDialog.Builder(this).
                            setTitle(getResources().getString(R.string.group_delete_confirmation_title) + " " + cursor.getString(4) + "?").
                            setMessage(getResources().getString(R.string.group_delete_confirmation_text)).
                            setIcon(R.drawable.ic_alert_circle_outline_black_48dp).
                            setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.databaseHelper.deleteStudentsByGroup(cursor.getString(4));

                                    Snackbar snackbar = Snackbar.make(findViewById(R.id.catalog_view),
                                            R.string.record_delete_confirmation_popup,
                                            Snackbar.LENGTH_SHORT);
                                    View view = snackbar.getView();
                                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                                    snackbar.show();

                                    getSupportLoaderManager().getLoader(0).forceLoad();
                                }
                            }).setNegativeButton(android.R.string.no, null).show();
                    break;
            }

            getSupportLoaderManager().getLoader(0).forceLoad();

            return true;
        }

        return super.onContextItemSelected(item);
    }
}
