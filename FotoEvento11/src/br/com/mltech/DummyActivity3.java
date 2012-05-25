package br.com.mltech;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
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
import br.com.mltech.modelo.Participacao;
import br.com.mltech.modelo.Participante;
import br.com.mltech.utils.ManipulaImagem;

/**
 * DummyActivity3
 * 
 * Activity responsável pela ...
 * 
 * @author maurocl
 * 
 */
public class DummyActivity3 extends Activity implements Constantes {

  private static final String TAG = "DummyActivity3";

  // TODO criar um diretório inicial, um lugar para configuração ...
  public static final String PATH_MOLDURAS = "/mnt/sdcard/Pictures/fotoevento/molduras/";
  public static final String PATH_FOTOS = "/mnt/sdcard/Pictures/fotoevento/fotos/";

  // Definição da Activies chamadas
  private static final int ACTIVITY_TIRA_FOTO_3 = 113;
  private static final int ACTIVITY_CHOOSER = 150;
  private static final int ACTIVITY_PARTICIPANTE = 102;

  // Definição dos atributos da aplicação
  private static Contratante mContratante;
  private static Evento mEvento;
  private static Participante mParticipante;
  private static Participacao mParticipacao;

  //
  private SharedPreferences mPreferences;

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

  // TODO essa variável deve ser lida do arquivo de configurações
  // Nº da câmera corrente em uso (se houver)
  private static int mCurrentCamera = -1;

  // Biblioteca de funções para manipulação de imagens
  private ManipulaImagem imagem = null;

  // Bitmaps contendo as molduras
  private static Bitmap mBitmapMolduraPolaroid;
  private static Bitmap mBitmapMolduraCabine;

  // Contador do nº de fotos cabine
  private static int mNumFotosCabine = 0;

  /**
   * onCreate(Bundle savedInstanceState)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    Log.d(TAG, "*** onCreate() ***");

    setContentView(R.layout.dummy);

    imagem = new ManipulaImagem();

    if (imagem == null) {
      Log.w(TAG, "onCreate() - Não foi possível obter uma instância da classe ManipulaImagm");
    }

    showBundle(savedInstanceState);

    // ---------------------------------------------------------------------
    mPreferences = getSharedPreferences("preferencias", MODE_PRIVATE);

    String molduraPolaroid = null;
    String molduraCabine = null;

    if (mPreferences == null) {

      Log.w(TAG, "mPreferences is null. Falha na execução do comandos getSharedPreferences()");
     // molduraPolaroid = PATH_MOLDURAS + "moldura-polaroid-340x416-red.png";
     // molduraCabine = PATH_MOLDURAS + "moldura-cabine-132x568-red.png";

    } else {

      molduraPolaroid = mPreferences.getString("evento_borda_polaroid", "");
      molduraCabine = mPreferences.getString("evento_borda_cabine", "");

    }

    mBitmapMolduraPolaroid = leArquivoMoldura(molduraPolaroid);
    mBitmapMolduraCabine = leArquivoMoldura(molduraCabine);

    Log.d(TAG, "mBitmapMolduraPolaroid =" + mBitmapMolduraPolaroid);
    Log.d(TAG, "mBitmapMolduraCabine = " + mBitmapMolduraCabine);

    // obtem o nº da câmera usada para tirar as fotos

    String s = getSharedPreference("pref_email", "preferencias_num_camera_frontal");

    Log.w(TAG,"onCreate() - Nº da câmera frontal="+s);
    
    if ( (s != null) && (!s.equals(""))) {
      mCurrentCamera = Integer.valueOf(s);      
    } else {
      mCurrentCamera=0;
      Log.w(TAG,"onCreate() - Nº da câmera frontal não foi definido. Assumindo o valor 0");
    }

    // ---------------------------------------------------------------------

    // incrementa o nº de vezes que a activity foi reiniciada
    mContador++;
    
    iniciaProcesso();
    
/*
    Button btn0 = (Button) findViewById(R.id.btn0);

 
    btn0.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        boolean b = iniciaVariaveis();
        if (b == false) {
          Log.w(TAG, "onCreate() - Não foi possível inicializar as variáveis");
          estadoFinal();
        }

      }

    });

*/  }
  
  /**
   * iniciaProcesso()
   */
  private void iniciaProcesso() {
    
    boolean b = iniciaVariaveis();
    
    if (b == false) {
      Log.w(TAG, "iniciaProcesso() - Não foi possível inicializar as variáveis");
      estadoFinal();
    }
    
  }

  /**
   * onActivityResult(int requestCode, int resultCode, Intent data)
   * 
   * Trata o resultado da execução das Activities
   * 
   */
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    Log.i(TAG, "onActivityResult(request " + requestCode + ", result=" + resultCode + ", data " + data + ") ...");

    if (requestCode == ACTIVITY_PARTICIPANTE) {

      resultActivityParticipante(resultCode, data);

    } else if (requestCode == ACTIVITY_TIRA_FOTO_3) {

      resultActivityTiraFoto3(resultCode, data);

    } else if (requestCode == ACTIVITY_CHOOSER) {

      resultActivityChooser(resultCode, data);

    } else {

      Log.w(TAG, "onActivityResult() - Erro ... requestCode: " + requestCode + " não pode ser processado");

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

    // obtem o nº de câmeras disponíveis pelo dispositivo onde a aplicação está
    // em execução
    numCameras = android.hardware.Camera.getNumberOfCameras();

    Log.d(TAG, "iniciaVariaveis() - Número de Câmeras disponíveis no hardware: " + numCameras);

    // verifica se a câmera fotogrática está em operação
    if (isCameraWorking(mCurrentCamera)) {

      Log.i(TAG, "iniciaVariaveis() - Camera: [" + mCurrentCamera + "] está em funcionamento...");

    } else {

      Log.w(TAG, "iniciaVariaveis() - Camera: [" + mCurrentCamera + "] não está em funcionamento");

      Toast.makeText(this, "Camera não está disponível", Toast.LENGTH_SHORT);

      return false;

    }

    // Obtem a Intent que iniciou esta Activity
    Intent i = this.getIntent();

    if (i == null) {
      Log.w(TAG, "iniciaVariaveis() - Erro gravíssimo !!!");
    }

    // indicador de erro de configuração
    int erro = 0;

    if (i.getSerializableExtra("br.com.mltech.contratante") != null) {
      mContratante = (Contratante) i.getSerializableExtra("br.com.mltech.contratante");
    } else {
      Log.w(TAG, "iniciaVariaveis() - Contratante não pode ser nulo.");
      Toast.makeText(this, "Contratante não pode ser nulo", Toast.LENGTH_SHORT).show();
      erro = 1;
    }

    if (i.getSerializableExtra("br.com.mltech.evento") != null) {
      mEvento = (Evento) i.getSerializableExtra("br.com.mltech.evento");
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

    Log.d(TAG, "mContratante=" + mContratante);
    Log.d(TAG, "mEvento=" + mEvento);

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

    // Cria uma nova Intent
    Intent intentParticipante = new Intent(this, ParticipanteActivity.class);

    // Inclui o parâmetro mEvento (com as informações sobre o evento em curso)
    intentParticipante.putExtra("br.com.mltech.evento", mEvento);

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

    showBundle(data.getExtras());

    if (data.getSerializableExtra("br.com.mltech.participante") != null) {
      mParticipante = (Participante) data.getSerializableExtra("br.com.mltech.participante");
    }

    if (data.getSerializableExtra("br.com.mltech.participacao") != null) {
      mParticipacao = (Participacao) data.getSerializableExtra("br.com.mltech.participacao");
    }

    // Exibe as informações sobre o participante e sua participaçaõ
    Log.d(TAG, "mParticipante=" + mParticipante);
    Log.d(TAG, "mParticipacao=" + mParticipacao);

    // Atualiza o estado da máquina de estados
    setEstado(1);
    // Processa o próximo estado
    obtemFoto();

  }

  /**
   * obtemFoto()
   * 
   * Executa a Intent ACTION_IMAGE_CAPTURE a fim de obter uma foto da câmera
   * 
   * A imagem capturada será armazenada na variável xUri
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
    mFilename = null; // nome do arquivo (full path) onde a foto está armazenada

    // TODO poderia ser substituido por isCameraWorking ???

    Log.d(TAG, "obtemFoto() - mCurrentCamera: " + mCurrentCamera);

    // Obtém informações sobre a câmera (atualmente configurada)
    // c = getCameraInstance(mCurrentCamera);

    // if(c==null) {
    // não foi possível conectar-se a câmera
    // Log.w(TAG,"Não foi possível conectar-se a câmera: "+mCurrentCamera);
    // estadoFinal();
    // }

    if (isCameraWorking(mCurrentCamera)) {
      Log.i(TAG, "obtemFoto() - instância da Camera obtida com sucesso");
    } else {
      Log.w(TAG, "obtemFoto() - Erro da obtenção da instância da Camera. Processo abortado !");
      estadoFinal();
    }

    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

    // File file = new File(PATH_FOTOS + "/" + System.currentTimeMillis() +
    // ".png");

    // arquivo com o nome da foto
    File file = new File(PATH_FOTOS + "/" + timeStamp + ".png");

    Log.w(TAG, "obtemFoto() - f.getAbsolutePath()=" + file.getAbsolutePath());

    if (file.exists()) {
      Log.w(TAG, "obtemFoto() - ARQUIVO EXISTE !!!");
    } else {
      Log.w(TAG, "obtemFoto() - ARQUIVO não EXISTE !!!");
    }

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

    // --------------------------------------------------------------

    // --------------------------------------------------------------
    // cria a Intent ACTION_IMAGE_CAPTURE para obter uma foto
    // --------------------------------------------------------------
    Intent intentTiraFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    // --------------------------------------------------------------
    // Passa como parâmetro a Uri onde a foto deve ser gravada
    intentTiraFoto.putExtra(MediaStore.EXTRA_OUTPUT, xUri);
    // --------------------------------------------------------------

    // --------------------------------------------------------------
    // Inicia a Activity
    // cria e inicia a Intent ACTION_IMAGE_CAPTURE
    // --------------------------------------------------------------
    startActivityForResult(intentTiraFoto, ACTIVITY_TIRA_FOTO_3);

    // --------------------------------------------------------------

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

    if (resultCode == RESULT_OK) {

      // activity executada com sucesso

      Log.i(TAG, "resultActivityTiraFoto3() - xUri: " + xUri);
      Log.i(TAG, "resultActivityTiraFoto3() - mFilename: " + mFilename);

      if (xUri == null) {
        // Uri is null
        Log.d(TAG, "resultActivityTiraFoto3() - xUri is null ==> nenhuma foto foi obtida");
        estadoFinal();
      }

      if (data != null) {
        showBundle(data.getExtras());
      }

      if (mParticipacao == null) {
        // mParticipacao é nulo
        Log.w(TAG, "resultActivityTiraFoto3() - mParticipação é nulo");
        estadoFinal();
      }

      // uma foto foi tirada e encontra-se no endereço xUri

      // a foto encontra-se no caminho dado por xUri.getPath();
      // uma foto foi tirada e encontra-se no endereço xUri
      // exibe informações sobre a localização da foto armazenada no sistema
      Log.d(TAG, "resultActivityTiraFoto3() - Foto tirada e armazenada em xUri.getPath()=" + xUri.getPath());

      // atualiza o caminho onde a foto foi armazenada.
      // getPath(): Gets the decoded path. (obtem o caminho decodificado)
      // the decoded path, or null if this is not a hierarchical URI
      // (like "mailto:nobody@google.com") or the URI is invalid
      // armazena o caminho onde se encontra a foto original

      // TODO alterar o caminho de onde se encontra a foto
      mParticipacao.setNomeArqFoto(xUri.getPath());

      Log.d(TAG, "resultActivityTiraFoto3() - mParticipacao=" + mParticipacao);

      // atualiza a orientação da tela para Portrait
      int orientacao = this.getResources().getConfiguration().orientation;

      if (orientacao == Configuration.ORIENTATION_LANDSCAPE) {
        Log.d(TAG, "resultActivityTiraFoto3() - Orientação da tela em LANDSCAPE");

        this.setRequestedOrientation(Configuration.ORIENTATION_PORTRAIT);

      } else if (orientacao == Configuration.ORIENTATION_PORTRAIT) {
        Log.d(TAG, "resultActivityTiraFoto3() - Orientação da tela em PORTRAIT");
      }

      // grava a foto redimensionada em um arquivo
      boolean gravou = imagem.gravaBitmapArquivo3(xUri);

      if (gravou) {
        Log.i(TAG, "Arquivo: " + xUri + " gravado com sucesso.");
      } else {
        Log.w(TAG, "Falha na gravação do arquivo: " + xUri);
        xUri = null;
        mFilename = null;
        estadoFinal();
      }

      if (mParticipacao != null) {

        if (mParticipacao.getTipoFoto() == 1) {
          // atualiza a máquina de estado
          setEstado(2);
          // Executa o próximo passo da máquina de estado
          processaFotos();
        } else if (mParticipacao.getTipoFoto() == 2) {

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
            // atualiza a máquina de estado
            setEstado(2);
            // Executa o próximo passo da máquina de estado
            processaFotos();

          }

        }

      }

    } else {

      // resultCode != RESULT_OK

      xUri = null;
      mFilename = null;

      // operação cancelada - indica que nenhuma foto foi tirada.
      Log.w(TAG, "resultActivityTiraFoto3() - Operação tirar foto cancelada pelo usuário");

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
  private void processaFotos() {

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
    imagem.showUri(xUri);

    // xUri.get

    // obtém o tipo da foto (se o formato da foto é Polaroid ou Cabine)
    tipoFoto = mParticipacao.getTipoFoto();

    // obtem o efeito para aplicação na foto (se a foto deve ser a cores ou P&B)
    efeitoFoto = mParticipacao.getEfeitoFoto();

    String pathCabine = mEvento.getBordaCabine();

    // --------

    /**
     * Processa a foto com o filtro desejado
     * 
     * 
     * 
     */
    // TODO será que esse processo não deveria ser feito depois ???
    Bitmap bb = processoEfeitoFiltroFoto(efeitoFoto, null);

    // --------

    // nome do arquivo contendo a foto formatada como Polaroid ou Cabine já com
    // a moldura aplicada
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
   * @param efeitoFoto
   */
  private Bitmap processoEfeitoFiltroFoto(int efeitoFoto, Bitmap bitmap) {

    Bitmap resultado = null;

    if (efeitoFoto == CORES) { // aplica efeito cores COR
      // TODO processa o efeito cores
      resultado = imagem.aplicaFiltroCores(bitmap);
    } else if (efeitoFoto == PB) { // aplica efeito P&B
      // TODO processa o efeito P&B, isto é, aplicaca um filtro P&B à foto
      resultado = imagem.aplicaFiltroPB(bitmap);
    } else {
      Log.w(TAG, "Efeito: " + efeitoFoto + " não é suportado pela aplicação");
    }

    return resultado;

  }

  /**
   * formataFotoPolaroid(Bitmap bmMoldura)
   * 
   * foto formato Polaroid exige o redimensionamento da foto bem como a inclusão
   * da moldura.
   * 
   * Observe que a moldura está relacionada ao evento em andamento. Portanto é
   * necessário ter informações sobre o evento
   * 
   * Pega a foto, redimensiona-a e aplica a moldura criando um novo bitmap.
   * 
   * 
   * @param bmMoldura
   *          Bitmap contendo a moldura da foto
   * 
   * @return o nome do arquivo onde a foto pronta está gravado
   * 
   * 
   */
  private String formataFotoPolaroid(Bitmap bmMoldura) {

    Log.i(TAG, "formataFotoPolaroid() - Foto tipo POLAROID foi selecionada");

    // Cria um bitmap a partir da Uri da foto
    Bitmap foto = imagem.criaBitmap(xUri);

    if (foto == null) {
      Log.w(TAG, "formataFotoPolaroid() - foto é nula");
      return null;
    }

    // Exibe informações a respeito da foto
    imagem.showBitmapInfo(foto);

    // Redimensiona a foto para o formato Polaroid
    // Bitmap fotoRedimensionada = imagem.getScaledBitmap2(foto, 228, 302);

    // --------------------------------------------------------------------------
    // redimensiona a foto original para 9x12 para manter a proporção 3:4
    // --------------------------------------------------------------------------

    Bitmap bmFoto9x12 = imagem.getScaledBitmap2(foto, 340, 454);

    String arqSaida = null;

    // Define o nome da foto redimensionada
    arqSaida = PATH_FOTOS + getFilename(xUri) + "_9x12.png";

    // grava a foto redimensionada (foto9x12) em um arquivo
    boolean gravou = imagem.gravaBitmapArquivo(bmFoto9x12, arqSaida);
    if (gravou) {
      // foto armazenada com sucesso
      Log.i(TAG, "formataFotoPolaroid() - (Foto9x12): " + arqSaida + " gravado com sucesso.");
    } else {
      // falha na gravação da foto
      Log.i(TAG, "formataFotoPolaroid() - (Foto9x12) - falha na gravação do arquivo: " + arqSaida);
    }

    // --------------------------------------------------------------------------
    // redimensiona a foto 9x12 para 8x8, isto é, copia uma "janela" 8x8 da foto
    // --------------------------------------------------------------------------

    Options options = new Options();

    Rect rect = new Rect(0, 0, 302, 302);

    // Obtem um bitmap com a foto redimensionada para 8x8
    Bitmap bmFoto8x8 = imagem.getBitmapRegion(arqSaida, rect, options);

    arqSaida = PATH_FOTOS + getFilename(xUri) + "_8x8.png";

    // grava a foto redimensionada em um arquivo
    boolean gravou2 = imagem.gravaBitmapArquivo(bmFoto8x8, arqSaida);

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
   * @param bmMoldura
   * 
   * @return
   */
  String gravaFotoPolaroidComMoldura(Bitmap bmFoto, Bitmap bmMoldura) {

    Bitmap fotoComMoldura = null;

    if ((bmFoto != null) && (bmMoldura != null)) {

      fotoComMoldura = imagem.overlay4(bmFoto, bmMoldura);

      if (fotoComMoldura == null) {
        Log.w(TAG, "gravaFotoPolaroidComMoldura() - erro na conversão da foto");
      }

    } else {
      Log.w(TAG, "gravaFotoPolaroidComMoldura() - ERRO");
    }

    String arqSaida = PATH_FOTOS + getFilename(xUri) + ".jpg";

    // grava a foto das imagens "juntada"
    boolean gravou = imagem.gravaBitmapArquivo2(fotoComMoldura, arqSaida);

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

    String nomeDoArquivo = imagem.processaFotoFormatoCabine(foto1, foto2, foto3, moldura);

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
    Bitmap foto = imagem.criaBitmap(xUri);

    // Exibe informações a respeito da foto
    imagem.showBitmapInfo(foto);

    Bitmap foto1 = formataFoto3x4(foto, "foto1.png");
    Bitmap foto2 = formataFoto3x4(foto, "foto2.png");
    Bitmap foto3 = formataFoto3x4(foto, "foto3.png");

    String nomeDoArquivo = imagem.processaFotoFormatoCabine2(foto1, foto2, foto3, bmMoldura);

    return nomeDoArquivo;

  }

  /**
   * Redimensiona uma foto para o tamanho 3x4 (observe que nesse caso 3 é a
   * largura e 4 é a altura)
   * 
   * @param foto
   * @param nome
   * 
   * @return o bitmap com a foto ou null em caso de erro
   */
  Bitmap formataFoto3x4(Bitmap foto, String nome) {

    // redimensiona a foto foto para 3x4
    Bitmap fotoRedimensionada = imagem.getScaledBitmap2(foto, 113, 151);

    if (fotoRedimensionada == null) {
      Log.w(TAG, "formataFoto3x4() - erro no redimensionamento da foto");
      return null;
    }

    // Define o nome da foto redimensionada
    String arqSaida = PATH_FOTOS + nome;

    // grava a foto redimensionada em um arquivo
    boolean gravou = imagem.gravaBitmapArquivo(fotoRedimensionada, arqSaida);

    if (gravou) {
      // foto armazenada com sucesso
      return fotoRedimensionada;
    } else {
      // falha na gravação da foto
      Log.w(TAG, "formataFoto3x4() - falha na gravação da foto");
      return null;
    }

  }

  /**
   * 
   * @param bmMoldura
   * @return
   */
  String xxxabc(Bitmap bmMoldura) {

    // Cria um bitmap a partir da Uri da foto
    Bitmap foto = imagem.criaBitmap(xUri);

    // Exibe informações a respeito da foto
    imagem.showBitmapInfo(foto);

    // Redimensiona a foto para o formato Polaroid
    // Bitmap fotoRedimensionada = imagem.getScaledBitmap2(foto, 228, 302);

    // primeiro, scala a foto para 9x12 cm para manter a proporção
    Bitmap fotoRedimensionada = imagem.getScaledBitmap2(foto, 340, 454);

    // Define o nome da foto redimensionada
    String arqSaida = PATH_FOTOS + "polaroid-escalada.png";

    // grava a foto redimensionada em um arquivo
    boolean gravou = imagem.gravaBitmapArquivo(fotoRedimensionada, arqSaida);

    // redimensiona a foto 9x12 para 8x8, isto é, copia uma "janela" 8x8 da foto

    Options options = new Options();

    Rect rect = new Rect(0, 0, 302, 302);

    //
    fotoRedimensionada = imagem.getBitmapRegion(arqSaida, rect, options);

    if (gravou) {
      // foto armazenada com sucesso
    } else {
      // falha na gravação da foto
    }

    Bitmap fotoComMoldura = null;

    if ((fotoRedimensionada != null) && (bmMoldura != null)) {

      fotoComMoldura = imagem.overlay4(fotoRedimensionada, bmMoldura);

      if (fotoComMoldura == null) {
        Log.w(TAG, "formataFotoPolaroid() - erro na conversão da foto");
      }

    } else {
      Log.w(TAG, "formataFotoPolaroid() - ERRO");
    }

    arqSaida = PATH_FOTOS + "polaroid-escalada-com-moldura.jpg";

    // grava a foto das imagens "juntada"
    gravou = imagem.gravaBitmapArquivo2(fotoComMoldura, arqSaida);

    if (gravou) {
      // foto armazenada com sucesso
      Toast.makeText(this, "Foto gravada com sucesso", Toast.LENGTH_SHORT).show();
    } else {
      // falha na gravação da foto
      Toast.makeText(this, "Falha na gravação da foto", Toast.LENGTH_SHORT).show();
    }

    // o nome completo do arquivo onde a foto com moldura foi armazenada
    return arqSaida;

  }

  /**
   * enviaEmail(Uri lastUri)
   * 
   * Inicia o processo de envio de email.
   * 
   * Verifica inicialmente se todas as condições necessárias estão satisfeita
   * para o envio do email com a foto anexada.
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
      Log.d(TAG, "enviaEmail() - não há foto");
      erro = true;
    }

    if (!erro) {

      // não há erro conhecido

      // carrega as preferências sobre o envio de email
      SharedPreferences emailPreferences = getSharedPreferences("pref_email", MODE_PRIVATE);

      if (emailPreferences == null) {
        Log.w(TAG, "enviaEmail() - SharedPreferences não foi encontrada.");
      }

      /**
       * Assunto do email
       */
      String subject = emailPreferences.getString("preferencias_assunto", "Evento Inicial");

      /**
       * Corpo do email
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

  }

  /**
   * sendEmail(String emailParticipante, String emailContratante, String
   * subject, String text, Uri imageURI)
   * 
   * @param emailParticipante
   *          Endereço de email do participante do evento
   * @param emailContratante
   *          Endereço de email do contratante do evento
   * @param subject
   *          String usada como "Subject" do email
   * @param text
   *          String usada como "Body" do email (o conteúdo da mensagem)
   * @param imageUri
   *          URL da foto processada
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

    // Imagem
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
    // TODO aqui pode acontecer de ser necessário forçar a aplicação de email
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
   */
  private void resultActivityChooser(int resultCode, Intent data) {

    Log.d(TAG, "resultActivityChooser() ===> processando resultado da ACTIVITY CHOOSER: resultCode=" + resultCode);

    // Obtém o intent
    Intent intent = this.getIntent();

    if (intent != null) {
      ComponentName compName = intent.getComponent();
      if (compName != null) {
        Log.d(TAG, "compName=" + compName.getClassName() + ", compName=" + compName.getPackageName());
      }
    }

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

      intent.putExtra("br.com.mltech.result", "OK");

      setResult(RESULT_OK, intent);

    } else {

      Toast.makeText(this, "Falha no processo. Estado atual: " + estadoCorrente, Toast.LENGTH_SHORT).show();

      // estado final atingido porém houve falha
      Log.w(TAG, "estadoFinal() - não foi possível chegar ao final do processamento.");

      intent.putExtra("br.com.mltech.result", "NOT_OK");

      setResult(RESULT_CANCELED, intent);

    }

    // obtém informações sobre a configuração de orientação da tela
    int orientacao = this.getResources().getConfiguration().orientation;

    if (orientacao == Configuration.ORIENTATION_LANDSCAPE) {

      // orientação está LANDSCAPE
      Log.d(TAG, "Orientação da tela em LANDSCAPE");

      // Altera a orientação para PORTRAIT
      this.setRequestedOrientation(Configuration.ORIENTATION_PORTRAIT);

    } else if (orientacao == Configuration.ORIENTATION_PORTRAIT) {
      Log.d(TAG, "Orientação da tela em PORTRAIT");
    }

    // Termina a execução da Activity responsável por tirar e enviar uma foto
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
    Log.d(TAG, "transição do estado: " + mEstado + " para o estado: " + e);
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

  @Override
  protected void onRestart() {

    super.onRestart();
    Log.d(TAG, "*");
    Log.d(TAG, "****************************************************");
    Log.d(TAG, "*** onRestart() - A aplicação foi restartada ... ***");
    Log.d(TAG, "****************************************************");
    Log.d(TAG, "*");
    showVariables();

  }

  @Override
  protected void onStart() {

    super.onStart();
    Log.d(TAG, "*** onStart() ***");
    showVariables();
  }

  @Override
  protected void onResume() {

    super.onResume();
    Log.d(TAG, "*** onResume() ***");
    showVariables();
  }

  @Override
  protected void onPause() {

    super.onPause();
    Log.d(TAG, "*** onPause() ***");
    showVariables();
  }

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

  @Override
  protected void onSaveInstanceState(Bundle outState) {

    super.onSaveInstanceState(outState);
    Log.d(TAG, "*** onSaveInstanceState() ***");
    showBundle(outState);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {

    super.onRestoreInstanceState(savedInstanceState);
    Log.d(TAG, "*** onRestoreInstanceState() ***");
    showBundle(savedInstanceState);
  }

  /**
   * isExternalMediaMounted()
   * 
   * @return true se uma media de armazenamento externo estiver montada ou
   *         false, caso contrário.
   * 
   */

  public boolean isExternalMediaMounted() {

    boolean isMounted;

    // Obtém o estado corrente do principal dispositivo de armazenamento
    // externo
    isMounted = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());

    if (isMounted) {
      // dispositivo está montado
      Log.d(TAG, "Media externa está montada.");
    } else {
      // dispositivo não não está montado
      Log.w(TAG, "Media externa não está montada.");
    }

    return isMounted;

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
    Log.v(TAG, "=================================");
  }

  /**
   * showBundle(Bundle b)
   * 
   * Exibe todas as chaves/valores armazenadas no bundle
   * 
   * @param bundle
   *          Instância da classe Bundle
   * 
   */
  private void showBundle(Bundle bundle) {

    if (bundle == null) {
      Log.w(TAG, "showBundle() - bundle está vazio");
      return;
    }

    // Obtém um conjunto de chaves do Bundle
    Set<String> setChaves = bundle.keySet();

    // Obtém o tamanho do conjunto
    int size = bundle.size();

    // Exibe o nº de elementos do conjunto
    Log.d(TAG, "showBundle() - bundle size=" + size);

    int i = 0;

    for (String chave : setChaves) {
      i++;
      Log.d(TAG, "  " + i + ") " + chave);
    }

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
   * isCameraWorking(int cameraID)
   * 
   * Verifica se a câmera identificada por cameraID está em funcionamento. Para
   * fazer essa verificação é tentado abrir a câmera. Caso ela consiga ser
   * aberta indica que a câmera está disponível e nesse caso simplesmente
   * liberamos a câmera para uso. Se houver erro indica que há problemas;
   * 
   * @param cameraID
   *          Identificador da câmera do dispositivo
   * 
   * @return true se for possível obter ima instância da classe Camera ou false,
   *         caso contrário.
   */
  boolean isCameraWorking(int cameraID) {

    Camera c = getCameraInstance(cameraID);

    if (c != null) {
      // foi possível obter uma instância da câmera
      // então, é necessário liberar a câmera para que possa ser usada pela
      // aplicação
      c.release();
      c = null;

      Log.i(TAG, "isCameraWorking() - câmera: " + cameraID + " liberada com sucesso");

      return true;

    } else {
      Log.i(TAG, "isCameraWorking() - erro na liberação da câmera: " + cameraID);
      return false;

    }

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
   */
  private Bitmap leArquivoMoldura(String arquivoMoldura) {

    File moldura = new File(arquivoMoldura);

    // lê o bitmap contendo a moldura
    Bitmap bmMoldura = imagem.getBitmapFromFile(moldura);

    if (bmMoldura == null) {

      Log.w(TAG, "leArquivoMoldura() - Não foi possível ler o arquivo: " + moldura);
      return null;

    } else {
      Log.v(TAG, "leArquivoMoldura() - Tamanho da moldura original: " + imagem.getStringBitmapSize(bmMoldura));

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
   * getFilename(Uri uri)
   * 
   * Obtém o nome do arquivo (sem a extensão) de um arquivo representado por uma
   * Uri cujo schema é file.
   * 
   * @param uri
   * 
   * @return
   */
  static String getFilename(Uri uri) {

    if (isFileURI(uri)) {

      String s = uri.getPath();

      if (s != null) {
        return s.substring(s.lastIndexOf("/") + 1, s.lastIndexOf("."));
      }

    }

    return null;

  }

  /**
   * isFileURI(Uri uri)
   * 
   * Testa se uma Uri é do tipo (schema) file.
   * 
   * @param uri
   * 
   * @return true em caso schema seja file ou null caso contrário
   */
  static boolean isFileURI(Uri uri) {

    if (uri == null) {
      return false;
    }

    return uri.getScheme().equals("file");

  }

  /**
   * getFileExtension(Uri uri)
   * 
   * Obtém a extensão de um arquivo representado por uma Uri cujo schema é file.
   * 
   * @param uri
   * 
   * @return
   * 
   */
  static String getFileExtension(Uri uri) {

    if (isFileURI(uri)) {
      String s = uri.getPath();

      return s.substring(s.lastIndexOf(".") + 1);
    }

    return null;

  }

}
