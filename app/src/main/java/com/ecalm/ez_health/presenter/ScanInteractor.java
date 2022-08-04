package com.ecalm.ez_health.presenter;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.ecalm.ez_health.tensorflowLite.Classifier;
import com.ecalm.ez_health.model.Food;
import com.ecalm.ez_health.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ScanInteractor implements ScanContract.Interactor{

    ScanContract.Presenter presenter;
    ArrayList<Food> foodArrayList;

    SharedPreferences sharedPreferences;
    AssetManager asset;
    DatabaseHelper db;

    public ScanInteractor() {
    }

    public void initInteractor(ScanContract.Presenter presenter, SharedPreferences share, AssetManager asset, DatabaseHelper db){
        this.presenter = presenter;
        this.sharedPreferences = share;
        this.asset = asset;
        this.db = db;
    }

    @Override
    public void done() {

    }

    @Override
    public void back() {

    }

    @Override
    public List<Classifier.Recognition> recognizeFoods(Bitmap bmp) {
        return null;
    }

    @Override
    public void addFood(int pos) {

    }

    @Override
    public Bitmap byteToBitmap(byte[] photo) {
        return null;
    }

    @Override
    public String Itest(String text) {
        return text;
    }
}
