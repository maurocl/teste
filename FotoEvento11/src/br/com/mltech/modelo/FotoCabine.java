package br.com.mltech.modelo;

import android.graphics.Bitmap;

/**
 * 
 * @author maurocl
 * 
 */
public class FotoCabine extends Foto {

  private String moldura;
  private Bitmap bmMoldura;
  private Foto[] fotos = new Foto[3];

  /**
   * 
   */
  public FotoCabine() {
    super("");
  }

  /**
   * 
   * @param i
   * @return
   */
  Bitmap getBitmap(int i) {
    return fotos[i].getBitmap();
  }
  
}
