package com.example.teste1.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.teste1.R;
import com.example.teste1.view.models.Publicacao;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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

        Glide.with(context)
                .load(pub.getFoto())
                .placeholder(R.drawable.icons8_mais_100)
                .into(holder.imgPublicacao);

        Glide.with(context)
                .load(pub.getAutor_foto())
                .placeholder(R.drawable.icons8_mais_100)
                .into(holder.imgAutor);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgAutor;
        ImageView imgPublicacao;
        TextView txtTitulo, txtDescricao, txtAutorNome;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAutor = itemView.findViewById(R.id.img_autor);
            imgPublicacao = itemView.findViewById(R.id.img_publicacao);
            txtTitulo = itemView.findViewById(R.id.txt_titulo_publi);
            txtDescricao = itemView.findViewById(R.id.txt_descricao_publi);
            txtAutorNome = itemView.findViewById(R.id.txt_autor_nome);
        }
    }
}