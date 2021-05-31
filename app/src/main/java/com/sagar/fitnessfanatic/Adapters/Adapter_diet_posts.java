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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sagar.fitnessfanatic.Activity.Read_posts;
import com.sagar.fitnessfanatic.Models.Model_diet_posts;
import com.sagar.fitnessfanatic.R;

import java.util.ArrayList;

public class Adapter_diet_posts extends RecyclerView.Adapter<Adapter_diet_posts.ViewHolder> {

    Context context;
    ArrayList<Model_diet_posts> model;

    public Adapter_diet_posts(Context context, ArrayList<Model_diet_posts> model) {
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diet_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        RequestOptions requestOptions=new RequestOptions();

        Glide
                .with(context).
                load(model.get(position).getPhoto())
                .into(holder.img_post);

        holder.tv_post.setText(model.get(position).getText());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Read_posts.class);
                i.putExtra("post_id",model.get(position).getId());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img_post;
        TextView tv_post;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_post = itemView.findViewById(R.id.img_post);
            tv_post = itemView.findViewById(R.id.tv_post);
        }

    }

}
