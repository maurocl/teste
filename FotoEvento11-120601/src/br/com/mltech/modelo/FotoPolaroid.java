package br.com.mltech.modelo;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import br.com.mltech.utils.FileUtils;
import br.com.mltech.utils.ManipulaImagem;

/**
 * FotoPolaroid
 * 
 * É uma foto no formato Polaroid.
 * 
 * Moldura 11x9 cm e Foto 8x8 cm
 * 
 * @author maurocl
 * 
 */
public class FotoPolaroid {

  public static final String TAG = "FotoPolaroid";

  public static final String PATH_MOLDURAS = "/mnt/sdcard/Pictures/fotoevento/molduras/";
  public static final String PATH_FOTOS = "/mnt/sdcard/Pictures/fotoevento/fotos/";

  // Foto
  private Foto foto;

  private Foto foto9x12;

  private Foto foto8x8;

  // Moldura
  private Moldura moldura;

  /**
   * FotoPolaroid(Foto f, Moldura m)
   * 
   * @param f
   * @param m
   */
  public FotoPolaroid(Foto f, Moldura m) {
    this.foto = f;
    this.moldura = m;
  }

  /**
   * FotoPolaroid(Foto f)
   * 
   * @param f
   */
  public FotoPolaroid(Foto f) {
    this(f, null);
  }

  /**
   * FotoPolaroid(String arquivo)
   * 
   * @param arquivo
   */
  public FotoPolaroid(String arquivo) {

    Foto foto = new Foto(arquivo);

    this.foto = foto;
    this.moldura = null;

  }

  /**
   * getMoldura()
   * 
   * @return
   */
  public Moldura getMoldura() {
    return moldura;
  }

  /**
   * setMoldura(Moldura moldura)
   * 
   * @param moldura
   */
  public void setMoldura(Moldura moldura) {
    this.moldura = moldura;
  }

  /**
   * getFoto()
   * 
   * @return
   */
  public Foto getFoto() {
    return foto;
  }

  /**
   * setFoto(Foto foto)
   * 
   * @param foto
   */
  public void setFoto(Foto foto) {
    this.foto = foto;
  }

  /**
   * overlay
   * 
   * Executa o overlay para fotos no formato Polaroid
   * 
   * @param bmFoto
   *          foto
   * 
   * @param bmMoldura
   *          moldura
   * 
   * @return Bitmap
   */
  public Bitmap overlay(Bitmap bmFoto, Bitmap bmMoldura) {

    int w = 0; // maior largura
    int h = 0; // maior altura

    // obtém a maior largura
    if (bmFoto.getWidth() > bmMoldura.getWidth()) {
      w = bmFoto.getWidth();
    } else {
      w = bmMoldura.getWidth();
    }

    // obtem a maior altura
    if (bmFoto.getHeight() > bmMoldura.getHeight()) {
      h = bmFoto.getHeight();
    } else {
      h = bmMoldura.getHeight();
    }

    // cria um novo bitmap onde será feito o overlay
    Bitmap bmOverlay = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

    // Log.v(TAG, "overlay4() - nova imagem criada: " + w + " x " + h);

    // Bitmap.Config bmConfig = bmp1.getConfig();

    // Log.v(TAG, "overlay4() - bitmap config: " + bmConfig.name());

    Canvas canvas = new Canvas(bmOverlay);

    // Preenche o canvas com a color cinza
    canvas.drawColor(Color.GRAY);

    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

    Matrix m = new Matrix();

    m.setTranslate(10, 10);

    // desenha a imagem de fundo (backgroud) - foto
    canvas.drawBitmap(bmFoto, m, paint);

    paint.setAlpha(255);

    m.setTranslate(0, 0);

    // desenha a imagem de frente (foreground) - moldura
    canvas.drawBitmap(bmMoldura, m, paint);

    return bmOverlay;

  }

  /**
   * hasFoto()
   * 
   * @return
   * 
   */
  public boolean hasFoto() {
    return this.getFoto() != null;
  }

  /**
   * hasMoldura()
   * 
   * @return
   * 
   */
  public boolean hasMoldura() {
    return this.getMoldura() != null;
  }

  /**
   * formatar(Foto original)
   * 
   * Formata uma foto para o formato Polaroid contendo uma foto 9x12 com moldura
   * e uma foto 8x8.
   * 
   * @param fotoOriginal
   * 
   * @return
   * 
   */
  public String formatar() {

    if (foto == null) {
      Log.w(TAG, "formatar() - foto está vazia");
      return null;
    }

    // obtem a URI da foto original
    Uri uriFotoOriginal = foto.getUri();

    // Cria um bitmap a partir da Uri da foto
    Bitmap bmFotoOriginal = ManipulaImagem.criaBitmap(uriFotoOriginal);

    // Exibe informações a respeito da foto
    ManipulaImagem.showBitmapInfo(bmFotoOriginal);

    // --------------------------------------------------------------------------
    // redimensiona a foto original para 9x12 para manter a proporção 3:4
    // --------------------------------------------------------------------------

    Bitmap bmFoto9x12 = ManipulaImagem.getScaledBitmap2(foto.getImagem(), 340, 454);

    // Define o nome da foto redimensionada
    String nomeArquivo = PATH_FOTOS + FileUtils.getFilename(uriFotoOriginal) + "_9x12.png";

    // cria uma foto padrão 9x12
    foto9x12 = new Foto(nomeArquivo);
    foto9x12.setImagem(bmFoto9x12);

    boolean gravou = false;

    try {

      gravou = foto9x12.gravar();

    } catch (FileNotFoundException e2) {

      e2.printStackTrace();
    } catch (IOException e2) {

      e2.printStackTrace();
    }

    if (gravou) {

      // foto armazenada com sucesso
      Log.i(TAG, "formataFotoPolaroid() - (Foto9x12): " + nomeArquivo + " gravado com sucesso.");

    } else {

      // falha na gravação da foto
      Log.i(TAG, "formataFotoPolaroid() - (Foto9x12) - falha na gravação do arquivo: " + nomeArquivo);

    }

    // --------------------------------------------------------------------------
    // redimensiona a foto 9x12 para 8x8, isto é, copia uma "janela" 8x8 da
    // foto
    // --------------------------------------------------------------------------

    Options options = new Options();

    Rect rect = new Rect(0, 0, 302, 302);

    // Obtem um bitmap com a foto redimensionada para 8x8
    Bitmap bmFoto8x8 = ManipulaImagem.getBitmapRegion(nomeArquivo, rect, options);

    nomeArquivo = PATH_FOTOS + FileUtils.getFilename(uriFotoOriginal) + "_8x8.png";

    foto8x8 = new Foto(nomeArquivo);

    foto8x8.setImagem(bmFoto8x8);

    boolean gravou2 = false;

    try {

      gravou2 = foto8x8.gravar();

    } catch (FileNotFoundException e1) {

      e1.printStackTrace();

    } catch (IOException e1) {

      e1.printStackTrace();

    }

    if (gravou2) {

      // foto armazenada com sucesso
      Log.i(TAG, "formataFotoPolaroid() - (Foto8x8):" + nomeArquivo + " gravado com sucesso.");

    } else {

      // falha na gravação da foto
      Log.i(TAG, "formataFotoPolaroid() - (Foto8x8) - falha na gravação do arquivo: " + nomeArquivo);

    }

    Foto fotoComMoldura = new Foto(foto.getArquivo());

    Bitmap bmFotoComMoldura = overlay4(bmFoto8x8, getMoldura().getImagem());

    fotoComMoldura.setImagem(bmFotoComMoldura);

    boolean gravou4 = false;

    try {
      gravou4 = fotoComMoldura.gravar();
    } catch (FileNotFoundException e) {

      e.printStackTrace();
    } catch (IOException e) {

      e.printStackTrace();
    }

    if (gravou4) {

      // foto armazenada com sucesso
      Log.i(TAG, "formataFotoPolaroid() - (Foto8x8):" + nomeArquivo + " gravado com sucesso.");

    } else {

      // falha na gravação da foto
      Log.i(TAG, "formataFotoPolaroid() - (Foto8x8) - falha na gravação do arquivo: " + nomeArquivo);

    }

    // o nome completo do arquivo onde a foto com moldura foi armazenada
    return nomeArquivo;

  }

  /**
   * Bitmap overlay4(Bitmap bmp1, Bitmap bmp2)
   * 
   * Executa o overlay para fotos no formato polaroid
   * 
   * @param bmp1
   *          foto Foto
   * 
   * @param bmp2
   *          moldura Moldura a ser aplicada na foto
   * 
   * @return Bitmap com a sobreposição da moldura a foto.
   * 
   */
  public Bitmap overlay4(Bitmap bmp1, Bitmap bmp2) {

    int w = 0;
    int h = 0;

    // obtem a maior largura entre dois bitmaps
    if (bmp1.getWidth() > bmp2.getWidth()) {
      w = bmp1.getWidth();
    } else {
      w = bmp2.getWidth();
    }

    // obtem a maior altura entre dois bitmap
    if (bmp1.getHeight() > bmp2.getHeight()) {
      h = bmp1.getHeight();
    } else {
      h = bmp2.getHeight();
    }

    // cria um novo bitmap de tamanho w x h onde será feito o overlay
    Bitmap bmOverlay = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

    Log.v(TAG, "overlay4() - novo bitmap criado: " + w + " x " + h);

    // obtém as configurações do bitmap bmp1 (foto)
    Bitmap.Config bmConfig = bmp1.getConfig();

    // exibe o nome da configuração
    Log.v(TAG, "overlay4() - bitmap config: " + bmConfig.name());

    // cria um canvas (área para execução de desenhos) no bitmap criado.
    Canvas canvas = new Canvas(bmOverlay);

    // Preenche o canvas com a color cinza
    canvas.drawColor(Color.GRAY);

    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

    Matrix m = new Matrix();

    // m.setTranslate(58, 24);
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

    return bmOverlay;

  }

  /**
   * overlay4(Foto foto, Moldura moldura)
   * 
   * @param foto
   * @param moldura
   * 
   * @return
   * 
   */
  public Bitmap overlay4(Foto foto, Moldura moldura) {

    if (foto == null) {
      Log.d(TAG, "foto está vazia");
      return null;
    }

    if (moldura == null) {
      Log.d(TAG, "moldura está vazia");
      return null;
    }

    Bitmap bmp1 = foto.getImagem();
    Bitmap bmp2 = moldura.getImagem();

    return overlay4(bmp1, bmp2);

  }

  
  /**
   * toString()
   */
  @Override
  public String toString() {
    return "FotoPolaroid [foto=" + foto + ", foto9x12=" + foto9x12 + ", foto8x8=" + foto8x8 + ", moldura=" + moldura + "]";
  }

  
  public Foto getFoto9x12() {
    return foto9x12;
  }

  
  public void setFoto9x12(Foto foto9x12) {
    this.foto9x12 = foto9x12;
  }

  
  public Foto getFoto8x8() {
    return foto8x8;
  }

  
  public void setFoto8x8(Foto foto8x8) {
    this.foto8x8 = foto8x8;
  }

  
  
}
