package com.example.projectstep4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class availableDriver extends AppCompatActivity {
    TextView driverName;
    String[] driverInfo;
    TextView rating;
    TextView numRides;
    TextView cost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_driver);
        driverName =findViewById(R.id.driverName);
        rating = findViewById(R.id.driverRating);
        numRides = findViewById(R.id.driverNumRides);
        cost = findViewById(R.id.rideCost);
        Intent intent = getIntent();
        String info = intent.getExtras().getString("usernameDriver");
        driverInfo = info.split(" ");

        driverName.setText(driverInfo[0]);
        rating.setText(driverInfo[1]);
        numRides.setText(driverInfo[2]);

    }
}