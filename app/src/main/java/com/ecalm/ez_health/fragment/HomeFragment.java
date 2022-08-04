package com.ecalm.ez_health.fragment;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.TextView;

import com.ecalm.ez_health.ScanActivity;
import com.ecalm.e_calm.R;
import com.ecalm.ez_health.StepCountingService;
import com.ecalm.ez_health.model.StepModel;
import com.ecalm.ez_health.model.SharedPrefManager;
import com.ecalm.ez_health.presenter.HomeContract;
import com.ecalm.ez_health.presenter.HomePresenter;
import com.ecalm.ez_health.sqlite.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements HomeContract.View {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    HomeContract.Presenter presenter;

    RelativeLayout makanSiang;
    RelativeLayout makanMalam;
    RelativeLayout sarapanPagi;
    RelativeLayout snack;

    TextView stepText;
    Switch stepSwitch;
    Boolean isStepServiceStopped;
    String countedStep;
    String DetectedStep;
    private Intent intentStep;
    private IntentFilter intentFilterStep;

    ImageButton nextDate, previousDate;

    ProgressBar calorieProgessBar;
    int counter = 0;
    int currentCalorie = 0;
    private Handler handler = new Handler();

    TextView date, calorieCounter, status, makanSiangText, makanMalamText, sarapanPagiText, snackText, kaloriMasuk, kaloriTerbakar, minumAirText;
    DatabaseHelper db;
    SharedPreferences sharedPreferences;

    SimpleDateFormat df,dft;
    Calendar c;
    int dayShift;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
               getActivity().moveTaskToBack(true);
            }
        });
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sharedPreferences = getActivity().getSharedPreferences("fanregisterlogin", Context.MODE_PRIVATE);
        db = new DatabaseHelper(getActivity());

        calorieCounter = view.findViewById(R.id.calorieCounter);
        calorieProgessBar = view.findViewById(R.id.kaloriProgressBar);

        minumAirText = view.findViewById(R.id.homeMinumText);

        kaloriMasuk = view.findViewById(R.id.kalorimasuk);
        kaloriTerbakar = view.findViewById(R.id.kaloriterbakar);

        makanSiang = view.findViewById(R.id.menuMakanSiang);
        makanSiangText = view.findViewById(R.id.makanSiangText);

        makanMalam = view.findViewById(R.id.menuMakanMalam);
        makanMalamText = view.findViewById(R.id.makanMalamText);

        sarapanPagi = view.findViewById(R.id.menuSarapanPagi);
        sarapanPagiText = view.findViewById(R.id.sarapanPagiText);

        snack = view.findViewById(R.id.menuSnack);
        snackText = view.findViewById(R.id.snackText);

        stepText = view.findViewById(R.id.jumlah_langkah_text);
        stepSwitch = view.findViewById(R.id.step_counter_switch);
        isStepServiceStopped = false;

        stepSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toasty.success(getActivity(),"Step Counter Aktif").show();
                    stepSwitch.setText(stepSwitch.getTextOn());
                    presenter.startStepCounterService(getActivity().getApplicationContext());
                }else{
                    Toasty.normal(getActivity(),"Step Counter Nonaktif").show();
                    stepSwitch.setText(stepSwitch.getTextOff());
                    presenter.stopStepCounterService(getActivity().getApplicationContext());
                }
            }
        });

        nextDate = view.findViewById(R.id.nextDate);
        previousDate = view.findViewById(R.id.previousDate);


        date = view.findViewById(R.id.dateTextView);


        status = view.findViewById(R.id.statusText);

        DatePickerDialog.OnDateSetListener dateDialog =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH,month);
                c.set(Calendar.DAY_OF_MONTH,day);
                date.setText(df.format(c.getTime()));
                changeDateDate();
            }
        };

        date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v == date) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.setAlpha(0.7f);
                    } else {
                        v.setAlpha(1f);
                    }
                }
                return false;
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),dateDialog,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Date dateNow = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("dd MMMM yyyy");
        dft = new SimpleDateFormat("yyyy-MM-dd");

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            c = (Calendar) extras.getSerializable("scanDate");
        }else{
            c= Calendar.getInstance();
        }
        date.setText(df.format(c.getTime()));
        dayShift = 0;



        sarapanPagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ScanActivity.class);
                //1 sarapan, 2 siang, 3 malam, 4 snack
                i.putExtra("tipe",1);
                i.putExtra("date", c);
                startActivity(i);
                //Toasty.info(getActivity(), "Scan Sarapan Pagi").show();
            }
        });

        makanSiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ScanActivity.class);
                //1 sarapan, 2 siang, 3 malam, 4 snack
                i.putExtra("tipe",2);
                i.putExtra("date", c);
                startActivity(i);
            }
        });

        makanMalam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ScanActivity.class);
                //1 sarapan, 2 siang, 3 malam, 4 snack
                i.putExtra("tipe",3);
                i.putExtra("date", c);
                startActivity(i);
            }
        });

        snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ScanActivity.class);
                //1 sarapan, 2 siang, 3 malam, 4 snack
                i.putExtra("tipe",4);
                i.putExtra("date", c);
                startActivity(i);
            }
        });

        nextDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v == nextDate) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.setAlpha(0.7f);
                    } else {
                        v.setAlpha(1f);
                    }
                }
                return false;
            }
        });

        nextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dayShift++;
                c.add(Calendar.DATE, 1);
                date.setText(df.format(c.getTime()));
                //ganti calori di makan pagi siang malam
                changeDateDate();
            }
        });

        previousDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v == previousDate) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.setAlpha(0.7f);
                    } else {
                        v.setAlpha(1f);
                    }
                }
                return false;
            }
        });
        previousDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dayShift--;
                c.add(Calendar.DATE, -1);
                date.setText(df.format(c.getTime()));
                changeDateDate();
            }
        });

        //init Presenter
        presenter = new HomePresenter(this,sharedPreferences, db,c);
        //check Permission
        presenter.checkPermission((AppCompatActivity) getActivity());
        //check health status
        presenter.healthyStatus(dft.format(c.getTime()));
        //check calorie counter
        presenter.calorieCounterByDate(dft.format(c.getTime()));
        //update Step Counter first
        presenter.stepCounter(dft.format(c.getTime()));
        //update kalori yang terbakar
        presenter.calorieCounterBurnedByDate(c.getTime());

        //check toggle step counter APP tem

        //dipindah ke presenter untuk update pertama;
        //set awal jumlah step nanti akan diganti dengan tanggal
        String drt = sharedPreferences.getString(SharedPrefManager.KEY_STEP_COUNTER_TOGGLE, null);
        if(drt.equals("1")){
            stepSwitch.setChecked(true);
        }else{
            stepSwitch.setChecked(false);
        }

        return  view;
    }

    //change data by dates
    private void changeDateDate() {
        presenter.calorieCounterByDate(dft.format(c.getTime()));
        //ganti status kesehatan letika ganti hari
        presenter.healthyStatus(dft.format(c.getTime()));
        presenter.stepCounter(dft.format(c.getTime()));
        presenter.calorieCounterBurnedByDate(c.getTime());
    }

    @Override
    public void updateHealthy(String sts) {
        status.setText(sts);
    }

    @Override
    public void updateCalorieCounter(String text, String pagi, String siang, String malam, String snack, String calorie) {
        calorieCounter.setText(text);
        sarapanPagiText.setText(pagi);
        makanSiangText.setText(siang);
        makanMalamText.setText(malam);
        snackText.setText(snack);
        kaloriMasuk.setText(calorie);
    }

    @Override
    public void updateCalorieBurned(String burn) {
        kaloriTerbakar.setText(burn);
    }

    @Override
    public void updateProgressBar(int calorie, int maxCalorie){
        counter= 0;
        currentCalorie = calorie;
        calorieProgessBar.setMax(maxCalorie);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (counter <= currentCalorie) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            calorieProgessBar.setProgress(counter);
                        }
                    });
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    counter+=5;
                }
            }
        }).start();
    }

    @Override
    public void updateMinumAir(String data) {
        minumAirText.setText(data);
    }


    @Override
    public void updateStepCounterService(int step) {
        stepText.setText(String.valueOf(step));
    }

    @Override
    public void test(String s) {
        Toasty.info(getActivity(), s).show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}