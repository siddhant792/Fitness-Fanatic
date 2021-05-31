package com.sagar.fitnessfanatic.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.sagar.fitnessfanatic.Adapters.Adapter_day_pager;
import com.sagar.fitnessfanatic.Adapters.Adapter_pagenumber;
import com.sagar.fitnessfanatic.Constants.UnivDialouge;
import com.sagar.fitnessfanatic.Models.Model_cal;
import com.sagar.fitnessfanatic.Models.Model_exercise;
import com.sagar.fitnessfanatic.Models.Model_pager;
import com.sagar.fitnessfanatic.R;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class Activity_Plan extends AppCompatActivity {

    RecyclerView rv_day;
    ArrayList<Model_cal> day_list = new ArrayList<>();
    ImageView back_img;
    String current_plan;
    Adapter_pagenumber.RecyclerViewCLick listener;

    ArrayList<Model_pager> pager_list = new ArrayList<>();

    ArrayList<Model_exercise> exercise_list = new ArrayList<>();

    ViewPager2 vp_days;
    View prev_view = null;
    UnivDialouge progress;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__plan);
        rv_day = findViewById(R.id.rv_day);
        back_img = findViewById(R.id.back_img);
        vp_days = findViewById(R.id.vp_days);
        progress = new UnivDialouge(Activity_Plan.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        AppBarLayout appBarLayout = findViewById(R.id.bar_header);

        progress.show();
        pref = getApplicationContext().getSharedPreferences("plan_status", MODE_PRIVATE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        current_plan = getIntent().getStringExtra("plan");
        onClick();
        if (current_plan.equalsIgnoreCase("Beginner")){
            back_img.setImageResource(R.drawable.beg_back);
        }else if (current_plan.equalsIgnoreCase("Intermediate")){
            back_img.setImageResource(R.drawable.inter_back);
        }else if (current_plan.equalsIgnoreCase("Advanced")){
            back_img.setImageResource(R.drawable.advance_back);
        }

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plan_name = pref.getString("plan",null);

                boolean plan_stats = pref.getBoolean("status",false);

                if(plan_stats){
                    if(plan_name.equalsIgnoreCase(current_plan)){
                        int dayy = pref.getInt("day",0);
                        if (dayy==30){
                            lastDay();
                        }else{
                            start_plan("Do you want to continue your existing plan ?",true);
                        }
                    }else{
                        plan_change();
                    }
                }else{
                    start_plan("Do you want to start this plan ?",false);
                }
            }
        });

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapseActionView);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {

            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (scrollRange == -1){
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + i == 0){
                    if (current_plan.equalsIgnoreCase("Beginner")){
                        collapsingToolbarLayout.setTitle("Beginner");
                    }else if (current_plan.equalsIgnoreCase("Intermediate")){
                        collapsingToolbarLayout.setTitle("Intermediate");
                    }else if (current_plan.equalsIgnoreCase("Advanced")){
                        collapsingToolbarLayout.setTitle("Advanced");
                    }
                    isShow = false;
                }else {
                    if (current_plan.equalsIgnoreCase("Beginner")){
                        collapsingToolbarLayout.setTitle("Beginner");
                    }else if (current_plan.equalsIgnoreCase("Intermediate")){
                        collapsingToolbarLayout.setTitle("Intermediate");
                    }else if (current_plan.equalsIgnoreCase("Advanced")){
                        collapsingToolbarLayout.setTitle("Advanced");
                    }
                    isShow = true;
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onCall();
            }
        }, 200);

    }

    private void plan_change(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Another plan is going on. If you continue the progress will be lost");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Proceed",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent i = new Intent(Activity_Plan.this, StartPlan_Activity.class);
                        i.putExtra("plan",current_plan);
                        startActivity(i);
                        CustomIntent.customType(Activity_Plan.this,"fadein-to-fadeout");
                    }
                });

        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void onCall(){
        for (int i=1;i<31;i++){
            day_list.add(new Model_cal("Day "+i));
            pager_list.add(new Model_pager(""));
        }
        Adapter_pagenumber adapter_exercise = new Adapter_pagenumber(this,day_list,listener);
        rv_day.setLayoutManager(new GridLayoutManager(this,1,GridLayoutManager.HORIZONTAL,false));
        rv_day.setAdapter(adapter_exercise);
        rv_day.setItemViewCacheSize(30);

        vp_days.setAdapter(new Adapter_day_pager(Activity_Plan.this,pager_list,current_plan));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                vp_days.setOffscreenPageLimit(30);
            }
        }, 300);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress.cancel();
            }
        }, 4000);

        vp_days.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position){
                super.onPageSelected(position);
                rv_day.scrollToPosition(position);
                if (prev_view!=null){
                    TextView textView = prev_view.findViewById(R.id.tv_current_day);
                    RelativeLayout linearLayout = prev_view.findViewById(R.id.day_background);
                    textView.setTextColor(getResources().getColor(R.color.black));
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.white));
                }
                View temp_view = rv_day.findViewHolderForAdapterPosition(position).itemView;
                TextView textView = temp_view.findViewById(R.id.tv_current_day);
                RelativeLayout linearLayout = temp_view.findViewById(R.id.day_background);
                textView.setTextColor(getResources().getColor(R.color.white));
                linearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                vp_days.setCurrentItem(position,true);
                prev_view = temp_view;
            }
        });
    }

    private void onClick() {
        listener = new Adapter_pagenumber.RecyclerViewCLick() {
            @Override
            public void Clicked(View v, int position, TextView tv_date, RelativeLayout background) {
                if (prev_view!=null){
                    TextView textView = prev_view.findViewById(R.id.tv_current_day);
                    RelativeLayout linearLayout = prev_view.findViewById(R.id.day_background);
                    textView.setTextColor(getResources().getColor(R.color.black));
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.white));
                }
                tv_date.setTextColor(getResources().getColor(R.color.white));
                background.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                vp_days.setCurrentItem(position,true);
                prev_view = v;

            }
        };
    }

    private void start_plan(String msg,boolean continue_plan){
        if (continue_plan){
            SharedPreferences.Editor editor = pref.edit();
            int dayy = pref.getInt("day",0);
            editor.putInt("day",dayy+1);
            editor.apply();
        }
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(msg);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent i = new Intent(Activity_Plan.this, StartPlan_Activity.class);
                        i.putExtra("plan",current_plan);
                        startActivity(i);
                        CustomIntent.customType(Activity_Plan.this,"fadein-to-fadeout");
                    }
                });

        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void lastDay(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("You have reached last day! Please start other plans");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
