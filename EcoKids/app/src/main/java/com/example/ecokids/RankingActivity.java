package com.example.ecokids;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    private ListView rankingListView;
    private List<User> userList;
    private UserAdapter userAdapter;
    private DatabaseReference usersRef;
    private Handler handler;

    private ImageView firstPlaceAvatar, secondPlaceAvatar, thirdPlaceAvatar;
    private TextView firstPlaceName, firstPlacePoints, secondPlaceName, secondPlacePoints, thirdPlaceName, thirdPlacePoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        rankingListView = findViewById(R.id.rankingListView);
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(this, userList);
        rankingListView.setAdapter(userAdapter);

        firstPlaceAvatar = findViewById(R.id.firstPlaceAvatar);
        firstPlaceName = findViewById(R.id.firstPlaceName);
        firstPlacePoints = findViewById(R.id.firstPlacePoints);
        secondPlaceAvatar = findViewById(R.id.secondPlaceAvatar);
        secondPlaceName = findViewById(R.id.secondPlaceName);
        secondPlacePoints = findViewById(R.id.secondPlacePoints);
        thirdPlaceAvatar = findViewById(R.id.thirdPlaceAvatar);
        thirdPlaceName = findViewById(R.id.thirdPlaceName);
        thirdPlacePoints = findViewById(R.id.thirdPlacePoints);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        handler = new Handler();
        startAutoRefresh();
    }

    private void startAutoRefresh() {
        loadRanking();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadRanking();
                handler.postDelayed(this, 60000);
            }
        }, 60000);
    }

    private void loadRanking() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }


                Collections.sort(userList, new Comparator<User>() {
                    @Override
                    public int compare(User u1, User u2) {
                        return Long.compare(u2.getPoints(), u1.getPoints());
                    }
                });

                if (userList.size() > 0) {
                    User firstPlace = userList.get(0);
                    firstPlaceName.setText(firstPlace.getName());
                    firstPlacePoints.setText(String.valueOf(firstPlace.getPoints()));
                    Picasso.get().load(firstPlace.getAvatarUrl()).into(firstPlaceAvatar);
                }

                if (userList.size() > 1) {
                    User secondPlace = userList.get(1);
                    secondPlaceName.setText(secondPlace.getName());
                    secondPlacePoints.setText(String.valueOf(secondPlace.getPoints()));
                    Picasso.get().load(secondPlace.getAvatarUrl()).into(secondPlaceAvatar);
                }

                if (userList.size() > 2) {
                    User thirdPlace = userList.get(2);
                    thirdPlaceName.setText(thirdPlace.getName());
                    thirdPlacePoints.setText(String.valueOf(thirdPlace.getPoints()));
                    Picasso.get().load(thirdPlace.getAvatarUrl()).into(thirdPlaceAvatar);
                }

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
