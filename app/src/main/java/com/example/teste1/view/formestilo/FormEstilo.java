package com.example.teste1.view.formestilo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teste1.R;
import com.example.teste1.view.api.ApiClient;
import com.example.teste1.view.api.ApiService;
import com.example.teste1.view.RespostasRegistros.RegistroPerfilEstilo;
import com.example.teste1.view.RespostasRegistros.RespostaRegistroEstilo;
import com.example.teste1.view.telas_usuario.TelaPerfil;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.*;

public class FormEstilo extends AppCompatActivity {
    private String estiloSelecionado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_estilo);
        Spinner spin_estilos = findViewById(R.id.spin_estilos);
        EditText edt_subestilo = findViewById(R.id.edit_subestilo);
        Button btn_continuar = findViewById(R.id.btn_continuar);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.lista_estilos,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_estilos.setAdapter(adapter);

        spin_estilos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                estiloSelecionado = parent.getItemAtPosition(position).toString();
                String teste1 = parent.getItemAtPosition(position).toString();
                Log.d("Registro", "Estilo escolhido: " + estiloSelecionado);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                estiloSelecionado = "";
            }
        });



        btn_continuar.setOnClickListener(view -> {
            String subestilo = edt_subestilo.getText().toString();
            if(subestilo.isEmpty() || estiloSelecionado.isEmpty()){
                Snackbar snackbar = Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.show();
            }
            else{

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                ApiService api = ApiClient.getClient().create(ApiService.class);
                Call<RespostaRegistroEstilo> call = api.registrar_estilo(estiloSelecionado, subestilo, uid);

                call.enqueue(new Callback<RespostaRegistroEstilo>() {
                    @Override
                    public void onResponse(Call<RespostaRegistroEstilo> call, Response<RespostaRegistroEstilo> response) {
                        if(response.isSuccessful()){
                            RespostaRegistroEstilo resposta = response.body();
                            String status = resposta.getStatus();

                            Log.d("Api", "Status: " + status);
                            Snackbar snackbar = Snackbar.make(view, "Estilo registrado com sucesso!", Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.GREEN);
                            snackbar.show();

                            Intent intent = new Intent(FormEstilo.this, TelaPerfil.class);
                            startActivity(intent);
                            finish();

                        }
                        else {
                            Log.e("API", "Erro: resposta nula ou inválida");

                            Snackbar snackbar = Snackbar.make(view, "Erro ao registrar estilo", Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.RED);
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RespostaRegistroEstilo> call, Throwable t) {
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