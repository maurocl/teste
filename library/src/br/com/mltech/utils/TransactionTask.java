package br.com.mltech.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * 
 * @author maurocl
 * 
 */
public class TransactionTask extends AsyncTask<Void, Void, Boolean> {

	private static final String TAG = "TransactionTask";

	private final Context context;
	private final Transaction transacao;
	private ProgressDialog progresso;
	private Throwable exceptionErro;
	private int aguardeMsg;

	/**
	 * TransactionTask(Context context, Transaction transacao, int aguardeMsg)
	 * 
	 * Construtor
	 * 
	 * @param context Contexto da aplicação
	 * @param transacao Uma transacao
	 * @param aguardeMsg 
	 */
	public TransactionTask(Context context, Transaction transacao, int aguardeMsg) {
		super();
		this.context = context;
		this.transacao = transacao;
		this.aguardeMsg = aguardeMsg;
	}

	/**
	 * Abre o diálogo de progresso
	 */
	@Override
	protected void onPreExecute() {

		super.onPreExecute();
		// inicia a janela de aguarde ...
		openProgress();
	}

	/**
	 * executa transação em background.
	 */
	@Override
	protected Boolean doInBackground(Void... params) {

		try {
		  
			transacao.execute();
			 
		} catch (Throwable e) {

			Log.e(TAG, e.getMessage(), e);
			
			// salva o erro e retorna false
			this.exceptionErro = e;
			
			return false;
			
		} finally {
			try {
				closeProgress();

			} catch (Exception e) {

				Log.e(TAG, e.getMessage(), e);
			}
		}

		// ok
		return true;

	}

	/**
	 * atualiza o componente visual da transação.
	 * 
	 * @result 
	 * 
	 */
	@Override
	protected void onPostExecute(Boolean result) {
	  
		if (result) {
			transacao.updateView();

		} else {
			//
			AndroidUtils.alertDialog(context, "TransactionTask", "Erro: " + exceptionErro.getMessage());
		}
		
	}

	/**
	 * Abre a janela de diálogo de progresso
	 */
	public void openProgress() {
	  
		try {
			progresso = ProgressDialog.show(context, "", "Aguarde ...");
		} catch (Throwable e) {
			Log.e(TAG, e.getMessage(), e);
		}
		
	}

	/**
	 * Fecha a janela de diálogo de progresso
	 */
	public void closeProgress() {
	  
		try {
		  
			if (progresso != null) {
				progresso.dismiss();
			}
			
		} catch (Throwable e) {

			Log.e(TAG, e.getMessage(), e);
			
		}
		
	}

}
