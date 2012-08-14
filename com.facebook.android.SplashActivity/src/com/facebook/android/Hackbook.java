/*
 * Copyright 2004 - Present Facebook, Inc.
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

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;

/**
 * 
 * 
 *
 */
public class Hackbook extends Activity implements OnItemClickListener {

	/*
	 * Your Facebook Application ID must be set before running this example See
	 * http://www.facebook.com/developers/createapp.php
	 */
	// public static final String APP_ID = "157111564357680";
	public static final String APP_ID = "304628302969929";

	// botão de login
	private LoginButton mLoginButton;

	private TextView mText;

	private ImageView mUserPic;

	private Handler mHandler;

	// caixa de diálogo de progresso
	ProgressDialog dialog;

	final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;
	final static int PICK_EXISTING_PHOTO_RESULT_CODE = 1;

	private String graph_or_fql;

	private ListView list;

	/**
	 * Itens principais
	 */
	String[] main_items = { "Update Status", "App Requests", "Get Friends", "Upload Photo", "Place Check-in", "Run FQL Query",
			"Graph API Explorer", "Token Refresh" };

	/**
	 * Permissões
	 */
	String[] permissions = { "offline_access", "publish_stream", "user_photos", "publish_checkins", "photo_upload" };

	/** Called when the activity is first created. */

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (APP_ID == null) {
			Util.showAlert(this, "Warning", "Facebook Applicaton ID must be " + "specified before running this example: see FbAPIs.java");
			return;
		}

		setContentView(R.layout.main);

		// Default constructor associates this handler with the queue for the
		// current thread.
		// If there isn't one, this handler won't be able to receive messages.
		mHandler = new Handler();

		mText = (TextView) Hackbook.this.findViewById(R.id.txt);
		mUserPic = (ImageView) Hackbook.this.findViewById(R.id.user_pic);

		// Create the Facebook Object using the app id.
		Utility.mFacebook = new Facebook(APP_ID);

		// Instantiate the asyncrunner object for asynchronous api calls.
		Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);

		mLoginButton = (LoginButton) findViewById(R.id.login);

		// restore session if one exists
		SessionStore.restore(Utility.mFacebook, this);

		// adiciona um listener para cuidar das autorizações
		SessionEvents.addAuthListener(new FbAPIsAuthListener());

		// adciona um novo listener para cuidar do precesso de logout
		SessionEvents.addLogoutListener(new FbAPIsLogoutListener());

		/*
		 * Source Tag: login_tag
		 */
		mLoginButton.init(this, AUTHORIZE_ACTIVITY_RESULT_CODE, Utility.mFacebook, permissions);

		if (Utility.mFacebook.isSessionValid()) {
			requestUserData();
		}

		list = (ListView) findViewById(R.id.main_list);

		list.setOnItemClickListener(this);

		list.setAdapter(new ArrayAdapter<String>(this, R.layout.main_list_item, main_items));

	}

	/**
	 * 
	 */
	public void onResume() {

		super.onResume();

		// se o Facebook ainda não estiver sido instanciado
		if (Utility.mFacebook != null) {

			// se a sessão não for válida
			if (!Utility.mFacebook.isSessionValid()) {

				// exibe mensagem indicando que o usuário foi logged out
				mText.setText("You are logged out! ");

				// remove a imagem do usuário
				mUserPic.setImageBitmap(null);

			} else {

				// sessão está válida

				//
				Utility.mFacebook.extendAccessTokenIfNeeded(this, null);

			}

		}

	}

	/**
	 * Trata o callback das activities.
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * 
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		/*
		 * if this is the activity result from authorization flow, do a call back to
		 * authorizeCallback Source Tag: login_tag
		 */
		case AUTHORIZE_ACTIVITY_RESULT_CODE: {
			Utility.mFacebook.authorizeCallback(requestCode, resultCode, data);
			break;
		}

		/*
		 * if this is the result for a photo picker from the gallery, upload the
		 * image after scaling it. You can use the Utility.scaleImage() function for
		 * scaling
		 */
		case PICK_EXISTING_PHOTO_RESULT_CODE: {

			if (resultCode == Activity.RESULT_OK) {

				// Retrieve data this intent is operating on
				Uri photoUri = data.getData();

				if (photoUri != null) {

					// há uma foto selecionada

					Bundle params = new Bundle();

					try {
						// Inserts a byte array value into the mapping of this Bundle,
						// replacing any existing value for the given key.
						// Either key or value may be null.
						params.putByteArray("photo", Utility.scaleImage(getApplicationContext(), photoUri));

					} catch (IOException e) {

						e.printStackTrace();

					}

					params.putString("caption", "FbAPIs Sample App photo upload");

					Utility.mAsyncRunner.request("me/photos", params, "POST", new PhotoUploadListener(), null);

				} else {

					Toast.makeText(getApplicationContext(), "Error selecting image from the gallery.", Toast.LENGTH_SHORT).show();

				}
			} else {

				Toast.makeText(getApplicationContext(), "No image selected for upload.", Toast.LENGTH_SHORT).show();

			}
			break;
		}

		}

	}

	/**
	 * parent The AdapterView where the click happened. view The view within the
	 * AdapterView that was clicked (this will be a view provided by the adapter)
	 * position The position of the view in the adapter. id The row id of the item
	 * that was clicked.
	 */
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

		switch (position) {
		/*
		 * Source Tag: update_status_tag Update user's status by invoking the feed
		 * dialog To post to a friend's wall, provide his uid in the 'to' parameter
		 * Refer to https://developers.facebook.com/docs/reference/dialogs/feed/ for
		 * more info.
		 */
		case 0: {

			Bundle params = new Bundle();

			params.putString("caption", getString(R.string.app_name));
			params.putString("description", getString(R.string.app_desc));
			params.putString("picture", Utility.HACK_ICON_URL);
			params.putString("name", getString(R.string.app_action));

			Utility.mFacebook.dialog(Hackbook.this, "feed", params, new UpdateStatusListener());

			// obtem o token de acesso
			String access_token = Utility.mFacebook.getAccessToken();

			// exibe o token de acesso
			System.out.println(access_token);

			break;

		}

		/*
		 * Source Tag: app_requests Send an app request to friends. If no friend is
		 * specified, the user will see a friend selector and will be able to select
		 * a maximum of 50 recipients. To send request to specific friend, provide
		 * the uid in the 'to' parameter Refer to
		 * https://developers.facebook.com/docs/reference/dialogs/requests/ for more
		 * info.
		 */
		case 1: {

			Bundle params = new Bundle();

			params.putString("message", getString(R.string.request_message));

			Utility.mFacebook.dialog(Hackbook.this, "apprequests", params, new AppRequestsListener());

			break;
		}

		/*
		 * Source Tag: friends_tag You can get friends using
		 * graph.facebook.com/me/friends, this returns the list sorted by UID OR
		 * using the friend table. With this you can sort the way you want it.
		 * Friend table - https://developers.facebook.com/docs/reference/fql/friend/
		 * User table - https://developers.facebook.com/docs/reference/fql/user/
		 */
		case 2: {

			if (!Utility.mFacebook.isSessionValid()) {

				Util.showAlert(this, "Warning", "You must first log in.");

			} else {
				dialog = ProgressDialog.show(Hackbook.this, "", getString(R.string.please_wait), true, true);
				new AlertDialog.Builder(this).setTitle(R.string.Graph_FQL_title).setMessage(R.string.Graph_FQL_msg)
						.setPositiveButton(R.string.graph_button, new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {

								graph_or_fql = "graph";

								Bundle params = new Bundle();

								// define quais campos serão retornaos
								params.putString("fields", "name, picture, location");

								Utility.mAsyncRunner.request("me/friends", params, new FriendsRequestListener());

							}

						}).setNegativeButton(R.string.fql_button, new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								graph_or_fql = "fql";

								String query = "select name, current_location, uid, pic_square from user where uid in (select uid2 from friend where uid1=me()) order by name";

								Bundle params = new Bundle();

								params.putString("method", "fql.query");

								params.putString("query", query);

								Utility.mAsyncRunner.request(null, params, new FriendsRequestListener());

							}

						}).setOnCancelListener(new DialogInterface.OnCancelListener() {

							public void onCancel(DialogInterface d) {
								dialog.dismiss();
							}
						}).show();

			}
			break;
		}

		/*
		 * Source Tag: upload_photo You can upload a photo from the media gallery or
		 * from a remote server How to upload photo:
		 * https://developers.facebook.com/blog/post/498/
		 */
		case 3: {
			if (!Utility.mFacebook.isSessionValid()) {

				// sessão não é válida
				Util.showAlert(this, "Warning", "You must first log in.");

			} else {

				// cria uma caixa de diálogo de progresso
				dialog = ProgressDialog.show(Hackbook.this, "", getString(R.string.please_wait), true, true);

				new AlertDialog.Builder(this).setTitle(R.string.gallery_remote_title).setMessage(R.string.gallery_remote_msg)
						.setPositiveButton(R.string.gallery_button, new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(Intent.ACTION_PICK, (MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
								startActivityForResult(intent, PICK_EXISTING_PHOTO_RESULT_CODE);
							}

						}).setNegativeButton(R.string.remote_button, new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								/*
								 * Source tag: upload_photo_tag
								 */
								Bundle params = new Bundle();
								params.putString("url", "http://www.facebook.com/images/devsite/iphone_connect_btn.jpg");
								params.putString("caption", "FbAPIs Sample App photo upload");
								Utility.mAsyncRunner.request("me/photos", params, "POST", new PhotoUploadListener(), null);
							}

						}).setOnCancelListener(new DialogInterface.OnCancelListener() {

							public void onCancel(DialogInterface d) {
								dialog.dismiss();
							}

						}).show();

			}
			break;
		}

		/*
		 * User can check-in to a place, you require publish_checkins permission for
		 * that. You can use the default Times Square location or fetch user's
		 * current location. Get user's checkins:
		 * https://developers.facebook.com/docs/reference/api/checkin/
		 */
		case 4: {

			final Intent myIntent = new Intent(getApplicationContext(), Places.class);

			new AlertDialog.Builder(this).setTitle(R.string.get_location).setMessage(R.string.get_default_or_new_location)
					.setPositiveButton(R.string.current_location_button, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							myIntent.putExtra("LOCATION", "current");
							startActivity(myIntent);
						}

					}).setNegativeButton(R.string.times_square_button, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							myIntent.putExtra("LOCATION", "times_square");
							startActivity(myIntent);
						}

					}).show();

			break;
		}

		case 5: {
			if (!Utility.mFacebook.isSessionValid()) {

				Util.showAlert(this, "Warning", "You must first log in.");

			} else {

				new FQLQuery(Hackbook.this).show();

			}
			break;
		}
		/*
		 * This is advanced feature where you can request new permissions Browser
		 * user's graph, his fields and connections. This is similar to the www
		 * version: http://developers.facebook.com/tools/explorer/
		 */
		case 6: {

			Intent myIntent = new Intent(getApplicationContext(), GraphExplorer.class);

			if (Utility.mFacebook.isSessionValid()) {
				Utility.objectID = "me";
			}

			startActivity(myIntent);

			break;

		}

		case 7: {

			if (!Utility.mFacebook.isSessionValid()) {
				Util.showAlert(this, "Warning", "You must first log in.");
			} else {
				new TokenRefreshDialog(Hackbook.this).show();
			}

		}

		}
	}

	/*
	 * callback for the feed dialog which updates the profile status
	 */
	public class UpdateStatusListener extends BaseDialogListener {

		/**
		 * 
		 */
		public void onComplete(Bundle values) {

			final String postId = values.getString("post_id");

			if (postId != null) {

				new UpdateStatusResultDialog(Hackbook.this, "Update Status executed", values).show();

			} else {

				Toast toast = Toast.makeText(getApplicationContext(), "No wall post made", Toast.LENGTH_SHORT);
				toast.show();

			}

		}

		/**
		 * 
		 */
		public void onFacebookError(FacebookError error) {

			Toast.makeText(getApplicationContext(), "Facebook Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

		}

		/**
		 * 
		 */
		public void onCancel() {

			Toast toast = Toast.makeText(getApplicationContext(), "Update status cancelled", Toast.LENGTH_SHORT);

			toast.show();

		}

	}

	/*
	 * callback for the apprequests dialog which sends an app request to user's
	 * friends.
	 */
	public class AppRequestsListener extends BaseDialogListener {

		/**
		 * 
		 */
		public void onComplete(Bundle values) {
			Toast toast = Toast.makeText(getApplicationContext(), "App request sent", Toast.LENGTH_SHORT);
			toast.show();
		}

		/**
		 * 
		 */
		public void onFacebookError(FacebookError error) {
			Toast.makeText(getApplicationContext(), "Facebook Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
		}

		/**
		 * 
		 */
		public void onCancel() {
			Toast toast = Toast.makeText(getApplicationContext(), "App request cancelled", Toast.LENGTH_SHORT);
			toast.show();
		}

	}

	/*
	 * callback after friends are fetched via me/friends or fql query.
	 */
	public class FriendsRequestListener extends BaseRequestListener {

		/**
		 * "operação" executada com sucesso
		 */
		public void onComplete(final String response, final Object state) {

			// fecha a caixa de diálogo
			// Dismiss this dialog, removing it from the screen.
			// This method can be invoked safely from any thread.
			dialog.dismiss();

			// executa a intent FriendsList
			Intent myIntent = new Intent(getApplicationContext(), FriendsList.class);

			// Estabelece os parâmetros:
			myIntent.putExtra("API_RESPONSE", response);
			myIntent.putExtra("METHOD", graph_or_fql);

			// Chama a activity
			startActivity(myIntent);

		}

		/**
		 * Operação executada com erro
		 * 
		 * @param error
		 * 
		 */
		public void onFacebookError(FacebookError error) {

			// Dismiss this dialog, removing it from the screen.
			dialog.dismiss();

			Toast.makeText(getApplicationContext(), "Facebook Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
		}

	}

	/*
	 * callback for the photo upload
	 */
	public class PhotoUploadListener extends BaseRequestListener {

		/**
		 * operação executada co sucesso
		 */
		public void onComplete(final String response, final Object state) {

			// Dismiss this dialog, removing it from the screen.
			dialog.dismiss();

			// Causes the Runnable r to be added to the message queue.
			// The runnable will be run on the thread to which this handler is
			// attached.
			mHandler.post(new Runnable() {

				// executa o diálogo de resultado do upload da foto
				public void run() {
					new UploadPhotoResultDialog(Hackbook.this, "Upload Photo executed", response).show();
				}

			});

		}

		/**
		 * operção executada com erro
		 * 
		 * @param error
		 */
		public void onFacebookError(FacebookError error) {

			// Desfaz o diálogo indicativo de operação em progresso removendo-o da
			// tela.
			dialog.dismiss();

			Toast.makeText(getApplicationContext(), "Facebook Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * Facebook Query Language Listener
	 * 
	 * 
	 */
	public class FQLRequestListener extends BaseRequestListener {

		/**
		 * operação completada com sucesso
		 */
		public void onComplete(final String response, final Object state) {

			// Causes the Runnable r to be added to the message queue.
			// The runnable will be run on the thread to which this handler is
			// attached.
			mHandler.post(new Runnable() {

				public void run() {
					Toast.makeText(getApplicationContext(), "Response: " + response, Toast.LENGTH_LONG).show();
				}

			});

		}

		/**
		 * operação executada com erro
		 * 
		 * @param error
		 *          erro do Facebook
		 * 
		 */
		public void onFacebookError(FacebookError error) {

			Toast.makeText(getApplicationContext(), "Facebook Error: " + error.getMessage(), Toast.LENGTH_LONG).show();

		}

	}

	/*
	 * Callback for fetching current user's name, picture, uid.
	 */
	public class UserRequestListener extends BaseRequestListener {

		/**
		 * operação realizada com sucesso
		 */
		public void onComplete(final String response, final Object state) {

			JSONObject jsonObject;

			try {

				// cria um object a partir da String de responsta recebida
				jsonObject = new JSONObject(response);

				final String picURL = jsonObject.getString("picture");
				final String name = jsonObject.getString("name");
				Utility.userUID = jsonObject.getString("id");

				// Causes the Runnable r to be added to the message queue.
				// The runnable will be run on the thread to which this handler is
				// attached.
				mHandler.post(new Runnable() {

					public void run() {

						mText.setText("Welcome " + name + "!");
						mUserPic.setImageBitmap(Utility.getBitmap(picURL));

					}

				});

			} catch (JSONException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();

			}

		}

	}

	/**
	 * The Callback for notifying the application when authorization succeeds or
	 * fails.
	 * 
	 * 
	 * 
	 */
	public class FbAPIsAuthListener implements AuthListener {

		/**
		 * autorização executada com sucesso
		 */
		public void onAuthSucceed() {

			requestUserData();

		}

		/**
		 * falha na autorização
		 */
		public void onAuthFail(String error) {
			mText.setText("Login Failed: " + error);
		}

	}

	/**
	 * The Callback for notifying the application when log out starts and
	 * finishes.
	 * 
	 * 
	 */
	public class FbAPIsLogoutListener implements LogoutListener {

		/**
		 * inicio do processo de logout
		 */
		public void onLogoutBegin() {
			mText.setText("Logging out...");
		}

		/**
		 * método executado apos terminar o processo de logout
		 */
		public void onLogoutFinish() {
			// exibe mensagem de logout
			mText.setText("You have logged out! ");
			// remove a imagem
			mUserPic.setImageBitmap(null);
		}

	}

	/**
	 * Request user name, and picture to show on the main screen.
	 * 
	 * Requisita o nome e foto do usuário para exibição na tela principal
	 * 
	 */
	public void requestUserData() {

		mText.setText("Fetching user name, profile pic...");

		Bundle params = new Bundle();

		// parâmetro fields com os valores: name, picture
		params.putString("fields", "name, picture");

		// executa a requisição de acordo com os parâmetros
		Utility.mAsyncRunner.request("me", params, new UserRequestListener());

	}

	/**
	 * Definition of the list adapter
	 */
	public class MainListAdapter extends BaseAdapter {

		/**
		 * 
		 */
		private LayoutInflater mInflater;

		/**
		 * 
		 */
		public MainListAdapter() {
			mInflater = LayoutInflater.from(Hackbook.this.getBaseContext());
		}

		/**
		 * 
		 */
		public int getCount() {
			return main_items.length;
		}

		/**
		 * 
		 */
		public Object getItem(int position) {
			return null;
		}

		/**
		 * 
		 */
		public long getItemId(int position) {
			return 0;
		}

		/**
		 * 
		 */
		public View getView(int position, View convertView, ViewGroup parent) {

			View hView = convertView;

			if (convertView == null) {

				hView = mInflater.inflate(R.layout.main_list_item, null);

				ViewHolder holder = new ViewHolder();

				holder.main_list_item = (TextView) hView.findViewById(R.id.main_api_item);

				hView.setTag(holder);

			}

			ViewHolder holder = (ViewHolder) hView.getTag();

			holder.main_list_item.setText(main_items[position]);

			return hView;

		}

	}

	/**
	 * 
	 * @author maurocl
	 * 
	 */
	class ViewHolder {

		TextView main_list_item;
	}

}
