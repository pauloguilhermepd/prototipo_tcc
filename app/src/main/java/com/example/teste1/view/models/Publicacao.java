package com.example.teste1.view.models;

public class Publicacao {
    private int id_publicacoes;
    private String titulo;
    private String descricao;
    private int quantidade_curtidas;
    private int quantidade_comentarios;

    public int getId_publicacoes() { return id_publicacoes; }
    public void setId_publicacoes(int id_publicacoes) { this.id_publicacoes = id_publicacoes; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getQuantidade_curtidas() { return quantidade_curtidas; }
    public void setQuantidade_curtidas(int quantidade_curtidas) { this.quantidade_curtidas = quantidade_curtidas; }

    public int getQuantidade_comentarios() { return quantidade_comentarios; }
    public void setQuantidade_comentarios(int quantidade_comentarios) { this.quantidade_comentarios = quantidade_comentarios; }
}
