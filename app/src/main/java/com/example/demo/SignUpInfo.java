package com.example.demo;

public class SignUpInfo {
    private String name;
    private String numTel;
    private String password;

    public String getName(){
        return name;
    }

    public String getNumTel(){
        return numTel;
    }

    public String getpassword(){
        return password;
    } 

    public SignUpInfo(String name, String number, String password){
        this.name=name;
        this.numTel=number;
        this.password=password;
    }
}