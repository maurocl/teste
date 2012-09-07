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
 * Representação uma foto armazenada em memória externa.
 * 
 * <br>
 * Permite ler e gravar uma foto.
 * 
 * <br>
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

  // nome do arquivo onde a foto está armazenada
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

    this.arquivo = arquivo; // arquivo
    this.dimensao = null; // dimensão da foto
    this.imagem = null;
    this.dados = null;
    this.formatoGravacao = CompressFormat.PNG;

  }

  /**
   * Construtor
   * 
   * @param arquivo
   *          onde a foto está armazenada
   * 
   * @param bitmap
   *          bitmap contendo a foto
   * 
   */
  public Foto(String arquivo, Bitmap bitmap) {

    if ((arquivo == null) || (bitmap == null)) {
      Log.w(TAG, "Foto() - não foi possível criar uma foto pois os dados fornecidos são nulos");
      return;
    }

    this.arquivo = arquivo;
    this.imagem = bitmap;
    this.dados = null;
    this.formatoGravacao = CompressFormat.PNG;

    if (this.getImagem() != null) {
      dimensao = new Dimensao(getImagem().getWidth(), getImagem().getHeight());
      this.setDimensao(dimensao);
    }

    this.filename = new File(this.arquivo);

  }

  // -------------------------------------------------------
  // Outros métodos
  // -------------------------------------------------------

  /**
   * Armazena a foto no arquivo<br>
   * 
   * O formato padrão de gravação é .PNG
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
   * Grava uma foto em um arquivo com um dado padrão de qualidade.
   * 
   * @param formato
   *          Bitmap.CompressFormat.JPEG
   * 
   * @param quality
   *          valor de 0 (pior qualidade) a 100 (melhor qualidade)
   * 
   * @return true em caso de sucesso ou null em caso de falha
   * 
   * @throws FileNotFoundException
   *           se o arquivo não for encontrado
   * 
   * @throws IOException
   * 
   */
  public boolean gravar(CompressFormat formato, int quality) throws FileNotFoundException, IOException {

    // o bitmap armazenado em imagem será salva
    if (getImagem() == null) {
      // bitmap está vazio
      Log.w(TAG, "gravar() - getImagem() - foto não pode ser gravada pois seu bitmap está vazio.");
      return false;
    }

    if (getArquivo() == null) {
      // nome do arquivo não foi fornecido
      Log.w(TAG, "gravar() - getArquivo() - foto não pode ser gravada pois o nome do arquivo está vazio.");
      return false;
    }

    boolean salvouFoto = false;

    // cria um stream de saída onde o bitmap será salvo
    OutputStream out = new FileOutputStream(new File(getArquivo()));

    // salva a imagem com o formato e qualidade solicitada
    salvouFoto = getImagem().compress(formato, quality, out);

    // fecha o stream de saída
    out.close();

    if (salvouFoto) {
      Log.v(TAG, "gravar() - foto: " + getArquivo() + " gravada com sucesso.");
    }
    else {
      Log.w(TAG, "gravar() - falha na gravação da foto: " + getArquivo());
    }

    return salvouFoto;

  }

  /**
   * Lê o bitmap contendo a foto.
   * 
   * @return true se a foto pode ser lida ou false em caso de erro
   * 
   */
  public boolean ler() {

    // a foto será lida do filename e armazenada no bitmap imagem

    if (getFilename() == null) {
      Log.w(TAG, "ler() - foto não pode ser lida pois o nome do arquivo está vazio.");
      return false;
    }

    if ((getFilename() != null) && (!getFilename().exists())) {
      Log.w(TAG, "ler() - foto não pode ser lida pois o arquivo não existe.");
      // arquivo não existe
      return false;
    }

    // obtém um bitmap a partir do arquivo
    Bitmap bitmap = ManipulaImagem.getBitmapFromFile(getFilename());

    if (bitmap == null) {

      // bitmap está vazio
      Log.w(TAG, "ler() - Bitmap com a foto não pode ser criada.");
      return false;

    }

    // guarda a imagem - atualiza o atributo com o bitmap lido
    Log.d(TAG, "ler() - atualiza imagem");

    // atualiza o bitmap lido
    this.setImagem(bitmap);

    // leitura feita com sucesso
    return true;

  }

  /**
   * Retorna a Uri da foto a partir do nome completo do arquivo onde o bitmap
   * está armazenado.
   * 
   * @return Uri da foto
   */
  public Uri getUri() {

    Uri uri = Uri.fromFile(this.getFilename());

    return uri;

  }

  /**
   * Verifica se a largura da foto é maior que sua altura.<br>
   * 
   * Verifica se razão entre a largura e a algura da foto for > 0
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
   * Verifica se a altura da foto é maior que sua largura.<br>
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
   * Cria uma nova foto como redimensionada para nova dimensão (largura e
   * altura).
   * 
   * @param largura
   *          nova largura da foto (em pixels)
   * 
   * @param altura
   *          nova altura da foto (em pixels)
   * 
   * @return a foto redimensionada ou null em caso de erro.
   * 
   */
  public Foto redimensiona(String nomeArquivo, int largura, int altura) {

    // TODO avaliar a semântica desse método
    // vale a pena criar uma nova foto ?

    if (nomeArquivo == null) {
      Log.w(TAG, "redimensiona() - nome do arquivo onde a foto será salva não foi informado.");
      return null;
    }

    if (getImagem() == null) {
      Log.w(TAG, "redimensiona() - foto não possui imagem associada");
      return null;
    }

    // Creates a new bitmap, scaled from an existing bitmap
    Bitmap bitmap = Bitmap.createScaledBitmap(getImagem(), largura, altura, true);

    // Cria uma nova foto
    Foto f = new Foto(nomeArquivo, bitmap);

    // Cria uma nova domensão
    Dimensao d = new Dimensao(largura, altura);

    // altera a dimensão da foto
    f.setDimensao(d);

    boolean gravou = false;

    try {

      // grava a foto
      gravou = f.gravar();

    } catch (FileNotFoundException e) {
      Log.w(TAG, "redimensiona() - arquivo não foi encontrado.", e);

    } catch (IOException e) {
      Log.w(TAG, "redimensiona() - erro de I/O.", e);

    }

    if (gravou) {
      // uma instância da foto redimensionada
      return f;
    }
    else {

      Log.w(TAG, "redimensiona() - erro na gravação da foto");
      return null;
    }

  }

  //--------------------------------------------------------
  // Métodos getters e setters
  //--------------------------------------------------------

  /**
   * Obtém o nome do arquivo onde a foto está gravada.
   * 
   * @return Uma string contendo o nome do arquivo.
   * 
   */
  public String getArquivo() {

    return arquivo;
  }

  /**
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
   * Retorna um array de bytes.
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
        Log.d(TAG, "getDados() - nº de bytes: " + getDados().length);
      }
    }

    return this.dados;

  }

  /**
   * 
   * 
   * @param dados
   * 
   */
  public void setDados(byte[] dados) {

    this.dados = dados;
  }

  /**
   * Retorna a dimensão de uma foto: sua largura e altura
   * 
   * @return um objeto da classe Dimensao com a largura e altura da foto
   */
  public Dimensao getDimensao() {

    return dimensao;
  }

  /**
   * Atualiza as dimensões da foto
   * 
   * @param dimensao
   */
  public void setDimensao(Dimensao dimensao) {

    this.dimensao = dimensao;
  }

  /**
   * Obtém o nome do arquivo.
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
   * Seta o nome do arquivo
   * 
   * @param filename
   *          nome completo do arquivo
   * 
   */
  public void setFilename(File filename) {

    this.filename = filename;
  }

  /**
   * Retorna o formato de gravação da foto.
   * 
   * @return o formato de gravação da foto (imagem)
   * 
   */
  public Bitmap.CompressFormat getFormato() {

    return formatoGravacao;

  }

  /**
   * Estabelece o formato de gravação da foto
   * 
   * @param formato
   *          formato de gravação da foto
   * 
   *          Bitmap.CompressFormat.JPEG<br>
   *          Bitmap.CompressFormat.PNG<br>
   * 
   */
  public void setFormato(Bitmap.CompressFormat formato) {

    this.formatoGravacao = formato;
  }

  /**
   * Retorna o bitmap contendo a foto.
   * 
   * @return o bitmap da foto ou null
   * 
   */
  public Bitmap getImagem() {

    return imagem;
  }

  /**
   * Atualiza o bitmap da foto e sua dimensão.
   * 
   * @param imagem
   *          o bitmap da foto
   * 
   */
  public void setImagem(Bitmap imagem) {

    this.imagem = imagem;

    if (imagem != null) {
      // atualiza a dimensão da imagem
      Dimensao d = new Dimensao(imagem.getWidth(), imagem.getHeight());
      this.setDimensao(d);
    }

  }

  /**
   * toString()
   */
  @Override
  public String toString() {

    return "\nFoto [\ndimensao=" + dimensao + ", arquivo=" + arquivo + ", filename=" + filename + ", imagem=" + imagem + ", dados="
        + Arrays.toString(dados) + ", formato=" + formatoGravacao + "\n]";
  }

}
