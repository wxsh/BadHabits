package model;

import com.google.firebase.database.Exclude;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//TODO: Implement maths in class? IE: getters for progress?
//TODO: Rename variables? ie. Price and InitialValue / GoalValue? - DO we need both or should ie. initialValue be renamed to price for alternative?


public class EconomicHabit extends Habit {
    private String currency;
    private float goalValue;
    private float price;
    private float alternativePrice;


    // Constructor
    public EconomicHabit(String title, String description, Date startDate, String currency, float alternativePrice, float goalValue, float price) {
        super(title, description, startDate);
        this.currency = currency;
        this.alternativePrice = alternativePrice;
        this.goalValue = goalValue;
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public float getPrice() { return price; }

    public float getGoalValue() { return goalValue; }

    public float getAlternativePrice() {
        return alternativePrice;
    }

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
        c.setTime(this.getStartDate());
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
}
