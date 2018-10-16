package model;

import java.util.Date;

//TODO: Implement maths in class? IE: getters for progress?
//TODO: Rename variables? ie. Price and InitialValue / GoalValue? - DO we need both or should ie. initialValue be renamed to price for alternative?

public class EconomicHabit extends Habit {
    private String currency;
    private float initialValue;
    private float goalValue;
    private float price;

    // Constructor
    public EconomicHabit(String title, String description, Date startDate, String currency, float initialValue, float goalValue, float price) {
        super(title, description, startDate);
        this.currency = currency;
        this.initialValue = initialValue;
        this.goalValue = goalValue;
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public float getPrice() { return price; }

    public float getGoalValue() { return goalValue; }

    public float getInitialValue() {
        return initialValue;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setInitialValue(float initialValue) {
        this.initialValue = initialValue;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setGoalValue(float goalValue) {
        this.goalValue = goalValue;
    }
}
