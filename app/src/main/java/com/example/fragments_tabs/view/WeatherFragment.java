package com.example.fragments_tabs.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fragments_tabs.R;
import com.example.fragments_tabs.controller.WeatherController;
import com.example.fragments_tabs.model.WeatherData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class WeatherFragment extends Fragment {
    private WeatherController weatherController;
    private EditText etCityName;
    private Button btnSearch;
    private LineChart lineChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        weatherController = new WeatherController(getContext());

        etCityName = view.findViewById(R.id.et_city_name);
        btnSearch = view.findViewById(R.id.btn_search);
        lineChart = view.findViewById(R.id.line_chart);

        btnSearch.setOnClickListener(v -> {
            String cityName = etCityName.getText().toString().trim();
            fetchWeatherData(cityName);
        });

        return view;
    }

    private void fetchWeatherData(String cityName) {
        weatherController.fetchWeatherData(cityName, new WeatherController.WeatherCallback() {
            @Override
            public void onSuccess(List<WeatherData> weatherDataList) {
                List<Entry> entries = new ArrayList<>();
                List<String> xAxisLabels = new ArrayList<>();
                for (int i = 0; i < weatherDataList.size(); i++) {
                    WeatherData weatherData = weatherDataList.get(i);
                    entries.add(new Entry(i, (float) weatherData.getTemperature()));
                    xAxisLabels.add(weatherData.getDate());
                }

                LineDataSet dataSet = new LineDataSet(entries, "Temperatura media(°C)");
                LineData lineData = new LineData(dataSet);
                lineChart.setData(lineData);

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
                xAxis.setLabelRotationAngle(-45);

                lineChart.invalidate();
            }

            @Override
            public void onError(Exception error) {
                Toast.makeText(getContext(), "No existe la ciudad o especifica el país", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
