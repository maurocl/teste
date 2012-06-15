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
 * Essa activity "simula" o funcionamento de uma c�mera, isto �, uma vez chamada
 * ela dever� retornar na Uri fornecida um bitmap.
 * 
 * @author maurocl
 * 
 * 
 * 
 */
public class ActivityCameraSimplesDummy extends Activity {

  // TODO falta implementar um m�todo que leia a imagem a partir dos recursos da aplica��o.
  
  
  public static final String TAG = "ActivityCameraSimplesDummy";

  // TODO remover o c�digo hardcoded
  public static String ARQUIVO = "/mnt/sdcard/Pictures/fotoevento/fotos/casa_320x240.png";

  /**
   * onCreate(Bundle savedInstanceState)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    this.setContentView(R.layout.preferencias);

    Log.d(TAG, "*** onCreate() ***");

    Uri uri = null;

    // Obtem a intent usada para chamar essa Activity.
    Intent intent = getIntent();

    Uri data = intent.getData();

    Log.d(TAG, "onCreate() - data: " + data);
    Log.d(TAG, "onCreate() - getExtras: " + intent.getExtras());

    // exibe informa��es sobre a intent chamadora
    showIntent(intent);

    if (intent.getExtras() != null) {

      // foram passados par�metros para Activity

      Log.d(TAG, "onCreate() - Par�metros:");

      // exibe a lista de par�metros recebidos
      FileUtils.showBundle(intent.getExtras());

      if (intent.getExtras().containsKey("br.com.mltech.outputFileUri")) {

        uri = (Uri) intent.getParcelableExtra("br.com.mltech.outputFileUri");

        Log.i(TAG, "onCreate() - br.com.mltech.outputFileUri: " + uri);

      }

    }

    // TODO gambiarra !!!
    // meuArquivo � um bitmap existente do sistema de arquivos e que ser� usado
    // para "foto" de retorno (exemplo)

    // Cria um intent de resposta
    Intent respostaIntent = new Intent();

    // define o nome do arquivo padr�o que ser� retornando quando uma foto for
    // solicitada
    String meuArquivo = ARQUIVO;

    if (validaArquivo(meuArquivo, uri)) {

      respostaIntent.putExtra("outputFileUri", uri);

      respostaIntent.setData(uri);

      // estabelece o resultado da execu��o da activity
      setResult(RESULT_OK, respostaIntent);

    }
    else {

      respostaIntent.putExtra("outputFileUri", (Uri) null);
      respostaIntent.setData(null);

      // estabelece o resultado da execu��o da activity
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
   * Verifica se a URI fornecida � diferente de null
   * 
   * Grava o bitmap 
   * 
   * @param meuArquivo
   * @param uri
   *          URI onde o arquivo ser� gravado
   * 
   * @return
   */
  private boolean validaArquivo(String meuArquivo, Uri uri) {

    boolean ret = false;

    if (meuArquivo == null) {
      Log.w(TAG, "validaArquivo() - nome do arquivo � nulo");
      ret = false;
    }
    else {
      Log.v(TAG, "validaArquivo() - arquivo " + meuArquivo + " existe");
      ret = true;
    }

    if (uri == null) {
      Log.w(TAG, "validaArquivo() - uri � nula");
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
   * <p>
   * Esse m�todo � usado para ler um arquivo contendo um bitmap armazenado no sistema de arquivos
   * e grav�-lo no endere�o dado pela URI fornecida.
   * 
   * � usado para gerar uma bitmap padr�o que "substitui" o bitmap padr�o gerado pela c�mera fotogr�fica.
   * 
   * <p>
   * L� o arquivo contendo o bitmap
   * <p>
   * Decodifica o arquivo lido e gera um bitmap (se poss�vel)
   * <p>
   * Grava o bitmap na URI
   * 
   * @param meuArquivo
   *          nome do arquivo que cont�m o bitmap
   * 
   * @param uri
   *          URI onde o bitmap deve ser disponibilizado
   * 
   * @return true em caso de sucesso ou false caso contr�rio.
   */
  private boolean gravaBitmapUri(String meuArquivo, Uri uri) {

    //
    // cria um bitmap a partir do arquivo
    //
    // Decode a file path into a bitmap. 
    // If the specified file name is null, or cannot be decoded into a bitmap, 
    // the function returns null.
    //
    Bitmap bitmap = BitmapFactory.decodeFile(meuArquivo);

    if (bitmap == null) {
      // bitmap n�o foi gerado
      // as poss�veis causas s�o: nome do arquivo � nulo ou n�o pode ser decodificado em um bitmap
      Log.d(TAG, "gravaBitmapUri() - bitmap n�o pode ser decodificado a partir do arquivo " + meuArquivo);
      return false;
    }
    else {
      // bitmap decodificado com sucesso
      Log.v(TAG, "gravaBitmapUri() - bitmap decodificado com sucesso");
    }

    // grava o bitmap usando a URI recebida
    boolean gravou = ManipulaImagem.gravaBitmapArquivo(bitmap, uri.getPath());

    if (gravou) {
      // Grava o bitmap no "novo arquivo" dado pela Uri
      Log.v(TAG, "gravaBitmapUri() - Imagem gravada com sucesso em: " + uri.getPath());
      return true;
    } else {
      Log.e(TAG, "gravaBitmapUri() - Falha na grava��o da imagem em " + uri.getPath());
      return false;
    }

  }

  /**
   * showIntent(Intent intent)
   * 
   * Exibe informa��es sobre uma Intent (se for diferente de null)
   * 
   * @param intent
   *          Inst�ncia de uma intent
   * 
   */
  private void showIntent(Intent intent) {

    if (intent == null) {
      Log.w(TAG, "showIntent() - intent est� nula.");
      return;
    }

    Log.d(TAG, "showIntent() - data(): " + intent.getData());
    Log.d(TAG, "showIntent() - dataString(): " + intent.getDataString());

    Log.v(TAG, "showIntent() - getAction(): " + intent.getAction());
    Log.v(TAG, "showIntent() - getType(): " + intent.getType());
    Log.v(TAG, "showIntent() - getPackage(): " + intent.getPackage());
    Log.v(TAG, "showIntent() - getScheme: " + intent.getScheme());
    Log.v(TAG, "showIntent() - getExtras(): " + intent.getExtras());

  }

}
