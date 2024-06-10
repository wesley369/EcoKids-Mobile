package com.example.ecokids;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btnTela = (Button) findViewById(R.id.btnTela);
        btnTela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        Button btnPersonagem = (Button) findViewById(R.id.btnPersonagem);
        btnPersonagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CharacterSelectionActivity.class);
                startActivity(intent);
            }
        });

        Button btnForum = (Button) findViewById(R.id.btnForum);
        btnForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ForumActivity.class);
                startActivity(intent);
            }
        });

        Button btnRanking = (Button) findViewById(R.id.btnRanking);
        btnRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RankingActivity.class);
                startActivity(intent);
            }
        });

        Button btnMetas = (Button) findViewById(R.id.btnMetas);
        btnMetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ToDoListActivity.class);
                startActivity(intent);
            }
        });
    }
}