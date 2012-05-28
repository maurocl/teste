package br.com.mltech;

import java.io.File;
import java.io.IOException;
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
 * @author maurocl
 * 
 */
public class DummyActivity3 extends Activity implements Constantes {

  private static final String TAG = "DummyActivity3";

  public static final String PATH_MOLDURAS = "/mnt/sdcard/Pictures/fotoevento/molduras/";
  public static final String PATH_FOTOS = "/mnt/sdcard/Pictures/fotoevento/fotos/";

  // Defini��o da Activies chamadas
  private static final int ACTIVITY_TIRA_FOTO_3 = 113;
  private static final int ACTIVITY_CHOOSER = 150;
  private static final int ACTIVITY_PARTICIPANTE = 102;

  // Defini��o dos atributos da aplica��o
  private static Contratante mContratante;
  private static Evento mEvento;
  private static Participante mParticipante;
  private static Participacao mParticipacao;

  //
  private SharedPreferences mPreferences;

  // Nome do arquivo onde est� armazenado a �ltima foto
  private static String mFilename;

  // Uri da �ltima foto
  private static Uri xUri;
  private Uri mUri;

  // Estado atual da m�quina de estado da aplica�a�
  private static int mEstado = -1;

  // N� de vezes que a activity � criada
  private static int mContador = 0;

  // Inst�ncia de uma c�mera
  private static Camera c = null;

  // N� de c�meras do dispositivo
  private static int numCameras = -1;

  // N� da c�mera corrente em uso (se houver)
  private static int mCurrentCamera = 0;

  // Biblioteca de fun��es para manipula��o de imagens
  private ManipulaImagem imagem = null;

  // Bitmaps contendo as molduras
  private static Bitmap mBitmapMolduraPolaroid;
  private static Bitmap mBitmapMolduraCabine;

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
      Log.w(TAG, "N�o foi poss�vel obter uma inst�ncia da classe ManipulaImagm");
    }

    showBundle(savedInstanceState);

    // ---------------------------------------------------------------------
    mPreferences = getSharedPreferences("preferencias", MODE_PRIVATE);

    String molduraPolaroid = null;
    String molduraCabine = null;

    if (mPreferences == null) {
      
      Log.w(TAG, "mPreferences is null. Falha na execu��o do comandos getSharedPreferences()");
      molduraPolaroid = PATH_MOLDURAS + "moldura-polaroid-340x416-red.png";
      molduraCabine = PATH_MOLDURAS + "moldura-cabine-132x568-red.png";

    } else {

      molduraPolaroid = mPreferences.getString("evento_borda_polaroid", PATH_MOLDURAS);
      molduraCabine = mPreferences.getString("evento_borda_cabine", PATH_MOLDURAS);

    }

    mBitmapMolduraPolaroid = leArquivoMoldura(molduraPolaroid);
    mBitmapMolduraCabine = leArquivoMoldura(molduraCabine);
    
    Log.d(TAG," mBitmapMolduraPolaroid ="+  mBitmapMolduraPolaroid);
    Log.d(TAG,"mBitmapMolduraCabine = "+ mBitmapMolduraCabine);
    
    
    // ---------------------------------------------------------------------

    mContador++;

    Button btn0 = (Button) findViewById(R.id.btn0);

    /**
     * Bot�o 0
     */
    btn0.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        boolean b = iniciaVariaveis();
        if (b == false) {
          Log.w(TAG, "N�o foi poss�vel inicializar as vari�veis");
          estadoFinal();
        }

      }

    });

  }

  /**
   * onActivityResult(int requestCode, int resultCode, Intent data)
   * 
   * Trata o resultado da execu��o das Activities
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

      Log.w(TAG, "Erro ... requestCode: " + requestCode + " n�o pode ser processado");

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

    // obtem o n� de c�meras dispon�veis pelo dispositivo onde a aplica��o est�
    // em execu��o
    numCameras = android.hardware.Camera.getNumberOfCameras();

    Log.d(TAG, "Number of Cameras: " + numCameras);

    // verifica se a c�mera fotogr�tica est� em opera��o
    if (isCameraWorking(0)) {
      Log.i(TAG, "iniciaVariaveis() - Camera est� em funcionamento...");
    } else {

      Log.w(TAG, "iniciaVariaveis() - Camera n�o est� em funcionamento");

      Toast.makeText(this, "Camera n�o est� dispon�vel", Toast.LENGTH_SHORT);
      // showAlert("Camera n�o est� dispon�vel para essa aplica��o");
      return false;
    }

    // Obtem a Intent que iniciou esta Activity
    Intent i = this.getIntent();

    if (i == null) {
      Log.w(TAG, "iniciaVariaveis() - Erro grav�ssimo !!!");
    }

    // indicador de erro de configura��o
    int erro = 0;

    if (i.getSerializableExtra("br.com.mltech.contratante") != null) {
      mContratante = (Contratante) i.getSerializableExtra("br.com.mltech.contratante");
    } else {
      Log.w(TAG, "iniciaVariaveis() - Contratante n�o pode ser nulo.");
      Toast.makeText(this, "Contratante n�o pode ser nulo", Toast.LENGTH_SHORT).show();
      erro = 1;
    }

    if (i.getSerializableExtra("br.com.mltech.evento") != null) {
      mEvento = (Evento) i.getSerializableExtra("br.com.mltech.evento");
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

    Log.d(TAG, "mContratante=" + mContratante);
    Log.d(TAG, "mEvento=" + mEvento);

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

    // Cria uma nova Intent
    Intent intent = new Intent(this, ParticipanteActivity.class);

    // Inclui o par�metro mEvento (com as informa��es sobre o evento em curso)
    intent.putExtra("br.com.mltech.evento", mEvento);

    // Inicia a Activity
    startActivityForResult(intent, ACTIVITY_PARTICIPANTE);

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

    Log.d(TAG, "==> processando resultado da ACTIVITY PARTICIPANTE");

    if (getEstado() < 0) {
      Log.d(TAG, "N�o h� informa��es sobre o participante");
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
      Log.w(TAG, "resultCode n�o conhecido: " + resultCode);
      return;

    }

    if (data == null) {
      // caso a Intent n�o retorne nada houve algum problema
      Log.w(TAG, "A intent n�o retornou os dados esperados");
      return;
    }

    if (data.getSerializableExtra("br.com.mltech.participante") != null) {
      mParticipante = (Participante) data.getSerializableExtra("br.com.mltech.participante");
    }

    if (data.getSerializableExtra("br.com.mltech.participacao") != null) {
      mParticipacao = (Participacao) data.getSerializableExtra("br.com.mltech.participacao");
    }

    // Exibe as informa��es sobre o participante e sua participa�a�
    Log.d(TAG, "mParticipante=" + mParticipante);
    Log.d(TAG, "mParticipacao=" + mParticipacao);

    // Atualiza o estado da m�quina de estados
    setEstado(1);
    // Processa o pr�ximo estado
    obtemFoto();

  }

  /**
   * obtemFoto()
   * 
   * Executa a Intent ACTION_IMAGE_CAPTURE para obter uma foto da c�mera
   * 
   * A imagem capturada ser� armazenada na vari�vel xUri
   * 
   */
  private void obtemFoto() {

    Log.d(TAG, "==> Executando obtemFoto()");

    if (getEstado() < 1) {
      Log.d(TAG, "N�o h� informa��es sobre o participante !");
      estadoFinal();
      return;
    }

    // cria e inicia a Intent ACTION_IMAGE_CAPTURE
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    File file = null;

    // Libera os recursos relacionados a c�mera
    xUri = null; // endere�o onde a foto ser� armazenada em caso de sucesso
    mUri = null;
    mFilename = null; // nome do arquivo onde a foto est� armazenada
    c = null;

    Log.d(TAG, "obtemFoto() - mCurrentCamera: " + mCurrentCamera);

    // Obt�m informa��es sobre a c�mera (atualmente configurada)
    c = getCameraInstance(mCurrentCamera);

    if (c != null) {

      // Camera obtida com sucesso
      Log.i(TAG, "Inst�ncia da Camera obtida com sucesso");
      c.release(); // libera a c�mera
      c = null; // limpa as vari�veis
      numCameras = -1; // TODO esse valor n�o deveria ser 0 ou 1 ???

    } else {
      //
      Log.w(TAG, "obtemFoto() - Erro da obten��o da inst�ncia da Camera");
      estadoFinal();
      return;
    }

    try {

      // obt�m um novo de arquivo
      file = setUpPhotoFile();

      // atualiza o nome do arquivo onde a foto ser� armazenada
      mFilename = file.getAbsolutePath();

      Log.d(TAG, "===> Arquivo=" + mFilename);

      if (file.canWrite()) {
        Log.d(TAG, "obtemFoto() - Arquivo pode ser gravado");
      } else {
        Log.d(TAG, "obtemFoto() - Arquivo n�o pode ser gravado");
      }

      // Obtem a URI do arquivo (esse valor ser� forncecido a Intent)
      xUri = Uri.fromFile(file); // informa a Uri onde a foto ser� armazenada
      mUri = Uri.fromFile(file);

      if (xUri == null) {
        Log.w(TAG, "obtemFoto() - xUri=null. Arquivo para armazenamento n�o foi criado.");
        return;
      } else {
        Log.w(TAG, "obtemFoto() ===> xUri=" + xUri.getPath() + ", xUri=" + xUri);
      }

      // Passa como par�metro a URI de onde a foto deve ser gravada
      intent.putExtra(MediaStore.EXTRA_OUTPUT, xUri);

      // Inicia a Activity
      startActivityForResult(intent, ACTIVITY_TIRA_FOTO_3);

    } catch (IOException e) {

      Log.d(TAG, ">>> obtemFoto() <<< - erro", e);

      e.printStackTrace();
      file = null;
      mFilename = null;
      mUri = null;
      xUri = null;
      c = null;
      estadoFinal();

    }

  }

  /**
   * resultActivityTiraFoto3(int resultCode, Intent data)
   * 
   * A foto encontra-se no caminho dado por xUri.getPath();
   * 
   * @param resultCode
   *          Resultado de execu��o da Activity
   * @param data
   */
  private void resultActivityTiraFoto3(int resultCode, Intent data) {

    Log.d(TAG, "==> Executando o resultado do processamento da ACTIVITY_TiraFoto3");
    Log.d(TAG, "==> resultCode=" + resultCode);

    if (resultCode == RESULT_OK) {

      // activity executada com sucesso

      Log.w(TAG, "resultActivityTiraFoto3() - xUri: " + xUri);
      Log.w(TAG, "resultActivityTiraFoto3() - mUri: " + mUri);
      Log.w(TAG, "resultActivityTiraFoto3() - mFilename: " + mFilename);

      // uma foto foi tirada e encontra-se no endere�o xUri
      if (xUri != null) {

        // uma foto foi tirada e encontra-se no endere�o xUri
        // exibe informa��es sobre a localiza��o da foto armazenada no
        // sistema
        Log.d(TAG, "Foto tirada e armazenada em xUri.getPath()=" + xUri.getPath());

        if (mParticipacao != null) {

          // atualiza o caminho onde a foto foi armazenada.
          // getPath(): Gets the decoded path.
          // the decoded path, or null if this is not a hierarchical URI
          // (like "mailto:nobody@google.com") or the URI is invalid
          mParticipacao.setNomeArqFoto(xUri.getPath());

          Log.d(TAG, "mParticipacao=" + mParticipacao);

          // atualiza a orienta��o da tela para Portrait
          int orientacao = this.getResources().getConfiguration().orientation;

          if (orientacao == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG, "Orienta��o da tela em LANDSCAPE");

            this.setRequestedOrientation(Configuration.ORIENTATION_PORTRAIT);

          } else if (orientacao == Configuration.ORIENTATION_PORTRAIT) {
            Log.d(TAG, "Orienta��o da tela em PORTRAIT");
          }

          // atualiza a m�quina de estado
          setEstado(2);
          // Executa o pr�ximo passo da m�quina de estado
          processaFotos();

        } else {
          // mParticipacao � nulo
          Log.d(TAG, "mParticipa��o � nulo");
        }

      } else {
        // Uri is null
        Log.d(TAG, "xUri is null");
      }

    } else {

      // resultCode != RESULT_OK

      xUri = null;
      mUri = null;
      mFilename = null;

      // opera��o cancelada - indica que nenhuma foto foi tirada.
      Log.w(TAG, "resultActivityTiraFoto3 - Opera��o Tirar foto cancelada pelo usu�rio");

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
  private void processaFotos() {

    Log.d(TAG, "====> processaFotos() <====");

    int tipoFoto = -1;
    int efeitoFoto = -1;

    if (xUri == null) {
      // URI da foto n�o est� dispon�vel
      Log.d(TAG, "URI contendo a foto n�o est� dispon�vel");
      estadoFinal();
    }

    if (mParticipacao != null) {
      // h� dados a respeito do participante do evento

      // obt�m o tipo da foto
      tipoFoto = mParticipacao.getTipoFoto();

      // obtem o efeito para aplica��o na foto
      efeitoFoto = mParticipacao.getEfeitoFoto();

    }

    String pathPolaroid = null;
    String pathCabine = null;

    if (mEvento != null) {

      pathPolaroid = mEvento.getBordaPolaroid();
      pathCabine = mEvento.getBordaCabine();

      Log.d(TAG, "processaFotos() - pathPolaroid: " + pathPolaroid);
      Log.d(TAG, "processaFotos() - pathPolaroid: " + pathCabine);

    }

    // --------

    /**
     * 
     */
    Bitmap bb = processoEfeitoFiltroFoto(efeitoFoto, null);

    // --------

    String arquivoFotoComMoldura = null;

    if (tipoFoto == TIPO_FOTO_POLAROID) {

      arquivoFotoComMoldura = formataFotoPolaroid(mBitmapMolduraPolaroid);

    } else if (tipoFoto == TIPO_FOTO_CABINE) {

      arquivoFotoComMoldura = formataFotoCabine(pathCabine);

    } else {
      Log.w(TAG, "processaFotos() - tipo de foto: " + tipoFoto + " n�o suportado.");

    }

    // -------------------------------------------------------------------------

    // TODO aqui poder�amos ter um passo intermedi�rio na m�quina de estados

    // TODO aqui tamb�m � necess�rio atualizar a Uri da foto processada

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

    Log.d(TAG, "lastUri: " + lastUri.toString());

    // O pr�ximo passo � enviar o email com a foto j� trabalhada.
    // Envia email com a foto pronta
    enviaEmail(lastUri);

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
      // TODO processa o efeito P&B, isto �, aplicaca um filtro P&B � foto
      resultado = imagem.aplicaFiltroPB(bitmap);
    } else {
      Log.w(TAG, "Efeito: " + efeitoFoto + " n�o � suportado pela aplica��o");
    }

    return resultado;

  }

  /**
   * formataFotoPolaroid(String pathPolaroid)
   * 
   * // foto formato Polaroid exige o redimensionamento da foto bem como // a
   * inclus�o da moldura // observe que a moldura est� relacionada ao evento em
   * andamento // portanto � necess�rio ter informa��es sobre o evento
   * 
   * Pega a foto e a moldura e cria um novo bitmap
   * 
   * @param pathPolaroid
   * 
   */
  private String formataFotoPolaroid(Bitmap bmMoldura) {

    String arqSaida = null;

    Log.i(TAG, "formataFotoPolaroid() - Foto tipo POLAROID foi selecionada");

    if (imagem == null) {
      Log.w(TAG, "formataFotoPolaroid() - biblioteca de fun��es n�o est� dispon�vel");
      return null;
    }

    // Cria um bitmap a partir da Uri da foto
    Bitmap foto = imagem.criaBitmap(xUri);

    // Exibe informa��es a respeito da foto
    imagem.showBitmapInfo(foto);

    // Redimensiona a foto para o formato polaroid
    Bitmap fotoRedimensionada = imagem.getScaledBitmap2(foto, 228, 302);

    // Define o nome da foto redimensionada
    arqSaida = PATH_FOTOS + "polaroid-escalada.png";

    // grava a foto redimensionada em um arquivo
    boolean gravou = imagem.gravaBitmapArquivo(fotoRedimensionada, arqSaida);

    if (gravou) {
      // foto armazenada com sucesso
    } else {
      // falha na grava��o da foto
    }

    Bitmap fotoComMoldura = null;

    if ((fotoRedimensionada != null) && (bmMoldura != null)) {

      fotoComMoldura = imagem.overlay4(fotoRedimensionada, bmMoldura);

      if (fotoComMoldura == null) {
        Log.w(TAG, "formataFotoPolaroid() - erro na convers�o da foto");
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
      // falha na grava��o da foto
      Toast.makeText(this, "Falha na grava��o da foto", Toast.LENGTH_SHORT).show();
    }

    // o nome completo do arquivo onde a foto com moldura foi armazenada
    return arqSaida;

  }

  /**
   * formataFotoCabine(String pathCabine)
   * 
   * @param pathCabine
   */
  private String formataFotoCabine(String pathCabine) {

    if (pathCabine == null) {
      Log.d(TAG, "formataFotoCabine() - N�o h� moldura definida para foto CABINE");
    }

    // foto formato Cabibe exige tr�s fotos.
    // as fotos ser�o montadas em sequencoa e ser� aplicada
    // uma moldura (conforme configura��o)
    // observe que a moldura est� relacionada ao evento em andamento
    // portanto � necess�rio ter informa��es sobre o evento

    // TODO verificar nesse ponto se h� tr�s fotos dispon�vel

    // TODO veja que aqui s�o necess�rias 3 fotos
    // as fotos podem ser armazenadas em um array de fotos
    Log.d(TAG, "processaFotos() - Foto tipo CABINE foi selecionada");

    String foto1 = PATH_FOTOS + "img-3x4-blue.png";
    String foto2 = PATH_FOTOS + "img-3x4-green.png";
    String foto3 = PATH_FOTOS + "img-3x4-yellow.png";

    String moldura = "moldura-cabine-132x568-red.png";

    String s = imagem.processaFotoFormatoCabine(foto1, foto2, foto3, moldura);

    return s;
  }

  /**
   * enviaEmail()
   * 
   * Verifica se todas as condi��es necess�rias est�o satisfeita para o envio do
   * email com a foto anexada.
   * 
   */
  private void enviaEmail(Uri lastUri) {

    boolean erro = false;

    if (getEstado() < 2) {
      Log.d(TAG, "Foto n�o foi tirada");
      erro = true;
    }

    if (mParticipante == null) {
      // Log.d(TAG, "mParticipante � null");
      Log.w(TAG, "N�o h� informa��es sobre o participante");
      erro = true;
    }

    if (mContratante == null) {
      // Log.d(TAG, "mContrante � null");
      Log.w(TAG, "N�o h� informa��es sobre o contratante");
      erro = true;
    }

    if (xUri == null) {
      // Log.d(TAG, "xUri � null");
      Log.d(TAG, "A foto n�o est� dispon�vel para envio");
      erro = true;
    }

    if (!erro) {

      // n�o h� erro conhecido

      // carrega as prefer�ncias sobre o envio de email
      SharedPreferences emailPreferences = getSharedPreferences("pref_email", MODE_PRIVATE);

      if (emailPreferences == null) {
        Log.w(TAG, "SharedPreferences n�o foi encontrada.");
      }

      String subject = emailPreferences.getString("preferencias_assunto", "Evento Inicial");
      String body = emailPreferences.getString("preferencias_descricao", "Segue as informa��es sobre o evento");

      emailPreferences = null;

      // obt�m o email do participante do evento
      String to = mParticipante.getEmail();

      // obt�m o email do contratante do evento
      // ele ser� copiado em BCC no email enviado
      String cc = mContratante.getEmail();

      /**
       * Assunto do email
       */
      /*
       * String subject = null; // Define o "subject" do email if
       * ((assuntoDoEmail != null) && (!assuntoDoEmail.equals(""))) { subject =
       * assuntoDoEmail; } else { subject = ""; }
       */

      /**
       * Corpo do email
       */
      /*
       * String body = null; // Define o corpo do email (mensagem do corpo do
       * email) if ((corpoDoEmail != null) && (!corpoDoEmail.equals(""))) { body
       * = corpoDoEmail; } else { body = ""; }
       */

      // envia o email
      // TODO substituir xUri por lastUri, isto �, a URI da foto processada
      // sendEmail(to, cc, subject, body, xUri);
      sendEmail(to, cc, subject, body, lastUri);

    }

  }

  /**
   * sendEmail(String emailParticipante, String emailContratante, String
   * subject, String text, Uri imageURI)
   * 
   * @param emailParticipante
   *          Endere�o de email do participante do evento
   * @param emailContratante
   *          Endere�o de email do contratante do evento
   * @param subject
   *          String usada como "Subject" do email
   * @param text
   *          String usada como "Body" do email (o conte�do da mensagem)
   * @param imageUri
   *          URL da foto processada
   * 
   */
  private void sendEmail(String emailParticipante, String emailContratante, String subject, String text, Uri imageUri) {

    Intent emailIntent = new Intent(Intent.ACTION_SEND);

    Log.d(TAG, "===> sendEmail()");

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

    // Imagem
    // emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageURI);
    // TODO
    // emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, xUri);
    emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);

    Log.d(TAG, "Anexando o arquivo: " + imageUri + " aos anexos ...");

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
   * 
   */
  private void sendEmailByChooser(Intent emailIntent) {

    // TODO aqui pode acontecer de ser necess�rio for�ar a aplica��o de
    // email

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
   * @param data
   */
  private void resultActivityChooser(int resultCode, Intent data) {

    Log.d(TAG, "===> processando resultado da ACTIVITY CHOOSER: resultCode=" + resultCode);

    // Obt�m o intent
    Intent i = this.getIntent();

    if (i != null) {
      ComponentName compName = i.getComponent();
      if (compName != null) {
        Log.d(TAG, "compName=" + compName.getClassName() + ", compName=" + compName.getPackageName());
      }
    }

    if (data != null) {
      Log.d(TAG, "data.getAction()=" + data.getAction());
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
      Log.d(TAG, "ACTIVITY_CHOOSER - o envio de email foi cancelado");
      estadoFinal();

    } else {
      Log.d(TAG, "ACTIVITY_CHOOSER - resultCode " + resultCode + " n�o pde ser tratado");
      estadoFinal();
    }

  }

  /**
   * estadoFinal()
   * 
   * Representa o estado final da m�quina de estado.
   * 
   */
  private void estadoFinal() {

    // Obtem as informa��es sobre a Intent "chamadora"
    Intent i = getIntent();

    // Obt�m o estado corrente da m�quina de estado
    int estadoCorrente = getEstado();

    if (estadoCorrente == 3) {

      // estado final atingido com sucesso
      Log.d(TAG, "DummyActivity3 - estadoFinal() - final do processamento");

      i.putExtra("br.com.mltech.result", "OK");

      setResult(RESULT_OK, i);

    } else {

      Toast.makeText(this, "Falha no processo. Email n�o foi enviado", Toast.LENGTH_SHORT).show();

      // estado final atingido por�m houve falha
      Log.d(TAG, "DummyActivity3 - estadoFinal() - n�o chegou ao final do processamento");

      i.putExtra("br.com.mltech.result", "NOT_OK");

      setResult(RESULT_CANCELED, i);

    }

    // obt�m informa��es sobre a configura��o de orienta��o da tela
    int orientacao = this.getResources().getConfiguration().orientation;

    if (orientacao == Configuration.ORIENTATION_LANDSCAPE) {

      // orienta��o est� LANDSCAPE
      Log.d(TAG, "Orienta��o da tela em LANDSCAPE");

      // Altera a orienta��o para PORTRAIT
      this.setRequestedOrientation(Configuration.ORIENTATION_PORTRAIT);

    } else if (orientacao == Configuration.ORIENTATION_PORTRAIT) {
      Log.d(TAG, "Orienta��o da tela em PORTRAIT");
    }

    // Termina a execu��o da intent respons�vel por tirar e enviar uma foto
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
    Log.d(TAG, "transi��o do estado: " + mEstado + " para o estado: " + e);
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

  @Override
  protected void onRestart() {

    super.onRestart();
    Log.d(TAG, "*");
    Log.d(TAG, "****************************************************");
    Log.d(TAG, "*** onRestart() - A aplica��o foi restartada ... ***");
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
     * Essa atribui��o n�o deve ser feita pois perdemos o valor de xUri que ser�
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
   *         false, caso contr�rio.
   * 
   */

  public boolean isExternalMediaMounted() {

    boolean isMounted;

    // Obt�m o estado corrente do principal dispositivo de armazenamento
    // externo
    isMounted = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());

    if (isMounted) {
      // dispositivo est� montado
      Log.d(TAG, "Media externa est� montada.");
    } else {
      // dispositivo n�o n�o est� montado
      Log.w(TAG, "Media externa n�o est� montada.");
    }

    return isMounted;

  }

  /**
   * getAlbumDir(String dir)
   * 
   * @param dir
   *          nome do "album" onde as fotos ser�o armazenadas no cart�o de
   *          mem�ria externa no diret�rio p�blico para armazenamento de
   *          imagens.
   * 
   * @return um arquivo incluindo o diret�rio onde as fotos ser�o armazenadas
   * 
   */
  public File getAlbumDir(String dir) {

    File storageDir = null;

    if (isExternalMediaMounted()) {
      Log.w("TAG", "getAlbumDir() - Cart�o de mom�ria externa n�o pode ser encontrado.");
      return null;
    }

    storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), dir);

    if (storageDir != null) {
      //

      if (!storageDir.mkdirs()) {
        // se houver erro na cria��o do diret�rio significa que ele j� existe
        if (!storageDir.exists()) {
          Log.d("TAG", "falha na cria��o do diret�rio");
        }

      }

    }

    return storageDir;

  }

  /**
   * createImageFile()
   * 
   * Cria um File e nomeando-o com a data e hora corrente
   * 
   * @return File Um File
   * 
   * @throws IOException
   */
  private File createImageFile() throws IOException {

    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

    String imageFileName = timeStamp;

    File albumF = getAlbumDir("fotos");

    File imageF = File.createTempFile(imageFileName, ".jpg", albumF);

    return imageF;

  }

  /**
   * setUpPhotoFile()
   * 
   * Atualiza a vari�vel mFilename.
   * 
   * @return Um objeto File com path completo do arquivo
   * 
   * @throws IOException
   *           Se houver erro.
   */
  private File setUpPhotoFile() throws IOException {

    File f = createImageFile();

    mFilename = f.getAbsolutePath();

    return f;

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
    Log.v(TAG, "Vari�veis:");
    if (mParticipante != null)
      Log.v(TAG, "  mParticipante=" + mParticipante);
    if (mParticipacao != null)
      Log.v(TAG, "  mParticipacao=" + mParticipacao);
    if (mContratante != null)
      Log.v(TAG, "  mContratante=" + mContratante);
    if (mFilename != null)
      Log.v(TAG, "  mFilename=" + mFilename);
    if (mUri != null)
      Log.v(TAG, "  mUri=" + mUri);
    if (xUri != null)
      Log.v(TAG, "  xUri=" + xUri);
    Log.v(TAG, "  mEstado=" + mEstado);
    Log.v(TAG, "  mContador=" + mContador);
    Log.v(TAG, "=================================");
  }

  /**
   * showBundle(Bundle b)
   * 
   * @param bundle
   *          Inst�ncia da classe Bundle
   * 
   */
  private void showBundle(Bundle bundle) {

    if (bundle == null) {
      Log.w(TAG, "Bundle est� vazio");
      return;
    }

    // Obt�m um conjunto de chaves do Bundle
    Set<String> setChaves = bundle.keySet();

    // Obt�m o tamanho do conjunto
    int size = bundle.size();

    // Exibe o n� de elementos do conjunto
    Log.d(TAG, "Bundle size=" + size);

    int i = 0;

    for (String chave : setChaves) {
      i++;
      Log.d(TAG, i + ") " + chave);
    }

  }

  /**
   * getCameraInstance(int cameraID)
   * 
   * Tenta obter uma inst�ncia da uma c�mera identificada por seu n�mero. A
   * primeira c�mera, se existir ter� o identificador 0.
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
       * Cria um novo objeto Camera para acesso a c�mera cameraID, Se a c�mera
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
   * isCameraWorking(int cameraID)
   * 
   * Verifica se a c�mera identificada por cameraID est� em funcionamento.
   * 
   * @param cameraID
   *          Identificador da c�mera do dispositivo
   * 
   * @return true se for poss�vel obter ima inst�ncia da classe Camera ou false,
   *         caso contr�rio.
   */
  boolean isCameraWorking(int cameraID) {

    Camera c = getCameraInstance(cameraID);

    if (c != null) {
      //
      c.release();
      c = null;
      numCameras = -1;

      return true;

    } else {

      return false;

    }

  }

  /**
   * Carrega arquivo de moldura
   * 
   * @param
   * 
   * @return
   */
  private Bitmap leArquivoMoldura(String arquivoMoldura) {

    // nome da moldura
    // File moldura = new File(PATH_MOLDURAS +
    // "moldura-polaroid-340x416-red.png");

    File moldura = new File(arquivoMoldura);

    // l� o bitmap contendo a moldura
    Bitmap bmMoldura = imagem.getBitmapFromFile(moldura);

    if (bmMoldura == null) {
      Log.w(TAG, "N�o foi poss�vel ler o arquivo: " + moldura);
      return null;

    } else {
      Log.v(TAG, " ==> Tamanho da moldura original: " + imagem.getStringBitmapSize(bmMoldura));

      return bmMoldura;

    }

  }

}
