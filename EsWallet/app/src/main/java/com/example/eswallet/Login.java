package com.example.eswallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {

    Button btn_auth, btn_signup;
    ImageView image;
    TextView hello, desc;
    TextInputLayout login, pass;
    CardView topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        //init
        btn_auth = findViewById(R.id.btn_auth);
        btn_signup  = findViewById(R.id.btn_signup);
        image = findViewById(R.id.logo_small);
        hello  = findViewById(R.id.tv_hello);
        desc  = findViewById(R.id.tv_desc);
        login = findViewById(R.id.auth_login);
        pass = findViewById(R.id.auth_password);
        topBar = findViewById(R.id.cv_top_bar);

        //events
        View.OnClickListener onSignUpClick = v -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            Pair[] pairs = new Pair[8];
            pairs[0] = new Pair<View, String>(image, "logo_image");
            pairs[1] = new Pair<View, String>(hello, "logo_text");
            pairs[2] = new Pair<View, String>(desc, "logo_desc");
            pairs[3] = new Pair<View, String>(login, "trans_login");
            pairs[4] = new Pair<View, String>(pass, "trans_pass");
            pairs[5] = new Pair<View, String>(btn_auth, "trans_go");
            pairs[6] = new Pair<View, String>(btn_signup, "trans_next");
            pairs[7] = new Pair<View, String>(topBar, "trans_top_bar");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this , pairs);
                startActivity(intent, options.toBundle());
            }
        };

        View.OnClickListener onAuthClick = v -> startActivity(new Intent(Login.this, UserProfile.class));

        btn_signup.setOnClickListener(onSignUpClick);
        btn_auth.setOnClickListener(onAuthClick);
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}