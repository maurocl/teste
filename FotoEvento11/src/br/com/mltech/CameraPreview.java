package br.com.mltech;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * CameraPreview
 * 
 * @author maurocl
 * 
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

  private static final String TAG = "CameraPreview";

  private static SurfaceHolder mHolder;

  private static Camera mCamera;

  /**
   * CameraPreview(Context context, Camera camera)
   * 
   * @param context
   *          Interface to global information about an application environment.
   * @param camera
   * 
   */
  public CameraPreview(Context context, Camera camera) {

    super(context);

    mCamera = camera;

    // obtem acesso a surface (superfície)
    mHolder = this.getHolder();

    // adiciona o tratator de eventos
    mHolder.addCallback(this);

    mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    Log.d(TAG, "CameraPreview() - inicializada");

  }

  /**
   * surfaceCreated(SurfaceHolder holder)
   * 
   * @param holder
   * 
   */
  public void surfaceCreated(SurfaceHolder holder) {

    Log.d(TAG, "surfaceCreated() ...");

    try {

      mCamera.setPreviewDisplay(holder);

      mCamera.startPreview();

    } catch (IOException e) {

      Log.w(TAG, "surfaceCreated() - Error setting camera preview: " + e.getMessage());

    } catch (Exception e) {
      Log.w(TAG, "surfaceCreated() - Error setting camera preview: ", e);

    }

  }

  /**
   * surfaceDestroyed(SurfaceHolder holder)
   * 
   * @param holder
   * 
   */
  public void surfaceDestroyed(SurfaceHolder holder) {

    // empty. Take care of releasing the camera preview in your activity

    Log.d(TAG, "surfaceDestroyed()");

  }

  /**
   * surfaceChanged(SurfaceHolder holder, int format, int width, int height)
   * 
   * @param holder
   * @param format
   * @param width
   * @param height
   * 
   */
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    if (mHolder.getSurface() == null) {

      // preview surface does not exist
      Log.d(TAG, "surfaceChanged - getSurface() is null");
      return;

    }
    else {

      Log.d(TAG, "surfaceChanged() - ");

    }

  }

}
