package br.com.mltech.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AndroidUtils {

	/**
	 * Verifica se h� algum tipo de conex�o externa dispon�vel.
	 * 
	 * @param context
	 *          Contexto da aplica��o
	 * 
	 * @return true se houver algum tipo de conex�o de rede ou false caso
	 *         contr�rio.
	 * 
	 */
	public static boolean isNetworkingAvailable(Context context) {

		boolean conectividade = false;

		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager != null) {

			NetworkInfo[] info = connectivityManager.getAllNetworkInfo();

			if (info != null) {

				for (int i = 0; i < info.length; i++) {

					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						
						conectividade = true;

					}

				}

			}

		}

		return conectividade;

	}

	/**
	 * Exibe uma caixa de di�logo de Alerta
	 * 
	 * @param context
	 *          Contexto da aplica��o
	 * @param message
	 *          Mensagem que ser� exibida
	 * 
	 */
	public static void alertDialog(Context context, int message) {

		AlertDialog dialog = new AlertDialog.Builder(context).setTitle("T�tulo").setMessage(message).create();
		dialog.setButton("OK", new OnClickListener() {

			// @Override
			public void onClick(DialogInterface dialog, int which) {
				// simplesmente retorna sem executar nenhuma a��o.
				return;
			}
		});

		// exibe a caixa de di�logo
		dialog.show();

	}

	/**
   * Exibe uma caixa de di�logo de Alerta
	 * 
	 * @param context
	 *          Contexto da Aplica��o
	 * @param title
	 *          T�tulo da janela
	 * @param message
	 *          Mensagem que ser� exibida
	 * 
	 */
	public static void alertDialog(Context context, String title, String message) {

		AlertDialog dialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message).create();
		
		dialog.setButton("OK", new OnClickListener() {

			// @Override
			public void onClick(DialogInterface dialog, int which) {
				// simplesmente retorna sem executar nenhuma a��o.
				return;
			}
		});

		// exibe a caixa de di�logo
		dialog.show();

	}

}
