package com.example.oramentopdf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Intent.ACTION_CHOOSER;
import static android.content.Intent.ACTION_OPEN_DOCUMENT;
import static android.content.Intent.ACTION_PICK;
import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.EXTRA_STREAM;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Produtos> listaProdutos = new ArrayList<>();
    Context context;
    Boolean longClick;
    int [] posicaoLista;
    boolean  [] selecaoLista;
    Menu menu1;
    boolean selecionarTudo;
    private InterstitialAd mInterstitialAd;
    private AdListener adListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.lista);
        context = this;
        longClick = false;
        selecionarTudo = false;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }


        final Dao dao = new Dao(this);
        atualizar();
        this.posicaoLista = new int[listaProdutos.size()];
        this.selecaoLista = new boolean[listaProdutos.size()];

        // eventos de click

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (longClick == false){
                    Produtos produtos = listaProdutos.get(position);
                    Intent intent = new Intent(MainActivity.this, Cadastro.class);
                    intent.putExtra("Produto", produtos);
                    startActivity(intent);}
                else {
                    posicaoLista [position] = position;
                    selecaoLista [position] = true;
                    view.setBackgroundColor(getResources().getColor(R.color.seleção));
                    view.setSelected(true);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
                longClick = true;
                posicaoLista [position] = position;
                selecaoLista [position] = true;
                final Produtos produtos = listaProdutos.get(position);
                view.setBackgroundColor(getResources().getColor(R.color.seleção));
                view.setSelected(true);
                menu1.findItem(R.id.excluir).setVisible(true);
                menu1.findItem(R.id.pdf).setVisible(true);

                /* AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Deseja excluir o produto "+produtos.getNome());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dao.deletar(produtos);
                        atualizar();


                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();*/
            }


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        adListener = new AdListener();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2398950190237031/1527616191");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1000:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permição concedida", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permição negada", Toast.LENGTH_SHORT).show();

                }
                finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_selecao, menu);
        menu1 = menu;
        menu.findItem(R.id.excluir).setVisible(false);
        menu.findItem(R.id.pdf).setVisible(false);

        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.cad){

            Intent intent = new Intent(MainActivity.this, CapaPDF.class);
            startActivity(intent);
            return true;



        }
        if (id == R.id.excluir){
            if (longClick == false) {
                Toast.makeText(getApplicationContext(), "Selecione Algum item para Excluir!", Toast.LENGTH_SHORT).show();

            }else {
                selecionarTudo = false;
                for (int i = 0; i < posicaoLista.length; i++){
                    if (selecaoLista[i] == true){
                        final Produtos p = listaProdutos.get(posicaoLista[i]);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(false);
                        builder.setMessage("Deseja excluir o produto "+p.getNome());
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final Dao dao = new Dao(getApplication());

                                dao.deletar(p);
                                atualizar();
                                limparArray();
                                menu1.findItem(R.id.excluir).setVisible(false);
                                menu1.findItem(R.id.pdf).setVisible(false);





                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }

                }


            }
        }
        if (id == R.id.selecionarTudo){
            if (listaProdutos.size()>0){
                selecionarTodos();
            }

        }
        if (id == R.id.abrirArquivo){

            selecionarArquivo();


        }
        if (id == R.id.pdf){

            gerrarIntertissial();
            if (longClick == false) {
                Toast.makeText(getApplicationContext(), "Selecione Algum item para incluir no Orçaento!", Toast.LENGTH_SHORT).show();

            }else{

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
                Date date = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                Date dataAtual = cal.getTime();
                String dataCompleta = dateFormat.format(dataAtual);

                Document document = new Document();
                String nomeDoArquivo = "Orçamento_"+dataCompleta;
                String patch = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()+
                        "/"+ nomeDoArquivo+".pdf";
                //Toast.makeText(getApplicationContext(), dataCompleta, Toast.LENGTH_SHORT).show();


                try {

                    PdfWriter.getInstance(document, new FileOutputStream(patch));
                    document.open();

                    int quantidadeDePdf = 0;
                    int identificadorLista [] = new int[listaProdutos.size()];
                    try {
                        List<ProdutoCapa>listaC = new ArrayList<>();
                        Dao d = new Dao(this);
                        listaC = d.buscarCapa();
                        ProdutoCapa pc = new ProdutoCapa();
                         pc = listaC.get(0);
                        //Toast.makeText(getApplicationContext(), ""+pc.getTitulo(), Toast.LENGTH_LONG).show();

                        document.newPage();
                        Font fonteTituloC = new Font(Font.FontFamily.TIMES_ROMAN, 30, Font.BOLD, BaseColor.BLACK);
                        Font fonteDescricaoC = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK);
                        Paragraph paragraph = new Paragraph();
                        paragraph.setSpacingAfter(40);
                        paragraph.setAlignment(Element.ALIGN_CENTER);
                        paragraph.setFont(fonteTituloC);
                        paragraph.add(pc.getTitulo());
                        document.add(paragraph);
                        Image image = Image.getInstance(pc.getFoto());
                        document.add(image);
                        paragraph.clear();
                        paragraph.setSpacingBefore(40);
                        paragraph.setFont(fonteDescricaoC);
                        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
                        paragraph.add(pc.getRodaPe());
                        document.add(paragraph);



                    }catch (Exception e){

                    }

                    for (int i = 0; i < posicaoLista.length; i++){

                        if (selecaoLista[i] == true){
                            identificadorLista[quantidadeDePdf] = posicaoLista[i];
                            quantidadeDePdf += 1;
                        }

                    }

                    for (int dd = 0; dd < quantidadeDePdf; dd++){


                        final Produtos p = listaProdutos.get(identificadorLista[dd]);
                        document.newPage();
                        Paragraph paragraph = new Paragraph();
                        paragraph.setAlignment(Element.ALIGN_CENTER);
                        Font fonteTitulo = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLACK);
                        Font fonteDescricao = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK);
                        paragraph.setFont(fonteTitulo);
                        paragraph.add(p.getNome());
                        paragraph.setSpacingAfter(20);
                        //document.addTitle(p.getNome());
                        document.add(paragraph);

                        Image image= Image.getInstance(p.getFoto());
                        document.add(image);
                        image = Image.getInstance(p.getFoto1());
                        image.setAbsolutePosition(320, 610);
                        document.add(image);
                        image = Image.getInstance(p.getFoto2());
                        image.setAbsolutePosition(320, 411);
                        document.add(image);

                        paragraph.clear();
                        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
                        paragraph.setFont(fonteDescricao);
                        paragraph.setSpacingBefore(20);
                        paragraph.add("Valor a vista: "+p.getValorAv()+"                                                      "+"Valor no Cartão: "+p.getValorCartao());
                        document.add(paragraph);
                        paragraph.clear();
                        paragraph.setSpacingBefore(20);
                        paragraph.add(p.getDescricao());
                        document.add(paragraph);
                        Toast.makeText(getApplicationContext(), p.getValorAv(), Toast.LENGTH_LONG).show();


                    }

                    document.close();
                    menu1.findItem(R.id.excluir).setVisible(false);
                    menu1.findItem(R.id.pdf).setVisible(false);
                    atualizar();
                    limparArray();
                    Toast.makeText(getApplicationContext(), "PDF salvo com sucesso!!", Toast.LENGTH_SHORT).show();

                }
                catch (Exception e){

                    Toast.makeText(getApplicationContext(),"deu erro "+e, Toast.LENGTH_SHORT ).show();

                }
            }

        }
        return super.onOptionsItemSelected(item);   }




    private void atualizar(){
        longClick = false;
        final Dao dao = new Dao(this);
        listaProdutos = dao.buscar();
        AdapterProdutos adapterProdutos = new AdapterProdutos(listaProdutos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration( new DividerItemDecoration(context, LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapterProdutos);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.branco));
        selecionarTudo = false;
    }


    public void selecionarTodos(){
        if (listaProdutos.size()>0) {
            if (selecionarTudo == false) {
                selecionarTudo = true;
                menu1.findItem(R.id.excluir).setVisible(true);
                menu1.findItem(R.id.pdf).setVisible(true);
                longClick = true;
                recyclerView.setBackgroundColor(getResources().getColor(R.color.seleção));

                for (int i = 0; i < listaProdutos.size(); i++) {

                    View view = recyclerView.getChildAt(i);
                    posicaoLista[i] = i;
                    selecaoLista[i] = true;
                    //view.setBackgroundColor(getResources().getColor(R.color.seleção));



                }

            } else if (selecionarTudo == true) {

                selecionarTudo = false;
                atualizar();
                menu1.findItem(R.id.excluir).setVisible(false);
                menu1.findItem(R.id.pdf).setVisible(false);
                limparArray();
            }
        }else if (listaProdutos.size() == 0 || listaProdutos.size() <1){
            atualizar();
            Toast.makeText(getApplicationContext(), "Nenhum Produto na Lista!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        if (longClick == false){
            super.onBackPressed();
        }else {
            longClick = false;
            atualizar();
            menu1.findItem(R.id.excluir).setVisible(false);
            menu1.findItem(R.id.pdf).setVisible(false);
            limparArray();

        }
    }

    public void limparArray(){
        atualizar();
        this.posicaoLista = new int[listaProdutos.size()];
        this.selecaoLista = new boolean[listaProdutos.size()];

    }

    public boolean abrirCadastro(View view){

        //gerrarIntertissial();

        Intent intent = new Intent(MainActivity.this, Cadastro.class);
        startActivity(intent);
        return true;


    }



    public void gerrarIntertissial(){

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();

        } else {
            //Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }

    public void fazerToast(){
        Toast.makeText(getApplicationContext(), "testAd", Toast.LENGTH_LONG).show();
    }

    public void selecionarArquivo() {

        /*Intent intent = new Intent(ACTION_PICK);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Selecionar Orçamento"), 0);*/
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                + "/Documents/");
        intent.setDataAndType(uri, "application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Selecionar Orçamento"), 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri arquivoUri = data.getData();
            Intent intent = new Intent(ACTION_VIEW);
            intent.setDataAndType(arquivoUri, "application/pdf");
            intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(intent);
        }
    }
}
