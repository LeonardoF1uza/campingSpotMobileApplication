package com.example.campingspot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campingspot.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Details extends AppCompatActivity {
    private static final String TAG = "DetailsPage";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        LinearLayout linearLayout = findViewById(R.id.linear_layout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(900, 700);
        String id_activity = getIntent().getStringExtra("id_activity");

        db.collection("activities").document(id_activity).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("Getting data from DB", "... Complete.");
                                Object location = document.getData().get("location");
                                Object image = document.getData().get("image");
                                Object initial_date = document.getData().get("duration_initial");
                                Object final_date = document.getData().get("duration_final");
                                Object capacity_value = document.getData().get("capacity");
                                Object description_value = document.getData().get("description");

                                Space spaceView = new Space(Details.this);
                                LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80);
                                spaceView.setLayoutParams(spaceParams);

                                TextView name = new TextView(Details.this);
                                name.setText("Activity " + id_activity + ", " + String.valueOf(location));
                                name.setGravity(Gravity.CENTER);

                                TextView duration = new TextView(Details.this);
                                duration.setText("\nSince   " + String.valueOf(initial_date) + "   to   " + String.valueOf(final_date));
                                duration.setGravity(Gravity.CENTER);

                                TextView capacity = new TextView(Details.this);
                                capacity.setText("\n" + String.valueOf(capacity_value) + " people");
                                capacity.setGravity(Gravity.CENTER);

                                TextView description = new TextView(Details.this);
                                description.setText("\n" + String.valueOf(description_value));
                                description.setGravity(Gravity.CENTER);

                                int id = getResources().getIdentifier(String.valueOf(image), "drawable", getPackageName());
                                ImageView imageView = new ImageView(Details.this);
                                imageView.setImageResource(id);
                                imageView.setLayoutParams(layoutParams);
                                imageView.setForegroundGravity(Gravity.CENTER);

                                linearLayout.addView(imageView);
                                linearLayout.addView(name);
                                linearLayout.addView(duration);
                                linearLayout.addView(capacity);
                                linearLayout.addView(description);
                                linearLayout.addView(spaceView);
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

    public void clickToBook(View view) {
        db.collection("users").document(mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("Getting data from DB", "... Complete.");
                                Object historic = document.getData().get("historic");
                                ArrayList<String> hist = (ArrayList<String>) convertObjectToList(historic);
                                Log.d("DEBUG_DETAILS", String.valueOf(hist));

                                String id_activity = getIntent().getStringExtra("id_activity");
                                hist.add(id_activity);
                                Log.d("DEBUG_DETAILS", String.valueOf(hist));
                                User user = new User(hist);

                                db.collection("users").document(mAuth.getUid())
                                        .update(user.toDB_2())
                                        .addOnCompleteListener(task1 -> {
                                            if(task1.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Successful paid", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(getApplicationContext(), HistoricPage.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(getApplicationContext(), "USER TABLE " + task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });

                            }
                        } else {
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                    }
                });
    }

    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[])obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>)obj);
        }
        return list;
    }
}