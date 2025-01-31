package com.example.bookapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class borrowbook extends AppCompatActivity {

    private ListView bookListView;

        private List<Book> books;
        private BorrowAdapter adapter;
        private TextView noBookMsg;
        private Button btn_back;


        private DatabaseReference booksRef;
        FirebaseAuth mAuth;
        private  String userId;
        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_borrowbook);
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            if(user != null){
                userId = user.getUid();
            }



            // toolbar
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Central Library");
            setSupportActionBar(toolbar);



            noBookMsg = findViewById(R.id.noBookMsg);
            btn_back = findViewById(R.id.btn_back);
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            bookListView = findViewById(R.id.bookListView);




            books = new ArrayList<>();
            adapter = new BorrowAdapter(this, books);
            bookListView.setAdapter(adapter);

            booksRef = FirebaseDatabase.getInstance().getReference("borrow").child(userId);

            loadBooks();



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
                    }else {
                        noBookMsg.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(borrowbook.this, "Failed to load books", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Database error: " + databaseError.getMessage(), databaseError.toException());
                }
            });
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_profile) {
                fun_profile();
                return true;

            }else if (itemId == R.id.menu_sign_out) {
                fun_sign_out();
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        }




        private void fun_sign_out() {
            Intent intent = new Intent(borrowbook.this, signout.class);
            startActivity(intent);
        }



        private void fun_profile() {
            Intent intent = new Intent(borrowbook.this, profile.class);
            startActivity(intent);
        }
    }