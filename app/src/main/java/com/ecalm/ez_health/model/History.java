package com.ecalm.ez_health.model;

import java.util.Date;

public class History {
    int id;
    int tipe;
    int foodid;
    Date createdDate;

    public History(){

    }

    public History(int id, int tipe, int foodid, Date createdDate) {
        this.id = id;
        this.tipe = tipe;
        this.foodid = foodid;
        this.createdDate = createdDate;
    }

    public History(int tipe, int foodid, Date createdDate) {
        this.tipe = tipe;
        this.foodid = foodid;
        this.createdDate = createdDate;
    }

    public History(int tipe, int foodid) {
        this.tipe = tipe;
        this.foodid = foodid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipe() {
        return tipe;
    }

    public void setTipe(int tipe) {
        this.tipe = tipe;
    }

    public int getFoodid() {
        return foodid;
    }

    public void setFoodid(int foodid) {
        this.foodid = foodid;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}
