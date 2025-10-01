package com.example.teste1.view.api;
import com.example.teste1.view.models.Comentario;
import com.example.teste1.view.models.Publicacao;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Field;
import retrofit2.http.Query;
public interface ApiService {
    @FormUrlEncoded
    @POST("registrar_perfil.php")
    Call<Void> registrarPerfil(
            @Field("uid") String uid,
            @Field("nome") String nome,
            @Field("data_nascimento") String dataNascimento,
            @Field("biografia") String biografia,
            @Field("pronomes") String pronomes
    );

    // === PUBLICAÇÕES ===
    @GET("listar_publicacoes.php")
    Call<List<Publicacao>> listarPublicacoes();

    // === COMENTÁRIOS ===
    @FormUrlEncoded
    @POST("registrar_comentario.php")
    Call<Void> registrarComentario(
            @Field("id_publicacoes") int idPublicacoes,
            @Field("id_usuario") int idUsuario,
            @Field("comentarios") String comentarios
    );

    @GET("listar_comentarios.php")
    Call<List<Comentario>> listarComentarios(
            @Query("id_publicacoes") int idPublicacoes
    );
}
