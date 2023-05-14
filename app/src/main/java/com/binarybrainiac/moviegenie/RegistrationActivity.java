package com.binarybrainiac.moviegenie;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {
    EditText ed_userName, ed_email, ed_password;
    Button btn_signUp;
    FirebaseAuth fAuth;
    ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ed_userName = findViewById(R.id.ed_userName);
        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        btn_signUp = findViewById(R.id.btn_signUp);
        mProgressBar = findViewById(R.id.ProgressBar);
        btn_signUp.setOnClickListener(view -> {
            String Name = ed_userName.getText().toString().trim();
            String Email = ed_email.getText().toString().trim();
            String Password = ed_password.getText().toString().trim();
            if (TextUtils.isEmpty(Name)) {
                ed_userName.setError("User Name is Required");
            } else if (TextUtils.isEmpty(Email)) {
                ed_email.setError("Email Id is Required");
            } else if (TextUtils.isEmpty(Password)) {
                ed_password.setError("Password is Required");
            } else {
                registerUser(Name, Email, Password);
            }
        });
    }

    private void registerUser(String name, String email, String password) {
        mProgressBar.setVisibility(View.VISIBLE);
        fAuth = FirebaseAuth.getInstance();
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = fAuth.getCurrentUser();
                String memail = Objects.requireNonNull(user).getEmail();
                int index = Objects.requireNonNull(memail).indexOf('@');
                String utag = memail.substring(0, index);
                String G_UID = user.getUid();
                String G_Email = user.getEmail();
                Log.d("------>>", "signIn: " + G_UID);
                FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
                crashlytics.setUserId(utag);
                writeUserData(G_UID, G_Email, name);
//                    Toast.makeText(RegistrationActivity.this,"SignUp is completed...", Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
    }

    private void writeUserData(String g_uid, String g_email, String name) {
        Map<String, Object> UserMap = new HashMap<>();
        UserMap.put("email", g_email);
        UserMap.put("name", name);
        UserMap.put("uid", g_uid);

        WriteBatch batch = FirebaseFirestore.getInstance().batch();
        DocumentReference UserRef = FirebaseFirestore.getInstance().collection("user_data")
                .document(g_uid);
        batch.set(UserRef, UserMap, SetOptions.merge());
        try {
            batch.commit().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    Toast.makeText(RegistrationActivity.this, "SignUp is completed...Now SignIn...", Toast.LENGTH_LONG).show();
                    mProgressBar.setVisibility(View.GONE);
                } else { // data not save try
                    Toast.makeText(RegistrationActivity.this, "Please try after some time.", Toast.LENGTH_LONG).show();
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}