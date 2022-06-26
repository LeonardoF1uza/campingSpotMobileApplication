package com.example.campingspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ResultsPage extends AppCompatActivity {

    private static final String TAG = "ResultsPage";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference storageReference;
    Boolean name = true;
    Boolean location = true;
    Boolean capacity = true;
    Boolean date = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_page);

        // GET FILTERS
        String name_value = getIntent().getStringExtra("NAME");
        if (name_value.isEmpty()) {
            name = false;
        }
        String location_value = getIntent().getStringExtra("LOCATION");
        if (location_value.isEmpty()) {
            location = false;
        }
        String capacity_value = getIntent().getStringExtra("CAPACITY");
        if (capacity_value.isEmpty()) {
            capacity = false;
        }
        String date_initial_value = getIntent().getStringExtra("START");
        String date_final_value = getIntent().getStringExtra("FINAL");
        if (date_initial_value.isEmpty()) {
            date = false;
        }
        if (date_final_value.isEmpty()) {
            date = false;
        }

        LinearLayout linearLayout = findViewById(R.id.linear_layout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(900, 700);

        db.collection("activities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> list_document = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list_document.add(document.getId());
                            }
                            Log.d("DEBUG_RESULTS_LIST_DOCUMENTS", list_document.toString());

                            for (String id_activity : list_document) {
                                db.collection("activities").document(id_activity).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        if (name) {
                                                            Object name_db = document.getData().get("name");
                                                            if (!String.valueOf(name_db).contains(name_value)) {
                                                                return;
                                                            }
                                                        }
                                                        if (location) {
                                                            Object location_db = document.getData().get("location");
                                                            if (!String.valueOf(location_db).contains(location_value)) {
                                                                return;
                                                            }
                                                        }
                                                        if (capacity) {
                                                            Object capacity_db = document.getData().get("capacity");
                                                            int capacity_db_value = Integer.parseInt(String.valueOf(capacity_db));
                                                            if (capacity_db_value < Integer.parseInt(capacity_value)) {
                                                                return;
                                                            }
                                                        }
                                                        if (date) {
                                                            Log.d("DEBUG_RES", "entre");
                                                            String start_date_db = String.valueOf(document.getData().get("duration_initial"));
                                                            String end_date_db = String.valueOf(document.getData().get("duration_final"));

                                                            int start_date_db_day = Integer.parseInt(start_date_db.substring(0, 2));
                                                            int start_date_db_mon = Integer.parseInt(start_date_db.substring(3, 5));
                                                            int start_date_db_year = Integer.parseInt(start_date_db.substring(6, 10));
                                                            int date_initial_value_day = Integer.parseInt(date_initial_value.substring(0,2));
                                                            int date_initial_value_mon = Integer.parseInt(date_initial_value.substring(3,5));
                                                            int date_initial_value_year = Integer.parseInt(date_initial_value.substring(6,10));

                                                            int end_date_db_day = Integer.parseInt(end_date_db.substring(0, 2));
                                                            int end_date_db_mon = Integer.parseInt(end_date_db.substring(3, 5));
                                                            int end_date_db_year = Integer.parseInt(end_date_db.substring(6, 10));
                                                            int date_final_value_day = Integer.parseInt(date_final_value.substring(0,2));
                                                            int date_final_value_mon = Integer.parseInt(date_final_value.substring(3,5));
                                                            int date_final_value_year = Integer.parseInt(date_final_value.substring(6,10));

                                                            if (date_final_value_year < start_date_db_year ) {
                                                                return;
                                                            } else {
                                                                if (date_final_value_mon < start_date_db_mon && date_final_value_year <= start_date_db_year) {
                                                                    return;
                                                                } else {
                                                                    if (date_final_value_day < start_date_db_day && date_final_value_mon <= start_date_db_mon && date_final_value_year <= start_date_db_year) {
                                                                        return;
                                                                    }
                                                                }
                                                            }

                                                            if (end_date_db_year < date_initial_value_year ) {
                                                                return;
                                                            } else {
                                                                if (end_date_db_mon < date_initial_value_mon && end_date_db_year <= date_initial_value_year) {
                                                                    return;
                                                                } else {
                                                                    if (end_date_db_day < date_initial_value_day && end_date_db_mon <= date_initial_value_mon && end_date_db_year <= date_initial_value_year) {
                                                                        return;
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        Log.d("Getting data from DB", "... Complete.");
                                                        Object location = document.getData().get("location");
                                                        Object image = document.getData().get("image");
                                                        Object name = document.getData().get("name");

                                                        ImageView imageView = new ImageView(ResultsPage.this);

                                                        storageReference = FirebaseStorage.getInstance().getReference().child("images/"+String.valueOf(image)+".jpg");
                                                        try {
                                                            final File localFile = File.createTempFile(String.valueOf(image), "");
                                                            storageReference.getFile(localFile)
                                                                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                                            imageView.setImageBitmap(bitmap);
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                }
                                                            });
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }

                                                        Space spaceView = new Space(ResultsPage.this);
                                                        LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80);
                                                        spaceView.setLayoutParams(spaceParams);

                                                        TextView textView = new TextView(ResultsPage.this);
                                                        textView.setText(String.valueOf(name) + ", " + String.valueOf(location));
                                                        textView.setGravity(Gravity.CENTER);
                                                        textView.isClickable();
                                                        textView.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent intent = new Intent(getApplicationContext(), Details.class);
                                                                intent.putExtra("id_activity", id_activity);
                                                                startActivity(intent);
                                                            }
                                                        });

                                                        int id = getResources().getIdentifier(String.valueOf(image), "drawable", getPackageName());
                                                        imageView.setLayoutParams(layoutParams);
                                                        imageView.setForegroundGravity(Gravity.CENTER);
                                                        imageView.isClickable();
                                                        imageView.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent intent = new Intent(getApplicationContext(), Details.class);
                                                                intent.putExtra("id_activity", id_activity);
                                                                startActivity(intent);
                                                            }
                                                        });

                                                        linearLayout.addView(imageView);
                                                        linearLayout.addView(textView);
                                                        linearLayout.addView(spaceView);
                                                    }
                                                } else {
                                                    Log.d(TAG, "Failed with: ", task.getException());
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                    }
                });
    }

    public void clickToSearch(View view) {
        Intent intent = new Intent(getApplicationContext(), SearchPage.class);
        startActivity(intent);
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