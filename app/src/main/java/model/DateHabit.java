package model;

import java.util.Date;

//TODO: Implement maths in class? IE: getters for progress?

public class DateHabit extends Habit {
    private Integer dateGoalValue;

    public DateHabit(String title, String description, Date startDate, Integer dateGoalValue) {
        super(title, description, startDate);
        this.dateGoalValue = dateGoalValue;
    }

    public Integer getDateGoalValue() {
        return dateGoalValue;
    }

    public void setDateGoalValue(Integer dateGoalValue) {
        this.dateGoalValue = dateGoalValue;
    }

}
