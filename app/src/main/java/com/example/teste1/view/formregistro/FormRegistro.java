package com.example.teste1.view.formregistro;

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
import com.example.teste1.view.api.ApiClient;
import com.example.teste1.view.api.ApiService;
import com.example.teste1.view.formestilo.FormEstilo;

import com.example.teste1.view.RespostasRegistros.RespostaRegistroPerfil;
import com.example.teste1.view.mascaraData;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormRegistro extends AppCompatActivity {
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
        setContentView(R.layout.activity_form_registro);
        Spinner spin_pronomes = findViewById(R.id.spin_pronomes);
        EditText edit_nome = findViewById(R.id.edit_nome_registro);
        EditText edit_data_aniversario = findViewById(R.id.edit_data_registro);
        EditText edit_bio = findViewById(R.id.edit_bio_registro);
        Button btn_prosseguir = findViewById(R.id.btn_prosseguir);
        CircleImageView imgPreview = findViewById(R.id.cim_foto_perfil);

        edit_data_aniversario.addTextChangedListener(mascaraData.mask(edit_data_aniversario, "####-##-##"));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, windowInsets) ->{
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return windowInsets;
        });

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

        btn_prosseguir.setOnClickListener(view -> {
            String nome = edit_nome.getText().toString();
            String data_aniver = edit_data_aniversario.getText().toString();
            String bio = edit_bio.getText().toString();

            if(nome.isEmpty() || data_aniver.isEmpty() || bio.isEmpty() || pronomeSelecionado.isEmpty() || uriFotoSelecionada == null){
                Snackbar snackbar = Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.show();
            }
            else{
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                File file = new File(getRealPathFromURI(uriFotoSelecionada));

                RequestBody fotoBody = RequestBody.create(MediaType.parse("image/*"), file);

                MultipartBody.Part foto_perfil = MultipartBody.Part.createFormData("foto_perfil", file.getName(), fotoBody);

                RequestBody uidBody = RequestBody.create(MediaType.parse("text/plain"), uid);
                RequestBody nomeBody = RequestBody.create(MediaType.parse("text/plain"), nome);
                RequestBody dataBody = RequestBody.create(MediaType.parse("text/plain"), data_aniver);
                RequestBody bioBody = RequestBody.create(MediaType.parse("text/plain"), bio);
                RequestBody pronomeBody = RequestBody.create(MediaType.parse("text/plain"), pronomeSelecionado);

                ApiService api = ApiClient.getClient().create(ApiService.class);
                Call<RespostaRegistroPerfil> call = api.registrarPerfil(uidBody, nomeBody, dataBody, bioBody, pronomeBody, foto_perfil);


                call.enqueue(new Callback<RespostaRegistroPerfil>() {
                    @Override
                    public void onResponse(Call<RespostaRegistroPerfil> call, Response<RespostaRegistroPerfil> response) {
                        if(response.isSuccessful()) {
                            RespostaRegistroPerfil resposta = response.body();
                            String idPerfil = resposta.getId_perfil_usuario();
                            String status = resposta.getStatus();

                            Log.d("Api", "Status: " + status + " | ID: " + idPerfil);

                            Snackbar snackbar = Snackbar.make(view, "Perfil registrado com sucesso!", Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.GREEN);
                            snackbar.show();

                            Intent intent = new Intent(FormRegistro.this, FormEstilo.class);
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
                    public void onFailure(Call<RespostaRegistroPerfil> call, Throwable t) {
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