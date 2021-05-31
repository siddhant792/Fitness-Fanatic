package com.sagar.fitnessfanatic.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.sagar.fitnessfanatic.R;

public class MainActivity extends AppCompatActivity{

    ViewPager viewPager;
    ChipNavigationBar bottom_bar;
    Vibrator vibrate;
    private int REQUEST_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottom_bar = findViewById(R.id.bottom_bar);
        viewPager = findViewById(R.id.vp_main);

        vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        bottom_bar.setItemSelected(R.id.home_item,true);

        PagerAdapter adapter = new com.sagar.fitnessfanatic.Adapters.PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setCurrentItem(1,true);

        bottom_bar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i){
                    case R.id.diet_item:
                        viewPager.setCurrentItem(0,true);
                        vibrate.vibrate(50);
                        break;
                    case R.id.home_item:
                        viewPager.setCurrentItem(1,true);
                        vibrate.vibrate(50);
                        break;
                    case R.id.report_item:
                        viewPager.setCurrentItem(2,true);
                        vibrate.vibrate(50);
                        break;
                    case R.id.pedo_item:
                        viewPager.setCurrentItem(3,true);
                        vibrate.vibrate(50);
                        break;
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottom_bar.setItemSelected(R.id.diet_item,true);
                        break;
                    case 1:
                        bottom_bar.setItemSelected(R.id.home_item,true);
                        break;
                    case 2:
                        bottom_bar.setItemSelected(R.id.report_item,true);
                        break;
                    case 3:
                        bottom_bar.setItemSelected(R.id.pedo_item,true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // app update

        // Creates instance of the manager.
        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);

// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    // Request the update.
                    try {
                            appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.FLEXIBLE,MainActivity.this,REQUEST_CODE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.e("Update flow failed!" , resultCode+"");
            }
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}