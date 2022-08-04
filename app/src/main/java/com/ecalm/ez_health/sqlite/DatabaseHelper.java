package com.ecalm.ez_health.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ecalm.ez_health.model.Burn;
import com.ecalm.ez_health.model.StepModel;
import com.ecalm.ez_health.model.Drink;
import com.ecalm.ez_health.model.Food;
import com.ecalm.ez_health.model.FoodHistory;
import com.ecalm.ez_health.model.History;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper
{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "secret.db";
    //jumlah makanan
    private int totalFood=18;

    //constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL("Create table food(id INTEGER PRIMARY KEY autoincrement, name TEXT, calorie REAL, weight REAL)");

        //tipe = waktu saat makan, pagi siang malam
        //1 sarapan, 2 siang, 3 malam, 4 snack
        db.execSQL("Create table history(id INTEGER PRIMARY KEY autoincrement, tipe INTEGER, foodid INTEGER, created_date TEXT, FOREIGN KEY(foodid) REFERENCES food(id))");
        db.execSQL("Create table drink(id INTEGER PRIMARY KEY autoincrement, count INTEGER, created_date TEXT)");
        db.execSQL("Create table stepCount(id INTEGER PRIMARY KEY autoincrement, count INTEGER, created_date TEXT)");
        db.execSQL("Create table burn(id INTEGER PRIMARY KEY autoincrement, calorie REAL, created_date TEXT)");
        //add data

        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('pisang', 105, 118)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('jagung rebus', 77, 89)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('timun irisan', 1, 8.3)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('tomat irisan', 4, 22.2)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('sop', 33, 150)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('roti tawar', 100, 37)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('nasi putih', 135.4, 105)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('lontong', 130, 90)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('nasi kuning', 100, 105)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('nasi goreng', 250, 149)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('mie instan', 350, 80)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('tempe goreng', 34, 15.1)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('tahu goreng', 35, 12.9)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('ayam goreng', 119, 49)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('ayam crispy', 47.5, 15.99)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('telur goreng', 78, 40.2)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('telur rebus', 68, 44.15)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('es teh', 90, 236.8)");

        db.execSQL("INSERT INTO history(tipe,foodid,created_date) VALUES(2, 1, '2021-11-20 02:59:59')");
        db.execSQL("INSERT INTO history(tipe,foodid,created_date) VALUES(2, 3, '2021-11-20 02:59:59')");

        db.execSQL("INSERT INTO drink(count,created_date) VALUES(2, '2021-12-03 02:59:59')");
        db.execSQL("INSERT INTO drink(count,created_date) VALUES(6, '2021-12-04 02:59:59')");
        db.execSQL("INSERT INTO drink(count,created_date) VALUES(5, '2021-12-02 02:59:59')");

        db.execSQL("INSERT INTO stepCount(count,created_date) VALUES(200, '2021-12-28 02:59:59')");
        db.execSQL("INSERT INTO stepCount(count,created_date) VALUES(220, '2021-12-27 02:59:59')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS food");
        db.execSQL("DROP TABLE IF EXISTS history");
        db.execSQL("DROP TABLE IF EXISTS drink");
        db.execSQL("DROP TABLE IF EXISTS stepCount");
        db.execSQL("DROP TABLE IF EXISTS burn");
        // create new tables
        onCreate(db);
    }

    public void deleteAlldata(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS food");
        db.execSQL("DROP TABLE IF EXISTS history");
        db.execSQL("DROP TABLE IF EXISTS drink");
        db.execSQL("DROP TABLE IF EXISTS stepCount");
        db.execSQL("DROP TABLE IF EXISTS burn");
        // create new tables
        onCreate(db);
    }


    public int getCalorieDate(String date) throws ParseException {
        int calorie = 0;
        try {
            List<History> histori = getHistoryByDate(date);
            for(History food : histori){
                calorie += searchFoodId(String.valueOf(food.getFoodid())).getCalorie();
            }
        }catch (Exception e){
            return 0;
        }
        return calorie;
    }

    public int getCalorieDateType(String date, int tipe) throws ParseException {
        int calorie = 0;
        List<History> histori = getHistoryByDateType(date, tipe);
        for(History food : histori){
            calorie += searchFoodId(String.valueOf(food.getFoodid())).getCalorie();
        }
        return calorie;
    }

    public boolean insertHistory(History history){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tipe", history.getTipe());
        values.put("foodid", history.getFoodid());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.put("created_date", dateFormat.format(history.getCreatedDate()));
        // insert row
        long ins = db.insert("history", null, values);
        db.close();
        if (ins == -1 ){
            return false;
        }else {
            return true;
        }
    }

    public List<FoodHistory> getAllHistoryFood() throws ParseException {
        List<FoodHistory> foodHistory = new ArrayList<FoodHistory>();
        String selectQuery = "SELECT history.id AS id, history.foodid AS foodid, food.name AS name, history.tipe AS tipe, " +
                "food.calorie AS calorie, food.weight AS weight, history.created_date AS created_date " +
                "FROM history LEFT JOIN food ON history.foodid = food.id ORDER BY history.id DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        //YYYY-MM-DD
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                FoodHistory td = new FoodHistory();
                td.setId(c.getInt((c.getColumnIndex("id"))));
                td.setFoodId(c.getInt((c.getColumnIndex("foodid"))));
                td.setName(c.getString(c.getColumnIndex("name")));
                td.setTipe(c.getInt((c.getColumnIndex("tipe"))));
                td.setCalorie(c.getFloat(c.getColumnIndex("calorie")));
                td.setWeight(c.getFloat(c.getColumnIndex("weight")));
                Date date = formatter.parse(c.getString(c.getColumnIndex("created_date")));
                td.setCreatedDate(date);
                // adding to todo list
                foodHistory.add(td);
            } while (c.moveToNext());
        }
        db.close();
        return foodHistory;
    }


    public List<FoodHistory> getSearchHistoryFood(String name) throws ParseException {
        List<FoodHistory> foodHistory = new ArrayList<FoodHistory>();
        String selectQuery = "SELECT history.id AS id, history.foodid AS foodid, food.name AS name, history.tipe AS tipe ," +
                "food.calorie AS calorie, food.weight AS weight, history.created_date AS created_date " +
                "FROM history LEFT JOIN food ON history.foodid = food.id WHERE " +"food.name LIKE " + "'%" + name + "%'"+" ORDER BY history.id DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        //YYYY-MM-DD
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                FoodHistory td = new FoodHistory();
                td.setId(c.getInt((c.getColumnIndex("id"))));
                td.setFoodId(c.getInt((c.getColumnIndex("foodid"))));
                td.setName(c.getString(c.getColumnIndex("name")));
                td.setTipe(c.getInt((c.getColumnIndex("tipe"))));
                td.setCalorie(c.getFloat(c.getColumnIndex("calorie")));
                td.setWeight(c.getFloat(c.getColumnIndex("weight")));
                Date date = formatter.parse(c.getString(c.getColumnIndex("created_date")));
                td.setCreatedDate(date);
                // adding to todo list
                foodHistory.add(td);
            } while (c.moveToNext());
        }
        db.close();
        return foodHistory;
    }

    public List<Food> getHistoryFoodByDate(String dates, int tipe) throws ParseException {
        List<Food> foodHistory = new ArrayList<Food>();
        String selectQuery = "SELECT history.id AS id, history.foodid AS foodid, food.name AS name, history.tipe AS tipe ," +
                "food.calorie AS calorie, food.weight AS weight, history.created_date AS created_date " +
                "FROM history LEFT JOIN food ON history.foodid = food.id WHERE " +"history.created_date LIKE " + "'%" + dates + "%'"+"AND history.tipe ="+tipe+" ORDER BY history.id ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        //YYYY-MM-DD
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Food td = new Food();
                td.setId(c.getInt((c.getColumnIndex("foodid"))));
                td.setName(c.getString(c.getColumnIndex("name")));
                td.setCalorie(c.getFloat(c.getColumnIndex("calorie")));
                td.setWeight(c.getFloat(c.getColumnIndex("weight")));
                //Date date = formatter.parse(c.getString(c.getColumnIndex("created_date")));
                //td.setCreatedDate(date);
                // adding to todo list
                foodHistory.add(td);
            } while (c.moveToNext());
        }
        db.close();
        return foodHistory;
    }

    public boolean updateHistory(int id, History history){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tipe", history.getTipe());
        values.put("foodid", history.getFoodid());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.put("created_date", dateFormat.format(history.getCreatedDate()));
        long ins = db.update("history", values, "id=" + id, null);
        db.close();
        if (ins == -1 ){
            return false;
        }else {
            return true;
        }
    }

    public boolean deleteHistory(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        // insert row
        long ins = db.delete("history", "id="+id, null);
        db.close();
        // assigning tags to todo
        if (ins == -1 ){
            return false;
        }else {
            return true;
        }
    }

    public List<History> getAllHistory() throws ParseException {
        List<History> history = new ArrayList<History>();
        String selectQuery = "SELECT * FROM history";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        //YYYY-MM-DD
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                History td = new History();
                td.setId(c.getInt((c.getColumnIndex("id"))));
                td.setFoodid(c.getInt((c.getColumnIndex("foodid"))));
                td.setTipe(c.getInt((c.getColumnIndex("tipe"))));
                Date date = formatter.parse(c.getString(c.getColumnIndex("created_date")));
                td.setCreatedDate(date);
                // adding to todo list
                history.add(td);
            } while (c.moveToNext());
        }
        db.close();
        return history;
    }

    public List<History> getHistoryByDate(String currentDate) throws ParseException {
        List<History> history = new ArrayList<History>();
        String selectQuery = "SELECT  * FROM " + "history" + " WHERE " + "created_date" + " LIKE " + "'%" + currentDate + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        //YYYY-MM-DD
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                History td = new History();
                td.setId(c.getInt((c.getColumnIndex("id"))));
                td.setFoodid(c.getInt((c.getColumnIndex("foodid"))));
                td.setTipe(c.getInt((c.getColumnIndex("tipe"))));
                Date date = formatter.parse(c.getString(c.getColumnIndex("created_date")));
                td.setCreatedDate(date);
                // adding to todo list
                history.add(td);
            } while (c.moveToNext());
        }
        db.close();
        return history;
    }

    public boolean clearHistoryForUpdate(String date, int tipe){
        SQLiteDatabase db = this.getWritableDatabase();
        // insert row
        long ins = db.delete("history", "created_date" + " LIKE " + "'%" + date + "%' AND tipe ="+tipe, null);
        db.close();
        // assigning tags to todo
        if (ins == -1 ){
            return false;
        }else {
            return true;
        }
    }

    public List<History> getHistoryByDateType(String currentDate, int tipe) throws ParseException {
        List<History> history = new ArrayList<History>();
        String selectQuery = "SELECT  * FROM " + "history" + " WHERE " + "created_date" + " LIKE " + "'%" + currentDate + "%'"+" AND tipe="+tipe;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        //YYYY-MM-DD
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                History td = new History();
                td.setId(c.getInt((c.getColumnIndex("id"))));
                td.setFoodid(c.getInt((c.getColumnIndex("foodid"))));
                td.setTipe(c.getInt((c.getColumnIndex("tipe"))));
                Date date = formatter.parse(c.getString(c.getColumnIndex("created_date")));
                td.setCreatedDate(date);
                // adding to todo list
                history.add(td);
            } while (c.moveToNext());
        }
        db.close();
        return history;
    }

    public Drink searchDrink(Date dates){
        Drink drink = new Drink();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        String date = dft.format(dates);
        SQLiteDatabase db = this.getReadableDatabase();
        //cek minuman ada atau tidak
        cekDrink(dates);
        try {
            String selectQuery = "SELECT  * FROM " + "drink" + " WHERE " + "created_date" + " LIKE " + "'%" + date + "%'";
            Cursor c = db.rawQuery(selectQuery, null);
            c.moveToFirst();
            drink.setId(c.getInt((c.getColumnIndex("id"))));
            drink.setCount((c.getInt(c.getColumnIndex("count"))));
            drink.setDate(df.parse(c.getString(c.getColumnIndex("created_date"))));
        } catch (Exception e){
            //masih belum dynamis
            drink = new Drink(0, Calendar.getInstance().getTime());
        }
        db.close();
        return drink;
    }

    public boolean updateDrink(int count, Date dates){
        if(cekDrink(dates)){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("count", count);
            SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
            String date = dtf.format(dates);
            long ins = db.update("drink", values, "created_date LIKE" + "'%" + date + "%'", null);
            db.close();
            if (ins == -1 ){
                return false;
            }else {
                return true;
            }
        }else{
            return  false;
        }
    }

    public boolean cekDrink(Date dates){
        SQLiteDatabase db = this.getReadableDatabase();
        Date dtb = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        String date = dft.format(dates);

        String query = "SELECT  * FROM " + "drink" + " WHERE " + "created_date" + " LIKE " + "'%" + date + "%'";
        Cursor c = db.rawQuery(query,null);
        if (c != null && c.getCount() > 0){
            return true;
        }else {
            boolean tmp = insertDrink(new Drink(0,dates));
            return tmp;
        }
    }

    public boolean insertDrink(Drink drink){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("count", drink.getCount());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.put("created_date", dateFormat.format(drink.getDate()));
        // insert row
        long ins = db.insert("drink", null, values);
        db.close();
        if (ins == -1 ){
            return false;
        }else {
            return true;
        }
    }

    public boolean insertFood(Food food){

        //cek nama makanan sama
        if (searchFoodName(food.getName().toLowerCase()) !=  null){
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", food.getName());
        values.put("calorie", food.getCalorie());
        values.put("weight", food.getWeight());
        // insert row
        long ins = db.insert("food", null, values);
        db.close();
        // assigning tags to todo
        if (ins == -1 ){
            return false;
        }else {
            return true;
        }
    }

    public boolean cekFood(int id){
        if(id <= totalFood){
            return false;
        }else {
            return true;
        }
    }

    //delete food id didalam object food
    public boolean deleteFood(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        //cek makanan asli aplikasi
        if(id <= totalFood){
            db.close();
            return false;
        }
        // insert row
        long ins = db.delete("food", "id="+id, null);
        db.close();
        // assigning tags to todo
        if (ins == -1 ){
            return false;
        }else {
            return true;
        }
    }

    public boolean updateFood(int id, Food food){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", food.getName());
        values.put("calorie", food.getCalorie());
        values.put("weight", food.getWeight());
        long ins = db.update("food", values, "id="+id, null);
        db.close();
        if (ins == -1 ){
            return false;
        }else {
            return true;
        }
    }

    public List<Food> getAllFood() {
        List<Food> foods = new ArrayList<Food>();
        String selectQuery = "SELECT  * FROM 'food'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Food td = new Food();
                td.setId(c.getInt((c.getColumnIndex("id"))));
                td.setName((c.getString(c.getColumnIndex("name"))));
                td.setCalorie((c.getFloat(c.getColumnIndex("calorie"))));
                td.setWeight((c.getFloat(c.getColumnIndex("weight"))));
                foods.add(td);
            } while (c.moveToNext());
        }
        db.close();
        return foods;
    }

    //mencari 1 makanan
    public Food searchFoodName(String name){
        //nama lowercase
        String selectQuery = "SELECT * FROM food WHERE name='"+name.toLowerCase()+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        Food data = new Food();
        if(c.moveToFirst()){
            data.setId(c.getInt((c.getColumnIndex("id"))));
            data.setName((c.getString(c.getColumnIndex("name"))));
            data.setCalorie((c.getFloat(c.getColumnIndex("calorie"))));
            data.setWeight((c.getFloat(c.getColumnIndex("weight"))));
            db.close();
            return data;
        }
        else return null;
    }

    //filter makanan di infomakanan
    public List<Food> searchFood(String name) throws ParseException {
        List<Food> foods = new ArrayList<Food>();
        String selectQuery = "SELECT  * FROM " + "food" + " WHERE " + "name" + " LIKE " + "'%" + name + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Food td = new Food();
                td.setId(c.getInt((c.getColumnIndex("id"))));
                td.setName(c.getString((c.getColumnIndex("name"))));
                td.setCalorie(c.getFloat((c.getColumnIndex("calorie"))));
                td.setWeight(c.getFloat((c.getColumnIndex("weight"))));
                // adding to todo list
                foods.add(td);
            } while (c.moveToNext());
        }
        db.close();
        return foods;
    }

    public Food searchFoodId(String id){
        String selectQuery = "SELECT * FROM food WHERE id='"+id+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        Food data = new Food();
        c.moveToFirst();
        data.setId(c.getInt((c.getColumnIndex("id"))));
        data.setName((c.getString(c.getColumnIndex("name"))));
        data.setCalorie((c.getFloat(c.getColumnIndex("calorie"))));
        data.setWeight((c.getFloat(c.getColumnIndex("weight"))));
        db.close();
        return data;
    }

    public boolean cekFood(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM food";
        Cursor c = db.rawQuery(query,null);
        db.close();
        if (c != null && c.getCount() > 0){
            return true;
        }else {
            return false;
        }
    }

    public StepModel searchStepCount(Date dates){
        StepModel stepModel = new StepModel();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        String date = dft.format(dates);
        SQLiteDatabase db = this.getReadableDatabase();
        //cek minuman ada atau tidak
        chekStepCount(dates);
        try {
            String selectQuery = "SELECT  * FROM " + "stepCount" + " WHERE " + "created_date" + " LIKE " + "'%" + date + "%'";
            Cursor c = db.rawQuery(selectQuery, null);
            c.moveToFirst();
            stepModel.setId(c.getInt((c.getColumnIndex("id"))));
            stepModel.setCount((c.getInt(c.getColumnIndex("count"))));
            stepModel.setDate(df.parse(c.getString(c.getColumnIndex("created_date"))));
        } catch (Exception e){
            //masih belum dynamis
            stepModel = new StepModel(0, dates);
        }
        db.close();
        return stepModel;
    }

    public boolean updateStepCount(int count, Date dates){
        if(chekStepCount(dates)){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("count", count);
            SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
            String date = dtf.format(dates);
            long ins = db.update("stepCount", values, "created_date LIKE" + "'%" + date + "%'", null);
            db.close();
            if (ins == -1 ){
                return false;
            }else {
                return true;
            }
        }else{
            return  false;
        }
    }

    public boolean chekStepCount(Date dates){
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        String date = dft.format(dates);

        String query = "SELECT * FROM " + "stepCount" + " WHERE " + "created_date" + " LIKE " + "'%" + date + "%'";
        Cursor c = db.rawQuery(query,null);
        if (c != null && c.getCount() > 0){
            return true;
        }else {
            boolean tmp = insertStepCount(new StepModel(0,dates));
            return tmp;
        }
    }

    public boolean insertStepCount(StepModel stepModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("count", stepModel.getCount());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.put("created_date", dateFormat.format(stepModel.getDate()));
        // insert row
        long ins = db.insert("stepCount", null, values);
        db.close();
        if (ins == -1 ){
            return false;
        }else {
            return true;
        }
    }


    public Burn searchBurn(Date dates){
        Burn burn = new Burn();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        String date = dft.format(dates);
        SQLiteDatabase db = this.getReadableDatabase();
        //cek minuman ada atau tidak
        chekBurn(dates);
        try {
            String selectQuery = "SELECT  * FROM " + "burn" + " WHERE " + "created_date" + " LIKE " + "'%" + date + "%'";
            Cursor c = db.rawQuery(selectQuery, null);
            c.moveToFirst();
            burn.setId(c.getInt((c.getColumnIndex("id"))));
            burn.setCalorie((c.getFloat(c.getColumnIndex("calorie"))));
            burn.setCreated_date(df.parse(c.getString(c.getColumnIndex("created_date"))));
        } catch (Exception e){
            //masih belum dynamis
            burn = new Burn(0, dates);
        }
        db.close();
        return burn;
    }

    public boolean updateBurn(float calorie, Date dates){
        if(chekBurn(dates)){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("calorie", calorie);
            SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
            String date = dtf.format(dates);
            long ins = db.update("burn", values, "created_date LIKE" + "'%" + date + "%'", null);
            db.close();
            if (ins == -1 ){
                return false;
            }else {
                return true;
            }
        }else{
            return  false;
        }
    }

    public boolean chekBurn(Date dates){
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        String date = dft.format(dates);

        String query = "SELECT * FROM " + "burn" + " WHERE " + "created_date" + " LIKE " + "'%" + date + "%'";
        Cursor c = db.rawQuery(query,null);
        if (c != null && c.getCount() > 0){
            return true;
        }else {
            boolean tmp = insertBurn(new Burn(0,dates));
            return tmp;
        }
    }

    public boolean insertBurn(Burn burn){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("calorie", burn.getCalorie());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.put("created_date", dateFormat.format(burn.getCreated_date()));
        // insert row
        long ins = db.insert("burn", null, values);
        db.close();
        if (ins == -1 ){
            return false;
        }else {
            return true;
        }
    }
}