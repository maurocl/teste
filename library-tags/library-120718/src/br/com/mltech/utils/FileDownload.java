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

/**
 * 
 */
public class FileDownload {

  /**
   * 
   */
  private static final String TAG = "FileDownload";

  /**
   * URL
   */
  private String mUrl;

  /**
   * contexto da aplica��o
   */
  private Context context;

  /**
   * componente onde a imagem ser� visualizada
   */
  private ImageView imageView;

  /**
   * ProgressDialog
   */
  private ProgressDialog dialog;

  /**
   * 
   */
  private Handler handler = new Handler();

  /**
   * imagem
   */
  private Bitmap bitmap;

  /**
   * FileDownload(String url)
   * 
   * M�todo construtor
   * 
   * @param url
   *          URL do arquivo
   * 
   */
  public FileDownload(String url) {

    this(url, null, null);
  }

  /**
   * FileDownload(String url, Context ctx, ImageView imageView)
   * 
   * M�todo construtor
   * 
   * @param url
   *          URL
   * @param ctx
   *          Contexto
   * @param imageView
   *          Componente onde o bitmap ser� visualizado
   * 
   */
  public FileDownload(String url, Context ctx, ImageView imageView) {

    this.mUrl = url;
    this.context = ctx;
    this.imageView = imageView;

    Log.d(TAG, "construtor - FileDownload: " + url);

    if (context != null) {
      dialog = ProgressDialog.show(context, "Exemplo", "Buscando imagem, aguarde ...", false, true);
    }

    if (dialog == null) {
      Log.d(TAG, "Dialog is null");
    } else {
      Log.d(TAG, "Dialog n�o �  null");
    }

  }

  /**
   * downloadImage()
   * 
   * Executa o download de um arquivo de imagem
   */
  public void downloadImage() {

    Log.d(TAG, "m�todo downloadImage");

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

          Log.w(TAG, "MalformedURLException",e);

          return;

        } catch (IOException e) {

         
          e.printStackTrace();

        }

      }

    };

    t.start();

  }

  /**
   * atualizaTela(final Bitmap imagem)
   * 
   * @param imagem Bitmap usado para atualizar a tela
   * 
   */
  private void atualizaTela(final Bitmap imagem) {

    Log.d(TAG, "m�todo atualizaTela()");

    if (handler == null) {
      Log.w(TAG, "handler is null");
    }

    handler.post(new Runnable() {

      //@Override
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

  /**
   * @return a URL de origem
   */
  public String getUrl() {

    return mUrl;
  }

  /**
   * Atualiza a URL de onde ser� feito o download
   * 
   * @param mUrl URL de onde ser� feito o download
   */
  public void setUrl(String mUrl) {

    this.mUrl = mUrl;
  }

  /**
   * Obt�m um di�logo de progresso da atividade
   * 
   * @return um ProgressDialog
   */
  public ProgressDialog getDialog() {

    return dialog;
  }

  /**
   * Estabelece um di�logo de progresso da atividade
   * 
   * @param dialog Inst�ncia de ProgressDialog
   */
  public void setDialog(ProgressDialog dialog) {

    this.dialog = dialog;
  }

  /**
   * Obtem o contexto da aplica��o
   * 
   * @return O contexto da aplica��o
   * 
   */
  public Context getContext() {

    return context;
  }

  /**
   * Estabelece o contexto da aplica��o
   * 
   * @param context Contexto da aplica��o
   * 
   */
  public void setContext(Context context) {

    this.context = context;
  }

  /**
   * Obt�m o componente de visualiza��o da imagem.
   * 
   * @return o componente de visualiza��o
   */
  public ImageView getImageView() {

    return imageView;
  }

  /**
   * Estabelece o componente de visualiza��o da imagem
   * 
   * @param imageView o componente de visualiza��o
   * 
   */
  public void setImageView(ImageView imageView) {

    this.imageView = imageView;
  }

  /**
   * Obt�m a imagem
   * 
   * @return o bitmap com a imagem
   * 
   */
  public Bitmap getBitmap() {

    return bitmap;
  }

  /**
   * Seta a imagem
   * 
   * @param image bitmap contendo a imagem
   * 
   */
  public void setBitmap(Bitmap image) {

    this.bitmap = image;
  }

}
