package com.example.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class signout extends AppCompatActivity {

    Button button_yes, button_no;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signout);

        mAuth = FirebaseAuth.getInstance();
                button_yes = findViewById(R.id.btn_yes);
                button_no = findViewById(R.id.btn_no);

                Toolbar toolbar = findViewById(R.id.toolbar);
                toolbar.setTitle("Central Library");
                setSupportActionBar(toolbar);


                button_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                button_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAuth.signOut();
                        Toast.makeText(signout.this, "Successfully Logout.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(signout.this, login.class);
                        startActivity(intent);
                    }
                });
            }

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
                } else if (itemId == R.id.menu_borrow) {
                    fun_borrow();
                    return true;
                } else {
                    return super.onOptionsItemSelected(item);
                }
            }

            private void fun_sign_out() {
                Intent intent = new Intent(signout.this, signout.class);
                startActivity(intent);
            }



            private void fun_profile() {
                Intent intent = new Intent(signout.this, profile.class);
                startActivity(intent);
            }
            private void fun_borrow() {
                Intent intent = new Intent(signout.this, borrowbook.class);
                startActivity(intent);
            }
        }