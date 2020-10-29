package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.demo.*;

public class profile extends AppCompatActivity {
    private TextView distance ;
    private SeekBar seekbar ;
    private Button goback ;
    private  Button leave ;
    private TextView name ;
    private TextView phonenumber ;
    private Button map ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        distance = (TextView) findViewById(R.id.textView) ;
        seekbar = (SeekBar) findViewById(R.id.seekBar) ;
        goback = (Button) findViewById(R.id.goback) ;
        leave = (Button) findViewById(R.id.signout) ;
        map = (Button)findViewById(R.id.button3) ;
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleMap() ;
            }
        });

        /**
         * Récup nom et num lors de la connexion
         */
        name = (TextView) findViewById(R.id.name) ;
        phonenumber = (TextView) findViewById(R.id.phonenumber) ;
        LoginResult currentUser = (LoginResult)Data.getInstance().retrieveData("CurrentUser");
        if (currentUser == null){
            name.setText("NULL"); //Il faut récup le nom lors de la connexion
            phonenumber.setText("NULL"); //Il faut récup le num lors de la connexion
        }
        else{
            name.setText(currentUser.getName()); 
            if (currentUser.getNumTel().equals("")){
                phonenumber.setText("fuck");
            }
            else{
                phonenumber.setText(currentUser.getNumTel());
            }
             
        }
        

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distance.setText(""+progress/2+" KM");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleGoback() ;
            }
        });

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLeave() ;
            }
        });
    }

    public void handleMap() {

            Intent intent = new Intent(this,MapsActivity.class);
            startActivity(intent);
        }


    private void handleLeave() {
        Intent intent = new Intent(this,Login.class);
        Data.getInstance().deleteData("CurrentUser");
        SharedPreferences.Editor deletion = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        deletion.remove("PREF_COOKIES");
        deletion.commit();
        startActivity(intent);
    }

    private void handleGoback() {
        Intent intent = new Intent(this,scrollmenu.class);
        startActivity(intent);
    }
}
