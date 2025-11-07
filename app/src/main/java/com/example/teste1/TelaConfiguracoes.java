package com.example.teste1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TelaConfiguracoes extends AppCompatActivity {
    private Button btn_editar_perfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_configuracoes);
        btn_editar_perfil = findViewById(R.id.btn_edit_perfil);

        btn_editar_perfil.setOnClickListener(view -> {
            Intent intent = new Intent(TelaConfiguracoes.this, TelaEditarPerfil.class);
            startActivity(intent);
            finish();
        });
    }
}