package com.deuceng.cengonline;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AnnouncementTeacherActivity extends AppCompatActivity {

    private Spinner spinnerCourse;
    private Spinner spinnerAnnouncement;
    private EditText editTextAnnouncementTitle;
    private EditText editTextAnnouncementContent;
    private TextView textViewAnnouncementListTitle;
    private ListView listViewAnnouncement;
    private Button buttonAddAnnouncement;

    private ArrayList<Course> courseArrayList;
    private ArrayList<String> courseIdArrayList;
    private ArrayList<String> announcementIdArrayList;
    private ArrayList<Announcement> announcementArrayList;

    private ValueEventListener listener;
    private ArrayAdapter<Course> adapter;
    private DatabaseReference databaseAnnouncements;
    private DatabaseReference databaseCourses_Announcements;
    private DatabaseReference databaseTeachers_Courses;
    private DatabaseReference databaseCourses;


    private String userId;
    private String selectedCourseId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_teacher);


        spinnerCourse = (Spinner) findViewById(R.id.spinnerCourses);
        spinnerAnnouncement = (Spinner) findViewById(R.id.spinnerAnnouncements);
        editTextAnnouncementTitle = (EditText) findViewById(R.id.editTextAnnouncementTitle);
        editTextAnnouncementContent = (EditText) findViewById(R.id.editTextAnnouncementContent);
        textViewAnnouncementListTitle = (TextView) findViewById(R.id.textViewAnnouncementListTitle);
        listViewAnnouncement = (ListView) findViewById(R.id.listViewAnnouncement);
        buttonAddAnnouncement = (Button) findViewById(R.id.buttonAddAnnouncement);
        courseArrayList = new ArrayList<>();
        courseIdArrayList = new ArrayList<String>();
        announcementIdArrayList = new ArrayList<String>();
        announcementArrayList = new ArrayList<>();

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseTeachers_Courses = FirebaseDatabase.getInstance().getReference("Teachers_Courses").child(userId);
        databaseCourses = FirebaseDatabase.getInstance().getReference("Courses");
        adapter = new ArrayAdapter<Course>(AnnouncementTeacherActivity.this, android.R.layout.simple_spinner_dropdown_item, courseArrayList);
        spinnerCourse.setAdapter(adapter);
        spinnerAnnouncement.setAdapter(adapter);
        fetchCourse();


        databaseAnnouncements = FirebaseDatabase.getInstance().getReference("Announcements");
        databaseCourses_Announcements = FirebaseDatabase.getInstance().getReference("Courses_Announcements");

        buttonAddAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = databaseAnnouncements.push().getKey();
                String title = editTextAnnouncementTitle.getText().toString().trim();
                String content = editTextAnnouncementContent.getText().toString().trim();
                String courseId = ((Course) spinnerCourse.getSelectedItem()).getId();
                if((!TextUtils.isEmpty(title)) && (!TextUtils.isEmpty((content)))){
                    Announcement announcement = new Announcement(id,title,content);

                    databaseCourses_Announcements.child(courseId).child(id).setValue(true);
                    databaseAnnouncements.child(id).setValue(announcement);

                    editTextAnnouncementTitle.setText("");
                    editTextAnnouncementContent.setText("");

                    Toast.makeText(AnnouncementTeacherActivity.this, "Announcement is added", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(AnnouncementTeacherActivity.this, "Please enter a title and content", Toast.LENGTH_LONG).show();
                }
            }
        });


        spinnerAnnouncement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCourseId = ((Course) spinnerAnnouncement.getSelectedItem()).getId();
                textViewAnnouncementListTitle.setText(((Course) spinnerAnnouncement.getSelectedItem()).getName() + "'s Announcement List");
                fetchAnnouncement(selectedCourseId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        listViewAnnouncement.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Announcement announcement = announcementArrayList.get(i);
                showUpdateDeleteDialog(selectedCourseId,announcement.getId(),announcement.getTitle(),announcement.getContent());
                return true;
            }
        });
    }

   private void fetchAnnouncement(String selectedCourseId){
        try {
            databaseCourses_Announcements.child(selectedCourseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                announcementIdArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    announcementIdArrayList.add(snapshot.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseAnnouncements.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                announcementArrayList.clear();
                for(String announcementId : announcementIdArrayList){
                    Announcement announcement = dataSnapshot.child(announcementId).getValue(Announcement.class);
                    announcementArrayList.add(announcement);
                }
                AnnouncementList courseAdapter = new AnnouncementList(AnnouncementTeacherActivity.this, announcementArrayList);

                listViewAnnouncement.setAdapter(courseAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        }catch (NullPointerException e){

        }
    }

    private void fetchCourse(){
        try {

            databaseTeachers_Courses.addValueEventListener(new ValueEventListener() {
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
                    adapter.notifyDataSetChanged();

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }catch (NullPointerException e){

        }

    }

    private void showUpdateDeleteDialog(final String courseId, final String announcementId, String announcementTitle, String announcementContent) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_announcement_edit, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextUpdateAnnouncementTitle =(EditText) dialogView.findViewById(R.id.editTextUpdateAnnouncementTitle);
        final EditText editTextUpdateAnnouncementContent = (EditText) dialogView.findViewById(R.id.editTextUpdateAnnouncementContent);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateAnnouncement);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteAnnouncement);

        editTextUpdateAnnouncementTitle.setText(announcementTitle);
        editTextUpdateAnnouncementContent.setText(announcementContent);

        dialogBuilder.setTitle(announcementTitle);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextUpdateAnnouncementTitle.getText().toString().trim();
                String content = editTextUpdateAnnouncementContent.getText().toString().trim();

                if ((!TextUtils.isEmpty(title)) && (!TextUtils.isEmpty(content))) {
                    updateAnnouncement(announcementId,title,content);
                    alertDialog.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteAnnouncement(courseId, announcementId);
                alertDialog.dismiss();

            }
        });
    }

    private boolean updateAnnouncement( final String id, String title, String content) {
        //getting the specified course reference
        DatabaseReference databaseUpdateRefAnnouncement = databaseAnnouncements.child(id);

        //updating announcement
        Announcement announcement = new Announcement(id,title,content);

        databaseUpdateRefAnnouncement.setValue(announcement);
        Toast.makeText(getApplicationContext(), "Announcement is up to date" , Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteAnnouncement(final String courseId, final String id) {
        //getting the specified artist reference
        DatabaseReference databaseDeleteRefCourses_Announcements = databaseCourses_Announcements.child(courseId).child(id);
        databaseDeleteRefCourses_Announcements.removeValue();
        DatabaseReference databaseDeleteRefAnnouncement = databaseAnnouncements.child(id);
        databaseDeleteRefAnnouncement.removeValue();

        Toast.makeText(getApplicationContext(), "Announcement is deleted", Toast.LENGTH_LONG).show();
        return true;
    }




}
