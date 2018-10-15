package no.hiof.andrekar.badhabits;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import model.Habit;
import model.SaveData;

public class HabitActivity extends AppCompatActivity {

    //DONE 01: https://developer.android.com/guide/topics/ui/controls/pickers#java - Time/date picker for date field
    //TODO 02: Cast Date field, or change field to accept string / Integers?
    //TODO 03: Save data
    //TODO 04: Logic to dynamically change fields to reflect type of habit

    private String title;
    private String description;
    private Date startDate;
    private EditText dateEditText;
    Calendar calendarPick = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
        EditText dateEditText = (EditText) findViewById(R.id.newHabit_startDate);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.newHabit_saveFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
                title = getText(R.id.newHabit_name).toString();
                description = getText(R.id.newHabit_description).toString();

                try {
                    startDate = format.parse(getText(R.id.newHabit_startDate).toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Habit habit = new Habit(title, description, startDate);
                //testhabit
                Habit habit2 = new Habit("a","test",new Date());


                SaveData saveData = new SaveData();
                saveData.saveToFile(habit,"testers.txt");
                saveData.saveToFile(habit2,"testers.txt");
                Snackbar.make(v, "Not yet implemented", Snackbar.LENGTH_LONG).show();
            }
        });

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(HabitActivity.this, date, calendarPick
                        .get(Calendar.YEAR), calendarPick.get(Calendar.MONTH),
                        calendarPick.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendarPick.set(Calendar.YEAR, year);
            calendarPick.set(Calendar.MONTH, month);
            calendarPick.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };


    private void updateLabel() {
        dateEditText = (EditText) findViewById(R.id.newHabit_startDate);
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        dateEditText.setText(sdf.format(calendarPick.getTime()));
    }


}
