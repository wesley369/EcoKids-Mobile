package com.example.ecokids;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView ivAvatar;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ivAvatar = findViewById(R.id.ivAvatar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference userRef = db.collection("users").document(userId);
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String avatarUrl = document.getString("avatarUrl");
                        if (avatarUrl != null && !avatarUrl.isEmpty()) {
                            Glide.with(MainActivity.this)
                                    .load(avatarUrl)
//                                    .placeholder(R.drawable.placeholder_avatar) // placeholder se a imagem estiver sendo carregada
//                                    .error(R.drawable.error_avatar) // imagem de erro caso ocorra algum problema
                                    .into(ivAvatar);
                        }
                    }
                }
            });
        }

//        Button btnTela = (Button) findViewById(R.id.btnTela);
//        btnTela.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
//                startActivity(intent);
//            }
//        });

        ImageView imgLogin = (ImageView) findViewById(R.id.ivAvatar);
        imgLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

//        Button btnPersonagem = (Button) findViewById(R.id.btnPersonagem);
//        btnPersonagem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getBaseContext(), CharacterSelectionActivity.class);
//                startActivity(intent);
//            }
//        });

        ImageView imgPersonagem = (ImageView) findViewById(R.id.ImgPersonagem);
        imgPersonagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CharacterSelectionActivity.class);
                startActivity(intent);
            }
        });

//        Button btnForum = (Button) findViewById(R.id.btnForum);
//        btnForum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getBaseContext(), ForumActivity.class);
//                startActivity(intent);
//            }
//        });

        ImageView imgForum = (ImageView) findViewById(R.id.ImgForum);
        imgForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ForumActivity.class);
                startActivity(intent);
            }
        });

//        Button btnRanking = (Button) findViewById(R.id.btnRanking);
//        btnRanking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getBaseContext(), RankingActivity.class);
//                startActivity(intent);
//            }
//        });

        ImageView imgRaking = (ImageView) findViewById(R.id.ImgRaking);
        imgRaking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RankingActivity.class);
                startActivity(intent);
            }
        });

//        Button btnMetas = (Button) findViewById(R.id.btnMetas);
//        btnMetas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getBaseContext(), ToDoListActivity.class);
//                startActivity(intent);
//            }
//        });

        ImageView imgMetas = (ImageView) findViewById(R.id.ImgMetas);
        imgMetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ToDoListActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (itemId == R.id.Metas) {
            startActivity(new Intent(this, ToDoListActivity.class));
            return true;
        } else if (itemId == R.id.Ranking) {
            startActivity(new Intent(this, RankingActivity.class));
            return true;
        }
        else if (itemId == R.id.Login) {
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        else if (itemId == R.id.Forum) {
            startActivity(new Intent(this, ForumActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
