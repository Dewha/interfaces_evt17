package com.example.eswallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

public class CostCategoryFragment extends Fragment implements View.OnClickListener {

    public static String category = "other";
    ImageButton[] buttons = new ImageButton[20];
    View prevBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //init
        View view = inflater.inflate(R.layout.fragment_cost_category, container, false);
        buttons[0] = view.findViewById(R.id.btn_transport);
        buttons[1] = view.findViewById(R.id.btn_sport);
        buttons[2] = view.findViewById(R.id.btn_family);
        buttons[3] = view.findViewById(R.id.btn_products);
        buttons[4] = view.findViewById(R.id.btn_gifts);
        buttons[5] = view.findViewById(R.id.btn_education);
        buttons[6] = view.findViewById(R.id.btn_cafe);
        buttons[7] = view.findViewById(R.id.btn_house);
        buttons[8] = view.findViewById(R.id.btn_leisure);
        buttons[9] = view.findViewById(R.id.btn_health);
        buttons[10] = view.findViewById(R.id.btn_texes);
        buttons[11] = view.findViewById(R.id.btn_serve);
        buttons[12] = view.findViewById(R.id.btn_penalties);
        buttons[13] = view.findViewById(R.id.btn_auto);
        buttons[14] = view.findViewById(R.id.btn_clothes);
        buttons[15] = view.findViewById(R.id.btn_medicine);
        buttons[16] = view.findViewById(R.id.btn_job);
        buttons[17] = view.findViewById(R.id.btn_debts);
        buttons[18] = view.findViewById(R.id.btn_animals);
        buttons[19] = view.findViewById(R.id.btn_other);
        prevBtn = buttons[19];

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