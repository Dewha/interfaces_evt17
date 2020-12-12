package com.example.eswallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity implements View.OnClickListener {

    //variables
    Button btn_auth, btn_signup;
    ImageView image;
    TextView hello, desc;
    TextInputLayout login, pass;
    CardView topBar;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";

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
        btn_signup.setOnClickListener(this);
        btn_auth.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signup : {
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
            } break;
            case R.id.btn_auth : {
                loginUser();
            } break;
        }
    }
    //validate login data
    private Boolean validateLogin(TextInputLayout login) {
        String value = Objects.requireNonNull(login.getEditText()).getText().toString();
        String noWhiteSpaces = "(?=\\S+$)";
        if (value.isEmpty()) {
            login.setError(getString(R.string.error_empty_field));
            return false;
        } else if (value.matches(noWhiteSpaces)) {
            login.setError(getString(R.string.error_no_white_spaces));
            return false;
        } else if (value.length()>15) {
            login.setError(getString(R.string.error_too_long));
            return false;
        } else {
            login.setError(null);
            login.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePass(TextInputLayout pass) {
        String value = Objects.requireNonNull(pass.getEditText()).getText().toString();
        if (value.isEmpty()) {
            pass.setError(getString(R.string.error_empty_field));
            return false;
        } else {
            pass.setError(null);
            pass.setErrorEnabled(false);
            return true;
        }
    }

    //authorization
    private void loginUser() {
        if (validateLogin(login) & validatePass(pass)) {
            String userEnteredLogin = Objects.requireNonNull(login.getEditText()).getText().toString().trim();
            String userEnteredPassword = Objects.requireNonNull(pass.getEditText()).getText().toString().trim();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            Query checkUser = reference.orderByChild("login").equalTo(userEnteredLogin);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        login.setError(null);
                        login.setErrorEnabled(false);
                        String passFromDB = snapshot.child(userEnteredLogin).child("password").getValue(String.class);
                        if (passFromDB.equals(userEnteredPassword)) {
                            pass.setError(null);
                            pass.setErrorEnabled(false);

                            //collect data from db to variables
                            String loginFromDB = snapshot.child(userEnteredLogin).child("login").getValue(String.class);
                            String nameFromDB = snapshot.child(userEnteredLogin).child("name").getValue(String.class);
                            String secondNameFromDB = snapshot.child(userEnteredLogin).child("second_name").getValue(String.class);

                            //save login and password
                            saveUser(loginFromDB, passFromDB);

                            //start new activity
                            Intent intent = new Intent(Login.this, Dashboard.class);
                            intent.putExtra("login", loginFromDB);
                            intent.putExtra("name", nameFromDB);
                            intent.putExtra("second_name", secondNameFromDB);

                            Pair[] pairs = new Pair[2];
                            pairs[0] = new Pair<View, String>(topBar, "trans_top_bar");
                            pairs[1] = new Pair<View, String>(image, "logo_image");
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this , pairs);
                                startActivity(intent, options.toBundle());
                            }
                        } else {
                            pass.setError(getString(R.string.error_wrong_password));
                            pass.requestFocus();
                        }
                    } else {
                        login.setError(getString(R.string.error_wrong_login));
                        login.requestFocus();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void saveUser(String login, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN, login);
        editor.putString(PASSWORD, password);
        editor.apply();
    }
}