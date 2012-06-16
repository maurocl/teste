package br.com.mltech;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

public class AplicacaoActivity extends Activity {

  private static final String TAG = "AplicacaoActivity";

  private static int numCreate = 0;

  private static int numRestarts = 0;

  private int valor1 = 0;

  private int valor2 = 5;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Log.d(TAG, "*** onCreate() ***");

    numCreate++;

    show();

  }

  @Override
  protected void onStart() {

    // TODO Auto-generated method stub
    super.onStart();
    Log.d(TAG, "*** onStart() ***");
    valor1++;
    valor2++;
  }

  @Override
  protected void onResume() {

    // TODO Auto-generated method stub
    super.onResume();
    Log.d(TAG, "*** onResume() ***");
    valor1++;
    valor2++;
  }

  @Override
  protected void onPause() {

    // TODO Auto-generated method stub
    super.onPause();
    Log.d(TAG, "*** onPause() ***");
    valor1++;
    valor2++;
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {

    // TODO Auto-generated method stub
    super.onRestoreInstanceState(savedInstanceState);
    Log.i(TAG, "*** onRestoreInstanceState() ***");

    if (savedInstanceState != null) {
      valor2 = savedInstanceState.getInt("valor2");
    }

    valor1++;
    valor2++;
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {

    // TODO Auto-generated method stub
    super.onSaveInstanceState(outState);

    outState.putInt("valor2", valor2);

    Log.i(TAG, "*** onSaveInstanceState() ***");
    valor1++;
    valor2++;
  }

  @Override
  protected void onRestart() {

    // TODO Auto-generated method stub
    super.onRestart();
    Log.d(TAG, "*** onRestart() ***");
    numRestarts++;
    valor1++;
    valor2++;
    show();

  }

  @Override
  protected void onStop() {

    // TODO Auto-generated method stub
    super.onStop();
    Log.d(TAG, "*** onStop() ***");
    valor1++;
    valor2++;
  }

  @Override
  protected void onDestroy() {

    // TODO Auto-generated method stub
    super.onDestroy();
    Log.w(TAG, "*** onDestroy() ***");
    valor1++;
    valor2++;
    show();
  }

  void show() {

    Log.v(TAG, "\n" + "numCreate: " + numCreate + ", numRestarts: " + numRestarts + ", valor1: " + valor1 + ", valor2: " + valor2
        + "\n");
    /*
     * Log.v(TAG,"numRestarts: "+numRestarts); Log.v(TAG,"valor1: "+valor1);
     * Log.v(TAG,"valor2: "+valor2);
     */
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {

    // TODO Auto-generated method stub
    super.onConfigurationChanged(newConfig);
    Log.d(TAG, "*** onConfigurationChanged() ***");

    Log.d(TAG, "newConfig=" + newConfig);

    Log.d(TAG, "newConfig.orientation=" + newConfig.orientation);

    Log.d(TAG, "fontScale: " + newConfig.fontScale);

    Log.d(TAG, "hardKeyboardHidden: " + newConfig.hardKeyboardHidden);

    Log.d(TAG, "keyboard:" + newConfig.keyboard);

    Log.d(TAG, "keyboardHidden:" + newConfig.keyboardHidden);

    Log.d(TAG, "mcc: " + newConfig.mcc); // Mobile Country Code

    Log.d(TAG, "mnc: " + newConfig.mnc); // Mobile Network Code 

    Log.d(TAG, "navigation: " + newConfig.navigation);

    Log.d(TAG, "navigationHidden: " + newConfig.navigationHidden);

    Log.d(TAG, "orientation: " + newConfig.orientation);

    Log.d(TAG, "screenLayout: " + newConfig.screenLayout);

    Log.d(TAG, "touchscreen: " + newConfig.touchscreen);

  }

}