package com.example.aplicacionguarderia;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChildrenFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ChildrenFragment() {
        // Required empty public constructor
    }

    public static ChildrenFragment newInstance(String param1, String param2) {
        ChildrenFragment fragment = new ChildrenFragment();
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


    private FloatingActionButton btn_addChild;
    private FirebaseFirestore db;
    private List<ListElement> childrenList;
    private List<ModelPerson> adultsList;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_children, container, false);
        btn_addChild = root.findViewById(R.id.btn_addChild);
        btn_addChild.setOnClickListener(this::onClick);
        db = FirebaseFirestore.getInstance();
        recyclerView = root.findViewById(R.id.childrenListRecyclerView);
        if(getArguments()!=null){
            adultsList = (List<ModelPerson>) getArguments().getSerializable("adultslist");
            childrenList = (List<ListElement>) getArguments().getSerializable("childrenlist");
        }
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        ListAdapter listAdapter = new ListAdapter(childrenList,getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_addChild:
                Fragment fragment = new AddChildFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("adultslist",(Serializable) adultsList);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }
}