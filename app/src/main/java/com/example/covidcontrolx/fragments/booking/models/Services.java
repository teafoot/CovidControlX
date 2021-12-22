package com.example.covidcontrolx.fragments.booking.models;

import java.util.List;

public class Services {
    private List<Vaccination> vaccination;
    private Pcr_test pcr_test;
    private Rapid_test rapid_test;

    public Services(List<Vaccination> vaccination, Pcr_test pcr_test, Rapid_test rapid_test) {
        this.vaccination = vaccination;
        this.pcr_test = pcr_test;
        this.rapid_test = rapid_test;
    }

    public void setVaccination(List<Vaccination> vaccination) {
        this.vaccination = vaccination;
    }
    public List<Vaccination> getVaccination() {
        return this.vaccination;
    }
    public void setPcr_test(Pcr_test pcr_test) {
        this.pcr_test = pcr_test;
    }
    public Pcr_test getPcr_test() {
        return this.pcr_test;
    }
    public void setRapid_test(Rapid_test rapid_test) {
        this.rapid_test = rapid_test;
    }
    public Rapid_test getRapid_test() {
        return this.rapid_test;
    }
}
