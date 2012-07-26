package br.com.mltech.modelo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.util.Log;
import br.com.mltech.utils.ManipulaImagem;

/**
 * FotoBitmap
 * 
 * @author maurocl
 *
 */
public class FotoBitmap {

  private static final String TAG = "FotoBitmap";

  // nome do arquivo onde está armazenado o bitmap
  private String arquivo;

  // imagem no formato bitmap
  private Bitmap bitmap;

  // dados
  private byte[] dados;

  // formato de gravação da foto
  private Bitmap.CompressFormat formatoGravacao;

  /**
   * Construtor
   * 
   */
  public FotoBitmap(String arquivo, Bitmap bitmap) {
    this.arquivo = arquivo;
    this.bitmap = bitmap;
    this.formatoGravacao = Bitmap.CompressFormat.PNG;
  }

  /**
   * Lê um bitmap de um arquivo
   * 
   * @return
   */
  public boolean load() {

    File f = xxx(getArquivo());

    if (f == null) {
      Log.w(TAG, "read() - o nome do arquivo está vazio");
      return false;
    }

    if (!f.exists()) {
      Log.w(TAG, "read() - o nome do arquivo não existe");
      // arquivo não existe
      return false;
    }

    // lê o bitmap
    Bitmap bitmap = ManipulaImagem.getBitmapFromFile(f);

    if (bitmap == null) {

      // bitmap está vazio
      Log.w(TAG, "read() - Bitmap está vazio");

      return false;
    }

    // guarda a imagem
    Log.d(TAG, "read() - atualiza imagem");
    this.setBitmap(bitmap);

    return true;

  }

  /**
   * Grava uma imagem um arquivo
   * 
   * @return true caso a foto seja salva ou false em caso de erro
   * 
   */
  public boolean save() throws FileNotFoundException, IOException {

    boolean gravou = false;

    if (this.getFormatoGravacao() == null) {
      this.setFormatoGravacao(Bitmap.CompressFormat.PNG);
    }

    if (this.getFormatoGravacao() == Bitmap.CompressFormat.JPEG) {
      gravou = save(this.getFormatoGravacao(), 75);
    } else if (this.getFormatoGravacao() == Bitmap.CompressFormat.PNG) {
      gravou = save(this.getFormatoGravacao(), 100);
    }

    return gravou;

  }

  /** 
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
  public boolean save(CompressFormat formato, int quality) throws FileNotFoundException, IOException {

    // a foto armazenada em imagem será salva
    if (getBitmap() == null) {
      // bitmap não pode ser vazio
      return false;
    }

    if (getArquivo() == null) {
      // arquivo não pode ser vazio
      return false;
    }

    boolean salvou = false;

    File f = xxx(getArquivo());

    OutputStream out = new FileOutputStream(f);

    salvou = getBitmap().compress(formato, quality, out);

    out.close();

    return salvou;

  }

  /**
   * @return retorna uma URI identificando o bitmap
   * 
   */
  public Uri getUri() {

    File f = xxx(getArquivo());

    Uri uri = Uri.fromFile(f);

    return uri;
  }

  /**
   * @param largura
   * @param altura
   * 
   * @return a foto redimensionada ou null em caso de erro
   */
  public FotoBitmap redimensiona(int largura, int altura) {

    boolean filter = true;

    // Creates a new bitmap, scaled from an existing bitmap
    Bitmap bitmap = Bitmap.createScaledBitmap(getBitmap(), largura, altura, filter);

    FotoBitmap f = new FotoBitmap(null, bitmap);

    return f;

  }
  
  
  // ---------------------------------------------------------------------------------------
  //
  //
  // ---------------------------------------------------------------------------------------

  /**
   * @return
   */
  public String getArquivo() {
    return arquivo;
  }

  /**
   * @param arquivo
   */
  public void setArquivo(String arquivo) {
    this.arquivo = arquivo;
  }

  /**
   * @return
   */
  public Bitmap getBitmap() {
    return bitmap;
  }

  /**
   * @param bitmap
   */
  public void setBitmap(Bitmap bitmap) {
    this.bitmap = bitmap;
  }

  /**
   * @return
   */
  public byte[] getDados() {

    ByteArrayOutputStream bos = new ByteArrayOutputStream();

    if (getBitmap() != null) {
      getBitmap().compress(CompressFormat.PNG, 100, bos);
      this.dados = bos.toByteArray();
    }

    if (getDados() != null) {
      Log.d(TAG, "getDados() - nº de bytes: " + getDados().length);
    }

    return this.dados;

  }

  /**
   * @param dados
   * 
   */
  public void setDados(byte[] dados) {
    this.dados = dados;
  }

  /**
   * @return
   */
  public Bitmap.CompressFormat getFormatoGravacao() {
    return formatoGravacao;
  }

  /**
   * @param formatoGravacao
   */
  public void setFormatoGravacao(Bitmap.CompressFormat formatoGravacao) {
    this.formatoGravacao = formatoGravacao;
  }

  // ---------------------------------------------------------------------------------------
  //
  //
  // ---------------------------------------------------------------------------------------
  private File xxx(String arquivo) {

    if (arquivo == null) {
      return null;
    }

    File f = new File(arquivo);

    return f;

  }

}
