package com.example.teste1.view.formlogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teste1.R;
import com.example.teste1.view.api.ApiClient;
import com.example.teste1.view.api.ApiService;
import com.example.teste1.view.formcadastro.FormCadastro;
import com.example.teste1.view.formestilo.FormEstilo;
import com.example.teste1.view.formregistro.FormRegistro;
import com.example.teste1.view.telas_usuario.TelaPerfil;
import com.example.teste1.view.verification.VerificacaoUsuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        ApiService api = ApiClient.getClient().create(ApiService.class);

                        Call<VerificacaoUsuario> call = api.verificarUsuario(uid);
                        call.enqueue(new Callback<VerificacaoUsuario>() {
                            @Override
                            public void onResponse(Call<VerificacaoUsuario> call, Response<VerificacaoUsuario> response) {
                                if(response.isSuccessful() && response.body() != null){
                                    VerificacaoUsuario verificacao = response.body();
                                    String status = verificacao.getStatus();

                                    if(status.equals("completo")){
                                        Intent intent = new Intent(FormLogin.this, TelaPerfil.class);
                                        startActivity(intent);
                                        finish();

                                    }else if (status.equals("sem_estilo")) {
                                        // Usuário tem perfil mas não escolheu estilo
                                        Intent intent = new Intent(FormLogin.this, FormEstilo.class);
                                        intent.putExtra("id_perfil_usuario", verificacao.getId_perfil_usuario());
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        // Usuário sem perfil
                                        Intent intent = new Intent(FormLogin.this, FormRegistro.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                } else {
                                    Log.e("Login", "Erro ao verificar usuário: " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<VerificacaoUsuario> call, Throwable t) {
                                Log.e("Login", "Falha: " + t.getMessage());
                            }
                        });

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