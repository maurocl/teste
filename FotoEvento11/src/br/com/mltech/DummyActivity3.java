package br.com.mltech;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
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

  // indica qual Activity ser� executada:
  // 0 = tira foto dummy;
  // 1 = Tira foto normal (usando a aplica��o Android);
  // 2 = tira foto usando uma aplica��o de c�mera customizada;
  private static int FLAG = 2;

  private static final int TIRA_FOTO = 200;

  private static final int TIRA_FOTO_POLAROID = 201;

  private static final int TIRA_FOTO_CABINE = 202;

  // ------------------
  // vari�veis da classe
  // ------------------

  //
  private static File file;

  //
  private static Uri outputFileUri;

  // Lista de todas as fotos tiradas
  private static List<Foto> listaFotos;

  // Foto
  private static Foto foto;

  // FotoCabine
  private static FotoCabine fotoCabine;

  // Defini��o dos atributos da aplica��o
  private static Contratante mContratante;

  private static Evento mEvento;

  private static Participante mParticipante;

  private static Participacao mParticipacao;

  private static SharedPreferences mPreferences;

  // Uri da �ltima foto
  private static Uri xUri;

  // Estado atual da m�quina de estado da aplica��o
  private static int mEstado = -1;

  // N� de vezes que a activity � criada
  private static int mContador = 0;

  // N� de c�meras do dispositivo
  private static int numCameras = -1;

  // N� da c�mera corrente em uso (se houver)
  private static int currentCamera = -1;

  // Bitmaps contendo as molduras
  private static Bitmap mBitmapMolduraPolaroid;

  private static Bitmap mBitmapMolduraCabine;

  // Molduras
  private static Moldura molduraPolaroid;

  private static Moldura molduraCabine;

  // Contador do n� de fotos cabine
  // private static int mNumFotosCabine = 0;

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
  private static int indiceFoto = 0;

  // n� de fotos tiradas no formato Polaroid
  private static int numFotosPolaroid = 0;

  // n� de fotos tiradas no formato Cabine
  private static int numFotosCabine = 0;

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
    currentCamera = obtemIdentificadorCamera();

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
   * Inicializa as vari�veis que v�o conter o bitmap das molduras:
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

    // o arquivo de configura��o possui a informa��o da localiza��o das
    // molduras

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
   * Verifica se existe uma configura��o explicita para usar um determinado n�
   * de c�mera (se o sistema possuir mais de uma c�mera). Esse recurso � usado
   * quando existe uma c�mera frontal que se deseja usar ao inv�s da c�mera
   * traseira.
   * 
   * @return o n� da c�mera frontal ou 0 caso esse par�metro n�o esteja
   *         cadastrado
   * 
   */
  private int obtemIdentificadorCamera() {

    // -----------------------------------------------
    // obtem o n� da c�mera usada para tirar as fotos
    // -----------------------------------------------
    String s = getSharedPreference("pref_email", "preferencias_num_camera_frontal");

    Log.i(TAG, "obtemIdentificadorCamera() - N� da c�mera frontal=" + s);

    // n�mero de c�mera
    int num = 0;

    if ((s != null) && (!s.equals(""))) {

      num = Integer.valueOf(s);

    } else {

      num = 0;
      Log.w(TAG, "obtemIdentificadorCamera() - N� da c�mera frontal n�o foi definido. Assumindo o valor 0");

    }

    // o n� da c�mera
    return num;

  }

  /**
   * onActivityResult(int requestCode, int resultCode, Intent data)
   * 
   * Trata o resultado da execu��o das Activities
   * 
   * Processa o resultado da execu��o das Activities
   * 
   * � chamado quando a activity lan�ada retorna, dando a voc� o requestCode com
   * o qual voc� iniciou, o resultCode retornado e qualquer dado adicional
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

    Log.i(TAG, "-------------------------------------------------------------------------------");
    Log.i(TAG, "onActivityResult(request " + requestCode + ", result=" + resultCode + ", data " + data + ") ...");
    Log.i(TAG, "-------------------------------------------------------------------------------");

    String sNomeArquivo = null;

    switch (requestCode) {

      case ACTIVITY_PARTICIPANTE:

        resultActivityParticipante(resultCode, data);
        break;

      case ACTIVITY_CHOOSER:

        resultActivityChooser(resultCode, data);
        break;

      case TIRA_FOTO:

        numFotosTiradas++;
        processaActivityResultTiraFoto(resultCode, data);
        meuMetodo(requestCode, sNomeArquivo);
        break;

      case TIRA_FOTO_POLAROID:

        numFotosTiradas++;
        sNomeArquivo = processaActivityResultPolaroid(resultCode, data);
        mParticipacao.setNomeArqFoto(sNomeArquivo);
        numFotosPolaroid++;
        meuMetodo(requestCode, sNomeArquivo);
        break;

      case TIRA_FOTO_CABINE:

        numFotosTiradas++;
        sNomeArquivo = processaActivityResultCabine(resultCode, data);
        if (sNomeArquivo != null) {
          mParticipacao.setNomeArqFoto(sNomeArquivo);
          numFotosCabine++;
          meuMetodo(requestCode, sNomeArquivo);
        }
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
   * 
   */
  private void meuMetodo(int requestCode, String meuArquivo) {

    Log.d(TAG, "meuMetodo() - requestCode=" + requestCode);
    Log.d(TAG, "meuMetodo() - meuArquivo=" + meuArquivo);

    String msg = null;

    // --------------------------------------------------------------
    // exibe a foto
    // --------------------------------------------------------------
    executaActivityExibeFoto(meuArquivo);
    // --------------------------------------------------------------

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

    // showAlert(msg);
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    File fff = null;

    if (meuArquivo != null) {
      fff = new File(meuArquivo);
    } else {
      String meuArquivoPadrao = "/mnt/sdcard/Pictures/fotoevento/fotos/casa_320x240.png";
      fff = new File(meuArquivoPadrao);
    }

    //showAlert("Envia email");

    // H� uma foto com moldura que ser� enviada por email
    setEstado(2);

    enviaEmail(Uri.fromFile(fff));

    Log.w(TAG, "meuMetodo() - fim");

  }

  /**
   * executaActivityExibeFoto(String meuArquivo)
   * 
   * @param meuArquivo
   *          nome completo do arquivo onde a foto est� localizada
   * 
   */
  private void executaActivityExibeFoto(String meuArquivo) {

    File f = new File(meuArquivo);
    Uri uri = Uri.fromFile(f);

    Intent intent = new Intent(this, ExibeFotoActivity.class);
    intent.setData(uri);
    startActivity(intent);

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
    if (CameraTools.isCameraWorking(currentCamera)) {

      Log.i(TAG, "iniciaVariaveis() - Camera: [" + currentCamera + "] est� em funcionamento...");

    } else {

      Log.w(TAG, "iniciaVariaveis() - Camera: [" + currentCamera + "] n�o est� em funcionamento");

      Toast.makeText(this, "Camera n�o est� dispon�vel", Toast.LENGTH_SHORT);

      return false;

    }

    // Obtem a Intent que iniciou esta Activity
    Intent i = this.getIntent();

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

    Log.v(TAG, "iniciaVariaveis() - mContratante=" + mContratante);
    Log.v(TAG, "iniciaVariaveis() - mEvento=" + mEvento);

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
   * Aguarda as informa��es sobre o participante do evento.
   * 
   */
  private void startActivityParticipante() {

    // Cria uma nova Intent para chamar a Activity Participante
    Intent intentParticipante = new Intent(this, ParticipanteActivity.class);

    // Inclui o par�metro mEvento (com as informa��es sobre o evento em
    // curso)
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
   * Nesse passo o usu�rio j� forneceu suas informa��es pessoais e agora �
   * necess�rio tirar a(s) foto(s).
   * 
   * Tira as fotos de acordo com a solicita��o do participante
   * 
   */
  private void tirarFotos() {

    if (mParticipacao == null) {
      Log.d(TAG, "tirarFotos() - n�o � poss�vel obter as informa��es do participante");
      estadoFinal();
    }

    if (mEvento == null) {
      Log.d(TAG, "tirarFotos() - n�o � poss�vel obter as informa��es sobre o evento");
      estadoFinal();
    }

    int tipoEfeito = mParticipacao.getEfeitoFoto();

    // obt�m o tipo da foto (se o formato da foto � Polaroid ou Cabine)
    int tipoFoto = mParticipacao.getTipoFoto();

    Log.i(TAG, "tirarFotos() - tipoFoto: " + tipoFoto);

    if (tipoFoto == TIPO_FOTO_POLAROID) {

      String arquivo = FileUtils.obtemNomeArquivo(".png").getAbsolutePath();

      Log.d(TAG, "tirarFotos() - arquivo: " + arquivo);

      Log.i(TAG, "tirarFotos() - tipoFoto: POLAROID");
      executaActivityTiraFotoPolaroid(arquivo);

    } else if (tipoFoto == TIPO_FOTO_CABINE) {

      Log.i(TAG, "tirarFotos() - tipoFoto: CABINE");
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
   * getIntentTirarFoto()
   * 
   * Cria uma intent para solicitar uma foto a uma activity
   * 
   * Observe que a vari�vel FLAG � usada para "escolher" a activity que ir�
   * retornar a foto
   * 
   * @return uma Intent (mensagem) para execu��o da activity desejada
   * 
   */
  private Intent getIntentTirarFoto() {

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
    else if (FLAG == 2) {

      // tira uma foto verdadeira usando a activity CameraActivity
      intent = new Intent(this, CameraActivity.class);

      intent.putExtra("br.com.mltech.outputFileUri", outputFileUri);

    }

    Log.d(TAG, "getIntentTirarFoto() -  " + intent);
    Log.d(TAG, "getIntentTirarFoto() - outputFileUri: " + outputFileUri);

    return intent;

  }

  /**
   * executaActivityTiraFotoPolaroid(String arquivo)
   * 
   * @param arquivo
   *          nome do arquivo onde a foto original ser� armazenada
   * 
   */
  private void executaActivityTiraFotoPolaroid(String arquivo) {

    Log.d(TAG, "executaActivityTiraFotoPolaroid() - arquivo=" + arquivo);

    file = new File(arquivo);

    outputFileUri = Uri.fromFile(file);

    Intent intent = getIntentTirarFoto();

    startActivityForResult(intent, TIRA_FOTO_POLAROID);

  }

  /**
   * processaActivityResultPolaroid(int resultCode, Intent data)
   * 
   * Processa o resultado da execu��o da activity respons�vel por fornecer uma
   * foto
   * 
   * Pega a foto retornada e formata-a no formato Polaroid
   * 
   * @param resultCode
   *          resultado da execu��o da activity
   * @param data
   *          intent contendo os dados retornados (se houver)
   * 
   *          Use a vari�vel de classe: file
   * 
   */
  private String processaActivityResultPolaroid(int resultCode, Intent data) {

    String meuArquivo2 = null;

    /**
     * 
     */
    if (resultCode == RESULT_CANCELED) {

      // opera��o cancelada
      Log.w(TAG, "processaActivityResultPolaroid() - Opera��o cancelada pelo usu�rio");
      estadoFinal();

    } else if (resultCode != RESULT_OK) {

      Log.w(TAG, "processaActivityResultPolaroid() - Opera��o n�o conhecida");
      estadoFinal();

    }

    //
    // RESULT_OK ==> activity retornou com sucesso
    //
    if (data != null) {

      Log.d(TAG, "processaActivityResultPolaroid() - data.getData()= " + data.getData());

      // Log.d(TAG, "processaActivityResultPolaroid() - outputFileUri: " +
      // data.getStringExtra("outputFileUri"));
      Log.d(TAG, "processaActivityResultPolaroid() - outputFileUri: " + data.getParcelableExtra("outputFileUri"));

      // nome do arquivo onde o bitmap est� armazenado
      // file = new File(data.getStringExtra("outputFileUri"));

    } else {

      // n�o houve retorno de dados
      Log.w(TAG, "processaActivityResultPolaroid - data (Intent) � vazia");
      // file = null;

    }

    // cria uma URI para referenciar o arquivo
    // outputFileUri = Uri.fromFile(file);

    Log.w(TAG, "processaActivityResultPolaroid() - outputFileUri: " + outputFileUri);

    // grava o bitmap (foto original)
    boolean gravouFotoOriginal = ManipulaImagem.gravaBitmapArquivo3(outputFileUri);

    if (!gravouFotoOriginal) {
      Log.w(TAG, "processaActivityResultPolaroid() - arquivo: " + outputFileUri + " n�o pode ser gravado");
      return null;
    }

    // String meuArquivo = FileUtils.getFilename(file);
    String meuArquivo = outputFileUri.getPath();

    meuArquivo = FileUtils.getFilename(outputFileUri);

    // cria um novo arquivo para armazenar a foto com a moldura
    meuArquivo2 = PATH_FOTOS + "/" + meuArquivo + ".jpg";

    Log.w(TAG, "meuArquivo=" + meuArquivo);
    Log.w(TAG, "meuArquivo2=" + meuArquivo2);

    // exibe a imagem
    carregaImagem();

    // Obt�m uma foto formato polaroid
    Bitmap fp = formatarPolaroid(outputFileUri);

    if (fp != null) {

      // bitmap obtido com sucesso
      boolean gravouFotoComMoldura = ManipulaImagem.gravaBitmapArquivo(fp, meuArquivo2);

      if (!gravouFotoComMoldura) {
        // foto n�o foi gravada
        Log.w(TAG, "processaActivityResultPolaroid() - arquivo: " + meuArquivo2 + " n�o pode ser gravado");
        return null;
      }

    }

    Log.w(TAG, "processaActivityResultPolaroid - FIM");

    return meuArquivo2;

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

    // TODO melhorar a constru��o abaixo
    String arquivo = (FileUtils.obtemNomeArquivo(".png")).getAbsolutePath();

    // cria um File usando o nome do arquivo gerado
    file = new File(arquivo);

    Log.d(TAG, "==>");
    Log.d(TAG, "==> executaActivityTiraFotoCabine() - contadorCabine: " + indiceFoto);
    Log.d(TAG, "==> executaActivityTiraFotoCabine() - arquivo=" + file.getAbsolutePath());
    Log.d(TAG, "==> ");

    // cria uma inst�ncia da classe Foto
    // fornecendo o nome do arquivo onde a foto
    // deve ser armazenada
    Foto foto = new Foto(file.getAbsolutePath());

    // armazena a foto no �ndece dado pela vari�vel contadorCabine no array de
    // fotos do objeto FotoCabine
    fotoCabine.setFoto(indiceFoto, foto);

    // especifica a Uri do arquivo onde a foto deve ser armazenada
    outputFileUri = Uri.fromFile(file);

    // cria uma intent para "tirar a foto"
    Intent intent = getIntentTirarFoto();

    // exibe informa��es sobre a intent criada
    Log.d(TAG, "executaActivityTiraFotoCabine() - intent: " + intent);

    Log.d(TAG, "executaActivityTiraFotoCabine() - " + intent.getParcelableExtra("br.com.mltech.outputFileUri"));

    // inicia a activity respons�vel por obter a foto usando a intent criada.
    startActivityForResult(intent, TIRA_FOTO_CABINE);

  }

  /**
   * processaActivityResultCabine(int resultCode, Intent data)
   * 
   * Esta rotina � respons�vel por obter tr�s foto da c�mera. A vari�vel
   * contadorCabine controla o n� de fotos (varia de 0 a 2)
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
  private String processaActivityResultCabine(int resultCode, Intent data) {

    Bitmap meuBitmap = null;

    if (resultCode == RESULT_OK) {

      // activity executada com sucesso
      Log.i(TAG, "processaActivityResultCabine() - processando o recebimento da foto - indiceFoto=" + indiceFoto);

      // incrementa o �ndice usado para armazenar a foto obtida no array de
      // fotos
      indiceFoto++;

      if (indiceFoto < 3) {

        // obtem uma outra foto
        executaActivityTiraFotoCabine();

      } else {

        // tr�s foto j� foram obtidas (tiradas)
        meuBitmap = fimExecucaoActivityTiraFotoCabine();

      }

    } else if (resultCode == RESULT_CANCELED) {

      // opera��o cancelada
      Log.w(TAG, "processaActivityResultCabine() - Opera��o cancelada pelo usu�rio na foto: " + indiceFoto);
      // TODO aqui deveremos "cancelar" a fotoCabine fazendo-a null (entre
      // outras coisas)
      meuBitmap = null;

    } else {
      Log.w(TAG, "processaActivityResultCabine() - Opera��o n�o suportada pelo usu�rio");
    }

    String arquivo = (FileUtils.obtemNomeArquivo(".png")).getAbsolutePath();

    Foto fotoProcessada = new Foto(arquivo, meuBitmap);

    boolean b;

    try {

      b = fotoProcessada.gravar();

    } catch (FileNotFoundException e) {

      e.printStackTrace();
    } catch (IOException e) {

      e.printStackTrace();
    }

    String s = null;

    // retorna o nome do arquivo onde a foto foi armazenada
    if (fotoProcessada != null) {
      s = fotoProcessada.getArquivo();
    }

    return s;

  }

  /**
   * fimExecucaoActivityTiraFotoCabine()
   * 
   * @return uma inst�ncia de Foto ou null em caso de algum problema.
   */
  private Bitmap fimExecucaoActivityTiraFotoCabine() {

    // Ap�s tirar as tr�s fotos � necess�rio:
    // - converter as foto para o formato 3x4
    // - montar a foto formato cabine juntando as fotos 3x4 e a moldura
    // fotoCabine.getFoto[0] --> Foto
    // fotoCabine.getFoto[1] --> Foto
    // fotoCabine.getFoto[2] --> Foto

    Log.d(TAG, "fimExecucaoActivityTiraFotoCabine()");

    if (fotoCabine == null) {
      Log.w(TAG, "fimExecucaoActivityTiraFotoCabine() - fotoCabine � nula");
      return null;
    }

    // cria o bitmap correspondente a cada foto
    criaBitmapFotoCabine(fotoCabine);

    // Exibe o estado da inst�ncia da classe FotoCabine
    Log.d(TAG, fotoCabine.toString());

    // nesse ponto todas as foto j� possuem seu bitmap

    Log.d(TAG, "fimExecucaoActivityTiraFotoCabine() - agora � montar a foto cabine ...");

    Bitmap meuBitmap = null;

    //
    try {

      meuBitmap = montaFotoCabine(fotoCabine);

    } catch (FileNotFoundException e) {

      Log.w(TAG, "fimExecucaoActivityTiraFotoCabine() - exece��o arquivo n�o encontrado (FileNotFound)", e);

    } catch (IOException e) {

      Log.w(TAG, "fimExecucaoActivityTiraFotoCabine() - exce��o de IO (IOException)", e);

    }

    return meuBitmap;

  }

  /**
   * montaFotoCabine(fotoCabine)
   * 
   * Cria uma foto com moldura cabine 3,5 x 15 cm a partir de tr�s fotos 3x4 e
   * uma moldura.
   * 
   * @return bitmap contendo a foto formatada
   * 
   * @throws IOException
   * 
   * @throws FileNotFoundException
   * 
   */
  private Bitmap montaFotoCabine(FotoCabine fotoCabine) throws FileNotFoundException, IOException {

    if (fotoCabine == null) {
      // foto n�o existe
      Log.w(TAG, "montaFotoCabine() - fotoCabine est� vazia");
      return null;
    }

    // cria um array contendo refer�ncias a tr�s inst�ncias de Bitmap
    Bitmap[] fotos3x4 = new Bitmap[3];

    // preenche o array com as fotos armazenadas na fotoCabine
    Foto[] fotos = fotoCabine.getFotos();

    int i = 0;

    // processa cada uma das fotos e faz o redimensionamento para o tamanho
    // 3x4
    for (Foto foto : fotos) {

      // transforma cada foto em 3x4
      Log.i(TAG, "montaFotoCabine() - foto[" + i + "] = " + foto.toString());

      // gera um bitmap com a imagem redimensionada em 3x4
      fotos3x4[i] = ManipulaImagem.getScaledBitmap2(fotos[i].getImagem(), 113, 151);

      i++;

    }

    //
    // gera um arquivo com as tr�s foto molduradas
    //
    Bitmap b = processaFotoFormatoCabine3(fotos3x4[0], fotos3x4[1], fotos3x4[2],
        mBitmapMolduraCabine);

    return b;

  }

  /**
   * alteraTamanhoFoto(int largura, int altura, Camera localCamera)
   * 
   * @param largura
   * @param altura
   * @param localCamera
   * 
   */
  private void alteraTamanhoFoto(int largura, int altura, Camera localCamera) {

    // l� os par�metros
    Camera.Parameters params = localCamera.getParameters();

    // altera o tamanho da foto
    params.setPictureSize(largura, altura);

    // Atualiza os par�metros
    localCamera.setParameters(params);

  }

  /**
   * processoEfeitoFiltroFoto(int efeitoFoto)
   * 
   * Altera os par�metros da m�quina respons�vel pela altera��o do efeito da
   * foto.
   * 
   * @param efeitoFoto
   *          "efeito" para aplicar a foto
   * 
   */
  private void processoEfeitoFiltroFoto(int efeitoFoto, Camera localCamera) {

    if (localCamera == null) {
      Log.w(TAG, "processoEfeitoFiltroFoto() - c�mera n�o est� selecionada");
      return;
    }

    // l� os par�metros
    Camera.Parameters params = localCamera.getParameters();

    // altera o tamanho da foto
    params.setPictureSize(640, 480);

    // EFFECT_AQUA 
    // EFFECT_BLACKBOARD 
    // EFFECT_MONO 
    // EFFECT_NEGATIVE 
    // EFFECT_NONE
    // EFFECT_POSTERIZE 
    // EFFECT_SEPIA 
    // EFFECT_SOLARIZE 
    // EFFECT_WHITEBOARD

    if (efeitoFoto == CORES) {

      params.setColorEffect(Camera.Parameters.EFFECT_NONE);

    } else if (efeitoFoto == PB) {

      params.setColorEffect(Camera.Parameters.EFFECT_MONO);

    } else {

      Log.w(TAG, "Efeito: " + efeitoFoto + " n�o � suportado pela aplica��o");

    }

    // Atualiza os par�metros
    localCamera.setParameters(params);

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
  private String gravaFotoPolaroidComMoldura(Bitmap bmFoto, Bitmap bmMoldura) {

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
    // TODO verificar o estado da aplica��o
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

    if (lastUri == null) {
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
   * Verifica se as mensagens devem ser postadas nas redes sociais
   * 
   * Usa mEvento (informa��es sobre o evento).
   * 
   */
  private void sendEmailRedesSociais() {

    Log.w(TAG, "sendEmailRedesSociais() - Inicio do m�todo ...");

    if (mEvento == null) {
      Log.w(TAG, "sendEmailRedesSociais() - N�o foi poss�vel obter os dados do evento.");
      return;
    }

    // TODO talvez pudesse ser feito ap�s o envio do email ???

    Log.d(TAG, "sendEmailRedesSociais() - mEvento.isEnviaFacebook()=" + mEvento.isEnviaFacebook());
    Log.d(TAG, "sendEmailRedesSociais() - mEvento.isEnviaTwitter()=" + mEvento.isEnviaTwitter());

    if (mEvento.isEnviaFacebook()) {

      // enviar foto ao Facebook
      // TODO qual texto ???

      Log.i(TAG, "sendEmailRedesSociais() - Envia foto ao Facebook ...");
      sendMsgFacebook();

    }

    if (mEvento.isEnviaTwitter()) {

      // enviar foto ao Twitter
      // TODO qual texto ?

      Log.i(TAG, "sendEmailRedesSociais() - Envia foto ao Twitter ...");
      sendMsgTwitter();

    }

  }

  /**
   * sendMsgFacebook()
   */
  private void sendMsgFacebook() {

  }

  /**
   * sendMsgTwitter()
   */
  private void sendMsgTwitter() {

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

      // retorna as informa��es sobre o participante a sobre sua
      // participa��o
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
    showClassVariables();

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

    showClassVariables();

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

    showClassVariables();

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
  private Dialog showAlert(String msg) {

    if (msg == null) {
      // retorna se a mensagem for nula
      return null;
    }

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(msg).setNeutralButton("Fechar", null);
    AlertDialog alert = builder.create();
    alert.show();

    return alert;

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

    if (xUri != null) {
      Log.v(TAG, "  xUri=" + xUri);
    }

    Log.v(TAG, "  mEstado=" + mEstado + ", mContador=" + mContador);
    Log.v(TAG, "  mCurrentCamera=" + currentCamera);

    showClassVariables();

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
   * showClassVariables()
   * 
   * Exibe o valor de algumas vari�veis selecionadas
   * 
   */
  private void showClassVariables() {

    Log.v(TAG, "    showClassVariables() - file: " + file);
    Log.v(TAG, "    showClassVariables() - outputFileUri: " + outputFileUri);
    Log.v(TAG, "    showClassVariables() - Contador: " + contador + ", i=" + i);
    Log.v(TAG, "    showClassVariables() - numCreate: " + numCreate + ", numRestart: " + numRestart);

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
   * criaBitmapFotoCabine(FotoCabine pFotoCabine)
   * 
   * Cria os bitmap das foto armazenadas no objeto FotoCabine
   * 
   * @param pFotoCabine
   *          inst�ncia da classe FotoCabine
   */
  private void criaBitmapFotoCabine(FotoCabine pFotoCabine) {

    if (pFotoCabine == null) {
      Log.w(TAG, "showFotoCabine() - fotoCabine � nula");
      return;
    }

    // contador local de fotos processadas
    int i = 0;

    //
    // percorre as fotos armazenada em FotoCabine
    //
    for (Foto foto : pFotoCabine.getFotos()) {

      // exibe informa��es sobre a foto
      Log.v(TAG, "criaBitmapFotoCabine() - processando foto[" + i + "] = " + pFotoCabine.toString());
      Log.d(TAG, "criaBitmapFotoCabine() - processando foto[" + i + "] = " + foto);

      if (foto != null) {

        // decodifica o bitmap referente ao arquivo com a foto
        Bitmap bm = BitmapFactory.decodeFile(foto.getArquivo());

        if (bm == null) {
          Log.d(TAG, "showFotoCabine() - n�o foi poss�vel decodificar a foto a partir do arquivo " + foto.getArquivo()
              + " - bitmap nulo !!!");
        }

        // armazena o bitmap (imagem) na foto
        foto.setImagem(bm);

      }

      i++;

    }

  }

  /**
   * carregaImagem()
   * 
   * Esse m�todo carrega uma imagem (um bitmap) a partir de um arquivo.
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

    // atualiza o n� de fotos carregadas
    numFotosCarregadas++;

    //
    // Aadiciona a nova foto a lista de fotos
    //
    listaFotos.add(foto);

    Log.i(TAG, "carregaImagem() ===> numFotosTiradas: " + numFotosTiradas + ", numFotosCarregadas: " + numFotosCarregadas);

  }

  /**
   * formatarPolaroid(Uri uriFotoOriginal)
   * 
   * Recebe o endere�o de uma foto; redimensiona a foto; insere a moldura e
   * transforma a foto no tamanho 9x11
   * 
   * @return um bitmap contendo a foto com a moldura
   * 
   *         TODO Esse m�todo gera dois arquivos contendo fotos intermedi�rios
   *         que poderiam ser descartadas
   * 
   */
  private Bitmap formatarPolaroid(Uri uriFotoOriginal) {

    Log.d(TAG, "formatarPolaroid() - uriFotoOriginal: " + uriFotoOriginal);

    // Cria um bitmap a partir da Uri da foto
    Bitmap bmFotoOriginal = ManipulaImagem.criaBitmap(uriFotoOriginal);

    if (bmFotoOriginal == null) {
      Log.d(TAG, "formatarPolaroid() - bmFotoOriginal: " + bmFotoOriginal);
      return null;
    }

    // redimensiona a foto original para 9x12 para manter a propor��o 3:4
    Bitmap bmFoto9x12 = ManipulaImagem.getScaledBitmap2(bmFotoOriginal, 340, 454);

    if (bmFoto9x12 == null) {
      Log.d(TAG, "formatarPolaroid() - bmFoto9x12: " + bmFoto9x12);
      return null;
    }

    // Define o nome da foto redimensionada
    String nomeArquivo = PATH_FOTOS + FileUtils.getFilename(uriFotoOriginal) + "_9x12.png";

    boolean gravou = ManipulaImagem.gravaBitmapArquivo(bmFoto9x12, nomeArquivo);

    if (!gravou) {
      Log.d(TAG, "formatarPolaroid() - foto n�o pode ser gravada");
      return null;
    }

    // redimensiona a foto 9x12 para 8x8, isto �, copia uma "janela" 8x8 da
    // foto
    Options options = new Options();

    Rect rect = new Rect(0, 0, 302, 302);

    // Obtem um bitmap com a foto redimensionada para 8x8
    Bitmap bmFoto8x8 = ManipulaImagem.getBitmapRegion(nomeArquivo, rect, options);

    nomeArquivo = PATH_FOTOS + FileUtils.getFilename(uriFotoOriginal) + "_8x8.png";

    Bitmap bmFotoComMoldura = ManipulaImagem.overlay4(bmFoto8x8, mBitmapMolduraPolaroid);

    // o nome completo do arquivo onde a foto com moldura foi armazenada
    return bmFotoComMoldura;

  }

  /**
   * atualizaModoTela(int novaOrientacao)
   * 
   * Atualiza a orienta��o atual da tela para outro modo (se necess�rio).
   * 
   * @param novaOrientacao
   *          nova orienta��o
   */
  private void atualizaModoTela(int novaOrientacao) {

    // obt�m a orienta��o atual da tela
    int orientacaoAtual = this.getResources().getConfiguration().orientation;

    // exibe a orienta��o atual da tela
    Log.d(TAG, "atualizaModoTela() - Orienta��o atual: " + orientacaoAtual + " - " + getScreenOrientation(orientacaoAtual));

    if (novaOrientacao != orientacaoAtual) {
      // muda a orienta��o
      this.setRequestedOrientation(novaOrientacao);
      // exibe a nova orienta��o
      Log.d(TAG, "atualizaModoTela() - nova orienta��o: " + novaOrientacao + " - " + getScreenOrientation(novaOrientacao));
    }

  }

  /**
   * getScreenOrientation(int orientacao)
   * 
   * Retorna uma string com o nome da orienta��o da tela: Landscape ou Portrait
   * 
   * @param orientacao
   *          Configuration.ORIENTATION_LANDSCAPE ou
   *          Configuration.ORIENTATION_PORTRAIT
   * 
   * @return uma string com o nome da orienta��o da tela
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
   * processaFotoFormatoCabine3(Bitmap bmFoto1, Bitmap bmFoto2, Bitmap bmFoto3,
   * Bitmap bmMoldura)
   * 
   * @param bmFoto1
   *          primeira foto
   * @param bmFoto2
   *          segunda foto
   * @param bmFoto3
   *          terceira foto
   * @param bmMoldura
   *          bitmap contendo a moldura
   * 
   * @return um bitmap contendo as tr�s fotos e a moldura
   */
  private static Bitmap processaFotoFormatoCabine3(Bitmap bmFoto1, Bitmap bmFoto2, Bitmap bmFoto3, Bitmap bmMoldura) {

    if (bmMoldura == null) {
      Log.w(TAG, "processaFotoFormatoCabine3() - Moldura formato Cabine est� vazia !");
      return null;
    }

    Bitmap bmImgJoin = mergeFotosCabine(bmFoto1, bmFoto2, bmFoto3);

    if (bmImgJoin == null) {
      Log.w(TAG, "processaFotoFormatoCabine3() - n�o foi poss�vel fazer o merge das tr�s fotos !");
      return null;
    }

    // Obt�m uma imagem redimensionada
    Bitmap scaledBitmap = ManipulaImagem.getScaledBitmap2(bmImgJoin, 113, 453);

    if (scaledBitmap == null) {
      //
      Log.w(TAG, "processaFotoFormatoCabine3() - n�o foi poss�vel redimensionar a foto");
      return null;
    }

    // combina a foto com a moldura
    Bitmap fotoComMoldura = ManipulaImagem.aplicaMolduraFoto(scaledBitmap, bmMoldura);

    return fotoComMoldura;

    /*
     * String arqSaida = PATH_FOTOS + "xxx3-join-moldura.png";
     * 
     * boolean gravouImagemComMoldura =
     * ManipulaImagem.gravaBitmapArquivo(fotoComMoldura, arqSaida);
     * 
     * if (gravouImagemComMoldura) {
     * 
     * // imagem escalonada gravada com sucesso Log.v(TAG,
     * "processaFotoFormatoCabine2() - Imagem com moldura gravada com sucesso no arquivo "
     * + arqSaida); } else { Log.v(TAG,
     * "processaFotoFormatoCabine2() - Falha na grava��o da imagem com moldura no arquivo: "
     * + arqSaida); return null; }
     * 
     * return arqSaida;
     */

  }

  /**
   * mergeFotosCabine(Bitmap bmFoto1, Bitmap bmFoto2, Bitmap bmFoto3)
   * 
   * Uma um novo bitmap a partir de tr�s fotos recebidas e uma moldura.
   * 
   * @param bmFoto1
   *          primeira foto
   * @param bmFoto2
   *          segunda foto
   * @param bmFoto3
   *          terceira foto
   * 
   * @return o bitmap contendo as tr�s fotos e a moldura ou null em caso de
   *         algum erro
   */
  private static Bitmap mergeFotosCabine(Bitmap bmFoto1, Bitmap bmFoto2, Bitmap bmFoto3) {

    if (bmFoto1 == null) {
      Log.w(TAG, "processaFotoFormatoCabine3() - Foto1 est� vazia !");
      return null;
    }

    if (bmFoto2 == null) {
      Log.w(TAG, "processaFotoFormatoCabine3() - Foto2 est� vazia !");
      return null;
    }

    if (bmFoto3 == null) {
      Log.w(TAG, "processaFotoFormatoCabine3() - Foto3 est� vazia !");
      return null;
    }

    // =========================================================================
    // Cria um novo bitmap a partir da composi��o das 3 fotos
    // A foto ser� repetida na vertical, isto �, uma nova foto
    // ser� colocada abaixo da outra.
    // =========================================================================
    Bitmap bmImgJoin = ManipulaImagem.verticalJoin(bmFoto1, bmFoto2, bmFoto3);

    if (bmImgJoin != null) {

      Log.i(TAG, "processaFotoFormatoCabin3() - Imagens foram juntadas com sucesso");
      Log.v(TAG, "processaFotoFormatoCabine3() -  ==> Tamanho da foto ap�s join: " + ManipulaImagem.getStringBitmapSize(bmImgJoin));

    } else {

      Log.w(TAG, "processaFotoFormatoCabine3() - Erro no merge das tr�s fotos");
      return null;

    }

    return bmImgJoin;

  }

}
