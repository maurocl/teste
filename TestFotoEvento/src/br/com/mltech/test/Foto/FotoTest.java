package br.com.mltech.test.Foto;

import android.test.AndroidTestCase;
import android.util.Log;
import br.com.mltech.modelo.Foto;

/**
 * 
 * @author maurocl
 *
 */
public class FotoTest extends AndroidTestCase {
  
  private static final String TAG = "FotoTest";
  
  private static final String nomeArquivo = "/mnt/sdcard/Pictures/fotoevento/molduras/cabine_132_568_red.png";
  
  /**
   * 
   */
  public void test0() {
    
    Foto f = new Foto(null);
    
    assertNotNull("Foto é vazia", f);
    
    Log.d(TAG,"foto==null: "+f);
    
  }
  
  /**
   * 
   */
  public void test1() {
    
    Foto f = new Foto(null,null);
    
    Log.d(TAG,"test1() - foto==null: "+f);
    
    assertNotNull("test1() - Foto é vazia", f);
    
    Log.d(TAG,"test1() - foto==null: "+f);
    
  }
  
  /**
   * 
   */
  public void test2() {
    
    Foto f = new Foto("/mnt/sdcard/");
    
    Log.d(TAG,"test2() - "+f);
    
    assertNotNull("arquivo está vazio", f.getArquivo());
    
  }
  
 public void test2_1() {
    
    Foto f = new Foto("/mnt/sdcard/");
    
    Log.d(TAG,"test2_1() - "+f);
    Log.d(TAG,"test2_1() - getFilename((): "+f.getFilename());
    Log.d(TAG,"test2_1() - getFilename().exists(): "+f.getFilename().exists());
    Log.d(TAG,"test2_1() - getFilename().isFile(): "+f.getFilename().isFile());
    Log.d(TAG,"test2_1() - getFilename().isDirectory(): "+f.getFilename().isDirectory());
    Log.d(TAG,"test2_1() - getUri: "+f.getUri());
    
    assertNotNull("arquivo está vazio", f.getFilename());
    
  }
  
  /**
   * 
   */
  public void test4() {
    
    Foto f = new Foto(nomeArquivo);
    
    assertNotNull("arquivo não é válido", f);
    
  }

  /**
   * 
   */
  public void test3() {
    
    Foto f = new Foto(nomeArquivo);
    
    assertNotNull("arquivo não é válido", f.getArquivo());
    
    Log.d(TAG,"test3() -f.getArquivo(): "+f.getArquivo());
    
  }
  
  /**
   * 
   */
  public void test5() {
    
    Foto f = new Foto(nomeArquivo);
    
    assertNotNull("arquivo não é válido", f.getFilename());
    Log.d(TAG,"test5() - f.getArquivo(): "+f.getArquivo());
    
  }
  
  /**
   * 
   */
  public void test6() {
    
    Foto f = new Foto(nomeArquivo);
    
    assertNull("test6() - Dimensão não é nula", f.getDimensao());
    
  }
  
  /**
   * 
   */
  public void test7() {
    
    Foto f = new Foto(nomeArquivo);
    
    assertNull("Formato não é nulo", f.getFormato());
    
    Log.d(TAG, "test7() - f.getFormato(): "+ f.getFormato());
    
  }

  /**
   * 
   */
  public void test8() {
    
    Foto f = new Foto(nomeArquivo);
    
    assertNull("arquivo apresentou dados", f.getDados());
    
    Log.d(TAG,"test8() - f.getDados(): "+f.getDados());
    
  }


  

  

}
