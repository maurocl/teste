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
   * chooseFileDir()
   * 
   * @return
   */
  private static Uri chooseFileDir(String nomeArquivo) {

    if (isExternalStorageMounted() == false) {
      Log.w(TAG, "chooseFileDir() - Erro - sdcard não foi montado.");
      return null;
    }

    File fotoDiretorio = new File(Environment.getExternalStorageDirectory(), "fotoevento");

    if (!fotoDiretorio.exists()) {
      Log.d(TAG, "chooseFileDir() - criando o diretório ...");
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
   * checkCameraFeatures(Context context)
   * 
   * Verifica as features das câmeras
   * 
   * @param context
   *          Contexto da aplicação
   * 
   * @return true ou false
   */
  public static boolean checkCameraFeatures(Context context) {

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
      Log.w(TAG, "checkCameraFeatures() - O dispositivo não possui câmera.");
      return false;
    }

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
     * Cria um arquivo vazio temporário no diretório dado usando o prefixo e
     * sufixo como parte do nome do arquivo. Se o sufixo for null então o sufixo
     * '.tmp' será usado.
     */
    File image = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, directory);

    return image;

  }

  /**
   * deleteExternalStoragePublicPicture()
   * 
   * Apaga (ou remove) uma imagem da área pública de armzenamento externo
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
   * @param cameraInfo
   * 
   */
  public static void exibeCameraInfo(int cameraId, CameraInfo cameraInfo) {

    Camera.getCameraInfo(cameraId, cameraInfo);

    Log.d(TAG, "exibeCameraInfo() - cameraInfo.facing=" + cameraInfo.facing);

    Log.d(TAG, "exibeCameraInfo() - cameraInfo.orientation=" + cameraInfo.orientation);

  }

  /**
   * getNumCameras()
   * 
   * Retorna o nº de câmeras de um dispositivo
   * 
   * @return Retorna o nº de câmeras físicas disponíveis em um dispositivo
   */
  public static int getNumCameras() {
    return Camera.getNumberOfCameras();
  }

  /**
   * getCameraInstance()
   * 
   * Obtém a instância de uma câmera
   * 
   * @return a primeira instância de câmera disponível no dispositivo
   */
  public static Camera getCameraInstance() {

    return getCameraInstance(0);

  }

  /**
   * getCameraInstance(int num)
   * 
   * Obtém a instância de uma câmera num. A primeira câmera possui o nº 0
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
        Log.d(TAG, "getCameraInstance() - Error in getCameraInstance()", e);
      }

    } else {
      // nº da câmera inválido (está fora do intervalo)
      Log.d(TAG, "getCameraInstance() - O número da câmera: " + num
          + " está fora do intervalo de câmeras válidas (deve estar entre 0 e " + getNumCameras() + ")");
    }

    return c; // return null if camera is unavailable

  }

  /**
   * getParametersFlatten(Camera c)
   * 
   * Obtém os detalhes da configuração de uma câmera em uma única string.
   * 
   * @param c
   *          Instância de uma câmera
   * 
   * @return Uma String com os detalhes da configuração da câmera ou null caso a
   *         camera esteja vazia
   * 
   */
  public static String getParametersFlatten(Camera c) {

    if (c == null) {
      // não há instância da câmera
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
   * @param c
   */
  public static HashMap<String, List<String>> getParametersDetail(Camera c) {

    HashMap<String, List<String>> hash = new HashMap<String, List<String>>();

    ArrayList<String> lista = null;

    if (c == null) {
      return null;
    }

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
   * getDir2(String dirName)
   * 
   * Retorna o diretório solicitado dentro de
   * getExternalStoragePublicDirectoryPictures.
   * Cria-o caso ele não exista
   * 
   * @param dirName Nome do diretório (no sistema de arquivos)
   * 
   * @return uma instância de File referenciando um diretório
   */
  public static File getDir2(String dirName) {

    File storageDir = null;

    boolean b = false;

    if (!isExternalStorageMounted()) {
      Log.w(TAG, "getDir2() - diretório não poderá ser armazenamento pois o dispositivo externo não está montado para leitura/escrita.");
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
      b = storageDir.mkdirs();

      if (b == false) {

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
        Log.w(TAG, "getDir2() - Diretório: "+dirName+" criado com sucesso: ");
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
  public static File getStorageDir(String name) {

    File f = null;

    String type = Environment.DIRECTORY_PICTURES;

    /*
     * Get a top-level public external storage directory for placing files of a
     * particular type. This is where the user will typically place and manage
     * their own files, so you should be careful about what you put here to
     * ensure you don't erase their files or get in the way of their own
     * organization.
     */
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
   * hasExternalStoragePublicPicture()
   * 
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
   * isExternalStorageMounted()
   * 
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
   * isExternalMediaMounted()
   * 
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
      Log.d(TAG, "Media externa está montada.");
    } else {
      // dispositivo não não está montado
      Log.w(TAG, "Media externa não está montada.");
    }

    return isMounted;

  }

  /**
   * createExternalStoragePublicPicture()
   * 
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
   * showHash(HashMap<String, List<String>> hash)
   * 
   * Exibe os dados de um HashMap onde a chave é uma string e o valor é uma
   * lista de strings
   * 
   * @param hash
   *          instância do HashMap
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
   * Exibe informações sobre a configuração de todas as câmeras
   * 
   */
  public static void showCameraInfo() {

    Camera camera = null;

    int num = Camera.getNumberOfCameras();

    Log.d(TAG, "showCameraInfo() - número de câmera(s) disponível(eis):  " + num);

    for (int i = 0; i < num; i++) {

      camera = getCameraInstance(i);

      if (camera != null) {

        Log.d(TAG, "showCameraInfo() - *** Detalhes de configuração da câmera: " + i);
        showParametersDetail(camera);

      } else {

        Log.d(TAG, "showCameraInfo() - câmera " + i + " não está disponível");

      }

    }

  }

  /**
   * showParametersDetail(Camera c)
   * 
   * Exibe os detalhes dos parâmetros de configuração da câmera
   * 
   */
  public static void showParametersDetail(Camera c) {

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

}
