package no.hiof.andrekar.badhabits;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import model.SaveData;

import static android.app.Activity.RESULT_OK;

public class MySettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    static List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

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


                //Denne kjøres hvis brukeren skal registreres.
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().enableAnonymousUsersAutoUpgrade().setAvailableProviders(providers).build(), 200);

                //Denne skal kjøres hvis brukeren har en eksisterende konto.
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(), 200);


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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    //userIdTW.setText("Du er logget inn som " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    SaveData saveData = new SaveData();
                    saveData.readFromFile();
                } else {
                    //userIdTW.setText("Error: sign in failed.");
                }
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }



}