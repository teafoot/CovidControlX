package com.example.covidcontrolx.fragments.account;

import android.app.Application;

import com.example.covidcontrolx.fragments.booking.models.Patient_certificate;

import androidx.lifecycle.AndroidViewModel;

public class DownloadCertificateViewModel extends AndroidViewModel {
    private Patient_certificate patientCertificate;

    public DownloadCertificateViewModel(Application application) {
        super(application);
    }

    public Patient_certificate getPatientCertificate() {
        return patientCertificate;
    }
    public void setPatientCertificate(Patient_certificate patientCertificate) {
        this.patientCertificate = patientCertificate;
    }
}