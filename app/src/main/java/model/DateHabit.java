package model;

import com.google.firebase.firestore.Exclude;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

//DONE: Implement maths in class? IE: getters for progress?

public class DateHabit extends Habit {
    private Integer dateGoalValue;
    public DateHabit() {}

    public DateHabit(String title, String description, long startDate, Integer dateGoalValue, boolean isFavourite) {
        super(title, description, startDate, isFavourite);
        this.dateGoalValue = dateGoalValue;
    }


    public Integer getDateGoalValue() {
        return dateGoalValue;
    }

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

    @Exclude
    public String getDaysSinceStart(){
        String dateGoal;
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(this.getStartDate()));
        //c.add(Calendar.DATE,this.getDateGoalValue());
        Date endDate = new Date(c.getTimeInMillis());
        //dateGoal = simpleDateFormat.format(endDate);
        long dateGoalL = ChronoUnit.DAYS.between(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().toLocalDate(),new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().toLocalDate());
        dateGoal = Long.toString(dateGoalL);
        if(Long.valueOf(dateGoalL) > 0){
            return dateGoal + " Days since start";
        }
        else return "Has not started yet.";
    }
}
