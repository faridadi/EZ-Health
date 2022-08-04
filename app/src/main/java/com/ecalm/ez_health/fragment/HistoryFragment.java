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
import android.widget.Switch;
import android.widget.TextView;

import com.ecalm.e_calm.R;
import com.ecalm.ez_health.HomeActivity;
import com.ecalm.ez_health.adapter.HistoryAdapter;
import com.ecalm.ez_health.adapter.SearchFoodAdapter;
import com.ecalm.ez_health.model.Food;
import com.ecalm.ez_health.model.FoodHistory;
import com.ecalm.ez_health.presenter.HistoryContract;
import com.ecalm.ez_health.presenter.HistoryPresenter;
import com.ecalm.ez_health.presenter.InfoMakananPresenter;
import com.ecalm.ez_health.sqlite.DatabaseHelper;

import java.text.ParseException;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment implements HistoryContract.View{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<FoodHistory> foods;
    RecyclerView recyclerView;
    EditText historySearch;
    HistoryAdapter historyAdapter;
    HistoryContract.Presenter presenter;
    DatabaseHelper db;
    Dialog deleteDialog;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        db = new DatabaseHelper(getActivity());
        foods = new ArrayList<>();
        historySearch = view.findViewById(R.id.historySearch);

        recyclerView = view.findViewById(R.id.historyrecyclerview);
        historyAdapter = new HistoryAdapter();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(historyAdapter);

        historyAdapter.setOnItemClickListener(new HistoryAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if(v.getId() == R.id.editfoodhistory){

                    //Toasty.info(getActivity(), "Edit "+position).show();
                }else if(v.getId() == R.id.deletefoodhistory){
                    showDeleteDialog(position);
                    //Toasty.info(getActivity(), "Delete "+position).show();
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        historySearch.addTextChangedListener(new TextWatcher() {
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



        presenter = new HistoryPresenter(this,db);
        presenter.listFood();
        return view;
    }

    @Override
    public void updateListFood(ArrayList<FoodHistory> food) {
        historyAdapter.setData(food);
        foods = food;
        historyAdapter.notifyDataSetChanged();
    }

    public void showDeleteDialog(final int pos){
        deleteDialog = new Dialog(getActivity());
        deleteDialog.setContentView(R.layout.dialog_delete_makanan);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView yesBtn, noBtn, desc, title;
        yesBtn = deleteDialog.findViewById(R.id.yes_del_makanan_btn);
        noBtn = deleteDialog.findViewById(R.id.no_del_makanan_btn);
        title = deleteDialog.findViewById(R.id.deleteMakananTitle);
        desc = deleteDialog.findViewById(R.id.deleteMakananDesc);
        title.setText("Menghapus Data Histori");
        desc.setText("Apakah Anda ingin menghapus data histori ?");

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.deleteHistoryFood(pos);
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

    @Override
    public void finishDeleteHistory(String msg) {
        Toasty.success(getActivity(), msg).show();
        deleteDialog.dismiss();
    }

    @Override
    public void failedDeleteHistory(String msg) {
        Toasty.error(getActivity(), msg).show();
        deleteDialog.dismiss();
    }
}