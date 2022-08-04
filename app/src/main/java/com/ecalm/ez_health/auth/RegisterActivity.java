package com.ecalm.ez_health.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ecalm.ez_health.HomeActivity;
import com.ecalm.e_calm.R;
import com.ecalm.ez_health.model.SharedPrefManager;
import com.ecalm.ez_health.model.User;
import com.ecalm.ez_health.presenter.RegisterContract;
import com.ecalm.ez_health.presenter.RegisterPresenter;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity implements RegisterContract.View {

    RegisterContract.Presenter presenter;
    EditText mUserName, mAge, mBodyWeight, mBodyHeight;
    Button btnRegister;
    TextView tvSignIn;
    int gender = 0;
    int aktivitas = 0;
    int program = 0;
    ProgressBar pgRegister;
    RadioGroup mGender;
    RadioGroup mActivity;
    RadioGroup mProgram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if the user is already logged in we will directly start the MainActivity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        mUserName = findViewById(R.id.edt_username);
        mAge = findViewById(R.id.edt_age);
        mBodyWeight = findViewById(R.id.edt_bodyweight);
        mBodyHeight = findViewById(R.id.edt_bodytall);
        btnRegister = findViewById(R.id.btn_signUp);
        pgRegister = findViewById(R.id.pg_register);
        mGender = findViewById(R.id.radioGroup);
        mActivity = findViewById(R.id.aktivitasGroup);
        mProgram = findViewById(R.id.programGroup);
        tvSignIn = findViewById(R.id.tv_signIn);

        //button untuk register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.register(RegisterActivity.this,mUserName, mAge, mBodyWeight, mBodyHeight, gender, aktivitas, program);
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        mGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.maleRadio:
                        gender = 0;
                        //Toasty.normal(RegisterActivity.this, "Male").show();
                        break;
                    case R.id.female:
                        gender = 1;
                        //Toasty.normal(RegisterActivity.this, "Female").show();
                        break;
                }
            }
        });

        mActivity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.aktivitas1Radio:
                        aktivitas = 0;
                        //Toasty.normal(RegisterActivity.this, "Aktivitas 1").show();
                        break;
                    case R.id.aktivitas2Radio:
                        aktivitas = 1;
                        //Toasty.normal(RegisterActivity.this, "Aktivitas 2").show();
                        break;
                    case R.id.aktivitas3Radio:
                        aktivitas = 2;
                        //Toasty.normal(RegisterActivity.this, "Aktivitas 3").show();
                        break;
                    case R.id.aktivitas4Radio:
                        aktivitas = 3;
                        //Toasty.normal(RegisterActivity.this, "Aktivitas 3").show();
                        break;
                }
            }
        });

        mProgram.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.program1Radio:
                        program = 0;
                        //Toasty.normal(RegisterActivity.this, "Program 1").show();
                        break;
                    case R.id.program2Radio:
                        program = 1;
                        //Toasty.normal(RegisterActivity.this, "Program 2").show();
                        break;
                    case R.id.program3Radio:
                        program = 2;
                        //Toasty.normal(RegisterActivity.this, "Program 3").show();
                        break;
                }
            }
        });
        presenter = new RegisterPresenter(this);
    }
    @Override
    public void finishRegister(User user) {
        Intent i =  new Intent(RegisterActivity.this, HomeActivity.class);
        startActivity(i);
        finish();
    }
    @Override
    public void failedRegister() {
        Toasty.warning(getApplicationContext(), "Pastikan semua terisi", Toast.LENGTH_SHORT).show();
    }
}