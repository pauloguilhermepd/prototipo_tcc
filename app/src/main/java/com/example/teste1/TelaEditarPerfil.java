package com.example.teste1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.teste1.view.RespostasRegistros.RespostaBuscarUsuario;
import com.example.teste1.view.RespostasRegistros.RespostaRegistroPerfil;
import com.example.teste1.view.api.ApiClient;
import com.example.teste1.view.api.ApiService;
import com.example.teste1.view.formestilo.FormEstilo;
import com.example.teste1.view.formregistro.FormRegistro;
import com.example.teste1.view.models.Usuario;
import com.example.teste1.view.telas_usuario.TelaPerfil;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaEditarPerfil extends AppCompatActivity {

    private CircleImageView imgPerfil;
    private EditText txtNome, edit_data_aniversario, txtBio;
    private Button btn_editar;
    private ApiService apiService;
    private FirebaseAuth auth;

    private String pronomeSelecionado = "";
    private Uri uriFotoSelecionada;

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
        setContentView(R.layout.activity_tela_editar_perfil);

        imgPerfil = findViewById(R.id.cim_foto_edit_perfil);
        txtNome = findViewById(R.id.edit_nome_editar);
        txtBio = findViewById(R.id.edit_bio_editar);
        Spinner spin_pronomes = findViewById(R.id.spin_pronomes);
        edit_data_aniversario = findViewById(R.id.edit_data_editar);
        btn_editar = findViewById(R.id.btn_editar_perfil);
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        carregarPerfil(uid);


        ActivityResultLauncher<String> escolherImagem = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        uriFotoSelecionada = uri;
                        imgPerfil.setImageURI(uri);
                    }
                }
        );

        imgPerfil.setOnClickListener(view -> escolherImagem.launch("image/*"));



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.lista_pronomes,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_pronomes.setAdapter(adapter);

        spin_pronomes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pronomeSelecionado = parent.getItemAtPosition(position).toString();
                String teste1 = parent.getItemAtPosition(position).toString();
                Log.d("Registro", "Pronome escolhido: " + pronomeSelecionado);
                if(pronomeSelecionado.endsWith("e")){
                    pronomeSelecionado = "E";
                }if(pronomeSelecionado.endsWith("a")){
                    pronomeSelecionado = "A";
                }if(pronomeSelecionado.endsWith("u")){
                    pronomeSelecionado = "U";
                }if(pronomeSelecionado.endsWith("o")){
                    pronomeSelecionado = "O";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pronomeSelecionado = "";
            }
        });

        btn_editar.setOnClickListener(view -> {
            String nome = txtNome.getText().toString();
            String data_aniver = edit_data_aniversario.getText().toString();
            String bio = txtBio.getText().toString();


                RequestBody uidBody = RequestBody.create(MediaType.parse("text/plain"), uid);
                RequestBody nomeBody = RequestBody.create(MediaType.parse("text/plain"), nome);
                RequestBody dataBody = RequestBody.create(MediaType.parse("text/plain"), data_aniver);
                RequestBody bioBody = RequestBody.create(MediaType.parse("text/plain"), bio);
                RequestBody pronomeBody = RequestBody.create(MediaType.parse("text/plain"), pronomeSelecionado);

                MultipartBody.Part foto_perfil_part = null;

                if(uriFotoSelecionada != null){
                    try{
                        File file = new File(getRealPathFromURI(uriFotoSelecionada));
                        RequestBody fotoBody = RequestBody.create(MediaType.parse("image/*"), file);
                        foto_perfil_part = MultipartBody.Part.createFormData("foto_perfil", file.getName(), fotoBody);
                    } catch (Exception e){
                        Log.e("EditarPerfil", "Erro ao criar arquivo de imagem: " + e.getMessage());
                        Snackbar snackbar = Snackbar.make(view, "Erro ao processar imagem", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.RED);
                        snackbar.show();
                        return;
                    }
                }

                ApiService api = ApiClient.getClient().create(ApiService.class);
                Call<RespostaRegistroPerfil> call = api.editarPerfil(uidBody, nomeBody, dataBody, bioBody, pronomeBody, foto_perfil_part);


                call.enqueue(new Callback<RespostaRegistroPerfil>() {
                    @Override
                    public void onResponse(Call<RespostaRegistroPerfil> call, Response<RespostaRegistroPerfil> response) {
                        if(response.isSuccessful()) {
                            RespostaRegistroPerfil resposta = response.body();
                            String status = resposta.getStatus();

                            Log.d("Api", "Status Edição: " + status);

                            Snackbar snackbar = Snackbar.make(view, "Perfil atualizado com sucesso!", Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.GREEN);
                            snackbar.show();

                            Intent intent = new Intent(TelaEditarPerfil.this, TelaPerfil.class);
                            startActivity(intent);
                            finish();

                        }
                        else{
                            Log.e("API", "Erro: resposta nula ou inválida");

                            Snackbar snackbar = Snackbar.make(view, "Erro ao atualizar perfil", Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.RED);
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RespostaRegistroPerfil> call, Throwable t) {
                        Log.e("API", "Falha na edição: " + t.getMessage());

                        Snackbar snackbar = Snackbar.make(view, "Falha: " + t.getMessage(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.RED);
                        snackbar.show();
                    }
                });



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
                    txtBio.setText(usuario.getBiografia());
                    edit_data_aniversario.setText(usuario.getData_nascimento());


                    if (usuario.getFoto_perfil() != null && !usuario.getFoto_perfil().isEmpty()) {
                        try {
                            String foto = usuario.getFoto_perfil();

                            if(foto.length() > 200){
                                byte[] imagemBytes = Base64.decode(usuario.getFoto_perfil(), Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(imagemBytes, 0, imagemBytes.length);
                                imgPerfil.setImageBitmap(bitmap);
                            } else {
                                String url = "https://interword-everleigh-coordinately.ngrok-free.dev/api_php/" + foto;
                                Glide.with(TelaEditarPerfil.this).load(url).into(imgPerfil);
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