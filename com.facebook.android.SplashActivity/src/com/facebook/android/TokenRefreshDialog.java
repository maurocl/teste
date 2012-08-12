package com.facebook.android;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * Executa um refresh (atualização) do token
 * 
 */
public class TokenRefreshDialog extends Dialog {

	/**
	 * 
	 */
	private EditText tokenEdit, tokenExpiresEdit;

	/**
	 * 
	 */
	private TextView mUsefulTip;

	/**
	 * botão de refresh
	 */
	private Button mRefreshButton;

	/**
	 * 
	 */
	private Activity activity;

	/**
	 * Construtor
	 * 
	 * @param activity
	 *          activity
	 */
	public TokenRefreshDialog(Activity activity) {

		super(activity);

		// guarda a instância da activity
		this.activity = activity;

		// estabelece o título
		setTitle(R.string.refresh_token_title);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.token_refresh);

		tokenEdit = (EditText) findViewById(R.id.tokenEdit);
		tokenEdit.setText(Utility.mFacebook.getAccessToken());

		tokenExpiresEdit = (EditText) findViewById(R.id.tokenExpiresEdit);

		// Estabelece o "tempo de expiração" da sessão
		setExpiresAt(Utility.mFacebook.getAccessExpires());

		mUsefulTip = (TextView) findViewById(R.id.usefulTip);

		// Estabelece o métdo de movimento
		mUsefulTip.setMovementMethod(LinkMovementMethod.getInstance());

		mRefreshButton = (Button) findViewById(R.id.refresh_button);

		mRefreshButton.setOnClickListener(new View.OnClickListener() {

			/**
			 * 
			 */
			public void onClick(View v) {

				// atualiza o estado dos botões para false
				changeButtonState(false);

				// cria um listener
				RefreshTokenListener listener = new RefreshTokenListener();

				if (!Utility.mFacebook.extendAccessToken(activity, listener)) {
					
					listener.onError(new Error(activity.getString(R.string.refresh_token_binding_error)));
					
				}

			}

		});

	}


	/**
	 * 
	 * Listener de refresh do token
	 *
	 */
	private class RefreshTokenListener implements Facebook.ServiceListener {

		/**
		 * 
		 */
		public void onFacebookError(FacebookError e) {

			changeButtonState(true);

			String title = String.format(activity.getString(R.string.facebook_error) + "%d", e.getErrorCode());

			Util.showAlert(activity, title, e.getMessage());

		}

		/**
		 * 
		 */
		public void onError(Error e) {
			
			changeButtonState(true);
			
			Util.showAlert(activity, activity.getString(R.string.error), e.getMessage());
			
		}

		/**
		 * 
		 */
		public void onComplete(Bundle values) {

			changeButtonState(true);

			// The access_token and expires_at values are automatically updated,
			// so they can be obtained by using:
			// - Facebook.getAccessToken()
			// - Facebook.getAccessExpires()
			// methods, but we can also get them from the 'values' bundle.
			tokenEdit.setText(values.getString(Facebook.TOKEN));

			setExpiresAt(values.getLong(Facebook.EXPIRES));

		}
		
	}

	/**
	 * Altera o estado do botão
	 * 
	 * @param enabled
	 */
	private void changeButtonState(boolean enabled) {

		mRefreshButton.setEnabled(enabled);

		mRefreshButton.setText(enabled ? R.string.refresh_button : R.string.refresh_button_pending);

	}

	/**
	 * Estabelece o "tempo" onde a sessão será expirada
	 *  
	 * @param time tempo 
	 */
	private void setExpiresAt(long time) {

		// 
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

		tokenExpiresEdit.setText(dateFormat.format(new Date(time)));

	}

}
