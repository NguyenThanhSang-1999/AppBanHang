package com.example.cuahangdientuonline.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start home activity
        gotoSplashActivity();
    }
    private void gotoSplashActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, DangNhapActivity.class);
                startActivity(intent);
            }
        }, SPLASH_DELAY);

        Toast.makeText(SplashActivity.this,
                "Welcome to SH Shop", Toast.LENGTH_SHORT).show();
    }
}