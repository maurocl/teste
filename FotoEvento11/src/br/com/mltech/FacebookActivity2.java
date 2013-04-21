package br.com.mltech;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import br.com.mltech.SessionEvents.AuthListener;
import br.com.mltech.SessionEvents.LogoutListener;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

/**
 * Activity de interface com o Facebook
 * 
 * 
 */
public class FacebookActivity2 extends Activity {

	private static final String TAG = "FacebookActivity2";

	final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;

	final static int PICK_EXISTING_PHOTO_RESULT_CODE = 1;

	// string contendo o token de acesso ao Facebook
	public String accessToken;

	// n� de segundos at� que o token de acesso expire
	public long accessExpires;

	// nome do arquivo contendo a foto
	String mFilename;

	// bitmap contendo a foto
	public static Bitmap mBitmap = null;

	// caixa de di�logo de progresso
	ProgressDialog dialog;

	private Handler mHandler;

	/*
	 * Your Facebook Application ID must be set before running this example See
	 * http://www.facebook.com/developers/createapp.php
	 */
	public static final String APP_ID = "316626011767784";

	// Cria uma inst�ncia do Facebook passando o identificador da aplica��o
	Facebook facebook = new Facebook(APP_ID);

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.facebook);

		Log.d(TAG, "onCreate() ...");

		// obt�m informa��es sobre a intent chamadora
		Intent intent = getIntent();

		if (intent != null) {

			// obtem o nome do arquivo passado
			mFilename = intent.getStringExtra("br.com.mltech.filename");

			// Uri data = intent.getData();
			if ((mFilename != null) && (!mFilename.equals(""))) {
				Log.i(TAG, "Recebido: " + mFilename);

			} else {
				Log.w(TAG, "Filename � nulo");
				mFilename = "/mnt/sdcard/Pictures/fotoevento/fotos/20120820_180211.jpg";
				Log.d(TAG, "Nenhum par�metro passado. Usando arquivo: " + mFilename);
			}

		} else {
			Log.d(TAG, "Passei aqui");

		}

		String[] permissions = { "publish_stream" };

		// Default constructor associates this handler with the queue for the
		// current thread.
		// If there isn't one, this handler won't be able to receive messages.
		mHandler = new Handler();

		facebook.authorize(this, permissions, new DialogListener() {

			/**
			 * autoriza��o realizada com sucesso
			 */
			public void onComplete(Bundle values) {

				Log.d(TAG, "onComplete()");

				// showBundle(values);

				accessToken = facebook.getAccessToken();
				accessExpires = facebook.getAccessExpires();

				Log.d(TAG, "AccessToken: [" + facebook.getAccessToken() + "]");
				Log.d(TAG, "AccessExpires: [" + facebook.getAccessExpires() + "]");
				Log.d(TAG, "AppId: [" + facebook.getAppId() + "]");

				// Instantiate the asyncrunner object for asynchronous api calls.
				// Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);
				Utility.mAsyncRunner = new AsyncFacebookRunner(facebook);

				// String caption = "FbAPIs Sample App photo upload";
				String caption = "Facebook APIs - foto upload - " + System.currentTimeMillis();

				// Executa o post da foto no facebook
				postImageOnWall(mFilename, caption);

				// ---------------------------------------------------------
				// Cria��o da Intent com resultado da execu��o da Activity
				// ---------------------------------------------------------
				Intent it = new Intent();

				// it.putExtra(Constantes.PARTICIPANTE, novoParticipante);

				setResult(RESULT_OK, it);

				finish();

			}

			/**
			 * erro Facebook na autoriza��o
			 */
			public void onFacebookError(FacebookError error) {

				Log.d(TAG, "onFacebookError()");

				finalizaActivity();

			}

			/**
       * 
       */
			public void onError(DialogError e) {

				Log.d(TAG, "onError()");

				finalizaActivity();

			}

			/**
			 * Autoriza��o foi cancelada
			 */
			public void onCancel() {

				Log.d(TAG, "onCancel() - Opera��o cancelada pelo usu�rio");

				finalizaActivity();
			}

		});

	}

	/**
	 * Finaliza a Activity
	 * 
	 */
	private void finalizaActivity() {

		Intent it = new Intent();

		// it.putExtra(Constantes.PARTICIPANTE, novoParticipante);

		setResult(RESULT_CANCELED, it);

		finish();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		Log.i(TAG, "onActivityResult() - requestCode: " + requestCode + ", resultCode: " + resultCode + ", data: " + data);

		facebook.authorizeCallback(requestCode, resultCode, data);

		if (data != null) {

			Bundle values = data.getExtras();
			// showBundle(values);
		}

	}

	/**
	 * Cria uma janela de di�logo usada como alerta
	 * 
	 * @param alert
	 */
	private void criaAlert(AlertDialog.Builder alert, final String caption, final String url) {

		alert.setTitle("T�tulo");
		alert.setMessage("Mensagem");

		alert.setPositiveButton("bot�o +", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				Intent intent = new Intent(Intent.ACTION_PICK, (MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
				startActivityForResult(intent, PICK_EXISTING_PHOTO_RESULT_CODE);
			}

		}

		);

		alert.setNegativeButton("bot�o -", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				/*
				 * Source tag: upload_photo_tag
				 */
				Bundle params = new Bundle();
				params.putString("url", url);
				params.putString("caption", caption);

				// Utility.mAsyncRunner.request("me/photos", params, "POST", new
				// PhotoUploadListener(), null);

				// Utility.mAsyncRunner.request("me/photos", params, "POST",
				// (RequestListener) new PhotoUploadListener(), null);
				Utility.mAsyncRunner.request("me/photos", params, "POST", new PhotoUploadListener(), null);

			}

		}

		);

		alert.setOnCancelListener(new DialogInterface.OnCancelListener() {

			public void onCancel(DialogInterface d) {

				// desfaz o di�logo
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
		 * opera��o executada com sucesso
		 */
		public void onComplete(final String response, final Object state) {

			// Dismiss this dialog, removing it from the screen.
			if (dialog != null) {
				dialog.dismiss();
			}

			Log.d(TAG, "response=" + response);

			// Causes the Runnable r to be added to the message queue.
			// The runnable will be run on the thread to which this handler is
			// attached.
			mHandler.post(new Runnable() {

				// executa o di�logo de resultado do upload da foto
				public void run() {

					//
					// new UploadPhotoResultDialog(FacebookActivity2.this,
					// "Upload da foto foi executado", response).show();

				}

			});

			Log.d(TAG, "Arquivo uploaded com sucesso !!!");

		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {

			super.onFacebookError(e, state);

			Log.d(TAG, "onFacebookError");

		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e, Object state) {

			super.onFileNotFoundException(e, state);

			Log.d(TAG, "onFileNotFoundException");
		}

		@Override
		public void onIOException(IOException e, Object state) {

			super.onIOException(e, state);
			Log.d(TAG, "onIOException");
		}

		@Override
		public void onMalformedURLException(MalformedURLException e, Object state) {

			super.onMalformedURLException(e, state);
			Log.d(TAG, "onMalformedURLException");

		}

	}

	/**
	 * Listener para tratar do di�logo de login
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
		 * opera��o executada com sucesso
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
	 * Listener de sess�o
	 * 
	 */
	private class SessionListener implements AuthListener, LogoutListener {

		/**
		 * A autoriza��o da sess�o feita com sucesso
		 */
		public void onAuthSucceed() {

			// salva a sess�o
			SessionStore.save(facebook, FacebookActivity2.this);

		}

		/**
		 * Falha na autoriza��o da sess�o
		 */
		public void onAuthFail(String error) {

		}

		/**
     * 
     */
		public void onLogoutBegin() {

		}

		/**
		 * Chamado no termino da sess�o
		 */
		public void onLogoutFinish() {

			// limpa a sess�o
			SessionStore.clear(FacebookActivity2.this);

		}

	}

	/**
	 * Executa um post de uma imagem no mural do Facebook.
	 * 
	 * @param filename
	 *          Nome do arquivo onde est� a foto
	 * @param caption T�tulo da foto          
	 */
	public void postImageOnWall(String filename, String caption) {

		byte[] data = null;

		if(filename==null) {
			return;
		}
		
		if(caption==null) {
			return;
		}
		
		// decodifica o bitmap a partir do arquivo
		Bitmap bi = BitmapFactory.decodeFile(filename);

		// Cria um stream de sa�da de bytes
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// Escreve o bitmap para o stream de sa�da
		bi.compress(Bitmap.CompressFormat.JPEG, 100, baos);

		// Obtem um array de bytes com o bitmap
		data = baos.toByteArray();

		Bundle params = new Bundle();

		// Configura os par�metros do post
		params.putString(Facebook.TOKEN, facebook.getAccessToken());
		params.putString("method", "photos.upload");
		params.putByteArray("picture", data);
		params.putString("caption", caption);

		AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);

		// Executa uma requisi��o de Post
		mAsyncRunner.request(null, params, "POST", new PhotoUploadListener(), null);

	}

}
