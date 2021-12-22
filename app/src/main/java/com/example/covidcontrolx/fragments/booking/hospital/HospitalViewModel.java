package com.example.covidcontrolx.fragments.booking.hospital;

import com.example.covidcontrolx.fragments.booking.models.Hospital;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HospitalViewModel extends ViewModel {
    private final MutableLiveData<Hospital> selectedHospital = new MutableLiveData<>();
    private final MutableLiveData<List<Hospital>> hospitals = new MutableLiveData<>();
    private final MutableLiveData<Boolean> filterActive = new MutableLiveData<>();
    private final MutableLiveData<List<Hospital>> filterHospitals = new MutableLiveData<>();
    private final MutableLiveData<String> selectedService = new MutableLiveData<>();

    public void setSelectedHospital(Hospital hospital) {
        selectedHospital.setValue(hospital);
    }
    public LiveData<Hospital> getSelectedHospital() {
        return selectedHospital;
    }

    public void setHospitals(List<Hospital> hospitals) {
        this.hospitals.setValue(hospitals);
    }
    public LiveData<List<Hospital>> getHospitals() {
        return hospitals;
    }

    public void setFilterActive(boolean isActive) {
        filterActive.setValue(isActive);
    }
    public LiveData<Boolean> getFilterActive() {
        return filterActive;
    }
    public void setFilterHospitals(List<Hospital> filterHospitals) {
        this.filterHospitals.setValue(filterHospitals);
    }
    public LiveData<List<Hospital>> getFilterHospitals() {
        return filterHospitals;
    }

    public void setSelectedService(String service) {
        selectedService.setValue(service);
    }
    public LiveData<String> getService() {
        return selectedService;
    }
}
