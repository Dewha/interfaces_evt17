package com.example.eswallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class SignUp extends AppCompatActivity {

    Button btn_AlreadyHaveAccount, btn_SignUp;
    TextInputLayout login, name, pass1, pass2;

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
        pass1 = findViewById(R.id.auth_password1);
        pass2 = findViewById(R.id.auth_password2);

        //events
        View.OnClickListener onBtnSignUpClick = v -> {
            if (!validateLogin(login) | !validateName(name) | !validatePass(pass1) | validatePass(pass2) | !isPassMatch(pass1, pass2)) {
                return;
            }
        };

        View.OnClickListener onBtnAlreadyHaveAccountClick = v -> SignUp.super.onBackPressed();

        btn_AlreadyHaveAccount.setOnClickListener(onBtnAlreadyHaveAccountClick);
        //btn_SignUp.setOnClickListener(onBtnSignUpClick);
    }

    //validate registration data
    private Boolean validateLogin(TextInputLayout login) {
        String value = login.getEditText().getText().toString();
        String noWhiteSpaces = "(?=\\S+$)";
        if (value.isEmpty()) {
            login.setError(getString(R.string.error_empty_field));
            return false;
        } else if (!value.matches(noWhiteSpaces)) {
            login.setError(getString(R.string.error_no_white_spaces));
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
        } else if (!value.matches(noWhiteSpaces)) {
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
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{5,}" +               //at least 5 characters
                "$";
        if (value.isEmpty()) {
            pass.setError(getString(R.string.error_empty_field));
            return false;
        } else if (!value.matches(passwordVal)) {
            pass.setError(getString(R.string.error_weak_password));
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
        if (value1 != value2) {
            pass2.setError(getString(R.string.error_password_are_not_match));
            return false;
        } else {
            pass2.setError(null);
            pass2.setErrorEnabled(false);
            return true;
        }
    }

}