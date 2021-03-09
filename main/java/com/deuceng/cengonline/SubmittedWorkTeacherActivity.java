package com.deuceng.cengonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubmittedWorkTeacherActivity extends AppCompatActivity {

    private TextView textViewAssignmentName;
    private TextView textViewAssignmentInfo;
    private ListView listViewSubmittedWork;

    private String userId;
    private DatabaseReference databaseStudent;
    private DatabaseReference databaseSubmittedWork;
    private DatabaseReference databaseAssignment_SubmittedWorks;

    private ArrayList<SubmittedWork> submittedWorkArrayList;
    private ArrayList<Student> studentArrayList;
    private ArrayList<String> submittedWorkIdArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitted_work_teacher);


        Intent intent = getIntent();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String assignmentId = intent.getStringExtra(AssignmentTeacherActivity.ASSIGNMENT_ID);
        String assignmentName = intent.getStringExtra(AssignmentTeacherActivity.ASSIGNMENT_NAME);
        String assignmentInfo = intent.getStringExtra(AssignmentTeacherActivity.ASSIGNMENT_INFO);

        textViewAssignmentName = (TextView) findViewById(R.id.textViewAssignmentName);
        textViewAssignmentInfo = (TextView)findViewById(R.id.textViewAssignmentInfo);
        listViewSubmittedWork = (ListView) findViewById(R.id.listViewAssignmentSubmitted);

        databaseAssignment_SubmittedWorks = FirebaseDatabase.getInstance().getReference("Assignment_SubmittedWorks").child(assignmentId);
        databaseSubmittedWork = FirebaseDatabase.getInstance().getReference("SubmittedWorks");
        databaseStudent = FirebaseDatabase.getInstance().getReference("Users").child("Students");


        submittedWorkIdArrayList = new ArrayList<>();
        submittedWorkArrayList = new ArrayList<>();
        studentArrayList = new ArrayList<>();

        textViewAssignmentName.setText(assignmentName);
        textViewAssignmentInfo.setText(assignmentInfo);
        /*
        fetchDatabaseAssignmentSubmitted();
        fetchDatabaseStudent();
    */


    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseAssignment_SubmittedWorks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // courseTeacherIdList.clear();
                submittedWorkIdArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    submittedWorkIdArrayList.add(snapshot.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseSubmittedWork.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // courseTeacherIdList.clear();
                submittedWorkArrayList.clear();
                for(String submittedWorkId : submittedWorkIdArrayList){
                    SubmittedWork submittedWork = dataSnapshot.child(submittedWorkId).getValue(SubmittedWork.class);
                    submittedWorkArrayList.add(submittedWork);
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentArrayList.clear();
                for (SubmittedWork submittedWork : submittedWorkArrayList){
                    Student student = dataSnapshot.child(submittedWork.getStudentId()).getValue(Student.class);
                    studentArrayList.add(student);
                }
                SubmittedWorkList submittedWorkAdapter = new SubmittedWorkList(SubmittedWorkTeacherActivity.this, submittedWorkArrayList,studentArrayList);
                //attaching adapter to the listview
                listViewSubmittedWork.setAdapter(submittedWorkAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
