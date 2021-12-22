package com.example.covidcontrolx.fragments.booking.models;

public class Timeslot {
    private int timeslot_id;
    private String timestart;
    private String timeend;
    private String status;
    private String service_type;
    private Patient_certificate patient_certificate;

    public Timeslot() {
    }

    public Timeslot(int timeslot_id, String timestart, String timeend, String status, String service_type, Patient_certificate patient_certificate) {
        this.timeslot_id = timeslot_id;
        this.timestart = timestart;
        this.timeend = timeend;
        this.status = status;
        this.service_type = service_type;
        this.patient_certificate = patient_certificate;
    }

    public void setTimeslot_id(int timeslot_id) {
        this.timeslot_id = timeslot_id;
    }
    public int getTimeslot_id() {
        return this.timeslot_id;
    }

    public void setTimestart(String timestart) {
        this.timestart = timestart;
    }
    public String getTimestart() {
        return this.timestart;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }
    public String getTimeend() {
        return this.timeend;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return this.status;
    }

    public void setService_type(String service_type){
        this.service_type = service_type;
    }
    public String getService_type(){
        return this.service_type;
    }

    public void setPatient_certificate(Patient_certificate patient_certificate){
        this.patient_certificate = patient_certificate;
    }
    public Patient_certificate getPatient_certificate(){
        return this.patient_certificate;
    }
}
