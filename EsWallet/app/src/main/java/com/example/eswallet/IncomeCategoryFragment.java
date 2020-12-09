package com.example.eswallet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class IncomeCategoryFragment extends Fragment implements View.OnClickListener {

    public static String category = "other";
    ImageButton[] buttons = new ImageButton[8];
    View prevBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //init
        View view = inflater.inflate(R.layout.fragment_income_category, container, false);
        buttons[0] = view.findViewById(R.id.btn_salary);
        buttons[1] = view.findViewById(R.id.btn_pt_job);
        buttons[2] = view.findViewById(R.id.btn_gifts);
        buttons[3] = view.findViewById(R.id.btn_investments);
        buttons[4] = view.findViewById(R.id.btn_percent);
        buttons[5] = view.findViewById(R.id.btn_debts);
        buttons[6] = view.findViewById(R.id.btn_rent);
        buttons[7] = view.findViewById(R.id.btn_other);
        prevBtn = buttons[7];
        for(int i = 0; i < buttons.length; i++) {
            buttons[i].setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        prevBtn.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        prevBtn.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        v.getLayoutParams().width = 200;
        v.getLayoutParams().height = 200;
        v.requestLayout();
        category = v.getTag().toString();
        prevBtn = v;
    }
}