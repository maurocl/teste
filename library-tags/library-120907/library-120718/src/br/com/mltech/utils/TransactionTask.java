package br.com.mltech.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Executa uma tarefa de transacao
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
	 * Construtor
	 * 
	 * @param context Contexto da aplica��o
	 * @param transacao Uma transacao
	 * @param aguardeMsg
	 *  
	 */
	public TransactionTask(Context context, Transaction transacao, int aguardeMsg) {
		super();
		this.context = context;
		this.transacao = transacao;
		this.aguardeMsg = aguardeMsg;
	}

	/**
	 * Abre o di�logo de progresso
	 */
	@Override
	protected void onPreExecute() {

		super.onPreExecute();
		// inicia a janela de aguarde ...
		openProgress();
	}

	/**
	 * executa transa��o em background.
	 */
	@Override
	protected Boolean doInBackground(Void... params) {

		try {
		 
			// executa a transacao
			transacao.execute();
			 
		} catch (Throwable e) {

			Log.e(TAG, e.getMessage(), e);
			
			// salva o erro e retorna false
			this.exceptionErro = e;
			
			return false;
			
		} finally {
			try {
				
				// fecha a caixa de di�logo
				closeProgress();

			} catch (Exception e) {

				Log.e(TAG, e.getMessage(), e);
			}
		}

		// ok
		return true;

	}

	/**
	 * Atualiza o componente visual da transa��o.
	 * 
	 * @param result Resultado da transa��o
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
	 * Abre a janela de di�logo de progresso
	 */
	public void openProgress() {
	  
		try {
			progresso = ProgressDialog.show(context, "", "Aguarde ...");
		} catch (Throwable e) {
			Log.e(TAG, e.getMessage(), e);
		}
		
	}

	/**
	 * Fecha a janela de di�logo de progresso
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
