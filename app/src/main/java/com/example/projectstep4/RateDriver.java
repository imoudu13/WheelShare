package com.example.projectstep4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RateDriver extends AppCompatActivity {
TextView driverName;
RatingBar rateDriverBar;
HashMap<String, String> clientMap;
String driverUID;
Button rateDriverButton;
String riderUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_driver);

        Intent intent = getIntent();

        clientMap = (HashMap<String, String>) intent.getSerializableExtra("info");
        //using this uid get the driver from fb, set name to below
        driverName = findViewById(R.id.driverNameTextViewRateDriver);
        rateDriverButton = findViewById(R.id.rateDriverButton);
        rateDriverBar = findViewById(R.id.rateDriverBar);

        riderUid = clientMap.get("uid");
        DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference("currentRides");
        DatabaseReference keyReference = rootReference.child(riderUid);

        keyReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("driverUID")) {
                        driverUID = dataSnapshot.child("driverUID").getValue(String.class);
                        boolean t = true;
                    } else {
                        Log.e("Firebase", "Child not found in dataSnapshot");
                    }
                } else {
                    Log.e("Firebase", "DataSnapshot does not exist");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Firebase", "Failed to read value.", error.toException());
            }
        });
        rateDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate();
            }
        });
    }
    public void rateDriver(View view){
        //Upload changes to FB
    }
    public void rate() {
        float rating = rateDriverBar.getRating();
        String ratingString = String.valueOf(rating);

        DatabaseReference root1 = FirebaseDatabase.getInstance().getReference("driver");
        DatabaseReference newRider1 = root1.child(driverUID);
        newRider1.child("rating").setValue(ratingString);

        Toast.makeText(this, "Thank you for taking the time to rate your driver", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(RateDriver.this, MainActivity.class);
        startActivity(intent);


        DatabaseReference root2 = FirebaseDatabase.getInstance().getReference("currentRides");
        DatabaseReference userToRemove = root2.child(riderUid);

        userToRemove.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RateDriver.this, "Current ride over. Create new rides as you desire!", Toast.LENGTH_SHORT).show();
                    Log.d("Firebase", "User removed from currentRides node");
                } else {
                    // Handle the error here
                    Log.e("Firebase", "Error removing user from currentRides node: " + task.getException().getMessage());
                }
            }
        });
    }
}