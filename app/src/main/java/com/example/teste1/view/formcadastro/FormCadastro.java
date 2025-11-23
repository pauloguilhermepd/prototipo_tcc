package com.example.teste1.view.formcadastro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.teste1.R;
import com.example.teste1.databinding.ActivityFormCadastroBinding;
import com.example.teste1.view.formlogin.FormLogin;
import com.example.teste1.view.formregistro.FormRegistro;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class FormCadastro extends AppCompatActivity {
    private ActivityFormCadastroBinding binding;
    private final FirebaseAuth auth  = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_cadastro);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, windowInsets) ->{
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return windowInsets;
        });

        binding = ActivityFormCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.txtLogin.setOnClickListener(view -> {
            Intent intent = new Intent(FormCadastro.this, FormLogin.class);
            startActivity(intent);
            finish();
        });



        binding.btnCadastrar.setOnClickListener(view -> {
            String email = binding.editEmailCadastro.getText().toString();
            String senha = binding.editSenhaCadastro.getText().toString();

            if(email.isEmpty() || senha.isEmpty()){
                Snackbar snackbar = Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.show();
            }else{
                auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(cadastro -> {
                    if(cadastro.isSuccessful()){

                        Intent intent = new Intent(FormCadastro.this, FormRegistro.class);
                        startActivity(intent);
                        finish();
                    }

                }).addOnFailureListener(e -> {
                    String msgErro = "Erro ao cadastrar usuário";

                    if (e instanceof FirebaseAuthWeakPasswordException) {
                        msgErro = "Digite uma senha com no mínimo 6 caracteres";
                    } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        msgErro = "Digite um email válido";
                    } else if (e instanceof FirebaseAuthUserCollisionException) {
                        msgErro = "Esta conta já foi cadastrada";
                    } else if(e instanceof FirebaseNetworkException){
                        msgErro = "Sem acesso a internet";
                    }
                    Snackbar snackbar = Snackbar.make(view, msgErro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();

                    binding.editEmailCadastro.setText("");
                    binding.editSenhaCadastro.setText("");

                });
            }
        });

    }
}