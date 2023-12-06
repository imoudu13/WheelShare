package com.example.projectstep4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CurrentlyRiding extends AppCompatActivity {
Button homeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currently_riding);

    }
    public void home(View view){
        Intent intent = new Intent(CurrentlyRiding.this,Login.class);
        startActivity(intent);
    }
}