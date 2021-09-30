package com.ecalm.e_calm;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecalm.e_calm.math.Status;
import com.ecalm.e_calm.presenter.MainContract;
import com.ecalm.e_calm.presenter.MainPresenter;
import com.ecalm.e_calm.sqlite.DatabaseHelper;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    MainContract.Presenter presenter;

    RelativeLayout remainder;
    RelativeLayout edu;
    RelativeLayout calorie;
    RelativeLayout profile;
    TextView date, calorieCounter, status;
    DatabaseHelper db;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences = this.getSharedPreferences("fanregisterlogin", Context.MODE_PRIVATE);
        db = new DatabaseHelper(this);
        calorieCounter = findViewById(R.id.calorieCounter);
        remainder = findViewById(R.id.menuRemainder);
        edu = findViewById(R.id.menuEdu);
        calorie = findViewById(R.id.menuCalorie);
        date = findViewById(R.id.dateTextView);
        profile = findViewById(R.id.menuProfile);
        status = findViewById(R.id.statusText);

        Date dateNow = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        date.setText(df.format(dateNow));

        remainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.normal(MainActivity.this, "Comming Soon (:").show();
            }
        });

        edu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.normal(MainActivity.this, "Comming Soon (:").show();
            }
        });

        calorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CaptureActivity.class);
                startActivity(i);
                Toasty.info(MainActivity.this, "Scan your food").show();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.normal(MainActivity.this, "My Profile").show();
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        //init Presenter
        presenter = new MainPresenter(this,sharedPreferences, db);
        //check Permission
        presenter.checkPermission(this);
        //check health status
        presenter.healthyStatus();
        //check calorie counter
        presenter.calorieCounter();
    }

    @Override
    public void updateHealthy(String sts) {
        status.setText(sts);
    }

    @Override
    public void updateCalorieCounter(String text) {
        calorieCounter.setText(text);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
