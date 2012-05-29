package br.com.mltech.modelo;

import java.io.File;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Foto
 * 
 * @author maurocl
 * 
 */
public class Foto implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -5873671630650408409L;

  // dimensões da foto (largura x altura)
  private Dimensao dimensao;

  // nome do arquivo onde a foto está armazenada
  private String arquivo;

  // bitmap contendo a imagem (foto)
  private Bitmap imagem;

  //
  private byte[] dados;

  /**
   * Foto(String arquivo)
   * 
   * @param arquivo
   *          nome do arquivo que contém a foto
   * 
   */
  public Foto(String arquivo) {
    this.arquivo = arquivo;
    this.dimensao = null;
    imagem = null;
    dados = null;
  
  }

  /**
   * Foto(String arquivo, byte[] dados)
   * @param arquivo
   * @param dados
   */
  public Foto(String arquivo, byte[] dados) {
    this.arquivo = arquivo;
    this.dados = dados;
  }

  /**
   * Foto(String arquivo, Bitmap bm)
   * 
   * @param arquivo
   * @param bm
   * 
   */
  public Foto(String arquivo, Bitmap bm) {
    this.arquivo = arquivo;
    this.imagem = bm;
  }

  /**
   * isLandscape()
   * 
   * Quando a razão entre a largura e a algura for > 0
   * 
   * @return true se landscape ou false caso contrário
   */
  public boolean isLandscape() {
    if (getDimensao() != null) {
      return (getDimensao().getLargura() > getDimensao().getAltura());
    } else {
      return false;
    }

  }

  /**
   * isPortrait()
   * 
   * Quando a razão entre a largura e a algura for <= 0
   * 
   * @return true se Portrait e false caso contrário
   */
  public boolean isPortrait() {
    if (getDimensao() != null) {
      return (getDimensao().getLargura() <= getDimensao().getAltura());
    } else {
      return false;
    }

  }

  /**
   * redimensiona(int largura, int altura)
   * 
   * @param largura
   * @param altura
   * 
   * @return a foto redimensionada ou null em caso de erro
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
   * getFilename()
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
   * getImagem()
   * 
   * @return
   */
  public Bitmap getImagem() {
    return imagem;
  }

  /**
   * setImagem(Bitmap imagem)
   * 
   * @param imagem
   * 
   */
  public void setImagem(Bitmap imagem) {

    this.imagem = imagem;

    if (imagem != null) {
      Dimensao d = new Dimensao(imagem.getWidth(), imagem.getHeight());
      this.dimensao = d;
    }

  }

  /**
   * getBitmap()
   * 
   * @return
   */
  public Bitmap getBitmap() {
    return imagem;
  }

  /**
   * tamanho()
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
   * setDimensao(Dimensao dimensao)
   * 
   * @param dimensao
   */
  public void setDimensao(Dimensao dimensao) {
    this.dimensao = dimensao;
  }

  /**
   * getArquivo()
   * 
   * @return
   * 
   */
  public String getArquivo() {
    return arquivo;
  }

  /**
   * setArquivo(String arquivo)
   * 
   * @param arquivo
   * 
   */
  public void setArquivo(String arquivo) {
    this.arquivo = arquivo;
  }

  /**
   * 
   * @return
   */
  public byte[] getDados() {
    return dados;
  }

  /**
   * 
   * @param dados
   */
  public void setDados(byte[] dados) {
    this.dados = dados;
  }

  /**
   * getUri()
   * 
   * @return
   */
  public Uri getUri() {
        
    File f = new File(arquivo);
    
    Uri uri = Uri.fromFile(f);
    
    return uri;
  }

  /**
   * toString()
   */
  @Override
  public String toString() {
    return "Foto [dimensao=" + dimensao + ", arquivo=" + arquivo + ", imagem=" + imagem + "]";
  }

  
  
}
