package com.example.ecokids;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(@NonNull Context context, @NonNull List<User> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_ranking, parent, false);
        }

        User user = getItem(position);

        ImageView userAvatar = convertView.findViewById(R.id.userAvatar);
        TextView userName = convertView.findViewById(R.id.userName);
        TextView userPoints = convertView.findViewById(R.id.userPoints);

        if (user != null) {
            userName.setText(user.getName());
            userPoints.setText(String.valueOf(user.getPoints()));
            Picasso.get().load(user.getAvatarUrl()).into(userAvatar);
        }

        return convertView;
    }
}
