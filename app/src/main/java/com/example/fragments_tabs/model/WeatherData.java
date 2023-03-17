package com.example.fragments_tabs.model;

public class WeatherData {

    private String date;
    private String mLocation;
    private double mTemperature;
    private String mWeatherDescription;

    public WeatherData(String date, double temperature, String weatherDescription) {
        this.date = date;
        mTemperature = temperature;
        mWeatherDescription = weatherDescription;

    }

    public String getLocation() {
        return mLocation;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public String getWeatherDescription() {
        return mWeatherDescription;
    }

    public String getDate() {
        return date;
    }
}
