package com.example.oramentopdf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Conexao extends SQLiteOpenHelper {

    private static final String NOME_BD = "Cadastro2";
    private static final int VERSAO = 22;


    public Conexao(Context context) {
        super(context, NOME_BD, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table produtos(_id integer primary key autoincrement, nome text not null, " +
                "descricao text, valorAv text, valorC text, foto blob, foto1 blob, foto2 blob);");
        db.execSQL("create table capa(_id integer primary key autoincrement, titulo text, rodaPe text, " +
                "capa blob)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        List<Produtos> list = new ArrayList<Produtos>();

        String[] colunas = new String[]{"_id", "nome", "descricao", "valorAv", "valorC", "foto", "foto1", "foto2"};

        Cursor cursor = db.query("produtos", colunas, null, null, null, null, "nome ASC");
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


        db.execSQL("drop table produtos;");
        db.execSQL("drop table capa");
        onCreate(db);
        for (int i = 0; i < list.size(); i++){
            Produtos p1 = new Produtos();
            p1 = list.get(i);
            ContentValues valores = new ContentValues();
            valores.put("nome", p1.getNome());
            valores.put("descricao", p1.getDescricao());
            valores.put("valorAv", p1.getValorAv());
            valores.put("valorC", p1.getValorCartao());
            valores.put("foto", p1.getFoto());
            valores.put("foto1", p1.getFoto1());
            valores.put("foto2", p1.getFoto2());
            db.insert("produtos", null, valores);



        }


    }
}
