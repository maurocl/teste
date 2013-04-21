
package br.com.mltech;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.Set;

import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

/**
 * Classe Utilit�ria
 * 
 * 
 */
public class Utility extends Application {

  public static final String TAG = "Utility";
  
  /**
   * Facebook
   */
  public static Facebook mFacebook;

  /**
   * AsyncFacebookRunner
   */
  public static AsyncFacebookRunner mAsyncRunner;

  /**
	 * 
	 */
  public static JSONObject mFriendsList;

  /**
   * Identificador do usu�rio
   */
  public static String userUID = null;

  /**
   * Identificador do objeto
   */
  public static String objectID = null;

  /**
	 * 
	 */
  // public static FriendsGetProfilePics model;

  /**
	 * 
	 */
  public static AndroidHttpClient httpclient = null;

  /**
   * Permiss�es atuais
   */
  public static Hashtable<String, String> currentPermissions = new Hashtable<String, String>();

  // Dimens�o m�xima da imagem
  private static int MAX_IMAGE_DIMENSION = 720;

  // Endere�o da imagem usada
  public static final String HACK_ICON_URL = "http://www.facebookmobileweb.com/hackbook/img/facebook_icon_large.png";

  /**
   * Obtem o bitmap dada sua URL
   * 
   * @param url
   *          URL onde se encontra o bitmap
   * 
   * @return o bitmap associado a URL ou null em caso de erro
   * 
   */
  public static Bitmap getBitmap(String url) {

    Bitmap bm = null;

    try {

      URL aURL = new URL(url);

      URLConnection conn = aURL.openConnection();

      conn.connect();

      InputStream is = conn.getInputStream();

      BufferedInputStream bis = new BufferedInputStream(is);

      // Cria o bitmap
      bm = BitmapFactory.decodeStream(new FlushedInputStream(is));

      bis.close();

      is.close();

    } catch (Exception e) {

      e.printStackTrace();

    } finally {

      if (httpclient != null) {
        httpclient.close();
      }

    }

    return bm;

  }


  /**
   * Obtem o bitmap localizado pela URL
   * 
   * @param url URL contendo um bitmap
   * 
   * @return o bitmap
   */
  public static Bitmap getBitmap2(String url) {

    Bitmap bm = null;

    try {

      Log.d(TAG,"url="+url);
      
      URL aURL = new URL(url);

      // abre uma conex�o
      URLConnection conn = aURL.openConnection();

      conn.connect();

      InputStream is = conn.getInputStream();

      // Cria o bitmap
      bm = BitmapFactory.decodeStream(is);

      is.close();

    } catch (Exception e) {

      Log.e(TAG,"Exce��o: ",e);
      

    } finally {

      if (httpclient != null) {
        httpclient.close();
      }

    }

    // retorna o bitmap criado ou null em caso de erro
    return bm;

  }

  
  /**
   * � um InputStream com flush autom�tico.
   */
  static class FlushedInputStream extends FilterInputStream {

    /**
     * Construtor
     * 
     * @param inputStream
     *          stream de entrada
     * 
     */
    public FlushedInputStream(InputStream inputStream) {

      super(inputStream);
    }

    @Override
    /**
     * @param n n� de bytes que ser�o skippados
     */
    public long skip(long n) throws IOException {

      long totalBytesSkipped = 0L;

      while (totalBytesSkipped < n) {

        long bytesSkipped = in.skip(n - totalBytesSkipped);

        if (bytesSkipped == 0L) {

          int b = read();

          if (b < 0) {
            break; // we reached EOF
          } else {
            bytesSkipped = 1; // we read one byte
          }

        }

        totalBytesSkipped += bytesSkipped;

      }

      // n� total de bytes "pulados"
      return totalBytesSkipped;

    }

  }

  /**
   * <p>Redimensiona uma imagem.
   * 
   * @param context
   *          contexto da aplica��o
   * @param photoUri
   *          URI da foto
   * 
   * @return um array de bytes com a imagem escalonada
   * 
   * @throws IOException caso haja algum erro de IO.
   * 
   */
  public static byte[] scaleImage(Context context, Uri photoUri) throws IOException {

    InputStream is = context.getContentResolver().openInputStream(photoUri);

    BitmapFactory.Options dbo = new BitmapFactory.Options();

    dbo.inJustDecodeBounds = true;

    // Decode an input stream into a bitmap.
    // If the input stream is null, or cannot be used to decode a bitmap,
    // the function returns null.
    // The stream's position will be where ever
    // it was after the encoded data was read.
    BitmapFactory.decodeStream(is, null, dbo);

    // fecha o stream
    is.close();

    int rotatedWidth, rotatedHeight;

    int orientation = getOrientation(context, photoUri);

    if (orientation == 90 || orientation == 270) {

      // The resulting height of the bitmap, set independent of the state of
      // inJustDecodeBounds.
      // However, if there is an error trying to decode,
      // outHeight will be set to -1.

      rotatedWidth = dbo.outHeight;
      rotatedHeight = dbo.outWidth;

    } else {

      rotatedWidth = dbo.outWidth;
      rotatedHeight = dbo.outHeight;

    }

    // Source bitmap
    Bitmap srcBitmap;

    is = context.getContentResolver().openInputStream(photoUri);

    // se a largura ou altura for maior que a dimens�o m�xima da imagem
    if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {

      // calcula a taxa de redu��o da largura
      float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);

      // calcula a taxa de redu��o da altura
      float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);

      // calcula o maior taxa de redu��o
      float maxRatio = Math.max(widthRatio, heightRatio);

      // Create the bitmap from file
      BitmapFactory.Options options = new BitmapFactory.Options();

      options.inSampleSize = (int) maxRatio;

      srcBitmap = BitmapFactory.decodeStream(is, null, options);

    } else {

      // Decode an input stream into a bitmap
      srcBitmap = BitmapFactory.decodeStream(is);

    }

    is.close();

    /*
     * if the orientation is not 0 (or -1, which means we don't know), we have
     * to do a rotation.
     */
    if (orientation > 0) {

      Matrix matrix = new Matrix();

      // Postconcats the matrix with the specified rotation. M' = R(degrees) * M
      matrix.postRotate(orientation);

      srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);

    }

    String type = context.getContentResolver().getType(photoUri);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    if (type.equals("image/png")) {

      srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

    } else if (type.equals("image/jpg") || type.equals("image/jpeg")) {

      srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

    }

    byte[] bMapArray = baos.toByteArray();

    baos.close();

    return bMapArray;

  }

  /**
   * Obt�m a orienta��o da tela
   * 
   * @param context
   *          contexto da aplica��o
   * @param photoUri
   *          URI da foto
   * 
   * @return
   */
  public static int getOrientation(Context context, Uri photoUri) {

    /* it's on the external media. */
    Cursor cursor = context.getContentResolver().query(photoUri, new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null,
        null, null);

    if (cursor.getCount() != 1) {
      return -1;
    }

    cursor.moveToFirst();

    return cursor.getInt(0);

  }

  /**
   * Exibe a rela��o de chaves e valores de um Bundle no log da aplica��o.
   * 
   * @param values Bundle contendo os valores
   * 
   */
  public void showBundle(Bundle values) {

    // Bundle: mapping from String values to various Parcelable types.

    Log.i(TAG, "------------------------------------------------------------");
    Log.i(TAG, "ShowBundle:");

    if (values != null) {

    	// Obt�m o conjunto de chaves
      Set<String> chaves = values.keySet();

      if (chaves != null) {

        Log.d(TAG, "N� de chaves:" + chaves.size());

        int i = 0;

        for (String chave : chaves) {

          Log.d(TAG,
              i + ": Chave: " + chave + ", valor="
                  + values.getString(chave) + ", size=" + values.getString(chave).length());
          Log.d(TAG, "");

          i++;

        }

      }

    }

    Log.i(TAG, "------------------------------------------------------------");

  }

  /**
   * Exibe o log de <i>warning</i> informa��es sobre o erro Facebook
   * 
   * @param error
   *          inst�ncia de um erro do facebook
   * 
   */
  public void showFacebookError(FacebookError error) {

    Log.i(TAG, "ShowFacebookError:");

    Log.w(TAG, "FacebookError: " + error);
    Log.w(TAG, "FacebookError: getErrorCode(): " + error.getErrorCode());
    Log.w(TAG, "FacebookError: getErrorType(): " + error.getErrorType());
    Log.w(TAG, "FacebookError: getMessage(): " + error.getMessage());
    Log.w(TAG, "FacebookError: getLocalizedMessage(): " + error.getLocalizedMessage());
    Log.w(TAG, "FacebookError: getCause(): " + error.getCause());
    Log.w(TAG, "FacebookError: getClass(): " + error.getClass());
  }

}
