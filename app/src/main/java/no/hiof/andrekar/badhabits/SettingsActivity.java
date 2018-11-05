package no.hiof.andrekar.badhabits;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private EditText regEmail, regPassword;
    private Button buttonRegUser;
    private TextView userIdTW;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth = FirebaseAuth.getInstance();

         regEmail = findViewById(R.id.registerEmail);
         regPassword = findViewById(R.id.registerPassword);
         buttonRegUser = findViewById(R.id.buttonRegisterUser);
         userIdTW = findViewById(R.id.userIdTextView);

         // Button to register the user
         buttonRegUser.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
         final String email = regEmail.getText().toString();
         String password = regPassword.getText().toString();

         if(TextUtils.isEmpty(email)) {
         Toast.makeText(getApplicationContext(), "Enter email address.", Toast.LENGTH_SHORT).show();
         }

         if(TextUtils.isEmpty(password)) {
         Toast.makeText(getApplicationContext(), "Enter password.", Toast.LENGTH_SHORT).show();
         }

         mAuth.createUserWithEmailAndPassword(email, password)
         .addOnCompleteListener(SettingsActivity.this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
        Toast.makeText(SettingsActivity.this, "createUserWithEmail:onComplete: " + task.isSuccessful(), Toast.LENGTH_SHORT).show();

        if(!task.isSuccessful()) {
        Toast.makeText(SettingsActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
        Log.d("SettingsActivity:", task.getException().toString());
        } else {
        Toast.makeText(SettingsActivity.this, "Successfully created user: " + email, Toast.LENGTH_SHORT).show();
        }

        }
        });

         }
         });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }



    private void updateUI(FirebaseUser user) {
        if(user != null) {
            userIdTW.setText("User ID: " + user.getUid());
        } else {
            userIdTW.setText("Error: sign in failed.");
        }
    }
}