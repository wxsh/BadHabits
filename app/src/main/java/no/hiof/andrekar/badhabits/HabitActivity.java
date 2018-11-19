package no.hiof.andrekar.badhabits;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;
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
    //TODO: Intent handling
    //DONE: and send data to fireBase.

    private String title;
    private String description;
    private long startDate;
    //Title and description
    private EditText editTitle, editDesc, dateEditText;
    private TextInputLayout dateGoalIT, economicGoalIT, economicPriceIT, economicAlternativePriceIT;
    private String pricePeriod, alternativePricePeriod;

    //Date habits
    private EditText dateGoalEditText;
    //Economic Habits
    private EditText economicGoalEditText, economicAlternativePriceEditText, economicPriceEditText;
    private Spinner economicPeriodSpinner, economicAlterntivePeriodSpinner;
    private float alternativePrice, goalValue, price;

    private RadioGroup typeHabitRG;

    private int dateGoalValue, editIndex;

    //Edit mode when habit is sent
    private boolean editMode;

    // 1 = Eco, 2 = Date
    private int typeHabit = 0;

    Calendar calendarPick = Calendar.getInstance();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String userTheme = sharedPreferences.getString("key_theme", "");
        if (userTheme.equals("Light")){
            setTheme(R.style.LightTheme);
        }
        else if (userTheme.equals("Dark")){
            setTheme(R.style.DarkTheme);
        }

        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        setContentView(R.layout.activity_habit);
        //Slide slide = TransitionInflater.from(this).inflateTransition(R.transition.slide_and_changebounds);
        //getWindow().setExitTransition(slide);

        View parentView = findViewById(R.id.activity_habit_view);
        if (userTheme.equals("Light")){
        }
        else if (userTheme.equals("Dark")){
            parentView.setBackgroundColor(getResources().getColor(GlobalConstants.COLOR_PRIMARY));
        }

        dateEditText = findViewById(R.id.newHabit_startDate);
        findviews();

        if(getIntent().hasExtra("TITLE")) {
            setTitle(getIntent().getStringExtra("TITLE"));
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.period_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        economicPeriodSpinner.setAdapter(adapter);
        economicAlterntivePeriodSpinner.setAdapter(adapter);

        if (getIntent().hasExtra("CURRENT_HABIT_INDEX")) {
            //DONE: Fill out fields and change save function to handle existing habit.
            Habit editHabit = Habit.habits.get(getIntent().getIntExtra("CURRENT_HABIT_INDEX", 0));
            setTitle("Editing " + editHabit.getTitle());
            editIndex = getIntent().getIntExtra("CURRENT_HABIT_INDEX", 0);
            editTitle.setText(editHabit.getTitle());
            editDesc.setText(editHabit.getTitle());
            String myFormat = "dd/MM/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
            dateEditText.setText(sdf.format(editHabit.getStartDate()));

            //DONE: Get start date.
            if(editHabit instanceof DateHabit) {
                typeHabitRG.check(R.id.newHabit_radioDate);
                typeHabit = 2;
                updateUI(2);
                dateGoalEditText.setText(((DateHabit) editHabit).getDateGoalValue().toString());
            } else if(editHabit instanceof EconomicHabit) {
                typeHabitRG.check(R.id.newHabit_radioEconomic);
                typeHabit = 1;
                updateUI(1);
                economicAlternativePriceEditText.setText(Float.toString(((EconomicHabit) editHabit).getAlternativePrice()));
                economicGoalEditText.setText(Float.toString(((EconomicHabit) editHabit).getGoalValue()));
                economicPriceEditText.setText(Float.toString(((EconomicHabit) editHabit).getPrice()));
                economicPeriodSpinner.setSelection(adapter.getPosition("Daily"));
                economicAlterntivePeriodSpinner.setSelection(adapter.getPosition("Daily"));
            }

            editMode = true;

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
                boolean fieldsOK = false;
                if(typeHabit == 1) {
                    fieldsOK = checkFields(new EditText[]{economicAlternativePriceEditText, economicGoalEditText, economicPriceEditText, editTitle, editDesc, dateEditText});
                } else if (typeHabit == 2) {
                    fieldsOK = checkFields(new EditText[]{dateGoalEditText, editTitle, editDesc, dateEditText});
                }

                if (typeHabit != 0) {
                    if (fieldsOK == true) {
                        saveHabit(typeHabit);
                    } else {
                        showTextNotification("Fields are empty");
                    }
                } else {
                    showTextNotification("Please select a type of habit");
                }
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
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
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
            economicPeriodSpinner.setVisibility(View.VISIBLE);
            economicAlterntivePeriodSpinner.setVisibility(View.VISIBLE);
        } else if (typeHabit == 2) {
            dateGoalIT.setVisibility(View.VISIBLE);
            economicPriceIT.setVisibility(View.INVISIBLE);
            economicGoalIT.setVisibility(View.INVISIBLE);
            economicAlternativePriceIT.setVisibility(View.INVISIBLE);
            economicPeriodSpinner.setVisibility(View.INVISIBLE);
            economicAlterntivePeriodSpinner.setVisibility(View.INVISIBLE);
        } else {
            dateGoalIT.setVisibility(View.INVISIBLE);
            economicPriceIT.setVisibility(View.INVISIBLE);
            economicGoalIT.setVisibility(View.INVISIBLE);
            economicAlternativePriceIT.setVisibility(View.INVISIBLE);
            economicPeriodSpinner.setVisibility(View.INVISIBLE);
            economicAlterntivePeriodSpinner.setVisibility(View.INVISIBLE);
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

    private void saveHabit(int typeHabit) {
        SaveData saveData = new SaveData();
        if (typeHabit == 1) {
            pricePeriod = economicPeriodSpinner.getSelectedItem().toString();
            alternativePricePeriod = economicAlterntivePeriodSpinner.getSelectedItem().toString();
            price = convertPrice(Float.parseFloat(economicPriceEditText.getText().toString()), economicPeriodSpinner);
            alternativePrice = convertPrice(Float.parseFloat(economicAlternativePriceEditText.getText().toString()), economicAlterntivePeriodSpinner);
            Log.d("Pricespinner", economicAlterntivePeriodSpinner.getSelectedItem().toString());

            goalValue = Float.parseFloat(economicGoalEditText.getText().toString());

            if (editMode == false) {
                EconomicHabit habit = new EconomicHabit(title, description, startDate, alternativePrice, goalValue, price, false);
                saveData.saveData(habit, typeHabit);
                Habit.habits.add((EconomicHabit) habit);
            } else if (editMode == true) {
                EconomicHabit habit = (EconomicHabit) Habit.habits.get(editIndex);
                habit.setTitle(title);
                habit.setDescription(description);
                habit.setStartDate(startDate);
                habit.setAlternativePrice(alternativePrice);
                habit.setGoalValue(goalValue);
                habit.setPrice(price);
                saveData.saveData(habit, typeHabit);
            }
        } else if (typeHabit == 2) {
            dateGoalValue = Integer.parseInt(dateGoalEditText.getText().toString());
            if (editMode == false) {
                DateHabit habit = new DateHabit(title, description, startDate, dateGoalValue, false);
                saveData.saveData(habit, typeHabit);
                Habit.habits.add((DateHabit) habit);
            } else if (editMode == true) {
                //DONE add datehabit edit handling
                DateHabit habit = (DateHabit) Habit.habits.get(editIndex);
                habit.setTitle(title);
                habit.setDescription(description);
                habit.setDateGoalValue(dateGoalValue);
                habit.setStartDate(startDate);
                saveData.saveData(habit, typeHabit);
            }
        }

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        setResult(Activity.RESULT_OK, intent);
        finish();
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
        economicPeriodSpinner = findViewById(R.id.newHabit_economicHabit_periodPrice);
        economicGoalEditText = findViewById(R.id.newHabit_economicHabit_goal);
        economicAlternativePriceEditText = findViewById(R.id.newHabit_economicHabit_alternativePrice);
        economicPriceEditText = findViewById(R.id.newHabit_economicHabit_price);
        economicAlterntivePeriodSpinner = findViewById(R.id.newHabit_economicHabit_periodAlternativePrice);
    }

    private float convertPrice(float price, Spinner spinner) {
        DecimalFormat df = new DecimalFormat("###.##");
        if (spinner.getSelectedItem().toString().equalsIgnoreCase(getResources().getStringArray(R.array.period_array)[0])) {
            return Float.parseFloat(df.format(price));
        } else if (spinner.getSelectedItem().toString().equalsIgnoreCase(getResources().getStringArray(R.array.period_array)[1])) {
            return Float.parseFloat(df.format(price/7));
        } else if (spinner.getSelectedItem().toString().equalsIgnoreCase(getResources().getStringArray(R.array.period_array)[2])) {
            return Float.parseFloat(df.format(price/30));
        } else if (spinner.getSelectedItem().toString().equalsIgnoreCase(getResources().getStringArray(R.array.period_array)[3])) {
            return Float.parseFloat(df.format(price/365));
        } else return 0;
    }

}

