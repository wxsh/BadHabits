package no.hiof.andrekar.badhabits;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import model.DateHabit;
import model.EconomicHabit;
import model.Habit;
import model.SaveData;

public class ShowHabitActivity extends AppCompatActivity {

    public static int currentNumber;
    public Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_habit);
        EconomicHabit ecohabit;
        DateHabit dateHabit;

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
                                //TODO fix this, alternatively we might need a new function in saveData model?
                                Habit habit = Habit.habits.get(currentNumber);

                                if (habit instanceof EconomicHabit) {
                                    Log.d("SHowHABIT", "Found Eco habit");
                                    for (int i = 0; i < EconomicHabit.ecohabits.size(); i++) {
                                        if (habit == EconomicHabit.ecohabits.get(i)) {
                                            Log.d("SHowHABIT", "Found MATCHING eco habit");
                                            EconomicHabit.ecohabits.remove(i);
                                            SaveData saveData = new SaveData();
                                            saveData.updateData(1);
                                        }
                                    }
                                } else if (habit instanceof DateHabit) {
                                    Log.d("SHowHABIT", "Found Date habit");
                                    for (int i = 0; i < DateHabit.dateHabits.size(); i++) {
                                        if (habit == DateHabit.dateHabits.get(i)) {
                                            Log.d("SHowHABIT", "Found MATCHING date habit");
                                            DateHabit.dateHabits.remove(i);
                                            SaveData saveData = new SaveData();
                                            saveData.updateData(2);
                                        }
                                    }
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

    }


    public static void setCurrentNumber(int number){
        currentNumber = number;
    }
}
