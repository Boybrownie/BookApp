package com.example.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {
    EditText ed1, ed2, ed4, ed5;
    Spinner roleSpinner;
    Button button;
    TextView txtview;
    FirebaseAuth mAuth;
    private DatabaseReference mUsersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);


        ed1 = findViewById(R.id.Accountemail);
        ed2 = findViewById(R.id.FirstName);
        ed4 = findViewById(R.id.password);
        ed5 = findViewById(R.id.Confirmpassword);
        roleSpinner = findViewById(R.id.roleSpinner);
        button = findViewById(R.id.submitbutton);
        txtview = findViewById(R.id.backLogin);

        mAuth = FirebaseAuth.getInstance();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference("users");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRegistration();
            }
        });

        txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(signup.this, login.class);
                startActivity(j);
                finish();
            }
        });
    }

    private void UserRegistration() {
        mUsersDatabase = FirebaseDatabase.getInstance().getReference("users");
        String emailaddress = ed1.getText().toString();
        String Fname = ed2.getText().toString();
        String pwdcode = ed4.getText().toString();
        String cpwd = ed5.getText().toString();
        String role = roleSpinner.getSelectedItem().toString();

        if (emailaddress.isEmpty() || Fname.isEmpty() || pwdcode.isEmpty() || cpwd.isEmpty()) {
            Toast.makeText(signup.this, "Enter all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pwdcode.equals(cpwd)) {
            ed5.setError("Password does not match");
            ed5.requestFocus();
            return;
        }

        if (pwdcode.length() < 8) {
            ed5.setError("Password should contain more than 8 characters");
            ed5.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailaddress).matches()) {
            ed1.setError("Email should be in correct format");
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailaddress, pwdcode).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userId = mAuth.getCurrentUser().getUid();
                    User user = new User(Fname, emailaddress, role);
                    mUsersDatabase.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(signup.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(signup.this, login.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(signup.this, "Failed to store user data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(signup.this, "Authentication failed: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signup.this, "Sign Up failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}