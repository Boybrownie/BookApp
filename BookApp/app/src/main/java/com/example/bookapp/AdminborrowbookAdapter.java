package com.example.bookapp;

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

public class AdminborrowbookAdapter extends BaseAdapter {

        private Context context;
        private List<User> users;
        private DatabaseReference mBooksDatabase;

        public AdminborrowbookAdapter(Context context, List<User> users) {
            this.context = context;
            this.users = users;
            mBooksDatabase = FirebaseDatabase.getInstance().getReference("books");
        }
        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public Object getItem(int position) {
            return users.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.admin_borrow_user_item, parent, false);
            }

            final User user = users.get(position);
            TextView userFirstName = convertView.findViewById(R.id.firstname);
            ImageButton viewBookBtn = convertView.findViewById(R.id.viewButton);

            userFirstName.setText(user.getName());


            viewBookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Adminviewborrowbook.class);
                    intent.putExtra("userId", user.getUserId());
                    context.startActivity(intent);
                }
            });

            return convertView;
        }
        public void updateData(List<User> data) {
            this.users.clear();
            this.users.addAll(data);
            notifyDataSetChanged();
        }
    }


