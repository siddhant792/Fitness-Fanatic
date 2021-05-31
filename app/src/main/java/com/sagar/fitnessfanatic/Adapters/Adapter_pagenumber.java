package com.sagar.fitnessfanatic.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagar.fitnessfanatic.Models.Model_cal;
import com.sagar.fitnessfanatic.R;

import java.util.ArrayList;

public class Adapter_pagenumber extends RecyclerView.Adapter<Adapter_pagenumber.ViewHolder> {

    Context context;
    ArrayList<Model_cal> model;
    RecyclerViewCLick recyclerViewCLick;

    public Adapter_pagenumber(Context context, ArrayList<Model_cal> model, RecyclerViewCLick recyclerViewCLick) {
        this.context = context;
        this.model = model;
        this.recyclerViewCLick = recyclerViewCLick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page_number,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tv_day.setText(model.get(position).getDay());

        holder.tv_day.setTextColor(context.getResources().getColor(R.color.black));
        holder.ly.setBackgroundColor(context.getResources().getColor(R.color.white));

    }

    public interface RecyclerViewCLick{
        void Clicked(View v, int position, TextView tv_date, RelativeLayout ly);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv_day;
        RelativeLayout ly;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_day = itemView.findViewById(R.id.tv_current_day);
            ly = itemView.findViewById(R.id.day_background);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerViewCLick.Clicked(view,getAdapterPosition(),tv_day,ly);
        }
    }

}
