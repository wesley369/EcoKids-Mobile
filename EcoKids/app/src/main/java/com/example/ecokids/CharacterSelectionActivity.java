package com.example.ecokids;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class CharacterSelectionActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private EditText edtCharacterName;
    private Button btnConfirm;

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

        CharacterAdapter adapter = new CharacterAdapter(characterImages);
        viewPager.setAdapter(adapter);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String characterName = edtCharacterName.getText().toString().trim();
                if (TextUtils.isEmpty(characterName)) {
                    Toast.makeText(CharacterSelectionActivity.this, "Por favor, insira o nome do personagem.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int selectedCharacterPosition = viewPager.getCurrentItem();
                int selectedCharacterImage = characterImages[selectedCharacterPosition];

                // Lógica para salvar o nome do personagem e a imagem selecionada
                // Você pode salvar em um banco de dados, enviar para uma próxima activity, etc.

                // Exemplo: Enviar os dados para a próxima activity
                Intent intent = new Intent(CharacterSelectionActivity.this, MainActivity.class);
                intent.putExtra("characterName", characterName);
                intent.putExtra("characterImage", selectedCharacterImage);
                startActivity(intent);
            }
        });
    }
}
