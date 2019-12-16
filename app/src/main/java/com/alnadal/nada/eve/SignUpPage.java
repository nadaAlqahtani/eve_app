package com.alnadal.nada.eve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUpPage extends AppCompatActivity {

    private EditText mName,mChildName,mEmail,mPassword;
    private Button but_continue;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser user;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        mName = (EditText) findViewById(R.id.ed_name);
        mChildName = (EditText) findViewById(R.id.ed_child_name);
        mEmail = (EditText) findViewById(R.id.ed_email);
        mPassword = (EditText) findViewById(R.id.ed_password);
        but_continue = (Button) findViewById(R.id.but_continue);

        mFirebaseAuth=FirebaseAuth.getInstance();
        user=mFirebaseAuth.getCurrentUser();
        mDatabaseReference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://eve-1a120.firebaseio.com/Users");



        but_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration();
            }
        });
    }

    public  void registration() {

         final String email = mEmail.getText().toString().trim();
         String password = mPassword.getText().toString().trim();
         final String username = mName.getText().toString().trim();
         final String childName = mChildName.getText().toString().trim();
         mProgressDialog = new ProgressDialog(this);

        if (TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please enter your Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(childName))
        {
            Toast.makeText(this, "Please enter Child Name ", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please write your Email", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your Password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mProgressDialog.setTitle("Create Account");
            mProgressDialog.setMessage("Please wait, while we are checking the credentials.");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {
                        Toast.makeText(SignUpPage.this, "الرجاء اعادة المحاولة", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }
                    else
                    {


                        DatabaseReference reference=mDatabaseReference.child(username).child("User_info");
                        HashMap<String, Object> userdataMap = new HashMap<>();
                        userdataMap.put("UserName",username);
                        userdataMap.put("ChildName",childName);
                        userdataMap.put("Email",email);
                        reference.updateChildren(userdataMap);
                        mProgressDialog.dismiss();
                        Intent intent=new Intent(SignUpPage.this,SignUpSecondPage.class);
                        startActivity(intent);

                    }
                }
            });

        }
    }

    }


