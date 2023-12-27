package com.example.oramentopdf;

public class ProdutoCapa {

    String titulo;
    String rodaPe;
    byte[] foto;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getRodaPe() {
        return rodaPe;
    }

    public void setRodaPe(String rodaPe) {
        this.rodaPe = rodaPe;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
}
