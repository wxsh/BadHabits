package no.hiof.andrekar.badhabits;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.SwitchPreference;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.preference.PreferenceManager;
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

import model.SaveData;

import static android.app.Activity.RESULT_OK;
import static android.media.CamcorderProfile.get;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class MySettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    static List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        Preference currency = findPreference(GlobalConstants.KEY_PREF_CURRENCY);
        currency.setSummary(sharedPreferences.getString(GlobalConstants.KEY_PREF_CURRENCY,""));

        Preference theme = findPreference(GlobalConstants.KEY_PREF_THEME);
        theme.setSummary(sharedPreferences.getString(GlobalConstants.KEY_PREF_THEME,""));



        Preference timePref = (Preference) findPreference(GlobalConstants.KEY_PREF_NOT_TIME);
        timePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //onClick

                DialogFragment newFragment  = new TimePickerFragment();
                newFragment.showNow(getActivity().getSupportFragmentManager(), "tid");

                return true;
            }
        });

        Preference googlePref = (Preference) findPreference(GlobalConstants.KEY_PREF_GOOGLE);

        if (!FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
            googlePref.setSummary(getString(R.string.settings_signed_in) + " " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }

        final Preference onboardPref = (Preference) findPreference(GlobalConstants.KEY_PREF_ONBOARD);
        onboardPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                editor.putBoolean(GlobalConstants.KEY_PREF_ONBOARD, false);
                editor.commit();

                restartSnackbar();
                return true;
            }
        });

        Preference onboardHabitPref = (Preference) findPreference(GlobalConstants.KEY_PREF_ONBOARDSHOWHABIT);
        onboardHabitPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                editor.putBoolean(GlobalConstants.KEY_PREF_ONBOARDSHOWHABIT, false);
                editor.commit();
                return true;
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        String userTheme = sharedPreferences.getString("key_theme", "");
        if (userTheme.equals("Light")){
            //view.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
        else if (userTheme.equals("Dark")){
            //view.setBackgroundColor(getResources().getColor(R.color.transparent));
        }

        return view;
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
            if( key.equals(GlobalConstants.KEY_PREF_GOOGLE)){

                if(sharedPreferences.getString(key,"").equals("Registrier")){
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().enableAnonymousUsersAutoUpgrade().setAvailableProviders(providers).setTheme(R.style.AppTheme).setLogo(R.mipmap.ic_launcher).build(), 200);
                }
                else if(sharedPreferences.getString(key,"").equals("Login")){
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).setTheme(R.style.AppTheme).setLogo(R.mipmap.ic_launcher).build(), 200);
                }
                else {
                    preference.setSummary("Login with google");
                }

            }else if (key.equals(GlobalConstants.KEY_PREF_THEME)) {
                Toast.makeText(getActivity(), "Restart app", Toast.LENGTH_LONG).show();
                preference.setSummary(sharedPreferences.getString(key,""));
                GlobalConstants.update(getContext());
                restartSnackbar();
            }else if(key.equals((GlobalConstants.KEY_PREF_NOT_ON))){
                Preference switchPreference = (Preference) findPreference(GlobalConstants.KEY_PREF_NOT_ON);
                if (sharedPreferences.getBoolean(GlobalConstants.KEY_PREF_NOT_ON, false))
                    switchPreference.setIcon(R.drawable.ic_notifications_active_black);
                else
                    switchPreference.setIcon(R.drawable.ic_notifications_off_black);
            }
            else
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
                Preference googlePref = (Preference) findPreference(GlobalConstants.KEY_PREF_GOOGLE);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    //userIdTW.setText("Du er logget inn som " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    googlePref.setSummary("Signed in with "+FirebaseAuth.getInstance().getCurrentUser().getUid());
                    SaveData saveData = new SaveData();
                    saveData.readFromFile();
                } else {
                    googlePref.setSummary("Something went wrong.");
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


    private void restartSnackbar() {
        // showing snack bar with restart  option
        Snackbar snackbar = Snackbar
                .make(getListView(), "Restart message here", Snackbar.LENGTH_LONG);
        snackbar.setAction("Restart Now", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Restarting app", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                int mPendingIntentId = 231;
                PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                System.exit(0);

                // undo is selected, restore the deleted item

            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();

    }
}