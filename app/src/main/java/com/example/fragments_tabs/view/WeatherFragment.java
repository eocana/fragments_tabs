package com.example.fragments_tabs.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fragments_tabs.R;
import com.example.fragments_tabs.controller.WeatherController;
import com.example.fragments_tabs.model.WeatherData;

public class WeatherFragment extends Fragment {
    private WeatherController weatherController;
    private EditText etCityName;
    private Button btnSearch;
    private TextView tvWeatherInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        weatherController = new WeatherController(getContext());

        etCityName = view.findViewById(R.id.et_city_name);
        btnSearch = view.findViewById(R.id.btn_search);
        tvWeatherInfo = view.findViewById(R.id.tv_weather_info);

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
