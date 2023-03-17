package com.example.fragments_tabs.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.fragments_tabs.R;
import com.example.fragments_tabs.controller.MapController;
import com.example.fragments_tabs.model.MapModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Map;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private ActivityResultLauncher<String[]> requestPermissionLauncher;
    private MapController mapController;
    private MapModel mapModel;
    private GoogleMap googleMap;

    private Button buttonView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        buttonView = view.findViewById(R.id.search_button);


        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                new ActivityResultCallback<Map<String, Boolean>>() {
                    @Override
                    public void onActivityResult(Map<String, Boolean> result) {
                        if (result.containsValue(false)) {
                            // Los permisos no fueron concedidos
                            Toast.makeText(getContext(), "Debes conceder los permisos", Toast.LENGTH_SHORT).show();
                        } else {
                            // Los permisos fueron concedidos
                            Toast.makeText(getContext(), "Permiso concedido", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            requestPermissionLauncher.launch(permissions);
        }
        // Ya se tienen los permisos necesarios, puedes ejecutar la lógica necesaria para acceder a la ubicación

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        EditText searchEditText = view.findViewById(R.id.search_text);

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = searchEditText.getText().toString();
                mapController.searchCity(cityName);
            }
        });



        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String cityName = v.getText().toString();
                    mapController.searchCity(cityName);
                    return true;
                }
                return false;
            }
        });
        return view;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        mapController.setMap(googleMap); // Llama a setMap() para inicializar la variable googleMap
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mapController = new MapController(context);

    }

}
