package com.ecalm.ez_health.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecalm.ez_health.HomeActivity;
import com.ecalm.e_calm.R;
import com.ecalm.ez_health.model.SharedPrefManager;
import com.ecalm.ez_health.auth.SplashScreenActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ProfileFragmentAdapter adapter;
    ViewPager2 pager2;
    Button biodata, history;
    SharedPreferences sharedPreferences;
    TextView name, age, tall, weight, gender, hobby;
    LinearLayout logout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ((HomeActivity)getActivity()).changeFragment(0);
            }
        });
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        biodata = view.findViewById(R.id.bioText);
        history = view.findViewById(R.id.historyText);
        pager2 = view.findViewById(R.id.profileviewpager);

        sharedPreferences = getActivity().getSharedPreferences(SharedPrefManager.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        FragmentManager fm = getParentFragmentManager();
        adapter = new ProfileFragmentAdapter(fm,getLifecycle());
        pager2.setAdapter(adapter);
        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position == 0){
                    biodata.setBackground(getContext().getDrawable(R.drawable.green_button));
                    biodata.setTextColor(getContext().getColor(R.color.whiteTextColor));
                    history.setBackground(getContext().getDrawable(R.drawable.tranparent_button));
                    history.setTextColor(getContext().getColor(R.color.colorGreen2));
                }else{
                    history.setBackground(getContext().getDrawable(R.drawable.green_button));
                    history.setTextColor(getContext().getColor(R.color.whiteTextColor));
                    biodata.setBackground(getContext().getDrawable(R.drawable.tranparent_button));
                    biodata.setTextColor(getContext().getColor(R.color.colorGreen2));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        biodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager2.setCurrentItem(0);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager2.setCurrentItem(1);
                history.setBackground(getContext().getDrawable(R.drawable.green_button));
            }
        });

        return view;
    }

    private  void showPopUp(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage("Are you Sure ?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPreferences.edit().clear().commit();
                        Intent i = new Intent(getActivity(), SplashScreenActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    }
                }).setNegativeButton("Cancel", null);
        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    public void refreshPager(int i){
        adapter.notifyDataSetChanged();
    }
}