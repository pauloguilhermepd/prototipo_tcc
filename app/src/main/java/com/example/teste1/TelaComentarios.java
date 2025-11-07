package com.example.teste1;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teste1.R;
import com.example.teste1.view.adapters.ComentarioAdapter;
import com.example.teste1.view.api.ApiClient;
import com.example.teste1.view.api.ApiService;
import com.example.teste1.view.models.Comentario;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaComentarios extends AppCompatActivity {

    private RecyclerView recyclerComentarios;
    private ComentarioAdapter adapter;
    private EditText edtComentario;
    private Button btnEnviar;
    private int idPublicacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_comentarios);

        recyclerComentarios = findViewById(R.id.recycler_comentarios);
        edtComentario = findViewById(R.id.edt_comentario);
        btnEnviar = findViewById(R.id.btn_enviar_comentario);

        idPublicacao = getIntent().getIntExtra("id_publicacoes", -1);
        recyclerComentarios.setLayoutManager(new LinearLayoutManager(this));

        carregarComentarios();

        btnEnviar.setOnClickListener(v -> {
            String texto = edtComentario.getText().toString().trim();
            if (!texto.isEmpty()) {
                enviarComentario(texto);
                edtComentario.setText("");
            }
        });
    }

    private void carregarComentarios() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.listarComentarios(idPublicacao).enqueue(new Callback<List<Comentario>>() {
            @Override
            public void onResponse(Call<List<Comentario>> call, Response<List<Comentario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new ComentarioAdapter(response.body(), TelaComentarios.this);
                    recyclerComentarios.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Comentario>> call, Throwable t) {
                Log.e("Comentarios", "Erro: " + t.getMessage());
            }
        });
    }

    private void enviarComentario(String texto) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        api.registrarComentario(idPublicacao, uid, texto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                carregarComentarios();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("EnviarComentário", "Erro: " + t.getMessage());
            }
        });
    }
}