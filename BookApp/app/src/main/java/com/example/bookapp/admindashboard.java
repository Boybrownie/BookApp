package com.example.bookapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class admindashboard extends AppCompatActivity {

    private static final String TAG = "AdminDashboard";
        Button add_book_btn;
        private ListView bookListView;
        private AdminBookAdapter adapter;
        private List<Book> books;

        private DatabaseReference booksRef;

        @SuppressLint({"MissingInflatedId", "WrongViewCast"})
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admindashboard);

            bookListView = findViewById(R.id.bookListView);
            add_book_btn = findViewById(R.id.add_book_btn);

            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Librarytrac");
            setSupportActionBar(toolbar);


            booksRef = FirebaseDatabase.getInstance().getReference("books");

            books = new ArrayList<>();
            adapter = new AdminBookAdapter(this, books);
            bookListView.setAdapter(adapter);

            loadBooks();

            add_book_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(admindashboard.this, addeditbook.class);
                    startActivity(intent);
                }
            });
        }

        private void loadBooks() {
            booksRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    books.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            try {
                                Book book = snapshot.getValue(Book.class);
                                if (book != null) {
                                    book.setId(snapshot.getKey());
                                    books.add(book);
                                } else {
                                    Log.e(TAG, "Book is null for snapshot: " + snapshot);
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error retrieving book from snapshot: " + snapshot, e);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(admindashboard.this, "Failed to load books", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Database error: " + databaseError.getMessage(), databaseError.toException());
                }
            });
        }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.admin_menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_profile) {
                Intent intent = new Intent(admindashboard.this, profile.class);
                startActivity(intent);
                return true;

            }else if (itemId == R.id.menu_sign_out) {
                Intent intent = new Intent(admindashboard.this, signout.class);
                startActivity(intent);
                return true;
            }
            else if (itemId == R.id.menu_borrow) {

                Intent intent = new Intent(admindashboard.this, Adminborrow.class);
                startActivity(intent);
                return true;
            }
            else {
                return super.onOptionsItemSelected(item);
            }
        }
    }
