package br.com.mltech.modelo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Arrays;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.util.Log;
import br.com.mltech.utils.ManipulaImagem;

/**
 * Foto
 * 
 * Representa uma foto
 * 
 * Permite ler e gravar uma foto.
 * 
 * Permite redimensionar uma foto gerando uma nova foto
 * 
 * @author maurocl
 * 
 */
public class Foto implements Serializable {

  public static final String TAG = "Foto";

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -5873671630650408409L;

  // dimensões da foto (largura x altura)
  private Dimensao dimensao;

  // nome do arquivo onde a foto está armazenada
  private String arquivo;

  // arquivo
  private File filename;

  // bitmap contendo a imagem (foto)
  private Bitmap imagem;

  // dados
  private byte[] dados;

  // formato de gravação da foto
  private Bitmap.CompressFormat formatoGravacao;

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
   * Foto(String arquivo, Bitmap bm)
   * 
   * @param arquivo
   *          onde a foto está armazenada
   * @param bitmap
   *          bitmap
   * 
   */
  public Foto(String arquivo, Bitmap bitmap) {

    this.arquivo = arquivo;
    this.imagem = bitmap;
    this.dados = null;

    if (getImagem() != null) {
      dimensao = new Dimensao(getImagem().getWidth(), getImagem().getHeight());
    }

    this.filename = new File(this.arquivo);

  }

  // Getters and Setters

  /**
   * getFilename()
   * 
   * @return uma instância da classe File
   */
  public File getFilename() {

    if (filename == null) {
      filename = new File(getArquivo());
    }

    return this.filename;

  }

  /**
   * setFilename(File filename)
   * 
   * @param filename
   * 
   */
  public void setFilename(File filename) {
    this.filename = filename;
  }

  /**
   * getImagem()
   * 
   * @return o bitmap da foto ou null
   * 
   */
  public Bitmap getImagem() {
    return imagem;
  }

  /**
   * setImagem(Bitmap imagem)
   * 
   * Atualiza a imagem da foto e sua dimensão
   * 
   * @param imagem
   *          o bitmap da foto
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
   * getDimensao()
   * 
   * Retora a dimensão de uma foto: sua largura e altura
   * 
   * @return um objeto da classe Dimensao com a largura e altura da foto
   */
  public Dimensao getDimensao() {
    return dimensao;
  }

  /**
   * setDimensao(Dimensao dimensao)
   * 
   * Atualiza as dimensões da foto
   * 
   * @param dimensao
   */
  public void setDimensao(Dimensao dimensao) {
    this.dimensao = dimensao;
  }

  /**
   * getArquivo()
   * 
   * Obtém o nomo do arquivo onde uma foto está gravada
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
   *          nome completo do arquivo
   * 
   */
  public void setArquivo(String arquivo) {
    this.arquivo = arquivo;
    filename = new File(arquivo);
  }

  /**
   * getDados()
   * 
   * @return um array de bytes de uma imagem
   */
  public byte[] getDados() {

    ByteArrayOutputStream bos = new ByteArrayOutputStream();

    if (getImagem() != null) {
      getImagem().compress(CompressFormat.PNG, 100, bos);
      this.dados = bos.toByteArray();
    }

    if (getDados() != null) {
      Log.d(TAG, "getDados() - nº de bytes: " + getDados().length);
    }

    return this.dados;

  }

  /**
   * setDados(byte[] dados)
   * 
   * @param dados
   * 
   */
  public void setDados(byte[] dados) {
    this.dados = dados;
  }

  // -----------------
  // Outros métodos
  // -----------------

  /**
   * gravar()
   * 
   * Grava uma foto em um arquivo
   * 
   * @return true caso a foto seja salva ou false em caso de erro
   * 
   */
  public boolean gravar() throws FileNotFoundException, IOException {

    boolean gravou = false;

    if (this.getFormato() == null) {
      this.setFormato(Bitmap.CompressFormat.PNG);
    }

    if (this.getFormato() == Bitmap.CompressFormat.JPEG) {
      gravou = gravar(this.getFormato(), 75);
    } else if (this.getFormato() == Bitmap.CompressFormat.PNG) {
      gravou = gravar(this.getFormato(), 100);
    }

    return gravou;

  }

  /**
   * gravar(CompressFormat formato, int quality)
   * 
   * @param formato
   *          Bitmap.CompressFormat.JPEG,
   * @param quality
   *          valor de 0 (pior qualidade) a 100 (melhor qualidade)
   * 
   * @return true
   * 
   * @throws FileNotFoundException
   *           se o arquivo não for encontrado
   * @throws IOException
   * 
   */
  public boolean gravar(CompressFormat formato, int quality) throws FileNotFoundException, IOException {

    // a foto armazenada em imagem será salva
    if (getImagem() == null) {
      // bitmap não pode ser vazio
      return false;
    }

    if (getArquivo() == null) {
      // arquivo não pode ser vazio
      return false;
    }

    boolean salvou = false;

    OutputStream out = new FileOutputStream(getFilename());

    salvou = getImagem().compress(formato, quality, out);

    out.close();

    return salvou;

  }

  /**
   * ler()
   * 
   * Lê uma foto armazenada em um arquivo
   * 
   * @return true se a foto pode ser lida ou false em caso de erro
   * 
   */
  public boolean ler() {

    // a foto será lida do filename e armazenada no bitmap imagem

    if (getFilename() == null) {
      Log.w(TAG, "ler() -  o nome do arquivo está vazio");
      return false;
    }

    if ((getFilename() != null) && (!getFilename().exists())) {
      Log.w(TAG, "ler() - o nome do arquivo não existe");
      // arquivo não existe
      return false;
    }

    // lê o bitmap
    Bitmap bitmap = ManipulaImagem.getBitmapFromFile(getFilename());

    if (bitmap == null) {

      // bitmap está vazio
      Log.w(TAG, "ler() - Bitmap está vazio");

      return false;
    }

    // guarda a imagem
    Log.d(TAG, "ler() - atualiza imagem");
    this.setImagem(bitmap);

    return true;

  }

  /**
   * getUri()
   * 
   * Retorna a Uri do arquivo
   * 
   * @return Uri do arquivo
   */
  public Uri getUri() {

    Uri uri = Uri.fromFile(this.getFilename());

    return uri;
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

    boolean filter = true;

    // Creates a new bitmap, scaled from an existing bitmap
    Bitmap bitmap = Bitmap.createScaledBitmap(getImagem(), largura, altura, filter);

    Foto f = new Foto(null, bitmap);

    Dimensao d = new Dimensao(largura, altura);

    f.setDimensao(d);

    return f;

  }

  /**
   * getFormato()
   * 
   * @return o formato de gravação da foto (imagem)
   * 
   */
  public Bitmap.CompressFormat getFormato() {
    return formatoGravacao;
  }

  /**
   * setFormato(Bitmap.CompressFormat formato)
   * 
   * Estabelece o formato de gravação da foto
   * 
   * @param formato
   *          formato de gravação da foto
   * 
   *          Bitmap.CompressFormat.JPEG Bitmap.CompressFormat.PNG
   * 
   */
  public void setFormato(Bitmap.CompressFormat formato) {
    this.formatoGravacao = formato;
  }


  /**
   * toString()
   */
  @Override
  public String toString() {
    return "Foto [dimensao=" + dimensao + ", arquivo=" + arquivo + ", filename=" + filename + ", imagem=" + imagem + ", dados="
        + Arrays.toString(dados) + ", formato=" + formatoGravacao + "]";
  }
  
  /**
   * 
   * armazena todos os bytes da imagem em um array de bytes
   */
  /*
   * public void xxx() {
   * 
   * ByteArrayOutputStream bos = new ByteArrayOutputStream();
   * 
   * if (getImagem() != null) { getImagem().compress(CompressFormat.PNG, 100,
   * bos); this.dados = bos.toByteArray(); }
   * 
   * if (getDados() != null) { Log.d(TAG, "xxx() - nº de bytes: " +
   * getDados().length); }
   * 
   * }
   */

}
