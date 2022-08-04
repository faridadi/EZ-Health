package com.ecalm.ez_health.presenter;

import android.content.Context;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.ecalm.ez_health.model.User;

public interface BiodataContract {
    interface View {
        void finishUpdate(User user);

        void toastGagal(String s);

        void toastBerhasil(String s);
    }

    interface Presenter {
        void updateData(Context activity, EditText uname, EditText age, EditText weight, EditText tall, int gender, int aktivitas, int program);

        boolean checkForm(EditText uname, EditText age, EditText weight, EditText tall);
    }
}