package com.example.projectstep4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RateDriver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_driver);
        /*Riders should be directed here from the login page if they have just finished a ride before being
        * sent to create page again, this is also why we need the riders to have a driveruid field so
        * we can update driver rating*/
    }
}