package com.example.projectstep4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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


        //find the other view
        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        userId = findViewById(R.id.userId);
        password = findViewById(R.id.pw);
        age = findViewById(R.id.age);


        //find radio group
        genderGroup = findViewById(R.id.radioGroup);

        //find check boxes
        riderBox = findViewById(R.id.disability);
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

                //makes sure the clients fills out all necessary information
                boolean fillAllBoxes = !uId.equals("") && !firstName.equals("") && !lastName.equals("") && !pw.equals("") && ageOfCust > 0;
                if(fillAllBoxes){

                    if(genderGroup.getCheckedRadioButtonId() != -1) {
                        RadioButton butt = findViewById(genderGroup.getCheckedRadioButtonId());

                        gender = butt.getText().toString();

                    }
                    if(driver){
                        HashMap<String, String> driverInformation = new HashMap<>();

                        driverInformation.put("uid", uId);
                        driverInformation.put("firstName", firstName);
                        driverInformation.put("lastName", lastName);
                        driverInformation.put("pw", pw);
                        driverInformation.put("age", String.valueOf(ageOfCust));
                        driverInformation.put("gender", gender);

                        Intent intent = new Intent(Signup.this, DriverInformation.class);
                        intent.putExtra("info", driverInformation);

                        //what information needs to be passed?
                        startActivity(intent);
                    }else{
                        writeToFB(uId, firstName, lastName, pw, ageOfCust, gender, rider ? "yes" : "no");

                        HashMap<String, String> riderInfo = new HashMap<>();
                        riderInfo.put("uid", uId);
                        riderInfo.put("firstName", firstName);
                        riderInfo.put("lastName", lastName);
                        riderInfo.put("password", pw);
                        riderInfo.put("age", String.valueOf(ageOfCust));
                        riderInfo.put("gender", gender);
                        riderInfo.put("rating", "0");
                        riderInfo.put("isRider", "true");
                        Intent intent = new Intent(Signup.this, CreateOrJoin.class);
                        intent.putExtra("info", riderInfo);

                        //what information needs to be passed?
                        startActivity(intent);
                    }
                }else{
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, "Please make sure that you have completely filled out the form.", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
    }
    private void writeToFB(String username, String firstName, String lastName, String password, int age, String gender, String disability){
        root = FirebaseDatabase.getInstance().getReference("rider");
        DatabaseReference newRider = root.child(username);

        //set the username as a new child of the root
        newRider.child("firstName").setValue(firstName);
        newRider.child("lastName").setValue(lastName);
        newRider.child("password").setValue(password);
        newRider.child("age").setValue(String.valueOf(age));
        newRider.child("gender").setValue(gender);
        newRider.child("rating").setValue("0");
        newRider.child("numberOfRides").setValue("0");
        newRider.child("disabilities").setValue(disability);
        newRider.child("start").setValue("");
        newRider.child("end").setValue("");
        newRider.child("depart").setValue("");
        newRider.child("isRider").setValue(true);
        newRider.child("currentlyRiding").setValue(false);
        newRider.child("needsToRate").setValue(false);
        newRider.child("driverUid").setValue("");

        // should we add other information to the
    }

}