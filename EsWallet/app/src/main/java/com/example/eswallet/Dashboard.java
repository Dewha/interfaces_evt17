package com.example.eswallet;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Pair;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Dashboard extends AppCompatActivity implements View.OnClickListener,
                                                            NavigationView.OnNavigationItemSelectedListener,
                                                            DatePickerDialog.OnDateSetListener {

    //variables
    Button btn_day, btn_week, btn_month, btn_year,
            btn_day_ul, btn_week_ul, btn_month_ul,
            btn_year_ul, btn_costs, btn_income, btn_date, btn_exit;
    ImageButton btn_edit, btn_menu, btn_add;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView tv_username, tv_sum, tv_remainder, tv_nv_remainder, tv_first_letter;
    CardView topBar;
    ListView cards;
    String fullNameFromDB, login, date;
    DatabaseReference reference;
    boolean isCost = true;
    int periodFilter = 0;
    long fullSum = 0;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";

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
        btn_add = findViewById(R.id.btn_add);
        btn_date = findViewById(R.id.btn_date);
        tv_sum = findViewById(R.id.tv_sum);
        tv_remainder = findViewById(R.id.tv_remainder_text);
        btn_exit = navigationView.getHeaderView(0).findViewById(R.id.btn_exit);
        tv_username = navigationView.getHeaderView(0).findViewById(R.id.tv_username);
        tv_nv_remainder = navigationView.getHeaderView(0).findViewById(R.id.tv_nv_remainder);
        tv_first_letter = navigationView.getHeaderView(0).findViewById(R.id.tv_name_first_letter);
        cards = findViewById(R.id.lv_cards);

        //form database
        fullNameFromDB = getIntent().getStringExtra("name") + " " + getIntent().getStringExtra("second_name");
        login = getIntent().getStringExtra("login");
        //database
        loadDataFromDB();

        //defaults
        btn_day_ul.setBackgroundResource(R.color.coal);
        btn_day_ul.setBackgroundResource(R.color.coal);
        btn_week_ul.setBackgroundResource(R.color.bone);
        btn_month_ul.setBackgroundResource(R.color.bone);
        btn_year_ul.setBackgroundResource(R.color.bone);
        btn_costs.setTextColor(getResources().getColor(R.color.bone));
        btn_income.setTextColor(getResources().getColor(R.color.bone_transparent));
        tv_username.setText(fullNameFromDB);
        tv_first_letter.setText(fullNameFromDB.substring(0,1).toUpperCase());

        date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "." +
                (Calendar.getInstance().get(Calendar.MONTH) + 1) + "." +
                Calendar.getInstance().get(Calendar.YEAR);
        btn_date.setText(date);

        //events
        btn_day.setOnClickListener(this);
        btn_week.setOnClickListener(this);
        btn_month.setOnClickListener(this);
        btn_year.setOnClickListener(this);
        btn_costs.setOnClickListener(this);
        btn_income.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_menu.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_date.setOnClickListener(this);

        //navigation menu
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setCustomAdapter(RecordsHelperClass records,
                                 DataSnapshot dataSnapshot,
                                 ArrayList<String> comments,
                                 ArrayList<String> categories,
                                 ArrayList<String> day,
                                 ArrayList<String> month,
                                 ArrayList<String> year,
                                 ArrayList<Integer> icons,
                                 ArrayList<String> ids) {
        String strCategory = getString(getResources().getIdentifier(records.getCategory(), "string", getPackageName()));
        categories.add(records.getSum() + " " + getResources().getString(R.string.currency) + " - " + strCategory);
        comments.add(records.getComment());
        day.add(records.getDay());
        month.add(records.getMonth());
        year.add(records.getYear());
        icons.add(getResources().getIdentifier("ic_" + records.getCategory(), "mipmap", getPackageName()));
        ids.add(dataSnapshot.getKey());
        fullSum += Long.parseLong(records.getSum());
    }

    public void showData(Iterable<DataSnapshot> data) {
        ArrayList<String> comments = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();
        ArrayList<String> day = new ArrayList<>();
        ArrayList<String> month = new ArrayList<>();
        ArrayList<String> year = new ArrayList<>();
        ArrayList<Integer> icons = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        String[] _date = date.split("[.]");
        fullSum = 0;
        for (DataSnapshot dataSnapshot: data) {
            RecordsHelperClass records = dataSnapshot.getValue(RecordsHelperClass.class);
            assert records != null;
            switch (periodFilter) {
                case 0: {
                    if (records.getDay().equals(_date[0]) && records.getMonth().equals(_date[1]) && records.getYear().equals(_date[2])) {
                        setCustomAdapter(records, dataSnapshot, comments, categories, day, month, year, icons, ids);
                    }
                } break;
                case 1: {
                    int dbDay = Integer.parseInt(records.getDay()), _day = Integer.parseInt(_date[0]);
                    if (dbDay>=_day-7&&dbDay<=_day&&records.getMonth().equals(_date[1]) && records.getYear().equals(_date[2])) {
                        setCustomAdapter(records, dataSnapshot, comments, categories, day, month, year, icons, ids);
                    }
                } break;
                case 2: {
                    if (records.getMonth().equals(_date[1])&&records.getYear().equals(_date[2])) {
                        setCustomAdapter(records, dataSnapshot, comments, categories, day, month, year, icons, ids);
                    }
                } break;
                case 3: {
                    if (records.getYear().equals(_date[2])) {
                        setCustomAdapter(records, dataSnapshot, comments, categories, day, month, year, icons, ids);
                    }
                } break;
            }
        }
        String _fullSum = fullSum + " " + getResources().getString(R.string.currency);
        tv_sum.setText(_fullSum);
        CustomAdapter customAdapter = new CustomAdapter(Dashboard.this, categories, comments, day, month, year, icons, ids, login, isCost);
        cards.setAdapter(customAdapter);
        updateRemainder();
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                showData(snapshot.getChildren());
            } else {
                cards.setAdapter(null);
                String _sum = "0 " + getString(R.string.currency);
                tv_sum.setText(_sum);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void loadDataFromDB() {
        Query query;
        if (isCost) {
            query = FirebaseDatabase.getInstance().getReference("users").child(login).child("cost");
        } else {
            query = FirebaseDatabase.getInstance().getReference("users").child(login).child("income");
        }
        query.addListenerForSingleValueEvent(valueEventListener);
        updateRemainder();
    }

    ValueEventListener onUpdateRemainder = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                String newRemainder = snapshot.getValue(String.class) + " " +  getString(R.string.currency);
                tv_remainder.setText(newRemainder);
                tv_nv_remainder.setText(newRemainder);
            } else {
                String newRemainder = "0 " + getString(R.string.currency);
                tv_remainder.setText(newRemainder);
                tv_nv_remainder.setText(newRemainder);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void updateRemainder() {
        Query query = FirebaseDatabase.getInstance().getReference("users").child(login).child("sum");
        query.addListenerForSingleValueEvent(onUpdateRemainder);
    }

    public void changeBtnColors(Button btn1, Button btn2, Button btn3, Button btn4) {
        btn1.setBackgroundResource(R.color.coal);
        btn2.setBackgroundResource(R.color.bone);
        btn3.setBackgroundResource(R.color.bone);
        btn4.setBackgroundResource(R.color.bone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_day: {
                changeBtnColors(btn_day_ul, btn_week_ul, btn_month_ul, btn_year_ul);
                periodFilter = 0;
                loadDataFromDB();
            } break;
            case R.id.btn_week: {
                changeBtnColors(btn_week_ul, btn_day_ul, btn_month_ul, btn_year_ul);
                periodFilter = 1;
                loadDataFromDB();
            } break;
            case R.id.btn_month: {
                changeBtnColors(btn_month_ul, btn_day_ul, btn_week_ul, btn_year_ul);
                periodFilter = 2;
                loadDataFromDB();
            } break;
            case R.id.btn_year: {
                changeBtnColors(btn_year_ul, btn_day_ul, btn_week_ul, btn_month_ul);
                periodFilter = 3;
                loadDataFromDB();
            } break;
            case R.id.btn_costs: {
                btn_costs.setTextColor(getResources().getColor(R.color.bone));
                btn_income.setTextColor(getResources().getColor(R.color.bone_transparent));
                isCost = true;
                loadDataFromDB();
            } break;
            case R.id.btn_income: {
                btn_costs.setTextColor(getResources().getColor(R.color.bone_transparent));
                btn_income.setTextColor(getResources().getColor(R.color.bone));
                isCost = false;
                loadDataFromDB();
            } break;
            case R.id.btn_edit: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.new_remainder);
                final EditText sumInput = new EditText(Dashboard.this);
                sumInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(sumInput);
                builder.setPositiveButton(R.string.ok, (dialog, which) -> {
                    reference = FirebaseDatabase.getInstance().getReference().child("users").child(login);
                    Query query = reference.child("sum");
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                HashMap<String, Object> hashMap = new HashMap<>();
                                if (!sumInput.getText().toString().isEmpty()) {
                                    hashMap.put("sum", sumInput.getText().toString());
                                    reference.updateChildren(hashMap).addOnSuccessListener(aVoid -> { });
                                    updateRemainder();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });
                });
                builder.setNegativeButton(R.string.cancel, (dialog, which) -> { });
                builder.show();

            } break;
            case R.id.btn_menu: {
                if (!drawerLayout.isDrawerOpen(Gravity.LEFT))
                    drawerLayout.openDrawer(Gravity.LEFT);
                else drawerLayout.closeDrawer(Gravity.RIGHT);
            } break;
            case R.id.btn_add: {
                Intent intent = new Intent(Dashboard.this, NewCost.class);
                intent.putExtra("category", isCost);
                intent.putExtra("login", login);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(topBar, "trans_top_bar");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Dashboard.this , pairs);
                    startActivity(intent, options.toBundle());
                }
            } break;
            case R.id.btn_date: {
                showDatePickerDialog();
            } break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT))
            drawerLayout.closeDrawer(Gravity.LEFT);
        else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home : onBackPressed(); break;
            case R.id.nav_about : {
                Intent intent = new Intent(Dashboard.this, About.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(topBar, "trans_top_bar");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Dashboard.this , pairs);
                    startActivity(intent, options.toBundle());
                }
            }
                break;
            case R.id.nav_profile : {
                Intent intent = new Intent(Dashboard.this, UserProfile.class);
                intent.putExtra("full_name", fullNameFromDB);
                intent.putExtra("login", login);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(topBar, "trans_top_bar");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Dashboard.this , pairs);
                    startActivity(intent, options.toBundle());
                }
            }
                break;
            case R.id.nav_logout : {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(LOGIN, "");
                editor.putString(PASSWORD, "");
                editor.apply();

                //start login activity
                Intent intent = new Intent(this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(topBar, "trans_top_bar");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Dashboard.this , pairs);
                    startActivity(intent, options.toBundle());
                }
            } break;
        }
        drawerLayout.closeDrawer(Gravity.LEFT);
        return true;
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
        this.date = date;
        loadDataFromDB();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadDataFromDB();
    }
}