package com.example.projectstep4;

import java.util.ArrayList;
import java.util.HashMap;

public class Client {
    String firstname="";
    String lastname = "";
    String username = "";
    String password = "";
    String gender = "";
    int age = 0;
    boolean rider = false;
    boolean driver = false;
    ArrayList<HashMap<String, ArrayList<String>>> previousCarpools;

    Client(String firstname, String lastname, String username, String pw, String gender, boolean rider, boolean driver, int age){
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = pw;
        this.gender = gender;
        this.rider = rider;
        this.driver = driver;
        this.age = age;

        previousCarpools = new ArrayList<>();
    }

    public ArrayList<HashMap<String, ArrayList<String>>> getPreviousCarpools(){
        return previousCarpools;
    }
}
