package com.example.projectstep4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

public class CreateCarpool extends AppCompatActivity {
    private EditText startLoc, endLoc, startTime, seatsAvailable;
    private RadioGroup genderGroup;
    private CheckBox disabilityBox;
    HashMap<String, String> clientMap;
    double startingPosLong = 0, startingPosLat = 0, endingPosLat = 0, endingPositionLong = 0;
    int hour,minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_carpool);

        //find start and end location boxes
        startLoc = findViewById(R.id.startLat);

        endLoc = findViewById(R.id.endLong);

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
                String start = startLoc.getText().toString();
                if(start.equals("")){
                    Toast.makeText(CreateCarpool.this, "Please enter a start location", Toast.LENGTH_SHORT).show();
                    return;
                }

                String end = endLoc.getText().toString();
                if(end.equals("")){
                    Toast.makeText(CreateCarpool.this, "Please enter a end location", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean itWorks = true;//isNumeric(startLat) && isNumeric(startLong) && isNumeric(endLong) && isNumeric(endLat);

                if(itWorks){
                    String deptTime = startTime.getText().toString();
                    if(deptTime.equals("")){
                        Toast.makeText(CreateCarpool.this, "Please enter a departure time", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(seatsAvailable.getText().toString().equals("")){
                        Toast.makeText(CreateCarpool.this, "Please enter a number of seats available", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int availableSeats = Integer.parseInt(seatsAvailable.getText().toString());

                    boolean disability = disabilityBox.isChecked();
                    String gender = "";

                    if(genderGroup.getCheckedRadioButtonId() != -1){
                        RadioButton butt = findViewById(genderGroup.getCheckedRadioButtonId());

                        gender = butt.getText().toString();
                    }

                    HashMap<String, Object> rideInformation = new HashMap<>();

                    //populate the map with all the information about this ride
                    rideInformation.put("depart", deptTime);
                    rideInformation.put("start",start);
                    rideInformation.put("end",end);
                    rideInformation.put("seats", availableSeats);
                    rideInformation.put("disability", disability ? "yes":"no");
                    rideInformation.put("gender", gender);
                    rideInformation.put("uid",clientMap.get("uid"));


                    int numRides = Integer.parseInt(clientMap.get("numberOfRides"));
                    numRides += 1;

                    //write to FB
                    writeToFB(clientMap.get("uid"), start, end, deptTime, clientMap.get("rating"), disability ? "yes" : "no", String.valueOf(numRides));

                    Intent intent = new Intent(CreateCarpool.this, CarpoolCreated.class);
                    intent.putExtra("ride info", rideInformation);      //pass the ride information map
                    intent.putExtra("client map", clientMap);           //pass the client information map

                    if(getStartLatLong(start)){
                        if(getEndLatLong(end)){
                            ArrayList<Double> list = new ArrayList<>();

                            list.add(startingPosLong);
                            list.add(startingPosLat);
                            list.add(endingPosLat);
                            list.add(endingPositionLong);

                            intent.putExtra("latlong", list);
                            intent.putExtra("info",clientMap);
                            startActivity(intent);
                        }
                    }



                }else{
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, "Please enter a valid latitude and longitude for both start and end positions", Toast.LENGTH_SHORT);
                    toast.show();
                }
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
    public void writeToFB(String username, String start, String end, String departureTime, String rating, String wheelchair, String number){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference("carpools");
        DatabaseReference newRider = root.child(username);

        //set the username as a new child of the root
        newRider.child("start").setValue(start);
        newRider.child("end").setValue(end);
        newRider.child("depart").setValue(departureTime);
        newRider.child("rating").setValue(rating);
        newRider.child("disability").setValue(wheelchair);
        newRider.child("numberOfRides").setValue(number);
        newRider.child("riders").setValue("");
        newRider.child("moneyEarned").setValue(0);
        root = FirebaseDatabase.getInstance().getReference("driver");
        DatabaseReference update = root.child(username);
        update.child("numberOfRides").removeValue();
        update.child("numberOfRides").setValue(number);
    }
    public boolean getStartLatLong(String s){
        Geocoder geocoder = new Geocoder(CreateCarpool.this);
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocationName(s,1);
            if(addressList.size() != 0){
                startingPosLat = addressList.get(0).getLatitude();
                startingPosLong = addressList.get(0).getLongitude();
            }
            else{
                Toast.makeText(this, "Please enter a valid start location", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
    public boolean getEndLatLong(String s){
        Geocoder geocoder = new Geocoder(CreateCarpool.this);
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocationName(s,1);
            if(addressList.size() != 0){
                endingPosLat = addressList.get(0).getLatitude();
                endingPositionLong = addressList.get(0).getLongitude();
            }
            else{
                Toast.makeText(this, "Please enter a valid end location", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}