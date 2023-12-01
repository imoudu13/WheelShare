package com.example.projectstep4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateOrJoin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_join);

        Button create = findViewById(R.id.createButton);
        Button join = findViewById(R.id.joinButton);
        Intent intent = getIntent();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOrJoin.this, CreateCarpool.class);
                startActivity(intent);
            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOrJoin.this, Create_ride.class);
                startActivity(intent);
            }
        });
    }
}