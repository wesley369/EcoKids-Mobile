package com.example.ecokids;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CommentAdapter extends ArrayAdapter<Comment> {
    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        super(context, 0, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
        }

        TextView commentText = convertView.findViewById(R.id.commentText);
        TextView commentTimestamp = convertView.findViewById(R.id.commentTimestamp);

        if (comment != null) {
            commentText.setText(comment.getComment());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String formattedDate = sdf.format(new Date(comment.getTimestamp()));
            commentTimestamp.setText(formattedDate);
        }

        return convertView;
    }
}