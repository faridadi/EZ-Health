package com.ecalm.ez_health.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.ecalm.ez_health.model.SharedPrefManager;
import com.ecalm.ez_health.model.User;

public class BiodataPresenter implements BiodataContract.Presenter {

    BiodataContract.View view;

    public BiodataPresenter(BiodataContract.View view) {
        this.view = view;
    }

    @Override
    public void updateData(Context activity, EditText uname, EditText age, EditText weight, EditText tall, int gender, int aktivitas, int program) {
        if (checkForm(uname, age, weight, tall)) {
            User model = new User(uname.getText().toString(), Integer.parseInt(age.getText().toString()), Float.parseFloat(tall.getText().toString()), Float.parseFloat(weight.getText().toString()), gender, aktivitas, program);
            SharedPrefManager.getInstance(activity).userRegister(model);
            view.finishUpdate(model);
            view.toastBerhasil("Data diri berhasil dirubah");
        } else {
            view.toastGagal("Data form yang and isi tidak sesuai");
        }
    }

    @Override
    public boolean checkForm(EditText uname, EditText age, EditText weight, EditText tall) {
        if (isEmpty(uname)) {
            uname.setError("Silahkan isi username");
        } else if (uname.getText().length() < 4) {
            uname.setError("Silahkan isi username minimal 4 huruf");
        } else if (isEmpty(age)) {
            age.setError("Silahkan isi umur anda");
        } else if (isEmpty(weight)) {
            weight.setError("Silahkan isi berat badan anda");
        } else if (isEmpty(tall)) {
            tall.setError("Silahkan isi tinggi badan anda");
        } else {
            return true;
        }
        return false;
    }

    private boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}