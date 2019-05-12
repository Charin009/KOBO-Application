package com.example.charin.koboapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.charin.koboapplication.Object.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
private FirebaseAuth mAuth;
private EditText name, email, password, confirmPass;
private Button signUp, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        name = (EditText) findViewById(R.id.nameText);
        email = (EditText) findViewById(R.id.emailText2);
        password = (EditText) findViewById(R.id.passText2);
        confirmPass = (EditText) findViewById(R.id.confirmPassText);
        signUp = (Button) findViewById(R.id.signUpBtn2);
        cancel = (Button) findViewById(R.id.cancelBtn2);

    }

    public void signUpClicked(View view){
            final String getName = name.getText().toString();
            final String getEmail = email.getText().toString();
            final String getPassword = password.getText().toString();
            String getConfirmPass = confirmPass.getText().toString();

            if(getName.isEmpty()){
                name.setError("Name required");
                name.requestFocus();
                return;
            }

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

            if(getConfirmPass.isEmpty()){
                confirmPass.setError("Please confirm your password");
                confirmPass.requestFocus();
                return;
            }


            if(getPassword.equals(getConfirmPass)) {
                callsignup(getName,getEmail, getPassword);
            } else{
                Toast.makeText(RegisterActivity.this, "Your password and confirm password not match",
                        Toast.LENGTH_SHORT).show();
            }
    }

    public void cancelClicked(View view){
        clearEditText();
    }


    public void clearEditText(){
        name.getText().clear();
        email.getText().clear();
        password.getText().clear();
        confirmPass.getText().clear();

    }

    private void callsignup(final String name, final String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(name,email);
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            userRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Account created.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            // Sign in success, update UI with the signed-in user's information
                            clearEditText();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Test", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();

    }
}
