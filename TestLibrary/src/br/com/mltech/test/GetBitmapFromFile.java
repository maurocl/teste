package br.com.mltech.test;

import java.io.File;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import br.com.mltech.utils.ManipulaImagem;

/**
 * 
 * @author maurocl
 * 
 */
public class GetBitmapFromFile extends AndroidTestCase {

  public static final String TAG = "GetBitmapFromFile";

  /**
   * 
   */
  public void test1() {

    String filename = null;
    Bitmap bm = ManipulaImagem.getBitmapFromFile(filename);
    assertNull(bm);

  }

  /**
   * 
   */
  public void test2() {

    String filename = "c:\\";
    Bitmap bm = ManipulaImagem.getBitmapFromFile(filename);
    assertNull(bm);

  }

  /**
   * 
   */
  public void test3() {

    String filename = "c:\\windows";
    Bitmap bm = ManipulaImagem.getBitmapFromFile(filename);
    assertNull(bm);

  }

  /**
   * 
   */
  public void test4() {

    String filename = "/mnt/sdcard";
    Bitmap bm = ManipulaImagem.getBitmapFromFile(filename);
    assertNull(bm);

  }

  /**
   * 
   */
  public void test5() {

    String filename = "/mnt/sdcard/Pictures";
    Bitmap bm = ManipulaImagem.getBitmapFromFile(filename);
    assertNull(bm);

  }

  /**
   * 
   */
  public void test6() {

    String filename = "/mnt/sdcard/Pictures/fotoevento";
    Bitmap bm = ManipulaImagem.getBitmapFromFile(filename);
    assertNull(bm);

  }

  /**
   * 
   */
  public void test7() {

    String filename = "/mnt/sdcard/Pictures/fotoevento/molduras";
    Bitmap bm = ManipulaImagem.getBitmapFromFile(filename);
    assertNull(bm);

  }

  /**
   * 
   */
  public void test8() {

    String filename = "/mnt/sdcard/Pictures/fotoevento/molduras/cabine_132_568_red.png";
    Bitmap bm = ManipulaImagem.getBitmapFromFile(filename);
    assertNotNull(bm);

  }

  // ---------------------------------------------------------------------------
  //
  // ---------------------------------------------------------------------------

  /**
   * Testa arquivo nulo
   */
  public void test9() {

    File f = null;

    Bitmap bm = ManipulaImagem.getBitmapFromFile(f);
    assertNull(bm);

  }

  /**
   * Testa para um arquivo não existnte
   */
  public void test10() {

    File f = new File("");

    Bitmap bm = ManipulaImagem.getBitmapFromFile(f);
    assertNull(bm);

  }

  /**
   * Testa um arquivo existente
   */
  public void test11() {

    File f = new File("/mnt/sdcard/Pictures/fotoevento/molduras/cabine_132_568_red.png");

    // String filename =
    // "/mnt/sdcard/Pictures/fotoevento/molduras/cabine_132_568_red.png";
    Bitmap bm = ManipulaImagem.getBitmapFromFile(f);
    assertNotNull(bm);

  }

  // ---------------------------------------------------------------------------
  //
  // ---------------------------------------------------------------------------

}
