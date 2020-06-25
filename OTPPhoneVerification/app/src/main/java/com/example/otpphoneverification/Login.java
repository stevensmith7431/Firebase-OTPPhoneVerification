package com.example.otpphoneverification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    EditText mobilenumber;
    EditText code;
    Button verify;
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        mobilenumber = findViewById(R.id.numberid);
        code = findViewById(R.id.codeid);
        verify = findViewById(R.id.verifyid);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s_mobnum = mobilenumber.getText().toString();
                String s_code = code.getText().toString();

                String finalnumber = "+" + s_code + s_mobnum;

                if (s_mobnum.isEmpty() || s_code.isEmpty()){

                    Toast.makeText(Login.this, "mobile number should not empty", Toast.LENGTH_SHORT).show();

                } else {

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            finalnumber,60, TimeUnit.SECONDS,Login.this,callbacks
                    );
                }
            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                firebaseAuth.signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){

                                    Intent intent = new Intent(Login.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {

                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){

                                        Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(final String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Login.this,OTP.class);
                        intent.putExtra("Auth",s);
                        startActivity(intent);
                    }
                },10000
                );
            }
        };
    }
}
