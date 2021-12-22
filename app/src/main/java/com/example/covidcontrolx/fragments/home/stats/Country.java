package com.example.covidcontrolx.fragments.home.stats;

public class Country {
    String infected;
    String tested;
    String recovered;
    String deceased;
    String country;
    String moreData;
    String historyData;
    String sourceUrl;
    String lastUpdatedApify;
    String flag;

    public Country(String infected, String tested, String recovered, String deceased, String country, String moreData, String historyData, String sourceUrl, String lastUpdatedApify) {
        this.infected = infected;
        this.tested = tested;
        this.recovered = recovered;
        this.deceased = deceased;
        this.country = country;
        this.moreData = moreData;
        this.historyData = historyData;
        this.sourceUrl = sourceUrl;
        this.lastUpdatedApify = lastUpdatedApify;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getInfected() {
        return infected;
    }

    public String getTested() {
        return tested;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getDeceased() {
        return deceased;
    }

    public String getCountry() {
        return country;
    }

    public String getMoreData() {
        return moreData;
    }

    public String getHistoryData() {
        return historyData;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getLastUpdatedApify() {
        return lastUpdatedApify;
    }
}