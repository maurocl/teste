package br.com.mltech.view;

import java.io.File;
import java.util.concurrent.ExecutionException;

import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;
import twitter4j.media.MediaProvider;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

/**
 * Activity respons�vel pelo envio do post ao Twitter.
 * 
 *
 */
public class TwitterActivity extends Activity {

	public static final String TAG = "TwitterActivity";

	private String filename; // nome do arquivo contendo a foto
	private String text; // mensagem

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		
		// Obt�m a intent chamadora
		Intent i = getIntent();
		
		// Obt�m as informa��es sobre a intent chamados
		Bundle bundle = i.getExtras();
		
		// S�o aguardadas as seguintes informa��es:
		// br.com.mltech.filename
		// br.com.mltech.text
		//
		if(bundle!=null) {
			if(bundle.containsKey("br.com.mltech.filename")) {
				filename=bundle.getString("br.com.mltech.filename");
			}
			else {
				filename=null;
			}
			if(bundle.containsKey("br.com.mltech.text")) {
				text = bundle.getString("br.com.mltech.text");
			}
			else {
				text=null;
			}
		}
		else {
			Log.w(TAG,"Par�metros filename e text est�o nulos");
			filename = "/mnt/sdcard/Pictures/fotoevento/fotos/20120823_182339.png";
			text = "texto";

		}
		
		Log.v(TAG,"onCreate() - Filename: "+filename);
		Log.v(TAG,"onCreate() - Text: "+text);
		
		String result = executaEnviaFotoTwitter(filename,text);
		
		//Intent resposta = new Intent();
		
		if(result!=null) {
			setResult(Activity.RESULT_OK);
			finish();
		}
		else {
			setResult(Activity.RESULT_CANCELED);
			finish();
		}

	}

	/**
	 * Cria um thread para envio de uma foto ao Twitter
	 * 
	 * @param filename nome do arquivo onde se encontra a foto
	 * @param text texto conte�do do post enviado
	 */
	private String executaEnviaFotoTwitter(String filename, String text) {

		String result=null;
	
		try {

			result = new MyAsyncTask().execute(filename, text).get();

			Log.d(TAG, "Resultado do processamento em background - result=["+ result+"]");

		} catch (InterruptedException e) {

			Log.w(TAG, "InterruptedException: ", e);

		} catch (ExecutionException e) {

			Log.w(TAG, "ExecutionException:", e);
		}

		Log.d(TAG, "fim");
		
		return result;

	}

	/**
	 * Tarefa assincrona respons�vel pelo envio da foto.
	 * 
	 * InParamType ProgressReportType ResultType
	 * 
	 */
	private class MyAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			Log.d(TAG, "preExecute");

		}

		@Override
		protected String doInBackground(String... params) {

			String result = null;

			int myProgress = 0;

			Log.d(TAG, "doInBackground()");

			Log.d(TAG, "doInBackground() - filename=params[0]: [" + params[0]+"]");
			Log.d(TAG, "doInBackground() - text=params[1]: [" + params[1]+"]");

			ConfigurationBuilder cb = new ConfigurationBuilder();

			// TODO esse c�digo hardcoded dever� ser removido
			cb.setDebugEnabled(true)
					.setOAuthConsumerKey("zFGaPH929CR11pKq8glzw")
					.setOAuthConsumerSecret(
							"urlbyKfdBARE4e02RxbDizWrvFUIFA8ozXZNQFTVNs")
					.setOAuthAccessToken(
							"462351999-QZ7gDmLGgcCenriVLHHKP2w6jTkz7KJNRM2PNm4e")
					.setOAuthAccessTokenSecret(
							"eGO5rmTywzynxJw7YujIRlEOjEnO0wJ08DoIpHMqsI");

			Configuration conf = cb.build();
			
			Log.d(TAG,"doInBackground() - conf.isDalvik(): "+conf.isDalvik());
			Log.d(TAG,"doInBackground() - conf.getUser(): "+conf.getUser());

			ImageUpload upload = new ImageUploadFactory(conf)
					.getInstance(MediaProvider.TWITTER);

			String url = null;

			myProgress++;
			publishProgress(myProgress);

			try {

				url = upload.upload(new File(filename), text);
				
				Log.d(TAG, "Upload da imagem feita com sucesso no Twitter em: " + url);
				
				result = url.toString();

			} catch (TwitterException e) {

				Log.w(TAG, "Falha no upload da imagem: " + e.getMessage(), e);

			}

			// retorna o valor que ser� passado ao PostExecute
			return result;

		}

		@Override
		protected void onProgressUpdate(Void... values) {

			super.onProgressUpdate(values);

			// [update progress bar, notifications, or other UI elements ...]

		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			Log.d(TAG, "onPostExecute() - result=" + result);

		}

		/**
		 * Atualiza o progresso da tarefa
		 * 
		 * @param i
		 * 
		 */
		void publishProgress(int i) {
			Log.d(TAG, "publishProgress() - Progresso: " + i);
		}

	}

}