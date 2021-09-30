package com.ecalm.e_calm.presenter;

import androidx.appcompat.app.AppCompatActivity;

public interface MainContract {
    interface View{
        void updateHealthy(String sts);
        void updateCalorieCounter(String text);
        //void updateHistory();
    }

    interface Presenter{
        void checkPermission(AppCompatActivity activity);
        void healthyStatus();
        void calorieCounter();
        //void checkHistory();
    }
}
