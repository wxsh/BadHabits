package no.hiof.andrekar.badhabits;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import model.DateHabit;
import model.EconomicHabit;
import model.Habit;
import model.SaveData;

public class HabitActivity extends AppCompatActivity {

    //DONE 01: https://developer.android.com/guide/topics/ui/controls/pickers#java - Time/date picker for date field
    //DONE 02: Cast Date field, or change field to accept string / Integers?
    //TODO 03: Save data
    //DONE 04: Logic to dynamically change fields to reflect type of habit
    //DONE 05: Make sure fields are filled out
    //NOT_NEEDED 06: Make sure date picker is available for all date fields.

    //TODO: SavedInstanceState on rotate? - Not needed?

    private String title;
    private String description;
    private Date startDate;
    //Title and description
    private EditText editTitle, editDesc, dateEditText;
    private String currency;

    //Date habits
    private EditText dateGoalEditText;
    //Economic Habits
    private EditText economicGoalEditText, economicCurrencyEditText, economicAlternativePriceEditText, economicPriceEditText;
    private float alternativePrice, goalValue, price;

    private RadioGroup typeHabitRG;

    private int dateGoalValue;

    // 1 = Eco, 2 = Date
    private int typeHabit = 0;

    Calendar calendarPick = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
        final EditText dateEditText = findViewById(R.id.newHabit_startDate);

        editTitle = findViewById(R.id.newHabit_name);
        editDesc = findViewById(R.id.newHabit_description);
        typeHabitRG = findViewById(R.id.radiogroup_typeHabit);

        //Extra UI items
        dateGoalEditText = findViewById(R.id.newHabit_dateHabit_goal);
        economicCurrencyEditText = findViewById(R.id.newHabit_economicHabit_currency);
        economicGoalEditText = findViewById(R.id.newHabit_economicHabit_goal);
        economicAlternativePriceEditText = findViewById(R.id.newHabit_economicHabit_alternativePrice);
        economicPriceEditText = findViewById(R.id.newHabit_economicHabit_price);

        updateUI(typeHabit);

        typeHabitRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //DONE: Show fields when selecting habit, and create a variable to hold type of Habit
                if (checkedId == R.id.newHabit_radioEconomic) {
                    showTextNotification("Economic Checked");
                    typeHabit = 1;
                    updateUI(typeHabit);
                } else if (checkedId == R.id.newHabit_radioDate) {
                    showTextNotification("Date Checked");
                    typeHabit = 2;
                    updateUI(typeHabit);
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.newHabit_saveFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
                title = editTitle.getText().toString();
                description = editDesc.getText().toString();
                startDate = convertToDate(dateEditText.getText().toString());


                //DONE: Check variable from radiogroup
                if (typeHabit == 1) {
                    boolean fieldsOK = checkFields(new EditText[] { economicCurrencyEditText, economicAlternativePriceEditText, economicGoalEditText, economicPriceEditText, editTitle, editDesc, dateEditText });
                    if (fieldsOK == true) {
                        currency = economicCurrencyEditText.getText().toString();
                        alternativePrice = Float.parseFloat(economicAlternativePriceEditText.getText().toString());
                        goalValue = Float.parseFloat(economicGoalEditText.getText().toString());
                        price = Float.parseFloat(economicPriceEditText.getText().toString());
                        EconomicHabit habit = new EconomicHabit(title, description, startDate, currency, alternativePrice, goalValue, price);
                        saveHabit(habit);
                    } else {
                        showTextNotification("Fields are empty");
                    }
                } else if (typeHabit == 2) {
                    boolean fieldsOK = checkFields(new EditText[] { dateGoalEditText, editTitle, editDesc, dateEditText });
                    if(fieldsOK == true) {
                        dateGoalValue = Integer.parseInt(dateGoalEditText.getText().toString());
                        DateHabit habit = new DateHabit(title, description, startDate, dateGoalValue);
                        saveHabit(habit);
                    } else {
                        showTextNotification("Fields are empty");
                    }

                } else {
                    showTextNotification("Please select a type of habit");
                }
                //Habit habit = new Habit(title, description, startDate);
                //testhabit
                //Habit habit2 = new Habit("a","test",new Date());
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

    private boolean checkFields(EditText[] fields) {
        for(int i = 0; i < fields.length; i++){
            EditText currentField = fields[i];
            if(currentField.getText().toString().length() <= 0){
                return false;
            }
        }
        return true;
    }

    private void updateUI(int typeHabit) {
        if (typeHabit == 1) {
            dateGoalEditText.setVisibility(View.INVISIBLE);
            economicPriceEditText.setVisibility(View.VISIBLE);
            economicGoalEditText.setVisibility(View.VISIBLE);
            economicAlternativePriceEditText.setVisibility(View.VISIBLE);
            economicCurrencyEditText.setVisibility(View.VISIBLE);
        } else if (typeHabit == 2) {
            dateGoalEditText.setVisibility(View.VISIBLE);
            economicPriceEditText.setVisibility(View.INVISIBLE);
            economicGoalEditText.setVisibility(View.INVISIBLE);
            economicAlternativePriceEditText.setVisibility(View.INVISIBLE);
            economicCurrencyEditText.setVisibility(View.INVISIBLE);
        } else {
            dateGoalEditText.setVisibility(View.INVISIBLE);
            economicPriceEditText.setVisibility(View.INVISIBLE);
            economicGoalEditText.setVisibility(View.INVISIBLE);
            economicAlternativePriceEditText.setVisibility(View.INVISIBLE);
            economicCurrencyEditText.setVisibility(View.INVISIBLE);
        }

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendarPick.set(Calendar.YEAR, year);
            calendarPick.set(Calendar.MONTH, month);
            calendarPick.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(R.id.newHabit_startDate);
        }
    };


    private void updateLabel(int viewId) {
        dateEditText = (EditText) findViewById(viewId);
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

    //Temp function to debug radioGroupListener
    public void showTextNotification(String msgToDisplay) {
        Toast.makeText(this, msgToDisplay, Toast.LENGTH_SHORT).show();
    }

    private void saveHabit(Habit habit) {
        SaveData saveData = new SaveData();
        saveData.saveToFile(habit);
        //saveData.saveToFile(habit2,"testers.txt");
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

}
