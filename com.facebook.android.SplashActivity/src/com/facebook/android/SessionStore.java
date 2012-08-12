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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Armazena a sessão:
 * 
 * - access_token
 * - expires_in
 * 
 */
public class SessionStore {

	private static final String TOKEN = "access_token";
	private static final String EXPIRES = "expires_in";
	private static final String KEY = "facebook-session";

	/**
	 * Save the access token and expiry date so you don't have to fetch it each time
	 * 
	 * @param session
	 * @param context
	 * @return
	 */
	public static boolean save(Facebook session, Context context) {
	
		// Abre o editor de sessão do facebook
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
		
		// armazena o token
		editor.putString(TOKEN, session.getAccessToken());
		
		// armazena a data de expiração
		editor.putLong(EXPIRES, session.getAccessExpires());
		
		// atualiza as informações e retorna o retultado da operação
		return editor.commit();
		
	}

	/**
	 * Restore the access token and the expiry date from the shared preferences.
	 * 
	 * @param session
	 * @param context
	 * @return
	 */
	public static boolean restore(Facebook session, Context context) {
		
		SharedPreferences savedSession = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
		
		// Lê o token (caso ele não exista retorna null)
		session.setAccessToken(savedSession.getString(TOKEN, null));
		
		// Lê a "data de expiração" (retorna 0 caso não seja encontrado)
		session.setAccessExpires(savedSession.getLong(EXPIRES, 0));
		
		// retorna a validade da sessão
		return session.isSessionValid();
		
	}

	/**
	 * Limpa os dados armazenados
	 * 
	 * @param context contexto da aplicação
	 * 
	 */
	public static void clear(Context context) {
		
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
		
		editor.clear();
		
		editor.commit();
		
	}

}
