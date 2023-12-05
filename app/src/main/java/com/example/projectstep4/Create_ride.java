package com.example.projectstep4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Create_ride extends AppCompatActivity {
    EditText setTime;
    int hour,minute;
    EditText startLocation;
    EditText endLocation;
    CheckBox accessibilityCheck;
    Button findRidesButton;
    ListView driversListView;
    ArrayList<String> driversArrayList;
    private DatabaseReference root;
    double startLat;
    double startLong;
    double endLat;
    double endLong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride);
        setTime = findViewById(R.id.setTimeCreateRide);
        startLocation = findViewById(R.id.startBoxCreateRide);
        endLocation = findViewById(R.id.endBoxCreateRide);
        accessibilityCheck = findViewById(R.id.wheelChairCheckBox);
        findRidesButton = findViewById(R.id.loadDriversButton);
        driversListView = findViewById(R.id.driversListView);

        root = FirebaseDatabase.getInstance().getReference("carpools");
        driversArrayList = new ArrayList<>();
        driversListView.setVisibility(View.INVISIBLE);


        findRidesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get info
                String start = startLocation.getText().toString();
                String end = endLocation.getText().toString();
                String departTime = setTime.getText().toString();
                if(checkInfo(start,end,departTime)){
                    if(getStartLatLong(start)){
                        if(getEndLatLong(end)){
                            displayDrivers();
                        }
                    }


                }
            }
        });
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker(getCurrentFocus());
            }
        });
            driversListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String usernameDriver = (String) parent.getItemAtPosition(position);
                    Intent intent = new Intent(Create_ride.this, availableDriver.class);
                    intent.putExtra("usernameDriver", usernameDriver);
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
                setTime.setText(String.format(Locale.getDefault(), "%02d:%02d",hour,minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public boolean checkInfo(String start, String end, String time){
        if(start.equals("")){
            Toast.makeText(this, "Please Submit a Start Location", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(end.equals("")){
            Toast.makeText(this, "Please Submit a End Location", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(time.equals("")){
            Toast.makeText(this, "Please Submit a Departure Time", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void displayDrivers(){
        OnCompleteListener<DataSnapshot> onValuesFetched = new
                OnCompleteListener<DataSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task)
                    {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                            Toast.makeText(Create_ride.this, "Error Getting Data", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            driversArrayList = new ArrayList<>();
                            DataSnapshot receivedValue = task.getResult();
                            for(DataSnapshot node: receivedValue.getChildren())
                            {
                                driversArrayList.add(node.getKey() + " " + node.child("rating").getValue().toString() + "/5" + " " + node.child("numberOfRides").getValue().toString() + " " + node.child("start").getValue().toString() + " " + node.child("end").getValue().toString());
                                //driversArrayList.add(node.child("firstName").getValue().toString());
                            }

                        }
                        // tempMap.put("firstName", node.child("firstName").getValue().toString());
                        ArrayAdapter arrayAdapter = new ArrayAdapter(Create_ride.this, android.R.layout.simple_list_item_1, driversArrayList);
                        driversListView.setAdapter(arrayAdapter);
                    }
                };
        root.get().addOnCompleteListener(onValuesFetched);
        driversListView.setVisibility(View.VISIBLE);

    }
        public boolean getStartLatLong(String s){
            Geocoder geocoder = new Geocoder(Create_ride.this);
            List<Address> addressList;
            try {
                addressList = geocoder.getFromLocationName(s,1);
                if(addressList.size() != 0){
                   startLat = addressList.get(0).getLatitude();
                   startLong = addressList.get(0).getLongitude();
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
        Geocoder geocoder = new Geocoder(Create_ride.this);
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocationName(s,1);
            if(addressList.size() != 0){
                endLat = addressList.get(0).getLatitude();
                endLong = addressList.get(0).getLongitude();
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
