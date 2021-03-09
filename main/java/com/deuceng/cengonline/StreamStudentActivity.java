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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;

public class StreamStudentActivity extends AppCompatActivity{
    private TextView textViewStreamTitleStudent;
    private ListView listViewStreamPostStudent;

    private String userId;
    private DatabaseReference databaseStream;
    private SimpleDateFormat sdf;

    private Stack<Post> postStackTemp;
    private ArrayList<Post> postArrayList;
    private ArrayList<Comment> commentArrayList;
    private ArrayList<String> commentIdArrayList;
    private HashMap<String, String> usersNameHashMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_student);

        textViewStreamTitleStudent = findViewById(R.id.textViewStreamTitleStudent);
        listViewStreamPostStudent = findViewById(R.id.listViewStreamPostStudent);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        sdf = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss");

        postStackTemp = new Stack<>();
        postArrayList = new ArrayList<>();
        usersNameHashMap = new HashMap<String, String>();
        commentArrayList = new ArrayList<>();
        commentIdArrayList = new ArrayList<>();
        fetchUsersName();

        Intent intent = getIntent();
        String courseID = intent.getStringExtra(CourseTeacherActivity.COURSE_ID);
        String courseName= intent.getStringExtra(CourseTeacherActivity.COURSE_NAME);
        databaseStream = FirebaseDatabase.getInstance().getReference("Stream").child(courseID);
        textViewStreamTitleStudent.setText(courseName+ " Stream");

        listViewStreamPostStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = postArrayList.get(position);
                openCommentsDialog(post.getId());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        databaseStream.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // courseTeacherIdList.clear();
                postStackTemp.clear();
                postArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    postStackTemp.push(post);
                }

                int size = postStackTemp.size();
                for (int i=0; i<size; i++){
                    postArrayList.add(postStackTemp.pop());
                }

                PostList postAdapter = new PostList(StreamStudentActivity.this,postArrayList,usersNameHashMap);
                listViewStreamPostStudent.setAdapter(postAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void fetchUsersName(){

        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersNameHashMap.clear();
                for (DataSnapshot userTypes: dataSnapshot.getChildren()){
                    for(DataSnapshot userIDs : userTypes.getChildren()){
                        String name = userIDs.child("name").getValue().toString();
                        String surname = userIDs.child("surname").getValue().toString();
                        String id = userIDs.child("id").getValue().toString();

                        usersNameHashMap.put(id,name+" "+surname);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void openCommentsDialog(final String postId){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_stream_comment, null);
        dialogBuilder.setView(dialogView);


        final ListView listViewCommentsList = dialogView.findViewById(R.id.listViewCommentsList);
        final EditText editTextSendComment = (EditText) dialogView.findViewById(R.id.editTextSendComment);
        final Button buttonSendComment = (Button) dialogView.findViewById(R.id.buttonSendComment);

        dialogBuilder.setTitle("Comments");
        final AlertDialog alertDialog = dialogBuilder.create();

        fetchComments(listViewCommentsList,postId);
        alertDialog.show();

        buttonSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editTextSendComment.getText().toString().trim();

                if (!TextUtils.isEmpty(text)) {
                    String commentId = databaseStream.child("Comments").push().getKey();
                    String currentDate = sdf.format(new Date());
                    Comment comment = new Comment(commentId,userId,text,currentDate);
                    databaseStream.child("PostsComments").child(postId).child(commentId).setValue(true);
                    databaseStream.child("Comments").child(commentId).setValue(comment);
                    editTextSendComment.setText("");
                }
            }
        });
    }

    private void fetchComments(final ListView listViewCommentsList, String postId){

        databaseStream.child("PostsComments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentIdArrayList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    commentIdArrayList.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseStream.child("Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentArrayList.clear();
                for (String commentId :commentIdArrayList){
                    Comment comment = dataSnapshot.child(commentId).getValue(Comment.class);
                    commentArrayList.add(comment);
                }

                CommentList commentAdapter = new CommentList(StreamStudentActivity.this,commentArrayList,usersNameHashMap);
                listViewCommentsList.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
