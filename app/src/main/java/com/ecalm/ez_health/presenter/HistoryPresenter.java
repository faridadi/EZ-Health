package com.ecalm.ez_health.presenter;

import android.util.Log;

import com.ecalm.ez_health.model.Food;
import com.ecalm.ez_health.model.FoodHistory;
import com.ecalm.ez_health.sqlite.DatabaseHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class HistoryPresenter implements HistoryContract.Presenter{
    HistoryContract.View view;
    DatabaseHelper db;
    ArrayList<FoodHistory> datalist;

    public HistoryPresenter(HistoryContract.View view, DatabaseHelper db) {
        this.view = view;
        this.db = db;
        datalist = new ArrayList<>();
    }

    @Override
    public void deleteHistoryFood(int pos) {
        try {
            FoodHistory data = datalist.get(pos);
            db.deleteHistory(data.getId());
            datalist = new ArrayList<FoodHistory>(db.getAllHistoryFood());
            view.updateListFood(datalist);
            view.finishDeleteHistory("Berhasil menghapus histori");
        }catch (Exception e){
            view.failedDeleteHistory("Gagal menghapus histori");
        }
    }

    @Override
    public void listFood() {
        try {
            datalist = new ArrayList<FoodHistory>(db.getAllHistoryFood());
//            for (FoodHistory data: datalist){
//                Log.d("Histori", "id: "+data.getId()+" food_id: "+data.getFoodId()+" nama: "+data.getName()+" kalori: "+data.getCalorie()+" berat: "+data.getWeight()+" tipe: "+data.getTipe()+" created_date: "+data.getCreatedDate());
//            }
            view.updateListFood(datalist);
        }catch (Exception e){

        }
    }

    @Override
    public void searchFood(String Name) {
        try {
            datalist = new ArrayList<FoodHistory>(db.getSearchHistoryFood(Name));
            view.updateListFood(datalist);
        }catch (Exception e){

        }
    }

    @Override
    public FoodHistory getFoodHistoryInfo(int pos){
        return datalist.get(pos);
    }
}
