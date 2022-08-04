package com.ecalm.ez_health.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ecalm.ez_health.HomeActivity;
import com.ecalm.e_calm.R;
import com.ecalm.ez_health.adapter.SearchFoodAdapter;
import com.ecalm.ez_health.model.Food;
import com.ecalm.ez_health.presenter.InfoMakananContract;
import com.ecalm.ez_health.presenter.InfoMakananPresenter;
import com.ecalm.ez_health.sqlite.DatabaseHelper;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoMakananFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoMakananFragment extends Fragment implements InfoMakananContract.View {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Dialog tambahDialog, editDialog, deleteDialog;

    ArrayList<Food> foods;
    RecyclerView recyclerView;
    SearchFoodAdapter searchFoodAdapter;

    EditText searchFood;

    TextView tambahBtn;
    InfoMakananContract.Presenter presenter;
    DatabaseHelper db;

    public InfoMakananFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoMakananFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoMakananFragment newInstance(String param1, String param2) {
        InfoMakananFragment fragment = new InfoMakananFragment();
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
        View view = inflater.inflate(R.layout.fragment_info_makanan, container, false);
        db = new DatabaseHelper(getActivity());
        foods = new ArrayList<>();

        tambahBtn = view.findViewById(R.id.tambahMakanan);
        tambahBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTambahDialog();
            }
        });

        searchFood = view.findViewById(R.id.searchfoodedittext);
        recyclerView = view.findViewById(R.id.searchfoodrecyclerview);
        searchFoodAdapter = new SearchFoodAdapter();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(searchFoodAdapter);

        searchFoodAdapter.setOnItemClickListener(new SearchFoodAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                //listener untuk tombol edit
                if(v.getId() == R.id.editfood){
                    showEditDialog(presenter.getFoodInfo(position));
                }else if(v.getId() == R.id.deletefood){
                    showDeleteDialog(presenter.getFoodInfo(position).getId());
                    //Toasty.info(getActivity(),"Delete food id "+presenter.getFoodInfo(position).getId()).show();
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        searchFood.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    presenter.searchFood(charSequence.toString());
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        presenter = new InfoMakananPresenter(this,db);
        presenter.listFood();
        return  view;
    }

    @Override
    public void updateListFood(ArrayList<Food> food) {
        searchFoodAdapter.setData(food);
        foods = food;
        searchFoodAdapter.notifyDataSetChanged();
    }

    @Override
    public void finishAddNewFood(String msg) {
        Toasty.success(getActivity(), msg).show();
        tambahDialog.dismiss();
    }

    @Override
    public void failedAddNewFood(String msg) {
        Toasty.warning(getActivity(), msg).show();
    }

    @Override
    public void finishEditFood(String msg) {
        Toasty.success(getActivity(), msg).show();
        editDialog.dismiss();
    }

    @Override
    public void failedEditFood(String msg) {
        Toasty.error(getActivity(), msg).show();
    }

    @Override
    public void finishDeleteFood(String msg) {
        Toasty.success(getActivity(), msg).show();
        deleteDialog.dismiss();
    }

    @Override
    public void failedDeleteFood(String msg) {
        Toasty.warning(getActivity(), msg).show();
    }

    public void showTambahDialog(){
        tambahDialog = new Dialog(getActivity());
        tambahDialog.setContentView(R.layout.dialog_tambah_makanan);
        tambahDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText nama, kalori, berat;
        TextView tambahBtn;

        nama = tambahDialog.findViewById(R.id.tambahMakananNama);
        kalori = tambahDialog.findViewById(R.id.tambahMakanankalori);
        berat = tambahDialog.findViewById(R.id.tambahMakananberat);
        tambahBtn = tambahDialog.findViewById(R.id.tambahMakananbtn);

        tambahBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addNewFood(nama, kalori, berat);
            }
        });
        tambahDialog.show();
    }

    public void showEditDialog(final Food food){
        editDialog = new Dialog(getActivity());
        editDialog.setContentView(R.layout.dialog_edit_makanan);
        editDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText nama, kalori, berat;
        TextView editBtn;

        nama = editDialog.findViewById(R.id.editMakananNama);
        kalori = editDialog.findViewById(R.id.editMakanankalori);
        berat = editDialog.findViewById(R.id.editMakananberat);
        editBtn = editDialog.findViewById(R.id.editMakananbtn);

        nama.setText(food.getName());
        kalori.setText(food.getCalorie()+"");
        berat.setText(food.getWeight()+"");


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.editFood(food.getId(),nama, kalori, berat);
            }
        });
        editDialog.show();
    }

    public void showDeleteDialog(final int id){
        deleteDialog = new Dialog(getActivity());
        deleteDialog.setContentView(R.layout.dialog_delete_makanan);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView yesBtn, noBtn;
        yesBtn = deleteDialog.findViewById(R.id.yes_del_makanan_btn);
        noBtn = deleteDialog.findViewById(R.id.no_del_makanan_btn);

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.removeFood( id);
                deleteDialog.dismiss();
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show();
    }
}