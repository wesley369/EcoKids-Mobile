package com.example.ecokids;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private LinearLayout firstPlaceContainer;
    private LinearLayout secondPlaceContainer;
    private LinearLayout thirdPlaceContainer;

    private ImageView firstPlaceAvatar;
    private TextView firstPlaceName;
    private TextView firstPlacePoints;

    private ImageView secondPlaceAvatar;
    private TextView secondPlaceName;
    private TextView secondPlacePoints;

    private ImageView thirdPlaceAvatar;
    private TextView thirdPlaceName;
    private TextView thirdPlacePoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        rankingListView = findViewById(R.id.rankingListView);
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(this, userList);
        rankingListView.setAdapter(userAdapter);

        firstPlaceContainer = findViewById(R.id.firstPlaceContainer);
        firstPlaceAvatar = findViewById(R.id.firstPlaceAvatar);
        firstPlaceName = findViewById(R.id.firstPlaceName);
        firstPlacePoints = findViewById(R.id.firstPlacePoints);

        secondPlaceContainer = findViewById(R.id.secondPlaceContainer);
        secondPlaceAvatar = findViewById(R.id.secondPlaceAvatar);
        secondPlaceName = findViewById(R.id.secondPlaceName);
        secondPlacePoints = findViewById(R.id.secondPlacePoints);

        thirdPlaceContainer = findViewById(R.id.thirdPlaceContainer);
        thirdPlaceAvatar = findViewById(R.id.thirdPlaceAvatar);
        thirdPlaceName = findViewById(R.id.thirdPlaceName);
        thirdPlacePoints = findViewById(R.id.thirdPlacePoints);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        loadRanking();
    }

    private void loadRanking() {
        usersRef.orderByChild("points").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }

                // Ordena a lista de usuários pelo número de pontos (decrescente)
                Collections.sort(userList, new Comparator<User>() {
                    @Override
                    public int compare(User u1, User u2) {
                        return Long.compare(u2.getPoints(), u1.getPoints());
                    }
                });

                // Atualiza os dados dos três primeiros colocados
                updateTopThreeUsers();

                // Atualiza o ListView com os novos dados
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Trata o erro de leitura dos dados
            }
        });
    }

    private void updateTopThreeUsers() {
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
    }
}
