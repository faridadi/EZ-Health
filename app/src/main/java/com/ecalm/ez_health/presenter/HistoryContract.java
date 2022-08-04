package com.ecalm.ez_health.presenter;
import com.ecalm.ez_health.model.Food;
import com.ecalm.ez_health.model.FoodHistory;

import java.text.ParseException;
import java.util.ArrayList;

public interface HistoryContract {

    interface View {
        void updateListFood(ArrayList<FoodHistory> foodHistory);
        void finishDeleteHistory(String msg);
        void failedDeleteHistory(String msg);
    }
    interface Presenter{
        void deleteHistoryFood(int foodid);
        void listFood();
        void searchFood(String Name);
        FoodHistory getFoodHistoryInfo(int pos);
    }
}
