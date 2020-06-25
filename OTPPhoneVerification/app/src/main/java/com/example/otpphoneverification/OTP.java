package com.example.otpphoneverification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTP extends AppCompatActivity {

    EditText otp;
    Button verify;
    private FirebaseAuth firebaseAuth;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);

        firebaseAuth = FirebaseAuth.getInstance();

        otp = findViewById(R.id.otpid);
        verify = findViewById(R.id.verifyid);

        id = getIntent().getStringExtra("Auth");

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s_otp = otp.getText().toString();

                if (s_otp.isEmpty()) {

                    Toast.makeText(OTP.this, "OTP should not empty", Toast.LENGTH_SHORT).show();

                } else {

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, s_otp);

                    firebaseAuth.signInWithCredential(credential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        Intent intent = new Intent(OTP.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {

                                        Toast.makeText(OTP.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() != null){

            Intent intent = new Intent(OTP.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

