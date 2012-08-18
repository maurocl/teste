
package com.facebook.android;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

/**
 * Diálogo de atualização do status do resultado
 * 
 * 
 */
public class UpdateStatusResultDialog extends Dialog {

  /**
	 * 
	 */
  private Bundle values;

  /**
	 * 
	 */
  private TextView mOutput, mUsefulTip;

  /**
	 * 
	 */
  private Button mViewPostButton, mDeletePostButton;

  /**
	 * 
	 */
  private Activity activity;

  /**
   * There are two main uses for a Handler: (1) to schedule messages and
   * runnables to be executed as some point in the future; and (2) to enqueue an
   * action to be performed on a different thread than your own.
   */
  private Handler mHandler;

  /**
   * Construtor
   * 
   * @param activity
   * @param title
   * @param values
   * 
   */
  public UpdateStatusResultDialog(Activity activity, String title, Bundle values) {

    super(activity);

    this.activity = activity;
    this.values = values;

    setTitle(title);

  }

  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    // cria um novo handler
    mHandler = new Handler();

    setContentView(R.layout.update_post_response);

    LayoutParams params = getWindow().getAttributes();

    params.width = LayoutParams.FILL_PARENT;
    params.height = LayoutParams.FILL_PARENT;

    getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

    mOutput = (TextView) findViewById(R.id.apiOutput);
    mOutput.setText(values.toString());

    mUsefulTip = (TextView) findViewById(R.id.usefulTip);
    mUsefulTip.setMovementMethod(LinkMovementMethod.getInstance());

    mViewPostButton = (Button) findViewById(R.id.view_post_button);
    mDeletePostButton = (Button) findViewById(R.id.delete_post_button);

    final String postId = values.getString("post_id");

    mViewPostButton.setOnClickListener(new View.OnClickListener() {

      public void onClick(View v) {

        /*
         * Source tag: view_post_tag
         */
        Utility.mAsyncRunner.request(postId, new WallPostRequestListener());

      }

    });

    mDeletePostButton.setOnClickListener(new View.OnClickListener() {

      public void onClick(View v) {

        /*
         * Source tag: delete_post_tag
         */
        Utility.mAsyncRunner.request(postId, new Bundle(), "DELETE", new WallPostDeleteListener(), null);
        
      }

    });

  }

  /**
   * Listener de requisição de post no mural
   * 
   * 
   */
  public class WallPostRequestListener extends BaseRequestListener {

    /**
     * comando executado com sucesso
     */
    public void onComplete(final String response, final Object state) {

      try {

        // obtem a resposta (como um objeto JSON)
        JSONObject json = new JSONObject(response);

        setText(json.toString(2));

      } catch (JSONException e) {

        setText(activity.getString(R.string.exception) + e.getMessage());

      }

    }

    /**
     * falha na execução do comando
     * 
     * @param error
     */
    public void onFacebookError(FacebookError error) {

      setText(activity.getString(R.string.facebook_error) + error.getMessage());

    }

  }

  /**
   * listener de remoção de um post no mural
   * 
   * 
   */
  public class WallPostDeleteListener extends BaseRequestListener {

    /**
     * Comando executado com sucesso
     */
    public void onComplete(final String response, final Object state) {

      if (response.equals("true")) {

        // post removido com sucesso
        String message = "Wall Post deleted" + "\n";

        message += "Api Response: " + response;

        // atualiza a resposta
        setText(message);

      } else {

        // falha na remoção do post
        setText("wall post could not be deleted");

      }

    }

    /**
     * 
     * @param error
     */
    public void onFacebookError(FacebookError error) {

      setText(activity.getString(R.string.facebook_error) + error.getMessage());
    }

  }

  /**
   * Atualiza a mensagem
   * 
   * @param txt
   *          mensagem
   * 
   */
  public void setText(final String txt) {

    // Causes the Runnable r to be added to the message queue. 
    // The runnable will be run on the thread to which this handler is attached.
    mHandler.post(new Runnable() {

      // atualiza a mensagem
      public void run() {

        mOutput.setText(txt);
      }

    });

  }

}
