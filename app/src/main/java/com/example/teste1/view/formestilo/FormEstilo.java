package com.example.teste1.view.formestilo;

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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.teste1.R;
import com.example.teste1.view.api.ApiClient;
import com.example.teste1.view.api.ApiService;
import com.google.android.material.snackbar.Snackbar;

public class FormEstilo extends AppCompatActivity {
    private String pronomeSelecionado;
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
                pronomeSelecionado = parent.getItemAtPosition(position).toString();
                String teste1 = parent.getItemAtPosition(position).toString();
                Log.d("Registro", "Pronome escolhido: " + pronomeSelecionado);
                if(pronomeSelecionado.endsWith("e")){
                    pronomeSelecionado = "E";
                }if(pronomeSelecionado.endsWith("a")){
                    pronomeSelecionado = "A";
                }if(pronomeSelecionado.endsWith("u")){
                    pronomeSelecionado = "U";
                }else{
                    pronomeSelecionado = "O";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pronomeSelecionado = "";
            }
        });



        btn_continuar.setOnClickListener(view -> {
            String subestilo = edt_subestilo.toString();
            if(subestilo.isEmpty() || pronomeSelecionado.isEmpty()){
                Snackbar snackbar = Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.show();
            }
            else{
                ApiService api = ApiClient.getClient().create(ApiService.class);
                Call<RepostaRegistroUsuarioEstilo> call = api.registrarEstilo(id_estilo, id)
            }
        });
    }
}