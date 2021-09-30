package com.ecalm.e_calm.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ecalm.e_calm.MainActivity;
import com.ecalm.e_calm.R;
import com.ecalm.e_calm.SharedPrefManager;
import com.ecalm.e_calm.math.BMR;
import com.ecalm.e_calm.model.User;
import com.ecalm.e_calm.network.ConnectivityHelper;
import com.ecalm.e_calm.presenter.RegisterContract;
import com.ecalm.e_calm.presenter.RegisterPresenter;

import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity implements RegisterContract.View {

    RegisterContract.Presenter presenter;
    EditText mUserName, mAge, mBodyWeight, mBodyTall;
    Button btnRegister;
    TextView tvSignIn;
    int gender = 0;
    ProgressBar pgRegister;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if the user is already logged in we will directly start the MainActivity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        mUserName = findViewById(R.id.edt_username);
        mAge = findViewById(R.id.edt_age);
        mBodyWeight = findViewById(R.id.edt_bodyweight);
        mBodyTall = findViewById(R.id.edt_bodytall);
        btnRegister = findViewById(R.id.btn_signUp);
        pgRegister = findViewById(R.id.pg_register);
        radioGroup = findViewById(R.id.radioGroup);
        tvSignIn = findViewById(R.id.tv_signIn);

        //button untuk register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.register(mUserName, mAge, mBodyWeight, mBodyTall, gender);
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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.maleRadio:
                        gender = 0;
                        Toasty.normal(RegisterActivity.this, "Male").show();
                        break;
                    case R.id.female:
                        gender = 1;
                        Toasty.normal(RegisterActivity.this, "Female").show();
                        break;
                }
            }
        });
        presenter = new RegisterPresenter(this);
    }

    @Override
    public void finishRegister(User user) {
        SharedPrefManager.getInstance(RegisterActivity.this).userRegister(user);
        Intent i =  new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void failedRegister() {
        Toasty.warning(getApplicationContext(), "Pastikan semua terisi", Toast.LENGTH_SHORT).show();
    }
}