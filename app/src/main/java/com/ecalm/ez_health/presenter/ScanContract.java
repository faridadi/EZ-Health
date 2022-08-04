package com.ecalm.ez_health.presenter;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.ecalm.ez_health.tensorflowLite.Classifier;
import com.ecalm.ez_health.model.Food;
import com.ecalm.ez_health.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public interface ScanContract {
    interface View {
        void updateRecognizeFood(ArrayList<Food> food);
        void updateCartFood(ArrayList<Food> food);
        void makeButtonVisible();
        void successAddCalorie(float kcal);
        void doneButton(float kcal);
        void backButton();
        void updateSearchFood(ArrayList<Food> food);
        //kode 1 = sukses, 0 error
        void Vtest(String text, int code);
    }
    interface Presenter{
        void done();
        void commit();
        void back();
        void searchFood(String s);
        List<Classifier.Recognition> recognizeFoods(Bitmap bmp);
        void addFood(int pos);
        void removeFood(int pos);
        Bitmap byteToBitmap(byte[] photo);
        void loadHistoryFood(Calendar c, int tipe);
        void Ptest(String text);
    }

    interface Interactor{
        public void initInteractor(ScanContract.Presenter presenter, SharedPreferences share, AssetManager asset, DatabaseHelper db);
        void done();
        void back();
        List<Classifier.Recognition> recognizeFoods(Bitmap bmp);
        void addFood(int pos);
        Bitmap byteToBitmap(byte[] photo);

        String Itest(String text);
    }
}
