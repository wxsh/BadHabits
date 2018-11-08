package no.hiof.andrekar.badhabits;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.SaveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import model.DateHabit;
import model.EconomicHabit;
import model.Habit;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public static MyAdapter adapter;
    public static MyFavoriteAdapter favAdapter;
    private boolean habitexists;

    ChildEventListener dateEventListener = new ChildEventListener() {
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
    ChildEventListener ecoEventListener = new ChildEventListener() {
        String TAG = "firebaseread, ECO";
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
            Log.d(TAG, "onChildAdded:" + dataSnapshot.getValue(EconomicHabit.class).getTitle());

            Habit habit = (EconomicHabit) dataSnapshot.getValue(EconomicHabit.class);
            for (Habit habitL: habit.habits) {
                if (habitL.getUid() == dataSnapshot.getValue(EconomicHabit.class).getUid()) {
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
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

            // A Habit has changed, use the key to determine if we are displaying this
            // Habit and if so displayed the changed Habit.
            EconomicHabit newHabit = dataSnapshot.getValue(EconomicHabit.class);
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
            EconomicHabit movedHabit = dataSnapshot.getValue(EconomicHabit.class);
            String HabitKey = dataSnapshot.getKey();

            // ...
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "postHabits:onCancelled", databaseError.toException());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mDatabase == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
            mDatabase = database.getReference();
            database.getReference(mAuth.getUid()).keepSynced(true);
        }
        mAuth = FirebaseAuth.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                mAuth.signInAnonymously()
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Login Main", "signInAnonymously:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Login Main", "signInAnonymously:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
            } else {
            FirebaseUser currentUser = mAuth.getCurrentUser();
        }

            setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);







        //code to ask user for permission to store data.
        int REQUEST_CODE=1;
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_CODE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_addHabit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), HabitActivity.class);
                intent.putExtra("TITLE", "Add new habit");
                startActivity(intent);
            }
        });

        //DONE: Implement this into habits model?
        Collections.sort(Habit.habits, Habit.HabitComparator);


        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            mDatabase.child(mAuth.getUid()).child("habits").child("DateHabits").addChildEventListener(dateEventListener);
            mDatabase.child(mAuth.getUid()).child("habits").child("EcoHabits").addChildEventListener(ecoEventListener);
            }
            initRecyclerView();
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.t
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
            startActivity(intent);

//            Snackbar.make(findViewById(android.R.id.content), "Not yet implemented", Snackbar.LENGTH_LONG).show();
//            return true;
        }
        if (id == R.id.action_populate) {
            populateData();
            return true;
        }
        if (id == R.id.action_remove) {
            removeData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void  initRecyclerView(){

        RecyclerView favoriteRecyclerView = findViewById(R.id.favorite_recycler_view);

        favAdapter = new MyFavoriteAdapter(this);
        favoriteRecyclerView.setAdapter(favAdapter);
        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new MyAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void populateData() {
        ArrayList<Habit> testHabits = new ArrayList<Habit>();
        Habit gumHabit = new EconomicHabit("Gum", "Stop with gum", new Date().getTime(), "kr", 10, 100, 10, false);
        Habit sodaHabit = new DateHabit("Soda", "Stop drinking soda", new Date().getTime(), 10, true);
        Habit poop = new EconomicHabit("Smokes", "Stop with smoking", new Date().getTime(), "kr", 10, 100, 10, false);
        Habit scoop = new DateHabit("Having fun", "Stop having fun", new Date().getTime(), 10, false);
        gumHabit.setFavourite(true);
        scoop.setFavourite(true);

        testHabits.add((EconomicHabit) gumHabit);
        testHabits.add((DateHabit) sodaHabit);
        testHabits.add((EconomicHabit) poop);
        testHabits.add((DateHabit) scoop);
        SaveData saveData = new SaveData();

        for (Habit habit : testHabits) {
            if (habit instanceof DateHabit) {
                saveData.saveToFile(habit, 2);
            } else if (habit instanceof EconomicHabit) {
                saveData.saveToFile(habit, 1);
            }
        }
        testHabits.clear();
        initRecyclerView();
        Collections.sort(Habit.habits, Habit.HabitComparator);
        }

        private void removeData() {
            Habit.habits.clear();
            SaveData saveData = new SaveData();
            //saveData.updateData(1);
            //saveData.updateData(2);
            initRecyclerView();
        }

        public static void updateRecyclerView(){
            adapter.notifyDataSetChanged();
            favAdapter.notifyDataSetChanged();
        }

}

