package com.example.projectstep4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RatePassengers extends AppCompatActivity implements RatingAdapter.OnItemClickListener{
    TextView selectedNameTextView;
    RatingBar bar;
    List<String> names;
    RecyclerView recyclerView;
    Button rateButton;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_passengers);

        /*Currently set that when you click an item it gets displayed on the textview. You only need to load data in the names arrayList and it should work
        * Get the list of riders and set the amount to the count variable this makes it so you have to rate all of them
        * We also need to include the total earnings for driver here
        * Here we should also set the riders and drivers ride info like start location to null*/

        recyclerView = findViewById(R.id.ratingList);
        selectedNameTextView = findViewById(R.id.textView14);
        bar = findViewById(R.id.ratingBar2);
        rateButton = findViewById(R.id.button4);

        names = new ArrayList<>();
        names.add("ASDasd");
        names.add("adasd");
        count = names.size();

        RatingAdapter adapter = new RatingAdapter(names, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    public void onItemClick(String selectedName) {
        selectedNameTextView.setText(selectedName);
    }
    public void rateNext(View view){
        String temp = rateButton.getText().toString();
        if(temp.equals("Rate"))
            rate();
        else next();
    }
    public void rate(){
        String name = selectedNameTextView.getText().toString();
        float rating = bar.getRating();
        boolean work = names.remove(name);

        RatingAdapter adapter = new RatingAdapter(names, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        count--;
        selectedNameTextView.setText("");
        bar.setRating(0);
        if(count==0){
            rateButton.setText("Next");
        }

    }
    public void next(){

    }
}