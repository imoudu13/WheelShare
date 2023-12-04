package com.example.projectstep4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class availableDriver extends AppCompatActivity {
    TextView abadaba;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_driver);
        abadaba  =findViewById(R.id.abadaba);
        Intent intent = getIntent();
        String username = intent.getExtras().getString("usernameDriver");
        abadaba.setText(username);
    }
}