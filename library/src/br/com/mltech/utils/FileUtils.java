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
 * Métodos estáticos para manipulação de arquivos
 * 
 * @author maurocl
 * 
 */
public class FileUtils {

  public static final String TAG = "FileUtils";

  /**
   * getTimeStamp()
   * 
   * Cria um nome de arquivo baseado na data e hora atual
   * 
   * @return string
   */
  private static String getTimeStamp() {
    
    return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    
  }
  
  /**
   * obtemNomeArquivo(String extensao)
   * 
   * gera um nome de arquivo a partir do numero de mili segundos atuais
   * retornados pelo sistema
   * 
   * extensao=".jpg"
   */
  public static File obtemNomeArquivo(String extensao) {

    // Obtém um nome de arquivo no diretório publico para armazenamento de
    // imagens (pictures)
    String arquivo = CameraTools.getExternalStoragePublicDirectoryPictures() + java.io.File.separator + System.currentTimeMillis()
        + extensao;

    // Cria o arquivo
    File file = new File(arquivo);

    Log.d(TAG, "obtemNomeArquivo() - arquivo=[" + file.getAbsolutePath() + "]");

    // retorna uma instância do File criado
    return file;

  }

  /**
   * obtemNomeArquivo()
   * 
   * @return
   */
  public static File obtemNomeArquivo() {

    // cria o arquivo com o nome da foto
    File file = new File(CameraTools.getExternalStoragePublicDirectoryPictures() + "/" + getTimeStamp() + ".png");

    return file;

  }

  /**
   * 
   * @return
   */
  public static File obtemNomeArquivoPNG() {

    // cria o arquivo com o nome da foto
    File file = new File(CameraTools.getExternalStoragePublicDirectoryPictures() + "/" + getTimeStamp() + ".png");

    return file;

  }

  /**
   * 
   * @return
   */
  public static File obtemNomeArquivoJPEG() {

    // cria o arquivo com o nome da foto
    File file = new File(CameraTools.getExternalStoragePublicDirectoryPictures() + "/" + getTimeStamp() + ".jpg");

    return file;

  }

  
  
  /**
   * getFileExtension(Uri uri)
   * 
   * Obtém a extensão de um arquivo representado por uma Uri cujo schema é file.
   * 
   * @param uri
   *          URI do arquivo
   * 
   * @return
   * 
   */
  public static String getFileExtension(Uri uri) {

    if (isFileURI(uri)) {
      String s = uri.getPath();

      return s.substring(s.lastIndexOf(".") + 1);
    }

    return null;

  }

  /**
   * getFilename(Uri uri)
   * 
   * Obtém o nome do arquivo (sem a extensão) de um arquivo representado por uma
   * Uri cujo schema é file.
   * 
   * @param uri
   *          URI do arquivo
   * 
   * @return o nome do arquivo ou null caso haja algum problema
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
   * isFileURI(Uri uri)
   * 
   * Testa se uma Uri é do tipo (schema) file.
   * 
   * @param uri
   *          URI
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
   * isValidFile(File f)
   * 
   * Verifica se um arquivo existe e é um arquivo
   * 
   * @param f
   * 
   * @return true se o arquivo existir e for um arquivo
   * 
   */
  public static boolean isValidFile(File f) {

    if (f == null) {
      return false;
    }

    if (f.exists() || f.isFile()) {
      return true;
    }

    return false;

  }

  /**
   * isValidDirectory(File f)
   * 
   * Verifica se um diretório existe.
   * 
   * @param f
   *          referência a um diretório no sistema de arquivos
   * 
   * @return true se o diretório existir
   * 
   */
  public static boolean isValidDirectory(File f) {

    if (f == null) {
      return false;
    }

    if (f.exists() || f.isDirectory()) {
      return true;
    }

    return false;

  }

  /**
   * showBundle(Bundle b)
   * 
   * Exibe todas as chaves/valores armazenadas no bundle
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
   * showFile(File f)
   * 
   * Exibe detalhes sobre um arquivo.
   * 
   * Exibe as informações no Log.
   * 
   * @param f
   *          Instância da classe File
   * 
   */
  public static void showFile(File f) {

    Log.v(TAG, "showFile():");

    if (f != null) {
      Log.v(TAG, "  getName()=" + f.getName());
      Log.v(TAG, "  getAbsolutePath()=" + f.getAbsolutePath());
      Log.v(TAG, "  getPath()=" + f.getPath());
    }

  }

  /**
   * ShowFileDetails(File f, String msg)
   * 
   * Exibe informações detalhadas sobre um arquivo
   * 
   * @param f
   *          Instância de um arquivo (File)
   * 
   */
  public static void showFileDetails(File f, String msg) {

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
   * showUri(Uri uri)
   * 
   * Exibe informações sobre uma Uri
   * 
   * @param uri
   */
  public static void showUri(Uri uri) {

    Log.v(TAG, "showUri() - exibe informações sobre uma Uri:");
    Log.v(TAG, "  uri=" + uri);
    Log.v(TAG, "  uri.getAuthority=" + uri.getAuthority());
    Log.v(TAG, "  uri.getHost=" + uri.getHost());
    Log.v(TAG, "  uri.getQuery=" + uri.getQuery());
    Log.v(TAG, "  uri.getPath=" + uri.getPath());
    Log.v(TAG, "  uri.getPort=" + uri.getPort());

  }

}
