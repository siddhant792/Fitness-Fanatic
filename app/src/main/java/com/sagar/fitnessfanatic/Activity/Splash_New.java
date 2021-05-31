package com.sagar.fitnessfanatic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.sagar.fitnessfanatic.R;

public class Splash_New extends AppCompatActivity {

    ImageView img_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__new);
        img_splash = findViewById(R.id.img_splash);
        ObjectAnimator.ofFloat(img_splash, View.ALPHA, 0.2f, 1.0f).setDuration(1050).start();
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash_New.this,MainActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }
}