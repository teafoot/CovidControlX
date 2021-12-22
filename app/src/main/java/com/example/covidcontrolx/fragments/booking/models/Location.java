package com.example.covidcontrolx.fragments.booking.models;

public class Location {
    private String street_address;

    private String city;

    private String county;

    private String state;

    private String zip_code;

    private String country_code;

    private double lat;

    private double lng;

    public Location(String street_address, String city, String county, String state, String zip_code, String country_code, double lat, double lng) {
        this.street_address = street_address;
        this.city = city;
        this.county = county;
        this.state = state;
        this.zip_code = zip_code;
        this.country_code = country_code;
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public String toString() {
        String[] location = {street_address, city, state, zip_code, country_code};
        return String.join(", ", location);
//        return "Location{" +
//                "street_address='" + street_address + '\'' +
//                ", city='" + city + '\'' +
//                ", county='" + county + '\'' +
//                ", state='" + state + '\'' +
//                ", zip_code='" + zip_code + '\'' +
//                ", country_code='" + country_code + '\'' +
//                ", lat='" + lat + '\'' +
//                ", lng='" + lng + '\'' +
//                '}';
    }

    public void setStreet_address(String street_address) {
        this.street_address = street_address;
    }

    public String getStreet_address() {
        return this.street_address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return this.city;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCounty() {
        return this.county;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getZip_code() {
        return this.zip_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getCountry_code() {
        return this.country_code;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLng() {
        return this.lng;
    }
}
