package com.example.caffinetracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.caffinetracker.model.FoodItem;

import java.util.List;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    public List<FoodItem> foodItems;
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView nameText, itemDescription;
        public Button button;
        public MyViewHolder(View v)
        {
            super(v);
            nameText = itemView.findViewById(R.id.textFoodTItle);
            itemDescription = itemView.findViewById(R.id.textFoodDesc);
            button = itemView.findViewById(R.id.ServingButton);

        }
    }
    public MyAdapter(List<FoodItem> p0){foodItems = p0;}
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.caffeine_dropdown_layout,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(contactView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        holder.nameText.setText(foodItems.get(position).getItemName());
        holder.itemDescription.setText(foodItems.get(position).details());
    }
    @Override
    public int getItemCount() {
        return foodItems.size();
    }

}
