package com.example.teste1.view.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.teste1.R;
import com.example.teste1.view.RespostasRegistros.RespostaRegistroPerfil;
import com.example.teste1.view.api.ApiClient;
import com.example.teste1.view.api.ApiService;
import com.example.teste1.view.models.Comentario;
import com.example.teste1.view.telas_usuario.TelaPerfil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ViewHolder> {
    private List<Comentario> lista;
    private Context context;

    public ComentarioAdapter(List<Comentario> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_comentario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comentario c = lista.get(position);
        holder.txtAutor.setText(c.getAutor_nome());
        holder.txtComentario.setText(c.getComentarios());

        Glide.with(context)
                .load(c.getAutorFoto())
                .placeholder(R.drawable.icons8_usuario_24)
                .into(holder.imgAutor);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String autorUID = c.getId_usuario(); //

        View.OnClickListener listenerPerfil = v -> {
            if (autorUID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                return;
            }
            Intent intent = new Intent(context, TelaPerfil.class);
            intent.putExtra("uid_perfil", autorUID);
            context.startActivity(intent);
        };

        holder.imgAutor.setOnClickListener(listenerPerfil);
        holder.txtAutor.setOnClickListener(listenerPerfil);
        holder.itemView.setOnLongClickListener(view -> {
            if(uid.equals(c.getId_usuario())){

                new AlertDialog.Builder(context)
                        .setTitle("Opções do Comentário")
                        .setItems(new String[]{"Editar", "Excluir"}, ((dialog, which) -> {

                            if(which == 0) {
                                mostrarDialogoEdicao(c, holder.getAdapterPosition());

                            } else if (which == 1) {
                                excluirComentario(c.getId_comentario(), uid, holder.getAdapterPosition());
                            }
                        })).show();
            }
            return true;
        });
    }

    private void mostrarDialogoEdicao(Comentario comentario, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Editar Comentário");

        final EditText input = new EditText(context);
        input.setText(comentario.getComentarios());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton("Salvar", (dialog, which) -> {
            String novoTexto = input.getText().toString().trim();
            if (!novoTexto.isEmpty()) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                editarComentario(comentario.getId_comentario(), uid, novoTexto, position);
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void editarComentario(int idComentario, String uid, String novoTexto, int position) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<RespostaRegistroPerfil> call = api.editarComentario(idComentario, uid, novoTexto);

        call.enqueue(new Callback<RespostaRegistroPerfil>() {
            @Override
            public void onResponse(Call<RespostaRegistroPerfil> call, Response<RespostaRegistroPerfil> response) {
                if (response.isSuccessful() && "success".equals(response.body().getStatus())) {
                    Comentario c = lista.get(position);
                    c.setComentarios(novoTexto);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Comentário atualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Erro ao editar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespostaRegistroPerfil> call, Throwable t) {
                Toast.makeText(context, "Falha: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void excluirComentario(int idComentario, String uid, int position) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<RespostaRegistroPerfil> call = api.excluirComentario(idComentario, uid);

        call.enqueue(new Callback<RespostaRegistroPerfil>() {
            @Override
            public void onResponse(Call<RespostaRegistroPerfil> call, Response<RespostaRegistroPerfil> response) {
                if (response.isSuccessful() && "success".equals(response.body().getStatus())) {
                    // Remove da lista local
                    lista.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, lista.size());
                    Toast.makeText(context, "Comentário excluído", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Erro ao excluir", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespostaRegistroPerfil> call, Throwable t) {
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
        TextView txtAutor, txtComentario;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAutor = itemView.findViewById(R.id.img_autor_coment);
            txtAutor = itemView.findViewById(R.id.txt_autor_coment);
            txtComentario = itemView.findViewById(R.id.txt_texto_coment);
        }
    }
}