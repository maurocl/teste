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

    // exibe informações sobre a intent chamadora
    showIntent(intent);

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

    // define o nome do arquivo padrão que será retornando quando uma foto for
    // solicitada
    String meuArquivo = "/mnt/sdcard/Pictures/fotoevento/fotos/casa_320x240.png";

    if (validaArquivo(meuArquivo, uri)) {

      // respostaIntent.putExtra("outputFileUri", uri.getPath());

      respostaIntent.putExtra("outputFileUri", uri);

      // estabelece o resultado da execução da activity
      setResult(RESULT_OK, respostaIntent);

    }
    else {

      respostaIntent.putExtra("outputFileUri", (Uri) null);

      // estabelece o resultado da execução da activity
      setResult(RESULT_CANCELED, respostaIntent);

    }

    // finaliza a activity
    finish();

  }

  /**
   * validaArquivo(String meuArquivo, Uri uri)
   * 
   * Verifica se o arquivo enviado existe
   * 
   * Verifica se a URI fornecida é diferente de null
   * 
   * @param meuArquivo
   * @param uri
   * 
   * @return
   */
  private boolean validaArquivo(String meuArquivo, Uri uri) {

    boolean ret = false;

    if (meuArquivo == null) {
      Log.w(TAG, "validaArquivo() - arquivo é nulo");
      ret = false;
    }
    else {
      Log.v(TAG, "validaArquivo() - arquivo " + meuArquivo + " existe");
      ret = true;
    }

    if (uri == null) {
      Log.w(TAG, "validaArquivo() - uri é nula");
      ret = false;
    }
    else {
      Log.v(TAG, "validaArquivo() - URI " + uri);
      ret = true;
    }

    if (ret) {
      ret = gravaBitmapUri(meuArquivo, uri);
    }

    return ret;

  }

  /**
   * gravaBitmapUri(String meuArquivo, Uri uri)
   * 
   * @param meuArquivo
   * @param uri
   * 
   * @return
   */
  private boolean gravaBitmapUri(String meuArquivo, Uri uri) {

    //
    // cria um bitmap a partir do arquivo
    //
    Bitmap bitmap = BitmapFactory.decodeFile(meuArquivo);

    if (bitmap == null) {
      // bitmap não foi gerado
      Log.d(TAG, "validaArquivo() - bitmap não pode ser decodificado a partir do arquivo " + meuArquivo);
      return false;
    }
    else {
      // bitmap decodificado com sucesso
      Log.v(TAG, "validaArquivo() - bitmap decodificado com sucesso");
    }

    // grava o bitmap usando a URI recebida
    boolean gravou = ManipulaImagem.gravaBitmapArquivo(bitmap, uri.getPath());

    if (gravou) {
      // Grava o bitmap no "novo arquivo" dado pela Uri
      Log.v(TAG, "validaArquivo() - Imagem gravada com sucesso em: " + uri.getPath());
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

  /**
   * showIntent(Intent intent)
   * 
   * Exibe informações sobre uma Intent
   * 
   * @param intent
   * 
   */
  private void showIntent(Intent intent) {

    Log.d(TAG, "showIntent() - data(): " + intent.getData());
    Log.d(TAG, "showIntent() - dataString(): " + intent.getDataString());

    Log.v(TAG, "showIntent() - getAction(): " + intent.getAction());
    Log.v(TAG, "showIntent() - getType(): " + intent.getType());
    Log.v(TAG, "showIntent() - getPackage(): " + intent.getPackage());
    Log.v(TAG, "showIntent() - getScheme: " + intent.getScheme());
    Log.v(TAG, "showIntent() - getExtras(): " + intent.getExtras());

  }

}
