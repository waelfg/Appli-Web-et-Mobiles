package com.example.demo;
import android.location.LocationListener;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.location.Location;
import android.content.Context;
import android.location.LocationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import com.example.demo.Data;
import okhttp3.*;
import java.util.concurrent.TimeUnit;
public class Login extends AppCompatActivity {

    private Button login ;
    private Retrofit retrofit ;
    private RetrofitInterface retrofitInterface ;
    private String URL = "http://10.0.2.2:3000/" ; //Ici il faut l'url
    protected LocationManager locationManager;
    protected LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        

        /**
         * Instanciation des paramètres
         */
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new CookiesSender(getApplicationContext())); 
        builder.addInterceptor(new CookiesReceiver(getApplicationContext())); 
        client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL) 
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build(); 

        retrofitInterface = retrofit.create(RetrofitInterface.class);
    

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        /**
         * When clicking on the submit button we handle the login
         */

        login = (Button) findViewById(R.id.submitarea) ;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
    }

    private void handleLogin() {
        EditText phonenum = findViewById(R.id.txtAdresseMail) ;
        EditText mdp = findViewById(R.id.txtpassword) ;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(mdp.getText().toString().getBytes());
        String encryptedPassword = new String(md.digest());
        ArrayList<String> contacts = getContacts();
        Location location=null;
        try {
           location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        catch(IllegalArgumentException | SecurityException e){
            e.printStackTrace();
        }
        if(location!=null ){
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
        
            Call<LoginResult> call = retrofitInterface.executeLogin(new LogInfo(phonenum.getText().toString(),encryptedPassword,longitude, latitude, contacts));
            call.enqueue(new Callback<LoginResult>() {
                @Override
                public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                    if (response.code() == 200) {
                        LoginResult loginResult = new LoginResult(response.body().getName(),response.body().getNumTel()) ;
                        Data.getInstance().saveData("CurrentUser", loginResult);
                        
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setTitle("Connexion Réussie");
                        builder.show();
                        openPrincipalmenu();
                    } else if(response.code() == 404) {
                        Toast.makeText(Login.this,"Données erronées",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(Login.this,"Erreur",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<LoginResult> call, Throwable t) {
                    Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
            builder.setTitle("Impossible d'obtenir la position");
            builder.show(); 
        }
        


    }
    /**
     * Opens the Principal menu
     * To be used after we successfully log in
     */
    public void openPrincipalmenu(){
        Intent intent = new Intent(this,scrollmenu.class);
        startActivity(intent);
        //finish();
    }


    public ArrayList<String> getContacts(){
        ArrayList<String> contacts = new ArrayList<String>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phoneNo = phoneNo.replace(" ", "");
                        phoneNo = phoneNo.replace("-", "");
                        phoneNo = phoneNo.replace("(", "");
                        phoneNo = phoneNo.replace(")", "");
                        contacts.add(phoneNo);
                    }
                    pCur.close();
                }
            }
        }
        return contacts;
    }

}
