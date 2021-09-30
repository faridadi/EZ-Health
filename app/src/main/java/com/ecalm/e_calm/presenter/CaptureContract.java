package com.ecalm.e_calm.presenter;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.ecalm.e_calm.Classifier;
import com.ecalm.e_calm.model.Food;
import com.ecalm.e_calm.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public interface CaptureContract {
    interface View {
        void updateRecognizeFood(ArrayList<Food> food);
        void updateAddFood(ArrayList<Food> food);
        void makeButtonVisible();
        void successAddCalorie(float kcal);
        void doneButton(float kcal);
        void backButton();

        void Vtest(String text);
    }
    interface Presenter{
        void done();
        void back();
        List<Classifier.Recognition> recognizeFoods(Bitmap bmp);
        void addFood(int pos);
        Bitmap byteToBitmap(byte[] photo);

        void Ptest(String text);
    }

    interface Interactor{
        public void initInteractor(CaptureContract.Presenter presenter, SharedPreferences share, AssetManager asset, DatabaseHelper db);
        void done();
        void back();
        List<Classifier.Recognition> recognizeFoods(Bitmap bmp);
        void addFood(int pos);
        Bitmap byteToBitmap(byte[] photo);

        String Itest(String text);
    }
}
