package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class menuprincipal extends AppCompatActivity {

    private Button send ;
    private Button profile ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuprincipal);
        
        

        send = (Button) findViewById(R.id.button) ;
        profile = (Button) findViewById(R.id.button2) ;

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProfile() ;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMessage() ;
            }
        });
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
