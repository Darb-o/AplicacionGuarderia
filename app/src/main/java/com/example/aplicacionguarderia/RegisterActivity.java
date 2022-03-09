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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextView txtCuenta;
    private TextInputLayout inputName,inputDni,inputAdress,inputPhone,inputPass,inputMail;
    private Button btnSignUp;
    private FirebaseAuth mAuth;
    private String userID;
    private FirebaseFirestore bd;
    private FirebaseUser user;
    private String type = "usuario";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtCuenta = (TextView) findViewById(R.id.txtCuenta);
        txtCuenta.setOnClickListener(this::onClick);
        inputName = findViewById(R.id.inputName);
        inputMail = findViewById(R.id.inputMail);
        inputPass = findViewById(R.id.inputPass);
        inputDni = findViewById(R.id.inputDni);
        inputAdress = findViewById(R.id.inputAdress);
        inputPhone = findViewById(R.id.inputPhone);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        mAuth = FirebaseAuth.getInstance();
        bd = FirebaseFirestore.getInstance();
        btnSignUp.setOnClickListener(this::onClick);
        user = mAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(user != null){
            type = "admin";
        }else{
            type = "usuario";
        }
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.txtCuenta:
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                break;
            case R.id.btnSignUp:
                createUser();
                break;
        }
    }

    private void createUser(){
        ModelPerson userdata = new ModelPerson(
                inputName.getEditText().getText().toString(),
                inputMail.getEditText().getText().toString(),
                inputPass.getEditText().getText().toString(),
                inputDni.getEditText().getText().toString(),
                inputAdress.getEditText().getText().toString(),
                inputPhone.getEditText().getText().toString()
                );
        if(userdata.check(userdata.getName())){
            inputName.setError("Ingrese un nombre");
            inputName.requestFocus();
        }else if(userdata.check(userdata.getMail())){
            inputMail.setError("Ingrese un correo");
            inputMail.requestFocus();
        }else if(userdata.check(userdata.getPass())){
            Toast.makeText(RegisterActivity.this,"Digite una contraseña",Toast.LENGTH_SHORT).show();
            inputPass.requestFocus();
        }else if(userdata.check(userdata.getDni())){
            inputDni.setError("Ingrese una identificacion");
            inputDni.requestFocus();
        }else if(userdata.check(userdata.getAdress())){
            inputAdress.setError("Ingrese una direccion");
            inputAdress.requestFocus();
        }else if(userdata.check(userdata.getPhone())){
            inputPhone.setError("Ingrese un telefono");
            inputPhone.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(userdata.getMail(),userdata.getPass()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        userID = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = bd.collection("users").document(userID);
                        Map<String,Object> userd = new HashMap<>();
                        userd.put("nombre", userdata.getName());
                        userd.put("correo", userdata.getMail());
                        userd.put("dni", userdata.getDni());
                        userd.put("direccion", userdata.getAdress());
                        userd.put("telefono", userdata.getPhone());
                        userd.put("tipo",type);
                        documentReference.set(userd).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("TAB","onSuccess: datos agregados"+userID);
                            }
                        });
                        Toast.makeText(RegisterActivity.this,"Usuario registrado con exito",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    }else{
                        Toast.makeText(RegisterActivity.this,"Hubo un error al registrar el usuario "
                                +task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}