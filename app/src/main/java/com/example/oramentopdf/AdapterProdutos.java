package com.example.oramentopdf;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class AdapterProdutos extends RecyclerView.Adapter<AdapterProdutos.MyViewHolder> {

    private List<Produtos> listaProdutos;


    public AdapterProdutos(List<Produtos> lista) {
        this.listaProdutos = lista;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_produtos, parent, false);


        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        Produtos produtos = listaProdutos.get(position);
        if (produtos.getFoto() != null) {
            Bitmap img = BitmapFactory.decodeByteArray(produtos.getFoto(), 0, produtos.getFoto().length);
            holder.foto.setImageBitmap(img);
        } else {
            holder.foto.setImageResource(R.drawable.ic_insert_photo_black_24dp);
        }
        /*if (produtos.getFoto1() != null) {
            Bitmap img = BitmapFactory.decodeByteArray(produtos.getFoto1(), 0, produtos.getFoto1().length);
            holder.foto1.setImageBitmap(img);
        } else {
            holder.foto1.setImageResource(R.drawable.ic_insert_photo_black_24dp);
        }
        if (produtos.getFoto2() != null) {
            Bitmap img = BitmapFactory.decodeByteArray(produtos.getFoto2(), 0, produtos.getFoto2().length);
            holder.foto2.setImageBitmap(img);
        } else {
            holder.foto2.setImageResource(R.drawable.ic_insert_photo_black_24dp);
        }*/

        holder.nome.setText(produtos.getNome());
        holder.valorC.setText(produtos.getValorCartao());
        holder.valorAv.setText(produtos.getValorAv());

    }

           @Override
    public int getItemCount() {

        return listaProdutos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView foto;
        TextView nome;
        TextView valorAv;
        TextView valorC;
        ImageView foto1;
        ImageView foto2;


        public MyViewHolder(View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.fotoLista);
            nome = itemView.findViewById(R.id.tNome);
            valorAv = itemView.findViewById(R.id.tValorAv);
            valorC = itemView.findViewById(R.id.tValorC);
            //foto1 = itemView.findViewById(R.id.fotoLista5);
            //foto2 = itemView.findViewById(R.id.fotoLista4);
        }




    }

}
