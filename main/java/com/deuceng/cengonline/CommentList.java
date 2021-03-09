package com.deuceng.cengonline;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CommentList extends ArrayAdapter<Comment> {
    private Activity context;
    private ArrayList<Comment> commentArrayList;
    private HashMap<String, String> usersName;

    public CommentList(Activity context, ArrayList<Comment> comments, HashMap<String,String> usersName){
        super(context,R.layout.activity_stream_comment_list, comments);
        this.context = context;
        this.commentArrayList = comments;
        this.usersName = usersName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_stream_comment_list,null,true);


        TextView textViewCommentSenderName =(TextView) listViewItem.findViewById(R.id.textViewCommentSenderName);
        TextView textViewCommentSendDate= (TextView) listViewItem.findViewById(R.id.textViewCommentSendDate);
        TextView textViewCommentText=(TextView) listViewItem.findViewById(R.id.textViewCommentText);

        Comment comment= commentArrayList.get(position);
        textViewCommentSenderName.setText("Sender: " + usersName.get(comment.getSenderId()));
        textViewCommentSendDate.setText(comment.getDate());
        textViewCommentText.setText(comment.getText());

        return listViewItem;
    }
}
