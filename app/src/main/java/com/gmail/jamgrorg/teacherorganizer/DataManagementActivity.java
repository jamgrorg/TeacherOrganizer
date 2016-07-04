package com.gmail.jamgrorg.teacherorganizer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dd.processbutton.iml.ActionProcessButton;
import com.gmail.jamgrorg.teacherorganizer.importExportUtils.ImportExportDialogActivity;

import dmax.dialog.SpotsDialog;

public class DataManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_management);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {}

        ActionProcessButton clearDatabaseButton = (ActionProcessButton) findViewById(R.id.data_management_clear_all_data_button);
        clearDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(v.getContext()).
                        setTitle(getResources().getString(R.string.data_management_clear_all_data_dialog_title)).
                        setMessage(getResources().getString(R.string.data_management_clear_all_data_dialog_message)).
                        setIcon(R.drawable.ic_alert_circle_outline_black_48dp).
                        setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final SpotsDialog spotsDialog = new SpotsDialog(v.getContext(), R.style.SpotCleanDatabaseDialogStyle);
                                spotsDialog.show();
                                MainActivity.databaseHelper.clearDatabase(MainActivity.sqLiteDatabase);
                                new CountDownTimer(2000, 1000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        spotsDialog.dismiss();
                                        Snackbar snackbar = Snackbar.make(v,
                                                R.string.database_clean_confirmaion_popup,
                                                Snackbar.LENGTH_SHORT);
                                        View view = snackbar.getView();
                                        view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                                        snackbar.show();
                                    }
                                }.start();
                            }
                        }).setNegativeButton(android.R.string.no, null).show();
            }
        });

        ActionProcessButton lessonTypeCatalogButton = (ActionProcessButton) findViewById(R.id.data_management_lesson_type_catalog_button);
        lessonTypeCatalogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataManagementActivity.this, CatalogActivity.class);
                intent.putExtra("catalogType", "lessonType");
                startActivity(intent);
            }
        });

        ActionProcessButton subjectsCatalogButton = (ActionProcessButton) findViewById(R.id.data_management_subjects_catalog_button);
        subjectsCatalogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataManagementActivity.this, CatalogActivity.class);
                intent.putExtra("catalogType", "subjects");
                startActivity(intent);
            }
        });

        ActionProcessButton groupsAndStudentsButton = (ActionProcessButton) findViewById(R.id.data_management_groups_and_students_catalog_button);
        groupsAndStudentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataManagementActivity.this, CatalogActivity.class);
                intent.putExtra("catalogType", "groups");
                startActivity(intent);
            }
        });

        ActionProcessButton importDataButton = (ActionProcessButton) findViewById(R.id.data_management_import_data_button);
        importDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataManagementActivity.this, ImportExportDialogActivity.class);
                intent.putExtra("importExportType", "import");
                startActivity(intent);
            }
        });

        ActionProcessButton exportDataButton = (ActionProcessButton) findViewById(R.id.data_management_export_data_button);
        exportDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataManagementActivity.this, ImportExportDialogActivity.class);
                intent.putExtra("importExportType", "export");
                startActivity(intent);
            }
        });
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
