package com.example.projectstep4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class CreateOrJoin extends AppCompatActivity {
    HashMap<String, String> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_join);

        Button create = findViewById(R.id.createButton);
        Button join = findViewById(R.id.joinButton);
        Intent intent = getIntent();

        map = (HashMap<String, String>) intent.getSerializableExtra("info");
        String isRider = map.get("isRider");
        if(isRider.equals("true")){
            create.setVisibility(View.INVISIBLE);
        }
        if(isRider.equals("false")){
            join.setVisibility(View.INVISIBLE);
        }

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOrJoin.this, CreateCarpool.class);
                intent.putExtra("info", map);
                startActivity(intent);
            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOrJoin.this, Create_ride.class);
                intent.putExtra("info", map);
                startActivity(intent);
            }
        });
    }
}