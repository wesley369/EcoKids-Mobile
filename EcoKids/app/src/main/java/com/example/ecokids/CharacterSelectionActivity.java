package com.example.ecokids;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CharacterSelectionActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private EditText edtCharacterName;
    private Button btnConfirm;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private Toolbar toolbar;
    private int[] characterImages = {
            R.drawable.avataralessandra,
            R.drawable.avatarrafael,
            R.drawable.avatartuanny,
            R.drawable.avatarwesley,
            R.drawable.laurinhaavatar,
    };

    private String[] characterImageUrls = {
            "https://firebasestorage.googleapis.com/v0/b/ecokids-88271.appspot.com/o/avataralessandra.png?alt=media&token=9ab6a972-a957-4be2-a832-9a1bdb20278b",
            "https://firebasestorage.googleapis.com/v0/b/ecokids-88271.appspot.com/o/avatarrafael.png?alt=media&token=6fd867ed-07c9-46e1-b6cd-c4800f9a7dfd",
            "https://firebasestorage.googleapis.com/v0/b/ecokids-88271.appspot.com/o/avatartuanny.png?alt=media&token=d4d525f5-7cb3-4314-a6ec-b84ba046dde9",
            "https://firebasestorage.googleapis.com/v0/b/ecokids-88271.appspot.com/o/avatarwesley.png?alt=media&token=683119d9-8caf-40ec-b0dc-cd3e0416556a",
            "https://firebasestorage.googleapis.com/v0/b/ecokids-88271.appspot.com/o/laurinhaavatar.webp?alt=media&token=b61076b3-88bf-4a4f-9486-c8552caefd77",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selection);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager);
        edtCharacterName = findViewById(R.id.edtCharacterName);
        btnConfirm = findViewById(R.id.btnConfirm);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        CharacterAdapter adapter = new CharacterAdapter(characterImages);
        viewPager.setAdapter(adapter);

        btnConfirm.setOnClickListener(v -> {
            String characterName = edtCharacterName.getText().toString().trim();
            if (TextUtils.isEmpty(characterName)) {
                Toast.makeText(CharacterSelectionActivity.this, "Por favor, insira o nome do personagem.", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedCharacterPosition = viewPager.getCurrentItem();
            String characterImageUrl = characterImageUrls[selectedCharacterPosition];

            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                saveCharacterDetails(user.getUid(), characterName, characterImageUrl);
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
    public boolean onOptionsItemSelected(MenuItem item) {
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
        } else if (itemId == R.id.Login) {
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        } else if (itemId == R.id.Forum) {
            startActivity(new Intent(this, ForumActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveCharacterDetails(String userId, String characterName, String characterImageUrl) {

        DocumentReference userRef = db.collection("users").document(userId);


        Map<String, Object> characterData = new HashMap<>();
        characterData.put("characterName", characterName);
        characterData.put("avatarUrl", characterImageUrl);


        userRef.update(characterData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CharacterSelectionActivity.this, "Personagem selecionado com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CharacterSelectionActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CharacterSelectionActivity.this, "Erro ao selecionar o personagem. Tente novamente.", Toast.LENGTH_SHORT).show();
                });
    }
}
