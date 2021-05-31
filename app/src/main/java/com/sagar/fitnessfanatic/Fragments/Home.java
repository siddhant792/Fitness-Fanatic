package com.sagar.fitnessfanatic.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sagar.fitnessfanatic.Activity.Activity_Plan;
import com.sagar.fitnessfanatic.Activity.Bmi_Calculator;
import com.sagar.fitnessfanatic.Activity.StartPlan_Activity;
import com.sagar.fitnessfanatic.R;

import java.util.Map;

import maes.tech.intentanim.CustomIntent;
import pl.droidsonroids.gif.GifDrawable;

import static android.content.Context.MODE_PRIVATE;

public class Home extends Fragment {

    ImageView bmi_calculator,plan_beginner,plan_inter,plan_advance;
    CardView ly_active_plan;
    SharedPreferences pref,cal_info;
    TextView tv_progress,ongoing_plan_name;
    int current_day;
    String plan_name;
    int day_new;
    LinearLayout ly_plan_proto;
    AlertDialog alertDialog,alertDialog2;
    String[] days = new String[30];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        bmi_calculator = view.findViewById(R.id.bmi_calculator);
        plan_beginner = view.findViewById(R.id.plan_beginner);
        ly_active_plan = view.findViewById(R.id.ly_active_plan);
        ongoing_plan_name = view.findViewById(R.id.ongoing_plan_name);
        ly_plan_proto = view.findViewById(R.id.ly_plan_proto);
        tv_progress = view.findViewById(R.id.tv_progress);
        plan_inter = view.findViewById(R.id.plan_inter);
        plan_advance = view.findViewById(R.id.plan_advance);
        pref = getContext().getSharedPreferences("plan_status", MODE_PRIVATE);
        cal_info = getContext().getSharedPreferences("cal_info", MODE_PRIVATE);

        view.findViewById(R.id.check_more_apps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(),R.style.BottomDialoge);
                View bottombar = LayoutInflater.from(getContext()).inflate(R.layout.ly_more_apps,(LinearLayout)getActivity().findViewById(R.id.container_paused));
                RelativeLayout click_musica = bottombar.findViewById(R.id.click_musica);
                RelativeLayout click_careyourself = bottombar.findViewById(R.id.click_careyourself);
                RelativeLayout click_wallpaper = bottombar.findViewById(R.id.click_wallpaper);

                click_musica.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.mark.tiktok20&hl=en&gl=US"));
                        startActivity(i);
                    }
                });

                click_careyourself.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.bharat.care&hl=en&gl=US"));
                        startActivity(i);
                    }
                });

                click_wallpaper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.mark.wallpaperarena&hl=en"));
                        startActivity(i);
                    }
                });

                bottomSheetDialog.setContentView(bottombar);
                bottomSheetDialog.show();

            }
        });

        for (int i = 0 ; i < 30 ; i++){
            int temp = i+1;
            days[i] = "         " + temp + "         ";
        }

        boolean plan_stats = pref.getBoolean("status",false);

        if (!plan_stats){
            ly_plan_proto.setVisibility(View.GONE);
        }else{
            ly_plan_proto.setVisibility(View.VISIBLE);
            current_day = pref.getInt("day",0);
            ongoing_plan_name.setText(pref.getString("plan",null));
            plan_name = pref.getString("plan",null);
            getOverAllProgress();
        }

        System.out.println("PLan_name" + plan_name + "dayyyy" + current_day + "Status : " + plan_stats);

        bmi_calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Bmi_Calculator.class);
                startActivity(i);
                CustomIntent.customType(getContext(),"fadein-to-fadeout");
            }
        });
        plan_beginner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Activity_Plan.class);
                i.putExtra("plan","Beginner");
                startActivity(i);
                CustomIntent.customType(getContext(),"fadein-to-fadeout");
            }
        });
        plan_inter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Activity_Plan.class);
                i.putExtra("plan","Intermediate");
                startActivity(i);
                CustomIntent.customType(getContext(),"fadein-to-fadeout");
            }
        });
        plan_advance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Activity_Plan.class);
                i.putExtra("plan","Advanced");
                startActivity(i);
                CustomIntent.customType(getContext(),"fadein-to-fadeout");
            }
        });

        ly_active_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                plan_continue();
            }
        });

        return view;
    }

    private void getOverAllProgress() {
        int prog = 0;
        Map<String, ?> allEntries = cal_info.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if(Integer.parseInt(entry.getValue().toString()) != 0) prog++;
        }
        int percent = (prog*100)/30;
        tv_progress.setText(percent+"%");
    }


    private void plan_continue(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogStyle);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view1 = layoutInflater.inflate(R.layout.dialog_next_day,null);
        view1.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        TextView tv_last_day = view1.findViewById(R.id.tv_last_day);
        TextView btn_reset = view1.findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("Your all progress will be lost ! Do you want to reset your plan ?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Reset",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                plan_rest();
                                dialog.cancel();
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
        });
        tv_last_day.setText(current_day + "");
        Spinner spinner = view1.findViewById(R.id.spinner_day);
        ArrayAdapter aa = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,days);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        spinner.setSelection(current_day,true);
        day_new = current_day + 1;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day_new = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        view1.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("day",day_new);
                editor.apply();
                if (day_new%4==0){
                    SharedPreferences.Editor editorE = cal_info.edit();
                    editorE.putInt(String.valueOf(day_new),10);
                    editorE.apply();
                    rest_Day(day_new);
                }else{
                    Intent i = new Intent(getContext(), StartPlan_Activity.class);
                    i.putExtra("plan",plan_name);
                    startActivity(i);
                    CustomIntent.customType(getContext(),"fadein-to-fadeout");
                }
            }
        });

        builder.setView(view1);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean plan_stats = pref.getBoolean("status",false);

        if (!plan_stats){
            ly_plan_proto.setVisibility(View.GONE);
        }else{
            ly_plan_proto.setVisibility(View.VISIBLE);
            current_day = pref.getInt("day",0);
            ongoing_plan_name.setText(pref.getString("plan",null));
            plan_name = pref.getString("plan",null);
            getOverAllProgress();
        }
    }

    private void rest_Day(int day){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogStyle);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view1 = layoutInflater.inflate(R.layout.item_restday_new,null);
        TextView tv_cd = view1.findViewById(R.id.tv_cd);
        tv_cd.setText("Day " + day + " is rest day");
        view1.findViewById(R.id.btn_explore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOverAllProgress();
                alertDialog2.cancel();
            }
        });

        view1.findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getActivity().finishAffinity();
            }
        });

        builder.setView(view1);
        builder.setCancelable(true);
        alertDialog2 = builder.create();
        alertDialog2.setCanceledOnTouchOutside(true);
        alertDialog2.show();
    }

    private void plan_rest(){
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("day",0);
        editor.apply();

        SharedPreferences.Editor editor2 = cal_info.edit();
        for(int i = 1; i<31; i++){
            editor2.putInt(i+"",0);
        }
        editor2.apply();
    }
}