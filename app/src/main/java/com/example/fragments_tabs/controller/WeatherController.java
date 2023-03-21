package com.example.fragments_tabs.controller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fragments_tabs.model.WeatherData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class WeatherController {

    private Context context;
    private static final String API_KEY = "c2ad606f8bb650078c66b77956b99615";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    private static final String BASE_URL2 = "https://pro.openweathermap.org/data/2.5/forecast/hourly?q=";

    public WeatherController(Context context) {
        this.context = context;
    }

    public interface WeatherCallback {
        void onSuccess(WeatherData weatherData);
        void onSuccess2(List<WeatherData> weatherData);
        void onError(Exception error);
    }

    public void fetchWeatherData(String cityName, WeatherCallback callback) {
        String url = BASE_URL + cityName + "&appid=" + API_KEY + "&units=metric";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        System.out.println("Response :: "+response);
                        WeatherData weatherData = parseWeatherData(response);
                        callback.onSuccess(weatherData);
                    } catch (JSONException e) {
                        callback.onError(e);
                    }
                },
                callback::onError
        );
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);
    }

    private WeatherData parseWeatherData(JSONObject response) throws JSONException {
        String cityName = response.getString("name");
        double temperature = response.getJSONObject("main").getDouble("temp");
        String description = response.getJSONArray("weather").getJSONObject(0).getString("description");

        return new WeatherData(cityName, temperature, description);
    }
}

