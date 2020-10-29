package com.example.demo;
import android.app.Application;
import android.content.Context;
public class Demo extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        Demo.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Demo.context;
    }
}