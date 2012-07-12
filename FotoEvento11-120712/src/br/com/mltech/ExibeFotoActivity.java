package br.com.mltech;

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

/**
 * 
 * O objetivo dessa activity é a exibição de uma foto.
 * 
 * @author maurocl
 * 
 * 
 */
public class ExibeFotoActivity extends Activity implements OnClickListener {

  public static final String TAG = "ExibeFotoActivity";

  // 0 - desligado; 1 - ligado
  public static int DEBUG = 0;

  public static int IMAGEM_DUMMY = 0;

  // botão confirma
  private static Button btnConfirmar;

  // botão cancela
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
     * Trata o evento de disparar o botão
     */
    btnConfirmar = (Button) findViewById(R.id.btnConfirmar);
    btnConfirmar.setOnClickListener(this);

    btnCancelar = (Button) findViewById(R.id.btnCancelar);
    btnCancelar.setOnClickListener(this);

    ImageView imageView = (ImageView) findViewById(R.id.imageView1);

    // Obtém a intent que iniciou a activity
    Intent intent = getIntent();

    // Retrieve data this intent is operating on. This URI specifies the name of
    // the data; often it uses the content: scheme, specifying data in a content
    // provider. Other schemes may be handled by specific activities, such as
    // http: by the web browser.

    // Obtem o dado que a intent está operando
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
        Log.d(TAG, "data é nula");
      }

    }
    else {

      Bitmap bm = getBitmapTest();
      imageView.setImageBitmap(bm);

    }

  }

  /**
   * onClick(View view)
   */
  public void onClick(View view) {

    Intent i = new Intent();

    if (view == btnConfirmar) {
      Log.d(TAG, "botão confirmar");

      setResult(RESULT_OK, i);

    } else if (view == btnCancelar) {
      Log.d(TAG, "botão cancelar");

      setResult(RESULT_CANCELED, i);

    }

    // finaliza a activity
    finish();

  }

  /**
   * getBitmapTest()
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
   * Esse callback é chamado a partir da sequencia: onCreate() --> onStart() -->
   * on Resume() ou após ( a aplicação estar no estado Pause e retorna a
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
   * É executado após um onStop()
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
