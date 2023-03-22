package com.example.fragments_tabs.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragments_tabs.R;
import com.example.fragments_tabs.controller.WeatherController;
import com.example.fragments_tabs.model.WeatherData;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class WeatherFragment extends Fragment {
    private WeatherController weatherController;
    private EditText etCityName;
    private Button btnSearch;
    private TextView tvWeatherInfo;
    private ImageView iconWeather;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        weatherController = new WeatherController(getContext());

        etCityName = view.findViewById(R.id.et_city_name);
        btnSearch = view.findViewById(R.id.btn_search);
        tvWeatherInfo = view.findViewById(R.id.tv_weather_info);
        iconWeather = view.findViewById(R.id.iconWeather);

        btnSearch.setOnClickListener(v -> {
            String cityName = etCityName.getText().toString().trim();
            fetchWeatherData(cityName);
        });

        return view;
    }


    private void fetchWeatherData(String cityName) {
        weatherController.fetchWeatherData(cityName, new WeatherController.WeatherCallback() {
            @Override
            public void onSuccess(WeatherData weatherData) {

                if (weatherData.getWeatherDescription().contains("clouds")) {
                    iconWeather.setImageResource(R.drawable.ic_cloud);
                } else if (weatherData.getWeatherDescription().contains("rain")) {
                    iconWeather.setImageResource(R.drawable.ic_rain);
                } else if (weatherData.getWeatherDescription().contains("snow")) {
                    iconWeather.setImageResource(R.drawable.ic_snow);
                } else {
                    iconWeather.setImageResource(R.drawable.ic_sun);
                }

                String info = "City: " + weatherData.getLocation() + "\nTemperature: " + weatherData.getTemperature() + "°C\nDescription: " + weatherData.getWeatherDescription();
                tvWeatherInfo.setText(info);
            }

            @Override
            public void onError(Exception error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
