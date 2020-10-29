package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.security.MessageDigest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
public class MainActivity extends AppCompatActivity {
    private Button tologin ;
    private Button register ;
    private Retrofit retrofit ;
    private RetrofitInterface retrofitInterface ;
    private String URL = "http://10.0.2.2:3000/" ; //Ici il faut l'url
    @Override
    protected void onDestroy(){
        SharedPreferences.Editor deletion = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        deletion.remove("PREF_COOKIES");
        deletion.commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * Instanciation des paramètres
         */
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //builder.addInterceptor(new CookiesSender(getApplicationContext())); // VERY VERY IMPORTANT
        //builder.addInterceptor(new CookiesReceiver(getApplicationContext())); // VERY VERY IMPORTANT
        client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL) // REQUIRED
                .client(client) // VERY VERY IMPORTANT
                .addConverterFactory(GsonConverterFactory.create())
                .build(); // REQUIRED

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        Data.getInstance().saveData("retrofit", retrofitInterface);



        /**
         * When clicking on already a member we go to the login activity
         */
        tologin = (Button) findViewById(R.id.imageView3) ;
        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginactvity();
            }
        });
        /**
         * When clicking on the submit button we handle the registration
         */

        register = (Button) findViewById(R.id.imageView2) ;
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegistration();
            }
        });



    }


    /**
     * Communication with the server for registration
     */

    private void handleRegistration() {
        EditText name = findViewById(R.id.editText) ;
        EditText phonenum = findViewById(R.id.editText4);
        EditText mdp = findViewById(R.id.editText3);
        String sName = name.getText().toString();
        String sNum = phonenum.getText().toString();
        String sPass = mdp.getText().toString();
        if (sName.equals("") || sNum.equals("") || sPass.equals("")){
            Toast.makeText(MainActivity.this,"Empty credentials",Toast.LENGTH_SHORT).show();
            return ;
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(mdp.getText().toString().getBytes());
        String encryptedPassword = new String(md.digest());
        Call<Void> call = retrofitInterface.executeSignup(new SignUpInfo(name.getText().toString(),phonenum.getText().toString(),encryptedPassword));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code()== 200) {
                    Toast.makeText(MainActivity.this,"Registration success",Toast.LENGTH_SHORT).show();
                    openScrollmenu();
                } else if (response.code() == 400 ){
                    Toast.makeText(MainActivity.this,"Already registred",Toast.LENGTH_SHORT).show();
                }
                
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }



    /**
     * Opens the login activity if we are already a member
     */
    public void openLoginactvity(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void openScrollmenu() {
        Intent intent = new Intent(this,scrollmenu.class);
        startActivity(intent);
    }


}

