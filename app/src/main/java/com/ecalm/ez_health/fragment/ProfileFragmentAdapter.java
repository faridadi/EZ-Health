package com.ecalm.ez_health.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ProfileFragmentAdapter extends FragmentStateAdapter {
    public ProfileFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public ProfileFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public ProfileFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0 :
                return new BiodataFragment();
            case 1 :
                return new HistoryFragment();

        }
        return new BiodataFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
