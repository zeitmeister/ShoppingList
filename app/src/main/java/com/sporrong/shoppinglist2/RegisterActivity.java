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
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    //Views
    private EditText regEmail;
    private EditText regPass1;
    private EditText regPass2;

    //Buttons
    private Button regButton;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        regEmail = findViewById(R.id.regEmail);
        regPass1 = findViewById(R.id.regPass1);
        regPass2 = findViewById(R.id.regPass2);

        regButton = findViewById(R.id.regButton);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(regEmail.getText().toString(), regPass1.getText().toString(), regPass2.getText().toString());
            }
        });
    }

    private void createAccount(String email, String password1, String password2) {
        String password;
        if(email.length() == 0 || password1.length() == 0 || password2.length() == 0 || !password1.equals(password2)){
            Toast.makeText(RegisterActivity.this, "Fields are empty or passwords doesn't match",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            password = password1;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    sendVerificationEmail();
                    startActivity(new Intent(RegisterActivity.this, AuthenticationActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this, "Authentication failed. Try Again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Send verification mail-method
    private void sendVerificationEmail() {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,
                            "Verification email sent to " + user.getEmail(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Hallådär!", "AuthenticationActivity", task.getException());
                    Toast.makeText(RegisterActivity.this,
                            "Failed to send verification email.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
