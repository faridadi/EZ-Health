package com.ecalm.e_calm.presenter;

import android.Manifest;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.ecalm.e_calm.SharedPrefManager;
import com.ecalm.e_calm.math.Status;
import com.ecalm.e_calm.sqlite.DatabaseHelper;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainPresenter implements MainContract.Presenter{

    SharedPreferences sharedPreferences;
    MainContract.View view;
    //untuk feature selenjutnya
    DatabaseHelper db;

    public MainPresenter(MainContract.View view, SharedPreferences share, DatabaseHelper db) {
        this.view = view;
        this.sharedPreferences = share;
        this.db = db;
    }

    @Override
    public void checkPermission(AppCompatActivity activity) {
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).onSameThread().check();
    }

    @Override
    public void healthyStatus() {
        Status sts = new Status();
        String send = sts.cekStatus(Float.parseFloat(sharedPreferences.getString(SharedPrefManager.KEY_CALORIE, null)),Float.parseFloat(sharedPreferences.getString(SharedPrefManager.KEY_LIMIT, null)));
        view.updateHealthy(send);
    }

    @Override
    public void calorieCounter() {
        String counter =  sharedPreferences.getString(SharedPrefManager.KEY_CALORIE, null)+"kcal / "+sharedPreferences.getString(SharedPrefManager.KEY_LIMIT, null)+"kcal";
        view.updateCalorieCounter(counter);
    }
}
