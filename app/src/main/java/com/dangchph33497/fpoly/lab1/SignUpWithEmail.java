package com.dangchph33497.fpoly.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpWithEmail extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ImageView btnBack;
    private EditText edtUsername,edtPassword;
    private com.google.android.material.button.MaterialButton btnSignUp;
    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_with_email);
        mAuth = FirebaseAuth.getInstance();
        btnBack = findViewById(R.id.btnBack);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpWithEmail.this, SignInWithEmail.class);
                startActivity(intent);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpWithEmail.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Main", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    Toast.makeText(getApplicationContext(), user.getEmail(), Toast.LENGTH_LONG).show();
                                } else {
                                    Log.w("Main", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpWithEmail.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });



    }
}