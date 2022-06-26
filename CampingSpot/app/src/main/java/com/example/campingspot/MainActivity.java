package com.example.campingspot;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickToLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(intent);
    }
    public void clickToRegister(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterPage.class);
        startActivity(intent);
    }
}