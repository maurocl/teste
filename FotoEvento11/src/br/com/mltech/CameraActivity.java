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
import br.com.mltech.utils.FileUtils;
import br.com.mltech.utils.ManipulaImagem;
import br.com.mltech.utils.camera.CameraTools;

/**
 * CameraActivity
 * 
 * @author maurocl
 *  
 */
public class CameraActivity extends Activity {

  private static final String TAG = "CameraActivity";

  // inst�ncia da c�mera
  private static Camera mCamera;

  // inst�ncia da CameraPreview
  private static CameraPreview mPreview;

  // diret�rio (onde as fotos ser�o armazenadas)
  private static File picsDir;

  // flag usado para controle da aplica��o
  private static int flag = 0;

  // Vari�vel usada para desenhar a foto na tela
  private FrameLayout layoutPreview;

  private static Button btnOk;

  private static Button btnNovo;

  private static Button btnCancelar;

  private static Button btnCapture;
  
  private Handler handler = new TestHandler();

  // controla o tipo de disparo da c�mera
  private static final int MANUAL = 1;
  private static final int AUTOMATICO = 2;

  
  private static int tipoDisparo = AUTOMATICO;
  
  
  /**
	 * 
	 */
  private Bitmap mImageBitmap;

  /**
   * Uri onde est� localizada a foto
   */
  private Uri mUri;

  /**
   * onCreate(Bundle savedInstanceState)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    Log.d(TAG, "*** onCreate() ***");

    setContentView(R.layout.cameraprev);

    int numCamerasDisponiveis = Camera.getNumberOfCameras();

    boolean isCameraAvailable = CameraTools.checkCameraHardware(this);

    if (isCameraAvailable) {

      if (numCamerasDisponiveis == 1) {
        Log.d(TAG, "onCreate() - h� uma c�mera dispon�vel.");
      }
      else {
        Log.d(TAG, "onCreate() - h�  " + numCamerasDisponiveis + " c�meras dispon�veis.");
      }
    } else {
      Log.e(TAG, "onCreate() - n�o h� c�meras dispon�veis");
      return;
    }

    for (int i = 0; i < Camera.getNumberOfCameras() - 1; i++) {

      Log.i(TAG, "onCreate() - verificando o estado da c�mera: " + i);

      boolean isCameraWorking = CameraTools.isCameraWorking(i);

      if (isCameraWorking) {
        Log.d(TAG, "onCreate() - c�mera " + i + " est� funcionando corretamente");
      } else {
        Log.e(TAG, "onCreate() - c�mera " + i + " n�o est� dispon�vel para uso pela aplica��o");
        return;
      }

    }

    // prepara o diret�rio para guardar as fotos
    preparaDiretorioGravarFotos();

    layoutPreview = (FrameLayout) findViewById(R.id.camera_preview);

    /**
     * Configura o tratador de eventos dos bot�es
     */

    btnCapture = (Button) findViewById(R.id.button_capture);
    btnOk = (Button) findViewById(R.id.btnOk);
    btnNovo = (Button) findViewById(R.id.btnNovo);
    btnCancelar = (Button) findViewById(R.id.btnCancelar);

    btnCapture.setVisibility(android.view.View.VISIBLE);
    btnOk.setVisibility(android.view.View.GONE);
    btnNovo.setVisibility(android.view.View.GONE);
    btnCancelar.setVisibility(android.view.View.GONE);

    /**
     * Bot�o Capturar
     */
    btnCapture.setOnClickListener(new OnClickListener() {

      public void onClick(View view) {

        if (mCamera == null) {
          Log.e(TAG, "bot�o Capturar - mCamera is null na hora de acionar o dispatador !!!");
          return;
        }

        // bot�o de captura foi pressionado
        Log.i(TAG, "---------------------------------");
        Log.i(TAG, "bot�o de captura foi pressionado");
        Log.i(TAG, "---------------------------------");

        mCamera.takePicture(shutter, null, jpeg);

      }
    });

    /**
     * Bot�o Nova foto
     */
    btnNovo.setOnClickListener(new OnClickListener() {

      public void onClick(View view) {

        Log.d(TAG, "bot�o nova foto");

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
     */
    btnOk.setOnClickListener(new OnClickListener() {

      public void onClick(View view) {

        Log.d(TAG, "bot�o Ok");

        CameraInfo cameraInfo = new CameraInfo();
        //CameraTools.exibeCameraInfo(0, cameraInfo);

        Camera.getCameraInfo(0, cameraInfo);

        // The direction that the camera faces
        Log.i(TAG, "exibeCameraInfo() - cameraInfo.facing=" + cameraInfo.facing);

        // The orientation of the camera image. 
        Log.i(TAG, "exibeCameraInfo() - cameraInfo.orientation=" + cameraInfo.orientation);

        Intent intent = new Intent();

        if (mImageBitmap == null) {
          Log.w(TAG, "mImageBitmap � nulo");
        }

        // exibe informa��es sobre a foto
        ManipulaImagem.showBitmapInfo2(mImageBitmap);

        // seta os dados de retorno (data)
        // Uri contendo a localiza��o da foto
        intent.putExtra("data", mUri);

        // estabelece o resultado da execu��o da activity
        setResult(RESULT_OK, intent);

        Log.d(TAG, "btnOk - retorno");

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
        Log.d(TAG, "bot�o cancelar");

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
   * Define um objeto que cont�m uma inst�ncia da classe PictureCallback
   * respons�vel pelo tratamento do evento do recebimento da imagem JPEG da
   * c�mera Invocado assim que a imagem comprimida, no formato JPEG, est�
   * dispon�vel.
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

      java.util.Formatter fmt = new java.util.Formatter();

      if (data != null) {
        Log.d(TAG, "onPictureTaken() - data: " + fmt.format("%.2f", data.length / 1024.0) + " KBytes");
      } else {
        Log.d(TAG, "onPictureTaken() - data n�o retornou nenhum dado");
      }

      // TODO aqui deveremos criar o nome dos arquivos

      // String nomeArquivo = System.currentTimeMillis() + ".jpg";
      // File f = FileUtils.obtemNomeArquivoJPEG();
      File f = FileUtils.obtemNomeArquivo(".jpg");

      String nomeArquivo = f.getAbsolutePath();

      if (data != null) {

        mImageBitmap = ManipulaImagem.getBitmapFromByteArray(data);

        boolean gravou = gravaArquivo(data, nomeArquivo);

        if (gravou) {
          Log.d(TAG, "onPictureTaken() - arquivo " + nomeArquivo + " gravado com sucesso.");
          Toast.makeText(getBaseContext(), "Arquivo " + nomeArquivo + " gravado com sucesso.", Toast.LENGTH_SHORT).show();
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

      btnOk.setVisibility(android.view.View.VISIBLE);
      btnNovo.setVisibility(android.view.View.VISIBLE);
      btnCancelar.setVisibility(android.view.View.VISIBLE);

    }

  };

  /**
   * onStart(2)
   */
  @Override
  protected void onStart() {

    super.onStart();

    Log.d(TAG, "*** onStart() ***");

    if (mCamera == null) {

      mCamera = getCameraInstance();

    }

    if (mCamera != null) {

      // configura a c�mera
      configuraCamera();

      mPreview = new CameraPreview(this, mCamera);

      // TODO verificar o layout da c�mera
      layoutPreview.addView(mPreview);
      
      
      // TODO verificar o disparo autom�tico da c�mera      
      if(tipoDisparo==AUTOMATICO) {
        setDisparoAutomatico(3);
      }
      else if(tipoDisparo==MANUAL) {
        // n�o faz nada
      }

    }

  }

  /**
   * configuraCamera()
   * 
   */
  private void configuraCamera() {

    mCamera.setDisplayOrientation(0);

    // l� os par�metros
    Camera.Parameters params = mCamera.getParameters();

    // altera o tamanho da foto
    params.setPictureSize(640, 480);

    /*
     * EFFECT_AQUA EFFECT_BLACKBOARD EFFECT_MONO EFFECT_NEGATIVE EFFECT_NONE
     * EFFECT_POSTERIZE EFFECT_SEPIA EFFECT_SOLARIZE EFFECT_WHITEBOARD
     */

    //params.setColorEffect(Camera.Parameters.EFFECT_MONO);
    params.setColorEffect(Camera.Parameters.EFFECT_SEPIA);

    // Atualiza os par�metros
    mCamera.setParameters(params);

    // Exibe alguns par�metros da c�mera
    showCameraParameters(mCamera);
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

      Log.d(TAG, "onResume - mCamera is null");

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

      // Disconnects and releases the Camera object resources.
      mCamera.release();

      Log.d(TAG, "onDestroy() - after release");

    } else {
      Log.w(TAG, "onDestroy() - c�mera est� liberada");
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
   * getCameraInstance()
   * 
   * Obtem uma inst�ncia de uma c�mera.
   * 
   * The Camera class is used to set image capture settings, start/stop preview,
   * snap pictures, and retrieve frames for encoding for video.
   * 
   * This class is a client for the Camera service, which manages the actual
   * camera hardware.
   * 
   * 
   * @return Uma inst�ncia da c�mera ou null caso ela n�o exista
   */
  public static Camera getCameraInstance() {

    Camera c = null;

    try {

      // Tenta obter uma inst�ncia da c�mera
      c = Camera.open();

    } catch (RuntimeException e) {
      Log.e(TAG,
          "getCameraInstance() - RuntimeException - Houve uma erro em tempo de execu��o na obten��o de uma inst�ncia da c�mera", e);

    } catch (Exception e) {
      // A c�mera n�o existe ou n�o se encontra dispon�vel
      Log.e(TAG, "getCameraInstance() - Exception - Houve uma exce��o na obten��o de uma inst�ncia da c�mera", e);
    }

    return c; // retorna null caso a camera n�o exista ou n�o pode ser inst�nciada

  }

  /**
   * preparaDirerorioFotos()
   * 
   * Prepara o diret�rio onde as fotos ser�o armazenadas
   * 
   */
  private void preparaDiretorioGravarFotos() {

    boolean isDirCreated = false;

    // obt�m o local onde as fotos s�o armazenas na mem�ria externa do
    // dispositivo (geralmente o SD Card)
    picsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/fotoevento/");

    Log.d(TAG, "preparaDiretorioGravarFotos() - picDir.absolutePath=" + picsDir.getAbsolutePath());
    Log.d(TAG, "preparaDiretorioGravarFotos() - picDir.name=" + picsDir.getName());

    // Make sure the Pictures directory exists.
    // Garante a exist�ncia do diret�rio
    isDirCreated = picsDir.mkdirs();

    if (isDirCreated) {

      // diret�rio criado com sucesso
      showFile(picsDir);

    } else {

      if (picsDir.exists()) {
        Log.w(TAG, "preparaDiretorioGravarFotos() - N�o foi poss�vel criar o diret�rio: " + picsDir.getName()
            + " pois ele j� existe !");
      } else {
        //
        Log.w(TAG, "preparaDiretorioGravarFotos() - Erro - n�o foi possivel criar o diret�rio: " + picsDir.getName());
      }

    }

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

    Camera.Parameters parameters = c.getParameters();

    if (parameters == null) {
      Log.w(TAG, "showCameraParameters() - Par�metros de configura��o da c�mera est� nulo");
      return;
    }

    // obt�m o tamanho da imagem
    Size size = parameters.getPictureSize();

    Log.d(TAG, "  getPictureSize: " + CameraTools.getCameraSize(size));
    Log.d(TAG, "  getPictureSize: " + parameters.getPictureSize());
    Log.d(TAG, "  getHorizontalViewAngle: " + parameters.getHorizontalViewAngle());
    Log.d(TAG, "  getJpegThumbnailQuality: " + parameters.getJpegThumbnailQuality());

  }

  /**
   * showFile(File f)
   * 
   * Exibe informa��es sobre um arquivo: - informa��es sobre a permiss�o de
   * leitura (R), escrita (W) e execu��o (X) do um arquivo f - informa��es sobre
   * a permiss�o de leitura (R), escrita (W) e execu��o (X) do diret�rio picsDir
   * 
   * @param f
   *          inst�ncia da classe File
   * 
   */
  private void showFile(File f) {

    Log.d(TAG, "showFile(): ");

    if (f == null) {
      Log.w(TAG, "arquivo � nulo");
      return;
    }

    Log.d(TAG, "  f.getAbsolutePath=" + f.getAbsolutePath());
    Log.d(TAG, "  f.getName()=" + f.getName());

    String canRead = f.canRead() == true ? "R" : "-";
    String canWrite = f.canWrite() == true ? "W" : "-";
    String canExecute = f.canRead() == true ? "X" : "-";

    String permission = canRead + canWrite + canExecute;
    Log.d(TAG, "  Permiss�o do arquivo: " + permission);

    Log.d(TAG, "  picsDir.canWrite(): " + picsDir.canWrite());
    Log.d(TAG, "  picsDir.canRead(): " + picsDir.canRead());
    Log.d(TAG, "  picsDir.canExecute(): " + picsDir.canExecute());

    Log.d(TAG, "  picsDir.getAbsolutePath()=" + picsDir.getAbsolutePath());

  };

  /**
   * reiniciaCamera()
   * 
   * Reinicia a c�mera
   * 
   */
  private void reiniciaCamera() {

    SurfaceHolder surfaceHolder = null;

    // aqui � necess�rio iniciar a c�mera novamente
    Log.d(TAG, "reiniciaCamera()");

    if (mCamera != null) {

      // cria uma nova inst�ncia de CameraPreview
      mPreview = new CameraPreview(this, mCamera);

      // adiciona o "tela" de preview ao layout
      layoutPreview.addView(mPreview);

    } else {
      Log.w(TAG, "reiniciaCamera() - mCamera est� nula");
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

    boolean isCameraReleased = false;

    if (mCamera != null) {

      // libera a c�mera
      // Disconnects and releases the Camera object resources. 
      // You must call this as soon as you're done with the Camera object.
      mCamera.release();

      // atribui null para c�mera corrente
      mCamera = null;

      Log.d(TAG, "releaseCamera() - c�mera liberada com sucesso");

      isCameraReleased = true;

    } else {

      // c�mera n�o estava ligada
      Log.w(TAG, "releaseCamera() - c�mera n�o pode ser liberada pois n�o h� uma inst�ncia da Camera em uso.");

    }

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

    /*********************************************************************************************
     * Quando o telefone est� conectado e em testes ele n�o permite montar o
     * cart�o de mem�ria
     ********************************************************************************************/

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

    java.util.Formatter fmt = new java.util.Formatter();

    try {

      try {

        fos = new FileOutputStream(f);
        fos.write(data);

        Log.d(
            TAG,
            "gravaArquivo() - Arquivo: " + f.getAbsolutePath() + " foi gerado e ocupa "
                + fmt.format("%.2f", (f.length() / 1024.0) + " KBytes"));

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
   * finalizaActivity(Intent intent, boolean sucesso)
   * 
   * @param intent
   *          Intente que
   * @param sucesso
   * 
   */
  private void finalizaActivity(Intent intent, boolean sucesso) {

    Log.d(TAG, "finalizaActivity() - sucesso: " + sucesso);

    if (sucesso) {

      setResult(RESULT_OK, intent);

    } else {

      setResult(RESULT_CANCELED, intent);

    }

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
  private void setDisparoAutomatico(int segundos) {

    if (segundos < 0) {
      // TODO
      // lan�a uma exce��o ???
    }

    if (mCamera == null) {
      Log.e(TAG, "camera n�o est� nula");
      return;
    }

    
    Toast.makeText(getBaseContext(), "disparo autom�tico em "+(segundos*1000)+" segundos.", Toast.LENGTH_SHORT).show();
    
    Message msg = new Message();
    msg.what=1;
    // envia a mensagem
    handler.sendMessageDelayed(msg, segundos*1000);
    
    
    
    
    Log.i(TAG, "-------------------------------------");
    Log.i(TAG, "captura foi disparada automaticamente");
    Log.i(TAG, "-------------------------------------");

    // tira uma foto
   // mCamera.takePicture(shutter, null, jpeg);

  }

  /**
   * 
   * @author maurocl
   *
   */
  private class TestHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {

      // TODO Auto-generated method stub
      super.handleMessage(msg);
      // o atributo msg.what permite identificar a mensagem
      if(msg.what==1) {
     
        Toast.makeText(getBaseContext(), "mensagem", Toast.LENGTH_SHORT).show();
        
        // tira uma foto
        mCamera.takePicture(shutter, null, jpeg);

        
        
      }
    }

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