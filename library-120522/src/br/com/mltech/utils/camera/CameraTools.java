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
   * Verifica se um dispositivo possui uma câmera.
   * 
   * @param context
   * 
   * @return true caso o dispositivo possua uma câmera ou false caso contrario.
   * 
   */
  public boolean checkCameraHardware(Context context) {

    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
      // this device has a camera
      return true;

    } else {
      // no câmera on this device
      return false;
    }

  }

  /**
   * checkCameraFeatures(Context context)
   * 
   * Verifica as features das câmeras
   * 
   * @param context
   *          Contexto da aplicação
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
      // no câmera on this device
      Log.w(TAG, "O dispositivo não possui câmera.");
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
     * Cria um arquivo vazio temporário no diretório dado usando o prefixo e
     * sufixo como parte do nome do arquivo. Se o sufixo for null então o sufixo
     * '.tmp' será usado.
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
   *          Número identificador da câmera
   * 
   * @return a instância de uma câmera solicitada ou null caso haja algum
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
      // nº da câmera inválido (está fora do intervalo)
      Log.d(TAG, "O número da câmera: " + num + " está fora do intervalo de câmeras válidas (deve estar entre 0 e "
          + getNumCameras() + ")");
    }

    return c; // return null if camera is unavailable

  }

  /**
   * getParametersFlatten(Camera c)
   * 
   * @param c
   *          Instância de uma câmera
   * 
   * @return Uma String com os detalhes da configuração da câmera ou null caso a
   *         camera esteja vazia
   * 
   */
  public String getParametersFlatten(Camera c) {

    if (c == null) {
      // não há instância da câmera
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
   * Obtém o diretório onde ...
   * 
   * @return
   */
  private File getAlbumDir() {

    File storageDir = null;

    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

      // storageDir =
      // mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

      if (storageDir != null) {

        // cria o diretório
        if (!storageDir.mkdirs()) {

          if (!storageDir.exists()) {
            // diretório já existente
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
   * Exibe os detalhes dos parâmetros de configuração da câmera
   * 
   */
  public void showParametersDetail(Camera c) {

    Parameters params = c.getParameters();

    // O método flatten retorna uma String
    // Creates a single string with all the parameters set in this
    // Camera.Parameters object.
    // flattened a String of parameters (key-value paired) that are
    // semi-colon
    // delimited

    Log.d(TAG, "flatten=" + params.flatten());

    String flatten = params.flatten();

    // Divide a string contendo os parâmetros separados por ";"
    String[] lines = flatten.split(";");

    int index = 0;
    if (lines != null) {

      Log.d(TAG, "Nº de linhas: " + lines.length);
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
      Log.w(TAG, "Error - SD não foi montado.");
      return null;
    }

    File fotoDiretorio = new File(Environment.getExternalStorageDirectory(), "fotoevento");

    if (!fotoDiretorio.exists()) {
      Log.d(TAG, "Criando o diretório ...");
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

    // Obtém um nome de arquivo
    String arquivo = Environment.getExternalStorageDirectory() + File.pathSeparator + +System.currentTimeMillis() + extensao;

    // Cria o arquivo
    File file = new File(arquivo);

    Log.d(TAG, "===> arquivo=" + file.getAbsolutePath());

    return file;

  }

  /**
   * isExternalStorageMounted()
   * 
   * Verifica se há algum disco externo disponível e montado no dispositivo.
   * 
   * @return true, caso haja ou false, caso contrário
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
   *         false, caso contrário.
   * 
   */

  public boolean isExternalMediaMounted() {

    boolean isMounted;

    // Obtém o estado corrente do principal dispositivo de armazenamento
    // externo
    isMounted = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());

    if (isMounted) {
      // dispositivo está montado
      Log.d(TAG, "Media externa está montada.");
    } else {
      // dispositivo não não está montado
      Log.w(TAG, "Media externa não está montada.");
    }

    return isMounted;

  }
  
  /**
   * 
   * Retorna o diretório solicitado dentro de
   * getExternalStoragePublicDirectoryPictures Cria-o caso ele não exista
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

    // obtem o diretório onde estão armazenadas as figuras publicas
    // no dispositivo de armazenamento externo
    File path = getExternalStoragePublicDirectoryPictures();

    storageDir = new File(path, dirName);

    if (storageDir != null) {

      // cria o diretório formado pelo nome completo

      b = storageDir.mkdirs();

      if (b == false) {

        // diretório não foi criado
        if (storageDir.exists()) {
          // diretório já existente e por isso não foi criado
          Log.w(TAG, "Diretório: " + dirName + " já existe");

        } else {
          // diretório ainda não existe e não pode ser criado.
          Log.w(TAG, "Falha na criação do diretório: " + dirName);

        }

      } else {
        // diretório criado com sucesso pois ele ainda não existia
        Log.w(TAG, "Diretório criado com sucesso: " + dirName);
      }

    }

    // retorna o diretório
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
   * @return O nome do diretório público onde são armazenada as imagens
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
   * Cria um storage externo público para armazenamento de figuras (pictures)
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
   * Apaga (ou remove) uma figura de uma área de armzenamento externa pública
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
   * @return true se o arquivo existir no diretório ou false, caso contrário.
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
   * Exibe informações sobre uma Uri
   * 
   * @param uri
   */
  public void showUri(Uri uri) {

    Log.v(TAG, "Exibe informações sobre uma Uri:");
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
   * Exibe informações sobre a configuração de todas as câmeras
   * 
   */
  public void showCameraInfo() {

    Camera camera = null;

    int num = Camera.getNumberOfCameras();

    Log.d(TAG, "Número de câmera(s) disponível(eis):  " + num);

    for (int i = 0; i < num; i++) {

      camera = getCameraInstance(i);

      if (camera != null) {

        Log.d(TAG, "*** Detalhes de configuração da câmera: " + i);
        showParametersDetail(camera);

      } else {

        Log.d(TAG, "Câmera " + i + " não está disponível");

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
   * ShowFileDetails(File f, String msg)
   * 
   * Exibe informações sobre um arquivo
   * 
   * @param f
   *          Instância de um arquivo (File)
   * 
   */
  public void ShowFileDetails(File f, String msg) {

    Log.v(TAG, " ==> " + msg);

    if (f.exists()) {
      Log.v(TAG, "f=" + f.getAbsolutePath() + " existe");
    } else {
      Log.v(TAG, "f=" + f.getAbsolutePath() + " não existe");
    }

    if (f.isDirectory()) {
      Log.v(TAG, "f=" + f.getAbsolutePath() + " é um diretório");
    } else {
      Log.v(TAG, "f=" + f.getAbsolutePath() + " não é um diretório");
    }

    if (f.isFile()) {
      Log.v(TAG, "f=" + f.getAbsolutePath() + " é um arquivo");
    } else {
      Log.v(TAG, "f=" + f.getAbsolutePath() + " não é um arquivo");
    }

  }

}
