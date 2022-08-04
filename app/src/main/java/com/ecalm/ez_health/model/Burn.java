package com.ecalm.ez_health.model;

import java.util.Date;

public class Burn {
    int id;
    float calorie;
    Date created_date;

    public Burn() {
    }

    public Burn(int calorie, Date created_date) {
        this.calorie = calorie;
        this.created_date = created_date;
    }

    public Burn(int id, int calorie, Date created_date) {
        this.id = id;
        this.calorie = calorie;
        this.created_date = created_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getCalorie() {
        return calorie;
    }

    public void setCalorie(float calorie) {
        this.calorie = calorie;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }
}
