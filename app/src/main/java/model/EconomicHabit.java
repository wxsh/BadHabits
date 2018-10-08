package model;

import java.util.Date;

public class EconomicHabit extends Habit {
    private String currency;
    private float initialValue;

    // Constructor
    public EconomicHabit(String title, String description, Date startDate, String currency, float initialValue) {
        super(title, description, startDate);
        this.currency = currency;
        this.initialValue = initialValue;
    }

    public String getCurrency() {
        return currency;
    }

    public float getInitialValue() {
        return initialValue;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setInitialValue(float initialValue);
        this.initialValue = initialValue;
}
