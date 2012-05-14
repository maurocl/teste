package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public class ManipulaImagem {

  private static final String TAG = "ManipulaImagem";

  /**
   * processaFotoFormatoPolaroid(File f, File moldura)
   * 
   * @param f
   * @param moldura
   * @return
   */
  public Bitmap processaFotoFormatoPolaroid(File f, File moldura) {

    return null;
  }

  /**
   * processaFotoFormatoCabine(File f1, File f2, File f3, File moldura)
   * 
   * @param f1
   * @param f2
   * @param f3
   * @param moldura
   * 
   * @return
   */
  public Bitmap processaFotoFormatoCabine(File f1, File f2, File f3, File moldura) {

    return null;
  }

  /**
   * adicionaMolduraFoto(Bitmap bm, File moldura)
   * 
   * @param bm
   * @param moldura
   * @return
   */
  public Bitmap adicionaMolduraFoto(Bitmap bm, File moldura) {
    return null;
  }

  /**
   * getBitmapFromFile(String filename)
   * 
   * @param filename
   * 
   * @return the resulting decoded bitmap, or null if it could not be decoded.
   */
  public Bitmap getBitmapFromFile(String filename) {

    Bitmap bm = null;

    File f = new File(filename);

    if (f != null) {

      showFile(f);

      bm = BitmapFactory.decodeFile(f.getAbsolutePath());

    }

    return bm;

  }

  /**
   * getBitmapFromFile(File f)
   * 
   * @param f
   * 
   * @return
   */
  public Bitmap getBitmapFromFile(File f) {

    Bitmap bm = null;

    if (f != null) {

      bm = getBitmapFromFile(f.getAbsoluteFile());

    }

    return bm;

  }

  /**
   * getBitmapFromByteArray(byte[] data)
   * 
   * Parameters: data byte array of compressed image data offset offset into
   * imageData for where the decoder should begin parsing. length the number of
   * bytes, beginning at offset, to parse
   * 
   * @param
   * @return data The decoded bitmap, or null if the image could not be decode.
   */
  public Bitmap getBitmapFromByteArray(byte[] data) {

    Bitmap bm = null;

    if (data != null) {

      int offset = 0;
      int length = data.length;
      bm = BitmapFactory.decodeByteArray(data, offset, length);

    }

    return bm;

  }

  /**
   * getBitmapFromResource(Resources res, int id)
   * 
   * Exemplo de uso: getBitmapFromResource(getResources(), R.drawable.foto1)
   * 
   * @param res
   *          Resource
   * @param id
   *          Resource ID
   * 
   * @return
   */
  public Bitmap getBitmapFromResource(Resources res, int id) {

    // Bitmap bm = BitmapFactory.decodeResource(getResources(),
    // R.drawable.foto1);
    Bitmap bm = BitmapFactory.decodeResource(res, id);

    // text.setText(Integer.toString(bMap.getWidth()) + " x " +
    // Integer.toString(bMap.getHeight()));
    Log.d(TAG, Integer.toString(bm.getWidth()) + " x " + Integer.toString(bm.getHeight()));

    return bm;
  }

  /**
   * getScaledBitmap(Bitmap bm, int factor)
   * 
   * @param bm
   *          Bitmap
   * @param factor
   *          fator de redução (0 a 100)
   * 
   * @return um Bitmap de tamanho reduzido
   */
  public Bitmap getScaledBitmap(Bitmap bm, int factor) {

    int dstWidth = bm.getWidth() * factor / 100;
    int dstHeight = bm.getHeight() * factor / 100;

    boolean filter = true;

    Bitmap bitmap = Bitmap.createScaledBitmap(bm, dstWidth, dstHeight, filter);

    showBitmapInfo(bitmap);

    return bitmap;

  }

  /**
   * getRotatedBitmap(Resources res, int id, int angle)
   * 
   * @param res
   * @param id
   * @param angle
   * 
   * @return
   */
  public Bitmap getRotatedBitmap(Resources res, int id, int angle) {

    Bitmap bm = BitmapFactory.decodeResource(res, id);

    Matrix mat = new Matrix();

    mat.postRotate(angle);

    Bitmap bMapRotate = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), mat, true);

    return bMapRotate;

  }

  /**
   * extractAlpha(Bitmap bm)
   * 
   * @param bm
   * 
   * @return
   */
  public Bitmap extractAlpha(Bitmap bm) {

    if (bm.hasAlpha()) {
      Log.d(TAG, "Bitmap has alpha()");
    } else {
      Log.d(TAG, "Bitmap hasn't alpha()");
    }

    Bitmap b = bm.extractAlpha();

    return b;

  }

  /**
   * updateBitmap(Bitmap bm, ImageView image)
   * 
   * @param bm
   * @param image
   * 
   */
  public void updateBitmap(Bitmap bm, ImageView image) {

    if ((bm != null) && (image != null)) {

      image.setImageBitmap(bm);

      showBitmapInfo(bm);

    } else {

      Log.d(TAG, "Bitmap is null");

    }

  }

  /**
   * overlay
   * 
   * Desenha o bitmap 1 e depois o bitmap 2.
   * 
   * Na prática, o bitmap2 irá sobrepor o bitmap 1
   * 
   * @param bmp1
   *          Bitmap de fundo (background)
   * @param bmp2
   *          Bitmap de frente (foreground)
   * 
   * @return um Bitmap com overlay
   * 
   */
  public Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {

    if ((bmp1 == null) || (bmp2 == null)) {
      return null;
    }

    // cria um novo bitmap com as dimensões e configuração do do bitmap1
    Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());

    Canvas canvas = new Canvas(bmOverlay);

    canvas.drawBitmap(bmp1, new Matrix(), null);
    canvas.drawBitmap(bmp2, new Matrix(), null);

    return bmOverlay;

  }

  public Bitmap overlay2(Bitmap bmp1, Bitmap bmp2) {

    // Bitmap bmOverlay = Bitmap.createBitmap(500, 500, bmp1.getConfig());

    // cria um novo bitmap onde será feito o overlay
    Bitmap bmOverlay = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);

    Bitmap.Config bmConfig = bmp1.getConfig();

    Log.d(TAG, "bitmap config: " + bmConfig.name());

    Canvas canvas = new Canvas(bmOverlay);

    canvas.drawColor(Color.BLUE);

    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

    canvas.drawBitmap(bmp1, new Matrix(), paint);
    // canvas.drawBitmap(bmp2, new Matrix(), paint);

    // canvas.drawColor(Color.argb(160, 21, 140, 21));

    canvas.drawColor(Color.argb(30, 0, 21, 21));

    paint.setAlpha(255);

    canvas.drawBitmap(bmp2, new Matrix(), paint);

    // canvas.drawCircle(250, 250, 200, paint);

    return bmOverlay;

  }

  /**
   * overlay3(Bitmap bmp1, Bitmap bmp2)
   * 
   * @param bmp1
   * @param bmp2
   * 
   * @return
   * 
   */
  public Bitmap overlay3(Bitmap bmp1, Bitmap bmp2) {

    int w = 0;
    int h = 0;

    w = bmp1.getWidth();
    h = bmp1.getHeight();

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

    Bitmap.Config bmConfig = bmp1.getConfig();

    Log.d(TAG, "bitmap config: " + bmConfig.name());

    Canvas canvas = new Canvas(bmOverlay);

    canvas.drawColor(Color.BLUE);

    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

    canvas.drawBitmap(bmp1, new Matrix(), paint);
    // canvas.drawBitmap(bmp2, new Matrix(), paint);

    // canvas.drawColor(Color.argb(160, 21, 140, 21));

    canvas.drawColor(Color.argb(30, 0, 21, 21));

    paint.setAlpha(255);

    canvas.drawBitmap(bmp2, new Matrix(), paint);

    // canvas.drawCircle(250, 250, 200, paint);

    return bmOverlay;

  }

  /**
   * combineImages(Bitmap c, Bitmap s)
   * 
   * @param c
   *          Bitmap c
   * @param s
   *          Bitmap s
   * 
   * @return A Bitmap
   */
  public Bitmap combineImages(Bitmap c, Bitmap s) {

    Bitmap cs = null;

    int width, height = 0;

    if (c.getWidth() > s.getWidth()) {

      width = c.getWidth();
      height = c.getHeight();

    } else {

      width = s.getWidth() + s.getWidth();
      height = c.getHeight();

    }

    cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

    Canvas comboImage = new Canvas(cs);

    comboImage.drawBitmap(c, 0, 0, null);
    comboImage.drawBitmap(s, 100, 300, null);

    /******
     * 
     * Write file to SDCard
     * 
     * ****/

    String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";

    OutputStream os = null;

    try {
      os = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + tmpImg);
      cs.compress(CompressFormat.PNG, 100, os);
    } catch (IOException e) {
      Log.e(TAG, "problem combining images", e);
    }

    return cs;

  }

  /**
   * criaBitmap(Uri uri)
   * 
   * @param uri
   *          Uri do bitmap
   * 
   * @return um Bitmap
   * 
   */
  public Bitmap criaBitmap(Uri uri) {

    if (uri == null) {
      Log.d(TAG, "criaBitmap - uri é null");
      return null;
    }

    // cria-se um arquivo
    File file = new File(uri.getPath());

    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

    if (bitmap != null) {

      // mMyBitmap = bitmap;
      // mMyFilename = file.getAbsolutePath();

      int w = bitmap.getWidth();
      int h = bitmap.getHeight();

      Log.d(TAG, "bitmap.w=" + w + ", bitmap.h=" + h);

      processa(bitmap, file.getAbsolutePath());

    }

    return bitmap;

  }

  /**
   * processa(Bitmap bitmap, String absolutePath)
   * 
   * @param bitmap
   * @param absolutePath
   * 
   */
  public void processa(Bitmap bitmap, String absolutePath) {
    // TODO Auto-generated method stub

  }

  /**
   * showFile(File f)
   * 
   * Exibe informações sobre um arquivo.
   * 
   * @param f
   *          Nome do arquivo
   * 
   */
  public void showFile(File f) {

    if (f != null) {
      Log.i(TAG, "getName()=" + f.getName());
      Log.i(TAG, "getAbsolutePath()=" + f.getAbsolutePath());
      Log.i(TAG, "getPath()=" + f.getPath());
    }

  }

  /**
   * showImageViewInfo(final ImageView image)
   * 
   * @param image
   * 
   */
  public void showImageViewInfo(final ImageView image) {

    int bottom = image.getBottom();
    int top = image.getTop();
    int left = image.getLeft();
    int right = image.getRight();

    Log.i(TAG, "b=" + bottom + ", top=" + top + ", left=" + left + ", right=" + right);

    Drawable d = image.getBackground();
    if (d != null) {
      int w = d.getIntrinsicWidth();
      int h = d.getIntrinsicHeight();
      Log.i(TAG, "w=" + w + ", h=" + h);
    }

  }

  /**
   * showBitmapInfo(Bitmap bm)
   * 
   * @param bm
   *          The source bitmap
   */
  public void showBitmapInfo(Bitmap bm) {

    if (bm != null) {

      Log.i(TAG, "Bitmap Info:");
      Log.i(TAG, "  w=" + bm.getWidth() + ", h=" + bm.getHeight());
      Log.i(TAG, "  getDensity()=" + bm.getDensity());
      Log.d(TAG, "  getRowBytes()=" + bm.getRowBytes());
      Log.d(TAG, "  isMutable=" + bm.isMutable());

    } else {

      Log.w(TAG, "Bitmap is null");

    }

  }

  /**
   * x(ImageView imageview)
   * 
   * @param imageview
   */
  private void x(ImageView imageview) {

    ClipDrawable drawable = (ClipDrawable) imageview.getDrawable();

    if (drawable != null) {
      drawable.setLevel(drawable.getLevel() + 1000);
    } else {
      Log.d(TAG, "drawable is null !!!");
    }

  }

  /**
   * 
   * @param cs
   * @return
   */
  public Bitmap XXX(Bitmap cs) {

    // Write file to SDCard

    String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";

    OutputStream os = null;

    try {

      os = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + tmpImg);

      /*
       * Write a compressed version of the bitmap to the specified outputstream.
       * If this returns true, the bitmap can be reconstructed by passing a
       * corresponding inputstream to BitmapFactory.decodeStream().
       * 
       * Note: not all Formats support all bitmap configs directly, so it is
       * possible that the returned bitmap from BitmapFactory could be in a
       * different bitdepth, and/or may have lost per-pixel alpha (e.g. JPEG
       * only supports opaque pixels).
       */

      cs.compress(CompressFormat.PNG, 100, os);

    } catch (IOException e) {

      Log.e(TAG, "problem combining images", e);

    }

    return cs;

  }

  /**
   * XXX1(Bitmap bitmap, String filename)
   * 
   * @param bitmap
   * @param filename
   * @return
   */
  public Bitmap XXX1(Bitmap bitmap, String filename) {

    OutputStream fos = null;

    /* 
     * Standard directory in which to place pictures that are available to the
     * user. Note that this is primarily a convention for the top-level public
     * directory, as the media scanner will find and collect pictures in any
     * directory.
     */
    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

    File file = new File(filename);

    File completePath = new File(dir+"/"+file);
    
    Log.d(TAG,"Gravando o bitmap em: "+completePath.getAbsolutePath());
    
    //os = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + tmpImg);
    
    try {
      fos = new FileOutputStream(completePath);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    /*
     * Write a compressed version of the bitmap to the specified outputstream.
     * If this returns true, the bitmap can be reconstructed by passing a
     * corresponding inputstream to BitmapFactory.decodeStream().
     * 
     * Note: not all Formats support all bitmap configs directly, so it is
     * possible that the returned bitmap from BitmapFactory could be in a
     * different bitdepth, and/or may have lost per-pixel alpha (e.g. JPEG only
     * supports opaque pixels).
     */

    bitmap.compress(CompressFormat.PNG, 100, fos);

    return bitmap;

  }

}
