package br.com.mltech;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.Util;

/**
 * 
 * @author maurocl
 * 
 */
public class FacebookActivity extends Activity {

	private static final String TAG = "FacebookActivity";

	/*
	 * Your Facebook Application ID must be set before running this example See
	 * http://www.facebook.com/developers/createapp.php
	 */
	// public static final String APP_ID = "157111564357680";
	public static final String APP_ID = "304628302969929";

	final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;

	final static int PICK_EXISTING_PHOTO_RESULT_CODE = 1;

	// caixa de diálogo de progresso
	ProgressDialog dialog;

	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		if (APP_ID == null) {
			Util.showAlert(this, "Aviso", "Facebook Applicaton ID must be "
					+ "specified before running this application: see FbAPIs.java");
			return;
		}

		super.onCreate(savedInstanceState);

		// Default constructor associates this handler with the queue for the
		// current thread.
		// If there isn't one, this handler won't be able to receive messages.
		mHandler = new Handler();

		// Create the Facebook Object using the app id.
		Utility.mFacebook = new Facebook(APP_ID);

		// Instantiate the asyncrunner object for asynchronous api calls.
		Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);

		xxx();

	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	/**
	 * 
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		/*
		 * if this is the activity result from authorization flow, do a call back to
		 * authorizeCallback Source Tag: login_tag
		 */
		case AUTHORIZE_ACTIVITY_RESULT_CODE: {
			// Utility.mFacebook.authorizeCallback(requestCode, resultCode, data);
			break;
		}

		/*
		 * if this is the result for a photo picker from the gallery, upload the
		 * image after scaling it. You can use the Utility.scaleImage() function for
		 * scaling
		 */
		case PICK_EXISTING_PHOTO_RESULT_CODE: {

			if (resultCode == Activity.RESULT_OK) {

				// Retrieve data this intent is operating on
				Uri photoUri = data.getData();

				if (photoUri != null) {

					// há uma foto selecionada

					Bundle params = new Bundle();

					// try {
					// Inserts a byte array value into the mapping of this Bundle,
					// replacing any existing value for the given key.
					// Either key or value may be null.
					// params.putByteArray("photo",
					// Utility.scaleImage(getApplicationContext(), photoUri));

					// } catch (IOException e) {

					// e.printStackTrace();

					// }

					params.putString("caption", "FbAPIs Sample App photo upload");

					// Utility.mAsyncRunner.request("me/photos", params, "POST", new
					// PhotoUploadListener(), null);

				} else {

					Toast.makeText(getApplicationContext(), "Error selecting image from the gallery.", Toast.LENGTH_SHORT).show();

				}
			} else {

				Toast.makeText(getApplicationContext(), "No image selected for upload.", Toast.LENGTH_SHORT).show();

			}
			break;
		}

		}

	}

	/**
	 * 
	 */
	private void xxx() {

		if (!Utility.mFacebook.isSessionValid()) {

			// sessão não é válida
			Util.showAlert(this, "Aviso", "Você precisa primeiro loggar no Facebook.");

			FacebookLogin fbl = new FacebookLogin(this);

			fbl.executaLogin(Utility.mFacebook);

		} else {

			// cria uma caixa de diálogo de progresso
			dialog = ProgressDialog.show(FacebookActivity.this, "", "Aguarde...", true, true);

			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			String caption = "Foto enviada pela aplicação FotoEvento";
			String url = "http://www.facebook.com/images/devsite/iphone_connect_btn.jpg";

			criaAlert(alert, caption, url);

			alert.show();

		}

	}

	/**
	 * Cria uma janela de diálogo usada como alerta
	 * 
	 * @param alert
	 */
	private void criaAlert(AlertDialog.Builder alert, final String caption, final String url) {

		alert.setTitle("Título");
		alert.setMessage("Mensagem");

		alert.setPositiveButton("botão +", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				Intent intent = new Intent(Intent.ACTION_PICK, (MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
				startActivityForResult(intent, PICK_EXISTING_PHOTO_RESULT_CODE);
			}

		}

		);

		alert.setNegativeButton("botão -", new DialogInterface.OnClickListener() {

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

		alert.setOnCancelListener(new DialogInterface.OnCancelListener() {

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
			dialog.dismiss();

			// Causes the Runnable r to be added to the message queue.
			// The runnable will be run on the thread to which this handler is
			// attached.
			mHandler.post(new Runnable() {

				// executa o diálogo de resultado do upload da foto
				public void run() {

					//
					new UploadPhotoResultDialog(FacebookActivity.this, "Upload da foto foi executado", response).show();

				}

			});

		}

	}

}
