package br.com.mltech;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import br.com.mltech.modelo.Contratante;
import br.com.mltech.modelo.Evento;
import br.com.mltech.modelo.Foto;
import br.com.mltech.modelo.FotoCabine;
import br.com.mltech.modelo.FotoPolaroid;
import br.com.mltech.modelo.Moldura;
import br.com.mltech.modelo.Participacao;
import br.com.mltech.modelo.Participante;
import br.com.mltech.utils.FileUtils;
import br.com.mltech.utils.ManipulaImagem;
import br.com.mltech.utils.camera.CameraTools;

/**
 * DummyActivity3
 * 
 * Activity respons�vel pela sequencia de passos para obten��o das informa��es
 * de um participante, tirar uma ou mais fotos e enviar um email.
 * 
 * @author maurocl
 * 
 */
public class DummyActivity3 extends Activity implements Constantes {

  private static final String TAG = "DummyActivity3";

  // indica qual Activity ser� executada (0 = Tira foto normal; 1 = tira foto
  // dummy)
  private static int FLAG = 0;

  private static final int TIRA_FOTO = 200;

  private static final int TIRA_FOTO_POLAROID = 201;

  private static final int TIRA_FOTO_CABINE = 202;

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

  // Defini��o dos atributos da aplica��o
  private static Contratante mContratante;

  private static Evento mEvento;

  private static Participante mParticipante;

  private static Participacao mParticipacao;

  private SharedPreferences mPreferences;

  // Nome do arquivo onde est� armazenado a �ltima foto
  private static String mFilename;

  // Uri da �ltima foto
  private static Uri xUri;

  //
  private static Uri[] fotosCabine = new Uri[3];

  // Estado atual da m�quina de estado da aplica��o
  private static int mEstado = -1;

  // N� de vezes que a activity � criada
  private static int mContador = 0;

  // N� de c�meras do dispositivo
  private static int numCameras = -1;

  // N� da c�mera corrente em uso (se houver)
  private static int mCurrentCamera = -1;

  // Bitmaps contendo as molduras
  private static Bitmap mBitmapMolduraPolaroid;

  private static Bitmap mBitmapMolduraCabine;

  // Molduras
  private static Moldura molduraPolaroid;

  private static Moldura molduraCabine;

  // Contador do n� de fotos cabine
  private static int mNumFotosCabine = 0;

  // Contador geral (iniciado em onCreate())
  public static int contador = 0;

  public static int i = 0;

  // public int j = 0;

  // n� de vezes que o m�todo onCreate() � chamado
  public static int numCreate = 0;

  // n� de vezes que o m�todo onRestart() � chamado
  public static int numRestart = 0;

  // n�mero de fotos carregas
  public static int numFotosCarregadas = 0;

  // n� de fotos efetivamente tiradas
  public static int numFotosTiradas = 0;

  // n� de fotos tiradas
  private static int contadorCabine = 0;

  // ---------------------------------------------
  // �rea de inicializa��o de vari�veis est�ticas
  // ---------------------------------------------
  static {
    // cria uma lista de fotos
    listaFotos = new ArrayList<Foto>();
    fotoCabine = new FotoCabine();

  }

  /**
   * onCreate(Bundle savedInstanceState)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    Log.d(TAG, "*** onCreate() ***");

    setContentView(R.layout.dummy);

    if (savedInstanceState != null) {
      // FileUtils.showBundle(savedInstanceState);
    }

    // Carrega as molduras para fotos Polaroid e Cabine
    carregaMolduras();

    // Obtem o identificado da c�mera que ser� usada para tirar as fotos
    mCurrentCamera = obtemIdentificadorCamera();

    // ---------------------------------------------------------------------

    // incrementa o n� de vezes que a activity foi reiniciada
    mContador++;

    contador++;
    numCreate++;
    i++;

    Button btn0 = (Button) findViewById(R.id.btn0);

    btn0.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        iniciaMaquinaDeEstados();

      }

    });

  }

  /**
   * carregaMolduras()
   * 
   * Inicializa as vari�veis que v�o conter o bitmap das molduras
   * 
   * mBitmapMolduraPolaroid. mBitmapMolduraCabine.
   * 
   * mBitmapMolduraPolaroid. mBitmapMolduraCabine.
   * 
   */
  private void carregaMolduras() {

    mPreferences = getSharedPreferences("preferencias", MODE_PRIVATE);

    if (mPreferences == null) {

      Log.w(TAG, "carregaMolduras() - mPreferences is null. Falha na execu��o do comandos getSharedPreferences()");
      return;

    }

    String arquivoMolduraPolaroid = null;
    String arquivoMolduraCabine = null;

    // o arquivo de configura��o possui a informa��o da localiza��o das molduras

    // Obt�m o arquivo contendo o bitmap da moldura formato polaroid
    arquivoMolduraPolaroid = mPreferences.getString("evento_borda_polaroid", "");

    // Obt�m o arquivo contendo o bitmap da moldura formato cabine
    arquivoMolduraCabine = mPreferences.getString("evento_borda_cabine", "");

    if ((arquivoMolduraPolaroid != null) && (arquivoMolduraPolaroid.equals(""))) {
      Log.d(TAG, "carregaMolduras() - moldura formato Polaroid n�o foi configurada.");
      estadoFinal();
    }

    if ((arquivoMolduraCabine != null) && (arquivoMolduraCabine.equals(""))) {
      Log.d(TAG, "carregaMolduras() - moldura formato Cabine n�o foi configurada.");
      estadoFinal();
    }

    // ----------------------------------------------------------
    // o arquivo contendo a moldura � lido no inicio da activity
    // ----------------------------------------------------------
    Log.d(TAG, "carregaMolduras() - lendo arquivo contendo moldura formato Polaroid");
    mBitmapMolduraPolaroid = leArquivoMoldura(arquivoMolduraPolaroid);
    Log.d(TAG, "carregaMolduras() - mBitmapMolduraPolaroid =" + mBitmapMolduraPolaroid);

    Log.d(TAG, "carregaMolduras() - Lendo arquivo contendo moldura formato Cabine");
    mBitmapMolduraCabine = leArquivoMoldura(arquivoMolduraCabine);
    Log.d(TAG, "carregaMolduras() - mBitmapMolduraCabine = " + mBitmapMolduraCabine);

    molduraPolaroid = new Moldura(arquivoMolduraPolaroid, "Moldura Polaroid 1");
    molduraPolaroid.setImagem(mBitmapMolduraPolaroid);

    molduraCabine = new Moldura(arquivoMolduraCabine, "Moldura Cabine 1");
    molduraCabine.setImagem(mBitmapMolduraCabine);

  }

  /**
   * obtemIdentificadorCamera()
   * 
   * Verifica se existe uma configura��o explicita para o n� da c�mera frontal
   * 
   * @return o n� da c�mera frontal ou 0 caso esse par�metro n�o esteja
   *         cadastrado
   * 
   */
  private int obtemIdentificadorCamera() {

    int num = 0;

    // -----------------------------------------------
    // obtem o n� da c�mera usada para tirar as fotos
    // -----------------------------------------------
    String s = getSharedPreference("pref_email", "preferencias_num_camera_frontal");

    Log.i(TAG, "obtemIdentificadorCamera() - N� da c�mera frontal=" + s);

    if ((s != null) && (!s.equals(""))) {
      num = Integer.valueOf(s);
    } else {
      num = 0;
      Log.w(TAG, "obtemIdentificadorCamera() - N� da c�mera frontal n�o foi definido. Assumindo o valor 0");
    }

    return num;

  }

  /**
   * onActivityResult(int requestCode, int resultCode, Intent data)
   * 
   * Trata o resultado da execu��o das Activities
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
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    Log.i(TAG, "===============================================================================");
    Log.i(TAG, "onActivityResult(request " + requestCode + ", result=" + resultCode + ", data " + data + ") ...");
    Log.i(TAG, "===============================================================================");

    numFotosTiradas++;

    switch (requestCode) {

      case ACTIVITY_PARTICIPANTE:

        resultActivityParticipante(resultCode, data);
        break;

      case ACTIVITY_TIRA_FOTO_3:

        resultActivityTiraFoto3(resultCode, data);
        break;

      case ACTIVITY_CHOOSER:

        resultActivityChooser(resultCode, data);
        break;

      case TIRA_FOTO:

        processaActivityResultTiraFoto(resultCode, data);
        meuMetodo(requestCode);
        break;

      case TIRA_FOTO_POLAROID:

        processaActivityResultPolaroid(resultCode, data);
        meuMetodo(requestCode);
        break;

      case TIRA_FOTO_CABINE:

        processaActivityResultCabine(resultCode, data);
        meuMetodo(requestCode);
        break;

      default:
        Log.w(TAG, "onActivityResult() - Erro ... requestCode: " + requestCode + " n�o pode ser processado");
        break;

    }

    Log.d(TAG, "onActivityResult() - >>> alterando a orienta��o da tela (se necess�rio)");
    atualizaModoTela(Configuration.ORIENTATION_PORTRAIT);

  }

  /**
   * meuMetodo(int requestCode)
   * 
   * @param requestCode
   */
  private void meuMetodo(int requestCode) {

    String msg = null;

    switch (requestCode) {

      case TIRA_FOTO:
        msg = "TIRA_FOTO";
        break;

      case TIRA_FOTO_POLAROID:
        msg = "TIRA_FOTO_POLAROID";
        break;

      case TIRA_FOTO_CABINE:
        msg = "TIRA_FOTO_CABINE";
        break;

      default:
        Log.w(TAG, "meuMetodo() - Erro ... requestCode: " + requestCode + " n�o pode ser processado");
        break;

    }

    showAlert(msg);

    String meuArquivo = "/mnt/sdcard/Pictures/fotoevento/fotos/casa_320x240.png";
    File fff = new File(meuArquivo);

    // enviaEmail(Uri.fromFile(fff));

  }

  /**
   * iniciaProcesso()
   */
  private void iniciaMaquinaDeEstados() {

    boolean sucesso = iniciaVariaveis();

    if (!sucesso) {
      Log.w(TAG, "iniciaProcesso() - N�o foi poss�vel inicializar as vari�veis");
      estadoFinal();
    }

  }

  /**
   * iniciaVariaveis()
   * 
   * Inicia a execu��o da m�quina de estados da activity
   * 
   * @return true se as opera��es iniciais foram executas com sucesso ou false
   *         em caso de erro
   * 
   */
  private boolean iniciaVariaveis() {

    // obtem o n� de c�meras dispon�veis pelo dispositivo onde a aplica��o
    // est� em execu��o
    numCameras = android.hardware.Camera.getNumberOfCameras();

    Log.d(TAG, "iniciaVariaveis() - N�mero de C�meras dispon�veis no hardware: " + numCameras);

    // verifica se a c�mera fotogr�tica est� em opera��o
    if (CameraTools.isCameraWorking(mCurrentCamera)) {

      Log.i(TAG, "iniciaVariaveis() - Camera: [" + mCurrentCamera + "] est� em funcionamento...");

    } else {

      Log.w(TAG, "iniciaVariaveis() - Camera: [" + mCurrentCamera + "] n�o est� em funcionamento");

      Toast.makeText(this, "Camera n�o est� dispon�vel", Toast.LENGTH_SHORT);

      return false;

    }

    // Obtem a Intent que iniciou esta Activity
    Intent i = this.getIntent();

    if (i == null) {
      Log.w(TAG, "iniciaVariaveis() - Erro grav�ssimo !!!");
    }

    // indicador de erro de configura��o
    int erro = 0;

    // Obt�m informa��es sobre o Contratante
    if (i.getSerializableExtra(CONTRATANTE) != null) {
      mContratante = (Contratante) i.getSerializableExtra(CONTRATANTE);
    } else {
      Log.w(TAG, "iniciaVariaveis() - Contratante n�o pode ser nulo.");
      Toast.makeText(this, "Contratante n�o pode ser nulo", Toast.LENGTH_SHORT).show();
      erro = 1;
    }

    // Obtem informa��es sobre o Evento
    if (i.getSerializableExtra(EVENTO) != null) {
      mEvento = (Evento) i.getSerializableExtra(EVENTO);
    } else {
      Log.w(TAG, "iniciaVariaveis() - Evento n�o pode ser nulo.");
      Toast.makeText(this, "Evento n�o pode ser nulo", Toast.LENGTH_SHORT).show();
      erro += 2;
    }

    if (erro > 0) {

      Log.w(TAG, "iniciaVariaveis() - Informa��es insuficientes para execu��o (erro=" + erro + ")");

      showAlert("Verifique a configura��o da aplica��o");

      return false;

    }

    Log.d(TAG, "iniciaVariaveis() - mContratante=" + mContratante);
    Log.d(TAG, "iniciaVariaveis() - mEvento=" + mEvento);

    // Altera o estado atual
    setEstado(0);

    // Inicia a obten��o dos Participantes
    startActivityParticipante();

    return true;

  }

  /**
   * startActivityParticipante()
   * 
   * Inicia a Activity Participante. Passa como par�metro as informa��es sobre o
   * Evento.
   * 
   * Aguarda as informa��es sobre o participante do evento
   * 
   */
  private void startActivityParticipante() {

    // Cria uma nova Intent para chamar a Activity Participante
    Intent intentParticipante = new Intent(this, ParticipanteActivity.class);

    // Inclui o par�metro mEvento (com as informa��es sobre o evento em curso)
    intentParticipante.putExtra(EVENTO, mEvento);

    // Inicia a Activity
    startActivityForResult(intentParticipante, ACTIVITY_PARTICIPANTE);

  }

  /**
   * activityParticipanteResult(int resultCode, Intent data)
   * 
   * @param resultCode
   *          resultado da execu��o da activity Participante
   * @param data
   *          Intent com os resultados (se houverem)
   * 
   */
  private void resultActivityParticipante(int resultCode, Intent data) {

    Log.d(TAG, "resultActivityParticipante() ==> processando resultado da ACTIVITY PARTICIPANTE");

    if (getEstado() < 0) {
      Log.w(TAG, "resultActivityParticipante() - N�o h� informa��es sobre o participante");
      return;
    }

    if (resultCode == RESULT_CANCELED) {

      // opera��o cancelada

      Log.d(TAG, "resultCode=RESULT_CANCELED - Participante cancelou sua participa��o");

      // Limpa as vari�veis
      mParticipante = null;
      mParticipacao = null;

      return;

    } else if (resultCode != RESULT_OK) {

      // o resultado execu��o da activity n�o � conhecido
      Log.w(TAG, "resultActivityParticipante() - resultCode n�o conhecido: " + resultCode);
      return;

    }

    if (data == null) {
      // caso a Intent n�o retorne nada houve algum problema
      Log.w(TAG, "resultActivityParticipante() - A intent n�o retornou nenhuma informa��o");
      return;
    }

    // exibe a lista de valores retornados
    FileUtils.showBundle(data.getExtras());

    // atualiza o participante
    if (data.getSerializableExtra(PARTICIPANTE) != null) {
      mParticipante = (Participante) data.getSerializableExtra(PARTICIPANTE);
    }

    // atualiza a participacao
    if (data.getSerializableExtra(PARTICIPACAO) != null) {
      mParticipacao = (Participacao) data.getSerializableExtra(PARTICIPACAO);
    }

    // Exibe as informa��es sobre o participante e sua participa��o
    Log.d(TAG, "resultActivityParticipante() - mParticipante=" + mParticipante);
    Log.d(TAG, "resultActivityParticipante() - mParticipacao=" + mParticipacao);

    // obs: nesse ponto j� sabemos qual ser� o tipo da foto

    // Atualiza o estado da m�quina de estados
    setEstado(1);

    // Processa o pr�ximo estado
    tirarFotos();

  }

  /**
   * tirarFotos()
   * 
   * Tira as fotos de acordo com a solicita��o do participante
   * 
   */
  void tirarFotos() {

    if (mParticipacao == null) {
      Log.d(TAG, "tirarFotos() - n�o � poss�vel obter as informa��es do participante");
      estadoFinal();
    }

    if (mEvento == null) {
      Log.d(TAG, "tirarFotos() - n�o � poss�vel obter as informa��es sobre o evento");
      estadoFinal();
    }

    // obt�m o tipo da foto (se o formato da foto � Polaroid ou Cabine)
    int tipoFoto = mParticipacao.getTipoFoto();

    Log.i(TAG, "tirarFotos() - tipoFoto: " + tipoFoto);

    if (tipoFoto == TIPO_FOTO_POLAROID) {

      // TODO alterar currentTimeMillis por TimeStamp

      // arquivoFotoComMoldura = formataFotoPolaroid(mBitmapMolduraPolaroid);
      String arquivo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/"
          + System.currentTimeMillis() + ".png";

      Log.i(TAG, "tirarFotos() - tipoFoto: POLAROID");
      executaActivityTiraFotoPolaroid(arquivo);

    } else if (tipoFoto == TIPO_FOTO_CABINE) {

      Log.i(TAG, "tirarFotos() - tipoFoto: CABINE");
      // arquivoFotoComMoldura = formataFotoCabine(pathCabine);
      // arquivoFotoComMoldura = formataFotoCabine2(mBitmapMolduraCabine);
      executaActivityTiraFotoCabine();

    } else {
      Log.w(TAG, "tirarFotos() - tipo de foto: " + tipoFoto + " n�o suportado.");
      estadoFinal();

    }

    //
    // Processa o pr�ximo estado
    //
    // obtemFoto();

  }

  /**
   * obtemFoto()
   * 
   * Executa a Intent ACTION_IMAGE_CAPTURE a fim de obter uma foto da c�mera
   * 
   * A imagem capturada ser� armazenada na vari�vel xUri
   * 
   * As condi��es iniciais para tirar uma foto s�o: - fornecer uma Url apontando
   * para o arquivo onde a foto ser� armazenada.
   * 
   */
  private void obtemFoto() {

    Log.d(TAG, "obtemFoto() ==> Executando obtemFoto()");

    if (getEstado() < 1) {
      Log.d(TAG, "obtemFoto() - n�o h� informa��es sobre o participante. Processo abortado !");
      estadoFinal();
    }

    // Libera os recursos relacionados a c�mera
    xUri = null; // endere�o onde a foto ser� armazenada em caso de sucesso
    mFilename = null; // nome do arquivo (full path) onde a foto est� armazenada

    // TODO poderia ser substituido por isCameraWorking ???

    Log.d(TAG, "obtemFoto() - mCurrentCamera: " + mCurrentCamera);

    // testa para saber se c�mera selecionada est� dispon�vel
    if (CameraTools.isCameraWorking(mCurrentCamera)) {

      Log.i(TAG, "obtemFoto() - inst�ncia da Camera obtida com sucesso");

    } else {

      Log.w(TAG, "obtemFoto() - Erro da obten��o da inst�ncia da Camera " + mCurrentCamera + ". Processo abortado !");

      Toast.makeText(this, "C�mera: " + mCurrentCamera + " n�o est� dispon�vel para uso pela aplica��o", Toast.LENGTH_LONG).show();

      estadoFinal();

    }

    File file = obtemNomeArquivo();

    Log.i(TAG, "obtemFoto() - f.getAbsolutePath()=" + file.getAbsolutePath());

    // atualiza o nome do arquivo onde a foto ser� armazenada
    mFilename = file.getAbsolutePath();

    if (file.canWrite()) {
      Log.d(TAG, "obtemFoto() - arquivo: " + mFilename + " pode ser gravado");
    } else {
      Log.w(TAG, "obtemFoto() - arquivo: " + mFilename + " n�o pode ser gravado");
    }

    // Obtem a URI do arquivo (esse valor ser� fornecido como par�metro da
    // Intent)
    xUri = Uri.fromFile(file); // informa a Uri onde a foto ser� armazenada

    if (xUri == null) {
      Log.w(TAG, "obtemFoto() - xUri=null. Arquivo para armazenamento n�o foi criado.");
      estadoFinal();
    } else {
      Log.i(TAG, "obtemFoto() ===> xUri=" + xUri.getPath() + ", xUri=" + xUri);
    }

    Intent intentTiraFoto = criaIntentFoto();

    // --------------------------------------------------------------
    // Inicia a Activity
    // cria e inicia a Intent ACTION_IMAGE_CAPTURE
    // --------------------------------------------------------------
    startActivityForResult(intentTiraFoto, ACTIVITY_TIRA_FOTO_3);

    // --------------------------------------------------------------

  }

  /**
   * obtemNomeArquivo()
   * 
   * Obtem o nome do arquivo que ser� usado para armazenar a foto
   * 
   * @return uma refer�ncia ao nome do arquivo criado
   * 
   */
  private File obtemNomeArquivo() {

    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

    // cria o arquivo com o nome da foto
    File file = new File(PATH_FOTOS + "/" + timeStamp + ".png");

    return file;

  }

  /**
   * criaIntentFoto()
   * 
   * Cria a intent que ser� usada para tirar uma foto
   * 
   * @return
   */
  private Intent criaIntentFoto() {

    // --------------------------------------------------------------

    // --------------------------------------------------------------
    // cria a Intent ACTION_IMAGE_CAPTURE para obter uma foto
    // --------------------------------------------------------------
    Intent intentTiraFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    // --------------------------------------------------------------
    // Passa como par�metro a Uri onde a foto deve ser gravada
    intentTiraFoto.putExtra(MediaStore.EXTRA_OUTPUT, xUri);
    // --------------------------------------------------------------
    return intentTiraFoto;

  }

  /**
   * resultActivityTiraFoto3(int resultCode, Intent data)
   * 
   * Resultado da execu��o da Activity que recebeu a intent identificada como
   * ACTIVITY_TIRA_FOTO_3
   * 
   * Se a activity executou com sucesso ent�o a foto encontra-se no caminho dado
   * por xUri.getPath(); mFilename possui o nome do arquivo gerado
   * 
   * @param resultCode
   *          Resultado de execu��o da Activity
   * 
   * @param data
   *          intent retornada da execu��o da activity
   * 
   */
  private void resultActivityTiraFoto3(int resultCode, Intent data) {

    Log.d(TAG, "resultActivityTiraFoto3() ==> Executando o resultado do processamento da ACTIVITY_TiraFoto3");
    Log.d(TAG, "resultActivityTiraFoto3   ==> resultCode=" + resultCode);

    // testa o resultado da execu��o da activity ACTIVITY_TiraFoto3
    if (resultCode == RESULT_OK) {

      // testa as confi��es necess�ria para continuar processando a foto
      testaCondicoesFotos(data);

      // activity executada com sucesso
      xxxx(data);

      // atualiza o modo da tela para portrait
      atualizaModoTela(Configuration.ORIENTATION_PORTRAIT);

      // grava foto redimensionada
      gravaFotoOriginal();

      String fotoComMoldura = null;

      switch (mParticipacao.getTipoFoto()) {
        case TIPO_FOTO_POLAROID:
          fotoComMoldura = processaFotoPolaroid();
          break;
        case TIPO_FOTO_CABINE:
          fotoComMoldura = processaFotoCabine();
          break;
        default:
          break;
      }

    } else {
      // Foto foi cancelada ou houve algum outro tipo de problema
      // resultCode != RESULT_OK

      xUri = null;
      mFilename = null;

      // opera��o cancelada - indica que nenhuma foto foi tirada.
      Log.w(TAG, "resultActivityTiraFoto3() - Opera��o tirar foto cancelada pelo usu�rio");

      estadoFinal();

    }

  }

  /**
   * xxxx(Intent data)
   * 
   * @param data
   */
  private void xxxx(Intent data) {

    // activity executada com sucesso

    // a foto encontra-se no caminho dado por xUri.getPath();
    // uma foto foi tirada e encontra-se no endere�o dado pela URI xUri
    // exibe informa��es sobre a localiza��o da foto armazenada no
    // sistema
    Log.i(TAG, "resultActivityTiraFoto3() - xUri: " + xUri);
    Log.i(TAG, "resultActivityTiraFoto3() - mFilename: " + mFilename);
    Log.i(TAG, "resultActivityTiraFoto3() - Foto tirada e armazenada em xUri.getPath()=" + xUri.getPath());

    // atualiza o caminho onde a foto foi armazenada.
    // getPath(): Gets the decoded path. (obtem o caminho decodificado)
    // the decoded path, or null if this is not a hierarchical URI
    // (like "mailto:nobody@google.com") or the URI is invalid
    // armazena o caminho onde se encontra a foto original

    // --------------------------------------------------
    // TODO alterar o caminho de onde se encontra a foto
    // --------------------------------------------------
    mParticipacao.setNomeArqFoto(xUri.getPath());

    Log.d(TAG, "resultActivityTiraFoto3() - mParticipacao=" + mParticipacao);

  }

  /**
   * processaFotoPolaroid()
   * 
   * @return
   */
  private String processaFotoPolaroid() {

    String fotoComMoldura = null;

    // atualiza a m�quina de estado
    setEstado(2);

    // Executa o pr�ximo passo da m�quina de estado
    fotoComMoldura = processaFotos();

    return fotoComMoldura;

  }

  /**
   * processaFotoCabine()
   * 
   * @return
   */
  private String processaFotoCabine() {

    String fotoComMoldura = null;

    // Sistema de foto cabine selecionado
    if (mNumFotosCabine < 3) {

      // tirar mais fotos
      fotosCabine[mNumFotosCabine] = xUri;

      Log.i(TAG, "***** mNumFotosCabine=" + mNumFotosCabine);

      mNumFotosCabine++;

      obtemFoto();

    } else {
      // reseta o contador
      mNumFotosCabine = 0;
      processaFotoPolaroid();

    }

    return fotoComMoldura;

  }

  /**
   * testaCondicoesFotos(Intent data)
   * 
   * @param data
   *          Intent recebida da Activity processada
   * 
   */
  private void testaCondicoesFotos(Intent data) {

    if (xUri == null) {
      Log.w(TAG, "testaCondicoesFotos() - xUri � nulo ==> nenhuma foto foi obtida");
      estadoFinal();
    }

    if (data != null) {
      FileUtils.showBundle(data.getExtras());
    }

    if (mParticipacao == null) {
      Log.w(TAG, "testaCondicoesFotos() - mParticipa��o � nulo");
      estadoFinal();
    }

  }

  /**
   * gravaFotoRedimensionada()
   * 
   * Grava foto redimesionada
   * 
   */
  private void gravaFotoOriginal() {

    // grava a foto original em um arquivo
    boolean gravou = ManipulaImagem.gravaBitmapArquivo3(xUri);

    if (gravou) {

      Log.i(TAG, "gravaFotoOriginal() - arquivo: " + xUri + " gravado com sucesso.");

    } else {

      Log.w(TAG, "gravaFotoOriginal() - falha na grava��o do arquivo: " + xUri);

      xUri = null;
      mFilename = null;
      estadoFinal();

    }

  }

  /**
   * processaFotos()
   * 
   * Respons�vel pelo processamento da foto, isto �, transformar a foto de
   * acordo com o formato e tipo solicitado.
   * 
   */
  private String processaFotos() {

    Log.d(TAG, "===========================");
    Log.d(TAG, "====> processaFotos() <====");
    Log.d(TAG, "===========================");

    int tipoFoto = -1;
    int efeitoFoto = -1;

    if (xUri == null) {
      // URI da foto n�o est� dispon�vel
      Log.d(TAG, "processaFotos() - n�o h� nenhuma foto na Uri fornecida");
      estadoFinal();
    }

    if (mParticipacao == null) {
      //
      Log.d(TAG, "processaFotos() - n�o � poss�vel obter as informa��es do participante");
      estadoFinal();
    }

    if (mEvento == null) {
      Log.d(TAG, "processaFotos() - n�o � poss�vel obter as informa��es sobre o evento");
      estadoFinal();
    }

    // exibe informa��es sobre a Uri de localiza��o da foto
    ManipulaImagem.showUri(xUri);

    // obt�m o tipo da foto (se o formato da foto � Polaroid ou Cabine)
    tipoFoto = mParticipacao.getTipoFoto();

    // obtem o efeito para aplica��o na foto (se a foto deve ser a cores ou
    // P&B)
    efeitoFoto = mParticipacao.getEfeitoFoto();

    String pathCabine = mEvento.getBordaCabine();

    // --------

    /**
     * Processa a foto com o filtro desejado
     */
    // TODO ser� que esse processo n�o deveria ser feito depois ???
    Bitmap bb = processoEfeitoFiltroFoto(efeitoFoto, null);

    // --------

    // nome do arquivo contendo a foto formatada como Polaroid ou Cabine j�
    // com a moldura aplicada
    String arquivoFotoComMoldura = null;

    arquivoFotoComMoldura = processaTipoFoto(tipoFoto, pathCabine);

    // -------------------------------------------------------------------------

    // TODO aqui poder�amos ter um passo intermedi�rio na m�quina de estados

    // TODO aqui tamb�m � necess�rio atualizar a Uri da foto processada

    // Cria uma Uri para foto com moldura
    Uri fotoProcessadaUri = null;

    if (arquivoFotoComMoldura != null) {

      // foto
      File ff = new File(arquivoFotoComMoldura);

      if (ff != null && ff.exists()) {
        fotoProcessadaUri = Uri.fromFile(ff);
      }

    }

    // Uri lastUri = xUri;

    Uri lastUri = null;

    if (fotoProcessadaUri != null) {
      lastUri = fotoProcessadaUri;
    } else {
      lastUri = xUri;
    }

    Log.d(TAG, "processaFotos() - lastUri: " + lastUri.toString());

    // O pr�ximo passo � enviar o email com a foto j� trabalhada.
    // Envia email com a foto pronta
    enviaEmail(lastUri);

    return arquivoFotoComMoldura;

  }

  /**
   * processaTipoFoto(int tipoFoto, String pathCabine)
   * 
   * @param tipoFoto
   *          Tipo da foto
   * @param pathCabine
   * 
   * @return Uma string contendo o path absoluto para foto ou uma string vazia
   *         no caso de erro
   * 
   */
  private String processaTipoFoto(int tipoFoto, String pathCabine) {

    String arquivoFotoComMoldura = null;

    if (tipoFoto == TIPO_FOTO_POLAROID) {

      arquivoFotoComMoldura = formataFotoPolaroid(mBitmapMolduraPolaroid);

    } else if (tipoFoto == TIPO_FOTO_CABINE) {

      // arquivoFotoComMoldura = formataFotoCabine(pathCabine);
      arquivoFotoComMoldura = formataFotoCabine2(mBitmapMolduraCabine);

    } else {
      Log.w(TAG, "processaFotos() - tipo de foto: " + tipoFoto + " n�o suportado.");
      estadoFinal();

    }

    return arquivoFotoComMoldura;

  }

  /**
   * processoEfeitoFiltroFoto(int efeitoFoto)
   * 
   * @param efeitoFoto
   */
  private Bitmap processoEfeitoFiltroFoto(int efeitoFoto, Bitmap bitmap) {

    Bitmap resultado = null;

    if (efeitoFoto == CORES) { // aplica efeito cores COR
      // TODO processa o efeito cores
      resultado = ManipulaImagem.aplicaFiltroCores(bitmap);
    } else if (efeitoFoto == PB) { // aplica efeito P&B
      // TODO processa o efeito P&B, isto �, aplicaca um filtro P&B � foto
      resultado = ManipulaImagem.aplicaFiltroPB(bitmap);
    } else {
      Log.w(TAG, "Efeito: " + efeitoFoto + " n�o � suportado pela aplica��o");
    }

    return resultado;

  }

  /**
   * formataFotoPolaroid(Bitmap bmMoldura)
   * 
   * A foto formato Polaroid exige o redimensionamento da foto bem como a
   * inclus�o da moldura.
   * 
   * Observe que a moldura est� relacionada ao evento em andamento. Portanto �
   * necess�rio ter informa��es sobre o evento
   * 
   * Pega a foto, redimensiona-a e aplica a moldura criando um novo bitmap.
   * 
   * @param bmMoldura
   *          Bitmap contendo a moldura da foto
   * 
   * @return o nome do arquivo onde a foto pronta est� gravado
   * 
   */
  private String formataFotoPolaroid(Bitmap bmMoldura) {

    Log.i(TAG, "formataFotoPolaroid() - Foto tipo POLAROID foi selecionada");

    if (bmMoldura == null) {
      Log.w(TAG, "formataFotoPolaroid() - bmMoldura � nula");
      return null;
    }

    // Cria um bitmap a partir da Uri da foto
    Bitmap foto = ManipulaImagem.criaBitmap(xUri);

    if (foto == null) {
      Log.w(TAG, "formataFotoPolaroid() - foto � nula");
      return null;
    }

    // Exibe informa��es a respeito da foto
    ManipulaImagem.showBitmapInfo(foto);

    // Redimensiona a foto para o formato Polaroid
    // Bitmap fotoRedimensionada = imagem.getScaledBitmap2(foto, 228, 302);

    // --------------------------------------------------------------------------
    // redimensiona a foto original para 9x12 para manter a propor��o 3:4
    // --------------------------------------------------------------------------

    Bitmap bmFoto9x12 = ManipulaImagem.getScaledBitmap2(foto, 340, 454);

    String arqSaida = null;

    // Define o nome da foto redimensionada
    arqSaida = PATH_FOTOS + FileUtils.getFilename(xUri) + "_9x12.png";

    // grava a foto redimensionada (foto9x12) em um arquivo
    boolean gravou = ManipulaImagem.gravaBitmapArquivo(bmFoto9x12, arqSaida);

    if (gravou) {
      // foto armazenada com sucesso
      Log.i(TAG, "formataFotoPolaroid() - (Foto9x12): " + arqSaida + " gravado com sucesso.");
    } else {
      // falha na grava��o da foto
      Log.i(TAG, "formataFotoPolaroid() - (Foto9x12) - falha na grava��o do arquivo: " + arqSaida);
    }

    // --------------------------------------------------------------------------
    // redimensiona a foto 9x12 para 8x8, isto �, copia uma "janela" 8x8 da
    // foto
    // --------------------------------------------------------------------------

    Options options = new Options();

    Rect rect = new Rect(0, 0, 302, 302);

    // Obtem um bitmap com a foto redimensionada para 8x8
    Bitmap bmFoto8x8 = ManipulaImagem.getBitmapRegion(arqSaida, rect, options);

    arqSaida = PATH_FOTOS + FileUtils.getFilename(xUri) + "_8x8.png";

    // grava a foto redimensionada em um arquivo
    boolean gravou2 = ManipulaImagem.gravaBitmapArquivo(bmFoto8x8, arqSaida);

    if (gravou2) {
      // foto armazenada com sucesso
      Log.i(TAG, "formataFotoPolaroid() - (Foto8x8):" + arqSaida + " gravado com sucesso.");
    } else {
      // falha na grava��o da foto
      Log.i(TAG, "formataFotoPolaroid() - (Foto8x8) - falha na grava��o do arquivo: " + arqSaida);
    }

    // --------------------------------------------------------------------------

    arqSaida = gravaFotoPolaroidComMoldura(bmFoto8x8, bmMoldura);

    // o nome completo do arquivo onde a foto com moldura foi armazenada
    return arqSaida;

  }

  /**
   * gravaFotoPolaroidComMoldura(Bitmap bmFoto, Bitmap bmMoldura)
   * 
   * @param bmFoto
   *          Foto
   * @param bmMoldura
   *          Moldura
   * 
   * @return o nome do arquivo onde a foto foi gravada ou null em caso de erro.
   */
  String gravaFotoPolaroidComMoldura(Bitmap bmFoto, Bitmap bmMoldura) {

    Bitmap fotoComMoldura = null;

    if (bmFoto == null) {
      Log.w(TAG, "gravaFotoPolaroidComMoldura() - bmFoto is null");
    }

    if (bmMoldura == null) {
      Log.w(TAG, "gravaFotoPolaroidComMoldura() - bmMoldura is null");
    }

    if ((bmFoto != null) && (bmMoldura != null)) {

      fotoComMoldura = ManipulaImagem.overlay4(bmFoto, bmMoldura);

      if (fotoComMoldura == null) {
        Log.w(TAG, "gravaFotoPolaroidComMoldura() - erro na convers�o da foto");
      }

    } else {
      Log.w(TAG, "gravaFotoPolaroidComMoldura() - ERRO");
    }

    String arqSaida = PATH_FOTOS + FileUtils.getFilename(xUri) + ".jpg";

    // grava a foto das imagens "juntada"
    boolean gravou = ManipulaImagem.gravaBitmapArquivo2(fotoComMoldura, arqSaida);

    if (gravou) {

      // foto armazenada com sucesso
      Toast.makeText(this, "Foto gravada no arquivo: " + arqSaida, Toast.LENGTH_SHORT).show();
      Log.d(TAG, "gravaFotoPolaroidComMoldura() - Foto gravada no arquivo: " + arqSaida);

    } else {

      // falha na grava��o da foto
      Toast.makeText(this, "Falha na grava��o da foto no arquivo: " + arqSaida, Toast.LENGTH_SHORT).show();
      Log.d(TAG, "gravaFotoPolaroidComMoldura() - Falha na grava��o da foto no arquivo: " + arqSaida);

    }

    return arqSaida;

  }

  /**
   * formataFotoCabine(String pathCabine)
   * 
   * foto formato Cabibe exige tr�s fotos. as fotos ser�o montadas em sequencoa
   * e ser� aplicada. uma moldura (conforme configura��o). observe que a moldura
   * est� relacionada ao evento em andamento. portanto � necess�rio ter
   * informa��es sobre o evento.
   * 
   * @param pathCabine
   */
  private String formataFotoCabine(String pathCabine) {

    if (pathCabine == null) {
      Log.d(TAG, "formataFotoCabine() - N�o h� moldura definida para foto CABINE");
    }

    // TODO verificar nesse ponto se h� tr�s fotos dispon�vel

    // TODO veja que aqui s�o necess�rias 3 fotos
    // as fotos podem ser armazenadas em um array de fotos
    Log.d(TAG, "processaFotos() - Foto tipo CABINE foi selecionada");

    String foto1 = PATH_FOTOS + "img-3x4-blue.png";
    String foto2 = PATH_FOTOS + "img-3x4-green.png";
    String foto3 = PATH_FOTOS + "img-3x4-yellow.png";

    String moldura = "moldura-cabine-132x568-red.png";

    String nomeDoArquivo = ManipulaImagem.processaFotoFormatoCabine(foto1, foto2, foto3, moldura);

    return nomeDoArquivo;

  }

  /**
   * formataFotoCabine2(Bitmap bmMoldura)
   * 
   * foto formato Cabibe exige tr�s fotos. as fotos ser�o montadas em sequencoa
   * e ser� aplicada. uma moldura (conforme configura��o). observe que a moldura
   * est� relacionada ao evento em andamento. portanto � necess�rio ter
   * informa��es sobre o evento.
   * 
   * @param pathCabine
   */
  private String formataFotoCabine2(Bitmap bmMoldura) {

    Log.d(TAG, "processaFotos() - Foto tipo CABINE foi selecionada");

    if (bmMoldura == null) {
      Log.d(TAG, "formataFotoCabine2() - N�o h� moldura definida para foto CABINE");
    }

    // TODO verificar nesse ponto se h� tr�s fotos dispon�vel

    // TODO veja que aqui s�o necess�rias 3 fotos
    // as fotos podem ser armazenadas em um array de fotos

    // Cria um bitmap a partir da Uri da foto
    Bitmap foto = ManipulaImagem.criaBitmap(xUri);

    // Exibe informa��es a respeito da foto
    ManipulaImagem.showBitmapInfo(foto);

    Bitmap foto1 = formataFoto3x4(foto, "foto1.png");
    Bitmap foto2 = formataFoto3x4(foto, "foto2.png");
    Bitmap foto3 = formataFoto3x4(foto, "foto3.png");

    String nomeDoArquivo = ManipulaImagem.processaFotoFormatoCabine2(foto1, foto2, foto3, bmMoldura);

    return nomeDoArquivo;

  }

  /**
   * formataFoto3x4(Bitmap foto, String nome)
   * 
   * Redimensiona uma foto para o tamanho 3x4 (observe que nesse caso 3 � a
   * largura e 4 � a altura)
   * 
   * @param foto
   *          bitmap com a foto
   * 
   * @param nome
   *          nome do arquivo onde a foto ser� gravada
   * 
   * @return o bitmap com a foto ou null em caso de erro
   * 
   */
  Bitmap formataFoto3x4(Bitmap foto, String nome) {

    // redimensiona a foto foto para 3x4
    Bitmap foto3x4 = ManipulaImagem.getScaledBitmap2(foto, 113, 151);

    if (foto3x4 == null) {
      Log.w(TAG, "formataFoto3x4() - erro no redimensionamento da foto");
      return null;
    }

    // Define o nome da foto redimensionada
    String nomeArquivo = PATH_FOTOS + nome;

    // grava a foto redimensionada em um arquivo
    boolean gravou = ManipulaImagem.gravaBitmapArquivo(foto3x4, nomeArquivo);

    if (gravou) {
      // foto armazenada com sucesso
      return foto3x4;

    } else {
      // falha na grava��o da foto
      Log.w(TAG, "formataFoto3x4() - falha na grava��o da foto");
      return null;

    }

  }

  /**
   * xxxabc(Bitmap bmMoldura)
   * 
   * @param bmMoldura
   * 
   * @return
   * 
   */
  private String xxxabc(Bitmap bmMoldura) {

    // Cria um bitmap a partir da Uri da foto
    Bitmap foto = ManipulaImagem.criaBitmap(xUri);

    // Exibe informa��es a respeito da foto
    ManipulaImagem.showBitmapInfo(foto);

    // primeiro, scala a foto para 9x12 cm para manter a propor��o
    Bitmap foto9x12 = ManipulaImagem.getScaledBitmap2(foto, 340, 454);

    // Define o nome da foto redimensionada
    String nomeArquivo = PATH_FOTOS + "polaroid-escalada.png";

    // grava a foto redimensionada em um arquivo
    boolean gravou = ManipulaImagem.gravaBitmapArquivo(foto9x12, nomeArquivo);

    // redimensiona a foto 9x12 para 8x8, isto �, copia uma "janela" 8x8 da
    // foto

    Options options = new Options();

    Rect rect = new Rect(0, 0, 302, 302);

    //
    foto9x12 = ManipulaImagem.getBitmapRegion(nomeArquivo, rect, options);

    if (gravou) {
      // foto armazenada com sucesso
    } else {
      // falha na grava��o da foto
    }

    Bitmap fotoComMoldura = null;

    if ((foto9x12 != null) && (bmMoldura != null)) {

      fotoComMoldura = ManipulaImagem.overlay4(foto9x12, bmMoldura);

      if (fotoComMoldura == null) {
        Log.w(TAG, "formataFotoPolaroid() - erro na convers�o da foto");
      }

    } else {
      Log.w(TAG, "formataFotoPolaroid() - ERRO");
    }

    nomeArquivo = PATH_FOTOS + "polaroid-escalada-com-moldura.jpg";

    // grava a foto das imagens "juntada"
    gravou = ManipulaImagem.gravaBitmapArquivo2(fotoComMoldura, nomeArquivo);

    if (gravou) {
      // foto armazenada com sucesso
      Toast.makeText(this, "Foto gravada com sucesso", Toast.LENGTH_SHORT).show();
    } else {
      // falha na grava��o da foto
      Toast.makeText(this, "Falha na grava��o da foto", Toast.LENGTH_SHORT).show();
    }

    // o nome completo do arquivo onde a foto com moldura foi armazenada
    return nomeArquivo;

  }

  /**
   * enviaEmail(Uri lastUri)
   * 
   * Inicia o processo de envio de email.
   * 
   * Verifica inicialmente se todas as condi��es necess�rias est�o satisfeita
   * para o envio do email com a foto anexada.
   * 
   * Usa: mParticipante e mContratante
   * 
   * @param lastUri
   *          Uri onde a foto est� armazenada
   * 
   */
  private void enviaEmail(Uri lastUri) {

    boolean erro = false;

    if (getEstado() < 2) {
      Log.d(TAG, "enviaEmail() - Foto n�o foi tirada");
      erro = true;
    }

    if (mParticipante == null) {
      // Log.d(TAG, "mParticipante � null");
      Log.w(TAG, "enviaEmail() - N�o h� informa��es sobre o participante");
      erro = true;
    }

    if (mContratante == null) {
      // Log.d(TAG, "mContrante � null");
      Log.w(TAG, "enviaEmail() - N�o h� informa��es sobre o contratante");
      erro = true;
    }

    if (xUri == null) {
      // Log.d(TAG, "xUri � null");
      Log.w(TAG, "enviaEmail() - n�o h� foto");
      erro = true;
    }

    if (erro) {
      // TODO o que fazer ???
    }

    // carrega as prefer�ncias sobre o envio de email
    SharedPreferences emailPreferences = getSharedPreferences("pref_email", MODE_PRIVATE);

    if (emailPreferences == null) {
      Log.w(TAG, "enviaEmail() - SharedPreferences n�o foi encontrada.");
    }

    /**
     * Assunto do email
     * 
     * Recupera o "subject" do email do arquivo de prefer�ncias
     */
    String subject = emailPreferences.getString("preferencias_assunto", "Evento Inicial");

    /**
     * Corpo do email
     * 
     * Recupera o "corpo" do email do arquivo de prefer�ncias
     * 
     */
    String body = emailPreferences.getString("preferencias_descricao", "Segue as informa��es sobre o evento");

    emailPreferences = null;

    // obt�m o email do participante do evento
    String to = mParticipante.getEmail();

    // obt�m o email do contratante do evento
    // ele ser� copiado em BCC no email enviado
    String cc = mContratante.getEmail();

    // envia o email
    sendEmail(to, cc, subject, body, lastUri);

  }

  /**
   * sendEmail(String emailParticipante, String emailContratante, String
   * subject, String text, Uri imageURI)
   * 
   * @param emailParticipante
   *          Endere�o de email do participante do evento
   * 
   * @param emailContratante
   *          Endere�o de email do contratante do evento
   * 
   * @param subject
   *          String usada como "Subject" do email
   * 
   * @param text
   *          String usada como "Body" do email (o conte�do da mensagem)
   * 
   * @param imageUri
   *          Uri da foto
   * 
   */
  private void sendEmail(String emailParticipante, String emailContratante, String subject, String text, Uri imageUri) {

    Intent emailIntent = new Intent(Intent.ACTION_SEND);

    Log.d(TAG, "sendEmail() ===> sendEmail()");

    // To:
    if (emailParticipante != null) {
      emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { emailParticipante });
    } else {
      // email do participante n�o pode ser vazio
    }

    // Bcc:
    if (emailContratante != null) {
      // email do contratante foi fornecido (BCC:)
      emailIntent.putExtra(Intent.EXTRA_BCC, new String[] { emailContratante });
    } else {
      // email do contratante do evento n�o pode ser vazio
    }

    /**
     * A constant string holding the desired subject line of a message. Constant
     * Value: "android.intent.extra.SUBJECT"
     */
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

    /**
     * A constant CharSequence that is associated with the Intent, used with
     * ACTION_SEND to supply the literal data to be sent. Note that this may be
     * a styled CharSequence, so you must use Bundle.getCharSequence() to
     * retrieve it. Constant Value: "android.intent.extra.TEXT"
     */
    emailIntent.putExtra(Intent.EXTRA_TEXT, text);

    // Anexa a foto ao email
    emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);

    Log.d(TAG, "sendEmail() - Anexando o arquivo: " + imageUri + " aos anexos ...");

    // Define o MIME type do email
    // emailIntent.setType("message/rfc822");
    // emailIntent.setType("image/png");
    emailIntent.setType("image/jpg");

    // TODO verificar se funciona !!!
    startActivityForResult(emailIntent, ACTIVITY_CHOOSER);

    // Envia "email" a redes sociais previamente cadastradas
    sendEmailRedesSociais();

    // sendEmailByChooser(emailIntent);

  }

  /**
   * sendEmailByChooser(Intent emailIntent)
   * 
   * Permite escolher qual aplica��o ser� usada para o envio de email
   * 
   * @param emailIntent
   *          Intent
   * 
   */
  private void sendEmailByChooser(Intent emailIntent) {

    //
    // TODO aqui pode acontecer de ser necess�rio for�ar a aplica��o de
    // email
    //

    // Cria uma intent do tipo chooser a partir da intent recebida
    Intent chooser = Intent.createChooser(emailIntent, "Selecione sua aplica��o de email !");

    if (chooser != null) {

      Log.w(TAG, "sendEmailByChooser() - chooser.getAction()=" + chooser.getAction());

      ComponentName compName = chooser.getComponent();

      if (compName != null) {
        Log.w(TAG, "==> CHOOSER ==> compName=" + compName.getClassName() + ", compName=" + compName.getPackageName());
      } else {
        Log.v(TAG, "compName IS NULL");
      }

    }

    // Inicia a Activity para envio do email
    // TODO aqui � necess�rio fixa o uso do email como mecanismo de envio
    // TODO talvez seja necess�rio permitir o envio via Facebook e Twitter
    // tamb�m

    sendEmailRedesSociais();

    startActivityForResult(chooser, ACTIVITY_CHOOSER);

  }

  /**
   * sendEmailRedesSociais()
   * 
   * Usa mEvento (informa��es sobre o evento).
   * 
   */
  private void sendEmailRedesSociais() {

    if (mEvento == null) {
      Log.d(TAG, "sendEmailRedesSociais() - N�o foi poss�vel obter os dados do evento.");
      return;
    }

    // TODO talvez pudesse ser feito ap�s o envio do email ???

    if (mEvento.isEnviaFacebook()) {

      // enviar foto ao Facebook
      // TODO qual texto ???
      Log.d(TAG, "sendEmailRedesSociais() - Envia foto ao Facebook ...");

    } else if (mEvento.isEnviaTwitter()) {

      // enviar foto ao Twitter
      // TODO qual texto ?
      Log.d(TAG, "sendEmailRedesSociais() - Envia foto ao Twitter ...");

    }

  }

  /**
   * activityChooserResult(int resultCode, Intent data)
   * 
   * Processa o resultado do envio do email (execu��o da activity)
   * 
   * @param resultCode
   *          Resultado a execu��o da activity
   * @param data
   *          Intent retornada
   * 
   */
  private void resultActivityChooser(int resultCode, Intent data) {

    Log.d(TAG, "resultActivityChooser() ===> processando resultado da ACTIVITY CHOOSER: resultCode=" + resultCode);

    // Obt�m o intent usado para execu��o da activity de envio de email
    Intent intent = this.getIntent();

    if (intent != null) {
      ComponentName compName = intent.getComponent();
      if (compName != null) {
        Log.d(TAG, "compName=" + compName.getClassName() + ", compName=" + compName.getPackageName());
      }
    }

    // verifica os dados retornados pela Activity que enviou o email
    if (data != null) {

      Log.w(TAG, "resultActivityChooser() - dados recebidos n�o foram trabalhados");

      // The action of this intent or null if none is specified.
      Log.w(TAG, "resultActivityChooser() - data.getAction()=" + data.getAction());

    }

    if (resultCode == 0) {

      Log.d(TAG, "ACTIVITY_CHOOSER - email enviado com sucesso");

      // mensagem exibida ap�s envio de email
      Toast.makeText(this, "Email enviado com sucesso", Toast.LENGTH_LONG).show();

      // Atualiza o estado da m�quina de estado
      setEstado(3);

      // Processa o pr�ximo estado
      estadoFinal();

    } else if (resultCode == RESULT_CANCELED) {
      // envio do email foi cancelado pelo usu�rio
      Log.d(TAG, "resultActivityChooser() - ACTIVITY_CHOOSER - o envio de email foi cancelado");
      estadoFinal();

    } else {

      Log.d(TAG, "resultActivityChooser() - ACTIVITY_CHOOSER - resultCode " + resultCode + " n�o pde ser tratado");
      estadoFinal();

    }

  }

  /**
   * estadoFinal()
   * 
   * Representa o estado final da m�quina de estado.
   * 
   * � tamb�m o respons�vel pela finaliza��o da activity, estabelecendo seu
   * resultado.
   * 
   * Se o estado final foi atingido ent�o o processo correu segundo esperado.
   * 
   */
  private void estadoFinal() {

    // Obtem as informa��es sobre a Intent "chamadora"
    Intent intent = getIntent();

    // Obt�m o estado corrente da m�quina de estado
    int estadoCorrente = getEstado();

    if (estadoCorrente == 3) {

      // estado final atingido com sucesso
      Log.i(TAG, "estadoFinal() - final do processamento");

      // retorna as informa��es sobre o participante a sobre sua participa��o
      intent.putExtra(PARTICIPANTE, mParticipante);
      intent.putExtra(PARTICIPACAO, mParticipacao);

      intent.putExtra("br.com.mltech.result", "OK");

      // estabelece o resultado da execu��o da Activity
      setResult(RESULT_OK, intent);

    } else {

      Toast.makeText(this, "Falha no processo. Estado atual: " + estadoCorrente, Toast.LENGTH_SHORT).show();

      // estado final atingido por�m houve falha
      Log.w(TAG, "estadoFinal() - n�o foi poss�vel chegar ao final do processamento.");

      intent.putExtra("br.com.mltech.result", "NOT_OK");

      // estabelece o resultado da execu��o da Activity
      setResult(RESULT_CANCELED, intent);

    }

    atualizaModoTela(Configuration.ORIENTATION_PORTRAIT);

    // Termina a execu��o da Activity respons�vel por tirar e enviar uma
    // foto
    finish();

  }

  /**
   * setEstado(int e)
   * 
   * Atualiza o estado da uma m�quina de estados
   * 
   * @param e
   *          novo estado (pr�ximo estado)
   */
  private void setEstado(int e) {

    Log.i(TAG, "----------------------------------------------------------");
    Log.i(TAG, "Transi��o do estado: " + mEstado + " para o estado: " + e);
    Log.i(TAG, "----------------------------------------------------------");
    mEstado = e;
  }

  /**
   * getEstado()
   * 
   * Obt�m o estado atual da m�quina de estados
   * 
   * @return Um inteiro contendo o estado da state machine
   * 
   */
  private int getEstado() {

    return mEstado;
  }

  /**
   * onRestart()
   */
  @Override
  protected void onRestart() {

    super.onRestart();
    Log.d(TAG, "*");
    Log.d(TAG, "****************************************************");
    Log.d(TAG, "*** onRestart() - A aplica��o foi restartada ... ***");
    Log.d(TAG, "****************************************************");
    Log.d(TAG, "*");

    contador++;
    i++;

    numRestart++;
    showXXX();

    showVariables();

  }

  /**
   * onStart()
   */

  @Override
  protected void onStart() {

    super.onStart();
    Log.d(TAG, "*** onStart() ***");
    showVariables();

  }

  /**
   * onResume()
   */

  @Override
  protected void onResume() {

    super.onResume();
    Log.d(TAG, "*** onResume() ***");
    showVariables();
  }

  /**
   * onPause()
   */
  @Override
  protected void onPause() {

    super.onPause();
    Log.d(TAG, "*** onPause() ***");
    showVariables();
  }

  /**
   * onStop()
   */
  @Override
  protected void onStop() {

    super.onStop();
    Log.d(TAG, "*** onStop() ***");

    /*
     * Essa atribui��o n�o deve ser feita pois perdemos o valor de xUri que ser�
     * usado posteriormente mFilename = null; mUri = null; xUri = null;
     */

    showVariables();
  }

  /**
   * onSaveInstanceState(Bundle outState)
   */

  @Override
  protected void onSaveInstanceState(Bundle outState) {

    super.onSaveInstanceState(outState);
    Log.d(TAG, "*** onSaveInstanceState() ***");

    outState.putSerializable(PARTICIPANTE, mParticipante);
    outState.putSerializable(PARTICIPACAO, mParticipacao);

    Log.i(TAG, "*** onSaveInstanceState()");

    showXXX();

    FileUtils.showBundle(outState);

  }

  /**
   * onRestoreInstanceState(Bundle savedInstanceState)
   */

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {

    super.onRestoreInstanceState(savedInstanceState);

    Log.d(TAG, "*********************************");
    Log.d(TAG, "*** onRestoreInstanceState() ***");
    Log.d(TAG, "*********************************");

    if (savedInstanceState == null) {
      Log.w(TAG, "onRestoreInstanceState() - savedInstaceState � nulo");
    }

    if (savedInstanceState.containsKey(PARTICIPANTE)) {
      mParticipante = (Participante) savedInstanceState.getSerializable(PARTICIPANTE);
    }

    if (savedInstanceState.containsKey(PARTICIPACAO)) {
      mParticipacao = (Participacao) savedInstanceState.getSerializable(PARTICIPACAO);
    }

    FileUtils.showBundle(savedInstanceState);

    showXXX();

  }

  /**
   * showAlert(String msg)
   * 
   * Exibe uma caixa de di�logo com uma mensagem e um bot�o de fechar a janela
   * 
   * @param msg
   *          Mensagem exibida.
   * 
   */
  void showAlert(String msg) {

    if (msg == null) {
      // retorna se a mensagem for nula
      return;
    }

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(msg).setNeutralButton("Fechar", null);
    AlertDialog alert = builder.create();
    alert.show();

  }

  /**
   * showVariables()
   * 
   * Exibe o valor dos atributos do objeto (se forem diferente de null)
   * 
   */
  public void showVariables() {

    Log.v(TAG, "=================================");
    Log.v(TAG, "showVariables() - Vari�veis de atributo:");

    if (mContratante != null) {
      // Log.v(TAG, "  mContratante=" + mContratante);
    }

    if (mEvento != null) {
      // Log.v(TAG, "  mEvento=" + mEvento);
    }

    if (mParticipante != null) {
      // Log.v(TAG, "  mParticipante=" + mParticipante);
    }

    if (mParticipacao != null) {
      // Log.v(TAG, "  mParticipacao=" + mParticipacao);
    }

    if (mFilename != null) {
      Log.v(TAG, "  mFilename=" + mFilename);
    }

    if (xUri != null) {
      Log.v(TAG, "  xUri=" + xUri);
    }

    Log.v(TAG, "  mEstado=" + mEstado + ", mContador=" + mContador);
    Log.v(TAG, "  mCurrentCamera=" + mCurrentCamera);

    showXXX();

    Log.v(TAG, "=================================");

  }

  /**
   * getCameraInstance(int cameraID)
   * 
   * Tenta obter uma inst�ncia da c�mera identificada por seu n�mero (cameraID).
   * A primeira c�mera, se existir ter� o identificador 0.
   * 
   * @param cameraID
   *          Identificador da c�mera do dispositivo
   * 
   * @return Uma inst�ncia da classe Camera ou null em caso de erro
   * 
   */
  public static Camera getCameraInstance(int cameraID) {

    Camera c = null;

    try {

      /*
       * Cria um novo objeto Camera para acesso a c�mera cameraID. Se a c�mera
       * j� estiver em uso ou n�o existir retorma nulo
       */

      c = Camera.open(cameraID); // attempt to get a Camera instance

    } catch (Exception e) {

      // Camera is not available (in use or does not exist)
      Log.w(TAG, ">>> getCameraInstance(" + cameraID + ") - C�mera est� indispon�vel (est� em uso ou n�o existe)", e);

    }

    return c; // returns null se a c�mera estiver indispon�vel

  }

  /**
   * leArquivoMoldura(String arquivoMoldura)
   * 
   * Carrega arquivo de moldura
   * 
   * @param arquivoMoldura
   *          Nome do arquivo
   * 
   * @return um bitmap com o arquivo contendo a moldura ou null no caso de algum
   *         problema
   * 
   */
  private Bitmap leArquivoMoldura(String arquivoMoldura) {

    Log.d(TAG, "leArquivoMoldura() - arquivoMoldura: [" + arquivoMoldura + "].");

    File moldura = new File(arquivoMoldura);

    if ((moldura != null) && (!moldura.exists())) {
      Log.w(TAG, "leArquivoMoldura() - arquivoMoldura: [" + arquivoMoldura + "] n�o existe.");
      return null;
    }

    // l� o bitmap contendo a moldura
    Bitmap bmMoldura = ManipulaImagem.getBitmapFromFile(moldura);

    if (bmMoldura == null) {

      Log.w(TAG, "leArquivoMoldura() - arquivo contento a moldura est� vazio.");
      return null;

    } else {
      Log.v(TAG, "leArquivoMoldura() - largura x altura da moldura: " + ManipulaImagem.getStringBitmapSize(bmMoldura));

      return bmMoldura;

    }

  }

  /**
   * getSharedPreference(String sharedPreferencesName, String attribute)
   * 
   * Obt�m um par�metro da lista de prefer�ncias.
   * 
   * @param sharedPreferencesName
   * @param attribute
   * 
   * @return o valor do atributo solicitado ou null caso haja algum erro (o
   *         par�metro n�o foi encontrado)
   * 
   */
  private String getSharedPreference(String sharedPreferencesName, String attribute) {

    SharedPreferences preferences = getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);

    String sValue = preferences.getString(attribute, "");

    preferences = null;

    return sValue;

  }

  /**
   * showXXX()
   * 
   * Exibe o valor de algumas vari�veis selecionadas
   * 
   */
  private void showXXX() {

    // TODO alterar o nome para showClassVariables

    Log.v(TAG, "    showXXX() - file: " + file);
    Log.v(TAG, "    showXXX() - outputFileUri: " + outputFileUri);
    Log.v(TAG, "    showXXX() - Contador: " + contador + ", i=" + i);
    Log.v(TAG, "    showXXX() - numCreate: " + numCreate + ", numRestart: " + numRestart);

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
   * 
   * Executa a Activity que captura uma foto
   * 
   * @param arquivo
   *          nome do arquivo onde a foto ser� armazenada
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
   * Executa a Activity que tira uma foto de "mentirinha", isto �, apenas
   * retorna uma imagem pre-armazenada como se fosse uma foto
   * 
   * @param arquivo
   *          nome do arquivo onde a foto ser� armazenada
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
   *          nome do arquivo onde a foto original ser� armazenada
   * 
   */
  private void executaActivityTiraFotoPolaroid(String arquivo) {

    file = new File(arquivo);

    Log.d(TAG, "executaActivityTiraFotoPolaroid() - arquivo=" + file.getAbsolutePath());

    outputFileUri = Uri.fromFile(file);

    Intent intent = null;

    // ------------------------------------------

    if (FLAG == 0) {

      // simula��o de "tirar uma foto"
      intent = new Intent(this, ActivityCameraSimplesDummy.class);

      intent.putExtra("br.com.mltech.outputFileUri", outputFileUri);

    } else if (FLAG == 1) {

      // tira uma foto verdadeira
      intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

      intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

    }

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

    // String arquivo =
    // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    // + "/" + System.currentTimeMillis() + ".jpg";

    // TODO melhorar a constru�a� abaixo
    String arquivo = (FileUtils.obtemNomeArquivo(".png")).getAbsolutePath();

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

    if (FLAG == 0) {

      // simula��o de "tirar uma foto"
      intent = new Intent(this, ActivityCameraSimplesDummy.class);

      intent.putExtra("br.com.mltech.outputFileUri", outputFileUri);

    } else if (FLAG == 1) {

      // tira uma foto verdadeira
      intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

      intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

    }

    // inicia a nova Activity relacionada na Intent
    startActivityForResult(intent, TIRA_FOTO_CABINE);

  }

  // ---------------------------------------------------------------------------
  //
  //
  // ---------------------------------------------------------------------------

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

        // cria um Bitmap a partir do arquivo lido
        Bitmap bitmapFoto = ManipulaImagem.criaBitmap(outputFileUri);

        // cria uma Foto
        foto = new Foto(file.getAbsolutePath(), bitmapFoto);

        if (!foto.ler()) {
          Log.w(TAG, "processaActivityResultTiraFoto() - Erro na leitura ...");
        }

        Log.i(TAG, "processaActivityResultTiraFoto() - foto f=" + foto);

      }

      // atualiza a imagem na activity
      // image.setImageBitmap(foto.getImagem());

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
   * Processa a foto retornada como uma foto formato Polaroid
   * 
   * @param resultCode
   * @param data
   * 
   *          Use a vari�vel de classe: file
   * 
   */
  private String processaActivityResultPolaroid(int resultCode, Intent data) {

    if (data == null) {
      Log.w(TAG, "processaActivityResultPolaroid - data (Intent) � vazia");
    }

    if (resultCode == RESULT_CANCELED) {

      // opera��o cancelada
      Log.w(TAG, "processaActivityResultPolaroid() - Opera��o cancelada pelo usu�rio");
      estadoFinal();

    } else if (resultCode == RESULT_OK) {

      if (data != null) {

        Log.d(TAG, "processaActivityResultPolaroid() - data.getData()= " + data.getData());

        Log.d(TAG, "processaActivityResultPolaroid() - extra: " + data.getStringExtra("extra1"));

        Log.d(TAG, "processaActivityResultPolaroid() - outputFileUri: " + data.getStringExtra("outputFileUri"));

        // nome do arquivo onde o bitmap est� armazenado
        file = new File(data.getStringExtra("outputFileUri"));

      } else {

        file = null;

      }

      // cria uma URI com o nome do arquivo
      outputFileUri = Uri.fromFile(file);

      // foto = obtemFotoCamera(outputFileUri);

      // exibe a imagem
      carregaImagem();

      Bitmap fp = formatarPolaroid(outputFileUri);

      if (fp != null) {

        boolean b = ManipulaImagem.gravaBitmapArquivo(fp, "fotoPolaroid.jpg");

        if (!b) {
          // foto n�o foi gravada
          return null;
        }

      }

      Log.w(TAG, "processaActivityResultPolaroid - FIM");

    }

    return null;

  }

  /**
   * obtemFotoCamera(Uri uri)
   * 
   * @param uri
   * @return
   */
  public Foto obtemFotoCamera(Uri uri) {

    if (uri == null) {
      return null;
    }

    File file = new File(uri.getPath());

    //
    // cria um objeto Foto
    //
    Foto fotoX = new Foto(file.getAbsolutePath());

    // cria um bitmap a partir de uma Uri
    Bitmap bitmap = ManipulaImagem.criaBitmap(uri);

    // armazena o bitmap
    fotoX.setImagem(bitmap);

    // nesse ponto a foto est� completa

    // aqui temos um objeto da classe Foto
    Log.i(TAG, "obtemFotoCamera() - fotoX: " + fotoX);

    return fotoX;

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
  private FotoCabine processaActivityResultCabine(int resultCode, Intent data) {

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

    return null;

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

    // preenche o array com as fotos armazenadas na fotoCabine
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
      boolean gravou = fotos3x4[i].gravar();

      if (gravou) {
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
    // image.setImageBitmap(fotoFinal.getImagem());

    Log.i(TAG, "montaFotoCabine() - fotoFinal: " + fotoFinal.toString());

    return foto;

  }

  /**
   * carregaImagem()
   * 
   * Esse Carrega uma imagem a partir de um arquivo
   * 
   * Decodifica a imagem armazenada no arquivo e tenta criar um bitmap que ser�
   * redimencionado e exibido.
   * 
   * Cria-se uma foto e acrescenta-a a lista de fotos tiradas.
   * 
   */
  private void carregaImagem() {

    // TODO avaliar a necessidade desse m�todo

    Log.d(TAG, "carregaImagem()");

    /*
     * if (file == null) { // Log.w(TAG,
     * "carregaImagem() ==> o arquivo contendo a imagem � nulo"); return; }
     */

    // exibe o bitmap
    // image.setImageBitmap(foto.getImagem());

    // atualiza o n� de fotos carregadas
    numFotosCarregadas++;

    //
    // Aadiciona a nova foto a lista de fotos
    //
    listaFotos.add(foto);

    Log.i(TAG, " ");
    Log.i(TAG, "carregaImagem() ===> numFotosTiradas: " + numFotosTiradas + ", numFotosCarregadas: " + numFotosCarregadas);
    Log.i(TAG, " ");

  }

  /**
   * atualizaModoTela(int novaOrientacao)
   * 
   * Atualiza a orienta��o da tela.
   * 
   * @param novaOrientacao
   */
  private void atualizaModoTela(int novaOrientacao) {

    int orientacaoAtual = this.getResources().getConfiguration().orientation;

    Log.d(TAG, "atualizaModoTela() - Orienta��o atual: " + orientacaoAtual + " - " + getScreenOrientation(orientacaoAtual));

    if (novaOrientacao != orientacaoAtual) {
      this.setRequestedOrientation(novaOrientacao);
      Log.d(TAG, "atualizaModoTela() - nova orienta��o: " + novaOrientacao + " - " + getScreenOrientation(novaOrientacao));
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

  /**
   * formatarPolaroid(Uri uriFotoOriginal)
   * 
   * Recebe o endere�o de uma foto redimensiona a foto insere a moldura e
   * transforma a foto no tamanho 9x11
   * 
   * @return um bitmap
   */
  public Bitmap formatarPolaroid(Uri uriFotoOriginal) {

    // Cria um bitmap a partir da Uri da foto
    Bitmap bmFotoOriginal = ManipulaImagem.criaBitmap(uriFotoOriginal);

    // redimensiona a foto original para 9x12 para manter a propor��o 3:4
    Bitmap bmFoto9x12 = ManipulaImagem.getScaledBitmap2(bmFotoOriginal, 340, 454);

    // Define o nome da foto redimensionada
    String nomeArquivo = PATH_FOTOS + FileUtils.getFilename(uriFotoOriginal) + "_9x12.png";

    boolean gravou = ManipulaImagem.gravaBitmapArquivo(bmFoto9x12, nomeArquivo);

    if (!gravou) {
      Log.d(TAG, "formatarPolaroid() - foto n�o pode ser gravada");
      return null;
    }

    // redimensiona a foto 9x12 para 8x8, isto �, copia uma "janela" 8x8 da foto
    Options options = new Options();

    Rect rect = new Rect(0, 0, 302, 302);

    // Obtem um bitmap com a foto redimensionada para 8x8
    Bitmap bmFoto8x8 = ManipulaImagem.getBitmapRegion(nomeArquivo, rect, options);

    nomeArquivo = PATH_FOTOS + FileUtils.getFilename(uriFotoOriginal) + "_8x8.png";

    Bitmap bmFotoComMoldura = ManipulaImagem.overlay4(bmFoto8x8, mBitmapMolduraPolaroid);

    // o nome completo do arquivo onde a foto com moldura foi armazenada
    return bmFotoComMoldura;

  }

}
