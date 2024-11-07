package com.example.easycooking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        EditText nameInput = findViewById(R.id.nameInput);
        Button getStartedButton = findViewById(R.id.getStartedButton);

        new Handler().postDelayed(() -> {
            // Hide loading icon and show the input and button
            progressBar.setVisibility(View.GONE);
            nameInput.setVisibility(View.VISIBLE);
            getStartedButton.setVisibility(View.VISIBLE);
        }, SPLASH_DELAY);

        getStartedButton.setOnClickListener(v -> {
            String userName = nameInput.getText().toString().trim();
            if (userName.isEmpty()) {
                Toast.makeText(SplashActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
            } else {
                Intent nextScreen = new Intent(SplashActivity.this, MainActivity.class);
                nextScreen.putExtra("USER_NAME", userName);
                startActivity(nextScreen);
                finish();
            }
        });
    }
}
