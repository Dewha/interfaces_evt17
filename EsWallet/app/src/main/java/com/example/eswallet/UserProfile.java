package com.example.eswallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {

    ImageButton btn_back;
    TextView tv_username, tv_login;
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

        //defaults
        tv_username.setText(getIntent().getStringExtra("full_name"));
        tv_login.setText(getIntent().getStringExtra("login"));

        //events
        View.OnClickListener onBackClick = v -> onBackPressed();
        btn_back.setOnClickListener(onBackClick);
    }
}