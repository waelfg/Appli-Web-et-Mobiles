package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/login")
    Call<LoginResult> executeLogin(@Body LogInfo loginInfo);

    @POST("/signup")
    Call<Void> executeSignup (@Body SignUpInfo SignInfo);

    @POST("/sendmsg")
    Call<Void> sendMsg(@Body Message msg);

    @POST("/contacts")
    Call<Void> sendContact(@Body ArrayList<Contact> contacts);

    @GET("/getmsg")
    Call<Messages> getMessages();
}
