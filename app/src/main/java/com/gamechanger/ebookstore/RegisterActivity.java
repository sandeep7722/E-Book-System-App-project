package com.gamechanger.ebookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    Button btn_register;
    EditText name,email,password;
    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        btn_register=findViewById(R.id.btn_register);
        auth=FirebaseAuth.getInstance();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pro();
                final String txt_name=name.getText().toString().trim();
                final String txt_email=email.getText().toString().trim();
                String txt_pass=password.getText().toString().trim();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_pass))
                {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(),"Empty",Toast.LENGTH_SHORT).show();
                }
                else if (txt_pass.length()<6)
                {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(),"Password lenght error",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    auth.createUserWithEmailAndPassword(txt_email,txt_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {

                                reference= FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid());
                                HashMap<String ,String > hashMap=new HashMap<>();
                                hashMap.put("name",txt_name);
                                hashMap.put("email",txt_email);
                                hashMap.put("id",auth.getUid());
                                hashMap.put("imageURL","default");
                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            progress.dismiss();
                                            Toast.makeText(getApplicationContext(),"Successfuly registered",Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else
                                        {
                                            progress.dismiss();
                                            Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                            else
                            {
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
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
