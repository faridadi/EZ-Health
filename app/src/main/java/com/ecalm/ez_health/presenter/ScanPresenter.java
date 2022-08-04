package com.ecalm.ez_health.presenter;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import com.ecalm.ez_health.model.History;
import com.ecalm.ez_health.tensorflowLite.Classifier;
import com.ecalm.ez_health.model.SharedPrefManager;
import com.ecalm.ez_health.tensorflowLite.TensorFlowImageClassifier;

import com.ecalm.ez_health.calculate.CapitalizeFirstLetter;
import com.ecalm.ez_health.model.Food;
import com.ecalm.ez_health.sqlite.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ScanPresenter implements ScanContract.Presenter{

    ScanContract.View view;
    ScanContract.Interactor interactor;
    float akurasiAI = 0.2f;
    SharedPreferences sharedPreferences;
    float totalCalorie = 0;
    ArrayList<Food> foodArrayList;
    ArrayList<Food> searchFoodArrayList;
    private Executor executor = Executors.newSingleThreadExecutor();
    AssetManager asset;
    private DatabaseHelper db;

    private Calendar c;

    private  int tipe;

    private static final String MODEL_PATH = "detect.tflite";
    //Kuantisasi sesuai config training
    private static final boolean QUANT = true;
    private static final String LABEL_PATH = "file:///android_asset/foodLabel.txt";
    //input image sesuai config training
    private static final int INPUT_SIZE = 300;
    private Classifier classifier;


    public ScanPresenter(ScanContract.View view, ScanContract.Interactor interactor, SharedPreferences share, AssetManager asset, DatabaseHelper db, int tipe, Calendar c) {
        this.view = view;
        //set interactor
        this.interactor = interactor;
        this.interactor.initInteractor(this, share, asset, db);
        this.sharedPreferences = share;
        this.asset = asset;
        this.db = db;
        this.tipe = tipe;
        this.c = c;
        foodArrayList = new ArrayList<>();
        searchFoodArrayList = new ArrayList<>();
        initTensorFlowAndLoadModel();
    }

    @Override
    public void addFood(int pos){
        foodArrayList.add(searchFoodArrayList.get(pos));
        view.Vtest(CapitalizeFirstLetter.capitaliseName(searchFoodArrayList.get(pos).getName()) +" ditambahkan",1);
        view.updateCartFood(foodArrayList);
    }

    @Override
    public void removeFood(int pos){
        view.Vtest(CapitalizeFirstLetter.capitaliseName(foodArrayList.get(pos).getName()) +" dihapus",0);
        foodArrayList.remove(pos);
        view.updateCartFood(foodArrayList);
    }

    @Override
    public void done() {
        int total=0;
        for(Food f : foodArrayList){
            total+=f.getCalorie();
        }
        view.doneButton(total);
    }


    //method commit kalori ke database
    @Override
    public void commit() {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        String date = dft.format(c.getTime());

        //menghapus data history tanggal dan tipe
        db.clearHistoryForUpdate(date,tipe);
        for (Food f : foodArrayList){
            History data = new History(tipe,f.getId(),this.c.getTime());
            db.insertHistory(data);
        }
    }

    @Override
    public void back() {
        view.backButton();
    }

    @Override
    public void searchFood(String Name) {
        try {
            searchFoodArrayList = new ArrayList<Food>(db.searchFood(Name));
            view.updateSearchFood(searchFoodArrayList);
        }catch (Exception e){
            view.Vtest("Tidak menemukan makanan",2);
        }
    }

    @Override
    public Bitmap byteToBitmap(byte[] photo) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        Bitmap dstBmp;
        //merubah aspek rasio foto 1:1
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

        dstBmp = Bitmap.createScaledBitmap(dstBmp, INPUT_SIZE, INPUT_SIZE, false);
        return dstBmp;
    }

    @Override
    public void loadHistoryFood(Calendar c, int tipe) {
        try {
            SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
            String date = dft.format(c.getTime());
            ArrayList<Food> food = new ArrayList<Food>();
            food = new ArrayList<Food>(db.getHistoryFoodByDate(date, tipe));
            foodArrayList = food;
            view.updateCartFood(food);
        }catch (Exception e){

        }
    }

    @Override
    public List<Classifier.Recognition> recognizeFoods(Bitmap bmp) {
        boolean status = false;
        List<Classifier.Recognition> results = classifier.recognizeImage(bmp);
        //result adalah hasil dari tensorflow ada 10 data return
        for(Classifier.Recognition cl : results){
            if(cl.getConfidence() >= akurasiAI){
                Food data = db.searchFoodName(cl.getTitle());
                if(data.getName() != null && !data.getName().isEmpty() && !data.getName().equals("null")) {
                    foodArrayList.add(new Food(data.getId(), CapitalizeFirstLetter.capitaliseName(data.getName()), data.getCalorie(), data.getWeight()));
                    status = true;
                }
            }
        }

        if(status){
            view.Vtest("Ai mendeteksi jenis Makanan",1);
        }else{
            view.Vtest("Ai Tidak Mendeteksi jenis Makanan",2);
        }

        view.updateRecognizeFood(foodArrayList);
        return results;
    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            asset,
                            MODEL_PATH,
                            LABEL_PATH,
                            INPUT_SIZE,
                            QUANT);
                    view.makeButtonVisible();
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }

    @Override
    public void Ptest(String text) {
        String tmp = interactor.Itest(text);
        view.Vtest(tmp,1);
    }
}