package com.example.googlemapproject;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.googlemapproject.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LatLng origin;
    private LatLng destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Set origin and destination coordinates here
        origin = new LatLng(49.939352, -119.394854);
        destination = new LatLng(49.88073247544819, -119.42845577887032);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add markers for origin and destination on the map
        mMap.addMarker(new MarkerOptions().position(origin).title("Start"));
        mMap.addMarker(new MarkerOptions().position(destination).title("Destination"));

        // Move camera to show both markers with explicit map dimensions
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(origin);
        builder.include(destination);
        LatLngBounds bounds = builder.build();
        int padding = 100; // Padding in pixels

        // Get the width and height of the map view
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        // Adjust camera with specified map dimensions
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.moveCamera(cameraUpdate);

        // Calculate ETA between origin and destination, as well as draw lines and the markers
        drawMap();
    }


    private void drawMap() {
        // Calling Directions API
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                origin.latitude + "," + origin.longitude +
                "&destination=" + destination.latitude + "," + destination.longitude +
                "&key=AIzaSyD2cajVsU9iTo7cjq0UTRMHwL_174k6NJc";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray routes = jsonObject.getJSONArray("routes");
                        JSONObject route = routes.getJSONObject(0);
                        JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                        String encodedPolyline = overviewPolyline.getString("points");

                        // Decode polyline and draw on the map
                        List<LatLng> decodedPath = PolyUtil.decode(encodedPolyline);
                        PolylineOptions polylineOptions = new PolylineOptions().addAll(decodedPath).color(Color.BLUE);
                        mMap.addPolyline(polylineOptions);
                        JSONObject leg = route.getJSONArray("legs").getJSONObject(0);
                        JSONObject duration = leg.getJSONObject("duration");

                        // Get the duration text (ETA) from the JSON response
                        String durationText = duration.getString("text");

                        // Update the TextView with the estimated time
                        TextView estimatedTimeTextView = findViewById(R.id.estimatedTimeTextView);
                        estimatedTimeTextView.setText(String.format("EST: %s", durationText));

                        // Move camera to show both markers after receiving response
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(origin);
                        builder.include(destination);
                        LatLngBounds bounds = builder.build();
                        int padding = 100; // Padding
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                        // Check if the map is ready before moving the camera
                        if (mMap != null) {
                            mMap.moveCamera(cameraUpdate);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error response
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(stringRequest);
    }

}