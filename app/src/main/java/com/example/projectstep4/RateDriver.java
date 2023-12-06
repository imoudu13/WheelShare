package com.example.projectstep4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class RateDriver extends AppCompatActivity {
TextView driverName;
RatingBar rateDriverBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_driver);
        /*Riders should be directed here from the login page if they have just finished a ride before being
        * sent to create page again, this is also why we need the riders to have a driveruid field so
        * we can update driver rating*/
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        //using this uid get the driver from fb, set name to below
        driverName = findViewById(R.id.textView20);

        rateDriverBar = findViewById(R.id.rateDriverBar);


    }
    public void rateDriver(View view){
        //Upload changes to FB
    }
}