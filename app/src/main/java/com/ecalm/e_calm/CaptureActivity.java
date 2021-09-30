package com.ecalm.e_calm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.camerakit.CameraKitView;
import com.ecalm.e_calm.adapter.FoodAdapter;
import com.ecalm.e_calm.model.Food;
import com.ecalm.e_calm.presenter.CaptureContract;
import com.ecalm.e_calm.presenter.CaptureInteractor;
import com.ecalm.e_calm.presenter.CapturePresenter;
import com.ecalm.e_calm.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class CaptureActivity extends AppCompatActivity implements CaptureContract.View {

    CaptureContract.Presenter presenter;

    private Button btnCapture, btnRecapture;
    private ImageView imageViewResult;
    private CameraKitView cameraKitView;

    RecyclerView recyclerView;
    FoodAdapter foodAdapter;

    TextView done, back;
    ProgressDialog mProgressDialog;

    SharedPreferences sharedPreferences;
    AssetManager asset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asset = getAssets();

        setContentView(R.layout.activity_capture);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mProgressDialog = new ProgressDialog(CaptureActivity.this);

        recyclerView = findViewById(R.id.Recycler);
        foodAdapter = new FoodAdapter();

        //Recycle item click listener
        foodAdapter.setOnItemClickListener(new FoodAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, android.view.View v) {
                presenter.addFood(position);
            }

            @Override
            public void onItemLongClick(int position, android.view.View v) {

            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CaptureActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(foodAdapter);

        cameraKitView = findViewById(R.id.camera);

        imageViewResult = findViewById(R.id.preview);

        done = findViewById(R.id.doneCapture);
        back = findViewById(R.id.backCapture);

        done.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                presenter.done();
            }
        });
        back.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                presenter.back();
            }
        });

        //init Database
        DatabaseHelper db = new DatabaseHelper(this);

        //init SharedPreference
        sharedPreferences = this.getSharedPreferences(SharedPrefManager.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        btnCapture = findViewById(R.id.btn_capture);
        btnRecapture = findViewById(R.id.btn_recapture);

        //Capture Food
        btnCapture.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                recognizeImage();
            }
        });
        btnRecapture.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                btnRecapture.setVisibility(android.view.View.GONE);
                btnCapture.setVisibility(android.view.View.VISIBLE);
                imageViewResult.setVisibility(android.view.View.GONE);
                cameraKitView.setVisibility(android.view.View.VISIBLE);
            }
        });

        btnCapture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                presenter.Ptest("Kirim MVP test");
                return false;
            }
        });

        //Init presenter
        presenter = new CapturePresenter(this, new CaptureInteractor(),sharedPreferences, asset, db);
    }

    //Done Button
    @Override
    public void doneButton(float kcal){
        AlertDialog.Builder alert = new AlertDialog.Builder(CaptureActivity.this);
        alert.setMessage("Are you Sure to add "+kcal+" kcal ?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                }).setNegativeButton("No", null);
        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    //Back Button
    @Override
    public void backButton() {
        AlertDialog.Builder alert = new AlertDialog.Builder(CaptureActivity.this);
        alert.setMessage("Are you Sure to Exit ?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                }).setNegativeButton("No", null);
        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    @Override
    public void successAddCalorie(float kcal) {
        Toasty.success(CaptureActivity.this, "Calorie Added "+ kcal).show();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void updateRecognizeFood(ArrayList<Food> food) {
        foodAdapter.setData(food);
        foodAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void updateAddFood(ArrayList<Food> food) {
        foodAdapter.setData(food);
        foodAdapter.notifyDataSetChanged();
    }

    @Override
    public void makeButtonVisible() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnCapture.setVisibility(android.view.View.VISIBLE);
            }
        });
    }

    private void recognizeImage(){
        btnRecapture.setVisibility(android.view.View.GONE);
        btnCapture.setVisibility(android.view.View.GONE);
        startLoadingProcessingImage();
        //settingan megapixel di xml
        cameraKitView.captureImage(new CameraKitView.ImageCallback() {
            @Override
            public void onImage(CameraKitView cameraKitView, final byte[] photo) {
                try {
                    //convert raw foto ke Bitmap
                    Bitmap tmp = presenter.byteToBitmap(photo);
                    //Mencari data dari foto
                    List<Classifier.Recognition> results = presenter.recognizeFoods(tmp);

                    imageViewResult.setImageBitmap(tmp);
                    imageViewResult.setVisibility(android.view.View.VISIBLE);
                    cameraKitView.setVisibility(android.view.View.GONE);

                    //test confidence data 0 - 1
                    Toasty.warning(CaptureActivity.this, String.valueOf(results.get(0).getConfidence())).show();
                    finishLoadingProcessingImage();
                }catch (Exception e){
                    finishLoadingProcessingImage();
                    Toasty.warning(CaptureActivity.this, "Try Again").show();
                }
                btnRecapture.setVisibility(android.view.View.VISIBLE);
            }
        });
    }

    private void startLoadingProcessingImage(){
        mProgressDialog.setTitle("Processing Image");
        mProgressDialog.setMessage("Please Wait");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }
    private void finishLoadingProcessingImage(){
        mProgressDialog.dismiss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        //onresume error
        cameraKitView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //on pause error
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        cameraKitView.onStop();
        Intent i = new Intent(CaptureActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void Vtest(String text) {
        Toasty.success(CaptureActivity.this, text).show();
    }
}