package com.gmail.jamgrorg.teacherorganizer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gmail.jamgrorg.teacherorganizer.fragments.FragmentNotebook;
import com.gmail.jamgrorg.teacherorganizer.notificationUtils.NotificationActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DetailNoteDialogActivity extends AppCompatActivity {

    private static EditText newNoteTitleField;
    private static EditText newNoteBodyField;
    private static Button saveButton;
    private static Button cancelButton;
    private static CheckBox newNoteNotificationCheckBox;
    private static EditText newNoteAlarmDateField;
    private static EditText newNoteAlarmTimeField;
    private static LinearLayout newNoteAlarmLayout;
    private static boolean newRecord = false;
    private static long id;
    private static String title;
    private static String body;
    private static SimpleDateFormat newNoteSimpleDateFormat;
    private static SimpleDateFormat newNoteSimpleTimeFormat;
    private static int newNoteIsNotification = 0;
    private static String newNoteAlarmDate;
    private static String newNoteAlarmTime;
    private static Calendar newNoteAlarmCalendar;
    private static RadioGroup newNoteAlarmTypeRadioGroup;
    private static int newNoteAlarmType = 0;
    private static RadioButton newNoteAlarmTypeOnceRadioButton;
    private static RadioButton newNoteAlarmTypeRepeatRadioButton;
    private static int newNoteNotificationYear;
    private static int newNoteNotificationMonth;
    private static int newNoteNotificationDay;
    private static int newNoteNotificationHour;
    private static int newNoteNotificationMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note_dialog);

        Bundle bundle = getIntent().getExtras();
        newRecord = bundle.getBoolean("newRecord");
        id = bundle.getLong("id");
        title = bundle.getString("title");
        body = bundle.getString("body");
        newNoteIsNotification = bundle.getInt("isNotification");
        newNoteAlarmDate = bundle.getString("alarmDate");
        newNoteAlarmTime = bundle.getString("alarmTime");
        newNoteAlarmType = bundle.getInt("alarmType");

        newNoteSimpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        newNoteSimpleTimeFormat = new SimpleDateFormat("HH.mm");

        newNoteTitleField = (EditText) findViewById(R.id.new_note_title_field);
        newNoteBodyField = (EditText) findViewById(R.id.new_note_body_field);
        saveButton = (Button) findViewById(R.id.new_note_save_button);
        cancelButton = (Button) findViewById(R.id.new_note_cancel_button);
        newNoteAlarmLayout = (LinearLayout) findViewById(R.id.new_note_alarm_layout);
        newNoteAlarmDateField = (EditText) findViewById(R.id.new_note_alarm_date_field);
        newNoteAlarmTimeField = (EditText) findViewById(R.id.new_note_alarm_time_field);
        newNoteAlarmTypeRadioGroup = (RadioGroup) findViewById(R.id.new_note_notification_type_radiogroup);
        newNoteAlarmTypeOnceRadioButton = (RadioButton) findViewById(R.id.new_note_notification_type_once);
        newNoteAlarmTypeRepeatRadioButton = (RadioButton) findViewById(R.id.new_note_notification_type_repeate);

        newNoteAlarmDateField.setKeyListener(null);
        newNoteAlarmTimeField.setKeyListener(null);

        newNoteAlarmTypeOnceRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newNoteAlarmType = 1;
            }
        });

        newNoteAlarmTypeRepeatRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newNoteAlarmType = 2;
            }
        });

        newNoteAlarmCalendar = Calendar.getInstance();

        newNoteAlarmDateField.setText(newNoteAlarmCalendar.getTime().toString().trim());

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newNoteAlarmCalendar.set(Calendar.YEAR, year);
                newNoteNotificationYear = year;
                newNoteAlarmCalendar.set(Calendar.MONTH, monthOfYear);
                newNoteNotificationMonth = monthOfYear;
                newNoteAlarmCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                newNoteNotificationDay = dayOfMonth;
                newNoteAlarmDateField.setText(newNoteSimpleDateFormat.format(newNoteAlarmCalendar.getTime()));
            }
        };

        newNoteAlarmDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(DetailNoteDialogActivity.this, date, newNoteAlarmCalendar.get(Calendar.YEAR),
                        newNoteAlarmCalendar.get(Calendar.MONTH), newNoteAlarmCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                newNoteAlarmCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                newNoteNotificationHour = hourOfDay;
                newNoteAlarmCalendar.set(Calendar.MINUTE, minute);
                newNoteNotificationMinute = minute;
                newNoteAlarmTimeField.setText(newNoteSimpleTimeFormat.format(newNoteAlarmCalendar.getTime()));
            }
        };

        newNoteAlarmTimeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(DetailNoteDialogActivity.this, time, newNoteAlarmCalendar.get(Calendar.HOUR_OF_DAY),
                        newNoteAlarmCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        // Делаем кнопку сохнарить недоступной
        saveButton.setEnabled(false);

        // Проверка поля на пустое значение в момент ввода текста
        newNoteTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (newNoteTitleField.getText().toString().trim().length() == 0) {
                    setTitle(R.string.title_activity_new_note);
                    saveButton.setEnabled(false);
                } else {
                    setTitle(newNoteTitleField.getText().toString().trim());
                    saveButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (!newRecord) {
            editRecord();
        }

        newNoteNotificationCheckBox = (CheckBox) findViewById(R.id.new_note_notification_checkbox);
        if (newNoteIsNotification == 0) {
            try {
                newNoteNotificationCheckBox.setChecked(false);
            } catch (NullPointerException e) {}
        } else {
            try {
                newNoteNotificationCheckBox.setChecked(true);
            } catch (NullPointerException e) {}
        }
        switchAlarmLayout(newNoteNotificationCheckBox, newNoteAlarmLayout);

        newNoteNotificationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                newNoteAlarmDateField.setText(newNoteSimpleDateFormat.format(newNoteAlarmCalendar.getTime()));
                newNoteAlarmTimeField.setText(newNoteSimpleTimeFormat.format(newNoteAlarmCalendar.getTimeInMillis() + 1000 * 60 * 60));
                switchAlarmLayout(newNoteNotificationCheckBox, newNoteAlarmLayout);
            }
        });
    }

    private void switchAlarmLayout(CheckBox checkBox, LinearLayout linearLayout) {
        if (checkBox.isChecked()) {
            linearLayout.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            layoutParams.height = ActionBar.LayoutParams.WRAP_CONTENT;
            linearLayout.setLayoutParams(layoutParams);
        } else {
            linearLayout.setVisibility(View.INVISIBLE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            layoutParams.height = 0;
            linearLayout.setLayoutParams(layoutParams);
        }
    }

    private void editRecord() {
        setTitle(title); // Устанавливаем заголовок окна в соответствии с заголовком заметки
        newNoteTitleField.setText(title); // Устанавливаем заголовок заметки
        newNoteBodyField.setText(body); // Устанавливаем текст заметки
        newNoteAlarmDateField.setText(newNoteAlarmDate); // Устанавливаем дату уведомления
        newNoteAlarmTimeField.setText(newNoteAlarmTime); // Устанавливаем время уведомления
        if (newNoteAlarmType == 1) { // Если уведомление без повтора
            newNoteAlarmTypeRepeatRadioButton.setChecked(true); // Выключаем radiobutton
        } else if (newNoteAlarmType == 2){
            newNoteAlarmTypeOnceRadioButton.setChecked(true); // Включаем radiobutton
        }
    }

    private void checkNotification() {
        if (newNoteNotificationCheckBox.isChecked()) { // Если выбран checkbox уведомления
            newNoteIsNotification = 1; // Устанавливаем тип заметки как уведомление
            if (newNoteAlarmDateField.getText().length() == 0 ||
                    newNoteAlarmTimeField.getText().length() == 0) {
                Toast.makeText(this, getString(R.string.new_note_fill_date_or_time_field_toast), Toast.LENGTH_LONG).show();
            }
        } else {
            newNoteIsNotification = 0; // Устанавливаем тип заметки просто заметка
            newNoteAlarmType = 0; // Указываем, что уведомление без повтора
            newNoteAlarmDateField.setText(""); // Убираем дату уведомление
            newNoteAlarmTimeField.setText(""); // Убираем время уведомления
        }
    }

    // Обработчик кнопки сохранить
    public void saveButtonClick(View view) {
        if (newNoteTitleField.getText().toString().trim().length() != 0) { // Проверка поля Заголовок на наличие текста
            checkNotification();

            if (newNoteNotificationCheckBox.isChecked()) { // Установка уведомления
                NotificationActivity notificationActivity = new NotificationActivity();
                notificationActivity.createNotification(
                        getApplicationContext(), newNoteNotificationYear, newNoteNotificationMonth,
                        newNoteNotificationDay, newNoteNotificationHour, newNoteNotificationMinute,
                        newNoteTitleField.getText().toString().trim(),
                        newNoteBodyField.getText().toString().trim());
            }

            if (newRecord) { // Если создается новая запись
                MainActivity.databaseHelper.insertNoteRecord(
                        newNoteTitleField.getText().toString().trim(),
                        newNoteBodyField.getText().toString().trim(),
                        newNoteIsNotification,
                        newNoteAlarmDateField.getText().toString().trim(),
                        newNoteAlarmTimeField.getText().toString().trim(),
                        newNoteAlarmType); // Вставляем новую запись в БД
            } else { // Если редактируется существующая заметка
                MainActivity.databaseHelper.updateNoteRecord(id,
                        newNoteTitleField.getText().toString().trim(),
                        newNoteBodyField.getText().toString().trim(),
                        newNoteIsNotification,
                        newNoteAlarmDateField.getText().toString().trim(),
                        newNoteAlarmTimeField.getText().toString().trim(),
                        newNoteAlarmType); // Редактируем существующую запись в БД
            }

            FragmentNotebook.isNoteSaved = true; // Устанавливаем флаг, что заметка сохранена
            finish(); // Закрываем окно
        }
    }

    // Обработчик кнопки Отмена
    public void cancelButtonClick(View view) {
        finish();
    }
}
