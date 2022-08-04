package com.ecalm.ez_health.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ecalm.ez_health.HomeActivity;
import com.ecalm.e_calm.R;
import com.ecalm.ez_health.presenter.MinumContract;
import com.ecalm.ez_health.presenter.MinumPresenter;
import com.ecalm.ez_health.sqlite.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MinumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MinumFragment extends Fragment implements MinumContract.View {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MinumContract.Presenter presenter;
    DatabaseHelper db;
    SharedPreferences sharedPreferences;

    CheckBox gelas1, gelas2, gelas3, gelas4, gelas5, gelas6, gelas7, gelas8;

    SimpleDateFormat df, dft, dftb;
    Calendar c;
    TextView date, literText;
    ImageButton nextDate, previousDate;

    boolean[] waterCheck = {false, false, false, false, false, false};

    public MinumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MinumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MinumFragment newInstance(String param1, String param2) {
        MinumFragment fragment = new MinumFragment();
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
                ((HomeActivity)getActivity()).changeFragment(0);
            }
        });
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_minum, container, false);
        sharedPreferences = getActivity().getSharedPreferences("fanregisterlogin", Context.MODE_PRIVATE);
        db = new DatabaseHelper(getActivity());

        c = Calendar.getInstance();

        df = new SimpleDateFormat("dd MMMM yyyy");
        dft = new SimpleDateFormat("yyyy-MM-dd");
        dftb = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        literText = view.findViewById(R.id.literTextView);

        date = view.findViewById(R.id.tglminumdate);
        nextDate = view.findViewById(R.id.nextDateMinum);
        previousDate = view.findViewById(R.id.previousDateMinum);

        date.setText(df.format(Calendar.getInstance().getTime()));

        gelas1 = view.findViewById(R.id.gelas1);
        gelas2 = view.findViewById(R.id.gelas2);
        gelas3 = view.findViewById(R.id.gelas3);
        gelas4 = view.findViewById(R.id.gelas4);
        gelas5 = view.findViewById(R.id.gelas5);
        gelas6 = view.findViewById(R.id.gelas6);
        gelas7 = view.findViewById(R.id.gelas7);
        gelas8 = view.findViewById(R.id.gelas8);


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


        gelas1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gelas1.isChecked()){
                    presenter.addMinum(1,c.getTime());
                }else{
                    presenter.addMinum(0,c.getTime());
                }
            }
        });
        gelas2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gelas2.isChecked()){
                    presenter.addMinum(2,c.getTime());
                }else{
                    presenter.addMinum(1,c.getTime());
                }
            }
        });
        gelas3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gelas3.isChecked()){
                    presenter.addMinum(3,c.getTime());
                }else{
                    presenter.addMinum(2,c.getTime());
                }
            }
        });
        gelas4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gelas4.isChecked()){
                    presenter.addMinum(4,c.getTime());
                }else{
                    presenter.addMinum(3,c.getTime());
                }
            }
        });
        gelas5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gelas5.isChecked()){
                    presenter.addMinum(5,c.getTime());
                }else{
                    presenter.addMinum(4,c.getTime());
                }
            }
        });
        gelas6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gelas6.isChecked()){
                    presenter.addMinum(6,c.getTime());
                }else{
                    presenter.addMinum(5,c.getTime());
                }
            }
        });
        gelas7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gelas7.isChecked()){
                    presenter.addMinum(7,c.getTime());
                }else{
                    presenter.addMinum(6,c.getTime());
                }
            }
        });
        gelas8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gelas8.isChecked()){
                    presenter.addMinum(8,c.getTime());
                }else{
                    presenter.addMinum(7,c.getTime());
                }
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
                c.add(Calendar.DATE, 1);
                date.setText(df.format(c.getTime()));
                changeDateDate();
            }
        });

        previousDate.setOnTouchListener(new View.OnTouchListener() {
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

        previousDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.add(Calendar.DATE, -1);
                date.setText(df.format(c.getTime()));
                changeDateDate();
            }
        });

        presenter = new MinumPresenter(this,sharedPreferences, db);

        //counter minum tgl sekarang
        presenter.minumCounterByDate(Calendar.getInstance().getTime());
        return view;


    }

    //change drink count by date
    private void changeDateDate() {
        presenter.minumCounterByDate(c.getTime());
    }

    @Override
    public void updateMinum(int minum) {
        if(minum == 1){
            gelas1.setChecked(true);
            gelas2.setChecked(false);
            gelas3.setChecked(false);
            gelas4.setChecked(false);
            gelas5.setChecked(false);
            gelas6.setChecked(false);
            gelas7.setChecked(false);
            gelas8.setChecked(false);
            literText.setText("0.25 / 2 Liter");
        }else if(minum == 2){
            gelas1.setChecked(true);
            gelas2.setChecked(true);
            gelas3.setChecked(false);
            gelas4.setChecked(false);
            gelas5.setChecked(false);
            gelas6.setChecked(false);
            gelas7.setChecked(false);
            gelas8.setChecked(false);
            literText.setText("0.5 / 2 Liter");
        }else if(minum == 3){
            gelas1.setChecked(true);
            gelas2.setChecked(true);
            gelas3.setChecked(true);
            gelas4.setChecked(false);
            gelas5.setChecked(false);
            gelas6.setChecked(false);
            gelas7.setChecked(false);
            gelas8.setChecked(false);
            literText.setText("0.75 / 2 Liter");
        }else if(minum == 4){
            gelas1.setChecked(true);
            gelas2.setChecked(true);
            gelas3.setChecked(true);
            gelas4.setChecked(true);
            gelas5.setChecked(false);
            gelas6.setChecked(false);
            gelas7.setChecked(false);
            gelas8.setChecked(false);
            literText.setText("1 / 2 Liter");
        }else if(minum == 5){
            gelas1.setChecked(true);
            gelas2.setChecked(true);
            gelas3.setChecked(true);
            gelas4.setChecked(true);
            gelas5.setChecked(true);
            gelas6.setChecked(false);
            gelas7.setChecked(false);
            gelas8.setChecked(false);
            literText.setText("1.25 / 2 Liter");
        }else if(minum == 6){
            gelas1.setChecked(true);
            gelas2.setChecked(true);
            gelas3.setChecked(true);
            gelas4.setChecked(true);
            gelas5.setChecked(true);
            gelas6.setChecked(true);
            gelas7.setChecked(false);
            gelas8.setChecked(false);
            literText.setText("1.5 / 2 Liter");
        }else if(minum == 7){
            gelas1.setChecked(true);
            gelas2.setChecked(true);
            gelas3.setChecked(true);
            gelas4.setChecked(true);
            gelas5.setChecked(true);
            gelas6.setChecked(true);
            gelas7.setChecked(true);
            gelas8.setChecked(false);
            literText.setText("1.75 / 2 Liter");
        }else if(minum == 8){
            gelas1.setChecked(true);
            gelas2.setChecked(true);
            gelas3.setChecked(true);
            gelas4.setChecked(true);
            gelas5.setChecked(true);
            gelas6.setChecked(true);
            gelas7.setChecked(true);
            gelas8.setChecked(true);
            literText.setText("2 / 2 Liter");
        }else if (minum == 0){
            gelas1.setChecked(false);
            gelas2.setChecked(false);
            gelas3.setChecked(false);
            gelas4.setChecked(false);
            gelas5.setChecked(false);
            gelas6.setChecked(false);
            gelas7.setChecked(false);
            gelas8.setChecked(false);
            literText.setText("0 / 2 Liter");
        }else{
            literText.setText("3 / 2 Liter error");
        }
    }
}