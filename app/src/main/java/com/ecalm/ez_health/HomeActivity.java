package com.ecalm.ez_health;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.ecalm.e_calm.R;
import com.ecalm.ez_health.fragment.HomeFragment;
import com.ecalm.ez_health.fragment.InfoMakananFragment;
import com.ecalm.ez_health.fragment.MinumFragment;
import com.ecalm.ez_health.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    LinearLayout fabBlock;
    FloatingActionButton fab, fabpagi, fabsiang, fabmalam, fabsnack;
    private final static String TAG = "DashBoardActivity";
    //angka 1 untuk animasi di awal hpage home
    Boolean fabClicked = false;
    Animation fabPagiOpen, fabSiangOpen, fabMalamOpen, fabSnackOpen, fabBlockOpen;
    Animation fabPagiClose, fabSiangClose, fabMalamClose, fabSnackClose, fabBlockClose;

    int curentPage=1;
    int nextPage=1;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        bottomNavigation = findViewById(R.id.bottom_navigatin_view);
        bottomNavigation.setBackground(null);
        bottomNavigation.getMenu().getItem(2).setEnabled(false);
        bottomNavigation.setOnItemSelectedListener(bnv);

        fabBlock = findViewById(R.id.fabblock);
        fabBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFabAnimation(fabClicked);
                setFabVisibility(fabClicked);
                fabClicked = !fabClicked;
            }
        });

        fabPagiOpen = AnimationUtils.loadAnimation(this, R.anim.fab_pagi_open);
        fabSiangOpen = AnimationUtils.loadAnimation(this, R.anim.fab_siang_open);
        fabMalamOpen = AnimationUtils.loadAnimation(this, R.anim.fab_malam_open);
        fabSnackOpen = AnimationUtils.loadAnimation(this, R.anim.fab_snack_open);
        fabBlockOpen = AnimationUtils.loadAnimation(this, R.anim.fab_block_open);

        fabPagiClose = AnimationUtils.loadAnimation(this, R.anim.fab_pagi_close);
        fabSiangClose = AnimationUtils.loadAnimation(this, R.anim.fab_siang_close);
        fabMalamClose = AnimationUtils.loadAnimation(this, R.anim.fab_malam_close);
        fabSnackClose = AnimationUtils.loadAnimation(this, R.anim.fab_snack_close);
        fabBlockClose = AnimationUtils.loadAnimation(this, R.anim.fab_block_close);

        fab = findViewById(R.id.fab);
        fabpagi = findViewById(R.id.fabpagi);
        fabsiang = findViewById(R.id.fabsiang);
        fabmalam = findViewById(R.id.fabmalam);
        fabsnack = findViewById(R.id.fabsnack);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFabVisibility(fabClicked);
                setFabAnimation(fabClicked);
                fabClicked = !fabClicked;
            }
        });

        fabpagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, ScanActivity.class);
                //1 sarapan, 2 siang, 3 malam, 4 snack
                i.putExtra("tipe",1);
                i.putExtra("date", Calendar.getInstance());
                startActivity(i);
            }
        });

        fabsiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, ScanActivity.class);
                //1 sarapan, 2 siang, 3 malam, 4 snack
                i.putExtra("tipe",2);
                i.putExtra("date", Calendar.getInstance());
                startActivity(i);
            }
        });

        fabmalam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, ScanActivity.class);
                //1 sarapan, 2 siang, 3 malam, 4 snack
                i.putExtra("tipe",3);
                i.putExtra("date", Calendar.getInstance());
                startActivity(i);
            }
        });

        fabsnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, ScanActivity.class);
                //1 sarapan, 2 siang, 3 malam, 4 snack
                i.putExtra("tipe",4);
                i.putExtra("date", Calendar.getInstance());
                startActivity(i);
            }
        });
    }

    private void setFabAnimation(boolean click){
        if (!click){
            fabpagi.startAnimation(fabPagiOpen);
            fabsiang.startAnimation(fabSiangOpen);
            fabmalam.startAnimation(fabMalamOpen);
            fabsnack.startAnimation(fabSnackOpen);
            fabBlock.startAnimation(fabBlockOpen);
        }else{
            fabpagi.startAnimation(fabPagiClose);
            fabsiang.startAnimation(fabSiangClose);
            fabmalam.startAnimation(fabMalamClose);
            fabsnack.startAnimation(fabSnackClose);
            fabBlock.startAnimation(fabBlockClose);
        }
    }

    private  void setFabVisibility(boolean click){
        if (!click){
            fabpagi.setVisibility(View.VISIBLE);
            fabsiang.setVisibility(View.VISIBLE);
            fabmalam.setVisibility(View.VISIBLE);
            fabsnack.setVisibility(View.VISIBLE);
            fabBlock.setVisibility(View.VISIBLE);
        }else{
            fabpagi.setVisibility(View.INVISIBLE);
            fabsiang.setVisibility(View.INVISIBLE);
            fabmalam.setVisibility(View.INVISIBLE);
            fabsnack.setVisibility(View.INVISIBLE);
            fabBlock.setVisibility(View.INVISIBLE);
        }
    }

    //method ganti fragment
    public void openFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        //logic transisi halaman
        if(nextPage > curentPage){ transaction.setCustomAnimations(R.anim.from_right, R.anim.to_left);
        }else if (nextPage < curentPage){
            transaction.setCustomAnimations(R.anim.from_left, R.anim.to_right);
        }
        transaction.replace(R.id.nav_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    NavigationBarView.OnItemSelectedListener bnv = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.homeFragment:
                    nextPage=1;
                    openFragment(HomeFragment.newInstance("", ""));
                    curentPage = nextPage;
                    return true;
                case R.id.infoMakananFragment:
                    nextPage = 2;
                    openFragment(InfoMakananFragment.newInstance("", ""));
                    curentPage = nextPage;
                    return true;
                case R.id.minumFragment:
                    nextPage = 3;
                    openFragment(MinumFragment.newInstance("", ""));
                    curentPage = nextPage;
                    return true;
                case R.id.profileFragment:
                    nextPage = 4;
                    openFragment(ProfileFragment.newInstance("", ""));
                    curentPage = nextPage;
                    return true;
            }
            return false;
        }
    };

    public void changeFragment(int i){
        switch (i){
            case 0:
                bottomNavigation.setSelectedItemId(R.id.homeFragment);
                return;
            case 1:
                bottomNavigation.setSelectedItemId(R.id.infoMakananFragment);
                return;
            case 2:
                bottomNavigation.setSelectedItemId(R.id.scanFragment);
                return;
            case 3:
                bottomNavigation.setSelectedItemId(R.id.minumFragment);
                return;
            case 4:
                bottomNavigation.setSelectedItemId(R.id.profileFragment);
                return;

        }
    }
}