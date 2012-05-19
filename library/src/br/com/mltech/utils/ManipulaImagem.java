package br.com.mltech.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

/**
 * ManipulaImagem.java
 * 
 * @author maurocl
 * 
 */
public class ManipulaImagem {

  private static final String TAG = "ManipulaImagem";

  private static final float SCALED_IMAGE_MAX_DIMENSION = 302f;

  /**
   * processaFotoFormatoPolaroid(File f, File moldura)
   * 
   * @param foto
   *          Arquivo que possui a foto
   * @param moldura
   *          Arquivo que possui a moldura
   * 
   * @return Um bitmap contendo a foto com a moldura sobreposta
   */
  public Bitmap processaFotoFormatoPolaroid(File foto, File moldura) {

    Bitmap bmp1 = null;
    Bitmap bmp2 = null;

    bmp1 = getBitmapFromFile(foto); // foto
    bmp2 = getBitmapFromFile(moldura); // moldura

    Bitmap bitmap = overlay3(bmp1, bmp2); // cria um novo bitmap com a moldura
                                          // sobreposta a foto

    if (bitmap == null) {
      Log.w(TAG, "processaFotoFormatoPolaroid() - erro na conversão da foto");
    }

    return bitmap;

  }

  /**
   * processaFotoFormatoPolaroid(String sFilename, String sMoldura)´
   * 
   * @param sFilename
   * @param sMoldura
   * 
   * @return
   */
  public Bitmap processaFotoFormatoPolaroid(String sFilename, String sMoldura) {

    File f = new File(sFilename);
    File moldura = new File(sMoldura);

    Bitmap bitmap = processaFotoFormatoPolaroid(f, moldura);

    if (bitmap == null) {
      Log.w(TAG, "processaFotoFormatoPolaroid() - erro na conversão da foto");
    }

    return bitmap;

  }

  /**
   * processaFotoFormatoPolaroid(Bitmap foto, File moldura)
   * 
   * @param foto
   * @param moldura
   * 
   * @return
   */
  public Bitmap processaFotoFormatoPolaroid(Bitmap foto, File moldura) {

    Bitmap bmp2 = null;

    bmp2 = getBitmapFromFile(moldura); // moldura

    Bitmap bitmap = overlay3(foto, bmp2); // cria um novo bitmap com a moldura
                                          // sobreposta a foto

    if (bitmap == null) {
      Log.w(TAG, "processaFotoFormatoPolaroid() - erro na conversão da foto");
    }

    return bitmap;

  }

  /**
   * processaFotoFormatoCabine(File f1, File f2, File f3, File moldura)
   * 
   * Retorna um bitmap composto pela concatenação das fotos f1, f2 e f3 na
   * vertical, ito é, as fotos são arranjadas na vertical sendo que a largura
   * permanece inalterada
   * 
   * @param f1
   *          Nome do arquivo de imagem 1
   * @param f2
   *          Nome do arquivo de imagem 2
   * @param f3
   *          Nome do arquivo de imagem 3
   * @param moldura
   *          Nome do arquivo contendo a moldura
   * 
   * @return um bitmap com a moldura aplicada
   */
  public Bitmap processaFotoFormatoCabine(File f1, File f2, File f3, File moldura) {

    Bitmap bmp1 = getBitmapFromFile(f1);
    Bitmap bmp2 = getBitmapFromFile(f2);
    Bitmap bmp3 = getBitmapFromFile(f3);

    Bitmap bmMoldura = getBitmapFromFile(moldura);

    Bitmap bitmap = verticalJoin(bmp1, bmp2, bmp3);

    Bitmap bm = aplicaMolduraFoto(bitmap, bmMoldura);

    if (bm == null) {
      Log.w(TAG, "processaFotoFormatoCabine() - erro na conversão da foto");
    }

    return bm;

  }

  /**
   * aplicaMolduraFoto(String foto, String moldura)
   * 
   * Cria uma imagem (bitmap) que é uma sobreposição de uma moldura em cima de
   * uma foto.
   * 
   * @param foto
   *          String contendo o caminho (path) da foto
   * @param moldura
   *          String contendo o caminho (path) da moldura
   * 
   * @return o bitmap resultante ou null em caso de erro
   */
  public Bitmap aplicaMolduraFoto(String foto, String moldura) {

    Bitmap bmOverlay = null;

    Bitmap bmFoto = getBitmapFromFile(foto);

    Bitmap bmMoldura = getBitmapFromFile(moldura);

    if (bmMoldura != null && bmFoto != null) {
      bmOverlay = overlay3(bmFoto, bmMoldura);
    }

    return bmOverlay;

  }

  /**
   * aplicaMolduraFoto(Bitmap bmFoto, Bitmap bmMoldura)
   * 
   * Cria uma imagem (bitmap) que é uma sobrteposição de uma moldura em cima de
   * uma foto.
   * 
   * @param bmFoto
   *          Bitmap contendo a foto
   * @param bmMoldura
   *          Bitmap contendo a moldura
   * 
   * @return o bitmap resultante ou null em caso de erro
   */
  public Bitmap aplicaMolduraFoto(Bitmap bmFoto, Bitmap bmMoldura) {

    Bitmap bmOverlay = null;

    if (bmMoldura != null && bmFoto != null) {
      bmOverlay = overlay3(bmFoto, bmMoldura);
    }

    return bmOverlay;

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
   * getBitmapFromFile(String filename)
   * 
   * Lê um bitmap armazenado em um arquivo localizado no SDCARD
   * 
   * @param filename
   *          Nome do arquivo (fullname)
   * 
   * @return the resulting decoded bitmap, or null if it could not be decoded.
   */
  public Bitmap getBitmapFromFile(String filename) {

    Bitmap bm = null;

    File f = new File(filename);

    if (f != null && f.exists()) {

      showFile(f);

      Log.v(TAG, "f.getAbsolutePath()=" + f.getAbsolutePath());

      bm = BitmapFactory.decodeFile(f.getAbsolutePath());

    } else {
      Log.w(TAG, "getBitmapFile(String) - não foi possível ler o arquivo: " + f.getAbsolutePath());
    }

    return bm;

  }

  /**
   * getBitmapFromFile(File f)
   * 
   * Lê um bitmap armazenado em um arquivo localizado no SDCARD
   * 
   * @param f
   *          Arquivo
   * 
   * @return o bitmap lido ou null caso haja algum erro
   */
  public Bitmap getBitmapFromFile(File f) {

    Bitmap bm = null;

    // File f = new File(filename);

    if (f != null) {

      showFile(f);

      bm = BitmapFactory.decodeFile(f.getAbsolutePath());

    } else {
      Log.w(TAG, "getBitmapFile(File) - não foi possível ler o arquivo: " + f.getAbsolutePath());
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
    Log.v(TAG, Integer.toString(bm.getWidth()) + " x " + Integer.toString(bm.getHeight()));

    return bm;
  }

  /**
   * getScaledBitmap(Bitmap bm, int factor)
   * 
   * Cria um novo bitmap, escalado (scaled) a partir do bitmap já existente.
   * 
   * @param bm
   *          Bitmap
   * @param factor
   *          fator de redução (0 a 100)
   * 
   * @return um Bitmap de tamanho escalado
   */
  public Bitmap getScaledBitmap(Bitmap bm, int factor) {

    int dstWidth = bm.getWidth() * factor / 100;
    int dstHeight = bm.getHeight() * factor / 100;

    boolean filter = true;

    // Creates a new bitmap, scaled from an existing bitmap
    Bitmap bitmap = Bitmap.createScaledBitmap(bm, dstWidth, dstHeight, filter);

    showBitmapInfo(bitmap);

    return bitmap;

  }

  /**
   * getScaledBitmap(Bitmap bm)
   * 
   * Cria um novo bitmap, escalado (scaled) a partir do bitmap já existente.
   * 
   * @param bm
   *          Bitmap original
   * 
   * @return um Bitmap de tamanho escalado
   */
  public Bitmap getScaledBitmap(Bitmap bm) {

    // BitmapDrawable d = (BitmapDrawable) getDrawable();

    Log.i(TAG, "Bitmap size:" + bm);

    Bitmap.Config config = bm.getConfig();
    Log.i(TAG, " - config = " + config);

    // Tamanho original do bitmap
    float origWidth = bm.getWidth();
    float origHeight = bm.getHeight();

    // Calcula o aspect ratio, isto é, a relação entre a largura e algura do
    // bitmap
    float aspect = origWidth / origHeight;

    Log.i(TAG, " - aspect = " + aspect);

    // Define o valor de escalonamente
    float scaledFactor = ((aspect > 1.0) ? origWidth : origHeight) / SCALED_IMAGE_MAX_DIMENSION;

    int scaledWidth = Math.round(origWidth / scaledFactor);
    int scaledHeight = Math.round(origHeight / scaledFactor);

    boolean filter = true;

    // Creates a new bitmap, scaled from an existing bitmap
    Bitmap bitmap = Bitmap.createScaledBitmap(bm, scaledWidth, scaledHeight, filter);

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
   * @return Um bitmap rotacionado
   */
  public Bitmap getRotatedBitmap(Resources res, int id, int angle) {

    Bitmap bm = BitmapFactory.decodeResource(res, id);

    Matrix matrix = new Matrix();

    matrix.postRotate(angle);

    // Cria um novo bitmap
    Bitmap bMapRotate = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);

    return bMapRotate;

  }

  /**
   * gravaBitmapArquivo(Bitmap bm, String filename)
   * 
   * Grava um bitmap em um arquivo.
   * 
   * @param bm
   *          Bitmap Bitmap contendo a imagem
   * @param filename
   *          Arquivo Nome do arquivo que conterá a imagem
   * 
   * @return true se o arquivo for gravado com sucesso ou false caso contrário.
   */
  public boolean gravaBitmapArquivo(Bitmap bm, String filename) {

    boolean salvou = false;

    if (bm == null) {
      // bitmap não pode ser vazio
      return false;
    }

    if (filename == null) {
      // arquivo não pode ser vazio
      return false;
    }

    File f = new File(filename);

    // transforma o nome do arquivo em uma URI
    URI uri = f.toURI();

    Log.v(TAG, "gravaBitmapArquivo() - uri=" + uri);

    OutputStream out = null;

    try {

      out = new FileOutputStream(filename);

      // boolean success = bm.compress(Bitmap.CompressFormat.valueOf("PNG"),
      // 100, out);
      boolean success = bm.compress(Bitmap.CompressFormat.PNG, 100, out);
      Log.i(TAG, "gravaBitmapArquivo() - sucess code from bitmap.compress: " + success);
      out.close();

      salvou = true;

    } catch (FileNotFoundException e) {
      Log.w(TAG, "gravaBitmapArquivo() - Erro na criação do arquivo", e);
    } catch (IOException e) {
      Log.w(TAG, "gravaBitmapArquivo() - Erro na criação do arquivo", e);
    }

    return salvou;

  }

  /**
   * getStringBitmapSize(Bitmap bm)
   * 
   * Retorna ums string contendo o tamanho (largura x altura) do bitmap.
   * 
   * @param bm
   *          Bitmap
   * 
   * @return o tamanho (largura x altura) do bitmap ou null caso o bitmap seja
   *         nulo
   * 
   */
  public String getStringBitmapSize(Bitmap bm) {

    String s = null;

    if (bm != null) {

      s = bm.getWidth() + "x" + bm.getHeight();
    } else {
      Log.w(TAG, "Bitmap é nulo");
    }

    return s;

  }

  /**
   * obtemMoldura(Uri uri)
   * 
   * Obtem uma moldura a partir de uma Uri
   * 
   * @param uri
   * 
   * @return um bitmap contendo a moldura
   * 
   *         TODO qual é o tamanho da moldura ???
   * 
   */
  public Bitmap obtemMoldura(Uri uri) {

    return null;

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
   *          Imagem de fundo (representa a foto)
   * @param bmp2
   *          Imagem de frente (representa a moldura)
   * 
   * @return Um bitmap contendo a imagem de fundo sobreposta da imagem de frente
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

    Log.v(TAG, "bitmap config: " + bmConfig.name());

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
   *          Bitmap s (source ???)
   * 
   * @return A Bitmap ou null caso haja algum erro
   */
  public Bitmap combineImages(Bitmap c, Bitmap s) {

    Bitmap cs = null;

    // largura e altura do novo bitmap
    int width, height = 0;

    if (c.getWidth() > s.getWidth()) {
      // obtém a maior largura
      width = c.getWidth();
      height = c.getHeight();

    } else {
      // obtem a nova largura com sendo a soma
      width = s.getWidth() + s.getWidth();
      height = c.getHeight();

    }

    // cria o novo bitmap com as medidas calculadas anteriormnete
    cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

    // Obtem um cancas para poder desenhar no bitmap
    Canvas comboImage = new Canvas(cs);

    // desenha o bitmap c
    comboImage.drawBitmap(c, 0, 0, null);

    // desenha o bitmap s
    comboImage.drawBitmap(s, 100, 300, null);

    /******
     * 
     * Write file to SDCard
     * 
     * ****/

    // cria um nome de arquivo
    String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";

    OutputStream os = null;

    try {
      // grava p arquivo contendo o bitmap
      os = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + tmpImg);
      // grava o bitmap no file stream de saída
      cs.compress(CompressFormat.PNG, 100, os);
    } catch (IOException e) {
      Log.e(TAG, "Houve algum problema ao combinar as imagens", e);
    }

    // retorna o novo bitmap criado
    return cs;

  }

  /**
   * criaBitmap(Uri uri)
   * 
   * Cria um bitmap a partir de uma Uri
   * 
   * @param uri
   *          Uri do bitmap
   * 
   * @return um Bitmap ou null caso não seja possível criar o bitmap
   * 
   */
  public Bitmap criaBitmap(Uri uri) {

    if (uri == null) {
      Log.d(TAG, "criaBitmap() - uri é null");
      return null;
    }

    // cria-se um arquivo
    File file = new File(uri.getPath());

    // cria um bitmap a partir do arquivo
    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
  
    Log.v(TAG, getStringBitmapSize(bitmap));

    return bitmap;

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

    Log.v(TAG, "showFile():");
    if (f != null) {
      Log.v(TAG, "  getName()=" + f.getName());
      Log.v(TAG, "  getAbsolutePath()=" + f.getAbsolutePath());
      Log.v(TAG, "  getPath()=" + f.getPath());
    }

  }

  /**
   * showImageViewInfo(final ImageView image)
   * 
   * Exibe informações sobre o objeto ImageView
   * 
   * @param imageView
   * 
   */
  public void showImageViewInfo(final ImageView imageView) {

    int bottom = imageView.getBottom();
    int top = imageView.getTop();
    int left = imageView.getLeft();
    int right = imageView.getRight();

    Log.v(TAG, "bottom=" + bottom + ", top=" + top + ", left=" + left + ", right=" + right);

    // Obtem informações sobre o fundo do ImagemView
    Drawable d = imageView.getBackground();

    if (d != null) {
      int w = d.getIntrinsicWidth();
      int h = d.getIntrinsicHeight();
      Log.v(TAG, "w=" + w + ", h=" + h);
    }

  }

  /**
   * showBitmapInfo(Bitmap bm)
   * 
   * Exibe o tamanho (largura x altura), densidade de pixels, etc
   * 
   * @param bm
   *          The source bitmap
   */
  public void showBitmapInfo(Bitmap bm) {

    if (bm != null) {

      Log.v(TAG, "showBitmapInfo(Bitmap) - Bitmap Info:");
      Log.v(TAG, "  w=" + bm.getWidth() + ", h=" + bm.getHeight());
      Log.v(TAG, "  getDensity()=" + bm.getDensity());
      Log.v(TAG, "  getRowBytes()=" + bm.getRowBytes());
      Log.v(TAG, "  isMutable=" + bm.isMutable());

    } else {

      Log.w(TAG, "Bitmap is null");

    }

  }

  /**
   * 
   * Cria uma string com o número que indica a data e hora atual em milisegundos
   * Obtém o diretório padrão onde são criados as figuras (pictures)
   * 
   * @param bmp
   * 
   * @return Retorna o bitmap gravado
   */
  public Bitmap XXX(Bitmap bmp) {

    String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";

    OutputStream fos = null;

    try {

      // Write file to SDCard
      fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + tmpImg);

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

      boolean b = bmp.compress(CompressFormat.PNG, 100, fos);

      if (b) {
        // o bitmap poderá ser reconstruido
      } else {
        // o bitmap não poderá ser reconstruido
      }

    } catch (IOException e) {

      Log.e(TAG, "problem combining images", e);

    }

    return bmp;

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

    File completePath = new File(dir + "/" + file);

    Log.d(TAG, "Gravando o bitmap em: " + completePath.getAbsolutePath());

    // os = new FileOutputStream(Environment.getExternalStorageDirectory() + "/"
    // + tmpImg);

    try {
      fos = new FileOutputStream(completePath);
    } catch (FileNotFoundException e) {
      Log.w(TAG, "Arquivo: " + filename + " não foi encontrado");
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

  /**
   * verticlJoin(Bitmap bmp1, Bitmap bmp2, Bitmap bmp3)
   * 
   * Faz a concatenação entre três arquivos bitmap
   * 
   * @param bmp1
   *          Foto 1
   * @param bmp2
   *          Foto 2
   * @param bmp3
   *          Foto 3
   * 
   * @return Um bitmap contendo as "junção" entre os três bitmaps (na vertica,
   *         isto é), um bitmap será posicionado abaixo do outro na vertical (a
   *         figura irá manter a largura e terá como altura a soma das alturas
   *         de cada uma das figuras) É importante notar que todas as imagem
   *         deverão ter a mesma largura.
   */
  public Bitmap verticalJoin(Bitmap bmp1, Bitmap bmp2, Bitmap bmp3) {

    if (bmp1 == null || bmp2 == null || bmp3 == null) {
      Log.w(TAG, "verticalJoin() - Pelo menos uma imagem é null");
      return null;
    }

    if (!((bmp1.getWidth() == bmp2.getWidth()) && (bmp2.getWidth() == bmp3.getWidth()))) {
      Log.w(TAG, "bmp1=" + bmp1.getWidth());
      Log.w(TAG, "bmp2=" + bmp2.getWidth());
      Log.w(TAG, "bmp3=" + bmp3.getWidth());
      Log.w(TAG, "As imagens não possuem a mesma largura");
      return null;
    }

    // a imagem resultante terá a mesma largura
    int w = bmp1.getWidth();

    // a altura da imagem resultante será dada pela soma das alturas das imagens
    int nh = bmp1.getHeight() + bmp2.getHeight() + bmp3.getHeight();

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

    // cria um novo bitmap onde será inseridas as três imagens
    // o tamanho do novo bitmap é dado por w + a soma das alturas dos bitmaps 1,
    // 2 e 3
    Bitmap bitmap = Bitmap.createBitmap(w, nh, Bitmap.Config.ARGB_8888);

    // Obtém o canvas
    Canvas canvas = new Canvas(bitmap);

    // Define a cor AZUL como cor de preenchimento
    canvas.drawColor(Color.BLUE);

    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

    // Define uma matriz identidade
    Matrix m = new Matrix();

    // Desenha o 1º bitmap
    canvas.drawBitmap(bmp1, m, paint);

    m.setTranslate(0, bmp1.getHeight());

    // Desenha o 2º bitmap
    canvas.drawBitmap(bmp2, m, paint);

    m.setTranslate(0, bmp1.getHeight() + bmp2.getHeight());

    // Desenha o 3º bitmap
    canvas.drawBitmap(bmp3, m, paint);

    // o novo bitmap formado
    return bitmap;

  }

  /**
   * exibeBitmap(ImageView imageView, Bitmap bitmap)
   * 
   * Exibe um bitmap em um imageView.
   * 
   * @param imageView
   * @param bitmap
   */
  public void exibeBitmap(ImageView imageView, Bitmap bitmap) {

    if ((imageView != null) && (bitmap != null)) {
      // imageView.setImageBitmap(bitmap);
      updateBitmap(bitmap, imageView);
    }

  }

  /**
   * updateBitmap(Bitmap bm, ImageView image)
   * 
   * Atualiza um ImageView com um bitmap
   * 
   * @param bm
   *          Bitmap
   * @param image
   *          imageView
   * 
   */
  public void updateBitmap(Bitmap bm, ImageView image) {

    if ((bm != null) && (image != null)) {

      image.setImageBitmap(bm);

      showBitmapInfo(bm);

    } else {

      Log.d(TAG, "updateBitmap() - Bitmap is null");

    }

  }

}
