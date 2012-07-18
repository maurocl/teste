package br.com.mltech.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

public class FileDownload {

  private static final String TAG = "FileDownload";

  private String mUrl;
  private Context context;
  private ImageView imageView;


  private ProgressDialog dialog;

  private Handler handler = new Handler();

  private Bitmap bitmap;

  /**
   * FileDownload(String url)
   * 
   * Método construtor
   * 
   * @param url
   */
  public FileDownload(String url) {
    this(url, null, null);
  }

  /**
   * FileDownload(String url, Context ctx, ImageView imageView)
   * 
   * Método construtor
   * 
   */
  public FileDownload(String url, Context ctx, ImageView imageView) {

    this.mUrl = url;
    this.context = ctx;
    this.imageView = imageView;

    Log.d(TAG, "construtor - FileDownload: " + url);

    if(context!=null) {
    dialog = ProgressDialog.show(context, "Exemplo", "Buscando imagem, aguarde ...", false, true);
    }

    if (dialog == null) {
      Log.d(TAG, "Dialog is null");
    } else {
      Log.d(TAG, "Dialog não é  null");
    }

  }

  /**
   * downloadImage()
   */
  public void downloadImage() {

    Log.d(TAG, "método downloadImage");

    Thread t = new Thread() {

      public void run() {

        URL url;

        try {

          url = new URL(mUrl);

          Log.d(TAG, "mUrl=" + mUrl);

          InputStream is = url.openStream();

          bitmap = BitmapFactory.decodeStream(is);

          if (bitmap != null) {
            Log.i(TAG, bitmap.getWidth() + "x" + bitmap.getHeight() + " pixels");
          }

          is.close();

          // atualiza a Tela
          atualizaTela(bitmap);

        } catch (MalformedURLException e) {

          Log.w(TAG, "MalformedURLException");

          // TODO Auto-generated catch block
          e.printStackTrace();

          return;

        } catch (IOException e) {

          // TODO Auto-generated catch block
          e.printStackTrace();

        }

      }

    };

    t.start();

  }

  /**
   * atualizaTela(final Bitmap imagem)
   * 
   * @param imagem
   */
  private void atualizaTela(final Bitmap imagem) {

    Log.d(TAG, "método atualizaTela()");

    if (handler == null) {
      Log.w(TAG, "handler is null");
    }

    handler.post(new Runnable() {

      @Override
      public void run() {

        Log.d(TAG, "run()");

        // Fecha a janela de progresso se estiver aberta
        if (dialog != null) {

          dialog.dismiss();

        } else {

        }

        if (imageView != null) {
          imageView.setImageBitmap(imagem);
        } else {
          Log.d(TAG, "imageView is null");
        }

      }

    });

  }

  public String getUrl() {
    return mUrl;
  }

  public void setUrl(String mUrl) {
    this.mUrl = mUrl;
  }

  public ProgressDialog getDialog() {
    return dialog;
  }

  public void setDialog(ProgressDialog dialog) {
    this.dialog = dialog;
  }

  public Context getContext() {
    return context;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  public ImageView getImageView() {
    return imageView;
  }

  public void setImageView(ImageView imageView) {
    this.imageView = imageView;
  }

  public Bitmap getBitmap() {
    return bitmap;
  }

  public void setBitmap(Bitmap image) {
    this.bitmap = image;
  }

}
