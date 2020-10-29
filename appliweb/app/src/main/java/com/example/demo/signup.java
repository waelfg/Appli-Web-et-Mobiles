package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class signup extends AppCompatActivity {
    private Button tologin ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tologin = (Button) findViewById(R.id.imageView3) ;
        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginactvity();
            }
        });
    }

    public void openLoginactvity(){
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }
}
