package br.com.mltech.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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

  public static final String PATH_MOLDURAS = "/mnt/sdcard/Pictures/fotoevento/molduras/";
  public static final String PATH_FOTOS = "/mnt/sdcard/Pictures/fotoevento/fotos/";

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

    // cria um novo bitmap com a
    // moldura
    // sobreposta a foto
    Bitmap bitmap = overlay4(bmp1, bmp2);

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
  public Bitmap processaFotoFormatoPolaroid(Bitmap foto, Bitmap moldura) {

    // cria um novo bitmap com a
    // moldura
    // sobreposta a foto
    Bitmap bitmap = overlay4(foto, moldura);

    if (bitmap == null) {
      Log.w(TAG, "processaFotoFormatoPolaroid() - erro na conversão da foto");
    }

    return bitmap;

  }

  /**
   * processaFotoFormatoCabine(String foto1, String foto2, String foto3, String
   * arqMoldura)
   * 
   * Testa o funcionamento da função verticalJoin().
   * 
   * Testa o funcionamento da criação de uma única foto a partir de três outras
   * fotos. Carrega as imagens a partir de um arquivo localizado em memória
   * externa (sdcard).
   * 
   * Cria um bitmap contendo três cópias da foto Exibe a foto gerada Grava o
   * resultado
   * 
   * @param foto1
   * @param foto2
   * @param foto3
   * @param arqMoldura
   * 
   * @return
   * 
   */
  public String processaFotoFormatoCabine(String foto1, String foto2, String foto3, String arqMoldura) {

    // Foto 1
    Bitmap bmFoto1 = carregaFoto(foto1);

    if (bmFoto1 == null) {
      // Log.w(TAG,
      // "processaFotoFormatoCabine() - Não foi possível ler o arquivo: " +
      // foto1);
      return null;
    }

    // Foto 2
    Bitmap bmFoto2 = carregaFoto(foto2);
    if (bmFoto2 == null) {
      // Log.w(TAG,
      // "processaFotoFormatoCabine() - Não foi possível ler o arquivo: " +
      // foto2);
      return null;
    }

    // Foto 3
    Bitmap bmFoto3 = carregaFoto(foto3);
    if (bmFoto3 == null) {
      // Log.w(TAG,
      // "processaFotoFormatoCabine() - Não foi possível ler o arquivo: " +
      // foto3);
      return null;
    }

    // Lê o arquivo contendo a moldura

    // String arqModura = "moldura-cabine-132x568-red.png";

    String moldura = PATH_MOLDURAS + arqMoldura;

    Bitmap bmMoldura = getBitmapFromFile(moldura);

    if (bmMoldura != null) {
      Log.v(TAG, " ==> Tamanho da moldura original: " + getStringBitmapSize(bmMoldura));
    } else {
      Log.w(TAG, "Não foi possível ler o arquivo: " + moldura);
      return null;
    }

    // =========================================================================
    // Cria um novo bitmap a partir da composição das 3 fotos
    // A foto será repetida na vertical, isto é, uma nova foto
    // será colocada embaixo da outra.
    // =========================================================================
    Bitmap bmImgJoin = verticalJoin(bmFoto1, bmFoto2, bmFoto3);

    if (bmImgJoin != null) {
      Log.i(TAG, "Imagens foram juntadas com sucesso");
      Log.v(TAG, " ==> Tamanho da foto após join: " + getStringBitmapSize(bmImgJoin));
      // imagem.exibeBitmap(mImageView, bmImagem);
    } else {
      Log.w(TAG, "Erro no merge das três fotos");
      return null;
    }

    Bitmap scaledBitmap = null;

    String arqSaida = PATH_FOTOS + "xxx-join.png";

    // grava a foto das imagens "juntada"
    boolean gravou = gravaBitmapArquivo(bmImgJoin, arqSaida);

    if (!gravou) {
      Log.w(TAG, "Erro na gravação do arquivo contendo as três fotos: " + arqSaida);
      return null;
    } else {

      Log.i(TAG, "testaVerticalJoin() - Arquivo: " + arqSaida + " gravado com sucesso");
    }

    // Obtém uma imagem em escala
    // scaledBitmap = imagem.getScaledBitmap(bmImgJoin);
    scaledBitmap = getScaledBitmap2(bmImgJoin, 113, 453);

    if (scaledBitmap == null) {
      //
      return null;
    }

    Log.v(TAG, "Tamanho depois do escalonamento: " + getStringBitmapSize(scaledBitmap));

    // exibe a imagem escalonada
    // exibeBitmap(mImageView, scaledBitmap);

    arqSaida = PATH_FOTOS + "xxx2-join.png";

    boolean gravouImagemEscalonda = gravaBitmapArquivo(scaledBitmap, arqSaida);

    if (gravouImagemEscalonda) {

      // imagem escalonada gravada com sucesso
      Log.v(TAG, "Imagem escalonada gravada com sucesso no arquivo " + arqSaida);
    } else {
      Log.v(TAG, "Falha na gravação da imagem escalonada no arquivo: " + arqSaida);
      return null;
    }

    // combina a foto com a moldura
    Bitmap fotoComMoldura = aplicaMolduraFoto(scaledBitmap, bmMoldura);

    // exibe a foto com a moldura
    // exibeBitmap(mImageView, fotoComMoldura);

    arqSaida = PATH_FOTOS + "xxx3-join-moldura.png";

    boolean gravouImagemComMoldura = gravaBitmapArquivo(fotoComMoldura, arqSaida);

    if (gravouImagemComMoldura) {

      // imagem escalonada gravada com sucesso
      Log.v(TAG, "Imagem com moldura gravada com sucesso no arquivo " + arqSaida);
    } else {
      Log.v(TAG, "Falha na gravação da imagem com moldura no arquivo: " + arqSaida);
      return null;
    }

    return arqSaida;

  }

  /**
   * aplicaFiltroCores(Bitmap bi)
   * 
   * @param bi
   *          Bitmap original
   * 
   * @return um bitmap com o filtro aplicado ou null
   * 
   */
  public Bitmap aplicaFiltroCores(Bitmap bi) {
    return null;
  }

  /**
   * aplicaFiltroPB(Bitmap bi)
   * 
   * @param bi
   *          Bitmap original
   * 
   * @return um bitmap com o filtro aplicado ou null
   * 
   */
  public Bitmap aplicaFiltroPB(Bitmap bi) {
    return null;
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

    Log.d(TAG, "aplicaMolduraFoto() - bmOverlay=" + getStringBitmapSize(bmOverlay));

    return bmOverlay;

  }

  /**
   * aplicaMolduraFotoPolaroid(Bitmap bmFoto, Bitmap bmMoldura)
   * 
   * @param bmFoto
   * @param bmMoldura
   * 
   * @return
   */
  public Bitmap aplicaMolduraFotoPolaroid(Bitmap bmFoto, Bitmap bmMoldura) {

    Bitmap bmOverlay = null;

    if (bmMoldura != null && bmFoto != null) {
      bmOverlay = overlay4(bmFoto, bmMoldura);
    }

    Log.d(TAG, "aplicaMolduraFoto() - bmOverlay=" + getStringBitmapSize(bmOverlay));

    return bmOverlay;

  }

  /**
   * carregaFoto(String foto)
   * 
   * Obtém um bitmap a partir de um arquivo
   * 
   * @param foto
   *          Caminho onde encontrar a foto
   * 
   * @return Um bitmap contendo a foto ou null caso a foto não seja encontrada
   * 
   */
  private Bitmap carregaFoto(String foto) {

    Bitmap bmFoto = getBitmapFromFile(foto);

    if (bmFoto != null) {
      Log.v(TAG, " ==> Tamanho da foto original: " + getStringBitmapSize(bmFoto));
    } else {
      Log.w(TAG, "Não foi possível ler o arquivo: " + foto);
    }

    return bmFoto;

  }

  /**
   * criaBitmap(Uri uri)
   * 
   * Tenta criar um bitmap a partir de um arquivo identificado por uma Uri
   * 
   * @param uri
   *          Uri do arquivo contendo uma imagem
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

    Log.v(TAG, "criaBitmap() - tamanho da imagem criada: " + getStringBitmapSize(bitmap));

    return bitmap;

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

      // showFile(f);

      Log.v(TAG, "getBitmapFromFile() - getAbsolutePath()=" + f.getAbsolutePath());

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

    if (f == null) {

      Log.w(TAG, "getBitmapFile() - arquivo é nulo");
      return null;

    } else {

      showFile(f);

      bm = BitmapFactory.decodeFile(f.getAbsolutePath());

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
   * getScaledBitmap2(Bitmap bm, int newWidth, int newHeight)
   * 
   * Esse método retorna um novo bitmap de tamanho reduzido;
   * 
   * @param bm
   *          Bitmap original
   * 
   * @param newWidth
   * @param newHeight
   * 
   * @return um bitmap com seu tamanho alterado
   * 
   */
  public Bitmap getScaledBitmap2(Bitmap bm, int newWidth, int newHeight) {

    Log.v(TAG, "getScaledBitmap2() - original Bitmap size:" + getStringBitmapSize(bm));

    // Tamanho original do bitmap
    float origWidth = bm.getWidth();

    float scaledFactor = newWidth / origWidth;

    Log.d(TAG, "getScaledBitmap2() - scaledFactor = " + scaledFactor);

    Log.d(TAG, "getScaledBitmap2() - new w=" + newWidth + ", new h=" + newHeight);

    boolean filter = true;

    // Creates a new bitmap, scaled from an existing bitmap
    Bitmap bitmap = Bitmap.createScaledBitmap(bm, newWidth, newHeight, filter);

    // showBitmapInfo(bitmap);

    return bitmap;

  }

  /**
   * getBitmapRegion(String filename, Rect rect, Options options)
   * 
   * @param filename
   * @param rect
   * @param options
   * 
   * @return The decoded bitmap, or null if the image data could not be decoded.
   */
  public Bitmap getBitmapRegion(String filename, Rect rect, Options options) {

    BitmapRegionDecoder brd = null;

    Bitmap bitmap = null;

    try {

      brd = BitmapRegionDecoder.newInstance(filename, true);

      bitmap = brd.decodeRegion(rect, options);

    } catch (IOException e) {

      Log.w(TAG, "getBitmapRegion() - ", e);

    }

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
   * getRoundedCornerBitmap(Bitmap bitmap)
   * 
   * @param bitmap
   * 
   * @return um bitmap
   */
  public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

    // Returns a mutable bitmap with the specified width and height.
    // Its initial density is as per getDensity().
    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

    // Construct a canvas with the specified bitmap to draw into.
    // The bitmap must be mutable.
    // The initial target density of the canvas is the same as the given
    // bitmap's density.
    Canvas canvas = new Canvas(output);

    final int color = 0xff424242;

    // Create a new paint with default settings.
    final Paint paint = new Paint();

    // Create a new rectangle with the specified coordinates.
    // Note: no range checking is performed, so the caller must ensure that
    // left <= right and top <= bottom.
    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

    // RectF holds four float coordinates for a rectangle.
    // The rectangle is represented by the coordinates of its 4 edges (left,
    // top, right bottom).
    // These fields can be accessed directly.
    // Use width() and height() to retrieve the rectangle's width and height.
    // Note: most methods do not check to see that the coordinates are sorted
    // correctly
    // (i.e. left <= right and top <= bottom).
    final RectF rectF = new RectF(rect);

    // final float roundPx = 15;
    final float roundPx = 25;

    // Helper for setFlags(), setting or clearing the ANTI_ALIAS_FLAG bit
    // AntiAliasing smooths out the edges of what is being drawn, but is has no
    // impact on the interior of the shape. See setDither() and
    // setFilterBitmap() to affect how colors are treated.
    paint.setAntiAlias(true);

    // Fill the entire canvas' bitmap (restricted to the current clip) with the
    // specified ARGB color, using srcover porterduff mode.
    canvas.drawARGB(0, 0, 0, 0);
    // canvas.drawARGB(0, 255, 255, 255);

    // Set the paint's color.
    // Note that the color is an int containing alpha as well as r,g,b.
    // This 32bit value is not pre multiplied, meaning that its
    // alpha can be any value, regardless of the values of r,g,b.
    // See the Color class for more details.
    paint.setColor(color);

    // Draw the specified round-rect using the specified paint.
    // The roundrect will be filled or framed based on the Style in the paint.
    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

    // Set or clear the xfermode object.
    // Pass null to clear any previous xfermode. As a convenience, the parameter
    // passed is also returned.
    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
    // paint.setXfermode(new PorterDuffXfermode(Mode.LIGHTEN));

    // Draw the specified bitmap, scaling/translating automatically to fill the
    // destination rectangle. If the source rectangle is not null, it specifies
    // the subset of the bitmap to draw.
    canvas.drawBitmap(bitmap, rect, rect, paint);

    return output;

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

      // boolean success =
      // bm.compress(Bitmap.CompressFormat.valueOf("PNG"),
      // 100, out);
      salvou = bm.compress(Bitmap.CompressFormat.PNG, 100, out);
      Log.i(TAG, "gravaBitmapArquivo() - sucess code from bitmap.compress: " + salvou);
      out.close();

    } catch (FileNotFoundException e) {
      Log.w(TAG, "gravaBitmapArquivo() - Erro na criação do arquivo", e);
    } catch (IOException e) {
      Log.w(TAG, "gravaBitmapArquivo() - Erro na criação do arquivo", e);
    }

    return salvou;

  }

  /**
   * gravaBitmapArquivo2(Bitmap bm, String filename)
   * 
   * @param bm
   * @param filename
   * 
   * @return
   */
  public boolean gravaBitmapArquivo2(Bitmap bm, String filename) {

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

      salvou = bm.compress(Bitmap.CompressFormat.JPEG, 75, out);
      Log.i(TAG, "gravaBitmapArquivo() - sucess code from bitmap.compress: " + salvou);
      out.close();

    } catch (FileNotFoundException e) {
      Log.w(TAG, "gravaBitmapArquivo() - Erro na criação do arquivo", e);
    } catch (IOException e) {
      Log.w(TAG, "gravaBitmapArquivo() - Erro na criação do arquivo", e);
    }

    return salvou;

  }

  /**
   * gravaBitmapArquivo3(Uri uri)
   * 
   * @param uri
   *          URI contendo a localização do dados que serão decodificados em um
   *          bitmap e salvos em um arquivo
   * 
   * @return true se o arquivo foi gerado com sucesso ou false em caso de algum
   *         erro
   */
  public boolean gravaBitmapArquivo3(Uri uri) {

    boolean salvou = false;

    if (uri == null) {
      // arquivo não pode ser vazio
      return false;
    }

    Log.v(TAG, "gravaBitmapArquivo3() - uri=" + uri);

    // cria-se um arquivo
    File file = new File(uri.getPath());

    // cria um bitmap a partir do arquivo
    Bitmap foto = BitmapFactory.decodeFile(file.getAbsolutePath());

    if (foto == null) {
      Log.w(TAG, "gravaBitmapArquivo3() - falha da conversão para bitmap");
      return false;
    }

    OutputStream out = null;

    try {

      out = new FileOutputStream(file);

      salvou = foto.compress(Bitmap.CompressFormat.PNG, 100, out);
      Log.i(TAG, "gravaBitmapArquivo3() - sucess code from bitmap.compress: " + salvou);
      out.close();

    } catch (FileNotFoundException e) {
      Log.w(TAG, "gravaBitmapArquivo3() - Erro na criação do arquivo", e);
    } catch (IOException e) {
      Log.w(TAG, "gravaBitmapArquivo3() - Erro na criação do arquivo", e);
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
      Log.w(TAG, "getStringBitmapSize() - Bitmap é nulo");
    }

    return s;

  }

  /**
   * isLandscape(Bitmap bm)
   * 
   * Verifica se o bitmap possui uma imagem cuja largura é MAIOR que a altura.
   * 
   * @param bm
   *          Bitmap
   * 
   * @return true se o bitmap contiver uma imagem formato landscape
   * 
   */
  public boolean isLandscape(Bitmap bm) {
    boolean ret = false;
    if (bm != null) {
      ret = bm.getWidth() > bm.getHeight();
    }
    return ret;
  }

  /**
   * isPortrait(Bitmap bm)
   * 
   * Verifica se o bitmap possui uma imagem cuja largura é MENOR que a altura.
   * 
   * @param bm
   *          Bitmap
   * 
   * @return true se o bitmap contiver uma imagem formato portrait
   * 
   */
  public boolean isPortrait(Bitmap bm) {
    boolean ret = false;
    if (bm != null) {
      ret = bm.getHeight() > bm.getWidth();
    }
    return ret;
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

  /**
   * overlay2(Bitmap bmp1, Bitmap bmp2)
   * 
   * @param bmp1
   * @param bmp2
   * 
   * @return
   */
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

    Log.v(TAG, "overlay3() - nova imagem criada: " + w + " x " + h);

    Bitmap.Config bmConfig = bmp1.getConfig();

    Log.v(TAG, "overlay3() - bitmap config: " + bmConfig.name());

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
   * overlay4
   * 
   * Executa o overlay para fotos no formato polaroid
   * 
   * @param bmp1
   *          foto
   * @param bmp2
   *          moldura
   * 
   * @return Bitmap
   */
  public Bitmap overlay4(Bitmap bmp1, Bitmap bmp2) {

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

    // cria um novo bitmap onde será feito o overlay
    Bitmap bmOverlay = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

    Log.v(TAG, "overlay4() - nova imagem criada: " + w + " x " + h);

    Bitmap.Config bmConfig = bmp1.getConfig();

    Log.v(TAG, "overlay4() - bitmap config: " + bmConfig.name());

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
   * showBitmapInfo2(Bitmap bm)
   * 
   * @param bm
   * 
   */
  public void showBitmapInfo2(Bitmap bm) {

    // TODO transformar ...
    if (bm == null) {
      Log.w(TAG, "Bitmap não pode ser nulo");
      return;
    }

    Config config = bm.getConfig();
    int density = bm.getDensity();
    int h = bm.getHeight();
    int w = bm.getWidth();
    boolean hasAlpha = bm.hasAlpha();
    boolean isMutable = bm.isMutable();
    boolean isRecycled = bm.isRecycled();
    String s = bm.toString();

    Log.v(TAG, "getConfig= " + bm.getConfig());
    Log.v(TAG, "getDensity= " + bm.getDensity());
    Log.v(TAG, "getHeight= " + bm.getHeight());
    Log.v(TAG, "getWidth= " + bm.getWidth());
    Log.v(TAG, "hasAlpha= " + bm.hasAlpha());
    Log.v(TAG, "isMutable= " + bm.isMutable());
    Log.v(TAG, "isRecycled= " + bm.isRecycled());
    Log.v(TAG, "toString= " + bm.toString());

    Log.v(TAG, "Size=" + w + "x" + h);
    Log.v(TAG, "config=" + config);
    Log.v(TAG, "getRowBytes()=" + bm.getRowBytes());
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

      Log.v(TAG, "showBitmapInfo(): " + getStringBitmapSize(bm));
      /*
       * Log.v(TAG, "  w=" + bm.getWidth() + ", h=" + bm.getHeight());
       * Log.v(TAG, "  getDensity()=" + bm.getDensity()); Log.v(TAG,
       * "  getRowBytes()=" + bm.getRowBytes()); Log.v(TAG, "  isMutable=" +
       * bm.isMutable());
       */

    } else {

      Log.w(TAG, "showBitmapInfo(): Bitmap is null");

    }

  }

  /**
   * showBitmapOptions(Options options)
   * 
   * @param options
   * 
   */
  void showBitmapOptions(Options options) {
    Log.v(TAG, "showBitmapOptions()=options=" + options);
    Log.v(TAG, "showBitmapOptions() - inDensity: " + options.inDensity);
    Log.v(TAG, "showBitmapOptions() - inSampleSize: " + options.inSampleSize);
    Log.v(TAG, "showBitmapOptions() - inScreenDensity: " + options.inScreenDensity);
    Log.v(TAG, "showBitmapOptions() - inTargetDensity: " + options.inTargetDensity);
    Log.v(TAG, "showBitmapOptions() - outHeight: " + options.outHeight);
    Log.v(TAG, "showBitmapOptions() - outWidth: " + options.outWidth);
  }

  /**
   * showUri(Uri uri)
   * 
   * Exibe informações sobre uma Uri
   * 
   * @param uri
   *          Uri
   * 
   */
  public static void showUri(Uri uri) {

    if (uri == null) {
      Log.w(TAG, "Uri é nula");
      return;
    }

    Log.v(TAG, "showUri(): ");
    Log.v(TAG, "  getScheme: " + uri.getScheme());
    Log.v(TAG, "  getAuthority: " + uri.getAuthority());
    Log.v(TAG, "  getPort: " + uri.getPort());
    Log.v(TAG, "  getPath: " + uri.getPath());
    Log.v(TAG, "  getQuery: " + uri.getQuery());
    Log.v(TAG, "  getUserInfo: " + uri.getUserInfo());
    Log.v(TAG, "  isAbsolute: " + uri.isAbsolute());
    Log.v(TAG, "  isRelative: " + uri.isRelative());
    Log.v(TAG, "  isHierarchical: " + uri.isHierarchical());
    Log.v(TAG, "  isOpaque: " + uri.isOpaque());
    Log.v(TAG, "  getFragment: " + uri.getFragment());

  }

  /**
   * verticlJoin(Bitmap bmp1, Bitmap bmp2, Bitmap bmp3)
   * 
   * Faz a concatenação entre três arquivos bitmap na vertical, isto é, as fotos
   * são colocadas uma abaixo da outra. As imagem ficam maiores na vertical
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

    // a altura da imagem resultante será dada pela soma das alturas das
    // imagens
    int newHeight = bmp1.getHeight() + bmp2.getHeight() + bmp3.getHeight();

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
    // o tamanho do novo bitmap é dado por w + a soma das alturas dos
    // bitmaps 1, 2 e 3
    Bitmap bitmap = Bitmap.createBitmap(w, newHeight, Bitmap.Config.ARGB_8888);

    // Obtém o canvas e carrega o bitmap ao canvas.
    // O canvas pode ser "pintado"
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

  /**
   * msgDialog(String msg, String sim, String nao)
   * 
   * @param msg
   * @param sim
   * @param nao
   * 
   * @return
   */
  private int msgDialog(Context ctx, String msg, String sim, String nao) {

    // TODO esse método está com problemas ...
    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

    builder.setMessage(msg);

    builder.setCancelable(false);

    builder.setPositiveButton(sim, new DialogInterface.OnClickListener() {

      public void onClick(DialogInterface dialog, int id) {
        // MyActivity7.this.finish();
        dialog.dismiss();
        // opcao = 1;
      }

    });

    builder.setNegativeButton(nao, new DialogInterface.OnClickListener() {

      public void onClick(DialogInterface dialog, int id) {
        dialog.cancel();
        // opcao = 0;
      }

    });

    AlertDialog alert = builder.create();
    alert.show();

    // return opcao;
    return 0;

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

    // os = new FileOutputStream(Environment.getExternalStorageDirectory() +
    // "/"
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

}
