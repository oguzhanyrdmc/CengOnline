package com.deuceng.cengonline;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AnnouncementList extends ArrayAdapter<Announcement> {

    private Activity context;
    private ArrayList<Announcement> announcements;

    public AnnouncementList(Activity context, ArrayList<Announcement> announcements) {
        super(context, R.layout.activity_course_list, announcements);
        this.context = context;
        this.announcements = announcements;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_announcement_list,null,true);

        TextView textViewAnnouncementTitle = (TextView) listViewItem.findViewById(R.id.textViewAnnouncementTitle);
        TextView textViewAnnouncementContent = (TextView) listViewItem.findViewById(R.id.textViewAnnouncementContent);

        Announcement announcement = announcements.get(position);
        textViewAnnouncementTitle.setText(announcement.getTitle());
        textViewAnnouncementContent.setText(announcement.getContent());
        return listViewItem;
    }
}