package br.com.mltech;

import java.io.IOException;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * CameraPreview
 * 
 * A basic Camera preview class
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
	 * Construtor
	 * 
	 * @param context
	 *          Interface to global information about an application environment.
	 * 
	 * @param camera
	 *          Instância da Camera
	 * 
	 */
	public CameraPreview(Context context, Camera camera) {

		super(context);

		// atualiza a câmera corrente
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
	 * Esse método é chamado imediatamente apos a primeira criação da superfície
	 * 
	 * @param holder
	 *          o SurfaceHolder cuja superfície (surface) está sendo criada
	 * 
	 */
	public void surfaceCreated(SurfaceHolder holder) {

		Log.d(TAG, "surfaceCreated() ...");

		try {

			mCamera.setPreviewDisplay(holder);

			mCamera.startPreview();

		} catch (IOException e) {

			Log.w(TAG, "surfaceCreated() - IOException - Error setting camera preview: " + e.getMessage());

		} catch (Exception e) {
			Log.w(TAG, "surfaceCreated() - Exception - Error setting camera preview: ", e);

		}

	}

	/**
	 * surfaceDestroyed(SurfaceHolder holder)
	 * 
	 * Chamado imediatamente antes da superficie estar sendo destruída. Após essa
	 * chamada você não deverá mais tentar acesso a superfície
	 * 
	 * @param holder
	 *          o SurfaceHolder cuja superfície (surface) está sendo destruída
	 * 
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {

		// empty. Take care of releasing the camera preview in your activity

		Log.d(TAG, "surfaceDestroyed()");

	}

	/**
	 * surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	 * 
	 * Chamado após alguma mudança estrutural (formato ou tamanho) feito na
	 * surface. Esse método é chamado pelo menos uma vez depois do SurfaceCreated.
	 * 
	 * @param holder
	 *          superfície de exibição
	 *          
	 * @param format
	 *          o novo PixelFormat da superfície (surface) - veja classe
	 *          PixelFormat
	 *          
	 * @param width
	 *          a nova largura da surface
	 *          
	 * @param height
	 *          altura a nova altura da surface
	 * 
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

		if (mHolder.getSurface() == null) {

			// preview surface does not exist
			Log.d(TAG, "surfaceChanged - getSurface() is null");
			return;

		} else {

			// PixelFormat 4 = RGB_565
			Log.d(TAG, "surfaceChanged() - format: " + format + ", w=" + width + ", h=" + height);
			
		}

	}

}
