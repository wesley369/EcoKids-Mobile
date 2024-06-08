package com.example.ecokids;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CadastroActivity extends AppCompatActivity {

    private static final String TAG = "CadastroActivity";

    private EditText edtEmail, edtSenha, edtNome;
    private Button btnCadastrar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtNome = findViewById(R.id.edtNome);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String senha = edtSenha.getText().toString().trim();
                String nome = edtNome.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha) || TextUtils.isEmpty(nome)) {
                    Toast.makeText(CadastroActivity.this, "Todos os campos são obrigatórios.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d(TAG, "Tentando criar usuário com email: " + email);

                // Create user with email and password
                mAuth.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(CadastroActivity.this, task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "createUserWithEmail: success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                if (user != null) {
                                    writeNewUser(user.getUid(), nome, email);
                                }
                            } else {
                                Log.w(TAG, "createUserWithEmail: failure", task.getException());
                                Toast.makeText(CadastroActivity.this, "Falha na autenticação: " + getFirebaseAuthErrorMessage(task.getException()),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void writeNewUser(String userId, String name, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);

        Log.d(TAG, "Escrevendo usuário no Firestore com ID: " + userId);

        db.collection("users").document(userId).set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Usuário cadastrado com sucesso.");
                        Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.w(TAG, "Erro ao cadastrar usuário no Firestore", task.getException());
                        Toast.makeText(CadastroActivity.this, "Erro ao cadastrar usuário.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getFirebaseAuthErrorMessage(Exception exception) {
        if (exception instanceof FirebaseAuthException) {
            FirebaseAuthException authException = (FirebaseAuthException) exception;
            switch (authException.getErrorCode()) {
                case "ERROR_INVALID_EMAIL":
                    return "O endereço de e-mail está mal formatado.";
                case "ERROR_EMAIL_ALREADY_IN_USE":
                    return "O endereço de e-mail já está sendo usado por outra conta.";
                case "ERROR_WEAK_PASSWORD":
                    return "A senha é muito fraca.";
                // Adicione outros códigos de erro conforme necessário
                default:
                    return "Erro desconhecido: " + authException.getErrorCode();
            }
        } else {
            return exception.getLocalizedMessage();
        }
    }
}
