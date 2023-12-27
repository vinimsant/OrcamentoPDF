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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CapaPDF extends AppCompatActivity {

    EditText txtTitulo;
    EditText txtRodaPé;
    ImageView imgCapa;
    int larguraFoto;
    int alturaFoto;
    List<ProdutoCapa> listaPcM = new ArrayList<>();
    private InterstitialAd mInterstitialAd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capa_pdf);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtTitulo = findViewById(R.id.txtTituloCapa);
        txtRodaPé = findViewById(R.id.txtRodaPeCapa);
        imgCapa = findViewById(R.id.imgCapa);
        Dao d = new Dao(this);
        listaPcM = d.buscarCapa();

        imgCapa.setImageResource(R.drawable.download_foto);
        larguraFoto = 520;
        alturaFoto = 600;

        if (listaPcM.size() > 0){
            ProdutoCapa pcM = new ProdutoCapa();
            pcM = listaPcM.get(0);
            txtTitulo.setText(pcM.getTitulo());
            txtRodaPé.setText(pcM.getRodaPe());
            Bitmap img = BitmapFactory.decodeByteArray(pcM.getFoto(), 0, pcM.getFoto().length);
            imgCapa.setImageBitmap(img);
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2398950190237031/1527616191");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                Intent intent = new Intent(CapaPDF.this, MainActivity.class);
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
            salvar();



        }
        return super.onOptionsItemSelected(item);

    }

    public void selecionarFoto(View view){


        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Selecionar Imagem para a Capa"), 0);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri arquivoUri = data.getData();
            imgCapa.setImageURI(arquivoUri);


        }
    }

    public void salvar(){
        List<ProdutoCapa> listaPc = new ArrayList<>();
        ProdutoCapa pc = new ProdutoCapa();
        Dao d = new Dao(this);
        listaPc = d.buscarCapa();
        if (listaPc.size() > 0){
            //aqui eu atualizo
            pc.setTitulo(txtTitulo.getText().toString());
            pc.setRodaPe(txtRodaPé.getText().toString());
            Bitmap bitmap = ((BitmapDrawable)imgCapa.getDrawable()).getBitmap();

            Bitmap redimensionado = Bitmap.createScaledBitmap(bitmap, larguraFoto, alturaFoto, true);
            ByteArrayOutputStream img = new ByteArrayOutputStream();
            redimensionado.compress(Bitmap.CompressFormat.PNG, 100, img);
            pc.setFoto(img.toByteArray());
            d.atualizarCapa(pc);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Capa alterada com sucesso!");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    gerrarIntertissial();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }else {
            //aqui eu salvo
            pc.setTitulo(txtTitulo.getText().toString());
            pc.setRodaPe(txtRodaPé.getText().toString());
            Bitmap bitmap = ((BitmapDrawable)imgCapa.getDrawable()).getBitmap();

            Bitmap redimensionado = Bitmap.createScaledBitmap(bitmap, larguraFoto, alturaFoto, true);
            ByteArrayOutputStream img = new ByteArrayOutputStream();
            redimensionado.compress(Bitmap.CompressFormat.PNG, 100, img);
            pc.setFoto(img.toByteArray());
            d.inserirCapa(pc);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Capa inserida com sucesso!");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    gerrarIntertissial();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }

    }

    public void gerrarIntertissial(){

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();

        } else {
            //Log.d("TAG", "The interstitial wasn't loaded yet.");
            Intent intent = new Intent(CapaPDF.this, MainActivity.class);
            startActivity(intent);

        }

    }

    public void salvarConexao(){

        ProdutoCapa pc = new ProdutoCapa();
        Dao d = new Dao(getApplicationContext());
        pc.setTitulo("Orçamentos Em PDF");
        pc.setRodaPe("");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.images);

        Bitmap redimensionado = Bitmap.createScaledBitmap(bitmap, larguraFoto, alturaFoto, true);
        ByteArrayOutputStream img = new ByteArrayOutputStream();
        redimensionado.compress(Bitmap.CompressFormat.PNG, 100, img);
        pc.setFoto(img.toByteArray());
        d.inserirCapa(pc);
    }
}
