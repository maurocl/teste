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

  // Foto
  private static Foto foto;

  // FotoCabine
  private static FotoCabine fotoCabine;

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

    int opcao = 1;

    // nome do arquivo onde a foto ser� armazenada
    String arquivo = null;

    switch (opcao) {
      case 0:
        arquivo = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg";
        executaActivityTiraFoto(arquivo);
        break;
      case 1:
        arquivo = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg";
        executaActivityTiraFotoDummy(arquivo);
        break;
      case 2:
        processaFotoCabine();
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
   * executaActivityTiraFoto()
   */
  private void executaActivityTiraFoto(String arquivo) {

    file = new File(arquivo);

    Log.d(TAG, "executaActivityTiraFoto() - arquivo=" + file.getAbsolutePath());

    outputFileUri = Uri.fromFile(file);

    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

    startActivityForResult(intent, TIRA_FOTO);

  }

  /**
   * executaActivityTiraFotoDummy(String arquivo)
   * 
   * @param arquivo
   */
  private void executaActivityTiraFotoDummy(String arquivo) {

    file = new File(arquivo);

    Log.d(TAG, "executaActivityTiraFoto() - arquivo=" + file.getAbsolutePath());

    outputFileUri = Uri.fromFile(file);

    Intent intent = new Intent(this, ActivityCameraSimplesDummy.class);

    Log.i(TAG, "outputFileUri=" + outputFileUri);

    intent.putExtra("br.com.mltech.arquivo", outputFileUri);

    startActivityForResult(intent, TIRA_FOTO);

  }

  /**
   * executaActivityTiraFotoCabine()
   * 
   * @param arquivo
   */
  private void executaActivityTiraFotoCabine() {

    //
    // cria um arquivo para armazenar a foto
    //
    String arquivo = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg";

    file = new File(arquivo);

    Log.d(TAG, "==> executaActivityTiraFotoCabine(" + contadorCabine + ") - arquivo=" + file.getAbsolutePath());

    // cria uma inst�ncia da classe Foto e armazena o caminho onde a foto tirada
    // deve ser armazenada
    Foto foto = new Foto(file.getAbsolutePath());

    // relaciona a "Foto criada" a primeira foto cabine (dada pelo indice
    // contadorCabine)
    fotoCabine.setFoto(contadorCabine, foto);

    outputFileUri = Uri.fromFile(file);

    Intent intent = null;

    int flag = 0; // indica qual Activity ser� executada

    if (flag == 0) {

      intent = new Intent(this, ActivityCameraSimplesDummy.class);

      intent.putExtra("br.com.mltech.arquivo", outputFileUri);

    } else if (flag == 1) {

      intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

      intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

    }

    // inicia a nova Activity
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
   * @param data
   *          dados retornados da execu��o da activity
   * 
   */
  private void processaActivityResultTiraFoto(int resultCode, Intent data) {

    if (resultCode == RESULT_OK) {

      if (data != null) {

        Log.w(TAG, "data.getData()= " + data.getData());
        Log.w(TAG, "extra: " + data.getStringExtra("extra1"));
        Log.w(TAG, "file: " + data.getStringExtra("file"));

        // File ff = new File(data.getStringExtra("file"));
        file = new File(data.getStringExtra("file"));

        outputFileUri = Uri.fromFile(file);

      }

      if (file != null) {

        // cria uma Foto
        foto = new Foto(file.getAbsolutePath());

        if (!foto.ler()) {
          Log.w(TAG, "Erro na leitura ...");
        }

        Log.i(TAG, "foto f=" + foto);

      }

      // atualiza a imagem na activity
      image.setImageBitmap(foto.getImagem());

      // exibe a imagem
      carregaImagem();

    } else if (resultCode == RESULT_CANCELED) {

      // opera��o cancelada
      Log.w(TAG, "onActivityResult() - Opera��o cancelada pelo usu�rio");

    } else {

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

    if (resultCode == RESULT_OK) {

      if (data != null) {

      }

    } else if (resultCode == RESULT_CANCELED) {

      // opera��o cancelada
      Log.w(TAG, "onActivityResult() - Opera��o cancelada pelo usu�rio");

    } else {

    }

  }

  /**
   * processaActivityResultCabine(int resultCode, Intent data)
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

      Log.i(TAG, "processaActivityResultCabine() - contadorCabine=" + contadorCabine);

      // atualiza a foto cabine

      // incrementa o contador
      contadorCabine++;

      if (contadorCabine <= 2) {

        executaActivityTiraFotoCabine();

      } else {

        // tr�s foto j� foram tiradas
        fimExecucaoActivityTiraFotoCabine();

      }

    } else if (resultCode == RESULT_CANCELED) {

      // opera��o cancelada
      Log.w(TAG, "processaActivityResultCabine() - Opera��o cancelada pelo usu�rio na foto: " + contadorCabine);

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
   */
  private void fimExecucaoActivityTiraFotoCabine() {

    Log.d(TAG, "fimExecucaoActivityTiraFotoCabine()");

    // cria um array com as foto de FotoCabine
    Foto[] fotos = fotoCabine.getFotos();

    int i = 0;

    // percorre as fotos do array
    for (Foto f : fotos) {

      Log.i(TAG, "fimExecucaoActivityTiraFotoCabine() - foto[" + i + "] = " + f.toString());

      // decodifica o bitmap referente ao arquivo com a foto
      Bitmap bm = BitmapFactory.decodeFile(f.getArquivo());

      if (bm == null) {
        Log.d(TAG, "fimExecucaoActivityTiraFotoCabine() - bitmap nulo !!!");
      }

      // armazena o bitmap (imagem) na foto
      f.setImagem(bm);

      i++;

    }

    // nesse ponto todas as foto j� possuem seu bitmap
    // TODO por que n�o fazer isso direto em fotoCabine ???

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

    // cria um array contendo refer�ncias a tr�s i Foto
    Foto[] fotos3x4 = new Foto[3];

    // preenche o array com as fotos armazenadas em fotoCabine
    Foto[] fotos = fotoCabine.getFotos();

    int i = 0;

    // processa cada uma das fotos e faz o redimensionamento para o tamanho 3x4
    for (Foto f : fotos) {

      // transforma cada foto em 3x4
      Log.i(TAG, "montaFotoCabine() - foto[" + i + "] = " + f.toString());

      // obt�m o nome de um arquivo
      File file1 = FileUtils.obtemNomeArquivo(".png");

      if (file1 != null) {
        // exibe o caminho do arquivo
        Log.d(TAG, "  montaFotoCabine() - " + file1.getAbsolutePath());
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
      Log.w(TAG, "montaFotoCabine() - j=" + j);

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
    Log.i(TAG, "===> numFotosTiradas: " + numFotosTiradas + ", numFotosCarregadas: " + numFotosCarregadas);
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
    // TODO Auto-generated method stub
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
