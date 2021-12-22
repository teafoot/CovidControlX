package com.example.covidcontrolx.fragments.booking.appointment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class AppointmentViewModel extends AndroidViewModel {

    String date, startTime;

    public AppointmentViewModel(@NonNull Application application) {
        super(application);
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
