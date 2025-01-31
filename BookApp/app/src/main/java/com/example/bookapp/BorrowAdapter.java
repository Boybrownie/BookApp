package com.example.bookapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BorrowAdapter extends BaseAdapter {
        private Context context;
        private List<Book> books;
        private DatabaseReference mBooksDatabase;
        ImageButton view_book_btn, delete_book_btn;

        public BorrowAdapter(Context context, List<Book> books){
            this.context = context;
            this.books = books;
            mBooksDatabase = FirebaseDatabase.getInstance().getReference("books");
        }
        @Override
        public int getCount() {
            return books.size();
        }

        @Override
        public Object getItem(int position) {
            return books.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("WrongViewCast")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.user_borrow_book_item, parent, false);
            }
            final Book book = books.get(position);
            TextView bookTitle = convertView.findViewById(R.id.bookTitle);
            TextView bookAuthor = convertView.findViewById(R.id.bookAuthor);
            TextView bookAvailability = convertView.findViewById(R.id.bookAvailability);
            bookTitle.setText(book.getTitle());
            bookAuthor.setText(book.getAuthor());

            view_book_btn = convertView.findViewById(R.id.viewButton);
            view_book_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, viewborrowbook.class);
                    intent.putExtra("bookId", book.getId());
                    context.startActivity(intent);
                }
            });
            return convertView;
        }
    }


