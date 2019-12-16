package com.alnadal.nada.eve;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpSecondPage extends AppCompatActivity {
    private CircleImageView img;
    private Button next;
    private TextView TakePicturesTextBtn;
    private StorageReference mStorageReference;
    private String mUri;
    private  Uri mImageUri;
    private ProgressDialog progressDialog;
    private static final int CAMERA_REQUEST =1;
    private StorageTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_second_page);
        progressDialog=new ProgressDialog(SignUpSecondPage.this);
        img = (CircleImageView) findViewById(R.id.image);
        next = (Button)findViewById(R.id.but_continue_second);
        progressDialog=new ProgressDialog(this);

        TakePicturesTextBtn = (TextView)findViewById(R.id.text_Take_pictures_of_the_child);


        mStorageReference= FirebaseStorage.getInstance().getReference();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST);


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST  && resultCode == RESULT_OK){
            progressDialog.setMessage("Uploading...");
            progressDialog.show();


            //get the camera image
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] dataBAOS = baos.toByteArray();



            mStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://eve-1a120.appspot.com/uploads");


            //name of the image file (add time to have different files to avoid write on the same file)

            final StorageReference imagesRef = mStorageReference.child("filename" + new Date().getTime());

            //upload image

            final UploadTask uploadTask = imagesRef.putBytes(dataBAOS);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(), "Sending failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //Download URL from storage
                    Toast.makeText(getApplicationContext(), "Uploading image Success..", Toast.LENGTH_SHORT).show();

                    imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            // put uri as string in database
                            mUri = uri.toString();
                            DatabaseReference dataRefUrl =FirebaseDatabase.getInstance().getReferenceFromUrl("https://eve-1a120.firebaseio.com/image/imageURL");
                            dataRefUrl.setValue(mUri);

                            Intent intent=new Intent(SignUpSecondPage.this,LoginPage.class);
                            startActivity(intent);

                        }
                    });

                    progressDialog.dismiss();
                }
            });
        }
    }
}



