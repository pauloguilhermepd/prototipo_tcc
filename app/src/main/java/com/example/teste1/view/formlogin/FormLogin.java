package com.example.teste1.view.formlogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teste1.R;
import com.example.teste1.databinding.ActivityFormLoginBinding;
import com.example.teste1.view.formcadastro.FormCadastro;
import com.example.teste1.view.telaprincipal.TelaPrincipal;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class FormLogin extends AppCompatActivity {
    private ActivityFormLoginBinding binding;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_login);
        binding = ActivityFormLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.txtLinkCadastrar.setOnClickListener(view -> {
            Intent intent = new Intent(FormLogin.this, FormCadastro.class);
            startActivity(intent);
            finish();
        });

        binding.btnEntrar.setOnClickListener(view -> {
            String email = binding.editEmailLogin.getText().toString();
            String senha = binding.editSenhaLogin.getText().toString();

            if(email.isEmpty() || senha.isEmpty()){
                Snackbar snackbar = Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.show();
            }else{
                auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(login -> {
                    if(login.isSuccessful()){
                        Intent intent = new Intent(FormLogin.this, TelaPrincipal.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(e -> {
                    String msgErro = "Erro ao fazer login";
                    if(e instanceof FirebaseAuthInvalidCredentialsException){
                        msgErro = "Email ou senha incorretos";
                    } else if(e instanceof FirebaseNetworkException){
                        msgErro = "Sem conexão com a internet";
                    } else if(e instanceof FirebaseAuthInvalidUserException){
                        msgErro = "Usuário não cadastrado";
                    }
                    Snackbar snackbar = Snackbar.make (view, msgErro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                });
            }
        });

    }
}