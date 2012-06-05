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

  // dimens�es da foto (largura x altura)
  private Dimensao dimensao;

  // nome do arquivo onde a foto est� armazenada
  private String arquivo;

  // nome do arquivo onde a foto est� armazenada
  private File filename;

  // bitmap contendo a imagem (foto)
  private Bitmap imagem;

  // dados
  private byte[] dados;

  // formato de grava��o da foto
  private Bitmap.CompressFormat formatoGravacao;

  /**
   * Foto(String arquivo)
   * 
   * @param arquivo
   *          nome do arquivo que cont�m a foto
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
   * Construtor
   * 
   * @param arquivo
   *          onde a foto est� armazenada
   * @param bitmap
   *          bitmap
   * 
   */
  public Foto(String arquivo, Bitmap bitmap) {

    if ((arquivo == null) || (bitmap == null)) {
      Log.d(TAG, "Foto() - n�o foi poss�vel criar uma foto pois os dados fornecidos s�o nulos");
      return;
    }

    this.arquivo = arquivo;
    this.imagem = bitmap;
    this.dados = null;

    if (this.getImagem() != null) {
      dimensao = new Dimensao(getImagem().getWidth(), getImagem().getHeight());
    }

    this.filename = new File(this.arquivo);

  }

  // Getters and Setters

  /**
   * getFilename()
   * 
   * @return uma inst�ncia da classe File
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
   * Atualiza a imagem da foto e sua dimens�o
   * 
   * @param imagem
   *          o bitmap da foto
   * 
   */
  public void setImagem(Bitmap imagem) {

    this.imagem = imagem;

    if (imagem != null) {
      // atualiza a dimens�o da imagem
      Dimensao d = new Dimensao(imagem.getWidth(), imagem.getHeight());
      this.dimensao = d;
    }

  }

  /**
   * getDimensao()
   * 
   * Retora a dimens�o de uma foto: sua largura e altura
   * 
   * @return um objeto da classe Dimensao com a largura e altura da foto
   */
  public Dimensao getDimensao() {

    return dimensao;
  }

  /**
   * setDimensao(Dimensao dimensao)
   * 
   * Atualiza as dimens�es da foto
   * 
   * @param dimensao
   */
  public void setDimensao(Dimensao dimensao) {

    this.dimensao = dimensao;
  }

  /**
   * getArquivo()
   * 
   * Obt�m o nome do arquivo onde uma foto est� gravada
   * 
   * @return uma string contendo o nome do arquivo
   * 
   */
  public String getArquivo() {

    return arquivo;
  }

  /**
   * setArquivo(String arquivo)
   * 
   * Estabelece o nome do arquivo e cria um objeto File (filename) associado ao
   * arquivo.
   * 
   * @param arquivo
   *          nome completo do arquivo
   * 
   */
  public void setArquivo(String arquivo) {

    this.arquivo = arquivo;
    this.filename = new File(arquivo);
  }

  /**
   * getDados()
   * 
   * @return um array de bytes de uma imagem
   */
  public byte[] getDados() {

    ByteArrayOutputStream bos = new ByteArrayOutputStream();

    if (this.getImagem() != null) {
      // grava o bitmap no output stream
      boolean gravouComSucesso = this.getImagem().compress(CompressFormat.PNG, 100, bos);
      if (gravouComSucesso) {
        this.dados = bos.toByteArray();
        Log.d(TAG, "getDados() - n� de bytes: " + getDados().length);
      }
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
  // Outros m�todos
  // -----------------

  /**
   * getFormato()
   * 
   * @return o formato de grava��o da foto (imagem)
   * 
   */
  public Bitmap.CompressFormat getFormato() {

    return formatoGravacao;
  }

  /**
   * setFormato(Bitmap.CompressFormat formato)
   * 
   * Estabelece o formato de grava��o da foto
   * 
   * @param formato
   *          formato de grava��o da foto
   * 
   *          Bitmap.CompressFormat.JPEG Bitmap.CompressFormat.PNG
   * 
   */
  public void setFormato(Bitmap.CompressFormat formato) {

    this.formatoGravacao = formato;
  }

  /**
   * gravar()
   * 
   * Armazena a foto no arquivo
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
   * Grava uma foto em um arquivo com um dado padr�o de qualidade.
   * 
   * @param formato
   *          Bitmap.CompressFormat.JPEG,
   * @param quality
   *          valor de 0 (pior qualidade) a 100 (melhor qualidade)
   * 
   * @return true
   * 
   * @throws FileNotFoundException
   *           se o arquivo n�o for encontrado
   * @throws IOException
   * 
   */
  public boolean gravar(CompressFormat formato, int quality) throws FileNotFoundException, IOException {

    // a foto armazenada em imagem ser� salva
    if (getImagem() == null) {
      // bitmap n�o pode ser vazio
      return false;
    }

    if (getArquivo() == null) {
      // arquivo n�o pode ser vazio
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
   * L� uma foto armazenada em um arquivo
   * 
   * @return true se a foto pode ser lida ou false em caso de erro
   * 
   */
  public boolean ler() {

    // a foto ser� lida do filename e armazenada no bitmap imagem

    if (getFilename() == null) {
      Log.w(TAG, "ler() -  o nome do arquivo est� vazio");
      return false;
    }

    if ((getFilename() != null) && (!getFilename().exists())) {
      Log.w(TAG, "ler() - o nome do arquivo n�o existe");
      // arquivo n�o existe
      return false;
    }

    // obt�m um bitmap a partir do arquivo
    Bitmap bitmap = ManipulaImagem.getBitmapFromFile(getFilename());

    if (bitmap == null) {

      // bitmap est� vazio
      Log.w(TAG, "ler() - Bitmap n�o pode ser criado (est� vazio)");
      return false;

    }

    // guarda a imagem - atualiza o atributo com o bitmap lido
    Log.d(TAG, "ler() - atualiza imagem");
    this.setImagem(bitmap);

    // leitura feita com sucesso
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
   * Quando a raz�o entre a largura e a algura for > 0
   * 
   * @return true se landscape ou false caso contr�rio
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
   * Quando a raz�o entre a largura e a algura for <= 0
   * 
   * @return true se Portrait e false caso contr�rio
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
   * toString()
   */
  @Override
  public String toString() {

    return "Foto [dimensao=" + dimensao + ", arquivo=" + arquivo + ", filename=" + filename + ", imagem=" + imagem + ", dados="
        + Arrays.toString(dados) + ", formato=" + formatoGravacao + "]";
  }

}
