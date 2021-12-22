package com.example.covidcontrolx.fragments.booking.models;

import java.util.List;

public class Hospital {
    private String hospital_id;
    private String name;
    private String phone;
    private String image;
    private Location location;
    private Services services;
    private List<Booking> bookings;

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }
    public String getHospital_id() {
        return this.hospital_id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhone() {
        return this.phone;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getImage() {
        return this.image;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    public Location getLocation() {
        return this.location;
    }

    public void setServices(Services services) {
        this.services = services;
    }
    public Services getServices() {
        return this.services;
    }

    public void setBookings(List<Booking> bookings){
        this.bookings = bookings;
    }
    public List<Booking> getBookings(){
        return this.bookings;
    }
}

