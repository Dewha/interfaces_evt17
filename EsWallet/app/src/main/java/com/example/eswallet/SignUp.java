package com.example.eswallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    Button btn_AlreadyHaveAccount, btn_SignUp;
    TextInputLayout login, name, second_name, pass1, pass2;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        //init
        btn_SignUp = findViewById(R.id.btn_signup);
        btn_AlreadyHaveAccount = findViewById(R.id.btn_alreadyhaveaccount);
        login = findViewById(R.id.auth_login);
        name = findViewById(R.id.auth_username);
        second_name = findViewById(R.id.auth_user_second_name);
        pass1 = findViewById(R.id.auth_password1);
        pass2 = findViewById(R.id.auth_password2);

        //events
        View.OnClickListener onBtnSignUpClick = v -> {
            if (validateLogin(login) & validateName(name) &
                    validateName(second_name) & validatePass(pass1) &
                    validatePass(pass2) & isPassMatch(pass1, pass2)) {
                String enteredLogin = login.getEditText().getText().toString().trim();
                String enteredName = name.getEditText().getText().toString().trim();
                String enteredSecName = second_name.getEditText().getText().toString().trim();
                String enteredPassword = pass1.getEditText().getText().toString().trim();
                UserHelperClass userHelperClass = new UserHelperClass(enteredLogin, enteredName, enteredSecName, enteredPassword);

                reference = FirebaseDatabase.getInstance().getReference("users");
                Query checkUser = reference.orderByChild("login").equalTo(enteredLogin);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            login.setError(null);
                            login.setErrorEnabled(false);
                            reference.child(enteredLogin).setValue(userHelperClass);
                            onBackPressed();
                        } else {
                            login.setError(getString(R.string.error_login_already_exists));
                            login.requestFocus();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
            else return;
        };

        View.OnClickListener onBtnAlreadyHaveAccountClick = v -> SignUp.super.onBackPressed();

        btn_AlreadyHaveAccount.setOnClickListener(onBtnAlreadyHaveAccountClick);
        btn_SignUp.setOnClickListener(onBtnSignUpClick);
    }

    //validate registration data
    private Boolean validateLogin(TextInputLayout login) {
        String value = login.getEditText().getText().toString();
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

    private Boolean validateName(TextInputLayout name) {
        String value = name.getEditText().getText().toString();
        String noWhiteSpaces = "(?=\\S+$)";
        if (value.isEmpty()) {
            name.setError(getString(R.string.error_empty_field));
            return false;
        } else if (value.matches(noWhiteSpaces)) {
            name.setError(getString(R.string.error_no_white_spaces));
            return false;
        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePass(TextInputLayout pass) {
        String value = pass.getEditText().getText().toString();
        if (value.isEmpty()) {
            pass.setError(getString(R.string.error_empty_field));
            return false;
        } else {
            pass.setError(null);
            pass.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean isPassMatch(TextInputLayout pass1, TextInputLayout pass2) {
        String value1 = pass1.getEditText().getText().toString();
        String value2 = pass2.getEditText().getText().toString();
        if (!value1.equals(value2)) {
            pass2.setError(getString(R.string.error_password_are_not_match));
            return false;
        } else {
            pass2.setError(null);
            pass2.setErrorEnabled(false);
            return true;
        }
    }

}