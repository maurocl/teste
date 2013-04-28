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

	private String mCurrentPhotoPath;

	private ImageView mImageView;

	private Bitmap mImageBitmap;

	// --------------------
	private static Camera mCamera;

	// diret�rio (onde as fotos ser�o armazenadas)
	private static File picsDir;

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
	 * Verifica as features b�sicas das c�meras.
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
	 * Cria um nome para identificar um arquivo.
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
	 * Apaga (ou remove) uma imagem da �rea p�blica de armazenamento externo.
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
	 * Exibe informa��es (a dire��o e a orienta��o) sobre a c�mera selecionada.
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
	 * Retorna o n� de c�meras de um dispositivo.<br>
	 * 
	 * @return Retorna o n� de c�meras f�sicas dispon�veis em um dispositivo.
	 * 
	 */
	public static int getNumCameras() {

		return Camera.getNumberOfCameras();
	}

	/**
	 * Obt�m a inst�ncia da c�mera padr�o (Id=0).<br>
	 * 
	 * @return a inst�ncia da c�mera padr�o do dispositivo (se houver) ou null
	 *         caso haja algum problema.
	 * 
	 */
	public static Camera getCameraInstance() {

		return getCameraInstance(0);

	}

	/**
	 * Obt�m a inst�ncia da c�mera dado seu Id. A c�mera traseira possui Id igual
	 * a 0.<br>
	 * 
	 * @param cameraId
	 *          identificador da c�mera
	 * 
	 * @return a inst�ncia da c�mera solicitada ou null caso haja algum problema
	 *         na Camera.
	 */
	public static Camera getCameraInstance(int cameraId) {

		Camera c = null;

		if (cameraId < getNumCameras()) {

			try {
				// Creates a new Camera object to access the first back-facing
				// camera on the device.
				// If the device does not have a back-facing camera, this
				// returns null.
				c = Camera.open(cameraId); // attempt to get a Camera instance
				//
			} catch (RuntimeException e) {
				// camera is not available (in use or does not exist)
				Log.d(TAG, "getCameraInstance() - Error in getCameraInstance()", e);
			}

		} else {
			// n� da c�mera inv�lido (est� fora do intervalo)
			Log.d(TAG, "getCameraInstance() - O n�mero da c�mera: " + cameraId
					+ " est� fora do intervalo de c�meras v�lidas (deve estar entre 0 e " + getNumCameras() + ")");
		}

		// return null if camera is unavailable
		return c;

	}

	/**
	 * Obt�m os detalhes da configura��o de uma c�mera em uma �nica string.<br>
	 * 
	 * @param c
	 *          Inst�ncia de uma c�mera<br>
	 * 
	 * @return Uma String com os detalhes da configura��o da c�mera ou null caso a
	 *         camera esteja vazia<br>
	 * 
	 */
	public static String getParametersFlatten(Camera c) {

		if (c == null) {
			// n�o h� inst�ncia da c�mera
			Log.d(TAG, "getParametersFlatten() - c�mera � nula");
			return null;
		}

		String flatten = c.getParameters().flatten();
		
		Log.d(TAG, "getParametersFlatten() - params.flatten=" + flatten);

		return flatten;

	}

	/**
	 * Retorna a data e hora do sistema de acordo com o formato solicitado.
	 * 
	 * @return String contendo a data atual no formato: yyyyMMdd_HHmmss
	 * 
	 */
	private static String getTimeStamp() {

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

		return timeStamp;

	}

	/**
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
	 * Cria uma estrutura de dados (HashMap) com todos os par�metros de
	 * configura��o de um c�mera.<br>
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
	 * Retorna o diret�rio solicitado dentro de
	 * getExternalStoragePublicDirectoryPictures.<br>
	 * Cria-o caso ele n�o exista.<br>
	 * 
	 * @param dirName
	 *          Nome do diret�rio (no sistema de arquivos)
	 * 
	 * @return uma inst�ncia de File referenciando um diret�rio ou null em caso de
	 *         algum erro.
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
	 * Cria um arquivo dentro do diret�rio p�blico para armazenamento de dados no
	 * dispositivo de armazenamento externo.
	 * 
	 * @param name
	 *          Nome do diret�rio
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
	 * Verifica se a c�mera identificada por cameraID est� em funcionamento.<br>
	 * Para fazer essa verifica��o tentamos abrir a c�mera. Caso ela consiga ser
	 * aberta indica que a c�mera est� dispon�vel e nesse caso simplesmente
	 * liberamos a c�mera para uso. Se houver erro indica que a c�mera n�o est�
	 * dispon�vel para uso pela aplica��o.
	 * 
	 * @param cameraID
	 *          Identificador da c�mera do dispositivo
	 * 
	 * @return true se for poss�vel obter ima inst�ncia da classe Camera ou false,
	 *         caso contr�rio.
	 */
	public static boolean isCameraWorking(int cameraID) {

		Camera c = getCameraInstance(cameraID);

		if (c == null) {

			// c�mera � nula
			Log.i(TAG, "isCameraWorking() - C�mera: " + cameraID + " n�o est� dispon�vel para uso pela aplica��o");

			return false;

		}

		// c�mera ID foi instanciada com sucesso.
		// Ent�o, desconecta e libera os recursos usados pela c�mera.
		c.release();

		c = null;

		Log.i(TAG, "isCameraWorking() - c�mera: " + cameraID + " est� funcionando corretamente");

		return true;

	}

	/**
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

	/**
   * 
   */
	public void releaseAndPreview() {

		// mPreview.setCamera(null);
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}

	}

	/**
	 * 
	 * @param id Identificador da c�mera
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
   * 
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
	 * Exibe os dados de um HashMap onde a chave � uma string e o valor � uma
	 * lista de strings.
	 * 
	 * @param hash
	 *          inst�ncia do HashMap
	 * 
	 */
	private static void showHash(HashMap<String, List<String>> hash) {

		int num = 0;

		// para cada chave ...
		for (String chave : hash.keySet()) {

			// incrementa o n� de chaves
			num++;

			// obtem a lista de strings associada a chave
			List<String> listaValues = hash.get(chave);

			// Loga o nome da chave e seu n� de sequencia associado
			Log.d(TAG, "showHash() chave(" + num + ")=" + chave);

			if (listaValues != null) {

				// exibe a lista de valores associada a chave
				for (String valor : listaValues) {
					Log.d(TAG, "  showHash() - value=" + valor);
				}

			}

		}

	}

	/**
	 * Exibe a configura��o de todas as c�meras dispon�veis no dispositivo no log
	 * da aplica��o.
	 * 
	 */
	public static void showCameraInfo() {

		Camera camera = null;

		// Obt�m o n� de c�meras do dispositivo.
		int num = Camera.getNumberOfCameras();

		Log.d(TAG, "showCameraInfo() - n�mero de c�mera(s) dispon�vel(eis):  " + num);

		// percorre todas as c�meras dispon�veis.
		for (int i = 0; i < num; i++) {

			// obt�m a inst�ncia de uma c�mera
			camera = getCameraInstance(i);

			if (camera != null) {

				// c�mera retornada
				Log.d(TAG, "showCameraInfo() - *** Detalhes de configura��o da c�mera: " + i);
				showParametersDetail(camera);

			} else {

				// c�mera n�o est� dispon�vel
				Log.d(TAG, "showCameraInfo() - c�mera " + i + " n�o est� dispon�vel");

			}

		}

	}

	/**
	 * Exibe os detalhes dos par�metros de configura��o da c�mera.<br>
	 * As informa��es ser�o exibidad no log.
	 * 
	 * @param c
	 *          Inst�ncia da classe Camera.
	 * 
	 */
	public static void showParametersDetail(Camera c) {

		int index = 0;

		// obt�m os parametros de configura��o da Camera.
		Parameters params = c.getParameters();

		// O m�todo flatten retorna uma String
		// Creates a single string with all the parameters set in this
		// Camera.Parameters object.
		// flattened a String of parameters (key-value paired) that are
		// semi-colon
		// delimited

		if (params != null) {

			Log.d(TAG, "flatten=" + params.flatten());

			String flatten = params.flatten();

			// Divide a string contendo os par�metros separados por ";"
			String[] lines = flatten.split(";");

			if (lines != null) {

				Log.d(TAG, "N� de linhas: " + lines.length);

				for (String line : lines) {

					// index = xxx(index, line);
					// Log.d(TAG,"line=["+line+"]");

					Log.v(TAG, ++index + ": " + line);

				}

			} else {
				// lines is null
			}

		} else {
			//
			Log.w(TAG, "flatten � null");
		}

	}

	/**
	 * Recebe uma linha que possui informa��es sobre a configura��o da m�quina.
	 * 
	 * A linha normalmente possui a seguinte estrutura:
	 * 
	 * nome=valor
	 * 
	 * ou
	 * 
	 * nome=
	 * 
	 * nome=valor1,valor2,...,valorN
	 * 
	 * @param line
	 * 
	 * @return
	 * 
	 */
	private static void xxx(String line) {

		if (line == null) {
			// retorna se a linha for vazia
			return;
			
		} else {

			// divide um par�metro em pares do tipo key-value paired
			String[] chaveValor = line.split("=");

			// a chave � representada pelo lado esquerdo da atribui��o
			String chave = null;

			// o valor � representado pelo lado direito da atribui��o
			String valor = null;

			if (chaveValor != null) {

				if (chaveValor.length == 2) {

					chave = chaveValor[0];
					valor = chaveValor[1];

					// os m�ltiplos valores das chaves (se existirem) s�o separados por
					// ","
					String[] multiplosValores = valor.split(",");

					Log.v(TAG, "chave: " + chave);

					if (multiplosValores != null) {

						Log.v(TAG, "valor(es): " + valor + " (" + multiplosValores.length + ") values");

						for (String value : multiplosValores) {
							Log.v(TAG, "  " + value);

						}

					}

				} else if (chaveValor.length == 1) {
					chave = chaveValor[0];
				}

			}

		}

		return;

	}

	/**
	 * Esse m�todo foi tirado do site developer.android.com (Reference)<br>
	 * 
	 * @param activity
	 * @param cameraId Identificador da c�mera
	 * @param camera
	 * 
	 */
	public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {

		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();

		android.hardware.Camera.getCameraInfo(cameraId, info);

		// obtem a rota��o do display default
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

	// ---------------------------------------------------------------------------

	/**
	 * @param param
	 * 
	 * @return
	 */
	public List<String> getSupportedAntibanding(Camera.Parameters param) {

		return null;

	}

	/**
	 * Obt�m uma lista de strings contendo os efeitos de cores suportados pela
	 * c�mera
	 * 
	 * @param param
	 *          Um array de strings
	 * 
	 * @return uma lista dos valores suportados
	 */
	public static List<String> getSupportedColorEffects(Camera.Parameters param) {

		return param.getSupportedColorEffects();

	}

	/**
	 * Obt�m uma lista de strings contendo os modos de flash suportados
	 * 
	 * @param param
	 * 
	 * @return Um array de strings
	 */
	public List<String> getSupportedFlashModes(Camera.Parameters param) {

		return null;
	}

	/**
	 * Obt�m uma lista de strings contendo os modos de focos suportados
	 * 
	 * @param param
	 * 
	 * @return
	 */
	public List<String> getSupportedFocusModes(Camera.Parameters param) {

		return null;
	}

	/**
	 * Obt�m uma lista de strings contendo os tamanhos de thumbnails suportados
	 * 
	 * @param param
	 * 
	 * @return
	 */
	public List<Camera.Size> getSupportedJpegThumbnailSizes(Camera.Parameters param) {

		return null;
	}

	/**
	 * Obt�m uma lista de strings contendo os formatos de imagens suportados
	 * 
	 * @param param
	 * 
	 * @return
	 */
	public List<Integer> getSupportedPictureFormats(Camera.Parameters param) {

		return null;
	}

	/**
	 * @param param
	 * 
	 * @return
	 */
	public List<Camera.Size> getSupportedPictureSizes(Camera.Parameters param) {

		return null;
	}

	/**
	 * Obt�m uma lista de strings contendo os formatos de preview suportados
	 * 
	 * @param param
	 * 
	 * @return
	 */
	public List<Integer> getSupportedPreviewFormats(Camera.Parameters param) {

		return null;
	}

	/**
	 * Obt�m uma lista de strings contendo as faixa de valores de fps para captura
	 * de imagens que s�o suportados
	 * 
	 * @param param
	 * 
	 * @return
	 */
	public List<int[]> getSupportedPreviewFpsRange(Camera.Parameters param) {

		return null;
	}

	/**
	 * @param param
	 * 
	 * @return
	 */
	public List<Integer> getSupportedPreviewFrameRates(Camera.Parameters param) {

		return null;
	}

	/**
	 * @param param
	 * 
	 * @return
	 */
	public List<Camera.Size> getSupportedPreviewSizes(Camera.Parameters param) {

		return param.getSupportedPreviewSizes();
	}

	/**
	 * @param param
	 * 
	 * @return
	 */
	public List<String> getSupportedSceneModes(Camera.Parameters param) {

		return null;
	}

	/**
	 * @param param
	 * 
	 * @return
	 */
	public List<Camera.Size> getSupportedVideoSizes(Camera.Parameters param) {

		return null;
	}

	/**
	 * @param param
	 * 
	 * @return
	 */
	public List<String> getSupportedWhiteBalance(Camera.Parameters param) {

		return null;
	}

	/**
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
	 * Exibe informa��es sobre um arquivo:<br>
	 * <ul>
	 * <li>informa��es sobre a permiss�o de leitura (R), escrita (W) e execu��o
	 * (X) do um arquivo f
	 * <li>informa��es sobre a permiss�o de leitura (R), escrita (W) e execu��o
	 * (X) do diret�rio picsDir
	 * </ul>
	 * 
	 * @param foto
	 *          inst�ncia de um objeto da classe File.
	 * 
	 */
	private void showFile(File foto) {

		Log.d(TAG, "showFile(): ");

		if (foto == null) {
			Log.w(TAG, "arquivo � nulo");
			return;
		}

		Log.d(TAG, "  f.getAbsolutePath=" + foto.getAbsolutePath());
		Log.d(TAG, "  f.getName()=" + foto.getName());

		String canRead = foto.canRead() == true ? "R" : "-";
		String canWrite = foto.canWrite() == true ? "W" : "-";
		String canExecute = foto.canRead() == true ? "X" : "-";

		String permission = canRead + canWrite + canExecute;
		Log.d(TAG, "  Permiss�o do arquivo: " + permission);

		Log.d(TAG, "  canWrite(): " + picsDir.canWrite());
		Log.d(TAG, "  canRead(): " + picsDir.canRead());
		Log.d(TAG, "  canExecute(): " + picsDir.canExecute());

		Log.d(TAG, "  getAbsolutePath()=" + picsDir.getAbsolutePath());

	};

	/**
	 * @param ctx
	 */
	private static void xxx(Context ctx) {

		int numCamerasDisponiveis = Camera.getNumberOfCameras();

		// boolean isCameraAvailable = CameraTools.checkCameraHardware(this);
		boolean isCameraAvailable = CameraTools.checkCameraHardware(ctx);

		if (isCameraAvailable) {

			if (numCamerasDisponiveis == 1) {
				Log.d(TAG, "onCreate() - h� uma c�mera dispon�vel.");
			} else {
				Log.d(TAG, "onCreate() - h�  " + numCamerasDisponiveis + " c�meras dispon�veis.");
			}

		} else {

			Log.e(TAG, "onCreate() - n�o h� c�meras dispon�veis");
			return;

		}

		for (int i = 0; i <= (Camera.getNumberOfCameras() - 1); i++) {

			Log.i(TAG, "onCreate() - verificando o estado da c�mera: " + i);

			boolean isCameraWorking = CameraTools.isCameraWorking(i);

			if (isCameraWorking) {
				Log.d(TAG, "onCreate() - c�mera " + i + " est� funcionando corretamente");
			} else {
				Log.e(TAG, "onCreate() - c�mera " + i + " n�o est� dispon�vel para uso pela aplica��o");
				return;
			}

		}

	}

}
