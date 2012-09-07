
package br.com.mltech;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.android.FacebookError;
import com.facebook.android.Util;

/**
 * Define uma caixa de di�logo usada para exibir o resultado do upload de foto
 * 
 * 
 */
public class UploadPhotoResultDialog extends Dialog {

  private final static String TAG = "UploadPhotoResultDialog";

  /**
   * resposta, identificador da foto
   */
  private String response, photo_id;

  /**
	 * 
	 */
  private TextView mOutput, mUsefulTip;

  /**
	 * Bot�es da caixa de di�logo
	 */
  private Button mViewPhotoButton, mTagPhotoButton;

  /**
	 * Imagem View onde a foto ser� exibida
	 */
  private ImageView mUploadedPhoto;

  /**
	 * 
	 */
  private Activity activity;

  /**
	 * di�logo de progresso
	 */
  private ProgressDialog dialog;

  /**
	 * 
	 */
  private boolean hidePhoto = false;

  /**
	 * 
	 */
  private Handler mHandler;

  /**
   * Construtor
   * 
   * @param activity
   *          activity
   * @param title
   *          t�tulo
   * @param response
   *          resposta
   * 
   */
  public UploadPhotoResultDialog(Activity activity, String title, String response) {

    super(activity);

    this.activity = activity;

    this.response = response;

    setTitle(title);

  }

  /**
	 * 
	 */
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    mHandler = new Handler();

    setContentView(R.layout.upload_photo_response);

    LayoutParams params = getWindow().getAttributes();

    params.width = LayoutParams.FILL_PARENT;
    params.height = LayoutParams.FILL_PARENT;

    getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

    mOutput           = (TextView) findViewById(R.id.apiOutput);
    mUsefulTip        = (TextView) findViewById(R.id.usefulTip);
    mViewPhotoButton  = (Button) findViewById(R.id.view_photo_button);
    mTagPhotoButton   = (Button) findViewById(R.id.tag_photo_button);
    mUploadedPhoto    = (ImageView) findViewById(R.id.uploadedPhoto);

    JSONObject json;

    try {

      json = Util.parseJson(response);

      Log.d(TAG, "response(1)=" + json.toString(2));

      Log.d(TAG, "response(2)=" + response);

      // identificador da foto
      final String photo_id = json.getString("id");

      this.photo_id = photo_id;

      Log.d(TAG, "photo_id=" + photo_id);

      mOutput.setText(json.toString(2));

      mUsefulTip.setText("R.string.photo_tip");

      Linkify.addLinks(mUsefulTip, Linkify.WEB_URLS);

      // tratador de eventos usado para visualizar a foto
      mViewPhotoButton.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {

          if (hidePhoto) {

            mViewPhotoButton.setText("R.string.view_photo");
            hidePhoto = false;
            mUploadedPhoto.setImageBitmap(null);

          } else {

            hidePhoto = true;

            // mViewPhotoButton("R.string.hide_photo");

            /*
             * Source tag: view_photo_tag
             */
            Bundle params = new Bundle();
            params.putString("fields", "picture");

            //
            dialog = ProgressDialog.show(activity, "", "R.string.please_wait", true, true);

            dialog.show();

            // executa uma requisi��o ass�ncrona para exibir a foto
            Utility.mAsyncRunner.request(photo_id, params, new ViewPhotoRequestListener());

          }

        }
      });

      mTagPhotoButton.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {

          /*
           * Source tag: tag_photo_tag
           */
          setTag();
        }

      });

    } catch (JSONException e) {

      setText("R.string.exception: " + e.getMessage());

    } catch (FacebookError e) {

      setText("R.string.facebook_error: " + e.getMessage());

    }

  }

  /**
   * Estabelece um tag a foto
   * 
   */
  public void setTag() {

    String relativePath = photo_id + "/tags/" + Utility.userUID;

    Bundle params = new Bundle();

    params.putString("x", "5");
    params.putString("y", "5");

    Utility.mAsyncRunner.request(relativePath, params, "POST", new TagPhotoRequestListener(), null);

  }

  /**
   * Listener de Requisi��o de Visualiza��o da Foto
   * 
   * 
   */
  public class ViewPhotoRequestListener extends BaseRequestListener {

    /**
     * A��o executada com sucesso
     */
    public void onComplete(final String response, final Object state) {

      try {

        JSONObject json = Util.parseJson(response);

        final String pictureURL = json.getString("picture");

        if (TextUtils.isEmpty(pictureURL)) {

          setText("Error getting \'picture\' field of the photo");

        } else {

          mHandler.post(new Runnable() {

            public void run() {

              new FetchImage().execute(pictureURL);
            }

          });
        }
      } catch (JSONException e) {

        dialog.dismiss();
        setText("R.string.exception: " + e.getMessage());

      } catch (FacebookError e) {

        dialog.dismiss();
        setText("R.string.facebook_error" + e.getMessage());

      }

    }

    /**
     * Erro na execu��o do Facebook
     * 
     * @param error
     */
    public void onFacebookError(FacebookError error) {

      setText("R.string.facebook_error: " + error.getMessage());
      dialog.dismiss();
      

    }

  }

  /**
   * Listener de Requisi��o do Tag da Foto
   * 
   * 
   */
  public class TagPhotoRequestListener extends BaseRequestListener {

    /**
     * A��o executada com sucesso
     */
    public void onComplete(final String response, final Object state) {

      if (response.equals("true")) {

        String message = "User tagged in photo at (5, 5)" + "\n";
        message += "Api Response: " + response;
        setText(message);

      } else {

        setText("Usu�rio n�o pode ser etiquetado (tagged, marcado).");

      }

    }

    /**
     * Erro na execu��o do Facebook
     * 
     * @param error
     * 
     */
    public void onFacebookError(FacebookError error) {

      setText("R.string.facebook_error: " + error.getMessage());
      
    }

  }

  /**
   * Estabelece o texto
   * 
   * @param txt
   *          Texto
   * 
   */
  public void setText(final String txt) {

    mHandler.post(new Runnable() {

      public void run() {

        mOutput.setText(txt);

      }

    });

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

      return Utility.getBitmap(urls[0]);

    }

    /**
     * Exibe o bitmap recebido
     */
    protected void onPostExecute(Bitmap result) {

      dialog.dismiss();

      mUploadedPhoto.setImageBitmap(result);

    }

  }

}
