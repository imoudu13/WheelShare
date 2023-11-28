package com.example.projectstep4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

public class CreateCarpool extends AppCompatActivity {
    private EditText startLocation, endLocation, startTime, seatsAvailable;
    private RadioGroup genderGroup;
    private CheckBox disabilityBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_carpool);

        //find start and end location boxes
        startLocation = findViewById(R.id.startBox);
        endLocation = findViewById(R.id.endBox);

        //find gender Radio group
        genderGroup = findViewById(R.id.genderToOfferRideGroup);

        //find departure time and seats view
        startTime = findViewById(R.id.deptTimeBox);

        //find disability checkbox
        disabilityBox = findViewById(R.id.disabilityCheckbox);

        //next button
        Button next = findViewById(R.id.nextButton);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startLoc = startLocation.getText().toString();
                String endLoc = endLocation.getText().toString();

            }
        });
    }
}