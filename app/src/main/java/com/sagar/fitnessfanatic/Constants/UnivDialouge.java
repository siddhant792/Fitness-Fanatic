package com.sagar.fitnessfanatic.Constants;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.sagar.fitnessfanatic.R;

public class UnivDialouge {


    Activity activity;
    AlertDialog alertDialog;


    public UnivDialouge(Activity activity) {
        this.activity = activity;
    }

    public void show(){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogStyle);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.item_dialouge,null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    public void cancel(){

        alertDialog.cancel();

    }

}
