package com.example.campingspot;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class pay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
    }
    public void clickToHome(View view) {
        Intent intent = new Intent(getApplicationContext(), FeedPage.class);
        startActivity(intent);
    }

}