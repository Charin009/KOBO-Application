package com.example.charin.koboapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.emailText);
        password = (EditText) findViewById(R.id.passText);
    }

    public void registerClicked(View view){
        Intent i = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(i);
    }

    public void signInClicked(View view){
        final String getEmail = email.getText().toString();
        final String getPassword = password.getText().toString();

        if(getEmail.isEmpty()){
            email.setError("Email required");
            email.requestFocus();
            return;
        }

        if(getPassword.isEmpty()){
            password.setError("Password required");
            password.requestFocus();
            return;
        }

        callsignin(getEmail, getPassword);
    }

    private void callsignin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(!task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Logging in failed.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent i = new Intent(MainActivity.this, LoadingActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        });
    }
}
