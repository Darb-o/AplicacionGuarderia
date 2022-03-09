package com.example.aplicacionguarderia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainPageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btnSignOut,btnSignUpAdmin;
    private FirebaseFirestore bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        mAuth = FirebaseAuth.getInstance();
        btnSignOut = (Button) findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(this::onClick);
        btnSignUpAdmin = (Button) findViewById(R.id.btnSignUpAdmin);
        btnSignUpAdmin.setOnClickListener(this::onClick);
        bd = FirebaseFirestore.getInstance();
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnSignOut:
                signOut();
                break;
            case R.id.btnSignUpAdmin:
                startActivity(new Intent(MainPageActivity.this,RegisterActivity.class));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(MainPageActivity.this,LoginActivity.class));
        }else{
            Toast.makeText(MainPageActivity.this,"Has iniciado sesion",Toast.LENGTH_SHORT).show();
        }
    }

    private void signOut(){
        mAuth.signOut();
        Toast.makeText(MainPageActivity.this,"Has cerrado sesion",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainPageActivity.this,LoginActivity.class));
    }
}