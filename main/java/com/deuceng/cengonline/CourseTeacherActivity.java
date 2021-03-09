package com.deuceng.cengonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CourseTeacherActivity<ArrayListList> extends AppCompatActivity {
    private EditText editTextCourseName;
    private EditText editTextCourseCode;
    private EditText editTextCourseCapacity;
    private Button buttonAddCourse;
    private ListView listViewCourse;

    private String userId;
    private ArrayList<Course> courseList;
    private ArrayList<String> courseIdList;
    private DatabaseReference databaseTeachers_Courses;
    private DatabaseReference databaseCourses;

    public static final String COURSE_ID = "courseID";
    public static final String COURSE_NAME ="courseName";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_teacher);


        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseTeachers_Courses = FirebaseDatabase.getInstance().getReference("Teachers_Courses").child(userId);
        databaseCourses = FirebaseDatabase.getInstance().getReference("Courses");
        editTextCourseName = (EditText) findViewById(R.id.editTextcourseName);
        editTextCourseCode = (EditText) findViewById(R.id.editTextcourseCode);
        editTextCourseCapacity = (EditText) findViewById(R.id.editTextcourseCapacity);
        buttonAddCourse = (Button) findViewById(R.id.buttonAddCourse);
        listViewCourse = (ListView) findViewById(R.id.listViewCourse);

        courseList = new ArrayList<Course>();
        courseIdList = new ArrayList<String>();

        buttonAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseName = editTextCourseName.getText().toString().trim();
                String courseCode = editTextCourseCode.getText().toString().trim();
                String courseCapacity = editTextCourseCapacity.getText().toString().trim();


                if((!TextUtils.isEmpty(courseName)) && (!TextUtils.isEmpty((courseCapacity)))){
                    String courseId = databaseCourses.push().getKey();

                    Course course = new Course(courseId,courseCode,courseName,courseCapacity);
                    //course add
                    databaseTeachers_Courses.child(courseId).setValue(true);
                    databaseCourses.child(courseId).setValue(course);


                    editTextCourseName.setText("");
                    editTextCourseCode.setText("");
                    editTextCourseCapacity.setText("");
                    Toast.makeText(CourseTeacherActivity.this, "Course added", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(CourseTeacherActivity.this, "Please enter a name and capacity", Toast.LENGTH_LONG).show();
                }
            }
        });

        listViewCourse.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Course course = courseList.get(i);
                showUpdateDeleteDialog(course.getId(),course.getCode(),course.getName(),course.getCapacity());
                return true;
            }
        });

        listViewCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Course course = courseList.get(position);
                Intent intent = new Intent(getApplicationContext(), StreamTeacherActivity.class);
                intent.putExtra(COURSE_ID, course.getId());
                intent.putExtra(COURSE_NAME,course.getName());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseTeachers_Courses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // courseTeacherIdList.clear();
                courseIdList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    courseIdList.add(snapshot.getKey());
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
                courseList.clear();
                for(String courseId : courseIdList){
                    Course course = dataSnapshot.child(courseId).getValue(Course.class);
                    courseList.add(course);
                }
                CourseList courseAdapter = new CourseList(CourseTeacherActivity.this, courseList);
                //attaching adapter to the listview
                listViewCourse.setAdapter(courseAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void showUpdateDeleteDialog(final String courseId, String courseCode, String courseName, String courseCapacity) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_course_edit, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextCourseName = (EditText) dialogView.findViewById(R.id.editTextcourseNameUpdate);
        final EditText editTextCourseCode = (EditText) dialogView.findViewById(R.id.editTextcourseCodeUpdate);
        final EditText editTextCourseCapacity = (EditText) dialogView.findViewById(R.id.editTextcourseCapacityUpdate);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateCourse);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteCourse);

        editTextCourseName.setText(courseName);
        editTextCourseCode.setText(courseCode);
        editTextCourseCapacity.setText(courseCapacity);

        dialogBuilder.setTitle(courseCode+ " - " + courseName);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextCourseName.getText().toString().trim();
                String code = editTextCourseCode.getText().toString().trim();
                String capacity = editTextCourseCapacity.getText().toString().trim();

                if ((!TextUtils.isEmpty(name)) && (!TextUtils.isEmpty(code)) && (!TextUtils.isEmpty(capacity))) {
                    updateCourse(courseId,name,code,capacity);
                    alertDialog.dismiss();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteCourse(courseId);
                alertDialog.dismiss();

            }
        });
    }

    private boolean updateCourse(String id, String name, String code,String capacity) {
        //getting the specified course reference
        DatabaseReference databaseUpdateRefCourses = FirebaseDatabase.getInstance().getReference("Courses").child(id);

        //updating course
        Course course = new Course(id,code,name,capacity);

        databaseUpdateRefCourses.setValue(course);
        Toast.makeText(getApplicationContext(), "Course is up to date" , Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteCourse(String id) {
        //getting the specified artist reference
        DatabaseReference databaseDeleteRefTeachers_courses = FirebaseDatabase.getInstance().getReference("Teachers_Courses").child(userId).child(id);
        databaseDeleteRefTeachers_courses.removeValue();
        DatabaseReference databaseDeleteRefCoursesTeacher = FirebaseDatabase.getInstance().getReference("Courses").child(id);
        databaseDeleteRefCoursesTeacher.removeValue();

        Toast.makeText(getApplicationContext(), "Course is deleted", Toast.LENGTH_LONG).show();
        return true;
    }
}

