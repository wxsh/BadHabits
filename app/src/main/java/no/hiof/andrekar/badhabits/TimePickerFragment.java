package no.hiof.andrekar.badhabits;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    SharedPreferences preferences;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());

        Calendar c = Calendar.getInstance();

        float hourWc = preferences.getFloat(GlobalConstants.KEY_PREF_NOT_TIME, 0.0f);
        int hour = (int)(hourWc);
        int minute = (int)((hourWc*60)%60);

        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog tpd = new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
        return tpd;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user

        //DONE: change the value in SharedPref
        SharedPreferences.Editor editor = preferences.edit();
        float timeInHours = ((float)hourOfDay + (float)(minute)/(float)60);
        editor.putFloat(GlobalConstants.KEY_PREF_NOT_TIME, timeInHours);
        editor.commit();

    }
}