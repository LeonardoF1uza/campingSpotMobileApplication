package com.example.campingspot;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfilePage extends AppCompatActivity {

    private static final String TAG = "ProfilePage";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        TextView name = findViewById(R.id.name_profile);
        TextView email = findViewById(R.id.email_profile);

        Log.d("DEBUG_PROFILE", mAuth.getUid());

        db.collection("users").document(mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("Getting data from DB", "... Complete.");
                                Object name_value = document.getData().get("name");
                                Object email_value = document.getData().get("email");
                                name.setText(String.valueOf(name_value));
                                email.setText(String.valueOf(email_value));
                            }
                        } else {
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                    }
                });

    }
    public void clickToHome(View view) {
        Intent intent = new Intent(getApplicationContext(), FeedPage.class);
        startActivity(intent);
    }
    public void clickToHistoric(View view) {
        Intent intent = new Intent(getApplicationContext(), HistoricPage.class);
        startActivity(intent);
    }
    public void clickToSearch(View view) {
        Intent intent = new Intent(getApplicationContext(), SearchPage.class);
        startActivity(intent);
    }
    public void clickToLogout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(view.getContext(), LoginPage.class);
        startActivity(intent);
    }
}