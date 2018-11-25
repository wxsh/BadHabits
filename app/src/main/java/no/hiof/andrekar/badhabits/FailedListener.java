package no.hiof.andrekar.badhabits;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import model.DateHabit;
import model.EconomicHabit;
import model.Habit;
import model.SaveData;

public class FailedListener implements OnClickListener {

    private int mPosition;
    private String failedAmount;
    private Context mContext;
    private Boolean mReload = false;
    private Activity mActivity;

    public FailedListener(int position, Context context) {
        this.mPosition = position;
        this.mContext = context;
    }

    public FailedListener(int position, Context context, Boolean reload, Activity activity) {
        this.mPosition = position;
        this.mContext = context;
        this.mReload = reload;
        this.mActivity = activity;
    }

    @Override
    public void onClick(View view) {
        final Habit habit = Habit.habits.get(mPosition);
        Log.d("TAG", "clicked on failed button");

        if (habit instanceof EconomicHabit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Don't worry, even if you fail, you can still do this! How much did you spend?");
            final EditText input = new EditText(view.getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SaveData saveData = new SaveData();
                    failedAmount = input.getText().toString();
                    ((EconomicHabit) habit).increaseFailedTotal(Integer.parseInt(failedAmount));
                    habit.setFailDate(new Date().getTime());
                    saveData.saveData(Habit.habits.get(mPosition), 1);

                    if(mReload) {
                        mActivity.recreate();
                    }
                    MainActivity.adapter.notifyDataSetChanged();
                    MainActivity.favAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog ecoAlert = builder.create();
            ecoAlert.show();

        } else if (habit instanceof DateHabit) {


            //DONE: editButton onclick
            final AlertDialog.Builder failedBuilder = new AlertDialog.Builder(mContext);
            failedBuilder.setMessage("Don't worry, even if you fail, you can still do this! Do you want to reset the days since last fail?").setTitle("Failed habit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    Date currentTime = Calendar.getInstance().getTime();
                    if(((DateHabit) habit).getToday() == 0){
                        Toast.makeText(mContext, "Already failed this habit today.", Toast.LENGTH_SHORT).show();
                    } else{
                        habit.setFailDate(currentTime.getTime());
                    }
                    SaveData saveData = new SaveData();
                    saveData.saveData(Habit.habits.get(mPosition), 2);

                    if(mReload) {
                        mActivity.recreate();
                    }
                    MainActivity.adapter.notifyDataSetChanged();
                    MainActivity.favAdapter.notifyDataSetChanged();

                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // CANCEL AND DO NOTHING
                }
            });
            // Create the AlertDialog object and return it
            AlertDialog dialog = failedBuilder.create();
            //ecoAlert.show();
            dialog.show();
            //dialog.show();

        }
    }
}
