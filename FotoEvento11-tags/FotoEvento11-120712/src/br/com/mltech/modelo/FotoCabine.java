package br.com.mltech.modelo;

import java.util.Arrays;

import br.com.mltech.utils.ManipulaImagem;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

/**
 * FotoCabine
 * 
 * @author maurocl
 * 
 */
public class FotoCabine {

  public static final String TAG = "FotoCabine";

  // moldura
  static private Moldura moldura;

  // array de fotos
  static private Foto[] fotos = new Foto[3];
  
  /**
   * construtor default
   */
  public FotoCabine() {
    
  }
  
  /**
   * FotoCabine(String arquivo, Bitmap file1, Bitmap file2, Bitmap file3)
   * 
   * Construtor vazio
   * 
   * @param arquivo
   * @param file1
   * @param file2
   * @param file3
   * 
   */
  public FotoCabine(String arquivo, Bitmap file1, Bitmap file2, Bitmap file3) {

  }

  /**
   * FotoCabine(Bitmap moldura, Bitmap file1, Bitmap file2, Bitmap file3)
   * 
   * Construtor vazio
   * 
   * @param moldura
   * @param file1
   * @param file2
   * @param file3
   * 
   */
  public FotoCabine(Bitmap moldura, Bitmap file1, Bitmap file2, Bitmap file3) {

    // fotos[0] = new Foto(file1, file2, file3);
    //this.moldura = moldura;
    
  }

 
  
  /**
   * FotoCabine(Moldura moldura, Bitmap file1, Bitmap file2, Bitmap file3)
   * 
   * Construtor vazio
   * 
   * @param moldura
   * @param file1
   * @param file2
   * @param file3
   * 
   */
  public FotoCabine(Moldura moldura, Bitmap file1, Bitmap file2, Bitmap file3) {

    this.moldura = moldura;
    
  }

  /**
   * getBitmap(int i)
   * 
   * @param i
   * 
   * @return
   */
  Bitmap getBitmap(int i) {

    return fotos[i].getImagem();
  }

  /**
   * getFotos()
   * 
   * @return um array de Foto(s)
   * 
   */
  public Foto[] getFotos() {

    return fotos;
  }

  /**
   * getFoto(int i)
   * 
   * Retorna a foto na posição i do array
   * 
   * @param i
   *          índice da foto no array
   * 
   * @return A foto na posição i do array ou null.
   * 
   */
  public Foto getFoto(int i) {

    if (i < getFotos().length) {
      return fotos[i];
    } else {
      return null;
    }

  }

  /**
   * setFotos(Foto[] fotos)
   * 
   * @param fotos
   * 
   */
  public void setFotos(Foto[] fotos) {

    this.fotos = fotos;
  }

  /**
   * getMoldura()
   * 
   * @return uma instância da classe moldura
   * 
   */
  public Moldura getMoldura() {

    return moldura;
  }

  /**
   * setMoldura(Moldura moldura)
   * 
   * @param moldura
   * 
   */
  public void setMoldura(Moldura moldura) {

    this.moldura = moldura;
  }

  /**
   * setFoto(int i, Foto f)
   * 
   * @param i
   *          atualiza a foto dada pelo indice i (0 a 2)
   * @param f
   *          foto
   * 
   */
  public void setFoto(int i, Foto f) {

    fotos[i] = f;
  }

  /**
   * overlay(Bitmap bmp1, Bitmap bmp2)
   * 
   * @param bmp1
   *          Imagem de fundo (representa a foto)
   * @param bmp2
   *          Imagem de frente (representa a moldura)
   * 
   * @return Um bitmap contendo a imagem de fundo sobreposta da imagem de frente
   * 
   */
  public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {

    int w = 0;
    int h = 0;

    if (bmp1.getWidth() > bmp2.getWidth()) {
      w = bmp1.getWidth();
    } else {
      w = bmp2.getWidth();
    }

    if (bmp1.getHeight() > bmp2.getHeight()) {
      h = bmp1.getHeight();
    } else {
      h = bmp2.getHeight();
    }

    // w = bmp1.getWidth();
    // h = bmp1.getHeight();

    /*
     * Possible bitmap configurations. A bitmap configuration describes how
     * pixels are stored. This affects the quality (color depth) as well as the
     * ability to display transparent/translucent colors.
     */

    /*
     * Each pixel is stored on 4 bytes. Each channel (RGB and alpha for
     * translucency) is stored with 8 bits of precision (256 possible values.)
     * This configuration is very flexible and offers the best quality. It
     * should be used whenever possible.
     */

    // cria um novo bitmap onde será feito o overlay
    Bitmap bmOverlay = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

    // Log.v(TAG, "overlay3() - nova imagem criada: " + w + " x " + h);

    Bitmap.Config bmConfig = bmp1.getConfig();

    // Log.v(TAG, "overlay3() - bitmap config: " + bmConfig.name());

    Canvas canvas = new Canvas(bmOverlay);

    canvas.drawColor(Color.GRAY);

    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

    Matrix m = new Matrix();

    m.setTranslate(10, 10);

    // desenha a imagem de fundo (backgroud) - foto
    canvas.drawBitmap(bmp1, m, paint);

    // canvas.drawBitmap(bmp2, new Matrix(), paint);

    // canvas.drawColor(Color.argb(160, 21, 140, 21));

    // canvas.drawColor(Color.argb(30, 0, 21, 21));

    paint.setAlpha(255);

    m.setTranslate(0, 0);

    // desenha a imagem de frente (foreground) - moldura
    canvas.drawBitmap(bmp2, m, paint);

    // canvas.drawCircle(250, 250, 200, paint);

    return bmOverlay;

  }

  /**
   * hasFotos()
   * 
   * @return true se todas as fotos estiverem tirardas
   * 
   */
  public boolean hasFotos() {
// TODO esse método precisa ser pensado melhor
    return this.getFotos() != null;
  }

  /**
   * hasMoldura()
   * 
   * @return true se houver uma moldura associada a foto ou false caso contrário.
   * 
   */
  public boolean hasMoldura() {

    return this.getMoldura() != null;
  }

  /**
   * formataFoto3x4(Bitmap foto, String nome)
   * 
   * Redimensiona uma foto para o tamanho 3x4
   * 
   * A foto será armazenada no diretório indicado pela constante PATH_FOTOS
   * 
   * @param foto
   *          bitmap com a foto
   * 
   * @param nome
   *          nome do arquivo onde a foto será gravada (não incluir o diretório)
   * 
   * @return o bitmap com a foto ou null em caso de erro
   * 
   */
  Bitmap formataFoto3x4(Bitmap foto, String nome) {

    // redimensiona a foto foto para 3x4
    Bitmap fotoRedimensionada = ManipulaImagem.getScaledBitmap2(foto, 113, 151);

    if (fotoRedimensionada == null) {
      Log.w(TAG, "formataFoto3x4() - erro no redimensionamento da foto");
      return null;
    }

    String PATH_FOTOS = "";

    // Define o nome da foto redimensionada
    String nomeArquivo = PATH_FOTOS + nome;

    // grava a foto redimensionada em um arquivo
    boolean gravou = ManipulaImagem.gravaBitmapArquivo(fotoRedimensionada, nomeArquivo);

    if (gravou) {
      // foto armazenada com sucesso
      return fotoRedimensionada;

    } else {
      // falha na gravação da foto
      Log.w(TAG, "formataFoto3x4() - falha na gravação da foto");
      return null;

    }

  }

  /**
   * toString()
   * 
   */
  @Override
  public String toString() {

    return "FotoCabine [moldura=" + moldura + ", fotos=" + Arrays.toString(fotos) + "]";
  }

}
