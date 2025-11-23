package com.example.teste1.view.formpublicacao;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.provider.MediaStore;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.teste1.R;
import com.example.teste1.TelaEditarPublicacao;
import com.example.teste1.view.RespostasRegistros.RespostaRegistroPublicacao;
import com.example.teste1.view.api.ApiClient;
import com.example.teste1.view.api.ApiService;


import com.example.teste1.view.telas_usuario.TelaPerfil;
import com.example.teste1.view.telas_usuario.TelaPrincipal;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.teste1.R;

public class FormPublicacao extends AppCompatActivity {

    private Uri uriFotoSelecionada;
    private CircleImageView cim_voltar;
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
        setContentView(R.layout.activity_form_publicacao);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, windowInsets) ->{
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return windowInsets;
        });

        cim_voltar = findViewById(R.id.cim_voltar);
        cim_voltar.setOnClickListener(view -> {
            Intent intent = new Intent(FormPublicacao.this, TelaPerfil.class);
            startActivity(intent);
            finish();
        });

        ImageView imgPreview = findViewById(R.id.img_publicacao);
        EditText edit_titulo = findViewById(R.id.edit_titulo_publi);
        EditText edit_descricao = findViewById(R.id.edit_descricao_publi);
        Button btn_criar_publi = findViewById(R.id.btn_criar_publi);

        ActivityResultLauncher<String> escolherImagem = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        uriFotoSelecionada = uri;
                        imgPreview.setImageURI(uri);
                    }
                }
        );

        imgPreview.setOnClickListener(view -> escolherImagem.launch("image/*"));

        btn_criar_publi.setOnClickListener(view-> {
            String titulo = edit_titulo.getText().toString();
            String descricao = edit_descricao.getText().toString();


            if(titulo.isEmpty() || descricao.isEmpty() || uriFotoSelecionada == null){
                Snackbar snackbar = Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.show();
            }
            else{
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                File file = new File(getRealPathFromURI(uriFotoSelecionada));

                RequestBody imagemBody = RequestBody.create(MediaType.parse("image/*"), file);

                MultipartBody.Part imagem_publi = MultipartBody.Part.createFormData("imagem_publi", file.getName(), imagemBody);

                RequestBody uidBody = RequestBody.create(MediaType.parse("text/plain"), uid);
                RequestBody tituloBody = RequestBody.create(MediaType.parse("text/plain"), titulo);
                RequestBody descricaoBody = RequestBody.create(MediaType.parse("text/plain"), descricao);


                ApiService api = ApiClient.getClient().create(ApiService.class);
                Call<RespostaRegistroPublicacao> call = api.registrarPublicacao(uidBody, tituloBody, descricaoBody, imagem_publi);


                call.enqueue(new Callback<RespostaRegistroPublicacao>() {
                    @Override
                    public void onResponse(Call<RespostaRegistroPublicacao> call, Response<RespostaRegistroPublicacao> response) {
                        if(response.isSuccessful()) {
                            RespostaRegistroPublicacao resposta = response.body();
                            String idPerfil = resposta.getIdPerfilUsuario();
                            String status = resposta.getStatus();

                            Log.d("Api", "Status: " + status + " | ID: " + idPerfil);

                            Snackbar snackbar = Snackbar.make(view, "Perfil registrado com sucesso!", Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.GREEN);
                            snackbar.show();

                            Intent intent = new Intent(FormPublicacao.this, TelaPrincipal.class);
                            intent.putExtra("id_perfil_usuario", uid);
                            startActivity(intent);
                            finish();


                        }
                        else{
                            Log.e("API", "Erro: resposta nula ou inválida");

                            Snackbar snackbar = Snackbar.make(view, "Erro ao registrar perfil", Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.RED);
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RespostaRegistroPublicacao> call, Throwable t) {
                        Log.e("API", "Falha na requisição: " + t.getMessage());

                        Snackbar snackbar = Snackbar.make(view, "Falha: " + t.getMessage(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.RED);
                        snackbar.show();
                    }
                });
            }
        });
    }
}