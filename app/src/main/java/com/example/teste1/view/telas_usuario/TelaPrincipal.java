package com.example.teste1.view.telas_usuario;

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
import com.example.teste1.view.models.Publicacao;

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