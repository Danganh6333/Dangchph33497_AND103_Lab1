package com.dangchph33497.fpoly.lab1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class WithPhoneNumber extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private EditText edtPhoneNumber, edtOTP;
    private String mVerificationId;
    private com.google.android.material.button.MaterialButton btnGetOTP,btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_phone_number);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtOTP = findViewById(R.id.edtOTP);
        btnLogin = findViewById(R.id.btnLogin);
        btnGetOTP = findViewById(R.id.btnGetOTP);

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                edtOTP.setText(phoneAuthCredential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                mVerificationId = s;
            }
        };
        btnGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = edtPhoneNumber.getText().toString().trim();
                if (!phoneNumber.isEmpty()) {
                    getOTP(phoneNumber);
                } else {
                    Toast.makeText(WithPhoneNumber.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = edtOTP.getText().toString().trim();
                if (!otp.isEmpty()) {
                    verifyOTP(otp);
                } else {
                    Toast.makeText(WithPhoneNumber.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void getOTP(String phoneNumber) {
        Log.d("PhoneNumber",phoneNumber);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+1 "+phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyOTP(String code) {
        if (mVerificationId != null && !mVerificationId.isEmpty()) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        } else {
            // Handle the case where mVerificationId is not properly set
            Log.e("Verification Error", "Verification ID is null or empty");
            Toast.makeText(this, "Verification ID is null or empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(WithPhoneNumber.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(WithPhoneNumber.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(WithPhoneNumber.this, "Sign in failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}