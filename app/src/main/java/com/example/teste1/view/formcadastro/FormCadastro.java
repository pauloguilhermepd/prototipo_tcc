package com.example.teste1.view.formcadastro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teste1.R;
import com.example.teste1.databinding.ActivityFormCadastroBinding;
import com.example.teste1.view.formlogin.FormLogin;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class FormCadastro extends AppCompatActivity {
    //Criando a variavel binding para usar no codigo
    private ActivityFormCadastroBinding binding;
    private FirebaseAuth auth  = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_cadastro);
        binding = ActivityFormCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Se clicar no texto de não ter conta ele vai pra tela de login
        binding.txtLogin.setOnClickListener(view -> {
            Intent intent = new Intent(FormCadastro.this, FormLogin.class);
            startActivity(intent);
            finish();
        });

        //Clicar no botão de cadastrar
        binding.btnCadastrar.setOnClickListener(view -> {
            String email = binding.editEmail.getText().toString();
            String senha = binding.editSenha.getText().toString();

            //Se verificar se o email esta vazio
            if(email.isEmpty() || senha.isEmpty()){
                Snackbar snackbar = Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.show();
            //Se não estiver vazio:
            }else{
                //Cadastra o usuário utilizando o firebase
                auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(cadastro -> {
                    //Verifica se o cadastro foi realizado com sucesso atraves do lambda
                    if(cadastro.isSuccessful()){

                        //Criar um snackbar com uma mensagem de sucesso
                        Snackbar snackbar = Snackbar.make(view, "Cadastro realizado com sucesso", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.BLUE);
                        snackbar.show();
                        binding.editEmail.setText("");
                        binding.editSenha.setText("");


                    }
                    //Se o cadastro não for realizado ele cria uma exception para verificar o erro
                }).addOnFailureListener(e -> {
                    String msgErro = "Erro ao cadastrar usuário";
                    //Erro de senha inválida
                    if (e instanceof FirebaseAuthWeakPasswordException) {
                        msgErro = "Digite uma senha com no mínimo 6 caracteres";
                    //Erro de email inválido
                    } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        msgErro = "Digite um email válido";
                    //Erro de email ja cadastrado
                    } else if (e instanceof FirebaseAuthUserCollisionException) {
                        msgErro = "Esta conta já foi cadastrada";
                    //Erro de conexão
                    } else if(e instanceof FirebaseNetworkException){
                        msgErro = "Sem acesso a internet";
                    }
                    //Cria um snack bar para mostrar a mensagem de erro
                    Snackbar snackbar = Snackbar.make(view, msgErro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();

                });
            }
        });

    }
}