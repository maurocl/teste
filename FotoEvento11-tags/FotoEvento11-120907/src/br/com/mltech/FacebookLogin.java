/*
 * Copyright 2010 Facebook, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package br.com.mltech;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import br.com.mltech.SessionEvents.AuthListener;
import br.com.mltech.SessionEvents.LogoutListener;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

/**
 * FacebookLogin
 * 
 * 
 */
public class FacebookLogin /* extends ImageButton */{

  private Facebook mFb;

  private Handler mHandler;

  private SessionListener mSessionListener = new SessionListener();

  private Activity mActivity;

  private int mActivityCode;

  // contexto da aplica��o
  private Context mContext;

  /**
   * Construtor
   * 
   * @param context
   *          contexto da aplica��o
   * @param permissions
   *          permiss�es aidcionais
   * 
   * 
   */
  public FacebookLogin(Context context) {

    this.mContext = context;

  }

  /**
   * M�todo de inicializa��o
   * 
   * @param activity
   *          activity
   * @param activityCode
   *          c�digo de identifica��o da activity
   * @param fb
   *          inst�ncia da classe Facebook
   */
  public void init(final Activity activity, final int activityCode, final Facebook fb) {

    //init(activity, activityCode, fb, new String[] {});

  }

  /**
   * M�todo de inicializa��o
   * 
   * @param activity
   *          activity
   * @param activityCode
   *          c�digo de identifica��o da activity
   * @param fb
   *          inst�ncia da classe Facebook
   * @param permissions
   *          array de string contendo as permiss�es necess�rias � opera��o
   * 
   */
  public void init(final Activity activity, final int activityCode, final Facebook fb, final String[] permissions) {

    mActivity = activity;
    mActivityCode = activityCode;
    mFb = fb;
    //mPermissions = permissions;

    // adiciona um listener para autoriza��es
    SessionEvents.addAuthListener(mSessionListener);

    // adiciona um listener para logout
    SessionEvents.addLogoutListener(mSessionListener);

  }

  /**
   * 
   */
  public void executaLogin(Facebook mFb) {

    // verifica se a sess�o do Facebook ainda � v�lida
    if (mFb.isSessionValid()) {

      // sess�o � v�lida

      // "inicia o processo de logout"
      SessionEvents.onLogoutBegin();

      // cria um runner para ser executado assincronamente
      AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);

      // executa o logout

      asyncRunner.logout(mContext, new LogoutRequestListener());

    } else {

      // sess�o n�o � v�lida

      // solicita autoriza��o
      //mFb.authorize(mActivity, mPermissions, mActivityCode, new LoginDialogListener());

    }

  }

  /**
   * Listener para tratar do di�logo de login
   * 
   * 
   * 
   */
  private final class LoginDialogListener implements DialogListener {

    /**
     * login executado com sucesso
     */
    public void onComplete(Bundle values) {

      SessionEvents.onLoginSuccess();
    }

    /**
     * login executado com erro (do Facebook)
     */
    public void onFacebookError(FacebookError error) {

      SessionEvents.onLoginError(error.getMessage());
    }

    /**
     * login executado com erro
     */
    public void onError(DialogError error) {

      SessionEvents.onLoginError(error.getMessage());
    }

    public void onCancel() {

      SessionEvents.onLoginError("Action Canceled");
    }

  }

  /**
   * Listener para cuidadar do processo de logout
   * 
   */
  private class LogoutRequestListener extends BaseRequestListener {

    /**
     * opera��o executada com sucesso
     */
    public void onComplete(String response, final Object state) {

      /*
       * callback should be run in the original thread, not the background
       * thread
       */
      mHandler.post(new Runnable() {

        public void run() {

          SessionEvents.onLogoutFinish();
        }

      });

    }

    public void onFacebookError(FacebookError e, Object state) {

    }

  }

  /**
   * 
   * Listener de sess�o
   * 
   */
  private class SessionListener implements AuthListener, LogoutListener {

    /**
     * A autoriza��o da sess�o feita com sucesso
     */
    public void onAuthSucceed() {

      // salva a sess�o
      SessionStore.save(mFb, mContext);

    }

    /**
     * Falha na autoriza��o da sess�o
     */
    public void onAuthFail(String error) {

    }

    /**
		 * 
		 */
    public void onLogoutBegin() {

    }

    /**
     * Chamado no termino da sess�o
     */
    public void onLogoutFinish() {

      // limpa a sess�o
      SessionStore.clear(mContext);

    }

  }

}
