package br.com.mltech;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import br.com.mltech.modelo.Participacao;
import br.com.mltech.utils.FileUtils;
import br.com.mltech.utils.ManipulaImagem;
import br.com.mltech.utils.camera.CameraTools;

/**
 * CameraActivity
 * 
 * Activity respons�vel por controlar (implementar) as funcionalidades de uma
 * uma c�mera fotogr�fica.
 * 
 * @author maurocl
 * 
 */
public class CameraActivity extends Activity implements Constantes {

  private static final String TAG = "CameraActivity";

  // inst�ncia da c�mera
  private static Camera mCamera;

  // inst�ncia da CameraPreview
  private static CameraPreview mPreview;

  // flag usado para controle da aplica��o
  private static int flag = 0;

  // Vari�vel usada para desenhar a imagem da c�mera na tela
  private FrameLayout layoutPreview;

  // bot�es da m�quina
  private static Button btnOk;

  // bot�o nova foto
  private static Button btnNovo;

  // bot�o cancelar
  private static Button btnCancelar;

  // bot�o capturar
  private static Button btnCapture;

  // handler usado para controle do disparo autom�tico
  private Handler handler = new TestHandler();

  // controla o tipo de disparo da c�mera
  private static final int MANUAL = 1;

  private static final int AUTOMATICO = 2;

  // controla o tipo de disparo da c�mera
  private static int tipoDisparo = AUTOMATICO;
  
  // controla o n� de vezes que a activity foi reinicializada.
  private static int numRestarts = 0;

  /**
   * Bitmap
   */
  private static Bitmap mImageBitmap;

  /**
   * Uri onde est� localizada a foto
   */
  private static Uri mUri;

  /**
   * Instancia da classe Participa��o.
   */
  private static Participacao mParticipacao;

  /**
   * identificador da c�mera do dispositivo
   */
  private static int cameraId = 0;

  /**
   * onCreate(Bundle savedInstanceState)<br>
   * 
   * Esta activity espera dois par�metros para sua execu��o:<br>
   * - uma inst�ncia da classe Participacao (para poder escolher ...)<br>
   * - o identificador da c�mera que ser� usado.
   * 
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    Log.d(TAG, "*** onCreate() ***");

    setContentView(R.layout.cameraprev);

    // obtem informa��es da intent chamadora
    Intent intent = getIntent();

    // Obt�m informa��es do participante
    obtemInfoParticipacao(intent);

    // Obt�m o identificador da c�mera
    obtemIdentificadorCamera(intent);

    Log.d(TAG, "onCreate() - N� de c�meras do dispositivo:  " + Camera.getNumberOfCameras());

    boolean isCameraAvailable = CameraTools.checkCameraHardware(this);

    if (!isCameraAvailable) {
      Log.e(TAG, "onCreate() - n�o h� c�meras dispon�veis");
      return;
    }

    if (!CameraTools.isCameraWorking(cameraId)) {
      Log.e(TAG, "onCreate() - c�mera: " + cameraId + " n�o est� funcionando");
      return;
    }

    // objeto onde ser� exibido a tela da c�mera
    layoutPreview = (FrameLayout) findViewById(R.id.camera_preview);

    /**
     * Configura o tratador de eventos dos bot�es
     */

    btnCapture = (Button) findViewById(R.id.button_capture);
    btnOk = (Button) findViewById(R.id.btnOk);
    btnNovo = (Button) findViewById(R.id.btnNovo);
    btnCancelar = (Button) findViewById(R.id.btnCancelar);

    if (tipoDisparo == MANUAL) {
      btnCapture.setVisibility(android.view.View.VISIBLE);
      btnOk.setVisibility(android.view.View.GONE);
      btnNovo.setVisibility(android.view.View.GONE);
      btnCancelar.setVisibility(android.view.View.GONE);
    }
    else if (tipoDisparo == AUTOMATICO) {
      btnCapture.setVisibility(android.view.View.GONE);
      btnOk.setVisibility(android.view.View.GONE);
      btnNovo.setVisibility(android.view.View.GONE);
      btnCancelar.setVisibility(android.view.View.GONE);
    }

    /**
     * Bot�o Capturar
     */
    btnCapture.setOnClickListener(new OnClickListener() {

      public void onClick(View view) {

        if (mCamera == null) {
          Log.e(TAG, "onClick(btnCapture) - bot�o Capturar - mCamera is null na hora de acionar o dispatador !!!");
          return;
        }

        // bot�o de captura foi pressionado
        Log.i(TAG, "---------------------------------");
        Log.i(TAG, "bot�o de captura foi pressionado");
        Log.i(TAG, "---------------------------------");

        // dispara uma foto
        mCamera.takePicture(shutter, null, jpeg);

      }
    });

    /**
     * Bot�o Nova foto
     */
    btnNovo.setOnClickListener(new OnClickListener() {

      public void onClick(View view) {

        Log.d(TAG, "onClick(btnNovo) - bot�o nova foto");

        // desabilita os bot�es n�o usados
        btnOk.setVisibility(android.view.View.GONE);
        btnNovo.setVisibility(android.view.View.GONE);
        btnCancelar.setVisibility(android.view.View.GONE);

        // torna vis�vel o bot�o capturar
        btnCapture.setVisibility(android.view.View.VISIBLE);

        reiniciaCamera();

      }
    });

    /**
     * Trata o pressionamento do Bot�o Ok.<br>
     * 
     * Confirma a foto.<br>
     * 
     * e sai da activity.<br>
     * 
     */
    btnOk.setOnClickListener(new OnClickListener() {

      public void onClick(View view) {

        Log.d(TAG, "onClick(btnOk) - bot�o Ok - Confirma a foto");

        CameraInfo cameraInfo = new CameraInfo();

        // obt�m informa��es sobre a c�mera
        Camera.getCameraInfo(cameraId, cameraInfo);

        // The direction that the camera faces
        Log.i(TAG, "onClick(btnOk) - cameraInfo.facing=" + cameraInfo.facing);

        // The orientation of the camera image. 
        Log.i(TAG, "onClick(btnOk) - cameraInfo.orientation=" + cameraInfo.orientation);

        if (mImageBitmap == null) {
          Log.w(TAG, "onClick(btnOk) - mImageBitmap � nulo");
        }

        // exibe informa��es sobre a foto
        ManipulaImagem.showBitmapInfo2(mImageBitmap);

        Log.i(TAG, "onClick(btnOk) - mUri: [" + mUri + "]");

        // cria uma intent de resposta
        Intent intent = new Intent();

        // Set the data this intent is operating on. 
        // This method automatically clears any type that was previously set by setType(String). 
        intent.setData(mUri);

        // estabelece o resultado da execu��o da activity
        setResult(RESULT_OK, intent);

        Log.d(TAG, "onClick(btnOk) - retorno");

        // finaliza a activity
        finish();

      }
    });

    /**
     * Bot�o cancelar
     */
    btnCancelar.setOnClickListener(new OnClickListener() {

      public void onClick(View view) {

        // finalizaActivity();
        Log.d(TAG, "onClick(btnCancelar) - bot�o cancelar");

        finalizaActivityResultCanceled();

      }
    });

  }

  /**
   * Obt�m o identificador da c�mera que ser� usada.<br>
   * 
   * Caso o par�metro de configura��o n�o seja configurado ser� usado 0 como default.
   * 
   * @param intent
   */
  private void obtemIdentificadorCamera(Intent intent) {

    // obt�m o identificador da c�mera
    cameraId = intent.getIntExtra("br.com.mltech.cameraId", -1);

    if (cameraId == -1) {
      Log.w(TAG, "obtemIdentificadorCamera() - O identificador da c�mera n�o foi encontrado. Ser� usado 0 como padr�o.");
      cameraId = 0;
    }

    Log.d(TAG, "obtemIdentificadorCamera() - atualizando o dientificador da c�mera - cameraId = " + cameraId);
  }

  /**
   * Obtem informa��o sobre a participa��o.
   * 
   * @param intent
   */
  private void obtemInfoParticipacao(Intent intent) {

    if (intent.getSerializableExtra(Constantes.PARTICIPACAO) != null) {

      mParticipacao = (Participacao) intent.getSerializableExtra(Constantes.PARTICIPACAO);

    }
    else {
      Log.w(TAG, "obtemInfoParticipacao() - mParticipa��o � nula");
      mParticipacao = new Participacao(null, Constantes.TIPO_FOTO_POLAROID, Constantes.CORES, null);
      Log.w(TAG, "obtemInfoParticipacao() - Usando um contratante null, formato de foto polaroid a cores, e nome do arquivo nulo");
    }
  }

  /**
   * � uma inst�ncia da classe ShutterCallback.
   * 
   * Define um objeto que cont�m uma inst�ncia da classe ShutterCallback
   * respons�vel pelo tratamento do evento do disparo do bot�o da c�mera �
   * invocado assim que o obturador da c�mera entra em a��o e captura a imagem.
   * 
   */
  final ShutterCallback shutter = new ShutterCallback() {

    public void onShutter() {

      Log.d(TAG, "----------------------------------");
      Log.d(TAG, "onShutter() callback disparado !!!");
      Log.d(TAG, "----------------------------------");
    }

  };

  /**
   * � uma inst�ncia da classe PictureCallback.
   * 
   * Define um objeto que cont�m uma inst�ncia da classe PictureCallback
   * respons�vel pelo tratamento do evento do recebimento da imagem "crua" da
   * c�mera (quando a imagem "crua" tirada pela c�mera est� dispon�vel.
   */
  final PictureCallback raw = new PictureCallback() {

    public void onPictureTaken(byte[] data, Camera camera) {

      Log.d(TAG, "--------------------");
      Log.d(TAG, "onPictureTaken - raw");
      Log.d(TAG, "--------------------");

    }

  };

  /**
   * � uma inst�ncia da classe PictureCallback.<br>
   * 
   * Define um objeto que cont�m uma inst�ncia da classe PictureCallback
   * respons�vel pelo tratamento do evento do recebimento da imagem JPEG da
   * c�mera.<br>
   * 
   * Invocado assim que a imagem com compress�o (comprimida), no formato JPEG,
   * est� dispon�vel.<br>
   * 
   */
  final PictureCallback jpeg = new PictureCallback() {

    /**
     * M�todo chamado quando houver uma foto JPEG dispon�vel (callback da
     * opera��o de tirar foto)
     */
    public void onPictureTaken(byte[] data, Camera camera) {

      Log.i(TAG, "-----------------------");
      Log.i(TAG, "onPictureTaken() - jpeg");
      Log.i(TAG, "-----------------------");

      if (data != null) {
        Log.d(TAG, "onPictureTaken() - data: " + fmtFloat((float) (data.length / 1024.0)) + " KBytes");
      } else {
        Log.d(TAG, "onPictureTaken() - data n�o retornou nenhum dado");
      }

      // TODO aqui deveremos criar o nome dos arquivos
      File f = FileUtils.obtemNomeArquivo(".jpg");
      String nomeArquivo = f.getAbsolutePath();

      if (data != null) {

        // cria o bitmap a partir do array de bytes capturados
        mImageBitmap = ManipulaImagem.getBitmapFromByteArray(data);

        if (mImageBitmap == null) {
          Log.w(TAG, "mImageBitmap � nulo");
        }

        // grava a foto no arquivo
        boolean gravou = gravaArquivo(data, nomeArquivo);

        if (gravou) {
          Log.d(TAG, "onPictureTaken() - arquivo " + nomeArquivo + " gravado com sucesso.");
          //Toast.makeText(getBaseContext(), "Arquivo " + nomeArquivo + " gravado com sucesso.", Toast.LENGTH_SHORT).show();
        } else {
          Log.d(TAG, "onPictureTaken() - falha na grava��o do arquivo " + nomeArquivo);
        }

        File file = new File(nomeArquivo);
        mUri = Uri.fromFile(file);

      } else {
        Log.w(TAG, "onPictureTaken() - nenhum dado foi retornado pela c�mera");
      }

      // foto j� foi tirada e gravada.
      // agora � ...
      // reiniciaCamera();

      if (tipoDisparo == MANUAL) {
        btnOk.setVisibility(android.view.View.VISIBLE);
        btnNovo.setVisibility(android.view.View.VISIBLE);
        btnCancelar.setVisibility(android.view.View.VISIBLE);
        // torna invis�vel o bot�o capturar
        btnCapture.setVisibility(android.view.View.GONE);
      }
      else if (tipoDisparo == AUTOMATICO) {
        // desabilita bot�es
        btnOk.setVisibility(android.view.View.GONE);
        btnNovo.setVisibility(android.view.View.GONE);
        btnCancelar.setVisibility(android.view.View.GONE);
        btnCapture.setVisibility(android.view.View.GONE);

        // confirma a foto
        confirmaFoto();

      }

    }

  };

  /**
   * onStart(2)
   */
  @Override
  protected void onStart() {

    super.onStart();

    Log.d(TAG, "*** onStart() ***");

  }

  /**
   * onResume(3)
   * 
   * Esse callback � chamado a partir da sequencia:<br>
   * onCreate() --> onStart() --> onResume() ou<br>
   * ap�s ( a aplica��o estar no estado Pause e retorna a funcionar).<br>
   * onPause() --> on Resume().<br>
   * <br>
   * Obs: cameraId � uma vari�vel da classe.
   * 
   */
  @Override
  protected void onResume() {

    super.onResume();

    Log.d(TAG, "*** onResume() ***");

    Log.d(TAG, "onResume() - cameraId=" + cameraId);

    Log.d(TAG, "==> -4");

    if (mCamera == null) {

      Log.d(TAG, "==> -3 - mCamera � nula");
      // obt�m uma inst�ncia da c�mera
      mCamera = CameraTools.getCameraInstance(cameraId);
      Log.d(TAG, "==> -2 - ap�s obter inst�ncia da c�mera: " + cameraId);

    }

    Log.i(TAG, "==> mCamera=" + mCamera);

    Log.d(TAG, "==> -1");

    //--------------------------------------------------------
    // configura os par�metros de configura��o da c�mera
    //--------------------------------------------------------

    if (mPreview == null) {

      Log.d(TAG, "==> mPreview � nulo");

      // configura os par�metros da c�mera apenas se mPreview for nulo.
      Log.w(TAG, "onResume() - chama configuraParamCamera()");

      configuraParamCamera(mParticipacao.getEfeitoFoto());

    }
    else {

      Log.d(TAG, "==> mPreview n�o � nulo");
      Log.w(TAG, "onResume() - Configura��o dos par�metros n�o foi feita !!!");

    }

    Log.d(TAG, "==> 0 - antes da cria��o do mPreview");

    // cria uma inst�ncia da "tela" de preview
    mPreview = new CameraPreview(this, mCamera);

    Log.d(TAG, "==> 0.1 - ap�s a cria��o do mPreview");

    // TODO verificar o layout da c�mera
    layoutPreview.addView(mPreview);

    Log.d(TAG, "==> 0.2 - ap�s adicionar a view no layout");

    // TODO verificar o disparo autom�tico da c�mera      
    if (tipoDisparo == AUTOMATICO) {
      setDisparoAutomatico(3);
    }
    else if (tipoDisparo == MANUAL) {
      // n�o faz nada
    }

    Log.d(TAG, "==> 1 - ap�s selecionar o tipo do disparo");

    if (mCamera == null) {

      Log.d(TAG, "==> 2");
      Log.w(TAG, "onResume - mCamera is null");

    } else {

      Log.d(TAG, "==> 3 - mCamera n�o � nula");

      // camera � diferente de null

      Log.d(TAG, "onResume() - flag=" + flag);

      if (flag == 0) {

        Log.d(TAG, "==> 4 - flag alterado de 0 ==> 1");

        flag = 1;

      } else {

        Log.d(TAG, "==> 5 - antes da chamada do m�todo mCamera.stopPreview()");

        // TODO observe que aqui estmos dando um stop e depois damos um start no preview
        // TODO ser� que isso � realmente necess�rio ????
        
        // Stops capturing and drawing preview frames to the surface, and resets
        // the camera for a future call to startPreview().
        mCamera.stopPreview();

        Log.d(TAG, "==> 6 - ap�s chamada do m�todo mCamera.stopPreview()");

        Log.d(TAG, "onResume() - antes da chamada do m�todo mCamera.startPreview()");

        // Starts capturing and drawing preview frames to the screen
        mCamera.startPreview();

        Log.d(TAG, "onResume() - ap�s chamada do m�todo mCamera.startPreview()");

      }

    }

  }

  /**
   * onPause(4)
   * 
   * Activity foi colocada em pausa
   * 
   */
  @Override
  protected void onPause() {

    super.onPause();

    Log.d(TAG, "*** onPause() ***");

    // TODO � necess�rio dar um stop no preview antes de liberar a c�mera ????
    
    // libera o uso da c�mera para outras aplica��es
    boolean isCameraReleased = releaseCamera();

    if (isCameraReleased) {

      // a c�mera foi liberada com sucesso
      Log.d(TAG, "onPause() - c�mera liberada com sucesso");

    } else {

      // a c�mera j� estava desligada
      Log.w(TAG, "onPause() - falha na libera��o da c�mera");

    }

  }

  /**
   * onStop()
   */
  @Override
  protected void onStop() {

    super.onStop();
    Log.d(TAG, "*** onStop() ***");

  }

  /**
   * onRestart()
   * 
   * Aplica��o foi reinicializada.<br>
   * 
   * � executado ap�s um onStop()<br>
   * 
   */
  @Override
  protected void onRestart() {

    super.onRestart();

    // incrementa o n� de vezes que a activity foi reinicializada.
    numRestarts++;
    
    Log.d(TAG, "*");
    Log.d(TAG, "*** onRestart("+numRestarts+") ***");
    Log.d(TAG, "*");

  }

  /**
   * onDestroy();
   */
  @Override
  protected void onDestroy() {

    super.onDestroy();

    Log.d(TAG, "*** onDestroy() ***");

    if (mCamera != null) {

      Log.d(TAG, "onDestroy() - before release");

      // TODO verificar se essa opera��o � realmente necess�ria
      // Disconnects and releases the Camera object resources.
      mCamera.release();

      Log.d(TAG, "onDestroy() - after release");

    } else {

      Log.i(TAG, "onDestroy() - c�mera est� liberada");

    }

  }

  /**
   * onSaveInstanceState(Bundle outState)
   */
  @Override
  protected void onSaveInstanceState(Bundle outState) {

    super.onSaveInstanceState(outState);

    Log.d(TAG, "*** onSaveInstanceState() ***");

  }

  /**
   * onRestoreInstanceState(Bundle savedInstanceState)
   */
  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {

    super.onRestoreInstanceState(savedInstanceState);

    Log.d(TAG, "*** onRestoreInstanceState() ***");

  }

  /**
   * Exibe algumas configura��es dos par�metros da c�mera.
   * 
   * @param c
   *          Inst�ncia de uma c�mera
   * 
   */
  public static void showCameraParameters(Camera c) {

    if (c == null) {
      Log.w(TAG, "showCameraParameters() - A inst�ncia da c�mera est� nula");
      return;
    }

    // Obt�m a configura��o dos par�metros atuais
    Camera.Parameters parameters = c.getParameters();

    if (parameters == null) {
      //Log.w(TAG, "showCameraParameters() - Par�metros de configura��o da c�mera est� nulo");
      Log.w(TAG, "showCameraParameters() - C�mera n�o retornou nenhum par�metro de configura��o");
      return;
    }

    // obt�m o tamanho da imagem
    Size size = parameters.getPictureSize();

    Log.v(TAG, "showCameraParameters() - getPictureSize: " + CameraTools.getCameraSize(size));
    Log.v(TAG, "showCameraParameters() - getHorizontalViewAngle: " + parameters.getHorizontalViewAngle());
    Log.v(TAG, "showCameraParameters() - getJpegThumbnailQuality: " + parameters.getJpegThumbnailQuality());

  }

  /**
   * Reinicia a c�mera (o preview da c�mera).
   * 
   */
  private void reiniciaCamera() {

    SurfaceHolder surfaceHolder = null;

    Log.d(TAG, "reiniciaCamera()");

    if (mCamera != null) {

      // cria uma nova inst�ncia de CameraPreview
      mPreview = new CameraPreview(this, mCamera);

      // adiciona o "tela" de preview ao layout
      layoutPreview.addView(mPreview);

    } else {

      Log.w(TAG, "reiniciaCamera() - c�mera n�o pode ser reinicializada pois � nula");

    }

    /*
     * if (mPreview != null) { surfaceHolder = mPreview.getHolder(); } else {
     * Log.w(TAG, "reiniciaCamera() - mPreview est� nulo"); }
     */

    try {
      // Sets the Surface to be used for live preview. 
      // Either a surface or surface texture is necessary for preview, 
      // and preview is necessary to take pictures. 
      // The same surface can be re-set without harm. 
      // Setting a preview surface will un-set any preview surface texture that was set 
      // via setPreviewTexture(SurfaceTexture). 
      mCamera.setPreviewDisplay(surfaceHolder);

      // Starts capturing and drawing preview frames to the screen. 
      // Preview will not actually start until a surface is supplied with 
      // setPreviewDisplay(SurfaceHolder) or setPreviewTexture(SurfaceTexture). 
      mCamera.startPreview();

    } catch (IOException e) {
      Log.w(TAG, "reiniciaCamera() - IOException", e);
    }

  }

  /**
   * Libera a c�mera para ser usada em outras aplica��es (se a c�mera estiver em
   * uso pela aplica��o)
   * 
   * @return true se a c�mera foi liberada com sucesso ou false em caso de erro.
   */
  private boolean releaseCamera() {

    if (mCamera == null) {
      // c�mera n�o estava ligada
      Log.w(TAG, "releaseCamera() - c�mera n�o pode ser liberada pois n�o h� uma inst�ncia da Camera em uso.");
      return false;
    }

    boolean isCameraReleased = false;

    // libera a c�mera
    // desconecta e libera os recursos do objeto Camera.
    // Voc� deve chamar esse m�todo t�o logo quanto tenha terminado de usar o objeto Camera
    // You must call this as soon as you're done with the Camera object.
    mCamera.release();

    // atribui null para c�mera corrente
    mCamera = null;

    Log.d(TAG, "releaseCamera() - c�mera liberada com sucesso");

    isCameraReleased = true;

    // retorna o resultado da libera��o da c�mera
    return isCameraReleased;

  }

  /**
   * Grava uma foto num arquivo. A foto encontra-se em representada por um array
   * de bytes.
   * 
   * @param data
   *          Array de bytes
   * 
   * @param nomeArquivo
   *          Nome do arquivo onde a foto ser� gravada
   * 
   * @return true se o arquivo gravado com sucesso ou false caso ocorra algum
   *         erro.
   * 
   */
  private boolean gravaArquivo(byte[] data, String nomeArquivo) {

    if (!validaExternalStorageAvailable()) {
      return false;
    }

    // cria uma arquivo para armazernar a foto
    File f = new File(nomeArquivo);

    // exibe informa��es sobre o arquivo criado
    FileUtils.showFile(f);

    FileOutputStream fos = null;

    boolean gravou = false;

    try {

      try {

        fos = new FileOutputStream(f);
        fos.write(data);

        gravou = true;

      } catch (FileNotFoundException e) {

        Log.w(TAG, "gravaArquivo() - FileNotFoundException: arquivo " + f.getAbsolutePath() + " n�o foi encontrado.");

      } catch (IOException e) {

        Log.w(TAG, "gravaArquivo() - IOException: " + f.getAbsolutePath());

      } catch (Exception e) {

        Log.w(TAG, "gravaArquivo() - Falha na grava��o do arquivo: " + f.getAbsolutePath());

      } finally {

        if (fos != null) {
          fos.close();
        }

      }

    } catch (IOException e) {
      Log.w(TAG, "gravaArquivo() - IOException", e);
    }

    if (gravou) {
      String msg = "gravaArquivo() - Arquivo: " + f.getAbsolutePath() + " foi gerado e ocupa "
          + fmtFloat((float) (f.length() / 1024.0)) + " KBytes";

      // loga a mensagem
      Log.d(TAG, msg);
    }

    // retorna o resultado da grava��o
    return gravou;

  }

  /**
   * 
   * @return true se a media existir e estiver permiss�o de escrita, caso
   *         contr�rio retorno false.
   */
  private boolean validaExternalStorageAvailable() {

    boolean retorno = true;

    // obt�m o estado do dispositivo de armazenamento externo
    String externalStorageState = Environment.getExternalStorageState();

    if (externalStorageState == null) {
      Log.w(TAG, "gravaArquivo() - externalStorageState retornou null");
      retorno = false;
    }

    if (!externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
      // m�dia n�o est� montada
      Log.w(TAG, "gravaArquivo() - m�dia n�o est� montada");
      retorno = false;
    }

    if (externalStorageState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
      // m�dia montada apenas para leitura
      Log.w(TAG, "gravaArquivo() - m�dia montada por�m est� ReadOnly");
      retorno = false;
    }

    // exibe o estado do dispositivo de externo de armazenamento
    Log.d(TAG, "gravaArquivo() - getExternalStorageState()=" + Environment.getExternalStorageState());

    return retorno;

  }

  /**
   * Formata um valor float para duas casas decimais.
   * 
   * @param args
   *          valor float para formatar
   * 
   * @return uma string contendo o valor formatado com duas casas decimais.
   * 
   */
  private static String fmtFloat(float args) {

    java.util.Formatter fmt = new java.util.Formatter();

    String formattedString = fmt.format("%.2f", args).toString();

    return formattedString;

  }

  /**
   * Dispara automaticamente a c�mera ap�s o n�mero de segundos fornecidos.
   * 
   * @param segundos
   *          n� de segundos antes do disparo da foto
   * 
   */
  private void setDisparoAutomatico(int segundos) throws IllegalArgumentException {

    if (segundos < 0) {
      throw new IllegalArgumentException("O tempo n�o poder� ser negativo");
    }

    if (mCamera == null) {
      Log.e(TAG, "setDisparoAutomatico() - c�mera est� nula");
      return;
    }

    // Exibe o aviso de disparo autom�tico
    Toast.makeText(getBaseContext(), "disparo autom�tico em " + segundos + " segundos.", Toast.LENGTH_SHORT).show();

    Message msg = new Message();
    msg.what = 1;
    // envia a mensagem
    handler.sendMessageDelayed(msg, segundos * 1000);

    Log.i(TAG, "-------------------------------------");
    Log.i(TAG, "captura foi disparada automaticamente");
    Log.i(TAG, "-------------------------------------");

    // tira uma foto
    // mCamera.takePicture(shutter, null, jpeg);

  }

  /**
   * TestHandler
   * 
   * @author maurocl
   * 
   */
  private class TestHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {

      super.handleMessage(msg);

      // o atributo msg.what permite identificar a mensagem
      if (msg.what == 1) {

        Toast.makeText(getBaseContext(), "Disparando a captura ...", Toast.LENGTH_SHORT).show();

        // tira uma foto
        mCamera.takePicture(shutter, null, jpeg);

      }
    }

  }

  /**
   * 
   * O bot�o captura somente estar� dispon�vel no modo manual.
   * 
   * 
   */
  private void configurarBotoes() {

    //btnOk --> confirma e grava a foto

    //btnNovo --> tira uma nova foto

    //btnCancelar; --> retorna sem uma foto

    //btnCapture; --> captura a foto de forma manual

  }

  /**
   * Configura os par�metros da c�mera.<br>
   * 
   * Usa a vari�veis: mCamera<br>
   * 
   * @param efeitoFoto
   *          Efeito da foto (cores ou P&B).
   * 
   */
  private void configuraParamCamera(int efeitoFoto) {

    // Set the clockwise rotation of preview display in degrees. 
    // This affects the preview frames and the picture displayed after snapshot. 
    // This method is useful for portrait mode applications. 
    // Note that preview display of front-facing cameras is flipped horizontally before the rotation, 
    // that is, the image is reflected along the central vertical axis of the camera sensor. 
    // So the users can see themselves as looking into a mirror. 
    int displayOrientation = 0;

    Log.d(TAG, "configuraParamCamera() - in�cio");

    Log.d(TAG, "configuraParamCamera() - setDisplayOrientation(" + displayOrientation + ")");
    mCamera.setDisplayOrientation(displayOrientation);

    Log.d(TAG, "configuraParamCamera() - setCameraDisplayOrientation");
    CameraTools.setCameraDisplayOrientation(this, cameraId, mCamera);

    // Obt�m os par�metros de configura��o da c�mera
    // Obt�m a configura��o corrente para esse servi�o de c�mera.
    Camera.Parameters params = mCamera.getParameters();

    //HashMap<String, List<String>> hash = CameraTools.getParametersDetail(mCamera);
    
    CameraTools.showParametersDetail(mCamera);
    
    // altera o tamanho da foto
    params.setPictureSize(640, 480);

    /*
     * EFFECT_AQUA EFFECT_BLACKBOARD EFFECT_MONO EFFECT_NEGATIVE EFFECT_NONE
     * EFFECT_POSTERIZE EFFECT_SEPIA EFFECT_SOLARIZE EFFECT_WHITEBOARD
     */

    if (efeitoFoto == CORES) {
      Log.d(TAG, "configuraParamCamera() - altera o color effect para cores");
      params.setColorEffect(Camera.Parameters.EFFECT_NONE);
    }
    else if (efeitoFoto == PB) {
      Log.d(TAG, "configuraParamCamera() - altera o color effect para preto & branco");
      params.setColorEffect(Camera.Parameters.EFFECT_MONO);
    }
    else {
      // o valor default � nenhum efeito
      params.setColorEffect(Camera.Parameters.EFFECT_NONE);
    }

    //params.setColorEffect(Camera.Parameters.EFFECT_SEPIA);

    // Atualiza os parametros
    // Atualiza a configura��o para esse servi�o de c�mera.
    try {
      mCamera.setParameters(params);
      Log.d(TAG, "configuraParamCamera() -  par�metros atualizados com sucesso");
    } catch (RuntimeException e) {
      Log.w(TAG, "configuraParamCamera() -  RuntimeException durante a configura��o da c�mera");
    }

    // Exibe alguns parametros da camera
    //showCameraParameters(mCamera);

    Log.d(TAG, "configuraParamCamera() - fim");

  }

  /**
   * Confirma a foto tirada, isto �, a foto que foi disparada automaticamente pelo temporizador.
   * 
   */
  private void confirmaFoto() {

    Log.i(TAG, "---------------------------------------------------------");
    Log.i(TAG, " => confirmaFoto()");
    Log.i(TAG, "---------------------------------------------------------");

    CameraInfo cameraInfo = new CameraInfo();

    // obt�m informa��es sobre a c�mera
    Camera.getCameraInfo(cameraId, cameraInfo);

    // The direction that the camera faces
    Log.i(TAG, "confirmaFoto() - cameraInfo.facing=" + cameraInfo.facing);

    // The orientation of the camera image. 
    Log.i(TAG, "confirmaFoto() - cameraInfo.orientation=" + cameraInfo.orientation);

    if (mImageBitmap == null) {
      Log.w(TAG, "confirmaFoto() - mImageBitmap � nulo");
    }

    // exibe informa��es sobre a foto
    ManipulaImagem.showBitmapInfo2(mImageBitmap);


    Log.i(TAG, "confirmaFoto() - mUri: [" + mUri + "]");

    Log.d(TAG, "confirmaFoto() - retorno");

    // Finaliza activity com sucesso
    finalizaActivityResultOk();

  }

  /**
   * Finaliza a activity com resultado Ok.
   */
  private void finalizaActivityResultOk() {

    Log.d(TAG, "finalizaActivityResultOk()");

    // cria uma intent de resposta
    Intent intent = new Intent();

    // Set the data this intent is operating on.
    intent.setData(mUri);

    // estabelece o resultado da execu��o da activity
    setResult(RESULT_OK, intent);

    finaliza();

  }

  /**
   * Finaliza a activity com um cancelamento.
   */
  private void finalizaActivityResultCanceled() {

    Log.d(TAG, "finalizaActivityResultCanceled()");

    Intent intent = new Intent();

    //intent.setData(null);

    // estabelece o resultado da execu��o da activity
    setResult(RESULT_CANCELED, intent);

    finaliza();

  }

  /**
   * Executa o fechamento da activity retornando o controle o m�todo onActivityResult().
   * 
   */
  private void finaliza() {

    Log.d(TAG, "finaliza()");

    // finaliza a activity
    finish();
  }

}

/**
 * 
 * @author maurocl
 * 
 */
class XXX implements Camera.AutoFocusCallback {

  private static final String TAG = "XXX";

  public void onAutoFocus(boolean success, Camera camera) {

    // TODO Auto-generated method stub
    Log.d(TAG, "XXX.onAutoFocurs() - sucess: " + success + ", camera: " + camera);
  }

}