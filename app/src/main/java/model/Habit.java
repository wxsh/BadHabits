package model;
//import no.hiof.andrekar.badhabits.R;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.firebase.firestore.Exclude;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Year;
import org.threeten.bp.ZoneId;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.threeten.bp.temporal.ChronoUnit.DAYS;
import static org.threeten.bp.temporal.ChronoUnit.MILLIS;
import static org.threeten.bp.temporal.ChronoUnit.YEARS;

public class Habit {
    //An ArrayList to contain all habits when they are created

    //TODO: implement Math for habits. Return progress values.

    //DONE: make this list create itself from stored files
    public static ArrayList<Habit> habits = new ArrayList<Habit>();

    //We need a title and description for our main class.
    private String title;
    private String description;
    //Favourite boolean?
    private boolean isFavourite;

    //Start date? - Maybe this is better as a String and cast it later?
    private long startDate;
    private String uid;
    private String habitType;
    private long failDate;
    private int failureTimes;


    //Constructors
    public Habit(String title, String description, long startDate, Boolean isFavourite, String habitType) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.isFavourite = isFavourite;
        this.uid = UUID.randomUUID().toString();
        this.failDate = 0;
        this.failureTimes = 0;
        this.habitType = habitType;
        //We add habits when we save them, so adding to list is currently not needed.
        //habits.add(this);
    }


    //Empty constructor for firestore.
    public Habit() {
    }

    //Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getStartDate() {
        return startDate;
    }

    public boolean getIsFavourite() {
        return isFavourite;
    }

    public long getFailDate() {
        return failDate;
    }

    public void setFailDate(long failDate) {
        this.failDate = failDate;
        failureTimes++;
    }

    //staticGetters
    public static boolean getHaveFavorite(){
        if (habits != null){
            for (Habit h : habits) {
                if (h.getIsFavourite())
                    return true;
            }
        }
        return false;
    }

    public static int getNumFavourites() {
        int num = 0;
        for (Habit habit: habits) {
            if (habit.getIsFavourite()) {
                num++;
            }
        }
        return num;
    }

    @Exclude
    public float getDaysFromStart() {
        return Habit.getDateDiff(this.getStartDate(), new Date().getTime(), ChronoUnit.DAYS);
    }

    public String getUid() { return uid; }

    //Setters

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFavourite(boolean isFavourite) {
        //Might want to have this as a toggle instead.
        this.isFavourite = isFavourite;
    }

    public static Comparator<Habit> HabitComparator = new Comparator<Habit>() {
        @Override
        public int compare(Habit h2, Habit h1) {
            int result = Boolean.compare(h1.getIsFavourite(), h2.getIsFavourite());
            if (result == 0) {
                // boolean values the same
                result = h2.getTitle().compareTo(h1.getTitle());
            }
            return result;
        }
    };

    @Exclude
    public String getHabitType() {
        return habitType;
    }

    public void setHabitType(String habitType) {
        this.habitType = habitType;
    }

    public static Comparator<Habit> HabitComparatorGoal = new Comparator<Habit>() {
        @Override
        public int compare(Habit h2, Habit h1) {
            int result = Boolean.compare(h1.getIsFavourite(), h2.getIsFavourite());
            if (result == 0) {
                // boolean values the same
                // TODO: Look into sorting both date habits and economic habit
                // DONE implement Comparator for sorting by remaining.
                if (h2 instanceof EconomicHabit && h1 instanceof EconomicHabit) {
                    if (((EconomicHabit) h2).getProgress() > ((EconomicHabit) h1).getProgress()) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else if (h2 instanceof EconomicHabit && h1 instanceof DateHabit) {
                    return -1;
                } else if (h2 instanceof DateHabit && h1 instanceof EconomicHabit) {
                    return 1;
                } else if (h2 instanceof DateHabit && h1 instanceof DateHabit) {
                    //TODO: Fix crash when goal is reached and sorting by Goal
                    if ((((DateHabit) h2).getDateGoal() > (((DateHabit) h1).getDateGoal()))) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
                else return 0;
            }
            return result;
        }
    };

    public static Comparator<Habit> HabitComparatorType = new Comparator<Habit>() {
        @Override
        public int compare(Habit h2, Habit h1) {
            int result = Boolean.compare(h1.getIsFavourite(), h2.getIsFavourite());
            if (result == 0) {
                if (h1 instanceof EconomicHabit && h2 instanceof DateHabit) {
                    return -1;
                } else if (h1 instanceof DateHabit && h2 instanceof EconomicHabit) {
                    return 1;
                } else {
                    return 0;
                }
            }
            return result;
        }
    };

    public static long getDateDiff(long date1, long date2, ChronoUnit timeUnit) {
        //TODO: Look into replacing this function? Seems to be one day off.

        LocalDate lDate1 =
                Instant.ofEpochMilli(date1).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate lDate2 =
                Instant.ofEpochMilli(date2).atZone(ZoneId.systemDefault()).toLocalDate();
        return timeUnit.between(lDate1, lDate2);

        //long diffInMillis = date2 - date1;
        //return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public int getFailureTimes() {
        return failureTimes;
    }

    public void setFailureTimes(int failureTimes) {
        this.failureTimes = failureTimes;
    }

    public static long convertMillisToDays(long milliseconds) {
        LocalDate ldt = Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault()).toLocalDate();
        return ldt.toEpochDay();
    }

    public static long convertDaysToMillis(long days) {
        return TimeUnit.DAYS.toMillis(days);
    }

}
