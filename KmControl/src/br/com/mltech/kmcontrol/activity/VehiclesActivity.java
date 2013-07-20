package br.com.mltech.kmcontrol.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
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
import br.com.mltech.kmcontrol.adapter.VeiculoListAdapter;
import br.com.mltech.kmcontrol.data.VeiculoData;
import br.com.mltech.kmcontrol.model.Veiculo;

/**
 * This activity is supposed to manage vehicles (add, remove, etc).
 * 
 * 
 */
public class VehiclesActivity extends Activity {

	private static String TAG = "VehiclesActivity";
	
	private LinearLayout mAddArea;
	private ImageButton mBtnAdd;
	private ImageButton mBtnCancelAdd;

	// Cursors
	private Cursor mCursor;

	// Edit Texts
	private EditText mNome;
	private EditText mMarca;
	private EditText mModelo;
	private EditText mPlaca;

	// List views
	private ListView mList;

	// VeiculoListAdapter
	private VeiculoListAdapter mListAdapter;

	// Create a Vehicle object
	Veiculo mVeiculo = new Veiculo();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Log.d(TAG, "onCreate() ...");

		setContentView(R.layout.activity_veiculo);

		manageVehicles();

	}

	/**
     * 
     */
	private void manageVehicles() {

		// enable home button on action bar
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// identificador do LinearLayout responsável pela edição dos dados do veículo
		mAddArea = (LinearLayout) findViewById(R.id.add_veiculo_area);

		// A área de inserção de veículos está inicialmente desabilitada
		mAddArea.setVisibility(View.GONE);

		// botão para inserção do veículo
		mBtnAdd = (ImageButton) findViewById(R.id.btn_add_veiculo);

		// Botão responsável pela inserção do veículo
		mBtnAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				addUpdateVehicle();

			}

		});

		// Botão para cancelamento da inserção do veículo
		mBtnCancelAdd = (ImageButton) findViewById(R.id.btn_add_abastecimento);

		// Cancel the changes
		mBtnCancelAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Clear object fields
				mVeiculo.clearFields();
				hideAddArea();
			}

		});

		// Associate the attributes with the widgets values
		// preenche as informações de um veículo
		mNome = (EditText) findViewById(R.id.edit_veiculo_nome);
		mMarca = (EditText) findViewById(R.id.edit_veiculo_marca);
		mModelo = (EditText) findViewById(R.id.edit_veiculo_modelo);
		mPlaca = (EditText) findViewById(R.id.edit_veiculo_placa);

		mList = (ListView) findViewById(R.id.abastecimento_list);

		// init data
		mCursor = null;

		// Log.d(TAG, "Creating VehicleListAdapter ...");
		mListAdapter = new VeiculoListAdapter(getApplicationContext(), mCursor,	true, mOnItemButtonClickListener);

		mList.setAdapter(mListAdapter);

	}

	/**
	 * Add or Update a Vehicle
	 */
	private void addUpdateVehicle() {

		if (isVehicleEmpty()) {
			return;
		}

		// Update Vehicle object
		mVeiculo.setNome(mNome.getText().toString());
		mVeiculo.setMarca(mMarca.getText().toString());
		mVeiculo.setModelo(mModelo.getText().toString());
		mVeiculo.setPlaca(mPlaca.getText().toString());

		if (mVeiculo.getId() > 0) {
			new UpdateVehicleItemAsync(mOnCursorChanged).execute(mVeiculo);
		} else {
			new AddVehicleItemAsync(mOnCursorChanged).execute(mVeiculo);
			Log.i(TAG, "Vehicle inserido: " + mVeiculo);
		}

		// clear fields (GUI fields)
		mNome.setText("");
		mMarca.setText("");
		mModelo.setText("");
		mPlaca.setText("");

		// Clear object fields
		mVeiculo.clearFields();

		// hide add area
		hideAddArea();

	}

	/**
	 * Verifica se algum campo da tela não foi preennchido.
	 * 
	 * @return true se houver algum campo sem preenchimento ou false caso todos os campos estejam preenchidos.
	 * 
	 */
	private boolean isVehicleEmpty() {

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

	}

	@Override
	protected void onStart() {

		super.onStart();

		new QueryVehicleItemAsync(mOnCursorChanged).execute(new Veiculo());

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
		getMenuInflater().inflate(R.menu.activity_vehicles, menu);

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Log.d(TAG, "onOptionsItemSelected() ...");

		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;

		case R.id.add_vehicle:
			showAddArea();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	/**
	 * Show add panel where the information about the vehicle can be changed
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
			AsyncTask<Veiculo, Void, Integer> {

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
		protected Integer doInBackground(Veiculo... vehicleList) {

			performDatabaseAction(vehicleList);

			loadVehicleList();

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
		protected abstract void performDatabaseAction(Veiculo... vehicleList);

		/**
		 * Load the vehicle list
		 */
		protected final void loadVehicleList() {

			// Log.d(TAG, "loadVehicleList() - before ...");

			Log.d(TAG, "VehicleData.CONTENT_URI: " + VeiculoData.CONTENT_URI);

			mCursor = getContentResolver().query(VeiculoData.CONTENT_URI, null,
					null, null, VeiculoData.VeiculoColumns.MARCA);

		}

	}

	/**
	 * Delete a vehicle
	 * 
	 */
	private class DeleteVehicleItemAsync extends PerformDatabaseAction {

		/**
		 * Constructor
		 * 
		 * @param onCursorChanged
		 */
		DeleteVehicleItemAsync(OnCursorChanged onCursorChanged) {
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
		protected void performDatabaseAction(Veiculo... vehicleList) {

			Uri updateURI = Uri.withAppendedPath(VeiculoData.CONTENT_URI,
					String.valueOf(vehicleList[0].getId()));

			int numRowsDeleted = getContentResolver().delete(updateURI, null,
					null);

			Log.d(TAG, numRowsDeleted + " row(s) where removed.");

			mVeiculo.clearFields();

		}

	}

	/**
	 * Add a new Vehicle
	 */
	private class AddVehicleItemAsync extends PerformDatabaseAction {

		/**
		 * Constructor
		 * 
		 * @param onCursorChanged
		 * 
		 */
		AddVehicleItemAsync(OnCursorChanged onCursorChanged) {
			super(onCursorChanged);
		}

		/**
		 * 
		 */
		protected Integer getToastMessageResourceId() {
			return Integer.valueOf(R.string.toast_item_added);
		}

		/**
		 * Create a ContentValue for a Vehicle
		 * 
		 * @param vehicleList
		 * 
		 * @return A ContentValue with Vehicle information.
		 */
		ContentValues createVehicle(Veiculo... vehicleList) {

			ContentValues values = new ContentValues();

			values.put(VeiculoData.VeiculoColumns.NOME,
					vehicleList[0].getNome());
			values.put(VeiculoData.VeiculoColumns.MARCA,
					vehicleList[0].getMarca());
			values.put(VeiculoData.VeiculoColumns.MODELO,
					vehicleList[0].getModelo());
			values.put(VeiculoData.VeiculoColumns.PLACA,
					vehicleList[0].getPlaca());

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
		protected void performDatabaseAction(Veiculo... vehicleList) {

			ContentValues values = createVehicle(vehicleList);

			Log.d(TAG, "vehicleList.length: " + vehicleList.length);
			Log.d(TAG, "performDatabaseAction - vehicleList: " + vehicleList[0]);

			// Get the Uri of the last inserted vehicle
			Uri myUri = getContentResolver().insert(VeiculoData.CONTENT_URI,
					values);

			String myIdVehicle = null;

			if (myUri != null) {

				showUri(myUri);

				myIdVehicle = myUri.getLastPathSegment();
				
				Log.d(TAG, "myIdVehicle: " + myIdVehicle);

			}

		}

	}

	/**
     * 
     * 
     *
     */
	private class UpdateVehicleItemAsync extends PerformDatabaseAction {

		/**
		 * Constructor
		 * 
		 * @param onCursorChanged
		 */
		UpdateVehicleItemAsync(OnCursorChanged onCursorChanged) {
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
		protected void performDatabaseAction(Veiculo... vehicleList) {

			ContentValues values = new ContentValues();

			values.put(VeiculoData.VeiculoColumns.NOME,
					vehicleList[0].getNome());
			values.put(VeiculoData.VeiculoColumns.MARCA,
					vehicleList[0].getMarca());
			values.put(VeiculoData.VeiculoColumns.MODELO,
					vehicleList[0].getModelo());
			values.put(VeiculoData.VeiculoColumns.PLACA,
					vehicleList[0].getPlaca());

			Uri updateURI = Uri.withAppendedPath(VeiculoData.CONTENT_URI,
					String.valueOf(vehicleList[0].getId()));

			int numRowsUpdated = getContentResolver().update(updateURI, values,
					null, null);

			Log.d(TAG, "numRowsUpdates: " + numRowsUpdated);

		}

	}

	/**
	 * Query Vehicle Item
	 * 
	 * 
	 */
	private class QueryVehicleItemAsync extends PerformDatabaseAction {

		/**
		 * Constructor
		 */
		QueryVehicleItemAsync(OnCursorChanged onCursorChanged) {
			super(onCursorChanged);
			Log.d(TAG, "QueryVehicleItemAsync ...");
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
		protected void performDatabaseAction(Veiculo... vehicleList) {
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
			mVeiculo.init(c);

			switch (v.getId()) {

			case R.id.button_abastecimento_remove: {
				new DeleteVehicleItemAsync(mOnCursorChanged).execute(mVeiculo);
				break;
			}

			case R.id.button_abastecimento_edit: {

				showAddArea();

				mNome.setText(mVeiculo.getNome());
				mMarca.setText(mVeiculo.getMarca());
				mModelo.setText(mVeiculo.getModelo());
				mPlaca.setText(mVeiculo.getPlaca());

				break;

			}

			}

		}

	};

}
