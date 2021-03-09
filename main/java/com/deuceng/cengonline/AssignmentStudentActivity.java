package com.deuceng.cengonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AssignmentStudentActivity extends AppCompatActivity {

    private ListView listViewAssignmentsStudent;
    private TextView textViewAssignmentsTitleStudent;
    private Spinner spinnerAssignmentsStudent;
    private ArrayAdapter<Course> adapter;

    private ArrayList<Course> courseArrayList;
    private ArrayList<String> courseIdArrayList;
    private ArrayList<String> assignmentIdArrayList;
    private ArrayList<Assignment> assignmentArrayList;

    private DatabaseReference databaseCourses;
    private DatabaseReference databaseStudents_Courses;
    private DatabaseReference databaseAssignments;
    private DatabaseReference databaseCourses_Assignments;

    private String userId;

    public static final String ASSIGNMENT_ID ="assignmentID";
    public static final String ASSIGNMENT_NAME = "assignmentName";
    public static final String ASSIGNMENT_INFO = "assignmentInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_student);

        listViewAssignmentsStudent = (ListView) findViewById(R.id.listViewAssignmentsStudent);
        textViewAssignmentsTitleStudent = (TextView) findViewById(R.id.textViewAssignmentsTitleStudent);
        spinnerAssignmentsStudent = (Spinner) findViewById(R.id.spinnerAssignmentsStudent);
        assignmentArrayList = new ArrayList<>();
        assignmentIdArrayList = new ArrayList<>();
        courseIdArrayList = new ArrayList<>();
        courseArrayList = new ArrayList<>();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        databaseStudents_Courses = FirebaseDatabase.getInstance().getReference("Students_Courses").child(userId);
        databaseCourses = FirebaseDatabase.getInstance().getReference("Courses");
        adapter = new ArrayAdapter<Course>(AssignmentStudentActivity.this, android.R.layout.simple_spinner_dropdown_item, courseArrayList);
        spinnerAssignmentsStudent.setAdapter(adapter);

        fetchStudentsCourse();
        fetchCourse();

        databaseCourses_Assignments = FirebaseDatabase.getInstance().getReference("Courses_Assignments");
        databaseAssignments = FirebaseDatabase.getInstance().getReference("Assignments");

        listViewAssignmentsStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Assignment assignment = assignmentArrayList.get(position);

                Intent intent = new Intent(getApplicationContext(), SubmittedWorkStudentActivity.class);
                intent.putExtra(ASSIGNMENT_ID,assignment.getId());
                intent.putExtra(ASSIGNMENT_NAME,assignment.getName());
                intent.putExtra(ASSIGNMENT_INFO,assignment.getInfo());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        spinnerAssignmentsStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCourseId = ((Course) spinnerAssignmentsStudent.getSelectedItem()).getId();
                textViewAssignmentsTitleStudent.setText(((Course) spinnerAssignmentsStudent.getSelectedItem()).getName() + "'s Assignments List");
                fetchAssignment(selectedCourseId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fetchStudentsCourse(){
        databaseStudents_Courses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseIdArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    courseIdArrayList.add(snapshot.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void fetchCourse(){

        databaseCourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // courseTeacherIdList.clear();
                courseArrayList.clear();
                for(String courseId : courseIdArrayList){
                    Course course = dataSnapshot.child(courseId).getValue(Course.class);
                    courseArrayList.add(course);
                }
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchAssignment(final String selectedCourseId){

        databaseCourses_Assignments.child(selectedCourseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // courseTeacherIdList.clear();
                assignmentIdArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    assignmentIdArrayList.add(snapshot.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseAssignments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // courseTeacherIdList.clear();
                assignmentArrayList.clear();
                for(String assignmentId : assignmentIdArrayList){
                    Assignment assignment = dataSnapshot.child(assignmentId).getValue(Assignment.class);
                    assignmentArrayList.add(assignment);
                }
                AssignmentList courseAdapter = new AssignmentList(AssignmentStudentActivity.this, assignmentArrayList);
                //attaching adapter to the listview
                listViewAssignmentsStudent.setAdapter(courseAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
