package com.deuceng.cengonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class AnnouncementStudentActivity extends AppCompatActivity {

    private class CourseStudent {
        private String TeacherID;
        private String CourseID;

        CourseStudent(){

        }

        public CourseStudent(String teacherID, String courseID) {
            TeacherID = teacherID;
            CourseID = courseID;
        }

        public String getTeacherID() {
            return TeacherID;
        }

        public void setTeacherID(String teacherID) {
            TeacherID = teacherID;
        }

        public String getCourseID() {
            return CourseID;
        }

        public void setCourseID(String courseID) {
            CourseID = courseID;
        }
    }

    private ListView listViewAnnouncementsStudent;
    private TextView textViewAnnouncementTitle;
    private Spinner spinnerAnnouncementsStudent;
    private ArrayAdapter<Course> adapter;

    private ArrayList<Course> courseArrayList;
    private ArrayList<String> courseIdArrayList;
    private ArrayList<String> announcementIdArrayList;
    private ArrayList<Announcement> announcementArrayList;

    private DatabaseReference databaseCourses;
    private DatabaseReference databaseStudents_Courses;
    private DatabaseReference databaseAnnouncement;
    private DatabaseReference databaseCourses_Announcement;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_student);

        listViewAnnouncementsStudent = (ListView) findViewById(R.id.listViewAnnouncementsStudent);
        textViewAnnouncementTitle = (TextView) findViewById(R.id.textViewAnnouncementTitle);
        spinnerAnnouncementsStudent = (Spinner) findViewById(R.id.spinnerAnnouncementsStudent);
        announcementArrayList = new ArrayList<>();
        announcementIdArrayList = new ArrayList<>();
        courseIdArrayList = new ArrayList<>();
        courseArrayList = new ArrayList<>();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        databaseStudents_Courses = FirebaseDatabase.getInstance().getReference("Students_Courses").child(userId);
        databaseCourses = FirebaseDatabase.getInstance().getReference("Courses");
        adapter = new ArrayAdapter<Course>(AnnouncementStudentActivity.this, android.R.layout.simple_spinner_dropdown_item, courseArrayList);
        spinnerAnnouncementsStudent.setAdapter(adapter);

        fetchStudentsCourse();
        fetchCourse();

        databaseCourses_Announcement = FirebaseDatabase.getInstance().getReference("Courses_Announcements");
        databaseAnnouncement = FirebaseDatabase.getInstance().getReference("Announcements");

        spinnerAnnouncementsStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCourseId = ((Course) spinnerAnnouncementsStudent.getSelectedItem()).getId();
                textViewAnnouncementTitle.setText(((Course) spinnerAnnouncementsStudent.getSelectedItem()).getName() + "'s Announcement List");
                fetchAnnouncement(selectedCourseId);
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

    private void fetchAnnouncement(final String selectedCourseId){

        databaseCourses_Announcement.child(selectedCourseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // courseTeacherIdList.clear();
                announcementIdArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    announcementIdArrayList.add(snapshot.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseAnnouncement.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // courseTeacherIdList.clear();
                announcementArrayList.clear();
                for(String announcementId : announcementIdArrayList){
                    Announcement announcement = dataSnapshot.child(announcementId).getValue(Announcement.class);
                    announcementArrayList.add(announcement);
                }
                AnnouncementList courseAdapter = new AnnouncementList(AnnouncementStudentActivity.this, announcementArrayList);
                //attaching adapter to the listview
                listViewAnnouncementsStudent.setAdapter(courseAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
