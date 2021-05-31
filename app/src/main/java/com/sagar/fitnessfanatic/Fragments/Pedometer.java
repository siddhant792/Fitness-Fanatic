package com.sagar.fitnessfanatic.Fragments;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sagar.fitnessfanatic.R;
import com.sagar.fitnessfanatic.Services.AlarmHandler;

import me.itangqi.waveloadingview.WaveLoadingView;

import static android.content.Context.MODE_PRIVATE;


public class Pedometer extends Fragment implements SensorEventListener {

    LinearLayout btn_start_pedo;
    TextView tv_today_tip,current_steps,tv_total_steps,tv_step;
    CardView pauseTextCardView;
    WaveLoadingView waveLoadingView;
    SharedPreferences step_db,sample_sp;
    Context context;
    SensorManager sensorManager;
    boolean running = false;
    boolean isSensorFound = false;
    Sensor countSensor;
    AlarmHandler alarmHandler;
    int sinceLastBoot = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View view =  inflater.inflate(R.layout.fragment_pedometer, container, false);
        btn_start_pedo = view.findViewById(R.id.btn_start_pedo);
        tv_today_tip = view.findViewById(R.id.tv_today_tip);
        current_steps = view.findViewById(R.id.stepsCounter);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        tv_total_steps = view.findViewById(R.id.tv_total_steps);
        pauseTextCardView = view.findViewById(R.id.pauseTextCardView);
        waveLoadingView = view.findViewById(R.id.waveLoadingView);
        tv_step = view.findViewById(R.id.tv_step);
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        step_db = context.getSharedPreferences("step_db", MODE_PRIVATE);
        sample_sp = context.getSharedPreferences("sample_sp", MODE_PRIVATE);
        sample_sp.edit().putInt("yoyo",0).apply();

        alarmHandler = new AlarmHandler(context);
        alarmHandler.cancelAlarmManager();
        alarmHandler.setAlarmManager();

        Toast.makeText(context, "set success", Toast.LENGTH_SHORT).show();


//        SharedPreferences.Editor editor = step_db.edit();
        countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        running = true;
        if (countSensor!=null){
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
            isSensorFound = true;
        }else {
            Toast.makeText(context, "Sensor not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (countSensor!=null){
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (!step_db.contains("since_last_boot")){
            SharedPreferences.Editor editor2 = step_db.edit();
            editor2.putInt("total_step",0);
            editor2.putInt("today_step",0);
            editor2.putInt("since_last_boot",(int) event.values[0]);
            editor2.apply();
        }else{
            if(step_db.getInt("since_last_boot",0) != (int) event.values[0]){
                SharedPreferences.Editor editor2 = step_db.edit();
                editor2.putInt("since_last_boot",(int) event.values[0]);
                editor2.apply();
            }
        }

        sinceLastBoot = step_db.getInt("since_last_boot",0);

        //Toast.makeText(context, "My Steps = " + (int) event.values[0], Toast.LENGTH_SHORT).show();
        if (running && isSensorFound){
            current_steps.setText(String.valueOf((int) event.values[0] - sinceLastBoot));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        alarmHandler.setAlarmManager();
    }
}