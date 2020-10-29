package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceivedMessage {
    private String from;
    private String to;
    private float longitude;
    private float latitude;
    private String body;
    private Date date;

    public ReceivedMessage( String from, String to, float longitude, float latitude, String body, Date date){
        this.longitude = longitude;
        this.latitude = latitude;
        this.from = from;
        this.to = to;
        this.date = date;
        this.body = body;
    }

    @Override
    public String toString(){
        return "From: " + this.from + "\n" + "To: " + this.to + "\n" + "Date: " + this.date + "\n" + "Message:\n" + this.body;
    }
}