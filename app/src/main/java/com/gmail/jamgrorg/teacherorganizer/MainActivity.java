package com.gmail.jamgrorg.teacherorganizer;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gmail.jamgrorg.teacherorganizer.databaseUtils.DatabaseHelper;
import com.gmail.jamgrorg.teacherorganizer.fragments.FragmentInformation;
import com.gmail.jamgrorg.teacherorganizer.fragments.FragmentJournal;
import com.gmail.jamgrorg.teacherorganizer.fragments.FragmentNotebook;
import com.gmail.jamgrorg.teacherorganizer.fragments.FragmentTimetable_of_classes;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Объявление фрагментов панели навигации
    private static FragmentNotebook fragmentNotebook;
    private static FragmentJournal fragmentJournal;
    private static FragmentTimetable_of_classes fragmentTimetable_of_classes;
    private static FragmentInformation fragmentInformation;
    public static FloatingActionButton fabAdd = null;
    public static FloatingActionButton fabShowWholeJournal = null;
    public static DatabaseHelper databaseHelper = null;
    public static SQLiteDatabase sqLiteDatabase = null;
    private static int startCount = 1;
    public static  android.support.v7.widget.SearchView searchView;
    public static MaterialCalendarView ttocCalendarWidget;
    public static String currFragment = "";
    private static MenuItem menuItemSettings;
    private static boolean doubleBackToExitPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ttocCalendarWidget = (MaterialCalendarView) findViewById(R.id.ttoc_calendar_view);

        try {
            ttocCalendarWidget.setLeftArrowMask(null);
        } catch (NullPointerException e) {}
        ttocCalendarWidget.setRightArrowMask(null);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        try {
            drawer.setDrawerListener(toggle);
        } catch (NullPointerException e) {}
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        try {
            navigationView.setNavigationItemSelectedListener(this);
        } catch (NullPointerException e) {}

        // Инициализация фрагментов панели навигации
        fragmentNotebook = new FragmentNotebook();
        fragmentJournal = new FragmentJournal();
        fragmentTimetable_of_classes = new FragmentTimetable_of_classes();
        fragmentInformation = new FragmentInformation();


        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        try {
            fabAdd.setVisibility(View.INVISIBLE);
        } catch (NullPointerException e) {}
        fabShowWholeJournal = (FloatingActionButton) findViewById(R.id.fabShowWholeJournal);
        try {
            fabShowWholeJournal.setVisibility(View.INVISIBLE);
        } catch (NullPointerException e) {}

        databaseHelper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, 1);
        sqLiteDatabase = databaseHelper.openDatabase();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressed) {
                super.onBackPressed();
                startCount = 1;
            }
            doubleBackToExitPressed = true;
            Toast.makeText(getApplicationContext(),
                    R.string.application_exit_confirmation, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressed = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (!drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.openDrawer(GravityCompat.START);
                } else {
                    drawer.closeDrawer(GravityCompat.START);
                }
                break;
            default:
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItemCalendar = menu.findItem(R.id.ttoc_actionbar_calendar_button);
        MenuItem menuItemSearch = menu.findItem(R.id.note_menu_search);
        menuItemSettings = menu.findItem(R.id.action_settings);

        switch (currFragment) {
            case "informationFragment":
                menuItemCalendar.setVisible(false);
                menuItemSearch.setVisible(false);
                break;
            case "notebookFragment":
                menuItemCalendar.setVisible(false);
                menuItemSearch.setVisible(true);
                break;
            case "ttocFragment":
                menuItemSearch.setVisible(false);
                break;
            case "journalFragment":
                menuItemCalendar.setVisible(false);
                menuItemSearch.setVisible(false);
                break;
            default:
                break;
        }

        searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.note_menu_search).getActionView();

        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint(getResources().getString(R.string.note_menu_search_hint));
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fragmentNotebook.searchNoteRecord(newText);
                return false;
            }
        });

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
            case R.id.action_settings:
                Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.ttoc_actionbar_calendar_button:
                ttocCalendarWidget.setCurrentDate(Calendar.getInstance().getTime());
                ttocCalendarWidget.setSelectedDate(Calendar.getInstance().getTime());
                // !!!! <----
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Создание объекта транзакции фрагментов панели навигации
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_info) {
            // Запуск интента с иняормацией
            fragmentTransaction.replace(R.id.fragment_container, fragmentInformation);
            setTitle(getResources().getString(R.string.app_name));

        } else if (id == R.id.nav_notebook) {
            // Запуск интента записной книжки
            fragmentTransaction.replace(R.id.fragment_container, fragmentNotebook);
            setTitle(getResources().getString(R.string.nav_notebook));

        } else if (id == R.id.nav_journal) {
            // Запуск интента журнала
            fragmentTransaction.replace(R.id.fragment_container, fragmentJournal);
            setTitle(getResources().getString(R.string.nav_journal));

        } else if (id == R.id.nav_timetable_of_classes) {
            fragmentTransaction.replace(R.id.fragment_container, fragmentTimetable_of_classes);
            setTitle(getResources().getString(R.string.nav_timetable_of_classes));

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_data_management) {
            Intent intent = new Intent(this, DataManagementActivity.class);
            startActivity(intent);

        }

        // Запуск фрагментов
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        try {
            drawer.closeDrawer(GravityCompat.START);
        } catch (NullPointerException e) {}

        return true;
    }

    @Override
    protected void onStart() {
        if (startCount == 1) { // Если активити впервые создана
            startCount++;

            android.support.v4.app.FragmentTransaction fragmentTransaction1 =
                    getSupportFragmentManager().beginTransaction(); // Показываем фрагмент с инфой
            fragmentTransaction1.replace(R.id.fragment_container, fragmentInformation);
            setTitle(getResources().getString(R.string.app_name));
            fragmentTransaction1.commit();
        }
        super.onStart();
    }

    @Override
    protected void onResume() {
        ttocCalendarWidget.setTileWidth(getWindowManager().getDefaultDisplay().getWidth()/7);
        ttocCalendarWidget.setTileHeight(getWindowManager().getDefaultDisplay().getHeight()/14);

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.closeDatabase(sqLiteDatabase);
    }
}
