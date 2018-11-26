package no.hiof.andrekar.badhabits;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
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

        if (habit instanceof EconomicHabit) {
            //Build alert dialog.
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(mContext.getString(R.string.popup_failed_ecohabit));
            builder.setTitle(mContext.getString(R.string.popup_failed_title));
            //Create inputfield for amount failed
            final EditText input = new EditText(view.getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

            builder.setPositiveButton(mContext.getString(R.string.popup_positive_eco), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SaveData saveData = new SaveData();
                    failedAmount = input.getText().toString();
                    //Float parse and rounding to get an integer
                    ((EconomicHabit) habit).increaseFailedTotal(Math.round(Float.parseFloat(failedAmount)));
                    habit.setFailDate(new Date().getTime());
                    saveData.saveData(Habit.habits.get(mPosition), GlobalConstants.ECO_HABIT);

                    if(mReload) {
                        mActivity.recreate();
                    }
                    MainActivity.adapter.notifyDataSetChanged();
                    MainActivity.favAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton(mContext.getString(R.string.popup_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            //Show built alertdialog.
            AlertDialog ecoAlert = builder.create();
            ecoAlert.show();

        } else if (habit instanceof DateHabit) {

            final AlertDialog.Builder failedBuilder = new AlertDialog.Builder(mContext);
            failedBuilder.setMessage(mContext.getString(R.string.popup_failed_datehabit))
                    .setTitle(mContext.getString(R.string.popup_failed_title))
                    .setPositiveButton(mContext.getString(R.string.popup_positive_date), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    Date currentTime = Calendar.getInstance().getTime();
                    if(((DateHabit) habit).getFailedDays() == 0){
                        Toast.makeText(mContext, mContext.getString(R.string.popup_failed_date_multiple), Toast.LENGTH_SHORT).show();
                    } else{
                        habit.setFailDate(currentTime.getTime());
                    }
                    SaveData saveData = new SaveData();
                    saveData.saveData(Habit.habits.get(mPosition), GlobalConstants.DATE_HABIT);

                    if(mReload) {
                        mActivity.recreate();
                    }
                    MainActivity.adapter.notifyDataSetChanged();
                    MainActivity.favAdapter.notifyDataSetChanged();

                }
            }).setNegativeButton(mContext.getString(R.string.popup_cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // CANCEL AND DO NOTHING
                }
            });
            // Create the AlertDialog object and return it
            AlertDialog dialog = failedBuilder.create();
            dialog.show();
        }
    }
}
