package no.hiof.andrekar.badhabits;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.FirebaseDatabase;

import model.SaveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import model.DateHabit;
import model.EconomicHabit;
import model.Habit;

public class MainActivity extends AppCompatActivity {


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Habit.habits.clear();
        SaveData saveData = new SaveData();
        saveData.readFromFile();





        //code to ask user for permission to store data.
        int REQUEST_CODE=1;
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_CODE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_addHabit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), HabitActivity.class);
                startActivity(intent);
            }
        });

        //TODO: Implement this into habits model?
        Collections.sort(Habit.habits, Habit.HabitComparator);


        initRecyclerView();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.t
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
            startActivity(intent);

//            Snackbar.make(findViewById(android.R.id.content), "Not yet implemented", Snackbar.LENGTH_LONG).show();
//            return true;
        }
        if (id == R.id.action_populate) {
            populateData();
            return true;
        }
        if (id == R.id.action_remove) {
            removeData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView(){

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        MyAdapter adapter = new MyAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void populateData() {
        ArrayList<Habit> testHabits = new ArrayList<Habit>();
        Habit gumHabit = new EconomicHabit("Gum", "Stop with gum", new Date().getTime(), "kr", 10, 100, 10, false);
        Habit sodaHabit = new DateHabit("Soda", "Stop drinking soda", new Date().getTime(), 10, true);
        Habit poop = new EconomicHabit("Smokes", "Stop with smoking", new Date().getTime(), "kr", 10, 100, 10, false);
        Habit scoop = new DateHabit("Having fun", "Stop having fun", new Date().getTime(), 10, false);
        gumHabit.setFavourite(true);
        scoop.setFavourite(true);

        testHabits.add((EconomicHabit) gumHabit);
        testHabits.add((DateHabit) sodaHabit);
        testHabits.add((EconomicHabit) poop);
        testHabits.add((DateHabit) scoop);
        SaveData saveData = new SaveData();

        for (Habit habit : testHabits) {
            if (habit instanceof DateHabit) {
                saveData.saveToFile(habit, 2);
            } else if (habit instanceof EconomicHabit) {
                saveData.saveToFile(habit, 1);
            }
        }
        testHabits.clear();
        initRecyclerView();
        Collections.sort(Habit.habits, Habit.HabitComparator);
        }

        private void removeData() {
            Habit.habits.clear();
            SaveData saveData = new SaveData();
            saveData.updateData(1);
            saveData.updateData(2);
            initRecyclerView();
        }
}

