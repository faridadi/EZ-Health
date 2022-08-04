package com.ecalm.ez_health.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.ecalm.ez_health.HomeActivity;
import com.ecalm.ez_health.IntroActivity;
import com.ecalm.e_calm.R;
import com.ecalm.ez_health.model.SharedPrefManager;

public class SplashScreenActivity extends AppCompatActivity {

    private int waktu = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPrefManager.getInstance(SplashScreenActivity.this).isLoggedIn()) {
                    startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                }else {
                    Intent first = new Intent(SplashScreenActivity.this, IntroActivity.class);
                    startActivity(first);
                }
                finish();
            }
        }, waktu);
    }
}