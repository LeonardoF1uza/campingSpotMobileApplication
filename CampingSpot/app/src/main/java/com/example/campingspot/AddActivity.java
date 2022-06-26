package com.example.campingspot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.campingspot.databinding.ActivityMainBinding;
import com.example.campingspot.model.Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class AddActivity extends AppCompatActivity {

    private static final String TAG = "AddPage";
    private static final int PERMISSION_CODE = 1000;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final int IMAGE_PICK_CODE = 1000;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference;
    ActivityMainBinding binding;
    Uri imageUri ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_add);

        EditText initial_date = findViewById(R.id.input_initial_date);
        EditText final_date = findViewById(R.id.input_final_date);

        Button imageButton = findViewById(R.id.pick_photo);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else {
                        pickImageFromGallery();
                    }
                }
                else {
                    pickImageFromGallery();
                }
            }
        });
        final_date.addTextChangedListener(new TextWatcher() {
            private String current = "";
            final String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void onTextChanged(CharSequence seq, int start, int before, int count) {
                if (!seq.toString().equals(current)) {
                    String clean = seq.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);

                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    final_date.setText(current);
                    final_date.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

        });
        initial_date.addTextChangedListener(new TextWatcher() {
            private String current = "";
            final String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void onTextChanged(CharSequence seq, int start, int before, int count) {
                if (!seq.toString().equals(current)) {
                    String clean = seq.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);

                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    initial_date.setText(current);
                    initial_date.setSelection(sel < current.length() ? sel : current.length());
                }
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

        });
    }

    public void clickToHome(View view) {
        Intent intent = new Intent(getApplicationContext(), FeedPage.class);
        startActivity(intent);
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                }
                else {
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT);
                }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            ImageView imageView = findViewById(R.id.image);
            imageView.setImageURI(data.getData());
            imageUri = data.getData();
        }
    }

    public void clickToSave(View view) {
        db.collection("activities").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                EditText initial_date = findViewById(R.id.input_initial_date);
                EditText final_date = findViewById(R.id.input_final_date);
                EditText name = findViewById(R.id.input_name);
                EditText location = findViewById(R.id.input_location);
                EditText description = findViewById(R.id.input_description);
                EditText capacity = findViewById(R.id.input_capacity);


                if (task.isSuccessful()) {
                    int id_activity = task.getResult().size() + 1;
                    Log.d("DEBUG_ADD", id_activity + "");

                    String name_value = name.getText().toString();
                    String description_value = description.getText().toString();
                    String capacity_value = capacity.getText().toString();
                    String location_value = location.getText().toString();
                    String duration_initial_value = initial_date.getText().toString();
                    String duration_final_value = final_date.getText().toString();

                    if (name_value.isEmpty() || description_value.isEmpty() || capacity_value.isEmpty() || location_value.isEmpty() || duration_final_value.isEmpty() || duration_initial_value.isEmpty()) {
                        Toast.makeText(AddActivity.this, "Labels empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                        int day_id = Integer.parseInt(duration_initial_value.substring(0,2));
                    int mon_id = Integer.parseInt(duration_initial_value.substring(3,5));
                    int year_id = Integer.parseInt(duration_initial_value.substring(6, 10));
                    int day_fd = Integer.parseInt(duration_final_value.substring(0,2));
                    int mon_fd = Integer.parseInt(duration_final_value.substring(3,5));
                    int year_fd = Integer.parseInt(duration_final_value.substring(6, 10));

                    if (year_id > year_fd) {
                        return ;
                    } else {
                        if (mon_id > mon_fd && year_id >= year_fd) {
                            return ;
                        } else {
                            if (day_id > day_fd && mon_id >= mon_fd && year_id >= year_fd) {
                                return ;
                            }
                        }
                    }

                    if (!name_value.isEmpty() && !description_value.isEmpty() && !capacity_value.isEmpty() && !location_value.isEmpty() && !duration_final_value.isEmpty() && !duration_initial_value.isEmpty()) {
                        Activity activity = new Activity(name_value, location_value, description_value, duration_initial_value, duration_final_value, capacity_value);

                        db.collection("activities").document(String.valueOf(id_activity))
                                .set(activity.toDB())
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()) {
                                        uploadImage(name_value);
                                        Toast.makeText(AddActivity.this, "Successful registration", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(AddActivity.this, FeedPage.class);
                                        try {
                                            TimeUnit.SECONDS.sleep(3);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(AddActivity.this, "ACTIVITIES TABLE " + task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

                    } else {
                        Log.w(TAG, "addActivity:failure");
                        Toast.makeText(AddActivity.this, "Labels are empty.",
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void uploadImage(String name_value) {
        storageReference = storage.getReference("images/"+name_value+".jpg");
        try {
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AddActivity.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            return;
        }
    }

}