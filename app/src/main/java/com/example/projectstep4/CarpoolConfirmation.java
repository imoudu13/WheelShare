package com.example.projectstep4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CarpoolConfirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool_confirmation);

        Button create = findViewById(R.id.createAnother);
        Button reportProblem = findViewById(R.id.problemButton);
        Button findSim = findViewById(R.id.findSimilar);        //find simalr carpool button

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarpoolConfirmation.this, CreateCarpool.class);
                startActivity(intent);
            }
        });
        reportProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "This feature has not been implemented yet", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        findSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to find similar carpool page
            }
        });
    }
}