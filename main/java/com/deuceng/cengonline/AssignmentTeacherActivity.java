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

public class AssignmentTeacherActivity extends AppCompatActivity {

    private Spinner spinnerCourse;
    private Spinner spinnerAssignments;

    private EditText editTextAssignmentName;
    private EditText editTextAssignmentDueDate;
    private EditText editTextAssignmentInfo;

    private TextView textViewAssignmentListTitle;

    private ListView listViewAssignments;
    private Button buttonAddAssignment;
    private ArrayList<Course> courseArrayList;
    private ArrayList<String> courseIdArrayList;
    private ArrayList<String> assignmentIdArrayList;
    private ArrayList<Assignment> assignmentArrayList;

    private ArrayAdapter<Course> adapter;
    private String userId;
    private String selectedCourseId;

    private DatabaseReference databaseTeachers_Courses;
    private DatabaseReference databaseCourses;
    private DatabaseReference databaseAssignments;
    private DatabaseReference databaseCourses_Assignments;

    public static final String COURSE_ID = "courseID";
    public static final String ASSIGNMENT_ID ="assignmentID";
    public static final String ASSIGNMENT_NAME = "assignmentName";
    public static final String ASSIGNMENT_DUE_DATE ="assignmentDueDate";
    public static final String ASSIGNMENT_INFO = "assignmentInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(true){
            setContentView(R.layout.activity_assignment_teacher);
        }
        else{
            setContentView(R.layout.activity_assignment_student);
        }

        spinnerCourse = (Spinner) findViewById(R.id.spinnerCourses);
        spinnerAssignments = (Spinner) findViewById(R.id.spinnerAssignments);
        textViewAssignmentListTitle = (TextView) findViewById(R.id.textViewAssignmentListTitle);
        editTextAssignmentName = (EditText) findViewById(R.id.editTextAssignmentName);
        editTextAssignmentDueDate = (EditText) findViewById(R.id.editTextAssignmentDueDate);
        editTextAssignmentInfo = (EditText) findViewById(R.id.editTextAssignmentInfo);
        listViewAssignments = (ListView) findViewById(R.id.listViewAssignments);
        buttonAddAssignment = (Button) findViewById(R.id.buttonAddAssignment);
        courseArrayList = new ArrayList<>();
        courseIdArrayList = new ArrayList<String>();
        assignmentIdArrayList = new ArrayList<>();
        assignmentArrayList = new ArrayList<>();

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseTeachers_Courses = FirebaseDatabase.getInstance().getReference("Teachers_Courses").child(userId);
        databaseCourses = FirebaseDatabase.getInstance().getReference("Courses");
        adapter = new ArrayAdapter<Course>(AssignmentTeacherActivity.this, android.R.layout.simple_spinner_dropdown_item, courseArrayList);
        spinnerCourse.setAdapter(adapter);
        spinnerAssignments.setAdapter(adapter);
        fetchCourse();

        databaseAssignments = FirebaseDatabase.getInstance().getReference("Assignments");
        databaseCourses_Assignments= FirebaseDatabase.getInstance().getReference("Courses_Assignments");

        buttonAddAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = databaseAssignments.push().getKey();
                String name = editTextAssignmentName.getText().toString().trim();
                String dueDate = editTextAssignmentDueDate.getText().toString().trim();
                String info = editTextAssignmentInfo.getText().toString().trim();
                String courseId = ((Course) spinnerCourse.getSelectedItem()).getId();

                if((!TextUtils.isEmpty(name)) && (!TextUtils.isEmpty((dueDate))) && (!TextUtils.isEmpty(info))){
                    Assignment assignment = new Assignment(id,name,info,dueDate);

                    databaseCourses_Assignments.child(courseId).child(id).setValue(true);
                    databaseAssignments.child(id).setValue(assignment);

                    editTextAssignmentName.setText("");
                    editTextAssignmentDueDate.setText("");
                    editTextAssignmentInfo.setText("");

                    Toast.makeText(AssignmentTeacherActivity.this, "Assignment is added", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(AssignmentTeacherActivity.this, "Please enter a name, due date and info", Toast.LENGTH_LONG).show();
                }
            }
        });

        spinnerAssignments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCourseId = ((Course) spinnerAssignments.getSelectedItem()).getId();;
                textViewAssignmentListTitle.setText(((Course) spinnerAssignments.getSelectedItem()).getName() + "'s Assignment List");
                fetchAssignment(selectedCourseId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listViewAssignments.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Assignment assignment = assignmentArrayList.get(i);
                showUpdateDeleteDialog(selectedCourseId,assignment.getId(),assignment.getName(),assignment.getDueDate(),assignment.getInfo());
                return true;
            }
        });

        listViewAssignments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Assignment assignment = assignmentArrayList.get(position);

                Intent intent = new Intent(getApplicationContext(), SubmittedWorkTeacherActivity.class);
                intent.putExtra(COURSE_ID, selectedCourseId);
                intent.putExtra(ASSIGNMENT_ID,assignment.getId());
                intent.putExtra(ASSIGNMENT_NAME,assignment.getName());
                intent.putExtra(ASSIGNMENT_DUE_DATE,assignment.getDueDate());
                intent.putExtra(ASSIGNMENT_INFO,assignment.getInfo());
                startActivity(intent);
            }
        });

    }

    private void fetchAssignment(String selectedCourseId){
        try {
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
                    AssignmentList assignmentListAdapter = new AssignmentList(AssignmentTeacherActivity.this, assignmentArrayList);
                    //attaching adapter to the listview
                    listViewAssignments.setAdapter(assignmentListAdapter);

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

    private void showUpdateDeleteDialog(final String courseId, final String assignmentId, String assignmentName, String assignmentDueDate,String assignmentInfo) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_assignment_edit, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextUpdateAssignmentName =(EditText) dialogView.findViewById(R.id.editTextUpdateAssignmentName);
        final EditText editTextUpdateAssignmentDueDate =(EditText) dialogView.findViewById(R.id.editTextUpdateAssignmentDueDate);
        final EditText editTextUpdateAssignmentInfo = (EditText) dialogView.findViewById(R.id.editTextUpdateAssignmentInfo);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateAssignment);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteAssignment);

        editTextUpdateAssignmentName.setText(assignmentName);
        editTextUpdateAssignmentDueDate.setText(assignmentDueDate);
        editTextUpdateAssignmentInfo.setText(assignmentInfo);

        dialogBuilder.setTitle(assignmentName);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextUpdateAssignmentName.getText().toString().trim();
                String dueDate = editTextUpdateAssignmentDueDate.getText().toString().trim();
                String info = editTextUpdateAssignmentInfo.getText().toString().trim();

                if ((!TextUtils.isEmpty(name)) && (!TextUtils.isEmpty(dueDate)) && (!TextUtils.isEmpty(info))) {
                    updateAssignment(courseId, assignmentId,name,dueDate,info);
                    alertDialog.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteAssignment(courseId, assignmentId);
                alertDialog.dismiss();

            }
        });
    }

    private boolean updateAssignment(final String courseId, final String id, String name, String dueDate, String info) {
        //getting the specified course reference
        DatabaseReference databaseUpdateRefAssignment = databaseAssignments.child(id);

        //updating assignment
        Assignment assignment = new Assignment(id,name,info,dueDate);

        databaseUpdateRefAssignment.setValue(assignment);
        Toast.makeText(getApplicationContext(), "Assignment is up to date" , Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteAssignment(final String courseId, final String id) {
        //getting the specified artist reference
        DatabaseReference databaseDeleteRefCourses_Assignment = databaseCourses_Assignments.child(courseId).child(id);
        databaseDeleteRefCourses_Assignment.removeValue();
        DatabaseReference databaseDeleteRefAssignment = databaseAssignments.child(id);
        databaseDeleteRefAssignment.removeValue();

        Toast.makeText(getApplicationContext(), "Assignment is deleted", Toast.LENGTH_LONG).show();
        return true;
    }
}
