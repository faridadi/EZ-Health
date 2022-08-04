package com.ecalm.ez_health.presenter;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.ecalm.ez_health.model.Food;
import com.ecalm.ez_health.model.FoodHistory;
import com.ecalm.ez_health.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class InfoMakananPresenter implements InfoMakananContract.Presenter{
    ArrayList<Food> datalist;
    InfoMakananContract.View view;
    DatabaseHelper db;

    public InfoMakananPresenter (InfoMakananContract.View view,  DatabaseHelper db) {
        this.view = view;
        this.db = db;
        datalist= new ArrayList<>();
    }

    @Override
    public void addFood(Food food) {
        db.insertFood(food);
        datalist = new ArrayList<Food>(db.getAllFood());
        view.updateListFood(datalist);
    }

    @Override
    public void removeFood(int foodid) {
        boolean cek =false;
        try{
            cek = db.deleteFood(foodid);
            if(cek){
                datalist = new ArrayList<Food>(db.getAllFood());
                view.updateListFood(datalist);
                view.finishDeleteFood("Makanan berhasil dihapus");
            }else{
                view.failedDeleteFood("Makanan tidak bisa dihapus");
            }
        }catch (Exception e){
            view.failedDeleteFood("Makanan gagal dihapus");
        }
    }

    @Override
    public void listFood() {
        datalist = new ArrayList<Food>(db.getAllFood());

//        for (Food data: datalist){
//            Log.d("Food", "id: "+data.getId()+" nama: "+data.getName()+" kalori: "+data.getCalorie()+" berat: "+data.getWeight());
//        }

        view.updateListFood(datalist);
    }

    @Override
    public Food getFoodInfo(int pos) {
        return datalist.get(pos);
    }

    @Override
    public void searchFood(String Name) {
        try {
            datalist = new ArrayList<Food>(db.searchFood(Name));
            view.updateListFood(datalist);
        }catch (Exception e){

        }
    }


    @Override
    public void addNewFood(EditText nama, EditText kalori, EditText berat) {
        if(checkForm(nama, kalori,berat)){
            boolean insert = db.insertFood(new Food(nama.getText().toString(), Float.parseFloat(kalori.getText().toString()), Float.parseFloat(berat.getText().toString())));
            if(insert){
                view.finishAddNewFood("Makanan berhasil ditambah");
                //update list data
                listFood();
            }else{
                view.failedAddNewFood("Nama makanan yang anda tambah sudah ada");
            }
        }else{
            view.failedAddNewFood("Makanan gagal ditambah");
        }
    }

    @Override
    public void editFood(int id, EditText nama, EditText kalori, EditText berat) {
        if(checkForm(nama, kalori,berat)){
            boolean edit = db.updateFood(id,new Food(nama.getText().toString(), Float.parseFloat(kalori.getText().toString()), Float.parseFloat(berat.getText().toString())));
            if(edit){
                view.finishEditFood("Makanan berhasil diedit");
                //update list data
                listFood();
            }else{
                view.failedEditFood("Database tidak berhasil mengedit");
            }
        }else{
            view.failedEditFood("Makanan gagal diedit");
        }


    }

    public boolean checkForm(EditText nama, EditText kalori, EditText berat){
        if(isEmpty(nama)){
            nama.setError("Silahkan isi nama makanan");
        }else if(isEmpty(kalori)){
            kalori.setError("Silahkan isi kalori makanan");
        }else if (isEmpty(berat)){
            berat.setError("Silahkan isi berat makanan");
        }else{
            return true;
        }
        return false;
    }

    private boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}
