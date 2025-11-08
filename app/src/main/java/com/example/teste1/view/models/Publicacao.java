package com.example.teste1.view.models;
    public class Publicacao {
        private int id_publicacoes;
        private String titulo;
        private String descricao;
        private String foto;
        private String data_publicacao;
        private String autor_nome;
        private String autor_foto;
        private int curtidas;
        private String id_perfil_usuario;

        // Getters e Setters


        public String getId_perfil_usuario() {
            return id_perfil_usuario;
        }

        public void setId_perfil_usuario(String id_perfil_usuario) {
            this.id_perfil_usuario = id_perfil_usuario;
        }

        public int getId_publicacoes() { return id_publicacoes; }
        public void setId_publicacoes(int id_publicacoes) { this.id_publicacoes = id_publicacoes; }

        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }

        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }

        public String getFoto() { return foto; }
        public void setFoto(String foto) { this.foto = foto; }

        public String getData_publicacao() { return data_publicacao; }
        public void setData_publicacao(String data_publicacao) { this.data_publicacao = data_publicacao; }

        public String getAutor_nome() { return autor_nome; }
        public void setAutor_nome(String autor_nome) { this.autor_nome = autor_nome; }

        public String getAutor_foto() { return autor_foto; }
        public void setAutor_foto(String autor_foto) { this.autor_foto = autor_foto; }

        public int getCurtidas() {
            return curtidas;
        }

        public void setCurtidas(int cutidas) {
            this.curtidas = cutidas;
        }
    }


