package com.sagar.fitnessfanatic.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mapzen.speakerbox.Speakerbox;
import com.sagar.fitnessfanatic.Constants.Exercise_Reps;
import com.sagar.fitnessfanatic.R;

import java.util.ArrayList;
import java.util.Arrays;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class StartPlan_Activity extends AppCompatActivity {

    LinearLayout ly_ongoing_exercise,ly_rest_period,skip_exe_rest,ly_restday;
    GifImageView gif_exe,gif_exe_up_next;
    TextView tv_exe_name,tv_next_exe_order,tv_exe_next_name,tv_next_duration,tv_ongoing_plan,tv_get_ready,tv_reps_info_current;
    CircularProgressIndicator progress_bar_exe,progress_bar_rest;
    ImageView img_speech,skip_left,skip_right,btn_exe_done_proceed,open_description,img_pause;
    RoundCornerProgressBar day_progress;
    RelativeLayout ly_plan_running,ly_pause,ly_next_v;

    private int duration = 30;

    //string staussss

    String status_timer;
    String current_plan;

    //plan details

    String plan_name="null";
    Boolean status;
    int day;

    ArrayList<String> exercise_name = new ArrayList<>();

    int exe_prog=0;

    int plan_reps = 0;

    CardView cv_tray;

    //timers

    CountDownTimer ready_timer,exercise_timer;

    //for pause action

    long current_ready,current_exercise;

    AdView mAdView;

    int ad_status=0;
    private InterstitialAd mInterstitialAd;

    SharedPreferences pref,cal_info;

    long exercise_duration;

    private Speakerbox speech_voice;

    private String text_countdown_3 = "3";
    private String text_countdown_2 = "2";
    private String text_countdown_1 = "1";
    private String text_countdown_go = "start";
    private String text_countdown_stop = "stop";
    private String text_next = "next exercise is ";
    private String text_press_tick = "press tick button when completed";

    private boolean status_timer_exe = true;
    private boolean status_speech = true;

    Vibrator vibrate;

    boolean loaderrr = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_plan_);

        System.out.println("MyAraryList" + exercise_name.size());
        ly_ongoing_exercise = findViewById(R.id.ly_ongoing_exercise);
        ly_plan_running = findViewById(R.id.ly_plan_running);
        cv_tray = findViewById(R.id.cv_tray);
        ly_rest_period = findViewById(R.id.ly_rest_period);
        tv_get_ready = findViewById(R.id.tv_get_ready);
        img_speech = findViewById(R.id.img_speech);
        gif_exe = findViewById(R.id.gif_exe);
        tv_exe_name = findViewById(R.id.tv_exe_name);
        progress_bar_exe = findViewById(R.id.progress_bar_exe);
        skip_left = findViewById(R.id.skip_left);
        skip_right = findViewById(R.id.skip_right);
        btn_exe_done_proceed = findViewById(R.id.btn_exe_done_proceed);
        tv_reps_info_current = findViewById(R.id.tv_reps_info_current);
        img_pause = findViewById(R.id.img_pause);
        tv_next_exe_order = findViewById(R.id.tv_next_exe_order);
        tv_exe_next_name = findViewById(R.id.tv_exe_next_name);
        tv_next_duration = findViewById(R.id.tv_next_duration);
        open_description = findViewById(R.id.open_description);
        gif_exe_up_next = findViewById(R.id.gif_exe_up_next);
        tv_ongoing_plan = findViewById(R.id.tv_ongoing_plan);
        progress_bar_rest = findViewById(R.id.progress_bar_rest);
        skip_exe_rest = findViewById(R.id.skip_exe_rest);
        day_progress = findViewById(R.id.day_progress);
        ly_restday = findViewById(R.id.ly_restday);
        ly_next_v = findViewById(R.id.ly_next_v);
        ly_pause = findViewById(R.id.ly_pause);

        speech_voice = new Speakerbox(getApplication());
        vibrate =  (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

//        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("D73F5DC57977D8638C209DD0DD0898BC")).build();
//        MobileAds.setRequestConfiguration(configuration);

        img_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate.vibrate(50);

                if (status_speech){
                    img_speech.setImageResource(R.drawable.voice_disabled);
                    status_speech = false;
                }else {
                    img_speech.setImageResource(R.drawable.voice_enabled);
                    status_speech = true;
                }
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-2955522562259928/5571894253",adRequest,new InterstitialAdLoadCallback(){
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                Log.v("TAG_Add_Inter", "onAdLoaded- INter");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.v("TAG_Add_Inter", loadAdError.getMessage() + " INter");
                mInterstitialAd = null;
            }
        });

        mAdView = findViewById(R.id.adView);
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                Log.v("TAG_Add_Ban", "Failed_Bnner");
            }

            @Override
            public void onAdLoaded() {
                mAdView.setBackgroundColor(getResources().getColor(R.color.white));
                Log.v("TAG_Add_Ban", "Loaded Banner");
            }
        });
        current_plan = getIntent().getStringExtra("plan");

        pref = getApplicationContext().getSharedPreferences("plan_status", MODE_PRIVATE);
        cal_info = getApplicationContext().getSharedPreferences("cal_info", MODE_PRIVATE);
        
        String saved_plan = pref.getString("plan",null);

        if (saved_plan.equals(current_plan)){
            plan_name = pref.getString("plan",null);
            day = pref.getInt("day",0);
            status = pref.getBoolean("status",false);
        }else {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("status",true);
            editor.putInt("day",1);
            editor.putString("plan",current_plan);
            editor.apply();

            SharedPreferences.Editor editor2 = cal_info.edit();
            for(int i = 1; i<31; i++){
                editor2.putInt(i+"",0);
            }
            editor2.apply();

            plan_name = pref.getString("plan",null);
            day = pref.getInt("day",0);
            status = pref.getBoolean("status",false);
        }

        tv_ongoing_plan.setText(plan_name + "   |   " + "Day : " + day);

        System.out.println("Plan_name" + plan_name + " Day : " + day + "Status : " + status);

        if (status){
            setData(plan_name,day-1);
            System.out.println("Setingdata0" + exercise_name.size());
        }

        if (plan_name.equalsIgnoreCase("Beginner")){

            duration = 30;
            plan_reps = 0;
            exercise_duration = 30000;

        }else if (current_plan.equalsIgnoreCase("Intermediate")){

            duration = 45;
            plan_reps = 2;
            exercise_duration = 45000;

        }else if (current_plan.equalsIgnoreCase("Advanced")){

            duration = 60;
            plan_reps = 4;
            exercise_duration = 60000;

        }

        get_ready(10000,10000);

        day_progress.setMax(exercise_name.size());


        skip_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exe_prog>=1){
                    if (ready_timer!=null){
                        ready_timer.cancel();
                        ready_timer = null;
                    }
                    if (exercise_timer!=null){
                        exercise_timer.cancel();
                        exercise_timer = null;
                    }
                    exe_prog--;
                    get_ready(10000,10000);
                }
            }
        });

        skip_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exe_prog+1!=exercise_name.size()){
                    if (ready_timer!=null){
                        ready_timer.cancel();
                        ready_timer = null;
                    }
                    if (exercise_timer!=null){
                        exercise_timer.cancel();
                        exercise_timer = null;
                    }
                    exe_prog++;
                    get_ready(10000,10000);
                }else{
                    onPlanCompleteDialouge();
                }
            }
        });

        open_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (status_timer_exe){
                    if (ready_timer!= null){
                        Log.e("Empty ready","true");
                        ready_timer.cancel();
                        status_timer = "ready";
                    }
                    if (exercise_timer!=null){
                        Log.e("Empty exercise","true");
                        exercise_timer.cancel();
                        status_timer = "exercise";
                    }
                }


                ((GifDrawable)gif_exe.getDrawable()).stop();

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(StartPlan_Activity.this,R.style.BottomDialoge);
                View bottombar = LayoutInflater.from(getApplicationContext()).inflate(R.layout.desp_dialouge,(LinearLayout)findViewById(R.id.bottom_sheet_container));
                GifImageView gif_desc = bottombar.findViewById(R.id.gif_desc);
                TextView tv_exe_name = bottombar.findViewById(R.id.tv_exe_name);
                TextView tv_decs = bottombar.findViewById(R.id.tv_decs);
                gif_desc.setImageResource(getGifcommon(exercise_name.get(exe_prog)));
                tv_exe_name.setText(exercise_name.get(exe_prog));
                tv_decs.setText(getDesccommon(exercise_name.get(exe_prog)));
                bottomSheetDialog.setContentView(bottombar);
                bottomSheetDialog.show();
                bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (status_timer_exe){
                            if (status_timer.equalsIgnoreCase("ready")){
                                get_ready(10000,current_ready);
                            }else if (status_timer.equalsIgnoreCase("exercise")){
                                exercise_timer_fun(exercise_duration,current_exercise);
                            }
                        }

                        ((GifDrawable)gif_exe.getDrawable()).start();
                    }
                });
            }
        });

        btn_exe_done_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (exe_prog+1!=exercise_name.size()){
                    exe_prog++;
                    get_ready(10000,10000);
                }else {
                    onPlanCompleteDialouge();
                }
                exercise_timer = null;

            }
        });


        img_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (status_timer_exe){
                    if (ready_timer!= null){
                        Log.e("Empty ready","true");
                        ready_timer.cancel();
                        status_timer = "ready";
                    }
                    if (exercise_timer!=null){
                        Log.e("Empty exercise","true");
                        exercise_timer.cancel();
                        status_timer = "exercise";
                    }
                }


                ((GifDrawable)gif_exe.getDrawable()).stop();

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(StartPlan_Activity.this,R.style.BottomDialoge);
                View bottombar = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dia_paused,(LinearLayout)findViewById(R.id.container_paused));
                LinearLayout click_resume = bottombar.findViewById(R.id.click_resume);
                RelativeLayout click_musica = bottombar.findViewById(R.id.click_musica);
                RelativeLayout click_careyourself = bottombar.findViewById(R.id.click_careyourself);
                RelativeLayout click_wallpaper = bottombar.findViewById(R.id.click_wallpaper);

                click_resume.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.cancel();
                    }
                });

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

                bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (status_timer_exe){
                            if (status_timer.equalsIgnoreCase("ready")){
                                get_ready(10000,current_ready);
                            }else if (status_timer.equalsIgnoreCase("exercise")){
                                exercise_timer_fun(exercise_duration,current_exercise);
                            }
                        }


                        ((GifDrawable)gif_exe.getDrawable()).start();
                    }
                });
            }
        });

    }

    private void get_ready(long max_time,long current_time){

        System.out.println("Exercise Ready");

        SharedPreferences.Editor editor = cal_info.edit();
        int round = (int) Math.round(((exe_prog+1)*10)/exercise_name.size());
        editor.putInt(String.valueOf(day),round);
        editor.apply();

        progress_bar_exe.setMaxProgress(max_time/1000);
        progress_bar_exe.setCurrentProgress(current_time/1000);
        ly_pause.setVisibility(View.GONE);
        cv_tray.setVisibility(View.GONE);
        gif_exe.setVisibility(View.GONE);
        ly_next_v.setVisibility(View.VISIBLE);

        //speech

        if (status_speech){
            speech_voice.play(text_next+exercise_name.get(exe_prog));
            Log.e("Status", String.valueOf(status_speech));
        }
        day_progress.setProgress(exe_prog+1);

        btn_exe_done_proceed.setVisibility(View.GONE);
        progress_bar_exe.setVisibility(View.VISIBLE);
        tv_reps_info_current.setVisibility(View.GONE);

        mAdView.setVisibility(View.VISIBLE);
        tv_exe_name.setVisibility(View.INVISIBLE);
        getGifnext(exercise_name.get(exe_prog));

        progress_bar_exe.setGradient(CircularProgressIndicator.LINEAR_GRADIENT,getResources().getColor(R.color.green));
        getGifcurrent(exercise_name.get(exe_prog));
        ready_timer = new CountDownTimer(current_time, 1000) {
            public void onTick(long millisUntilFinished) {
                current_ready = millisUntilFinished;
                tv_get_ready.setVisibility(View.VISIBLE);
                progress_bar_exe.setCurrentProgress(millisUntilFinished/1000);
                if (status_speech){
                    if (millisUntilFinished<4000 && millisUntilFinished>3000){
                        speech_voice.play(text_countdown_3);
                    }else if (millisUntilFinished<3000 && millisUntilFinished>2000){
                        speech_voice.play(text_countdown_2);
                    }else if (millisUntilFinished<2000 && millisUntilFinished>1000){
                        speech_voice.play(text_countdown_1);
                    }else if (millisUntilFinished<1000){
                        speech_voice.play(text_countdown_go);
                    }
                }

            }

            public void onFinish() {
                tv_get_ready.setVisibility(View.GONE);
                exercise_timer_fun(exercise_duration,exercise_duration);
                ready_timer = null;
            }
        };
        ready_timer.start();
    }
    private void exercise_timer_fun(long max_time,long current_time){

        System.out.println("Exercise started");

        //def set to 30 and 30000
        progress_bar_exe.setMaxProgress(max_time/1000);
        progress_bar_exe.setCurrentProgress(current_time/1000);
        ly_pause.setVisibility(View.VISIBLE);
        tv_exe_name.setVisibility(View.VISIBLE);
        gif_exe.setVisibility(View.VISIBLE);
        cv_tray.setVisibility(View.VISIBLE);
        ly_next_v.setVisibility(View.GONE);
        mAdView.setVisibility(View.GONE);
        tv_reps_info_current.setVisibility(View.GONE);

        //checking non countable exercises

        switch (exercise_name.get(exe_prog)){

            case "Squats" :

                reps_exe_data();
                tv_reps_info_current.setText((Exercise_Reps.Squats+plan_reps)+" Reps");

                break;
            case "Push Up" :

                reps_exe_data();
                tv_reps_info_current.setText((Exercise_Reps.PushUps+plan_reps)+" Reps");

                break;
            case "Reverse Crunches" :

                reps_exe_data();
                tv_reps_info_current.setText((Exercise_Reps.ReverseCrunches+plan_reps)+" Reps");

                break;
            case "Triceps Dips" :

                reps_exe_data();
                tv_reps_info_current.setText((Exercise_Reps.TricepDips+plan_reps)+" Reps");

                break;
            case "Long Arm Crunches" :

                reps_exe_data();
                tv_reps_info_current.setText((Exercise_Reps.LongArmCrunches+plan_reps)+" Reps");

                break;
            case "Bicycle Crunches" :

                reps_exe_data();
                tv_reps_info_current.setText((Exercise_Reps.BicycleCrunches+plan_reps)+" Reps");

                break;
            case "Heel Touch" :

                reps_exe_data();
                tv_reps_info_current.setText((Exercise_Reps.HeelTouch+plan_reps)+" Reps");

                break;
            case "Lunges" :

                reps_exe_data();
                tv_reps_info_current.setText((Exercise_Reps.Lunges+plan_reps)+" Reps");

                break;
            case "Butt Bridge":

                reps_exe_data();
                tv_reps_info_current.setText((Exercise_Reps.ButtBridge+plan_reps)+" Reps");

                break;
            case "Step up Onto Chair" :

                reps_exe_data();
                tv_reps_info_current.setText((Exercise_Reps.StepUpOntoChair+plan_reps)+" Reps");

                break;
            case "Reclined Oblique Twist" :

                reps_exe_data();
                tv_reps_info_current.setText((Exercise_Reps.ReclinedObliqueTwist+plan_reps)+" Reps");

                break;
            case "Abdominal Crunches" :

                reps_exe_data();
                tv_reps_info_current.setText((Exercise_Reps.AbdominalCrunches+plan_reps)+" Reps");

                break;
            case "Burpee" :

                reps_exe_data();
                tv_reps_info_current.setText((Exercise_Reps.Burpee+plan_reps)+" Reps");


                break;
            case "V-UP" :

                reps_exe_data();
                tv_reps_info_current.setText((Exercise_Reps.VUps+plan_reps)+" Reps");

                break;
            case "Leg Raise":

                reps_exe_data();
                tv_reps_info_current.setText((Exercise_Reps.LegRaise+plan_reps)+" Reps");

                break;
            case "Crunches With Legs Raised" :

                reps_exe_data();
                tv_reps_info_current.setText((Exercise_Reps.CrunchesWithLegRaised+plan_reps)+" Reps");

                break;

            default:
                status_timer_exe = true;
                progress_bar_exe.setGradient(CircularProgressIndicator.LINEAR_GRADIENT,getResources().getColor(R.color.under_bmi));

                exercise_timer = new CountDownTimer(current_time, 1000) {
                    public void onTick(long millisUntilFinished) {
                        current_exercise = millisUntilFinished;
                        progress_bar_exe.setCurrentProgress(millisUntilFinished/1000);
                        if (status_speech){
                            if (millisUntilFinished<4000 && millisUntilFinished>3000){
                                speech_voice.play(text_countdown_3);
                            }else if (millisUntilFinished<3000 && millisUntilFinished>2000){
                                speech_voice.play(text_countdown_2);
                            }else if (millisUntilFinished<2000 && millisUntilFinished>1000){
                                speech_voice.play(text_countdown_1);
                            }else if (millisUntilFinished<1000){
                                speech_voice.play(text_countdown_stop);
                            }
                        }

                    }
                    public void onFinish() {
                        if (exe_prog+1!=exercise_name.size()){
                            exe_prog++;
                            get_ready(10000,10000);
                        }else {
                            onPlanCompleteDialouge();
                        }
                        exercise_timer = null;
                    }
                };
                exercise_timer.start();
        }

    }

    private void reps_exe_data(){
        System.out.println("My data : Reached");
        if (status_speech){
            speech_voice.play(text_press_tick);
        }

        status_timer_exe = false;
        btn_exe_done_proceed.setVisibility(View.VISIBLE);
        progress_bar_exe.setVisibility(View.GONE);
        tv_reps_info_current.setVisibility(View.VISIBLE);
    }

    private void onPlanCompleteDialouge(){
        final View view = getLayoutInflater().inflate( R.layout.item_completed, null,false);
        AlertDialog.Builder dialog = new AlertDialog.Builder( StartPlan_Activity.this,R.style.MyDialogStyle);
        if (view.getParent() != null){
            ((ViewGroup) view.getParent()).removeView(view);
        }
        dialog.setView(view);

        ((GifDrawable)gif_exe.getDrawable()).stop();

        Button btn_exit = view.findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loaderrr){
                    finish();
                }else{
                    if (mInterstitialAd != null) {
                    mInterstitialAd.show(StartPlan_Activity.this);
                    loaderrr = true;
                } else {
                    finish();
                }
                }
            }
        });

        AlertDialog testDialog;
        testDialog = dialog.create();
        testDialog.getWindow().setDimAmount(0.6f);
        testDialog.show();
        testDialog.setCancelable(false);
        testDialog.setCanceledOnTouchOutside(false);
    }
    private void getGifcurrent(String name){
        switch (name){
            case "Mountain Climber" : gif_exe.setImageResource(R.drawable.thumb_mountainclimber);
                tv_exe_name.setText("Mountain Climber");
                break;
            case "Squats" : gif_exe.setImageResource(R.drawable.thumb_squats);
                tv_exe_name.setText("Squats");
                break;
            case "High Stepping" : gif_exe.setImageResource(R.drawable.thumb_high_stepper);
                tv_exe_name.setText("High Stepping");
                break;
            case "Push Up" : gif_exe.setImageResource(R.drawable.thumb_pushup);
                tv_exe_name.setText("Push Up");
                break;
            case "Reverse Crunches" : gif_exe.setImageResource(R.drawable.thumb_reverse_crucnches);
                tv_exe_name.setText("Reverse Crunches");
                break;
            case "Plank" : gif_exe.setImageResource(R.drawable.thumb_plank);
                tv_exe_name.setText("Plank");
                break;
            case "Cobra Stretch" : gif_exe.setImageResource(R.drawable.thumb_cobra_stretch);
                tv_exe_name.setText("Cobra Stretch");
                break;
            case "Triceps Dips" : gif_exe.setImageResource(R.drawable.thumb_tricep_dips);
                tv_exe_name.setText("Triceps Dips");
                break;
            case "Jumping Jack": gif_exe.setImageResource(R.drawable.thumb_jumping_jack);
                tv_exe_name.setText("Jumping Jack");
                break;
            case "Long Arm Crunches" : gif_exe.setImageResource(R.drawable.thumb_longarn_crunches);
                tv_exe_name.setText("Long Arm Crunches");
                break;
            case "Bicycle Crunches" : gif_exe.setImageResource(R.drawable.thumb_bicycle_crunches);
                tv_exe_name.setText("Bicycle Crunches");
                break;
            case "Heel Touch" : gif_exe.setImageResource(R.drawable.thumb_heel_touch);
                tv_exe_name.setText("Heel Touch");
                break;
            case "Flutter Kick" : gif_exe.setImageResource(R.drawable.thumb_flutter_kick);
                tv_exe_name.setText("Flutter Kick");
                break;
            case "Skipping Without Rope" : gif_exe.setImageResource(R.drawable.thumb_skipping_norope);
                tv_exe_name.setText("Skipping Without Rope");
                break;
            case "Lunges" : gif_exe.setImageResource(R.drawable.thumb_lunges);
                tv_exe_name.setText("Lunges");
                break;
            case "Squat Pulses" : gif_exe.setImageResource(R.drawable.thumb_squat_pulses);
                tv_exe_name.setText("Squat Pulses");
                break;
            case "Butt Bridge": gif_exe.setImageResource(R.drawable.thumb_butt_bridge);
                tv_exe_name.setText("Butt Bridge");
                break;
            case "Step up Onto Chair" : gif_exe.setImageResource(R.drawable.thumb_step_up_ontochair);
                tv_exe_name.setText("Step up Onto Chair");
                break;
            case "Reclined Oblique Twist" : gif_exe.setImageResource(R.drawable.thumb_reclined_oblique_twist);
                tv_exe_name.setText("Reclined Oblique Twist");
                break;
            case "Abdominal Crunches" : gif_exe.setImageResource(R.drawable.thumb_abdominal_crunches);
                tv_exe_name.setText("Abdominal Crunches");
                break;
            case "Burpee" : gif_exe.setImageResource(R.drawable.thumb_burpee);
                tv_exe_name.setText("Burpee");
                break;
            case "Lateral Plank Walk" : gif_exe.setImageResource(R.drawable.thumb_lateral_plank);
                tv_exe_name.setText("Lateral Plank Walk");
                break;
            case "V-UP" : gif_exe.setImageResource(R.drawable.thumb_vups);
                tv_exe_name.setText("V-UP");
                break;
            case "Plank Jacks" : gif_exe.setImageResource(R.drawable.thumb_plank_jack);
                tv_exe_name.setText("Plank Jacks");
                break;
            case "Leg Raise": gif_exe.setImageResource(R.drawable.thumb_leg_raise);
                tv_exe_name.setText("Leg Raise");
                break;
            case "Crunches With Legs Raised" : gif_exe.setImageResource(R.drawable.thumb_leg_raised_crunches);
                tv_exe_name.setText("Crunches With Legs Raised");
                break;
        }
    }
    private void getGifnext(String name){
        int current = exe_prog+1;
        tv_next_exe_order.setText("Up Next - "+current+"/"+exercise_name.size());
        switch (name){
            case "Mountain Climber" : gif_exe_up_next.setImageResource(R.drawable.thumb_mountainclimber);
                tv_exe_next_name.setText("Mountain Climber");
                tv_next_duration.setText("00:"+duration);
                break;
            case "Squats" : gif_exe_up_next.setImageResource(R.drawable.thumb_squats);
                tv_exe_next_name.setText("Squats");
                tv_next_duration.setText((Exercise_Reps.Squats+plan_reps)+" Reps");
                break;
            case "High Stepping" : gif_exe_up_next.setImageResource(R.drawable.thumb_high_stepper);
                tv_exe_next_name.setText("High Stepping");
                tv_next_duration.setText("00:"+duration);
                break;
            case "Push Up" : gif_exe_up_next.setImageResource(R.drawable.thumb_pushup);
                tv_exe_next_name.setText("Push Up");
                tv_next_duration.setText((Exercise_Reps.PushUps+plan_reps)+" Reps");
                break;
            case "Reverse Crunches" : gif_exe_up_next.setImageResource(R.drawable.thumb_reverse_crucnches);
                tv_exe_next_name.setText("Reverse Crunches");
                tv_next_duration.setText((Exercise_Reps.ReverseCrunches+plan_reps)+" Reps");
                break;
            case "Plank" : gif_exe_up_next.setImageResource(R.drawable.thumb_plank);
                tv_exe_next_name.setText("Plank");
                tv_next_duration.setText("00:"+duration);
                break;
            case "Cobra Stretch" : gif_exe_up_next.setImageResource(R.drawable.thumb_cobra_stretch);
                tv_exe_next_name.setText("Cobra Stretch");
                tv_next_duration.setText("00:"+duration);
                break;
            case "Triceps Dips" : gif_exe_up_next.setImageResource(R.drawable.thumb_tricep_dips);
                tv_exe_next_name.setText("Triceps Dips");
                tv_next_duration.setText((Exercise_Reps.TricepDips+plan_reps)+" Reps");
                break;
            case "Jumping Jack": gif_exe_up_next.setImageResource(R.drawable.thumb_jumping_jack);
                tv_exe_next_name.setText("Jumping Jack");
                tv_next_duration.setText("00:"+duration);
                break;
            case "Long Arm Crunches" : gif_exe_up_next.setImageResource(R.drawable.thumb_longarn_crunches);
                tv_exe_next_name.setText("Long Arm Crunches");
                tv_next_duration.setText((Exercise_Reps.LongArmCrunches+plan_reps)+" Reps");
                break;
            case "Bicycle Crunches" : gif_exe_up_next.setImageResource(R.drawable.thumb_bicycle_crunches);
                tv_exe_next_name.setText("Bicycle Crunches");
                tv_next_duration.setText((Exercise_Reps.BicycleCrunches+plan_reps)+" Reps");
                break;
            case "Heel Touch" : gif_exe_up_next.setImageResource(R.drawable.thumb_heel_touch);
                tv_exe_next_name.setText("Heel Touch");
                tv_next_duration.setText((Exercise_Reps.HeelTouch+plan_reps)+" Reps");
                break;
            case "Flutter Kick" : gif_exe_up_next.setImageResource(R.drawable.thumb_flutter_kick);
                tv_exe_next_name.setText("Flutter Kick");
                tv_next_duration.setText("00:"+duration);
                break;
            case "Skipping Without Rope" : gif_exe_up_next.setImageResource(R.drawable.thumb_skipping_norope);
                tv_exe_next_name.setText("Skipping Without Rope");
                tv_next_duration.setText("00:"+duration);
                break;
            case "Lunges" : gif_exe_up_next.setImageResource(R.drawable.thumb_lunges);
                tv_exe_next_name.setText("Lunges");
                tv_next_duration.setText((Exercise_Reps.Lunges+plan_reps)+" Reps");
                break;
            case "Squat Pulses" : gif_exe_up_next.setImageResource(R.drawable.thumb_squat_pulses);
                tv_exe_next_name.setText("Squat Pulses");
                tv_next_duration.setText("00:"+duration);
                break;
            case "Butt Bridge": gif_exe_up_next.setImageResource(R.drawable.thumb_butt_bridge);
                tv_exe_next_name.setText("Butt Bridge");
                tv_next_duration.setText((Exercise_Reps.ButtBridge+plan_reps)+" Reps");
                break;
            case "Step up Onto Chair" : gif_exe_up_next.setImageResource(R.drawable.thumb_step_up_ontochair);
                tv_exe_next_name.setText("Step up Onto Chair");
                tv_next_duration.setText((Exercise_Reps.StepUpOntoChair+plan_reps)+" Reps");
                break;
            case "Reclined Oblique Twist" : gif_exe_up_next.setImageResource(R.drawable.thumb_reclined_oblique_twist);
                tv_exe_next_name.setText("Reclined Oblique Twist");
                tv_next_duration.setText((Exercise_Reps.ReclinedObliqueTwist+plan_reps)+" Reps");
                break;
            case "Abdominal Crunches" : gif_exe_up_next.setImageResource(R.drawable.thumb_abdominal_crunches);
                tv_exe_next_name.setText("Abdominal Crunches");
                tv_next_duration.setText((Exercise_Reps.AbdominalCrunches+plan_reps)+" Reps");
                break;
            case "Burpee" : gif_exe_up_next.setImageResource(R.drawable.thumb_burpee);
                tv_exe_next_name.setText("Burpee");
                tv_next_duration.setText((Exercise_Reps.Burpee+plan_reps)+" Reps");
                break;
            case "Lateral Plank Walk" : gif_exe_up_next.setImageResource(R.drawable.thumb_lateral_plank);
                tv_exe_next_name.setText("Lateral Plank Walk");
                tv_next_duration.setText("00:"+duration);
                break;
            case "V-UP" : gif_exe_up_next.setImageResource(R.drawable.thumb_vups);
                tv_exe_next_name.setText("V-UP");
                tv_next_duration.setText((Exercise_Reps.VUps+plan_reps)+" Reps");
                break;
            case "Plank Jacks" : gif_exe_up_next.setImageResource(R.drawable.thumb_plank_jack);
                tv_exe_next_name.setText("Plank Jacks");
                tv_next_duration.setText("00:"+duration);
                break;
            case "Leg Raise": gif_exe_up_next.setImageResource(R.drawable.thumb_leg_raise);
                tv_exe_next_name.setText("Leg Raise");
                tv_next_duration.setText((Exercise_Reps.LegRaise+plan_reps)+" Reps");
                break;
            case "Crunches With Legs Raised" : gif_exe_up_next.setImageResource(R.drawable.thumb_leg_raised_crunches);
                tv_exe_next_name.setText("Crunches With Legs Raised");
                tv_next_duration.setText((Exercise_Reps.CrunchesWithLegRaised+plan_reps)+" Reps");
                break;
        }
    }
    private int getGifcommon(String name){
        switch (name){
            case "Mountain Climber" : return R.drawable.thumb_mountainclimber;
            case "Squats" : return R.drawable.thumb_squats;
            case "High Stepping" : return R.drawable.thumb_high_stepper;
            case "Push Up" : return R.drawable.thumb_pushup;
            case "Reverse Crunches" : return R.drawable.thumb_reverse_crucnches;
            case "Plank" : return R.drawable.thumb_plank;
            case "Cobra Stretch" : return R.drawable.thumb_cobra_stretch;
            case "Triceps Dips" : return R.drawable.thumb_tricep_dips;
            case "Jumping Jack": return R.drawable.thumb_jumping_jack;
            case "Long Arm Crunches" : return R.drawable.thumb_longarn_crunches;
            case "Bicycle Crunches" : return R.drawable.thumb_bicycle_crunches;
            case "Heel Touch" : return R.drawable.thumb_heel_touch;
            case "Flutter Kick" : return R.drawable.thumb_flutter_kick;
            case "Skipping Without Rope" : return R.drawable.thumb_skipping_norope;
            case "Lunges" : return R.drawable.thumb_lunges;
            case "Squat Pulses" : return R.drawable.thumb_squat_pulses;
            case "Butt Bridge": return R.drawable.thumb_butt_bridge;
            case "Step up Onto Chair" : return R.drawable.thumb_step_up_ontochair;
            case "Reclined Oblique Twist" : return R.drawable.thumb_reclined_oblique_twist;
            case "Abdominal Crunches" : return R.drawable.thumb_abdominal_crunches;
            case "Burpee" : return R.drawable.thumb_burpee;
            case "Lateral Plank Walk" : return R.drawable.thumb_lateral_plank;
            case "V-UP" : return R.drawable.thumb_vups;
            case "Plank Jacks" : return R.drawable.thumb_plank_jack;
            case "Leg Raise": return R.drawable.thumb_leg_raise;
            case "Crunches With Legs Raised" : return R.drawable.thumb_leg_raised_crunches;
            default:return 0;
        }
    }
    private int getDesccommon(String name){
        switch (name){
            case "Mountain Climber" : return R.string.MountainClimber;
            case "Squats" : return R.string.Squats;
            case "High Stepping" : return R.string.highstepping;
            case "Push Up" : return R.string.pushup;
            case "Reverse Crunches" : return R.string.reversecrunches;
            case "Plank" : return R.string.plank;
            case "Cobra Stretch" : return R.string.cobra;
            case "Triceps Dips" : return R.string.tricepdips;
            case "Jumping Jack": return R.string.jumpingjack;
            case "Long Arm Crunches" : return R.string.longarmcrunches;
            case "Bicycle Crunches" : return R.string.bicyclecrunches;
            case "Heel Touch" : return R.string.heeltouch;
            case "Flutter Kick" : return R.string.flutterkick;
            case "Skipping Without Rope" : return R.string.skippingnorope;
            case "Lunges" : return R.string.lunges;
            case "Squat Pulses" : return R.string.squatpulses;
            case "Butt Bridge": return R.string.buttbridge;
            case "Step up Onto Chair" : return R.string.stepupontochair;
            case "Reclined Oblique Twist" : return R.string.reclinedoblique;
            case "Abdominal Crunches" : return R.string.abdominalcrunches;
            case "Burpee" : return R.string.burpee;
            case "Lateral Plank Walk" : return R.string.lateralplankwalk;
            case "V-UP" : return R.string.vups;
            case "Plank Jacks" : return R.string.plankjack;
            case "Leg Raise": return R.string.legraise;
            case "Crunches With Legs Raised" : return R.string.cruncheswithlegraised;
            default:return 0;
        }
    }
    private void setData(String started_plan,int position){
        
        if (started_plan.equalsIgnoreCase("Beginner")){

            switch (position){
                case 0 :exercise_name.add("Mountain Climber");
                    exercise_name.add("Squats");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Push Up");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 1 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 2 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Flutter Kick");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 3 :  ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 4 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Lunges");
                    exercise_name.add("Push Up");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 5 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Squat Pulses");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 6 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Lunges");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 7 : ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 8 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Squats");
                    exercise_name.add("Plank jack");
                    exercise_name.add("Push Up");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    break;
                case 9 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Lunges");
                    exercise_name.add("Plank Jack");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 10 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Squats");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Push Up");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 11 : ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 12 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 13 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Step up Onto Chair");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Butt Bridge");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 14 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Step up Onto Chair");
                    exercise_name.add("Push Up");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Reclined Oblique Twist");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 15 : ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 16 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Lunges");
                    exercise_name.add("Plank Jack");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Butt Bridge");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Reclined Oblique Twist");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 17 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Squats");
                    exercise_name.add("Burpee");
                    exercise_name.add("Push Ups");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 18 : exercise_name.add("Mountain Climber");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Squat Pulses");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Butt bridge");
                    exercise_name.add("Reclined Oblique Twist");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 19 : ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 20 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Burpee");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Butt bridge");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Reclined Oblique Twist");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 21 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Squats");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Plank Jacks");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 22 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Lunges");
                    exercise_name.add("Plank Jacks");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 23 : ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 24 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Butt Bridge");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Reclined Oblique Twist");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 25 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Lunges");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Step Up Onto Chair");
                    exercise_name.add("Push Up");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Reclined Oblique Twist");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 26 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Squats");
                    exercise_name.add("Plank Jack");
                    exercise_name.add("Burpees");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 27 : ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 28 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Squats");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Squat Pulses");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 29 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Lunges");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Push Up");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                default: ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
            }

        }else if (current_plan.equalsIgnoreCase("Intermediate")){

            switch (position){
                case 0 :exercise_name.add("Mountain Climber");
                    exercise_name.add("Squats");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Push Up");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Push Up");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 1 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 2 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Flutter Kick");
                    exercise_name.add("Plank");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Flutter Kick");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 3 :    ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 4 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Lunges");
                    exercise_name.add("Push Up");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Lunges");
                    exercise_name.add("Push Up");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 5 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Squat Pulses");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Squat Pulses");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 6 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Lunges");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Lunges");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 7 : ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 8 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Squats");
                    exercise_name.add("Plank jack");
                    exercise_name.add("Push Up");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Squats");
                    exercise_name.add("Plank jack");
                    exercise_name.add("Push Up");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    break;
                case 9 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Lunges");
                    exercise_name.add("Plank Jack");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Lunges");
                    exercise_name.add("Plank Jack");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 10 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Squats");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Push Up");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Push Up");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 11 : ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 12 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 13 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Step up Onto Chair");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Butt Bridge");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Step up Onto Chair");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Butt Bridge");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 14 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Step up Onto Chair");
                    exercise_name.add("Push Up");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Reclined Oblique Twist");
                    exercise_name.add("Plank");
                    exercise_name.add("Step up Onto Chair");
                    exercise_name.add("Push Up");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Reclined Oblique Twist");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 15 : ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 16 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Lunges");
                    exercise_name.add("Plank Jack");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Butt Bridge");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Reclined Oblique Twist");
                    exercise_name.add("Plank");
                    exercise_name.add("Lunges");
                    exercise_name.add("Plank Jack");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Butt Bridge");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Reclined Oblique Twist");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 17 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Squats");
                    exercise_name.add("Burpee");
                    exercise_name.add("Push Ups");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("Burpee");
                    exercise_name.add("Push Ups");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 18 : exercise_name.add("Mountain Climber");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Squat Pulses");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Butt bridge");
                    exercise_name.add("Reclined Oblique Twist");
                    exercise_name.add("Plank");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Squat Pulses");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Butt bridge");
                    exercise_name.add("Reclined Oblique Twist");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 19 : ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 20 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Burpee");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Butt bridge");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Reclined Oblique Twist");
                    exercise_name.add("Plank");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Burpee");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Butt bridge");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Reclined Oblique Twist");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 21 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Squats");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Plank Jacks");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Plank Jacks");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 22 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Lunges");
                    exercise_name.add("Plank Jacks");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("Plank");
                    exercise_name.add("Lunges");
                    exercise_name.add("Plank Jacks");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 23 : ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 24 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Butt Bridge");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Reclined Oblique Twist");
                    exercise_name.add("Plank");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Butt Bridge");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Reclined Oblique Twist");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 25 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Lunges");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Step Up Onto Chair");
                    exercise_name.add("Push Up");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Reclined Oblique Twist");
                    exercise_name.add("Plank");
                    exercise_name.add("Lunges");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Step Up Onto Chair");
                    exercise_name.add("Push Up");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Reclined Oblique Twist");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 26 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Squats");
                    exercise_name.add("Plank Jack");
                    exercise_name.add("Burpees");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("Plank Jack");
                    exercise_name.add("Burpees");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 27 : ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 28 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Squats");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Squat Pulses");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Squat Pulses");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 29 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Lunges");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Push Up");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Lunges");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Push Up");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                default: ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
            }

        }else if (current_plan.equalsIgnoreCase("Advanced")){

            switch (position){
                case 0 :exercise_name.add("Mountain Climber");
                    exercise_name.add("Squats");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Push Up");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Push Up");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Push Up");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 1 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 2 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Flutter Kick");
                    exercise_name.add("Plank");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Flutter Kick");
                    exercise_name.add("Plank");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Flutter Kick");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 3 :    ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 4 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Lunges");
                    exercise_name.add("Push Up");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Lunges");
                    exercise_name.add("Push Up");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Lunges");
                    exercise_name.add("Push Up");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 5 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Squat Pulses");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Squat Pulses");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Squat Pulses");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 6 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Lunges");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Lunges");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Lunges");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 7 : ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 8 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Squats");
                    exercise_name.add("Plank jack");
                    exercise_name.add("Push Up");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("Plank jack");
                    exercise_name.add("Push Up");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("Plank jack");
                    exercise_name.add("Push Up");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 9 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Lunges");
                    exercise_name.add("Plank Jack");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Lunges");
                    exercise_name.add("Plank Jack");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Lunges");
                    exercise_name.add("Plank Jack");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 10 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Squats");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Push Up");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Push Up");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Push Up");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 11 : ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 12 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 13 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Step up Onto Chair");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Butt Bridge");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Step up Onto Chair");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Butt Bridge");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Step up Onto Chair");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Butt Bridge");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 14 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Step up Onto Chair");
                    exercise_name.add("Push Up");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("V-UP");
                    exercise_name.add("Plank");
                    exercise_name.add("Step up Onto Chair");
                    exercise_name.add("Push Up");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("V-UP");
                    exercise_name.add("Plank");
                    exercise_name.add("Step up Onto Chair");
                    exercise_name.add("Push Up");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("V-UP");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 15 : ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 16 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Lunges");
                    exercise_name.add("Plank Jack");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("V-UP");
                    exercise_name.add("Plank");
                    exercise_name.add("Lunges");
                    exercise_name.add("Plank Jack");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("V-UP");
                    exercise_name.add("Plank");
                    exercise_name.add("Lunges");
                    exercise_name.add("Plank Jack");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("V-UP");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 17 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Squats");
                    exercise_name.add("Burpee");
                    exercise_name.add("Push Ups");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("Burpee");
                    exercise_name.add("Push Ups");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("Burpee");
                    exercise_name.add("Push Ups");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 18 : exercise_name.add("Mountain Climber");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Squat Pulses");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("V-UP");
                    exercise_name.add("Plank");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Squat Pulses");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("V-UP");
                    exercise_name.add("Plank");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Squat Pulses");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("V-UP");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 19 : ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 20 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Burpee");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Butt bridge");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("V-UP");
                    exercise_name.add("Plank");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Burpee");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Butt bridge");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("V-UP");
                    exercise_name.add("Plank");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Burpee");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Butt bridge");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("V-UP");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 21 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Squats");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Plank Jacks");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Plank Jacks");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Plank Jacks");
                    exercise_name.add("Leg Raise");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 22 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Lunges");
                    exercise_name.add("Plank Jacks");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("Plank");
                    exercise_name.add("Lunges");
                    exercise_name.add("Plank Jacks");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("Plank");
                    exercise_name.add("Lunges");
                    exercise_name.add("Plank Jacks");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Bicycle Crunches");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 23 : ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 24 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("V-UP");
                    exercise_name.add("Plank");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("V-UP");
                    exercise_name.add("Plank");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("High Stepping");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("V-UP");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 25 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Lunges");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Step Up Onto Chair");
                    exercise_name.add("Push Up");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("V-UP");
                    exercise_name.add("Plank");
                    exercise_name.add("Lunges");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Step Up Onto Chair");
                    exercise_name.add("Push Up");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("V-UP");
                    exercise_name.add("Plank");
                    exercise_name.add("Lunges");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Step Up Onto Chair");
                    exercise_name.add("Push Up");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("V-UP");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 26 : exercise_name.add("Mountain Climber");
                    exercise_name.add("Squats");
                    exercise_name.add("Plank Jack");
                    exercise_name.add("Burpees");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("Plank Jack");
                    exercise_name.add("Burpees");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("Plank Jack");
                    exercise_name.add("Burpees");
                    exercise_name.add("Reverse Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Crunches With Legs Raised");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 27 : ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
                case 28 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Squats");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Squat Pulses");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Squat Pulses");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Plank");
                    exercise_name.add("Squats");
                    exercise_name.add("Triceps Dips");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Squat Pulses");
                    exercise_name.add("Long Arm Crunches");
                    exercise_name.add("Flutter Kicks");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                case 29 : exercise_name.add("Skipping Without Rope");
                    exercise_name.add("Lunges");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Push Up");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Lunges");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Push Up");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Lunges");
                    exercise_name.add("Jumping Jack");
                    exercise_name.add("Lateral Plank Walk");
                    exercise_name.add("Push Up");
                    exercise_name.add("Abdominal Crunches");
                    exercise_name.add("Heel Touch");
                    exercise_name.add("Plank");
                    exercise_name.add("Cobra Stretch");
                    break;
                default: ly_restday.setVisibility(View.VISIBLE);
                    ly_plan_running.setVisibility(View.GONE);
                    break;
            }

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ready_timer!=null) ready_timer.cancel(); Log.e("Stopped","ready");
        if (exercise_timer!=null) exercise_timer.cancel(); Log.e("Stopped","exercise");
        if (speech_voice !=null) speech_voice.shutdown(); Log.e("Stopped","speech");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ready_timer!=null) ready_timer.cancel(); Log.e("Stopped","ready");
        if (exercise_timer!=null) exercise_timer.cancel(); Log.e("Stopped","exercise");
        if (speech_voice !=null) speech_voice.shutdown(); Log.e("Stopped","speech");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ready_timer!=null) ready_timer.cancel(); Log.e("Stopped","ready");
        if (exercise_timer!=null) exercise_timer.cancel(); Log.e("Stopped","exercise");
        if (speech_voice !=null) speech_voice.shutdown(); Log.e("Stopped","speech");
    }

    @Override
    public void onBackPressed() {
        backDialouge();
    }

    private void backDialouge(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Do you want to end the ongoing plan now ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
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
}