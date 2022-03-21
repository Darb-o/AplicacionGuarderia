package com.example.aplicacionguarderia;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddChildFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddChildFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AddChildFragment() {
        // Required empty public constructor
    }

    public static AddChildFragment newInstance(String param1, String param2) {
        AddChildFragment fragment = new AddChildFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //Variables globales
    private EditText inputNameText,inputFeeText,inputDateFormat,inputAllergiesText, inputParentText;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private int year=0,month=0,day=0;
    private CharSequence[] options = {"Cereales","Crustaceos","Huevos","Pescado","Cacahuates"
    ,"Soja","Leche","Frutos de cascara","Apio","Mostaza","Granos de Sesamo","Antioxidantes y Conservantes"
    ,"Altramuces","Moluscos"};
    private boolean[] selectedOptions = new boolean[options.length];
    private CharSequence[] adults;
    private List<ModelPerson> adultsList;
    private boolean[] selectedAdults;
    private ArrayList<String> listAllergies = new ArrayList<>();
    private ArrayList<String> listAdults = new ArrayList<>();
    private Button btnAddChild;
    private FirebaseFirestore bd;
    private FirebaseAuth mAuth;
    private ImageView inputPhoto;
    private FirebaseStorage storage;
    private Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_child, container, false);

        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        inputNameText = root.findViewById(R.id.inputNameText);
        inputFeeText = root.findViewById(R.id.inputFeeText);
        inputDateFormat = root.findViewById(R.id.inputDateFormat);
        inputAllergiesText = root.findViewById(R.id.inputAllergiesText);
        inputParentText = root.findViewById(R.id.inputParentText);
        inputPhoto = root.findViewById(R.id.inputPhoto);
        btnAddChild = root.findViewById(R.id.btnAddChild);
        adultsList = (List<ModelPerson>) getArguments().getSerializable("adultslist");
        fillArrays();
        listeners();
        instances();
        return root;
    }

    public void instances(){
        mAuth = FirebaseAuth.getInstance();
        bd = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public void listeners(){
        inputDateFormat.setOnClickListener(this::onClick);
        inputAllergiesText.setOnClickListener(this::onClick);
        inputParentText.setOnClickListener(this::onClick);
        inputPhoto.setOnClickListener(this::onClick);
        btnAddChild.setOnClickListener(this::onClick);
    }

    public void fillArrays(){
        String text = "";
        adults = new CharSequence[adultsList.size()];
        selectedAdults = new boolean[adults.length];
        for(int i = 0; i<adults.length; i++){
            text = adultsList.get(i).getName() + " - " + adultsList.get(i).getDni();
            adults[i] = text;
        }
    }

    public void onClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog;
        switch(v.getId()){
            case R.id.inputDateFormat:
                dateSetListener = new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth+"/"+month+"/"+year;
                        inputDateFormat.setText(date);
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
                break;
            case R.id.inputAllergiesText:
                builder.setCancelable(true)
                        .setTitle("Lista de alergias")
                        .setMultiChoiceItems(options, selectedOptions, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if(isChecked){
                                    listAllergies.add(String.valueOf(options[which]));
                                }else{
                                    listAllergies.remove(Integer.valueOf(which));
                                }
                            }
                        }).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = "";
                        for(String textList:listAllergies){
                            text+=textList+" ";
                        }
                        inputAllergiesText.setText(text);
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
                break;
            case R.id.inputParentText:
                builder.setCancelable(true)
                        .setTitle("Lista de personas registradas")
                        .setMultiChoiceItems(adults, selectedAdults, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if(isChecked){
                                    listAdults.add(String.valueOf(adults[which]));
                                }else{
                                    listAdults.remove(Integer.valueOf(which));
                                }
                            }
                        }).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = "";
                        for(String textList:listAdults){
                            text+=textList+" ";
                        }
                        inputParentText.setText(text);
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
                break;
            case R.id.btnAddChild:
                String name = inputNameText.getText().toString();
                String fee = inputFeeText.getText().toString();
                String birth = inputDateFormat.getText().toString();
                String date = day-1+"/"+month+1+"/"+year;
                String status = "activo";
                if(TextUtils.isEmpty(name)){
                    inputNameText.setError("Ingrese un nombre");
                    inputNameText.requestFocus();
                }else if(TextUtils.isEmpty(birth)){
                    inputDateFormat.setError("Ingrese fecha de nacimiento");
                    inputDateFormat.requestFocus();
                }else if(TextUtils.isEmpty(fee)){
                    inputFeeText.setError("Ingrese una identificacion");
                    inputFeeText.requestFocus();
                }else if(listAdults.isEmpty()){
                    inputParentText.setError("Seleccione al menos un responsable");
                }else if(imageUri==null) {
                    Toast.makeText(getActivity(), "Por favor selecciona una foto", Toast.LENGTH_SHORT).show();
                }else{
                    bd.collection("children").document(fee).get().addOnSuccessListener(documentSnapshot ->{
                        if(documentSnapshot.exists()){
                            Toast.makeText(getActivity(), "El niño ya se encuentra inscrito", Toast.LENGTH_SHORT).show();
                        }else{
                            StorageReference reference = storage.getReference().child("childrenProfilePhotos/"+name.trim()+fee);
                            reference.putFile(imageUri).addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                    reference.getDownloadUrl().addOnSuccessListener(uri -> {
                                        int i = 0;
                                        DocumentReference documentReference = bd.collection("children").document(fee);
                                        Map<String,Object> object = new HashMap<>();
                                        object.put("nombre",name);
                                        object.put("identificacion",fee);
                                        object.put("fecha_de_nacimiento",birth);
                                        object.put("fecha_inscripcion",date);
                                        object.put("estado",status);
                                        object.put("imagen",uri.toString());
                                        Map<String,Object> parents = new HashMap<>();
                                        for(String item: listAdults){
                                            parents.put(String.valueOf(i),item);
                                            i++;
                                        }
                                        object.put("responsables",parents);
                                        if(!listAllergies.isEmpty()){
                                            Map<String,Object> allergies = new HashMap<>();
                                            i = 0;
                                            for(String item: listAllergies){
                                                allergies.put(String.valueOf(i),item);
                                                i++;
                                            }
                                            object.put("alergias",allergies);
                                        }
                                        documentReference.set(object).addOnCompleteListener(task1 -> {
                                            if(task1.isSuccessful()){
                                                Toast.makeText(getActivity(), "Se ha inscrito el niño", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getContext(),MainPageActivity.class));
                                            }else{
                                                Toast.makeText(getActivity(), "Hubo un error, intentalo de nuevo", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    });
                                }
                            });
                        }
                    });
                }
                break;
            case R.id.inputPhoto:
                getContent.launch("image/*");
                break;
        }
    }

    ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if(result!=null){
                        inputPhoto.setImageURI(result);
                        imageUri = result;
                    }
                }
            });
}