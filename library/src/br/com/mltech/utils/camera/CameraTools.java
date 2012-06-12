package br.com.mltech.utils.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

/**
 * CameraTools
 * 
 * @author maurocl
 * 
 */
public class CameraTools {

	private static final String TAG = "CameraTools";

	private static final String JPEG_FILE_PREFIX = "IMG_";

	private static final String JPEG_FILE_SUFFIX = ".jpg";

	private static final String PNG_FILE_PREFIX = "IMG_";
	private static final String PNG_FILE_SUFFIX = ".PNG";

	private String mCurrentPhotoPath;

	private ImageView mImageView;

	private Bitmap mImageBitmap;

	// --------------------
	private static Camera mCamera;
	private static SurfaceView mPreview;

	// --------------------

	/**
	 * CameraTools()
	 * 
	 * Construtor
	 * 
	 */
	public CameraTools() {

		super();
	}

	/**
	 * checkCameraHardware(Context context)
	 * 
	 * Verifica se um dispositivo possui uma c�mera.
	 * 
	 * @param context
	 *          Contexto da aplica��o.
	 * 
	 * @return true caso o dispositivo possua uma c�mera ou false caso contrario.
	 * 
	 */
	public static boolean checkCameraHardware(Context context) {

		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;

		} else {
			// no c�mera on this device
			return false;
		}

	}

	/**
	 * chooseFileDir()
	 * 
	 * @return a Uri de um arquivo
	 */
	private static Uri chooseFileDir(String nomeArquivo) {

		if (isExternalStorageMounted() == false) {
			Log.w(TAG, "chooseFileDir() - Erro - sdcard n�o foi montado.");
			return null;
		}

		// cria o nome de um diret�rio
		File fotoDiretorio = new File(Environment.getExternalStorageDirectory(), "fotoevento");

		if (!fotoDiretorio.exists()) {
			// diret�rio n�o existe
			Log.d(TAG, "chooseFileDir() - criando o diret�rio ...");
			// cria o diret�rio
			boolean b = fotoDiretorio.mkdirs();
			if (b) {
				Log.d(TAG, "chooseFileDir() - diret�rio criado com sucesso");
			} else {
				Log.w(TAG, "chooseFileDir() - falha na cria��o do diret�rio");
			}
		}

		// cria um arquivo em um diret�rio
		File fotoArquivo = new File(fotoDiretorio, nomeArquivo);

		Uri fotoUri = null;

		if (fotoArquivo != null) {

			fotoUri = Uri.fromFile(fotoArquivo);

		}

		return fotoUri;

	}

	/**
	 * checkCameraFeatures(Context context)
	 * 
	 * Verifica as features b�sicas das c�meras
	 * 
	 * @param context
	 *          Contexto da aplica��o
	 * 
	 * @return true ou false
	 */
	public static boolean checkCameraFeatures(Context context) {

		if (checkCameraHardware(context) == false) {
			// no c�mera on this device
			Log.w(TAG, "checkCameraFeatures() - O dispositivo n�o possui c�mera.");
			return false;
		}

		Log.d(TAG, "checkCameraFeatures() - FEATURE_CAMERA");
		// this device has a camera

		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {
			Log.d(TAG, "checkCameraFeatures() - FEATURE_CAMERA_AUTOFOCUS");
		}

		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
			Log.d(TAG, "checkCameraFeatures() - FEATURE_CAMERA_FLASH");
		}

		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
			Log.d(TAG, "checkCameraFeatures() - FEATURE_CAMERA_FRONT");
		}

		return true;

	}

	/**
	 * createImageFile()
	 * 
	 * Cria um nome para identificar um arquivo
	 * 
	 * @return um arquivo File criado ou null caso haja algum problema
	 * 
	 * @throws IOException
	 */
	public File createImageFile() throws IOException {

		// Cria o nome da imagem (Create an image file name)
		// Obtem uma string com o valor da timeStamp()
		String timeStamp = getTimeStamp();

		// Adiciona um prefixo e um sufixo ao nome gerado
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";

		File directory = getAlbumDir();

		/*
		 * Creates an empty temporary file in the given directory using the given
		 * prefix and suffix as part of the file name. If suffix is null, .tmp is
		 * used.
		 * 
		 * Cria um arquivo vazio tempor�rio no diret�rio dado usando o prefixo e
		 * sufixo como parte do nome do arquivo. Se o sufixo for null ent�o o sufixo
		 * '.tmp' ser� usado.
		 */
		File image = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, directory);

		return image;

	}

	/**
	 * deleteExternalStoragePublicPicture()
	 * 
	 * Apaga (ou remove) uma imagem da �rea p�blica de armzenamento externo
	 * 
	 */
	public static void deleteExternalStoragePublicPicture() {

		// Create a path where we will place our picture in the user's
		// public pictures directory and delete the file.
		// If external storage is not currently mounted this will fail.
		File path = getExternalStoragePublicDirectoryPictures();
		File file = new File(path, "DemoPicture.jpg");
		file.delete();

	}

	/**
	 * exibeCameraInfo(int cameraId, CameraInfo cameraInfo)
	 * 
	 * @param cameraId
	 *          Identificador da c�mera
	 * 
	 * @param cameraInfo
	 *          Informa��es sobre a c�mera
	 * 
	 */

	public static void exibeCameraInfo(int cameraId, CameraInfo cameraInfo) {

		// public static final int CAMERA_FACING_BACK (0) - The facing of the camera
		// is opposite to that of the screen
		// public static final int CAMERA_FACING_FRONT (1) - The facing of the
		// camera is the same as that of the screen

		Camera.getCameraInfo(cameraId, cameraInfo);

		// The direction that the camera faces
		Log.i(TAG, "exibeCameraInfo() - cameraInfo.facing=" + cameraInfo.facing);

		// The orientation of the camera image.
		Log.i(TAG, "exibeCameraInfo() - cameraInfo.orientation=" + cameraInfo.orientation);

	}

	/**
	 * getNumCameras()
	 * 
	 * Retorna o n� de c�meras de um dispositivo
	 * 
	 * @return Retorna o n� de c�meras f�sicas dispon�veis em um dispositivo
	 */
	public static int getNumCameras() {

		return Camera.getNumberOfCameras();
	}

	/**
	 * getCameraInstance()
	 * 
	 * Obt�m a inst�ncia de uma c�mera
	 * 
	 * @return instancia da primeira c�mera dispon�vel no dispositivo ou null caso
	 *         haja algum problema.
	 * 
	 */
	public static Camera getCameraInstance() {

		return getCameraInstance(0);

	}

	/**
	 * getCameraInstance(int num)
	 * 
	 * Obt�m a inst�ncia de uma c�mera num. A primeira c�mera possui o n� 0
	 * 
	 * Uma maneira de obter uma inst�ncia do objeto Camera
	 * 
	 * @param num
	 *          N�mero identificador da c�mera
	 * 
	 * @return a inst�ncia de uma c�mera solicitada ou null caso haja algum
	 *         problema na Camera.
	 */
	public static Camera getCameraInstance(int num) {

		Camera c = null;

		if (num < getNumCameras()) {

			try {
				// Creates a new Camera object to access the first back-facing
				// camera on the device.
				// If the device does not have a back-facing camera, this
				// returns null.
				c = Camera.open(num); // attempt to get a Camera instance
				//
			} catch (Exception e) {
				// camera is not available (in use or does not exist)
				Log.d(TAG, "getCameraInstance() - Error in getCameraInstance()", e);
			}

		} else {
			// n� da c�mera inv�lido (est� fora do intervalo)
			Log.d(TAG, "getCameraInstance() - O n�mero da c�mera: " + num
					+ " est� fora do intervalo de c�meras v�lidas (deve estar entre 0 e " + getNumCameras() + ")");
		}

		return c; // return null if camera is unavailable

	}

	/**
	 * getParametersFlatten(Camera c)
	 * 
	 * Obt�m os detalhes da configura��o de uma c�mera em uma �nica string.
	 * 
	 * @param c
	 *          Inst�ncia de uma c�mera
	 * 
	 * @return Uma String com os detalhes da configura��o da c�mera ou null caso a
	 *         camera esteja vazia
	 * 
	 */
	public static String getParametersFlatten(Camera c) {

		if (c == null) {
			// n�o h� inst�ncia da c�mera
			return null;
		}

		String flatten = c.getParameters().flatten();
		Log.d(TAG, "getParametersFlatten() - params.flatten=" + flatten);

		return flatten;

	}

	/**
	 * getTimeStamp()
	 * 
	 * Retorna a data e hora do sistema de acordo com o formato solicitado
	 * 
	 * @return String contendo a data atual no formato: yyyyMMdd_HHmmss
	 * 
	 */
	private static String getTimeStamp() {

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

		return timeStamp;

	}

	/**
	 * File getAlbumDir()
	 * 
	 * Obt�m o diret�rio onde ...
	 * 
	 * @return
	 */
	private File getAlbumDir() {

		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

			// cria o diret�rio
			if (!storageDir.mkdirs()) {

				if (!storageDir.exists()) {
					// diret�rio j� existente
					Log.d("CameraSample", "failed to create directory");
					return null;
				}

			}

		} else {
			// Log.v(getString(R.string.app_name),
			// "External storage is not mounted READ/WRITE.");
			Log.v(TAG, "External storage is not mounted READ/WRITE.");
		}

		return storageDir;

	}

	/**
	 * getAlbumStorageDir(String albumName)
	 * 
	 * @param albumName
	 * 
	 * @return um arquivo
	 */
	public static File getAlbumStorageDir(String albumName) {

		/*
		 * File (File dir, String name)
		 * 
		 * Constructs a new file using the specified directory and name.
		 * 
		 * Parameters: dir the directory where the file is stored. name the file's
		 * name.
		 * 
		 * Throws NullPointerException if name is null.
		 */

		return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);

	}

	/**
	 * getParametersDetail(Camera c)
	 * 
	 * Cria um HashMap com todos os par�metros de um c�mera.
	 * 
	 * @param c
	 *          Inst�ncia de uma c�mera
	 * 
	 * @return um HashMap onde cada chave � um par�metro e cada valor � uma lista
	 *         dos valores poss�vel do par�metro. Retorna null em caso de erro.
	 * 
	 */
	public static HashMap<String, List<String>> getParametersDetail(Camera c) {

		HashMap<String, List<String>> hash = new HashMap<String, List<String>>();

		ArrayList<String> lista = null;

		if (c == null) {
			return null;
		}

		// Returns the current settings for this Camera service.
		// If modifications are made to the returned Parameters, they must be passed
		// to
		// setParameters(Camera.Parameters) to take effect.
		Parameters params = c.getParameters();

		// O m�todo flatten retorna uma String

		// Creates a single string with all the parameters set in this
		// Camera.Parameters object.
		// flattened a String of parameters (key-value paired) that are
		// semi-colon delimited

		String flatten = params.flatten();

		// Divide a string contendo os par�metros separados por ";"
		String[] lines = flatten.split(";");

		if (lines == null) {
			return null;
		}

		for (String line : lines) {

			// divide um par�metro em pares do tipo key-value paired
			String[] chavesValores = line.split("=");

			// a chave � representada pelo lado esquerdo da atribui��o
			// o valor � representado pelo lado direito da atribui��o
			String chave = null, valor = null;

			// os m�ltiplos valores das chaves (se existirem) s�o separados
			// por ","
			String[] valoresPossiveis = null;

			if (chavesValores != null && chavesValores.length == 2) {

				chave = chavesValores[0];
				valor = chavesValores[1];

				lista = new ArrayList<String>();

				if (valor != null) {
					valoresPossiveis = valor.split(",");
					for (String value : valoresPossiveis) {
						lista.add(value);
					}
				}

			}

			hash.put(chave, lista);

		}
		showHash(hash);

		return hash;

	}

	/**
	 * getDir2(String dirName)
	 * 
	 * Retorna o diret�rio solicitado dentro de
	 * getExternalStoragePublicDirectoryPictures. Cria-o caso ele n�o exista
	 * 
	 * @param dirName
	 *          Nome do diret�rio (no sistema de arquivos)
	 * 
	 * @return uma inst�ncia de File referenciando um diret�rio ou null em caso de algum erro.
	 */
	public static File getDir2(String dirName) {

		File storageDir = null;

		boolean diretorioCriado = false;

		if (!isExternalStorageMounted()) {
			Log.w(TAG,
					"getDir2() - diret�rio n�o poder� ser armazenamento pois o dispositivo externo n�o est� montado para leitura/escrita.");
			return null;
		}

		// obtem o (caminho do) diret�rio onde est�o armazenadas as figuras publicas
		// no dispositivo de armazenamento externo
		File path = getExternalStoragePublicDirectoryPictures();

		// Referencia o diret�rio
		storageDir = new File(path, dirName);

		if (storageDir != null) {

			// cria o diret�rio formado pelo nome completo

			// cria o diret�rio (e sua arvore de subdiret�rios) se necess�rio
			diretorioCriado = storageDir.mkdirs();

			if (diretorioCriado == false) {

				// diret�rio n�o foi criado
				if (storageDir.exists()) {

					// diret�rio j� existente e por isso n�o foi criado
					Log.w(TAG, "getDir2() - Diret�rio: " + dirName + " j� existe.");

				} else {

					// diret�rio ainda n�o existe por�m n�o pode ser criado.
					Log.w(TAG, "getDir2() - Falha na cria��o do diret�rio: " + dirName);

				}

			} else {
				// diret�rio criado com sucesso pois ele ainda n�o existia
				Log.w(TAG, "getDir2() - Diret�rio: " + dirName + " criado com sucesso: ");
			}

		}

		// retorna o diret�rio
		return storageDir;

	}

	/**
	 * getStorageDir(String name)
	 * 
	 * Cria um arquivo dentro do dieret�rio publico para armazenamento de dados no
	 * dispositivo de armazenamento externo.
	 * 
	 * @param name
	 *          Nome
	 * 
	 * @return Um nome de arquivo
	 * 
	 */
	public static File getStorageDir(String name) {

		String type = Environment.DIRECTORY_PICTURES;

		/*
		 * Get a top-level public external storage directory for placing files of a
		 * particular type. This is where the user will typically place and manage
		 * their own files, so you should be careful about what you put here to
		 * ensure you don't erase their files or get in the way of their own
		 * organization.
		 */
		File dir = Environment.getExternalStoragePublicDirectory(type);

		File f = null;

		if (dir != null) {
			f = new File(dir, name);
		}

		return f;

	}

	// ---------------------------------------------------------

	/**
	 * getExternalStoragePublicDirectory()
	 * 
	 * Obt�m o nome do diret�rio p�blico onde est�o armazenadas as imagens
	 * 
	 * @return O nome do diret�rio p�blico onde s�o armazenada as imagens
	 *         (pictures)
	 * 
	 */
	public static File getExternalStoragePublicDirectoryPictures() {

		String type = Environment.DIRECTORY_PICTURES;

		File path = Environment.getExternalStoragePublicDirectory(type);

		return path;

	}

	/* Photo album for this application */
	// private String getAlbumName() {
	// return getString(R.string.album_name);
	// // return "nomeAlbum";
	// }

	/**
	 * handleSmallCameraPhoto(Intent intent)
	 * 
	 * @param intent
	 * 
	 */
	private void handleSmallCameraPhoto(Intent intent) {

		Bundle extras = intent.getExtras();

		if (extras != null) {

			mImageBitmap = (Bitmap) extras.get("data");

			mImageView.setImageBitmap(mImageBitmap);

			mImageView.setVisibility(View.VISIBLE);

		}

	}

	/**
	 * hasExternalStoragePublicPicture()
	 * 
	 * @return true se o arquivo existir no diret�rio ou false, caso contr�rio.
	 * 
	 */
	public static boolean hasExternalStoragePublicPicture(String filename) {

		// Create a path where we will place our picture in the user's
		// public pictures directory and check if the file exists.

		// If external storage is not currently mounted this will think the
		// picture doesn't exist.

		File path = getExternalStoragePublicDirectoryPictures();

		File file = new File(path, filename);

		return file.exists();

	}

	/**
	 * isExternalStorageMounted()
	 * 
	 * Verifica se h� algum disco externo dispon�vel e montado no dispositivo.
	 * 
	 * @return true, caso haja ou false, caso contr�rio
	 */
	public static boolean isExternalStorageMounted() {

		boolean isMounted = false;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			isMounted = true;
		} else {
			isMounted = false;
		}

		return isMounted;

	}

	/**
	 * isExternalMediaMounted()
	 * 
	 * @return true se uma media de armazenamento externo estiver montada; retorna
	 *         false, caso contr�rio.
	 * 
	 */
	public static boolean isExternalMediaMounted() {

		boolean isMounted;

		// Obt�m o estado corrente do principal dispositivo de armazenamento
		// externo
		isMounted = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());

		if (isMounted) {
			// dispositivo est� montado
			Log.d(TAG, "isExternalMediaMounted() - Media externa est� montada.");
		} else {
			// dispositivo n�o n�o est� montado
			Log.w(TAG, "isExternalMediaMounted() - Media externa n�o est� montada.");
		}

		return isMounted;

	}

	/**
	 * isCameraWorking(int cameraID)
	 * 
	 * Verifica se a c�mera identificada por cameraID est� em funcionamento. Para
	 * fazer essa verifica��o tentamos abrir a c�mera. Caso ela consiga ser aberta
	 * indica que a c�mera est� dispon�vel e nesse caso simplesmente liberamos a
	 * c�mera para uso. Se houver erro indica que a c�mera n�o est� dispon�vel
	 * para uso pela aplica��o.
	 * 
	 * @param cameraID
	 *          Identificador da c�mera do dispositivo
	 * 
	 * @return true se for poss�vel obter ima inst�ncia da classe Camera ou false,
	 *         caso contr�rio.
	 */
	public static boolean isCameraWorking(int cameraID) {

		Camera c = getCameraInstance(cameraID);

		if (c != null) {
			// foi poss�vel obter uma inst�ncia da c�mera
			// ent�o, � necess�rio liberar a c�mera para que possa ser usada
			// pela
			// aplica��o
			c.release();
			c = null;

			Log.i(TAG, "isCameraWorking() - c�mera: " + cameraID + " liberada com sucesso");

			return true;

		} else {

			Log.i(TAG, "isCameraWorking() - C�mera: " + cameraID + " n�o est� dispon�vel para uso pela aplica��o");

			return false;

		}

	}

	/**
	 * createExternalStoragePublicPicture()
	 * 
	 * Cria um storage externo p�blico para armazenamento de figuras (pictures)
	 * 
	 */
	public static void createExternalStoragePublicPicture() {

		// Create a path where we will place our picture in the user's
		// public pictures directory.
		// Note that you should be careful about
		// what you place here, since the user often manages
		// these files.
		// For pictures and other media owned by the
		// application, consider Context.getExternalMediaDir().

		File path = getExternalStoragePublicDirectoryPictures();

		File file = new File(path, "DemoPicture.jpg");

		try {

			// Make sure the Pictures directory exists.
			boolean b = path.mkdirs();

			// Very simple code to copy a picture from the application's
			// resource into the external file. Note that this code does
			// no error checking, and assumes the picture is small (does not
			// try to copy it in chunks). Note that if external storage is
			// not currently mounted this will silently fail.

			// InputStream is =
			// getResources().openRawResource(R.drawable.balloons);

			InputStream is = null;

			OutputStream os = new FileOutputStream(file);
			byte[] data = new byte[is.available()];
			is.read(data);
			os.write(data);
			is.close();
			os.close();

			Context context = null;

			// Tell the media scanner about the new file so that it is
			// immediately available to the user.
			MediaScannerConnection.scanFile(context, new String[] { file.toString() }, null,
					new MediaScannerConnection.OnScanCompletedListener() {

						public void onScanCompleted(String path, Uri uri) {

							Log.i("ExternalStorage", "Scanned " + path + ":");
							Log.i("ExternalStorage", "-> uri=" + uri);
						}
					});

		} catch (IOException e) {
			// Unable to create file, likely because external storage is
			// not currently mounted.
			Log.w("ExternalStorage", "Error writing " + file, e);
		}

	}

	public void releaseAndPreview() {
		// mPreview.setCamera(null);
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}

	/**
	 * safeCamera(int id)
	 * 
	 * @param id
	 * 
	 * @return
	 * 
	 */
	public boolean safeCamera(int id) {
		boolean qOpened = false;

		// releaseCameraAndPreview();
		mCamera = Camera.open(id);
		qOpened = (mCamera != null);

		return qOpened;

	}

	// ---------------------------------------------------------

	/**
	 * setUpPhotoFile()
	 * 
	 * @return
	 * 
	 * @throws IOException
	 * 
	 */
	private File setUpPhotoFile() throws IOException {

		File f = createImageFile();

		mCurrentPhotoPath = f.getAbsolutePath();

		return f;

	}

	/**
	 * setPic()
	 */
	private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Obtem o tamanho do ImageView (Get the size of the ImageView) */
		int targetW = mImageView.getWidth();
		int targetH = mImageView.getHeight();

		/* Obt�m o tamanho da imagem (Get the size of the image) */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();

		bmOptions.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;

		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW / targetW, photoH / targetH);
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
		mImageView.setImageBitmap(bitmap);

		/*
		 * mImageView.setVisibility(View.VISIBLE);
		 * mVideoView.setVisibility(View.INVISIBLE);
		 */

	}

	/**
	 * showHash(HashMap<String, List<String>> hash)
	 * 
	 * Exibe os dados de um HashMap onde a chave � uma string e o valor � uma
	 * lista de strings
	 * 
	 * @param hash
	 *          inst�ncia do HashMap
	 * 
	 */
	private static void showHash(HashMap<String, List<String>> hash) {

		int num = 0;

		for (String chave : hash.keySet()) {

			num++;

			List<String> listaValues = hash.get(chave);

			Log.d(TAG, "showHash() chave(" + num + ")=" + chave);

			if (listaValues != null) {

				for (String s2 : listaValues) {
					Log.d(TAG, "  showHash() - value=" + s2);
				}

			}

		}

	}

	/**
	 * showCameraInfo()
	 * 
	 * Exibe informa��es sobre a configura��o de todas as c�meras
	 * 
	 */
	public static void showCameraInfo() {

		Camera camera = null;

		int num = Camera.getNumberOfCameras();

		Log.d(TAG, "showCameraInfo() - n�mero de c�mera(s) dispon�vel(eis):  " + num);

		for (int i = 0; i < num; i++) {

			camera = getCameraInstance(i);

			if (camera != null) {

				Log.d(TAG, "showCameraInfo() - *** Detalhes de configura��o da c�mera: " + i);
				showParametersDetail(camera);

			} else {

				Log.d(TAG, "showCameraInfo() - c�mera " + i + " n�o est� dispon�vel");

			}

		}

	}

	/**
	 * showParametersDetail(Camera c)
	 * 
	 * Exibe os detalhes dos par�metros de configura��o da c�mera
	 * 
	 */
	public static void showParametersDetail(Camera c) {

		Parameters params = c.getParameters();

		// O m�todo flatten retorna uma String
		// Creates a single string with all the parameters set in this
		// Camera.Parameters object.
		// flattened a String of parameters (key-value paired) that are
		// semi-colon
		// delimited

		Log.d(TAG, "flatten=" + params.flatten());

		String flatten = params.flatten();

		// Divide a string contendo os par�metros separados por ";"
		String[] lines = flatten.split(";");

		int index = 0;
		if (lines != null) {

			Log.d(TAG, "N� de linhas: " + lines.length);
			for (String line : lines) {

				// divide um par�metro em pares do tipo key-value paired
				String[] chavesValores = line.split("=");

				// a chave � representada pelo lado esquerdo da atribui��o
				// o valor � representado pelo lado direito da atribui��o
				String chave = null, valor = null;

				// os m�ltiplos valores das chaves (se existirem) s�o separados
				// por ","
				String[] valoresPossiveis = null;

				if (chavesValores != null && chavesValores.length == 2) {

					chave = chavesValores[0];
					valor = chavesValores[1];

					if (valor != null) {
						valoresPossiveis = valor.split(",");
					}

				}

				Log.d(TAG, "chave: " + chave);
				Log.d(TAG, "valor(es): " + valor + " (" + valoresPossiveis.length + ") values");
				for (String value : valoresPossiveis) {
					Log.d(TAG, "  " + value);
				}

				Log.d(TAG, ++index + ": " + line);
			}
		}

	}

	/**
	 * setCameraDisplayOrientation(Activity activity, int cameraId,
	 * android.hardware.Camera camera)
	 * 
	 * Esse m�todo foi tirado do site developer.android.com (Reference)
	 * 
	 * @param activity
	 * @param cameraId
	 * @param camera
	 * 
	 */
	public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {

		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();

		android.hardware.Camera.getCameraInfo(cameraId, info);

		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();

		int degrees = 0;

		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int result;

		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}

		camera.setDisplayOrientation(result);

	}

	/**
	 * getCameraSize(Camera.Size size)
	 * 
	 * Image size (width and height dimensions). height of the picture width of
	 * the picture
	 * 
	 * @param size
	 *          Inst�ncia da classe Camera.Size
	 * 
	 * @return uma string contendo a largura x altura da c�mera
	 */
	public static String getCameraSize(Camera.Size size) {
		if (size != null) {
			return size.width + "x" + size.height;
		}
		return null;
	}

	
	//---------------------------------------------------------------------------
	
	public List<String> getSupportedAntibanding(Camera.Parameters param) {

		return null;

	}

	/**
	 * getSupportedColorEffects(Camera.Parameters param)
	 * 
	 * @param param
	 * @return
	 */
	public static List<String> getSupportedColorEffects(Camera.Parameters param) {
	  
	  return param.getSupportedColorEffects();
	  
	}

	public List<String> getSupportedFlashModes(Camera.Parameters param) {
		return null;
	}

	public List<String> getSupportedFocusModes(Camera.Parameters param) {
		return null;
	}

	public List<Camera.Size> getSupportedJpegThumbnailSizes(Camera.Parameters param) {
		return null;
	}

	public List<Integer> getSupportedPictureFormats(Camera.Parameters param) {
		return null;
	}

	public List<Camera.Size> getSupportedPictureSizes(Camera.Parameters param) {
		return null;
	}

	public List<Integer> getSupportedPreviewFormats(Camera.Parameters param) {
		return null;
	}

	public List<int[]> getSupportedPreviewFpsRange(Camera.Parameters param) {
		return null;
	}

	public List<Integer> getSupportedPreviewFrameRates(Camera.Parameters param) {
		return null;
	}

	/**
	 * 
	 * @param param
	 * @return
	 */
	public List<Camera.Size> getSupportedPreviewSizes(Camera.Parameters param) {
	  return param.getSupportedPreviewSizes();
	}

	public List<String> getSupportedSceneModes(Camera.Parameters param) {
		return null;
	}

	public List<Camera.Size> getSupportedVideoSizes(Camera.Parameters param) {
		return null;
	}

	public List<String> getSupportedWhiteBalance(Camera.Parameters param) {
		return null;
	}

	//---------------------------------------------------------------------------
	
}
