package com.example.demo;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.HashSet;
import android.widget.Toast;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.Request;
public class CookiesReceiver implements Interceptor {
    private Context context;
    public CookiesReceiver(Context context) {
        this.context = context;
    } 
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).getStringSet("PREF_COOKIES", new HashSet<String>());
            
            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }
            

            SharedPreferences.Editor memes = PreferenceManager.getDefaultSharedPreferences(context).edit();
            memes.putStringSet("PREF_COOKIES", cookies).apply();
            memes.commit();
        } 

        return originalResponse;
    }
}