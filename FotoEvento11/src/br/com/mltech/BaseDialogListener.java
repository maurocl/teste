package br.com.mltech;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;



/**
 * Skeleton base class for RequestListeners, providing default error handling.
 * Applications should handle these error conditions.
 * 
 * Esqueletos de classes b�sicas para RequestListeners, provendo tratamento de erros default.
 * Aplica��es devem tratar essas condi��es de erro
 */
public abstract class BaseDialogListener implements DialogListener {

	/**
	 * Erro no envio do Facebook
	 */
    public void onFacebookError(FacebookError e) {
        e.printStackTrace();
    }

    /**
     * 
     */
    public void onError(DialogError e) {
        e.printStackTrace();
    }

    /**
     * 
     */
    public void onCancel() {
    }

}