package com.gmail.jamgrorg.teacherorganizer.databaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.gmail.jamgrorg.teacherorganizer.MainActivity;

/**
 * Created by jamgr on 21.04.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;
    // Таблицы
    private static final String NOTES_TABLE = "notes";
    private static final String TIMETABLES_OF_CLASSES_TABLE = "timetable_of_classes";
    public static final String LESSON_TYPE_TABLE = "lesson_type";
    public static final String SUBJECTS_TABLE = "subjects";
    public static final String STUDENTS_TABLE = "students";
    private static final String JOURNAL_TABLE = "journal";
    // Структура таблицы notes
    public static final String TITLE_NOTE_COLUMN = "note_title";
    public static final String BODY_NOTE_COLUMN = "note_body";
    public static final String IS_NOTIFICATION_NOTE_COLUMN = "is_notification";
    public static final String ALARM_DATE_NOTE_COLUMN = "alarm_date";
    public static final String ALARM_TIME_NOTE_COLUMN = "alarm_time";
    public static final String ALARM_TYPE_NOTE_COLUMN = "alarm_type";
    // Структура таблицы timetable_of_classes
    public static final String DATE_TTOC_COLUMN = "date";
    public static final String NUM_TTOC_COLUMN = "num";
    public static final String START_TIME_TTOC_COLUMN = "start_time";
    public static final String END_TIME_TTOC_COLUMN = "end_time";
    public static final String TYPE_TTOC_COLUMN = "type";
    public static final String SUBJECT_TTOC_COLUMN = "subject";
    public static final String GROUP_TTOC_COLUMN = "students_group";
    public static final String LECTURE_HALL_TTOC_COLUMN = "lecture_hall";
    // Структура таблицы lesson_type
    public static final String TITLE_LESSON_TYPE_COLUMN = "title";
    // Структура таблицы subjects
    public static final String TITLE_SUBJECTS_COLUMN = "title";
    // Структура тиблицы students
    public static final String SNAME_STUDENTS_COLUMN = "second_name";
    public static final String FNAME_STUDENTS_COLUMN = "first_name";
    public static final String SURNAME_STUDENTS_COLUMN = "surname";
    public static final String GROUP_STUDENTS_COLUMN = "students_group";
    // Структура тиблицы journal
    private static final String DATE_JOURNAL_COLUMN = "date";
    private static final String LESSON_TYPE_JOURNAL_COLUMN = "lesson_type";
    private static final String GROUP_JOURNAL_COLUMN = "students_group";
    private static final String SUBJECT_JOURNAL_COLUMN = "subject";
    private static final String STUDENT_JOURNAL_COLUMN = "student";
    private static final String MARK_JOURNAL_COLUMN = "mark";

    private static final String[] allTablesList = new String[] {NOTES_TABLE,
            TIMETABLES_OF_CLASSES_TABLE,LESSON_TYPE_TABLE, SUBJECTS_TABLE,
            STUDENTS_TABLE, JOURNAL_TABLE};

    private static final String CREATE_JOURNAL_TABLE_QUERY = "create table "
            + JOURNAL_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, "
            + DATE_JOURNAL_COLUMN + " text not null, "
            + LESSON_TYPE_JOURNAL_COLUMN + " text not null, "
            + GROUP_JOURNAL_COLUMN + " text not null, "
            + SUBJECT_JOURNAL_COLUMN + " text not null, "
            + STUDENT_JOURNAL_COLUMN + " text not null, "
            + MARK_JOURNAL_COLUMN + " text not null);";

    private static final String CREATE_STUDENTS_TABLE_QUERY = "create table "
            + STUDENTS_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, "
            + SNAME_STUDENTS_COLUMN + " text not null, "
            + FNAME_STUDENTS_COLUMN + " text not null, "
            + SURNAME_STUDENTS_COLUMN + " text not null, "
            + GROUP_STUDENTS_COLUMN + " text not null);";

    private static final String CREATE_SUBJECTS_TABLE_QUERY = "create table "
            + SUBJECTS_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, "
            + TITLE_SUBJECTS_COLUMN + " text not null);";

    private static final String CREATE_LESSON_TYPE_TABLE_QUERY = "create table "
            + LESSON_TYPE_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, "
            + TITLE_LESSON_TYPE_COLUMN + " text not null);";

    private static final String CREATE_NOTES_TABLE_QUERY = "create table "
            + NOTES_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + TITLE_NOTE_COLUMN
            + " text not null, " + BODY_NOTE_COLUMN + " text, "
            + IS_NOTIFICATION_NOTE_COLUMN + " integer not null, "
            + ALARM_DATE_NOTE_COLUMN + " text, "
            + ALARM_TIME_NOTE_COLUMN + " text, "
            + ALARM_TYPE_NOTE_COLUMN + " integer not null);";

    private static final String CREATE_TTOC_TABLE_QUERY = "create table "
            + TIMETABLES_OF_CLASSES_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + DATE_TTOC_COLUMN
            + " text not null, " + NUM_TTOC_COLUMN + " integer not null, "
            + START_TIME_TTOC_COLUMN + " text not null, " + END_TIME_TTOC_COLUMN
            + " text not null, " + TYPE_TTOC_COLUMN + " text not null, "
            + SUBJECT_TTOC_COLUMN + " text not null, " + GROUP_TTOC_COLUMN
            + " text not null, " + LECTURE_HALL_TTOC_COLUMN + " text not null);";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    private void createTables(SQLiteDatabase db, String[] createTablesQuerys) {
        for (String s : createTablesQuerys) {
            db.execSQL(s);
        }
    }

    private void deleteTables(SQLiteDatabase db, String[] tables) {
        for (String s : tables) {
            db.execSQL("DROP TABLE IF EXISTS " + s + ";");
        }
    }

    public void clearDatabase(SQLiteDatabase db) {
        for (String s : allTablesList) {
            db.delete(s, null, null);
        }
    }

    public SQLiteDatabase openDatabase() {
        return this.getWritableDatabase();
    }

    public void closeDatabase(SQLiteDatabase sqlDb) {
        sqlDb.close();
        this.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db, new String[]{CREATE_NOTES_TABLE_QUERY, CREATE_TTOC_TABLE_QUERY,
                CREATE_LESSON_TYPE_TABLE_QUERY, CREATE_SUBJECTS_TABLE_QUERY,
                CREATE_STUDENTS_TABLE_QUERY, CREATE_JOURNAL_TABLE_QUERY});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTables(db, allTablesList);
        onCreate(db);
    }

    // Запросы к таблице notes
    public Cursor getAllNoteRecords() {
        return MainActivity.sqLiteDatabase.query(NOTES_TABLE, null, null, null, null, null, null);
    }

    public Cursor getLastThreeNote() {
        return MainActivity.sqLiteDatabase.query(false, NOTES_TABLE, null,
                null, null, null, null, _ID + " DESC", "3");
    }

    public void deleteNoteRecord(long id) {
        MainActivity.sqLiteDatabase.delete(NOTES_TABLE, _ID + " = " + id, null);
    }

    public void updateNoteRecord(long note_id, String note_title, String note_body,
                                 int is_notification, String alarm_date,
                                 String alarm_time, int alarm_type) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE_NOTE_COLUMN, note_title);
        contentValues.put(BODY_NOTE_COLUMN, note_body);
        contentValues.put(IS_NOTIFICATION_NOTE_COLUMN, is_notification);
        contentValues.put(ALARM_DATE_NOTE_COLUMN, alarm_date);
        contentValues.put(ALARM_TIME_NOTE_COLUMN, alarm_time);
        contentValues.put(ALARM_TYPE_NOTE_COLUMN, alarm_type);
        MainActivity.sqLiteDatabase.update(NOTES_TABLE,
                contentValues, _ID + " = " + note_id, null);
    }

    public void insertNoteRecord(String note_title, String note_body, int is_notification,
                                 String alarm_date, String alarm_time, int alarm_type) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE_NOTE_COLUMN, note_title);
        contentValues.put(BODY_NOTE_COLUMN, note_body);
        contentValues.put(IS_NOTIFICATION_NOTE_COLUMN, is_notification);
        contentValues.put(ALARM_DATE_NOTE_COLUMN, alarm_date);
        contentValues.put(ALARM_TIME_NOTE_COLUMN, alarm_time);
        contentValues.put(ALARM_TYPE_NOTE_COLUMN, alarm_type);
        MainActivity.sqLiteDatabase.insert(NOTES_TABLE, null, contentValues);
    }

    public Cursor getNotesRecordByText(String searchText) {
        return MainActivity.sqLiteDatabase.query(NOTES_TABLE, null, TITLE_NOTE_COLUMN + " like '%"
                + searchText + "%' or " + BODY_NOTE_COLUMN + " like '%" + searchText + "%'", null,
                null, null, null, null);
    }

    // Запросы к таблице timetables_of_classes
    public Cursor getAllTTOCRecords() {
        return MainActivity.sqLiteDatabase.query(TIMETABLES_OF_CLASSES_TABLE, null, null, null,
                null, null, null);
    }

    public Cursor getTTOCRecordByDate(String ttoc_date) {
        return MainActivity.sqLiteDatabase.query(TIMETABLES_OF_CLASSES_TABLE, null,
                DATE_TTOC_COLUMN + " like '" + ttoc_date + "'", null,
                null, null, null);
    }

    public void insertTTOCRecord(String date, int num, String startTime, String endTime, String type,
                                 String subject, String group, String lecture_hall) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE_TTOC_COLUMN, date);
        contentValues.put(NUM_TTOC_COLUMN, num);
        contentValues.put(START_TIME_TTOC_COLUMN, startTime);
        contentValues.put(END_TIME_TTOC_COLUMN, endTime);
        contentValues.put(TYPE_TTOC_COLUMN, type);
        contentValues.put(SUBJECT_TTOC_COLUMN, subject);
        contentValues.put(GROUP_TTOC_COLUMN, group);
        contentValues.put(LECTURE_HALL_TTOC_COLUMN, lecture_hall);
        MainActivity.sqLiteDatabase.insert(TIMETABLES_OF_CLASSES_TABLE, null, contentValues);
    }

    public void updateTTOCRecord(long ttoc_id, String date, String startTime, String endTime,
                                 String type, String subject, String group, String lecture_hall) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE_TTOC_COLUMN, date);
        contentValues.put(START_TIME_TTOC_COLUMN, startTime);
        contentValues.put(END_TIME_TTOC_COLUMN, endTime);
        contentValues.put(TYPE_TTOC_COLUMN, type);
        contentValues.put(SUBJECT_TTOC_COLUMN, subject);
        contentValues.put(GROUP_TTOC_COLUMN, group);
        contentValues.put(LECTURE_HALL_TTOC_COLUMN, lecture_hall);
        MainActivity.sqLiteDatabase.update(TIMETABLES_OF_CLASSES_TABLE,
                contentValues, _ID + " = " + ttoc_id, null);
    }

    public void deleteTTOCRecord(long id) {
        MainActivity.sqLiteDatabase.delete(TIMETABLES_OF_CLASSES_TABLE, _ID + " = " + id, null);
    }

    // Запросы к таблице lesson_type
    public void insertLessonTypeRecord(String lessonTypeTitle) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE_LESSON_TYPE_COLUMN, lessonTypeTitle);
        MainActivity.sqLiteDatabase.insert(LESSON_TYPE_TABLE, null, contentValues);
    }

    public void updateLessonTypeRecord(long id, String lessonTypeTitle) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE_LESSON_TYPE_COLUMN, lessonTypeTitle);
        MainActivity.sqLiteDatabase.update(LESSON_TYPE_TABLE, contentValues,
                _ID + " = " + id, null);
    }

    public void deleteLessonTypeRecord(long id) {
        MainActivity.sqLiteDatabase.delete(LESSON_TYPE_TABLE, _ID + " = " + id, null);
    }

    public Cursor getAllLessonTypeRecords() {
        return MainActivity.sqLiteDatabase.query(LESSON_TYPE_TABLE,
                null, null, null,null, null, null);
    }

    public Cursor getLessonTypeRecordById(long id) {
        return MainActivity.sqLiteDatabase.query(LESSON_TYPE_TABLE,
                null, _ID + " = " + id, null,null, null, null);
    }

    // Запросы к таблице subjects
    public void insertSubjectsRecord(String subjectsTitle) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE_SUBJECTS_COLUMN, subjectsTitle);
        MainActivity.sqLiteDatabase.insert(SUBJECTS_TABLE, null, contentValues);
    }

    public void updateSubjectsRecord(long id, String subjectsTitle) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE_SUBJECTS_COLUMN, subjectsTitle);
        MainActivity.sqLiteDatabase.update(SUBJECTS_TABLE, contentValues,
                _ID + " = " + id, null);
    }

    public void deleteSubjectsRecord(long id) {
        MainActivity.sqLiteDatabase.delete(SUBJECTS_TABLE, _ID + " = " + id, null);
    }

    public Cursor getAllSubjectsRecords() {
        return MainActivity.sqLiteDatabase.query(SUBJECTS_TABLE,
                null, null, null,null, null, null);
    }

    public Cursor getSubjectsRecordById(long id) {
        return MainActivity.sqLiteDatabase.query(SUBJECTS_TABLE,
                null, _ID + " = " + id, null,null, null, null);
    }

    // Запросы к таблице students
    public void insertStudentsRecord(String sName, String fName, String surname, String group) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SNAME_STUDENTS_COLUMN, sName);
        contentValues.put(FNAME_STUDENTS_COLUMN, fName);
        contentValues.put(SURNAME_STUDENTS_COLUMN, surname);
        contentValues.put(GROUP_STUDENTS_COLUMN, group);
        MainActivity.sqLiteDatabase.insert(STUDENTS_TABLE, null, contentValues);
    }

    public void updateStudentsRecord(long id, String sName, String fName, String surname, String group) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SNAME_STUDENTS_COLUMN, sName);
        contentValues.put(FNAME_STUDENTS_COLUMN, fName);
        contentValues.put(SURNAME_STUDENTS_COLUMN, surname);
        contentValues.put(GROUP_STUDENTS_COLUMN, group);
        MainActivity.sqLiteDatabase.update(STUDENTS_TABLE, contentValues,
                _ID + " = " + id, null);
    }

    public void deleteStudentsRecord(long id) {
        MainActivity.sqLiteDatabase.delete(STUDENTS_TABLE, _ID + " = " + id, null);
    }

    public Cursor getAllStudentsRecords() {
        return MainActivity.sqLiteDatabase.query(STUDENTS_TABLE,
                null, null, null,null, null, null);
    }

    public Cursor getStudentsRecordsByGroup(String group) {
        return MainActivity.sqLiteDatabase.query(STUDENTS_TABLE,
                null, GROUP_STUDENTS_COLUMN + " like '" + group +"'", null, null, null, null);
    }

    public Cursor getAllGroupsRecords() {
        return MainActivity.sqLiteDatabase.query(true, STUDENTS_TABLE,
                null, null, null, GROUP_STUDENTS_COLUMN, null, null, null);
    }

    public Cursor getGroupsRecordsById(long id) {
        return MainActivity.sqLiteDatabase.query(true, STUDENTS_TABLE,
                null, _ID + " = " + id, null, GROUP_STUDENTS_COLUMN, null, null, null);
    }

    public void deleteStudentsByGroup(String group) {
        MainActivity.sqLiteDatabase.delete(STUDENTS_TABLE,
                GROUP_STUDENTS_COLUMN + " like '" + group + "'", null);
    }

    public void insertRecord(String table, String[] fields, String[] values) {
        ContentValues contentValues = new ContentValues();
        for (int colls = 0; colls < fields.length; colls++) {
            contentValues.put(fields[colls], values[colls]);
        }
        MainActivity.sqLiteDatabase.insert(table, null, contentValues);
    }

    // Запросы к таблице journal
    public void updateJournalRecord(String date, String lesson_type, String group, String subject, String student, String mark) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE_JOURNAL_COLUMN, date);
        contentValues.put(LESSON_TYPE_JOURNAL_COLUMN, lesson_type);
        contentValues.put(GROUP_JOURNAL_COLUMN, group);
        contentValues.put(SUBJECT_JOURNAL_COLUMN, subject);
        contentValues.put(STUDENT_JOURNAL_COLUMN, student);
        contentValues.put(MARK_JOURNAL_COLUMN, mark);
        MainActivity.sqLiteDatabase.update(JOURNAL_TABLE, contentValues,
                DATE_JOURNAL_COLUMN + " like '" + date + "' and " +
                LESSON_TYPE_JOURNAL_COLUMN + " like '" + lesson_type + "' and " +
                GROUP_JOURNAL_COLUMN + " like '" + group + "' and " +
                SUBJECT_JOURNAL_COLUMN + " like '" + subject + "' and " +
                STUDENT_JOURNAL_COLUMN + " like '" + student + "'", null);
    }

    public void insertJournalRecord(String date, String lesson_type, String group, String subject, String student, String mark) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE_JOURNAL_COLUMN, date);
        contentValues.put(LESSON_TYPE_JOURNAL_COLUMN, lesson_type);
        contentValues.put(GROUP_JOURNAL_COLUMN, group);
        contentValues.put(SUBJECT_JOURNAL_COLUMN, subject);
        contentValues.put(STUDENT_JOURNAL_COLUMN, student);
        contentValues.put(MARK_JOURNAL_COLUMN, mark);
        MainActivity.sqLiteDatabase.insert(JOURNAL_TABLE, null, contentValues);
    }

    public Cursor getJournalDatesRecords(String lessonType, String group, String subject) {
        return MainActivity.sqLiteDatabase.query(true, JOURNAL_TABLE, null,
                LESSON_TYPE_JOURNAL_COLUMN + " like '" + lessonType + "' and " +
                GROUP_JOURNAL_COLUMN + " like '" + group + "' and " +
                SUBJECT_JOURNAL_COLUMN + " like '" + subject + "'",
                null, DATE_JOURNAL_COLUMN, null, null, null);
    }

    public Cursor getJournalMarksRecords(String date, String lessonType, String group, String subject, String student) {
        return MainActivity.sqLiteDatabase.query(JOURNAL_TABLE, null,
                DATE_JOURNAL_COLUMN + " like '" + date + "' and " +
                LESSON_TYPE_JOURNAL_COLUMN + " like '" + lessonType + "' and " +
                GROUP_JOURNAL_COLUMN + " like '" + group + "' and " +
                SUBJECT_JOURNAL_COLUMN + " like '" + subject + "' and " +
                STUDENT_JOURNAL_COLUMN + " like '" + student + "'", null, null, null, null);
    }

    public Cursor getJournalStudentMarks(String group, String subject, String student) {
        return MainActivity.sqLiteDatabase.query(JOURNAL_TABLE, new String[] {MARK_JOURNAL_COLUMN},
                GROUP_JOURNAL_COLUMN + " like '" + group + "' and " +
                SUBJECT_JOURNAL_COLUMN + " like '" + subject + "' and " +
                STUDENT_JOURNAL_COLUMN + " like '" + student + "'", null, null, null, null);
    }

    public void deleteJournalRecord(String date, String lessonType, String group, String subject, String student) {
        MainActivity.sqLiteDatabase.delete(JOURNAL_TABLE,
                DATE_JOURNAL_COLUMN + " like '" + date + "' and " +
                LESSON_TYPE_JOURNAL_COLUMN + " like '" + lessonType + "' and " +
                GROUP_JOURNAL_COLUMN + " like '" + group + "' and " +
                SUBJECT_JOURNAL_COLUMN + " like '" + subject + "' and " +
                STUDENT_JOURNAL_COLUMN + " like '" + student + "'", null);
    }
}
