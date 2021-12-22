package com.example.covidcontrolx.fragments.booking.appointment.UserDetails;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class UserInfoViewModel extends AndroidViewModel {

    public String fullName, idNum, dateOfBirth, phoneNum, email, doseNum;
    public boolean confirmCheckBox;

    public UserInfoViewModel(Application application) {
        super(application);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public boolean isConfirmCheckBox() {
        return confirmCheckBox;
    }

    public String getDoseNum() {
        return doseNum;
    }

    public void setDoseNum(String doseNum) {
        this.doseNum = doseNum;
    }

    public void setConfirmCheckBox(boolean confirmCheckBox) {
        this.confirmCheckBox = confirmCheckBox;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return this.email;
    }
}