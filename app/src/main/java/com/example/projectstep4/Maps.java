package com.example.projectstep4;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.projectstep4.databinding.ActivityMaps2Binding;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMaps2Binding binding;
    double startingPosLong = 0, startingPosLat = 0, endingPosLat = 0, endingPositionLong = 0;
    ArrayList<Double> lsit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        ArrayList<Double> lsit = (ArrayList<Double>) intent.getSerializableExtra("latlong");

        startingPosLong = lsit.get(0);
        startingPosLat = lsit.get(0);
        endingPosLat = lsit.get(0);
        endingPositionLong = lsit.get(0);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent intent = getIntent();

        ArrayList<Double> list = (ArrayList<Double>) intent.getSerializableExtra("latlong");

        startingPosLong = list.get(0);
        startingPosLat = list.get(1);
        endingPosLat = list.get(2);
        endingPositionLong = list.get(3);

        LatLng start = new LatLng(startingPosLat, startingPosLong);
        LatLng end = new LatLng(endingPosLat, endingPositionLong);

        mMap.addMarker(new MarkerOptions().position(start).title("Start"));
        mMap.addMarker(new MarkerOptions().position(end).title("finish"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
    }
    public ArrayList<Double> getPos(){
        return  lsit;
    }
}