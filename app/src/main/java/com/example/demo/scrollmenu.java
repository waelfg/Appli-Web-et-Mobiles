package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.demo.Data;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams ;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.gson.JsonElement;
import okhttp3.OkHttpClient;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.Toast;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.gson.Gson;

import android.content.Intent;
public class scrollmenu extends AppCompatActivity {
    private Retrofit retrofit ;
    private RetrofitInterface retrofitInterface ;
    private String URL = "http://10.0.2.2:3000/" ; //Ici il faut l'url
    private Button send ;
    private Button profile ;
    private MessagesAdapter adapter;
    private ArrayList<ReceivedMessage> messages;
    private Button reload ;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLogin();
        setContentView(R.layout.activity_scrollmenu);
        messages = new ArrayList<ReceivedMessage>();
        this.adapter = new MessagesAdapter(getApplicationContext(),messages);
        adapter.setNotifyOnChange(true);

        
        listView = (ListView) findViewById(R.id.listMsg);
        listView.setAdapter(adapter);
        send = (Button) findViewById(R.id.buttonReview) ;
        profile = (Button) findViewById(R.id.profile) ;
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProfile() ;
            }
        });

        reload = (Button) findViewById(R.id.reload) ;
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleReload() ;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMessage() ;
            }
        });

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

        receiveMessages();
    }

    private void HandleReload() {
        this.adapter.clear();
        receiveMessages();
    }


    private void receiveMessages(){
        Call<Messages> call = retrofitInterface.getMessages();
        call.enqueue(new Callback<Messages>() {
            @Override
            public void onResponse(Call<Messages> call, Response<Messages> response){
                if(response.code()==200){
                    Messages truc = response.body();

                    Gson gson = new Gson();
                    System.out.println("ANSWER IS : "+truc);
                    ArrayList<ReceivedMessage> res = truc.getMessageList();
                    for(ReceivedMessage msg : res){
                        System.out.println("Message is : "+msg);
                        scrollmenu.this.messages.add(0, msg);
                        scrollmenu.this.adapter.notifyDataSetChanged();
                    }
                }
                else if(response.code()==440){
                    AlertDialog.Builder builder = new AlertDialog.Builder(scrollmenu.this);
                    builder.setTitle("Session expriree");
                    builder.show();
                    Data.getInstance().deleteData("CurrentUser");
                    SharedPreferences.Editor deletion = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                    deletion.remove("PREF_COOKIES");
                    deletion.commit();
                    Intent intent = new Intent(scrollmenu.this,Login.class);
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(scrollmenu.this);
                    builder.setTitle("Impossible de recuperer les messages");
                    builder.show();
                }
            
            }

            @Override
            public void onFailure(Call<Messages> call, Throwable t){
                Toast.makeText(scrollmenu.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }


    private void checkLogin(){
        LoginResult currentUser = (LoginResult)Data.getInstance().retrieveData("CurrentUser");
        if (currentUser==null){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }

    private void gotoProfile() {
        Intent intent = new Intent(this,profile.class);
        startActivity(intent);
    }

    private void gotoMessage() {
        Intent intent = new Intent(this,SendMessage.class);
        startActivity(intent);
    }

    
}

