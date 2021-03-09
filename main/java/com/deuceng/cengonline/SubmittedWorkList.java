package com.deuceng.cengonline;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SubmittedWorkList extends ArrayAdapter<SubmittedWork> {

    private Activity context;
    private ArrayList<SubmittedWork> assignmentSubmitted;
    private ArrayList<Student> students;

    public SubmittedWorkList(Activity context, ArrayList<SubmittedWork> assignmentSubmitted, ArrayList<Student> students) {
        super(context, R.layout.activity_summitted_work_list, assignmentSubmitted);
        this.context = context;
        this.assignmentSubmitted = assignmentSubmitted;
        this.students=students;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_summitted_work_list,null,true);

        TextView textViewAssignmentSubmittedStudentName = (TextView) listViewItem.findViewById(R.id.textViewAssignmentSubmittedStudentName);
        TextView textViewAssignmentSubmittedStudentNumber = (TextView) listViewItem.findViewById(R.id.textViewAssignmentSubmittedStudentNumber);
        TextView textViewAssignmentSubmittedIsCompleted = (TextView) listViewItem.findViewById(R.id.textViewAssignmentSubmittedSentDate);
        TextView textViewAssignmentSubmittedText = (TextView) listViewItem.findViewById(R.id.textViewAssignmentSubmittedText);

        SubmittedWork submittedWork = assignmentSubmitted.get(position);
        Student student = students.get(position);
        textViewAssignmentSubmittedStudentName.setText(student.getName());
        textViewAssignmentSubmittedStudentNumber.setText(String.valueOf(student.getNumber()));
        textViewAssignmentSubmittedIsCompleted.setText(submittedWork.getSentDate());
        textViewAssignmentSubmittedText.setText(submittedWork.getText());

        return listViewItem;
    }
}
