package com.ecalm.ez_health.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.ecalm.ez_health.calculate.UserAnalysis;

import java.util.Locale;

public class SharedPrefManager {
    Context ctx;
    private static SharedPrefManager mInstance;
    private Float REC = 0f;

    public static final String SHARED_PREF_NAME = "fanregisterlogin";
    public static final String KEY_NAME = "keyname";
    public static final String KEY_WEIGHT = "keyweight";
    public static final String KEY_TALL = "keytall";
    public static final String KEY_GENDER = "keygender";
    public static final String KEY_AGE = "keyage";
    //0 aktifitas rendah, 1 aktifitas aktiv, 2 aktifitas sangat aktif
    public static final String KEY_ACTIVITY = "keyACTIVITY";
    //0 mempertahankan berat badan,1 menaikkan berat badan, 2 menurunkan berat badan,
    public static final String KEY_PROGRAM = "keyTARGET";
    public static final String KEY_BURN_CALORIE = "keyburncalorie";
    public static final String KEY_CALORIE = "keycalorie";
    public static final String KEY_TDEE = "keylimit";
    public static final String KEY_BMR = "keybmr";
    //1=Severe Thinness 2=Moderate Thinness 3=Mild Thinness 4=Normal 5=Overweight 6=Obese Class I 7=Obese Class II 8=Obese Class III
    public static final String KEY_BMI = "keybmi";
    public static final String KEY_LIMIT_PAGI = "keylimitpagi";
    public static final String KEY_LIMIT_SIANG = "keylimitsiang";
    public static final String KEY_LIMIT_MALAM = "keylimitmalam";
    public static final String KEY_LIMIT_SNACK = "keylimitsnack";
    public static final String KEY_STRIDE_LENGTH = "keystridelength";
    //0 =off 1=on
    public static final String KEY_STEP_COUNTER_TOGGLE = "keytogglecounter";

    private SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will store the user data in shared preferences
    public void userRegister(User userModel) {
        UserAnalysis cal = new UserAnalysis();
        float bmr, stride, tdee;
        float program=0;
        float factor=0;
        int bmi = 4;

        //faktor aktivitas
        factor = getFactorActivity(userModel.getActivity());

        //calori program
        program = getCalorieProgram(userModel.getProgram());

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, userModel.getName());
        editor.putString(KEY_GENDER, Integer.toString(userModel.getGender()));
        editor.putString(KEY_AGE, Integer.toString(userModel.getAge()));
        editor.putString(KEY_WEIGHT, Float.toString(userModel.getWeight()));
        editor.putString(KEY_TALL, Float.toString(userModel.getHeight()));
        editor.putString(KEY_ACTIVITY, Integer.toString(userModel.getActivity()));
        editor.putString(KEY_PROGRAM, Integer.toString(userModel.getProgram()));
        editor.putString(KEY_CALORIE, "0");

        //menghitung nilai bmi
        bmi = cal.getCategoryBmi(userModel.getWeight(), userModel.getHeight());

        if (userModel.getGender()==0){
            bmr = cal.getMaleBmr(userModel.getWeight(), userModel.getHeight(), userModel.getAge());
            stride = cal.getMaleStride(userModel.getHeight())/100;
        }else if (userModel.getGender()==1){
            bmr = cal.getFemaleBmr(userModel.getWeight(), userModel.getHeight(), userModel.getAge());
            stride = cal.getFemaleStride(userModel.getHeight()/100);
        }else {
            bmr = 1000f;
            stride = 0.6f;
        }

        //total kalori
        tdee = cal.getTdee(bmr,factor,program,REC);

        //format 1 decimal Locale untuk koma dan titik format angka desimal
        tdee = Float.parseFloat(String.format(Locale.US,"%.1f", tdee));
        float pagi = Float.parseFloat(String.format(Locale.US,"%.2f", tdee*30/100));
        float siang = Float.parseFloat(String.format(Locale.US,"%.2f", tdee*30/100));
        float malam = Float.parseFloat(String.format(Locale.US,"%.2f", tdee*30/100));
        float snack = Float.parseFloat(String.format(Locale.US,"%.2f", tdee*10/100));

        float burnCalorie = cal.getBurnedCalorie(userModel.getWeight());
        editor.putString(KEY_BURN_CALORIE, Float.toString(burnCalorie));

        editor.putString(KEY_LIMIT_PAGI, Float.toString(pagi));
        editor.putString(KEY_LIMIT_SIANG, Float.toString(siang));
        editor.putString(KEY_LIMIT_MALAM, Float.toString(malam));
        editor.putString(KEY_LIMIT_SNACK, Float.toString(snack));

        editor.putString(KEY_STRIDE_LENGTH, Float.toString(stride));

        editor.putString(KEY_BMR, Float.toString(bmr));
        editor.putString(KEY_TDEE, Float.toString(tdee));
        editor.putString(KEY_BMI, Integer.toString(bmi));
        editor.putString(KEY_STEP_COUNTER_TOGGLE, "0");
        editor.apply();
    }

    //this methode will checker user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, null) != null;
    }

    private float getFactorActivity(int activity){
        float factor = 0;
        if(activity==0){
            factor = 1.2f;
        }else if(activity==1){
            factor = 1.375f;
        }else if(activity==2){
            factor = 1.55f;
        }else if(activity==3){
            factor = 1.725f;
        }
        return factor;
    }

    private float getCalorieProgram(int program){
        float cal=0;
        if(program==2){
            cal = -500;
        }else if(program==0){
            cal = 0;
        }else if(program==1){
            cal = 500;
        }else {
            cal = 0;
        }
        return cal;
    }

    public void setStepCounter(boolean b){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(b) {
            editor.putString(KEY_STEP_COUNTER_TOGGLE, "1");
        }else{
            editor.putString(KEY_STEP_COUNTER_TOGGLE, "0");
        }
        editor.apply();
    }
}