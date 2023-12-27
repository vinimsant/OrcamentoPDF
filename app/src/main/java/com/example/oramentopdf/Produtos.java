package com.example.oramentopdf;

import java.io.Serializable;

public class Produtos implements Serializable {

    private String nome;
    private String descricao;
    private String valorAv;
    private String valorCartao;
    private byte[] foto;
    private byte[] foto1;
    private byte [] foto2;
    private int id;


    public byte[] getFoto1() {
        return foto1;
    }

    public void setFoto1(byte[] foto1) {
        this.foto1 = foto1;
    }

    public byte[] getFoto2() {
        return foto2;
    }

    public void setFoto2(byte[] foto2) {
        this.foto2 = foto2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getValorAv() {
        return valorAv;
    }

    public void setValorAv(String valorAv) {
        this.valorAv = valorAv;
    }

    public String getValorCartao() {
        return valorCartao;
    }

    public void setValorCartao(String valorCartao) {
        this.valorCartao = valorCartao;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    @Override
    public String toString(){

        return this.getNome();
    }
}
