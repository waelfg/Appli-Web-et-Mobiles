package com.example.demo;
import java.util.ArrayList;
public class Messages {
    private ArrayList<ReceivedMessage> messages;
    public Messages(){
        this.messages = new ArrayList<ReceivedMessage>();
    } 

    public ArrayList<ReceivedMessage> getMessageList(){
        return this.messages;
    }
}