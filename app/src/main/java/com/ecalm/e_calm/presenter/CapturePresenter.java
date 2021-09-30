package com.ecalm.e_calm.presenter;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ecalm.e_calm.Classifier;
import com.ecalm.e_calm.SharedPrefManager;
import com.ecalm.e_calm.TensorFlowImageClassifier;

import com.ecalm.e_calm.math.CapitalizeFirstLetter;
import com.ecalm.e_calm.model.Food;
import com.ecalm.e_calm.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CapturePresenter implements CaptureContract.Presenter{

    CaptureContract.View view;
    CaptureContract.Interactor interactor;

    SharedPreferences sharedPreferences;
    float totalCalorie;
    ArrayList<Food> foodArrayList;
    private Executor executor = Executors.newSingleThreadExecutor();
    AssetManager asset;
    private DatabaseHelper db;

    private static final String MODEL_PATH = "detect.tflite";
    //Kuantisasi sesuai config training
    private static final boolean QUANT = true;
    private static final String LABEL_PATH = "file:///android_asset/foodLabel.txt";
    //input image sesuai config training
    private static final int INPUT_SIZE = 300;
    private Classifier classifier;


    public CapturePresenter(CaptureContract.View view, CaptureContract.Interactor interactor, SharedPreferences share, AssetManager asset, DatabaseHelper db) {
        this.view = view;
        //set interactor
        this.interactor = interactor;
        this.interactor.initInteractor(this, share, asset, db);
        this.sharedPreferences = share;
        this.asset = asset;
        this.db = db;
        foodArrayList = new ArrayList<>();
        initTensorFlowAndLoadModel();
    }

    @Override
    public void addFood(int pos){
        float tmp = foodArrayList.get(pos).getCalorie()*foodArrayList.get(pos).getWeight();
        totalCalorie += tmp;
        view.updateAddFood(foodArrayList);
        foodArrayList.remove(pos);
        view.successAddCalorie(tmp);
    }

    @Override
    public void done() {
        int tmp = Integer.parseInt(sharedPreferences.getString(SharedPrefManager.KEY_CALORIE, null));
        tmp += totalCalorie;
        sharedPreferences.edit().putString(SharedPrefManager.KEY_CALORIE, Integer.toString(tmp)).apply();
        view.doneButton(totalCalorie);
    }

    @Override
    public void back() {
        view.backButton();
    }

    @Override
    public Bitmap byteToBitmap(byte[] photo) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        Bitmap tmp = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
        return tmp;
    }

    @Override
    public List<Classifier.Recognition> recognizeFoods(Bitmap bmp) {
        List<Classifier.Recognition> results = classifier.recognizeImage(bmp);
        //result adalah hasil dari tensorflow ada 10 data return
        Food data = db.searchFood(results.get(0).getTitle());
        foodArrayList.add(new Food(CapitalizeFirstLetter.capitaliseName(data.getName()), data.getCalorie()/data.getWeight(), data.getWeight()));
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
        view.Vtest(tmp);
    }
}