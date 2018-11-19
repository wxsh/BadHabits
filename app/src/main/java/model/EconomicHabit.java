package model;

import android.util.Log;

import com.google.firebase.firestore.Exclude;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//TODO: Implement maths in class? IE: getters for progress?
//DONE: Rename variables? ie. Price and InitialValue / GoalValue? - DO we need both or should ie. initialValue be renamed to price for alternative?
//TODO: Clean up unused functions.

public class EconomicHabit extends Habit {
    private String currency;
    private float goalValue;
    private float price;
    private float alternativePrice;
    private static final String habitType = "Eco";
    private int failedTotal;
    private Map<String, Integer> failedMap = new HashMap<>();

    // Constructor
    public EconomicHabit(String title, String description, long startDate, String currency, float alternativePrice, float goalValue, float price, Boolean isFavourite) {
        super(title, description, startDate, isFavourite, habitType);
        this.currency = currency;
        this.alternativePrice = alternativePrice;
        this.goalValue = goalValue;
        this.price = price;
    }

    public EconomicHabit() {}

    public String getCurrency() {
        return currency;
    }

    public float getPrice() { return price; }

    public float getGoalValue() { return goalValue; }

    public float getAlternativePrice() {
        return alternativePrice;
    }

    public String getHabitType() {return habitType; }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setAlternativePrice(float alternativePrice) {
        this.alternativePrice = alternativePrice;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setGoalValue(float goalValue) {
        this.goalValue = goalValue;
    }

    public Map<String, Integer> getMappedFail() {
        return failedMap;
    }

    @Exclude
    public float getProgress() {
        float dateGoalL = Habit.getDateDiff(this.getStartDate(), new Date().getTime(), TimeUnit.DAYS);
        float saved = (( - this.getGoalValue() - (dateGoalL*this.getAlternativePrice()) ) + (dateGoalL*this.getPrice()) - this.getFailedTotal());
        if (saved < 0) {
            return saved;
        }
        else {
            return saved;
        }

    }

    public int getFailedTotal() {
        return failedTotal;
    }

    public void setFailedTotal(int failedTotal) {
        this.failedTotal = failedTotal;
    }
    
    public void increaseFailedTotal(int failedAmout){
        failedMap.put(Long.toString(new Date().getTime()), failedAmout);
        this.failedTotal += failedAmout;
    }
}
