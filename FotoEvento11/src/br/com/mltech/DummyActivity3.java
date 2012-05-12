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
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable.Orientation;
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

public class DummyActivity3 extends Activity {

  private static final String TAG = "DummyActivity3";

  // Definição de constantes para o tipo de foto
  private static final int POLAROID = 1;
  private static final int CABINE = 2;

  // Definição de constantes para o efeito de cores
  private static final int CORES = 11;

  private static final int ACTIVITY_TIRA_FOTO_3 = 113;
  private static final int ACTIVITY_CHOOSER = 150;
  private static final int ACTIVITY_PARTICIPANTE = 102;

  private static Contratante mContratante;
  private static Evento mEvento;
  private static Participante mParticipante;
  private static Participacao mParticipacao;

  private static String mFilename;

  private static Uri xUri;
  private Uri mUri;

  private static int mEstado = -1;

  private static int mContador = 0;

  private static Camera mCamera = null;

  private static int numCameras = -1;

  private static int mCurrentCamera = 0;

  private ManipulaImagem imagem = null;

  /**
   * onCreate(Bundle savedInstanceState)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    Log.d(TAG, "*** onCreate() ***");

    // imagem = new ManipulaImagemOld();
    imagem = new ManipulaImagem();

    showBundle(savedInstanceState);

    mContador++;

    setContentView(R.layout.dummy);

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
   */
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == ACTIVITY_TIRA_FOTO_3) {
      resultActivityTiraFoto3(resultCode, data);
    } else if (requestCode == ACTIVITY_CHOOSER) {
      resultActivityChooser(resultCode, data);
    } else if (requestCode == ACTIVITY_PARTICIPANTE) {
      resultActivityParticipante(resultCode, data);
    } else {
      Log.w(TAG, "Erro ... requestCode: " + requestCode + " não pode ser processado");
    }

  }

  /**
   * iniciaVariaveis()
   */
  private boolean iniciaVariaveis() {

    numCameras = android.hardware.Camera.getNumberOfCameras();

    Log.d(TAG, "Number of Cameras: " + numCameras);

    if (isCameraWorking(0)) {
      Log.w(TAG, "Camera is working ...");
    } else {
      Log.w(TAG, "Camera is not working");
      // showAlert("Camera não está disponível para essa aplicação");
      return false;
    }

    Intent i = this.getIntent();

    int erro = 0;

    if (i.getSerializableExtra("br.com.mltech.contratante") != null) {
      mContratante = (Contratante) i.getSerializableExtra("br.com.mltech.contratante");
    } else {
      Log.w(TAG, "Contratante não pode ser nulo.");
      erro = 1;
    }

    if (i.getSerializableExtra("br.com.mltech.evento") != null) {
      mEvento = (Evento) i.getSerializableExtra("br.com.mltech.evento");
    } else {
      Log.w(TAG, "Evento não pode ser nulo.");
      erro += 2;
    }

    if (erro > 0) {
      Log.w(TAG, "Informações insuficientes para execução (erro=" + erro + ")");

      showAlert("Verifique a configuração da aplicação");

      return false;

    }

    Log.d(TAG, "mContratante=" + mContratante);
    Log.d(TAG, "mEvento=" + mEvento);

    setEstado(0);
    startActivityParticipante();

    return true;

  }

  /**
   * startActivityParticipante()
   */
  private void startActivityParticipante() {

    Intent intent = new Intent(this, ParticipanteActivity.class);

    intent.putExtra("br.com.mltech.evento", mEvento);

    // intent.putExtras(params);

    // Inicia a Activity
    startActivityForResult(intent, ACTIVITY_PARTICIPANTE);

  }

  /**
   * activityParticipanteResult(int resultCode, Intent data)
   * 
   * @param resultCode
   * @param data
   * 
   */
  private void resultActivityParticipante(int resultCode, Intent data) {

    if (getEstado() < 0) {
      Log.d(TAG, "Não há informações sobre o participante");
      return;
    }

    Log.d(TAG, "==> processando resultado da ACTIVITY PARTICIPANTE");

    if (resultCode == RESULT_CANCELED) {
      Log.d(TAG, "resultCode=RESULT_CANCELED - Participante cancelou sua participação");
      mParticipante = null;
      mParticipacao = null;
      return;
    } else if (resultCode != RESULT_OK) {
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

    Log.d(TAG, "mParticipante=" + mParticipante);
    Log.d(TAG, "mParticipacao=" + mParticipacao);

    setEstado(1);
    obtemFoto();

  }

  /**
   * obtemFoto()
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

    xUri = null;
    mUri = null;
    mFilename = null;

    Log.d(TAG, "obtemFoto() - mCurrentCamera: " + mCurrentCamera);

    mCamera = getCameraInstance(mCurrentCamera);

    if (mCamera != null) {
      //
      Log.w(TAG, "Camera is NOT null");
      mCamera.release();
      mCamera = null;
      numCameras = -1;
    } else {
      //
      Log.w(TAG, "Camera is null");
      estadoFinal();
      return;
    }

    try {

      file = setUpPhotoFile();

      mFilename = file.getAbsolutePath();

      Log.d(TAG, "===> Arquivo=" + file.getAbsolutePath());

      if (file.canWrite()) {
        Log.d(TAG, "Arquivo pode ser gravado");
      } else {
        Log.d(TAG, "Arquivo não pode ser gravado");
      }

      xUri = Uri.fromFile(file);
      mUri = Uri.fromFile(file);

      if (xUri == null) {
        Log.w(TAG, "xUri=null. Arquivo para armazenamento não foi criado.");
        return;
      } else {
        Log.w(TAG, "===> xUri=" + xUri.getPath() + ", xUri=" + xUri);
      }

      intent.putExtra(MediaStore.EXTRA_OUTPUT, xUri);

      startActivityForResult(intent, ACTIVITY_TIRA_FOTO_3);

    } catch (IOException e) {

      Log.d(TAG, ">>> obtemFoto() <<< - erro", e);

      e.printStackTrace();
      file = null;
      mFilename = null;
      mUri = null;
      xUri = null;
      estadoFinal();

    }

  }

  /**
   * activityTiraFoto3Result(int resultCode, Intent data)
   * 
   * A foto encontra-se no caminho dado por xUri.getPath();
   * 
   * @param resultCode
   * @param data
   */
  private void resultActivityTiraFoto3(int resultCode, Intent data) {

    Log.d(TAG, "==> Executando o resultado do processamento da ACTIVITY_TiraFoto3");
    Log.d(TAG, "==> resultCode=" + resultCode);

    if (resultCode == RESULT_OK) {

      Log.w(TAG, "xUri: " + xUri);
      Log.w(TAG, "mUri: " + mUri);
      Log.w(TAG, "mFilename: " + mFilename);

      // uma foto foi tirada e encontra-se no endereço xUri
      if (xUri != null) {

        // exibe informações sobre a localização da foto armazenada no
        // sistema
        Log.d(TAG, "Foto tirada e armazenada em xUri.getPath()=" + xUri.getPath());

        if (mParticipacao != null) {

          // atualiza o caminho onde a foto foi armazenada.
          mParticipacao.setNomeArqFoto(xUri.getPath());

          Log.d(TAG, "mParticipacao=" + mParticipacao);

          setEstado(2);
          
          int orientacao = this.getResources().getConfiguration().orientation;
          
          if(orientacao==Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG,"Orientação da tela em LANDSCAPE");
            
            this.setRequestedOrientation(Configuration.ORIENTATION_PORTRAIT);
            
          }
          else if(orientacao==Configuration.ORIENTATION_PORTRAIT) {
            Log.d(TAG,"Orientação da tela em PORTRAIT");      
          }
          
          
          processaFotos();

        } else {
          Log.d(TAG, "mParticipação é nulo");
        }

      } else {
        Log.d(TAG, "xUri is null");
      }

    } else {

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
   * 
   */
  private void processaFotos() {

    Log.d(TAG, "+++ processaFotos() +++");

    int tipoFoto = -1;
    int efeitoFoto = -1;

    if (xUri == null) {
      Log.d(TAG, "xUri é null");
      estadoFinal();
    }

    if (mParticipacao != null) {

      tipoFoto = mParticipacao.getTipoFoto();

      efeitoFoto = mParticipacao.getEfeitoFoto();

    }

    // -------------------------------------------------------------------------

    if (tipoFoto == POLAROID) {

      // foto formato Polaroid exige o redimensionamento da foto bem como
      // a inclusão da moldura
      // observe que a moldura está relacionada ao evento em andamento
      // portanto é necessário ter informações sobre o evento

      Log.d(TAG, "processaFotos() - Foto tipo POLAROID foi selecionada");

      Bitmap bitmap = imagem.criaBitmap(xUri);

      imagem.showBitmapInfo(bitmap);

      Bitmap bm = imagem.getScaledBitmap(bitmap, 20);

      imagem.showBitmapInfo(bm);

    } else if (tipoFoto == CABINE) {

      // TODO veja que aqui são necessárias 3 fotos
      // as fotos podem ser armazenadas em um array de fotos
      Log.d(TAG, "processaFotos() - Foto tipo CABINE foi selecionada");

    } else if (tipoFoto == CABINE) {

    }

    // -------------------------------------------------------------------------

    // Envia email com a foto pronta
    enviaEmail();

  }

  /**
   * enviaEmail()
   */
  private void enviaEmail() {

    boolean erro = false;

    if (getEstado() < 2) {
      Log.d(TAG, "Foto não foi tirada");
      erro = true;
    }

    if (mParticipante == null) {
      Log.d(TAG, "mParticipante é null");
      erro = true;
    }

    if (mContratante == null) {
      Log.d(TAG, "mContrante é null");
      erro = true;
    }

    if (xUri == null) {
      Log.d(TAG, "xUri é null");
      erro = true;
    }

    if (!erro) {

      String to = mParticipante.getEmail();
      String cc = mContratante.getEmail();
      String subject = "Evento Inicial";
      String body = "Segue as informações sobre o evento";

      sendEmail(to, cc, subject, body, xUri);

    }

  }

  /**
   * sendEmail(String emailParticipante, String emailContratante, String
   * subject, String text, Uri imageURI)
   * 
   * @param emailParticipante
   * @param emailContratante
   * @param subject
   * @param text
   * @param imageURI
   * 
   */
  private void sendEmail(String emailParticipante, String emailContratante, String subject, String text, Uri imageURI) {

    Intent emailIntent = new Intent(Intent.ACTION_SEND);

    // emailIntent.setType("message/rfc822");
    emailIntent.setType("image/jpg");

    Log.d(TAG, "===> sendEmail");

    // To:
    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { emailParticipante });

    if (emailContratante != null) {
      // email do contratante foi fornecido (BCC:)
      emailIntent.putExtra(Intent.EXTRA_BCC, new String[] { emailContratante });
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
    emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, xUri);

    // emailIntent.setType("image/png");
    emailIntent.setType("image/jpg");

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

    startActivityForResult(chooser, ACTIVITY_CHOOSER);

  }

  /**
   * activityChooserResult(int resultCode, Intent data)
   * 
   * @param resultCode
   * @param data
   */
  private void resultActivityChooser(int resultCode, Intent data) {

    Log.d(TAG, "===> processando resultado da ACTIVITY CHOOSER: resultCode=" + resultCode);

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

      setEstado(3);
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
   */
  private void estadoFinal() {

    Intent i = getIntent();

    int estadoCorrente = getEstado();

    if (estadoCorrente == 3) {

      // estado final atingido com sucesso
      Log.d(TAG, "DummyActivity3 - estadoFinal() - final do processamento");

      i.putExtra("br.com.mltech.result", "OK");

      setResult(RESULT_OK, i);

    } else {
      Log.d(TAG, "DummyActivity3 - estadoFinal() - não chegou ao final do processamento");

      i.putExtra("br.com.mltech.result", "NOT_OK");

      setResult(RESULT_CANCELED, i);

    }

    
    int orientacao = this.getResources().getConfiguration().orientation;
    
    if(orientacao==Configuration.ORIENTATION_LANDSCAPE) {
      Log.d(TAG,"Orientação da tela em LANDSCAPE");
      
      this.setRequestedOrientation(Configuration.ORIENTATION_PORTRAIT);
      
    }
    else if(orientacao==Configuration.ORIENTATION_PORTRAIT) {
      Log.d(TAG,"Orientação da tela em PORTRAIT");      
    }
    
    finish();

  }

  /**
   * setEstado(int e)
   * 
   * cONFIGURA O estado de uma máquina de estado
   * 
   * @param e
   *          novo estado
   */
  private void setEstado(int e) {
    Log.d(TAG, "transição do estado: " + mEstado + " para o estado: " + e);
    mEstado = e;
  }

  /**
   * getEstado()
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

    boolean b;

    b = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());

    if (b) {
      // disco está montado
      Log.d(TAG, "Media externa está montada.");
    } else {
      // disco não está montado
      Log.w(TAG, "Media externa não está montada.");
    }

    return b;

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
   * @return File
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
   * @return
   * 
   * @throws IOException
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
   */
  public void showVariables() {
    Log.d(TAG, "=================================");
    Log.d(TAG, "Variáveis:");
    if (mParticipante != null)
      Log.d(TAG, "  mParticipante=" + mParticipante);
    if (mParticipacao != null)
      Log.d(TAG, "  mParticipacao=" + mParticipacao);
    if (mContratante != null)
      Log.d(TAG, "  mContratante=" + mContratante);
    if (mFilename != null)
      Log.d(TAG, "  mFilename=" + mFilename);
    if (mUri != null)
      Log.d(TAG, "  mUri=" + mUri);
    if (xUri != null)
      Log.d(TAG, "  xUri=" + xUri);
    Log.d(TAG, "  mEstado=" + mEstado);
    Log.d(TAG, "  mContador=" + mContador);
    Log.d(TAG, "=================================");
  }

  /**
   * showBundle(Bundle b)
   * 
   * @param b
   */
  private void showBundle(Bundle b) {

    if (b == null) {
      Log.w(TAG, "Bundle está vazio");
      return;
    }

    // Obtém um conjunto de chaves do Bundle
    Set<String> setChaves = b.keySet();

    // Obtém o tamanho do conjunto
    int size = b.size();

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
   * 
   * @return
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

    if (mParticipacao.getTipoFoto() == POLAROID) {

      //

    } else if (mParticipacao.getTipoFoto() == CABINE) {

      //

    } else {
      Log.d(TAG, "Tipo de foto " + mParticipacao.getTipoFoto() + " não é suportado");
    }

    return null;

  }

}
