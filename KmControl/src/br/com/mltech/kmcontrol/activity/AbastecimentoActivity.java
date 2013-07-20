package br.com.mltech.kmcontrol.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import br.com.mltech.kmcontrol.R;
import br.com.mltech.kmcontrol.adapter.AbastecimentoListAdapter;
import br.com.mltech.kmcontrol.adapter.VeiculoListAdapter;
import br.com.mltech.kmcontrol.data.AbastecimentoData;
import br.com.mltech.kmcontrol.model.Abastecimento;

public class AbastecimentoActivity extends Activity {

private static String TAG = "AbastecimentoActivity";
	
	private LinearLayout mAddArea;
	private ImageButton mBtnAdd;
	private ImageButton mBtnCancelAdd;

	// Cursors
	private Cursor mCursor;

	// Edit Texts
	private EditText mData;
	private EditText mKmTotal;
	private EditText mValor;
	private EditText mLitros;
	private EditText mKmParcial;
	private EditText mMedia;

	// List views
	private ListView mList;

	// VeiculoListAdapter
	private AbastecimentoListAdapter mListAdapter;

	// Create a Abastecimento object
	Abastecimento mAbastecimento = new Abastecimento();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Log.d(TAG, "onCreate() ...");

		setContentView(R.layout.activity_abastecimento);

		manageAbastecimento();

	}

	/**
     * 
     */
	private void manageAbastecimento() {

		// enable home button on action bar
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// identificador do LinearLayout responsável pela edição dos dados do veículo
		mAddArea = (LinearLayout) findViewById(R.id.add_abastecimento_area);

		// A área de inserção de veículos está inicialmente desabilitada
		mAddArea.setVisibility(View.GONE);

		// botão para inserção do veículo
		mBtnAdd = (ImageButton) findViewById(R.id.btn_add_abastecimento);

		// Botão responsável pela inserção do veículo
		mBtnAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				addUpdateAbastecimento();

			}

		});

		// Botão para cancelamento da inserção do veículo
		mBtnCancelAdd = (ImageButton) findViewById(R.id.btn_cancel_add_abastecimento);

		// Cancel the changes
		mBtnCancelAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Clear object fields
				mAbastecimento.clearFields();
				hideAddArea();
			}

		});

		// Associate the attributes with the widgets values
		// preenche as informações de um abastecimento
		mData = (EditText) findViewById(R.id.edit_abs_data);
		mKmTotal = (EditText) findViewById(R.id.edit_abs_km_total);
		mValor = (EditText) findViewById(R.id.edit_abs_valor);
		mLitros = (EditText) findViewById(R.id.edit_abs_num_litros);
		mKmParcial = (EditText) findViewById(R.id.edit_abs_km_parcial);
		mMedia = (EditText) findViewById(R.id.edit_abs_media);

		mList = (ListView) findViewById(R.id.abastecimento_list);

		// init data
		mCursor = null;

		// Log.d(TAG, "Creating AbastecimentoListAdapter ...");
		mListAdapter = new AbastecimentoListAdapter(getApplicationContext(), mCursor,	true, mOnItemButtonClickListener);

		mList.setAdapter(mListAdapter);

	}

	/**
	 * Add or Update a Abastecimento
	 */
	private void addUpdateAbastecimento() {

		if (isAbastecimentoEmpty()) {
			return;
		}

		// Update Abastecimento object
		mAbastecimento.setData(mData.getText().toString());
		mAbastecimento.setKmTotal(Integer.valueOf(mKmTotal.getText().toString()));
		mAbastecimento.setValor(Double.valueOf(mValor.getText().toString()));
		mAbastecimento.setLitros(Double.valueOf(mLitros.getText().toString()));
		mAbastecimento.setKmParcial(Integer.valueOf(mKmParcial.getText().toString()));
		mAbastecimento.setMedia(Double.valueOf(mMedia.getText().toString()));

		if (mAbastecimento.getId() > 0) {
			new UpdateAbastecimentoItemAsync(mOnCursorChanged).execute(mAbastecimento);
		} else {
			new AddAbastecimentoItemAsync(mOnCursorChanged).execute(mAbastecimento);
			Log.i(TAG, "Abastecimento inserido: " + mAbastecimento);
		}

		// clear fields (GUI fields)
		mData.setText("");
		mKmTotal.setText("");
		mValor.setText("");
		mLitros.setText("");
		mKmParcial.setText("");
		mMedia.setText("");
		
		// Clear object fields
		mAbastecimento.clearFields();

		// hide add area
		hideAddArea();

	}

	/**
	 * Verifica se algum campo da tela não foi preennchido.
	 * 
	 * @return true se houver algum campo sem preenchimento ou false caso todos os campos estejam preenchidos.
	 * 
	 */
	private boolean isAbastecimentoEmpty() {

		/*
		CharSequence nome = mNome.getText().toString();
		CharSequence marca = mMarca.getText().toString();
		CharSequence modelo = mModelo.getText().toString();
		CharSequence placa = mPlaca.getText().toString();

		boolean empty = TextUtils.isEmpty(nome) || TextUtils.isEmpty(marca) || TextUtils.isEmpty(modelo) || TextUtils.isEmpty(placa);

		if (empty) {

			Toast.makeText(getApplicationContext(),
					R.string.toast_missed_fill_fields, Toast.LENGTH_SHORT)
					.show();

		}

		return empty;
		*/
		return false;

	}

	@Override
	protected void onStart() {

		super.onStart();

		new QueryAbastecimentoItemAsync(mOnCursorChanged).execute(new Abastecimento());

	}

	@Override
	protected void onStop() {
		super.onStop();

		Log.d(TAG, "onStop");

		if (mCursor != null) {
			mCursor.close();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_abastecimento, menu);

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Log.d(TAG, "onOptionsItemSelected() ...");

		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;

		case R.id.add_abastecimento:
			showAddArea();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	/**
	 * Show add panel where the information about the Abastecimento can be changed
	 */
	private void showAddArea() {

		// Show the add area
		mAddArea.setVisibility(View.VISIBLE);

	}

	/**
	 * Hide add area
	 */
	private void hideAddArea() {

		mAddArea.setVisibility(View.GONE);

	}

	private interface OnCursorChanged {
		public void updateCursor();
	}

	/**
	 * Abstract class for performing database async tasks
	 * 
	 * 
	 */
	private abstract class PerformDatabaseAction extends
			AsyncTask<Abastecimento, Void, Integer> {

		private OnCursorChanged mAsyncTaskOnCursorChanged;

		/**
		 * Constructor
		 * 
		 * @param onCursorChanged
		 * 
		 */
		PerformDatabaseAction(OnCursorChanged onCursorChanged) {
			mAsyncTaskOnCursorChanged = onCursorChanged;
		}

		/**
         * 
         */
		protected Integer doInBackground(Abastecimento... abastecimento) {

			performDatabaseAction(abastecimento);

			loadAbastecimentoList();

			return getToastMessageResourceId();
		}

		/**
         * 
         */
		protected void onProgressUpdate(Void... param) {

		}

		/**
         * 
         */
		protected void onPostExecute(Integer resourceId) {

			displayToast(resourceId.intValue());

			mAsyncTaskOnCursorChanged.updateCursor();

		}

		/**
		 * Display a Toast
		 * 
		 * @param resourceId
		 * 
		 */
		protected void displayToast(int resourceId) {

			Toast.makeText(getApplicationContext(), resourceId,
					Toast.LENGTH_SHORT).show();

		}

		/**
		 * Returns the resource id of a toast messae
		 * 
		 * @return An integer with the resource id.
		 */
		protected abstract Integer getToastMessageResourceId();

		/**
		 * 
		 */
		protected abstract void performDatabaseAction(Abastecimento... abastecimentoList);

		/**
		 * Load the abastecimento list
		 */
		protected final void loadAbastecimentoList() {

			// Log.d(TAG, "load() - before ...");

			Log.d(TAG, "AbastecimentoData.CONTENT_URI: " + AbastecimentoData.CONTENT_URI);

			mCursor = getContentResolver().query(AbastecimentoData.CONTENT_URI, null,
					null, null, AbastecimentoData.AbastecimentoColumns.DATA);

		}

	}

	/**
	 * Delete a abastecimento
	 * 
	 */
	private class DeleteAbastecimentoItemAsync extends PerformDatabaseAction {

		/**
		 * Constructor
		 * 
		 * @param onCursorChanged
		 */
		DeleteAbastecimentoItemAsync(OnCursorChanged onCursorChanged) {
			super(onCursorChanged);
		}

		/**
         * 
         */
		protected Integer getToastMessageResourceId() {
			return Integer.valueOf(R.string.toast_item_removed);
		}

		/**
         * 
         */
		protected void performDatabaseAction(Abastecimento... abastecimento) {

			Uri updateURI = Uri.withAppendedPath(AbastecimentoData.CONTENT_URI,
					String.valueOf(abastecimento[0].getId()));

			int numRowsDeleted = getContentResolver().delete(updateURI, null,
					null);

			Log.d(TAG, numRowsDeleted + " row(s) where removed.");

			mAbastecimento.clearFields();

		}

	}

	/**
	 * Add a new Abastecimento
	 */
	private class AddAbastecimentoItemAsync extends PerformDatabaseAction {

		/**
		 * Constructor
		 * 
		 * @param onCursorChanged
		 * 
		 */
		AddAbastecimentoItemAsync(OnCursorChanged onCursorChanged) {
			super(onCursorChanged);
		}

		/**
		 * 
		 */
		protected Integer getToastMessageResourceId() {
			return Integer.valueOf(R.string.toast_item_added);
		}

		/**
		 * Create a ContentValue for a Abastecimento
		 * 
		 * @param abastecimento
		 * 
		 * @return A ContentValue with Abastecimento information.
		 */
		ContentValues createAbastecimento(Abastecimento... abastecimento) {

			ContentValues values = new ContentValues();

			values.put(AbastecimentoData.AbastecimentoColumns.DATA, abastecimento[0].getData());
			values.put(AbastecimentoData.AbastecimentoColumns.KMTOTAL, abastecimento[0].getKmTotal());
			values.put(AbastecimentoData.AbastecimentoColumns.VALOR, abastecimento[0].getValor());
			values.put(AbastecimentoData.AbastecimentoColumns.LITROS, abastecimento[0].getLitros());
			values.put(AbastecimentoData.AbastecimentoColumns.KMPARCIAL, abastecimento[0].getKmParcial());
			values.put(AbastecimentoData.AbastecimentoColumns.MEDIA, abastecimento[0].getMedia());

			return values;

		}

		/**
		 * Show Uri information
		 * 
		 * @param myUri
		 *            Uri instance
		 * 
		 */
		private void showUri(Uri myUri) {

			Log.d(TAG, "myUri=" + myUri);

		

		}

		/**
		 * 
		 */
		protected void performDatabaseAction(Abastecimento... abastecimento) {

			ContentValues values = createAbastecimento(abastecimento);

			Log.d(TAG, ".length: " + abastecimento.length);
			Log.d(TAG, "performDatabaseAction - : " + abastecimento[0]);

			// Get the Uri of the last inserted abastecimento
			Uri myUri = getContentResolver().insert(AbastecimentoData.CONTENT_URI,
					values);

			String myIdAbastecimento = null;

			if (myUri != null) {

				showUri(myUri);

				myIdAbastecimento = myUri.getLastPathSegment();
				
				Log.d(TAG, "myIdAbastecimento: " + myIdAbastecimento);

			}

		}

	}

	/**
     * 
     * 
     *
     */
	private class UpdateAbastecimentoItemAsync extends PerformDatabaseAction {

		/**
		 * Constructor
		 * 
		 * @param onCursorChanged
		 */
		UpdateAbastecimentoItemAsync(OnCursorChanged onCursorChanged) {
			super(onCursorChanged);
		}

		/**
         * 
         */
		protected Integer getToastMessageResourceId() {
			return Integer.valueOf(R.string.toast_item_updated);
		}

		/**
         * 
         */
		protected void performDatabaseAction(Abastecimento... abastecimento) {

			ContentValues values = new ContentValues();

			values.put(AbastecimentoData.AbastecimentoColumns.DATA, abastecimento[0].getData());
			values.put(AbastecimentoData.AbastecimentoColumns.KMTOTAL, abastecimento[0].getKmTotal());
			values.put(AbastecimentoData.AbastecimentoColumns.VALOR, abastecimento[0].getValor());
			values.put(AbastecimentoData.AbastecimentoColumns.LITROS, abastecimento[0].getLitros());
			values.put(AbastecimentoData.AbastecimentoColumns.KMPARCIAL, abastecimento[0].getKmParcial());
			values.put(AbastecimentoData.AbastecimentoColumns.MEDIA, abastecimento[0].getMedia());			
			
			Uri updateURI = Uri.withAppendedPath(AbastecimentoData.CONTENT_URI,
					String.valueOf(abastecimento[0].getId()));

			int numRowsUpdated = getContentResolver().update(updateURI, values,
					null, null);

			Log.d(TAG, "numRowsUpdates: " + numRowsUpdated);

		}

	}

	/**
	 * Query Abastecimento Item
	 * 
	 * 
	 */
	private class QueryAbastecimentoItemAsync extends PerformDatabaseAction {

		/**
		 * Constructor
		 */
		QueryAbastecimentoItemAsync(OnCursorChanged onCursorChanged) {
			super(onCursorChanged);
			Log.d(TAG, "QueryAbastecimentoItemAsync ...");
		}

		/**
         * 
         */
		protected Integer getToastMessageResourceId() {

			return Integer.valueOf(R.string.toast_list_loaded);

		}

		/**
         * 
         */
		protected void performDatabaseAction(Abastecimento... abastecimentoList) {
		}

	}

	/**
	 * Interface to update cursor adapter
	 */
	private OnCursorChanged mOnCursorChanged = new OnCursorChanged() {

		@Override
		public void updateCursor() {

			// Log.d(TAG, "onCursorChanged().updateCursor() ... before");

			if (mCursor == null) {
				// Log.w(TAG, "onCursorChanged().updateCursor() - null");
			} else {
				// Log.d(TAG, "onCursorChanged().updateCursor() IS NOT null");

				mListAdapter.changeCursor(mCursor);

			}

			// mFuelListAdapter.changeCursor(mFuelsCursor);

			// Log.d(TAG, "onCursorChanged().updateCursor() ... after");

		}

	};

	// List item button click
	private OnClickListener mOnItemButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			// position where clicked
			final int position = mList.getPositionForView((View) v.getParent());

			// cursor
			Cursor c = (Cursor) mListAdapter.getItem(position);

			//
			mAbastecimento.init(c);

			switch (v.getId()) {

			case R.id.button_abastecimento_remove: {
				new DeleteAbastecimentoItemAsync(mOnCursorChanged).execute(mAbastecimento);
				break;
			}

			case R.id.button_abastecimento_edit: {

				showAddArea();
				
				mData.setText(mAbastecimento.getData());
				mKmTotal.setText(String.valueOf(mAbastecimento.getKmTotal()));
				mValor.setText(String.valueOf(mAbastecimento.getValor()));
				mLitros.setText(String.valueOf(mAbastecimento.getLitros()));
				mKmParcial.setText(String.valueOf(mAbastecimento.getKmParcial()));
				mMedia.setText(String.valueOf(mAbastecimento.getMedia()));
				

				break;

			}

			}

		}

	};

	
}
