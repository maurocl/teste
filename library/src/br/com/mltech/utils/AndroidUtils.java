package br.com.mltech.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AndroidUtils {

  /**
   * isNetworkingAvailable(Context context)
   * 
   * @param context
   * 
   * @return
   */
  public static boolean isNetworkingAvailable(Context context) {

    boolean conectividade = false;

    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    if (connectivityManager == null) {

      //

    } else {
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
   * alertDialog(Context context, int message)
   * 
   * @param context
   * @param message
   * 
   */
  public static void alertDialog(Context context, int message) {

    AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Título").setMessage(message).create();
    dialog.setButton("OK", new OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub
        return;
      }
    });

    dialog.show();

  }

}
