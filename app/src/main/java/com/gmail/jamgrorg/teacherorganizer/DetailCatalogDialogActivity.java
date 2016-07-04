package com.gmail.jamgrorg.teacherorganizer;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class DetailCatalogDialogActivity extends AppCompatActivity {

    private static boolean newRecord = false;
    private static long id;
    private static String title;
    private static EditText newCatalogRecordTitleField;
    private static Button newCatalogRecordSaveButton;
    private static String catalogType;
    private static String group = "";
    private static EditText newCatalogRecordSNameField;
    private static EditText newCatalogRecordFNameField;
    private static EditText newCatalogRecordSurnameField;
    private static EditText newCatalogRecordGroupField;
    private static String sName;
    private static String fName;
    private static String surname;
    private static final boolean[] validStudentsFields = {false, false, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_catalog_dialog);

        Bundle bundle = getIntent().getExtras();
        newRecord = bundle.getBoolean("newRecord");
        id = bundle.getLong("id");
        title = bundle.getString("title");
        catalogType = bundle.getString("catalogType");

        newCatalogRecordTitleField = (EditText) findViewById(R.id.new_catalog_title_field);
        newCatalogRecordSaveButton = (Button) findViewById(R.id.new_catalog_save_button);
        Button newCatalogRecordCancelButton = (Button) findViewById(R.id.new_catalog_cancel_button);
        newCatalogRecordSNameField = (EditText) findViewById(R.id.new_catalog_sname_field);
        newCatalogRecordFNameField = (EditText) findViewById(R.id.new_catalog_fname_field);
        newCatalogRecordSurnameField = (EditText) findViewById(R.id.new_catalog_surname_field);
        newCatalogRecordGroupField = (EditText) findViewById(R.id.new_catalog_group_field);

        TextInputLayout newCatalogRecordTitle = (TextInputLayout) findViewById(R.id.new_catalog_title);

        LinearLayout newCatalogLayout = (LinearLayout) findViewById(R.id.new_catalog_layout);
        LinearLayout newCatalogStudentsLayout = (LinearLayout) findViewById(R.id.new_catalog_student_layout);

        newCatalogLayout.setVisibility(View.VISIBLE);
        newCatalogStudentsLayout.setVisibility(View.GONE);

        newCatalogRecordSaveButton.setEnabled(false);

        newCatalogRecordTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (newCatalogRecordTitleField.getText().toString().trim().length() == 0) {
                    setTitle(R.string.new_record_title);
                    newCatalogRecordSaveButton.setEnabled(false);
                } else {
                    setTitle(newCatalogRecordTitleField.getText().toString().trim());
                    newCatalogRecordSaveButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newCatalogRecordSNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (newCatalogRecordSNameField.getText().toString().length() != 0) {
                    validStudentsFields[0] = true;
                } else {
                    validStudentsFields[0] = false;
                }
                setTitle(newCatalogRecordGroupField.getText().toString().trim()
                        + " : " + newCatalogRecordSNameField.getText().toString().trim()
                        + " " + newCatalogRecordFNameField.getText().toString().trim()
                        + " " + newCatalogRecordSurnameField.getText().toString().trim());
                checkValidStudentsFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newCatalogRecordFNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validStudentsFields[1] = newCatalogRecordFNameField.getText().toString().length() != 0;
                setTitle(newCatalogRecordGroupField.getText().toString().trim()
                        + " : " + newCatalogRecordSNameField.getText().toString().trim()
                        + " " + newCatalogRecordFNameField.getText().toString().trim()
                        + " " + newCatalogRecordSurnameField.getText().toString().trim());
                checkValidStudentsFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newCatalogRecordSurnameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validStudentsFields[2] = newCatalogRecordSurnameField.getText().toString().length() != 0;
                setTitle(newCatalogRecordGroupField.getText().toString().trim()
                        + " : " + newCatalogRecordSNameField.getText().toString().trim()
                        + " " + newCatalogRecordFNameField.getText().toString().trim()
                        + " " + newCatalogRecordSurnameField.getText().toString().trim());
                checkValidStudentsFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newCatalogRecordGroupField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validStudentsFields[3] = newCatalogRecordGroupField.getText().toString().length() != 0;
                setTitle(newCatalogRecordGroupField.getText().toString().trim()
                        + " : " + newCatalogRecordSNameField.getText().toString().trim()
                        + " " + newCatalogRecordFNameField.getText().toString().trim()
                        + " " + newCatalogRecordSurnameField.getText().toString().trim());
                checkValidStudentsFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        switch (catalogType) {
            case "lessonType":
                newCatalogRecordTitle.setHint(
                        getResources().getString(R.string.detail_lesson_type_title_text));
                break;
            case "subjects":
                newCatalogRecordTitle.setHint(
                        getResources().getString(R.string.detail_subjects_title_text));
                break;
            case "students":
                group = bundle.getString("group");
                sName = bundle.getString("sName");
                fName = bundle.getString("fName");
                surname = bundle.getString("surname");
                newCatalogLayout.setVisibility(View.GONE);
                newCatalogStudentsLayout.setVisibility(View.VISIBLE);
                newCatalogRecordGroupField.setText(group);
                if (newCatalogRecordGroupField.getText().toString().length() == 0) {
                    setTitle(R.string.new_record_title);
                }
                break;
            default:
                break;
        }

        if (!newRecord) {
            editRecord();
        }

        newCatalogRecordSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newRecord) {
                    switch (catalogType) {
                        case "lessonType":
                            MainActivity.databaseHelper.insertLessonTypeRecord(
                                    newCatalogRecordTitleField.getText().toString().trim());
                            break;
                        case "subjects":
                            MainActivity.databaseHelper.insertSubjectsRecord(
                                    newCatalogRecordTitleField.getText().toString().trim());
                            break;
                        case "students":

                            MainActivity.databaseHelper.insertStudentsRecord(
                                    newCatalogRecordSNameField.getText().toString().trim(),
                                    newCatalogRecordFNameField.getText().toString().trim(),
                                    newCatalogRecordSurnameField.getText().toString().trim(),
                                    newCatalogRecordGroupField.getText().toString().trim());
                            break;
                    }
                } else {
                    switch (catalogType) {
                        case "lessonType":
                            MainActivity.databaseHelper.updateLessonTypeRecord(id,
                                    newCatalogRecordTitleField.getText().toString().trim());
                            break;
                        case "subjects":
                            MainActivity.databaseHelper.updateSubjectsRecord(id,
                                    newCatalogRecordTitleField.getText().toString().trim());
                            break;
                        case "students":
                            MainActivity.databaseHelper.updateStudentsRecord(id,
                                    newCatalogRecordSNameField.getText().toString().trim(),
                                    newCatalogRecordFNameField.getText().toString().trim(),
                                    newCatalogRecordSurnameField.getText().toString().trim(),
                                    group);
                            break;
                    }
                }

                CatalogStudentsActivity.catalogStudentsRecordSaved = true;
                CatalogActivity.catalogRecordSaved = true;
                finish();
            }
        });

        newCatalogRecordCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void editRecord() {
        if (catalogType.equals("students")) {
            setTitle(group + " : " + sName + " " + fName + " " + surname);
            newCatalogRecordSNameField.setText(sName);
            newCatalogRecordFNameField.setText(fName);
            newCatalogRecordSurnameField.setText(surname);
            newCatalogRecordGroupField.setText(group);
            for (int i = 0; i <= 3; i++) {
                validStudentsFields[i] = true;
            }
            checkValidStudentsFields();
        } else {
            setTitle(title);
            newCatalogRecordTitleField.setText(title);
        }
    }

    private void checkValidStudentsFields() {
        int validStudentsFieldCount = 0;
        for (int i = 0; i <= 3; i++) {
            if (validStudentsFields[i]) {
                validStudentsFieldCount++;
            } else {
                validStudentsFieldCount--;
            }
        }

        if (validStudentsFieldCount == 4) {
            newCatalogRecordSaveButton.setEnabled(true);
        } else {
            newCatalogRecordSaveButton.setEnabled(false);
        }
    }
}
