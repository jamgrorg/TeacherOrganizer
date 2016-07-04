package com.gmail.jamgrorg.teacherorganizer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class SettingsActivity extends AppCompatActivity {

    private static EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        LinearLayout settingsRatingButton = (LinearLayout) findViewById(R.id.settings_student_rating_coefficient_button);

        settingsRatingButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.settings_student_rating_coefficient_layout, null);


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(getResources().getColor(R.color.md_blue_grey_500));
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(getResources().getColor(R.color.md_grey_200));

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                        builder.setView(view);
                        Button coefficientCancelButton = (Button) view.findViewById(R.id.settings_student_rating_coefficient_cancel_button);
                        Button coefficientSaveButton = (Button) view.findViewById(R.id.settings_student_rating_coefficient_save_button);

                        editText = (EditText) view.findViewById(R.id.settings_student_rating_coefficient_field);
                        editText.setText(settings.getString("rating", "0.5"));

                        final AlertDialog alertDialog = builder.create();

                        coefficientCancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.cancel();
                            }
                        });

                        coefficientSaveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("rating", editText.getText().toString().trim());
                                editor.apply();

                                alertDialog.cancel();
                            }
                        });

                        alertDialog.show();
                        break;
                    default:
                        break;
                }

                return true;
            }
        });

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {}
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