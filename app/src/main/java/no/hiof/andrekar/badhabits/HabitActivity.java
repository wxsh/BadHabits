package no.hiof.andrekar.badhabits;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import model.Habit;

public class HabitActivity extends AppCompatActivity {

    //DONE 01: https://developer.android.com/guide/topics/ui/controls/pickers#java - Time/date picker for date field
    //TODO 02: Cast Date field, or change field to accept string / Integers?
    //TODO 03: Save data
    //TODO 04: Logic to dynamically change fields to reflect type of habit

    private String title;
    private String description;
    private Date startDate;
    private EditText dateEditText;

    private EditText editTitle;
    private EditText editDesc;

    Calendar calendarPick = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
        final EditText dateEditText = findViewById(R.id.newHabit_startDate);

        editTitle = findViewById(R.id.newHabit_name);
        editDesc = findViewById(R.id.newHabit_description);


        FloatingActionButton fab = findViewById(R.id.newHabit_saveFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                title = editTitle.getText().toString();
                description = editDesc.getText().toString();
                startDate = convertToDate(dateEditText.getText().toString());

                Habit habit = new Habit(title, description, startDate);

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);


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


    private Date convertToDate(String dateToConvert) {
        Date convertedDate = new Date();

        try {
            convertedDate = new SimpleDateFormat("dd/MM/yy").parse(dateToConvert);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertedDate;

    }

}
