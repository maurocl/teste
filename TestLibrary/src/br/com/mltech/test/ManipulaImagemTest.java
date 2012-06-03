package br.com.mltech.test;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import br.com.mltech.utils.ManipulaImagem;

public class ManipulaImagemTest extends AndroidTestCase {

  /**
   * testCriaBitmap_string
   */
  public void testCriaBitmap_string() {

    Context context = getContext();

    Bitmap bm = ManipulaImagem.criaBitmap("c:\\");

    assertNull(bm);

  }

  /**
   * testCriaBitmap_string_null
   */
  public void testCriaBitmap_string_null() {

    Bitmap bm = ManipulaImagem.criaBitmap((String) null);

    assertNull(bm);

  }

  /**
   * testCriaBitmap_file_null
   */
  public void testCriaBitmap_file_null() {

    File f = null;
    
    Bitmap bm = ManipulaImagem.criaBitmap(f);

    assertNull(bm);

  }

  /**
   * testCriaBitmap_file_1
   */
  public void testCriaBitmap_file_1() {

    File f = new File("");
    
    Bitmap bm = ManipulaImagem.criaBitmap(f);

    assertNull(bm);

  }

  /**
   * testCriaBitmap_file_2
   */
  public void testCriaBitmap_file_2() {

    File f = new File("c:\\");
    
    Bitmap bm = ManipulaImagem.criaBitmap(f);

    assertNull(bm);

  }
  
  
}
