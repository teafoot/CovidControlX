package com.example.covidcontrolx.fragments.booking.vaccine;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class SelectVaccineViewModel extends AndroidViewModel {
    private String vaccineTypeSelected;
    private long vaccineQuantity;

    public SelectVaccineViewModel(@NonNull Application application) {
        super(application);
    }

    public String getVaccineTypeSelected() {
        return vaccineTypeSelected;
    }
    public void setVaccineTypeSelected(String vaccineTypeSelected) {
        this.vaccineTypeSelected = vaccineTypeSelected;
    }
    public long getVaccineQuantity() {
        return vaccineQuantity;
    }
    public void setVaccineQuantity(long quantity) {
        this.vaccineQuantity = quantity;
    }
}