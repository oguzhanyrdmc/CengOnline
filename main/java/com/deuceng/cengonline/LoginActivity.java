package com.deuceng.cengonline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail,mPassword;
    private Button mLoginBtn;
    private RadioGroup userTypes;
    private FirebaseAuth fAuth;
    private DatabaseReference dRef;
    private String userType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.loginButton);
        userTypes = findViewById(R.id.userTypeRadioGroup);
        fAuth = FirebaseAuth.getInstance();

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int checkId = userTypes.getCheckedRadioButtonId();
                if(checkId == -1){
                    Toast.makeText(LoginActivity.this, "Please choose a type", Toast.LENGTH_SHORT).show();
                }
                else{
                    switch (checkId){
                        case R.id.radioTeacher:
                            userType = "Teachers";
                            break;
                        case R.id.radioStudent:
                            userType = "Students";
                            break;
                    }
                }

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmail.setError("Email is not valid.");
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            dRef = FirebaseDatabase.getInstance().getReference("Users").child(userType).child(userId);
                            dRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    System.out.println(dataSnapshot.getValue());
                                    if(dataSnapshot.getValue() == null){
                                        Toast.makeText(LoginActivity.this, "Invalid User Type", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),UserActivity.class);
                                        intent.putExtra("userType",userType);
                                        startActivity(intent);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }else {
                            Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}


