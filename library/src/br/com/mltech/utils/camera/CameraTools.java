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

	// diretório (onde as fotos serão armazenadas)
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
	 * Verifica se um dispositivo possui uma câmera.
	 * 
	 * @param context
	 *          Contexto da aplicação.
	 * 
	 * @return true caso o dispositivo possua uma câmera ou false caso contrario.
	 * 
	 */
	public static boolean checkCameraHardware(Context context) {

		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;

		} else {
			// no câmera on this device
			return false;
		}

	}

	/**
	 * @return a Uri de um arquivo
	 */
	private static Uri chooseFileDir(String nomeArquivo) {

		if (isExternalStorageMounted() == false) {
			Log.w(TAG, "chooseFileDir() - Erro - sdcard não foi montado.");
			return null;
		}

		// cria o nome de um diretório
		File fotoDiretorio = new File(Environment.getExternalStorageDirectory(), "fotoevento");

		if (!fotoDiretorio.exists()) {
			// diretório não existe
			Log.d(TAG, "chooseFileDir() - criando o diretório ...");
			// cria o diretório
			boolean b = fotoDiretorio.mkdirs();
			if (b) {
				Log.d(TAG, "chooseFileDir() - diretório criado com sucesso");
			} else {
				Log.w(TAG, "chooseFileDir() - falha na criação do diretório");
			}
		}

		// cria um arquivo em um diretório
		File fotoArquivo = new File(fotoDiretorio, nomeArquivo);

		Uri fotoUri = null;

		if (fotoArquivo != null) {

			fotoUri = Uri.fromFile(fotoArquivo);

		}

		return fotoUri;

	}

	/**
	 * Verifica as features básicas das câmeras.
	 * 
	 * @param context
	 *          Contexto da aplicação
	 * 
	 * @return true ou false
	 */
	public static boolean checkCameraFeatures(Context context) {

		if (checkCameraHardware(context) == false) {
			// no câmera on this device
			Log.w(TAG, "checkCameraFeatures() - O dispositivo não possui câmera.");
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
		 * Cria um arquivo vazio temporário no diretório dado usando o prefixo e
		 * sufixo como parte do nome do arquivo. Se o sufixo for null então o sufixo
		 * '.tmp' será usado.
		 */
		File image = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, directory);

		return image;

	}

	/**
	 * Apaga (ou remove) uma imagem da área pública de armazenamento externo.
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
	 * Exibe informações (a direção e a orientação) sobre a câmera selecionada.
	 * 
	 * @param cameraId
	 *          Identificador da câmera
	 * 
	 * @param cameraInfo
	 *          Informações sobre a câmera
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
	 * Retorna o nº de câmeras de um dispositivo.<br>
	 * 
	 * @return Retorna o nº de câmeras físicas disponíveis em um dispositivo.
	 * 
	 */
	public static int getNumCameras() {

		return Camera.getNumberOfCameras();
	}

	/**
	 * Obtém a instância da câmera padrão (Id=0).<br>
	 * 
	 * @return a instância da câmera padrão do dispositivo (se houver) ou null
	 *         caso haja algum problema.
	 * 
	 */
	public static Camera getCameraInstance() {

		return getCameraInstance(0);

	}

	/**
	 * Obtém a instância da câmera dado seu Id. A câmera traseira possui Id igual
	 * a 0.<br>
	 * 
	 * @param cameraId
	 *          identificador da câmera
	 * 
	 * @return a instância da câmera solicitada ou null caso haja algum problema
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
			// nº da câmera inválido (está fora do intervalo)
			Log.d(TAG, "getCameraInstance() - O número da câmera: " + cameraId
					+ " está fora do intervalo de câmeras válidas (deve estar entre 0 e " + getNumCameras() + ")");
		}

		// return null if camera is unavailable
		return c;

	}

	/**
	 * Obtém os detalhes da configuração de uma câmera em uma única string.<br>
	 * 
	 * @param c
	 *          Instância de uma câmera<br>
	 * 
	 * @return Uma String com os detalhes da configuração da câmera ou null caso a
	 *         camera esteja vazia<br>
	 * 
	 */
	public static String getParametersFlatten(Camera c) {

		if (c == null) {
			// não há instância da câmera
			Log.d(TAG, "getParametersFlatten() - câmera é nula");
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
	 * Obtém o diretório onde ...
	 * 
	 * @return
	 */
	private File getAlbumDir() {

		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

			// cria o diretório
			if (!storageDir.mkdirs()) {

				if (!storageDir.exists()) {
					// diretório já existente
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
	 * Cria uma estrutura de dados (HashMap) com todos os parâmetros de
	 * configuração de um câmera.<br>
	 * 
	 * @param c
	 *          Instância de uma câmera
	 * 
	 * @return um HashMap onde cada chave é um parâmetro e cada valor é uma lista
	 *         dos valores possível do parâmetro. Retorna null em caso de erro.
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

		// O método flatten retorna uma String

		// Creates a single string with all the parameters set in this
		// Camera.Parameters object.
		// flattened a String of parameters (key-value paired) that are
		// semi-colon delimited

		String flatten = params.flatten();

		// Divide a string contendo os parâmetros separados por ";"
		String[] lines = flatten.split(";");

		if (lines == null) {
			return null;
		}

		for (String line : lines) {

			// divide um parâmetro em pares do tipo key-value paired
			String[] chavesValores = line.split("=");

			// a chave é representada pelo lado esquerdo da atribuição
			// o valor é representado pelo lado direito da atribuição
			String chave = null, valor = null;

			// os múltiplos valores das chaves (se existirem) são separados
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
	 * Retorna o diretório solicitado dentro de
	 * getExternalStoragePublicDirectoryPictures.<br>
	 * Cria-o caso ele não exista.<br>
	 * 
	 * @param dirName
	 *          Nome do diretório (no sistema de arquivos)
	 * 
	 * @return uma instância de File referenciando um diretório ou null em caso de
	 *         algum erro.
	 */
	public static File getDir2(String dirName) {

		File storageDir = null;

		boolean diretorioCriado = false;

		if (!isExternalStorageMounted()) {
			Log.w(TAG,
					"getDir2() - diretório não poderá ser armazenamento pois o dispositivo externo não está montado para leitura/escrita.");
			return null;
		}

		// obtem o (caminho do) diretório onde estão armazenadas as figuras publicas
		// no dispositivo de armazenamento externo
		File path = getExternalStoragePublicDirectoryPictures();

		// Referencia o diretório
		storageDir = new File(path, dirName);

		if (storageDir != null) {

			// cria o diretório formado pelo nome completo

			// cria o diretório (e sua arvore de subdiretórios) se necessário
			diretorioCriado = storageDir.mkdirs();

			if (diretorioCriado == false) {

				// diretório não foi criado
				if (storageDir.exists()) {

					// diretório já existente e por isso não foi criado
					Log.w(TAG, "getDir2() - Diretório: " + dirName + " já existe.");

				} else {

					// diretório ainda não existe porém não pode ser criado.
					Log.w(TAG, "getDir2() - Falha na criação do diretório: " + dirName);

				}

			} else {
				// diretório criado com sucesso pois ele ainda não existia
				Log.w(TAG, "getDir2() - Diretório: " + dirName + " criado com sucesso: ");
			}

		}

		// retorna o diretório
		return storageDir;

	}

	/**
	 * Cria um arquivo dentro do diretório público para armazenamento de dados no
	 * dispositivo de armazenamento externo.
	 * 
	 * @param name
	 *          Nome do diretório
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
	 * Obtém o nome do diretório público onde estão armazenadas as imagens
	 * 
	 * @return O nome do diretório público onde são armazenada as imagens
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
	 * @return true se o arquivo existir no diretório ou false, caso contrário.
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
	 * Verifica se há algum disco externo disponível e montado no dispositivo.
	 * 
	 * @return true, caso haja ou false, caso contrário
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
	 *         false, caso contrário.
	 * 
	 */
	public static boolean isExternalMediaMounted() {

		boolean isMounted;

		// Obtém o estado corrente do principal dispositivo de armazenamento
		// externo
		isMounted = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());

		if (isMounted) {
			// dispositivo está montado
			Log.d(TAG, "isExternalMediaMounted() - Media externa está montada.");
		} else {
			// dispositivo não não está montado
			Log.w(TAG, "isExternalMediaMounted() - Media externa não está montada.");
		}

		return isMounted;

	}

	/**
	 * Verifica se a câmera identificada por cameraID está em funcionamento.<br>
	 * Para fazer essa verificação tentamos abrir a câmera. Caso ela consiga ser
	 * aberta indica que a câmera está disponível e nesse caso simplesmente
	 * liberamos a câmera para uso. Se houver erro indica que a câmera não está
	 * disponível para uso pela aplicação.
	 * 
	 * @param cameraID
	 *          Identificador da câmera do dispositivo
	 * 
	 * @return true se for possível obter ima instância da classe Camera ou false,
	 *         caso contrário.
	 */
	public static boolean isCameraWorking(int cameraID) {

		Camera c = getCameraInstance(cameraID);

		if (c == null) {

			// câmera é nula
			Log.i(TAG, "isCameraWorking() - Câmera: " + cameraID + " não está disponível para uso pela aplicação");

			return false;

		}

		// câmera ID foi instanciada com sucesso.
		// Então, desconecta e libera os recursos usados pela câmera.
		c.release();

		c = null;

		Log.i(TAG, "isCameraWorking() - câmera: " + cameraID + " está funcionando corretamente");

		return true;

	}

	/**
	 * Cria um storage externo público para armazenamento de figuras (pictures)
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
	 * @param id Identificador da câmera
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

		/* Obtém o tamanho da imagem (Get the size of the image) */
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
	 * Exibe os dados de um HashMap onde a chave é uma string e o valor é uma
	 * lista de strings.
	 * 
	 * @param hash
	 *          instância do HashMap
	 * 
	 */
	private static void showHash(HashMap<String, List<String>> hash) {

		int num = 0;

		// para cada chave ...
		for (String chave : hash.keySet()) {

			// incrementa o nº de chaves
			num++;

			// obtem a lista de strings associada a chave
			List<String> listaValues = hash.get(chave);

			// Loga o nome da chave e seu nº de sequencia associado
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
	 * Exibe a configuração de todas as câmeras disponíveis no dispositivo no log
	 * da aplicação.
	 * 
	 */
	public static void showCameraInfo() {

		Camera camera = null;

		// Obtém o nº de câmeras do dispositivo.
		int num = Camera.getNumberOfCameras();

		Log.d(TAG, "showCameraInfo() - número de câmera(s) disponível(eis):  " + num);

		// percorre todas as câmeras disponíveis.
		for (int i = 0; i < num; i++) {

			// obtém a instância de uma câmera
			camera = getCameraInstance(i);

			if (camera != null) {

				// câmera retornada
				Log.d(TAG, "showCameraInfo() - *** Detalhes de configuração da câmera: " + i);
				showParametersDetail(camera);

			} else {

				// câmera não está disponível
				Log.d(TAG, "showCameraInfo() - câmera " + i + " não está disponível");

			}

		}

	}

	/**
	 * Exibe os detalhes dos parâmetros de configuração da câmera.<br>
	 * As informações serão exibidad no log.
	 * 
	 * @param c
	 *          Instância da classe Camera.
	 * 
	 */
	public static void showParametersDetail(Camera c) {

		int index = 0;

		// obtém os parametros de configuração da Camera.
		Parameters params = c.getParameters();

		// O método flatten retorna uma String
		// Creates a single string with all the parameters set in this
		// Camera.Parameters object.
		// flattened a String of parameters (key-value paired) that are
		// semi-colon
		// delimited

		if (params != null) {

			Log.d(TAG, "flatten=" + params.flatten());

			String flatten = params.flatten();

			// Divide a string contendo os parâmetros separados por ";"
			String[] lines = flatten.split(";");

			if (lines != null) {

				Log.d(TAG, "Nº de linhas: " + lines.length);

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
			Log.w(TAG, "flatten é null");
		}

	}

	/**
	 * Recebe uma linha que possui informações sobre a configuração da máquina.
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

			// divide um parâmetro em pares do tipo key-value paired
			String[] chaveValor = line.split("=");

			// a chave é representada pelo lado esquerdo da atribuição
			String chave = null;

			// o valor é representado pelo lado direito da atribuição
			String valor = null;

			if (chaveValor != null) {

				if (chaveValor.length == 2) {

					chave = chaveValor[0];
					valor = chaveValor[1];

					// os múltiplos valores das chaves (se existirem) são separados por
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
	 * Esse método foi tirado do site developer.android.com (Reference)<br>
	 * 
	 * @param activity
	 * @param cameraId Identificador da câmera
	 * @param camera
	 * 
	 */
	public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {

		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();

		android.hardware.Camera.getCameraInfo(cameraId, info);

		// obtem a rotação do display default
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
	 *          Instância da classe Camera.Size
	 * 
	 * @return uma string contendo a largura x altura da câmera
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
	 * Obtém uma lista de strings contendo os efeitos de cores suportados pela
	 * câmera
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
	 * Obtém uma lista de strings contendo os modos de flash suportados
	 * 
	 * @param param
	 * 
	 * @return Um array de strings
	 */
	public List<String> getSupportedFlashModes(Camera.Parameters param) {

		return null;
	}

	/**
	 * Obtém uma lista de strings contendo os modos de focos suportados
	 * 
	 * @param param
	 * 
	 * @return
	 */
	public List<String> getSupportedFocusModes(Camera.Parameters param) {

		return null;
	}

	/**
	 * Obtém uma lista de strings contendo os tamanhos de thumbnails suportados
	 * 
	 * @param param
	 * 
	 * @return
	 */
	public List<Camera.Size> getSupportedJpegThumbnailSizes(Camera.Parameters param) {

		return null;
	}

	/**
	 * Obtém uma lista de strings contendo os formatos de imagens suportados
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
	 * Obtém uma lista de strings contendo os formatos de preview suportados
	 * 
	 * @param param
	 * 
	 * @return
	 */
	public List<Integer> getSupportedPreviewFormats(Camera.Parameters param) {

		return null;
	}

	/**
	 * Obtém uma lista de strings contendo as faixa de valores de fps para captura
	 * de imagens que são suportados
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
	 * Prepara o diretório onde as fotos serão armazenadas
	 * 
	 */
	private void preparaDiretorioGravarFotos() {

		boolean isDirCreated = false;

		// obtém o local onde as fotos são armazenas na mem´ria externa do
		// dispositivo (geralmente o SD Card)
		picsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/fotoevento/");

		Log.d(TAG, "preparaDiretorioGravarFotos() - picDir.absolutePath=" + picsDir.getAbsolutePath());
		Log.d(TAG, "preparaDiretorioGravarFotos() - picDir.name=" + picsDir.getName());

		// Make sure the Pictures directory exists.
		// Garante a existência do diretório
		isDirCreated = picsDir.mkdirs();

		if (isDirCreated) {

			// diretório criado com sucesso
			showFile(picsDir);

		} else {

			if (picsDir.exists()) {
				Log.w(TAG, "preparaDiretorioGravarFotos() - Não foi possível criar o diretório: " + picsDir.getName()
						+ " pois ele já existe !");
			} else {
				//
				Log.w(TAG, "preparaDiretorioGravarFotos() - Erro - não foi possivel criar o diretório: " + picsDir.getName());
			}

		}

	}

	/**
	 * Exibe informações sobre um arquivo:<br>
	 * <ul>
	 * <li>informações sobre a permissão de leitura (R), escrita (W) e execução
	 * (X) do um arquivo f
	 * <li>informações sobre a permissão de leitura (R), escrita (W) e execução
	 * (X) do diretório picsDir
	 * </ul>
	 * 
	 * @param foto
	 *          instância de um objeto da classe File.
	 * 
	 */
	private void showFile(File foto) {

		Log.d(TAG, "showFile(): ");

		if (foto == null) {
			Log.w(TAG, "arquivo é nulo");
			return;
		}

		Log.d(TAG, "  f.getAbsolutePath=" + foto.getAbsolutePath());
		Log.d(TAG, "  f.getName()=" + foto.getName());

		String canRead = foto.canRead() == true ? "R" : "-";
		String canWrite = foto.canWrite() == true ? "W" : "-";
		String canExecute = foto.canRead() == true ? "X" : "-";

		String permission = canRead + canWrite + canExecute;
		Log.d(TAG, "  Permissão do arquivo: " + permission);

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
				Log.d(TAG, "onCreate() - há uma câmera disponível.");
			} else {
				Log.d(TAG, "onCreate() - há  " + numCamerasDisponiveis + " câmeras disponíveis.");
			}

		} else {

			Log.e(TAG, "onCreate() - não há câmeras disponíveis");
			return;

		}

		for (int i = 0; i <= (Camera.getNumberOfCameras() - 1); i++) {

			Log.i(TAG, "onCreate() - verificando o estado da câmera: " + i);

			boolean isCameraWorking = CameraTools.isCameraWorking(i);

			if (isCameraWorking) {
				Log.d(TAG, "onCreate() - câmera " + i + " está funcionando corretamente");
			} else {
				Log.e(TAG, "onCreate() - câmera " + i + " não está disponível para uso pela aplicação");
				return;
			}

		}

	}

}
