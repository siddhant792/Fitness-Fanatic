package com.sagar.fitnessfanatic.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sagar.fitnessfanatic.Adapters.Adapter_cal;
import com.sagar.fitnessfanatic.Models.Model_cal;
import com.sagar.fitnessfanatic.R;

import java.util.ArrayList;
import java.util.Map;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

import static android.content.Context.MODE_PRIVATE;

public class Report extends Fragment{

    RecyclerView rv_calender;
    Adapter_cal adapter_cal;
    ArrayList<Model_cal> calArrayList = new ArrayList<>();
    CircularProgressIndicator progress_bar;
    Adapter_cal.RecyclerViewCLick recyclerViewCLick;
    View prev_click = null;
    SharedPreferences pref,cal_info;
    Context context;
    TextView tv_cal_lost,tv_target_ac,min_progress;
    String current_plan,plan_name;
    int day;
    boolean status;
    ArrayList<Integer> myCalories = new ArrayList<>();
    ArrayList<Integer> myCalories_key = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_report, container, false);
        rv_calender = view.findViewById(R.id.rv_calender);
        progress_bar = view.findViewById(R.id.progress_bar);
        tv_cal_lost = view.findViewById(R.id.tv_cal_lost);
        tv_target_ac = view.findViewById(R.id.tv_target_ac);
        min_progress = view.findViewById(R.id.min_progress);
        context = getActivity();
        progress_bar.setMaxProgress(100);
        addData();
        pref = context.getSharedPreferences("plan_status", MODE_PRIVATE);
        cal_info = context.getSharedPreferences("cal_info", MODE_PRIVATE);


        if(!cal_info.contains("1")){
            SharedPreferences.Editor editor = cal_info.edit();
            for(int i = 1; i<31; i++){
                editor.putInt(i+"",0);
            }
            editor.apply();
        }

        boolean plan_stats = pref.getBoolean("status",false);

        if (plan_stats){
                plan_name = pref.getString("plan",null);
                day = pref.getInt("day",0);
                status = pref.getBoolean("status",false);
            }else {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("status",false);
                editor.putInt("day",0);
                editor.putString("plan","");
                editor.apply();

                plan_name = pref.getString("plan",null);
                day = pref.getInt("day",0);
                status = pref.getBoolean("status",false);
            }

        System.out.println("PLan : " + plan_name + "  Day : " + day + "   Status : " + status);
        return view;
    }

    private void getCaloriesInfo(){
        Map<String, ?> allEntries = cal_info.getAll();
        myCalories_key.clear();
        myCalories.clear();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            myCalories.add(Integer.parseInt(entry.getValue().toString()));
            myCalories_key.add(Integer.parseInt(entry.getKey()));
        }

        System.out.println("mYdata_Values" + myCalories);
        System.out.println("mYdata_Key" + myCalories);

    }

    private void addData() {
        for (int i=1;i<31;i++){
            calArrayList.add(new Model_cal(String.valueOf(i)));
        }
        setAdapter();
    }

    private void setAdapter() {
        onClickAction();
        adapter_cal = new Adapter_cal(getContext(),calArrayList,recyclerViewCLick);
        rv_calender.setLayoutManager(new GridLayoutManager(getContext(),7,GridLayoutManager.VERTICAL,false));
        rv_calender.setAdapter(adapter_cal);
    }

    private void onClickAction() {
        recyclerViewCLick = new Adapter_cal.RecyclerViewCLick() {
            @Override
            public void Clicked(View v, int position, TextView tv_date) {
                boolean plan_stats = pref.getBoolean("status",false);
                if (!plan_stats){
                    plan_change("Please start a workout plan first to track your progress");
                    return;
                }
                if (prev_click!=null){
                    TextView tv_temp = prev_click.findViewById(R.id.tv_day);
                    tv_temp.setTextColor(getResources().getColor(R.color.black));
                    tv_temp.setBackgroundResource(R.drawable.draw_calender_white);
                }
                int found_idx = myCalories_key.indexOf(position+1);
                tv_date.setTextColor(getResources().getColor(R.color.white));
                tv_date.setBackgroundResource(R.drawable.draw_calender_dark);
                progress_bar.setCurrentProgress(myCalories.get(found_idx)*10);
                min_progress.setText(myCalories.get(found_idx)*10 + "%");
                if((position+1)%4==0){
                    tv_cal_lost.setText("Rest Day");
                    tv_target_ac.setText("Rest Day");
                }else{
                    tv_target_ac.setText(myCalories.get(found_idx)*10 + "%");
                    switch (myCalories.get(found_idx)){
                        case 1 : tv_cal_lost.setText("32 cal");
                            break;
                        case 2 : tv_cal_lost.setText("43 cal");
                            break;
                        case 3 : tv_cal_lost.setText("66 cal");
                            break;
                        case 4 : tv_cal_lost.setText("74 cal");
                            break;
                        case 5 : tv_cal_lost.setText("91 cal");
                            break;
                        case 6 : tv_cal_lost.setText("102 cal");
                            break;
                        case 7 : tv_cal_lost.setText("134 cal");
                            break;
                        case 8 : tv_cal_lost.setText("152 cal");
                            break;
                        case 9 : tv_cal_lost.setText("161 cal");
                            break;
                        case 10 : tv_cal_lost.setText("188 cal");
                            break;
                        default : tv_cal_lost.setText("Null");
                            tv_target_ac.setText("Null");
                            plan_change("You have not reached this day yet");
                            progress_bar.setCurrentProgress(0);
                    }
                }
                prev_click = v;
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        getCaloriesInfo();
    }


    private void plan_change(String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage(msg);
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