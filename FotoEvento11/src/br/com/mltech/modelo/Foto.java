package br.com.mltech.modelo;

import java.io.File;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Foto
 * 
 * @author maurocl
 * 
 */
public class Foto {

  private Dimensao dimensao;

  // nome do arquivo
  private String arquivo;

  // bitmap contendo a imagem (foto)
  private Bitmap imagem;

  private byte[] dados;

  private Uri uri;

  /**
   * 
   * @param arquivo
   */
  public Foto(String arquivo) {
    this.arquivo = arquivo;
  }

  /**
   * 
   * @param arquivo
   * @param dados
   */
  public Foto(String arquivo, byte[] dados) {
    this.arquivo = arquivo;
    this.dados = dados;
  }

  /**
   * 
   * @param arquivo
   * @param bm
   */
  public Foto(String arquivo, Bitmap bm) {
    this.arquivo = arquivo;
    this.imagem = bm;
  }

  /**
   * 
   * @param uri
   */
  public Foto(Uri uri) {
    this.uri = uri;
  }

  /**
   * 
   * @param uri
   * @param dados
   */
  public Foto(Uri uri, byte[] dados) {
    this.uri = uri;
    this.dados = dados;
  }

  /**
   * 
   * @return
   */
  public boolean isLandscape() {
    if (getDimensao() != null) {
      return (getDimensao().getLargura() > getDimensao().getAltura());
    } else {
      return false;
    }

  }

  /**
   * 
   * @return
   */
  public boolean isPortrait() {
    if (getDimensao() != null) {
      return (getDimensao().getLargura() <= getDimensao().getAltura());
    } else {
      return false;
    }

  }

  /**
   * 
   * @param largura
   * @param altura
   * @return
   */
  public Foto redimensiona(int largura, int altura) {
    return null;
  }

  /**
   * Grava uma foto em um arquivo
   * 
   * @return
   */
  public boolean gravar() {

    // a foto armazenada em imagem será salva
    if (getImagem() == null) {
      return false;
    }

    if (getFilename() == null) {
      return false;
    }

    return false;
  }

  /**
   * Lê uma foto armazenada em um arquivo
   * 
   * @return
   * 
   */
  public boolean ler() {
    // a foto será lida do filename e armazenada no bitmap imagem

    if (getFilename() == null) {
      return false;
    }

    if (!getFilename().exists()) {
      // arquivo não existe
    }

    return false;

  }

  /**
   * 
   * @return
   */
  private File getFilename() {
    File f = new File(getArquivo());
    if ((f != null) && (f.exists())) {
      return f;
    }
    return null;
  }

  /**
   * 
   * @return
   */
  public Bitmap getImagem() {
    return imagem;
  }

  /**
   * 
   * @param imagem
   */
  public void setImagem(Bitmap imagem) {
    this.imagem = imagem;
  }

  /**
   * 
   * @return
   */
  public Bitmap getBitmap() {
    return null;
  }

  /**
   * 
   * @return
   */
  public String tamanho() {
    return null;
  }

  /**
   * 
   * @return
   */
  public Dimensao getDimensao() {
    return dimensao;
  }

  /**
   * 
   * @param dimensao
   */
  public void setDimensao(Dimensao dimensao) {
    this.dimensao = dimensao;
  }

  /**
   * 
   * @return
   */
  public String getArquivo() {
    return arquivo;
  }

  /**
   * 
   * @param arquivo
   */
  public void setArquivo(String arquivo) {
    this.arquivo = arquivo;
  }

  /**
   * toString()
   */
  @Override
  public String toString() {
    return "Foto [dimensao=" + dimensao + ", arquivo=" + arquivo + ", imagem=" + imagem + "]";
  }

}
