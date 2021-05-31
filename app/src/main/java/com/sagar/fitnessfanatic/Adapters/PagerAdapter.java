package com.sagar.fitnessfanatic.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sagar.fitnessfanatic.Fragments.Diet;
import com.sagar.fitnessfanatic.Fragments.Home;
import com.sagar.fitnessfanatic.Fragments.Pedometer;
import com.sagar.fitnessfanatic.Fragments.Report;

public class PagerAdapter extends FragmentPagerAdapter {


    public PagerAdapter(FragmentManager fm){
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Diet diet = new Diet();
                return diet;
            case 1:
                Home home = new Home();
                return home;
            case 2:
                Report report = new Report();
                return report;
            case 3:
                Pedometer pedometer = new Pedometer();
                return pedometer;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "diet";
            case 1:
                return "home";
            case 2:
                return "report";
            case 3:
                return "pedometer";
            default:
                return super.getPageTitle(position);
        }
    }
}
