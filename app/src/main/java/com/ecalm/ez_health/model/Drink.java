package com.ecalm.ez_health.model;

import java.util.Date;

public class Drink {
    int id;
    int count;
    Date date;

    public Drink(int id, int count, Date date) {

        this.id = id;
        this.count = count;
        this.date = date;
    }

    public Drink(int count, Date date) {
        this.count = count;
        this.date = date;
    }

    public Drink(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
