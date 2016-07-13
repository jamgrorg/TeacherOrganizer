package com.gmail.jamgrorg.teacherorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gmail.jamgrorg.teacherorganizer.fragments.FragmentTimetable_of_classes;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DetailTTOCDialogActivity extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {

    private static boolean newRecord = false;
    private static long id;
    private static String date;
    private static int recordCount;
    private static String startTime;
    private static String endTime;
    private static String type;
    private static String subject;
    private static String group;
    private static String lesson_hall;
    private static EditText newTTOCDateField;
    private static EditText newTTOCStartTimeField;
    private static EditText newTTOCEndTimeField;
    private static EditText newTTOCTypeField;
    private static EditText newTTOCSubjectField;
    private static EditText newTTOCGroupField;
    private static EditText newTTOCLessonHallField;
    private static Button newTTOCSaveButton;
    private static SimpleDateFormat simpleDateFormat;
    private static final boolean[] validFields = {false, false, false, false, false, false};
    private static CheckBox newTTOCRepeatCheckbox;
    private static LinearLayout newTTOCRepeatLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ttoc_dialog);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getLong("id");
        newRecord = bundle.getBoolean("newRecord");
        recordCount = bundle.getInt("recordCount");
        date = bundle.getString("date");
        startTime = bundle.getString("startTime");
        endTime = bundle.getString("endTime");
        type = bundle.getString("type");
        subject = bundle.getString("subject");
        group = bundle.getString("group");
        lesson_hall = bundle.getString("lesson_hall");

        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        newTTOCDateField = (EditText) findViewById(R.id.new_ttoc_date_field);
        newTTOCStartTimeField = (EditText) findViewById(R.id.new_ttoc_start_time_field);
        newTTOCEndTimeField = (EditText) findViewById(R.id.new_ttoc_end_time_field);
        newTTOCTypeField = (EditText) findViewById(R.id.new_ttoc_type_field);
        newTTOCSubjectField = (EditText) findViewById(R.id.new_ttoc_subject_field);
        newTTOCGroupField = (EditText) findViewById(R.id.new_ttoc_group_field);
        newTTOCLessonHallField = (EditText) findViewById(R.id.new_ttoc_lesson_hall_field);
        newTTOCSaveButton = (Button) findViewById(R.id.new_ttoc_save_button);
        Button newTTOCCancelButton = (Button) findViewById(R.id.new_ttoc_cancel_buton);
        newTTOCRepeatCheckbox = (CheckBox) findViewById(R.id.new_ttoc_repeat_checkbox);
        newTTOCRepeatLayout = (LinearLayout) findViewById(R.id.new_ttoc_repeat_layout);

        newTTOCDateField.setKeyListener(null);
        newTTOCStartTimeField.setKeyListener(null);
        newTTOCEndTimeField.setKeyListener(null);
        newTTOCTypeField.setKeyListener(null);
        newTTOCSubjectField.setKeyListener(null);
        newTTOCGroupField.setKeyListener(null);

        if (!newRecord) {
            newTTOCRepeatCheckbox.setVisibility(View.GONE);
        }

        newTTOCRepeatCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    newTTOCRepeatLayout.setVisibility(View.VISIBLE);
                } else {
                    newTTOCRepeatLayout.setVisibility(View.GONE);
                }
            }
        });

        newTTOCTypeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CatalogSelectionActivity.class);
                intent.putExtra("catalogType", "lessonType");
                startActivityForResult(intent, 1);
            }
        });

        newTTOCSubjectField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CatalogSelectionActivity.class);
                intent.putExtra("catalogType", "subjects");
                startActivityForResult(intent, 2);
            }
        });

        newTTOCGroupField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CatalogSelectionActivity.class);
                intent.putExtra("catalogType", "groups");
                startActivityForResult(intent, 3);
            }
        });

        final Calendar newTTOCCalendar = Calendar.getInstance();

        newTTOCDateField.setText(date);

        final com.wdullaer.materialdatetimepicker.time.TimePickerDialog startTime =
                com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                        this, newTTOCCalendar.get(Calendar.HOUR_OF_DAY),
                        newTTOCCalendar.get(Calendar.MINUTE),
                        newTTOCCalendar.get(Calendar.SECOND), true
                );

        newTTOCStartTimeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime.setOnTimeSetListener(new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                        try {
                            newTTOCStartTimeField.setText(
                                    new SimpleDateFormat("HH").format(
                                            new SimpleDateFormat("HH").parse(
                                                    String.valueOf(hourOfDay))
                                    ) + "." +
                                    new SimpleDateFormat("mm").format(
                                            new SimpleDateFormat("mm").parse(
                                                    String.valueOf(minute))
                                    ));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                startTime.show(getFragmentManager(), "startTimePicker");
            }
        });

        final com.wdullaer.materialdatetimepicker.time.TimePickerDialog endTime =
                com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                        this, newTTOCCalendar.get(Calendar.HOUR_OF_DAY),
                        newTTOCCalendar.get(Calendar.MINUTE),
                        newTTOCCalendar.get(Calendar.SECOND), true
                );

        newTTOCEndTimeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTime.setOnTimeSetListener(new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                        try {
                            newTTOCEndTimeField.setText(
                                    new SimpleDateFormat("HH").format(
                                            new SimpleDateFormat("HH").parse(
                                                    String.valueOf(hourOfDay))
                                    ) + "." +
                                            new SimpleDateFormat("mm").format(
                                                    new SimpleDateFormat("mm").parse(
                                                            String.valueOf(minute))
                                            ));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                endTime.show(getFragmentManager(), "endTimePicker");
            }
        });

        newTTOCCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (!newRecord) {
            editRecord();
        }

        newTTOCSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newRecord) { // Если создается новая запись
                    MainActivity.databaseHelper.insertTTOCRecord(
                            newTTOCDateField.getText().toString().trim(),
                            recordCount + 1,
                            newTTOCStartTimeField.getText().toString().trim(),
                            newTTOCEndTimeField.getText().toString().trim(),
                            newTTOCTypeField.getText().toString().trim(),
                            newTTOCSubjectField.getText().toString().trim(),
                            newTTOCGroupField.getText().toString().trim(),
                            newTTOCLessonHallField.getText().toString().trim());

                } else { // Если редактируется существующая заметка
                    MainActivity.databaseHelper.updateTTOCRecord(id,
                            newTTOCDateField.getText().toString().trim(),
                            newTTOCStartTimeField.getText().toString().trim(),
                            newTTOCEndTimeField.getText().toString().trim(),
                            newTTOCTypeField.getText().toString().trim(),
                            newTTOCSubjectField.getText().toString().trim(),
                            newTTOCGroupField.getText().toString().trim(),
                            newTTOCLessonHallField.getText().toString().trim()); // Редактируем существующую запись в БД
                }

                FragmentTimetable_of_classes.isTTOCSaved = true;
                finish();
            }
        });

        newTTOCStartTimeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validFields[0] = newTTOCStartTimeField.getText().length() != 0;
                 checkValidFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newTTOCEndTimeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (newTTOCEndTimeField.getText().length() != 0) {
                    validFields[1] = true;
                } else {
                    validFields[1] = false;
                }
                checkValidFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newTTOCTypeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validFields[2] = newTTOCTypeField.getText().length() != 0;
                checkValidFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newTTOCSubjectField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validFields[3] = newTTOCSubjectField.getText().length() != 0;
                checkValidFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newTTOCGroupField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validFields[4] = newTTOCGroupField.getText().length() != 0;
                checkValidFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newTTOCLessonHallField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validFields[5] = newTTOCLessonHallField.getText().length() != 0;
                checkValidFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void editRecord() {
        setTitle(subject); // Устанавливаем заголовок окна в соответствии с заголовком заметки
        newTTOCDateField.setText(date); // Устанавливаем заголовок заметки
        newTTOCStartTimeField.setText(startTime); // Устанавливаем текст заметки
        newTTOCEndTimeField.setText(endTime); // Устанавливаем дату уведомления
        newTTOCTypeField.setText(type); // Устанавливаем время уведомления
        newTTOCSubjectField.setText(subject);
        newTTOCGroupField.setText(group);
        newTTOCLessonHallField.setText(lesson_hall);
        for (int i = 0; i <= 5; i++) {
            validFields[i] = true;
        }
        checkValidFields();
    }

    private void checkValidFields() {
        int validFieldCount = 0;
        for (int i = 0; i <= 5; i++) {
            if (validFields[i]) {
                validFieldCount++;
            }
        }

        if (validFieldCount == 6) {
            newTTOCSaveButton.setEnabled(true);
        } else {
            newTTOCSaveButton.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        com.wdullaer.materialdatetimepicker.time.TimePickerDialog newStartTime =
                (com.wdullaer.materialdatetimepicker.time.TimePickerDialog)
                        getFragmentManager().findFragmentByTag("startTimePicker");

        if(newStartTime != null) {
            newStartTime.setOnTimeSetListener(new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                    try {
                        newTTOCStartTimeField.setText(
                                new SimpleDateFormat("HH").format(
                                        new SimpleDateFormat("HH").parse(
                                                String.valueOf(hourOfDay))
                                ) + "." +
                                        new SimpleDateFormat("mm").format(
                                                new SimpleDateFormat("mm").parse(
                                                        String.valueOf(minute))
                                        ));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        com.wdullaer.materialdatetimepicker.time.TimePickerDialog newEbdTime =
                (com.wdullaer.materialdatetimepicker.time.TimePickerDialog)
                        getFragmentManager().findFragmentByTag("endTimePicker");

        if(newEbdTime != null) {
            newEbdTime.setOnTimeSetListener(new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                    try {
                        newTTOCEndTimeField.setText(
                                new SimpleDateFormat("HH").format(
                                        new SimpleDateFormat("HH").parse(
                                                String.valueOf(hourOfDay))
                                ) + "." +
                                        new SimpleDateFormat("mm").format(
                                                new SimpleDateFormat("mm").parse(
                                                        String.valueOf(minute))
                                        ));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        } else {
            switch (requestCode) {
                case 2:
                    newTTOCSubjectField.setText(data.getStringExtra("subject"));
                    break;
                case 1:
                    newTTOCTypeField.setText(data.getStringExtra("lessonType"));
                    break;
                case 3:
                    newTTOCGroupField.setText(data.getStringExtra("group"));
                default:
                    break;
            }
        }
    }
}
