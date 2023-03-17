package com.example.fragments_tabs.controller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fragments_tabs.model.WeatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class WeatherController {
    private static final String API_KEY = "c2ad606f8bb650078c66b77956b99615";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast?q=";
    private Context context;

    public WeatherController(Context context) {
        this.context = context;
    }

    public interface WeatherCallback {
        void onSuccess(List<WeatherData> weatherDataList);
        void onError(Exception error);
    }

    public void fetchWeatherData(String cityName, WeatherCallback callback) {
        String url = BASE_URL + cityName + ",gb&mode=json&appid=" + API_KEY + "&units=metric";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        List<WeatherData> weatherDataList = parseWeatherData(response);
                        callback.onSuccess(weatherDataList);
                    } catch (JSONException e) {
                        callback.onError(e);
                    }
                },
                callback::onError
        );
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);
    }

    private List<WeatherData> parseWeatherData(JSONObject response) throws JSONException {
        JSONArray list = response.getJSONArray("list");
        Map<String, List<Double>> dailyTemps = new LinkedHashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (int i = 0; i < list.length(); i++) {
            JSONObject item = list.getJSONObject(i);
            long dt = item.getLong("dt") * 1000;
            Date date = new Date(dt);
            String dateString = dateFormat.format(date);
            double temperature = item.getJSONObject("main").getDouble("temp");

            if (!dailyTemps.containsKey(dateString)) {
                dailyTemps.put(dateString, new ArrayList<>());
            }
            dailyTemps.get(dateString).add(temperature);
        }

        List<WeatherData> weatherDataList = new ArrayList<>();
        for (Map.Entry<String, List<Double>> entry : dailyTemps.entrySet()) {
            double avgTemp = calculateAverage(entry.getValue());
            weatherDataList.add(new WeatherData(entry.getKey(), avgTemp, ""));
            if (weatherDataList.size() >= 5) {
                break;
            }
        }

        return weatherDataList;
    }

    private double calculateAverage(List<Double> values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.size();
    }
}
