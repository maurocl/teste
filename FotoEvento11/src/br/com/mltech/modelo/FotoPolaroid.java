package br.com.mltech.modelo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

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
  
  public static final int TIPO_FOTO_POLAROID = 1;
  
  // Foto
  private Foto foto;

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
   * @param f
   */
  public FotoPolaroid(Foto f) {
    this(f, null);
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
   * Executa o overlay para fotos no formato polaroid
   * 
   * @param bmFoto
   *          foto
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

    //Log.v(TAG, "overlay4() - nova imagem criada: " + w + " x " + h);

    //Bitmap.Config bmConfig = bmp1.getConfig();

    //Log.v(TAG, "overlay4() - bitmap config: " + bmConfig.name());

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
    return this.getFoto()!=null;
  }

  /**
   * hasMoldura()
   * 
   * @return
   * 
   */
  public boolean hasMoldura() {
    return this.getMoldura()!=null;
  }

  
}
