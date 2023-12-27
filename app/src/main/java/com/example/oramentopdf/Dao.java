package com.example.oramentopdf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Dao {
    private SQLiteDatabase bd;
    public Dao(Context ctx){
        Conexao auxDao = new Conexao(ctx);
        bd = auxDao.getWritableDatabase();
    }

    public void inserirCapa(ProdutoCapa pc){
        ContentValues values = new ContentValues();
        values.put("capa", pc.getFoto());
        values.put("titulo", pc.getTitulo());
        values.put("rodaPe", pc.getRodaPe());
        bd.insert("capa", null, values);
    }

    public void inserir(Produtos p){
        ContentValues valores = new ContentValues();
        valores.put("nome", p.getNome());
        valores.put("descricao", p.getDescricao());
        valores.put("valorAv", p.getValorAv());
        valores.put("valorC", p.getValorCartao());
        valores.put("foto", p.getFoto());
        valores.put("foto1", p.getFoto1());
        valores.put("foto2", p.getFoto2());
        bd.insert("produtos", null, valores);

    }

    public void atualizarCapa(ProdutoCapa pc){
        ContentValues values = new ContentValues();
        values.put("capa", pc.getFoto());
        values.put("titulo", pc.getTitulo());
        values.put("rodaPe", pc.getRodaPe());
        bd.update("capa", values, "_id = ?", new String[]{"1"});
    }

    public void atualizar(Produtos produtos){
        ContentValues valores = new ContentValues();
        valores.put("nome", produtos.getNome());
        valores.put("descricao", produtos.getDescricao());
        valores.put("valorAv", produtos.getValorAv());
        valores.put("valorC", produtos.getValorCartao());
        valores.put("foto", produtos.getFoto());
        valores.put("foto1", produtos.getFoto1());
        valores.put("foto2", produtos.getFoto2());

        bd.update("produtos", valores, "_id = ?", new String[]{""+produtos.getId()});

    }

    public void deletar(Produtos produtos){

        bd.delete("produtos", "_id = "+produtos.getId(), null);
    }

    public List<ProdutoCapa> buscarCapa(){
        List<ProdutoCapa> list = new ArrayList<>();
        String[] colunas = new String[]{"_id", "titulo", "rodaPe", "capa"};

        Cursor cursor = bd.query("capa", colunas, null, null, null, null, "_id ASC");
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                ProdutoCapa pc = new ProdutoCapa();
                pc.setFoto(cursor.getBlob(3));
                pc.setRodaPe(cursor.getString(2));
                pc.setTitulo(cursor.getString(1));
                pc.setId(cursor.getInt(0));

                list.add(pc);
            }while (cursor.moveToNext());
        }
        return (list);
    }

    public List<Produtos> buscar(){
        List<Produtos> list = new ArrayList<Produtos>();
        String[] colunas = new String[]{"_id", "nome", "descricao", "valorAv", "valorC", "foto", "foto1", "foto2"};

        Cursor cursor = bd.query("produtos", colunas, null, null, null, null, "nome ASC");
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                Produtos p = new Produtos();
                p.setId(cursor.getInt(0));
                p.setNome(cursor.getString(1));
                p.setDescricao(cursor.getString(2));
                p.setValorAv(cursor.getString(3));
                p.setValorCartao(cursor.getString(4));
                p.setFoto(cursor.getBlob(5));
                p.setFoto1(cursor.getBlob(6));
                p.setFoto2(cursor.getBlob(7));

                list.add(p);
            }while (cursor.moveToNext());
        }
        return (list);

}

}
