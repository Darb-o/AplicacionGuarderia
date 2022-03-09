package com.example.aplicacionguarderia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView dontHaveAccount,forgetPassword;
    private TextInputLayout inputEmail,inputPassword;
    private Button btnLogin;
    private FirebaseFirestore bd;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        dontHaveAccount = (TextView) findViewById(R.id.dontHaveAccount);
        dontHaveAccount.setOnClickListener(this::onClick);
        forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(this::onClick);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this::onClick);
        bd = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.dontHaveAccount:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
            case R.id.btnLogin:
                userLogin();
                break;
            case R.id.forgetPassword:
                startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(user != null){
            mAuth.signOut();
        }
    }

    public void checkUser(){
        user = mAuth.getCurrentUser();
        bd.collection("users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String tipo = documentSnapshot.getString("tipo");
                    if(tipo.equals("usuario")){
                        startActivity(new Intent(LoginActivity.this,MainPageUsersActivity.class));
                    }else{
                        startActivity(new Intent(LoginActivity.this,MainPageActivity.class));
                    }
                }
            }
        });
    }

    public void userLogin(){
        String email = inputEmail.getEditText().getText().toString();
        String password = inputPassword.getEditText().getText().toString();
        if(TextUtils.isEmpty(email)){
            inputEmail.setError("Ingrese un correo");
            inputEmail.requestFocus();
        }else if(TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this,"Digite la contraseña",Toast.LENGTH_SHORT).show();
            inputPassword.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this,"Bienvenido",Toast.LENGTH_SHORT).show();
                        checkUser();
                    }else{
                        Log.w("TAB","Error",task.getException());
                    }
                }
            });
        }
    }


}