package no.hiof.andrekar.badhabits;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

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
    //DONE 03: Save data
    //DONE 04: Logic to dynamically change fields to reflect type of habit
    //DONE 05: Make sure fields are filled out
    //NOT_NEEDED 06: Make sure date picker is available for all date fields.

    //TODO: SavedInstanceState on rotate? - Not needed?
    //TODO: Intent handling and send data to fireBase.

    private String title;
    private String description;
    private long startDate;
    //Title and description
    private EditText editTitle, editDesc, dateEditText;
    private TextInputLayout dateGoalIT, economicGoalIT, economicPriceIT, economicAlternativePriceIT;
    private String currency;

    //Date habits
    private EditText dateGoalEditText;
    //Economic Habits
    private EditText economicGoalEditText, economicAlternativePriceEditText, economicPriceEditText;
    private Spinner economicCurrencySpinner;
    private float alternativePrice, goalValue, price;

    private RadioGroup typeHabitRG;

    private int dateGoalValue;

    // 1 = Eco, 2 = Date
    private int typeHabit = 0;

    Calendar calendarPick = Calendar.getInstance();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
        final EditText dateEditText = findViewById(R.id.newHabit_startDate);
        findviews();

        if(getIntent().hasExtra("TITLE")) {
            setTitle(getIntent().getStringExtra("TITLE"));
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        economicCurrencySpinner.setAdapter(adapter);

        if (getIntent().hasExtra("HABIT_NAME")) {
            // TODO: Add logic to handle intent that sends habit.
            // Needs to set a variable to handle saving, most likely.
        }
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
                    boolean fieldsOK = checkFields(new EditText[] { economicAlternativePriceEditText, economicGoalEditText, economicPriceEditText, editTitle, editDesc, dateEditText });
                    if (fieldsOK == true) {
                        currency = economicCurrencySpinner.getSelectedItem().toString();
                        alternativePrice = Float.parseFloat(economicAlternativePriceEditText.getText().toString());
                        goalValue = Float.parseFloat(economicGoalEditText.getText().toString());
                        price = Float.parseFloat(economicPriceEditText.getText().toString());
                        EconomicHabit habit = new EconomicHabit(title, description, startDate, currency, alternativePrice, goalValue, price, false);
                        saveHabit(habit, typeHabit);
                    } else {
                        showTextNotification("Fields are empty");
                    }
                } else if (typeHabit == 2) {
                    boolean fieldsOK = checkFields(new EditText[] { dateGoalEditText, editTitle, editDesc, dateEditText });
                    if(fieldsOK == true) {
                        dateGoalValue = Integer.parseInt(dateGoalEditText.getText().toString());
                        DateHabit habit = new DateHabit(title, description, startDate, dateGoalValue, false);
                        saveHabit(habit, typeHabit);
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
            dateGoalIT.setVisibility(View.INVISIBLE);
            economicGoalIT.setVisibility(View.VISIBLE);
            economicPriceIT.setVisibility(View.VISIBLE);
            economicAlternativePriceIT.setVisibility(View.VISIBLE);
            economicCurrencySpinner.setVisibility(View.VISIBLE);
        } else if (typeHabit == 2) {
            dateGoalIT.setVisibility(View.VISIBLE);
            economicPriceIT.setVisibility(View.INVISIBLE);
            economicGoalIT.setVisibility(View.INVISIBLE);
            economicAlternativePriceIT.setVisibility(View.INVISIBLE);
            economicCurrencySpinner.setVisibility(View.INVISIBLE);
        } else {
            dateGoalIT.setVisibility(View.INVISIBLE);
            economicPriceIT.setVisibility(View.INVISIBLE);
            economicGoalIT.setVisibility(View.INVISIBLE);
            economicAlternativePriceIT.setVisibility(View.INVISIBLE);
            economicCurrencySpinner.setVisibility(View.INVISIBLE);
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


    private long convertToDate(String dateToConvert) {
        Date convertedDate = new Date();

        try {
            convertedDate = new SimpleDateFormat("dd/MM/yy").parse(dateToConvert);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertedDate.getTime();

    }

    //Temp function to debug radioGroupListener
    public void showTextNotification(String msgToDisplay) {
        Toast.makeText(this, msgToDisplay, Toast.LENGTH_SHORT).show();
    }

    private void saveHabit(Habit habit, int typeHabit) {
        SaveData saveData = new SaveData();
        saveData.saveToFile(habit, typeHabit);
        //saveData.saveToFile(habit2,"testers.txt");
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    private void saveHabit(Habit habit, int typeHabit, int habitIndex) {
        SaveData saveData = new SaveData();
        saveData.saveToFile(habit, typeHabit, habitIndex);
        //saveData.saveToFile(habit2,"testers.txt");
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    private void findviews() {
        editTitle = findViewById(R.id.newHabit_name);
        editDesc = findViewById(R.id.newHabit_description);
        typeHabitRG = findViewById(R.id.radiogroup_typeHabit);

        //Extra UI items
        dateGoalEditText = findViewById(R.id.newHabit_dateHabit_goal);
        dateGoalIT = findViewById(R.id.newHabit_dateHabit_goalIT);
        economicGoalIT = findViewById(R.id.newHabit_economicHabit_goalIT);
        economicPriceIT = findViewById(R.id.newHabit_economicHabit_priceIT);
        economicAlternativePriceIT = findViewById(R.id.newHabit_economicHabit_alternativepriceIT);
        economicCurrencySpinner = findViewById(R.id.newHabit_economicHabit_currency);
        economicGoalEditText = findViewById(R.id.newHabit_economicHabit_goal);
        economicAlternativePriceEditText = findViewById(R.id.newHabit_economicHabit_alternativePrice);
        economicPriceEditText = findViewById(R.id.newHabit_economicHabit_price);
    }

}

