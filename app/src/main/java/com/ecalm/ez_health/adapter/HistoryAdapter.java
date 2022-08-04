package com.ecalm.ez_health.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ecalm.e_calm.R;
import com.ecalm.ez_health.calculate.CapitalizeFirstLetter;
import com.ecalm.ez_health.model.Food;
import com.ecalm.ez_health.model.FoodHistory;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{
    private ArrayList<FoodHistory> dataList;
    private Context context;
    private static HistoryAdapter.ClickListener clickListener;


    public HistoryAdapter(ArrayList<FoodHistory> dataList) {
        this.dataList = dataList;
    }
    public HistoryAdapter() {
    }

    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.historyfood_item, parent, false);
        context = parent.getContext();
        return new HistoryAdapter.HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HistoryAdapter.HistoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtName.setText(CapitalizeFirstLetter.capitaliseName(dataList.get(position).getName()));
        holder.txtWeignt.setText(dataList.get(position).getWeight()+" g");
        holder.txtCalorie.setText(dataList.get(position).getCalorie()+" Kalori");
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView txtName, txtWeignt, txtCalorie;
        private View mview;
        private LinearLayout hisotryLayout;
        private ImageView edit, delete;
        private int position;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.name_history);
            txtWeignt = itemView.findViewById(R.id.weight_history);
            txtCalorie = itemView.findViewById(R.id.calorie_history);
            hisotryLayout = itemView.findViewById(R.id.historyfoodlayout);
            edit = itemView.findViewById(R.id.editfoodhistory);
            delete= itemView.findViewById(R.id.deletefoodhistory);
            mview = itemView;

            edit.setOnClickListener(this);
            delete.setOnClickListener(this);
            //hisotryLayout.setOnClickListener(this);
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

    public void setData(ArrayList<FoodHistory> food){
        this.dataList = food;
    }

    public void setOnItemClickListener(HistoryAdapter.ClickListener clickListener) {
        HistoryAdapter.clickListener = clickListener;
    }
    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}