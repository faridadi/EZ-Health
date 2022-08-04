package com.ecalm.ez_health.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ecalm.e_calm.R;
import com.ecalm.ez_health.auth.SplashScreenActivity;
import com.ecalm.ez_health.model.SharedPrefManager;
import com.ecalm.ez_health.model.User;
import com.ecalm.ez_health.presenter.BiodataContract;
import com.ecalm.ez_health.presenter.BiodataPresenter;
import com.ecalm.ez_health.sqlite.DatabaseHelper;
import com.google.android.material.textfield.TextInputLayout;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BiodataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BiodataFragment extends Fragment implements BiodataContract.View {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    BiodataContract.Presenter presenter;

    String[] aktivitasItem = {"Jarang melakukan olahraga", "Berolahraga 1-3 hari dalam semingu", "Berolahraga 3-5 hari dalam semingu", "Berolahraga 5-7 hari dalam semingu"};
    String[] programItem = {"Mempertahankan berat badan", "Menambah berat badan", "Mengurangi berat badan"};
    ArrayAdapter<String> aktivitasAdapter;
    ArrayAdapter<String> programAdapter;

    SharedPreferences sharedPreferences;
    EditText name, age, tall, weight;
    TextView calorieLimit;
    RadioGroup gender;
    Button delAccount, delData;
    ImageView imgProfile;

    Button save;
    //tipe data untuk menyimpan perubahan
    int act = 0, prog = 0, gen = 0;

    TextInputLayout aktivitasTextInputLayout, programTextInputLayout;
    AutoCompleteTextView aktivitasAutoCompleteTextView, programAutoCompleteTextView;

    public BiodataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BiodataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BiodataFragment newInstance(String param1, String param2) {
        BiodataFragment fragment = new BiodataFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_biodata, container, false);

        sharedPreferences = getActivity().getSharedPreferences(SharedPrefManager.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        name = view.findViewById(R.id.profileNamaText);
        calorieLimit = view.findViewById(R.id.bio_calorie_limit);
        gender = view.findViewById(R.id.bio_gender_radio);
        age = view.findViewById(R.id.profileUmurText);
        tall = view.findViewById(R.id.profileTbText);
        weight = view.findViewById(R.id.profileBbText);
        save = view.findViewById(R.id.update_bio_btn);
        delAccount = view.findViewById(R.id.profilehapusakun);
        delData = view.findViewById(R.id.profilehapusdata);

        imgProfile = view.findViewById(R.id.imageProfile);

        name.setText(sharedPreferences.getString(SharedPrefManager.KEY_NAME, null));
        calorieLimit.setText(sharedPreferences.getString(SharedPrefManager.KEY_TDEE, null) + "");
        age.setText(sharedPreferences.getString(SharedPrefManager.KEY_AGE, null) + "");
        tall.setText(sharedPreferences.getString(SharedPrefManager.KEY_TALL, null) + "");
        weight.setText(sharedPreferences.getString(SharedPrefManager.KEY_WEIGHT, null) + "");

        if (sharedPreferences.getString(SharedPrefManager.KEY_GENDER, null).equals("0")) {
            gender.check(R.id.bio_gender_male);
            gen = 0;
            imgProfile.setImageResource(R.drawable.ic_men);
        } else {
            gender.check(R.id.bio_gender_female);
            gen = 1;
            imgProfile.setImageResource(R.drawable.ic_woman);
        }

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.bio_gender_male:
                        gen = 0;
                        //Toasty.normal(RegisterActivity.this, "Male").show();
                        break;
                    case R.id.bio_gender_female:
                        gen = 1;
                        //Toasty.normal(RegisterActivity.this, "Female").show();
                        break;
                }
            }
        });

        //dropdown untuk Aktivitas
        aktivitasTextInputLayout = view.findViewById(R.id.aktivitas_menu);
        aktivitasAutoCompleteTextView = view.findViewById(R.id.aktivitas_item);
        aktivitasAdapter = new ArrayAdapter<>(getActivity(), R.layout.aktivitas_item, aktivitasItem);
        if (sharedPreferences.getString(SharedPrefManager.KEY_ACTIVITY, null).equals("0")) {
            aktivitasAutoCompleteTextView.setText(aktivitasAdapter.getItem(0));
            act = 0;
        } else if (sharedPreferences.getString(SharedPrefManager.KEY_ACTIVITY, null).equals("1")) {
            aktivitasAutoCompleteTextView.setText(aktivitasAdapter.getItem(1));
            act = 1;
        } else {
            aktivitasAutoCompleteTextView.setText(aktivitasAdapter.getItem(2));
            act = 2;
        }
        aktivitasAutoCompleteTextView.setAdapter(aktivitasAdapter);
        aktivitasAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                act = i;
                aktivitasAutoCompleteTextView.dismissDropDown();
            }
        });

        //dropdown untuk program
        programTextInputLayout = view.findViewById(R.id.program_menu);
        programAutoCompleteTextView = view.findViewById(R.id.program_item);
        programAdapter = new ArrayAdapter<>(getActivity(), R.layout.program_item, programItem);
        if (sharedPreferences.getString(SharedPrefManager.KEY_PROGRAM, null).equals("0")) {
            programAutoCompleteTextView.setText(programAdapter.getItem(0));
            prog = 0;
        } else if (sharedPreferences.getString(SharedPrefManager.KEY_PROGRAM, null).equals("1")) {
            programAutoCompleteTextView.setText(programAdapter.getItem(1));
            prog = 1;

        } else if (sharedPreferences.getString(SharedPrefManager.KEY_PROGRAM, null).equals("2")) {
            programAutoCompleteTextView.setText(programAdapter.getItem(2));
            prog = 2;

        }else {
            programAutoCompleteTextView.setText(programAdapter.getItem(3));
            prog = 3;
        }
        programAutoCompleteTextView.setAdapter(programAdapter);
        programAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                prog = i;
                programAutoCompleteTextView.dismissDropDown();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    presenter.updateData(getContext(), name, age, weight, tall, gen, act, prog);
                } catch (Exception e) {
                    toastGagal("Sistem tidak berhasil merubah data");
                }
            }
        });

        delData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUpDeleteData();
            }
        });

        delAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpDeleteAccount();
            }
        });

        presenter = new BiodataPresenter(this);
        return view;
    }

    private void showPopUpDeleteAccount() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage("Apakah anda yakin ingin menghapus akun ?")
                .setPositiveButton("Hapus Akun", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPreferences.edit().clear().commit();
                        DatabaseHelper db = new DatabaseHelper(getContext());
                        db.deleteAlldata();
                        Intent i = new Intent(getActivity(), SplashScreenActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    }
                }).setNegativeButton("Tidak", null);
        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    private void showPopUpDeleteData() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage("Apakah anda yakin ingin menghapus semua data yang sudah tersimpan ?")
                .setPositiveButton("Hapus Data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper db = new DatabaseHelper(getContext());
                        db.deleteAlldata();
                    }
                }).setNegativeButton("Tidak", null);
        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    @Override
    public void finishUpdate(User user) {
        //object user belum dipakai
        name.setText(sharedPreferences.getString(SharedPrefManager.KEY_NAME, null));
        calorieLimit.setText(sharedPreferences.getString(SharedPrefManager.KEY_TDEE, null) + "");
        age.setText(sharedPreferences.getString(SharedPrefManager.KEY_AGE, null) + "");
        tall.setText(sharedPreferences.getString(SharedPrefManager.KEY_TALL, null) + "");
        weight.setText(sharedPreferences.getString(SharedPrefManager.KEY_WEIGHT, null) + "");

        aktivitasAdapter = new ArrayAdapter<>(getActivity(), R.layout.aktivitas_item, aktivitasItem);
        if (sharedPreferences.getString(SharedPrefManager.KEY_ACTIVITY, null).equals("0")) {
            aktivitasAutoCompleteTextView.setText(aktivitasAdapter.getItem(0));
            act = 0;
        } else if (sharedPreferences.getString(SharedPrefManager.KEY_ACTIVITY, null).equals("1")) {
            aktivitasAutoCompleteTextView.setText(aktivitasAdapter.getItem(1));
            act = 1;
        } else {
            aktivitasAutoCompleteTextView.setText(aktivitasAdapter.getItem(2));
            act = 2;
        }
        aktivitasAutoCompleteTextView.setAdapter(aktivitasAdapter);
        aktivitasAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                act = i;
            }
        });

        if (sharedPreferences.getString(SharedPrefManager.KEY_GENDER, null).equals("0")) {
            gender.check(R.id.bio_gender_male);
            gen = 0;
            imgProfile.setImageResource(R.drawable.ic_men);
        } else {
            gender.check(R.id.bio_gender_female);
            gen = 1;
            imgProfile.setImageResource(R.drawable.ic_woman);
        }

        programAdapter = new ArrayAdapter<>(getActivity(), R.layout.program_item, programItem);
        if (sharedPreferences.getString(SharedPrefManager.KEY_PROGRAM, null).equals("0")) {
            programAutoCompleteTextView.setText(programAdapter.getItem(0));
            prog = 0;
        } else if (sharedPreferences.getString(SharedPrefManager.KEY_PROGRAM, null).equals("1")) {
            programAutoCompleteTextView.setText(programAdapter.getItem(1));
            prog = 1;
        } else {
            programAutoCompleteTextView.setText(programAdapter.getItem(2));
            prog = 2;
        }
        programAutoCompleteTextView.setAdapter(programAdapter);
        programAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                prog = i;
            }
        });
        //menghilangkan dropdown setelah update profile
        programAutoCompleteTextView.dismissDropDown();
        aktivitasAutoCompleteTextView.dismissDropDown();
    }

    @Override
    public void toastGagal(String s) {
        Toasty.error(getActivity(), s).show();
    }

    @Override
    public void toastBerhasil(String s) {
        Toasty.success(getActivity(), s).show();
    }
}