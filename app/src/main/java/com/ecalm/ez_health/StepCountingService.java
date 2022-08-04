package com.ecalm.ez_health;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.ecalm.e_calm.R;
import com.ecalm.ez_health.model.Burn;
import com.ecalm.ez_health.model.SharedPrefManager;
import com.ecalm.ez_health.model.StepModel;
import com.ecalm.ez_health.sqlite.DatabaseHelper;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

/**
 * Created by Abhi on 4/8/2018.
 * Here we are handling the step detector service and broadcasting the values
 * On my Android Lenevo K5 note device step detector was more accurate than step counter sensor
 * So I have went ahead using step detector sensor.
 */

public class StepCountingService extends Service implements SensorEventListener {
    SensorManager sensorManager;
    //Sensor stepCounterSensor;
    Sensor stepDetectorSensor, stepCounterSensor;

    SharedPreferences sharedPreferences;
    DatabaseHelper db;

    int currentStepsDetected;

    int stepCounter;
    int newStepCounter;

    int oldStep = 0;
    int incrementStep = 0;
    int stepDatabase = 0;

    boolean serviceStopped;

    Intent intent;

    String channelId = "step_counter";
    int notifyID = 13102017;

    private static final String TAG = "StepService";
    public static final String BROADCAST_ACTION = ".StepCountingService";

    // Create a handler - that will be used to broadcast our data, after a specified amount of time.
    private final Handler handler = new Handler();
    // counter number of times the service carried out updates.
    float bmr=0;
    int counter = 0;
    float distance=0;
    float stride =0f;
    int WALK_INTERVAL = 50;
    private long startTime=0;

    float weight=0;

    float WALK_MET=3.5f;
    float RUN_MET=11.5f;

    float WALK_SPEED = 1.34112f;
    float RUN_SPEED = 3.12928f;

    Handler customHandler = new Handler();

    // Service is being created
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        //testing
        startTime = System.currentTimeMillis();
        sharedPreferences = this.getSharedPreferences("fanregisterlogin", Context.MODE_PRIVATE);
        stride = Float.valueOf(sharedPreferences.getString(SharedPrefManager.KEY_STRIDE_LENGTH, null));
        bmr = Float.valueOf(sharedPreferences.getString(SharedPrefManager.KEY_TDEE, null));
        db = new DatabaseHelper(this);

        //untuk start awal notifikasi dengan StartForeground
        startForeground(notifyID,getMyActivityNotification());

        //customHandler.postDelayed(updateTimerThread, 5000);

        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }


    // startService() starts service
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(sensorManager!=null){
            sensorManager.unregisterListener(this, stepCounterSensor);
            sensorManager.unregisterListener(this, stepDetectorSensor);
        }
        if(this.db==null){
            init();
        }

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //pilih salah satu
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        sensorManager.registerListener(this, stepCounterSensor, 0);
        sensorManager.registerListener(this, stepDetectorSensor, 0);

        currentStepsDetected = 0;

        stepCounter = 0;
        newStepCounter = 0;

        serviceStopped = false;

        return START_STICKY;
    }

    //update notifikasi dengan notifikasi manager
    private void updateNotification(){
        Notification notification = getMyActivityNotification();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notifyID, notification);
    }

    //get Notification
    private Notification getMyActivityNotification(){
        NotificationManager ntfmgr = (NotificationManager)getSystemService(Service.NOTIFICATION_SERVICE);
        RemoteViews notificationLayout = new RemoteViews(this.getPackageName(), R.layout.notification_layout);
        //Creating the notification object
        String step = String.valueOf(db.searchStepCount(Calendar.getInstance().getTime()).getCount());
        notificationLayout.setTextViewText(R.id.notif_step_count_text,step );
        String burn = String.format("%.02f",db.searchBurn(Calendar.getInstance().getTime()).getCalorie());
        notificationLayout.setTextViewText(R.id.notif_burned_calorie_text, burn);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            // Configure the notification channel.
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setDescription("Channel description");
            notificationChannel.setSound(null,null);
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(false);
            ntfmgr.createNotificationChannel(notificationChannel);
        }

        Intent launchApp = new Intent(this, HomeActivity.class);
        PendingIntent launchNotification = PendingIntent.getActivity(this, 0, launchApp, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        notificationBuilder
                .setOngoing(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_logo)
                .setCustomContentView(notificationLayout)
                .setSilent(true)
                .setContentIntent(launchNotification);

        return  notificationBuilder.build();
    }

    IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public StepCountingService getService() {
            return StepCountingService.this;
        }
    }
    // Binding the service
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // Service is being destroyed when not in use
    @Override
    public void onDestroy() {
        //Toasty.info(getApplicationContext(),"Service Step Destroy").show();
        customHandler.removeCallbacks(updateTimerThread);

        sensorManager.unregisterListener(this,stepCounterSensor);
        sensorManager.unregisterListener(this,stepDetectorSensor);
        serviceStopped = true;
        stopForeground(true);
        super.onDestroy();
    }

    // system is running low on memory,actively running processes should reduce their memory usage. */
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//    }

    //Here Step detector was more accurate than step counter

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(!serviceStopped) {
            //Step count sensor
            if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                int countSteps = (int) event.values[0];
                // Initially stepCounter will be zero
                if (stepCounter < 1) {
                    stepCounter = (int) event.values[0];
                }
                newStepCounter = countSteps - stepCounter;

                //mendapatkan setiap increment data dari step counter
                incrementStep = newStepCounter - oldStep;
                if (incrementStep < 0) {
                    incrementStep = Math.abs(incrementStep);
                }
                oldStep = newStepCounter;

                //menyimpan data step di database;
                StepModel data = db.searchStepCount(Calendar.getInstance().getTime());
                stepDatabase = data.getCount() + incrementStep;

                db.updateStepCount(stepDatabase, Calendar.getInstance().getTime());
                //Toasty.info(getApplicationContext(),"Sensor Run").show();

                //kalkulasi calorie yang dibakar berdsarkan MET dengan interval jumlah langkah
                //masih maintanance
                counter += incrementStep;
                if (counter >= WALK_INTERVAL) {
                    if (System.currentTimeMillis() > startTime) {
                        long timeInteval = System.currentTimeMillis() - startTime;
                        distance = counter * stride;
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeInteval);
                        float time = Float.valueOf(seconds);

                        // speed m/s
                        float speed = (distance) / time;
                        float calorieBurned = 0;

                        weight = Float.parseFloat(sharedPreferences.getString(SharedPrefManager.KEY_WEIGHT, null));
                        if (speed <= WALK_SPEED) {
                            Toasty.normal(getApplicationContext(),weight+"").show();
                            calorieBurned = (float) (WALK_MET * 0.0175 * weight*((distance/WALK_SPEED)/60));
                        } else {
                            calorieBurned = (float) (RUN_MET * 0.0175 * weight*((distance/RUN_SPEED)/60));
                        }

                        Burn burn = db.searchBurn(Calendar.getInstance().getTime());
                        Float calorie = burn.getCalorie() + calorieBurned;
                        db.updateBurn(calorie, Calendar.getInstance().getTime());

                        startTime = System.currentTimeMillis();
                        counter = 0;
                    }
                }
                updateNotification();
                //createNotification();
                broadcastSensorValue();
            }
            // Step detector sensor
            if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                int detectSteps = (int) event.values[0];
                currentStepsDetected += detectSteps;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            updateNotification();
            Toasty.normal(getApplicationContext(), "Handler").show();
            customHandler.postDelayed(this, 5000);
        }
    };

    // Broadcast data through intent
    private void broadcastSensorValue() {
        Log.d(TAG, "Data to Activity");
        // add step counter to intent.
        intent.putExtra("Counted_Step_Int", newStepCounter);
        intent.putExtra("Counted_Step", String.valueOf(newStepCounter));
        intent.putExtra("increment_Counted_Step", String.valueOf(incrementStep));
        // add step detector to intent.
        intent.putExtra("Detected_Step_Int", currentStepsDetected);
        intent.putExtra("Detected_Step", String.valueOf(currentStepsDetected));
        //sendBroadcast sends a message to whoever is registered to receive it.
        sendBroadcast(intent);
    }

    private  void offStepCounter(){
        sensorManager.unregisterListener(this,stepCounterSensor);
        sensorManager.unregisterListener(this,stepDetectorSensor);
    }
}