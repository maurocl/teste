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

	// � inicializado no construtor e testado quando da mudan�a do preview
	private static SurfaceHolder mHolder;

	//
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
	 *          Inst�ncia da Camera
	 * 
	 */
	public CameraPreview(Context context, Camera camera) {

		super(context);

		// atualiza a c�mera corrente
		mCamera = camera;

		// obtem acesso a surface (superf�cie)
		mHolder = this.getHolder();

		// adiciona o tratator de eventos
		mHolder.addCallback(this);

		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		Log.d(TAG, "CameraPreview() - inicializada");

	}

	/**
	 * surfaceCreated(SurfaceHolder holder)
	 * 
	 * Esse m�todo � chamado imediatamente ap�s a primeira cria��o da superf�cie
	 * 
	 * @param holder
	 *          o SurfaceHolder cuja superf�cie (surface) est� sendo criada
	 * 
	 */
	public void surfaceCreated(SurfaceHolder holder) {

		Log.d(TAG, "surfaceCreated() ...");

		try {

			// estabelece o display para visualiza��o do preview da c�mera
			// containing the Surface on which to place the preview, or null to remove
			// the preview surface
			mCamera.setPreviewDisplay(holder);

			// inicia o preview
			mCamera.startPreview();

		} catch (IOException e) {
			// if the method fails (for example, if the surface is unavailable or
			// unsuitable).
			Log.w(TAG, "surfaceCreated() - IOException - Error setting camera preview: " + e.getMessage());

		} catch (Exception e) {

			Log.w(TAG, "surfaceCreated() - Exception - Error setting camera preview: ", e);

		}

	}

	/**
	 * surfaceDestroyed(SurfaceHolder holder)
	 * 
	 * Chamado imediatamente antes da superficie estar sendo destru�da. Ap�s essa
	 * chamada voc� n�o dever� mais tentar acesso a superf�cie
	 * 
	 * @param holder
	 *          o SurfaceHolder cuja superf�cie (surface) est� sendo destru�da
	 * 
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {

		// empty. Take care of releasing the camera preview in your activity
		// m�todo vazio. Tome cuidado em liberar a preview da c�mera em sua activity

		Log.d(TAG, "surfaceDestroyed()");

	}

	/**
	 * surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	 * 
	 * Chamado ap�s alguma mudan�a estrutural (formato ou tamanho) feito na
	 * surface. Esse m�todo � chamado pelo menos uma vez depois do SurfaceCreated.
	 * 
	 * @param holder
	 *          superf�cie de exibi��o
	 * 
	 * @param format
	 *          o novo PixelFormat da superf�cie (surface) - veja classe
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
			Log.d(TAG, "surfaceChanged() - getSurface() is null");
			return;

		} else {

			// PixelFormat 4 = RGB_565
			Log.d(TAG, "surfaceChanged() - format: " + format + ", w=" + width + ", h=" + height);

		}

	}

}
