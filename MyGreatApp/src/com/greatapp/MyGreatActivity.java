
package com.greatapp;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mygreatapp.R;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

/**
 * 
 * 
 *
 */
public class MyGreatActivity extends Activity {

  private static final String TAG = "greatapp";

  // Facebook facebook = new Facebook("YOUR_APP_ID");
  Facebook facebook = new Facebook("316626011767784");

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Log.d(TAG, "onCreate() ...");

    facebook.authorize(this, new DialogListener() {

      /**
       * autorização realizada com sucesso
       */
      public void onComplete(Bundle values) {

        Log.d(TAG, "onComplete()");

        showBundle(values);

      }

      /**
       * erro Facebook na autorização
       */
      public void onFacebookError(FacebookError error) {

        Log.d(TAG, "onFacebookError");
        showFacebookError(error);
      }

      /**
			 * 
			 */
      public void onError(DialogError e) {

        Log.d(TAG, "onDialog()");
      }

      /**
       * Autorização foi cancelada
       */
      public void onCancel() {

        Log.d(TAG, "onCancel()");
      }

    });

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    Log.d(TAG, "onActivityResult() - requestCode: " + requestCode
        + ", resultCode: " + resultCode + ", data: " + data);

    facebook.authorizeCallback(requestCode, resultCode, data);

    if (data != null) {
      Bundle values = data.getExtras();
      showBundle(values);
    }

  }

  /**
   * Exibe informações sobre o erro Facebook
   * 
   * @param error instância de um erro do facebook
   * 
   */
  private void showFacebookError(FacebookError error) {

    Log.i(TAG, "ShowFacebookError:");

    Log.w(TAG, "FacebookError: " + error);
    Log.w(TAG, "FacebookError: getErrorCode(): " + error.getErrorCode());
    Log.w(TAG, "FacebookError: getErrorType(): " + error.getErrorType());
    Log.w(TAG, "FacebookError: getMessage(): " + error.getMessage());
    Log.w(TAG, "FacebookError: getLocalizedMessage(): " + error.getLocalizedMessage());
    Log.w(TAG, "FacebookError: getCause(): " + error.getCause());
    Log.w(TAG, "FacebookError: getClass(): " + error.getClass());
  }

  /**
   * Exibe a relação de chaves e valores de um Bundle
   * 
   * @param values
   * 
   */
  private void showBundle(Bundle values) {

    // Bundle: mapping from String values to various Parcelable types. 
    Log.i(TAG, "ShowBundle:");

    if (values != null) {

      Set<String> chaves = values.keySet();

      if (chaves != null) {

        Log.d(TAG, "Nº de chaves:" + chaves.size());
        
        int i = 0;
        
        for (String chave : chaves) {

          Log.d(TAG,
              i + ": Chave: " + chave + ", valor="
                  + values.getString(chave) + ", size=" + values.getString(chave).length());
          Log.d(TAG, "");

          i++;

        }

      }

    }

  }

}
