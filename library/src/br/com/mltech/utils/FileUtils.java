
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
 * M�todos est�ticos para manipula��o de arquivos.
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

  // � formado pelo diret�rio base (nome da aplica��o) + o diret�rio onde s�o armazenadas as fotos dentro da aplica��o
  public static final String APP_NAME = BASE_DIR + File.separator + PHOTO_DIR;

  /**
   * Retorna uma string com a data e hora no formato yyyyMMdd_HHmmss. � �til
   * para gera��o de nomes de arquivos.
   * 
   * @return string contendo a data e hora formatada como yyyyMMdd_HHmmss
   */
  private static String getTimeStamp() {

    return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

  }

  /**
   * Obt�m o diret�rio b�sico onde s�o armazenadas as fotos no dispositivo.
   * 
   * @return o arquivo onde as fotos s�o armazenadas.
   * 
   */
  public static File getBaseDirectory() {

    File file = CameraTools.getExternalStoragePublicDirectoryPictures();

    return file;

  }

  /**
   * Obt�m o diret�rio onde s�o armazenadas as fotos da aplica��o
   * 
   * @return obt�m o diret�rio onde s�o armazenadas as fotos da aplica��o.
   * 
   */
  public static File getBasePhotoDirectory() {

    String s = CameraTools.getExternalStoragePublicDirectoryPictures().getAbsoluteFile() + File.separator + APP_NAME;

    File file = new File(s);

    return file;

  }

  /**
   * Obt�m o diret�rio onde s�o armazenadas as molduras da aplica��o.
   * 
   * @return O diret�rio onde s�o armazenadas as molduras da aplica��o
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
   * formato .png (sem compress�o).
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
   * Gera um nome de arquivo a partir do n�mero de milisegundos atuais
   * retornados pelo sistema operacional. O arquivo gerado possui a extens�o
   * .jpg
   * 
   * extensao=".jpg"
   */
  public static File obtemNomeArquivo(String extensao) {

    // Obt�m um nome de arquivo no diret�rio publico para armazenamento de
    // imagens (pictures)
    String arquivo = CameraTools
        .getExternalStoragePublicDirectoryPictures()
        + java.io.File.separator + APP_NAME + java.io.File.separator + getTimeStamp() + extensao;

    // Cria o arquivo
    File file = new File(arquivo);

    Log.d(TAG, "obtemNomeArquivo() - arquivo=[" + file.getAbsolutePath()
        + "]");

    // retorna uma inst�ncia do File criado
    return file;

  }

  /**
   * Obt�m o nome de um arquivo no formato .png
   * 
   * @return o nome do arquivo localizado no diret�rio de fotos com a extens�o
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
   * Obt�m o nome de um arquivo com extens�o .jpg
   * 
   * @return @return o nome do arquivo localizado no diret�rio de fotos com a
   *         extens�o .jpg
   */
  public static File obtemNomeArquivoJPEG() {

    // cria o arquivo com o nome da foto
    File file = new File(
        CameraTools.getExternalStoragePublicDirectoryPictures()
            + APP_NAME + File.separator + getTimeStamp() + ".jpg");

    return file;

  }

  /**
   * Obt�m a extens�o do nome do arquivo referenciado por sua Uri (o schema da
   * URI deve ser do tipo file).<br>
   * <br>
   * 
   * <b>Exemplo:</b><br>
   * 
   * <pre>
   * Se a Uri do arquivo for:
   *   file:///mnt/sdcard/Pictures/fotoevento/fotos20120603_130124.png 
   * Ent�o
   *   retorna: png
   * </pre>
   * 
   * @param uri
   *          URI do arquivo
   * 
   * @return uma string contendo a extens�o do arquivo, sem (".")
   * 
   * 
   */
  public static String getFileExtension(Uri uri) {

    if (isFileURI(uri)) {
    	
    	// � uma URI do tipo file
      String s = uri.getPath();

      return s.substring(s.lastIndexOf(".") + 1);
      
    }

    return null;

  }

  /**
   * Obt�m a extens�o do nome do arquivo.
   * 
   * @param f
   *          Nome do arquivo.
   * 
   * @return uma string contendo a extens�o do arquivo, sem (".")
   * 
   */
  public static String getFileExtension(File f) {

    if (f == null) {
    	// a extens�o de um arquivo vazio � nula
      return null;
    }

    // retorna o path do arquivo
    String s = f.getPath();

    return s.substring(s.lastIndexOf(".") + 1);

  }

  /**
   * Obt�m o nome do arquivo referenciado por uma URI. O nome do arquivo voltar�
   * sem a sua extens�o.
   * 
   * @param uri
   *          URI do arquivo
   * 
   * @return o nome do arquivo (sem extens�o) ou null caso haja algum problema.
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
   * Obt�m o nome do arquivo (sem a extens�o).
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
   * @return true em caso schema seja file ou null caso contr�rio
   */
  public static boolean isFileURI(Uri uri) {

    if (uri == null) {
      return false;
    }

    return uri.getScheme().equals("file");

  }

  /**
   * Verifica se um objeto do tipo File existe e � do tipo arquivo.
   * 
   * @param f
   *          Inst�ncia um um objeto File
   * 
   * @return true se o arquivo existir e for um arquivo.
   * 
   */
  public static boolean isValidFile(File f) {

    if (f == null) {
      return false;
    }

    // TODO: verificar se n�o seria && ao inv�s de ||
    if (f.exists() && f.isFile()) {
      return true;
    }

    return false;

  }

  /**
   * Verifica se um objeto do tipo File existe e � do tipo diret�rio.
   * 
   * @param f Inst�ncia um um objeto File
   *          
   * 
   * @return true se o diret�rio existir
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
   * Os dados s�o armazenados no log do sistema.
   * 
   * @param bundle
   *          Inst�ncia da classe Bundle
   * 
   */
  public static void showBundle(Bundle bundle) {

    if (bundle == null) {
      Log.w(TAG, "showBundle() - bundle n�o contem nenhum elemento.");
      return;
    }

    // Obt�m um conjunto de chaves do Bundle
    Set<String> setChaves = bundle.keySet();

    // Obt�m o tamanho do conjunto
    int size = bundle.size();

    // Exibe o n� de elementos do conjunto
    Log.d(TAG, "showBundle() - n� de elementos do bundle: " + size);

    int i = 0;

    for (String chave : setChaves) {
      i++;
      Log.d(TAG, "  " + i + ") " + chave);
    }

  }

  /**
   * Exibe detalhes sobre um arquivo no log da aplica��o.<br>
   * 
   * Se o arquivo for nulo ent�o nenhuma mensagem ser� exibida.
   * 
   * @param file
   *          Inst�ncia da classe File
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
   * Exibe informa��es detalhadas sobre um arquivo
   * 
   * @param f
   *          Inst�ncia de um arquivo (File)
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

  /**
   * Exibe informa��es detalhadas uma Uri.<br>
   * 
   * Se a URI fornecida for nula ent�o nenhuma mensagem ser� exibida.
   * 
   * @param uri
   *          Uri referenciando um objeto.
   */
  public static void showUri(Uri uri) {

    if (uri == null) {
      return;
    }

    Log.v(TAG, "showUri() - exibe informa��es sobre uma Uri:");
    Log.v(TAG, "  uri=" + uri);
    Log.v(TAG, "  uri.getScheme=" + uri.getScheme());
    Log.v(TAG, "  uri.getAuthority=" + uri.getAuthority());
    Log.v(TAG, "  uri.getHost=" + uri.getHost());
    Log.v(TAG, "  uri.getQuery=" + uri.getQuery());
    Log.v(TAG, "  uri.getPath=" + uri.getPath());
    Log.v(TAG, "  uri.getPort=" + uri.getPort());

  }

}
