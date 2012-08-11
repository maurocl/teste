/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * LoginButton � um "controle" (bot�o usado para fazer login)
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
    * @param context contexto da aplica��o
    * 
    */
    public LoginButton(Context context) {
        super(context);
    }

    /**
     * Construtor
     *  
     * @param context contexto da aplica��o
     * @param attrs atributos
     * 
     */
    public LoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Construtor
     * 
     * @param context contexto da aplica��o
     * @param attrs atributos
     * @param defStyle defini��o de estilo
     * 
     */
    public LoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 
     * @param activity
     * @param activityCode
     * @param fb
     */
    public void init(final Activity activity, final int activityCode, final Facebook fb) {
    	
        init(activity, activityCode, fb, new String[] {});
        
    }

    /**
     * 
     * @param activity
     * @param activityCode
     * @param fb
     * @param permissions
     * 
     */
    public void init(final Activity activity, final int activityCode, final Facebook fb,  final String[] permissions) {
       
    	mActivity = activity;
        mActivityCode = activityCode;
        mFb = fb;
        mPermissions = permissions;
        mHandler = new Handler();

        // estabelece a cor de fundo
        setBackgroundColor(Color.TRANSPARENT);
        
        setImageResource(fb.isSessionValid() ? R.drawable.logout_button : R.drawable.login_button);
        
        drawableStateChanged();

        SessionEvents.addAuthListener(mSessionListener);
        SessionEvents.addLogoutListener(mSessionListener);
        
        setOnClickListener(new ButtonOnClickListener());
        
    }

    /**
     * 
     * 
     *
     */
    private final class ButtonOnClickListener implements OnClickListener {
        /*
         * Source Tag: login_tag
         */
        
        public void onClick(View arg0) {
        	
        	// verifica se a sess�o do Facebook ainda � v�lida
            if (mFb.isSessionValid()) {
            
            	// sess�o � v�lida
            	
            	// "inicia o processo de logout"
            	SessionEvents.onLogoutBegin();
                
            	AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);
                
            	// executa o logout
            	asyncRunner.logout(getContext(), new LogoutRequestListener());
            	
            } else {
            	
            	// sess�o n�o � v�lida
            	
            	// solicita autoriza��o 
                mFb.authorize(mActivity, mPermissions, mActivityCode, new LoginDialogListener());
                
            }
            
        }
        
    }

    /**
     * 
     * 
     *
     */
    private final class LoginDialogListener implements DialogListener {
       
    	/**
    	 * 
    	 */
        public void onComplete(Bundle values) {
            SessionEvents.onLoginSuccess();
        }

        
        /**
         * 
         */
        public void onFacebookError(FacebookError error) {
            SessionEvents.onLoginError(error.getMessage());
        }

        /**
         * 
         */
        public void onError(DialogError error) {
            SessionEvents.onLoginError(error.getMessage());
        }

        /**
         * 
         */
        public void onCancel() {
            SessionEvents.onLoginError("Action Canceled");
        }
        
    }

    /**
     * 
     * 
     *
     */
    private class LogoutRequestListener extends BaseRequestListener {
       
    	/**
    	 * 
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
     * Listener de sess�o
     *
     */
    private class SessionListener implements AuthListener, LogoutListener {

        /**
         * A autoriza��o da sess�o feita com sucesso
         */
        public void onAuthSucceed() {
           
        	setImageResource(R.drawable.logout_button);
            
        	SessionStore.save(mFb, getContext());
        	
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
         *  Chamado no termino da sess�o
         */
        public void onLogoutFinish() {
        	
            SessionStore.clear(getContext());
            
            setImageResource(R.drawable.login_button);
            
        }
        
    }

}
