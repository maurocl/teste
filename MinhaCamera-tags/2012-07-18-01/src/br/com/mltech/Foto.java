package br.com.mltech;

import java.io.Serializable;

import android.graphics.Bitmap;

public class Foto implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7693317510053929998L;

  private Bitmap foto;

  private String arquivo;

  public Foto(Bitmap foto, String arquivo) {

    super();
    this.foto = foto;
    this.arquivo = arquivo;
  }

  @Override
  public String toString() {

    return "Foto [foto=" + foto + ", arquivo=" + arquivo + "] - size: " + foto.getWidth() + "x" + foto.getHeight();
  }

  public Bitmap getFoto() {

    return foto;
  }

  public void setFoto(Bitmap foto) {

    this.foto = foto;
  }

  public String getArquivo() {

    return arquivo;
  }

  public void setArquivo(String arquivo) {

    this.arquivo = arquivo;
  }

}
