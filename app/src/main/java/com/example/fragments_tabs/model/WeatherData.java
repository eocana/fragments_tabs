package com.example.fragments_tabs.model;

public class WeatherData {

    private String date;
    private String mLocation;
    private double mTemperature;
    private String mWeatherDescription;

    public WeatherData(String location, double temperature, String weatherDescription) {
        mLocation = location;
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
