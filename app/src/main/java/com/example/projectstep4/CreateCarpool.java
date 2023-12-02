package com.example.projectstep4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import java.util.HashMap;
import java.util.Locale;

public class CreateCarpool extends AppCompatActivity {
    private EditText startLocation, endLocation, startTime, seatsAvailable;
    private RadioGroup genderGroup;
    private CheckBox disabilityBox;
    HashMap<String, String> clientMap;
    int hour,minute;
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
        startTime = findViewById(R.id.timeBox);
        seatsAvailable = findViewById(R.id.seatsAvailableBox);
        //find disability checkbox
        disabilityBox = findViewById(R.id.disabilityCheckbox);

        //next button
        Button next = findViewById(R.id.nextButton);

        Intent intent = getIntent();
        clientMap = (HashMap<String, String>) intent.getSerializableExtra("info");

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker(getCurrentFocus());
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startLoc = startLocation.getText().toString();
                String endLoc = endLocation.getText().toString();
                int deptTime = Integer.parseInt(startTime.getText().toString());
                int availableSeats = Integer.parseInt(seatsAvailable.getText().toString());
                boolean disability = disabilityBox.isChecked();
                String gender = "";

                if(genderGroup.getCheckedRadioButtonId() != -1){
                    RadioButton butt = findViewById(genderGroup.getCheckedRadioButtonId());

                    gender = butt.getText().toString();
                }else{
                    gender = "all";
                }

                HashMap<String, Object> rideInformation = new HashMap<>();

                //populate the map with all the information about this ride
                rideInformation.put("start", startLoc);
                rideInformation.put("end", endLoc);
                rideInformation.put("depart", deptTime);
                rideInformation.put("seats", availableSeats);
                rideInformation.put("disability", disability ? "yes":"no");
                rideInformation.put("gender", gender);


                Intent intent = new Intent(CreateCarpool.this, CarpoolConfirmation.class);
                intent.putExtra("ride info", rideInformation);
                intent.putExtra("client map", clientMap);

                startActivity(intent);
            }
        });

    }
    public void timePicker(View v) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minuteOfDay) {
                hour = hourOfDay;
                minute = minuteOfDay;
                startTime.setText(String.format(Locale.getDefault(), "%02d:%02d",hour,minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}