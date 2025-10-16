package com.example.teste1.view.api;
import com.example.teste1.view.models.Comentario;
import com.example.teste1.view.models.Publicacao;


import com.example.teste1.view.models.RespostasRegistros.RegistroPerfilEstilo;
import com.example.teste1.view.models.RespostasRegistros.RespostaRegistroEstilo;
import com.example.teste1.view.models.RespostasRegistros.RespostaRegistroPerfil;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Field;
import retrofit2.http.Query;
public interface ApiService {
    @FormUrlEncoded
    @POST("registrar_usuario.php")
    Call<RespostaRegistroPerfil> registrarPerfil(
            @Field("uid") String uid,
            @Field("nome") String nome,
            @Field("data_nascimento") String dataNascimento,
            @Field("biografia") String biografia,
            @Field("pronomes") String pronomes
    );

    @GET("listar_publicacoes.php")
    Call<List<Publicacao>> listarPublicacoes();

    @FormUrlEncoded
    @POST("registrar_comentario.php")
    Call<Void> registrarComentario(
            @Field("id_publicacoes") int idPublicacoes,
            @Field("id_usuario") String idUsuario,
            @Field("comentarios") String comentarios
    );

    @FormUrlEncoded
    @POST("registrar_estilo.php")
    Call<RespostaRegistroEstilo> registrar_estilo(
            @Field("estilo") String estilo,
            @Field("sub_estilo") String subEstilo

    );

    @FormUrlEncoded
    @POST("registrar_usuario_estilo.php")
    Call<RegistroPerfilEstilo> registrarUsuarioEstilo(
            @Field("estilo") String estilo,
            @Field("sub_estilo") String subEstilo,
            @Field("id_perfil_usuario") String idPerfilUsuario
    );

    @GET("listar_comentarios.php")
    Call<List<Comentario>> listarComentarios(
            @Query("id_publicacoes") int idPublicacoes
    );
}
