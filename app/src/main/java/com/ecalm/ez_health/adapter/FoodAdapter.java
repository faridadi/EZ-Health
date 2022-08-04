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

import java.util.ArrayList;

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
    public void onBindViewHolder(final FoodViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.txtName.setText(CapitalizeFirstLetter.capitaliseName(dataList.get(position).getName()));
        holder.txtWeignt.setText(dataList.get(position).getWeight()+" g");
        holder.txtCalorie.setText(dataList.get(position).getCalorie()+" kal");
        //holder.txtTotal.setText(dataList.get(position).getCalorie()* dataList.get(position).getWeight()+" kcal");
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView txtName, txtWeignt, txtCalorie, txtTotal;
        private LinearLayout foodCardLayout;
        private View mview;
        private ImageView delete;
        private int position;

        public FoodViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.tv_fname);
            txtWeignt = itemView.findViewById(R.id.tv_fsize);
            txtCalorie = itemView.findViewById(R.id.tv_frate);
            txtTotal = itemView.findViewById(R.id.tv_ftotal);
            foodCardLayout = itemView.findViewById(R.id.foodcardlayout);
            delete = itemView.findViewById(R.id.deletescanfood);
            mview = itemView;
            foodCardLayout.setOnClickListener(this);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view);
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