package com.example.projectstep4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarpoolCreated extends AppCompatActivity {
    Button nextButton;
    Button cancelButton;
    ListView ridersListView;
    DatabaseReference root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool_created);
        nextButton = findViewById(R.id.startButton);
        cancelButton = findViewById(R.id.cancelButton);
        ridersListView = findViewById(R.id.ridersListView);
        Intent intent = getIntent();
        ArrayList<Double> list = (ArrayList<Double>) intent.getSerializableExtra("latlong");

        Map<String, HashMap<String, String>>  ridersMap = new HashMap<>();
        root = FirebaseDatabase.getInstance().getReference("currentRides");

        //this retrieves all information from database and puts it in a hashmap to check
        OnCompleteListener<DataSnapshot> onValuesFetched = new
                OnCompleteListener<DataSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task)
                    {
                        if (!task.isSuccessful())
                            Log.e("firebase", "Error getting data", task.getException());
                        else
                        {
                            DataSnapshot receivedValue = task.getResult();
                            for(DataSnapshot node: receivedValue.getChildren())
                            {
                                HashMap<String, String> tempMap = new HashMap<>();

                                tempMap.put("uid", node.getKey());
                                tempMap.put("cost", getStringValue(node.child("cost")));
                                tempMap.put("depart", getStringValue(node.child("depart")));
                                tempMap.put("disability", getStringValue(node.child("disability")));
                                tempMap.put("end", getStringValue(node.child("end")));
                                tempMap.put("gender", getStringValue(node.child("gender")));
                                tempMap.put("rating", getStringValue(node.child("rating")));
                                tempMap.put("start",getStringValue(node.child("start")));




                                ridersMap.put(node.getKey(), tempMap);

                            }

                        }
                        List<String> keyList = new ArrayList<>(ridersMap.keySet());
                        List<ListModel> initialData = new ArrayList<>();
                        for(int i = 0; i <keyList.size(); i++){
                            HashMap<String, String> currRider = ridersMap.get(keyList.get(i));
                            String uid = currRider.get("uid");
                            Float rating = Float.parseFloat(currRider.get("rating"));
                            if(true){
                                initialData.add(new ListModel(uid, rating));
                            }


                        }

                        MyAdapter adapter = new MyAdapter(CarpoolCreated.this, initialData);
                        ridersListView.setAdapter(adapter);

                    }
                };
        root.get().addOnCompleteListener(onValuesFetched);



        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CarpoolCreated.this, Maps.class);
                intent.putExtra("latlong", list);
                startActivity(intent);
            }
        });


    }
    private String getStringValue(DataSnapshot dataSnapshot) {
        Object value = dataSnapshot.getValue();
        return (value != null) ? value.toString() : "";
    }
}