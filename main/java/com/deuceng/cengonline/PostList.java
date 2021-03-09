package com.deuceng.cengonline;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import java.util.ArrayList;

import java.util.HashMap;

public class PostList extends ArrayAdapter<Post> {

    private Activity context;
    private ArrayList<Post> postArrayList;
    private HashMap<String, String> usersName;

    public PostList(Activity context, ArrayList<Post> posts, HashMap<String,String> usersName){
        super(context,R.layout.activity_stream_post_list, posts);
        this.context = context;
        this.postArrayList = posts;
        this.usersName = usersName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_stream_post_list,null,true);

        TextView textViewPostSenderName =(TextView) listViewItem.findViewById(R.id.textViewPostSenderName);
        TextView textViewPostSendDate= (TextView) listViewItem.findViewById(R.id.textViewPostSendDate);
        TextView textViewPostText=(TextView) listViewItem.findViewById(R.id.textViewPostText);

        Post post= postArrayList.get(position);
        textViewPostSenderName.setText("Sender: " + usersName.get(post.getSenderId()));
        textViewPostSendDate.setText(post.getDate());
        textViewPostText.setText(post.getText());

        return listViewItem;
    }

}
