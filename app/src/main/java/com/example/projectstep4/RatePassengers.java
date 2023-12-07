package com.example.projectstep4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RatePassengers extends AppCompatActivity implements RatingAdapter.OnItemClickListener {
    TextView selectedNameTextView;
    RatingBar bar;
    List<String> names;
    RecyclerView recyclerView;
    Button rateButton;
    int count;
    HashMap<String, String> clientMap;
    String riders = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_passengers);

        Intent intent = getIntent();
        clientMap = (HashMap<String, String>) intent.getSerializableExtra("info");
        recyclerView = findViewById(R.id.ratingList);
        selectedNameTextView = findViewById(R.id.selectedRiderRating);
        bar = findViewById(R.id.ratingBar2);
        rateButton = findViewById(R.id.button4);
        String driverUid = clientMap.get("uid");
        DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference("carpools");
        DatabaseReference keyReference = rootReference.child(driverUid);

        keyReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("riders")) {
                        riders = dataSnapshot.child("riders").getValue(String.class);
                        boolean t = true;
                        updateAdapter();
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
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!selectedNameTextView.getText().equals("")){
                    rate();
                }
                next();
            }
        });
    }

    private void updateAdapter() {
        names = new ArrayList<>();
        String[] ridersArr = riders.split(",");
        for (int i = 0; i < ridersArr.length; i++) {
            names.add(ridersArr[i]);
        }

        count = names.size();

        RatingAdapter adapter = new RatingAdapter(names, RatePassengers.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(RatePassengers.this));
    }

    public void onItemClick(String selectedName) {
        selectedNameTextView.setText(selectedName);
    }

    public void rateNext(View view) {
        String temp = rateButton.getText().toString();
        if (temp.equals("Rate")) {
            rate();
        } else {
            next();
        }
    }

    public void rate() {
        String name = selectedNameTextView.getText().toString();
        float rating = bar.getRating();
        String ratingString = String.valueOf(rating);
        boolean work = names.remove(name);

        RatingAdapter adapter = new RatingAdapter(names, RatePassengers.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(RatePassengers.this));
        //write to database
        DatabaseReference root1 = FirebaseDatabase.getInstance().getReference("rider");
        DatabaseReference newRider1 = root1.child(name);

        //set the username as a new child of the root
        newRider1.child("rating").setValue(ratingString);

        count--;
        selectedNameTextView.setText("");
        bar.setRating(0);
        if (count == 0) {
            rateButton.setText("Next");
        }
    }

    public void next() {
        if(rateButton.getText().equals("Next")){
            Intent intent = new Intent(RatePassengers.this, MainActivity.class);
            startActivity(intent);

            DatabaseReference root2 = FirebaseDatabase.getInstance().getReference("carpools");
            DatabaseReference userToRemove = root2.child(clientMap.get("uid"));

            userToRemove.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RatePassengers.this, "Current ride over. Create new rides as you desire!", Toast.LENGTH_SHORT).show();
                        Log.d("Firebase", "User removed from currentRides node");
                    } else {
                        // Handle the error here
                        Log.e("Firebase", "Error removing user from currentRides node: " + task.getException().getMessage());
                    }
                }
            });
        }
    }
}
