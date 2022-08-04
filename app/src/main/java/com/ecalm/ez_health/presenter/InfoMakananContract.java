package com.ecalm.ez_health.presenter;

import android.widget.EditText;

import com.ecalm.ez_health.model.Food;

import java.util.ArrayList;

public interface InfoMakananContract {
    interface View {
        void updateListFood(ArrayList<Food> food);
        void finishAddNewFood(String msg);
        void failedAddNewFood(String msg);

        void finishEditFood(String msg);
        void failedEditFood(String msg);

        void finishDeleteFood(String msg);
        void failedDeleteFood(String msg);
    }
    interface Presenter{
        void addFood(Food food);
        void removeFood(int foodid);
        void listFood();
        Food getFoodInfo(int pos);
        void searchFood(String Name);
        void addNewFood(EditText nama, EditText kalori, EditText berat);
        void editFood(int id, EditText nama, EditText kalori, EditText berat);
    }
}
