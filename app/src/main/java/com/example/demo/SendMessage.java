package com.example.demo;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.demo.Data;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import okhttp3.*;
public class SendMessage extends AppCompatActivity {
    private Button camera ;
    private Button send ;
    private Button gallery ;
    private Retrofit retrofit ;
    private RetrofitInterface retrofitInterface ;
    private String URL = "http://10.0.2.2:3000/" ; //Ici il faut l'url



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sengmsg);

        /**
         * Instanciation des paramètres
         */

        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new CookiesSender(getApplicationContext())); 
        
        client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL) 
                .client(client) 
                .addConverterFactory(GsonConverterFactory.create())
                .build(); 

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        camera = (Button) findViewById(R.id.camera) ;
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleCamera() ;
            }
        });
        gallery = (Button) findViewById(R.id.gallery) ;
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleGallery() ;
            }
        });

        send = (Button) findViewById(R.id.sendmsg) ;
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage() ; 
            }
        });
    }

    private void HandleGallery() {
        Intent intent = new Intent(this,Gallery.class);
        startActivity(intent);
    }

    private void HandleCamera() {
        Intent intent = new Intent(this,Camera.class);
        startActivity(intent);
    }

    private void sendMessage() {
        EditText texttosend = findViewById(R.id.typemsg) ; 
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        float longitude = (float)location.getLongitude();
        float latitude = (float)location.getLatitude();
        System.out.println("longitude: "+longitude +"; \n latitude: "+latitude+"; \n" );
        LoginResult user = (LoginResult)Data.getInstance().retrieveData("CurrentUser");
        String num = user.getNumTel();
        Message toSend = new Message(num,longitude, latitude, texttosend.getText().toString());
        Call<Void> call = retrofitInterface.sendMsg(toSend);
        call.enqueue(new Callback<Void>(){

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code()== 200) {

                } else if (response.code() == 400 ){

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                
            }

        });
    }
}
