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
 * Activity responsável pela sequencia de passos para obtenção das informações
 * de um participante, tirar uma ou mais fotos e enviar um email.
 * 
 * @author maurocl
 * 
 */
public class DummyActivity3 extends Activity implements Constantes {

  private static final String TAG = "DummyActivity3";

  // indica qual Activity será executada (0 = Tira foto normal; 1 = tira foto
  // dummy)
  private static int FLAG = 0;

  private static final int TIRA_FOTO = 200;

  private static final int TIRA_FOTO_POLAROID = 201;

  private static final int TIRA_FOTO_CABINE = 202;

  // ------------------
  // variáveis da classe
  // ------------------

  private static File file;

  private static Uri outputFileUri;

  // Lista de todas as fotos tiradas
  private static List<Foto> listaFotos;

  // Foto
  private static Foto foto;

  // FotoCabine
  private static FotoCabine fotoCabine;

  // Definição dos atributos da aplicação
  private static Contratante mContratante;

  private static Evento mEvento;

  private static Participante mParticipante;

  private static Participacao mParticipacao;

  private static SharedPreferences mPreferences;

  // Nome do arquivo onde está armazenado a última foto
  private static String mFilename;

  // Uri da última foto
  private static Uri xUri;

  //
  private static Uri[] fotosCabine = new Uri[3];

  // Estado atual da máquina de estado da aplicação
  private static int mEstado = -1;

  // Nº de vezes que a activity é criada
  private static int mContador = 0;

  // Nº de câmeras do dispositivo
  private static int numCameras = -1;

  // Nº da câmera corrente em uso (se houver)
  private static int mCurrentCamera = -1;

  // Bitmaps contendo as molduras
  private static Bitmap mBitmapMolduraPolaroid;

  private static Bitmap mBitmapMolduraCabine;

  // Molduras
  private static Moldura molduraPolaroid;

  private static Moldura molduraCabine;

  // Contador do nº de fotos cabine
  private static int mNumFotosCabine = 0;

  // Contador geral (iniciado em onCreate())
  public static int contador = 0;

  public static int i = 0;

  // public int j = 0;

  // nº de vezes que o método onCreate() é chamado
  public static int numCreate = 0;

  // nº de vezes que o método onRestart() é chamado
  public static int numRestart = 0;

  // número de fotos carregas
  public static int numFotosCarregadas = 0;

  // nº de fotos efetivamente tiradas
  public static int numFotosTiradas = 0;

  // nº de fotos tiradas
  private static int indiceFoto = 0;

  // ---------------------------------------------
  // área de inicialização de variáveis estáticas
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

    // Obtem o identificado da câmera que será usada para tirar as fotos
    mCurrentCamera = obtemIdentificadorCamera();

    // ---------------------------------------------------------------------

    // incrementa o nº de vezes que a activity foi reiniciada
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
   * Inicializa as variáveis que vão conter o bitmap das molduras
   * 
   * mBitmapMolduraPolaroid. mBitmapMolduraCabine.
   * 
   * mBitmapMolduraPolaroid. mBitmapMolduraCabine.
   * 
   */
  private void carregaMolduras() {

    mPreferences = getSharedPreferences("preferencias", MODE_PRIVATE);

    if (mPreferences == null) {

      Log.w(TAG, "carregaMolduras() - mPreferences is null. Falha na execução do comandos getSharedPreferences()");
      return;

    }

    String arquivoMolduraPolaroid = null;
    String arquivoMolduraCabine = null;

    // o arquivo de configuração possui a informação da localização das
    // molduras

    // Obtém o arquivo contendo o bitmap da moldura formato polaroid
    arquivoMolduraPolaroid = mPreferences.getString("evento_borda_polaroid", "");

    // Obtém o arquivo contendo o bitmap da moldura formato cabine
    arquivoMolduraCabine = mPreferences.getString("evento_borda_cabine", "");

    if ((arquivoMolduraPolaroid != null) && (arquivoMolduraPolaroid.equals(""))) {
      Log.d(TAG, "carregaMolduras() - moldura formato Polaroid não foi configurada.");
      estadoFinal();
    }

    if ((arquivoMolduraCabine != null) && (arquivoMolduraCabine.equals(""))) {
      Log.d(TAG, "carregaMolduras() - moldura formato Cabine não foi configurada.");
      estadoFinal();
    }

    // ----------------------------------------------------------
    // o arquivo contendo a moldura é lido no inicio da activity
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
   * Verifica se existe uma configuração explicita para o nº da câmera frontal
   * 
   * @return o nº da câmera frontal ou 0 caso esse parâmetro não esteja
   *         cadastrado
   * 
   */
  private int obtemIdentificadorCamera() {

    int num = 0;

    // -----------------------------------------------
    // obtem o nº da câmera usada para tirar as fotos
    // -----------------------------------------------
    String s = getSharedPreference("pref_email", "preferencias_num_camera_frontal");

    Log.i(TAG, "obtemIdentificadorCamera() - Nº da câmera frontal=" + s);

    if ((s != null) && (!s.equals(""))) {
      num = Integer.valueOf(s);
    } else {
      num = 0;
      Log.w(TAG, "obtemIdentificadorCamera() - Nº da câmera frontal não foi definido. Assumindo o valor 0");
    }

    return num;

  }

  /**
   * onActivityResult(int requestCode, int resultCode, Intent data)
   * 
   * Trata o resultado da execução das Activities
   * 
   * Processa o resultado da execução das Activities
   * 
   * É chamado quando a activity lançada retorna, dando a você o requestCode
   * com o qual você iniciou, o resultCode retornado e qualquer dado adicional
   * resultado do processamento da activity. O resultCode será RESULT_CANCELED
   * se a activity retornar explicitamente esse valor, não retornar nenhum valor
   * ou haver algum crash dureante a operação.
   * 
   * Esse método será chamado imediatamente antes da execução do onResume()
   * quando sua activity é reinicializada.
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

    numFotosTiradas++;

    String sNomeArquivo = null;

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
        meuMetodo(requestCode, sNomeArquivo);
        break;

      case TIRA_FOTO_POLAROID:

        sNomeArquivo = processaActivityResultPolaroid(resultCode, data);
        meuMetodo(requestCode, sNomeArquivo);
        break;

      case TIRA_FOTO_CABINE:

        sNomeArquivo = processaActivityResultCabine(resultCode, data);
        if (sNomeArquivo != null) {
          meuMetodo(requestCode, sNomeArquivo);
        }
        break;

      default:
        Log.w(TAG, "onActivityResult() - Erro ... requestCode: " + requestCode + " não pode ser processado");
        break;

    }

    Log.d(TAG, "onActivityResult() - >>> alterando a orientação da tela (se necessário)");
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
        Log.w(TAG, "meuMetodo() - Erro ... requestCode: " + requestCode + " não pode ser processado");
        break;

    }

    showAlert(msg);

    File fff = null;

    if (meuArquivo != null) {
      fff = new File(meuArquivo);
    } else {
      String meuArquivoPadrao = "/mnt/sdcard/Pictures/fotoevento/fotos/casa_320x240.png";
      fff = new File(meuArquivoPadrao);
    }

    showAlert("Envia email");

    enviaEmail(Uri.fromFile(fff));

    Log.w(TAG, "meuMetodo() - fim");

  }

  /**
   * iniciaProcesso()
   */
  private void iniciaMaquinaDeEstados() {

    boolean sucesso = iniciaVariaveis();

    if (!sucesso) {
      Log.w(TAG, "iniciaProcesso() - Não foi possível inicializar as variáveis");
      estadoFinal();
    }

  }

  /**
   * iniciaVariaveis()
   * 
   * Inicia a execução da máquina de estados da activity
   * 
   * @return true se as operações iniciais foram executas com sucesso ou false
   *         em caso de erro
   * 
   */
  private boolean iniciaVariaveis() {

    // obtem o nº de câmeras disponíveis pelo dispositivo onde a aplicação
    // está em execução
    numCameras = android.hardware.Camera.getNumberOfCameras();

    Log.d(TAG, "iniciaVariaveis() - Número de Câmeras disponíveis no hardware: " + numCameras);

    // verifica se a câmera fotogrática está em operação
    if (CameraTools.isCameraWorking(mCurrentCamera)) {

      Log.i(TAG, "iniciaVariaveis() - Camera: [" + mCurrentCamera + "] está em funcionamento...");

    } else {

      Log.w(TAG, "iniciaVariaveis() - Camera: [" + mCurrentCamera + "] não está em funcionamento");

      Toast.makeText(this, "Camera não está disponível", Toast.LENGTH_SHORT);

      return false;

    }

    // Obtem a Intent que iniciou esta Activity
    Intent i = this.getIntent();
    Uri data = i.getData();

    // indicador de erro de configuração
    int erro = 0;

    // Obtém informações sobre o Contratante
    if (i.getSerializableExtra(CONTRATANTE) != null) {
      mContratante = (Contratante) i.getSerializableExtra(CONTRATANTE);
    } else {
      Log.w(TAG, "iniciaVariaveis() - Contratante não pode ser nulo.");
      Toast.makeText(this, "Contratante não pode ser nulo", Toast.LENGTH_SHORT).show();
      erro = 1;
    }

    // Obtem informações sobre o Evento
    if (i.getSerializableExtra(EVENTO) != null) {
      mEvento = (Evento) i.getSerializableExtra(EVENTO);
    } else {
      Log.w(TAG, "iniciaVariaveis() - Evento não pode ser nulo.");
      Toast.makeText(this, "Evento não pode ser nulo", Toast.LENGTH_SHORT).show();
      erro += 2;
    }

    if (erro > 0) {

      Log.w(TAG, "iniciaVariaveis() - Informações insuficientes para execução (erro=" + erro + ")");

      showAlert("Verifique a configuração da aplicação");

      return false;

    }

    Log.v(TAG, "iniciaVariaveis() - mContratante=" + mContratante);
    Log.v(TAG, "iniciaVariaveis() - mEvento=" + mEvento);

    // Altera o estado atual
    setEstado(0);

    // Inicia a obtenção dos Participantes
    startActivityParticipante();

    return true;

  }

  /**
   * startActivityParticipante()
   * 
   * Inicia a Activity Participante. Passa como parâmetro as informações sobre o
   * Evento.
   * 
   * Aguarda as informações sobre o participante do evento
   * 
   */
  private void startActivityParticipante() {

    // Cria uma nova Intent para chamar a Activity Participante
    Intent intentParticipante = new Intent(this, ParticipanteActivity.class);

    // Inclui o parâmetro mEvento (com as informações sobre o evento em
    // curso)
    intentParticipante.putExtra(EVENTO, mEvento);

    // Inicia a Activity
    startActivityForResult(intentParticipante, ACTIVITY_PARTICIPANTE);

  }

  /**
   * activityParticipanteResult(int resultCode, Intent data)
   * 
   * @param resultCode
   *          resultado da execução da activity Participante
   * @param data
   *          Intent com os resultados (se houverem)
   * 
   */
  private void resultActivityParticipante(int resultCode, Intent data) {

    Log.d(TAG, "resultActivityParticipante() ==> processando resultado da ACTIVITY PARTICIPANTE");

    if (getEstado() < 0) {
      Log.w(TAG, "resultActivityParticipante() - Não há informações sobre o participante");
      return;
    }

    if (resultCode == RESULT_CANCELED) {

      // operação cancelada

      Log.d(TAG, "resultCode=RESULT_CANCELED - Participante cancelou sua participação");

      // Limpa as variáveis
      mParticipante = null;
      mParticipacao = null;

      return;

    } else if (resultCode != RESULT_OK) {

      // o resultado execução da activity não é conhecido
      Log.w(TAG, "resultActivityParticipante() - resultCode não conhecido: " + resultCode);
      return;

    }

    if (data == null) {
      // caso a Intent não retorne nada houve algum problema
      Log.w(TAG, "resultActivityParticipante() - A intent não retornou nenhuma informação");
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

    // Exibe as informações sobre o participante e sua participação
    Log.d(TAG, "resultActivityParticipante() - mParticipante=" + mParticipante);
    Log.d(TAG, "resultActivityParticipante() - mParticipacao=" + mParticipacao);

    // obs: nesse ponto já sabemos qual será o tipo da foto

    // Atualiza o estado da máquina de estados
    setEstado(1);

    // Processa o próximo estado
    tirarFotos();

  }

  /**
   * tirarFotos()
   * 
   * Nesse passo o usuário já forneceu suas informações pessoais e agora é
   * necessário tirar a(s) foto(s).
   * 
   * Tira as fotos de acordo com a solicitação do participante
   * 
   */
  void tirarFotos() {

    if (mParticipacao == null) {
      Log.d(TAG, "tirarFotos() - não é possível obter as informações do participante");
      estadoFinal();
    }

    if (mEvento == null) {
      Log.d(TAG, "tirarFotos() - não é possível obter as informações sobre o evento");
      estadoFinal();
    }

    // obtém o tipo da foto (se o formato da foto é Polaroid ou Cabine)
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

      Log.w(TAG, "tirarFotos() - tipo de foto: " + tipoFoto + " não suportado.");
      estadoFinal();

    }

    //
    // Processa o próximo estado
    //
    // obtemFoto();

  }

  /**
   * getIntentTirarFoto()
   * 
   * cria uma intent para solicitar uma foto a uma activity
   * 
   * Observe que a variável FLAG é usada para "escolher" a activity
   * 
   * @return uma Intent
   * 
   */
  private Intent getIntentTirarFoto() {

    Intent intent = null;

    if (FLAG == 0) {

      // simulação de "tirar uma foto"
      intent = new Intent(this, ActivityCameraSimplesDummy.class);

      intent.putExtra("br.com.mltech.outputFileUri", outputFileUri);

    } else if (FLAG == 1) {

      // tira uma foto verdadeira
      intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

      intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

    }

    Log.d(TAG, "getIntentTirarFoto() -  " + intent);
    Log.d(TAG, "getIntentTirarFoto() - outputFileUri: " + outputFileUri);

    return intent;

  }

  /**
   * executaActivityTiraFotoPolaroid(String arquivo)
   * 
   * @param arquivo
   *          nome do arquivo onde a foto original será armazenada
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
   * Processa o resultado da execução da activity responsável por fornecer uma
   * foto
   * 
   * Pega a foto retornada e formata-a no formato Polaroid
   * 
   * @param resultCode
   *          resultado da execução da activity
   * @param data
   *          intent contendo os dados retornados (se houver)
   * 
   *          Use a variável de classe: file
   * 
   */
  private String processaActivityResultPolaroid(int resultCode, Intent data) {

    String meuArquivo2 = null;

    /**
     * 
     */
    if (resultCode == RESULT_CANCELED) {

      // operação cancelada
      Log.w(TAG, "processaActivityResultPolaroid() - Operação cancelada pelo usuário");
      estadoFinal();

    } else if (resultCode != RESULT_OK) {

      Log.w(TAG, "processaActivityResultPolaroid() - Operação não conhecida");
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

      // nome do arquivo onde o bitmap está armazenado
      // file = new File(data.getStringExtra("outputFileUri"));

    } else {

      // não houve retorno de dados
      Log.w(TAG, "processaActivityResultPolaroid - data (Intent) é vazia");
      // file = null;

    }

    // cria uma URI para referenciar o arquivo
    // outputFileUri = Uri.fromFile(file);

    Log.w(TAG, "processaActivityResultPolaroid() - outputFileUri: " + outputFileUri);

    // grava o bitmap (foto original)
    boolean gravouFotoOriginal = ManipulaImagem.gravaBitmapArquivo3(outputFileUri);

    if (!gravouFotoOriginal) {
      Log.w(TAG, "processaActivityResultPolaroid() - arquivo: " + outputFileUri + " não pode ser gravado");
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

    // Obtém uma foto formato polaroid
    Bitmap fp = formatarPolaroid(outputFileUri);

    if (fp != null) {

      // bitmap obtido com sucesso
      boolean gravouFotoComMoldura = ManipulaImagem.gravaBitmapArquivo(fp, meuArquivo2);

      if (!gravouFotoComMoldura) {
        // foto não foi gravada
        Log.w(TAG, "processaActivityResultPolaroid() - arquivo: " + meuArquivo2 + " não pode ser gravado");
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

    // TODO melhorar a construção abaixo
    String arquivo = (FileUtils.obtemNomeArquivo(".png")).getAbsolutePath();

    // cria um File usando o nome do arquivo gerado
    file = new File(arquivo);

    Log.d(TAG, "==>");
    Log.d(TAG, "==> executaActivityTiraFotoCabine() - contadorCabine: " + indiceFoto);
    Log.d(TAG, "==> executaActivityTiraFotoCabine() - arquivo=" + file.getAbsolutePath());
    Log.d(TAG, "==> ");

    // cria uma instância da classe Foto
    // fornecendo o nome do arquivo onde a foto
    // deve ser armazenada
    Foto foto = new Foto(file.getAbsolutePath());

    // armazena a foto no índece dado pela variável contadorCabine no array de
    // fotos do objeto FotoCabine
    fotoCabine.setFoto(indiceFoto, foto);

    // especifica a Uri do arquivo onde a foto deve ser armazenada
    outputFileUri = Uri.fromFile(file);

    // cria uma intent para "tirar a foto"
    Intent intent = getIntentTirarFoto();

    // exibe informações sobre a intent criada
    Log.d(TAG, "executaActivityTiraFotoCabine() - intent: " + intent);

    Log.d(TAG, "executaActivityTiraFotoCabine() - " + intent.getParcelableExtra("br.com.mltech.outputFileUri"));

    // inicia a activity responsável por obter a foto usando a intent criada.
    startActivityForResult(intent, TIRA_FOTO_CABINE);

  }

  /**
   * processaActivityResultCabine(int resultCode, Intent data)
   * 
   * Esta rotina é responsável por obter três foto da câmera. A variável
   * contadorCabine controla o nº de fotos (varia de 0 a 2)
   * 
   * Processa o resultado da execução da Activity responsável por obter uma
   * foto.
   * 
   * @param resultCode
   *          resultado da execução da activity
   * 
   * @param data
   *          dados retornados da execução da activity
   * 
   */
  private String processaActivityResultCabine(int resultCode, Intent data) {

    Bitmap meuBitmap = null;

    if (resultCode == RESULT_OK) {

      // activity executada com sucesso
      Log.i(TAG, "processaActivityResultCabine() - processando o recebimento da foto - indiceFoto=" + indiceFoto);

      // incrementa o índice usado para armazenar a foto obtida no array de
      // fotos
      indiceFoto++;

      if (indiceFoto < 3) {

        // obtem uma outra foto
        executaActivityTiraFotoCabine();

      } else {

        // três foto já foram obtidas (tiradas)
        meuBitmap = fimExecucaoActivityTiraFotoCabine();

      }

    } else if (resultCode == RESULT_CANCELED) {

      // operação cancelada
      Log.w(TAG, "processaActivityResultCabine() - Operação cancelada pelo usuário na foto: " + indiceFoto);
      // TODO aqui deveremos "cancelar" a fotoCabine fazendo-a null (entre
      // outras coisas)
      meuBitmap = null;

    } else {
      Log.w(TAG, "processaActivityResultCabine() - Operação não suportada pelo usuário");
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
   * @return uma instância de Foto ou null em caso de algum problema.
   */
  private Bitmap fimExecucaoActivityTiraFotoCabine() {

    // Após tirar as três fotos é necessário:
    // - converter as foto para o formato 3x4
    // - montar a foto formato cabine juntando as fotos 3x4 e a moldura
    // fotoCabine.getFoto[0] --> Foto
    // fotoCabine.getFoto[1] --> Foto
    // fotoCabine.getFoto[2] --> Foto

    Log.d(TAG, "fimExecucaoActivityTiraFotoCabine()");

    if (fotoCabine == null) {
      Log.w(TAG, "fimExecucaoActivityTiraFotoCabine() - fotoCabine é nula");
      return null;
    }

    // cria o bitmap correspondente a cada foto
    criaBitmapFotoCabine(fotoCabine);

    // Exibe o estado da instância da classe FotoCabine
    Log.d(TAG, fotoCabine.toString());

    // nesse ponto todas as foto já possuem seu bitmap

    Log.d(TAG, "fimExecucaoActivityTiraFotoCabine() - agora é montar a foto cabine ...");

    Bitmap meuBitmap = null;

    //
    try {

      meuBitmap = montaFotoCabine(fotoCabine);

    } catch (FileNotFoundException e) {

      Log.w(TAG, "fimExecucaoActivityTiraFotoCabine() - execeção arquivo não encontrado (FileNotFound)", e);

    } catch (IOException e) {

      Log.w(TAG, "fimExecucaoActivityTiraFotoCabine() - exceção de IO (IOException)", e);

    }

    return meuBitmap;

  }

  /**
   * montaFotoCabine(fotoCabine)
   * 
   * cria uma foto com moldura cabine 3,5 x 15 cm a partir de três fotos 3x4 e
   * uma moldura
   * 
   * @return uma instância da classe Foto
   * 
   * @throws IOException
   * 
   * @throws FileNotFoundException
   * 
   */
  private Bitmap montaFotoCabine(FotoCabine fotoCabine) throws FileNotFoundException, IOException {

    if (fotoCabine == null) {
      // foto não existe
      Log.w(TAG, "montaFotoCabine() - fotoCabine está vazia");
      return null;
    }

    // cria um array contendo referências a três instâncias de Bitmap
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
    // gera um arquivo com as três foto molduradas
    //
    Bitmap b = processaFotoFormatoCabine3(fotos3x4[0], fotos3x4[1], fotos3x4[2],
        mBitmapMolduraCabine);

    return b;

  }

  private Foto montaFotoCabine_OLD(FotoCabine fotoCabine) throws FileNotFoundException, IOException {

    if (fotoCabine == null) {
      // foto não existe
      Log.w(TAG, "montaFotoCabine() - fotoCabine está vazia");
      return null;
    }

    // cria um array contendo referências a três instâncias de Foto
    Foto[] fotos3x4 = new Foto[3];

    // preenche o array com as fotos armazenadas na fotoCabine
    Foto[] fotos = fotoCabine.getFotos();

    int i = 0;

    // processa cada uma das fotos e faz o redimensionamento para o tamanho
    // 3x4
    for (Foto foto : fotos) {

      // transforma cada foto em 3x4
      Log.i(TAG, "montaFotoCabine() - foto[" + i + "] = " + foto.toString());

      // obtém o nome de um arquivo
      File file1 = FileUtils.obtemNomeArquivo(".png");

      if (file1 != null) {
        // exibe o caminho do arquivo
        Log.d(TAG, "  montaFotoCabine(" + i + ") - " + file1.getAbsolutePath());
      } else {
        Log.w(TAG, "  montaFotoCabine() - file1 está vazio");
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
        Log.w(TAG, "  montaFotoCabine() - falha na gravação do arquivo: " + fotos3x4[i].getArquivo());
      }

      i++;

    }

    // onde armazenar essas fotos ???

    /*
     * String arquivoMoldura = PATH_MOLDURAS + "cabine_132_568_red.png";
     * 
     * Log.i(TAG, "arquivoMoldura = " + arquivoMoldura);
     * 
     * // obtem a moldura da Foto Cabine Moldura moldura =
     * fotoCabine.getMoldura();
     * 
     * if (moldura == null) { Log.w(TAG, "montaFotoCabine() - moldura nula");
     * moldura = new Moldura(arquivoMoldura); }
     * 
     * // atualiza o nome do arquivo moldura.setArquivo(arquivoMoldura);
     * 
     * // lê o arquivo contendo a moldura
     * moldura.leArquivoMoldura(arquivoMoldura);
     * 
     * // obtem a imagem (bitmap) da moldura Bitmap bmMoldura =
     * moldura.getImagem();
     */

    // Sobrepõe a moldura as três foto 3x4

    // array contendo as três fotos
    Bitmap[] bitmapFotos = new Bitmap[3];

    for (int j = 0; j < 3; j++) {

      bitmapFotos[j] = fotos3x4[j].getImagem();
      Log.d(TAG, "montaFotoCabine() - j=" + j);

    }

    //
    // gera um arquivo com as três foto molduradas
    //
    String nomeDoArquivo = ManipulaImagem.processaFotoFormatoCabine2(bitmapFotos[0], bitmapFotos[1], bitmapFotos[2],
        mBitmapMolduraCabine);

    //
    // cria a foto final
    //
    Foto fotoFinal = new Foto(nomeDoArquivo);

    //
    fotoFinal.ler();

    Log.i(TAG, "montaFotoCabine() - fotoFinal: " + fotoFinal.toString());

    return foto;

  }

  /**
   * obtemFoto()
   * 
   * Executa a Intent ACTION_IMAGE_CAPTURE a fim de obter uma foto da câmera
   * 
   * A imagem capturada será armazenada na variável xUri
   * 
   * As condições iniciais para tirar uma foto são: - fornecer uma Url apontando
   * para o arquivo onde a foto será armazenada.
   * 
   */
  private void obtemFoto() {

    Log.d(TAG, "obtemFoto() ==> Executando obtemFoto()");

    if (getEstado() < 1) {
      Log.d(TAG, "obtemFoto() - não há informações sobre o participante. Processo abortado !");
      estadoFinal();
    }

    // Libera os recursos relacionados a câmera
    xUri = null; // endereço onde a foto será armazenada em caso de sucesso
    mFilename = null; // nome do arquivo (full path) onde a foto está
    // armazenada

    // TODO poderia ser substituido por isCameraWorking ???

    Log.d(TAG, "obtemFoto() - mCurrentCamera: " + mCurrentCamera);

    // testa para saber se câmera selecionada está disponível
    if (CameraTools.isCameraWorking(mCurrentCamera)) {

      Log.i(TAG, "obtemFoto() - instância da Camera obtida com sucesso");

    } else {

      Log.w(TAG, "obtemFoto() - Erro da obtenção da instância da Camera " + mCurrentCamera + ". Processo abortado !");

      Toast.makeText(this, "Câmera: " + mCurrentCamera + " não está disponível para uso pela aplicação", Toast.LENGTH_LONG).show();

      estadoFinal();

    }

    File file = obtemNomeArquivo();

    Log.i(TAG, "obtemFoto() - f.getAbsolutePath()=" + file.getAbsolutePath());

    // atualiza o nome do arquivo onde a foto será armazenada
    mFilename = file.getAbsolutePath();

    if (file.canWrite()) {
      Log.d(TAG, "obtemFoto() - arquivo: " + mFilename + " pode ser gravado");
    } else {
      Log.w(TAG, "obtemFoto() - arquivo: " + mFilename + " não pode ser gravado");
    }

    // Obtem a URI do arquivo (esse valor será fornecido como parâmetro da
    // Intent)
    xUri = Uri.fromFile(file); // informa a Uri onde a foto será armazenada

    if (xUri == null) {
      Log.w(TAG, "obtemFoto() - xUri=null. Arquivo para armazenamento não foi criado.");
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
   * Obtem o nome do arquivo que será usado para armazenar a foto
   * 
   * @return uma referência ao nome do arquivo criado
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
   * Cria a intent que será usada para tirar uma foto
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
    // Passa como parâmetro a Uri onde a foto deve ser gravada
    intentTiraFoto.putExtra(MediaStore.EXTRA_OUTPUT, xUri);
    // --------------------------------------------------------------
    return intentTiraFoto;

  }

  /**
   * resultActivityTiraFoto3(int resultCode, Intent data)
   * 
   * Resultado da execução da Activity que recebeu a intent identificada como
   * ACTIVITY_TIRA_FOTO_3
   * 
   * Se a activity executou com sucesso então a foto encontra-se no caminho dado
   * por xUri.getPath(); mFilename possui o nome do arquivo gerado
   * 
   * @param resultCode
   *          Resultado de execução da Activity
   * 
   * @param data
   *          intent retornada da execução da activity
   * 
   */
  private void resultActivityTiraFoto3(int resultCode, Intent data) {

    Log.d(TAG, "resultActivityTiraFoto3() ==> Executando o resultado do processamento da ACTIVITY_TiraFoto3");
    Log.d(TAG, "resultActivityTiraFoto3   ==> resultCode=" + resultCode);

    // testa o resultado da execução da activity ACTIVITY_TiraFoto3
    if (resultCode == RESULT_OK) {

      // testa as confições necessária para continuar processando a foto
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

      // operação cancelada - indica que nenhuma foto foi tirada.
      Log.w(TAG, "resultActivityTiraFoto3() - Operação tirar foto cancelada pelo usuário");

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
    // uma foto foi tirada e encontra-se no endereço dado pela URI xUri
    // exibe informações sobre a localização da foto armazenada no
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

    // atualiza a máquina de estado
    setEstado(2);

    // Executa o próximo passo da máquina de estado
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
      Log.w(TAG, "testaCondicoesFotos() - xUri é nulo ==> nenhuma foto foi obtida");
      estadoFinal();
    }

    if (mParticipacao == null) {
      Log.w(TAG, "testaCondicoesFotos() - mParticipação é nulo");
      estadoFinal();
    }

    if (data != null) {
      FileUtils.showBundle(data.getExtras());
    }

  }

  /**
   * gravaFotoOriginal()
   * 
   * Grava foto original
   * 
   * Usa a variável xUri
   */
  private void gravaFotoOriginal() {

    // grava a foto original em um arquivo
    boolean gravou = ManipulaImagem.gravaBitmapArquivo3(xUri);

    if (gravou) {

      Log.i(TAG, "gravaFotoOriginal() - arquivo: " + xUri + " gravado com sucesso.");

    } else {

      Log.w(TAG, "gravaFotoOriginal() - falha na gravação do arquivo: " + xUri);

      xUri = null;
      mFilename = null;
      estadoFinal();

    }

  }

  /**
   * processaFotos()
   * 
   * Responsável pelo processamento da foto, isto é, transformar a foto de
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
      // URI da foto não está disponível
      Log.d(TAG, "processaFotos() - não há nenhuma foto na Uri fornecida");
      estadoFinal();
    }

    if (mParticipacao == null) {
      //
      Log.d(TAG, "processaFotos() - não é possível obter as informações do participante");
      estadoFinal();
    }

    if (mEvento == null) {
      Log.d(TAG, "processaFotos() - não é possível obter as informações sobre o evento");
      estadoFinal();
    }

    // exibe informações sobre a Uri de localização da foto
    ManipulaImagem.showUri(xUri);

    // obtém o tipo da foto (se o formato da foto é Polaroid ou Cabine)
    tipoFoto = mParticipacao.getTipoFoto();

    // obtem o efeito para aplicação na foto (se a foto deve ser a cores ou
    // P&B)
    efeitoFoto = mParticipacao.getEfeitoFoto();

    String pathCabine = mEvento.getBordaCabine();

    // --------

    /**
     * Processa a foto com o filtro desejado
     */
    // TODO será que esse processo não deveria ser feito depois ???
    Bitmap bb = processoEfeitoFiltroFoto(efeitoFoto, null);

    // --------

    // nome do arquivo contendo a foto formatada como Polaroid ou Cabine já
    // com a moldura aplicada
    String arquivoFotoComMoldura = null;

    arquivoFotoComMoldura = processaTipoFoto(tipoFoto, pathCabine);

    // -------------------------------------------------------------------------

    // TODO aqui poderíamos ter um passo intermediário na máquina de estados

    // TODO aqui também é necessário atualizar a Uri da foto processada

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

    // O próximo passo é enviar o email com a foto já trabalhada.
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
      Log.w(TAG, "processaFotos() - tipo de foto: " + tipoFoto + " não suportado.");
      estadoFinal();

    }

    return arquivoFotoComMoldura;

  }

  /**
   * processoEfeitoFiltroFoto(int efeitoFoto)
   * 
   * Aplica um efeito ("filtro") a um bitmap obtendo outro bitmap com o
   * resultado final.
   * 
   * @param efeitoFoto
   *          "efeito" para aplicar a foto
   * 
   */
  private Bitmap processoEfeitoFiltroFoto(int efeitoFoto, Bitmap bitmap) {

    Bitmap novoBitmap = null;

    if (efeitoFoto == CORES) { // aplica efeito cores COR
      // TODO processa o efeito cores
      novoBitmap = ManipulaImagem.aplicaFiltroCores(bitmap);
      //
    } else if (efeitoFoto == PB) { // aplica efeito P&B
      // TODO processa o efeito P&B, isto é, aplicaca um filtro P&B à foto
      novoBitmap = ManipulaImagem.aplicaFiltroPB(bitmap);
      //
    } else {
      Log.w(TAG, "Efeito: " + efeitoFoto + " não é suportado pela aplicação");
    }

    return novoBitmap;

  }

  /**
   * formataFotoPolaroid(Bitmap bmMoldura)
   * 
   * A foto formato Polaroid exige o redimensionamento da foto bem como a
   * inclusão da moldura.
   * 
   * Observe que a moldura está relacionada ao evento em andamento. Portanto é
   * necessário ter informações sobre o evento
   * 
   * Pega a foto, redimensiona-a e aplica a moldura criando um novo bitmap.
   * 
   * @param bmMoldura
   *          Bitmap contendo a moldura da foto
   * 
   * @return o nome do arquivo onde a foto pronta está gravado
   * 
   */
  private String formataFotoPolaroid(Bitmap bmMoldura) {

    Log.i(TAG, "formataFotoPolaroid() - Foto tipo POLAROID foi selecionada");

    if (bmMoldura == null) {
      Log.w(TAG, "formataFotoPolaroid() - bmMoldura é nula");
      return null;
    }

    // Cria um bitmap a partir da Uri da foto
    Bitmap foto = ManipulaImagem.criaBitmap(xUri);

    if (foto == null) {
      Log.w(TAG, "formataFotoPolaroid() - foto é nula");
      return null;
    }

    // Exibe informações a respeito da foto
    ManipulaImagem.showBitmapInfo(foto);

    // Redimensiona a foto para o formato Polaroid
    // Bitmap fotoRedimensionada = imagem.getScaledBitmap2(foto, 228, 302);

    // --------------------------------------------------------------------------
    // redimensiona a foto original para 9x12 para manter a proporção 3:4
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
      // falha na gravação da foto
      Log.i(TAG, "formataFotoPolaroid() - (Foto9x12) - falha na gravação do arquivo: " + arqSaida);
    }

    // --------------------------------------------------------------------------
    // redimensiona a foto 9x12 para 8x8, isto é, copia uma "janela" 8x8 da
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
      // falha na gravação da foto
      Log.i(TAG, "formataFotoPolaroid() - (Foto8x8) - falha na gravação do arquivo: " + arqSaida);
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
        Log.w(TAG, "gravaFotoPolaroidComMoldura() - erro na conversão da foto");
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

      // falha na gravação da foto
      Toast.makeText(this, "Falha na gravação da foto no arquivo: " + arqSaida, Toast.LENGTH_SHORT).show();
      Log.d(TAG, "gravaFotoPolaroidComMoldura() - Falha na gravação da foto no arquivo: " + arqSaida);

    }

    return arqSaida;

  }

  /**
   * formataFotoCabine(String pathCabine)
   * 
   * foto formato Cabibe exige três fotos. as fotos serão montadas em sequencoa
   * e será aplicada. uma moldura (conforme configuração). observe que a moldura
   * está relacionada ao evento em andamento. portanto é necessário ter
   * informações sobre o evento.
   * 
   * @param pathCabine
   */
  private String formataFotoCabine(String pathCabine) {

    if (pathCabine == null) {
      Log.d(TAG, "formataFotoCabine() - Não há moldura definida para foto CABINE");
    }

    // TODO verificar nesse ponto se há três fotos disponível

    // TODO veja que aqui são necessárias 3 fotos
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
   * foto formato Cabibe exige três fotos. as fotos serão montadas em sequencoa
   * e será aplicada. uma moldura (conforme configuração). observe que a moldura
   * está relacionada ao evento em andamento. portanto é necessário ter
   * informações sobre o evento.
   * 
   * @param pathCabine
   */
  private String formataFotoCabine2(Bitmap bmMoldura) {

    Log.d(TAG, "processaFotos() - Foto tipo CABINE foi selecionada");

    if (bmMoldura == null) {
      Log.d(TAG, "formataFotoCabine2() - Não há moldura definida para foto CABINE");
    }

    // TODO verificar nesse ponto se há três fotos disponível

    // TODO veja que aqui são necessárias 3 fotos
    // as fotos podem ser armazenadas em um array de fotos

    // Cria um bitmap a partir da Uri da foto
    Bitmap foto = ManipulaImagem.criaBitmap(xUri);

    // Exibe informações a respeito da foto
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
   * Redimensiona uma foto para o tamanho 3x4 (observe que nesse caso 3 é a
   * largura e 4 é a altura)
   * 
   * @param foto
   *          bitmap com a foto
   * 
   * @param nome
   *          nome do arquivo onde a foto será gravada
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
      // falha na gravação da foto
      Log.w(TAG, "formataFoto3x4() - falha na gravação da foto");
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

    // Exibe informações a respeito da foto
    ManipulaImagem.showBitmapInfo(foto);

    // primeiro, scala a foto para 9x12 cm para manter a proporção
    Bitmap foto9x12 = ManipulaImagem.getScaledBitmap2(foto, 340, 454);

    // Define o nome da foto redimensionada
    String nomeArquivo = PATH_FOTOS + "polaroid-escalada.png";

    // grava a foto redimensionada em um arquivo
    boolean gravou = ManipulaImagem.gravaBitmapArquivo(foto9x12, nomeArquivo);

    // redimensiona a foto 9x12 para 8x8, isto é, copia uma "janela" 8x8 da
    // foto

    Options options = new Options();

    Rect rect = new Rect(0, 0, 302, 302);

    //
    foto9x12 = ManipulaImagem.getBitmapRegion(nomeArquivo, rect, options);

    if (gravou) {
      // foto armazenada com sucesso
    } else {
      // falha na gravação da foto
    }

    Bitmap fotoComMoldura = null;

    if ((foto9x12 != null) && (bmMoldura != null)) {

      fotoComMoldura = ManipulaImagem.overlay4(foto9x12, bmMoldura);

      if (fotoComMoldura == null) {
        Log.w(TAG, "formataFotoPolaroid() - erro na conversão da foto");
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
      // falha na gravação da foto
      Toast.makeText(this, "Falha na gravação da foto", Toast.LENGTH_SHORT).show();
    }

    // o nome completo do arquivo onde a foto com moldura foi armazenada
    return nomeArquivo;

  }

  /**
   * enviaEmail(Uri lastUri)
   * 
   * Inicia o processo de envio de email.
   * 
   * Verifica inicialmente se todas as condições necessárias estão satisfeita
   * para o envio do email com a foto anexada.
   * 
   * Usa: mParticipante e mContratante
   * 
   * @param lastUri
   *          Uri onde a foto está armazenada
   * 
   */
  private void enviaEmail(Uri lastUri) {

    boolean erro = false;

    if (getEstado() < 2) {
      Log.d(TAG, "enviaEmail() - Foto não foi tirada");
      erro = true;
    }

    if (mParticipante == null) {
      // Log.d(TAG, "mParticipante é null");
      Log.w(TAG, "enviaEmail() - Não há informações sobre o participante");
      erro = true;
    }

    if (mContratante == null) {
      // Log.d(TAG, "mContrante é null");
      Log.w(TAG, "enviaEmail() - Não há informações sobre o contratante");
      erro = true;
    }

    if (xUri == null) {
      // Log.d(TAG, "xUri é null");
      Log.w(TAG, "enviaEmail() - não há foto");
      erro = true;
    }

    if (erro) {
      // TODO o que fazer ???
    }

    // carrega as preferências sobre o envio de email
    SharedPreferences emailPreferences = getSharedPreferences("pref_email", MODE_PRIVATE);

    if (emailPreferences == null) {
      Log.w(TAG, "enviaEmail() - SharedPreferences não foi encontrada.");
    }

    /**
     * Assunto do email
     * 
     * Recupera o "subject" do email do arquivo de preferências
     */
    String subject = emailPreferences.getString("preferencias_assunto", "Evento Inicial");

    /**
     * Corpo do email
     * 
     * Recupera o "corpo" do email do arquivo de preferências
     * 
     */
    String body = emailPreferences.getString("preferencias_descricao", "Segue as informações sobre o evento");

    emailPreferences = null;

    // obtém o email do participante do evento
    String to = mParticipante.getEmail();

    // obtém o email do contratante do evento
    // ele será copiado em BCC no email enviado
    String cc = mContratante.getEmail();

    // envia o email
    sendEmail(to, cc, subject, body, lastUri);

  }

  /**
   * sendEmail(String emailParticipante, String emailContratante, String
   * subject, String text, Uri imageURI)
   * 
   * @param emailParticipante
   *          Endereço de email do participante do evento
   * 
   * @param emailContratante
   *          Endereço de email do contratante do evento
   * 
   * @param subject
   *          String usada como "Subject" do email
   * 
   * @param text
   *          String usada como "Body" do email (o conteúdo da mensagem)
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
      // email do participante não pode ser vazio
    }

    // Bcc:
    if (emailContratante != null) {
      // email do contratante foi fornecido (BCC:)
      emailIntent.putExtra(Intent.EXTRA_BCC, new String[] { emailContratante });
    } else {
      // email do contratante do evento não pode ser vazio
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
   * Permite escolher qual aplicação será usada para o envio de email
   * 
   * @param emailIntent
   *          Intent
   * 
   */
  private void sendEmailByChooser(Intent emailIntent) {

    //
    // TODO aqui pode acontecer de ser necessário forçar a aplicação de
    // email
    //

    // Cria uma intent do tipo chooser a partir da intent recebida
    Intent chooser = Intent.createChooser(emailIntent, "Selecione sua aplicação de email !");

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
    // TODO aqui é necessário fixa o uso do email como mecanismo de envio
    // TODO talvez seja necessário permitir o envio via Facebook e Twitter
    // também

    sendEmailRedesSociais();

    startActivityForResult(chooser, ACTIVITY_CHOOSER);

  }

  /**
   * sendEmailRedesSociais()
   * 
   * Usa mEvento (informações sobre o evento).
   * 
   */
  private void sendEmailRedesSociais() {

    if (mEvento == null) {
      Log.d(TAG, "sendEmailRedesSociais() - Não foi possível obter os dados do evento.");
      return;
    }

    // TODO talvez pudesse ser feito após o envio do email ???

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
   * Processa o resultado do envio do email (execução da activity)
   * 
   * @param resultCode
   *          Resultado a execução da activity
   * @param data
   *          Intent retornada
   * 
   */
  private void resultActivityChooser(int resultCode, Intent data) {

    Log.d(TAG, "resultActivityChooser() ===> processando resultado da ACTIVITY CHOOSER: resultCode=" + resultCode);

    // Obtém o intent usado para execução da activity de envio de email
    Intent intent = this.getIntent();

    if (intent != null) {
      ComponentName compName = intent.getComponent();
      if (compName != null) {
        Log.d(TAG, "compName=" + compName.getClassName() + ", compName=" + compName.getPackageName());
      }
    }

    // verifica os dados retornados pela Activity que enviou o email
    if (data != null) {

      Log.w(TAG, "resultActivityChooser() - dados recebidos não foram trabalhados");

      // The action of this intent or null if none is specified.
      Log.w(TAG, "resultActivityChooser() - data.getAction()=" + data.getAction());

    }

    if (resultCode == 0) {

      Log.d(TAG, "ACTIVITY_CHOOSER - email enviado com sucesso");

      // mensagem exibida após envio de email
      Toast.makeText(this, "Email enviado com sucesso", Toast.LENGTH_LONG).show();

      // Atualiza o estado da máquina de estado
      setEstado(3);

      // Processa o próximo estado
      estadoFinal();

    } else if (resultCode == RESULT_CANCELED) {
      // envio do email foi cancelado pelo usuário
      Log.d(TAG, "resultActivityChooser() - ACTIVITY_CHOOSER - o envio de email foi cancelado");
      estadoFinal();

    } else {

      Log.d(TAG, "resultActivityChooser() - ACTIVITY_CHOOSER - resultCode " + resultCode + " não pde ser tratado");
      estadoFinal();

    }

  }

  /**
   * estadoFinal()
   * 
   * Representa o estado final da máquina de estado.
   * 
   * É também o responsável pela finalização da activity, estabelecendo seu
   * resultado.
   * 
   * Se o estado final foi atingido então o processo correu segundo esperado.
   * 
   */
  private void estadoFinal() {

    // Obtem as informações sobre a Intent "chamadora"
    Intent intent = getIntent();

    // Obtém o estado corrente da máquina de estado
    int estadoCorrente = getEstado();

    if (estadoCorrente == 3) {

      // estado final atingido com sucesso
      Log.i(TAG, "estadoFinal() - final do processamento");

      // retorna as informações sobre o participante a sobre sua
      // participação
      intent.putExtra(PARTICIPANTE, mParticipante);
      intent.putExtra(PARTICIPACAO, mParticipacao);

      intent.putExtra("br.com.mltech.result", "OK");

      // estabelece o resultado da execução da Activity
      setResult(RESULT_OK, intent);

    } else {

      Toast.makeText(this, "Falha no processo. Estado atual: " + estadoCorrente, Toast.LENGTH_SHORT).show();

      // estado final atingido porém houve falha
      Log.w(TAG, "estadoFinal() - não foi possível chegar ao final do processamento.");

      intent.putExtra("br.com.mltech.result", "NOT_OK");

      // estabelece o resultado da execução da Activity
      setResult(RESULT_CANCELED, intent);

    }

    atualizaModoTela(Configuration.ORIENTATION_PORTRAIT);

    // Termina a execução da Activity responsável por tirar e enviar uma
    // foto
    finish();

  }

  /**
   * setEstado(int e)
   * 
   * Atualiza o estado da uma máquina de estados
   * 
   * @param e
   *          novo estado (próximo estado)
   */
  private void setEstado(int e) {

    Log.i(TAG, "----------------------------------------------------------");
    Log.i(TAG, "Transição do estado: " + mEstado + " para o estado: " + e);
    Log.i(TAG, "----------------------------------------------------------");
    mEstado = e;
  }

  /**
   * getEstado()
   * 
   * Obtém o estado atual da máquina de estados
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
    Log.d(TAG, "*** onRestart() - A aplicação foi restartada ... ***");
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
     * Essa atribuição não deve ser feita pois perdemos o valor de xUri que será
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
      Log.w(TAG, "onRestoreInstanceState() - savedInstaceState é nulo");
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
   * Exibe uma caixa de diálogo com uma mensagem e um botão de fechar a janela
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
    Log.v(TAG, "showVariables() - Variáveis de atributo:");

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
   * Tenta obter uma instância da câmera identificada por seu número (cameraID).
   * A primeira câmera, se existir terá o identificador 0.
   * 
   * @param cameraID
   *          Identificador da câmera do dispositivo
   * 
   * @return Uma instância da classe Camera ou null em caso de erro
   * 
   */
  public static Camera getCameraInstance(int cameraID) {

    Camera c = null;

    try {

      /*
       * Cria um novo objeto Camera para acesso a câmera cameraID. Se a câmera
       * já estiver em uso ou não existir retorma nulo
       */

      c = Camera.open(cameraID); // attempt to get a Camera instance

    } catch (Exception e) {

      // Camera is not available (in use or does not exist)
      Log.w(TAG, ">>> getCameraInstance(" + cameraID + ") - Câmera está indisponível (está em uso ou não existe)", e);

    }

    return c; // returns null se a câmera estiver indisponível

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
      Log.w(TAG, "leArquivoMoldura() - arquivoMoldura: [" + arquivoMoldura + "] não existe.");
      return null;
    }

    // lê o bitmap contendo a moldura
    Bitmap bmMoldura = ManipulaImagem.getBitmapFromFile(moldura);

    if (bmMoldura == null) {

      Log.w(TAG, "leArquivoMoldura() - arquivo contento a moldura está vazio.");
      return null;

    } else {
      Log.v(TAG, "leArquivoMoldura() - largura x altura da moldura: " + ManipulaImagem.getStringBitmapSize(bmMoldura));

      return bmMoldura;

    }

  }

  /**
   * getSharedPreference(String sharedPreferencesName, String attribute)
   * 
   * Obtém um parâmetro da lista de preferências.
   * 
   * @param sharedPreferencesName
   * @param attribute
   * 
   * @return o valor do atributo solicitado ou null caso haja algum erro (o
   *         parâmetro não foi encontrado)
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
   * Exibe o valor de algumas variáveis selecionadas
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
   * Processa a ação disparada pelo pressionamento do botão Confirma
   * 
   */
  private void processaBotaoConfirma() {

    Log.d(TAG, "");
    Log.d(TAG, "============================");
    Log.d(TAG, "===> processaBotaoConfirma()");
    Log.d(TAG, "============================");

    int opcao = 3;

    // nome do arquivo onde a foto será armazenada
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
      Log.w(TAG, "processaBotaoLista() - lista está vazia !");
      return;
    }

    Log.d(TAG, "processaBotaoLista() - nº de fotos: " + listaFotos.size());

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
   *          nome do arquivo onde a foto será armazenada
   * 
   */
  private void executaActivityTiraFoto(String arquivo) {

    file = new File(arquivo);

    outputFileUri = Uri.fromFile(file);

    Log.d(TAG, "executaActivityTiraFoto() - arquivo=" + file.getAbsolutePath());

    // cria um intent com a ação MediaStore.ACTION_IMAGE_CAPTURE
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    // especifica o parâmetro com a URI onde a foto deve ser armazenada
    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

    // inicia a Activity com requestCode TIRA_FOTO
    startActivityForResult(intent, TIRA_FOTO);

  }

  /**
   * executaActivityTiraFotoDummy(String arquivo)
   * 
   * Executa a Activity que tira uma foto de "mentirinha", isto é, apenas
   * retorna uma imagem pre-armazenada como se fosse uma foto
   * 
   * @param arquivo
   *          nome do arquivo onde a foto será armazenada
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
   *          resultado da execução da activity
   * 
   * @param data
   *          dados retornados da execução da activity
   * 
   */
  private void processaActivityResultTiraFoto(int resultCode, Intent data) {

    if (data == null) {
      Log.w(TAG, "processaActivityResultTiraFoto() - data (Intent) é vazia");
      return;
    }

    if (resultCode == RESULT_CANCELED) {

      // operação cancelada
      Log.w(TAG, "processaActivityResultTiraFoto() - Operação cancelada pelo usuário");
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

      // operação cancelada
      Log.w(TAG, "processaActivityResultTiraFoto() - Operação cancelada pelo usuário");

    }

  }

  /**
   * criaBitmapFotoCabine(FotoCabine pFotoCabine)
   * 
   * Cria os bitmap das foto armazenadas no objeto FotoCabine
   * 
   * @param pFotoCabine
   *          instância da classe FotoCabine
   */
  public void criaBitmapFotoCabine(FotoCabine pFotoCabine) {

    if (pFotoCabine == null) {
      Log.w(TAG, "showFotoCabine() - fotoCabine é nula");
      return;
    }

    // contador local de fotos processadas
    int i = 0;

    //
    // percorre as fotos armazenada em FotoCabine
    //
    for (Foto foto : pFotoCabine.getFotos()) {

      // exibe informações sobre a foto
      Log.v(TAG, "criaBitmapFotoCabine() - processando foto[" + i + "] = " + pFotoCabine.toString());
      Log.d(TAG, "criaBitmapFotoCabine() - processando foto[" + i + "] = " + foto);

      if (foto != null) {

        // decodifica o bitmap referente ao arquivo com a foto
        Bitmap bm = BitmapFactory.decodeFile(foto.getArquivo());

        if (bm == null) {
          Log.d(TAG, "showFotoCabine() - não foi possível decodificar a foto a partir do arquivo " + foto.getArquivo()
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
   * Esse Carrega uma imagem a partir de um arquivo
   * 
   * Decodifica a imagem armazenada no arquivo e tenta criar um bitmap que será
   * redimencionado e exibido.
   * 
   * Cria-se uma foto e acrescenta-a a lista de fotos tiradas.
   * 
   */
  private void carregaImagem() {

    // TODO avaliar a necessidade desse método

    Log.d(TAG, "carregaImagem()");

    // atualiza o nº de fotos carregadas
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
   * Recebe o endereço de uma foto redimensiona a foto insere a moldura e
   * transforma a foto no tamanho 9x11
   * 
   * @return um bitmap contendo a foto com a moldura
   * 
   *         TODO Esse método gera dois arquivos contendo fotos intermediários
   *         que poderiam ser descartadas
   * 
   */
  public Bitmap formatarPolaroid(Uri uriFotoOriginal) {

    Log.d(TAG, "formatarPolaroid() - uriFotoOriginal: " + uriFotoOriginal);

    // Cria um bitmap a partir da Uri da foto
    Bitmap bmFotoOriginal = ManipulaImagem.criaBitmap(uriFotoOriginal);

    if (bmFotoOriginal == null) {
      Log.d(TAG, "formatarPolaroid() - bmFotoOriginal: " + bmFotoOriginal);
      return null;
    }

    // redimensiona a foto original para 9x12 para manter a proporção 3:4
    Bitmap bmFoto9x12 = ManipulaImagem.getScaledBitmap2(bmFotoOriginal, 340, 454);

    if (bmFoto9x12 == null) {
      Log.d(TAG, "formatarPolaroid() - bmFoto9x12: " + bmFoto9x12);
      return null;
    }

    // Define o nome da foto redimensionada
    String nomeArquivo = PATH_FOTOS + FileUtils.getFilename(uriFotoOriginal) + "_9x12.png";

    boolean gravou = ManipulaImagem.gravaBitmapArquivo(bmFoto9x12, nomeArquivo);

    if (!gravou) {
      Log.d(TAG, "formatarPolaroid() - foto não pode ser gravada");
      return null;
    }

    // redimensiona a foto 9x12 para 8x8, isto é, copia uma "janela" 8x8 da
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
   * Atualiza a orientação atual da tela para outro modo (se necessário).
   * 
   * @param novaOrientacao
   *          nova orientação
   */
  private void atualizaModoTela(int novaOrientacao) {

    // obtém a orientação atual da tela
    int orientacaoAtual = this.getResources().getConfiguration().orientation;

    // exibe a orientação atual da tela
    Log.d(TAG, "atualizaModoTela() - Orientação atual: " + orientacaoAtual + " - " + getScreenOrientation(orientacaoAtual));

    if (novaOrientacao != orientacaoAtual) {
      // muda a orientação
      this.setRequestedOrientation(novaOrientacao);
      // exibe a nova orientação
      Log.d(TAG, "atualizaModoTela() - nova orientação: " + novaOrientacao + " - " + getScreenOrientation(novaOrientacao));
    }

  }

  /**
   * getScreenOrientation(int orientacao)
   * 
   * Retorna uma string com o nome da orientação da tela: Landscape ou Portrait
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
      s = "Não suportado";
    }

    return s;

  }

  /**
   * 
   * @param bmFoto1
   * @param bmFoto2
   * @param bmFoto3
   * @param bmMoldura
   * @return
   */
  public static Bitmap processaFotoFormatoCabine3(Bitmap bmFoto1, Bitmap bmFoto2, Bitmap bmFoto3, Bitmap bmMoldura) {

    if (bmMoldura == null) {
      Log.w(TAG, "processaFotoFormatoCabine3() - Moldura formato Cabine está vazia !");
      return null;
    }

    Bitmap bmImgJoin = mergeFotosCabine(bmFoto1, bmFoto2, bmFoto3);

    if (bmImgJoin == null) {
      Log.w(TAG, "processaFotoFormatoCabine3() - não foi possível fazer o merge das três fotos !");
      return null;
    }

    // Obtém uma imagem redimensionada
    Bitmap scaledBitmap = ManipulaImagem.getScaledBitmap2(bmImgJoin, 113, 453);

    if (scaledBitmap == null) {
      //
      Log.w(TAG, "processaFotoFormatoCabine3() - não foi possível redimensionar a foto");
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
     * "processaFotoFormatoCabine2() - Falha na gravação da imagem com moldura no arquivo: "
     * + arqSaida); return null; }
     * 
     * return arqSaida;
     */

  }

  /**
   * mergeFotosCabine(Bitmap bmFoto1, Bitmap bmFoto2, Bitmap bmFoto3)
   * 
   * @param bmFoto1
   * @param bmFoto2
   * @param bmFoto3
   * 
   * @return a bitmap com as fotos ou null em caso de algum erro
   */
  private static Bitmap mergeFotosCabine(Bitmap bmFoto1, Bitmap bmFoto2, Bitmap bmFoto3) {

    if (bmFoto1 == null) {
      Log.w(TAG, "processaFotoFormatoCabine3() - Foto1 está vazia !");
      return null;
    }

    if (bmFoto2 == null) {
      Log.w(TAG, "processaFotoFormatoCabine3() - Foto2 está vazia !");
      return null;
    }

    if (bmFoto3 == null) {
      Log.w(TAG, "processaFotoFormatoCabine3() - Foto3 está vazia !");
      return null;
    }

    // =========================================================================
    // Cria um novo bitmap a partir da composição das 3 fotos
    // A foto será repetida na vertical, isto é, uma nova foto
    // será colocada abaixo da outra.
    // =========================================================================
    Bitmap bmImgJoin = ManipulaImagem.verticalJoin(bmFoto1, bmFoto2, bmFoto3);

    if (bmImgJoin != null) {

      Log.i(TAG, "processaFotoFormatoCabin3() - Imagens foram juntadas com sucesso");
      Log.v(TAG, "processaFotoFormatoCabine3() -  ==> Tamanho da foto após join: " + ManipulaImagem.getStringBitmapSize(bmImgJoin));

    } else {

      Log.w(TAG, "processaFotoFormatoCabine3() - Erro no merge das três fotos");
      return null;

    }

    return bmImgJoin;

  }

}
