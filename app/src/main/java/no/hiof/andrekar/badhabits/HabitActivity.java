package no.hiof.andrekar.badhabits;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Date;

import model.Habit;

public class HabitActivity extends AppCompatActivity {

    //TODO 01: https://developer.android.com/guide/topics/ui/controls/pickers#java - Time/date picker for date field
    //TODO 02: Cast Date field
    //TODO 03: Save data
    //TODO 04: Logic to dynamically change fields to reflect type of habit

    private String title;
    private String description;
    private Date startDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.newHabit_saveFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = getText(R.id.newHabit_name);
                description = getText(R.id.newHabit_description);
                startDate = getText(R.id.newHabit_startDate);
                Habit habit = new Habit(title, description, startDate);
            }
        });

    }
}
