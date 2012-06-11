package br.com.mltech;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class CriacaoArquivo extends Activity {

  private static final String TAG = "CriacaoArquivo";

  public static final String PATH_FOTOS = "/mnt/sdcard/Pictures/fotoevento/fotos/";

  protected void onCreate(android.os.Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.tela);

    teste();

  }

  private void pqp() {
    String mFilename = null;

    Uri xUri = null;

    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

    File f = null;

    String prefix = PATH_FOTOS + "pqp_" + timeStamp;

    prefix = "/mnt/sdcard/Pictures/test";

    try {
      f = File.createTempFile(prefix, null);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    if (f == null) {
      Log.w(TAG, "merda");
      return;
    }

    Log.w(TAG, "obtemFoto() - f.getAbsolutePath()=" + f.getAbsolutePath());

    if (f.exists()) {
      Log.w(TAG, "ARQUIVO EXISTE !!!");
    } else {
      Log.w(TAG, "ARQUIVO não EXISTE !!!");
    }

    // atualiza o nome do arquivo onde a foto será armazenada
    mFilename = f.getAbsolutePath();

    if (f.canWrite()) {
      Log.d(TAG, "obtemFoto() - arquivo: " + mFilename + " pode ser gravado");
    } else {
      Log.d(TAG, "obtemFoto() - arquivo: " + mFilename + " não pode ser gravado");
    }

    // Obtem a URI do arquivo (esse valor será forncecido a Intent)
    xUri = Uri.fromFile(f); // informa a Uri onde a foto será armazenada

    if (xUri == null) {
      Log.w(TAG, "obtemFoto() - xUri=null. Arquivo para armazenamento não foi criado.");

    } else {
      Log.i(TAG, "obtemFoto() ===> xUri=" + xUri.getPath() + ", xUri=" + xUri);
    }
  };

  private void teste() {

    String arquivo = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg";
    File file = new File(arquivo);

    if (file.exists()) {
      Log.w(TAG, "ARQUIVO EXISTE !!!");
    } else {
      Log.w(TAG, "ARQUIVO não EXISTE !!!");
    }
    ;

    Uri uri = Uri.fromFile(file);

    if (file != null) {
      Log.d(TAG, "file=" + file.getAbsolutePath());
    }
    Log.d(TAG, "uri=" + uri);

  }

}
