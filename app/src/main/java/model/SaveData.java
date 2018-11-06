package model;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import no.hiof.andrekar.badhabits.MainActivity;
import no.hiof.andrekar.badhabits.MyAdapter;

public class SaveData {
    //Todo: Change this into internal storage, no need to use Downloads
    String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/TestGSon";
    String ecofile = filename+"Eco.txt";
    String datefile = filename+"Date.txt";
    ArrayList<EconomicHabit> ecohabits = new ArrayList<EconomicHabit>();
    ArrayList<DateHabit> datehabits = new ArrayList<DateHabit>();
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    private boolean habitexists = false;

    //DONE: Fix duplication problem.
    //TODO: Make Adapter refresh after sync


    public void readFromFile(final Context context) {
        //Create a new Gson object
        //Habit.habits.clear();
        //Gson gson = new Gson();
        //Habit.habits.clear();
        ChildEventListener childEventListener = new ChildEventListener() {
            String TAG = "firebaseread";
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getValue(DateHabit.class).getTitle());

                Habit habit = (DateHabit) dataSnapshot.getValue(DateHabit.class);
                for (Habit habitL: habit.habits) {
                    if (habitL.getUid() == dataSnapshot.getValue(DateHabit.class).getUid()) {
                        habitexists = true;
                        Log.d(TAG, "Habit already found");
                    }
                }
                if(habitexists == false) {
                    Habit.habits.add(habit);
                    Log.d(TAG, "Adding" + habit.toString() + "From firebase");
                }
                Log.d(TAG, "This should be after adapter has loaded list; Habits length"+Habit.habits.size());
                MainActivity.updateRecyclerView();

                //TODO: Callback to mainactivity to update list.
                // A new Habit has been added, add it to the displayed list
                //Habit habit = (DateHabit) dataSnapshot.getValue(DateHabit.class);

                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A Habit has changed, use the key to determine if we are displaying this
                // Habit and if so displayed the changed Habit.
                DateHabit newHabit = dataSnapshot.getValue(DateHabit.class);
                String HabitKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A Habit has changed, use the key to determine if we are displaying this
                // Habit and if so remove it.
                String HabitKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A Habit has changed position, use the key to determine if we are
                // displaying this Habit and if so move it.
                DateHabit movedHabit = dataSnapshot.getValue(DateHabit.class);
                String HabitKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postHabits:onCancelled", databaseError.toException());
            }
        };

        dbRef.child(fbAuth.getUid()).child("habits").child("DateHabits").addChildEventListener(childEventListener);

        /*
        try {
        //Read the employee.json file
            BufferedReader br = new BufferedReader(
                new FileReader(datefile));

            //convert the json to  Java object (Employee)

            Type collectionType = new TypeToken<ArrayList<DateHabit>>(){}.getType();
            ArrayList<DateHabit> habitsF = gson.fromJson(br, collectionType);
            for (int i = 0; i < habitsF.size(); i++ ) {
                    Habit habit = (DateHabit) habitsF.get(i);
                    Habit.habits.add(habit);
                }
            }
            catch (IOException e)
        {
            e.printStackTrace();
        }

        try {
            //Read the employee.json file
            BufferedReader br = new BufferedReader(
                    new FileReader(ecofile));

            //convert the json to  Java object

            Type collectionType = new TypeToken<ArrayList<EconomicHabit>>(){}.getType();
            ArrayList<EconomicHabit> habitsF = gson.fromJson(br, collectionType);
            for (int i = 0; i < habitsF.size(); i++ ) {
                Habit habit = (EconomicHabit) habitsF.get(i);
                Habit.habits.add(habit);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        */

}

    public void saveToFile(Habit habit, int typeHabit) {
        //try {
            // Create a new Gson object
            //Gson gson = new Gson();
        if (typeHabit == 1) {
            dbRef.child(fbAuth.getUid()).child("habits").child("EcoHabits").child(habit.getUid()).setValue(habit);
        } else if (typeHabit == 2) {
            dbRef.child(fbAuth.getUid()).child("habits").child("DateHabits").child(habit.getUid()).setValue(habit);
        }

            /*
            //convert the Java object to json
            if(typeHabit == 1) {
                Log.d("InstanceOF", "Got Eco Habit");

                Habit.habits.add(habit);
                ecohabits.clear();
                for ( Habit habitListed : Habit.habits ) {
                    Log.d("InstanceOF", "Looping through habits");
                    if (habitListed instanceof EconomicHabit) {
                        Log.d("InstanceOF", "EcoHabit");
                        ecohabits.add((EconomicHabit) habitListed);
                    }
                }
                String jsonString = gson.toJson(ecohabits);

                FileWriter fileWriter = new FileWriter(ecofile, false);
                fileWriter.write(jsonString);
                fileWriter.close();
                //EconomicHabit.ecohabits.clear();
            } else if (typeHabit == 2) {

                Log.d("InstanceOF", "Got Date Habit");

                Habit.habits.add(habit);
                datehabits.clear();
                for ( Habit habitListed : Habit.habits ) {
                    Log.d("InstanceOF", "Looping through habits");
                    if (habitListed instanceof DateHabit) {
                        Log.d("InstanceOF", "EcoHabit");
                        datehabits.add((DateHabit) habitListed);
                    }
                }
                String jsonString = gson.toJson(datehabits);
                FileWriter fileWriter = new FileWriter(datefile, false);
                fileWriter.write(jsonString);
                fileWriter.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        */

    };

    public void removeData(Habit habit, int typeHabit) {
        if (typeHabit == 1) {
            dbRef.child(fbAuth.getUid()).child("habits").child("EcoHabits").child(habit.getUid()).removeValue();
        } else if (typeHabit == 2) {
            dbRef.child(fbAuth.getUid()).child("habits").child("DateHabits").child(habit.getUid()).removeValue();
        }
    }
    public void saveToFile(Habit habit, int typeHabit, int habitIndex) {
        try {
            // Create a new Gson object
            Gson gson = new Gson();

            //convert the Java object to json
            if(typeHabit == 1) {
                Log.d("InstanceOF", "Got Eco Habit");

                Habit.habits.set(habitIndex, habit);
                ecohabits.clear();
                for ( Habit habitListed : Habit.habits ) {
                    Log.d("InstanceOF", "Looping through habits");
                    if (habitListed instanceof EconomicHabit) {
                        Log.d("InstanceOF", "EcoHabit");
                        ecohabits.add((EconomicHabit) habitListed);
                    }
                }
                String jsonString = gson.toJson(ecohabits);

                FileWriter fileWriter = new FileWriter(ecofile, false);
                fileWriter.write(jsonString);
                fileWriter.close();
                //EconomicHabit.ecohabits.clear();
            } else if (typeHabit == 2) {

                Log.d("InstanceOF", "Got Date Habit");

                Habit.habits.set(habitIndex, habit);
                datehabits.clear();
                for ( Habit habitListed : Habit.habits ) {
                    Log.d("InstanceOF", "Looping through habits");
                    if (habitListed instanceof DateHabit) {
                        Log.d("InstanceOF", "EcoHabit");
                        datehabits.add((DateHabit) habitListed);
                    }
                }
                String jsonString = gson.toJson(datehabits);
                FileWriter fileWriter = new FileWriter(datefile, false);
                fileWriter.write(jsonString);
                fileWriter.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateData(Habit habit, int typeHabit) {
        // Gson gson = new Gson();

        if (typeHabit == 1) {
            dbRef.child(fbAuth.getUid()).child("habits").child("EcoHabits").child(habit.getUid()).setValue(habit);
        } else if (typeHabit == 2) {
            dbRef.child(fbAuth.getUid()).child("habits").child("DateHabits").child(habit.getUid()).setValue(habit);
        }
        /*
        try {
            if (typeHabit == 1) {
                ecohabits.clear();
                for ( Habit habitListed : Habit.habits ) {
                    Log.d("InstanceOF", "Looping through habits");
                    if (habitListed instanceof EconomicHabit) {
                        Log.d("InstanceOF", "EcoHabit");
                        ecohabits.add((EconomicHabit) habitListed);
                    }
                }
                String jsonString = gson.toJson(ecohabits);

                FileWriter fileWriter = new FileWriter(ecofile, false);
                fileWriter.write(jsonString);
                fileWriter.close();
            } else if (typeHabit == 2) {
                datehabits.clear();
                for ( Habit habitListed : Habit.habits ) {
                    Log.d("InstanceOF", "Looping through habits");
                    if (habitListed instanceof DateHabit) {
                        Log.d("InstanceOF", "EcoHabit");
                        datehabits.add((DateHabit) habitListed);
                    }
                }
                String jsonString = gson.toJson(datehabits);
                FileWriter fileWriter = new FileWriter(datefile, false);
                fileWriter.write(jsonString);
                fileWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }
}
