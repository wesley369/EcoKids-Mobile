package com.example.ecokids;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CharacterSelectionActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private EditText edtCharacterName;
    private Button btnConfirm;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private int[] characterImages = {
            R.drawable.avataralessandra,
            R.drawable.avatarrafael,
            R.drawable.avatartuanny,
            R.drawable.avatarwesley,
            R.drawable.laurinhaavatar,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selection);

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
            int selectedCharacterImage = characterImages[selectedCharacterPosition];
            String characterImageUrl = "url_para_o_avatar_" + selectedCharacterPosition; // Mapeie a URL correspondente ao índice da imagem

            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                saveCharacterDetails(user.getUid(), characterName, characterImageUrl);
            }
        });
    }

    private void saveCharacterDetails(String userId, String characterName, String characterImageUrl) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("avatarUrl", characterImageUrl);
        updates.put("characterName", characterName);

        db.collection("users").document(userId).update(updates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(CharacterSelectionActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CharacterSelectionActivity.this, "Erro ao salvar os detalhes do personagem.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

