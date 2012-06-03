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
 * M�todos est�ticos para manipula��o de arquivos
 * 
 * @author maurocl
 * 
 */
public class FileUtils {

	public static final String TAG = "FileUtils";

	public static final String APP_NAME = "/fotoevento/fotos";

	/**
	 * getTimeStamp()
	 * 
	 * Retorna uma string com a data e hora no formato yyyyMMdd_HHmmss. � �til
	 * para gera��o de nomes de arquivos.
	 * 
	 * @return string contendo a data e hora formatada como yyyyMMdd_HHmmss
	 */
	private static String getTimeStamp() {

		return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

	}

	/**
	 * obtemNomeArquivo()
	 * 
	 * @return
	 */
	public static File obtemNomeArquivo() {

		// cria o arquivo com o nome da foto
		File file = new File(
				CameraTools.getExternalStoragePublicDirectoryPictures() + File.separator+ APP_NAME +File.separator+
						getTimeStamp() + ".png");

		return file;

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

		// Obt�m um nome de arquivo no diret�rio publico para armazenamento de
		// imagens (pictures)
		String arquivo = CameraTools
				.getExternalStoragePublicDirectoryPictures()
				+ java.io.File.separator + APP_NAME+ java.io.File.separator + getTimeStamp() + extensao;

		// Cria o arquivo
		File file = new File(arquivo);

		Log.d(TAG, "obtemNomeArquivo() - arquivo=[" + file.getAbsolutePath()
				+ "]");

		// retorna uma inst�ncia do File criado
		return file;

	}

	/**
	 * obtemNomeArquivoPNG()
	 * 
	 * @return
	 */
	public static File obtemNomeArquivoPNG() {

		// cria o arquivo com o nome da foto
		File file = new File(
				CameraTools.getExternalStoragePublicDirectoryPictures()
						+ APP_NAME + getTimeStamp() + ".png");

		return file;

	}

	/**
	 * obtemNomeArquivoJPEG()
	 * 
	 * @return
	 */
	public static File obtemNomeArquivoJPEG() {

		// cria o arquivo com o nome da foto
		File file = new File(
				CameraTools.getExternalStoragePublicDirectoryPictures()
						+ APP_NAME + getTimeStamp() + ".jpg");

		return file;

	}

	/**
	 * getFileExtension(Uri uri)
	 * 
	 * Obt�m a extens�o de um arquivo representado por uma Uri cujo schema �
	 * file.
	 * 
	 * Se a Uri do arquivo for:
	 * file:///mnt/sdcard/Pictures/fotoevento/fotos20120603_130124.png Ent�o
	 * retorna: png
	 * 
	 * @param uri
	 *            URI do arquivo
	 * 
	 * @return uma string contendo a extens�o do arquivo, sem (".")
	 * 
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
	 * getFileExtension(File f)
	 * 
	 * Obt�m a extens�o de um arquivo.
	 *   
	 * @param f
	 *           
	 * @return uma string contendo a extens�o do arquivo, sem (".")
	 * 
	 */
	public static String getFileExtension(File f) {

		if (f == null) {
			return null;
		}

		String s = f.getPath();

		return s.substring(s.lastIndexOf(".") + 1);

	}

	/**
	 * getFilename(Uri uri)
	 * 
	 * Obt�m o nome do arquivo (sem a extens�o) de um arquivo representado por
	 * uma Uri cujo schema � file.
	 * 
	 * @param uri
	 *            URI do arquivo
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
	 * getFilename(File f)
	 * 
	 * Obt�m o nome do arquivo (sem a extens�o) 
	 * 
	 * @param f
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
	 * isFileURI(Uri uri)
	 * 
	 * Testa se uma Uri � do tipo (schema) file.
	 * 
	 * @param uri
	 *            URI
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
	 * isValidFile(File f)
	 * 
	 * Verifica se um arquivo existe e � um arquivo
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
	 * Verifica se um diret�rio existe.
	 * 
	 * @param f
	 *            refer�ncia a um diret�rio no sistema de arquivos
	 * 
	 * @return true se o diret�rio existir
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
	 *            Inst�ncia da classe Bundle
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
	 * showFile(File f)
	 * 
	 * Exibe detalhes sobre um arquivo.
	 * 
	 * Exibe as informa��es no Log.
	 * 
	 * @param f
	 *            Inst�ncia da classe File
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
	 * Exibe informa��es detalhadas sobre um arquivo
	 * 
	 * @param f
	 *            Inst�ncia de um arquivo (File)
	 * 
	 */
	public static void showFileDetails(File f, String msg) {

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
	 * showUri(Uri uri)
	 * 
	 * Exibe informa��es sobre uma Uri
	 * 
	 * @param uri
	 */
	public static void showUri(Uri uri) {

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
