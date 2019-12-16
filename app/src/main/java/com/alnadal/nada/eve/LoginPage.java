package com.alnadal.nada.eve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPage extends AppCompatActivity {

    private Button login;
    private EditText mEmail, mPassword;
    private TextView signUp, forgetPassword;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final String TAG = "FirstPage";
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mEmail = (EditText) findViewById(R.id.log_ed_email);
        mPassword = (EditText) findViewById(R.id.log_ed_password);
        login = (Button) findViewById(R.id.but_login);
        signUp = (TextView) findViewById(R.id.sign_up);
        forgetPassword = (TextView) findViewById(R.id.forget_password);

        mProgressDialog = new ProgressDialog(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginPage.this,SignUpPage.class);
                startActivity(intent);
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser()!= null){


                }

            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(LoginPage.this,ResetPasswordPage.class);
                startActivity(intent);
            }
        });

    }

    private  void startSignIn() {

        Log.d(TAG, "startSignIn: ");

        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        mProgressDialog = new ProgressDialog(this);

         if (TextUtils.isEmpty(email))
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
            mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    if (task.isSuccessful()) {
                        mProgressDialog.dismiss();
                        Intent intent = new Intent(LoginPage.this, HomePage.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginPage.this, "Email or Password is not correct ", Toast.LENGTH_LONG).show();
                    }


                }
            });

        }
    }

    }