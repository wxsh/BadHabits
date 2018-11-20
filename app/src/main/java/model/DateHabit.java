package model;

import android.util.Log;

import com.google.firebase.firestore.Exclude;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

//DONE: Implement maths in class? IE: getters for progress?

public class DateHabit extends Habit {
    private Integer dateGoalValue;

    public DateHabit() {}
    private String habitType = "DATE_HABIT";

    public DateHabit(String title, String description, long startDate, Integer dateGoalValue, boolean isFavourite) {
        super(title, description, startDate, isFavourite, "DATE_HABIT");
        this.dateGoalValue = dateGoalValue;
    }


    public Integer getDateGoalValue() {
        return dateGoalValue;
    }

    public void setDateGoalValue(Integer dateGoalValue) {
        this.dateGoalValue = dateGoalValue;
    }

    public String getHabitType() {
        return habitType;
    }

    public void setHabitType(String habitType) {
        this.habitType = habitType;
    }

    @Exclude
    public String getDateGoal(){
        String dateGoal;
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(this.getStartDate()));
        c.add(Calendar.DATE,this.getDateGoalValue());
        long dateGoalL = Habit.getDateDiff(new Date().getTime(), c.getTimeInMillis(), TimeUnit.DAYS);
        dateGoal = Long.toString(dateGoalL);
        if (Long.valueOf(dateGoalL) > 0) {
            return dateGoal + " Remaining";
        }
        else return "Goal achieved";
    }

    @Exclude
    public String getDaysSinceStart(){
        String dateGoal;
        long dateGoalL = Habit.getDateDiff(this.getStartDate(), new Date().getTime(), TimeUnit.DAYS);
        dateGoal = Long.toString(dateGoalL);
        if(Long.valueOf(dateGoalL) > 0){
            return dateGoal + " Days since start";
        }
        else return "Has not started yet.";
    }

    public long getDateGoalMillis(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(this.getStartDate()));
        c.add(Calendar.DATE,this.getDateGoalValue());
        return getDateDiff(new Date().getTime(), c.getTimeInMillis(), TimeUnit.MILLISECONDS);

    }
}
