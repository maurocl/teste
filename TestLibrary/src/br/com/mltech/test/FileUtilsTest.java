package br.com.mltech.test;

import java.io.File;

import android.test.AndroidTestCase;
import br.com.mltech.utils.FileUtils;

public class FileUtilsTest extends AndroidTestCase {

	public static final String TAG = "FileUtils";

	  /**
	   * 	   */
	  public void test1() {

	    String filename = null;
	    File f = FileUtils.obtemNomeArquivo();
	    assertNull(f);

	  }
	
}
