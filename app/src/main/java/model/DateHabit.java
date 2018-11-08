package model;

import android.util.Log;

import com.google.firebase.firestore.Exclude;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//DONE: Implement maths in class? IE: getters for progress?

public class DateHabit extends Habit {
    private Integer dateGoalValue;
    private static final String habitType = "Date";

    public DateHabit() {}

    public DateHabit(String title, String description, long startDate, Integer dateGoalValue, boolean isFavourite) {
        super(title, description, startDate, isFavourite, habitType);
        this.dateGoalValue = dateGoalValue;
    }


    public Integer getDateGoalValue() {
        return dateGoalValue;
    }

    public String getHabitType() { return habitType; }

    public void setDateGoalValue(Integer dateGoalValue) {
        this.dateGoalValue = dateGoalValue;
    }

    @Exclude
    public String getDateGoal(){
        String dateGoal;
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(this.getStartDate()));
        c.add(Calendar.DATE,this.getDateGoalValue());
        Date endDate = new Date(c.getTimeInMillis());
        //dateGoal = simpleDateFormat.format(endDate);
        long dateGoalL = ChronoUnit.DAYS.between(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().toLocalDate(), endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().toLocalDate());
        dateGoal = Long.toString(dateGoalL);
        if (Long.valueOf(dateGoalL) > 0) {
            return dateGoal + " Remaining";
        }
        else return "Mål nådd";
    }
}
