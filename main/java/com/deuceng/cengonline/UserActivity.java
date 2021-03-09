package com.deuceng.cengonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserActivity extends AppCompatActivity {

    private Button buttonCourse;
    private Button buttonAnnouncement;
    private Button buttonAssignment;
    private Button buttonMessage;
    private FirebaseAuth fAuth;
    private DatabaseReference fRef;
    private String userId;
    private String userType;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        userType = intent.getStringExtra("userType");

        userName = (TextView) findViewById(R.id.userName);


        fRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userType).child(userId);

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String surname = dataSnapshot.child("surname").getValue().toString();
                userName.setText(name+" "+ surname);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        buttonCourse = (Button) findViewById(R.id.btnCourse);
        buttonCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (userType){
                    case "Teachers":
                        openCourseTeacherActivity();
                        break;
                    case "Students":
                        openCourseStudentActivity();
                        break;
                }
            }
        });

        buttonAnnouncement = (Button) findViewById(R.id.btnAnnouncement);
        buttonAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (userType){
                    case "Teachers":
                        openAnnouncementTeacherActivity();
                        break;
                    case "Students":
                        openAnnouncementStudentActivity();
                        break;
                }
            }
        });

        buttonAssignment = (Button) findViewById(R.id.btnAssignment);
        buttonAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (userType){
                    case "Teachers":
                        openAssignmentTeacherActivity();
                        break;
                    case "Students":
                        openAssignmentStudentActivity();
                        break;
                }
            }
        });

        buttonMessage = (Button) findViewById(R.id.btnMessage);
        buttonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMessageActivity();
            }
        });
    }

    public void openCourseTeacherActivity(){
        Intent intent = new Intent(this, CourseTeacherActivity.class);
        startActivity(intent);
    }

    public void openCourseStudentActivity(){
        Intent intent = new Intent(this, CourseStudentActivity.class);
        startActivity(intent);
    }

    public void openAnnouncementTeacherActivity(){
        Intent intent = new Intent(this, AnnouncementTeacherActivity.class);
        startActivity(intent);
    }
    public void openAnnouncementStudentActivity(){
        Intent intent = new Intent(this, AnnouncementStudentActivity.class);
        startActivity(intent);
    }

    public void openAssignmentTeacherActivity(){
        Intent intent = new Intent(this, AssignmentTeacherActivity.class);
        startActivity(intent);
    }

    public void openAssignmentStudentActivity(){
        Intent intent = new Intent(this, AssignmentStudentActivity.class);
        startActivity(intent);
    }

    public void openMessageActivity(){
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }

}
