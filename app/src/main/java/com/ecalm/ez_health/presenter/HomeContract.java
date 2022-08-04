package com.ecalm.ez_health.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public interface HomeContract {
    interface View{
        void updateHealthy(String sts);
        void updateCalorieCounter(String text, String pagi, String siang, String malam, String snack, String calorie);
        void updateCalorieBurned(String burn);
        void updateProgressBar(int calorie, int macCalorie);
        void updateMinumAir(String data);
        void updateStepCounterService(int step);
        void test(String s);
        //void updateHistory();
    }

    interface Presenter{
        void startStepCounterService(Context ctx);
        void stopStepCounterService(Context ctx);
        void checkPermission(AppCompatActivity activity);
        void healthyStatus(String date);
        void calorieCounterByDate(String date);
        void calorieCounterBurnedByDate(Date date);
        void stepCounter(String date);
        //void checkHistory();
    }
}
