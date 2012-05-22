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

  // Definição da Activies chamadas
  private static final int ACTIVITY_TIRA_FOTO_3 = 113;
  private static final int ACTIVITY_CHOOSER = 150;
  private static final int ACTIVITY_PARTICIPANTE = 102;

  // Definição dos atributos da aplicação
  private static Contratante mContratante;
  private static Evento mEvento;
  private static Participante mParticipante;
  private static Participacao mParticipacao;

  // Nome do arquivo onde está armazenado a última foto
  private static String mFilename;

  // Uri da última foto
  private static Uri xUri;
  private Uri mUri;

  // Estado atual da máquina de estado da aplicaçaõ
  private static int mEstado = -1;

  // Nº de vezes que a activity é criada
  private static int mContador = 0;

  // Instância de uma câmera
  private static Camera mCamera = null;

  // Nº de câmeras do dispositivo
  private static int numCameras = -1;

  // Nº da câmera corrente em uso (se houver)
  private static int mCurrentCamera = 0;

  // Biblioteca de funções para manipulação de imagens
  private ManipulaImagem imagem = null;

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
      Log.w(TAG, "Não foi possível obter uma instância da classe ManipulaImagm");
    }

    showBundle(savedInstanceState);

    mContador++;

    Button btn0 = (Button) findViewById(R.id.btn0);

    /**
     * Botão 0
     */
    btn0.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        boolean b = iniciaVariaveis();
        if (b == false) {
          Log.w(TAG, "Não foi possível inicializar as variáveis");
          estadoFinal();
        }

      }

    });

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

      Log.w(TAG, "Erro ... requestCode: " + requestCode + " não pode ser processado");

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

    Log.d(TAG, "Number of Cameras: " + numCameras);

    // verifica se a câmera fotogrática está em operação
    if (isCameraWorking(0)) {
      Log.i(TAG, "iniciaVariaveis() - Camera está em funcionamento...");
    } else {

      Log.w(TAG, "iniciaVariaveis() - Camera não está em funcionamento");

      Toast.makeText(this, "Camera não está disponível", Toast.LENGTH_SHORT);
      // showAlert("Camera não está disponível para essa aplicação");
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
    Intent intent = new Intent(this, ParticipanteActivity.class);

    // Inclui o parâmetro mEvento (com as informações sobre o evento em curso)
    intent.putExtra("br.com.mltech.evento", mEvento);

    // Inicia a Activity
    startActivityForResult(intent, ACTIVITY_PARTICIPANTE);

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

    Log.d(TAG, "==> processando resultado da ACTIVITY PARTICIPANTE");

    if (getEstado() < 0) {
      Log.d(TAG, "Não há informações sobre o participante");
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
      Log.w(TAG, "resultCode não conhecido: " + resultCode);
      return;

    }

    if (data == null) {
      // caso a Intent não retorne nada houve algum problema
      Log.w(TAG, "A intent não retornou os dados esperados");
      return;
    }

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
   * Executa a Intent ACTION_IMAGE_CAPTURE para obter uma foto da câmera
   * 
   * A imagem capturada será armazenada na variável xUri
   * 
   */
  private void obtemFoto() {

    Log.d(TAG, "==> Executando obtemFoto()");

    if (getEstado() < 1) {
      Log.d(TAG, "Não há informações sobre o participante !");
      estadoFinal();
      return;
    }

    // cria e inicia a Intent ACTION_IMAGE_CAPTURE
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    File file = null;

    // Libera os recursos relacionados a câmera
    xUri = null; // endereço onde a foto será armazenada em caso de sucesso
    mUri = null;
    mFilename = null; // nome do arquivo onde a foto está armazenada
    mCamera = null;

    Log.d(TAG, "obtemFoto() - mCurrentCamera: " + mCurrentCamera);

    // Obtém informações sobre a câmera (atualmente configurada)
    mCamera = getCameraInstance(mCurrentCamera);

    if (mCamera != null) {

      // Camera obtida com sucesso
      Log.i(TAG, "Instância da Camera obtida com sucesso");
      mCamera.release(); // libera a câmera
      mCamera = null; // limpa as variáveis
      numCameras = -1; // TODO esse valor não deveria ser 0 ou 1 ???

    } else {
      //
      Log.w(TAG, "obtemFoto() - Erro da obtenção da instância da Camera");
      estadoFinal();
      return;
    }

    try {

      // obtém um novo de arquivo
      file = setUpPhotoFile();

      // atualiza o nome do arquivo onde a foto será armazenada
      mFilename = file.getAbsolutePath();

      Log.d(TAG, "===> Arquivo=" + mFilename);

      if (file.canWrite()) {
        Log.d(TAG, "obtemFoto() - Arquivo pode ser gravado");
      } else {
        Log.d(TAG, "obtemFoto() - Arquivo não pode ser gravado");
      }

      // Obtem a URI do arquivo (esse valor será forncecido a Intent)
      xUri = Uri.fromFile(file); // informa a Uri onde a foto será armazenada
      mUri = Uri.fromFile(file);

      if (xUri == null) {
        Log.w(TAG, "obtemFoto() - xUri=null. Arquivo para armazenamento não foi criado.");
        return;
      } else {
        Log.w(TAG, "obtemFoto() ===> xUri=" + xUri.getPath() + ", xUri=" + xUri);
      }

      // Passa como parâmetro a URI de onde a foto deve ser gravada
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
      mCamera = null;
      estadoFinal();

    }

  }

  /**
   * resultActivityTiraFoto3(int resultCode, Intent data)
   * 
   * A foto encontra-se no caminho dado por xUri.getPath();
   * 
   * @param resultCode
   *          Resultado de execução da Activity
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

      // uma foto foi tirada e encontra-se no endereço xUri
      if (xUri != null) {

        // uma foto foi tirada e encontra-se no endereço xUri
        // exibe informações sobre a localização da foto armazenada no
        // sistema
        Log.d(TAG, "Foto tirada e armazenada em xUri.getPath()=" + xUri.getPath());

        if (mParticipacao != null) {

          // atualiza o caminho onde a foto foi armazenada.
          // getPath(): Gets the decoded path.
          // the decoded path, or null if this is not a hierarchical URI
          // (like "mailto:nobody@google.com") or the URI is invalid
          mParticipacao.setNomeArqFoto(xUri.getPath());

          Log.d(TAG, "mParticipacao=" + mParticipacao);

          // atualiza a orientação da tela para Portrait
          int orientacao = this.getResources().getConfiguration().orientation;

          if (orientacao == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG, "Orientação da tela em LANDSCAPE");

            this.setRequestedOrientation(Configuration.ORIENTATION_PORTRAIT);

          } else if (orientacao == Configuration.ORIENTATION_PORTRAIT) {
            Log.d(TAG, "Orientação da tela em PORTRAIT");
          }

          // atualiza a máquina de estado
          setEstado(2);
          // Executa o próximo passo da máquina de estado
          processaFotos();

        } else {
          // mParticipacao é nulo
          Log.d(TAG, "mParticipação é nulo");
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

      // operação cancelada - indica que nenhuma foto foi tirada.
      Log.w(TAG, "resultActivityTiraFoto3 - Operação Tirar foto cancelada pelo usuário");

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

    Log.d(TAG, "====> processaFotos() <====");

    int tipoFoto = -1;
    int efeitoFoto = -1;

    if (xUri == null) {
      // URI da foto não está disponível
      Log.d(TAG, "URI contendo a foto não está disponível");
      estadoFinal();
    }

    if (mParticipacao != null) {
      // há dados a respeito do participante do evento

      // obtém o tipo da foto
      tipoFoto = mParticipacao.getTipoFoto();

      // obtem o efeito para aplicação na foto
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

      arquivoFotoComMoldura = formataFotoPolaroid(pathPolaroid);

    } else if (tipoFoto == TIPO_FOTO_CABINE) {

      arquivoFotoComMoldura = formataFotoCabine(pathCabine);

    } else {
      Log.w(TAG, "processaFotos() - tipo de foto: " + tipoFoto + " não suportado.");

    }

  

    // -------------------------------------------------------------------------

    // TODO aqui poderíamos ter um passo intermediário na máquina de estados

    // TODO aqui também é necessário atualizar a Uri da foto processada

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
    
    Log.d(TAG,"lastUri: "+lastUri.toString());

    // O próximo passo é enviar o email com a foto já trabalhada.
    // Envia email com a foto pronta
    enviaEmail(lastUri);

  }

  /**
   * processoEfeitoFiltroFoto(int efeitoFoto)
   * 
   * @param efeitoFoto
   */
  private Bitmap processoEfeitoFiltroFoto(int efeitoFoto, Bitmap bitmap) {

    Bitmap resultado=null;
    
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
   * formataFotoCabine(String pathCabine)
   * 
   * @param pathCabine
   */
  private String formataFotoCabine(String pathCabine) {

    if (pathCabine == null) {
      Log.d(TAG, "formataFotoCabine() - Não há moldura definida para foto CABINE");
    }

    // foto formato Cabibe exige três fotos.
    // as fotos serão montadas em sequencoa e será aplicada
    // uma moldura (conforme configuração)
    // observe que a moldura está relacionada ao evento em andamento
    // portanto é necessário ter informações sobre o evento

    // TODO verificar nesse ponto se há três fotos disponível

    // TODO veja que aqui são necessárias 3 fotos
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
   * formataFotoPolaroid(String pathPolaroid)
   * 
   * Pega a foto e a moldura e cria um novo bitmap
   * 
   * @param pathPolaroid
   * 
   */
  private String formataFotoPolaroid(String pathPolaroid) {

    // foto formato Polaroid exige o redimensionamento da foto bem como
    // a inclusão da moldura
    // observe que a moldura está relacionada ao evento em andamento
    // portanto é necessário ter informações sobre o evento

    String arqSaida = null;

    Log.d(TAG, "formataFotoPolaroid() - Foto tipo POLAROID foi selecionada");

    if (pathPolaroid == null) {
      Log.d(TAG, "formataFotoPolaroid() - Não há moldura definida para foto POLAROID");
    }

    if (imagem != null) {

      Bitmap bitmap = imagem.criaBitmap(xUri);

      // Exibe informações a respeito da foto
      imagem.showBitmapInfo(bitmap);

      // Executa o mudança de tamanho da foto
      Bitmap scaledBitmap = imagem.getScaledBitmap2(bitmap, 228, 302);
      // imagem.showBitmapInfo(bm);

      arqSaida = PATH_FOTOS + "polaroid-escalada.png";

      // grava a foto das imagens "juntada"
      boolean gravou = imagem.gravaBitmapArquivo(scaledBitmap, arqSaida);

      if (gravou) {
        // foto armazenada com sucesso
      } else {
        // falha na gravação da foto
      }

      // Bitmap bm = bitmap;

      File moldura = new File(PATH_MOLDURAS + "moldura-polaroid-340x416-red.png");

      Bitmap bmMoldura = imagem.getBitmapFromFile(moldura);

      if (bmMoldura != null) {
        Log.v(TAG, " ==> Tamanho da moldura original: " + imagem.getStringBitmapSize(bmMoldura));
      } else {
        Log.w(TAG, "Não foi possível ler o arquivo: " + moldura);
        return null;
      }

      if ((scaledBitmap != null) && (moldura != null) && (moldura.exists())) {
        scaledBitmap = imagem.processaFotoFormatoPolaroid(scaledBitmap, moldura);
      } else {
        Log.w(TAG, "ERRO");
      }

      // TODO aplicar a moldura a foto

      // combina a foto com a moldura
      Bitmap fotoComMoldura = imagem.aplicaMolduraFotoPolaroid(scaledBitmap, bmMoldura);

      arqSaida = PATH_FOTOS + "polaroid-escalada-com_moldura.png";

      // grava a foto das imagens "juntada"
      gravou = imagem.gravaBitmapArquivo(fotoComMoldura, arqSaida);

    } else {
      Log.w(TAG, "A instância da biblioteca de imagens não está disponível");
    }

    // TODO é necessário gravar o arquivo obtido para que ele possa ser
    // enviado por email

    return arqSaida;
  }

  /**
   * enviaEmail()
   * 
   * Verifica se todas as condições necessárias estão satisfeita para o envio do
   * email com a foto anexada.
   * 
   */
  private void enviaEmail(Uri lastUri) {

    boolean erro = false;

    if (getEstado() < 2) {
      Log.d(TAG, "Foto não foi tirada");
      erro = true;
    }

    if (mParticipante == null) {
      // Log.d(TAG, "mParticipante é null");
      Log.w(TAG, "Não há informações sobre o participante");
      erro = true;
    }

    if (mContratante == null) {
      // Log.d(TAG, "mContrante é null");
      Log.w(TAG, "Não há informações sobre o contratante");
      erro = true;
    }

    if (xUri == null) {
      // Log.d(TAG, "xUri é null");
      Log.d(TAG, "A foto não está disponível para envio");
      erro = true;
    }

    if (!erro) {

      // não há erro conhecido

      // carrega as preferências sobre o envio de email
      SharedPreferences emailPreferences = getSharedPreferences("pref_email", MODE_PRIVATE);

      if (emailPreferences == null) {
        Log.w(TAG, "SharedPreferences não foi encontrada.");
      }

      String subject = emailPreferences.getString("preferencias_assunto", "Evento Inicial");
      String body = emailPreferences.getString("preferencias_descricao", "Segue as informações sobre o evento");

      emailPreferences = null;

      // obtém o email do participante do evento
      String to = mParticipante.getEmail();

      // obtém o email do contratante do evento
      // ele será copiado em BCC no email enviado
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
      // TODO substituir xUri por lastUri, isto é, a URI da foto processada
      //sendEmail(to, cc, subject, body, xUri);
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

    Log.d(TAG, "===> sendEmail()");

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
    // emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageURI);
    // TODO
    //emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, xUri);
    emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);

    Log.d(TAG,"Anexando o arquivo: "+imageUri+" aos anexos ...");
    
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
   * @param emailIntent
   * 
   */
  private void sendEmailByChooser(Intent emailIntent) {

    // TODO aqui pode acontecer de ser necessário forçar a aplicação de
    // email
    Intent chooser = Intent.createChooser(emailIntent, "Selecione sua aplicação de email !");

    if (chooser != null) {

      Log.w(TAG, "chooser.getAction()=" + chooser.getAction());

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
   * @param data
   */
  private void resultActivityChooser(int resultCode, Intent data) {

    Log.d(TAG, "===> processando resultado da ACTIVITY CHOOSER: resultCode=" + resultCode);

    // Obtém o intent
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

      // mensagem exibida após envio de email
      Toast.makeText(this, "Email enviado com sucesso", Toast.LENGTH_LONG).show();

      // Atualiza o estado da máquina de estado
      setEstado(3);
      // Processa o próximo estado
      estadoFinal();

    } else if (resultCode == RESULT_CANCELED) {
      Log.d(TAG, "ACTIVITY_CHOOSER - o envio de email foi cancelado");
      estadoFinal();

    } else {
      Log.d(TAG, "ACTIVITY_CHOOSER - resultCode " + resultCode + " não pde ser tratado");
      estadoFinal();
    }

  }

  /**
   * estadoFinal()
   * 
   * Representa o estado final da máquina de estado.
   * 
   */
  private void estadoFinal() {

    // Obtem as informações sobre a Intent "chamadora"
    Intent i = getIntent();

    // Obtém o estado corrente da máquina de estado
    int estadoCorrente = getEstado();

    if (estadoCorrente == 3) {

      // estado final atingido com sucesso
      Log.d(TAG, "DummyActivity3 - estadoFinal() - final do processamento");

      i.putExtra("br.com.mltech.result", "OK");

      setResult(RESULT_OK, i);

    } else {

      // estado final atingido porém houve falha
      Log.d(TAG, "DummyActivity3 - estadoFinal() - não chegou ao final do processamento");

      i.putExtra("br.com.mltech.result", "NOT_OK");

      setResult(RESULT_CANCELED, i);

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

    // Termina a execução da intent responsável por tirar e enviar uma foto
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
   * getAlbumDir()
   * 
   * @return File
   * 
   */
  public File getAlbumDir(String dir) {

    File storageDir = null;

    if (isExternalMediaMounted()) {

      storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), dir);

      if (storageDir != null) {

        if (!storageDir.mkdirs()) {

          if (!storageDir.exists()) {
            Log.d("TAG", "falha na criação do diretório");
          }

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
   * Atualiza a variável mFilename.
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
    Log.v(TAG, "Variáveis:");
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
   *          Instância da classe Bundle
   * 
   */
  private void showBundle(Bundle bundle) {

    if (bundle == null) {
      Log.w(TAG, "Bundle está vazio");
      return;
    }

    // Obtém um conjunto de chaves do Bundle
    Set<String> setChaves = bundle.keySet();

    // Obtém o tamanho do conjunto
    int size = bundle.size();

    // Exibe o nº de elementos do conjunto
    Log.d(TAG, "Bundle size=" + size);

    int i = 0;

    for (String chave : setChaves) {
      i++;
      Log.d(TAG, i + ") " + chave);
    }

  }

  /**
   * getCameraInstance()
   * 
   * Obtém a instância de uma câmera.
   * 
   * @param cameraID
   *          Identificador da câmera do dispositivo
   * 
   * @return Uma instância de Camera ou null em caso de erro
   * 
   */
  public static Camera getCameraInstance(int cameraID) {

    Camera c = null;

    try {

      /*
       * Creates a new Camera object to access the first back-facing camera on
       * the device. If the device does not have a back-facing camera, this
       * returns null.
       */

      c = Camera.open(cameraID); // attempt to get a Camera instance

    } catch (Exception e) {

      // Camera is not available (in use or does not exist)
      Log.d(TAG, ">>> getCameraInstance(" + cameraID + ") - Camera is not available (in use or does not exist)", e);

    }

    return c; // returns null if camera is unavailable

  }

  /**
   * isCameraWorking(int cameraID)
   * 
   * @param cameraID
   *          Identificador da câmera do dispositivo
   * 
   * @return true se for possível obter ima instância da câmera ou false, caso
   *         contrário.
   */
  boolean isCameraWorking(int cameraID) {

    boolean b = false;

    mCamera = getCameraInstance(cameraID);

    if (mCamera != null) {
      //
      mCamera.release();
      mCamera = null;
      numCameras = -1;

      b = true;

    }

    return b;

  }

  /**
   * verificaComoFormatarFoto()
   * 
   * Processa a foto de acordo com as escolhas do participante. Usa as
   * informações de mParticipação.
   * 
   * @return
   * 
   */
  public Bitmap verificaComoFormatarFoto() {

    // a partir dos dados em mParticipacao é obtida uma foto
    if (mParticipacao == null) {
      Log.d(TAG, "mParticipacao é null");
      return null;
    }

    if (mParticipacao.getTipoFoto() == TIPO_FOTO_POLAROID) {

      //

    } else if (mParticipacao.getTipoFoto() == TIPO_FOTO_CABINE) {

      //

    } else {
      Log.d(TAG, "Tipo de foto " + mParticipacao.getTipoFoto() + " não é suportado");
    }

    return null;

  }

}
