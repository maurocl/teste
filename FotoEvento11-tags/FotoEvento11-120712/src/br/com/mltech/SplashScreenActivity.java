package br.com.mltech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * SplashScreenActivity.java
 * 
 * Splash Screen
 * 
 * @author maurocl
 * 
 */
public class SplashScreenActivity extends Activity implements Runnable {

  private static final String TAG = "SplashScreenActivity";

  private TextView text1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);

    // Exibe o layout com a imagem
    setContentView(R.layout.splashscreen);

    Log.d(TAG, "onCreate() - Running ...");

    text1 = (TextView) findViewById(R.id.text1);

    Toast.makeText(this, "Aguarde o carregamento da aplicação...", Toast.LENGTH_SHORT).show();

    // 
    text1.setText("Aguarde ...");

    Handler h = new Handler();
    h.postDelayed(this, 3000);

  }

  /**
   * 
   */
  public void run() {

    // abre o activity principal
    startActivity(new Intent(this, FotoEventoActivity.class));
    // finaliza a activity corrente
    finish();
  }

}
