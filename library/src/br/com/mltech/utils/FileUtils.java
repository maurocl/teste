
package br.com.mltech.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import br.com.mltech.utils.camera.CameraTools;

/**
 * Métodos estáticos para manipulação de arquivos.
 * 
 * @author maurocl
 * 
 */
public class FileUtils {

  public static final String TAG = "FileUtils";

  public static final String BASE_DIR = "fotoevento";

  public static final String PHOTO_DIR = "fotos";

  public static final String MOLDURA_DIR = "molduras";

  public static final String TELAINICIAL_DIR = "telainicial";

  // é formado pelo diretório base (nome da aplicação) + o diretório onde são armazenadas as fotos dentro da aplicação
  public static final String APP_NAME = BASE_DIR + File.separator + PHOTO_DIR;

  /**
   * Retorna uma string com a data e hora no formato yyyyMMdd_HHmmss. É útil
   * para geração de nomes de arquivos.
   * 
   * @return string contendo a data e hora formatada como yyyyMMdd_HHmmss
   */
  private static String getTimeStamp() {

    return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

  }

  /**
   * Obtém o diretório básico onde são armazenadas as fotos no dispositivo.
   * 
   * @return o arquivo onde as fotos são armazenadas.
   * 
   */
  public static File getBaseDirectory() {

    File file = CameraTools.getExternalStoragePublicDirectoryPictures();

    return file;

  }

  /**
   * Obtém o diretório onde são armazenadas as fotos da aplicação
   * 
   * @return obtém o diretório onde são armazenadas as fotos da aplicação.
   * 
   */
  public static File getBasePhotoDirectory() {

    String s = CameraTools.getExternalStoragePublicDirectoryPictures().getAbsoluteFile() + File.separator + APP_NAME;

    File file = new File(s);

    return file;

  }

  /**
   * Obtém o diretório onde são armazenadas as molduras da aplicação.
   * 
   * @return O diretório onde são armazenadas as molduras da aplicação
   * 
   */
  public static File getBaseMolduraDirectory() {

    String s = CameraTools.getExternalStoragePublicDirectoryPictures().getAbsoluteFile() + File.separator + BASE_DIR
        + File.separator + MOLDURA_DIR;

    File file = new File(s);

    return file;

  }

  /**
   * Cria um arquivo (File) para uso na captura de uma foto e armazenamento no
   * formato .png (sem compressão).
   * 
   * @return o arquivo
   */
  public static File obtemNomeArquivo() {

    // cria o arquivo com o nome da foto
    File file = new File(
        CameraTools.getExternalStoragePublicDirectoryPictures() + File.separator + APP_NAME + File.separator +
            getTimeStamp() + ".png");

    return file;

  }

  /**
   * Gera um nome de arquivo a partir do número de milisegundos atuais
   * retornados pelo sistema operacional. O arquivo gerado possui a extensão
   * .jpg
   * 
   * extensao=".jpg"
   */
  public static File obtemNomeArquivo(String extensao) {

    // Obtém um nome de arquivo no diretório publico para armazenamento de
    // imagens (pictures)
    String arquivo = CameraTools
        .getExternalStoragePublicDirectoryPictures()
        + java.io.File.separator + APP_NAME + java.io.File.separator + getTimeStamp() + extensao;

    // Cria o arquivo
    File file = new File(arquivo);

    Log.d(TAG, "obtemNomeArquivo() - arquivo=[" + file.getAbsolutePath()
        + "]");

    // retorna uma instância do File criado
    return file;

  }

  /**
   * Obtém o nome de um arquivo no formato .png
   * 
   * @return o nome do arquivo localizado no diretório de fotos com a extensão
   *         .png
   */
  public static File obtemNomeArquivoPNG() {

    // cria o arquivo com o nome da foto
    File file = new File(
        CameraTools.getExternalStoragePublicDirectoryPictures()
            + APP_NAME + File.separator + getTimeStamp() + ".png");

    return file;

  }

  /**
   * Obtém o nome de um arquivo com extensão .jpg
   * 
   * @return @return o nome do arquivo localizado no diretório de fotos com a
   *         extensão .jpg
   */
  public static File obtemNomeArquivoJPEG() {

    // cria o arquivo com o nome da foto
    File file = new File(
        CameraTools.getExternalStoragePublicDirectoryPictures()
            + APP_NAME + File.separator + getTimeStamp() + ".jpg");

    return file;

  }

  /**
   * Obtém a extensão do nome do arquivo referenciado por sua Uri (o schema da
   * URI deve ser do tipo file).<br>
   * <br>
   * 
   * <b>Exemplo:</b><br>
   * 
   * <pre>
   * Se a Uri do arquivo for:
   *   file:///mnt/sdcard/Pictures/fotoevento/fotos20120603_130124.png 
   * Então
   *   retorna: png
   * </pre>
   * 
   * @param uri
   *          URI do arquivo
   * 
   * @return uma string contendo a extensão do arquivo, sem (".")
   * 
   * 
   */
  public static String getFileExtension(Uri uri) {

    if (isFileURI(uri)) {
    	
    	// é uma URI do tipo file
      String s = uri.getPath();

      return s.substring(s.lastIndexOf(".") + 1);
      
    }

    return null;

  }

  /**
   * Obtém a extensão do nome do arquivo.
   * 
   * @param f
   *          Nome do arquivo.
   * 
   * @return uma string contendo a extensão do arquivo, sem (".")
   * 
   */
  public static String getFileExtension(File f) {

    if (f == null) {
    	// a extensão de um arquivo vazio é nula
      return null;
    }

    // retorna o path do arquivo
    String s = f.getPath();

    return s.substring(s.lastIndexOf(".") + 1);

  }

  /**
   * Obtém o nome do arquivo referenciado por uma URI. O nome do arquivo voltará
   * sem a sua extensão.
   * 
   * @param uri
   *          URI do arquivo
   * 
   * @return o nome do arquivo (sem extensão) ou null caso haja algum problema.
   */
  public static String getFilename(Uri uri) {

    if (isFileURI(uri)) {

      String s = uri.getPath();

      if (s != null) {
        return s.substring(s.lastIndexOf("/") + 1, s.lastIndexOf("."));
      }

    }

    return null;

  }

  /**
   * Obtém o nome do arquivo (sem a extensão).
   * 
   * @param f
   *          Nome completo do arquivo.
   * 
   * @return o nome do arquivo ou null caso haja algum problema
   */
  public static String getFilename(File f) {

    if (f == null) {
      return null;
    }

    String s = f.getPath();

    if (s != null) {
      return s.substring(s.lastIndexOf("/") + 1, s.lastIndexOf("."));
    }

    return null;

  }

  /**
   * Testa se a Uri referencia um objeto do tipo (schema) file.
   * 
   * @param uri
   *          URI do objeto.
   * 
   * @return true em caso schema seja file ou null caso contrário
   */
  public static boolean isFileURI(Uri uri) {

    if (uri == null) {
      return false;
    }

    return uri.getScheme().equals("file");

  }

  /**
   * Verifica se um objeto do tipo File existe e é do tipo arquivo.
   * 
   * @param f
   *          Instância um um objeto File
   * 
   * @return true se o arquivo existir e for um arquivo.
   * 
   */
  public static boolean isValidFile(File f) {

    if (f == null) {
      return false;
    }

    // TODO: verificar se não seria && ao invés de ||
    if (f.exists() && f.isFile()) {
      return true;
    }

    return false;

  }

  /**
   * Verifica se um objeto do tipo File existe e é do tipo diretório.
   * 
   * @param f Instância um um objeto File
   *          
   * 
   * @return true se o diretório existir
   * 
   */
  public static boolean isValidDirectory(File f) {

    if (f == null) {
      return false;
    }

    if (f.exists() && f.isDirectory()) {
      return true;
    }

    return false;

  }

  /**
   * Exibe todas as chaves/valores armazenadas no bundle.<br>
   * Os dados são armazenados no log do sistema.
   * 
   * @param bundle
   *          Instância da classe Bundle
   * 
   */
  public static void showBundle(Bundle bundle) {

    if (bundle == null) {
      Log.w(TAG, "showBundle() - bundle não contem nenhum elemento.");
      return;
    }

    // Obtém um conjunto de chaves do Bundle
    Set<String> setChaves = bundle.keySet();

    // Obtém o tamanho do conjunto
    int size = bundle.size();

    // Exibe o nº de elementos do conjunto
    Log.d(TAG, "showBundle() - nº de elementos do bundle: " + size);

    int i = 0;

    for (String chave : setChaves) {
      i++;
      Log.d(TAG, "  " + i + ") " + chave);
    }

  }

  /**
   * Exibe detalhes sobre um arquivo no log da aplicação.<br>
   * 
   * Se o arquivo for nulo então nenhuma mensagem será exibida.
   * 
   * @param file
   *          Instância da classe File
   * 
   */
  public static void showFile(File file) {

    if (file == null) {
      return;
    }

    Log.v(TAG, "showFile():");

    Log.v(TAG, "  getName()=" + file.getName());
    Log.v(TAG, "  getAbsolutePath()=" + file.getAbsolutePath());
    Log.v(TAG, "  getPath()=" + file.getPath());

  }

  /**
   * Exibe informações detalhadas sobre um arquivo
   * 
   * @param f
   *          Instância de um arquivo (File)
   * 
   */
  public static void showFileDetails(File f, String msg) {

    if (f == null) {
      return;
    }

    Log.v(TAG, "ShowFileDetails() ==> " + msg);

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

  /**
   * Exibe informações detalhadas uma Uri.<br>
   * 
   * Se a URI fornecida for nula então nenhuma mensagem será exibida.
   * 
   * @param uri
   *          Uri referenciando um objeto.
   */
  public static void showUri(Uri uri) {

    if (uri == null) {
      return;
    }

    Log.v(TAG, "showUri() - exibe informações sobre uma Uri:");
    Log.v(TAG, "  uri=" + uri);
    Log.v(TAG, "  uri.getScheme=" + uri.getScheme());
    Log.v(TAG, "  uri.getAuthority=" + uri.getAuthority());
    Log.v(TAG, "  uri.getHost=" + uri.getHost());
    Log.v(TAG, "  uri.getQuery=" + uri.getQuery());
    Log.v(TAG, "  uri.getPath=" + uri.getPath());
    Log.v(TAG, "  uri.getPort=" + uri.getPort());

  }

}
