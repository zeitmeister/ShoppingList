package com.sporrong.shoppinglist2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationActivity extends AppCompatActivity{
    private FirebaseAuth mAuth;
    //Views
    private EditText emailEdit;
    private EditText passwordEdit;
    //Buttons
    private Button registerButton;
    private Button logInButton;
    private Button logOutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        mAuth = FirebaseAuth.getInstance();
        emailEdit = findViewById(R.id.emailInput);
        passwordEdit = findViewById(R.id.passwordInput);
        registerButton = findViewById(R.id.registerButton);
        logInButton = findViewById(R.id.logInBtn);
    }

    @Override
    protected void onStart() {
       super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AuthenticationActivity.this, RegisterActivity.class));
            }
        });

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(emailEdit.getText().toString(), passwordEdit.getText().toString());
            }
        });

        emailEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    emailEdit.setText("");
                }
            }
        });

        passwordEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    passwordEdit.setText("");
                }
            }
        });
    }





    private void signIn(String email, String password){
        if(email.length() == 0 || password.length() == 0){
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong login credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
