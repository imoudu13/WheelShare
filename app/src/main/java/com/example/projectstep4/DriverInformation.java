package com.example.projectstep4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DriverInformation extends AppCompatActivity {
    private EditText licenseNumber, criminalHistory;
    private HashMap<String, String> driverInfo;
    Intent intent;
    private DatabaseReference root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_information);

        licenseNumber = findViewById(R.id.licenseBox);
        criminalHistory = findViewById(R.id.historyBox);
        Button finishButton = findViewById(R.id.startButton);

        intent = getIntent();

        driverInfo = (HashMap<String, String>) intent.getSerializableExtra("info");

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String licenseNo = licenseNumber.getText().toString();
                String crimHist = criminalHistory.getText().toString();

                driverInfo.put("license number", licenseNo);
                driverInfo.put("criminal history", crimHist);
                driverInfo.put("rating", "0");
                driverInfo.put("numberOfRides", "0");
                driverInfo.put("isRider", "false");

                intent = new Intent(DriverInformation.this, CreateOrJoin.class);

                writeToFB((String)driverInfo.get("uid"), (String)driverInfo.get("firstName"),
                        (String)driverInfo.get("lastName"), (String)driverInfo.get("pw"),
                        Integer.parseInt((String)driverInfo.get("age")),
                        (String)driverInfo.get("gender"), licenseNo, crimHist);

                intent.putExtra("info", driverInfo);
                startActivity(intent);
            }
        });
    }
    private void writeToFB(String username, String firstName, String lastName, String password, int age, String gender, String driverInfo, String crimHist){
        root = FirebaseDatabase.getInstance().getReference("driver");
        DatabaseReference newRider = root.child(username);

        //set the username as a new child of the root
        newRider.child("firstName").setValue(firstName);
        newRider.child("lastName").setValue(lastName);
        newRider.child("password").setValue(password);
        newRider.child("age").setValue(age);
        newRider.child("gender").setValue(gender);
        newRider.child("driver information").setValue(driverInfo);
        newRider.child("criminal history").setValue(crimHist);
        newRider.child("rating").setValue("0");
        newRider.child("numberOfRides").setValue("0");
        newRider.child("isRider").setValue(false);

        // should we add other information to the
    }
}