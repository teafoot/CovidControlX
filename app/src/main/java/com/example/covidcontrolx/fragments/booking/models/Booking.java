package com.example.covidcontrolx.fragments.booking.models;

import com.google.type.DateTime;

import java.util.List;

public class Booking {
    private String date;
    private List<Timeslot> timeslotList;

    public Booking() {
    }
    public Booking(List<Timeslot> timeslotList) {
        this.timeslotList = timeslotList;
    }

    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return this.date;
    }

    public void setTimeslotList(List<Timeslot> timeslotList) {
        this.timeslotList = timeslotList;
    }
    public List<Timeslot> getTimeslotList() {
        return this.timeslotList;
    }
}
