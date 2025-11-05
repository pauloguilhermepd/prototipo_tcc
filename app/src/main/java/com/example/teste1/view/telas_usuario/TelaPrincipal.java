package com.example.teste1.view.telas_usuario;

import android.content.Intent;
import android.os.Bundle;


import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teste1.R;
import com.example.teste1.view.adapters.FeedAdapter;
import com.example.teste1.view.api.ApiClient;
import com.example.teste1.view.api.ApiService;
import com.example.teste1.view.formpublicacao.FormPublicacao;
import com.example.teste1.view.models.Publicacao;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.Intent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.teste1.view.formpublicacao.FormPublicacao;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaPrincipal extends AppCompatActivity {
    private RecyclerView recyclerFeed;
    private FeedAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_principal);

        recyclerFeed = findViewById(R.id.recycler_feed);
        recyclerFeed.setLayoutManager(new LinearLayoutManager(this));


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Já está na tela principal
                return true;
            } else if (id == R.id.nav_publicar) {
                Intent intent = new Intent(TelaPrincipal.this, FormPublicacao.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_perfil) {
                Intent intent = new Intent(TelaPrincipal.this, TelaPerfil.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        carregarPublicacoes();

    }

    private void carregarPublicacoes() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<List<Publicacao>> call = api.listarPublicacoes();

        call.enqueue(new Callback<List<Publicacao>>() {
            @Override
            public void onResponse(Call<List<Publicacao>> call, Response<List<Publicacao>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new FeedAdapter(response.body(), TelaPrincipal.this);
                    recyclerFeed.setAdapter(adapter);
                } else {
                    Log.e("FEED", "Erro na resposta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Publicacao>> call, Throwable t) {
                Log.e("FEED", "Falha: " + t.getMessage());
            }
        });
    }
}