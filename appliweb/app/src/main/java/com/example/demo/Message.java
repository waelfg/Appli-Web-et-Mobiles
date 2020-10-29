package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String numSender;
    private float longPos;
    private float latPos;
    private String body;

    public Message(String numSender, float longPos, float latPos, String body){
        this.body = body;
        this.latPos = latPos;
        this.longPos=longPos;
        this.numSender=numSender;
    }

    @Override
    public String toString(){
        return "From: " + this.numSender + "\n" + "Message:\n" + this.body;
    }
}