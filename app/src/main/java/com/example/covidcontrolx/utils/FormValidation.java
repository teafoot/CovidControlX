package com.example.covidcontrolx.utils;

import android.util.Patterns;

public class FormValidation {
    public static boolean validateEmail(String email){
        if(email.isEmpty()){
            return false;
        }else return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean validatePassword(String password){
        if(password.isEmpty()){
            return false;
        }else return password.length() >= 8;
    }

    public static boolean validatePasswordRepeat(String password, String passwordRepeat){
        if(passwordRepeat.isEmpty()){
            return false;
        }else return passwordRepeat.equals(password);
    }
}
