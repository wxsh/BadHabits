package no.hiof.andrekar.badhabits;

import android.app.DatePickerDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import model.Habit;

public class HabitActivity extends AppCompatActivity {

    //TODO 01: https://developer.android.com/guide/topics/ui/controls/pickers#java - Time/date picker for date field
    //TODO 02: Cast Date field
    //TODO 03: Save data
    //TODO 04: Logic to dynamically change fields to reflect type of habit

    private String title;
    private String description;
    private Date startDate;
    private EditText dateEditText;
    Calendar calendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
        EditText dateEditText = (EditText) findViewById(R.id.newHabit_startDate);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.newHabit_saveFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //title = getText(R.id.newHabit_name);
                //description = getText(R.id.newHabit_description);
                //startDate = getText(R.id.newHabit_startDate);
                Habit habit = new Habit(title, description, startDate);
            }
        });

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(HabitActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };


    private void updateLabel() {
        dateEditText = (EditText) findViewById(R.id.newHabit_startDate);
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateEditText.setText(sdf.format(calendar.getTime()));
    }


}
