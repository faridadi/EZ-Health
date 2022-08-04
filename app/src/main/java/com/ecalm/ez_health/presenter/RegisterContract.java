package com.ecalm.ez_health.presenter;

import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.ecalm.ez_health.model.User;

public interface RegisterContract {
    interface View {
        void finishRegister(User user);

        void failedRegister();
    }

    interface Presenter {
        void register(AppCompatActivity activity, EditText uname, EditText age, EditText weight, EditText tall, int gender, int aktivitas, int program);

        boolean checkForm(EditText uname, EditText age, EditText weight, EditText tall);
    }
}