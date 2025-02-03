
package com.example.bookapp;
import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Adminviewborrowbook extends AppCompatActivity {

        private TextView tvTitle, tvAuthor,tvlanguage,tvgener, tvavailability, tvBorrowDate;
        private ImageView ivimage;
        private Button  btnBack, btnReturn;
        private String bookId, title, author, language, gener, image, borrowDate ;
        private Boolean isAvailable;
        private DatabaseReference booksRef, books;
        FirebaseAuth mAuth;
        private  String userId;


        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_adminviewborrowbook);
            mAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = mAuth.getCurrentUser();
//        if(user != null){
//            userId = user.getUid();
//        }
            userId = getIntent().getStringExtra("userId");
            Log.e("userId", userId);
            // Toast.makeText(this, "userId"+ userId, Toast.LENGTH_SHORT).show();
            btnBack = findViewById(R.id.btnBack);
            btnReturn = findViewById(R.id.btnReturn);
            tvTitle = findViewById(R.id.Title);
            tvAuthor = findViewById(R.id.Author);
            tvlanguage = findViewById(R.id.Language);
            tvgener = findViewById(R.id.Genere);
            ivimage = findViewById(R.id.Image);
            //tvavailability = findViewById(R.id.Availability);
            //tvBorrowDate = findViewById(R.id.BorrowDate);
            //booksDatabase = FirebaseDatabase.getInstance().getReference("books");

            booksRef = FirebaseDatabase.getInstance().getReference("borrow").child(userId);
            bookId = getIntent().getStringExtra("bookId");
            Log.e("bookId", bookId);
            Toast.makeText(this, "Book id"+ bookId, Toast.LENGTH_SHORT).show();
            books = FirebaseDatabase.getInstance().getReference("books").child(bookId);
            if (bookId != null) {
                booksRef.child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            title = snapshot.child("title").getValue(String.class);
                            author = snapshot.child("author").getValue(String.class);
                            language = snapshot.child("language").getValue(String.class);
                            gener = snapshot.child("gener").getValue(String.class);
                            image = snapshot.child("image").getValue(String.class);
                            isAvailable = snapshot.child("isAvailable").getValue(Boolean.class);
                            borrowDate = snapshot.child("borrowDate").getValue(String.class);
                            tvTitle.setText("Title: " + title);
                            tvAuthor.setText("Author: " + author);
                            tvlanguage.setText("Language: "+ language);
                            tvgener.setText("Gener: " + gener);
                            ivimage.setImageURI(Uri.parse(image));
                            Picasso.get()
                                    .load(image)
                                    .into(ivimage);
                            // tvavailability.setText(Boolean.TRUE.equals(isAvailable) ? "Availability: Yes" : "Availability: No");
                            tvBorrowDate.setText("Borrowed Date: " + borrowDate);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                // can't find book
            }
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            btnReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isAvailable){
                        showBookOptions(bookId, title, author, language, gener, image, isAvailable);
                        Toast.makeText(Adminviewborrowbook.this, "This book return success.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Adminviewborrowbook.this, "This book can't return now, Try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private void showBookOptions(String bookId, String title, String author, String language, String gener, String image, Boolean isAvailable) {
            books.child("isAvailable").setValue(true);
            booksRef.child(bookId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Adminviewborrowbook.this, "Book returns success", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Adminviewborrowbook.this, "Book returns failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }