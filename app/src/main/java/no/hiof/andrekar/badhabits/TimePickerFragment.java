package no.hiof.andrekar.badhabits;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int hour = 12;
        int minute = 0;

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user

        //TODO: change the value in SharedPref
        //TODO: Change to sliderClock

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(SettingsActivity.KEY_PREF_NOT_TIME, hourOfDay + ((minute/100.0f * 60.0f))/100.0f);
        editor.commit();


    }
}