package com.example.demo;
import java.util.ArrayList;
public class LogInfo {
    private String numTel;
    private String password;
    private double longPos;
    private double latPos;
    private ArrayList<String> contacts;

    public LogInfo(String number, String password,double longPos, double latPos, ArrayList<String> contacts){
        this.numTel = number;
        this.password = password;
        this.longPos = longPos;
        this.latPos = latPos;
        this.contacts = contacts;
    }
}