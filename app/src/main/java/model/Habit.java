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

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

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


    //Constructors
    public Habit(String title, String description, long startDate, Boolean isFavourite, String habitType) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.isFavourite = isFavourite;
        this.uid = UUID.randomUUID().toString();
        //We add habits when we save them, so adding to list is currently not needed.
        //habits.add(this);
    }

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Exclude
    public float getDaysFromStart() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(this.getStartDate()));
        Date startDate = new Date(c.getTimeInMillis());
        return ChronoUnit.DAYS.between(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().toLocalDate(), new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().toLocalDate());
    }

    public String getUid() { return uid; }

    public String getHabitType() { return habitType; }



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

}
