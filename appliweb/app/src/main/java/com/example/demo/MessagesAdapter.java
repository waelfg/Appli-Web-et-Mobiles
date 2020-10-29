package com.example.demo;
import android.widget.ArrayAdapter;
import android.content.Context;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup ;
import android.widget.Adapter;
import android.widget.TextView;
import android.view.LayoutInflater ;
public class MessagesAdapter extends ArrayAdapter<ReceivedMessage> {

    public MessagesAdapter(Context context, ArrayList<ReceivedMessage> messages) {

       super(context, 0, messages);

    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {


        ReceivedMessage message = getItem(position);


       if (convertView == null) {

          convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);

       }


       TextView msg = (TextView) convertView.findViewById(R.id.message);


       msg.setText(message.toString());

       

       return convertView;

   }

}