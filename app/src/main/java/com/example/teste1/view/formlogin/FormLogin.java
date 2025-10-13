package com.example.teste1.view.formlogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teste1.R;
import com.example.teste1.view.formcadastro.FormCadastro;
import com.example.teste1.view.formregistro.FormRegistro;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class FormLogin extends AppCompatActivity {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_login);

        //Chamanda as views
        TextView txtLinkCadastrar = findViewById(R.id.txt_link_cadastrar);
        TextView editEmailLogin = findViewById(R.id.edit_email_login);
        TextView editSenhaLogin = findViewById(R.id.edit_senha_login);
        Button btnEntrar = findViewById(R.id.btn_entrar);

        txtLinkCadastrar.setOnClickListener(view -> {
            Intent intent = new Intent(FormLogin.this, FormCadastro.class);
            startActivity(intent);
            finish();
        });


        btnEntrar.setOnClickListener(view -> {
            String email = editEmailLogin.getText().toString();
            String senha = editSenhaLogin.getText().toString();

            if(email.isEmpty() || senha.isEmpty()){
                Snackbar snackbar = Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.show();
            }else{
                auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(login -> {
                    if(login.isSuccessful()){
                        Intent intent = new Intent(FormLogin.this, FormRegistro.class);
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