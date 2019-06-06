package com.example.caffinetracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caffinetracker.model.FoodItem;

import java.util.List;

import static com.example.caffinetracker.MainActivity.consumed;
import static com.example.caffinetracker.MainActivity.totalCaffeine;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    public List<FoodItem> foodItems;
 class MyViewHolder extends RecyclerView.ViewHolder{
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
    public void onBindViewHolder(final MyViewHolder holder, final int position){
        holder.nameText.setText(foodItems.get(position).getItemName());
        holder.itemDescription.setText(foodItems.get(position).details());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                String name = foodItems.get(position).getItemName();
                //FoodItem caffeine = new FoodItem();
                FoodItem caffeine =  foodItems.get(position);
                //for(int i = 0; i < foodItems.size(); i++) {
                //    if (foodItems.get(i).getItemName() == name) {
                //        caffeine = foodItems.get(i);
                //        break;
                //    }
                //}
                if (consumed.size() == 0) {
                    consumed.add(caffeine);
                    int serving = Integer.parseInt(caffeine.getItemValue());
                    totalCaffeine += serving;
                }
                else{
                    for (int i = 0; i < consumed.size(); i++)
                    {
                        if (i == consumed.size() - 1 && caffeine != consumed.get(i))
                        {
                            consumed.add(caffeine);
                            int serving = Integer.parseInt(caffeine.getItemValue());
                            totalCaffeine += serving;
                            break;
                        }
                        else if(consumed.get(i) == caffeine)
                        {
                            int serving =Integer.parseInt( caffeine.getItemValue());
                            serving += serving;
                            totalCaffeine += serving;
                            consumed.get(i).setItemValue(Integer.toString(serving));
                            break;
                        }
                    }
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return foodItems.size();
    }

}
