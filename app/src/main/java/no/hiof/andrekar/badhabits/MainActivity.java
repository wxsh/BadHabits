package no.hiof.andrekar.badhabits;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.SaveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import model.DateHabit;
import model.EconomicHabit;
import model.Habit;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public static MyAdapter adapter;
    public static MyFavoriteAdapter favAdapter;
    private boolean habitexists;
    private static float totalSaved, totalDays, longestStreak;
    private static TextView ecoBottomText, dateBottomText, longestStreakText;
    private static SwipeRefreshLayout swipeContainer;
    private static String longestStreakName;



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

        ecoBottomText = findViewById(R.id.bottom_sheet_top_eco);
        dateBottomText = findViewById(R.id.bottom_sheet_top_date);
        longestStreakText = findViewById(R.id.longestStreakText);

        bottomSheet();
        updateBottomSheet();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.recyclerSwipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                SaveData saveData = new SaveData();
                saveData.readFromFile();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);





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
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivityForResult(intent, 500);
            }
        });

        //DONE: Implement this into habits model?
        Collections.sort(Habit.habits, Habit.HabitComparator);


        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            SaveData saveData = new SaveData();
            saveData.readFromFile();
            }
            initRecyclerView();
        }

    public void bottomSheet() {
        // get the bottom sheet view
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        final ImageButton btnBottomSheet = (ImageButton) findViewById(R.id.btn_bottomSheetToggle);

        // init the bottom sheet behavior
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        btnBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    btnBottomSheet.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_more));
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    btnBottomSheet.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_less));
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

        });

        // change the state of the bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // set hideable or not
        bottomSheetBehavior.setHideable(false);

        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;  
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //DO stuff when expanded
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        //DO stuff when collapsed
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MAIN", "Paused");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRecyclerView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 500 && resultCode == RESULT_OK) {
            updateRecyclerView();
        }
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
        if (id == R.id.action_refresh) {
            updateRecyclerView();
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

        testHabits.add((EconomicHabit) gumHabit);
        testHabits.add((DateHabit) sodaHabit);
        testHabits.add((EconomicHabit) poop);
        testHabits.add((DateHabit) scoop);
        SaveData saveData = new SaveData();

        for (Habit habit : testHabits) {
            if (habit instanceof DateHabit) {
                saveData.saveData(habit, 2);
                Habit.habits.add((DateHabit) habit);
            } else if (habit instanceof EconomicHabit) {
                saveData.saveData(habit, 1);
                Habit.habits.add((EconomicHabit) habit);
            }
        }
        testHabits.clear();
        MainActivity.updateRecyclerView();
        }

        private void removeData() {
            SaveData saveData = new SaveData();
            for (Habit habit: Habit.habits) {
                if(habit instanceof EconomicHabit) {
                    saveData.removeData(habit, 1);
                } else if(habit instanceof DateHabit) {
                    saveData.removeData(habit, 2);
                }
            }
            Habit.habits.clear();
            initRecyclerView();
        }

        public static void updateRecyclerView(){
            adapter.notifyDataSetChanged();
            favAdapter.notifyDataSetChanged();
            updateBottomSheet();
            swipeContainer.setRefreshing(false);
        }

        public static void updateBottomSheet() {
            totalSaved = 0;
            totalDays = 0;
            longestStreak = -1;
            for (Habit habit: Habit.habits) {
                if (habit instanceof EconomicHabit) {
                    totalSaved += ((EconomicHabit) habit).getProgress();
                    ecoBottomText.setText("Left for goals: " + totalSaved + "NOK");
                } if (habit instanceof DateHabit) {
                    totalDays += habit.getDaysFromStart();
                    dateBottomText.setText("Days without: " + totalDays + " days");
                    if (Habit.getDateDiff(habit.getFailDate(), new Date().getTime(), TimeUnit.DAYS) > longestStreak && habit.getFailDate() != 0) {
                        longestStreak = Habit.getDateDiff(habit.getFailDate(), new Date().getTime(),  TimeUnit.DAYS);
                        Log.d("BottomSheet", Long.toString(Habit.getDateDiff(habit.getFailDate(), new Date().getTime(),  TimeUnit.DAYS)));
                        longestStreakName = habit.getTitle();
                    } else {
                        longestStreak = -1;
                    }

                }
                if (longestStreak == -1) {
                    longestStreakText.setText("NO FAILS! Hooray!");
                }else {
                    longestStreakText.setText("Days since last fail: " + longestStreak + " (" + longestStreakName + ")");
                }
            }

        }

}

