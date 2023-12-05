package com.example.projectstep4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Map;

public class Login extends AppCompatActivity {
    DatabaseReference root;
    EditText username, password;
    Map<String, HashMap<String, String>> clientMap;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        root = FirebaseDatabase.getInstance().getReference("rider");

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
                                tempMap.put("firstName", node.child("firstName").getValue().toString());
                                tempMap.put("lastName", node.child("lastName").getValue().toString());
                                tempMap.put("password", node.child("password").getValue().toString());
                                tempMap.put("gender", node.child("gender").getValue().toString());
                                tempMap.put("rating", node.child("rating").getValue().toString());
                                tempMap.put("numberOfRides", node.child("numberOfRides").getValue().toString());



                                clientMap.put(node.getKey(), tempMap);
                            }
                            boolean t = true;
                        }
                    }
                };
        root.get().addOnCompleteListener(onValuesFetched);
        root = FirebaseDatabase.getInstance().getReference("driver");
        root.get().addOnCompleteListener(onValuesFetched);


        clientMap = new HashMap<>();
        //find views and buttons
        Button button = findViewById(R.id.LoginSubmit);

        //find username and password box
        username = findViewById(R.id.usernameBox);
        password = findViewById(R.id.passwordBox);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = username.getText().toString();
                String pw = password.getText().toString();


                Context context = getApplicationContext();
                Toast toast;

                if(clientMap.containsKey(uid)) {
                    HashMap<String, String> temp = clientMap.get(uid);

                    String tempPass = temp.get("password");

                    if (tempPass.equals(pw)) {
                        //start next activity, pass necessary information as well
                        toast = Toast.makeText(context, "This works.", Toast.LENGTH_SHORT);     //this is just for testing, will change later
                        Intent intent = new Intent(Login.this, CreateOrJoin.class);
                        intent.putExtra("info", temp);
                        //what information needs to be passed?
                        startActivity(intent);

                    } else {
                        toast = Toast.makeText(context, "Incorrect password, please try again.", Toast.LENGTH_SHORT);
                    }
                    toast.show();
                }else{
                    toast = Toast.makeText(context, "Incorrect username, please try again.", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

    }
}