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

  // Vari�vel usada para desenhar a foto na tela
  private FrameLayout layoutPreview;

  // bot�es da m�quina
  private static Button btnOk;

  private static Button btnNovo;

  private static Button btnCancelar;

  private static Button btnCapture;

  // handler usado para controle do disparo autom�tico
  private Handler handler = new TestHandler();

  // controla o tipo de disparo da c�mera
  private static final int MANUAL = 1;

  private static final int AUTOMATICO = 2;

  // controla o tipo de disparo da c�mera
  private static int tipoDisparo = AUTOMATICO;

  /**
   * Bitmap
   */
  private static Bitmap mImageBitmap;

  /**
   * Uri onde est� localizada a foto
   */
  private static Uri mUri;

  private static Participacao mParticipacao;

  /**
   * identificador da c�mera do dispositivo
   */
  private static int cameraId = 0;

  /**
   * onCreate(Bundle savedInstanceState)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    Log.d(TAG, "*** onCreate() ***");

    setContentView(R.layout.cameraprev);

    // obtem informa��es da intent chamadora
    Intent intent = getIntent();

    if (intent.getSerializableExtra(Constantes.PARTICIPACAO) != null) {

      mParticipacao = (Participacao) intent.getSerializableExtra(Constantes.PARTICIPACAO);

    }

    // obt�m o identificador da c�mera
    cameraId = (intent.getIntExtra("br.com.mltech.cameraId", 0));
    Log.d(TAG, "onCreate() - atualizando o dientificador da c�mera - cameraId = " + cameraId);

    int numCamerasDisponiveis = Camera.getNumberOfCameras();

    Log.d(TAG, "onCreate() - N� de c�meras do dispositivo:  " + Camera.getNumberOfCameras());

    boolean isCameraAvailable = CameraTools.checkCameraHardware(this);

    if (isCameraAvailable) {

      if (numCamerasDisponiveis == 1) {
        Log.d(TAG, "onCreate() - h� uma c�mera dispon�vel.");
      }
      else {
        Log.d(TAG, "onCreate() - h�  " + Camera.getNumberOfCameras() + " c�meras dispon�veis.");
      }

    } else {

      Log.e(TAG, "onCreate() - n�o h� c�meras dispon�veis");
      return;

    }

    if (!CameraTools.isCameraWorking(cameraId)) {
      Log.e(TAG, "onCreate() - c�mera: " + cameraId + " n�o est� funcionando");
      return;
    }

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
     * Bot�o Ok
     * 
     * Confirma a foto.
     * 
     * e sai da activity
     * 
     */
    btnOk.setOnClickListener(new OnClickListener() {

      public void onClick(View view) {

        Log.d(TAG, "onClick(btnOk) - bot�o Ok");
        Log.d(TAG, "onClick(btnOk) - Confirma a foto");

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

        // seta os dados de retorno (data)
        // Uri contendo a localiza��o da foto
        //intent.putExtra("data", mUri);

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

        Intent intent = new Intent();

        // estabelece o resultado da execu��o da activity
        setResult(RESULT_CANCELED, intent);

        // finaliza a activity
        finish();

      }
    });

  }

  /**
   * shutter
   * 
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
   * raw
   * 
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
   * jpeg
   * 
   * � uma inst�ncia da classe PictureCallback.
   * 
   * Define um objeto que cont�m uma inst�ncia da classe PictureCallback
   * respons�vel pelo tratamento do evento do recebimento da imagem JPEG da
   * c�mera. Invocado assim que a imagem com compress�o (comprimida), no formato
   * JPEG, est� dispon�vel.
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

      // String nomeArquivo = System.currentTimeMillis() + ".jpg";
      // File f = FileUtils.obtemNomeArquivoJPEG();
      File f = FileUtils.obtemNomeArquivo(".jpg");

      String nomeArquivo = f.getAbsolutePath();

      if (data != null) {

        // cria o bitmap a partir do array de bytes capturados
        mImageBitmap = ManipulaImagem.getBitmapFromByteArray(data);
        
        if(mImageBitmap==null) {
          Log.w(TAG,"mImageBitmap � nulo");
        }

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
   * Esse callback � chamado a partir da sequencia: onCreate() --> onStart() -->
   * onResume() ou ap�s ( a aplica��o estar no estado Pause e retorna a
   * funcionar) onPause() --> on Resume().
   * 
   */
  @Override
  protected void onResume() {

    super.onResume();

    Log.d(TAG, "*** onResume() ***");

    if (mCamera == null) {

      // obt�m uma inst�ncia da c�mera
      mCamera = CameraTools.getCameraInstance(cameraId);

    }

    //--------------------------------------------------------
    // configura os par�metros usados para configurar a c�mera
    //--------------------------------------------------------
    configuraParamCamera();

    // cria uma inst�ncia da "tela" de preview
    mPreview = new CameraPreview(this, mCamera);

    // TODO verificar o layout da c�mera
    layoutPreview.addView(mPreview);

    // TODO verificar o disparo autom�tico da c�mera      
    if (tipoDisparo == AUTOMATICO) {
      setDisparoAutomatico(3);
    }
    else if (tipoDisparo == MANUAL) {
      // n�o faz nada
    }

    if (mCamera == null) {

      Log.w(TAG, "onResume - mCamera is null");

    } else {

      // camera � diferente de null

      Log.d(TAG, "onResume() - flag=" + flag);

      if (flag == 0) {

        flag = 1;

      } else {

        Log.d(TAG, "onResume() - antes da chamada do m�todo mCamera.startPreview()");

        // Stops capturing and drawing preview frames to the surface, and resets
        // the camera for a future call to startPreview().
        mCamera.stopPreview();

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
   * Aplica��o foi reinicializada.
   * 
   * � executado ap�s um onStop()
   * 
   */
  @Override
  protected void onRestart() {

    super.onRestart();

    Log.d(TAG, "*******************");
    Log.d(TAG, "*** onRestart() ***");
    Log.d(TAG, "*******************");

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
   * showCameraParameters
   * 
   * Exibe algumas configura��es dos par�metros da c�mera
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
      Log.w(TAG, "showCameraParameters() - Par�metros de configura��o da c�mera est� nulo");
      return;
    }

    // obt�m o tamanho da imagem
    Size size = parameters.getPictureSize();

    Log.v(TAG, "showCameraParameters() - getPictureSize: " + CameraTools.getCameraSize(size));
    Log.v(TAG, "showCameraParameters() - getHorizontalViewAngle: " + parameters.getHorizontalViewAngle());
    Log.v(TAG, "showCameraParameters() - getJpegThumbnailQuality: " + parameters.getJpegThumbnailQuality());

  }

  /**
   * reiniciaCamera()
   * 
   * Reinicia a c�mera (o preview da c�mera)
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
   * releaseCamera()
   * 
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
   * gravaArquivo(byte[] data, String nomeArquivo)
   * 
   * Grava um array de bytes (contendo a foto) em um arquivo em disco
   * 
   * @param data
   *          Array de bytes
   * 
   * @param nomeArquivo
   *          Nome do arquivo
   * 
   * @return true se o arquivo gravado com sucesso ou false caso ocorra algum
   *         erro.
   * 
   */
  private boolean gravaArquivo(byte[] data, String nomeArquivo) {

    String externalStorageState = Environment.getExternalStorageState();

    if (!externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
      // m�dia n�o est� montada
      Log.w(TAG, "gravaArquivo() - m�dia n�o est� montada");
      return false;
    }

    if (externalStorageState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
      // m�dia montada apenas para leitura
      Log.w(TAG, "gravaArquivo() - m�dia montada por�m est� ReadOnly");
      return false;
    }

    // exibe o estado do dispositivo de externo de armazenamento
    Log.d(TAG, "gravaArquivo() - getExternalStorageState()=" + Environment.getExternalStorageState());

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

        String msg = "gravaArquivo() - Arquivo: " + f.getAbsolutePath() + " foi gerado e ocupa "
            + fmtFloat((float) (f.length() / 1024.0)) + " KBytes";

        Log.d(TAG, msg);

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

    return gravou;

  }

  /**
   * fmtFloat(String args)
   * 
   * Formata um valor float para 2 casas decimais
   * 
   * @param args
   *          valor float para formatar
   * 
   * @return uma string contendo um float formatado com duas casas decimais
   * 
   */
  private static String fmtFloat(float args) {

    java.util.Formatter fmt = new java.util.Formatter();

    String formattedString = fmt.format("%.2f", args).toString();

    return formattedString;

  }

  /**
   * setDisparoAutomatico(int segundos)
   * 
   * Dispara automaticamente a c�mera ap�s o n�mero de segundos fornecidos
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
      Log.e(TAG, "setDisparoAutomatico() - camera n�o est� nula");
      return;
    }

    Toast.makeText(getBaseContext(), "disparo autom�tico em " + (segundos * 1000) + " segundos.", Toast.LENGTH_SHORT).show();

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

        Toast.makeText(getBaseContext(), "mensagem", Toast.LENGTH_SHORT).show();

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
   * configuraParamCamera()
   */
  private void configuraParamCamera() {

    mCamera.setDisplayOrientation(0);
    
    CameraTools.setCameraDisplayOrientation(this, cameraId, mCamera);
    

    // 
    Camera.Parameters params = mCamera.getParameters();

    // altera o tamanho da foto
    params.setPictureSize(640, 480);

    /*
     * EFFECT_AQUA EFFECT_BLACKBOARD EFFECT_MONO EFFECT_NEGATIVE EFFECT_NONE
     * EFFECT_POSTERIZE EFFECT_SEPIA EFFECT_SOLARIZE EFFECT_WHITEBOARD
     */

    int efeitoFoto = mParticipacao.getEfeitoFoto();

    if (efeitoFoto == CORES) {
      params.setColorEffect(Camera.Parameters.EFFECT_NONE);
    }
    else if (efeitoFoto == PB) {
      params.setColorEffect(Camera.Parameters.EFFECT_MONO);
    }
    else {
      // o valor default � nenhum efeito
      params.setColorEffect(Camera.Parameters.EFFECT_NONE);
    }

    //params.setColorEffect(Camera.Parameters.EFFECT_SEPIA);

    // Atualiza os parametros
    mCamera.setParameters(params);

    // Exibe alguns parametros da camera
    showCameraParameters(mCamera);
  }

  /**
   * confirmaFoto()
   */
  private void confirmaFoto() {
    Log.i(TAG,"---------------------------------------------------------");
    Log.i(TAG," => confirmaFoto()");
    Log.i(TAG,"---------------------------------------------------------");
    
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

    // seta os dados de retorno (data)
    // Uri contendo a localiza��o da foto
    //intent.putExtra("data", mUri);

    Log.i(TAG, "confirmaFoto() - mUri: [" + mUri + "]");

    // cria uma intent de resposta
    Intent intent = new Intent();

    // Set the data this intent is operating on. 
    // This method automatically clears any type that was previously set by setType(String). 
    intent.setData(mUri);

    // estabelece o resultado da execu��o da activity
    setResult(RESULT_OK, intent);

    Log.d(TAG, "confirmaFoto() -  - retorno");

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