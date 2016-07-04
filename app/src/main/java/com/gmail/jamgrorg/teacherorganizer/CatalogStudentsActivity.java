package com.gmail.jamgrorg.teacherorganizer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gmail.jamgrorg.teacherorganizer.databaseUtils.DatabaseHelper;

public class CatalogStudentsActivity extends AppCompatActivity implements
        android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CM_DELETE_ID = 1;
    private static SimpleCursorAdapter catalogStudentsSimpleCursorAdapter;
    public static boolean catalogStudentsRecordSaved = false;
    private static String catalogType;
    private static String group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog_students);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        catalogType = bundle.getString("catalogType");
        group = bundle.getString("group");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetailCatalogDialogActivity.class);
                intent.putExtra("newRecord", true);
                intent.putExtra("catalogType", catalogType);
                intent.putExtra("group", group);
                startActivity(intent);
            }
        });
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {}

        String[] from = new String[]{DatabaseHelper.SNAME_STUDENTS_COLUMN,
                DatabaseHelper.FNAME_STUDENTS_COLUMN, DatabaseHelper.SURNAME_STUDENTS_COLUMN};
        int[] to = new int[] {R.id.catalog_list_item_sname_text,
                R.id.catalog_list_item_fname_text, R.id.catalog_list_item_surname_text};

        catalogStudentsSimpleCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.catalog_students_list_item, null, from, to, 0);

        ListView catalogStudentsList = (ListView) findViewById(R.id.catalog_students_list);
        try {
            catalogStudentsList.setAdapter(catalogStudentsSimpleCursorAdapter);
        } catch (NullPointerException e) {}

        catalogStudentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetailCatalogDialogActivity.class);
                Cursor cursor = MainActivity.databaseHelper.getStudentsRecordsByGroup(group);
                intent.putExtra("catalogType", catalogType);
                intent.putExtra("newRecord", false);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    if (cursor.getInt(0) == id) {
                        intent.putExtra("id", id);
                        intent.putExtra("sName", cursor.getString(1));
                        intent.putExtra("fName", cursor.getString(2));
                        intent.putExtra("surname", cursor.getString(3));
                        intent.putExtra("group", group);
                    }
                    cursor.moveToNext();
                }
                startActivity(intent);

            }
        });

        registerForContextMenu(catalogStudentsList);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (catalogStudentsRecordSaved) {

            Snackbar snackbar = Snackbar.make(findViewById(R.id.catalog_students_view),
                    R.string.record_save_confirmation_popup, Snackbar.LENGTH_SHORT);
            View view = snackbar.getView();
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            snackbar.show();

            catalogStudentsRecordSaved = false;
        }

        getSupportLoaderManager().getLoader(0).forceLoad();
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new lessonTypeCursorLoader(getApplicationContext(), MainActivity.databaseHelper);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        catalogStudentsSimpleCursorAdapter.swapCursor(data);
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

            return databaseHelper.getStudentsRecordsByGroup(group);
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
            Toast.makeText(getApplicationContext(), group, Toast.LENGTH_SHORT).show();
            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            MainActivity.databaseHelper.deleteStudentsRecord(adapterContextMenuInfo.id);

            Snackbar snackbar = Snackbar.make(findViewById(R.id.catalog_students_view),
                    R.string.record_delete_confirmation_popup, Snackbar.LENGTH_SHORT);
            View view = snackbar.getView();
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            snackbar.show();

            getSupportLoaderManager().getLoader(0).forceLoad();

            return true;
        }

        return super.onContextItemSelected(item);
    }
}
