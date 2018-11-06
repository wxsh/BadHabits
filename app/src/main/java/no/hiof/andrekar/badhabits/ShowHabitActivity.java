package no.hiof.andrekar.badhabits;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import model.DateHabit;
import model.EconomicHabit;
import model.Habit;
import model.SaveData;

public class ShowHabitActivity extends AppCompatActivity {

    public static int currentNumber;
    public ImageButton deleteButton;
    public ImageButton editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_habit);
        EconomicHabit ecohabit;
        DateHabit dateHabit;


        TextView goalView = findViewById(R.id.goalTextView);

        if (Habit.habits.get(currentNumber).getClass() == DateHabit.class){
            dateHabit = (DateHabit) Habit.habits.get(currentNumber);
            goalView.setText(dateHabit.getDateGoal().toString());
        }
        else {
            ecohabit = (EconomicHabit) Habit.habits.get(currentNumber);
            goalView.setText(String.valueOf(ecohabit.getGoalValue()));
        }

        TextView titleView = findViewById(R.id.habitTitleTextView);
        titleView.setText(Habit.habits.get(currentNumber).getTitle());

        TextView descriptionView = findViewById(R.id.descriptionTextView);
        descriptionView.setText(Habit.habits.get(currentNumber).getDescription());


        deleteButton = findViewById(R.id.btn_habitDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowHabitActivity.this);
                builder.setMessage("This can not be reversed!")
                        .setTitle("Delete habit?")
                        .setPositiveButton("Yes, I Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //DONE can this be done without looping through all habits?
                                Habit habit = Habit.habits.get(currentNumber);
                                SaveData saveData = new SaveData();
                                //DONE: Fix deletion after implementing firebase. We can null out habit to firebase.
                                if (habit instanceof EconomicHabit) {
                                    saveData.removeData(Habit.habits.get(currentNumber), 1);
                                    Habit.habits.remove(currentNumber);
                                } else if (habit instanceof DateHabit) {
                                    saveData.removeData(Habit.habits.get(currentNumber), 2);
                                    Habit.habits.remove(currentNumber);
                                }


                                Intent intent = new Intent(ShowHabitActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // CANCEL AND DO NOTHING
                            }
                        });
                // Create the AlertDialog object and return it
                AlertDialog dialog = builder.create();
                dialog.show();
           }
        });

        editButton = findViewById(R.id.btn_habitEdit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: editButton onclick
                Intent intent = new Intent(getBaseContext(), HabitActivity.class);
                intent.putExtra("TITLE", "Add new habit");
                intent.putExtra("CURRENT_HABIT_INDEX", currentNumber);
                startActivity(intent);
                Snackbar.make(findViewById(android.R.id.content), "Not yet implemented", Snackbar.LENGTH_LONG).show();
            }
        });

    }


    public static void setCurrentNumber(int number){
        currentNumber = number;
    }
}
