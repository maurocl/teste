package br.com.mltech;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * CameraPreview
 * 
 * @author maurocl
 *
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = "CameraPreview";

	private SurfaceHolder mHolder;
	private Camera mCamera;

	/**
	 * CameraPreview(Context context, Camera camera)
	 * 
	 * @param context Interface to global information about an application environment.
	 * @param camera
	 * 
	 */
	public CameraPreview(Context context, Camera camera) {

		super(context);

		this.mCamera = camera;

		this.mHolder = this.getHolder();

		mHolder.addCallback(this);

		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		Log.d(TAG, "CameraPreview() inicializad");

	}

	/**
	 * surfaceCreated(SurfaceHolder holder)
	 */
	public void surfaceCreated(SurfaceHolder holder) {

		Log.d(TAG, "surfaceCreated() ...");

		try {

			mCamera.setPreviewDisplay(holder);

			mCamera.startPreview();

		} catch (IOException e) {

			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
		} catch (Exception e) {
			Log.d(TAG, "surfaceCreated - Error setting camera preview: ", e);
		}

	}

	/**
	 * surfaceDestroyed(SurfaceHolder holder)
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {

		// empty. Take care of releasing the camera preview in your activity

		Log.d(TAG, "surfaceDestroyed()");

	}

	/**
	 * surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		if (mHolder.getSurface() == null) {
			
			// preview surface does not exist
			Log.d(TAG, "surfaceChanged - getSurface() is null");
			return;
			
		}
		else {
			
			Log.d(TAG, "surfaceChanged");
			
		}

	}

}
