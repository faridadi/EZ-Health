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

public class SearchFoodAdapter extends RecyclerView.Adapter<SearchFoodAdapter.SearchFoodViewHolder>{
    private ArrayList<Food> dataList;
    private Context context;
    private static SearchFoodAdapter.ClickListener clickListener;


    public SearchFoodAdapter(ArrayList<Food> dataList) {
        this.dataList = dataList;
    }
    public SearchFoodAdapter() {
    }

    @Override
    public SearchFoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.infofoodlist_item, parent, false);
        context = parent.getContext();
        return new SearchFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchFoodAdapter.SearchFoodViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtName.setText(CapitalizeFirstLetter.capitaliseName(dataList.get(position).getName()));
        holder.txtWeignt.setText(dataList.get(position).getWeight()+" g");
        holder.txtCalorie.setText(dataList.get(position).getCalorie()+" Kalori");
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class SearchFoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView txtName, txtWeignt, txtCalorie;
        private View mview;
        private LinearLayout searchFoodLayout;
        private ImageView edit, delete;
        private int position;

        public SearchFoodViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.name_search);
            txtWeignt = itemView.findViewById(R.id.weight_search);
            txtCalorie = itemView.findViewById(R.id.calorie_search);
            searchFoodLayout = itemView.findViewById(R.id.searchfoodlayout);
            edit = itemView.findViewById(R.id.editfood);
            delete = itemView.findViewById(R.id.deletefood);
            mview = itemView;
            searchFoodLayout.setOnClickListener(this);
            edit.setOnClickListener(this);
            delete.setOnClickListener(this);
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

    public void setOnItemClickListener(SearchFoodAdapter.ClickListener clickListener) {
        SearchFoodAdapter.clickListener = clickListener;
    }
    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}
