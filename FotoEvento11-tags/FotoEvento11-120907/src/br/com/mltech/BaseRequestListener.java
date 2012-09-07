package br.com.mltech;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.util.Log;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;

/**
 * Skeleton base class for RequestListeners, providing default error handling.
 * Applications should handle these error conditions.
 * 
 * Esqueleto de classe b�sica para RequestListener, provendo tratamento de erros
 * padr�o. Aplica��es devem tratar essas condi��es de erro.
 * 
 */
public abstract class BaseRequestListener implements RequestListener {

	/**
	 * Erro
	 */
	public void onFacebookError(FacebookError e, final Object state) {
		Log.e("Facebook", e.getMessage());
		e.printStackTrace();
	}

	/**
	 * Arquivo n�o foi encontrado
	 */
	public void onFileNotFoundException(FileNotFoundException e, final Object state) {
		Log.e("Facebook", e.getMessage());
		e.printStackTrace();
	}

	/**
	 * Erro de IO
	 */
	public void onIOException(IOException e, final Object state) {
		Log.e("Facebook", e.getMessage());
		e.printStackTrace();
	}

	/**
	 * Erro de URL mal formada
	 */
	public void onMalformedURLException(MalformedURLException e, final Object state) {
		Log.e("Facebook", e.getMessage());
		e.printStackTrace();
	}

}
