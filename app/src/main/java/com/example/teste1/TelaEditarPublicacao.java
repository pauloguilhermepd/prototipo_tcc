package com.example.teste1;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.teste1.view.RespostasRegistros.RespostaRegistroPerfil;
import com.example.teste1.view.api.ApiClient;
import com.example.teste1.view.api.ApiService;
import com.example.teste1.view.telas_usuario.TelaPerfil;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaEditarPublicacao extends AppCompatActivity {
    private ImageView imgPublicacao;
    private EditText editTitulo, editDescricao;
    private Button btnSalvar;

    private Uri uriNovaFoto;
    private int idPublicacao;
    private String uid;

    private String getRealPathFromURI(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        android.content.CursorLoader loader = new android.content.CursorLoader(this, uri, proj, null, null, null);
        android.database.Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_editar_publicacao);

        imgPublicacao = findViewById(R.id.img_editar_publicacao);
        editTitulo = findViewById(R.id.edit_titulo_editar);
        editDescricao = findViewById(R.id.edit_descricao_editar);
        btnSalvar = findViewById(R.id.btn_salvar_edicao_publi);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Intent intent = getIntent();
        idPublicacao = intent.getIntExtra("ID_PUBLICACAO", 0);
        String tituloAtual = intent.getStringExtra("TITULO_ATUAL");
        String descAtual = intent.getStringExtra("DESCRICAO_ATUAL");
        String fotoUrlAtual = intent.getStringExtra("FOTO_ATUAL_URL");

        if (idPublicacao == 0) {
            Toast.makeText(this, "Erro: ID da publicação não encontrado", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        editTitulo.setText(tituloAtual);
        editDescricao.setText(descAtual);
        Glide.with(this).load(fotoUrlAtual).into(imgPublicacao);

        ActivityResultLauncher<String> escolherImagem = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        uriNovaFoto = uri;
                        imgPublicacao.setImageURI(uri);
                    }
                }
        );

        imgPublicacao.setOnClickListener(v -> escolherImagem.launch("image/*"));

        btnSalvar.setOnClickListener(v -> {
            salvarAlteracoes();
        });

    }

    private void salvarAlteracoes() {
        String novoTitulo = editTitulo.getText().toString();
        String novaDescricao = editDescricao.getText().toString();

        if (novoTitulo.isEmpty()) {
            Toast.makeText(this, "O título não pode estar vazio", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody uidBody = RequestBody.create(MediaType.parse("text/plain"), uid);
        RequestBody idPubliBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idPublicacao));
        RequestBody tituloBody = RequestBody.create(MediaType.parse("text/plain"), novoTitulo);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), novaDescricao);


        MultipartBody.Part fotoPart = null;

        if (uriNovaFoto != null) {
            try {
                File file = new File(getRealPathFromURI(uriNovaFoto));
                RequestBody fotoBody = RequestBody.create(MediaType.parse("image/*"), file);
                fotoPart = MultipartBody.Part.createFormData("imagem_publi", file.getName(), fotoBody);
            } catch (Exception e) {
                Log.e("EditarPublicacao", "Erro ao criar arquivo de imagem: " + e.getMessage());
                Toast.makeText(this, "Erro ao processar imagem", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<RespostaRegistroPerfil> call = api.editarPublicacao(uidBody, idPubliBody, tituloBody, descBody, fotoPart);

        call.enqueue(new Callback<RespostaRegistroPerfil>() {
            @Override
            public void onResponse(Call<RespostaRegistroPerfil> call, Response<RespostaRegistroPerfil> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().getStatus())) {
                    Toast.makeText(TelaEditarPublicacao.this, "Publicação atualizada!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(TelaEditarPublicacao.this, TelaPerfil.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMsg = response.body() != null ? response.body().getMessage() : "Resposta inválida do servidor";
                    Log.e("API", "Erro ao editar: " + errorMsg);
                    Toast.makeText(TelaEditarPublicacao.this, "Erro: " + errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RespostaRegistroPerfil> call, Throwable t) {
                Log.e("API", "Falha ao editar: " + t.getMessage());
                Toast.makeText(TelaEditarPublicacao.this, "Falha: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
