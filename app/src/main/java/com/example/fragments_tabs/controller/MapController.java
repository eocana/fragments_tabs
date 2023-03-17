package com.example.fragments_tabs.controller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.fragments_tabs.model.MapModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

public class MapController implements OnMapReadyCallback {

    private MapModel mapModel;
    private GoogleMap googleMap;

    private Context context;

    public MapController(Context context) {
        this.context = context;
    }


    public void setMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (googleMap != null) {
            onMapReady(googleMap);
        }else{
            System.out.println("ERROR: googleMap esta vacío verificar MapController.java y MapFragment.java");
        }
    }

    public void searchCity(String cityName) {
            Geocoder geocoder = new Geocoder(context);

            try {
                List<Address> addresses = geocoder.getFromLocationName(cityName, 1);
                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
                    try{
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                        System.out.println("ERROR: googleMap es null no deberia ser así");
                    }

                } else {
                    Toast.makeText(context, "Ciudad no encontrada o especifica el país", Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
    }

    public void getCurrentLocation() {

        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
                } else {
                    Toast.makeText(context, "Ubicación no disponible", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addMarker(LatLng position, String title) {
        if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions().position(position).title(title));
        }
    }

    public void animateCamera(LatLng position, float zoom) {
        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
    }
}