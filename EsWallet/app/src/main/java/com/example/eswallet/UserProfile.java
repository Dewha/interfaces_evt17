package com.example.eswallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    //variables
    ImageButton btn_back;
    TextView tv_username, tv_login, tv_remainder, tv_total_cost, tv_total_income, tv_first_letter;
    Button btn_exit;
    String login, username;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //init
        btn_back = findViewById(R.id.btn_back);
        tv_username = findViewById(R.id.tv_username);
        tv_login = findViewById(R.id.tv_login);
        tv_remainder = findViewById(R.id.tv_remainder_text);
        tv_total_cost = findViewById(R.id.tv_total_cost_text);
        tv_total_income = findViewById(R.id.tv_total_income_text);
        btn_exit = findViewById(R.id.btn_exit);
        tv_first_letter = findViewById(R.id.tv_name_first_letter);

        //defaults
        login = getIntent().getStringExtra("login");
        username = getIntent().getStringExtra("full_name");
        tv_username.setText(username);
        tv_login.setText(login);
        String defaultValue = "0 " + getString(R.string.currency);
        tv_remainder.setText(defaultValue);
        tv_total_cost.setText(defaultValue);
        tv_total_income.setText(defaultValue);
        tv_first_letter.setText(username.substring(0,1).toUpperCase());

        //database
        Query query = FirebaseDatabase.getInstance().getReference("users").child(login);
        Query queryRemainder = FirebaseDatabase.getInstance().getReference("users").child(login).child("sum");
        query.addListenerForSingleValueEvent(valueEventListener);
        queryRemainder.addListenerForSingleValueEvent(remainderEventListener);

        //events
        View.OnClickListener onBackClick = v -> onBackPressed();
        View.OnClickListener onExitClick = v -> saveUser();
        btn_back.setOnClickListener(onBackClick);
        btn_exit.setOnClickListener(onExitClick);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.child("cost").exists()) {
                getData(snapshot.child("cost").getChildren(), tv_total_cost);
            }
            if (snapshot.child("income").exists()) {
                getData(snapshot.child("income").getChildren(), tv_total_income);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private void getData(Iterable<DataSnapshot> data, TextView field) {
        int total = 0;
        for(DataSnapshot dataSnapshot: data) {
            RecordsHelperClass records = dataSnapshot.getValue(RecordsHelperClass.class);
            assert records != null;
            total += Integer.parseInt(records.getSum());
        }
        String _fullSum = total + " " + getResources().getString(R.string.currency);
        field.setText(_fullSum);
    }

    ValueEventListener remainderEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                String newRemainder = snapshot.getValue(String.class) + " " +  getString(R.string.currency);
                tv_remainder.setText(newRemainder);
            } else {
                String newRemainder = "0 " + getString(R.string.currency);
                tv_remainder.setText(newRemainder);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void saveUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN, "");
        editor.putString(PASSWORD, "");
        editor.apply();

        //start login activity
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}