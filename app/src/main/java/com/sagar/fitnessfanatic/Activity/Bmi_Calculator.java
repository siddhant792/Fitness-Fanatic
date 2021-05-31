package com.sagar.fitnessfanatic.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sagar.fitnessfanatic.R;

import java.text.DecimalFormat;

public class Bmi_Calculator extends AppCompatActivity {

    Button calculate_bmi_btn;
    EditText et_height,et_weight;
    Vibrator vibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi__calculator);

        calculate_bmi_btn = findViewById(R.id.calculate_bmi_btn);
        et_weight = findViewById(R.id.et_weight);
        et_height = findViewById(R.id.et_height);
        vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        calculate_bmi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_height.getText().toString())) {
                    et_height.setError("Cannot be empty");
                } else if (TextUtils.isEmpty(et_weight.getText().toString())) {
                    et_weight.setError("Cannot be empty");
                } else {
                    final View dialouge = getLayoutInflater().inflate(R.layout.dialouge_bmi, null, false);
                    vibrate.vibrate(80);
                    View v = Bmi_Calculator.this.getCurrentFocus();
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    double height_int = Double.parseDouble(et_height.getText().toString());
                    double height = height_int / 100;
                    double weight = Double.parseDouble(et_weight.getText().toString());
                    double calculated_bmi = weight / (height * height);

                    if (calculated_bmi > 45 || calculated_bmi < 0) {
                        Toast.makeText(Bmi_Calculator.this, "Unexpected Input", Toast.LENGTH_SHORT).show();
                    } else {
                        TextView calculated_bmi_figure, bmi_text_status, bmi_advice_text;
                        ImageView img_advice, close_dialouge;
                        LinearLayout ly_back_bmi_color;

                        calculated_bmi_figure = dialouge.findViewById(R.id.calculated_bmi_figure);
                        bmi_text_status = dialouge.findViewById(R.id.bmi_text_status);
                        bmi_advice_text = dialouge.findViewById(R.id.bmi_advice_text);
                        img_advice = dialouge.findViewById(R.id.img_advice);
                        ly_back_bmi_color = dialouge.findViewById(R.id.ly_back_bmi_color);
                        close_dialouge = dialouge.findViewById(R.id.close_dialouge);

                        calculated_bmi_figure.setText(String.valueOf(calculated_bmi));
                        calculated_bmi_figure.setText(new DecimalFormat("##.#").format(calculated_bmi));
                        if (calculated_bmi <= 18.5) {
                            bmi_text_status.setText("Underweight");
                            bmi_advice_text.setText("Here are some tips to increase your weight -");
                            img_advice.setImageResource(R.drawable.under_new);
                            ly_back_bmi_color.setBackgroundColor(getResources().getColor(R.color.under_bmi));
                        } else if (calculated_bmi > 18.5 && calculated_bmi < 24.9) {
                            bmi_text_status.setText("Normal");
                            bmi_advice_text.setText("Here are some tips to maintain your BMI -");
                            img_advice.setImageResource(R.drawable.normal_new);
                            ly_back_bmi_color.setBackgroundColor(getResources().getColor(R.color.normal_bmi));
                        } else {
                            bmi_text_status.setText("Overweight");
                            bmi_advice_text.setText("Here are some tips to reduce your weight -");
                            img_advice.setImageResource(R.drawable.over_new);
                            ly_back_bmi_color.setBackgroundColor(getResources().getColor(R.color.over_bmi));
                        }

                        AlertDialog.Builder dialog = new AlertDialog.Builder(Bmi_Calculator.this, R.style.MyDialogStyle);
                        if (dialouge.getParent() != null) {
                            ((ViewGroup) view.getParent()).removeView(dialouge);
                        }
                        dialog.setView(dialouge);
                        final AlertDialog testDialog = dialog.create();
                        testDialog.show();

                        close_dialouge.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                testDialog.cancel();
                            }
                        });
                    }


                }
            }
        });




    }
}