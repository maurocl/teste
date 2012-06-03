package br.com.mltech.test;

import junit.framework.Test;
import junit.framework.TestFailure;
import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import android.util.Log;
import br.com.mltech.utils.ManipulaImagem;

/**
 * 
 * @author maurocl
 * 
 */
public class GetScaledBitmap extends AndroidTestCase {

  public static final String TAG = "GetScaledBitmap";

  /**
   * 
   */
  public void test1() {

    Bitmap bm = null;

    int w = 0;
    int h = 0;

    bm = ManipulaImagem.getScaledBitmap2(bm, w, h);

    assertNull(bm);

  }

  /**
   * 
   */
  public void test2() {

    Bitmap bm = null;

    int w = 1;
    int h = 1;

    bm = ManipulaImagem.getScaledBitmap2(bm, w, h);

    assertNull(bm);

  }

  /**
   * 
   */
  public void test3() {

    Bitmap bm = Bitmap.createBitmap(96, 96, Bitmap.Config.ARGB_8888);

    int w = 0;
    int h = 0;

    bm = ManipulaImagem.getScaledBitmap2(bm, w, h);

    assertNull(bm);

  }

  /**
   * 
   */
  public void test4() {

    Bitmap bm = Bitmap.createBitmap(96, 96, Bitmap.Config.ARGB_8888);

    int w = 0;
    int h = 0;

    bm = ManipulaImagem.getScaledBitmap2(bm, w, h);

    // TestFailure tf = new TestFailure(failedTest, thrownException);

    Test t = null;
    TestFailure tf = new TestFailure(t, new IllegalArgumentException());
    if (tf.isFailure()) {
      Log.d(TAG, "isFailure()");
    }

    assertNull(bm);


  
  }

  /**
   * 
   * Cria um bitmap e depois redimensiona-o e testa se ele foi redimensionado a contento
   * 
   */
  public void test5() {

    Bitmap bm = Bitmap.createBitmap(96, 96, Bitmap.Config.ARGB_8888);

    assertEquals(bm.getWidth(),96);
    assertEquals(bm.getWidth(),96);
    
    int w = 10;
    int h = 20;

    bm = ManipulaImagem.getScaledBitmap2(bm, w, h);

    assertNotNull(bm);
    
    assertEquals("width()",bm.getWidth(),10);
    assertEquals("height()",bm.getHeight(),20);
    

  }
  
  
}
