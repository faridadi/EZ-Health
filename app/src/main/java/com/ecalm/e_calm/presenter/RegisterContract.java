package com.ecalm.e_calm.presenter;

import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.ecalm.e_calm.model.User;

public interface RegisterContract {
    interface View{
        void finishRegister(User user);
        void failedRegister();
    }

    interface Presenter{
        void register(EditText uname, EditText age, EditText weight, EditText tall, int gender);
        boolean checkForm(EditText uname, EditText age, EditText weight, EditText tall);
    }
}
