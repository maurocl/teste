package br.com.mltech.utils.camera.test;

import br.com.mltech.utils.ManipulaImagem;
import android.graphics.Bitmap;
import android.test.AndroidTestCase;

public class TestCamera extends AndroidTestCase {

	public static final String TAG = "TestCamera";

	/**
   * 
   */
	public void test1() {

		String filename = null;
		Bitmap bm = ManipulaImagem.getBitmapFromFile(filename);
		assertNull(bm);

	}
	
	public void test2() {
		
	}

}
