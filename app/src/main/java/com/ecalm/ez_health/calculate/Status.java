package com.ecalm.ez_health.calculate;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.ecalm.ez_health.model.SharedPrefManager;
import com.ecalm.ez_health.sqlite.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;

public class Status {

    float batas = 0.8f;
    float toleransi = 10f; //persen

    public Status() {

    }

    public String cekStatus(float calorie, float recomend){
        if (calorie <= recomend){
            return "Healthy";
        }else if(calorie > recomend){
            return "Not Healthy";
        }else{
            return "Health";
        }
    }

    public String cekStatus(SharedPreferences share,DatabaseHelper db, Calendar c){
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");

        //cek tanggal sekarang atau tidak
        boolean isDateNow = dft.format(c.getTime()).equals(dft.format(Calendar.getInstance().getTime()));

        LocalTime jamPagi = LocalTime.parse( "06:00" );
        LocalTime jamSiang = LocalTime.parse( "12:00" );
        LocalTime jamMalam = LocalTime.parse( "18:00" );
        LocalTime jamTengah = LocalTime.parse( "23:00" );
        LocalTime jam = LocalTime.now();

        float pagiLim = Float.parseFloat(share.getString(SharedPrefManager.KEY_LIMIT_PAGI, null));
        float siangLim = Float.parseFloat(share.getString(SharedPrefManager.KEY_LIMIT_SIANG, null));
        float malamLim = Float.parseFloat(share.getString(SharedPrefManager.KEY_LIMIT_MALAM, null));
        float snackLim = Float.parseFloat(share.getString(SharedPrefManager.KEY_LIMIT_SNACK, null));
        float totCalLim = Float.parseFloat(share.getString(SharedPrefManager.KEY_TDEE, null));
        int bmi = Integer.parseInt(share.getString(SharedPrefManager.KEY_BMI, null));
        int program = Integer.parseInt(share.getString(SharedPrefManager.KEY_PROGRAM, null));
        int minumLim = 8;

        float pagi = 0;
        float siang = 0;
        float malam = 0;
        float snack = 0;
        float totCal = 0;
        int minum = 0;



        try {
            pagi = db.getCalorieDateType(dft.format(c.getTime()),1);
            siang = db.getCalorieDateType(dft.format(c.getTime()),2);
            malam = db.getCalorieDateType(dft.format(c.getTime()),3);
            snack = db.getCalorieDateType(dft.format(c.getTime()),4);
            totCal = db.getCalorieDate(dft.format(c.getTime()));

        }catch (Exception e){

        }
        String strMinum = "";
        minum = db.searchDrink(c.getTime()).getCount();
        if (minum < minumLim && isDateNow){
            strMinum = "> Minum anda kurang "+(minumLim-minum)+" gelas\n";
        }

        String strPagi = "";
        String strSiang = "";
        String strMalam = "";
        String strSnack = "";
        String strTotCal = "";
        String strBmi="";

        if(bmi ==  1 && (program == 0 || program == 2)){
            strBmi = "> Anda disarankan memilih program manambah berat badan\n";
        }else if(bmi ==  2 && (program == 0 || program == 2)){
            strBmi = "> Anda disarankan memilih program manambah berat badan\n";
        }else if(bmi ==  3 && (program == 0 || program == 2)){
            strBmi = "> Anda disarankan memilih program manambah berat badan\n";
        }else if(bmi ==  4 && (program == 2 || program == 1)){
            strBmi = "> Anda disarankan memilih program menjaga berat badan\n";
        }else if(bmi ==  5 && (program == 0 || program == 1)){
            strBmi = "> Anda disarankan memilih program mengurangi berat badan\n";
        }else if(bmi ==  6 && (program == 0 || program == 1)){
            strBmi = "> Anda disarankan memilih program mengurangi berat badan\n";
        }else if(bmi ==  7 && (program == 0 || program == 1)){
            strBmi = "> Anda disarankan memilih program mengurangi berat badan\n";
        }else if(bmi ==  8 && (program == 0 || program == 1)){
            strBmi = "> Anda disarankan memilih program mengurangi berat badan\n";
        }

       if((pagi > pagiLim)&&isDateNow){
           strPagi = "> Kelebihan kalori Pagi\n";
       }else if (isDateNow&&((jam.isBefore(jamSiang)&& jam.isAfter(jamPagi))&&(pagi<=pagiLim*batas))){
           strPagi = "> Kaloi pagi yang tersisa "+decimalFormat(Math.abs(pagiLim-pagi))+"\n";
       }

        if(siang > siangLim){
            strSiang = "> Kelebihan kalori siang\n";
        }else if (isDateNow && ((jam.isAfter(jamSiang)&& jam.isBefore(jamMalam))&&(siang<=siangLim*batas))){
            strSiang = "> Kalori siang yang tersisa "+decimalFormat(Math.abs(siangLim-siang))+"\n";
        }

        if(malam > malamLim && isDateNow){
            strMalam = "> Kelebihan kalori malam\n";
        }else if (isDateNow && ((jam.isAfter(jamMalam)&&jam.isBefore(jamTengah))&&(malam<=malamLim*batas))){
            strMalam = "> Kaloi pagi yang tersisa "+decimalFormat(Math.abs(malamLim-malam))+"\n";
        }

        if(snack > snackLim && isDateNow){
            strSnack = "> Kelebihan kalori snack\n";
        }

        if(totCal > totCalLim){
            strTotCal = ">Anda Kelebihan "+decimalFormat(Math.abs(totCalLim-totCal))+" kalori Harian";
        }

        return strBmi+strMinum+strPagi+strSiang+strMalam+strSnack+strTotCal;
    }

    @NonNull
    private String decimalFormat(float num){
        return String.format("%.1f", num);
    }
}
