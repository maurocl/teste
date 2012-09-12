package br.com.mltech.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import br.com.mltech.R;

/** 
 * O objetivo dessa activity � a exibi��o de uma foto.
 * 
 * 
 * 
 */
public class ExibeFotoActivity extends Activity implements OnClickListener {

  public static final String TAG = "ExibeFotoActivity";

  // 0 - desligado; 1 - ligado
  public static int DEBUG = 0;

  // usado para testes da activity (para retornar uma imagem padr�o).
  public static int IMAGEM_DUMMY = 0;

  // bot�o confirma (confirma a visualiza��o da foto)
  private static Button btnConfirmar;

  // bot�o cancela
  private static Button btnCancelar;

  /**
   * onCreate(Bundle savedInstanceState)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    Log.d(TAG, "*** onCreate() ***");

    setContentView(R.layout.exibefoto);

    /**
     * Trata o evento do bot�o Confirmar
     */
    btnConfirmar = (Button) findViewById(R.id.btnConfirmar);
    btnConfirmar.setOnClickListener(this);

    /**
     * Trata o evento do bot�o Cancelar
     */
    btnCancelar = (Button) findViewById(R.id.btnCancelar);
    btnCancelar.setOnClickListener(this);

    /**
     * objeto onde a imagem ser� exibida.
     */
    ImageView imageView = (ImageView) findViewById(R.id.imageView1);

    // Obt�m a intent que iniciou a activity
    Intent intent = getIntent();

    // Retrieve data this intent is operating on. This URI specifies the name of
    // the data; often it uses the content: scheme, specifying data in a content
    // provider. Other schemes may be handled by specific activities, such as
    // http: by the web browser.

    // Obtem o dado que a intent est� operando
    // A Uri especifica o nome do dado
    Uri data = intent.getData();

    if (data != null) {
      Log.d(TAG, "data=" + data);
      Log.d(TAG, "getScheme()=" + data.getScheme());
    }

    if (IMAGEM_DUMMY == 0) {

      if (data != null) {
        imageView.setImageURI(data);
      }
      else {
        Log.d(TAG, "onCreate() - data � nula");
      }

    }
    else {

      Bitmap bm = getBitmapTest();
      imageView.setImageBitmap(bm);

    }

  }

  /**
   * onClick(View view)
   * 
   * Trata o evento de click de bot�o.
   */
  public void onClick(View view) {

  	// obt�m informa��es sobre a intent chamadora
    Intent intent = new Intent();

    if (view == btnConfirmar) {
      Log.d(TAG, "bot�o confirmar");

      setResult(RESULT_OK, intent);

    } else if (view == btnCancelar) {
      Log.d(TAG, "bot�o cancelar");

      setResult(RESULT_CANCELED, intent);

    }

    // finaliza a activity
    finish();

  }

  /**
   * Cria um bitmap de tamanho 100 x 100 pixels
   * 
   * @return um bitmap de teste
   * 
   */
  private Bitmap getBitmapTest() {

    Bitmap bm = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
    Canvas c = new Canvas(bm);

    Paint paint = new Paint();
    paint.setColor(Color.BLUE);

    c.drawLine(10, 10, 80, 80, paint);

    return bm;

  }

  /**
   * onStart(2)
   */
  @Override
  protected void onStart() {

    super.onStart();

    if (DEBUG == 1) {
      Log.d(TAG, "*** onStart() ***");
    }

  }

  /**
   * onResume(3)
   * 
   * Esse callback � chamado a partir da sequencia: onCreate() --> onStart() -->
   * on Resume() ou ap�s ( a aplica��o estar no estado Pause e retorna a
   * funcionar) onPause() --> on Resume().
   * 
   */
  @Override
  protected void onResume() {

    super.onResume();
    if (DEBUG == 1) {
      Log.d(TAG, "*** onResume() ***");
    }

  }

  /**
   * onPause(4)
   * 
   * Activity foi colocada em pausa
   * 
   */
  @Override
  protected void onPause() {

    super.onPause();
    if (DEBUG == 1) {
      Log.d(TAG, "*** onPause() ***");
    }

  }

  /**
   * onStop()
   */
  @Override
  protected void onStop() {

    super.onStop();
    if (DEBUG == 1) {
      Log.d(TAG, "*** onStop() ***");
    }
  }

  /**
   * onRestart()
   * 
   * � executado ap�s um onStop()
   */
  @Override
  protected void onRestart() {

    super.onRestart();
    if (DEBUG == 1) {
      Log.d(TAG, "*** onRestart() ***");
    }
  }

  /**
   * onDestroy();
   */
  @Override
  protected void onDestroy() {

    super.onDestroy();

    if (DEBUG == 1) {
      Log.d(TAG, "*** onDestroy() ***");
    }

  }

}
