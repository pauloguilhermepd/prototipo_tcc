package com.example.teste1.view.models;

public class Comentario {
    private int id_comentario;
    private String comentarios;
    private String data_comentario;
    private int id_usuario;

    // Getters e Setters
    public int getId_comentario() { return id_comentario; }
    public void setId_comentario(int id_comentario) { this.id_comentario = id_comentario; }

    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }

    public String getData_comentario() { return data_comentario; }
    public void setData_comentario(String data_comentario) { this.data_comentario = data_comentario; }

    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }
}
