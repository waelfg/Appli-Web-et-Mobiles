package com.example.demo;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.Request;
public class CookiesSender implements Interceptor {
    public static final String PREF_COOKIES = "PREF_COOKIES";
    
    private Context context;

    public CookiesSender(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        HashSet<String> preferences = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).getStringSet(PREF_COOKIES, new HashSet<String>());
        
        
        
        for (String cookie : preferences) {
          builder.addHeader("Cookie", cookie);
        }

        return chain.proceed(builder.build());
    }
}