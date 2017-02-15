package com.example.dellpc.start;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by dell pc on 14-Feb-17.
 */

public class UserAdapter extends ArrayAdapter<User> {
        public UserAdapter(Context context, int resource, List<User> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.user_list, parent, false);
            }

            ImageView photoImageView = (ImageView) convertView.findViewById(R.id.UserPic);
            TextView nameTextView = (TextView) convertView.findViewById(R.id.UserName);
            TextView emailTextView = (TextView) convertView.findViewById(R.id.UserEmail);

            User newUser = getItem(position);

            Glide.with(photoImageView.getContext())
                        .load(newUser.getPhotoUrl())
                        .into(photoImageView);

            nameTextView.setText(newUser.getName());
            emailTextView.setText(newUser.getEmail());

            return convertView;
        }
    }

