package com.example.youchat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://console.firebase.google.com/project/youchat-ac267/database/youchat-ac267-default-rtdb/data/~2F?hl=en_GB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText name = findViewById(R.id.r_name);
         @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText mobile = findViewById(R.id.r_number);
         @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText Email= findViewById(R.id.r_email);
         @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final AppCompatButton registerBtn= findViewById(R.id.r_registerbtn);

        ProgressDialog progressDialog = new ProgressDialog (this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");



        registerBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 progressDialog.show();
                 final String nameTxt = name.getText().toString();
                 final String mobileTxt = mobile.getText().toString();
                 final  String emailTxt = Email.getText().toString();

                 if(nameTxt.isEmpty() || mobileTxt.isEmpty()|| emailTxt.isEmpty())
                 {
                     Toast.makeText(Register.this, "A l l  F i e l d s  R e q u i r e d  !!!", Toast.LENGTH_SHORT).show();
                     progressDialog.dismiss();
                 }
                 else {

                     databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot) {
                             progressDialog.dismiss();


                             if(snapshot.child("users").hasChild(mobileTxt))
                             {
                                 Toast.makeText(Register.this, "Mobile Already Exist", Toast.LENGTH_SHORT).show();
                             }
                             else {
                                 databaseReference.child("users").child(mobileTxt).child("email").setValue(emailTxt);
                                 databaseReference.child("users").child(mobileTxt).child("name").setValue(nameTxt);

                                 Toast.makeText(Register.this, "SuccessFully", Toast.LENGTH_SHORT).show();

                                 Intent intent = new Intent(Register.this,MainActivity.class);
                                 intent.putExtra("mobile",mobileTxt);
                                 intent.putExtra("name",nameTxt);
                                 intent.putExtra("email",emailTxt);
                                 startActivity(intent);
                                 finish();
                             }

                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {

                             progressDialog.dismiss();

                         }
                     });

                 }
             }
         });



    }
}