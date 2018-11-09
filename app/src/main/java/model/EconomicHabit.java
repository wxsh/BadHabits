package model;

import com.google.firebase.firestore.Exclude;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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



    @Exclude
    public String getProgress() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(this.getStartDate()));
        Date startDate = new Date(c.getTimeInMillis());
        float dateGoalL = ChronoUnit.DAYS.between(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().toLocalDate(), new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().toLocalDate());
        float saved = (( - this.getGoalValue() - (dateGoalL*this.getAlternativePrice()) ) + (dateGoalL*this.getPrice()));
        if (saved < 0) {
            //Should maybe build this string in activity instead if we want to color it
            return Float.toString(saved)+" "+this.getCurrency();
        }
        else {
            return Float.toString(saved)+" "+this.getCurrency();
        }

    }

    public int getFailedTotal() {
        return failedTotal;
    }

    public void setFailedTotal(int failedTotal) {
        this.failedTotal = failedTotal;
    }
    public void increaseFailedTotal(int failedAmout){
        this.failedTotal += failedAmout;
    }
}
