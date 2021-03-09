package com.deuceng.cengonline;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CourseList extends ArrayAdapter<Course> {

    private Activity context;
    private ArrayList<Course> courses;

    public CourseList(Activity context, ArrayList<Course> courses) {
        super(context, R.layout.activity_course_list, courses);
        this.context = context;
        this.courses = courses;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_course_list,null,true);
        TextView textViewCourseName = (TextView) listViewItem.findViewById(R.id.textViewCourseName);
        TextView textViewCourseCapacity = (TextView) listViewItem.findViewById(R.id.textViewCourseCapacity);

        Course course = courses.get(position);
        textViewCourseName.setText(course.getCode()+ " - " +course.getName());
        textViewCourseCapacity.setText("Capacity "+course.getCapacity());
        return listViewItem;
    }
}
