package com.example.teste1.view.models;

public class Usuario {
    private String id_perfil_usuario;
    private String nome_completo;
    private String data_nascimento;
    private String biografia;
    private String pronomes;
    private String foto_perfil;
    private String status; // opcional (se PHP mandar)
    private String estilo;
    private String sub_estilo;
    public String getId_perfil_usuario() { return id_perfil_usuario; }
    public String getNome_completo() { return nome_completo; }
    public String getData_nascimento() { return data_nascimento; }
    public String getBiografia() { return biografia; }
    public String getPronomes() { return pronomes; }
    public String getFoto_perfil() { return foto_perfil; }
    public String getStatus() { return status; }
    public String getEstilo() { return estilo; }
    public String getSub_estilo() { return sub_estilo; }
}
