package com.example.demo;

public class LoginResult {
    private String name;
    private String numTel;
   

    public LoginResult(String name, String numTel ) {
        this.name = name;
        this.numTel = numTel;
       
    }

    public String getName() {
        return name;
    }

    public String getNumTel() {
        return numTel;
    }

    
}
