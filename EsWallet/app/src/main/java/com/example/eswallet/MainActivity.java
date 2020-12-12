package com.example.eswallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    //variables
    private static int SPLACH_SCREEN = 3000;
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView logo;
    CardView topBar;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    private String loadedLogin = "";
    private String loadedPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init
        image = findViewById(R.id.iv_logo);
        logo = findViewById(R.id.tv_logo);
        topBar = findViewById(R.id.cv_top_bar);
        //animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        image.startAnimation(topAnim);
        logo.startAnimation(bottomAnim);

        loadUser();
        if (loadedLogin.isEmpty() || loadedPassword.isEmpty()) {
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, Login.class);
                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View,String>(image,"logo_image");
                pairs[1] = new Pair<View,String>(logo,"logo_text");
                pairs[2] = new Pair<View,String>(topBar,"trans_top_bar");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                    startActivity(intent, options.toBundle());
                }
            },SPLACH_SCREEN);
        } else {
            loginUser();
        }
    }

    public void loadUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        loadedLogin = sharedPreferences.getString(LOGIN, "");
        loadedPassword = sharedPreferences.getString(PASSWORD, "");
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                //collect data from db to variables
                String loginFromDB = snapshot.child(loadedLogin).child("login").getValue(String.class);
                String nameFromDB = snapshot.child(loadedLogin).child("name").getValue(String.class);
                String secondNameFromDB = snapshot.child(loadedLogin).child("second_name").getValue(String.class);
                //start new activity
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
                    intent.putExtra("login", loginFromDB);
                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("second_name", secondNameFromDB);

                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View, String>(topBar, "trans_top_bar");
                    pairs[1] = new Pair<View, String>(image, "logo_image");
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                        startActivity(intent, options.toBundle());
                    }
                },SPLACH_SCREEN);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void  loginUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("login").equalTo(loadedLogin);
        checkUser.addListenerForSingleValueEvent(valueEventListener);
    }
}