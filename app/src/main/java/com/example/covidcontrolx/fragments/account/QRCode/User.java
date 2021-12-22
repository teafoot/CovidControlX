package com.example.covidcontrolx.fragments.account.QRCode;


import java.util.Date;

public class User {
    String fName;
    int nationalId;
    Date dob;

    public User(){

    }

    public User(String fName, int nationalId, Date dob) {
        this.fName = fName;
        this.nationalId = nationalId;
        this.dob = dob;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public int getNationalId() {
        return nationalId;
    }

    public void setNationalId(int nationalId) {
        this.nationalId = nationalId;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
}



























