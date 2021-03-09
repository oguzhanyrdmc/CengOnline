package com.deuceng.cengonline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageList extends ArrayAdapter<Message> {

    private Activity context;
    private ArrayList<Message> messageArrayList;
    private HashMap<String, String> usersName;

    public MessageList(Activity context, ArrayList<Message> messages, HashMap<String,String> usersName){
        super(context,R.layout.activity_message_list, messages);
        this.context = context;
        this.messageArrayList = messages;
        this.usersName = usersName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_message_list,null,true);
        TextView textViewSenderName = (TextView) listViewItem.findViewById(R.id.textViewSenderName);
        TextView textViewMessageComingDate = (TextView) listViewItem.findViewById(R.id.textViewMessageComingDate);

        Message message = messageArrayList.get(position);
        textViewSenderName.setText("Sender: " + usersName.get(message.getFromID()));
        textViewMessageComingDate.setText(message.getDate());
        return listViewItem;
    }
}
