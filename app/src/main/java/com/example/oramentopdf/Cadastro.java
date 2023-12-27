package com.example.oramentopdf;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class Cadastro extends AppCompatActivity {


    EditText txtNome;
    EditText txtDescricao;
    EditText txtValorAv;
    EditText txtValorC;
    ImageView foto;
    Button btnSalvar;
    String tag;
    int comparador;
    int salvarOuAlterar;
    int id;
    ImageView foto1;
    ImageView foto2;
    int comparadorF1;
    int comparadorF2;
    boolean compF;
    boolean compF1;
    boolean compF2;
    int larguraFotoRetrato;
    int alturaFotoRetrato;
    int larguraFotoPaisagem;
    int alturaFotoPaisagem;
    ImageView imgSalvar;
    private InterstitialAd mInterstitialAd;
    int qualidade;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //imgSalvar = findViewById(R.id.imageViewSalvar);
        txtNome = findViewById(R.id.txtNome);
        txtDescricao = findViewById(R.id.txtDescricao);
        txtValorAv = findViewById(R.id.txtValorAv);
        txtValorC = findViewById(R.id.txtValorC);
        foto = findViewById(R.id.foto3);
        //btnSalvar = findViewById(R.id.btnSalvar);
        foto1 = findViewById(R.id.novaFoto1);
        foto2 = findViewById(R.id.novaFoto2);
        comparador = 0;
        salvarOuAlterar = 0;
        comparadorF1 = 0;
        comparadorF2 = 0;
        compF = false;
        compF1 = false;
        compF2 = false;
        larguraFotoRetrato = 263;
        alturaFotoRetrato = 350;
        larguraFotoPaisagem = 200;
        alturaFotoPaisagem = 150;
        qualidade = 100;


        Intent intent = getIntent();
        if (intent.hasExtra("Produto")){
            salvarOuAlterar = 1;
            Produtos produtos = (Produtos)intent.getSerializableExtra("Produto");
            id = produtos.getId();
            txtNome.setText(produtos.getNome());
            txtDescricao.setText(produtos.getDescricao());
            txtValorC.setText(produtos.getValorCartao());
            txtValorAv.setText(produtos.getValorAv());
            if (produtos.getFoto()!=null){
                Bitmap img = BitmapFactory.decodeByteArray(produtos.getFoto(), 0, produtos.getFoto().length);
                foto.setImageBitmap(img);
                comparador = 1;
            }
            if (produtos.getFoto1()!=null){
                Bitmap img = BitmapFactory.decodeByteArray(produtos.getFoto1(), 0, produtos.getFoto1().length);
                foto1.setImageBitmap(img);
                comparadorF1 = 1;
            }
            if (produtos.getFoto2()!=null){
                Bitmap img = BitmapFactory.decodeByteArray(produtos.getFoto2(), 0, produtos.getFoto2().length);
                foto2.setImageBitmap(img);
                comparadorF2 = 1;
            }
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2398950190237031/1527616191");

        //ca-app-pub-2398950190237031/5849014909

        //ca-app-pub-2398950190237031/5849014909
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                Intent intent = new Intent(Cadastro.this, MainActivity.class);
                startActivity(intent);


            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.salvarMC){
            salvar2();



        }
        return super.onOptionsItemSelected(item);

}

    public void salvar(View view){
        Produtos p = new Produtos();
        Dao d = new Dao(this);
        if (txtNome.getText().length() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Por Favor Adicione um Nome ao Produto!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            // Toast.makeText(this, "Por Favor Adicione um Nome ao Produto!", Toast.LENGTH_SHORT).show();
        }else if (salvarOuAlterar == 0){
            p.setNome(txtNome.getText().toString());
            p.setDescricao(txtDescricao.getText().toString());
            p.setValorAv(txtValorAv.getText().toString());
            p.setValorCartao(txtValorC.getText().toString());

            if (comparador != 0){
                Bitmap bitmap = ((BitmapDrawable)foto.getDrawable()).getBitmap();

       /* Bitmap yourBitmap;
        Bitmap resized = Bitmap.createScaledBitmap(yourBitmap, newWidth, newHeight, true);
// ou:
        resized = Bitmap.createScaledBitmap(yourBitmap,(int)(yourBitmap.getWidth()*0.3), (int)(yourBitmap.getHeight()*0.3), true);
*/
                Bitmap redimensionado = Bitmap.createScaledBitmap(bitmap, larguraFotoRetrato, alturaFotoRetrato, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionado.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto(img.toByteArray());
            }else {

                Bitmap imagem = BitmapFactory.decodeResource(getResources(), R.drawable.download_foto);
                Bitmap redimensionamento = Bitmap.createScaledBitmap(imagem, larguraFotoRetrato, alturaFotoRetrato, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionamento.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto(img.toByteArray());

            }

            if (comparadorF1 != 0){
                Bitmap bitmap = ((BitmapDrawable)foto1.getDrawable()).getBitmap();


                Bitmap redimensionado = Bitmap.createScaledBitmap(bitmap, larguraFotoPaisagem, alturaFotoPaisagem, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionado.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto1(img.toByteArray());
            }else {

                Bitmap imagem = BitmapFactory.decodeResource(getResources(), R.drawable.download_foto);
                Bitmap redimensionamento = Bitmap.createScaledBitmap(imagem, larguraFotoPaisagem, alturaFotoPaisagem, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionamento.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto1(img.toByteArray());

            }

            if (comparadorF2 != 0){
                Bitmap bitmap = ((BitmapDrawable)foto2.getDrawable()).getBitmap();

                Bitmap redimensionado = Bitmap.createScaledBitmap(bitmap, larguraFotoPaisagem, alturaFotoPaisagem, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionado.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto2(img.toByteArray());
            }else {

                Bitmap imagem = BitmapFactory.decodeResource(getResources(), R.drawable.download_foto);
                Bitmap redimensionamento = Bitmap.createScaledBitmap(imagem, larguraFotoPaisagem, alturaFotoPaisagem, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionamento.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto2(img.toByteArray());

            }


            d.inserir(p);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Produto inserido com sucesso!");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                   gerrarIntertissial();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            //Toast.makeText(this, "Produto inserido com sucesso!", Toast.LENGTH_SHORT).show();

        }else {
            p.setNome(txtNome.getText().toString());
            p.setDescricao(txtDescricao.getText().toString());
            p.setValorAv(txtValorAv.getText().toString());
            p.setValorCartao(txtValorC.getText().toString());
            p.setId(id);

            if (comparador != 0){
                Bitmap bitmap = ((BitmapDrawable)foto.getDrawable()).getBitmap();

       /* Bitmap yourBitmap;
        Bitmap resized = Bitmap.createScaledBitmap(yourBitmap, newWidth, newHeight, true);
// ou:
        resized = Bitmap.createScaledBitmap(yourBitmap,(int)(yourBitmap.getWidth()*0.3), (int)(yourBitmap.getHeight()*0.3), true);
*/
                Bitmap redimensionado = Bitmap.createScaledBitmap(bitmap, larguraFotoRetrato, alturaFotoRetrato, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionado.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto(img.toByteArray());
            }else {

                Bitmap imagem = BitmapFactory.decodeResource(getResources(), R.drawable.download_foto);
                Bitmap redimensionamento = Bitmap.createScaledBitmap(imagem, larguraFotoRetrato, alturaFotoRetrato, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionamento.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto(img.toByteArray());

            }

            if (comparadorF1 != 0){
                Bitmap bitmap = ((BitmapDrawable)foto1.getDrawable()).getBitmap();

                Bitmap redimensionado = Bitmap.createScaledBitmap(bitmap, larguraFotoPaisagem, alturaFotoPaisagem, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionado.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto1(img.toByteArray());
            }else {

                Bitmap imagem = BitmapFactory.decodeResource(getResources(), R.drawable.download_foto);
                Bitmap redimensionamento = Bitmap.createScaledBitmap(imagem, larguraFotoPaisagem, alturaFotoPaisagem, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionamento.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto1(img.toByteArray());

            }

            if (comparadorF2 != 0){
                Bitmap bitmap = ((BitmapDrawable)foto2.getDrawable()).getBitmap();

                Bitmap redimensionado = Bitmap.createScaledBitmap(bitmap, larguraFotoPaisagem, alturaFotoPaisagem, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionado.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto2(img.toByteArray());
            }else {

                Bitmap imagem = BitmapFactory.decodeResource(getResources(), R.drawable.download_foto);
                Bitmap redimensionamento = Bitmap.createScaledBitmap(imagem, larguraFotoPaisagem, alturaFotoPaisagem, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionamento.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto2(img.toByteArray());

            }


            d.atualizar(p);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Produto alterado com sucesso!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    gerrarIntertissial();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            //Toast.makeText(this, "Produto inserido com sucesso!", Toast.LENGTH_SHORT).show();





        }

    }

    public void selecionarFoto(View view){

        comparador = 1;
        compF = true;

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Selecionar Imagem"), 0);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri arquivoUri = data.getData();
            if (compF == true){
                foto.setImageURI(arquivoUri);
                compF = false;

            }else if (compF1 == true){
                foto1.setImageURI(arquivoUri);
                compF1 = false;

            }else if (compF2 == true){
                foto2.setImageURI(arquivoUri);
                compF2 = false;

            }

        }
    }


    public void selecionarFoto1(View view){

        comparadorF1 = 1;
        compF1 = true;

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Selecionar Imagem"), 0);

    }

    public void selecionarFoto2(View view){

        comparadorF2 = 1;
        compF2 = true;

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Selecionar Imagem"), 0);

    }

    public void salvar2(){
        Produtos p = new Produtos();
        Dao d = new Dao(this);
        if (txtNome.getText().length() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Por Favor Adicione um Nome ao Produto!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            // Toast.makeText(this, "Por Favor Adicione um Nome ao Produto!", Toast.LENGTH_SHORT).show();
        }else if (salvarOuAlterar == 0){
            p.setNome(txtNome.getText().toString());
            p.setDescricao(txtDescricao.getText().toString());
            p.setValorAv(txtValorAv.getText().toString());
            p.setValorCartao(txtValorC.getText().toString());

            if (comparador != 0){
                Bitmap bitmap = ((BitmapDrawable)foto.getDrawable()).getBitmap();

       /* Bitmap yourBitmap;
        Bitmap resized = Bitmap.createScaledBitmap(yourBitmap, newWidth, newHeight, true);
// ou:
        resized = Bitmap.createScaledBitmap(yourBitmap,(int)(yourBitmap.getWidth()*0.3), (int)(yourBitmap.getHeight()*0.3), true);
*/
                Bitmap redimensionado = Bitmap.createScaledBitmap(bitmap, larguraFotoRetrato, alturaFotoRetrato, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionado.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto(img.toByteArray());
            }else {

                Bitmap imagem = BitmapFactory.decodeResource(getResources(), R.drawable.download_foto);
                Bitmap redimensionamento = Bitmap.createScaledBitmap(imagem, larguraFotoRetrato, alturaFotoRetrato, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionamento.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto(img.toByteArray());

            }

            if (comparadorF1 != 0){
                Bitmap bitmap = ((BitmapDrawable)foto1.getDrawable()).getBitmap();


                Bitmap redimensionado = Bitmap.createScaledBitmap(bitmap, larguraFotoPaisagem, alturaFotoPaisagem, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionado.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto1(img.toByteArray());
            }else {

                Bitmap imagem = BitmapFactory.decodeResource(getResources(), R.drawable.download_foto);
                Bitmap redimensionamento = Bitmap.createScaledBitmap(imagem, larguraFotoPaisagem, alturaFotoPaisagem, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionamento.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto1(img.toByteArray());

            }

            if (comparadorF2 != 0){
                Bitmap bitmap = ((BitmapDrawable)foto2.getDrawable()).getBitmap();

                Bitmap redimensionado = Bitmap.createScaledBitmap(bitmap, larguraFotoPaisagem, alturaFotoPaisagem, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionado.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto2(img.toByteArray());
            }else {

                Bitmap imagem = BitmapFactory.decodeResource(getResources(), R.drawable.download_foto);
                Bitmap redimensionamento = Bitmap.createScaledBitmap(imagem, larguraFotoPaisagem, alturaFotoPaisagem, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionamento.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto2(img.toByteArray());

            }


            d.inserir(p);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Produto inserido com sucesso!");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   gerrarIntertissial();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            //Toast.makeText(this, "Produto inserido com sucesso!", Toast.LENGTH_SHORT).show();

        }else {
            p.setNome(txtNome.getText().toString());
            p.setDescricao(txtDescricao.getText().toString());
            p.setValorAv(txtValorAv.getText().toString());
            p.setValorCartao(txtValorC.getText().toString());
            p.setId(id);

            if (comparador != 0){
                Bitmap bitmap = ((BitmapDrawable)foto.getDrawable()).getBitmap();

       /* Bitmap yourBitmap;
        Bitmap resized = Bitmap.createScaledBitmap(yourBitmap, newWidth, newHeight, true);
// ou:
        resized = Bitmap.createScaledBitmap(yourBitmap,(int)(yourBitmap.getWidth()*0.3), (int)(yourBitmap.getHeight()*0.3), true);
*/
                Bitmap redimensionado = Bitmap.createScaledBitmap(bitmap, larguraFotoRetrato, alturaFotoRetrato, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionado.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto(img.toByteArray());
            }else {

                Bitmap imagem = BitmapFactory.decodeResource(getResources(), R.drawable.download_foto);
                Bitmap redimensionamento = Bitmap.createScaledBitmap(imagem, larguraFotoRetrato, alturaFotoRetrato, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionamento.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto(img.toByteArray());

            }

            if (comparadorF1 != 0){
                Bitmap bitmap = ((BitmapDrawable)foto1.getDrawable()).getBitmap();

                Bitmap redimensionado = Bitmap.createScaledBitmap(bitmap, larguraFotoPaisagem, alturaFotoPaisagem, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionado.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto1(img.toByteArray());
            }else {

                Bitmap imagem = BitmapFactory.decodeResource(getResources(), R.drawable.download_foto);
                Bitmap redimensionamento = Bitmap.createScaledBitmap(imagem, larguraFotoPaisagem, alturaFotoPaisagem, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionamento.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto1(img.toByteArray());

            }

            if (comparadorF2 != 0){
                Bitmap bitmap = ((BitmapDrawable)foto2.getDrawable()).getBitmap();

                Bitmap redimensionado = Bitmap.createScaledBitmap(bitmap, larguraFotoPaisagem, alturaFotoPaisagem, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionado.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto2(img.toByteArray());
            }else {

                Bitmap imagem = BitmapFactory.decodeResource(getResources(), R.drawable.download_foto);
                Bitmap redimensionamento = Bitmap.createScaledBitmap(imagem, larguraFotoPaisagem, alturaFotoPaisagem, true);
                ByteArrayOutputStream img = new ByteArrayOutputStream();
                redimensionamento.compress(Bitmap.CompressFormat.PNG, qualidade, img);
                p.setFoto2(img.toByteArray());

            }


            d.atualizar(p);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Produto alterado com sucesso!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                   gerrarIntertissial();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            //Toast.makeText(this, "Produto inserido com sucesso!", Toast.LENGTH_SHORT).show();

        }

    }

    public void gerrarIntertissial(){

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();

        } else {
            //Log.d("TAG", "The interstitial wasn't loaded yet.");
            Intent intent = new Intent(Cadastro.this, MainActivity.class);
            startActivity(intent);

        }

    }




}
