package br.com.mltech;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

  private static final String TAG = "CameraPreview";

  // � inicializado no construtor e testado quando da mudan�a do preview
  private SurfaceHolder mHolder;

  // inst�ncia de um objeto Camera
  private Camera mCamera;

  /**
   * 
   * @param context
   * @param camera
   */
  public CameraPreview(Context context, Camera camera) {

    super(context);

    mCamera = camera;

    mHolder = this.getHolder();

    mHolder.addCallback(this);

    //mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    Log.d(TAG, "CameraPreview() - inicializada");

  }

  public void surfaceCreated(SurfaceHolder holder) {

    Log.d(TAG, "*** surfaceCreated() ***");

    try {

      mCamera.setPreviewDisplay(holder);

    } catch (IOException e) {

      Log.w(TAG, "surfaceCreated() - IOException - Error setting camera preview: " + e.getMessage(), e);

    } catch (Exception e) {

      Log.w(TAG, "surfaceCreated() - Exception - Error setting camera preview: ", e);

    }

  }

  public void surfaceDestroyed(SurfaceHolder holder) {

    // empty. Take care of releasing the camera preview in your activity
    // m�todo vazio. Tome cuidado em liberar a preview da c�mera em sua activity

    Log.d(TAG, "*** surfaceDestroyed() ***");

  }

  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    // TODO a grande d�vida aqui � saber se uso a vari�vel holder ou mHolder

    Log.w(TAG, "*** surfaceChanged() *** - in�cio");
    //Log.w(TAG, "  surfaceChanged() - holder=" + holder + ", mHolder=" + mHolder);

    //showSurfaceHolder(holder, "holder");
    //showSurfaceHolder(mHolder, "mHolder");

    if (mHolder.getSurface() == null) {

      // preview surface does not exist
      // superf�cie de pre-visualiza��o n�o existe
      Log.d(TAG, "  surfaceChanged() - getSurface() �  nula");
      return;

    } else {

      // PixelFormat 4 = RGB_565
      Log.d(TAG, "  surfaceChanged() - format: " + format + ", w=" + width + ", h=" + height);

    }

    Log.w(TAG, "*** surfaceChanged() *** - fim");

  }

  /**
   * Exibe informa��es sobre SurfaceHolder.
   * 
   * @param sh
   *          uma inst�ncia de SurfaceHolder
   * 
   * @param msg
   *          mensagem exibida
   * 
   */
  void showSurfaceHolder(SurfaceHolder sh, String msg) {

    if (sh == null) {
      return;
    }

    Log.d(TAG, "showSurfaceHolder() - msg:" + msg);

    Log.d(TAG, "showSurfaceHolder() - sh.toString(): " + sh.toString());
    Log.d(TAG, "showSurfaceHolder() - sh.isCreating(): " + sh.isCreating());
    Log.d(TAG, "showSurfaceHolder() - sh.getSurfaceFrame(): " + sh.getSurfaceFrame());

  }

}
