package com.deuceng.cengonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CourseStudentActivity extends AppCompatActivity {

    private ListView listViewCourseStudent;

    private DatabaseReference databaseStudents_Courses;
    private DatabaseReference databaseCourses;

    private ArrayList<Course> courseArrayList;
    private ArrayList<String> courseIdArrayList;

    private String userId;
    public static final String COURSE_ID = "courseID";
    public static final String COURSE_NAME ="courseName";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_student);

        listViewCourseStudent = (ListView) findViewById(R.id.listViewCourseStudent);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseStudents_Courses = FirebaseDatabase.getInstance().getReference("Students_Courses").child(userId);
        databaseCourses = FirebaseDatabase.getInstance().getReference("Courses");

        courseArrayList = new ArrayList<Course>();
        courseIdArrayList = new ArrayList<String>();

        listViewCourseStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Course course = courseArrayList.get(position);
                Intent intent = new Intent(getApplicationContext(), StreamStudentActivity.class);
                intent.putExtra(COURSE_ID, course.getId());
                intent.putExtra(COURSE_NAME,course.getName());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseStudents_Courses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // courseTeacherIdList.clear();
                courseIdArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    courseIdArrayList.add(snapshot.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseCourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // courseTeacherIdList.clear();
                courseArrayList.clear();
                for(String courseId : courseIdArrayList){
                    Course course = dataSnapshot.child(courseId).getValue(Course.class);
                    courseArrayList.add(course);
                }
                CourseList courseAdapter = new CourseList(CourseStudentActivity.this, courseArrayList);
                //attaching adapter to the listview
                listViewCourseStudent.setAdapter(courseAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
