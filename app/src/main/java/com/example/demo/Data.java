package com.example.demo;
import java.util.HashMap;
import java.util.Map;
import java.lang.ref.WeakReference;



public class Data {
    Map<String, Object> data = new HashMap<String, Object>();

    private Data(){}

    private static final Data INSTANCE = new Data();

    public static Data getInstance(){
        return INSTANCE;
    }

    public void saveData(String id, Object object){
        data.put(id, object);
    }

    public Object retrieveData(String id){
        Object object = data.get(id);
        return object;
    }

    public void deleteData(String id){
        data.remove(id);
    }

}