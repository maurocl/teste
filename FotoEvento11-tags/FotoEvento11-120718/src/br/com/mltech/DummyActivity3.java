package br.com.mltech;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.mail.internet.InternetAddress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
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
import br.com.mltech.utils.Transaction;
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
public class DummyActivity3 extends Activity implements Constantes, Transaction {

  // ------------------
  // constantes
  // ------------------

  private static final String TAG = "DummyActivity3";

  // -------------------
  // vari�veis da classe
  // -------------------

  // indica qual Activity ser� executada:
  // 0 = tira foto dummy;
  // 1 = Tira foto normal (usando a aplica��o Android);
  // 2 = tira foto usando uma aplica��o de c�mera customizada;
  private static int FLAG = 2;

  // indica qual a forma ser� usada para envio do email ao participante
  // 0 = atrav�s do uso de activities
  // 1 = atraves do uso de sistema de email externo (JavaMail)
  private static int EMAIL_TIPO = 1;

  // inst�ncia de um arquivo
  private static File file;

  // URI onde o ser� colocada a foto
  private static Uri outputFileUri;

  // Lista de todas as fotos tiradas
  private static List<Foto> listaFotos;

  // Foto
  private static Foto foto;

  // FotoCabine
  private static FotoCabine fotoCabine;

  // Defini��o dos atributos da aplica��o
  private static Contratante mContratante;

  // Um evento
  private static Evento mEvento;

  // Um participante
  private static Participante mParticipante;

  // uma participa��o
  private static Participacao mParticipacao;

  private static SharedPreferences mPreferences;

  // Uri da �ltima foto
  // private static Uri xUri;

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

  // servidor de email
  private static Mail mailServer = null;

  // modo da tela (portrait / landscape)
  private static int modoTela;

  // inst�ncia do Logger
  // private static br.com.mltech.utils.Logger logger;

  // inst�ncia do Logger 2
  private static Logger logger2 = Logger.getLogger("br.com.mltech");

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

    // Inicia o log da activity
    iniciaLogger();

    Log.d(TAG, "*** onCreate() ***");
    logger2.finest("*** onCreate() ***");

    setContentView(R.layout.dummy);

    if (savedInstanceState != null) {
      // FileUtils.showBundle(savedInstanceState);
    }

    // guarda a orienta��o inicial da tela
    modoTela = this.getResources().getConfiguration().orientation;
    Log.d(TAG, "onCreate() - Orienta��o inicial da tela: " + getScreenOrientation(modoTela) + "(" + modoTela + ")");
    logger2.info("onCreate() - Orienta��o inicial da tela: " + getScreenOrientation(modoTela) + "(" + modoTela + ")");

    // L� as configura��es a respeito da conta de email
    try {

      initEmailConfig();

    } catch (IllegalArgumentException e) {

      Log.w(TAG, "onCreate() - email n�o est� configurado adequadamente");
      logger2.info("onCreate() - email n�o est� configurado adequadamente");

    }

    // Carrega as molduras para fotos Polaroid e Cabine

    try {
      carregaMolduras();
    } catch (IllegalArgumentException e) {
      Log.w(TAG, "onCreate() - molduras n�o foram configuradas adequadamente.");
      logger2.info("onCreate() - molduras n�o foram configuradas adequadamente.");
    }

    // Obtem o identificado da c�mera que ser� usada para tirar as fotos
    currentCamera = obtemIdentificadorCamera();

    // ---------------------------------------------------------------------

    // incrementa o n� de vezes que a activity foi reiniciada
    mContador++;

    contador++;
    numCreate++;
    i++;

    Button btnIniciar = (Button) findViewById(R.id.btn0);

    btnIniciar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        iniciaMaquinaDeEstados();

      }

    });

  }

  /**
   * onStart() - inicio da activity.
   */
  @Override
  protected void onStart() {

    super.onStart();
    Log.d(TAG, "*** onStart() ***");
    logger2.finest("*** onStart() ***");
    showVariables();

  }

  /**
   * onResume()
   */
  @Override
  protected void onResume() {

    super.onResume();

    // cria o sistema de log em disco
    /*
     * try { logger = new
     * br.com.mltech.utils.Logger("/mnt/sdcard/Pictures/logger.log", true); }
     * catch (IOException e1) { Log.w(TAG,
     * "onCreate() - falha na abertura do sistema de logs", e1);
     * 
     * }
     */

    Log.d(TAG, "*** onResume() ***");
    logger2.finest("*** onResume() ***");
    showVariables();

  }

  /**
   * onSaveInstanceState(Bundle outState)
   */
  @Override
  protected void onSaveInstanceState(Bundle outState) {

    super.onSaveInstanceState(outState);
    Log.d(TAG, "*** onSaveInstanceState() ***");

    logger2.info("*** onSaveInstanceState() ***");

    outState.putSerializable(PARTICIPANTE, mParticipante);
    outState.putSerializable(PARTICIPACAO, mParticipacao);

    showClassVariables();

    // FileUtils.showBundle(outState);

  }

  /**
   * onPause()
   */
  @Override
  protected void onPause() {

    super.onPause();
    Log.d(TAG, "*** onPause() ***");
    logger2.info("*** onPause() ***");
    showVariables();

    // logger.close();

  }

  /**
   * onStop()
   */
  @Override
  protected void onStop() {

    super.onStop();
    Log.d(TAG, "*** onStop() ***");

    logger2.info("*** onStop() ***");

    showVariables();

    // logger.close();

  }

  /**
   * Aplica��o foi reinicializada.<br>
   * 
   * Por qual motivo a aplica��o foi reinicializada ???
   * 
   */
  @Override
  protected void onRestart() {

    super.onRestart();

    contador++;
    i++;

    numRestart++;

    showClassVariables();

    showVariables();

    Log.i(TAG, "*");
    Log.i(TAG, "*********************************************************************");
    Log.i(TAG, "*** onRestart() - A aplica��o foi restartada (" + numRestart + ") ...");
    Log.i(TAG, "*********************************************************************");
    Log.i(TAG, "*");

    logger2.warning("*** onRestart() - A aplica��o foi restartada (" + numRestart + ") ...");

  }

  /**
   * onRestoreInstanceState(Bundle savedInstanceState)
   */
  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {

    super.onRestoreInstanceState(savedInstanceState);

    logger2.info("*** onRestoreInstanceState() ***");

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

    // FileUtils.showBundle(savedInstanceState);

    showClassVariables();

  }

  /**
   * Inicia o sistema de log da activity.<br>
   * 
   * Usa o atributo de classe logger2.
   * 
   */
  private void iniciaLogger() {

    FileHandler fh = null;

    try {
      // cria um arquivo onde ser� armazenado os logs de execu��o da aplica��o
      fh = new FileHandler(APP_LOG, true);
    } catch (IOException e1) {
      Log.w(TAG, "onCreate() - n�o foi poss�vel criar o arquivo de log.", e1);
    }

    if (fh != null) {
      fh.setFormatter(new SimpleFormatter());
    }

    // associa um FileHandler ao logger.
    logger2.addHandler(fh);

    Handler[] xxx = logger2.getHandlers();
    if (xxx != null) {
      Log.d(TAG, "iniciarLogger() - n� de handlers: " + xxx.length);
      for (int i = 0; i < xxx.length; i++) {
        Handler h = xxx[i];
        Log.d(TAG, "iniciarLogger() - i=" + i + ", h=" + h);
      }
    }

    // Estabelece o n�vel do log
    logger2.setLevel(Level.FINEST);

    logger2.info("");

  }

  /**
   * Inicializa as vari�veis (da classe) que v�o conter o bitmap das molduras:<br>
   * 
   * mBitmapMolduraPolaroid.<br>
   * mBitmapMolduraCabine.<br>
   * <br>
   * molduraPolaroid<br>
   * molduraCabine<br>
   * 
   */
  private void carregaMolduras() throws IllegalArgumentException {

    mPreferences = getSharedPreferences("preferencias", MODE_PRIVATE);

    if (mPreferences == null) {

      Log.w(TAG, "carregaMolduras() - mPreferences � nula. Falha na execu��o do comandos getSharedPreferences()");
      throw new IllegalArgumentException();

    }

    // o arquivo de configura��o possui a informa��o da localiza��o das
    // molduras

    // Obt�m o arquivo contendo o bitmap da moldura formato polaroid
    String arquivoMolduraPolaroid = mPreferences.getString(Constantes.EVENTO_BORDA_POLAROID, null);

    // Obt�m o arquivo contendo o bitmap da moldura formato cabine
    String arquivoMolduraCabine = mPreferences.getString(Constantes.EVENTO_BORDA_CABINE, null);

    if (arquivoMolduraPolaroid == null) {
      // n�o foi poss�vel encontrar a vari�vel Constantes.EVENTO_BORDA_POLAROID
      // no arquivo de configura��o
      Log.w(TAG, "carregaMolduras() - moldura formato Polaroid n�o foi configurada.");
      logger2.warning("carregaMolduras() - moldura formato Polaroid n�o foi configurada.");
      return;
    }

    if (arquivoMolduraCabine == null) {
      // n�o foi poss�vel encontrar a vari�vel Constantes.EVENTO_BORDA_CABINE no
      // arquivo de configura��o
      Log.w(TAG, "carregaMolduras() - moldura formato Cabine n�o foi configurada.");
      logger2.warning("carregaMolduras() - moldura formato Cabine n�o foi configurada.");
      throw new IllegalArgumentException();
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

    // cria uma inst�ncia da classe Moldura - Polaroid
    molduraPolaroid = new Moldura(arquivoMolduraPolaroid, "Moldura Polaroid 1");
    molduraPolaroid.setImagem(mBitmapMolduraPolaroid);

    // cria uma inst�ncia da classe Moldura - Cabine
    molduraCabine = new Moldura(arquivoMolduraCabine, "Moldura Cabine 1");
    molduraCabine.setImagem(mBitmapMolduraCabine);

  }

  /**
   * Verifica se existe uma configura��o explicita para usar um determinado
   * identificador de c�mera (se o sistema possuir mais de uma c�mera). Esse
   * recurso � usado quando existe mais de uma c�mera suportada pelo
   * dispositivo, como, por exemplo, uma c�mera frontal que se deseja usar ao
   * inv�s da c�mera traseira.
   * 
   * @return o identificador da c�mera frontal ou 0 caso esse par�metro n�o
   *         esteja cadastrado
   * 
   */
  private int obtemIdentificadorCamera() {

    // -----------------------------------------------
    // obtem o identificador da c�mera usada para tirar as fotos
    // -----------------------------------------------
    String sCameraId = getSharedPreference(Constantes.PREF_EMAIL, Constantes.PREFERENCIAS_NUM_CAMERA_FRONTAL);

    Log.i(TAG, "obtemIdentificadorCamera() - Id da c�mera frontal=" + sCameraId);

    // identificador (Id) da c�mera
    int id = 0;

    if ((sCameraId != null) && (!sCameraId.equals(""))) {

      id = Integer.valueOf(sCameraId);

    } else {

      id = 0;
      Log.w(TAG, "obtemIdentificadorCamera() - Identificador da c�mera frontal n�o foi definido. Assumindo o valor 0");

    }

    // o identificador da c�mera
    return id;

  }

  /**
   * L� as configura��es para envio de email usando uma conta externa.<br>
   * <br>
   * <u>IMPORTANTE</u>: Usa a vari�vel membro: <b>mailServer.</b><br>
   * <br>
   * 
   * usuario, senha, host, porta, remetente
   * 
   */
  private void initEmailConfig() throws IllegalArgumentException {

    Log.d(TAG, "initEmailConfig() - inicio");
    logger2.finer("initEmailConfig() - in�cio");

    String mEmail = getSharedPreference(Constantes.PREF_EMAIL, Constantes.PREFERENCIAS_USUARIO_EMAIL);
    String mSenha = getSharedPreference(Constantes.PREF_EMAIL, Constantes.PREFERENCIAS_SENHA_EMAIL);

    String mHost = getSharedPreference(Constantes.PREF_EMAIL, Constantes.PREFERENCIAS_SERVIDOR_SMTP);
    String mPort = getSharedPreference(Constantes.PREF_EMAIL, Constantes.PREFERENCIAS_SERVIDOR_SMTP_PORTA);

    String mRemetente = getSharedPreference(Constantes.PREF_EMAIL, Constantes.PREFERENCIAS_REMETENTE_EMAIL);

    String mEmailDebug = getSharedPreference(Constantes.PREF_EMAIL, Constantes.PREFERENCIAS_EMAIL_DEBUG);
    String mEmailAuth = getSharedPreference(Constantes.PREF_EMAIL, Constantes.PREFERENCIAS_EMAIL_AUTH);
    String mEmailSsl = getSharedPreference(Constantes.PREF_EMAIL, Constantes.PREFERENCIAS_EMAIL_SSL);

    // cria a inst�ncia do sistema de email
    mailServer = new Mail(mEmail, mSenha);

    Log.d(TAG, "initEmailConfig() - cria a inst�ncia de mailServer");
    logger2.finer("initEmailConfig() - cria a inst�ncia de mailServer");

    mailServer.setHost(mHost);
    mailServer.setPort(mPort);
    mailServer.setFrom(mRemetente);
    mailServer.setDebuggable((mEmailDebug.equalsIgnoreCase("true")) ? true : false);
    mailServer.setAuth((mEmailAuth.equalsIgnoreCase("true")) ? true : false);
    mailServer.setSsl((mEmailSsl.equalsIgnoreCase("true")) ? true : false);

    Log.d(TAG, "initEmailConfig() - SMTP host: " + mailServer.getHost());
    Log.d(TAG, "initEmailConfig() - SMTP port: " + mailServer.getPort());
    Log.d(TAG, "initEmailConfig() - from: " + mailServer.getFrom());
    Log.d(TAG, "initEmailConfig() - debuggable: " + mailServer.isDebuggable());
    Log.d(TAG, "initEmailConfig() - authorization: " + mailServer.isAuth());
    Log.d(TAG, "initEmailConfig() - SSL: " + mailServer.isSsl());

    // valida os argumentos obrigat�rios do email
    validaArgumentosEmail(mEmail, mSenha, mailServer.getHost(), mailServer.getPort(), mailServer.getFrom());

    Log.d(TAG, "initEmailConfig() - fim");
    logger2.finer("initEmailConfig() - fim");

  }

  /**
   * Inicia o processo do participante do evento.
   */
  private void iniciaMaquinaDeEstados() {

    boolean sucesso = iniciaVariaveis();

    if (!sucesso) {
      Log.w(TAG, "iniciaProcesso() - N�o foi poss�vel inicializar as vari�veis");
      estadoFinal();
    }

  }

  /**
   * Inicia a execu��o da m�quina de estados da activity.<br>
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
    if (i.hasExtra(CONTRATANTE)) {
      mContratante = (Contratante) i.getSerializableExtra(CONTRATANTE);
    } else {
      Log.w(TAG, "iniciaVariaveis() - Contratante n�o pode ser nulo.");
      Toast.makeText(this, "Contratante n�o pode ser nulo", Toast.LENGTH_SHORT).show();
      erro = 1;
    }

    // Obtem informa��es sobre o Evento
    if (i.hasExtra(EVENTO)) {
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
   * Obt�m informa��es sobre o participante do evento<br>
   * 
   * Inicia a Activity Participante. Passa como par�metro as informa��es sobre o
   * Evento.<br>
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
   * Obt�m uma inst�ncia de um Participante como retorno da execu��o da
   * activity.<br>
   * 
   * @param resultCode
   *          resultado da execu��o da activity Participante
   * 
   * @param data
   *          Intent com os resultados (se houverem)
   * 
   */
  private void resultActivityParticipante(int resultCode, Intent data) {

    Log.d(TAG, "resultActivityParticipante() ==> processando resultado da ACTIVITY PARTICIPANTE");

    if (resultCode == RESULT_CANCELED) {

      // opera��o cancelada
      Log.d(TAG, "resultCode=RESULT_CANCELED - Participante cancelou sua participa��o");

      // Limpa as informa��es do participante
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

    // atualiza a vari�vel com os dados do participante
    if (data.hasExtra(PARTICIPANTE)) {
      mParticipante = (Participante) data.getSerializableExtra(PARTICIPANTE);
      // Exibe as informa��es sobre o participante e sua participa��o
      Log.d(TAG, "resultActivityParticipante() - mParticipante=" + mParticipante);

    }

    // inicia a activity Participa��o
    startActivityParticipacao();

    // obs: nesse ponto j� sabemos qual ser� o tipo da foto

    // Atualiza o estado da m�quina de estados
    setEstado(1);

  }

  /**
   * A activity participa��o pega as informa��es de um participante e escolhe o
   * tipo e efeito da foto.
   * 
   */
  private void startActivityParticipacao() {

    // Cria uma nova Intent para chamar a Activity Participacao
    Intent intentParticipacao = new Intent(this, ParticipacaoActivity.class);

    // Inclui o par�metro mEvento (com as informa��es sobre o evento em
    // curso)
    intentParticipacao.putExtra(EVENTO, mEvento);
    intentParticipacao.putExtra(PARTICIPANTE, mParticipante);

    // Inicia a Activity
    startActivityForResult(intentParticipacao, ACTIVITY_PARTICIPACAO);

  }

  /**
   * resultActivityParticipacao(int resultCode, Intent data)
   * 
   * Processa o resultado da execu��o da activity Participa��o.
   * 
   * @param resultCode
   *          C�digo de retorno da execu��o da activity
   * 
   * @param data
   *          intent com os dados retornados
   * 
   */
  private void resultActivityParticipacao(int resultCode, Intent data) {

    Log.d(TAG, "resultActivityParticipacao() ==> processando resultado da ACTIVITY PARTICIPACAO");

    if (resultCode == RESULT_CANCELED) {

      // opera��o cancelada

      Log.d(TAG, "resultCode=RESULT_CANCELED - Participa��o foi cancelada.");

      // Limpa as vari�veis
      mParticipacao = null;

      return;

    } else if (resultCode != RESULT_OK) {

      // o resultado execu��o da activity n�o � conhecido
      Log.w(TAG, "resultActivityParticipacao() - resultCode n�o conhecido: " + resultCode);
      return;

    }

    if (data == null) {
      // caso a Intent n�o retorne nada houve algum problema
      Log.w(TAG, "resultActivityParticipacao() - A intent n�o retornou nenhuma informa��o");
      return;
    }

    // exibe a lista de valores retornados
    FileUtils.showBundle(data.getExtras());

    // atualiza a participacao
    if (data.hasExtra(PARTICIPACAO)) {
      mParticipacao = (Participacao) data.getSerializableExtra(PARTICIPACAO);
      // Exibe as informa��es sobre o participante e sua participa��o
      Log.d(TAG, "resultActivityParticipacao() - mParticipacao=" + mParticipacao);
    }

    // Atualiza o estado da m�quina de estados
    setEstado(1);

    // Processa o pr�ximo estado
    tirarFotos();

  }

  /**
   * Tira as fotos de acordo com a solicita��o do participante.<br>
   * 
   * Nesse ponto o usu�rio j� forneceu suas informa��es pessoais e agora �
   * necess�rio tirar a(s) foto(s).<br>
   * 
   * Esse m�todo depende dos valores das vari�veis:<br>
   * mParticipante, mParticipa��o, mEvento.<br>
   * 
   * A aplica��o prepara-se para tirar as fotos de acordo com a escolha do
   * participante. Poder� ser do tipo Polaroid ou Cabine.<br>
   * 
   * Cada escolha ir� disparar uma nova activity encarregada pela obten��o das
   * fotos.
   * 
   */
  private void tirarFotos() {

    logger2.finest("tirarFotos() - inicio");

    // valida o participante
    if (mParticipante == null) {
      Log.d(TAG, "tirarFotos() - n�o � poss�vel obter as informa��es do participante");
      estadoFinal();
    }

    // valida a participa��o
    if (mParticipacao == null) {
      Log.d(TAG, "tirarFotos() - n�o � poss�vel obter as informa��es da participa��o");
      estadoFinal();
    }

    // valida o evento
    if (mEvento == null) {
      Log.d(TAG, "tirarFotos() - n�o � poss�vel obter as informa��es sobre o evento");
      estadoFinal();
    }

    // obt�m o tipo da foto (se o formato da foto � Polaroid ou Cabine)
    int tipoFoto = mParticipacao.getTipoFoto();

    Log.i(TAG, "------------------------------------------------------------");
    Log.i(TAG, "==> tirarFotos() - tipoFoto: " + tipoFoto);
    Log.i(TAG, "------------------------------------------------------------");

    logger2.finest("tirarFotos() - tipoFoto: " + tipoFoto);

    if (tipoFoto == TIPO_FOTO_POLAROID) {

      // --------------------
      // TIPO_FOTO_POLAROID
      // --------------------

      // gera um nome para o arquivo onde a foto ser� armazenada
      String arquivo = FileUtils.obtemNomeArquivo(".png").getAbsolutePath();

      Log.i(TAG, "tirarFotos() - tipoFoto: POLAROID, arquivo: " + arquivo);
      logger2.info("tirarFotos() - tipoFoto: POLAROID, arquivo: " + arquivo);

      // executa a activity respons�vel por tirar uma foto e format�-la como
      // Polaroid.
      executaActivityTiraFotoPolaroid(arquivo);

    } else if (tipoFoto == TIPO_FOTO_CABINE) {

      // --------------------
      // TIPO_FOTO_CABINE
      // --------------------

      Log.i(TAG, "tirarFotos() - tipoFoto: CABINE");
      logger2.info("tirarFotos() - tipoFoto: CABINE");

      // executa a activity respons�vel por tirar uma tr�s foto e format�-las
      // como Cabine.
      executaActivityTiraFotoCabine();

    } else {

      Log.w(TAG, "tirarFotos() - tipo de foto: " + tipoFoto + " n�o suportado.");
      estadoFinal();

    }

  }

  /**
   * Cria uma intent para solicitar uma foto a uma activity.<br>
   * 
   * <br>
   * Observe que a vari�vel FLAG � usada para "escolher" a activity que ser�
   * executada e que ir� retornar a foto.
   * 
   * @return uma Intent (mensagem) para execu��o da activity desejada.
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

    } else if (FLAG == 2) {

      // tira uma foto verdadeira usando a activity CameraActivity
      intent = new Intent(this, CameraActivity.class);

      intent.putExtra(Constantes.PARTICIPACAO, mParticipacao);

      intent.putExtra("br.com.mltech.cameraId", currentCamera);
      intent.putExtra("br.com.mltech.outputFileUri", outputFileUri);

    }

    Log.d(TAG, "getIntentTirarFoto() -  " + intent);
    Log.d(TAG, "getIntentTirarFoto() - outputFileUri: " + outputFileUri);

    return intent;

  }

  /**
   * Inicia a activity respons�vel por tirar uma foto e format�-la como
   * polaroid.<br>
   * 
   * @param arquivo
   *          nome do arquivo onde a foto original tirada pela c�mera ser�
   *          armazenada.
   * 
   */
  private void executaActivityTiraFotoPolaroid(String arquivo) {

    logger2.info("executaActivityTiraFotoPolaroid() - arquivo=" + arquivo);

    Log.d(TAG, "executaActivityTiraFotoPolaroid() - arquivo=" + arquivo);

    file = new File(arquivo);

    outputFileUri = Uri.fromFile(file);

    // obt�m a intent respons�vel pela obten��o da foto
    Intent intent = getIntentTirarFoto();

    // inicia a activity selecionada
    startActivityForResult(intent, TIRA_FOTO_POLAROID);

  }

  /**
   * Processa o resultado da execu��o da activity respons�vel por fornecer uma
   * foto.
   * 
   * Pega a foto retornada e formata-a no formato Polaroid.<br>
   * 
   * @param resultCode
   *          resultado da execu��o da activity
   * 
   * @param data
   *          intent contendo os dados retornados (se houver)
   * 
   *          Use a vari�vel de classe: file
   * 
   */
  private String processaActivityResultPolaroid(int resultCode, Intent data) {

    Toast.makeText(this, "Processando foto Polaroid ...", Toast.LENGTH_LONG).show();

    logger2.info("Processando foto Polaroid ...");

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

      outputFileUri = data.getData();

      Log.d(TAG, "processaActivityResultPolaroid() - outputFileUri: " + outputFileUri);

      logger2.info("processaActivityResultPolaroid() - outputFileUri: " + outputFileUri);

    } else {

      // n�o houve retorno de dados
      Log.w(TAG, "processaActivityResultPolaroid - data (Intent) � vazia");

      outputFileUri = null;

    }

    // exibe a URI contendo a foto
    Log.i(TAG, "processaActivityResultPolaroid() - outputFileUri: " + outputFileUri);

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
    meuArquivo2 = FileUtils.getBasePhotoDirectory() + File.separator + meuArquivo + ".jpg";

    // meuArquivo2 = FileUtils.obtemNomeArquivo("");

    Log.d(TAG, "meuArquivo=" + meuArquivo);
    Log.d(TAG, "meuArquivo2=" + meuArquivo2);

    // -------------------------------------------------------------------------------
    Log.d(TAG, "carregaImagem()");

    // atualiza o n� de fotos carregadas
    numFotosCarregadas++;

    //
    // Adiciona a nova foto a lista de fotos
    //
    listaFotos.add(foto);

    Log.i(TAG, "carregaImagem() ===> numFotosTiradas: " + numFotosTiradas + ", numFotosCarregadas: " + numFotosCarregadas);

    // -------------------------------------------------------------------------------

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
    logger2.info("processaActivityResultPolaroid - FIM");

    // o nome do arquivo onde a foto Polaroid foi gravada.
    return meuArquivo2;

  }

  /**
   * executaActivityTiraFotoCabine()
   * 
   * Activity respons�vel por obter as tr�s fotos que ir�o compor a foto no
   * formato cabine.
   * 
   */
  private void executaActivityTiraFotoCabine() {

    Log.i(TAG, "==>");
    Log.i(TAG, "==> executaActivityTiraFotoCabine() - indiceFoto: " + indiceFoto);
    Log.i(TAG, "==>");

    // TODO melhorar a constru��o abaixo
    // gera o nome de um arquivo onde ser� armazenada a foto
    String arquivo = (FileUtils.obtemNomeArquivo(".png")).getAbsolutePath();

    // cria um File usando o nome do arquivo gerado
    file = new File(arquivo);

    Log.d(TAG, "==>");
    Log.d(TAG, "==> executaActivityTiraFotoCabine() - arquivo=" + file.getAbsolutePath());
    Log.d(TAG, "==> ");

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
   * Esta rotina � respons�vel por obter tr�s foto da c�mera.<br>
   * A vari�vel contadorCabine controla o n� de fotos (varia de 0 a 2).<br>
   * 
   * @param resultCode
   *          resultado da execu��o da activity
   * 
   * @param data
   *          dados retornados da execu��o da activity
   * 
   */
  private String processaActivityResultCabine(int resultCode, Intent data) {

    Log.i(TAG, "processaActivityResultCabine() - Inicio do m�todo");

    Bitmap meuBitmap = null;

    if (resultCode == RESULT_OK) {

      // activity executada com sucesso
      Log.i(TAG, "processaActivityResultCabine() -------------------");
      Log.i(TAG, "processaActivityResultCabine() - indiceFoto=" + indiceFoto);
      Log.i(TAG, "processaActivityResultCabine() -------------------");

      if (data != null) {

        outputFileUri = data.getData();

        Log.i(TAG, "processaActivityResultCabine() - outputFileUri: " + outputFileUri);

        // gera o bitmap com a foto
        Bitmap b = ManipulaImagem.criaBitmap(outputFileUri);

        // cria uma inst�ncia da classe Foto
        // fornecendo o nome do arquivo onde a foto
        // deve ser armazenada e o bitmap contendo a foto
        Foto foto = new Foto(file.getAbsolutePath(), b);

        try {

          // grava a foto
          foto.gravar();

          Log.v(TAG, "processaActivityResultCabine() - Foto: " + file.getAbsolutePath() + " gravada com sucesso");
          Log.d(TAG, "processaActivityResultCabine() - Foto: " + file.getPath() + " gravada com sucesso");
          Log.d(TAG, "processaActivityResultCabine() - Foto: " + file.getName() + " gravada com sucesso");

        } catch (FileNotFoundException e) {
          Log.w(TAG, "processaActivityResultCabine() - Arquivo n�o encontrado", e);

        } catch (IOException e) {
          Log.w(TAG, "processaActivityResultCabine() - Exce��o de I/O lan�ada: ", e);

        }

        // armazena a foto no �ndice dado pela vari�vel indiceFoto no array de
        // fotos do objeto FotoCabine
        fotoCabine.setFoto(indiceFoto, foto);

      }

      // incrementa o �ndice usado para armazenar a foto obtida no array de
      // fotos
      indiceFoto++;

      if (indiceFoto < 3) {

        // obtem uma outra foto
        executaActivityTiraFotoCabine();

      } else {

        // tr�s foto j� foram obtidas (tiradas)
        // obt�m um bitmap composto pelas tr�s fotos
        meuBitmap = fimExecucaoActivityTiraFotoCabine();

        if (meuBitmap == null) {
          Log.w(TAG, "processaActivityResultCabine() - bitmap retornado � nulo");
        }

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

    if (indiceFoto >= 3) {

      if (meuBitmap == null) {
        Log.w(TAG, "processaActivityResultCabine() - n�o foi poss�vel criar o bitmap");
        return null;
      }

      // cria um novo arquivo para guardar a foto processada
      String arquivo = (FileUtils.obtemNomeArquivo(".png")).getAbsolutePath();

      // cria uma inst�ncia de Foto para guardar a foto processada, isto �, a foto no formato cabine j� com a moldura.
      Foto fotoProcessada = new Foto(arquivo, meuBitmap);

      boolean gravouFotoProcessada = false;

      try {

        // grava a foto
        gravouFotoProcessada = fotoProcessada.gravar();

        if (gravouFotoProcessada == false) {
          Log.w(TAG, "processaActivityResultCabine() - fotoProcessada n�o foi gravada !!!");
        }

      } catch (FileNotFoundException e) {
        Log.w(TAG, "processaActivityResultCabine() - arquivo n�o foi encontrado", e);

      } catch (IOException e) {
        Log.w(TAG, "processaActivityResultCabine() - erro de I/O", e);

      }

      String s = null;

      // retorna o nome do arquivo onde a foto foi armazenada
      if ((fotoProcessada != null) && (gravouFotoProcessada == true)) {
        s = fotoProcessada.getArquivo();
      }

      // retorna o nome do arquivo contendo a foto processada ou null em caso de erro.
      return s;
      
    }
    else {
      return null;
    }

  

  }

  /**
   * Processa as tr�s foto tiradas para montar uma foto formato cabine.<br>
   * Cada foto � convertida para o tamanho 3x4.<br>
   * 
   * Usa a vari�vel fotoCabine
   * 
   * @return uma inst�ncia de Foto ou null em caso de algum problema.
   */
  private Bitmap fimExecucaoActivityTiraFotoCabine() {

    // Ap�s tirar as tr�s fotos � necess�rio:
    // - converter as foto para o formato 3x4
    // - montar a foto formato cabine juntando as fotos 3x4 e a moldura
    //
    // fotoCabine.getFoto[0] --> Foto
    // fotoCabine.getFoto[1] --> Foto
    // fotoCabine.getFoto[2] --> Foto

    Log.d(TAG, "fimExecucaoActivityTiraFotoCabine() - inicio");

    if (fotoCabine == null) {
      Log.w(TAG, "fimExecucaoActivityTiraFotoCabine() - fotoCabine � nula");
      return null;
    }

    // Exibe o estado da inst�ncia da classe FotoCabine
    Log.d(TAG, fotoCabine.toString());

    Log.d(TAG, "fimExecucaoActivityTiraFotoCabine() - agora � montar a foto cabine ...");

    // monta uma foto do tipo cabine
    Bitmap meuBitmap = montaFotoCabine(fotoCabine);

    if (meuBitmap == null) {
      Log.w(TAG, "fimExecucaoActivityTiraFotoCabine() - bitmap retornado � nulo");
    }

    return meuBitmap;

  }

  /**
   * Cria uma foto com moldura cabine de tamanho 3.5 x 15.0 cm a partir de tr�s
   * fotos 3x4 e uma moldura.<br>
   * 
   * @param fotoCabine
   *          Inst�ncia da classe FotoCabine
   * 
   * @return Bitmap contendo a foto formatada ou null em caso de erro
   * 
   * @throws IOException
   * 
   * @throws FileNotFoundException
   * 
   */
  private Bitmap montaFotoCabine(FotoCabine fotoCabine) {

    if (fotoCabine == null) {
      // fotoCabine n�o existe.
      Log.w(TAG, "montaFotoCabine() - fotoCabine est� vazia");
      return null;
    }

    // cria um array contendo refer�ncias a tr�s inst�ncias de Bitmap
    Bitmap[] fotosRedimesionadas = new Bitmap[3];

    // o array fotos possui uma posi��o para cada foto
    // preenche o array com as fotos armazenadas na fotoCabine
    Foto[] fotos = fotoCabine.getFotos();

    //
    // processa cada uma das fotos e faz o redimensionamento
    // para o tamanho 3x4
    //
    for (int i = 0; i < 3; i++) {

      Log.i(TAG, "montaFotoCabine() - foto[" + i + "] = " + fotos[i]);

      // transforma cada foto em 3x4
      // gera um bitmap com a imagem redimensionada em 3x4
      fotosRedimesionadas[i] = ManipulaImagem.getScaledBitmap2(fotos[i].getImagem(), 113, 151);

    }

    //
    // gera um �nico bitmap a partir das tr�s foto 3x4 e inclui a moldura
    //
    Bitmap bitmap = processaFotoFormatoCabine3(fotosRedimesionadas[0], fotosRedimesionadas[1], fotosRedimesionadas[2],
        mBitmapMolduraCabine);

    if (bitmap != null) {
      Log.v(TAG, "montaFotoCabine() - bitmap gerado");
    } else {
      Log.w(TAG, "montaFotoCabine() - bitmap n�o foi gerado (retornou nulo)");
    }

    // retorna um bitmap ou null em caso de erro
    return bitmap;

  }

  /**
   * Recebe o nome do arquivo da foto j� processado.
   * 
   * @param requestCode
   *          c�dido da requisi��o
   * 
   * @param meuArquivo
   *          nome do arquivo contendo a foto processada
   * 
   */
  private void meuMetodo(int requestCode, String meuArquivo) {

    // TODO alterar o nome do m�todo

    Log.d(TAG, "meuMetodo() - requestCode=" + requestCode);

    logger2.info("meuMetodo() - requestCode=" + requestCode);

    if (meuArquivo != null) {
      Log.d(TAG, "meuMetodo() - meuArquivo=" + meuArquivo);
      // logger.println("meuMetodo() - meuArquivo=" + meuArquivo);
      logger2.info("meuMetodo() - meuArquivo=" + meuArquivo);
    } else {
      Log.d(TAG, "meuMetodo() - meuArquivo retornou nulo");
    }

    // --------------------------------------------------------------
    // executa a activity que exibe a foto
    // --------------------------------------------------------------
    if (meuArquivo != null) {
      executaActivityExibeFoto(meuArquivo);
    }

    // --------------------------------------------------------------

    String msg = null;

    // executa a��o baseada no requestCode
    switch (requestCode) {

      case TIRA_FOTO_POLAROID:
        msg = "TIRA_FOTO_POLAROID";
        Log.w(TAG, "meuMetodo() - requestCode: " + requestCode);
        break;

      case TIRA_FOTO_CABINE:
        msg = "TIRA_FOTO_CABINE";
        Log.w(TAG, "meuMetodo() - requestCode: " + requestCode);
        break;

      default:
        Log.w(TAG, "meuMetodo() - Erro ... requestCode: " + requestCode + " n�o pode ser processado");
        break;

    }

    // TODO aqui � necess�rio a cria��o de uma thread pois a proxima opera��o �
    // muito demorada !!!
    // showAlert(msg);
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    File fileMeuArquivoPadrao = null;

    if (meuArquivo != null) {

      fileMeuArquivoPadrao = new File(meuArquivo);

    } else {
      // TODO: remover
      String meuArquivoPadrao = "/mnt/sdcard/Pictures/fotoevento/fotos/casa_320x240.png";
      fileMeuArquivoPadrao = new File(meuArquivoPadrao);

    }

    logger2.info("meuMetodo() - antes do envio do email");

    // H� uma foto com moldura que ser� enviada por email
    setEstado(2);

    // chama enviaEmail passando a Uri onde se encontra a foto

    Log.i(TAG, "meuMetodo() - Enviando email com foto em: " + Uri.fromFile(fileMeuArquivoPadrao));
    logger2.info("meuMetodo() - Enviando email com foto em: " + Uri.fromFile(fileMeuArquivoPadrao));

    try {

      // Transaction transacao = null;

      // TransactionTask task = new TransactionTask(this, transacao, 0);

      // task.execute();

      // Envia o email com a foto
      enviaEmail(Uri.fromFile(fileMeuArquivoPadrao));

    } catch (Exception e) {

      Log.w(TAG, "meuMetodo() - Erro no envio do email", e);
      // logger.println("meuMetodo() - Erro no envio do email");
      logger2.info("meuMetodo() - Erro no envio do email");

    }

    Log.w(TAG, "meuMetodo() - fim");

  }

  /**
   * Inicia o processo de envio de email.<br>
   * 
   * Verifica inicialmente se todas as condi��es necess�rias est�o satisfeitas
   * para o envio do email com a foto anexada.<br>
   * 
   * Usa: mParticipante e mContratante
   * 
   * @param lastUri
   *          Uri onde a foto est� armazenada
   * 
   * @throws Exception
   * 
   */
  private void enviaEmail(Uri lastUri) throws Exception {

    Log.d(TAG, "*** enviaEmail() - lastUri=" + lastUri);

    // valida as informa��es para envio do email
    validaDadosEmail(lastUri);

    // carrega as prefer�ncias sobre o envio de email
    SharedPreferences emailPreferences = getSharedPreferences(Constantes.PREF_EMAIL, MODE_PRIVATE);

    if (emailPreferences == null) {
      Log.w(TAG, "enviaEmail() - SharedPreferences n�o foi encontrada.");
    }

    /**
     * Assunto do email
     * 
     * Recupera o "subject" do email do arquivo de prefer�ncias
     */
    String subject = emailPreferences.getString(Constantes.PREFERENCIAS_ASSUNTO, "Evento Inicial");

    /**
     * Corpo do email
     * 
     * Recupera o "corpo" do email do arquivo de prefer�ncias
     * 
     */
    String body = emailPreferences.getString(Constantes.PREFERENCIAS_DESCRICAO, "Segue as informa��es sobre o evento");

    emailPreferences = null;

    // obt�m o email do participante do evento
    String to = mParticipante.getEmail();

    // obt�m o email do contratante do evento
    // ele ser� copiado em BCC no email enviado
    String bcc = mContratante.getEmail();

    // envia o email

    if (EMAIL_TIPO == 0) {

      // envia email usando uma intent
      sendEmail(to, bcc, subject, body, lastUri);

    } else if (EMAIL_TIPO == 1) {

      // envia email usando JavaMail ao inv�s de uma intent

      Log.d(TAG, "to: " + to + ", bcc=" + bcc + ", subject=" + subject + ", body=" + body + ", lastUri=" + lastUri);

      boolean enviadoComSucesso = sendEmailExternal(to, bcc, subject, body, lastUri);
      // boolean enviadoComSucesso = sendEmailExternal2(to, bcc, subject, body,
      // lastUri);

      Log.w(TAG, "enviaEmail() - EMAIL ENVIADO COM SUCESSO: " + enviadoComSucesso);

      if (enviadoComSucesso) {
        // processa o resultado do envio do email com sucesso
        aposEnviarEmail();
      } else {
        // vai para o estado final
        estadoFinal();
      }

    }

  }

  /**
   * Valida se existe informa��es suficientes para envio do email.<br>
   * 
   * As condi��es necess�rio verificadas s�o:<br>
   * <ul>
   * <li>contratante
   * <li>participante
   * <li>foto
   * </ul>
   * 
   * @param lastUri
   *          URI da foto
   */
  private void validaDadosEmail(Uri lastUri) {

    boolean erro = false;

    //
    // TODO verificar o estado da aplica��o
    // aqui � necess�rio saber se existe uma foto
    //

    Log.d(TAG, "validaDadosEmail() - lastUri=" + lastUri);

    if (getEstado() < 2) {
      Log.d(TAG, "validaDadosEmail() - Foto n�o foi tirada");
      erro = true;
    }

    if (mParticipante == null) {
      Log.w(TAG, "validaDadosEmail() - N�o h� informa��es sobre o participante");
      erro = true;
    }

    if (mContratante == null) {
      Log.w(TAG, "validaDadosEmail() - N�o h� informa��es sobre o contratante");
      erro = true;
    }

    if (lastUri == null) {
      Log.w(TAG, "validaDadosEmail() - N�o h� foto");
      erro = true;
    }

    if (erro) {
      // TODO o que fazer ???
    } else {
      // TODO o que fazer ???
    }

    Log.d(TAG, "validaDadosEmail() - fim");

  }

  /**
   * Envia o email ao participante e ao contratante (em bcc) com a foto tirada
   * no evento.<br>
   * 
   * Esse m�todo usa o mecanismo de envio de email usado pelo Android.<br>
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
   * Envia email usando a biblioteca externa (JavaMail) de envio de email.<br>
   * 
   * O contratante do evento sempre receber� (em c�pia invis�vel ao
   * destinat�rio) o email enviado ao participante do evento.<br>
   * 
   * Obs: usa a vari�vel membro mailServer.<br>
   * 
   * @param emailParticipante
   *          Email do participante do evento
   * 
   * @param emailContratante
   *          Email do contratante do evento
   * 
   * @param subject
   *          Assunto tratado pelo email
   * 
   * @param text
   *          Corpo do email (mensagem)
   * 
   * @param imageUri
   *          Uri da foto tirada
   * 
   * @return true se email enviado com sucesso ou false em caso de erro.
   * 
   * @throws Exception
   *           erro no envio do email
   */
  private boolean sendEmailExternal(String emailParticipante, String emailContratante, String subject, String text, Uri imageUri)
      throws Exception {

    Log.d(TAG, "sendEmailExternal() - inicio");

    // cria um file que ser� adicionado (anexado) ao email
    File f = new File(imageUri.getPath());

    // TODO object mailServer come�a ser configurado em initEmailConfig()
    // host, port, from, debugabble, auth, ssl

    try {

      if (mailServer != null) {

        // anexa o arquivo ao email
        // mailServer.addAttachment(f.getAbsolutePath());
        mailServer.setFilename(f.getAbsolutePath());

        Log.d(TAG, "sendEmailExternal() - foto anexada ao email.");

      } else {
        Log.w(TAG, "sendEmailExternal() - mailServer est� nulo.");
      }

    } catch (Exception e) {

      Log.w(TAG, "sendEmailExternal() - n�o foi encontrado o anexo contendo a foto", e);

      logger2.info("sendEmailExternal() - n�o foi encontrado o anexo contendo a foto");

      return false;

    }

    // From:
    mailServer.setFrom(emailContratante);

    // To:
    mailServer.setTo(new String[] { emailParticipante });

    // Bcc:
    mailServer.setBcc(new String[] { emailContratante });

    // Subject
    mailServer.setSubject(subject);

    // Body
    mailServer.setBody(text);

    Log.d(TAG, "sendEmailExternal() - " + mailServer);

    boolean enviou = false;

    try {

      // envia o email
      enviou = mailServer.send();

      Log.d(TAG, "sendEmailExternal() - enviou: " + enviou);

    } catch (Exception e) {

      enviou = false;

      Log.w(TAG, "sendEmailExternal() - falha no envio do email", e);
      Log.w(TAG, "sendEmailExternal() - getMessage(): " + e.getMessage());

    }

    // resultado do envio do email
    return enviou;

  }

  /**
   * Envia um email
   * 
   * @param emailParticipante
   *          Email do participante do evento
   * @param emailContratante
   *          Email do contratante do evento
   * @param subject
   *          Assunto do Email
   * @param text
   *          Corpo do Email
   * @param imageUri
   *          Uri onde se encontra a foto
   * 
   * @return true se o email foi enviado com sucesso ou false caso contr�rio.
   * 
   * @throws Exception
   */
  private boolean sendEmailExternal2(String emailParticipante, String emailContratante, String subject, String text, Uri imageUri)
      throws Exception {

    Log.d(TAG, "sendEmailExternal2() - in�cio");

    MsgMultiSend meuMail = new MsgMultiSend("maurocl@mltech.com.br", "mcl16d");

    String from = emailContratante;
    String to = emailParticipante;
    String bcc = emailContratante;

    String subject2 = subject;
    String body = text;
    String filename = imageUri.getPath();

    boolean enviou = false;

    try {

      meuMail.send(to, bcc, from, subject2, body, filename);

      Log.d(TAG, "sendEmailExternal2() - enviou: " + enviou);

      enviou = true;

    } catch (Exception e) {

      enviou = false;

      Log.w(TAG, "sendEmailExternal2() - falha no envio do email", e);
      Log.w(TAG, "sendEmailExternal2() - getMessage(): " + e.getMessage());

    }

    // resultado do envio do email
    return enviou;

  }

  /**
   * Verifica se as mensagens devem ser postadas nas redes sociais<br>
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

    Log.i(TAG, "sendMsgFacebook() - em constru��o");
  }

  /**
   * sendMsgTwitter()
   */
  private void sendMsgTwitter() {

    Log.i(TAG, "sendMsgTwitter() - em constru��o");
  }

  /**
   * Processa o resultado do envio do email (execu��o da activity)
   * 
   * @param resultCode
   *          Resultado a execu��o da activity
   * 
   * @param data
   *          Intent retornada
   * 
   */
  private void resultActivityChooser(int resultCode, Intent data) {

    Log.d(TAG, "resultActivityChooser() ===> processando resultado da ACTIVITY CHOOSER: resultCode=" + resultCode);

    // Obt�m informa��es sobre a intent chamadora
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

      // processa
      aposEnviarEmail();

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
   * Executa a��es ap�s o envio do email com sucesso.<br>
   * 
   * Exibe a mensagem de envio de email com sucesso.<br>
   * 
   * Atualiza o estado da aplica��o.<br>
   * 
   * Finaliza a a��o. <br>
   * 
   */
  private void aposEnviarEmail() {

    Log.d(TAG, "aposEnviarEmail() - email enviado com sucesso");

    // logger.println("aposEnviarEmail() - email enviado com sucesso");
    logger2.info("aposEnviarEmail() - email enviado com sucesso");

    // mensagem exibida ap�s envio de email
    Toast.makeText(this, "Email enviado com sucesso", Toast.LENGTH_LONG).show();

    // Atualiza o estado da m�quina de estado
    setEstado(3);

    // Processa o pr�ximo estado
    estadoFinal();

  }

  /**
   * Executa a activity que exibe uma foto.<br>
   * 
   * Envia uma intent com a URI da foto que ser� exibida.
   * 
   * @param arquivo
   *          nome completo do arquivo onde a foto est� localizada.
   * 
   */
  private void executaActivityExibeFoto(String arquivo) {

    File f = new File(arquivo);
    Uri uri = Uri.fromFile(f);

    Intent intent = new Intent(this, ExibeFotoActivity.class);
    intent.setData(uri);
    startActivity(intent);

  }

  /**
   * � o ponto final da activity onde � definido o resultado final da execu��o
   * da activity.<br>
   * 
   * Representa o estado final da m�quina de estado.<br>
   * 
   * � tamb�m o respons�vel pela finaliza��o da activity, estabelecendo seu
   * resultado.<br>
   * 
   * Se o estado final foi atingido ent�o o processo correu segundo esperado.<br>
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

      // retorna as informa��es sobre o participante e sobre sua
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
      // logger.println("estadoFinal() - n�o foi poss�vel chegar ao final do processamento.");
      logger2.info("estadoFinal() - n�o foi poss�vel chegar ao final do processamento.");

      intent.putExtra("br.com.mltech.result", "NOT_OK");

      // estabelece o resultado da execu��o da Activity
      setResult(RESULT_CANCELED, intent);

    }

    // TODO verificar se sempre � necess�rio retornar para o modo Portrait
    // atualiza o modo da tela
    // atualizaModoTela(Configuration.ORIENTATION_PORTRAIT);
    atualizaModoTela(modoTela);

    // Termina a execu��o da Activity respons�vel por tirar e enviar uma
    // foto
    finish();

  }

  /**
   * Atualiza o estado da uma m�quina de estados.
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
   * Obt�m o estado atual da m�quina de estados.
   * 
   * @return Um inteiro contendo o estado da state machine.
   * 
   */
  private int getEstado() {

    return mEstado;
  }

  /**
   * Trata o resultado da execu��o das Activities.<br>
   * 
   * Processa o resultado da execu��o das Activities.<br>
   * 
   * � chamado quando a activity lan�ada retorna, dando a voc� o requestCode com
   * o qual voc� iniciou, o resultCode retornado e qualquer dado adicional
   * resultado do processamento da activity. O resultCode ser� RESULT_CANCELED
   * se a activity retornar explicitamente esse valor, n�o retornar nenhum valor
   * ou haver algum crash dureante a opera��o.<br>
   * 
   * Esse m�todo ser� chamado imediatamente antes da execu��o do onResume()
   * quando sua activity � reinicializada.<br>
   * 
   * Called when an activity you launched exits, giving you the requestCode you
   * started it with, the resultCode it returned, and any additional data from
   * it. The resultCode will be RESULT_CANCELED if the activity explicitly
   * returned that, didn't return any result, or crashed during its operation.<br>
   * 
   * You will receive this call immediately before onResume() when your activity
   * is re-starting.<br>
   * 
   */
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    Log.i(TAG, "------------------------------------------------------------------------------------------------------------");
    Log.i(TAG, "onActivityResult(request (" + requestCode + ") " + getActivityName(requestCode) + ", result=" + resultCode
        + ", data " + data + ") ...");
    Log.i(TAG, "------------------------------------------------------------------------------------------------------------");

    String sNomeArquivo = null;

    switch (requestCode) {

      case ACTIVITY_PARTICIPANTE:

        resultActivityParticipante(resultCode, data);
        break;

      case ACTIVITY_PARTICIPACAO:

        resultActivityParticipacao(resultCode, data);
        break;

      case ACTIVITY_CHOOSER:

        resultActivityChooser(resultCode, data);
        break;

      case TIRA_FOTO_POLAROID:

        numFotosTiradas++;

        sNomeArquivo = processaActivityResultPolaroid(resultCode, data);

        // atualiza a informa��o sobre a localiza��o da foto
        mParticipacao.setNomeArqFoto(sNomeArquivo);
        numFotosPolaroid++;

        meuMetodo(requestCode, sNomeArquivo);
        break;

      case TIRA_FOTO_CABINE:

        numFotosTiradas++;

        sNomeArquivo = processaActivityResultCabine(resultCode, data);

        if (sNomeArquivo != null) {
          // atualiza a informa��o sobre a localiza��o da foto
          mParticipacao.setNomeArqFoto(sNomeArquivo);
          numFotosCabine++;

          meuMetodo(requestCode, sNomeArquivo);

        } else {
          Log.w(TAG, "onActivityResult() - foto cabine retornou nula");
        }

        break;

      default:
        Log.w(TAG, "onActivityResult() - Erro ... requestCode: " + requestCode + " n�o pode ser processado");
        break;

    }

    // TODO avaliar se o c�digo abaixo deve ser removido.
    /*
     * 
     * // se a aplica��o estiver executando em Landscape a quest�o abaixo n�o
     * deve ser alterada if (requestCode == TIRA_FOTO_POLAROID) { Log.d(TAG,
     * "onActivityResult() - >>> alterando a orienta��o da tela (se necess�rio) - requestCode="
     * + requestCode); logger2.info("onActivityResult() - requestCode=" +
     * requestCode); //atualizaModoTela(Configuration.ORIENTATION_PORTRAIT);
     * atualizaModoTela(modoTela); }
     */

  }

  /**
   * Exibe uma mensagem numa caixa de di�logo. Use o bot�o para fechar a janela.
   * 
   * @param msg
   *          Mensagem que ser� exibida na caixa de di�logo exibida.
   * 
   */
  private Dialog showAlert(String msg) {

    if (msg == null) {
      // retorna se a mensagem for nula
      return null;
    }

    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setMessage(msg).setNeutralButton("Fechar", null);

    // Cria uma janela a partir dos dados solicitados no builder
    AlertDialog alert = builder.create();

    // exibe a janela
    alert.show();

    // retorna a inst�ncia da ciaxa de di�logos criada (para que possa ser
    // destru�da).
    return alert;

  }

  /**
   * Carrega arquivo de moldura.<br>
   * 
   * @param arquivoMoldura
   *          Nome do arquivo onde se encontra o bitmap com a moldura.
   * 
   * @return um bitmap com o arquivo contendo a moldura ou null no caso de algum
   *         problema.
   * 
   */
  private Bitmap leArquivoMoldura(String arquivoMoldura) {

    Log.d(TAG, "leArquivoMoldura() - arquivoMoldura: [" + arquivoMoldura + "].");

    if ((arquivoMoldura != null) && (arquivoMoldura.equals(""))) {
      Log.w(TAG, "leArquivoMoldura() - o nome do arquivo est� vazio.");
      return null;
    }

    File moldura = new File(arquivoMoldura);

    if (!moldura.exists()) {
      Log.w(TAG, "leArquivoMoldura() - arquivoMoldura: [" + arquivoMoldura + "] n�o existe.");
      return null;
    }

    // l� o bitmap contendo a moldura do arquivo.
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
   * Obt�m um par�metro da lista de prefer�ncias.
   * 
   * @param sharedPreferencesName
   *          nome do arquivo de prefer�ncias
   * @param attribute
   *          nome do atributo guardado no arquivo
   * 
   * @return o valor do atributo solicitado ou uma string vazia caso haja algum
   *         erro (o par�metro n�o foi encontrado)
   * 
   */
  private String getSharedPreference(String sharedPreferencesName, String attribute) {

    // TODO o problema desse m�todo � o acesso ao disco toda veze que buscamos
    // um par�metro.

    SharedPreferences preferences = getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);

    String sValue = preferences.getString(attribute, "");

    preferences = null;

    return sValue;

  }

  /**
   * Exibe o valor de alguns atributos da classe (se forem diferente de null).<br>
   * 
   * Apenas os atributos n�o nulos ser�o exibidos.
   * 
   */
  private void showVariables() {

    Log.v(TAG, "=================================");
    Log.v(TAG, "showVariables() - Vari�veis de classe:");

    logger2.info("showVariables()");

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

    // if (xUri != null) {
    // Log.v(TAG, "  xUri=" + xUri);
    // }

    Log.v(TAG, "  mEstado=" + mEstado + ", mContador=" + mContador);
    Log.v(TAG, "  mCurrentCamera=" + currentCamera);

    showClassVariables();

    Log.v(TAG, "=================================");

  }

  /**
   * Exibe o valor de algumas vari�veis de classe previamente selecionadas no
   * log da aplica��o.
   * 
   */
  private void showClassVariables() {

    Log.v(TAG, "    showClassVariables() - file: " + file);
    Log.v(TAG, "    showClassVariables() - outputFileUri: " + outputFileUri);
    Log.v(TAG, "    showClassVariables() - Contador: " + contador + ", i=" + i);
    Log.v(TAG, "    showClassVariables() - numCreate: " + numCreate + ", numRestart: " + numRestart);

  }

  // ---------------------------------------------------------------------------
  //
  //
  // ---------------------------------------------------------------------------

  /**
   * Formata uma foto no formato Polaroid.<br>
   * 
   * Recebe o endere�o de uma foto;<br>
   * redimensiona a foto;<br>
   * Insere a moldura e transforma a foto no tamanho 9x11<br>
   * 
   * @param uriFotoOriginal
   *          Uri da foto original
   * 
   * @return Um bitmap contendo a foto com a moldura ou null em caso de algum
   *         erro.
   * 
   */
  private Bitmap formatarPolaroid(Uri uriFotoOriginal) {

    /**
     * TODO Esse m�todo gera dois arquivos contendo fotos intermedi�rios que
     * poderiam ser descartadas
     * 
     */

    Log.d(TAG, "formatarPolaroid() - uriFotoOriginal: " + uriFotoOriginal);

    // Cria um bitmap a partir da Uri da foto.
    Bitmap bmFotoOriginal = ManipulaImagem.criaBitmap(uriFotoOriginal);

    if (bmFotoOriginal == null) {
      Log.w(TAG, "formatarPolaroid() - N�o foi poss�vel criar o bitmap com a foto original: " + bmFotoOriginal);
      return null;
    }

    Log.d(TAG, "formatarPolaroid() - w=" + bmFotoOriginal.getWidth() + ", h=" + bmFotoOriginal.getHeight());

    if (ManipulaImagem.isLandscape(bmFotoOriginal)) {
      // foto possui a largura maior que a altura
      Log.d(TAG, "formatarPolaroid() - Foto landscape");
    } else {
      // foto possui a altura maior que a largura
      Log.d(TAG, "formatarPolaroid() - Foto portrait");
    }

    // redimensiona a foto original para 9x12 para manter a propor��o 3:4
    // Bitmap bmFoto9x12 = ManipulaImagem.getScaledBitmap2(bmFotoOriginal, 340,
    // 454);
    Bitmap bmFoto9x12 = ManipulaImagem.getScaledBitmap2(bmFotoOriginal, 454, 340);

    if (bmFoto9x12 == null) {
      Log.d(TAG, "formatarPolaroid() - falha no redimensionamento da foto - bmFoto9x12: " + bmFoto9x12);
      return null;
    }

    // Define o nome da foto redimensionada
    String nomeArquivo = FileUtils.getBasePhotoDirectory() + File.separator + FileUtils.getFilename(uriFotoOriginal) + "_9x12.png";

    // Grava a foto redimensionada em um arquivo "intermedi�rio"
    boolean gravou = ManipulaImagem.gravaBitmapArquivo(bmFoto9x12, nomeArquivo);

    if (!gravou) {
      // foto n�o pode ser gravada. Retorna.
      Log.d(TAG, "formatarPolaroid() - foto n�o pode ser gravada");
      return null;
    }

    //
    // redimensiona a foto 9x12 para 8x8, isto �, copia uma "janela" 8x8 da foto
    //
    // Create a default Options object, which if left unchanged will give the
    // same result
    // from the decoder as if null were passed.
    //
    Options options = new Options();

    // TODO verificar qual ser� a "janela" da foto
    Rect rect = new Rect(0, 0, 312, 312);

    // Obtem um bitmap com a foto redimensionada para 8x8
    Bitmap bmFoto8x8 = ManipulaImagem.getBitmapRegion(nomeArquivo, rect, options);

    nomeArquivo = FileUtils.getBasePhotoDirectory() + FileUtils.getFilename(uriFotoOriginal) + "_8x8.png";

    // Aplica a moldura a foto
    Bitmap bmFotoComMoldura = ManipulaImagem.overlay4(bmFoto8x8, mBitmapMolduraPolaroid);

    // o bitmap contendo a foto com a moldura aplicada
    return bmFotoComMoldura;

  }

  /**
   * Cria uma foto a partir da combi��o de tr�s outras fotos. As fotos s�o
   * colocadas na vertical, isto �, uma embaixo da outra.
   * 
   * <pre>
   * 
   * |--------|
   * |        |
   * | foto1  |
   * |        |
   * |        |
   * |--------|
   * |        |
   * | foto2  |
   * |        |
   * |        |
   * |--------|
   * |        |
   * | foto3  |
   * |        |
   * |        |
   * |--------|
   * 
   * 
   * </pre>
   * 
   * 
   * @param bmFoto1
   *          primeira foto
   * 
   * @param bmFoto2
   *          segunda foto
   * 
   * @param bmFoto3
   *          terceira foto
   * 
   * @param bmMoldura
   *          bitmap contendo a moldura
   * 
   * @return uma nova foto (bitmap) contendo as tr�s fotos e a moldura ou null
   *         em caso de erro.
   */
  private static Bitmap processaFotoFormatoCabine3(Bitmap bmFoto1, Bitmap bmFoto2, Bitmap bmFoto3, Bitmap bmMoldura) {

    if (bmMoldura == null) {
      Log.w(TAG, "processaFotoFormatoCabine3() - Moldura formato Cabine est� vazia !");
      return null;
    }

    // executa o merge das tr�s fotos
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

  }

  /**
   * Cria uma nova foto (bitmap) a partir de tr�s fotos recebidas.<br>
   * 
   * <pre>
   * 
   * |foto1|
   * |foto2|
   * |foto3|
   * 
   * </pre>
   * 
   * @param bmFoto1
   *          primeira foto
   * 
   * @param bmFoto2
   *          segunda foto
   * 
   * @param bmFoto3
   *          terceira foto
   * 
   * @return o bitmap contendo as tr�s fotos ou null em caso de algum erro
   */
  private static Bitmap mergeFotosCabine(Bitmap bmFoto1, Bitmap bmFoto2, Bitmap bmFoto3) {

    if (bmFoto1 == null) {
      Log.w(TAG, "mergeFotosCabine() - Foto1 est� vazia !");
      throw new IllegalArgumentException("mergeFotosCabine() - Foto1 est� vazia !");
    }

    if (bmFoto2 == null) {
      Log.w(TAG, "mergeFotosCabine() - Foto2 est� vazia !");
      throw new IllegalArgumentException("mergeFotosCabine() - Foto2 est� vazia !");
    }

    if (bmFoto3 == null) {
      Log.w(TAG, "mergeFotosCabine() - Foto3 est� vazia !");
      throw new IllegalArgumentException("mergeFotosCabine() - Foto3 est� vazia !");
    }

    // =========================================================================
    // Cria um novo bitmap a partir da composi��o das tr�s fotos.
    // As fotos ser�o organizadas na vertical, isto �, uma nova foto
    // ser� colocada abaixo da outra.
    // =========================================================================
    Bitmap bmImgJoin = ManipulaImagem.verticalJoin(bmFoto1, bmFoto2, bmFoto3);

    if (bmImgJoin != null) {

      Log.i(TAG, "mergeFotosCabine() - Imagens foram juntadas com sucesso");
      Log.v(TAG, "mergeFotosCabine() -  ==> Tamanho da foto ap�s join: " + ManipulaImagem.getStringBitmapSize(bmImgJoin));

    } else {

      Log.w(TAG, "mergeFotosCabine() - Erro no merge das tr�s fotos");
      return null;

    }

    // bitmap contendo uma �nica foto criada a partir de tr�s fotos.
    return bmImgJoin;

  }

  // ---------------------------------------------------------------------------
  // M�todos n�o usados
  // TODO avaliar a remo��o dos mesmos
  // ---------------------------------------------------------------------------

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
   * Retorna o nome da Activity dado seu requestCode.<br>
   * 
   * @param requestCode
   *          c�digo que identifica o retorna da Activity
   * 
   * @return Retorna o nome da Activity
   */
  private String getActivityName(int requestCode) {

    String nome = null;

    switch (requestCode) {
      case TIRA_FOTO:
        nome = "TIRA_FOTO";
        break;

      case TIRA_FOTO_POLAROID:
        nome = "TIRA_FOTO_POLAROID";
        break;

      case TIRA_FOTO_CABINE:
        nome = "TIRA_FOTO_CABINE";
        break;

      case ACTIVITY_PARTICIPANTE:
        nome = "ACTIVITY_PARTICIPANTE";
        break;

      case ACTIVITY_PARTICIPACAO:
        nome = "ACTIVITY_PARTICIPACAO";
        break;

      case ACTIVITY_CHOOSER:
        nome = "ACTIVITY_CHOOSER";
        break;

      default:
        nome = "Activity n�o encontrada.";
        break;
    }

    return nome;

  }

  /**
   * Verifica se todos os argumentos enviado ao email s�o n�o nulos.<br>
   * 
   * @param mEmail
   *          identificador da conta de email
   * @param mSenha
   *          senha da conta de email
   * @param mHost
   *          endere�o do servidor SMTP
   * @param mPort
   *          porta do servidor SMTP
   * @param mRemetente
   *          remetente do email
   * 
   */
  private void validaArgumentosEmail(String mEmail, String mSenha, String mHost, String mPort, String mRemetente) {

    if ((mEmail != null) && (mEmail.equals(""))) {
      throw new IllegalArgumentException("Usu�rio da conta de email n�o foi fornecido");
    }

    if ((mSenha != null) && (mSenha.equals(""))) {
      throw new IllegalArgumentException("Senha da conta de email n�o foi fornecida");
    }

    if ((mHost != null) && (mHost.equals(""))) {
      throw new IllegalArgumentException("Endere�o do servidor de email (SMTP) n�o foi fornecido");
    }

    if ((mPort != null) && (mPort.equals(""))) {
      throw new IllegalArgumentException("Porta do servidor de email (SMTP) n�o foi fornecida");
    }

    // TODO validar se a porta � um n� v�lido entre 0 e 1023 ???

    if ((mRemetente != null) && (mRemetente.equals(""))) {
      throw new IllegalArgumentException("Endere�o do remetente do email n�o foi fornecido");
    } else if (!isValidInternetAddress(mRemetente, "Remetente")) {
      throw new IllegalArgumentException("Endere�o do remetente do email n�o � v�lido");
    }

  }

  /**
   * Verifica se o endere�o de email fornecido � v�lido (sintaticamente).
   * 
   * @param emailAddress
   *          Endere�o do email do remetente
   * 
   * @param mMsg
   *          nome do campo
   * 
   * @return true se o endere�o de email fornecido for v�lido ou false caso
   *         contr�rio.
   * 
   */
  @SuppressWarnings("unused")
  private boolean isValidInternetAddress(String emailAddress, String mMsg) {

    try {
      InternetAddress address = new InternetAddress(emailAddress);
    } catch (javax.mail.internet.AddressException e) {

      String mensagem = mMsg + " n�o � v�lido (posi��o: " + e.getPos() + ", ref: " + e.getRef() + ")";

      Log.w(TAG, mensagem, e);

      return false;

    }
    return true;
  }

  /**
   * 
   */
  public void execute() throws Exception {

    // TODO Auto-generated method stub

  }

  /**
   * 
   */
  public void updateView() {

    // TODO Auto-generated method stub

  }

  /**
   * Atualiza a orienta��o atual da tela para outro modo (se necess�rio). <br>
   * 
   * Configuration.ORIENTATION_LANDSCAPE ou Configuration.ORIENTATION_PORTRAIT
   * 
   * @param novaOrientacao
   *          nova orienta��o
   */
  private void atualizaModoTela(int novaOrientacao) {

    // obt�m a orienta��o atual da tela
    int orientacaoAtual = this.getResources().getConfiguration().orientation;

    // exibe qual � a orienta��o atual da tela
    Log.d(TAG, "atualizaModoTela() - Orienta��o atual: " + orientacaoAtual + " - " + getScreenOrientation(orientacaoAtual));

    logger2.info("atualizaModoTela() - Orienta��o atual: " + orientacaoAtual + " - " + getScreenOrientation(orientacaoAtual));

    if (novaOrientacao != orientacaoAtual) {
      // muda a orienta��o
      this.setRequestedOrientation(novaOrientacao);
      // exibe a nova orienta��o
      Log.d(TAG, "atualizaModoTela() - nova orienta��o: " + novaOrientacao + " - " + getScreenOrientation(novaOrientacao));
      logger2.info("atualizaModoTela() - nova orienta��o: " + novaOrientacao + " - " + getScreenOrientation(novaOrientacao));
    }

  }

  /**
   * Obt�m o nome correspondente a orienta��o da tela: Landscape ou Portrait<br>
   * 
   * @param orientacao
   *          Configuration.ORIENTATION_LANDSCAPE ou
   *          Configuration.ORIENTATION_PORTRAIT
   * 
   * @return uma string com o nome da orienta��o da tela ou "N�o suportado"
   *         indicando que a orienta��o n�o � suportada.
   * 
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