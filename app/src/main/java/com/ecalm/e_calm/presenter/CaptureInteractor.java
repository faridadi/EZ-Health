package com.ecalm.e_calm.presenter;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.ecalm.e_calm.Classifier;
import com.ecalm.e_calm.model.Food;
import com.ecalm.e_calm.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CaptureInteractor implements CaptureContract.Interactor{

    CaptureContract.Presenter presenter;
    ArrayList<Food> foodArrayList;

    SharedPreferences sharedPreferences;
    AssetManager asset;
    DatabaseHelper db;

    public CaptureInteractor() {
    }

    public void initInteractor(CaptureContract.Presenter presenter, SharedPreferences share, AssetManager asset, DatabaseHelper db){
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
