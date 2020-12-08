package com.example.eswallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfile extends AppCompatActivity {

    //variables
    ImageButton btn_back;
    TextView tv_username, tv_login;
    Button btn_exit;
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
        btn_exit = findViewById(R.id.btn_exit);

        //defaults
        tv_username.setText(getIntent().getStringExtra("full_name"));
        tv_login.setText(getIntent().getStringExtra("login"));

        //events
        View.OnClickListener onBackClick = v -> onBackPressed();
        View.OnClickListener onExitClick = v -> saveUser();
        btn_back.setOnClickListener(onBackClick);
        btn_exit.setOnClickListener(onExitClick);
    }

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