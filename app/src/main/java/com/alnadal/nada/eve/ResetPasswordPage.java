package com.alnadal.nada.eve;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordPage extends AppCompatActivity {

    private static final String TAG ="ResetPasswordPage" ;
    private EditText mEnterEmail;
    private Button mNext;
    private FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_page);

        mEnterEmail=(EditText)findViewById(R.id.ed_reset_email);
        mNext=(Button)findViewById(R.id.mNext);

        mFirebaseAuth=FirebaseAuth.getInstance();

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendEmail();

            }
        });

    }

    public  void sendEmail() {

        String email = mEnterEmail.getText().toString().trim();
        mFirebaseAuth.useAppLanguage();
        if (TextUtils.isEmpty(email))
        {

            Toast.makeText(this, "Please Enter Your Email..", Toast.LENGTH_LONG).show();
        }
        else
        {
            mFirebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Log.d(TAG, "sendEmail: ");

                        Toast.makeText(ResetPasswordPage.this, "email send", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ResetPasswordPage.this, ResetPasswordSecondPage.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(ResetPasswordPage.this, "Plase enter correct email", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

}
