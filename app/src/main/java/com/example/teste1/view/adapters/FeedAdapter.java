package com.example.teste1.view.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.teste1.R;
import com.example.teste1.TelaComentarios;
import com.example.teste1.TelaEditarPublicacao;
import com.example.teste1.view.RespostasRegistros.RespostaRegistroCurtida;
import com.example.teste1.view.api.ApiClient;
import com.example.teste1.view.api.ApiService;
import com.example.teste1.view.models.Publicacao;
import com.example.teste1.view.telas_usuario.TelaPerfil;
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

        Glide.with(context).load(pub.getFoto()).into(holder.imgPublicacao);
        Glide.with(context).load(pub.getAutor_foto()).into(holder.imgAutor);

        String autorUID = pub.getId_perfil_usuario();

        View.OnClickListener listenerPerfil = v -> {
            if (autorUID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                return;
            }
            Intent intent = new Intent(context, TelaPerfil.class);
            intent.putExtra("uid_perfil", autorUID);
            context.startActivity(intent);
        };

// Aplique o listener à foto e ao nome
        holder.imgAutor.setOnClickListener(listenerPerfil);
        holder.txtAutorNome.setOnClickListener(listenerPerfil);

        holder.isCurtido = pub.getUsuario_curtiu() == 1;
        holder.txtNumCurtidas.setText(pub.getCurtidas() + " curtidas");
        holder.btnCurtir.setImageResource(
                holder.isCurtido ? R.drawable.ic_like_filled : R.drawable.ic_like_white
        );

        holder.btnCurtir.setOnClickListener(v -> {
            ApiService api = ApiClient.getClient().create(ApiService.class);
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            api.registrarCurtida(pub.getId_publicacoes(), uid).enqueue(new Callback<RespostaRegistroCurtida>() {
                @Override
                public void onResponse(Call<RespostaRegistroCurtida> call, Response<RespostaRegistroCurtida> response) {
                    // SÓ MUDA A UI DEPOIS QUE O SERVIDOR CONFIRMAR
                    if (response.isSuccessful() && response.body() != null) {
                        RespostaRegistroCurtida resposta = response.body();
                        int novasCurtidas;

                        if ("curtido".equals(resposta.getStatus())) {
                            holder.isCurtido = true;
                            novasCurtidas = pub.getCurtidas() + 1;
                        } else if ("removido".equals(resposta.getStatus())) {
                            holder.isCurtido = false;
                            novasCurtidas = pub.getCurtidas() - 1;
                        } else {
                            return;
                        }

                        pub.setCurtidas(novasCurtidas);

                        holder.txtNumCurtidas.setText(novasCurtidas + " curtidas");
                        holder.btnCurtir.setImageResource(
                                holder.isCurtido ? R.drawable.ic_like_filled : R.drawable.ic_like_white
                        );
                    } else {
                        Log.e("Curtida", "Erro: Resposta não foi bem-sucedida.");
                        Toast.makeText(context, "Erro ao processar curtida", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RespostaRegistroCurtida> call, Throwable t) {
                    Log.e("Curtida", "Erro: " + t.getMessage());
                    Toast.makeText(context, "Falha na conexão", Toast.LENGTH_SHORT).show();
                }
            });
        });


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        holder.itemView.setOnLongClickListener(view -> {
            if (uid.equals(pub.getId_perfil_usuario())) {
                new AlertDialog.Builder(context).setTitle("Opções da publicação").setItems(new String[]{"Editar", "Excluir"}, ((dialog, which) -> {
                    if (which == 0) {
                        Intent intent = new Intent(context, TelaEditarPublicacao.class);
                        intent.putExtra("ID_PUBLICACAO", pub.getId_publicacoes());
                        intent.putExtra("TITULO_ATUAL", pub.getTitulo());
                        intent.putExtra("DESCRICAO_ATUAL", pub.getDescricao());
                        intent.putExtra("FOTO_ATUAL_URL", pub.getFoto());
                        context.startActivity(intent);
                    } else if (which == 1) {
                        excluirPublicacao(pub.getId_publicacoes(), uid, holder.getAdapterPosition());
                    }
                })).show();
            }
            return true;
        });

        holder.btnComentar.setOnClickListener(v -> {
            Intent intent = new Intent(context, TelaComentarios.class);
            intent.putExtra("id_publicacoes", pub.getId_publicacoes());
            context.startActivity(intent);
        });
    }

    private void excluirPublicacao(int idPublicacao, String uid, int adapterPosition) {
        ApiService api = ApiClient.getClient().create(ApiService.class);

        Call<Void> call = api.excluirPublicacao(idPublicacao, uid);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    lista.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                    notifyItemRangeChanged(adapterPosition, lista.size());
                    Toast.makeText(context, "Publicação excluída", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Erro ao excluir", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Falha: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
        boolean isCurtido;

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