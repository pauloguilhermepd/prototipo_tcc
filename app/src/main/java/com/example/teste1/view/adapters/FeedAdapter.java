package com.example.teste1.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.teste1.R;
import com.example.teste1.TelaComentarios;
import com.example.teste1.view.RespostasRegistros.RespostaRegistroCurtida;
import com.example.teste1.view.api.ApiClient;
import com.example.teste1.view.api.ApiService;
import com.example.teste1.view.models.Publicacao;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.*;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private List<Publicacao> lista;
    private Context context;


    public FeedAdapter(List<Publicacao> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_publicacao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.ViewHolder holder, int position) {
        Publicacao pub = lista.get(position);

        holder.txtTitulo.setText(pub.getTitulo());
        holder.txtDescricao.setText(pub.getDescricao());
        holder.txtAutorNome.setText(pub.getAutor_nome());
        holder.txtNumCurtidas.setText(pub.getCurtidas() + " curtidas");

        Glide.with(context)
                .load(pub.getFoto())
                .into(holder.imgPublicacao);

        Glide.with(context)
                .load(pub.getAutor_foto())
                .into(holder.imgAutor);

        holder.btnCurtir.setOnClickListener(v -> {
                    ApiService api = ApiClient.getClient().create(ApiService.class);
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    api.registrarCurtida(pub.getId_publicacoes(), uid).enqueue(new Callback<RespostaRegistroCurtida>() {
                        @Override
                        public void onResponse(Call<RespostaRegistroCurtida> call, Response<RespostaRegistroCurtida> response) {
                            if(response.isSuccessful()){
                                RespostaRegistroCurtida resposta = response.body();
                                String idPerfilUsuario = resposta.getIdPerfilUsuario();
                                int idPublicacoes = resposta.getIdPublicacaoes();
                            }
                            //Terminar de resolver bug da curtida
                            boolean curtiu = holder.isCurtido;
                            holder.isCurtido = !curtiu;
                            holder.btnCurtir.setImageResource(
                                    holder.isCurtido ? R.drawable.ic_like_filled : R.drawable.ic_like_outline
                            );

                            int novasCurtidas = holder.isCurtido
                                    ? pub.getCurtidas() + 1
                                    : pub.getCurtidas() - 1;

                            pub.setCurtidas(novasCurtidas);
                            holder.txtNumCurtidas.setText(novasCurtidas + " curtidas");
                        }

                        @Override
                        public void onFailure(Call<RespostaRegistroCurtida> call, Throwable t) {
                            Log.e("Curtida", "Erro: " + t.getMessage());
                        }
                    });
        });

        holder.btnComentar.setOnClickListener(v -> {
                    Intent intent = new Intent(context, TelaComentarios.class);
                    intent.putExtra("id_publicacoes", pub.getId_publicacoes());
                    context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgAutor;
        ImageView imgPublicacao, btnCurtir, btnComentar;
        TextView txtTitulo, txtDescricao, txtAutorNome, txtNumCurtidas;
        boolean isCurtido = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAutor = itemView.findViewById(R.id.img_autor);
            imgPublicacao = itemView.findViewById(R.id.img_publicacao);
            txtTitulo = itemView.findViewById(R.id.txt_titulo_publi);
            txtDescricao = itemView.findViewById(R.id.txt_descricao_publi);
            txtAutorNome = itemView.findViewById(R.id.txt_autor_nome);
            txtNumCurtidas = itemView.findViewById(R.id.txt_num_curtidas);
            btnCurtir = itemView.findViewById(R.id.btn_curtir);
            btnComentar = itemView.findViewById(R.id.btn_comentar);
        }
    }
}