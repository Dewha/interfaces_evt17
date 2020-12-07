package com.example.eswallet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.Calendar;

public class NewCost extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    Button btn_date;
    ImageButton btn_back;
    TextView tv;
    Bundle arguments;
    boolean isCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_cost);

        //init
        btn_date = findViewById(R.id.btn_date);
        btn_back = findViewById(R.id.btn_back);
        tv = findViewById(R.id.category);

        arguments = getIntent().getExtras();
        isCost = arguments.getBoolean("category");

        //events
        btn_date.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        //defaults
        String date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "." +
                Calendar.getInstance().get(Calendar.MONTH) + "." +
                Calendar.getInstance().get(Calendar.YEAR);
        btn_date.setText(date);

        if (isCost)
            tv.setText("Расход");
        else
            tv.setText("Доход");
    }


    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "."  + month + "." + year;
        btn_date.setText(date);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_date : showDatePickerDialog(); break;
            case R.id.btn_back : onBackPressed(); break;
        }
    }
}