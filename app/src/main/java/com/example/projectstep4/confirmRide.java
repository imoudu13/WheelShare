package com.example.projectstep4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class confirmRide extends AppCompatActivity {
    TextView feeBreakdown;

    TextView costTextView;
    Button backButton;
    Button payAndConfirm;
    EditText cardName;
    EditText cardNum;
    EditText cardExpiration;
    EditText cardSecurityCode;
    HashMap<String, String> clientMap;
    String wheel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_ride);
        feeBreakdown = findViewById(R.id.feeBreakdown);
        backButton = findViewById(R.id.backButton);
        costTextView = findViewById(R.id.rideCost);
        payAndConfirm = findViewById(R.id.payAndConfirmButton);
        cardName = findViewById(R.id.cardName);
        cardNum = findViewById(R.id.cardNum);
        cardExpiration = findViewById(R.id.cardExpire);
        cardSecurityCode = findViewById(R.id.cardSecurity);
        Intent intent = getIntent();
        double startLat = intent.getExtras().getDouble("startLat");
        double startLong = intent.getExtras().getDouble("startLong");
        double endLat = intent.getExtras().getDouble("endLat");
        double endLong = intent.getExtras().getDouble("endLong");
        clientMap = (HashMap<String, String>) intent.getSerializableExtra("info");
        String name = clientMap.get("firstname") + " " + clientMap.get("lastname");
        String rating = clientMap.get("rating");
        String username = clientMap.get("uid");
        String gender = clientMap.get("gender");
        String start = intent.getStringExtra("start");
        String end = intent.getStringExtra("end");
        String time = intent.getStringExtra("time");
        boolean isWheelChair = intent.getBooleanExtra("wheelchair", false);
        wheel = "No";
        if(isWheelChair){
            wheel = "Yes";
        }
        double distanceBetweenStartAndEnd = Math.acos(Math.sin(Math.toRadians(startLat))*Math.sin(Math.toRadians(endLat))+Math.cos(Math.toRadians(startLat))*Math.cos(Math.toRadians(endLat))*Math.cos(Math.toRadians(endLong)-Math.toRadians(startLong)))*6371;
        double cost = distanceBetweenStartAndEnd * 1.5;
        double endCost = Math.round(cost*100.0)/100.0;
        double taxes = 0.13* endCost;
        double total = taxes + endCost + 2;
        feeBreakdown.setText("\n" + "Service Fee: $2.00" + "\n" + "Ride Cost: $" + endCost + "\n" + "Taxes: $" + Math.round(taxes*100.0)/100.0);
        costTextView.setText(" $" + Math.round(total*100.0)/100.0 + " ");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        payAndConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInfo(cardName.getText().toString(), cardNum.getText().toString(), cardSecurityCode.getText().toString(), cardExpiration.getText().toString())){
                    writeToFB(username,start,end,time,rating,wheel, total);
                    Intent intent = new Intent(confirmRide.this, Maps.class);

                    ArrayList<Double> list = new ArrayList<>();

                    list.add(startLong);
                    list.add(startLat);
                    list.add(endLat);
                    list.add(endLong);

                    intent.putExtra("latlong", list);

                    startActivity(intent);
                }
            }
        });

    }
    public boolean checkInfo(String name, String num, String security, String expire){
        if(name.equals("")){
            Toast.makeText(this, "Please submit a credit card name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(num.equals("") || num.length()!=16){
            Toast.makeText(this, "Please submit a valid card number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(security.equals("") || security.length()!=3){
            Toast.makeText(this, "Please submit a valid security code", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(expire.equals("")){
            Toast.makeText(this, "Please submit a valid expiry date", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    public void writeToFB(String username, String start, String end, String departureTime, String rating, String wheelchair, double cost){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference("currentRides");
        DatabaseReference newRider = root.child(username);

        //set the username as a new child of the root
        newRider.child("start").setValue(start);
        newRider.child("end").setValue(end);
        newRider.child("depart").setValue(departureTime);
        newRider.child("rating").setValue(rating);
        newRider.child("disability").setValue(wheelchair);
        newRider.child("cost").setValue(cost);
        newRider.child("gender").setValue(clientMap.get("gender"));


    }
}