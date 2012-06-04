package br.com.mltech;

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
    Intent intent = getIntent();

    Uri data = intent.getData();

    Log.d(TAG, "onCreate() - data: " + data);
    Log.d(TAG, "onCreate() - getExtras: " + intent.getExtras());

    if (uri == null) {
      Log.w(TAG, "onCreate() - URI fornecida para gravação da foto é nula");
      
    }

    if (intent.getExtras() != null) {

      // foram passados parâmetros para Activity

      Log.d(TAG, "onCreate() - Parâmetros:");

      // exibe a lista de parâmetros recebidos
      FileUtils.showBundle(intent.getExtras());

      if (intent.getExtras().containsKey("br.com.mltech.outputFileUri")) {

        uri = (Uri) intent.getParcelableExtra("br.com.mltech.outputFileUri");

        Log.i(TAG, "onCreate() - br.com.mltech.outputFileUri: " + uri);

      }

    }

    // TODO gambiarra !!!
    // meuArquivo é um bitmap existente do sistema de arquivos e que será usado
    // para "foto" de retorno (exemplo)

    // Cria um intent de resposta
    Intent respostaIntent = new Intent();

    String meuArquivo = "/mnt/sdcard/Pictures/fotoevento/fotos/casa_320x240.png";

    if (validaArquivo(meuArquivo, uri)) {

      //respostaIntent.putExtra("outputFileUri", uri.getPath());
      
      respostaIntent.putExtra("outputFileUri", uri);
      
      // estabelece o resultado da execução da activity
      setResult(RESULT_OK, respostaIntent);

    }
    else {

      respostaIntent.putExtra("outputFileUri", (String) null);

      // estabelece o resultado da execução da activity
      setResult(RESULT_CANCELED, respostaIntent);

    }

    // finaliza a activity
    finish();

  }

  /**
   * validaArquivo(String meuArquivo, Uri uri)
   * 
   * @param meuArquivo
   * @param uri
   * 
   * @return
   */
  private boolean validaArquivo(String meuArquivo, Uri uri) {

    if (meuArquivo == null) {
      Log.w(TAG, "validaArquivo() - arquivo é nulo");
      return false;
    }
    else {
      Log.d(TAG, "validaArquivo() - arquivo " + meuArquivo + " existe");
    }

    if (uri == null) {
      Log.w(TAG, "validaArquivo() - uri é nula");
      return false;
    }
    else {
      Log.d(TAG, "validaArquivo() - URI " + uri);
    }

    // Lê o arquivo e armazena o bitmap
    Bitmap bitmap = BitmapFactory.decodeFile(meuArquivo);

    if (bitmap == null) {
      Log.d(TAG, "validaArquivo() - bitmap não pode ser decodificado a partir do arquivo " + meuArquivo);
      return false;
    }
    else {
      Log.d(TAG, "validaArquivo() - bitmap decodificado com sucesso");
    }

    boolean gravou = ManipulaImagem.gravaBitmapArquivo(bitmap, uri.getPath());

    if (gravou) {
      // Grava o bitmap no "novo arquivo" dado pela Uri
      Log.d(TAG, "validaArquivo() - Imagem gravada com sucesso em: " + uri.getPath());
      return true;
    } else {
      Log.e(TAG, "validaArquivo() - Falha na gravação da imagem em " + uri.getPath());
      return false;
    }

  }

  /**
   * xxx(int resultado, Bundle bundle)
   * 
   * @param resultado
   * @param bundle
   * @return
   */
  Intent xxx(int resultado, Bundle bundle) {

    // Cria um intent de resposta
    Intent respostaIntent = new Intent();

    if (bundle != null) {
      respostaIntent.putExtras(bundle);
    }

    // estabelece o resultado da execução da activity
    setResult(resultado, respostaIntent);

    return respostaIntent;

  }

}
