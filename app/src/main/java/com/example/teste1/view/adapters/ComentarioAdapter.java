package com.example.teste1.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.teste1.R;
import com.example.teste1.view.models.Comentario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
