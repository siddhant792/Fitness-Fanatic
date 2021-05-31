package com.sagar.fitnessfanatic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sagar.fitnessfanatic.Constants.Exercise_Reps;
import com.sagar.fitnessfanatic.R;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class Explore_Exercise extends AppCompatActivity {

    GifImageView exe_gif;
    String exe_name;
    TextView tv_exercise_name,tv_description,tv_current_exe,tv_total_exe,tv_duration,tv_duration_reps;

    ArrayList<String> exercise_array = new ArrayList<>();
    String position_initial;
    int position;

    ImageView shift_left,shift_right;
    Button btn_start;
    int duration=0,plan_reps=0;
    String plan_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore__exercise);
        exe_gif = findViewById(R.id.exe_gif);
        tv_exercise_name = findViewById(R.id.tv_exercise_name);
        tv_description = findViewById(R.id.tv_description);
        shift_left = findViewById(R.id.shift_left);
        shift_right = findViewById(R.id.shift_right);
        tv_duration_reps = findViewById(R.id.tv_duration_reps);
        tv_duration = findViewById(R.id.tv_duration);
        tv_current_exe = findViewById(R.id.tv_current_exe);
        btn_start = findViewById(R.id.btn_start);
        tv_total_exe = findViewById(R.id.tv_total_exe);
        exercise_array = getIntent().getStringArrayListExtra("array");
        position_initial = getIntent().getStringExtra("position");
        plan_name = getIntent().getStringExtra("plan");
        position = Integer.parseInt(position_initial);
        if (plan_name.equalsIgnoreCase("beginner")){
            duration = 30;
            plan_reps = 0;

        }else if (plan_name.equalsIgnoreCase("intermediate")){
            duration = 45;
            plan_reps = 2;

        }else if (plan_name.equalsIgnoreCase("advanced")){
            duration = 60;
            plan_reps = 4;

        }
        setExercise(exercise_array.get(position));

        tv_total_exe.setText("/"+String.valueOf(exercise_array.size()));

        tv_current_exe.setText(String.valueOf(position+1));

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        if (position==0){
            //shift_left.setVisibility(View.INVISIBLE);
            shift_left.setImageResource(R.drawable.ic_shift_left_non);
        }else if (position+1==exercise_array.size()){
            //shift_right.setVisibility(View.INVISIBLE);
            shift_right.setImageResource(R.drawable.ic_shift_right_non);
        }

        shift_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position!=0){
                    position--;
                    setExercise(exercise_array.get(position));
                    shift_right.setImageResource(R.drawable.ic_shift_right);
                    //shift_right.setVisibility(View.VISIBLE);
                    tv_current_exe.setText(String.valueOf(position+1));
                    if (position == 0){
                        //shift_left.setVisibility(View.INVISIBLE);
                        shift_left.setImageResource(R.drawable.ic_shift_left_non);
                    }
                }
                else{
                    //shift_left.setVisibility(View.INVISIBLE);
                    shift_left.setImageResource(R.drawable.ic_shift_left_non);
                }
            }
        });

        shift_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position+1!=exercise_array.size()){
                    position++;
                    setExercise(exercise_array.get(position));
                    //shift_left.setVisibility(View.VISIBLE);
                    shift_left.setImageResource(R.drawable.ic_shiift_left);
                    tv_current_exe.setText(String.valueOf(position+1));
                    if (position+1 == exercise_array.size()){
                        //shift_right.setVisibility(View.INVISIBLE);
                        shift_right.setImageResource(R.drawable.ic_shift_right_non);
                    }
                }else {
                    //shift_right.setVisibility(View.INVISIBLE);
                    shift_right.setImageResource(R.drawable.ic_shift_right_non);
                }

            }
        });

    }

    private void setExercise(String name){
        switch (name){
            case "Mountain Climber" : exe_gif.setImageResource(R.drawable.thumb_mountainclimber);
                tv_exercise_name.setText("Mountain Climber");
                tv_description.setText(R.string.MountainClimber);
                tv_duration.setText("00:"+duration);
                tv_duration_reps.setText("Duration");
                break;
            case "Squats" : exe_gif.setImageResource(R.drawable.thumb_squats);
                tv_exercise_name.setText("Squats");
                tv_description.setText(R.string.Squats);
                tv_duration.setText(String.valueOf(Exercise_Reps.Squats+plan_reps));
                tv_duration_reps.setText("Repetitions");
                break;
            case "High Stepping" : exe_gif.setImageResource(R.drawable.thumb_high_stepper);
                tv_exercise_name.setText("High Stepping");
                tv_description.setText(R.string.highstepping);
                tv_duration.setText("00:"+duration);
                tv_duration_reps.setText("Duration");
                break;
            case "Push Up" : exe_gif.setImageResource(R.drawable.thumb_pushup);
                tv_exercise_name.setText("Push Up");
                tv_description.setText(R.string.pushup);
                tv_duration.setText(String.valueOf(Exercise_Reps.PushUps+plan_reps));
                tv_duration_reps.setText("Repetitions");
                break;
            case "Reverse Crunches" : exe_gif.setImageResource(R.drawable.thumb_reverse_crucnches);
                tv_exercise_name.setText("Reverse Crunches");
                tv_description.setText(R.string.reversecrunches);
                tv_duration.setText(String.valueOf(Exercise_Reps.ReverseCrunches+plan_reps));
                tv_duration_reps.setText("Repetitions");
                break;
            case "Plank" : exe_gif.setImageResource(R.drawable.thumb_plank);
                tv_exercise_name.setText("Plank");
                tv_description.setText(R.string.plank);
                tv_duration.setText("00:"+duration);
                tv_duration_reps.setText("Duration");
                break;
            case "Cobra Stretch" : exe_gif.setImageResource(R.drawable.thumb_cobra_stretch);
                tv_exercise_name.setText("Cobra Stretch");
                tv_description.setText(R.string.cobra);
                tv_duration.setText("00:"+duration);
                tv_duration_reps.setText("Duration");
                break;
            case "Triceps Dips" : exe_gif.setImageResource(R.drawable.thumb_tricep_dips);
                tv_exercise_name.setText("Triceps Dips");
                tv_description.setText(R.string.tricepdips);
                tv_duration.setText(String.valueOf(Exercise_Reps.TricepDips+plan_reps));
                tv_duration_reps.setText("Repetitions");
                break;
            case "Jumping Jack": exe_gif.setImageResource(R.drawable.thumb_jumping_jack);
                tv_exercise_name.setText("Jumping Jack");
                tv_description.setText(R.string.jumpingjack);
                tv_duration.setText("00:"+duration);
                tv_duration_reps.setText("Duration");
                break;
            case "Long Arm Crunches" : exe_gif.setImageResource(R.drawable.thumb_longarn_crunches);
                tv_exercise_name.setText("Long Arm Crunches");
                tv_description.setText(R.string.longarmcrunches);
                tv_duration.setText(String.valueOf(Exercise_Reps.LongArmCrunches+plan_reps));
                tv_duration_reps.setText("Repetitions");
                break;
            case "Bicycle Crunches" : exe_gif.setImageResource(R.drawable.thumb_bicycle_crunches);
                tv_exercise_name.setText("Bicycle Crunches");
                tv_description.setText(R.string.bicyclecrunches);
                tv_duration.setText(String.valueOf(Exercise_Reps.BicycleCrunches+plan_reps));
                tv_duration_reps.setText("Repetitions");
                break;
            case "Heel Touch" : exe_gif.setImageResource(R.drawable.thumb_heel_touch);
                tv_exercise_name.setText("Heel Touch");
                tv_description.setText(R.string.heeltouch);
                tv_duration.setText(String.valueOf(Exercise_Reps.HeelTouch+plan_reps));
                tv_duration_reps.setText("Repetitions");
                break;
            case "Flutter Kick" : exe_gif.setImageResource(R.drawable.thumb_flutter_kick);
                tv_exercise_name.setText("Flutter Kick");
                tv_description.setText(R.string.flutterkick);
                tv_duration.setText("00:"+duration);
                tv_duration_reps.setText("Duration");
                break;
            case "Skipping Without Rope" : exe_gif.setImageResource(R.drawable.thumb_skipping_norope);
                tv_exercise_name.setText("Skipping Without Rope");
                tv_description.setText(R.string.skippingnorope);
                tv_duration.setText("00:"+duration);
                tv_duration_reps.setText("Duration");
                break;
            case "Lunges" : exe_gif.setImageResource(R.drawable.thumb_lunges);
                tv_exercise_name.setText("Lunges");
                tv_description.setText(R.string.lunges);
                tv_duration.setText(String.valueOf(Exercise_Reps.Lunges+plan_reps));
                tv_duration_reps.setText("Repetitions");
                break;
            case "Squat Pulses" : exe_gif.setImageResource(R.drawable.thumb_squat_pulses);
                tv_exercise_name.setText("Squat Pulses");
                tv_description.setText(R.string.squatpulses);
                tv_duration.setText("00:"+duration);
                tv_duration_reps.setText("Duration");
                break;
            case "Butt Bridge": exe_gif.setImageResource(R.drawable.thumb_butt_bridge);
                tv_exercise_name.setText("Butt Bridge");
                tv_description.setText(R.string.buttbridge);
                tv_duration.setText(String.valueOf(Exercise_Reps.ButtBridge+plan_reps));
                tv_duration_reps.setText("Repetitions");
                break;
            case "Step up Onto Chair" : exe_gif.setImageResource(R.drawable.thumb_step_up_ontochair);
                tv_exercise_name.setText("Step up Onto Chair");
                tv_description.setText(R.string.stepupontochair);
                tv_duration.setText(String.valueOf(Exercise_Reps.StepUpOntoChair+plan_reps));
                tv_duration_reps.setText("Repetitions");
                break;
            case "Reclined Oblique Twist" : exe_gif.setImageResource(R.drawable.thumb_reclined_oblique_twist);
                tv_exercise_name.setText("Reclined Oblique Twist");
                tv_description.setText(R.string.reclinedoblique);
                tv_duration.setText(String.valueOf(Exercise_Reps.ReclinedObliqueTwist+plan_reps));
                tv_duration_reps.setText("Repetitions");
                break;
            case "Abdominal Crunches" : exe_gif.setImageResource(R.drawable.thumb_abdominal_crunches);
                tv_exercise_name.setText("Abdominal Crunches");
                tv_description.setText(R.string.abdominalcrunches);
                tv_duration.setText(String.valueOf(Exercise_Reps.AbdominalCrunches+plan_reps));
                tv_duration_reps.setText("Repetitions");
                break;
            case "Burpee" : exe_gif.setImageResource(R.drawable.thumb_burpee);
                tv_exercise_name.setText("Burpee");
                tv_description.setText(R.string.burpee);
                tv_duration.setText(String.valueOf(Exercise_Reps.Burpee+plan_reps));
                tv_duration_reps.setText("Repetitions");
                break;
            case "Lateral Plank Walk" : exe_gif.setImageResource(R.drawable.thumb_lateral_plank);
                tv_exercise_name.setText("Lateral Plank Walk");
                tv_description.setText(R.string.lateralplankwalk);
                tv_duration.setText("00:"+duration);
                tv_duration_reps.setText("Duration");
                break;
            case "V-UP" : exe_gif.setImageResource(R.drawable.thumb_vups);
                tv_exercise_name.setText("V-UP");
                tv_description.setText(R.string.vups);
                tv_duration.setText(String.valueOf(Exercise_Reps.VUps+plan_reps));
                tv_duration_reps.setText("Repetitions");
                break;
            case "Plank Jacks" : exe_gif.setImageResource(R.drawable.thumb_plank_jack);
                tv_exercise_name.setText("Plank Jacks");
                tv_description.setText(R.string.plankjack);
                tv_duration.setText("00:"+duration);
                tv_duration_reps.setText("Duration");
                break;
            case "Leg Raise": exe_gif.setImageResource(R.drawable.thumb_leg_raise);
                tv_exercise_name.setText("Leg Raise");
                tv_description.setText(R.string.legraise);
                tv_duration.setText(String.valueOf(Exercise_Reps.LegRaise+plan_reps));
                tv_duration_reps.setText("Repetitions");
                break;
            case "Crunches With Legs Raised" : exe_gif.setImageResource(R.drawable.thumb_leg_raised_crunches);
                tv_exercise_name.setText("Crunches With Legs Raised");
                tv_description.setText(R.string.cruncheswithlegraised);
                tv_duration.setText(String.valueOf(Exercise_Reps.CrunchesWithLegRaised+plan_reps));
                tv_duration_reps.setText("Repetitions");
                break;
        }
    }
}