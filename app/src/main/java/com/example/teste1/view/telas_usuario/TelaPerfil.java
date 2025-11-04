package com.example.teste1.view.telas_usuario;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.teste1.R;
import com.example.teste1.view.RespostasRegistros.RespostaBuscarUsuario;
import com.example.teste1.view.api.ApiClient;
import com.example.teste1.view.api.ApiService;
import com.example.teste1.view.formpublicacao.FormPublicacao;
import com.example.teste1.view.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.teste1.R;

public class TelaPerfil extends AppCompatActivity {
    private ImageView imgPerfil;
    private TextView txtNome, txtPronome, txtBio, txtEstilo, txtSubestilo;
    private Button btn_publi, btn_publi_telaprincipal;
    private ApiService apiService;
    private FirebaseAuth auth;
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
        btn_publi = findViewById(R.id.btn_publi);
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        btn_publi_telaprincipal = findViewById(R.id.btn_publi_telaprincipal);
        carregarPerfil(uid);

        btn_publi.setOnClickListener(view -> {
            Intent intent = new Intent(TelaPerfil.this, FormPublicacao.class);
            startActivity(intent);
            finish();
        });

        btn_publi_telaprincipal.setOnClickListener(view -> {
            Intent intent = new Intent(TelaPerfil.this, TelaPrincipal.class);
            startActivity(intent);
            finish();
        } );
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




}