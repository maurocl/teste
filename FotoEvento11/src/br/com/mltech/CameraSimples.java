package br.com.mltech;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import br.com.mltech.modelo.Foto;
import br.com.mltech.modelo.FotoCabine;
import br.com.mltech.modelo.FotoPolaroid;
import br.com.mltech.modelo.Moldura;
import br.com.mltech.utils.FileUtils;
import br.com.mltech.utils.ManipulaImagem;



/**
 * CameraSimples
 * 
 * @author maurocl
 * 
 */
public class CameraSimples extends Activity implements OnClickListener, Constantes {

  // ---------------
  // Constantes
  // ---------------

  private static final String TAG = "CameraSimples";

  private static final int TIRA_FOTO = 100;

  private static final int TIRA_FOTO_POLAROID = 101;
  private static final int TIRA_FOTO_CABINE = 102;

  // ------------------
  // Interface Gr�fica
  // ------------------

  // Defini��o dos bot�es
  private static Button btnConfirma;
  private static Button btnLista;

  // Defini��o do ImageView respons�vel pela visualiza��o da imagem (foto)
  private static ImageView image;

  // ------------------
  // vari�veis da classe
  // ------------------

  private static File file;
  private static Uri outputFileUri;

  // Lista de todas as fotos tiradas
  private static List<Foto> listaFotos;

  // Foto
  private static Foto foto;

  // FotoCabine
  private static FotoCabine fotoCabine;

  // FotoPolaroid
  private static FotoPolaroid fotoPolaroid;

  // Molduras
  private static Moldura molduraPolaroid;
  //private static Moldura molduraCabine;

  // Contador geral (iniciado em onCreate())
  public static int contador = 0;

  public static int i = 0;
  public int j = 0;

  // n� de vezes que o m�todo onCreate() � chamado
  public static int numCreate = 0;

  // n� de vezes que o m�todo onRestart() � chamado
  public static int numRestart = 0;

  //
  public static int numFotosCarregadas = 0;

  //
  public static int numFotosTiradas = 0;

  // n� de fotos tiradas
  private static int contadorCabine = 0;

  // -----------------------------------------------------------------
  // bloco de inicializa��o est�tica
  // -----------------------------------------------------------------
  static {
    // cria uma lista de fotos
    listaFotos = new ArrayList<Foto>();
    fotoCabine = new FotoCabine();
  }

  /**
   * onCreate()
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.camerasimples);

    Log.d(TAG, "*** onCreate() contador=" + contador + " ***");

    if (savedInstanceState != null) {
      // h� estados de inst�ncia gravados. Ele dever�o ser recuperados ...
      Log.w(TAG, "onCreate() - h� estados de inst�ncia gravados. Ele dever�o ser recuperados");
      FileUtils.showBundle(savedInstanceState);
    }

    // obtem informa��es sobre a intent que iniciou essa activity
    Intent intent = getIntent();

    if (intent.getExtras() != null) {
      Log.w(TAG, "onCreate() - A intent que inicou essa activity possui um bundle");
    }

    contador++;
    numCreate++;
    i++;
    j++;

    // Bot�o Confirma
    btnConfirma = (Button) findViewById(R.id.btnConfirma);
    btnConfirma.setOnClickListener(this);

    // Bot�o Lista
    btnLista = (Button) findViewById(R.id.btnLista);
    btnLista.setOnClickListener(this);

    image = (ImageView) findViewById(R.id.imageView1);

    showXXX();

  }

  /**
   * onClick(View v)
   * 
   * Trata os eventos
   * 
   * @param v
   * 
   */
  public void onClick(View v) {

    if (v == btnConfirma) {
      processaBotaoConfirma();
    } else if (v == btnLista) {
      processaBotaoLista();
    }

  }

  /**
   * processaBotaoConfirma()
   * 
   * Processa a a��o disparada pelo pressionamento do bot�o Confirma
   * 
   */
  private void processaBotaoConfirma() {

    Log.d(TAG, "");
    Log.d(TAG, "============================");
    Log.d(TAG, "===> processaBotaoConfirma()");
    Log.d(TAG, "============================");

    int opcao = 3;

    // nome do arquivo onde a foto ser� armazenada
    String arquivo = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg";
    ;

    switch (opcao) {
      case 0:
        // tira uma foto
        executaActivityTiraFoto(arquivo);
        break;
      case 1:
        // simula uma foto
        executaActivityTiraFotoDummy(arquivo);
        break;
      case 2:
        // processa 3 fotos
        processaFotoCabine();
        break;
      case 3:
        executaActivityTiraFotoPolaroid(arquivo);
        break;
    }

  }

  /**
   * processaFotoCabine()
   * 
   * Executa o processo para criar uma foto no formato cabine
   * 
   */
  private void processaFotoCabine() {

    Log.d(TAG, "==> processaFotoCabine()");

    executaActivityTiraFotoCabine();

  }

  /**
   * processaBotaoLista
   */
  private void processaBotaoLista() {

    Log.i(TAG, "");
    Log.i(TAG, "----------------------");
    Log.i(TAG, "- processaBotaoLista()");
    Log.i(TAG, "----------------------");

    if (listaFotos == null) {
      Log.w(TAG, "processaBotaoLista() - lista est� vazia !");
      return;
    }

    Log.d(TAG, "processaBotaoLista() - n� de fotos: " + listaFotos.size());

    for (Foto foto : listaFotos) {
      if (foto != null) {
        Log.d(TAG, foto.toString());
      }
    }

  }

  /**
   * 
   * executaActivityTiraFoto()
   * 
   * Executa a Activity que captura uma foto
   * 
   * @param arquivo nome do arquivo onde a foto ser� armazenada
   * 
   */
  private void executaActivityTiraFoto(String arquivo) {

    file = new File(arquivo);

    outputFileUri = Uri.fromFile(file);
    
    Log.d(TAG, "executaActivityTiraFoto() - arquivo=" + file.getAbsolutePath());

    // cria um intent com a a��o MediaStore.ACTION_IMAGE_CAPTURE
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    // especifica o par�metro com a URI onde a foto deve ser armazenada
    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

    // inicia a Activity com requestCode TIRA_FOTO
    startActivityForResult(intent, TIRA_FOTO);

  }

  /**
   * executaActivityTiraFotoDummy(String arquivo)
   * 
   * Executa a Activity que tira uma foto de "mentirinha", isto �, apenas retorna uma
   * imagem pre-armazenada como se fosse uma foto
   * 
   * @param arquivo nome do arquivo onde a foto ser� armazenada
   */
  private void executaActivityTiraFotoDummy(String arquivo) {

    file = new File(arquivo);

    Log.d(TAG, "executaActivityTiraFotoDummy() - arquivo=" + file.getAbsolutePath());

    outputFileUri = Uri.fromFile(file);
    
    Log.i(TAG, "executaActivityTiraFotoDummy - outputFileUri=" + outputFileUri);

    // cria um intent ActivityCameraSimplesDummy
    Intent intent = new Intent(this, ActivityCameraSimplesDummy.class);

    intent.putExtra("br.com.mltech.outputFileUri", outputFileUri);

    // inicia a Activity com requestCode TIRA_FOTO
    startActivityForResult(intent, TIRA_FOTO);

  }

  /**
   * executaActivityTiraFotoPolaroid(String arquivo)
   * 
   * @param arquivo
   */
  private void executaActivityTiraFotoPolaroid(String arquivo) {

    file = new File(arquivo);

    Log.d(TAG, "executaActivityTiraFotoDummy() - arquivo=" + file.getAbsolutePath());

    outputFileUri = Uri.fromFile(file);

    Intent intent = new Intent(this, ActivityCameraSimplesDummy.class);

    Log.i(TAG, "executaActivityTiraFotoDummy - outputFileUri=" + outputFileUri);

    intent.putExtra("br.com.mltech.outputFileUri", outputFileUri);

    startActivityForResult(intent, TIRA_FOTO_POLAROID);

  }

  /**
   * executaActivityTiraFotoCabine()
   * 
   * 
   * 
   */
  private void executaActivityTiraFotoCabine() {

    //
    // cria um arquivo para armazenar a foto
    //

    // String arquivo = Environment.getExternalStorageDirectory() + "/" +
    // System.currentTimeMillis() + ".jpg";

    String arquivo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/"
        + System.currentTimeMillis() + ".jpg";

    file = new File(arquivo);

    Log.d(TAG, "==> executaActivityTiraFotoCabine(" + contadorCabine + ") - arquivo=" + file.getAbsolutePath());

    // cria uma inst�ncia da classe Foto e armazena o caminho onde a foto tirada
    // deve ser armazenada
    Foto foto = new Foto(file.getAbsolutePath());

    // relaciona a "Foto criada" a primeira foto cabine (dada pelo indice
    // contadorCabine)
    fotoCabine.setFoto(contadorCabine, foto);

    // especifica a Uri onde a foto deve ser armazenada
    outputFileUri = Uri.fromFile(file);

    Intent intent = null;

    int flag = 0; // indica qual Activity ser� executada

    if (flag == 0) {

      // simula��o de "tirar uma foto"
      intent = new Intent(this, ActivityCameraSimplesDummy.class);

      intent.putExtra("br.com.mltech.outputFileUri", outputFileUri);

    } else if (flag == 1) {

      // tira uma foto verdadeira
      intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

      intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

    }

    // inicia a nova Activity relacionada na Intent
    startActivityForResult(intent, TIRA_FOTO_CABINE);

  }

  /**
   * onActivityResult(int requestCode, int resultCode, Intent data)
   * 
   * Processa o resultado da execu��o das Activities
   * 
   * � chamado quando a activcity lan�ada retorna, dando a voc� o requestCode
   * com o qual voc� iniciou, o resultCode retornado e qualquer dado adicional
   * resultado do processamento da activity. O resultCode ser� RESULT_CANCELED
   * se a activity retornar explicitamente esse valor, n�o retornar nenhum valor
   * ou haver algum crash dureante a opera��o.
   * 
   * Esse m�todo ser� chamado imediatamente antes da execu��o do onResume()
   * quando sua activity � reinicializada.
   * 
   * Called when an activity you launched exits, giving you the requestCode you
   * started it with, the resultCode it returned, and any additional data from
   * it. The resultCode will be RESULT_CANCELED if the activity explicitly
   * returned that, didn't return any result, or crashed during its operation.
   * 
   * You will receive this call immediately before onResume() when your activity
   * is re-starting.
   * 
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    Log.d(TAG, "-----------------------------------------------------------------");
    Log.d(TAG, "*** onActivityResult() - requestCode=" + requestCode + ", resultCode=" + resultCode);
    Log.d(TAG, "*** onActivityResult() - data=" + ((data == null) ? "null" : "not null"));
    Log.d(TAG, "-----------------------------------------------------------------");

    numFotosTiradas++;

    switch (requestCode) {
      case TIRA_FOTO:
        processaActivityResultTiraFoto(resultCode, data);
        break;
      case TIRA_FOTO_POLAROID:
        processaActivityResultPolaroid(resultCode, data);
        break;
      case TIRA_FOTO_CABINE:
        processaActivityResultCabine(resultCode, data);
        break;
    }

    Log.d(TAG, ">>> alterando a orienta��o da tela (se necess�rio)");
    atualizaModoTela(Configuration.ORIENTATION_PORTRAIT);

  }

  /**
   * processaActivityResultTiraFoto(int resultCode, Intent data)
   * 
   * @param resultCode
   *          resultado da execu��o da activity
   * 
   * @param data
   *          dados retornados da execu��o da activity
   * 
   */
  private void processaActivityResultTiraFoto(int resultCode, Intent data) {

    if (data == null) {
      Log.w(TAG, "processaActivityResultTiraFoto() - data (Intent) � vazia");
      return;
    }

    if (resultCode == RESULT_CANCELED) {

      // opera��o cancelada
      Log.w(TAG, "processaActivityResultTiraFoto() - Opera��o cancelada pelo usu�rio");
      return;

    }

    else if (resultCode == RESULT_OK) {

      Log.d(TAG, "processaActivityResultTiraFoto() - data.getData()= " + data.getData());

      Log.d(TAG, "processaActivityResultTiraFoto() - extra: " + data.getStringExtra("extra1"));

      Log.d(TAG, "processaActivityResultTiraFoto() - outputFileUri: " + data.getStringExtra("outputFileUri"));

      file = new File(data.getStringExtra("outputFileUri"));

      outputFileUri = Uri.fromFile(file);

      if (file != null) {
        
        Bitmap b = ManipulaImagem.criaBitmap(outputFileUri);

        // cria uma Foto
        foto = new Foto(file.getAbsolutePath(),b);
        
        if (!foto.ler()) {
          Log.w(TAG, "processaActivityResultTiraFoto() - Erro na leitura ...");
        }

        Log.i(TAG, "processaActivityResultTiraFoto() - foto f=" + foto);

      }

      // atualiza a imagem na activity
      image.setImageBitmap(foto.getImagem());

      // exibe a imagem
      carregaImagem();

    } else if (resultCode == RESULT_CANCELED) {

      // opera��o cancelada
      Log.w(TAG, "processaActivityResultTiraFoto() - Opera��o cancelada pelo usu�rio");

    }

  }

  /**
   * processaActivityResultPolaroid(int resultCode, Intent data)
   * 
   * @param resultCode
   * @param data
   * 
   */
  private void processaActivityResultPolaroid(int resultCode, Intent data) {

    if (data == null) {
      Log.w(TAG, "processaActivityResultPolaroid - data (Intent) � vazia");
    }

    if (resultCode == RESULT_CANCELED) {

      // opera��o cancelada
      Log.w(TAG, "processaActivityResultPolaroid() - Opera��o cancelada pelo usu�rio");
      return;

    } else if (resultCode == RESULT_OK) {

      if (data != null) {

        Log.d(TAG, "processaActivityResultPolaroid() - data.getData()= " + data.getData());

        Log.d(TAG, "processaActivityResultPolaroid() - extra: " + data.getStringExtra("extra1"));

        Log.d(TAG, "processaActivityResultPolaroid() - outputFileUri: " + data.getStringExtra("outputFileUri"));

        file = new File(data.getStringExtra("outputFileUri"));

      } else {
        file = null;
      }

      outputFileUri = Uri.fromFile(file);

      if (file != null) {

        // cria uma Foto
        foto = new Foto(file.getAbsolutePath());

        Bitmap abc = ManipulaImagem.criaBitmap(outputFileUri);

        // armazena o bitmap
        foto.setImagem(abc);

        if (!foto.ler()) {
          Log.w(TAG, "processaActivityResultPolaroid() - Erro na leitura ...");
        }

        Log.i(TAG, "processaActivityResultPolaroid() - foto f=" + foto);

      }

      // atualiza a imagem na activity
      image.setImageBitmap(foto.getImagem());

      // exibe a imagem
      carregaImagem();

      // ---------------------

      try {

        molduraPolaroid = new Moldura(PATH_MOLDURAS + "polaroid_340_416_red.png");

        molduraPolaroid.leArquivoMoldura(PATH_MOLDURAS + "polaroid_340_416_red.png");

        fotoPolaroid = new FotoPolaroid(foto, molduraPolaroid);

        // CriaFotoPolaroid
        criaFotoPolaroid();

        fotoPolaroid.formatar();

        Bitmap bit = fotoPolaroid.overlay(fotoPolaroid.getFoto8x8().getImagem(), fotoPolaroid.getMoldura().getImagem());

        foto = new Foto(fotoPolaroid.getFoto().getArquivo());

        if (bit != null) {
          foto.setImagem(bit);

          image.setImageBitmap(bit);

        }

        Log.i(TAG, "processaActivityResultPolaroid() - fotoPolaroid=" + fotoPolaroid.toString());

        Log.i(TAG, "processaActivityResultPolaroid() - foto=" + foto.toString());

      } catch (FileNotFoundException e) {
        Log.w(TAG, "processaActivityResultPolaroid - FileNotFound exception", e);
      } catch (IOException e) {
        Log.w(TAG, "processaActivityResultPolaroid - IO Exception", e);
      }

      Log.w(TAG, "processaActivityResultPolaroid - FIM");

    }

  }

  /**
   * criaFotoPolaroid(Foto foto) throws FileNotFoundException, IOException
   * 
   * @param foto
   * 
   * @throws FileNotFoundException
   * @throws IOException
   * 
   */
  private void criaFotoPolaroid() throws FileNotFoundException, IOException {

    Log.i(TAG, "criaFotoPolaroid() - iniciando ...");

    if (fotoPolaroid != null) {
      // fotoPolaroid.formatar();
      Log.w(TAG, "criaFotoPolaroid() - fotoPolaroid n�o � nula");
    } else {

      Log.w(TAG, "criaFotoPolaroid() - fotoPolaroid � nula");
    }

    // pega a foto e redimensiona para o tamanho 9x12

    // pega a foto e redimensiona para o tamanho 8x8

    // cria uma nova foto
    Foto fotoFinal = null;

    // obt�m o nome de um arquivo
    File file1 = FileUtils.obtemNomeArquivo(".png");

    if (file1 != null) {

      // exibe o caminho do arquivo
      Log.d(TAG, "criaFotoPolaroid()  - file1=" + file1.getAbsolutePath());
    } else {
      Log.w(TAG, "criaFotoPolaroid()  - file1 est� vazio");
    }

    // cria uma foto com o arquivo criado
    fotoFinal = new Foto(file1.getAbsolutePath());

    Bitmap imagem1 = null;

    if ((foto != null) && (foto.getImagem() != null)) {

      // gera um bitmap com a imagem redimensionada
      imagem1 = ManipulaImagem.getScaledBitmap2(foto.getImagem(), 113, 151);

    }

    Log.w(TAG, "criaFotoPolaroid() - imagem1=" + imagem1);

    // Log.i(TAG, "criaFotoPolaroid() - foto=" + fotoPolaroid.toString());

    // atualiza a foto
    fotoFinal.setImagem(imagem1);

    // grava o a foto
    boolean b = fotoFinal.gravar();

    if (b) {
      // foto gravada com sucesso
      Log.w(TAG, "criaFotoPolaroid() - sucesso - b=" + b);
    } else {
      // falha na grava��o da foto
      Log.w(TAG, "criaFotoPolaroid() - falha - b=" + b);
    }

    Log.w(TAG, "criaFotoPolaroid() - fim");

  }

  /**
   * processaActivityResultCabine(int resultCode, Intent data)
   * 
   * Processa o resultado da execu��o da Activity respons�vel por obter uma
   * foto.
   * 
   * @param resultCode
   *          resultado da execu��o da activity
   * 
   * @param data
   *          dados retornados da execu��o da activity
   * 
   */
  private void processaActivityResultCabine(int resultCode, Intent data) {

    if (resultCode == RESULT_OK) {
      // activity executada com sucesso

      if (data != null) {

        // Log.d(TAG, "processaActivityResultCabine() - data n�o � nula");

        // Bundle bundle = data.getExtras();

        // Set<String> xx = bundle.keySet();

        // exibe a lista do nome das chaves
        // Log.d(TAG, xx.toString());

      }

      Log.i(TAG, "processaActivityResultCabine() - processando o recebimento da foto - contadorCabine=" + contadorCabine);

      // incrementa o contador de fotos para compor uma foto no formato cabine
      contadorCabine++;

      if (contadorCabine < 3) {

        // obtem uma foto foto
        executaActivityTiraFotoCabine();

      } else {

        // tr�s foto j� foram obtidas (tiradas)
        fimExecucaoActivityTiraFotoCabine();

      }

    } else if (resultCode == RESULT_CANCELED) {

      // opera��o cancelada
      Log.w(TAG, "processaActivityResultCabine() - Opera��o cancelada pelo usu�rio na foto: " + contadorCabine);
      // TODO aqui deveremos "cancelar" a fotoCabine fazendo-a null (entre
      // outras coisas)

    } else {
      Log.w(TAG, "processaActivityResultCabine() - Opera��o n�o suportada pelo usu�rio");
    }

  }

  /**
   * fimExecucaoActivityTiraFotoCabine()
   * 
   * Ap�s tirar as tr�s fotos � necess�rio: - converter as foto para o formato
   * 3x4 - montar a foto formato cabine juntando as fotos 3x4 e a moldura
   * 
   * fotoCabine.getFoto[0] --> Foto fotoCabine.getFoto[1] --> Foto
   * fotoCabine.getFoto[2] --> Foto
   * 
   */
  private void fimExecucaoActivityTiraFotoCabine() {

    Log.d(TAG, "fimExecucaoActivityTiraFotoCabine()");

    if (fotoCabine == null) {
      Log.w(TAG, "fimExecucaoActivityTiraFotoCabine() - fotoCabine � nula");
      return;
    }

    // exibe infoma��es sobre a foto formato cabine
    showFotoCabine();

    //
    Log.d(TAG, fotoCabine.toString());

    // nesse ponto todas as foto j� possuem seu bitmap

    Log.d(TAG, "fimExecucaoActivityTiraFotoCabine() - agora � montar a foto cabine ...");

    //
    try {

      montaFotoCabine();

    } catch (FileNotFoundException e) {

      Log.w(TAG, "fimExecucaoActivityTiraFotoCabine() - FileNotFound exception", e);

    } catch (IOException e) {

      Log.w(TAG, "fimExecucaoActivityTiraFotoCabine() - IOException", e);

    }

  }

  /**
   * showFotoCabine()
   */
  public void showFotoCabine() {

    // contador local de fotos
    int i = 0;

    if (fotoCabine == null) {
      Log.w(TAG, "showFotoCabine() - fotoCabine � nula");
      return;
    }

    //
    // percorre as fotos
    //
    for (Foto foto : fotoCabine.getFotos()) {

      // exibe informa��es sobre a foto
      Log.i(TAG, "showFotoCabine() - foto[" + i + "] = " + foto.toString());

      // decodifica o bitmap referente ao arquivo com a foto
      Bitmap bm = BitmapFactory.decodeFile(foto.getArquivo());

      if (bm == null) {
        Log.d(TAG, "showFotoCabine() - n�o foi poss�vel decodificar a foto a partir do arquivo " + foto.getArquivo()
            + " - bitmap nulo !!!");
      }

      // armazena o bitmap (imagem) na foto
      foto.setImagem(bm);

      i++;

    }

  }

  /**
   * montaFotoCabine()
   * 
   * cria uma foto com moldura cabine a partir de tr�s fotos 3x4 e uma moldura
   * 
   * @return uma inst�ncia da classe Foto
   * 
   * @throws IOException
   * 
   * @throws FileNotFoundException
   * 
   */
  public Foto montaFotoCabine() throws FileNotFoundException, IOException {

    if (fotoCabine == null) {
      // foto n�o existe
      Log.w(TAG, "montaFotoCabine() - fotoCabine est� vazia");
      return null;
    }

    // cria um array contendo refer�ncias a tr�s inst�ncias de Foto
    Foto[] fotos3x4 = new Foto[3];

    // preenche o array com as fotos armazenadas em fotoCabine
    Foto[] fotos = fotoCabine.getFotos();

    int i = 0;

    // processa cada uma das fotos e faz o redimensionamento para o tamanho 3x4
    for (Foto foto : fotos) {

      // transforma cada foto em 3x4
      Log.i(TAG, "montaFotoCabine() - foto[" + i + "] = " + foto.toString());

      // obt�m o nome de um arquivo
      File file1 = FileUtils.obtemNomeArquivo(".png");

      if (file1 != null) {
        // exibe o caminho do arquivo
        Log.d(TAG, "  montaFotoCabine(" + i + ") - " + file1.getAbsolutePath());
      } else {
        Log.w(TAG, "  montaFotoCabine() - file1 est� vazio");
      }

      // cria uma foto com o arquivo criado
      fotos3x4[i] = new Foto(file1.getAbsolutePath());

      // gera um bitmap com a imagem redimensionada em 3x4
      Bitmap imagem1 = ManipulaImagem.getScaledBitmap2(fotos[i].getImagem(), 113, 151);

      // atualiza a foto
      fotos3x4[i].setImagem(imagem1);

      // grava o a foto
      boolean b = fotos3x4[i].gravar();

      if (b) {
        Log.d(TAG, "  montaFotoCabine() - arquivo " + fotos3x4[i].getArquivo() + " gravado com sucesso");
      } else {
        Log.w(TAG, "  montaFotoCabine() - falha na grava��o do arquivo: " + fotos3x4[i].getArquivo());
      }

      i++;

    }

    // onde armazenar essas fotos ???

    String arquivoMoldura = PATH_MOLDURAS + "cabine_132_568_red.png";

    Log.i(TAG, "arquivoMoldura = " + arquivoMoldura);

    // obtem a moldura da Foto Cabine
    Moldura moldura = fotoCabine.getMoldura();

    if (moldura == null) {
      Log.w(TAG, "montaFotoCabine() - moldura nula");
      moldura = new Moldura(arquivoMoldura);

    }

    // atualiza o nome do arquivo
    moldura.setArquivo(arquivoMoldura);

    // l� o arquivo contendo a moldura
    moldura.leArquivoMoldura(arquivoMoldura);

    // obtem a imagem (bitmap) da moldura
    Bitmap bmMoldura = moldura.getImagem();

    // Sobrep�e a moldura as tr�s foto 3x4

    // array contendo as tr�s fotos
    Bitmap[] bitmapFotos = new Bitmap[3];

    for (int j = 0; j < 3; j++) {

      bitmapFotos[j] = fotos3x4[j].getImagem();
      Log.d(TAG, "montaFotoCabine() - j=" + j);

    }

    // gera um arquivo com as tr�s foto molduradas
    String nomeDoArquivo = ManipulaImagem.processaFotoFormatoCabine2(bitmapFotos[0], bitmapFotos[1], bitmapFotos[2], bmMoldura);

    // cria a foto final
    Foto fotoFinal = new Foto(nomeDoArquivo);
    fotoFinal.ler();

    // atualiza a imagem na activity
    image.setImageBitmap(fotoFinal.getImagem());

    Log.i(TAG, "montaFotoCabine() - fotoFinal: " + fotoFinal.toString());

    return foto;

  }

  /**
   * carregaImagem()
   * 
   * Carrega uma imagem a partir de um arquivo
   * 
   * Decodifica a imagem armazenada no arquivo e tenta criar um bitmap que ser�
   * redimencionado e exibido.
   * 
   * Cria-se uma foto e acrescenta-a a lista de fotos tiradas.
   * 
   */
  private void carregaImagem() {

    Log.d(TAG, "carregaImagem()");

    if (file == null) {
      //
      Log.w(TAG, "carregaImagem() ==> o arquivo contendo a imagem � nulo");
      return;
    }

    // exibe o bitmap
    image.setImageBitmap(foto.getImagem());

    // atualiza o n� de fotos carregadas
    numFotosCarregadas++;

    // adiciona a nova foto a lista de fotos
    listaFotos.add(foto);

    Log.i(TAG, " ");
    Log.i(TAG, "carregaImagem() ===> numFotosTiradas: " + numFotosTiradas + ", numFotosCarregadas: " + numFotosCarregadas);
    Log.i(TAG, " ");

  }

  /**
   * 
   */
  @Override
  protected void onStart() {

    super.onStart();
    Log.d(TAG, "*** onStart() ***");
    showXXX();

  }

  /**
   * 
   */
  @Override
  protected void onResume() {

    super.onResume();
    Log.d(TAG, "*** onResume() ***");
    showXXX();
  }

  /**
   * 
   */
  @Override
  protected void onPause() {

    super.onPause();
    Log.d(TAG, "*** onPause() ***");
    showXXX();
  }

  /**
   * 
   */
  @Override
  protected void onStop() {

    super.onStop();
    Log.d(TAG, "*** onStop() ***");
    showXXX();
  }

  /**
   * 
   */
  @Override
  protected void onRestart() {

    super.onRestart();
    Log.w(TAG, "*******************");
    Log.w(TAG, "*** onRestart() ***");
    Log.w(TAG, "*******************");
    contador++;
    i++;
    j++;
    numRestart++;
    showXXX();
  }

  /**
   * 
   */
  @Override
  protected void onDestroy() {  
    super.onDestroy();
    Log.d(TAG, "*** onDestroy() ***");
    showXXX();
  }

  /**
   * onRestoreInstanceState(Bundle savedInstanceState)
   * 
   * Recupera o estado das (vari�veis) de inst�ncia gravadas
   * 
   */
  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {

    super.onRestoreInstanceState(savedInstanceState);

    Log.i(TAG, "*** onRestoreInstanceState(j=" + j + ")");

    if (savedInstanceState.containsKey("j")) {
      j = savedInstanceState.getInt("j");
    }

    showXXX();

  }

  /**
   * onSaveInstanceState(Bundle outState)
   * 
   * Sava o estado das (vari�veis) de inst�ncia
   */
  @Override
  protected void onSaveInstanceState(Bundle outState) {

    super.onSaveInstanceState(outState);
    Log.i(TAG, "*** onSaveInstanceState()");

    outState.putInt("j", j);

    showXXX();
  }

  /**
   * showXXX()
   * 
   * Exibe o valor de algumas vari�veis selecionadas
   * 
   */
  private void showXXX() {
    Log.v(TAG, "    showXXX() - file: " + file);
    Log.v(TAG, "    showXXX() - outputFileUri: " + outputFileUri);
    Log.v(TAG, "    showXXX() - Contador: " + contador + ", i=" + i + ", j=" + j);
    Log.v(TAG, "    showXXX() - numCreate: " + numCreate + ", numRestart: " + numRestart);

  }

  /**
   * atualizaModoTela(int novaOrientacao)
   * 
   * Atualiza a orienta��o da tela.
   * 
   * @param novaOrientacao
   *          Portrait ou Landscape
   * 
   */
  private void atualizaModoTela(int novaOrientacao) {

    // obt�m a orienta��o atual da tela
    int orientacaoAtual = this.getResources().getConfiguration().orientation;

    // exibe a orienta��o atual
    Log.d(TAG, "atualizaModoTela() - Orienta��o atual da tela: " + getScreenOrientation(orientacaoAtual));

    // se a nova orienta��o for diferente da orienta��o atual ent�o atualiza a
    // orienta��o
    if (novaOrientacao != orientacaoAtual) {
      this.setRequestedOrientation(novaOrientacao);
      Log.d(TAG, "atualizaModoTela() - atualizando de [" + getScreenOrientation(orientacaoAtual) + "] para ["
          + getScreenOrientation(novaOrientacao) + "]");
    }

  }

  /**
   * getScreenOrientation(int orientacao)
   * 
   * Retorna uma string com representando a orienta��o da tela: Landscape ou
   * Portrait
   * 
   * @param orientacao
   *          Configuration.ORIENTATION_LANDSCAPE ou
   *          Configuration.ORIENTATION_PORTRAIT
   * 
   * @return uma string
   */
  private static String getScreenOrientation(int orientacao) {

    String s = null;

    if (orientacao == Configuration.ORIENTATION_LANDSCAPE) {
      s = "Landscape";
    } else if (orientacao == Configuration.ORIENTATION_PORTRAIT) {
      s = "Portrait";
    } else {
      s = "N�o suportado";
    }

    return s;

  }

}
