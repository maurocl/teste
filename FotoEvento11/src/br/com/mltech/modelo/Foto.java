package br.com.mltech.modelo;

import java.io.File;

import android.graphics.Bitmap;

/**
 * 
 * @author maurocl
 * 
 */
public class Foto {

  private String arquivo;
  private Bitmap foto;
  private File filename;

  public Foto(String arquivo) {
    this.arquivo = arquivo;
  }

  public Bitmap getBitmap() {
    return null;
  }

  public boolean gravar() {
    return false;
  }
  
  public boolean ler() {
    return false;
  }
  
  public String tamanho() {
    return null;
  }
}
