package com.example.bookapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class deletebook extends AppCompatActivity {
    Button confirm_delete_book_btn, cancel_delete_book_btn;
    private DatabaseReference booksDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_deletebook);

        confirm_delete_book_btn = findViewById(R.id.confirm_delete_book_btn);
                cancel_delete_book_btn = findViewById(R.id.cancel_delete_book_btn);

                confirm_delete_book_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String bookId = getIntent().getStringExtra("bookId");
                        if (bookId != null) {
                            DatabaseReference booksDatabase = FirebaseDatabase.getInstance().getReference("books").child(bookId);
                            booksDatabase.removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "Book deleted successfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Failed to delete book: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                        }
                    }
                });


                cancel_delete_book_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });


            }
        }

