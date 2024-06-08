package com.example.ecokids;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText edtEmail, edtSenha;
    private Button btnEntrar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String senha = edtSenha.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
                    Toast.makeText(LoginActivity.this, "Todos os campos são obrigatórios.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d(TAG, "Tentando fazer login com email: " + email);

                // Sign in with email and password
                mAuth.signInWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                // Redirect to MainActivity (or the main page of your app)
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish(); // Finish the login activity
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Falha na autenticação: " + getFirebaseAuthErrorMessage(task.getException()),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        TextView txvCadastrar = findViewById(R.id.txvcadastrar);
        txvCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CadastroActivity.class);
                startActivity(intent);
            }
        });
    }

    private String getFirebaseAuthErrorMessage(Exception exception) {
        if (exception instanceof FirebaseAuthException) {
            FirebaseAuthException authException = (FirebaseAuthException) exception;
            switch (authException.getErrorCode()) {
                case "ERROR_INVALID_EMAIL":
                    return "O endereço de e-mail está mal formatado.";
                case "ERROR_USER_NOT_FOUND":
                    return "Não há registro de usuário correspondente a este identificador. O usuário pode ter sido excluído.";
                case "ERROR_WRONG_PASSWORD":
                    return "A senha é inválida.";
                default:
                    return "Erro desconhecido: " + authException.getErrorCode();
            }
        } else {
            return exception.getLocalizedMessage();
        }
    }
}
