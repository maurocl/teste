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

	// nº de segundos até que o token de acesso expire
	public long accessExpires;

	// nome do arquivo contendo a foto
	String mFilename;

	// bitmap contendo a foto
	public static Bitmap mBitmap = null;

	// caixa de diálogo de progresso
	ProgressDialog dialog;

	private Handler mHandler;

	/*
	 * Your Facebook Application ID must be set before running this example See
	 * http://www.facebook.com/developers/createapp.php
	 */
	public static final String APP_ID = "316626011767784";

	// Cria uma instância do Facebook passando o identificador da aplicação
	Facebook facebook = new Facebook(APP_ID);

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.facebook);

		Log.d(TAG, "onCreate() ...");

		// obtém informações sobre a intent chamadora
		Intent intent = getIntent();

		if (intent != null) {

			// obtem o nome do arquivo passado
			mFilename = intent.getStringExtra("br.com.mltech.filename");

			// Uri data = intent.getData();
			if ((mFilename != null) && (!mFilename.equals(""))) {
				Log.i(TAG, "Recebido: " + mFilename);

			} else {
				Log.w(TAG, "Filename é nulo");
				mFilename = "/mnt/sdcard/Pictures/fotoevento/fotos/20120820_180211.jpg";
				Log.d(TAG, "Nenhum parâmetro passado. Usando arquivo: " + mFilename);
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
			 * autorização realizada com sucesso
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
				// Criação da Intent com resultado da execução da Activity
				// ---------------------------------------------------------
				Intent it = new Intent();

				// it.putExtra(Constantes.PARTICIPANTE, novoParticipante);

				setResult(RESULT_OK, it);

				finish();

			}

			/**
			 * erro Facebook na autorização
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
			 * Autorização foi cancelada
			 */
			public void onCancel() {

				Log.d(TAG, "onCancel() - Operação cancelada pelo usuário");

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

				// Utility.mAsyncRunner.request("me/photos", params, "POST",
				// (RequestListener) new PhotoUploadListener(), null);
				Utility.mAsyncRunner.request("me/photos", params, "POST", new PhotoUploadListener(), null);

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
			if (dialog != null) {
				dialog.dismiss();
			}

			Log.d(TAG, "response=" + response);

			// Causes the Runnable r to be added to the message queue.
			// The runnable will be run on the thread to which this handler is
			// attached.
			mHandler.post(new Runnable() {

				// executa o diálogo de resultado do upload da foto
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
	 * Listener para tratar do diálogo de login
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
	 * Executa um post de uma imagem no mural do Facebook.
	 * 
	 * @param filename
	 *          Nome do arquivo onde está a foto
	 * @param caption Título da foto          
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

		// Cria um stream de saída de bytes
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// Escreve o bitmap para o stream de saída
		bi.compress(Bitmap.CompressFormat.JPEG, 100, baos);

		// Obtem um array de bytes com o bitmap
		data = baos.toByteArray();

		Bundle params = new Bundle();

		// Configura os parâmetros do post
		params.putString(Facebook.TOKEN, facebook.getAccessToken());
		params.putString("method", "photos.upload");
		params.putByteArray("picture", data);
		params.putString("caption", caption);

		AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);

		// Executa uma requisição de Post
		mAsyncRunner.request(null, params, "POST", new PhotoUploadListener(), null);

	}

}
