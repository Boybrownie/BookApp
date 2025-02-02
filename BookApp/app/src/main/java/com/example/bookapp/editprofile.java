package com.example.bookapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class editprofile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private DatabaseReference firebaseDatabase;

    private EditText nameEt;
    private ImageView profileIv;
    private Uri imageUri;
    private Button updateBtn;
    private TextView changeImageTv;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editprofile);

        // Initialize views
        ImageButton backBtn = findViewById(R.id.backBtn);
        nameEt = findViewById(R.id.nameEt);

        profileIv = findViewById(R.id.profileIv);
        changeImageTv = findViewById(R.id.changeImageTv);
        updateBtn = findViewById(R.id.updateBtn);

        // Initialize Firebase components
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("user_profile_pictures");
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Fetch user data and populate fields
        fetchUserData();

        // Handle back button click
        backBtn.setOnClickListener(v -> onBackPressed());

        // Handle change profile image click
        changeImageTv.setOnClickListener(v -> openFileChooser());

        // Handle update button click
        updateBtn.setOnClickListener(v -> validateAndUpdateProfile());
    }

    private void fetchUserData() {
        String userId = firebaseUser.getUid();

        firebaseDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        nameEt.setText(user.getName()); // Set user name
                        Glide.with(editprofile.this) // Load profile image
                                .load(user.getPhotoUrl())
                                .placeholder(R.drawable.ic_person_gray)
                                .into(profileIv);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(editprofile.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // Save selected image URI
            profileIv.setImageURI(imageUri); // Preview selected image
        }
    }

    private void validateAndUpdateProfile() {
        if (user == null) {
            user = new User(); // Initialize user object if null
        }

        // Update name
        user.setName(nameEt.getText().toString());



        // Update profile picture if new image is selected
        if (imageUri != null) {
            uploadImageToFirebaseStorage();
        } else {
            updateInFirebase(); // Update user info without changing the profile picture
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference fileReference = storageReference.child(firebaseUser.getUid() + ".jpg");

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            user.setPhotoUrl(uri.toString()); // Save image URL
                            updateInFirebase(); // Update user info in the database
                        })
                        .addOnFailureListener(e -> Toast.makeText(editprofile.this, "Failed to get image URL", Toast.LENGTH_SHORT).show()))
                .addOnFailureListener(e -> Toast.makeText(editprofile.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
    }

    private void updateInFirebase() {
        firebaseDatabase.child(firebaseUser.getUid()).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(editprofile.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity after successful update
                })
                .addOnFailureListener(e -> Toast.makeText(editprofile.this, "Error updating profile", Toast.LENGTH_SHORT).show());
    }
}
