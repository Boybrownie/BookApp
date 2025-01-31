package com.example.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    EditText Username, password;
    TextView userforgotpwd, usernewtolibrary;
    Button button;

    FirebaseAuth mAuth;
    DatabaseReference mUsersDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

                Username = findViewById(R.id.Email);
                password = findViewById(R.id.passwrd);
                button = findViewById(R.id.submitbutton);
                userforgotpwd = findViewById(R.id.forgotpassword);
                usernewtolibrary = findViewById(R.id.newtolibrary);
                mAuth = FirebaseAuth.getInstance();
                mUsersDatabase = FirebaseDatabase.getInstance().getReference("users");

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userlogin();
                    }
                });

                userforgotpwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent forgotpassword = new Intent(login.this, forgotpassword.class);
                        startActivity(forgotpassword);
                        finish();
                    }
                });

                usernewtolibrary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent Accountcreationpage = new Intent(login.this, signup.class);
                        startActivity(Accountcreationpage);
                        finish();
                    }
                });
            }

            private void userlogin() {
                String emailadd = Username.getText().toString();
                String passcode = password.getText().toString();
                DatabaseReference reference = FirebaseDatabase.getInstance()
                        .getReference("users");

                if (emailadd.isEmpty() || passcode.isEmpty()) {
                    Toast.makeText(login.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(emailadd, passcode).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();

                            mUsersDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        String role = dataSnapshot.child("role").getValue(String.class);
                                        if (role != null && role.equals("Admin")) {
                                            Intent k = new Intent(login.this, admindashboard.class);
                                            startActivity(k);
                                            finish();
                                        } else {
                                            Intent k = new Intent(login.this, dashboard.class);
                                            startActivity(k);
                                            finish();
                                        }
                                    } else {
                                        Toast.makeText(login.this, "Failed to retrieve user role", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(login.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(login.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login.this, "Sign In failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

