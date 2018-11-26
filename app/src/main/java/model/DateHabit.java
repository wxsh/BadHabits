package model;
import com.google.firebase.firestore.Exclude;
import java.util.Calendar;
import java.util.Date;

import static org.threeten.bp.temporal.ChronoUnit.DAYS;

//DONE: Implement maths in class? IE: getters for progress?
public class DateHabit extends Habit {
    private Integer dateGoalValue;
    //Empty constructor for firestore.
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

    //Gets amount of days until finished.
    @Exclude
    public long getDateGoal(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(this.getStartDate()));
        c.add(Calendar.DATE,this.getDateGoalValue());
        long dateGoalL = Habit.getDateDiff(new Date().getTime(), c.getTimeInMillis(), DAYS);
        return dateGoalL;
    }

    //Get amount of days since start
    @Exclude
    public long getDaysSinceStart(){
        long dateGoalL = Habit.getDateDiff(this.getStartDate(), new Date().getTime(), DAYS);
        return dateGoalL;
    }
    //Gets the time until finished in milliseconds
    public long getDateGoalMillis() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(this.getStartDate()));
        c.add(Calendar.DATE, this.getDateGoalValue());
        //DONE: Double check this conversion
        long a = c.getTimeInMillis() - new Date().getTime();
        return a;
    }
    //gets amount of days between failed date and today.
    public long getFailedDays(){
        long dateGoalL = Habit.getDateDiff(this.getFailDate(), new Date().getTime(), DAYS);
        return dateGoalL;
    }
}
