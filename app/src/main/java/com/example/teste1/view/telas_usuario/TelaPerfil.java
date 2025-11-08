package com.example.teste1.view.telas_usuario;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.bumptech.glide.Glide;
import com.example.teste1.R;
import com.example.teste1.TelaConfiguracoes;
import com.example.teste1.view.RespostasRegistros.RespostaBuscarUsuario;
import com.example.teste1.view.adapters.FeedAdapter;
import com.example.teste1.view.api.ApiClient;
import com.example.teste1.view.api.ApiService;
import com.example.teste1.view.formpublicacao.FormPublicacao;
import com.example.teste1.view.models.Publicacao;
import com.example.teste1.view.models.Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class TelaPerfil extends AppCompatActivity {
    private ImageView imgPerfil;
    private TextView txtNome, txtPronome, txtBio, txtEstilo, txtSubestilo;
    private ApiService apiService;
    private FirebaseAuth auth;
    private RecyclerView recyclerPerfil;
    private String uid;
    private FeedAdapter feedAdapter;
    private CircleImageView imgConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_perfil);

        imgPerfil = findViewById(R.id.cim_foto_perfil);
        txtNome = findViewById(R.id.tp_nome);
        txtPronome = findViewById(R.id.tp_pronome);
        txtBio = findViewById(R.id.tp_bio);
        txtEstilo = findViewById(R.id.tp_estilo);
        txtSubestilo = findViewById(R.id.tp_subestilo);
        imgConfig = findViewById(R.id.cim_configuracoes);
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();

        recyclerPerfil = findViewById(R.id.recycler_publicacoes_perfil);
        recyclerPerfil.setLayoutManager(new LinearLayoutManager(this));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent = new Intent(TelaPerfil.this, TelaPrincipal.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_publicar) {
                Intent intent = new Intent(TelaPerfil.this, FormPublicacao.class);
                startActivity(intent);
                return true;
            } else return id == R.id.nav_perfil;
        });

        carregarPerfil(uid);

        carregarPublicacoesDoUsuario(uid);

        imgConfig.setOnClickListener(view -> {
            Intent intent = new Intent(TelaPerfil.this, TelaConfiguracoes.class);
            startActivity(intent);
            finish();
        });


    }

    private void carregarPerfil(String uid) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<RespostaBuscarUsuario> call = api.buscarUsuario(uid);

        call.enqueue(new Callback<RespostaBuscarUsuario>() {
            @Override
            public void onResponse(Call<RespostaBuscarUsuario> call, Response<RespostaBuscarUsuario> response) {
                RespostaBuscarUsuario resposta = response.body();
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuario = resposta.getUsuario();

                    txtNome.setText(usuario.getNome_completo());
                    txtPronome.setText(usuario.getPronomes());
                    txtBio.setText(usuario.getBiografia());
                    txtEstilo.setText(usuario.getEstilo());
                    txtSubestilo.setText(usuario.getSub_estilo());

                    if (usuario.getFoto_perfil() != null && !usuario.getFoto_perfil().isEmpty()) {
                        try {
                            String foto = usuario.getFoto_perfil();

                            if(foto.length() > 200){
                                byte[] imagemBytes = Base64.decode(usuario.getFoto_perfil(), Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(imagemBytes, 0, imagemBytes.length);
                                imgPerfil.setImageBitmap(bitmap);
                            } else {
                                String url = "https://abby-unbeaued-tyron.ngrok-free.dev/api_php/" + foto;
                                Glide.with(TelaPerfil.this).load(url).into(imgPerfil);
                            }


                        } catch (Exception e) {
                            Log.e("PERFIL", "Erro ao decodificar imagem: " + e.getMessage());
                        }
                    }
                } else {
                    Log.e("Perfil", "Erro na resposta: " + response.message());

                }
            }

            @Override
            public void onFailure(Call<RespostaBuscarUsuario> call, Throwable t) {
                Log.e("Perfil", "Falha na requisição: " + t.getMessage());
            }
        });

    }

    private void carregarPublicacoesDoUsuario(String uid) {
        ApiService api = ApiClient.getClient().create(ApiService.class);

        Call<List<Publicacao>> call = api.listarPublicacoesDoUsuario(uid, uid);

        call.enqueue(new Callback<List<Publicacao>>() {
            @Override
            public void onResponse(Call<List<Publicacao>> call, Response<List<Publicacao>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    feedAdapter = new FeedAdapter(response.body(), TelaPerfil.this);
                    recyclerPerfil.setAdapter(feedAdapter);
                } else {
                    Log.e("Perfil", "Erro ao carregar publicações do usuário: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Publicacao>> call, Throwable t) {
                Log.e("Perfil", "Falha ao carregar publicações do usuário: " + t.getMessage());
            }
        });
    }




}