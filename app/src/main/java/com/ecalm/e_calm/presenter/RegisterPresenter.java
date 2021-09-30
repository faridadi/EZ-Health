package com.ecalm.e_calm.presenter;

import android.text.TextUtils;
import android.widget.EditText;

import com.ecalm.e_calm.model.User;

public class RegisterPresenter implements RegisterContract.Presenter{

    RegisterContract.View view;

    public RegisterPresenter(RegisterContract.View view) {
        this.view = view;
    }

    @Override
    public void register( EditText uname, EditText age, EditText weight, EditText tall, int gender) {
        if (checkForm(uname, age, weight, tall)) {
            User model = new User(uname.getText().toString(), Integer.parseInt(age.getText().toString()), Float.parseFloat(tall.getText().toString()), Float.parseFloat(weight.getText().toString()), gender);
            view.finishRegister(model);
        } else {
            view.failedRegister();
        }
    }

    @Override
    public boolean checkForm(EditText uname, EditText age, EditText weight, EditText tall) {
        if (isEmpty(uname)) {
            uname.setError("Silahkan isi username");
        } else if (uname.getText().length() < 4) {
            uname.setError("Silahkan isi username minimal 4 huruf");
        }  else if (isEmpty(age)) {
            age.setError("Silahkan isi umur anda");
        } else if (isEmpty(weight)) {
            weight.setError("Silahkan isi berat badan anda");
        } else if (isEmpty(tall)){
            tall.setError("Silahkan isi tinggi badan anda");
        }else {
            return true;
        }
        return false;
    }

    private boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}