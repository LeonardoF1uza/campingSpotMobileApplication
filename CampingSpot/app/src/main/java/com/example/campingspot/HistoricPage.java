package com.example.campingspot;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class HistoricPage extends AppCompatActivity {

    private static final String TAG = "HistoricPage";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_page);
        LinearLayout linearLayout = findViewById(R.id.linear_layout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(900, 700);

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
                                Log.d("DEBUG_HISTORIC", String.valueOf(historic));
                                List<String> list_historic = (List<String>) convertObjectToList(historic);
                                for (String id_activity : list_historic) {
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
                                                            Object name = document.getData().get("name");

                                                            ImageView imageView = new ImageView(HistoricPage.this);

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

                                                            Space spaceView = new Space(HistoricPage.this);
                                                            LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80);
                                                            spaceView.setLayoutParams(spaceParams);

                                                            TextView textView = new TextView(HistoricPage.this);
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

    public void clickToSearch(View view) {
        Intent intent = new Intent(getApplicationContext(), SearchPage.class);
        startActivity(intent);
    }
    public void clickToProfile(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
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