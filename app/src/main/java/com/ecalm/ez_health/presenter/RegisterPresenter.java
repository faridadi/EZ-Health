package com.ecalm.ez_health.presenter;

import android.text.TextUtils;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.ecalm.ez_health.model.SharedPrefManager;
import com.ecalm.ez_health.model.User;

public class RegisterPresenter implements RegisterContract.Presenter{

    RegisterContract.View view;

    public RegisterPresenter(RegisterContract.View view) {
        this.view = view;
    }

    @Override
    public void register(AppCompatActivity activity, EditText uname, EditText age, EditText weight, EditText tall, int gender, int aktivitas, int program) {
        if (checkForm(uname, age, weight, tall)) {
            User model = new User(uname.getText().toString(), Integer.parseInt(age.getText().toString()), Float.parseFloat(tall.getText().toString()), Float.parseFloat(weight.getText().toString()), gender, aktivitas,program);
            SharedPrefManager.getInstance(activity).userRegister(model);
            view.finishRegister(model);
        } else {
            view.failedRegister();
        }
    }

    @Override
    public boolean checkForm(EditText uname, EditText age, EditText weight, EditText tall) {
        if (isEmpty(uname)) {
            uname.setError("Silahkan isi username");
            return false;
        }
        if (uname.getText().length() < 4) {
            uname.setError("Silahkan isi nama anda");
            return false;
        }
        if (isEmpty(age)) {
            age.setError("Silahkan isi umur anda");
            return false;
        }
        if (!isInteger(age.getText().toString())) {
            age.setError("Data Umur anda bukan data angka");
            return false;
        }

        if (isEmpty(weight)) {
            weight.setError("Silahkan isi berat badan anda");
            return false;
        }
        if (!isFloat(weight.getText().toString())||!isInteger(weight.getText().toString())) {
            weight.setError("Data Berat badan anda bukan data angka");
            return false;
        }

        if (isEmpty(tall)){
            tall.setError("Silahkan isi tinggi badan anda");
            return false;
        }
        if (!isFloat(tall.getText().toString())||!isInteger(tall.getText().toString())) {
            tall.setError("Data tinggi badan anda bukan data angka");
            return false;
        }

        return true;
    }

    private boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

}