package com.gamechanger.ebookstore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class UploadActivity extends AppCompatActivity {
    Button btn_selectfile,btn_upload;
    EditText book_name;
    Uri pdfUri;
    FirebaseStorage storage;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressDialog dialog;
    StorageTask uploadTask;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        btn_selectfile=findViewById(R.id.btn_SelectFile);
        btn_upload=findViewById(R.id.btn_upload);
        book_name=findViewById(R.id.BookName);

        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();

        btn_selectfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(UploadActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    selectpdf();
                }
                else
                {
                    ActivityCompat.requestPermissions(UploadActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }
            }
        });
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pro();
                String book=book_name.getText().toString().trim();
                if (TextUtils.isEmpty(book))
                {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(),"Empty book name",Toast.LENGTH_LONG).show();
                }
                else {


                    if (pdfUri != null) {
                        uploadfile(pdfUri);
                        progress.dismiss();
                    } else {
                        progress.dismiss();
                        Toast.makeText(UploadActivity.this, "select file", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

    }
    void uploadfile(Uri pdfUri)
    {
        final String filename=System.currentTimeMillis()+".pdf";
        final String filename1=System.currentTimeMillis()+"";
        StorageReference storageReference=FirebaseStorage.getInstance().getReference("uploads");

        final StorageReference fileReference =storageReference.child("Uploads").child(filename);
        uploadTask=fileReference.putFile(pdfUri);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {

            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful())
                {
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                DatabaseReference reference=database.getReference("Books");
                if (task.isSuccessful())
                {
                    Uri downloaduri= (Uri) task.getResult();
                    String mUri=downloaduri.toString();
                    Toast.makeText(getApplicationContext(),mUri,Toast.LENGTH_LONG).show();
                    String b=book_name.getText().toString();
                    //*
                    reference.child(b).setValue(mUri).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(UploadActivity.this,"File successfully uploaded",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else {
                                Toast.makeText(UploadActivity.this,"File not uploaded",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    //**

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            selectpdf();
        }
    }

    void selectpdf()
    {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==86 && resultCode==RESULT_OK && data!=null)
        {
            pdfUri=data.getData();
        }
        else {
            Toast.makeText(UploadActivity.this,"Please select file",Toast.LENGTH_SHORT).show();

        }
    }
    void pro()
    {
        progress=new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage("please wait");
        progress.setTitle("Loding");
        progress.setCancelable(false);
        progress.show();
    }
}
