package com.example.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {
    private ImageButton backBtn, profileEditBtn;
        private TextView nameTv, accountTypeTv;
        private com.google.android.material.imageview.ShapeableImageView profileIv;

        private FirebaseAuth firebaseAuth;
        private DatabaseReference userDatabase;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);

            // Initialize views
            backBtn = findViewById(R.id.backBtn);
            profileEditBtn = findViewById(R.id.profileEditBtn);
            nameTv = findViewById(R.id.nameTv);
            accountTypeTv = findViewById(R.id.accountTypeTv);
            profileIv = findViewById(R.id.profileIv);

            // Initialize Firebase components
            firebaseAuth = FirebaseAuth.getInstance();
            userDatabase = FirebaseDatabase.getInstance().getReference("users");

            // Load user info
            loadUserInfo();

            // Handle back button click
            backBtn.setOnClickListener(v -> onBackPressed());

            // Navigate to edit profile page
            profileEditBtn.setOnClickListener(v -> {
                Intent intent = new Intent(profile.this, editprofile.class);
                startActivity(intent);
            });
        }

        @Override
        protected void onResume() {
            super.onResume();
            // Refresh user info when returning to this activity
            loadUserInfo();
        }

        private void loadUserInfo() {
            // Get the currently logged-in user's UID
            String uid = firebaseAuth.getUid();

            if (uid == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            // Reference the user in the database
            userDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Check if user exists in the database
                    if (snapshot.exists()) {
                        // Retrieve user information
                        User user = snapshot.getValue(User.class);

                        if (user != null) {
                            // Set user information to UI components
                            nameTv.setText(user.getName() != null && !user.getName().isEmpty() ? user.getName() : "N/A");
                            accountTypeTv.setText(user.getRole() != null && !user.getRole().isEmpty() ? user.getRole() : "N/A");

                            // Load profile picture with Glide
                            Glide.with(profile.this)
                                    .load(user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty() ? user.getPhotoUrl() : R.drawable.ic_person_gray)
                                    .placeholder(R.drawable.ic_person_gray)
                                    .into(profileIv);
                        } else {
                            Toast.makeText(profile.this, "User data is null", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(profile.this, "User not found in the database", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database errors
                    Toast.makeText(profile.this, "Failed to load user info: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

