package com.gmail.jamgrorg.teacherorganizer.importExportUtils;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dd.processbutton.iml.ActionProcessButton;
import com.gmail.jamgrorg.teacherorganizer.DetailJournalActivity;
import com.gmail.jamgrorg.teacherorganizer.MainActivity;
import com.gmail.jamgrorg.teacherorganizer.R;
import com.gmail.jamgrorg.teacherorganizer.databaseUtils.DatabaseHelper;
import com.nononsenseapps.filepicker.FilePickerActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ImportExportDialogActivity extends AppCompatActivity implements View.OnClickListener {

    private static int FILE_CODE;
    private static String importExportType;
    private static boolean isJournal = false;
    private static int DATA_TYPE_CODE = 0;
    private static EditText importExportFileNameField;
    private static Cursor lessonTypeCursor = null;
    private static Cursor subjectsCursore = null;
    private static Cursor groupsAndStudentsCursor = null;
    private static ActionProcessButton importExportButton;
    private static Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_export_dialog);

        bundle = getIntent().getExtras();
        importExportType = bundle.getString("importExportType");
        isJournal = bundle.getBoolean("isJournal");

        importExportFileNameField = (EditText) findViewById(R.id.import_export_file_name_field);

        RadioGroup importExportCatalogTypeRadiogroup = (RadioGroup) findViewById(R.id.import_export_catalog_type_radiogroup);

        importExportButton = (ActionProcessButton) findViewById(R.id.import_export_button1);
        try {
            importExportButton.setOnClickListener(this);
        } catch (NullPointerException e) {}
        RadioButton importExportLessonTypeRadioButton = (RadioButton) findViewById(R.id.import_export_lesson_type_radiobutton);
        try {
            importExportLessonTypeRadioButton.setChecked(true);
        } catch (NullPointerException e) {}
        importExportLessonTypeRadioButton.setOnClickListener(this);
        RadioButton importExportSubjectsRadioButton = (RadioButton) findViewById(R.id.import_export_subjects_radiobutton);
        try {
            importExportSubjectsRadioButton.setOnClickListener(this);
        } catch (NullPointerException e) {}
        RadioButton importExportGroupsAndStudentsRadioButton = (RadioButton) findViewById(R.id.import_export_groups_and_students_radiobutton);
        try {
            importExportGroupsAndStudentsRadioButton.setOnClickListener(this);
        } catch (NullPointerException e) {}

        if (!isJournal) {
            importExportCatalogTypeRadiogroup.setVisibility(View.VISIBLE);

            if (importExportType.equals("export")) {
                setTitle(R.string.data_management_export_data_button_title);

                lessonTypeCursor = MainActivity.databaseHelper.getAllLessonTypeRecords();
                subjectsCursore = MainActivity.databaseHelper.getAllSubjectsRecords();
                groupsAndStudentsCursor = MainActivity.databaseHelper.getAllStudentsRecords();

                importExportFileNameField.setVisibility(View.VISIBLE);
                importExportButton.setNormalText(getResources().getString(R.string.export_button_text));
                importExportButton.setEnabled(false);
            } else {
                setTitle(R.string.data_management_import_data_button_title);
                importExportFileNameField.setVisibility(View.GONE);
                importExportButton.setNormalText(getResources().getString(R.string.import_button_text));
            }

            checkCatalogs(lessonTypeCursor);
        } else {
            setTitle(R.string.data_management_export_data_button_title);
            importExportButton.setEnabled(false);
            importExportButton.setErrorText(getResources().getText(R.string.export_enter_file_name_button_text));
            importExportButton.setProgress(-1);
            importExportCatalogTypeRadiogroup.setVisibility(View.GONE);
        }

        importExportFileNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (importExportFileNameField.getText().toString().trim().length() != 0) {
                    importExportButton.setEnabled(true);
                    importExportButton.setProgress(0);
                } else {
                    importExportButton.setErrorText(getResources().getText(R.string.export_enter_file_name_button_text));
                    importExportButton.setProgress(-1);
                    importExportButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void readXLSFile(int dataTypeCode, String file) throws IOException, BiffException {
        File xlsFile = new File(file);

        Workbook workbook = Workbook.getWorkbook(xlsFile);

        Sheet sheet = workbook.getSheet(0);

        String[][] val = new String[sheet.getRows()-1][sheet.getColumns()];

        for (int colls = 0; colls < sheet.getColumns(); colls++) {
            for (int rows = 1; rows < sheet.getRows(); rows++) {
                val[rows-1][colls] = sheet.getCell(colls, rows).getContents();
            }
        }

        switch (dataTypeCode) {
            case 0:
                for (String[] aVal : val) {
                    MainActivity.databaseHelper.insertRecord(
                            DatabaseHelper.LESSON_TYPE_TABLE,
                            new String[]{DatabaseHelper.TITLE_LESSON_TYPE_COLUMN},
                            aVal);
                }
                break;
            case 1:
                for (String[] aVal : val) {
                    MainActivity.databaseHelper.insertRecord(
                            DatabaseHelper.SUBJECTS_TABLE,
                            new String[]{DatabaseHelper.TITLE_SUBJECTS_COLUMN},
                            aVal);
                }
                break;
            case 2:
                for (String[] aVal : val) {
                    MainActivity.databaseHelper.insertRecord(DatabaseHelper.STUDENTS_TABLE,
                            new String[]{DatabaseHelper.SNAME_STUDENTS_COLUMN,
                                    DatabaseHelper.FNAME_STUDENTS_COLUMN,
                                    DatabaseHelper.SURNAME_STUDENTS_COLUMN,
                                    DatabaseHelper.GROUP_STUDENTS_COLUMN},
                            aVal);
                }
                break;
            case 3:

                break;
            default:
                break;
        }
    }

    private void writeXLSFile(int dataTypeCode, String dir, int rowCount,
                              int collCount, String[][] journalData) throws IOException, WriteException {
        File xlsFile = new File(dir + "/" +
                importExportFileNameField.getText().toString().trim() + ".xls");

        WritableWorkbook workbook = Workbook.createWorkbook(xlsFile);

        WritableSheet sheet = workbook.createSheet("Sheet1", 0);

        Label[] labels;

        Cursor cursor;

        int currRow = 1;

        switch (dataTypeCode) {
            case 0:
                labels = new Label[1];
                labels[0] = new Label(0, 0, "title");
                sheet.addCell(labels[0]);

                cursor = MainActivity.databaseHelper.getAllLessonTypeRecords();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Label data = new Label(0, currRow, cursor.getString(1));
                    sheet.addCell(data);
                    cursor.moveToNext();
                    currRow++;
                }
                break;
            case 1:
                labels = new Label[1];
                labels[0] = new Label(0, 0, "title");
                sheet.addCell(labels[0]);

                cursor = MainActivity.databaseHelper.getAllSubjectsRecords();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Label data = new Label(0, currRow, cursor.getString(1));
                    sheet.addCell(data);
                    cursor.moveToNext();
                    currRow++;
                }
                break;
            case 2:
                labels = new Label[4];
                labels[0] = new Label(0, 0, "sName");
                labels[1] = new Label(1, 0, "fName");
                labels[2] = new Label(2, 0, "surname");
                labels[3] = new Label(3, 0, "group");
                for (int currColl = 0; currColl < 4; currColl++) {
                    sheet.addCell(labels[currColl]);
                }

                cursor = MainActivity.databaseHelper.getAllStudentsRecords();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Label[] data = new Label[4];
                    for (int currColl = 0; currColl < 4; currColl++) {
                        data[currColl] = new Label(currColl, currRow, cursor.getString(currColl+1));
                        sheet.addCell(data[currColl]);
                    }
                    cursor.moveToNext();
                    currRow++;
                }
                break;
            case 3:
                labels = new Label[rowCount];
                for (int i = 0; i < rowCount; i++) {
                    for (int j = 0; j < collCount; j++) {
                        labels[i] = new Label(j, i, journalData[j][i]);
                        sheet.addCell(labels[i]);
                    }
                }

                break;
            default:
                break;
        }

        workbook.write();
        workbook.close();
    }

    private void getDir() {
        Intent i = new Intent(getApplicationContext(), FilePickerActivity.class);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        if (!isJournal) {
            if (importExportType.equals("import")) {
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
            } else {
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
            }
        } else {
            i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
        }

        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
        startActivityForResult(i, FILE_CODE);
    }

    private boolean checkExtension(String file) {
        return file.substring(file.length() - 4, file.length()).equals(".xls");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                // For JellyBean and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clip = data.getClipData();

                    if (clip != null) {
                        for (int i = 0; i < clip.getItemCount(); i++) {
                            Uri uri = clip.getItemAt(i).getUri();
                            // Do something with the URI
                            try {
                                if (importExportType.equals("import")) {
                                    if (checkExtension(uri.toString())) {
                                        readXLSFile(DATA_TYPE_CODE, uri.toString().substring(7));
                                        showProgress(importExportType);
                                    } else {
                                        showError(importExportType);
                                    }
                                } else if (importExportType.equals("export")) {
                                    if (!isJournal) {
                                        writeXLSFile(DATA_TYPE_CODE, uri.toString().substring(7), 1, 1, null);
                                    } else {
                                        writeXLSFile(3, uri.toString().substring(7),
                                                bundle.getInt("rowCount"),
                                                bundle.getInt("collCount"),
                                                DetailJournalActivity.data);
                                    }
                                    showProgress(importExportType);
                                }
                            } catch (IOException | BiffException e) {
                                //e.printStackTrace();
                            } catch (WriteException e) {
                               //e.printStackTrace();
                            }
                        }
                    }
                    // For Ice Cream Sandwich
                } else {
                    ArrayList<String> paths = data.getStringArrayListExtra
                            (FilePickerActivity.EXTRA_PATHS);

                    if (paths != null) {
                        for (String path: paths) {
                            Uri uri = Uri.parse(path);
                            // Do something with the URI
                            try {
                                if (importExportType.equals("import")) {
                                    if (checkExtension(uri.toString())) {
                                        readXLSFile(DATA_TYPE_CODE, uri.toString().substring(7));
                                        showProgress(importExportType);
                                    } else {
                                        showError(importExportType);
                                    }
                                } else if (importExportType.equals("export")) {
                                    if (!isJournal) {
                                        writeXLSFile(DATA_TYPE_CODE, uri.toString().substring(7), 1, 1, null);
                                    } else {
                                        writeXLSFile(3, uri.toString().substring(7),
                                                bundle.getInt("rowCount"),
                                                bundle.getInt("collCount"),
                                                DetailJournalActivity.data);
                                    }
                                    showProgress(importExportType);
                                }
                            } catch (IOException | BiffException e) {
                                //e.printStackTrace();
                            } catch (WriteException e) {
                                //e.printStackTrace();
                            }
                        }
                    }
                }

            } else {
                Uri uri = data.getData();
                // Do something with the URI
                try {
                    if (importExportType.equals("import")) {
                        if (checkExtension(uri.toString())) {
                            readXLSFile(DATA_TYPE_CODE, uri.toString().substring(7));
                            showProgress(importExportType);
                        } else {
                            showError(importExportType);
                        }
                    } else if (importExportType.equals("export")) {
                        if (!isJournal) {
                            writeXLSFile(DATA_TYPE_CODE, uri.toString().substring(7), 1, 1, null);
                        } else {
                            writeXLSFile(3, uri.toString().substring(7),
                                    bundle.getInt("rowCount"),
                                    bundle.getInt("collCount"),
                                    /*(String[][]) bundle.getSerializable("data")*/
                                    DetailJournalActivity.data);
                        }
                        showProgress(importExportType);
                    }
                } catch (IOException | BiffException e) {
                    //e.printStackTrace();
                } catch (WriteException e) {
                    //e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.import_export_button1:
                getDir();
                break;
            case R.id.import_export_lesson_type_radiobutton:
                //labelCount = 1;
                DATA_TYPE_CODE = 0;
                checkCatalogs(lessonTypeCursor);
                break;
            case R.id.import_export_subjects_radiobutton:
                //labelCount = 1;
                DATA_TYPE_CODE = 1;
                checkCatalogs(subjectsCursore);
                break;
            case R.id.import_export_groups_and_students_radiobutton:
                //labelCount = 4;
                DATA_TYPE_CODE = 2;
                checkCatalogs(groupsAndStudentsCursor);
                break;
            default:
                break;
        }
    }

    private void checkCatalogs(Cursor cursor) {
        if (importExportType.equals("export")) {
            if ((cursor == null) || (cursor.getCount() <= 0)) {
                importExportFileNameField.setEnabled(false);
                showError(importExportType);
            } else {
                importExportButton.setProgress(0);
                importExportFileNameField.setEnabled(true);
            }
        }
    }

    private void showProgress(String importExportType) {
        if (importExportType.equals("import")) {
            importExportButton.setNormalText(getResources().getString(R.string.import_button_text));
            importExportButton.setLoadingText(
                    getResources().getString(R.string.data_management_import_progress_dialog_title));
            importExportButton.setCompleteText(
                    getResources().getString(R.string.catalog_import_confirmaion_popup));
            importExportButton.setErrorText(
                    getResources().getString(R.string.data_management_import_file_extension_error));
        } else if (importExportType.equals("export")) {
            importExportButton.setNormalText(getResources().getString(R.string.export_button_text));
            importExportButton.setLoadingText(
                    getResources().getString(R.string.data_management_export_progress_dialog_title));
            importExportButton.setCompleteText(
                    getResources().getString(R.string.catalog_export_confirmaion_popup));
        }

        importExportButton.setMode(ActionProcessButton.Mode.ENDLESS);

        importExportButton.setProgress(1);
        setOnOutsideTouchFinish(false);

        importExportButton.setEnabled(false);

        new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                importExportButton.setProgress(100);
                new CountDownTimer(2000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        importExportButton.setProgress(0);
                        importExportButton.setEnabled(true);
                        setOnOutsideTouchFinish(true);
                    }
                }.start();
            }
        }.start();
    }

    private void showError(String importExportType) {
        if (importExportType.equals("import")) {
            importExportButton.setNormalText(getResources().getString(R.string.import_button_text));
            importExportButton.setErrorText(
                    getResources().getString(R.string.data_management_import_file_extension_error));

            importExportButton.setMode(ActionProcessButton.Mode.ENDLESS);
            importExportButton.setProgress(-1);
            importExportButton.setEnabled(false);

            new CountDownTimer(3000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {

                    importExportButton.setProgress(0);
                    importExportButton.setEnabled(true);
                }
            }.start();

        } else  if (importExportType.equals("export")) {
            importExportButton.setNormalText(getResources().getString(R.string.export_button_text));
            importExportButton.setErrorText(
                    getResources().getString(R.string.no_data_in_catalog_toast_message_text));
            importExportButton.setProgress(-1);
        }
    }

    private void setOnOutsideTouchFinish(boolean state) {
        this.setFinishOnTouchOutside(state);
    }
}
