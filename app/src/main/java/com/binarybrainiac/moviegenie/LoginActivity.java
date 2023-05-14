package com.binarybrainiac.moviegenie;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText ed_email, ed_password;
    Button btn_signIn;
    TextView btn_needHelp, btn_register;
    FirebaseAuth fAuth;
    ProgressBar mProgressBar;
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String NAME= "name";
    public static final String UID = "uid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        btn_signIn = findViewById(R.id.btn_signIn);
        btn_needHelp = findViewById(R.id.btn_needHelp);
        btn_register = findViewById(R.id.btn_register);
        fAuth = FirebaseAuth.getInstance();
        mProgressBar = findViewById(R.id.ProgressBar);

        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String shp_email = sharedPreferences.getString(EMAIL, null);
        String shp_name = sharedPreferences.getString(NAME, null);
        String shp_uid = sharedPreferences.getString(UID, null);
        if (shp_email != null || shp_name != null || shp_uid != null){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        btn_signIn.setOnClickListener(view -> {
            String email = ed_email.getText().toString().trim();
            String password = ed_password.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                ed_email.setError("Email is Required");
            } else if (TextUtils.isEmpty(password)) {
                ed_password.setError("Password is Required");
            } else {
                signIn(email, password);
            }
        });
        btn_register.setOnClickListener(view -> {
            registerUser();
        });
        btn_needHelp.setOnClickListener(this::forgetPassword);
    }

    private void signIn(String email, String password) {
        mProgressBar.setVisibility(View.VISIBLE);
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = fAuth.getCurrentUser();
                String memail = Objects.requireNonNull(user).getEmail();
                int index = Objects.requireNonNull(memail).indexOf('@');
                String utag = memail.substring(0, index);
                String G_UID = user.getUid();
                String G_Email = user.getEmail();
                Log.d("------>>", "signIn: "+G_UID);
                FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
                crashlytics.setUserId(utag);
                getUseData(G_UID, password);
            } else if (Objects.equals(Objects.requireNonNull(task.getException()).getMessage(), "There is no user record corresponding to this identifier. The user may have been deleted.")) {
                Toast.makeText(LoginActivity.this, "This Email Id and Password does not exit. Please SignUp...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                mProgressBar.setVisibility(View.GONE);
            } else {
                Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getUseData(String G_UID, String password){
        DocumentReference StudentRef = FirebaseFirestore.getInstance()
                .collection("user_data")
                .document(G_UID);
        StudentRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult() != null && task.getResult().exists()) {
                    String Email = (String) task.getResult().get("email");
                    String Name = (String) task.getResult().get("name");
                    String UID = (String) task.getResult().get("uid");
                    Log.d("-------=*", "onComplete: "+Name);
                    userExist(Email, Name, UID, password);
                } else {
                    Log.d("---====>>", "FAIL");
                }
            } else {
                Log.d("---====>>", "FAIL: ");
            }
        });
    }

    private void userExist(String email, String name, String uid, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL, email);
        editor.putString(PASSWORD, password);
        editor.putString(NAME, name);
        editor.putString(UID, uid);
        editor.apply();
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        mProgressBar.setVisibility(View.GONE);
    }

    private void registerUser() {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void forgetPassword(View view) {
        final EditText resetMail = new EditText(view.getContext());
        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(LoginActivity.this, R.style.AlertDialog);
        passwordResetDialog.setTitle("Reset Password");
        passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
        passwordResetDialog.setView(resetMail);

        passwordResetDialog.setPositiveButton("Yes", (dialog, which) -> {

            String mail = resetMail.getText().toString();
            fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(aVoid -> Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "Error ! Reset link is Not Sent." + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        passwordResetDialog.create().show();
    }
}