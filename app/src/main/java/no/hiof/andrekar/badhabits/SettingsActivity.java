package no.hiof.andrekar.badhabits;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

//NO TIME: Handle merging if account exits and data is present for anon user in database?
//DONE change this to preferenceactivity


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String userTheme = sharedPreferences.getString("key_theme", "");
        if (userTheme.equals("Light")){
           setTheme(R.style.LightTheme);
        }
        else if (userTheme.equals("Dark")){
           setTheme(R.style.DarkTheme);
        }

        super.onCreate(savedInstanceState);

        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MySettingsFragment())
                .commit();
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);

    }
}
