package com.example.projectstep4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class CarpoolCreated extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool_created);

        //Creates a recycler view for a scrollable list of all the riders
        List<ListModel> initialData = new ArrayList<>();
        initialData.add(new ListModel("Item 1", 4.5f));
        initialData.add(new ListModel("Item 2", 3.0f));
        initialData.add(new ListModel("Item 3", 5.0f));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter adapter = new MyAdapter(initialData);
        recyclerView.setAdapter(adapter);
    }
}