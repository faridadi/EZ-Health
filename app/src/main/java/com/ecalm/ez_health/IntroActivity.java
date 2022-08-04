package com.ecalm.ez_health;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ecalm.ez_health.auth.RegisterActivity;
import com.ecalm.ez_health.intro.FirstIntroFragment;
import com.ecalm.ez_health.intro.SecondIntroFragment;
import com.ecalm.ez_health.intro.ThridIntroFragment;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class IntroActivity extends AppIntro {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean isFirstStart = getPrefs.getBoolean("firstStart", false);

        //cek pertama memakai aplikasi
        if (isFirstStart) {
            final Intent i = new Intent(IntroActivity.this, RegisterActivity.class);
            runOnUiThread(new Runnable() {
                @Override public void run() {
                    startActivity(i);
                    finish();
                }
            });
        }else{
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            FirstIntroFragment first = new FirstIntroFragment();
            SecondIntroFragment second = new SecondIntroFragment();
            ThridIntroFragment thrid = new ThridIntroFragment();

            //SliderPage sliderPage = new SliderPage();
            //sliderPage.setTitle("Let's Go");
            //sliderPage.setDescription("^_^");

            //tambah slide
            addSlide(first);
            addSlide(second);
            addSlide(thrid);
            //addSlide(AppIntroFragment.newInstance(sliderPage));

            showSkipButton(false);
            setProgressButtonEnabled(true);
        }
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor e = getPrefs.edit();
        e.putBoolean("firstStart", true);
        e.apply();

        Intent in = new Intent(IntroActivity.this, RegisterActivity.class);
        startActivity(in);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
