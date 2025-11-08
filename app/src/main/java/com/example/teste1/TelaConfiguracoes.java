package com.example.teste1;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.teste1.view.api.ApiClient;
import com.example.teste1.view.api.ApiService;
// ADICIONE ESTE IMPORT para a TelaPrincipal (ou sua tela de login/entrada)
import com.example.teste1.view.telas_usuario.TelaPrincipal;
import com.example.teste1.view.telas_usuario.TelaPerfil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser; // ADICIONE ESTE IMPORT

import retrofit2.*;

public class TelaConfiguracoes extends AppCompatActivity {
    private Button btn_editar_perfil, btn_excluir_perfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_configuracoes);
        btn_editar_perfil = findViewById(R.id.btn_edit_perfil);
        btn_excluir_perfil = findViewById(R.id.btn_excluir_perfil);

        btn_editar_perfil.setOnClickListener(view -> {
            Intent intent = new Intent(TelaConfiguracoes.this, TelaEditarPerfil.class);
            startActivity(intent);
            finish();
        });

        btn_excluir_perfil.setOnClickListener(view -> {
            new AlertDialog.Builder(TelaConfiguracoes.this).setTitle("Você deseja realmente excluir seu perfil?").setItems(new String[]{"Sim", "excluir", "Voltar"}, ((dialog, which) -> {
                        if(which == 0) {
                            // Excluir
                            ApiService api = ApiClient.getClient().create(ApiService.class);
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                            if (currentUser == null) {
                                Toast.makeText(this, "Usuário não autenticado.", Toast.LENGTH_SHORT).show();
                                return; // Sai se não houver usuário
                            }
                            String uid = currentUser.getUid();

                            Call<Void> call = api.excluirPerfil(uid);
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    // O script PHP foi corrigido, agora response.isSuccessful() deve ser true
                                    if (response.isSuccessful()){
                                        // 1. Dados do banco de dados foram excluídos.
                                        // 2. Agora, exclua o usuário do Firebase Auth.
                                        currentUser.delete().addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                // 3. Sucesso ao excluir do Auth.
                                                Toast.makeText(TelaConfiguracoes.this, "Perfil excluído com sucesso", Toast.LENGTH_SHORT).show();

                                                // 4. Enviar para a tela principal e limpar o histórico de telas
                                                Intent intent = new Intent(TelaConfiguracoes.this, TelaPrincipal.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                // Falha ao excluir do Auth (pode precisar de re-autenticação)
                                                Toast.makeText(TelaConfiguracoes.this, "Dados do perfil excluídos, mas falha ao excluir conta. Faça login novamente.", Toast.LENGTH_LONG).show();
                                                // Deslogar de qualquer forma
                                                FirebaseAuth.getInstance().signOut();
                                                Intent intent = new Intent(TelaConfiguracoes.this, TelaPrincipal.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    } else{
                                        // Isso ainda pode acontecer se houver um erro 404 (usuário não encontrado) ou 500 (falha de BD)
                                        Toast.makeText(TelaConfiguracoes.this, "Erro ao excluir dados do perfil (Código: " + response.code() + ")", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(TelaConfiguracoes.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (which == 1) {
                            // Voltar - Apenas fecha o diálogo, não precisa navegar
                            dialog.dismiss();

                    /* // O código abaixo é desnecessário e gasta recursos
                    Intent intent = new Intent(TelaConfiguracoes.this, TelaPerfil.class);
                    startActivity(intent);
                    finish();
                    */
                        }
                    })).show();
        });
    }
}