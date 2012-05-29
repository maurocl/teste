package br.com.mltech;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Activity2
 * 
 * @author maurocl
 * 
 */
public class ActivityCameraSimplesDummy extends Activity {

  public static final String TAG = "Activity2";

  /**
   * 
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    this.setContentView(R.layout.preferencias);

    Log.d(TAG, "*** onCreate() ***");

    Intent i = getIntent();

    if (i.getExtras() != null) {

      Log.d(TAG, "Há bundle");

      Set<String> chaves = i.getExtras().keySet();

      for (String chave : chaves) {
        Log.i(TAG, "chave=" + chave);
      }

    }

    Intent resp = new Intent();

    resp.putExtra("file", "/mnt/sdcard/1338317354741.jpg");
    resp.putExtra("extra1", "1234");

    setResult(RESULT_OK, resp);

    // finaliza a activity
    finish();

  }

}
