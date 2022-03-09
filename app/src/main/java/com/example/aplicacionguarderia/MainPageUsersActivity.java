package com.example.aplicacionguarderia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainPageUsersActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btnSignOut;
    private FirebaseFirestore bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_users);
        mAuth = FirebaseAuth.getInstance();
        btnSignOut = (Button) findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(this::onClick);
        bd = FirebaseFirestore.getInstance();
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnSignOut:
                signOut();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(MainPageUsersActivity.this,LoginActivity.class));
        }else{
            Toast.makeText(MainPageUsersActivity.this,"Has iniciado sesion",Toast.LENGTH_SHORT).show();
        }
    }

    private void signOut(){
        mAuth.signOut();
        Toast.makeText(MainPageUsersActivity.this,"Has cerrado sesion",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainPageUsersActivity.this,LoginActivity.class));
    }
}