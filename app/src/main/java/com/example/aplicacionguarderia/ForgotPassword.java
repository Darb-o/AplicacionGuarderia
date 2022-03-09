package com.example.aplicacionguarderia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private TextInputLayout inputEmail;
    private Button btnForgotPassword;
    private FirebaseAuth mAuth;
    private TextView goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        inputEmail = findViewById(R.id.inputEmail);
        btnForgotPassword = (Button) findViewById(R.id.btnForgotPassword);
        btnForgotPassword.setOnClickListener(this::onClick);
        goBack = (TextView) findViewById(R.id.txtGoBack);
        goBack.setOnClickListener(this::onClick);
        mAuth = FirebaseAuth.getInstance();

    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnForgotPassword:
                resetPassword();
                break;
            case R.id.txtGoBack:
                startActivity(new Intent(ForgotPassword.this,LoginActivity.class));
                break;
        }
    }

    public void resetPassword(){
        String email = inputEmail.getEditText().getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            inputEmail.setError("Digite un correo electronico");
            inputEmail.requestFocus();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputEmail.setError("Digite un correo electronico valido");
            inputEmail.requestFocus();
        }else{
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ForgotPassword.this,"Verifica tu correo para restablecer la contraseña",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ForgotPassword.this,"Ocurrio un error, intentalo de nuevo",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}