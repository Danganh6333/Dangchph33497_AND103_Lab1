package com.dangchph33497.fpoly.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class LogOut extends AppCompatActivity {
    com.google.android.material.button.MaterialButton btnLogOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_out);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogOut.this, MainActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
            }
        });
    }
}