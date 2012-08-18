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

package com.facebook.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;

/**
 * LoginButton é um "controle" (botão usado para fazer login)
 * 
 * 
 */
public class LoginButton extends ImageButton {

  private Facebook mFb;

  private Handler mHandler;

  private SessionListener mSessionListener = new SessionListener();

  private String[] mPermissions;

  private Activity mActivity;

  private int mActivityCode;

  /**
   * Construtor
   * 
   * @param context
   *          contexto da aplicação
   * 
   */
  public LoginButton(Context context) {

    super(context);
  }

  /**
   * Construtor
   * 
   * @param context
   *          contexto da aplicação
   * @param attrs
   *          atributos
   * 
   */
  public LoginButton(Context context, AttributeSet attrs) {

    super(context, attrs);
  }

  /**
   * Construtor
   * 
   * @param context
   *          contexto da aplicação
   * @param attrs
   *          atributos
   * @param defStyle
   *          definição de estilo
   * 
   */
  public LoginButton(Context context, AttributeSet attrs, int defStyle) {

    super(context, attrs, defStyle);
  }

  /**
   * Método de inicialização
   * 
   * @param activity activity
   * @param activityCode código da activity
   * @param fb instância do Facebook
   * 
   */
  public void init(final Activity activity, final int activityCode, final Facebook fb) {

    init(activity, activityCode, fb, new String[] {});

  }

  /**
   * Método de inicialização
   * 
   * @param activity
   * @param activityCode
   * @param fb
   * @param permissions array de permissões
   * 
   */
  public void init(final Activity activity, final int activityCode, final Facebook fb, final String[] permissions) {

    mActivity = activity;
    mActivityCode = activityCode;
    mFb = fb;
    mPermissions = permissions;

    mHandler = new Handler();

    // estabelece a cor de fundo
    setBackgroundColor(Color.TRANSPARENT);

    // estabelece o logo do botão
    setImageResource(fb.isSessionValid() ? R.drawable.logout_button : R.drawable.login_button);

    drawableStateChanged();

    // adiciona um listener para autorizações
    SessionEvents.addAuthListener(mSessionListener);

    // adiciona um listener para logout
    SessionEvents.addLogoutListener(mSessionListener);

    // estabele o listener para cuidar do click no botão
    setOnClickListener(new ButtonOnClickListener());

  }

  /**
   * Classe usada para tratar o clique do botão de login.
   * 
   * 
   */
  private final class ButtonOnClickListener implements OnClickListener {

    /*
     * Source Tag: login_tag
     */

    public void onClick(View arg0) {

      // verifica se a sessão do Facebook ainda é válida
      if (mFb.isSessionValid()) {

        // sessão é válida

        // "inicia o processo de logout"
        SessionEvents.onLogoutBegin();

        // cria um runner para ser executado assincronamente
        AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);

        // executa o logout
        asyncRunner.logout(getContext(), new LogoutRequestListener());

      } else {

        // sessão não é válida

        // solicita autorização
        mFb.authorize(mActivity, mPermissions, mActivityCode, new LoginDialogListener());

      }

    }

  }

  /**
   * Listener para tratar do diálogo de login
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
     * operação executada com sucesso
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

  }

  /**
   * 
   * Listener de sessão
   * 
   */
  private class SessionListener implements AuthListener, LogoutListener {

    /**
     * A autorização da sessão feita com sucesso
     */
    public void onAuthSucceed() {

      // altera a imagem do botão para logout
      setImageResource(R.drawable.logout_button);

      // salva a sessão
      SessionStore.save(mFb, getContext());

    }

    /**
     * Falha na autorização da sessão
     */
    public void onAuthFail(String error) {

    }

    /**
		 * Início do logout
		 */
    public void onLogoutBegin() {

    }

    /**
     * Chamado no final da sessão
     */
    public void onLogoutFinish() {

      // limpa a sessão
      SessionStore.clear(getContext());

      // restabelece a imagem do botão de login
      setImageResource(R.drawable.login_button);

    }

  }

}
