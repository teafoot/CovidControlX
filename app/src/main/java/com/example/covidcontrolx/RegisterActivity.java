package com.example.covidcontrolx;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.covidcontrolx.utils.FormValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText txtRegisterEmail, txtRegisterPassword, txtRegisterPasswordRepeat;
    Button registerBtn;
    ImageView backBtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtRegisterEmail = findViewById(R.id.txtRegisterEmail);
        txtRegisterPassword = findViewById(R.id.txtRegisterPassword);
        txtRegisterPasswordRepeat = findViewById(R.id.txtRegisterPasswordRepeat);

        backBtn = findViewById(R.id.btnBackArrow);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        registerBtn = findViewById(R.id.btnRegister);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void register() {
        boolean emailVerification = FormValidation.validateEmail(this.txtRegisterEmail.getText().toString());
        boolean passwordVerification = FormValidation.validatePassword(this.txtRegisterPassword.getText().toString());
        boolean passwordRepeatVerification = FormValidation.validatePasswordRepeat(this.txtRegisterPassword.getText().toString(), this.txtRegisterPasswordRepeat.getText().toString());
        if(emailVerification && passwordVerification && passwordRepeatVerification){
            String email = txtRegisterEmail.getText().toString().trim();
            String password = txtRegisterPassword.getText().toString().trim();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Firebase couldn't register user.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
