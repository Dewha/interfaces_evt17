package com.example.eswallet;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class NewCost extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    Button btn_date, btn_add;
    ImageButton btn_back;
    TextInputLayout ti_sum, ti_comment;
    Bundle arguments;
    FragmentTransaction fragmentTransaction;
    public static CostCategoryFragment costCategoryFragment;
    public static IncomeCategoryFragment incomeCategoryFragment;
    DatabaseReference reference;
    RecordsHelperClass recordsHelperClass;
    String userLogin;
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
        btn_add = findViewById(R.id.btn_add);
        ti_sum = findViewById(R.id.ti_sum_text);
        ti_comment = findViewById(R.id.ti_comment);
        arguments = getIntent().getExtras();
        isCost = arguments.getBoolean("category");
        userLogin = getIntent().getStringExtra("login");

        //events
        btn_date.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_add.setOnClickListener(this);

        //defaults
        String date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "." +
                (Calendar.getInstance().get(Calendar.MONTH)+1) + "." +
                Calendar.getInstance().get(Calendar.YEAR);
        btn_date.setText(date);

        if (isCost) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            costCategoryFragment = new CostCategoryFragment();
            fragmentTransaction.replace(R.id.fragment, costCategoryFragment);
            fragmentTransaction.commit();
        }
        else {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            incomeCategoryFragment = new IncomeCategoryFragment();
            fragmentTransaction.replace(R.id.fragment, incomeCategoryFragment);
            fragmentTransaction.commit();
        }

        //database
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(userLogin);
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
        String date = dayOfMonth + "."  + (month + 1) + "." + year;
        btn_date.setText(date);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_date : showDatePickerDialog(); break;
            case R.id.btn_back : onBackPressed(); break;
            case R.id.btn_add : {
                String sumValue = ti_sum.getEditText().getText().toString();
                if (!sumValue.isEmpty()) {
                    String[] date = btn_date.getText().toString().split("[.]");
                    String comment = ti_comment.getEditText().getText().toString();
                    if (isCost) {
                        String category = CostCategoryFragment.category;
                        recordsHelperClass = new RecordsHelperClass(sumValue, category, date[0], date[1], date[2], comment);
                        reference.child("cost").push().setValue(recordsHelperClass);

                        Query query = reference.child("sum");
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    String sumFromDB = snapshot.getValue(String.class);
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    assert sumFromDB != null;
                                    String newSum = String.valueOf(Integer.parseInt(sumFromDB)-Integer.parseInt(sumValue));
                                    hashMap.put("sum", newSum);
                                    reference.updateChildren(hashMap).addOnSuccessListener(aVoid -> { });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { }
                        });
                    } else {
                        String category = IncomeCategoryFragment.category;
                        recordsHelperClass = new RecordsHelperClass(sumValue, category, date[0], date[1], date[2], comment);
                        reference.child("income").push().setValue(recordsHelperClass);
                    }
                    onBackPressed();
                }
            }
        }
    }
}
