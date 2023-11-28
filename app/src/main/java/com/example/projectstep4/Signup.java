package com.example.projectstep4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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
                writeToFB(uId, firstName, lastName, pw, ageOfCust, rider, driver, gender);
            }
        });
    }
    private void writeToFB(String username, String firstName, String lastName, String password, int age, boolean rider, boolean driver, String gender){
        DatabaseReference newCustomer = root.child(username);

        //set the username as a new child of the root
        newCustomer.child("firstName").setValue(firstName);
        newCustomer.child("lastName").setValue(lastName);
        newCustomer.child("password").setValue(password);
        newCustomer.child("age").setValue(age);
        newCustomer.child("rider").setValue(rider);
        newCustomer.child("driver").setValue(driver);
        newCustomer.child("gender").setValue(gender);

        // should we add other information to the
    }

}