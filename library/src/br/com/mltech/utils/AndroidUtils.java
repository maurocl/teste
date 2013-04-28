package br.com.mltech.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AndroidUtils {

	/**
	 * Verifica se há algum tipo de conexão externa disponível.
	 * 
	 * @param context
	 *          Contexto da aplicação
	 * 
	 * @return true se houver algum tipo de conexão de rede ou false caso
	 *         contrário.
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
	 * Exibe uma caixa de diálogo de Alerta
	 * 
	 * @param context
	 *          Contexto da aplicação
	 * @param message
	 *          Mensagem que será exibida
	 * 
	 */
	public static void alertDialog(Context context, int message) {

		AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Título").setMessage(message).create();
		dialog.setButton("OK", new OnClickListener() {

			// @Override
			public void onClick(DialogInterface dialog, int which) {
				// simplesmente retorna sem executar nenhuma ação.
				return;
			}
		});

		// exibe a caixa de diálogo
		dialog.show();

	}

	/**
   * Exibe uma caixa de diálogo de Alerta
	 * 
	 * @param context
	 *          Contexto da Aplicação
	 * @param title
	 *          Título da janela
	 * @param message
	 *          Mensagem que será exibida
	 * 
	 */
	public static void alertDialog(Context context, String title, String message) {

		AlertDialog dialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message).create();
		
		dialog.setButton("OK", new OnClickListener() {

			// @Override
			public void onClick(DialogInterface dialog, int which) {
				// simplesmente retorna sem executar nenhuma ação.
				return;
			}
		});

		// exibe a caixa de diálogo
		dialog.show();

	}

}
