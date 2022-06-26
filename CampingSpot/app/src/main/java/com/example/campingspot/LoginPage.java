package com.example.campingspot;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class LoginPage extends AppCompatActivity {


    private static final String TAG = "LoginPage";

    private EditText emailText, passwordText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        emailText = findViewById(R.id.login_email);
        passwordText = findViewById(R.id.login_password);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null) {
           Log.d(TAG, "signInWithEmail:success");
           FirebaseUser userId = mAuth.getCurrentUser();
           Intent intent = new Intent(LoginPage.this, FeedPage.class);
           intent.putExtra("userId", userId);
           startActivity(intent);

        }
    }

    public void clickToLogin(View view) {
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        Log.d("DEBUG_LOGIN_EMAIL", email);
        Log.d("DEBUG_LOGIN_PASSWWORD", password);

        if (!email.isEmpty() || !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser userId = mAuth.getCurrentUser();
                                Intent intent = new Intent(LoginPage.this, FeedPage.class);
                                intent.putExtra("userId", userId);
                                startActivity(intent);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginPage.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Log.w(TAG, "signInWithEmail:failure");
            Toast.makeText(LoginPage.this, "Labels are empty.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void clickToBack(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}