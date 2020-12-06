package com.example.eswallet;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Dashboard extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    //variables
    Button btn_day, btn_week, btn_month, btn_year,
            btn_day_ul, btn_week_ul, btn_month_ul,
            btn_year_ul, btn_costs, btn_income;
    ImageButton btn_edit, btn_menu;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CardView topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_dashboard);

        //init
        btn_day = findViewById(R.id.btn_day);
        btn_week = findViewById(R.id.btn_week);
        btn_month = findViewById(R.id.btn_month);
        btn_year = findViewById(R.id.btn_year);
        btn_day_ul = findViewById(R.id.btn_day_underline);
        btn_week_ul = findViewById(R.id.btn_week_underline);
        btn_month_ul = findViewById(R.id.btn_month_underline);
        btn_year_ul = findViewById(R.id.btn_year_underline);
        btn_costs = findViewById(R.id.btn_costs);
        btn_income = findViewById(R.id.btn_income);
        btn_edit = findViewById(R.id.btn_edit);
        btn_menu = findViewById(R.id.btn_menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        topBar = findViewById(R.id.cv_top_bar);
        //defaults
        btn_day_ul.setBackgroundColor(getResources().getColor(R.color.coal));
        btn_week_ul.setBackgroundColor(getResources().getColor(R.color.bone));
        btn_month_ul.setBackgroundColor(getResources().getColor(R.color.bone));
        btn_year_ul.setBackgroundColor(getResources().getColor(R.color.bone));
        btn_costs.setTextColor(getResources().getColor(R.color.bone));
        btn_income.setTextColor(getResources().getColor(R.color.bone_transparent));

        //events
        btn_day.setOnClickListener(this);
        btn_week.setOnClickListener(this);
        btn_month.setOnClickListener(this);
        btn_year.setOnClickListener(this);
        btn_costs.setOnClickListener(this);
        btn_income.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_menu.setOnClickListener(this);

        //navigation menu
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_day: {
                btn_day_ul.setBackgroundColor(getResources().getColor(R.color.coal));
                btn_week_ul.setBackgroundColor(getResources().getColor(R.color.bone));
                btn_month_ul.setBackgroundColor(getResources().getColor(R.color.bone));
                btn_year_ul.setBackgroundColor(getResources().getColor(R.color.bone));
            } break;
            case R.id.btn_week: {
                btn_day_ul.setBackgroundColor(getResources().getColor(R.color.bone));
                btn_week_ul.setBackgroundColor(getResources().getColor(R.color.coal));
                btn_month_ul.setBackgroundColor(getResources().getColor(R.color.bone));
                btn_year_ul.setBackgroundColor(getResources().getColor(R.color.bone));
            } break;
            case R.id.btn_month: {
                btn_day_ul.setBackgroundColor(getResources().getColor(R.color.bone));
                btn_week_ul.setBackgroundColor(getResources().getColor(R.color.bone));
                btn_month_ul.setBackgroundColor(getResources().getColor(R.color.coal));
                btn_year_ul.setBackgroundColor(getResources().getColor(R.color.bone));
            } break;
            case R.id.btn_year: {
                btn_day_ul.setBackgroundColor(getResources().getColor(R.color.bone));
                btn_week_ul.setBackgroundColor(getResources().getColor(R.color.bone));
                btn_month_ul.setBackgroundColor(getResources().getColor(R.color.bone));
                btn_year_ul.setBackgroundColor(getResources().getColor(R.color.coal));
            } break;
            case R.id.btn_costs: {
                btn_costs.setTextColor(getResources().getColor(R.color.bone));
                btn_income.setTextColor(getResources().getColor(R.color.bone_transparent));
            } break;
            case R.id.btn_income: {
                btn_costs.setTextColor(getResources().getColor(R.color.bone_transparent));
                btn_income.setTextColor(getResources().getColor(R.color.bone));
            } break;
            case R.id.btn_edit: {
                //some code 1
            } break;
            case R.id.btn_menu: {
                if (!drawerLayout.isDrawerOpen(Gravity.LEFT))
                    drawerLayout.openDrawer(Gravity.LEFT);
                else drawerLayout.closeDrawer(Gravity.RIGHT);
            } break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT))
            drawerLayout.closeDrawer(Gravity.LEFT);
        else super.onBackPressed();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home : onBackPressed(); break;
            case R.id.nav_about :
                startActivity(new Intent(Dashboard.this, About.class));
                break;
            case R.id.nav_profile : {
                Intent intent = new Intent(Dashboard.this, UserProfile.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(topBar, "trans_top_bar");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Dashboard.this , pairs);
                    startActivity(intent, options.toBundle());
                }
            }
                break;
            case R.id.nav_logout : break;
        }
        drawerLayout.closeDrawer(Gravity.LEFT);
        return true;
    }
}