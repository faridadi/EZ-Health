package com.ecalm.ez_health.model;

import java.util.Date;

public class StepModel {
    int id;
    int count;
    Date date;

    public StepModel() {
    }
    public StepModel(int id, int count, Date date) {
        this.id = id;
        this.count = count;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StepModel(int count, Date date) {
        this.count = count;
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
