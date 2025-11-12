package com.example.teste1;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.teste1.view.api.ApiClient;
import com.example.teste1.view.api.ApiService;
import com.example.teste1.view.formcadastro.FormCadastro;
import com.example.teste1.view.formregistro.FormRegistro;
import com.example.teste1.view.telas_usuario.TelaPrincipal;
import com.example.teste1.view.telas_usuario.TelaPerfil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.*;

public class TelaConfiguracoes extends AppCompatActivity {
    private Button btn_editar_perfil, btn_excluir_perfil;
    private LinearLayout llt_editar, llt_excluir, llt_sair;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_configuracoes);
        llt_editar = findViewById(R.id.llt_editar_perfil);
        llt_excluir = findViewById(R.id.llt_excluir_perfil);
        llt_sair = findViewById(R.id.llt_sair_perfil);

        llt_editar.setOnClickListener(view -> {
            Intent intent = new Intent(TelaConfiguracoes.this, TelaEditarPerfil.class);
            startActivity(intent);
            finish();
        });

        llt_sair.setOnClickListener(view -> {
            Intent intent = new Intent(TelaConfiguracoes.this, FormCadastro.class);
            startActivity(intent);
            finish();
        });

        llt_excluir.setOnClickListener(view -> {
            new AlertDialog.Builder(TelaConfiguracoes.this).setTitle("Você deseja realmente excluir seu perfil?").setItems(new String[]{"Sim", "Voltar"}, ((dialog, which) -> {
                        if(which == 0) {
                            ApiService api = ApiClient.getClient().create(ApiService.class);
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                            if (currentUser == null) {
                                Toast.makeText(this, "Usuário não autenticado.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String uid = currentUser.getUid();

                            Call<Void> call = api.excluirPerfil(uid);
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()){
                                        currentUser.delete().addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(TelaConfiguracoes.this, "Perfil excluído com sucesso", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(TelaConfiguracoes.this, FormCadastro.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(TelaConfiguracoes.this, "Dados do perfil excluídos, mas falha ao excluir conta. Faça login novamente.", Toast.LENGTH_LONG).show();
                                                FirebaseAuth.getInstance().signOut();
                                                Intent intent = new Intent(TelaConfiguracoes.this, FormCadastro.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    } else{
                                        Toast.makeText(TelaConfiguracoes.this, "Erro ao excluir dados do perfil (Código: " + response.code() + ")", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(TelaConfiguracoes.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (which == 1) {
                            dialog.dismiss();
                        }
                    })).show();
        });
    }
}