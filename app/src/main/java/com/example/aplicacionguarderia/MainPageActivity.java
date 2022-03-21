package com.example.aplicacionguarderia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore bd;
    private BottomNavigationView navigationView;
    private HomeFragment homeFragment = new HomeFragment();
    private List<ListElement> childrenList;
    private List<ModelPerson> adultsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        mAuth = FirebaseAuth.getInstance();
        bd = FirebaseFirestore.getInstance();
        navigationView = findViewById(R.id.bottom_navigation);
        navigationViewListener();
        loadfragment(homeFragment);
    }

    public void navigationViewListener(){
        navigationView.setOnItemSelectedListener(item -> {
            Fragment fragment;
            switch (item.getItemId()){
                case R.id.nav_child:
                    fragment = new ChildrenFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("childrenlist", (Serializable) childrenList);
                    bundle.putSerializable("adultslist",(Serializable) adultsList);
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;
                case R.id.nav_earnings:
                    fragment = new EarningsFragment();
                    loadfragment(fragment);
                    break;
                case R.id.nav_menu:
                    fragment = new MenuFragment();
                    loadfragment(fragment);
                    break;
                case R.id.nav_home:
                    fragment = new HomeFragment();
                    loadfragment(fragment);
                    break;
                case R.id.nav_sesion:
                    showDialog();
                    break;
            }
            return true;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(MainPageActivity.this,LoginActivity.class));
        }else{
            fillDataForFragments();
            Toast.makeText(MainPageActivity.this,"Has iniciado sesion",Toast.LENGTH_SHORT).show();
        }
    }

    private void fillDataForFragments(){
        bd.collection("children").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    String name = "", date = "", status = "", color = "", imgUrl = "";
                    childrenList = new ArrayList<>();
                    for(QueryDocumentSnapshot document: task.getResult()){
                        name = document.get("nombre").toString();
                        date = document.get("fecha_inscripcion").toString();
                        status = document.get("estado").toString();
                        imgUrl = document.get("imagen").toString();
                        if(status.equals("activo")){
                            color = "#69F13E";
                        }else{
                            color = "#F12B10";
                        }
                        childrenList.add(new ListElement(color,name,date,status,imgUrl));
                    }
                }else{
                    Toast.makeText(MainPageActivity.this,
                            "Error al llenar los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bd.collection("users").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                adultsList = new ArrayList<>();
                String name = "", dni = "", tipo = "";
                for(QueryDocumentSnapshot document: task.getResult()){
                    name = document.get("nombre").toString();
                    dni = document.get("dni").toString();
                    tipo = document.get("tipo").toString();
                    if(tipo.equals("usuario")){
                        adultsList.add(new ModelPerson(name,dni));
                    }
                }
            }else{
                Toast.makeText(MainPageActivity.this,
                        "Error al llenar los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signOut(){
        mAuth.signOut();
        Toast.makeText(MainPageActivity.this,"Has cerrado sesion",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainPageActivity.this,LoginActivity.class));
    }

    private void loadfragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainPageActivity.this);
        builder.setMessage("¿Quieres cerrar sesion?")
                .setPositiveButton("Si",(dialog, which) -> {
                    signOut();
                }).setNegativeButton("No",(dialog, which) -> {
                    dialog.dismiss();
                }).setCancelable(false).show();
    }
}