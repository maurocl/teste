package br.com.mltech.utils.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import android.view.View;
import android.widget.ImageView;

/**
 * CameraTools
 * 
 * @author maurocl
 * 
 */
public class CameraTools {

  public static final String TAG = "CameraTools";

  private static final String JPEG_FILE_PREFIX = "IMG_";
  private static final String JPEG_FILE_SUFFIX = ".jpg";

  private Camera mCamera;
  private String mCurrentPhotoPath;
  private ImageView mImageView;
  private Bitmap mImageBitmap;

  /**
   * CameraTools()
   * 
   * Construtor
   * 
   */
  public CameraTools() {
    super();

    mCamera = null;

  }

  /**
   * checkCameraHardware(Context context)
   * 
   * Verifica se um dispositivo possui uma c�mera.
   * 
   * @param context
   * 
   * @return true caso o dispositivo possua uma c�mera ou false caso contrario.
   * 
   */
  public boolean checkCameraHardware(Context context) {

    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
      // this device has a camera
      return true;

    } else {
      // no c�mera on this device
      return false;
    }

  }

  /**
   * checkCameraFeatures(Context context)
   * 
   * Verifica as features das c�meras
   * 
   * @param context
   *          Contexto da aplica��o
   * 
   * @return true ou false
   */
  public boolean checkCameraFeatures(Context context) {

    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

      Log.d(TAG, "FEATURE_CAMERA");
      // this device has a camera

      if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {
        Log.d(TAG, "FEATURE_CAMERA_AUTOFOCUS");
      }

      if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
        Log.d(TAG, "FEATURE_CAMERA_FLASH");
      }

      if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
        Log.d(TAG, "FEATURE_CAMERA_FRONT");
      }

      return true;

    } else {
      // no c�mera on this device
      Log.w(TAG, "O dispositivo n�o possui c�mera.");
      return false;
    }

  }

  /**
   * createImageFile()
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
   * getNumCameras()
   * 
   * @return Returns the number of physical cameras available on this device.
   */
  public static int getNumCameras() {
    return Camera.getNumberOfCameras();
  }

  /**
   * getCameraInstance()
   * 
   * @return
   */
  public static Camera getCameraInstance() {

    return getCameraInstance(0);

  }

  /**
   * getCameraInstance(int num)
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
        Log.d(TAG, "Error in getCameraInstance()", e);
      }

    } else {
      // n� da c�mera inv�lido (est� fora do intervalo)
      Log.d(TAG, "O n�mero da c�mera: " + num + " est� fora do intervalo de c�meras v�lidas (deve estar entre 0 e "
          + getNumCameras() + ")");
    }

    return c; // return null if camera is unavailable

  }

  /**
   * getParametersFlatten(Camera c)
   * 
   * @param c
   *          Inst�ncia de uma c�mera
   * 
   * @return Uma String com os detalhes da configura��o da c�mera ou null caso a
   *         camera esteja vazia
   * 
   */
  public String getParametersFlatten(Camera c) {

    if (c == null) {
      // n�o h� inst�ncia da c�mera
      return null;
    }

    String flatten = c.getParameters().flatten();
    Log.d(TAG, "params.flatten=" + flatten);

    return flatten;

  }

  /**
   * getTimeStamp()
   * 
   * @return String contendo a data atual no formato: yyyyMMdd_HHmmss
   * 
   */
  private String getTimeStamp() {

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

      // storageDir =
      // mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

      if (storageDir != null) {

        // cria o diret�rio
        if (!storageDir.mkdirs()) {

          if (!storageDir.exists()) {
            // diret�rio j� existente
            Log.d("CameraSample", "failed to create directory");
            return null;
          }

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
  public File getAlbumStorageDir(String albumName) {

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
   * showParametersDetail(Camera c)
   * 
   * Exibe os detalhes dos par�metros de configura��o da c�mera
   * 
   */
  public void showParametersDetail(Camera c) {

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

    mImageBitmap = (Bitmap) extras.get("data");

    mImageView.setImageBitmap(mImageBitmap);

    mImageView.setVisibility(View.VISIBLE);

  }

  /**
   * chooseFileDir()
   * 
   * @return
   */
  private Uri chooseFileDir(String nomeArquivo) {

    if (isExternalStorageMounted() == false) {
      Log.w(TAG, "Error - SD n�o foi montado.");
      return null;
    }

    File fotoDiretorio = new File(Environment.getExternalStorageDirectory(), "fotoevento");

    if (!fotoDiretorio.exists()) {
      Log.d(TAG, "Criando o diret�rio ...");
      fotoDiretorio.mkdirs();
    }

    File fotoArquivo = new File(fotoDiretorio, nomeArquivo);

    Uri fotoUri = null;

    if (fotoArquivo != null) {

      fotoUri = Uri.fromFile(fotoArquivo);

    }

    return fotoUri;

  }

  /**
   * obtemNomeArquivo(String extensao)
   * 
   * gera um nome de arquivo a partir do numero de mili segundos atuais
   * retornados pelo sistema
   * 
   * extensao=".jpg"
   */
  File obtemNomeArquivo(String extensao) {

    // Obt�m um nome de arquivo
    String arquivo = Environment.getExternalStorageDirectory() + File.pathSeparator + +System.currentTimeMillis() + extensao;

    // Cria o arquivo
    File file = new File(arquivo);

    Log.d(TAG, "===> arquivo=" + file.getAbsolutePath());

    return file;

  }

  /**
   * isExternalStorageMounted()
   * 
   * Verifica se h� algum disco externo dispon�vel e montado no dispositivo.
   * 
   * @return true, caso haja ou false, caso contr�rio
   */
  public boolean isExternalStorageMounted() {

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
   * @return true se uma media de armazenamento externo estiver montada ou
   *         false, caso contr�rio.
   * 
   */

  public boolean isExternalMediaMounted() {

    boolean isMounted;

    // Obt�m o estado corrente do principal dispositivo de armazenamento
    // externo
    isMounted = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());

    if (isMounted) {
      // dispositivo est� montado
      Log.d(TAG, "Media externa est� montada.");
    } else {
      // dispositivo n�o n�o est� montado
      Log.w(TAG, "Media externa n�o est� montada.");
    }

    return isMounted;

  }
  
  /**
   * 
   * Retorna o diret�rio solicitado dentro de
   * getExternalStoragePublicDirectoryPictures Cria-o caso ele n�o exista
   * 
   * @param dirName
   * @return
   */
  public File getDir2(String dirName) {

    File storageDir = null;

    boolean b = false;

    if (!isExternalStorageMounted()) {
      Log.w(TAG, "External storage is not mounted READ/WRITE.");
      return null;
    }

    // obtem o diret�rio onde est�o armazenadas as figuras publicas
    // no dispositivo de armazenamento externo
    File path = getExternalStoragePublicDirectoryPictures();

    storageDir = new File(path, dirName);

    if (storageDir != null) {

      // cria o diret�rio formado pelo nome completo

      b = storageDir.mkdirs();

      if (b == false) {

        // diret�rio n�o foi criado
        if (storageDir.exists()) {
          // diret�rio j� existente e por isso n�o foi criado
          Log.w(TAG, "Diret�rio: " + dirName + " j� existe");

        } else {
          // diret�rio ainda n�o existe e n�o pode ser criado.
          Log.w(TAG, "Falha na cria��o do diret�rio: " + dirName);

        }

      } else {
        // diret�rio criado com sucesso pois ele ainda n�o existia
        Log.w(TAG, "Diret�rio criado com sucesso: " + dirName);
      }

    }

    // retorna o diret�rio
    return storageDir;

  }

  /**
   * getStorageDir(String name)
   * 
   * @param name
   *          Nome
   * 
   * @return Um nome de arquivo
   * 
   */
  public File getStorageDir(String name) {

    File f = null;

    String type = Environment.DIRECTORY_PICTURES;

    File dir = Environment.getExternalStoragePublicDirectory(type);

    if (dir != null) {
      f = new File(dir, name);
    }

    return f;

  }

  // ---------------------------------------------------------

  /**
   * getExternalStoragePublicDirectory()
   * 
   * @return O nome do diret�rio p�blico onde s�o armazenada as imagens
   *         (pictures)
   * 
   */
  public File getExternalStoragePublicDirectoryPictures() {

    String type = Environment.DIRECTORY_PICTURES;

    File path = Environment.getExternalStoragePublicDirectory(type);

    return path;

  }

  /**
   * createExternalStoragePublicPicture()
   * 
   * Cria um storage externo p�blico para armazenamento de figuras (pictures)
   * 
   */
  public void createExternalStoragePublicPicture() {

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
   * deleteExternalStoragePublicPicture()
   * 
   * Apaga (ou remove) uma figura de uma �rea de armzenamento externa p�blica
   * 
   */
  void deleteExternalStoragePublicPicture() {

    // Create a path where we will place our picture in the user's
    // public pictures directory and delete the file.
    // If external storage is not currently mounted this will fail.
    File path = getExternalStoragePublicDirectoryPictures();
    File file = new File(path, "DemoPicture.jpg");
    file.delete();
  }

  /**
   * hasExternalStoragePublicPicture()
   * 
   * @return true se o arquivo existir no diret�rio ou false, caso contr�rio.
   * 
   */
  boolean hasExternalStoragePublicPicture(String filename) {

    // Create a path where we will place our picture in the user's
    // public pictures directory and check if the file exists.

    // If external storage is not currently mounted this will think the
    // picture doesn't exist.

    File path = getExternalStoragePublicDirectoryPictures();

    File file = new File(path, filename);

    return file.exists();

  }

  // ---------------------------------------------------------

  /**
   * showUri(Uri uri)
   * 
   * Exibe informa��es sobre uma Uri
   * 
   * @param uri
   */
  public void showUri(Uri uri) {

    Log.v(TAG, "Exibe informa��es sobre uma Uri:");
    Log.v(TAG, "  uri=" + uri);
    Log.v(TAG, "  uri.getAuthority=" + uri.getAuthority());
    Log.v(TAG, "  uri.getHost=" + uri.getHost());
    Log.v(TAG, "  uri.getQuery=" + uri.getQuery());
    Log.v(TAG, "  uri.getPath=" + uri.getPath());
    Log.v(TAG, "  uri.getPort=" + uri.getPort());

  }

  /**
   * showCameraInfo()
   * 
   * Exibe informa��es sobre a configura��o de todas as c�meras
   * 
   */
  public void showCameraInfo() {

    Camera camera = null;

    int num = Camera.getNumberOfCameras();

    Log.d(TAG, "N�mero de c�mera(s) dispon�vel(eis):  " + num);

    for (int i = 0; i < num; i++) {

      camera = getCameraInstance(i);

      if (camera != null) {

        Log.d(TAG, "*** Detalhes de configura��o da c�mera: " + i);
        showParametersDetail(camera);

      } else {

        Log.d(TAG, "C�mera " + i + " n�o est� dispon�vel");

      }

    }

  }

  /**
   * exibeCameraInfo(int cameraId, CameraInfo cameraInfo)
   * 
   * @param cameraId
   * @param cameraInfo
   */
  public void exibeCameraInfo(int cameraId, CameraInfo cameraInfo) {

    Camera.getCameraInfo(cameraId, cameraInfo);

    Log.d(TAG, "cameraInfo.facing=" + cameraInfo.facing);

    Log.d(TAG, "cameraInfo.orientation=" + cameraInfo.orientation);

  }

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
   * ShowFileDetails(File f, String msg)
   * 
   * Exibe informa��es sobre um arquivo
   * 
   * @param f
   *          Inst�ncia de um arquivo (File)
   * 
   */
  public void ShowFileDetails(File f, String msg) {

    Log.v(TAG, " ==> " + msg);

    if (f.exists()) {
      Log.v(TAG, "f=" + f.getAbsolutePath() + " existe");
    } else {
      Log.v(TAG, "f=" + f.getAbsolutePath() + " n�o existe");
    }

    if (f.isDirectory()) {
      Log.v(TAG, "f=" + f.getAbsolutePath() + " � um diret�rio");
    } else {
      Log.v(TAG, "f=" + f.getAbsolutePath() + " n�o � um diret�rio");
    }

    if (f.isFile()) {
      Log.v(TAG, "f=" + f.getAbsolutePath() + " � um arquivo");
    } else {
      Log.v(TAG, "f=" + f.getAbsolutePath() + " n�o � um arquivo");
    }

  }

}
