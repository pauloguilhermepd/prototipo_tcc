package com.example.teste1.view.formcadastro;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teste1.R;
import com.example.teste1.databinding.ActivityFormCadastroBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class FormCadastro extends AppCompatActivity {
    private ActivityFormCadastroBinding binding;
    private FirebaseAuth auth  = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_cadastro);
        binding = ActivityFormCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnCadastrar.setOnClickListener(view -> {
            String email = binding.editEmail.getText().toString();
            String senha = binding.editSenha.getText().toString();

            if(email.isEmpty() || senha.isEmpty()){
                Snackbar snackbar = Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.show();
            }else{
                auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(cadastro -> {
                    if(cadastro.isSuccessful()){

                        Snackbar snackbar = Snackbar.make(view, "Cadastro realizado com sucesso", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.BLUE);
                        snackbar.show();
                        binding.editEmail.setText("");
                        binding.editSenha.setText("");
                    }
                }).addOnFailureListener( erro-> {


                });

            }
        });

    }
}