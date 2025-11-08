package com.example.teste1.view.RespostasRegistros;

public class RespostaRegistroPerfil {
    private String status;
    private String id_perfil_usuario;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setId_perfil_usuario(String id_perfil_usuario) {
        this.id_perfil_usuario = id_perfil_usuario;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus(){ return status; }

    public  String getId_perfil_usuario() { return id_perfil_usuario; }

}

