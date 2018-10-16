package no.hiof.andrekar.badhabits;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import model.Habit;

public class ShowHabitActivity extends AppCompatActivity {

    public static int currentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_habit);

        TextView titleView = findViewById(R.id.habitTitleTextView);
        titleView.setText(Habit.habits.get(currentNumber).getTitle());

        TextView descriptionView = findViewById(R.id.descriptionTextView);
        descriptionView.setText(Habit.habits.get(currentNumber).getDescription());

    }


    public static void setCurrentNumber(int number){
        currentNumber = number;
    }
}
