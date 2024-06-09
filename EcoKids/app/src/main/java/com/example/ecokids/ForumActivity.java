package com.example.ecokids;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ForumActivity extends AppCompatActivity {

    private EditText commentInput;
    private Button btnSubmit;
    private ListView commentsListView;
    private DatabaseReference commentsRef;
    private FirebaseAuth mAuth;
    private ArrayList<Comment> commentsList;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        commentInput = findViewById(R.id.commentInput);
        btnSubmit = findViewById(R.id.btnSubmit);
        commentsListView = findViewById(R.id.commentsListView);

        mAuth = FirebaseAuth.getInstance();
        commentsRef = FirebaseDatabase.getInstance().getReference("forum/comments");

        commentsList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentsList);
        commentsListView.setAdapter(commentAdapter);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentText = commentInput.getText().toString();
                if (!commentText.isEmpty()) {
                    addComment(commentText);
                } else {
                    Toast.makeText(ForumActivity.this, "Por favor, escreva um comentário", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadComments();
    }

    private void addComment(String commentText) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            long timestamp = System.currentTimeMillis();
            String commentId = commentsRef.push().getKey();

            if (commentId != null) {
                Comment comment = new Comment(userId, commentText, timestamp);
                commentsRef.child(commentId).setValue(comment);
                commentInput.setText("");
            }
        }
    }

    private void loadComments() {
        commentsRef.orderByChild("timestamp").limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    if (comment != null) {
                        commentsList.add(comment);
                    }
                }
                Collections.reverse(commentsList); // Reverses the list to show the most recent comments at the top
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ForumActivity.this, "Falha ao carregar comentários.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
