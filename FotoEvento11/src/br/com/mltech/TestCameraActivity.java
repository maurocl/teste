package br.com.mltech;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/**
 * TestCameraActivity
 * 
 * @author maurocl
 * 
 */
public class TestCameraActivity extends Activity {

  private static final String TAG = "TestCameraActivity";

  private ImageView mImageView;

  private Uri mUri;

  private static int numRestarts;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    Log.d(TAG, "*** onCreate() ***");

    setContentView(R.layout.testcameraprev);

    Button btnOk = (Button) findViewById(R.id.btnOk);
    btnOk.setText("Capturar");

    Button btnCancelar = (Button) findViewById(R.id.btnCancelar);
    btnCancelar.setText("Voltar");

    mImageView = (ImageView) findViewById(R.id.imageView);

    // tratamento do botão Ok
    btnOk.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Log.d(TAG, "btnOk - botão ok");

        // cria uma intent para execução da CameraActivity
        Intent i = new Intent(getBaseContext(), CameraActivity2.class);

        // inicia a activity
        startActivityForResult(i, 100);

      }

    });

    // tratamento do botão Cancelar
    btnCancelar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Log.d(TAG, "btnCancelar - botão cancelar");
        finish();

      }

    });

  }

  /**
	 * 
	 */
  @Override
  protected void onStart() {

    super.onStart();

    Log.d(TAG, "*** onStart() ***");

  }

  /**
   * onResume(3)
   * 
   * Esse callback é chamado a partir da sequência:<br>
   * 
   * onCreate() --> onStart() --> onResume() ou<br>
   * 
   * após (a aplicação estar no estado Pause e retorna a funcionar)<br>
   * 
   * onPause() --> on Resume().
   * 
   */
  @Override
  protected void onResume() {

    super.onResume();

    Log.d(TAG, "*** onResume() ***");

  }

  /**
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
   * Aplicação foi reinicializada.<br>
   * 
   * É executado após um onStop()
   * 
   */
  @Override
  protected void onRestart() {

    super.onRestart();

    numRestarts++;

    Log.d(TAG, "*******************");
    Log.d(TAG, "*** onRestart(" + numRestarts + ") ***");
    Log.d(TAG, "*******************");

  }

  /**
   * Callback chamado quando da destruição da activity.
   */
  @Override
  protected void onDestroy() {

    super.onDestroy();

    Log.d(TAG, "*** onDestroy() ***");

  }

  /**
   * 
   */
  @Override
  protected void onSaveInstanceState(Bundle outState) {

    super.onSaveInstanceState(outState);

    Log.d(TAG, "*** onSaveInstanceState() *** "+outState);

  }

  /**
   * 
   */
  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {

    super.onRestoreInstanceState(savedInstanceState);

    Log.d(TAG, "*** onRestoreInstanceState() *** "+savedInstanceState);

  }

  /**
   * 
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    Log.i(TAG, "===> onActivityResult() - requestCode: " + requestCode + ", resultCode: " + resultCode + ", data: " + data);

    if (requestCode == 100) {

      testaResultado(resultCode, data);

    }
    else {
      Log.w(TAG, "onActivityResult() - requestCode: " + requestCode + " é desconhecido.");
    }

  }

  /**
   * Testa o resultado da execução da activity.
   * 
   * @param resultCode
   *          resultado da execução da activity
   * @param data
   *          intent com os dados recebidos
   */
  private void testaResultado(int resultCode, Intent data) {

    Log.d(TAG, "testaResultado() - resultCode: " + resultCode);

    if (resultCode == RESULT_OK) {

      Log.d(TAG, "testaResultado() - resultado: OK");

      //
      trataData(data);

      // exibe a imagem a partir de um Uri
      mImageView.setImageURI(mUri);

    } else if (resultCode == RESULT_CANCELED) {

      Log.w(TAG, "testaResultado() - resultado: CANCELED");

    }
    else {

      Log.w(TAG, "testaResultado() - resultado: " + resultCode + " é desconhecido.");

    }

  }

  /**
   * Trata a intent data
   * 
   * @param data
   * 
   */
  private void trataData(Intent data) {

    if (data == null) {
      Log.w(TAG, "trataData() - data é nulo");
      return;
    }

    mUri = data.getData();

    if (mUri == null) {
      Log.w(TAG, "trataData() - mUri é nulo");
    }
    else {
      Log.d(TAG, "trataData() - mUri: " + mUri);
    }

  }

}
