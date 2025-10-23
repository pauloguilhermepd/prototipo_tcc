package com.example.teste1.view.models;

public class Usuario {
    private String nome;
    private String pronome;
    private String bio;
    private String foto_perfil;

    // Getters (Retrofit precisa deles)
    public String getNome() {
        return nome;
    }

    public String getPronome() {
        return pronome;
    }

    public String getBio() {
        return bio;
    }

    public String getFotoPerfil() {
        return foto_perfil;
    }
}
