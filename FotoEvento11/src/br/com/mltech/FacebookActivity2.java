
package br.com.mltech;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import br.com.mltech.SessionEvents.AuthListener;
import br.com.mltech.SessionEvents.LogoutListener;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

/**
 * 
 * 
 * 
 */
public class FacebookActivity2 extends Activity {

  private static final String TAG = "FacebookActivity2";

  public String accessToken;

  public long accessExpires;

  final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;

  final static int PICK_EXISTING_PHOTO_RESULT_CODE = 1;

  // caixa de diálogo de progresso
  ProgressDialog dialog;

  private Handler mHandler;

  /*
   * Your Facebook Application ID must be set before running this example See
   * http://www.facebook.com/developers/createapp.php
   */
  // public static final String APP_ID = "157111564357680";
  //public static final String APP_ID = "304628302969929";

  public static final String APP_ID = "316626011767784";

  // Facebook facebook = new Facebook("YOUR_APP_ID");
  Facebook facebook = new Facebook(APP_ID);

  public static Bitmap mBitmap = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.facebook);

    Log.d(TAG, "onCreate() ...");

    String[] permissions = { "publish_stream" };

    // Default constructor associates this handler with the queue for the
    // current thread.
    // If there isn't one, this handler won't be able to receive messages.
    mHandler = new Handler();

    facebook.authorize(this, permissions, new DialogListener() {

      /**
       * autorização realizada com sucesso
       */
      public void onComplete(Bundle values) {

        Log.d(TAG, "onComplete()");

        //showBundle(values);

        accessToken = facebook.getAccessToken();
        accessExpires = facebook.getAccessExpires();

        Log.d(TAG, "AccessToken: [" + facebook.getAccessToken() + "]");
        Log.d(TAG, "AccessExpires: [" + facebook.getAccessExpires() + "]");
        Log.d(TAG, "AppId: [" + facebook.getAppId() + "]");

        // Instantiate the asyncrunner object for asynchronous api calls.
        //Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);
        Utility.mAsyncRunner = new AsyncFacebookRunner(facebook);

        // url onde será retirada a foto
        String url = "http://www.facebook.com/images/devsite/iphone_connect_btn.jpg";

        //String caption = "FbAPIs Sample App photo upload";
        String caption = "Facebook APIs - foto upload";

        xxx(url, caption);

      }

      /**
       * erro Facebook na autorização
       */
      public void onFacebookError(FacebookError error) {

        Log.d(TAG, "onFacebookError()");
        showFacebookError(error);

        finish();

      }

      /**
       * 
       */
      public void onError(DialogError e) {

        Log.d(TAG, "onError()");

        finish();

      }

      /**
       * Autorização foi cancelada
       */
      public void onCancel() {

        Log.d(TAG, "onCancel()");

        finish();
      }

    });

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    Log.i(TAG, "onActivityResult() - requestCode: " + requestCode + ", resultCode: " + resultCode + ", data: " + data);

    facebook.authorizeCallback(requestCode, resultCode, data);

    if (data != null) {
      Bundle values = data.getExtras();
      //showBundle(values);
    }

  }

  /**
   * 
   */
  private void xxx(final String url, String caption) {

    Log.d(TAG, "xxx() - url=" + url);

    byte[] data = null;

    mHandler.post(new Runnable() {

      public void run() {

        new FetchImage().execute(url);

      }

    });

    //Bitmap bitmap = Utility.getBitmap(url);

    Bitmap bitmap = mBitmap;

    if (bitmap != null) {

      Log.i(TAG, "Bitmap não é nulo");

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
      data = baos.toByteArray();

    }
    else {
      Log.w(TAG, "Bitmap é nulo");
    }

    /*
     * Source tag: upload_photo_tag
     */
    Bundle params = new Bundle();

    params.putString("url", url);
    params.putString("caption", caption);

    params.putString(Facebook.TOKEN, facebook.getAccessToken());
    params.putString("method", "photos.upload");
    params.putByteArray("picture", data);

    // cria uma caixa de diálogo de progresso
    //dialog = ProgressDialog.show(FacebookActivity2.this, "", "Aguarde...", true, true);
    dialog = ProgressDialog.show(this, "", "Aguarde...", true, true);

    //Utility.mAsyncRunner.request("me/photos", params, "POST", new PhotoUploadListener(), null);
    Utility.mAsyncRunner.request(null, params, "POST", new PhotoUploadListener(), null);

  }

  /**
   * Cria uma janela de diálogo usada como alerta
   * 
   * @param alert
   */
  private void criaAlert(AlertDialog.Builder alert, final String caption, final String url) {

    alert.setTitle("Título");
    alert.setMessage("Mensagem");

    alert.setPositiveButton("botão +",
        new DialogInterface.OnClickListener() {

          public void onClick(DialogInterface dialog, int which) {

            Intent intent = new Intent(Intent.ACTION_PICK, (MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
            startActivityForResult(intent, PICK_EXISTING_PHOTO_RESULT_CODE);
          }

        }

        );

    alert.setNegativeButton("botão -",
        new DialogInterface.OnClickListener() {

          public void onClick(DialogInterface dialog, int which) {

            /*
             * Source tag: upload_photo_tag
             */
            Bundle params = new Bundle();
            params.putString("url", url);
            params.putString("caption", caption);

            // Utility.mAsyncRunner.request("me/photos", params, "POST", new
            // PhotoUploadListener(), null);

            Utility.mAsyncRunner.request("me/photos", params, "POST", (RequestListener) new PhotoUploadListener(), null);

          }

        }

        );

    alert.setOnCancelListener(
        new DialogInterface.OnCancelListener() {

          public void onCancel(DialogInterface d) {

            // desfaz o diálogo
            dialog.dismiss();

          }

        }

        );

  }

  /**
   * Listener usado no upload da foto
   * 
   * 
   */
  class PhotoUploadListener extends BaseRequestListener {

    /**
     * operação executada com sucesso
     */
    public void onComplete(final String response, final Object state) {

      // Dismiss this dialog, removing it from the screen.
      if (dialog != null) {
        dialog.dismiss();
      }

      // Causes the Runnable r to be added to the message queue.
      // The runnable will be run on the thread to which this handler is
      // attached.
      mHandler.post(new Runnable() {

        // executa o diálogo de resultado do upload da foto
        public void run() {

          // 
          new UploadPhotoResultDialog(FacebookActivity2.this, "Upload da foto foi executado", response).show();

        }

      });

    }

    @Override
    public void onFacebookError(FacebookError e, Object state) {

      // TODO Auto-generated method stub
      super.onFacebookError(e, state);

      Log.d(TAG, "onFacebookError");

    }

    @Override
    public void onFileNotFoundException(FileNotFoundException e, Object state) {

      // TODO Auto-generated method stub
      super.onFileNotFoundException(e, state);

      Log.d(TAG, "onFileNotFoundException");
    }

    @Override
    public void onIOException(IOException e, Object state) {

      // TODO Auto-generated method stub
      super.onIOException(e, state);
      Log.d(TAG, "onIOException");
    }

    @Override
    public void onMalformedURLException(MalformedURLException e, Object state) {

      // TODO Auto-generated method stub
      super.onMalformedURLException(e, state);
      Log.d(TAG, "onMalformedURLException");

    }

  }

  /**
   * Listener para tratar do diálogo de login
   * 
   * 
   * 
   */
  private final class LoginDialogListener implements DialogListener {

    /**
     * login executado com sucesso
     */
    public void onComplete(Bundle values) {

      SessionEvents.onLoginSuccess();
    }

    /**
     * login executado com erro (do Facebook)
     */
    public void onFacebookError(FacebookError error) {

      SessionEvents.onLoginError(error.getMessage());
    }

    /**
     * login executado com erro
     */
    public void onError(DialogError error) {

      SessionEvents.onLoginError(error.getMessage());
    }

    public void onCancel() {

      SessionEvents.onLoginError("Action Canceled");
    }

  }

  /**
   * Listener para cuidadar do processo de logout
   * 
   */
  private class LogoutRequestListener extends BaseRequestListener {

    /**
     * operação executada com sucesso
     */
    public void onComplete(String response, final Object state) {

      /*
       * callback should be run in the original thread, not the background
       * thread
       */
      mHandler.post(new Runnable() {

        public void run() {

          SessionEvents.onLogoutFinish();
        }

      });

    }

    public void onFacebookError(FacebookError e, Object state) {

    }

  }

  /**
   * 
   * Listener de sessão
   * 
   */
  private class SessionListener implements AuthListener, LogoutListener {

    /**
     * A autorização da sessão feita com sucesso
     */
    public void onAuthSucceed() {

      // salva a sessão
      SessionStore.save(facebook, FacebookActivity2.this);

    }

    /**
     * Falha na autorização da sessão
     */
    public void onAuthFail(String error) {

    }

    /**
     * 
     */
    public void onLogoutBegin() {

    }

    /**
     * Chamado no termino da sessão
     */
    public void onLogoutFinish() {

      // limpa a sessão
      SessionStore.clear(FacebookActivity2.this);

    }

  }

  /**
   * Busca uma imagem
   * 
   * 
   * 
   */
  private class FetchImage extends AsyncTask<String, Void, Bitmap> {

    /**
     * Obtem um bitmap a partir de sua URL
     */
    protected Bitmap doInBackground(String... urls) {

      Log.d(TAG, "FetchImage() - Lendo o arquivo: " + urls[0]);

      return Utility.getBitmap(urls[0]);

    }

    /**
     * Exibe o bitmap recebido
     */
    protected void onPostExecute(Bitmap result) {

      dialog.dismiss();

      mBitmap = result;

      // mUploadedPhoto.setImageBitmap(result);

    }

  }

  /**
   * Exibe informações sobre o erro Facebook
   * 
   * @param error
   *          instância de um erro do facebook
   * 
   */
  private void showFacebookError(FacebookError error) {

    Log.i(TAG, "ShowFacebookError:");

    Log.w(TAG, "FacebookError: " + error);
    Log.w(TAG, "FacebookError: getErrorCode(): " + error.getErrorCode());
    Log.w(TAG, "FacebookError: getErrorType(): " + error.getErrorType());
    Log.w(TAG, "FacebookError: getMessage(): " + error.getMessage());
    Log.w(TAG, "FacebookError: getLocalizedMessage(): " + error.getLocalizedMessage());
    Log.w(TAG, "FacebookError: getCause(): " + error.getCause());
    Log.w(TAG, "FacebookError: getClass(): " + error.getClass());
  }

  /**
   * Exibe a relação de chaves e valores de um Bundle
   * 
   * @param values
   * 
   */
  private void showBundle(Bundle values) {

    // Bundle: mapping from String values to various Parcelable types.

    Log.i(TAG, "------------------------------------------------------------");
    Log.i(TAG, "ShowBundle:");

    if (values != null) {

      Set<String> chaves = values.keySet();

      if (chaves != null) {

        Log.d(TAG, "Nº de chaves:" + chaves.size());

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

}
