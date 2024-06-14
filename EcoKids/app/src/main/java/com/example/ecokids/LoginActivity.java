package com.example.ecokids;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText edtEmail, edtSenha;
    private Button btnEntrar;
    private TextView txvCadastrar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        txvCadastrar = findViewById(R.id.txvcadastrar);

        btnEntrar.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String senha = edtSenha.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
                Toast.makeText(LoginActivity.this, "Todos os campos são obrigatórios.", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(LoginActivity.this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                checkAndCreateUserDocument(user.getUid());
                            }
                        } else {
                            String errorMessage = getFirebaseAuthErrorMessage(task.getException());
                            Log.d(TAG, "Erro de autenticação: " + errorMessage);
                            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        txvCadastrar.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void checkAndCreateUserDocument(String userId) {
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {

                    createUserData(userId);
                }
            } else {
                Toast.makeText(LoginActivity.this, "Erro ao verificar usuário.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUserData(String userId) {

        Map<String, Object> userData = new HashMap<>();
        userData.put("avatarUrl", "url_para_o_avatar_padrao");
        userData.put("name", "Nome do Usuário");
        userData.put("points", 0);


        db.collection("users").document(userId)
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(LoginActivity.this, "Documento do usuário criado com sucesso.", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LoginActivity.this, "Erro ao criar documento do usuário.", Toast.LENGTH_SHORT).show();
                });
    }

    private String getFirebaseAuthErrorMessage(Exception exception) {
        if (exception != null) {
            String errorCode = ((FirebaseAuthException) exception).getErrorCode();
            switch (errorCode) {
                case "ERROR_INVALID_EMAIL":
                    return "O endereço de e-mail está mal formatado.";
                case "ERROR_USER_NOT_FOUND":
                    return "Email ou senha incorretos!";
                case "ERROR_WRONG_PASSWORD":
                    return "Email ou senha incorretos!";
                default:
                    return "Erro ao autenticar: " + errorCode;
            }
        } else {
            return "Erro ao autenticar.";
        }
    }
}
