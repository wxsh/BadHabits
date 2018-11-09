package no.hiof.andrekar.badhabits;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import model.DateHabit;
import model.EconomicHabit;
import model.Habit;
import model.SaveData;

public class ShowHabitActivity extends AppCompatActivity {

    public static int currentNumber;
    private String failedAmount = "";
    public ImageButton deleteButton;
    public ImageButton editButton;
    public ImageButton failedButton;
    HorizontalBarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_habit);
        EconomicHabit ecohabit;
        DateHabit dateHabit;


        TextView goalView = findViewById(R.id.getGoalTextView);
        TextView progressView = findViewById(R.id.getProgressTextView);
        TextView startView = findViewById(R.id.getStartTextView);
        TextView failedView = findViewById(R.id.getFailTextView);
        chart = findViewById(R.id.detailChart);

        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        Habit habit = Habit.habits.get(currentNumber);

        if (habit instanceof DateHabit) {
            goalView.setText(((DateHabit)habit).getDateGoal());
            progressView.setText(((DateHabit)habit).getDaysSinceStart());
            //temp code?
            Date date=new Date(habit.getStartDate());
            String dateText = df2.format(date);
            startView.setText(dateText);
        }
        else {
            TextView progressText = findViewById(R.id.progressTextView);
            progressText.setText("Progress:");
            goalView.setText(String.valueOf(((EconomicHabit) habit).getGoalValue()));
            progressView.setText(((EconomicHabit) habit).getProgress());
            Date date=new Date(habit.getStartDate());
            String dateText = df2.format(date);
            startView.setText(dateText);
            setEcoData();
        }

        if (habit.getFailDate() == 0) {
            TextView failText = findViewById(R.id.failTextView);
            failedView.setVisibility(View.GONE);
            failText.setVisibility(View.GONE);
        } else {
            //DONE: Format this as "Days since last fail, maybe?"
            failedView.setText(Long.toString(Habit.getDateDiff(habit.getFailDate(), new Date().getTime(), TimeUnit.DAYS)));
        }

        TextView titleView = findViewById(R.id.habitTitleTextView);
        titleView.setText(Habit.habits.get(currentNumber).getTitle());


        TextView descriptionView = findViewById(R.id.getDescriptionTextView);
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
                //DONE: editButton onclick
                Intent intent = new Intent(getBaseContext(), HabitActivity.class);
                intent.putExtra("TITLE", "Editing: " + Habit.habits.get(currentNumber).getTitle());
                intent.putExtra("CURRENT_HABIT_INDEX", currentNumber);
                startActivity(intent);
                //Snackbar.make(findViewById(android.R.id.content), "Not yet implemented", Snackbar.LENGTH_LONG).show();
            }
        });

        failedButton = findViewById(R.id.btn_habitFailed);
        failedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Habit habit = Habit.habits.get(currentNumber);

                if (habit instanceof EconomicHabit) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowHabitActivity.this);
                    builder.setTitle("Don't worry, even if you fail, you can still do this! How much did you spend?");
                    final EditText input = new EditText(ShowHabitActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SaveData saveData = new SaveData();
                            failedAmount = input.getText().toString();
                            ((EconomicHabit) habit).increaseFailedTotal(Integer.parseInt(failedAmount));
                            habit.setFailDate(new Date().getTime());
                            saveData.saveData(Habit.habits.get(currentNumber), 1);
                            recreate();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog ecoAlert = builder.create();
                    ecoAlert.show();

                } else if (habit instanceof DateHabit) {


                    //DONE: editButton onclick
                    final AlertDialog.Builder failedBuilder = new AlertDialog.Builder(ShowHabitActivity.this);
                    failedBuilder.setMessage("Don't worry, even if you fail, you can still do this! Do you want to reset the days since last fail?").setTitle("Failed habit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Date currentTime = Calendar.getInstance().getTime();
                            habit.setFailDate(currentTime.getTime());
                            SaveData saveData = new SaveData();
                            saveData.saveData(Habit.habits.get(currentNumber), 2);

                            recreate();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // CANCEL AND DO NOTHING
                        }
                    });
                    // Create the AlertDialog object and return it
                    AlertDialog dialog = failedBuilder.create();
                    //ecoAlert.show();
                    dialog.show();
                    //dialog.show();
                }
            }
        });
    }

    private void setEcoData() {

        float barWidth = 9f;
        float spaceForBar = 10f;
        ArrayList<BarEntry> values = new ArrayList<>();
        ArrayList<BarEntry> values2 = new ArrayList<>();

        EconomicHabit habit = ((EconomicHabit) Habit.habits.get(currentNumber));

        values.add(new BarEntry(0 * spaceForBar, habit.getAlternativePrice() * habit.getDaysFromStart(),
                getResources().getDrawable(R.drawable.star_on)));
        values2.add(new BarEntry(1 * spaceForBar, habit.getPrice() * habit.getDaysFromStart(),
                getResources().getDrawable(R.drawable.star_on)));


        BarDataSet set1;
        BarDataSet set2;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) chart.getData().getDataSetByIndex(1);
            set1.setValues(values);
            set2.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "The alternative have cost");
            set2 = new BarDataSet(values2, "Would have used");

            set1.setDrawIcons(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            dataSets.add(set2);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            //data.setValueTypeface();
            data.setBarWidth(barWidth);
            chart.setData(data);
        }
    }

    public static void setCurrentNumber(int number) {
        currentNumber = number;
    }
}
