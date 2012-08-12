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

import java.util.LinkedList;

/**
 * Eventos da Sessão
 * 
 * 
 */
public class SessionEvents {

	/**
	 * Lista contendo os listeners de autorização
	 */
	private static LinkedList<AuthListener> mAuthListeners = new LinkedList<AuthListener>();

	/**
	 * Lista contendo os listeners de logouts
	 */
	private static LinkedList<LogoutListener> mLogoutListeners = new LinkedList<LogoutListener>();

	/**
	 * Associate the given listener with this Facebook object. The listener's
	 * callback interface will be invoked when authentication events occur.
	 * 
	 * @param listener
	 *          The callback object for notifying the application when auth events
	 *          happen.
	 * 
	 */
	public static void addAuthListener(AuthListener listener) {
		
		// adiciona o listener a lista
		mAuthListeners.add(listener);
		
	}

	/**
	 * Remove the given listener from the list of those that will be notified when
	 * authentication events occur.
	 * 
	 * @param listener
	 *          The callback object for notifying the application when auth events
	 *          happen.
	 * 
	 */
	public static void removeAuthListener(AuthListener listener) {

		// remove o listener da lista
		mAuthListeners.remove(listener);
		
	}

	/**
	 * Associate the given listener with this Facebook object. The listener's
	 * callback interface will be invoked when logout occurs.
	 * 
	 * @param listener
	 *          The callback object for notifying the application when log out
	 *          starts and finishes.
	 */
	public static void addLogoutListener(LogoutListener listener) {
		
		mLogoutListeners.add(listener);
		
	}

	/**
	 * Remove the given listener from the list of those that will be notified when
	 * logout occurs.
	 * 
	 * @param listener
	 *          The callback object for notifying the application when log out
	 *          starts and finishes.
	 */
	public static void removeLogoutListener(LogoutListener listener) {
		
		mLogoutListeners.remove(listener);
		
	}

	/**
	 * login executado com sucesso
	 */
	public static void onLoginSuccess() {
		
		// percorre a lista de autorizações
		for (AuthListener listener : mAuthListeners) {
			
			// executa o método de autorização obtida com sucesso
			listener.onAuthSucceed();
			
		}
		
	}

	/**
	 * erro no login
	 * 
	 * @param error
	 * 
	 */
	public static void onLoginError(String error) {
		
		// notifica os interesados nesse evento
		for (AuthListener listener : mAuthListeners) {
			listener.onAuthFail(error);
		}
		
	}

	/**
	 * inicio de logout
	 */
	public static void onLogoutBegin() {

		// notifica os interessados nesse evento
		for (LogoutListener l : mLogoutListeners) {
			l.onLogoutBegin();
		}
		
	}

	/**
	 * fim do logout
	 */
	public static void onLogoutFinish() {

		// notifica os interesados nesse evento
		for (LogoutListener l : mLogoutListeners) {

			// executa o método de logout para cada elemento da lista
			l.onLogoutFinish();

		}

	}

	/**
	 * Callback interface for authorization events.
	 */
	public static interface AuthListener {

		/**
		 * Called when a auth flow completes successfully and a valid OAuth Token
		 * was received. Executed by the thread that initiated the authentication.
		 * API requests can now be made.
		 */
		public void onAuthSucceed();

		/**
		 * Called when a login completes unsuccessfully with an error.
		 * 
		 * Executed by the thread that initiated the authentication.
		 */
		public void onAuthFail(String error);

	}

	/**
	 * Callback interface for logout events.
	 */
	public static interface LogoutListener {

		/**
		 * Called when logout begins, before session is invalidated. Last chance to
		 * make an API call. Executed by the thread that initiated the logout.
		 */
		public void onLogoutBegin();

		/**
		 * Called when the session information has been cleared. UI should be
		 * updated to reflect logged-out state.
		 * 
		 * Executed by the thread that initiated the logout.
		 */
		public void onLogoutFinish();

	}

}
