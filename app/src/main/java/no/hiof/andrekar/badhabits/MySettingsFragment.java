package no.hiof.andrekar.badhabits;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Map;

public class MySettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        Preference currency = findPreference(SettingsActivity.KEY_PREF_CURRENCY);
        currency.setSummary(sharedPreferences.getString(SettingsActivity.KEY_PREF_CURRENCY,""));

        Preference email = findPreference(SettingsActivity.KEY_PREF_EMAIL);
        email.setSummary(sharedPreferences.getString(SettingsActivity.KEY_PREF_EMAIL,""));

        Preference googlePref = (Preference) findPreference(SettingsActivity.KEY_PREF_GOOGLE);
        googlePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {

                getActivity().setContentView(R.layout.activity_settings);

                return true;
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key) {

        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        Preference preference = findPreference(key);
        try {
            preference.setSummary(sharedPreferences.getString(key,""));
        }catch (Exception e){
            e.printStackTrace();
        }
    }




}