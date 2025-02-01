package com.example.bookapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class addeditbook extends AppCompatActivity {


    private EditText edtTitle, edtAuthor,edtlanguage,edtgener, edtimage;
    private CheckBox checkboxAvailable;
    private Button btnAddEdit, btnBack;
    private DatabaseReference booksDatabase;
    private String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_addeditbook);

          edtTitle = findViewById(R.id.edtTitle);
                edtAuthor = findViewById(R.id.edtAuthor);
                edtlanguage = findViewById(R.id.languages);
                edtgener = findViewById(R.id.genere);
                edtimage = findViewById(R.id.image);
                checkboxAvailable = findViewById(R.id.checkboxAvailable);

                btnAddEdit = findViewById(R.id.btnAddEdit);
                btnBack = findViewById(R.id.btnBack);

                booksDatabase = FirebaseDatabase.getInstance().getReference("books");

                bookId = getIntent().getStringExtra("bookId");
                if (bookId != null) {
                    setTitle("Edit Book");
                    btnAddEdit.setText("Edit");
                    booksDatabase.child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String title = snapshot.child("title").getValue(String.class);
                                String author = snapshot.child("author").getValue(String.class);
                                String language = snapshot.child("language").getValue(String.class);
                                String genre = snapshot.child("genre").getValue(String.class);
                                String image = snapshot.child("image").getValue(String.class);
                                Boolean isAvailable = snapshot.child("isAvailable").getValue(Boolean.class);
                                edtTitle.setText(title);
                                edtAuthor.setText(author);
                                edtlanguage.setText(language);
                                edtgener.setText(genre);
                                edtimage.setText(image);
                                checkboxAvailable.setChecked(isAvailable != null && isAvailable);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    setTitle("Add Book");
                }

                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

                btnAddEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = edtTitle.getText().toString();
                        String author = edtAuthor.getText().toString();
                        String language = edtlanguage.getText().toString();
                        String gener = edtgener.getText().toString();
                        String image = edtimage.getText().toString();
                        boolean isAvailable = checkboxAvailable.isChecked();
                        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author) || TextUtils.isEmpty(language) || TextUtils.isEmpty(gener) ) {
                            Toast.makeText(addeditbook.this, "All fields are required", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (bookId != null) {
                            updateBook(title, author,language,gener, image, isAvailable);
                        } else {
                            addBook(title, author,language,gener,image, isAvailable);
                        }
                    }
                });

            }

            private void addBook(String title, String author,String language, String gener, String image, Boolean isAvailable) {
                String id = booksDatabase.push().getKey();
                Book book = new Book(id, title, author,language,gener,image, isAvailable);
                booksDatabase.child(id).setValue(book).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(addeditbook.this, "Book added", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(addeditbook.this, "Failed to add book", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            private void updateBook(String title, String author, String language, String gener, String image, boolean isAvailable) {
                Book book = new Book(bookId, title, author,language,gener, image, isAvailable);
                booksDatabase.child(bookId).setValue(book).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(addeditbook.this, "Book updated", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(addeditbook.this, "Failed to update book", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

