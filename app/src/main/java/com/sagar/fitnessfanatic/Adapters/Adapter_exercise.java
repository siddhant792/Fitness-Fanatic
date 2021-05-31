package com.sagar.fitnessfanatic.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagar.fitnessfanatic.Activity.Explore_Exercise;
import com.sagar.fitnessfanatic.Models.Model_exercise;
import com.sagar.fitnessfanatic.R;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class Adapter_exercise extends RecyclerView.Adapter<Adapter_exercise.ViewHolder> {

    Context context;
    ArrayList<Model_exercise> model;
    String current_plan;

    public Adapter_exercise(Context context, ArrayList<Model_exercise> model,String current_plan) {
        this.context = context;
        this.model = model;
        this.current_plan = current_plan;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.exercise_tv.setText(model.get(position).getText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> exe_name = new ArrayList<>();
                for (int i=0;i<model.size();i++){
                    exe_name.add(model.get(i).getText());
                }

                Intent intent = new Intent(context,Explore_Exercise.class);
                intent.putExtra("array",exe_name);
                intent.putExtra("position",String.valueOf(position));
                intent.putExtra("plan",current_plan);
                context.startActivity(intent);
            }
        });
        holder.exercise_gif.setImageResource(model.get(position).getImage());

    }

    public interface RecyclerViewCLick{
        void Clicked(View v, int position, TextView tv_date);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView exercise_tv;
        //GifImageView exercise_gif;
        ImageView open_exercise;
        GifImageView exercise_gif;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exercise_tv = itemView.findViewById(R.id.exercise_tv);
            exercise_gif = itemView.findViewById(R.id.exercise_gif);
            open_exercise = itemView.findViewById(R.id.open_exercise);
        }
    }

}
