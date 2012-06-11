package br.com.mltech;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

/**
 * ShowBitmapInfo()
 * 
 * @author maurocl
 *
 */
public class ShowBitmapInfo extends Activity {

  public static final String TAG = "ShowBitmapInfo";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.tela);

    Log.i(TAG, "*** onCreate() ***");

    ImageView imageView = (ImageView) findViewById(R.id.imageView1);

    String filename = "/mnt/sdcard/Pictures/fotoevento/fotos/casa-320x240.png";

    Bitmap bm = getBitmapFromFile(filename);

    if (bm == null) {
      Log.d(TAG, "arquivo não encontrado");
    }

    Rect rect = new Rect(10, 10, 100, 100);

    BitmapFactory.Options options = new BitmapFactory.Options();

    Bitmap regionBitmap = getBitmapRegion(filename, rect, options);

    // imageView.setImageBitmap(bm);
    imageView.setImageBitmap(regionBitmap);

  }

  /**
   * getBitmapFromFile(String filename)
   * 
   * @param filename
   *          Nome completo do arquivo (incluindo o path)
   * 
   * @return o bitmap com a imagem contida no arquivo ou null caso haja algum
   *         problema.
   * 
   */
  public Bitmap getBitmapFromFile(String filename) {

    Bitmap bm = BitmapFactory.decodeFile(filename);

    if (bm == null) {
      Log.d(TAG, "getBitmapFromFile() - arquivo: " + filename + " não foi encontrado.");
    }

    showBitmapInfo(bm);

    return bm;

  }

  public void showBitmapInfo(Bitmap bm) {
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

    Log.v(TAG, "Size=" + w + "x" + h);
    Log.v(TAG, "config=" + config);
    Log.v(TAG, "getRowBytes()=" + bm.getRowBytes());
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

    // BitmapRegionDecoder.newInstance(fd, isShareable);

    BitmapRegionDecoder brd = null;

    try {
      brd = BitmapRegionDecoder.newInstance(filename, true);
      
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    showBitmapOptions(options);
    
    Bitmap b = brd.decodeRegion(rect, options);

    showBitmapInfo(b);
    
    showBitmapOptions(options);

    return b;

  }

  /**
   * showBitmapOptions(Options options)
   * 
   * @param options
   * 
   */
  void showBitmapOptions(Options options) {
    Log.v(TAG,"showBitmapOptions()=options="+options);
    Log.v(TAG,"showBitmapOptions() - inDensity: "+options.inDensity);
    Log.v(TAG,"showBitmapOptions() - inSampleSize: "+options.inSampleSize);
    Log.v(TAG,"showBitmapOptions() - inScreenDensity: "+options.inScreenDensity);
    Log.v(TAG,"showBitmapOptions() - inTargetDensity: "+options.inTargetDensity);
    Log.v(TAG,"showBitmapOptions() - outHeight: "+options.outHeight);
    Log.v(TAG,"showBitmapOptions() - outWidth: "+options.outWidth);
  }
  
}



