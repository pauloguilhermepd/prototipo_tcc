package com.example.teste1.view.telas_usuario;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.teste1.R;
import com.example.teste1.view.api.ApiClient;
import com.example.teste1.view.api.ApiService;
import com.example.teste1.view.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.teste1.R;

public class TelaPerfil extends AppCompatActivity {
    private ImageView cimFotoPerfil;
    private TextView tpNome, tpPronome, tpBio;
    private ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_perfil);

        cimFotoPerfil = findViewById(R.id.cim_foto_perfil);
        tpNome = findViewById(R.id.tp_nome);
        tpPronome = findViewById(R.id.tp_pronome);
        tpBio = findViewById(R.id.tp_bio);

        // 🔹 Pega o UID do usuário logado no Firebase
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // 🔹 Cria a instância da API
        apiService = ApiClient.getClient().create(ApiService.class);

        // 🔹 Faz a requisição
        carregarPerfil(uid);


    }

    private void carregarPerfil(String uid) {
        Call<Usuario> call = apiService.buscarUsuario(uid);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuario = response.body();

                    tpNome.setText(usuario.getNome());
                    tpPronome.setText(usuario.getPronome());
                    tpBio.setText(usuario.getBio());

                    // Carrega a imagem com Glide
                    Glide.with(TelaPerfil.this)
                            .load(usuario.getFotoPerfil()) // URL completa da imagem
                            .placeholder(R.drawable.icons8_usuario_48)
                            .into(cimFotoPerfil);

                } else {
                    Log.e("Perfil", "Erro na resposta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("Perfil", "Falha na requisição: " + t.getMessage());
            }
        });

    }




}