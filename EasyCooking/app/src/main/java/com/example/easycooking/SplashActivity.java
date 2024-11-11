package com.example.easycooking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000;
    private EditText nameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        nameInput = findViewById(R.id.nameInput);
        Button getStartedButton = findViewById(R.id.getStartedButton);

        new Handler().postDelayed(() -> {
            // Hide the progress bar and show the input box and button
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                float x = event.getRawX() + view.getLeft() - location[0];
                float y = event.getRawY() + view.getTop() - location[1];

                if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) {
                    hideKeyboard(view);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
