package com.ecalm.ez_health.presenter;

import android.content.SharedPreferences;

import com.ecalm.ez_health.model.Drink;
import com.ecalm.ez_health.sqlite.DatabaseHelper;

import java.util.Date;

public class MinumPresenter implements MinumContract.Presenter{



    SharedPreferences sharedPreferences;
    MinumContract.View view;
    DatabaseHelper db;

    public MinumPresenter(MinumContract.View view, SharedPreferences share, DatabaseHelper db) {
        this.view = view;
        this.sharedPreferences = share;
        this.db = db;
    }

    @Override
    public void minumCounterByDate(Date date) {
        Drink dr = db.searchDrink(date);
        view.updateMinum(dr.getCount());
    }

    @Override
    public void addMinum(int count, Date date) {
        if(db.updateDrink(count, date)){
            view.updateMinum(count);
        }else{
            view.updateMinum(0);
        }
    }

    @Override
    public void removeMinum(int count, String date) {

    }
}
