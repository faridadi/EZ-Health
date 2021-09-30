package com.ecalm.e_calm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ecalm.e_calm.CaptureActivity;
import com.ecalm.e_calm.R;
import com.ecalm.e_calm.model.Food;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {


    private ArrayList<Food> dataList;
    private Context context;
    private static ClickListener clickListener;


    public FoodAdapter(ArrayList<Food> dataList) {
        this.dataList = dataList;
    }
    public FoodAdapter() {
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.foodlist_item, parent, false);
        context = parent.getContext();
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FoodViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtName.setText(dataList.get(position).getName());
        holder.txtWeignt.setText(dataList.get(position).getWeight()+"");
        holder.txtRate.setText(dataList.get(position).getCalorie()+"");
        holder.txtTotal.setText(dataList.get(position).getCalorie()* dataList.get(position).getWeight()+" kcal");
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView txtName, txtWeignt, txtRate, txtTotal;
        private View mview;
        private int position;

        public FoodViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.tv_fname);
            txtWeignt = itemView.findViewById(R.id.tv_fsize);
            txtRate = itemView.findViewById(R.id.tv_frate);
            txtTotal = itemView.findViewById(R.id.tv_ftotal);
            mview = itemView;
            txtName.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            txtTotal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition(), v);
                }
            });
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

    public void setOnItemClickListener(ClickListener clickListener) {
        FoodAdapter.clickListener = clickListener;
    }
    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}