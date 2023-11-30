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
    private HashMap<String, Object> driverInfo;
    Intent intent;
    private DatabaseReference root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_information);

        root = FirebaseDatabase.getInstance().getReference("customers");

        licenseNumber = findViewById(R.id.licenseBox);
        criminalHistory = findViewById(R.id.historyBox);
        Button finishButton = findViewById(R.id.button);

        intent = getIntent();

        driverInfo = (HashMap<String, Object>) intent.getSerializableExtra("information");

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String licenseNo = licenseNumber.getText().toString();
                String crimHist = criminalHistory.getText().toString();

                driverInfo.put("license number", licenseNo);
                driverInfo.put("criminal history", crimHist);

                intent = new Intent(DriverInformation.this, CreateOrJoin.class);

                writeToFB((String)driverInfo.get("uid"), (String)driverInfo.get("firstName"), (String)driverInfo.get("lastName"), (String)driverInfo.get("pw"), Integer.parseInt((String)driverInfo.get("ageOfCust")), (String)driverInfo.get("rider"), (String)driverInfo.get("gender"));

                intent.putExtra("information", driverInfo);
                startActivity(intent);
            }
        });
    }
    private void writeToFB(String username, String firstName, String lastName, String password, int age, String rider, String gender){
        root = FirebaseDatabase.getInstance().getReference("driver");
        DatabaseReference newRider = root.child(username);

        //set the username as a new child of the root
        newRider.child("firstName").setValue(firstName);
        newRider.child("lastName").setValue(lastName);
        newRider.child("password").setValue(password);
        newRider.child("age").setValue(age);
        newRider.child("rider").setValue(rider);
        newRider.child("gender").setValue(gender);

        // should we add other information to the
    }
}