package br.com.mltech;

import java.io.File;

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

      FileUtils.showBundle(i.getExtras());

      if (i.getExtras().containsKey("br.com.mltech.outputFileUri")) {

        uri = (Uri) i.getParcelableExtra("br.com.mltech.outputFileUri");

        Log.i(TAG, "onCreate() - br.com.mltech.outputFileUri: " + uri);

      }

    }

    // TODO gambiarra !!!
     // meuArquivo é um bitmap existente do sistema de arquivos e que será usado
    // para "foto" de retorno (exemplo)
    //String meuArquivo = "/mnt/sdcard/Pictures/fotoevento/fotos/20120528_155116.jpg";
    String meuArquivo = "/mnt/sdcard/Pictures/fotoevento/fotos/casa_320x240.png";

    if (FileUtils.isValidFile(new File(meuArquivo))) {
      Log.d(TAG, "onCreate() - meu arquivo " + meuArquivo + " existe");
    }

    boolean b;

    // Lê o arquivo e armazena o bitmap
    Bitmap bitmap = BitmapFactory.decodeFile(meuArquivo);

    // Grava o bitmap no "novo arquivo" dado pela Uri
    b = ManipulaImagem.gravaBitmapArquivo(bitmap, uri.getPath());

    if (b) {
      Log.d(TAG, "onCreate() - Imagem gravada com sucesso em: " + uri.getPath());
    }

    // Cria um intent de resposta
    Intent resp = new Intent();

    // preenche os parâmetros de retorno
    // resp.putExtra("file", "/mnt/sdcard/1338317354741.jpg");

    resp.putExtra("outputFileUri", uri.getPath());

    resp.putExtra("extra1", "1234");

    // estabelece o resultado da execução da activity
    setResult(RESULT_OK, resp);

    // finaliza a activity
    finish();

  }

}
