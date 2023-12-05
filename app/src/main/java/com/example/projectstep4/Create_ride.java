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
    CheckBox wheelchair;
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
        wheelchair = findViewById(R.id.wheelChairCheckBox);
        Intent intent = getIntent();
        HashMap<String, String> clientMap = (HashMap<String, String>) intent.getSerializableExtra("info");

        root = FirebaseDatabase.getInstance().getReference("carpools");



        findRidesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get info
                String start = startLocation.getText().toString();
                String end = endLocation.getText().toString();
                String departTime = setTime.getText().toString();
                Boolean isWheelchair = wheelchair.isChecked();
                if(checkInfo(start,end,departTime)){
                    if(getStartLatLong(start)){
                        if(getEndLatLong(end)){
                            Intent intent = new Intent(Create_ride.this, confirmRide.class);
                            intent.putExtra("startLat", startLat);
                            intent.putExtra("startLong", startLong);
                            intent.putExtra("endLat", endLat);
                            intent.putExtra("endLong", endLong);
                            intent.putExtra("start", start);
                            intent.putExtra("end", end);
                            intent.putExtra("time", departTime);
                            intent.putExtra("wheelchair",isWheelchair);
                            intent.putExtra("info", clientMap);
                            startActivity(intent);
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
    public void doNothign(){}

}
