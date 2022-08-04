package com.ecalm.ez_health;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.camerakit.CameraKit;
import com.camerakit.CameraKitView;
import com.ecalm.e_calm.R;
import com.ecalm.ez_health.adapter.FoodAdapter;
import com.ecalm.ez_health.adapter.ScanSearchAdapter;
import com.ecalm.ez_health.tensorflowLite.Classifier;
import com.ecalm.ez_health.model.Food;
import com.ecalm.ez_health.model.SharedPrefManager;
import com.ecalm.ez_health.presenter.ScanContract;
import com.ecalm.ez_health.presenter.ScanInteractor;
import com.ecalm.ez_health.presenter.ScanPresenter;
import com.ecalm.ez_health.sqlite.DatabaseHelper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ScanActivity extends AppCompatActivity implements ScanContract.View {

    int heightBottomSheetDP = 45;
    FrameLayout scansearch;
    EditText searchfood;
    TextView totalCalorie;
    ArrayList<Food> foods;
    RecyclerView searchrecycler;
    ScanSearchAdapter scanSearchAdapter;
    TextView searchFoodBtn, finish, retake;
    Dialog infoDialog;
    //awal toggel true untuk foto
    boolean searchOrPhotoToggle = true;


    ScanContract.Presenter presenter;
    private ImageButton btnCapture, flash;
    private Boolean flashOn = false;
    private Boolean capture = true;

    private ImageView imageViewResult;
    private CameraKitView cameraKitView;

    int tipe;
    Calendar c;

    ConstraintLayout cameraAction;
    int heightCameraAction=0;
    RecyclerView recyclerView;
    FoodAdapter foodAdapter;
    LinearLayout bottomSheetLayout;
    BottomSheetBehavior bottomSheetBehavior;

    TextView back;
    ProgressDialog mProgressDialog;

    SharedPreferences sharedPreferences;
    AssetManager asset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asset = getAssets();

        setContentView(R.layout.activity_scan);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //get data tipe from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //get tipe makanan
            tipe = extras.getInt("tipe");
            //get tanggal kapan makan
            c = (Calendar) extras.getSerializable("date");
        }else{
            tipe = 1;
            c= Calendar.getInstance();
        }

        DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        int maxHeight = (int) (height*0.50);


        cameraAction = findViewById(R.id.cameraaction);
        cameraAction.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                heightCameraAction = bottom - top + heightBottomSheetDP;
            }
        });


        scansearch = findViewById(R.id.scansearch);
        searchFoodBtn = findViewById(R.id.carimanualbtn);
        searchFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchOrPhotoToggle){
                    cameraKitView.setVisibility(View.GONE);
                    imageViewResult.setVisibility(View.GONE);
                    retake.setVisibility(View.GONE);
                    scansearch.setVisibility(View.VISIBLE);
                    searchFoodBtn.setText("Foto Makanan");
                    searchFoodBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_scan_photo_camera_24, 0, 0, 0);
                    bottomSheetBehavior.setPeekHeight(heightCameraAction);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                    searchOrPhotoToggle = false;
                }else{
                    scansearch.setVisibility(View.GONE);
                    retake.setVisibility(View.VISIBLE);
                    searchFoodBtn.setText("Cari Makanan");
                    searchFoodBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_search_24, 0, 0, 0);
                    btnCapture.setImageResource(R.drawable.ic_baseline_camera_24);
                    imageViewResult.setVisibility(android.view.View.GONE);
                    bottomSheetBehavior.setPeekHeight(dpToPx(heightBottomSheetDP),true);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    cameraKitView.setVisibility(android.view.View.VISIBLE);
                    capture =!capture;
                    searchOrPhotoToggle = true;
                }
            }
        });

        retake= findViewById(R.id.retakeBottomSheet);
        retake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setPeekHeight(dpToPx(heightBottomSheetDP));
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                btnCapture.setImageResource(R.drawable.ic_baseline_camera_24);
                imageViewResult.setVisibility(android.view.View.GONE);
                cameraKitView.setVisibility(android.view.View.VISIBLE);
                capture =!capture;
            }
        });

        totalCalorie = findViewById(R.id.totalcaloriebottomsheet);
        searchfood = findViewById(R.id.searchfoodscan);
        searchrecycler = findViewById(R.id.scanrecycler);
        scanSearchAdapter = new ScanSearchAdapter();

        RecyclerView.LayoutManager layoutManagerScan = new LinearLayoutManager(this);
        searchrecycler.setLayoutManager(layoutManagerScan);
        searchrecycler.setAdapter(scanSearchAdapter);

        scanSearchAdapter.setOnItemClickListener(new ScanSearchAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if(v.getId() == R.id.tambahfoodscan){
                    presenter.addFood(position);
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        searchfood.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    presenter.searchFood(charSequence.toString());
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        bottomSheetLayout = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        //bottomSheetLayout.getLayoutParams().height = heightCameraAction;//maxHeight;
        //bottomSheetBehavior.setPeekHeight((int)(height*0.15));
        bottomSheetBehavior.setPeekHeight(dpToPx(heightBottomSheetDP));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        mProgressDialog = new ProgressDialog(ScanActivity.this);

        recyclerView = findViewById(R.id.Recycler);
        foodAdapter = new FoodAdapter();

        //Recycle item click listener
        foodAdapter.setOnItemClickListener(new FoodAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, android.view.View v) {
                //presenter.addFood(position);
                if(v.getId() == R.id.deletescanfood){
                    presenter.removeFood(position);
                }
            }
            @Override
            public void onItemLongClick(int position, android.view.View v) {

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(ScanActivity.this);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(foodAdapter);

        cameraKitView = findViewById(R.id.camera);
        cameraKitView.setFacing(CameraKit.FACING_BACK);
        cameraKitView.setImageMegaPixels(0.5f);
        cameraKitView.setAspectRatio(1f);
        cameraKitView.setFocus(CameraKit.FOCUS_AUTO);

        flash = findViewById(R.id.flashbtn);

        imageViewResult = findViewById(R.id.preview);

        back = findViewById(R.id.backCapture);

        back.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                presenter.back();
            }
        });

        //init Database
        final DatabaseHelper db = new DatabaseHelper(this);

        //init SharedPreference
        sharedPreferences = this.getSharedPreferences(SharedPrefManager.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        btnCapture = findViewById(R.id.btn_capture);

        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flashOn){
                    flash.setImageResource(R.drawable.ic_baseline_flash_on_24);
                    cameraKitView.setFlash(CameraKit.FLASH_ON);
                    flashOn = !flashOn;
                    Toasty.info(ScanActivity.this, "Flash ON",200).show();
                }else{
                    flash.setImageResource(R.drawable.ic_baseline_flash_off_24);
                    cameraKitView.setFlash(CameraKit.FLASH_OFF);
                    flashOn = !flashOn;
                    Toasty.info(ScanActivity.this, "Flash OFF",200).show();
                }
            }
        });

        //Capture Food
        btnCapture.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if(capture){
                    btnCapture.setImageResource(R.drawable.ic_baseline_retake_camera_android_24);
                    bottomSheetBehavior.setPeekHeight(heightCameraAction);
                    //bottomSheetLayout.getLayoutParams().height = heightCameraAction;
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    recognizeImage();
                    capture =!capture;
                }else{
                    btnCapture.setImageResource(R.drawable.ic_baseline_camera_24);
                    imageViewResult.setVisibility(android.view.View.GONE);
                    cameraKitView.setVisibility(android.view.View.VISIBLE);
                    capture =!capture;
                }

            }
        });
        finish = findViewById(R.id.finishbottomsheetbtn);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.done();
            }
        });

        presenter = new ScanPresenter(this, new ScanInteractor(),sharedPreferences, asset, db, tipe,c);
        presenter.loadHistoryFood(c, tipe);
        showInfoDialog();

    }

    //Done Button
    @Override
    public void doneButton(float kcal){
        AlertDialog.Builder alert = new AlertDialog.Builder(ScanActivity.this);
        alert.setMessage(kcal+" Kalori akan dismpan\n Apakah Anda sudah Selesai ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //commit kalorie je database
                        presenter.commit();
                        onBackPressed();
                    }
                }).setNegativeButton("Tidak", null);
        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    //Back Button
    @Override
    public void backButton() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ScanActivity.this);
        alert.setMessage("Apakah anda mau kembali ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                }).setNegativeButton("Tidak", null);
        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    @Override
    public void updateSearchFood(ArrayList<Food> food) {
        scanSearchAdapter.setData(food);
        scanSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void successAddCalorie(float kcal) {
        Toasty.success(ScanActivity.this, "Calorie Added "+ kcal).show();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void updateRecognizeFood(ArrayList<Food> food) {
        foodAdapter.setData(food);
        //jumlah total calori yang di cart
        int totCal= 0;
        for(Food foods : food){
            totCal += foods.getCalorie();
        }
        totalCalorie.setText(totCal+" Kalori");
        foodAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void updateCartFood(ArrayList<Food> food) {
        foodAdapter.setData(food);
        int totCal= 0;
        for(Food foods : food){
            totCal += foods.getCalorie();
        }
        totalCalorie.setText(totCal+" Kalori");
        foodAdapter.notifyDataSetChanged();
    }

    @Override
    public void makeButtonVisible() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnCapture.setVisibility(android.view.View.VISIBLE);
            }
        });
    }

    private void recognizeImage(){
        btnCapture.setVisibility(View.INVISIBLE);
        startLoadingProcessingImage();
        //settingan megapixel di deklarasi
        cameraKitView.captureImage(new CameraKitView.ImageCallback() {
            @Override
            public void onImage(CameraKitView cameraKitView, final byte[] photo) {
                try {
                    //convert raw foto ke Bitmap
                    Bitmap tmp = presenter.byteToBitmap(photo);
                    //Mencari data dari foto
                    List<Classifier.Recognition> results = presenter.recognizeFoods(tmp);
                    imageViewResult.setImageBitmap(byteToBitmapOriginal(photo));
                    imageViewResult.setVisibility(android.view.View.VISIBLE);
                    cameraKitView.setVisibility(android.view.View.GONE);
                    //test confidence data 0 - 1
                    //Toasty.warning(ScanActivity.this, String.valueOf(results.get(0).getConfidence())).show();

                }catch (Exception e){
                    Vtest("Coba lagi",2);
                }
                finishLoadingProcessingImage();
                btnCapture.setVisibility(android.view.View.VISIBLE);
            }
        });
    }

    public Bitmap byteToBitmapOriginal(byte[] photo) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        Bitmap dstBmp;
        //merubah aspek rasio menjadi 1:1
        if (bitmap.getWidth() >= bitmap.getHeight()){
            dstBmp = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth()/2 - bitmap.getHeight()/2,
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight()
            );
        }else{
            dstBmp = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight()/2 - bitmap.getWidth()/2,
                    bitmap.getWidth(),
                    bitmap.getWidth()
            );
        }
        return dstBmp;
    }

    private void startLoadingProcessingImage(){
        mProgressDialog.setTitle("Processing Image");
        mProgressDialog.setMessage("Please Wait");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }
    private void finishLoadingProcessingImage(){
        mProgressDialog.dismiss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        //onresume error
        cameraKitView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //on pause error
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        cameraKitView.onStop();
        Intent i = new Intent(ScanActivity.this, HomeActivity.class);
        i.putExtra("scanDate", c);
        startActivity(i);
        finish();
    }

    @Override
    public void Vtest(String text,int code) {
        if(code == 1){
            Toasty.success(ScanActivity.this, text).show();
        }else if (code == 2){
            Toasty.warning(ScanActivity.this, text).show();
        }else{
            Toasty.error(ScanActivity.this, text).show();
        }
    }

    private int  dpToPx(int dp){
        return (dp* (int)getBaseContext().getResources().getDisplayMetrics().density);
    }
    private int  pxToDp(int px){
        return (px / (int)getBaseContext().getResources().getDisplayMetrics().density);
    }

    public void showInfoDialog(){
        infoDialog = new Dialog(this);
        infoDialog.setContentView(R.layout.dialog_info_scan);
        infoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button close = infoDialog.findViewById(R.id.close_info);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoDialog.cancel();
            }
        });

        infoDialog.show();
    }

}