package model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import no.hiof.andrekar.badhabits.GlobalConstants;
import no.hiof.andrekar.badhabits.MainActivity;

public class SaveData  {
    //NOT NEEDED: Change this into internal storage, no need to use Downloads
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //DONE: Fix duplication problem.
    //DONE: Make Adapter refresh after sync


    public void readFromFile() {
        readFromFile(true);
    }

    public void readFromFile(final boolean animate) {
        final String TAG = "Firestoreread";

        //Clear existing arraylist if there are habits saved
        if (Habit.habits.size() > 0) {
            Habit.habits.clear();
            MainActivity.adapter.notifyDataSetChanged();
            MainActivity.favAdapter.notifyDataSetChanged();
        }

        //Start firebase pull
        db.collection(fbAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                        //Log.d(TAG, document.getId() + " => " + document.getData());
                                        //Log.d(TAG, Boolean.toString(document.getData().containsValue("DATE_HABIT")));
                                //Iterate through received data
                                if(document.getData().containsValue("DATE_HABIT")) {
                                    //Log.d(TAG, "Found date");
                                    //If the document is a date habit, add as a date habit.
                                    DateHabit tempHabit = document.toObject(DateHabit.class);
                                    Habit habit = (DateHabit) tempHabit;
                                    Habit.habits.add(habit);
                                        if(habit.getIsFavourite()) {
                                            //Update favourite adapter if it is a favourite.
                                            MainActivity.favAdapter.notifyDataSetChanged();
                                        } else {
                                            //Else update the main adapter.
                                            MainActivity.adapter.addP(Habit.habits.size());
                                        }
                                } else if (document.getData().containsValue("ECO_HABIT")) {
                                    //If the document is an eco habit, add as a eco habit.
                                    EconomicHabit tempHabit = document.toObject(EconomicHabit.class);
                                        Habit habit = (EconomicHabit) tempHabit;
                                        Habit.habits.add(habit);
                                        if(habit.getIsFavourite()) {
                                            //Update favourite adapter if it is a favourite.
                                            MainActivity.favAdapter.notifyDataSetChanged();
                                        } else {
                                            //Else update the main adapter.
                                            MainActivity.adapter.addP(Habit.habits.size());
                                        }
                                        MainActivity.favAdapter.notifyDataSetChanged();
                                }
                            }
                            //Log.d(TAG, "Refreshing");
                            //Update main ui again since we are done.
                            MainActivity.refreshUi();
                            //Create notification since we have fresh data.
                            MainActivity.createNotificationChannel();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
                //Set mainActivity swipe to refresh to done
                MainActivity.setRefreshing();

        }


    public void saveData(Habit habit, int typeHabit) {
        if (typeHabit == GlobalConstants.ECO_HABIT) {
            //Save into correct UID and on the correct habit.
            db.collection(fbAuth.getUid()).document(habit.getUid()).set(habit);
        } else if (typeHabit == GlobalConstants.DATE_HABIT) {
            db.collection(fbAuth.getUid()).document(habit.getUid()).set(habit);
        }
    }

    public void removeData(Habit habit, int typeHabit) {
        if (typeHabit == GlobalConstants.ECO_HABIT) {
            //Remove the data for the provided habit.
            db.collection(fbAuth.getUid()).document(habit.getUid()).delete();
        } else if (typeHabit == GlobalConstants.DATE_HABIT) {
            db.collection(fbAuth.getUid()).document(habit.getUid()).delete();
        }
    }
}
