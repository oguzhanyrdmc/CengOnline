package com.deuceng.cengonline;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AssignmentList extends ArrayAdapter<Assignment> {
    private Activity context;
    private ArrayList<Assignment> assignments;

    public AssignmentList(Activity context, ArrayList<Assignment> assignments) {
        super(context, R.layout.activity_assignment_list, assignments);
        this.context = context;
        this.assignments = assignments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_assignment_list,null,true);

        TextView textViewAssignmentName = (TextView) listViewItem.findViewById(R.id.textViewAssignmentName);
        TextView textViewAssignmentDueDate = (TextView) listViewItem.findViewById(R.id.textViewAssignmentDueDate);

        Assignment assignment = assignments.get(position);
        textViewAssignmentName.setText(assignment.getName());
        textViewAssignmentDueDate.setText(assignment.getDueDate());
        return listViewItem;
    }
}
