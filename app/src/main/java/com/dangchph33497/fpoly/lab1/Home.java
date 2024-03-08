package com.dangchph33497.fpoly.lab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.dangchph33497.fpoly.lab1.List.DTO;
import com.dangchph33497.fpoly.lab1.List.RecycleViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity {
    FirebaseFirestore db;
    RecyclerView rc;
    RecycleViewAdapter adapter;
    Context context;
    com.google.android.material.button.MaterialButton btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db = FirebaseFirestore.getInstance();
        rc = findViewById(R.id.rc);
        btnAdd = findViewById(R.id.btnAdd);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogAddBox();
            }
        });

        firestore.collection("cities").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<DTO> dataList = new ArrayList<>();

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        DTO dto = documentSnapshot.toObject(DTO.class);
                        dataList.add(dto);
                    }
                    adapter = new RecycleViewAdapter(this, dataList, firestore);
                    rc.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                });
        rc.setLayoutManager(linearLayoutManager);
    }
    private void loadData() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("cities").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<DTO> dataList = new ArrayList<>();

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        DTO dto = documentSnapshot.toObject(DTO.class);
                        dataList.add(dto);
                    }
                    adapter = new RecycleViewAdapter(this, dataList, firestore);
                    rc.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Log.e("HomeActivity", "Failed to load data", e);
                });
    }
    void ShowDialogAddBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.activity_add_cities, null);
        builder.setView(view);

        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtState = view.findViewById(R.id.edtState);
        EditText edtCountry = view.findViewById(R.id.edtCountry);
        EditText edtPopulation = view.findViewById(R.id.edtPopulation);
        RadioButton radioCapital = view.findViewById(R.id.radioCapital);
        RadioButton radioNotCapital = view.findViewById(R.id.radioNotCapital);
        EditText edtRegions = view.findViewById(R.id.edtRegions);
        com.google.android.material.button.MaterialButton btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String state = edtState.getText().toString();
                String country = edtCountry.getText().toString();
                int population = Integer.parseInt(edtPopulation.getText().toString());
                boolean capital = radioCapital.isChecked();

                List<String> regions = Arrays.asList(edtRegions.getText().toString().split(","));

                if (name.isEmpty() || state.isEmpty() || country.isEmpty() || population == 0) {
                    Toast.makeText(Home.this, "Trống Dữ Liệu", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> cityData = new HashMap<>();
                cityData.put("name", name);
                cityData.put("state", state);
                cityData.put("country", country);
                cityData.put("capital", capital);
                cityData.put("population", population);
                cityData.put("regions", regions);

                db.collection("cities").add(cityData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("HomeActivity", "DocumentSnapshot added with ID: " + documentReference.getId());
                                dialog.dismiss();
                                loadData();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("HomeActivity", "Error adding document", e);
                            }
                        });
            }
        });
        dialog.show();
    }
    private void ghiDuLieu () {
        CollectionReference cities = db.collection("cities");

        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "San Francisco");
        data1.put("state", "CA");
        data1.put("country", "USA");
        data1.put("capital", false);
        data1.put("population", 860000);
        data1.put("regions", Arrays.asList("west_coast", "norcal"));
        cities.document("SF").set(data1);

        Map<String, Object> data2 = new HashMap<>();
        data2.put("name", "Los Angeles");
        data2.put("state", "CA");
        data2.put("country", "USA");
        data2.put("capital", false);
        data2.put("population", 3900000);
        data2.put("regions", Arrays.asList("west_coast", "socal"));
        cities.document("LA").set(data2);

        Map<String, Object> data3 = new HashMap<>();
        data3.put("name", "Washington D.C.");
        data3.put("state", null);
        data3.put("country", "USA");
        data3.put("capital", true);
        data3.put("population", 680000);
        data3.put("regions", Arrays.asList("east_coast"));
        cities.document("DC").set(data3);

        Map<String, Object> data4 = new HashMap<>();
        data4.put("name", "Tokyo");
        data4.put("state", null);
        data4.put("country", "Japan");
        data4.put("capital", true);
        data4.put("population", 9000000);
        data4.put("regions", Arrays.asList("kanto", "honshu"));
        cities.document("TOK").set(data4);

        Map<String, Object> data5 = new HashMap<>();
        data5.put("name", "Beijing");
        data5.put("state", null);
        data5.put("country", "China");
        data5.put("capital", true);
        data5.put("population", 21500000);
        data5.put("regions", Arrays.asList("jingjinji", "hebei"));
        cities.document("BJ").set(data5);
    }

    private void docDulieu () {
        DocumentReference docRef = db.collection("cities").document("SF");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("HomeActivity", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("HomeActivity", "No such document");
                    }
                } else {
                    Log.d("HomeActivity", "get failed with ", task.getException());
                }
            }
        });
    }
}