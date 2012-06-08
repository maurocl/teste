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
 *         !!! Essa vers�o funciona no telefone por�m ela trava no simulador !!!
 * 
 */
public class CameraActivity extends Activity {

	private static final String TAG = "CameraActivity";

	// inst�ncia da c�mera
	private static Camera mCamera;

	// inst�ncia da CameraPreview
	private static CameraPreview mPreview;

	// nome do diret�rio
	private static File picsDir;

	// flag usado para controle da aplica��o
	private static int flag = 0;

	// Vari�vel usada para desenhar a foto na tela
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
		 * Trata o evento de disparar o bot�o
		 */
		Button capture = (Button) findViewById(R.id.button_capture);

		capture.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {

				if (mCamera == null) {
					Log.e(TAG, "mCamera is null na hora de acionar o dispatador !!!");
				}

				// bot�o de captura foi pressionado
				Log.i(TAG, "bot�o de captura foi pressionado");

				mCamera.takePicture(shutter, null, jpeg);

			}
		});

	}

	/**
	 * shutter
	 * 
	 * Define um objeto que cont�m uma inst�ncia da classe ShutterCallback
	 * respons�vel pelo tratamento do evento do disparo do bot�o da c�mera
	 * � invocado assim que o obturador da c�mera entra em a��o e captura a imagem.
	 * 
	 */
	final ShutterCallback shutter = new ShutterCallback() {

		public void onShutter() {

			Log.d(TAG, "onShutter() callback disparado !!!");
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

			Log.d(TAG, "onPictureTaken - raw");
		}

	};

	/**
	 * jpeg
	 * 
	 * Define um objeto que cont�m uma inst�ncia da classe PictureCallback
	 * respons�vel pelo tratamento do evento do recebimento da imagem JPEG da
	 * c�mera
	 * Invocado assim que a imagem comprimida, no formato JPEG, est� dispon�vel.
	 * 
	 */
	final PictureCallback jpeg = new PictureCallback() {

		/**
		 * M�todo chamado quando houver uma foto JPEG dispon�vel
		 */
		public void onPictureTaken(byte[] data, Camera camera) {

			Log.d(TAG, "onPictureTaken() - jpeg");

			if (data != null) {
				Log.d(TAG, "data: " + data.length + " bytes");
			} else {
				Log.d(TAG, "data est� vazio");
			}

			// TODO aqui deveremos criar o nome dos arquivos

			String nomeArquivo = System.currentTimeMillis() + ".jpg";

			boolean gravou = gravaArquivo(data, nomeArquivo);

			// foto j� foi tirada e gravada.
			// agora � ...

			reiniciaCamera();

			// -------------------------------------------------------------
			// Cria uma Intent de retorno
			// -------------------------------------------------------------
			Intent intent = new Intent();

			// it.putExtra("br.com.mltech.usuarioValidado", "OK");
			// Log.d(TAG, "Usu�rio validado");

			if (gravou) {

				intent.putExtra("br.com.mltech.dados", data);

				// intent.putExtra("br.com.mltech.image.filename", imageFile.getName());

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

				Log.d(TAG, "onResume - antes da chamada do m�todo mCamera.startPreview()");

				mCamera.stopPreview();

				// Starts capturing and drawing preview frames to the screen
				mCamera.startPreview();

				Log.d(TAG, "onResume - ap�s chamada do m�todo mCamera.startPreview()");

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
			Log.w(TAG, "onDestroy() - mCamera is null");
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

			c = Camera.open();

		} catch (RuntimeException e) {
			Log.w(TAG, "getCameraInstance() - RuntimeException error na obten��o de uma inst�ncia da c�mera", e);

		} catch (Exception e) {
			// A c�mera n�o existe ou n�o se encontra dispon�vel
			Log.w(TAG, "getCameraInstance() - Erro na obten��o de uma inst�ncia da c�mera", e);
		}

		if (c == null) {
			Log.w(TAG, "getCameraInstance() - CAMERA IS NULL");
		}

		return c; // retorna null caso a camera n�o exista

	}

	/**
	 * preparaDirerorioFotos()
	 * 
	 * Prepara o diret�rio onde as fotos ser�o armazenadas
	 */
	private void preparaDiretorioGravarFotos() {

		boolean isDirCreated = false;

		picsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/fotos/");

		Log.d(TAG, "preparaDiretorioGravarFotos() - picDir.absolutePath=" + picsDir.getAbsolutePath());
		Log.d(TAG, "preparaDiretorioGravarFotos() - picDir.name=" + picsDir.getName());

		// Make sure the Pictures directory exists.
		// Garante a exist�ncia do diret�rio
		isDirCreated = picsDir.mkdirs();

		if (isDirCreated) {

			showFile(picsDir);

		} else {
			Log.w(TAG, "preparaDiretorioGravarFotos() - N�o foi possivel criar o diret�rio: " + picsDir.getName());

			if (picsDir.exists()) {
				Log.w(TAG, "preparaDiretorioGravarFotos() - Diret�rio " + picsDir.getName() + " j� existe !");
			}
		}

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
	 * 
	 * 
	 */
	private void reiniciaCamera() {

		SurfaceHolder surfaceHolder = null;

		// aqui � necess�rio iniciar a c�mera novamente
		Log.d(TAG, "reiniciaCamera()");

		if (mPreview != null) {
			surfaceHolder = mPreview.getHolder();
		} else {
			Log.w(TAG, "reiniciaCamera() - mPreview est� nulo");
		}

		try {
			mCamera.setPreviewDisplay(surfaceHolder);
			mCamera.startPreview();
		} catch (IOException e) {
			Log.w(TAG, "reiniciaCamera() - IOException", e);
		}

	}

	/**
	 * releaseCamera()
	 * 
	 * Libera a c�mera para ser usada em outras aplica��es
	 * 
	 * @return true se a c�mera foi liberada com sucesso ou false em caso de erro.
	 */
	private boolean releaseCamera() {

		boolean isCameraReleased = false;

		if (mCamera != null) {

			mCamera.release();

			mCamera = null;

			Log.d(TAG, "releaseCamera() - c�mera liberada com sucesso");

			isCameraReleased = true;

		} else {

			// c�mera n�o estava ligada
			Log.w(TAG, "releaseCamera() - c�mera n�o pode ser liberada pois mCamera � nula");

		}

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
		 * cart�o de mem�roia
		 ********************************************************************************************/

		String externalStorageState = Environment.getExternalStorageState();

		if (!externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
			Log.w(TAG, "gravaArquivo() - m�dia n�o est� montada");
			return false;
		}

		if (externalStorageState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			Log.w(TAG, "gravaArquivo() - m�dia montada por�m est� ReadOnly");
			return false;
		}

		Log.d(TAG, "gravaArquivo() - getExternalStorageState()=" + Environment.getExternalStorageState());

		// cria uma arquivo para armazernar a foto
		File f = new File(picsDir, nomeArquivo);

		FileUtils.showFile(f);

		FileOutputStream fos = null;

		boolean gravou = false;

		try {

			try {

				fos = new FileOutputStream(f);
				fos.write(data);

				Log.d(TAG, "gravaArquivo() - Arquivo: " + f.getName() + " foi gerado e ocupa " + f.length() + " bytes");

				gravou = true;

			} catch (FileNotFoundException e) {

				Log.d(TAG, "gravaArquivo() - FileNotFoundException: arquivo " + f.getName() + " n�o foi encontrado.");

			} catch (IOException e) {

				Log.d(TAG, "gravaArquivo() - IOException: " + f.getName());

			} catch (Exception e) {

				Log.d(TAG, "gravaArquivo() - Falha na grava��o do arquivo: " + f.getName());

			} finally {

				if (fos != null) {
					fos.close();
				}

			}

		} catch (IOException e) {
			Log.d(TAG, "gravaArquivo() - IOException", e);
		}

		return gravou;

	}

}
