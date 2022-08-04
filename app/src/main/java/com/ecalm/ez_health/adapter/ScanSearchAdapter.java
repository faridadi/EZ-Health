package com.ecalm.ez_health.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ecalm.e_calm.R;
import com.ecalm.ez_health.calculate.CapitalizeFirstLetter;
import com.ecalm.ez_health.model.Food;

import java.util.ArrayList;

public class ScanSearchAdapter extends RecyclerView.Adapter<ScanSearchAdapter.ScanSearchViewHolder>{
    private ArrayList<Food> dataList;
    private Context context;
    private static ScanSearchAdapter.ClickListener clickListener;


    public ScanSearchAdapter(ArrayList<Food> dataList) {
        this.dataList = dataList;
    }
    public ScanSearchAdapter() {
    }

    @Override
    public ScanSearchAdapter.ScanSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.scanfoodlist_item, parent, false);
        context = parent.getContext();
        return new ScanSearchAdapter.ScanSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ScanSearchAdapter.ScanSearchViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtName.setText(CapitalizeFirstLetter.capitaliseName(dataList.get(position).getName()));
        holder.txtWeignt.setText(dataList.get(position).getWeight()+" g");
        holder.txtCalorie.setText(dataList.get(position).getCalorie()+" Kalori");
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class ScanSearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView txtName, txtWeignt, txtCalorie;
        private View mview;
        private LinearLayout scanSearchLayout;
        private Button add;
        private int position;

        public ScanSearchViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.name_scan);
            txtWeignt = itemView.findViewById(R.id.weight_scan);
            txtCalorie = itemView.findViewById(R.id.calorie_scan);
            scanSearchLayout = itemView.findViewById(R.id.scanfoodlayout);
            add = itemView.findViewById(R.id.tambahfoodscan);
            mview = itemView;
            //scanSearchLayout.setOnClickListener(this);
            add.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }

    public void setData(ArrayList<Food> food){
        this.dataList = food;
    }

    public void setOnItemClickListener(ScanSearchAdapter.ClickListener clickListener) {
        ScanSearchAdapter.clickListener = clickListener;
    }
    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}
