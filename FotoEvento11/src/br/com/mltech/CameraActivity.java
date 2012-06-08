package br.com.mltech;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
 *         !!! Essa versão funciona no telefone porém ela trava no simulador !!!
 * 
 */
public class CameraActivity extends Activity {

	private static final String TAG = "CameraActivity";

	// instância da câmera
	private static Camera mCamera;

	// instância da CameraPreview
	private static CameraPreview mPreview;

	// nome do diretório
	private static File picsDir;

	// flag usado para controle da aplicação
	private static int flag = 0;

	// Variável usada para desenhar a foto na tela
	private FrameLayout layoutPreview;

	private static Button btnOk;
	private static Button btnNovo;
	private static Button btnCancelar;

	/**
	 * 
	 */
	private Bitmap mImageBitmap;

	private Uri mUri;

	/**
	 * onCreate(Bundle savedInstanceState)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Log.d(TAG, "*** onCreate() ***");

		setContentView(R.layout.cameraprev);

		boolean isCameraAvailable = CameraTools.checkCameraHardware(this);
		if (isCameraAvailable) {
			Log.d(TAG, "onCreate() - há câmera " + Camera.getNumberOfCameras() + " disponível.");
		} else {
			Log.e(TAG, "onCreate() - não há câmeras disponíveis");
			return;
		}

		for (int i = 0; i < Camera.getNumberOfCameras() - 1; i++) {

			Log.e(TAG, "onCreate() - verificando o estado da câmera: " + i);

			boolean isCameraWorking = CameraTools.isCameraWorking(i);

			if (isCameraWorking) {
				Log.e(TAG, "onCreate() - câmera está funcionando corretamente");
			} else {
				Log.e(TAG, "onCreate() - câmera não está disponível para uso pela aplicação");
				return;
			}

		}

		preparaDiretorioGravarFotos();

		layoutPreview = (FrameLayout) findViewById(R.id.camera_preview);

		/**
		 * Trata o evento de disparar o botão
		 */
		Button capture = (Button) findViewById(R.id.button_capture);

		btnOk = (Button) findViewById(R.id.btnOk);
		btnNovo = (Button) findViewById(R.id.btnNovo);
		btnCancelar = (Button) findViewById(R.id.btnCancelar);

		btnOk.setVisibility(android.view.View.GONE);
		btnNovo.setVisibility(android.view.View.GONE);
		btnCancelar.setVisibility(android.view.View.GONE);

		/**
		 * Botão Capturar
		 */
		capture.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {

				if (mCamera == null) {
					Log.e(TAG, "botão Capturar - mCamera is null na hora de acionar o dispatador !!!");
					return;
				}

				// botão de captura foi pressionado
				Log.i(TAG, "botão de captura foi pressionado");

				mCamera.takePicture(shutter, null, jpeg);

			}
		});

		/**
		 * Nova foto
		 */
		btnNovo.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				Log.d(TAG, "botão nova foto");

				btnOk.setVisibility(android.view.View.GONE);
				btnNovo.setVisibility(android.view.View.GONE);
				btnCancelar.setVisibility(android.view.View.GONE);

				reiniciaCamera();

			}
		});

		/**
		 * Botão Ok
		 */
		btnOk.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {

				Log.d(TAG, "botão Ok");

				Intent intent = new Intent();

				if (mImageBitmap == null) {
					Log.w(TAG, "mImageBitmap é nulo");
				}

				// data
				intent.putExtra("data", mUri);

				setResult(RESULT_OK, intent);

				Log.d(TAG, "btnOk - retorno");

				// finaliza a activity
				finish();

			}
		});

		/**
		 * Botão cancelar
		 */
		btnCancelar.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				// finalizaActivity();
				Log.d(TAG, "botão cancelar");

				Intent intent = new Intent();

				setResult(RESULT_CANCELED, intent);

				// finaliza a activity
				finish();

			}
		});

	}

	/**
	 * shutter
	 * 
	 * Define um objeto que contém uma instância da classe ShutterCallback
	 * responsável pelo tratamento do evento do disparo do botão da câmera É
	 * invocado assim que o obturador da câmera entra em ação e captura a imagem.
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
	 * Define um objeto que contém uma instância da classe PictureCallback
	 * responsável pelo tratamento do evento do recebimento da imagem "crua" da
	 * câmera (quando a imagem "crua" tirada pela câmera está disponível.
	 */
	final PictureCallback raw = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {

			Log.d(TAG, "onPictureTaken - raw");
		}

	};

	/**
	 * jpeg
	 * 
	 * Define um objeto que contém uma instância da classe PictureCallback
	 * responsável pelo tratamento do evento do recebimento da imagem JPEG da
	 * câmera Invocado assim que a imagem comprimida, no formato JPEG, está
	 * disponível.
	 * 
	 */
	final PictureCallback jpeg = new PictureCallback() {

		/**
		 * Método chamado quando houver uma foto JPEG disponível
		 */
		public void onPictureTaken(byte[] data, Camera camera) {

			Bitmap b = null;

			Log.d(TAG, "onPictureTaken() - jpeg");

			if (data != null) {
				Log.d(TAG, "onPictureTaken() - data: " + (data.length / 1024.0) + " KBytes");
			} else {
				Log.d(TAG, "onPictureTaken() - data não retornou nenhum dado");
			}

			// TODO aqui deveremos criar o nome dos arquivos

			// String nomeArquivo = System.currentTimeMillis() + ".jpg";
			//File f = FileUtils.obtemNomeArquivoJPEG();
			File f = FileUtils.obtemNomeArquivo(".jpg");

			String nomeArquivo = f.getAbsolutePath();

			if (data != null) {

				mImageBitmap = ManipulaImagem.getBitmapFromByteArray(data);

				boolean gravou = gravaArquivo(data, nomeArquivo);

				if (gravou) {
					Log.d(TAG, "onPictureTaken() - arquivo " + nomeArquivo + " gravado com sucesso.");
					Toast.makeText(getBaseContext(), "Arquivo " + nomeArquivo + " gravado com sucesso.", Toast.LENGTH_SHORT).show();
				} else {
					Log.d(TAG, "onPictureTaken() - falha na gravação do arquivo " + nomeArquivo);
				}

				File file = new File(nomeArquivo);
				mUri = Uri.fromFile(file);

			} else {
				Log.w(TAG, "onPictureTaken() - nenhum data foi retornado");
			}

			// foto já foi tirada e gravada.
			// agora é ...
			// reiniciaCamera();

			btnOk.setVisibility(android.view.View.VISIBLE);
			btnNovo.setVisibility(android.view.View.VISIBLE);
			btnCancelar.setVisibility(android.view.View.VISIBLE);

			// finalizaActivity(intent, sucesso);

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

			Log.d(TAG, "onResume() - flag=" + flag);

			if (flag == 0) {

				flag = 1;

			} else {

				Log.d(TAG, "onResume() - antes da chamada do método mCamera.startPreview()");

				// Stops capturing and drawing preview frames to the surface, and resets
				// the camera for a future call to startPreview().
				mCamera.stopPreview();

				// Starts capturing and drawing preview frames to the screen
				mCamera.startPreview();

				Log.d(TAG, "onResume() - após chamada do método mCamera.startPreview()");

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
			// a câmera foi liberada com sucesso
			Log.d(TAG, "onPause() - câmera liberada com sucesso");
		} else {
			// a câmera já estava desligada
			Log.w(TAG, "onPause() - falha na liberação da câmera");
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
	 * Aplicação foi reinicializada.
	 * 
	 * É executado após um onStop()
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
			Log.w(TAG, "onDestroy() - câmera está liberada");
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

			// Tenta obter uma instância da câmera
			c = Camera.open();

		} catch (RuntimeException e) {
			Log.e(TAG,
					"getCameraInstance() - RuntimeException - Houve uma erro em tempo de execução na obtenção de uma instância da câmera", e);

		} catch (Exception e) {
			// A câmera não existe ou não se encontra disponível
			Log.e(TAG, "getCameraInstance() - Exception - Houve uma exceção na obtenção de uma instância da câmera", e);
		}

		return c; // retorna null caso a camera não exista

	}

	/**
	 * preparaDirerorioFotos()
	 * 
	 * Prepara o diretório onde as fotos serão armazenadas
	 */
	private void preparaDiretorioGravarFotos() {

		boolean isDirCreated = false;

		picsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/fotos/");

		Log.d(TAG, "preparaDiretorioGravarFotos() - picDir.absolutePath=" + picsDir.getAbsolutePath());
		Log.d(TAG, "preparaDiretorioGravarFotos() - picDir.name=" + picsDir.getName());

		// Make sure the Pictures directory exists.
		// Garante a existência do diretório
		isDirCreated = picsDir.mkdirs();

		if (isDirCreated) {

			showFile(picsDir);

		} else {

			Log.w(TAG, "preparaDiretorioGravarFotos() - Não foi possivel criar o diretório: " + picsDir.getName());

			if (picsDir.exists()) {
				Log.w(TAG, "preparaDiretorioGravarFotos() - Diretório " + picsDir.getName() + " já existe !");
			}

		}

	}

	/**
	 * showFile(File f)
	 * 
	 * Exibe informações sobre um arquivo: - informações sobre a permissão de
	 * leitura (R), escrita (W) e execução (X) do um arquivo f - informações sobre
	 * a permissão de leitura (R), escrita (W) e execução (X) do diretório picsDir
	 * 
	 * @param f
	 *          instância da classe File
	 * 
	 */
	private void showFile(File f) {

		Log.d(TAG, "showFile(): ");

		if (f == null) {
			Log.w(TAG, "arquivo é nulo");
			return;
		}

		Log.d(TAG, "  f.getAbsolutePath=" + f.getAbsolutePath());
		Log.d(TAG, "  f.getName()=" + f.getName());

		String canRead = f.canRead() == true ? "R" : "-";
		String canWrite = f.canWrite() == true ? "W" : "-";
		String canExecute = f.canRead() == true ? "X" : "-";

		String permission = canRead + canWrite + canExecute;
		Log.d(TAG, "  Permissão do arquivo: " + permission);

		Log.d(TAG, "  picsDir.canWrite(): " + picsDir.canWrite());
		Log.d(TAG, "  picsDir.canRead(): " + picsDir.canRead());
		Log.d(TAG, "  picsDir.canExecute(): " + picsDir.canExecute());

		Log.d(TAG, "  picsDir.getAbsolutePath()=" + picsDir.getAbsolutePath());

	};

	/**
	 * reiniciaCamera()
	 * 
	 * Reinicia a câmera
	 * 
	 */
	private void reiniciaCamera() {

		SurfaceHolder surfaceHolder = null;

		// aqui é necessário iniciar a câmera novamente
		Log.d(TAG, "reiniciaCamera()");

		if (mCamera != null) {

			mPreview = new CameraPreview(this, mCamera);

			layoutPreview.addView(mPreview);

		} else {
			Log.w(TAG, "reiniciaCamera() - mCamera está nula");
		}

		/*
		 * if (mPreview != null) { surfaceHolder = mPreview.getHolder(); } else {
		 * Log.w(TAG, "reiniciaCamera() - mPreview está nulo"); }
		 */

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
	 * Libera a câmera para ser usada em outras aplicações (se a câmera estiver em
	 * uso pela aplicação)
	 * 
	 * @return true se a câmera foi liberada com sucesso ou false em caso de erro.
	 */
	private boolean releaseCamera() {

		boolean isCameraReleased = false;

		if (mCamera != null) {

			// libera a câmera
			mCamera.release();

			// atribui null para câmera corrente
			mCamera = null;

			Log.d(TAG, "releaseCamera() - câmera liberada com sucesso");

			isCameraReleased = true;

		} else {

			// câmera não estava ligada
			Log.w(TAG, "releaseCamera() - câmera não pode ser liberada pois não há uma instância da Camera em uso.");

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
		 * Quando o telefone está conectado e em testes ele não permite montar o
		 * cartão de memória
		 ********************************************************************************************/

		String externalStorageState = Environment.getExternalStorageState();

		if (!externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
			Log.w(TAG, "gravaArquivo() - mídia não está montada");
			return false;
		}

		if (externalStorageState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			Log.w(TAG, "gravaArquivo() - mídia montada porém está ReadOnly");
			return false;
		}

		Log.d(TAG, "gravaArquivo() - getExternalStorageState()=" + Environment.getExternalStorageState());

		// cria uma arquivo para armazernar a foto
		File f = new File(nomeArquivo);

		FileUtils.showFile(f);

		FileOutputStream fos = null;

		boolean gravou = false;

		try {

			try {

				fos = new FileOutputStream(f);
				fos.write(data);

				Log.d(TAG, "gravaArquivo() - Arquivo: " + f.getAbsolutePath() + " foi gerado e ocupa " + (f.length() / 1024.0) + " KBytes");

				gravou = true;

			} catch (FileNotFoundException e) {

				Log.d(TAG, "gravaArquivo() - FileNotFoundException: arquivo " + f.getAbsolutePath() + " não foi encontrado.");

			} catch (IOException e) {

				Log.d(TAG, "gravaArquivo() - IOException: " + f.getAbsolutePath());

			} catch (Exception e) {

				Log.d(TAG, "gravaArquivo() - Falha na gravação do arquivo: " + f.getAbsolutePath());

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

	/**
	 * finalizaActivity(boolean gravou, byte[] data)
	 * 
	 * 
	 */
	void finalizaActivity(Intent intent, boolean sucesso) {

		Log.d(TAG, "finalizaActivity() - sucesso: " + sucesso);

		if (sucesso) {

			setResult(RESULT_OK, intent);

		} else {

			setResult(RESULT_CANCELED, intent);

		}

	}

}
