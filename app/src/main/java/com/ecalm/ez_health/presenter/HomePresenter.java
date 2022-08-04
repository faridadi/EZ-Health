package com.ecalm.ez_health.presenter;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ecalm.ez_health.StepCountingService;
import com.ecalm.ez_health.model.Burn;
import com.ecalm.ez_health.model.StepModel;
import com.ecalm.ez_health.model.Drink;
import com.ecalm.ez_health.model.SharedPrefManager;
import com.ecalm.ez_health.calculate.Status;
import com.ecalm.ez_health.sqlite.DatabaseHelper;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class HomePresenter implements HomeContract.Presenter{

    SharedPreferences sharedPreferences;
    HomeContract.View view;
    DatabaseHelper db;
    Intent intentStep;
    IntentFilter intentFilterStep;
    Calendar c;

    public HomePresenter(HomeContract.View view, SharedPreferences share, DatabaseHelper db, Calendar c) {
        this.view = view;
        this.sharedPreferences = share;
        this.db = db;
        this.c = c;
    }


    @Override
    public void stepCounter(String date) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        StepModel data = new StepModel();
        try{
            data = db.searchStepCount(dft.parse(date));
        }catch (Exception e){

        }
        view.updateStepCounterService(data.getCount());
    }

    @Override
    public void startStepCounterService(Context ctx) {
        SharedPrefManager.getInstance(ctx).setStepCounter(true);
        intentStep = new Intent(ctx, StepCountingService.class);
        intentFilterStep = new IntentFilter(StepCountingService.BROADCAST_ACTION);

        ctx.registerReceiver(broadcastReceiver, intentFilterStep);

        // register BroadcastReceiver
        ContextCompat.startForegroundService(ctx,intentStep);
    }

    @Override
    public void stopStepCounterService(Context ctx) {
        SharedPrefManager.getInstance(ctx).setStepCounter(false);
        intentStep = new Intent(ctx, StepCountingService.class);

        ctx.unregisterReceiver(broadcastReceiver);

        ctx.stopService(intentStep);
    }

    //vroadcast reciver dari stepCountingService
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String countedStep = intent.getStringExtra("Counted_Step");
//            String DetectedStep = intent.getStringExtra("Detected_Step");
            SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
            String aTmp = dft.format(c.getTime());
            String bTmp = dft.format(Calendar.getInstance().getTime());
            //update view step counter untuk tgl sekarang, bukan kemarin atau besok
            if(aTmp.equals(bTmp)){
                StepModel data = db.searchStepCount(Calendar.getInstance().getTime());
                view.updateStepCounterService(data.getCount());
                Burn data1 = db.searchBurn(Calendar.getInstance().getTime());

                //update ui if stepeCounter service running
                String recBurn = sharedPreferences.getString(SharedPrefManager.KEY_BURN_CALORIE, null);
                view.updateCalorieBurned(String.format("%.02f", data1.getCalorie())+" dari "+ recBurn+" kalori");
            }
        }
    };

    @Override
    public void checkPermission(AppCompatActivity activity) {
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACTIVITY_RECOGNITION,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.FOREGROUND_SERVICE,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED

                ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {  // check if all permissions are granted
                            //Toasty.success(activity, "All permissions are granted by user!").show();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) { // check for permanent denial of any permission
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
                        @Override
                        public void onError(DexterError error) {
                            Toasty.error(activity, "Some Error! ").show();
                        }
                    })
                    .onSameThread()
                    .check();
    }

    @Override
    public void healthyStatus(String date) {
        Status sts = new Status();
        int calorie = 0;
        try {
            calorie = db.getCalorieDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String send = sts.cekStatus(calorie,Float.parseFloat(sharedPreferences.getString(SharedPrefManager.KEY_TDEE, null)));
        send = sts.cekStatus(sharedPreferences,db,c);
        view.updateHealthy(send);
    }

    @Override
    public void calorieCounterByDate(String date) {
        String calorie, pagi, siang, malam, snack, burnedCalorie;
        int cal = 0, calSend=0;
        float burn = 0;
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Drink dr = new Drink();
        try {
            dr = db.searchDrink(dft.parse(date));
            cal = db.getCalorieDate(date);
            burn = db.searchBurn(dft.parse(date)).getCalorie();
            calSend = cal;//(cal-Math.round(burn)) < 0 ? 0 : (cal-Math.round(burn));
            pagi = String.valueOf(db.getCalorieDateType(date,1));
            siang = String.valueOf(db.getCalorieDateType(date,2));
            malam = String.valueOf(db.getCalorieDateType(date,3));
            snack = String.valueOf(db.getCalorieDateType(date,4));
        }catch (Exception e){
            dr.setCount(0);
            calorie = "0 kalori";
            pagi = "0 kalori";
            siang = "0 kalori";
            malam = "0 kalori";
            snack = "0 kalori";
        }
        String counter =  calSend+" / "+sharedPreferences.getString(SharedPrefManager.KEY_TDEE, null)+" kalori";
        pagi =  pagi+" / "+sharedPreferences.getString(SharedPrefManager.KEY_LIMIT_PAGI, null)+" kalori";
        siang =  siang+" / "+sharedPreferences.getString(SharedPrefManager.KEY_LIMIT_SIANG, null)+" kalori";
        malam =  malam+" / "+sharedPreferences.getString(SharedPrefManager.KEY_LIMIT_MALAM, null)+" kalori";
        snack =  snack+" / "+sharedPreferences.getString(SharedPrefManager.KEY_LIMIT_SNACK, null)+" kalori";


        view.updateMinumAir(dr.getCount()+" / 8 Gelas ("+dr.getCount()*0.25+" liter)");
        float tmp = Float.parseFloat(sharedPreferences.getString(SharedPrefManager.KEY_TDEE, null));
        view.updateProgressBar(calSend,Math.round(tmp));
        view.updateCalorieCounter(counter, pagi,siang, malam, snack, tmp-cal+" kalori");
        //view.updateCalorieCounter(counter, pagi,siang, malam, snack, calorie+" cal", burnedCalorie);
    }

    @Override
    public void calorieCounterBurnedByDate(Date date) {
        Burn data = db.searchBurn(date);
        String recBurn = sharedPreferences.getString(SharedPrefManager.KEY_BURN_CALORIE, null);
        view.updateCalorieBurned(String.format("%.02f", data.getCalorie())+" dari "+ recBurn+" kalori");
    }
}
