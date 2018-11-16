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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
import java.util.concurrent.CountDownLatch;

import no.hiof.andrekar.badhabits.MainActivity;
import no.hiof.andrekar.badhabits.MyAdapter;

public class SaveData  {
    //NOT NEEDED: Change this into internal storage, no need to use Downloads
    String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/TestGSon";
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CountDownLatch latch = new CountDownLatch(1);
    volatile boolean ecoHabitsOk;
    volatile boolean dateHabitsOk;
    //DONE: Fix duplication problem.
    //DONE: Make Adapter refresh after sync


    public void readFromFile() {
        readFromFile(true);
    }

    public void readFromFile(final boolean animate) {
        Log.d("Firestoreread", "Reading file");
        final String TAG = "Firestoreread";
        ecoHabitsOk = false;
        dateHabitsOk = false;
        if (Habit.habits.size() > 0) {
            Habit.habits.clear();
            MainActivity.adapter.notifyDataSetChanged();
            MainActivity.favAdapter.notifyDataSetChanged();
        }
        db.collection(fbAuth.getUid()).document("habits").collection("DateHabits")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                DateHabit tempHabit = document.toObject(DateHabit.class);
                                Habit habit = (DateHabit) tempHabit;
                                Habit.habits.add(habit);
                                if(habit.getIsFavourite()) {
                                    MainActivity.favAdapter.notifyDataSetChanged();
                                } else {
                                    MainActivity.adapter.addP(Habit.habits.size());
                                }
                                Log.d(TAG, "Adding habit");
                            }
                            if(!animate) {
                                //MainActivity.updateRecyclerView(false, true, true);
                            } else {
                                //MainActivity.updateRecyclerView();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        db.collection(fbAuth.getUid()).document("habits").collection("EcoHabits")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                EconomicHabit tempHabit = document.toObject(EconomicHabit.class);
                                Habit habit = (EconomicHabit) tempHabit;
                                Habit.habits.add(habit);
                                Log.d(TAG, "Adding habit");
                                if(habit.getIsFavourite()) {
                                    MainActivity.favAdapter.notifyDataSetChanged();
                                } else {
                                    MainActivity.adapter.addP(Habit.habits.size());
                                }
                            }
                            if(!animate) {
                                //MainActivity.updateRecyclerView(false, true, true);
                            } else {
                                //MainActivity.updateRecyclerView();
                            }                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
                MainActivity.setRefreshing();
        }


    public void saveData(Habit habit, int typeHabit) {
        if (typeHabit == 1) {
            db.collection(fbAuth.getUid()).document("habits").collection("EcoHabits").document(habit.getUid()).set(habit);
        } else if (typeHabit == 2) {
            db.collection(fbAuth.getUid()).document("habits").collection("DateHabits").document(habit.getUid()).set(habit);
        }
    }

    public void removeData(Habit habit, int typeHabit) {
        if (typeHabit == 1) {
            db.collection(fbAuth.getUid()).document("habits").collection("EcoHabits").document(habit.getUid()).delete();
        } else if (typeHabit == 2) {
            db.collection(fbAuth.getUid()).document("habits").collection("DateHabits").document(habit.getUid()).delete();
        }
    }
}
