package com.example.ecokids;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
