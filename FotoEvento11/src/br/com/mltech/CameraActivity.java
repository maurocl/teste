package br.com.mltech;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import br.com.mltech.utils.FileUtils;

/**
 * CameraActivity
 * 
 * @author maurocl
 * 
 *         !!! Essa versão funciona no telefone porém ela trava no simulador !!!
 * 
 */
public class CameraActivity extends Activity {

  private static final String TAG = "CameraActivity";

  private static Camera mCamera;

  private static CameraPreview mPreview;

  private static File imageFile;

  private static File picsDir;

  private static int flag = 0;

  private FrameLayout layoutPreview;

  /**
   * onCreate(Bundle savedInstanceState)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    Log.d(TAG, "*** onCreate() ***");

    setContentView(R.layout.cameraprev);

    layoutPreview = (FrameLayout) findViewById(R.id.camera_preview);

    preparaDiretorioGravarFotos();

    /**
     * Trata o evento de disparar o botão
     */
    Button capture = (Button) findViewById(R.id.button_capture);

    capture.setOnClickListener(new OnClickListener() {

      public void onClick(View view) {

        if (mCamera == null) {
          Log.e(TAG, "mCamera is null na hora de acionar o dispatador !!!");
        }

        // botão de captura foi pressionado
        Log.i(TAG, "botão de captura foi pressionado");

        mCamera.takePicture(shutter, null, jpeg);

      }
    });

  }

  /**
   * 
   */
  final ShutterCallback shutter = new ShutterCallback() {

    public void onShutter() {

      Log.d(TAG, "onShutter() callback disparado !!!");
    }
  };

  /**
   * 
   */
  final PictureCallback raw = new PictureCallback() {

    public void onPictureTaken(byte[] data, Camera camera) {

      Log.d(TAG, "onPictureTaken - raw");
    }
  };

  /**
   * 
   */
  final PictureCallback jpeg = new PictureCallback() {

    /**
     * Método chamado quando houver uma foto JPEG disponível
     */
    public void onPictureTaken(byte[] data, Camera camera) {

      Log.d(TAG, "onPictureTaken() - jpeg");

      if (data != null) {
        Log.d(TAG, "data: " + data.length + " bytes");
      } else {
        Log.d(TAG, "data está vazio");
      }

      // TODO aqui deveremos criar o nome dos arquivos

      String nomeArquivo = System.currentTimeMillis() + ".jpg";

      boolean b = gravaArquivo(data, nomeArquivo);

      // foto já foi tirada e gravada.
      // agora é ...

      reiniciaCamera();

      // -------------------------------------------------------------
      // Intent de retorno
      // -------------------------------------------------------------
      Intent intent = new Intent();

      // it.putExtra("br.com.mltech.usuarioValidado", "OK");
      // Log.d(TAG, "Usuário validado");

      if (b) {
        intent.putExtra("br.com.mltech.dados", data);

        intent.putExtra("br.com.mltech.image.filename", imageFile.getName());

        setResult(RESULT_OK, intent);

      } else {

        setResult(RESULT_CANCELED, intent);

      }

      // finaliza a activity
      finish();

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
      mPreview = new CameraPreview(this, mCamera);

      layoutPreview.addView(mPreview);

    }

  }

  /**
   * onResume(3)
   * 
   * Esse callback é chamado a partir da sequencia: onCreate() --> onStart() -->
   * onResume() ou após ( a aplicação estar no estado Pause e retorna a
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

      // camera é diferente de null

      Log.d(TAG, "onResume - flag=" + flag);

      if (flag == 0) {

        flag = 1;

      } else {

        Log.d(TAG, "onResume - antes da chamada do método mCamera.startPreview()");

        mCamera.stopPreview();

        // Starts capturing and drawing preview frames to the screen
        mCamera.startPreview();

        Log.d(TAG, "onResume - após chamada do método mCamera.startPreview()");

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

    boolean b = false;

    super.onPause();

    Log.d(TAG, "*** onPause() ***");

    b = releaseCamera();

    if (b) {
      // a câmera foi liberada com sucesso
    } else {
      // a câmera já estava desligada
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
   * É executado após um onStop()
   */
  @Override
  protected void onRestart() {

    
    super.onRestart();
    Log.d(TAG, "*** onRestart() ***");
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
      mCamera.release();
      Log.d(TAG, "onDestroy() - after release");
    } else {
      Log.d(TAG, "mCamera is null");
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
   * Obtem uma instância de uma câmera.
   * 
   * The Camera class is used to set image capture settings, start/stop preview,
   * snap pictures, and retrieve frames for encoding for video.
   * 
   * This class is a client for the Camera service, which manages the actual
   * camera hardware.
   * 
   * 
   * @return Uma instância da câmera ou null caso ela não exista
   */
  public static Camera getCameraInstance() {

    Camera c = null;

    try {

      c = Camera.open();

    } catch (RuntimeException e) {
      Log.w(TAG, "getCameraInstance() - RuntimeException error na obtenção de uma instância da câmera", e);

    } catch (Exception e) {
      // A câmera não existe ou não se encontra disponível
      Log.w(TAG, "getCameraInstance() - Erro na obtenção de uma instância da câmera", e);
    }

    if (c == null) {
      Log.w(TAG, "getCameraInstance() - CAMERA IS NULL");
    }

    return c; // retorna null caso a camera não exista

  }

  /**
   * preparaDirerorioFotos()
   * 
   * Prepara o diretório onde as fotos serão armazenadas
   */
  private void preparaDiretorioGravarFotos() {

    boolean b = false;

    picsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/fotos/");

    Log.d(TAG, "preparaDiretorioGravarFotos() - picDir.absolutePath=" + picsDir.getAbsolutePath());
    Log.d(TAG, "preparaDiretorioGravarFotos() - picDir.name=" + picsDir.getName());

    // Make sure the Pictures directory exists.
    b = picsDir.mkdirs();

    if (b) {
      
      showFile(picsDir);

    } else {
      Log.w(TAG, "preparaDiretorioGravarFotos() - Não foi possivel criar o diretório: " + picsDir.getName());

      if (picsDir.exists()) {
        Log.w(TAG, "preparaDiretorioGravarFotos() - Diretório " + picsDir.getName() + " já existe !");
      }
    }

  }

  /**
   * 
   * @param f
   */
  private void showFile(File f) {

    Log.d(TAG, "showFile(): ");
    
    Log.d(TAG, "  f.getAbsolutePath=" + imageFile.getAbsolutePath());
    Log.d(TAG, "  f.getName()=" + imageFile.getName());

    String s1 = f.canRead() == true ? "R" : "-";
    String s2 = f.canWrite() == true ? "W" : "-";
    String s3 = f.canRead() == true ? "X" : "-";

    String permisao = s1 + s2 + s3;
    Log.d(TAG, "  Permissão do arquivo: " + permisao);

    Log.d(TAG, "  picsDir.canWrite(): " + picsDir.canWrite());
    Log.d(TAG, "  picsDir.canRead(): " + picsDir.canRead());
    Log.d(TAG, "  picsDir.canExecute(): " + picsDir.canExecute());

    Log.d(TAG, "  picsDir.getAbsolutePath()=" + picsDir.getAbsolutePath());

  };

  /**
   * reiniciaCamera()
   */
  private void reiniciaCamera() {

    SurfaceHolder sh = null;

    // aqui é necessário iniciar a câmera novamente
    Log.d(TAG, "==> reiniciaCamera()");

    if (mPreview != null) {
      sh = mPreview.getHolder();
    } else {
      Log.w(TAG, "reiniciaCamera() - mPreview está nulo");
    }

    try {
      mCamera.setPreviewDisplay(sh);
      mCamera.startPreview();
    } catch (IOException e) {
      Log.w(TAG, "reiniciaCamera() - ", e);
    }

  }

  /**
   * releaseCamera()
   * 
   * Libera a câmera para ser usada em outras aplicações
   * 
   */
  private boolean releaseCamera() {

    boolean b = false;

    if (mCamera != null) {

      mCamera.release();
      mCamera = null;

      Log.d(TAG, "releaseCamera() executado com sucesso");

      b = true;

    } else {

      // câmera não estava ligada
      Log.d(TAG, "releaseCamera() - mCamera is NULL");

    }

    return b;

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
   */
  private boolean gravaArquivo(byte[] data, String nomeArquivo) {

    /*********************************************************************************************
     * Quando o telefone está conectado e em testes ele não permite montar o
     * cartão de mem´roia
     ********************************************************************************************/

    boolean b = false;

    FileOutputStream fos = null;

    try {

      try {

        Log.d(TAG, "**** getExternalStorageState()=" + Environment.getExternalStorageState());

        // if (Environment.getExternalStorageState() ==
        // Environment.MEDIA_MOUNTED) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

          if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {

            Log.d(TAG, "MEDIA montada porém está ReadOnly");
          } else {
            Log.d(TAG, "MEDIA montada RW");
          }

          // cria uma arquivo para armazernar a foto
          imageFile = new File(picsDir, nomeArquivo);

          FileUtils.showFile(imageFile);

          fos = new FileOutputStream(imageFile);
          fos.write(data);

          Log.d(TAG, "Arquivo: " + imageFile.getName() + " foi gerado e ocupa " + imageFile.length() + " bytes");

          b = true;

        } else {
          Log.w(TAG, "MEDIA não está montada");
        }

      } catch (FileNotFoundException e) {

        Log.d(TAG, "File not found exception: " + imageFile.getName());

      } catch (IOException e) {

        Log.d(TAG, "IOException: " + imageFile.getName());

      } catch (Exception e) {

        Log.d(TAG, "Falha na gravação do arquivo: " + imageFile.getName());

      } finally {

        if (fos != null) {
          fos.close();
        }

      }

    } catch (IOException e) {
      Log.d(TAG, "IOException", e);
    }

    return b;

  }

}
