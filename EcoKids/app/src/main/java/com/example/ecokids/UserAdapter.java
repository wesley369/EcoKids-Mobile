package com.example.ecokids;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        super(context, 0, userList);
        this.context = context;
        this.userList = userList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item_user, parent, false);
        }

        User currentUser = userList.get(position);

        ImageView userAvatarImageView = listItemView.findViewById(R.id.userAvatar);
        TextView userNameTextView = listItemView.findViewById(R.id.userName);
        TextView userPointsTextView = listItemView.findViewById(R.id.userPoints);

        userNameTextView.setText(currentUser.getName());
        userPointsTextView.setText(String.valueOf(currentUser.getPoints()));
        Picasso.get().load(currentUser.getAvatarUrl()).into(userAvatarImageView);

        return listItemView;
    }
}
