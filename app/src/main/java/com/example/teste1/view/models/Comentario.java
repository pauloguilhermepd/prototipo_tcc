package com.example.teste1.view.models;

public class Comentario {
    private int id_comentario;
    private String comentarios;
    private String data_comentario;
    private String id_usuario;
    private String autor_foto;
    private String autor_nome;
    // Getters e Setters

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getAutor_foto() {
        return autor_foto;
    }

    public void setAutor_foto(String autor_foto) {
        this.autor_foto = autor_foto;
    }

    public String getAutor_nome() {
        return autor_nome;
    }

    public void setAutor_nome(String autor_nome) {
        this.autor_nome = autor_nome;
    }

    public int getId_comentario() { return id_comentario; }
    public void setId_comentario(int id_comentario) { this.id_comentario = id_comentario; }

    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }

    public String getData_comentario() { return data_comentario; }
    public void setData_comentario(String data_comentario) { this.data_comentario = data_comentario; }

    public String getAutorFoto() {
        return autor_foto;
    }

    public void setAutorFoto(String id_autor_foto) {
        this.autor_foto = id_autor_foto;
    }
}
