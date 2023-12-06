package com.example.projectstep4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarpoolCreated extends AppCompatActivity {
    Button nextButton;
    Button cancelButton;
    ListView ridersListView;
    DatabaseReference root;
    HashMap<String, String> clientMap;
    Map<String, HashMap<String, String>>  ridersMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool_created);
        nextButton = findViewById(R.id.startButton);
        cancelButton = findViewById(R.id.cancelButton);
        ridersListView = findViewById(R.id.ridersListView);
        Intent intent = getIntent();
        ArrayList<Double> list = (ArrayList<Double>) intent.getSerializableExtra("latlong");
        HashMap<String, Object> rideInformation = (HashMap<String, Object>) intent.getSerializableExtra("ride info");
        clientMap = (HashMap<String, String>) intent.getSerializableExtra("info");
        ridersMap = new HashMap<>();
        root = FirebaseDatabase.getInstance().getReference("currentRides");

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                            String deptTime = (String)rideInformation.get("depart");
                            String[] timeDriver =  deptTime.split(":");
                            String[] timeRider = currRider.get("depart").split(":");
                            int timeDriverInt = Integer.parseInt(timeDriver[0]);
                            int timeRiderInt = Integer.parseInt(timeRider[0]);
                            int timeDiff = Math.abs(timeDriverInt-timeRiderInt);
                            String disability = (String)rideInformation.get("disability");
                            String gender = (String)rideInformation.get("gender");
                            String start = (String)rideInformation.get("start");
                            String end = (String)rideInformation.get("end");
                            if (timeDiff <= 2 &&
                                    start.toLowerCase().equals(currRider.get("start").toLowerCase()) &&
                                    end.toLowerCase().equals(currRider.get("end").toLowerCase()) &&
                                    ((gender.equals("All") || gender.equals(currRider.get("gender"))) &&
                                            (disability.toLowerCase().equals(currRider.get("disability").toLowerCase()) || disability.toLowerCase().equals("yes")))) {
                                boolean t = true;
                                initialData.add(new ListModel(uid, rating));
                            }
                            if(initialData.size()==0){
                                Toast.makeText(CarpoolCreated.this, "No matching rides :(", Toast.LENGTH_SHORT).show();
                            }

                        }

                        MyAdapter adapter = new MyAdapter(CarpoolCreated.this, initialData);
                        ridersListView.setAdapter(adapter);

                    }
                };
        root.get().addOnCompleteListener(onValuesFetched);

        ridersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String driverUid = (String)rideInformation.get("uid");
                // Reference to the root node
                DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference("carpools");

                DatabaseReference keyReference = rootReference.child(driverUid);

                keyReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            if (dataSnapshot.hasChild("moneyEarned") && dataSnapshot.hasChild("riders")) {

                                Double moneyEarned = dataSnapshot.child("moneyEarned").getValue(Double.class);
                                String currentRiders = dataSnapshot.child("riders").getValue(String.class);
                                ListModel clickedItem = (ListModel) parent.getItemAtPosition(position);
                                String name = clickedItem.getName();
                                Double riderPay = Double.parseDouble(ridersMap.get(name).get("cost"));
                                if(!currentRiders.contains(name)){
                                    boolean t = true;
                                    moneyEarned += riderPay;
                                    currentRiders = name + "," + currentRiders;
                                    Toast.makeText(CarpoolCreated.this, "Rider added to ride", Toast.LENGTH_SHORT).show();
                                }
                                DatabaseReference root = FirebaseDatabase.getInstance().getReference("carpools");
                                DatabaseReference newRider = root.child(driverUid);

                                //set the username as a new child of the root
                                newRider.child("moneyEarned").setValue(moneyEarned);
                                newRider.child("riders").setValue(currentRiders);

                                DatabaseReference root1 = FirebaseDatabase.getInstance().getReference("currentRides");
                                DatabaseReference newRider1 = root1.child(name);

                                //set the username as a new child of the root
                                newRider1.child("driverUID").setValue(driverUid);

                                boolean t = true;


                            } else {
                                Log.e("Firebase", "Child not found in dataSnapshot");
                            }
                        } else {
                            Log.e("Firebase", "DataSnapshot does not exist");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("Firebase", "Failed to read value.", error.toException());
                    }
                });
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CarpoolCreated.this, Maps.class);
                intent.putExtra("latlong", list);
                intent.putExtra("info",clientMap);
                startActivity(intent);
            }
        });


    }
    private String getStringValue(DataSnapshot dataSnapshot) {
        Object value = dataSnapshot.getValue();
        return (value != null) ? value.toString() : "";
    }
}