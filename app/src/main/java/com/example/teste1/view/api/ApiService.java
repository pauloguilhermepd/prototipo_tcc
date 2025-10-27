package com.example.teste1.view.api;
import com.example.teste1.view.models.Comentario;
import com.example.teste1.view.models.Publicacao;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import com.example.teste1.view.RespostasRegistros.RegistroPerfilEstilo;
import com.example.teste1.view.RespostasRegistros.RespostaRegistroEstilo;
import com.example.teste1.view.RespostasRegistros.RespostaRegistroPerfil;
import com.example.teste1.view.models.Usuario;
import com.example.teste1.view.verification.VerificacaoUsuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;
public interface ApiService {
    @Multipart
    @POST("registrar_usuario.php")
    Call<RespostaRegistroPerfil> registrarPerfil(
            @Part("uid") RequestBody uidBody,
            @Part("nome") RequestBody nomeBody,
            @Part("data_aniver") RequestBody dataNascimento,
            @Part("bio") RequestBody bioBody,
            @Part("pronome") RequestBody pronomeBody,
            @Part MultipartBody.Part foto_perfil
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
            @Field("sub_estilo") String subEstilo,
            @Field("id_perfil_usuario") String id_perfil_usuario

    );

    /*
    @FormUrlEncoded
    @POST("registrar_usuario_estilo.php")
    Call<RegistroPerfilEstilo> registrarUsuarioEstilo(
            @Field("estilo") String estilo,
            @Field("sub_estilo") String subEstilo,
            @Field("id_perfil_usuario") String idPerfilUsuario
    );
    */
    @GET("verificar_usuario.php")
    Call<VerificacaoUsuario> verificarUsuario(@Query("uid") String uid);

    @GET("buscar_usuario.php")
    Call<Usuario> buscarUsuario(@Query("uid") String uid);



    @GET("listar_comentarios.php")
    Call<List<Comentario>> listarComentarios(
            @Query("id_publicacoes") int idPublicacoes
    );
}
