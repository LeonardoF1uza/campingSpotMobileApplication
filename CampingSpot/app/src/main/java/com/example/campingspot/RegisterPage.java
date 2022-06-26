package com.example.campingspot;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.campingspot.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterPage extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        mAuth = FirebaseAuth.getInstance();
    }

    public void clickToRegister(View view) {
       registerUser(view);
    }
    public void clickToBack(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void registerUser(View view) {

        EditText name = findViewById(R.id.register_name);
        EditText email = findViewById(R.id.register_email);
        EditText password = findViewById(R.id.register_password);
        EditText password_confirm = findViewById(R.id.register_password_confirm);

        String name_value = name.getText().toString().trim();
        String email_value = email.getText().toString().trim();
        String password_value = password.getText().toString().trim();
        String confirm_password_value = password_confirm.getText().toString().trim();

        if(name_value.isEmpty() || email_value.isEmpty() || password_value.isEmpty() || confirm_password_value.isEmpty()) {
            Toast.makeText(RegisterPage.this, "Labels are empty.",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        if(!Patterns.EMAIL_ADDRESS.matcher(email_value).matches()) {
            Toast.makeText(RegisterPage.this, "Email invalid.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(password_value.length() < 6) {
            Toast.makeText(RegisterPage.this, "Password must have 6 chars.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password_value.equals(confirm_password_value)) {
            Toast.makeText(RegisterPage.this, "Passwords different.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email_value, password_value)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // store additional fields in firebase database
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            User user = new User(userId, name_value, email_value);

                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            db.collection("users").document(userId)
                                    .set(user.toDB())
                                    .addOnCompleteListener(task1 -> {

                                        if(task1.isSuccessful()) {
                                            Toast.makeText(RegisterPage.this, "Successful registration", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(RegisterPage.this, "USERS TABLE " + task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterPage.this, "FIREBASE AUTH " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

