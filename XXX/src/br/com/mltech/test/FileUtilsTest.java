package br.com.mltech.test;

import java.io.File;

import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;
import br.com.mltech.utils.FileUtils;

public class FileUtilsTest extends AndroidTestCase {

	public static final String TAG = "FileUtilsTest";

	/**
	 * afirma que o objeto File é criado porém não ainda no sistema de arquivos
	 */
	public void test1() {

		File f = FileUtils.obtemNomeArquivo();
		assertNotNull(f);

		Log.d(TAG, "f=" + f);

		assertEquals("f não existe", false, f.exists());

	}

	
	/**
	 * 
	 */
	public void test1a() {

		File f = FileUtils.obtemNomeArquivo(".png");
		assertNotNull(f);

		Log.d(TAG, "f=" + f);

		assertEquals("f não existe", false, f.exists());

		Uri uri = Uri.fromFile(f);

		Log.d(TAG,"test1a() - Uri: "+uri);
		Log.d(TAG,"test1a() - extensao: "+FileUtils.getFileExtension(uri));
		
		assertEquals("extensao não é .png","png", FileUtils.getFileExtension(uri));

	}

	/**
	 * 
	 */
	public void test1a1() {

		File f = FileUtils.obtemNomeArquivo(".png");
		assertNotNull(f);

		Log.d(TAG, "f=" + f);

		assertEquals("f não existe", false, f.exists());

		Log.d(TAG,"test1a1() - f: "+f);
		Log.d(TAG,"test1a1() - extensao: "+FileUtils.getFileExtension(f));
		
		assertEquals("extensao não é .png","png", FileUtils.getFileExtension(f));

	}

	/**
	 * Afirma que uma objeto File nulo possui a extensão nula
	 */
	public void test1a0() {

		File f = null;
		assertNull(f);

		Log.d(TAG, "f=" + f);

		Log.d(TAG,"test1a0() - extensao: "+FileUtils.getFileExtension(f));
		
		assertNull("extensao não é nula",FileUtils.getFileExtension(f));

	}

	
	
	/**
	 * 
	 */
	public void test1b() {

		File f = FileUtils.obtemNomeArquivo(".png");
		assertNotNull(f);

		Log.d(TAG, "f=" + f);

		assertEquals("f não existe", false, f.exists());

		Uri uri = Uri.fromFile(f);

		Log.d(TAG,"test1b() - Uri: "+uri);
		Log.d(TAG,"test1b() - filename: "+FileUtils.getFilename(uri));
		
		//assertEquals("extensao não é .png","png", FileUtils.getFileExtension(uri));

		FileUtils.showUri(uri);
		
	}
	

	/**
	 * 
	 */
	public void test1c() {

		File f = FileUtils.obtemNomeArquivo(".png");
		assertNotNull(f);

		Log.d(TAG, "f=" + f);

		assertEquals("f não existe", false, f.exists());

		Log.d(TAG,"test1b() - f: "+f);
		Log.d(TAG,"test1b() - filename: "+FileUtils.getFilename(f));
		
		//assertEquals("extensao não é .png","png", FileUtils.getFileExtension(uri));
		
	}
	
	/**
	 * 
	 */
	public void test2() {
		
		File f = FileUtils.obtemNomeArquivoPNG();
		assertNotNull(f);

		Log.d(TAG, "test2() - f=" + f);

		assertEquals("f não existe", false, f.exists());
		
	}

	/**
	 * 
	 */
	public void test3() {
		
		File f = FileUtils.obtemNomeArquivoJPEG();
		assertNotNull(f);

		Log.d(TAG, "test3() - f=" + f);

		assertEquals("f não existe", false, f.exists());
	}
	
	
	/**
	 * 
	 */
	public void test4() {
		
		File f = FileUtils.obtemNomeArquivoJPEG();
		assertNotNull(f);

		Log.d(TAG, "test4() - f=" + f);

		// transforma File em Uri
		Uri uri = Uri.fromFile(f);
		
		assertNotNull("Uri não existe", uri);
		
		Log.d(TAG,"uri: "+uri);
		
	}
	

	/**
	 * 
	 */
	public void test5() {
		
		File f = FileUtils.obtemNomeArquivoJPEG();
		assertNotNull("Arquivo é nulo", f);

		Log.d(TAG, "test5() - f=" + f);

		// transforma File em Uri
		Uri uri = Uri.fromFile(f);
		
		assertNotNull("Uri não existe", uri);
		
		Log.d(TAG,"test5() - uri: "+uri);
		
		assertEquals("O schema não é file",true, FileUtils.isFileURI(uri));
		
	}
		
}
