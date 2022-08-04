package com.ecalm.ez_health.model;

import java.util.Date;

public class FoodHistory {
    int id;
    int foodId;
    String name;
    int tipe;
    float calorie;
    float weight;
    Date createdDate;
    public FoodHistory() {

    }

    public FoodHistory(int id, int foodId, String name, int tipe, float calorie, float weight, Date date) {
        this.id = id;
        this.foodId = foodId;
        this.name = name;
        this.tipe = tipe;
        this.calorie = calorie;
        this.weight = weight;
        this.createdDate = date;
    }

    public int getTipe() {
        return tipe;
    }

    public void setTipe(int tipe) {
        this.tipe = tipe;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCalorie() {
        return calorie;
    }

    public void setCalorie(float calorie) {
        this.calorie = calorie;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
