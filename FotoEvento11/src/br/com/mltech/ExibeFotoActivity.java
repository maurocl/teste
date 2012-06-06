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
 * ExibeFotoActivity
 * 
 * O objetivo dessa activity é a exibição de uma foto.
 * 
 * @author maurocl
 * 
 * 
 */
public class ExibeFotoActivity extends Activity implements OnClickListener {

  public static final String TAG = "ExibeFotoActivity";

  Button confirmar;

  Button cancelar;

  /**
   * onCreate(Bundle savedInstanceState)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    Log.d(TAG, "*** onCreate() ***");

    setContentView(R.layout.exibefoto);

    Intent intent = getIntent();

    // Retrieve data this intent is operating on. This URI specifies the name of
    // the data; often it uses the content: scheme, specifying data in a content
    // provider. Other schemes may be handled by specific activities, such as
    // http: by the web browser.
    
    Uri data = intent.getData();
    
    if(data!=null) {
      Log.d(TAG,"getScheme()="+data.getScheme());
    }

    Bitmap bm = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
    Canvas c = new Canvas(bm);

    Paint paint = new Paint();
    paint.setColor(Color.BLUE);

    c.drawLine(10, 10, 80, 80, paint);

    ImageView imageView = (ImageView) findViewById(R.id.imageView1);

    if (bm != null) {
      // imageView.setImageURI(data);
      imageView.setImageBitmap(bm);
    }
    else {
      Log.d(TAG, "Imagem é nula");
    }

    /**
     * Trata o evento de disparar o botão
     */
    Button confirmar = (Button) findViewById(R.id.btnConfirmar);
    confirmar.setOnClickListener(this);

    Button cancelar = (Button) findViewById(R.id.btnCancelar);
    cancelar.setOnClickListener(this);

  }

  /**
   * 
   */
  public void onClick(View view) {

    if (view == confirmar) {
      Log.d(TAG, "botão confirmar");

    } else if (view == cancelar) {
      Log.d(TAG, "botão cancelar");

      Intent i = new Intent();

      setResult(RESULT_CANCELED, i);

      // finaliza a activity
      finish();

    }

  }

  /**
   * onStart(2)
   */
  @Override
  protected void onStart() {

    super.onStart();

    Log.d(TAG, "*** onStart() ***");

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

    Log.d(TAG, "*** onResume() ***");

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

    Log.d(TAG, "*** onPause() ***");

  }

  /**
   * onStop()
   */
  @Override
  protected void onStop() {

    super.onStop();
    Log.d(TAG, "*** onStop() ***");
  }

  /**
   * onRestart()
   * 
   * É executado após um onStop()
   */
  @Override
  protected void onRestart() {

    super.onRestart();
    Log.d(TAG, "*** onRestart() ***");
  }

  /**
   * onDestroy();
   */
  @Override
  protected void onDestroy() {

    super.onDestroy();

    Log.d(TAG, "*** onDestroy() ***");

  }

}
