package br.com.mltech;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import br.com.mltech.utils.FileUtils;
import br.com.mltech.utils.ManipulaImagem;

/**
 * ActivityCameraSimplesDummy
 * 
 * Essa activity devera "simular" o funcionamento de uma câmera, isto é, uma vez
 * chamada ela deverá retornar na Uri fornecida um bitmap.
 * 
 * @author maurocl
 * 
 */
public class ActivityCameraSimplesDummy extends Activity {

  public static final String TAG = "ActivityCameraSimplesDummy";

  /**
   * onCreate(Bundle savedInstanceState)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    this.setContentView(R.layout.preferencias);

    Log.d(TAG, "*** onCreate() ***");

    Uri uri = null;

    // Obtem a intent usada para chamar essa Activity
    Intent i = getIntent();

    if (i.getExtras() != null) {

      // foram passados parâmetros para Activity

      Log.d(TAG, "onCreate() - Parâmetros:");

      Set<String> chaves = i.getExtras().keySet();

      for (String chave : chaves) {
        Log.i(TAG, "  chave=" + chave);
      }

      if (i.getExtras().containsKey("br.com.mltech.arquivo")) {

        uri = (Uri) i.getParcelableExtra("br.com.mltech.arquivo");

        Log.i(TAG, "br.com.mltech.arquivo=" + uri);

      }

    }

    // TODO gambiarra !!!
    Bitmap bitmap = BitmapFactory.decodeFile("/mnt/sdcard/1338317354741.jpg");

    boolean b = ManipulaImagem.gravaBitmapArquivo(bitmap, uri.getPath());

    // Cria um intent de resposta
    Intent resp = new Intent();

    // preenche os parâmetros de retorno
    resp.putExtra("file", "/mnt/sdcard/1338317354741.jpg");
    resp.putExtra("extra1", "1234");

    // estabelece o resultado da execução da activity
    setResult(RESULT_OK, resp);

    // finaliza a activity
    finish();

  }

}
