package com.example.projectstep4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


public class Signup extends AppCompatActivity {
    private DatabaseReference root;
    private RadioGroup genderGroup;
    private EditText fName, lName, age, userId, password;
    private CheckBox riderBox, driverBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        root = FirebaseDatabase.getInstance().getReference("customers");



        //find the other view
        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        userId = findViewById(R.id.userId);
        password = findViewById(R.id.pw);
        age = findViewById(R.id.bdate);


        //find radio group
        genderGroup = findViewById(R.id.radioGroup);

        //find check boxes
        riderBox = findViewById(R.id.riderbox);
        driverBox = findViewById(R.id.driverBox);

        //submit button
        Button submit = findViewById(R.id.submitButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uId = userId.getText().toString();
                String firstName = fName.getText().toString();
                String lastName = lName.getText().toString();
                String pw = password.getText().toString();
                int ageOfCust = Integer.parseInt(age.getText().toString());
                boolean rider = riderBox.isChecked();
                boolean driver = driverBox.isChecked();

                String gender = "";


                if(genderGroup.getCheckedRadioButtonId() != -1) {
                    RadioButton butt = findViewById(genderGroup.getCheckedRadioButtonId());

                   gender = butt.getText().toString();

                }

                if(driver){
                    HashMap<String, Object> driverInformation = new HashMap<>();
                    driverInformation.put("uid", uId);
                    driverInformation.put("firstName", firstName);
                    driverInformation.put("lastName", lastName);
                    driverInformation.put("pw", pw);
                    driverInformation.put("ageOfCust", String.valueOf(ageOfCust));
                    driverInformation.put("rider", rider ? "true" : "false");
                    driverInformation.put("gender", gender);

                    Intent intent = new Intent(Signup.this, DriverInformation.class);
                    intent.putExtra("information", driverInformation);

                    //what information needs to be passed?
                    startActivity(intent);
                }else{
                    writeToFB(uId, firstName, lastName, pw, ageOfCust, rider, gender);

                    HashMap<String, Object> riderInfo = new HashMap<>();
                    riderInfo.put("uid", uId);
                    riderInfo.put("firstName", firstName);
                    riderInfo.put("lastName", lastName);
                    riderInfo.put("pw", pw);
                    riderInfo.put("ageOfCust", String.valueOf(ageOfCust));
                    riderInfo.put("rider", rider);
                    riderInfo.put("gender", gender);

                    Intent intent = new Intent(Signup.this, CreateOrJoin.class);
                    intent.putExtra("info", riderInfo);

                    //what information needs to be passed?
                    startActivity(intent);
                }

            }
        });
    }
    private void writeToFB(String username, String firstName, String lastName, String password, int age, boolean rider, String gender){
        root = FirebaseDatabase.getInstance().getReference("rider");
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