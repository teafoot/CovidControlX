package com.example.covidcontrolx.fragments.booking.models;

public class Patient_certificate {
    private String national_id;
    private String first_name;
    private String last_name;
    private String date_of_birth;
    private String phone_number;
    private String email;
    private String vaccine_type;
    private String vaccine_shot;
    private String vaccination_location;
    private String vaccination_date;
    private String qr_code;

    public Patient_certificate() {

    }

    public Patient_certificate(String national_id, String first_name, String last_name, String date_of_birth, String email, String vaccine_type, String vaccine_shot, String vaccination_location, String vaccination_date, String qr_code) {
        this.national_id = national_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.email = email;
        this.vaccine_type = vaccine_type;
        this.vaccine_shot = vaccine_shot;
        this.vaccination_location = vaccination_location;
        this.vaccination_date = vaccination_date;
        this.qr_code = qr_code;
    }

    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }
    public String getNational_id() {
        return this.national_id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public String getFirst_name() {
        return this.first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    public String getLast_name() {
        return this.last_name;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
    public String getDate_of_birth() {
        return this.date_of_birth;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return this.email;
    }

    public void setVaccine_type(String vaccine_type) {
        this.vaccine_type = vaccine_type;
    }
    public String getVaccine_type() {
        return this.vaccine_type;
    }

    public void setVaccine_shot(String vaccine_shot) {
        this.vaccine_shot = vaccine_shot;
    }
    public String getVaccine_shot() {
        return this.vaccine_shot;
    }

    public void setVaccination_location(String vaccination_location) {
        this.vaccination_location = vaccination_location;
    }
    public String getVaccination_location() {
        return this.vaccination_location;
    }

    public void setVaccination_date(String vaccination_date) {
        this.vaccination_date = vaccination_date;
    }
    public String getVaccination_date() {
        return this.vaccination_date;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }
    public String getQr_code() {
        return this.qr_code;
    }

    public String getPhone_number() {
        return phone_number;
    }
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
