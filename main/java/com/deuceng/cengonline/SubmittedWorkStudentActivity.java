package com.deuceng.cengonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SubmittedWorkStudentActivity extends AppCompatActivity {

    private TextView textViewAssignmentNameStudent;
    private TextView textViewAssignmentInfoStudent;
    private EditText editTextAnnouncementContent;
    private Button buttonSubmitWork;
    private String userId;

    private DatabaseReference databaseAssignment_SubmittedWorks;
    private DatabaseReference databaseSubmittedWorks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitted_work_student);

        Intent intent = getIntent();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String assignmentId = intent.getStringExtra(AssignmentTeacherActivity.ASSIGNMENT_ID);
        String assignmentName = intent.getStringExtra(AssignmentTeacherActivity.ASSIGNMENT_NAME);
        String assignmentInfo = intent.getStringExtra(AssignmentTeacherActivity.ASSIGNMENT_INFO);

        textViewAssignmentNameStudent = (TextView) findViewById(R.id.textViewAssignmentNameStudent);
        textViewAssignmentNameStudent.setText(assignmentName);
        textViewAssignmentInfoStudent = (TextView) findViewById(R.id.textViewAssignmentInfoStudent);
        textViewAssignmentInfoStudent.setText(assignmentInfo);
        editTextAnnouncementContent = (EditText) findViewById(R.id.editTextAnnouncementContent);
        buttonSubmitWork = (Button) findViewById(R.id.buttonSubmitWork);

        databaseAssignment_SubmittedWorks = FirebaseDatabase.getInstance().getReference("Assignment_SubmittedWorks").child(assignmentId);
        databaseSubmittedWorks = FirebaseDatabase.getInstance().getReference("SubmittedWorks");

        buttonSubmitWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editTextAnnouncementContent.getText().toString();


                if(!TextUtils.isEmpty(text)){
                    String submittedWorkId = databaseSubmittedWorks.push().getKey();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss");
                    String currentDate = sdf.format(new Date());
                    SubmittedWork submittedWork = new SubmittedWork(submittedWorkId,userId,currentDate,text);

                    databaseAssignment_SubmittedWorks.child(submittedWorkId).setValue(true);
                    databaseSubmittedWorks.child(submittedWorkId).setValue(submittedWork);

                    Toast.makeText(SubmittedWorkStudentActivity.this, "Work is submitted", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),AssignmentStudentActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(SubmittedWorkStudentActivity.this, "Work is not empty", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
