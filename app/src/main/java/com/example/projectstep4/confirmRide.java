package com.example.projectstep4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class confirmRide extends AppCompatActivity {
    TextView feeBreakdown;

    TextView costTextView;
    Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_ride);
        feeBreakdown = findViewById(R.id.feeBreakdown);
        backButton = findViewById(R.id.backButton);
        costTextView = findViewById(R.id.rideCost);
        Intent intent = getIntent();
        double startLat = intent.getExtras().getDouble("startLat");
        double startLong = intent.getExtras().getDouble("startLong");
        double endLat = intent.getExtras().getDouble("endLat");
        double endLong = intent.getExtras().getDouble("endLong");

        double distanceBetweenStartAndEnd = Math.acos(Math.sin(Math.toRadians(startLat))*Math.sin(Math.toRadians(endLat))+Math.cos(Math.toRadians(startLat))*Math.cos(Math.toRadians(endLat))*Math.cos(Math.toRadians(endLong)-Math.toRadians(startLong)))*6371;
        double cost = distanceBetweenStartAndEnd * 1.5;
        double endCost = Math.round(cost*100.0)/100.0;
        double taxes = 0.13* endCost;
        double total = taxes + endCost + 2;
        feeBreakdown.setText("\n" + "Service Fee: $2.00" + "\n" + "Ride Cost: $" + endCost + "\n" + "Taxes: $" + Math.round(taxes*100.0)/100.0);
        costTextView.setText(" $" + Math.round(total*100.0)/100.0 + " ");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}