package com.ecalm.ez_health.presenter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public interface MinumContract {
    interface View{
        void updateMinum(int minum);
    }

    interface Presenter{
        void minumCounterByDate(Date date);
        void addMinum(int count, Date date);
        void removeMinum(int count, String date);
    }
}
