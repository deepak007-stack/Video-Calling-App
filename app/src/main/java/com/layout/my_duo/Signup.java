package com.layout.my_duo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.jar.Attributes;

public class Signup extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore database;
    private EditText Name,Password,Email;
    private Button createAcc,alreadyAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        Name = (EditText) findViewById(R.id.name);
        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);
        createAcc = (Button) findViewById(R.id.create_acc);
        alreadyAcc = (Button) findViewById(R.id.already_acc);

        alreadyAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Name.getText().toString();
                String email = Email.getText().toString();
                String password = Password.getText().toString();

                // First create the authentication account; then write non-sensitive profile
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = task.getResult() != null ? task.getResult().getUser() : null;
                            String uid = firebaseUser != null ? firebaseUser.getUid() : null;

                            User user = new User();
                            user.setName(name);
                            user.setEmail(email);

                            if (uid != null) {
                                database.collection("users")
                                        .document(uid)
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Signup.this, "Account created", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                finish();
                                            }
                                        });
                            } else {
                                Toast.makeText(Signup.this, "Account created, but user ID missing", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                            }
                        } else {
                            Toast.makeText(Signup.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



    }
}