package br.com.mltech.kmcontrol.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import br.com.mltech.kmcontrol.R;
import br.com.mltech.kmcontrol.data.VeiculoData;

/**
 * Adapter para lista de Veículos
 * 
 */
public class VeiculoListAdapter extends CursorAdapter {

	public static final String TAG = "VeiculoListAdapter";

	OnClickListener mBtnClickListener;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            Application context
	 * @param cursor
	 *            Cursor
	 * @param autoRequery
	 *            Auto requery
	 * @param btnClickListener
	 *            Button event handler
	 */
	public VeiculoListAdapter(Context context, Cursor cursor,boolean autoRequery, OnClickListener btnClickListener) {

		super(context, cursor, autoRequery);

		// Log.d(TAG, "VehicleListAdapter() constructor");

		// Update the code that will execute when the button were pressed.
		mBtnClickListener = btnClickListener;

	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		VehicleListViewHolder holder = (VehicleListViewHolder) view.getTag();

		// Log.d(TAG, "bindView(): " + holder);

		// get information from cursor
		String vehicleNome = cursor.getString(VeiculoData.VeiculoColumns.NOME_IDX);
		String vehicleBrand = cursor.getString(VeiculoData.VeiculoColumns.MARCA_IDX);
		String vehicleModel = cursor.getString(VeiculoData.VeiculoColumns.MODELO_IDX);
		String vehicleLicensePlate = cursor.getString(VeiculoData.VeiculoColumns.PLACA_IDX);

		// update ViewHolder with cursor information
		updateViewHolder(holder, vehicleNome, vehicleBrand, vehicleModel, vehicleLicensePlate);

		view.setId(cursor.getInt(VeiculoData.VeiculoColumns._ID_IDX));

	}

	/**
	 * 
	 * @param holder
	 * @param vehicleNome
	 * @param vehicleBrand
	 * @param vehicleModel
	 * @param vehicleLicensePlate
	 */
	private void updateViewHolder(VehicleListViewHolder holder,	String vehicleNome, String vehicleBrand, String vehicleModel, String vehicleLicensePlate) {
		holder.mTextViewNome.setText(vehicleNome);
		holder.mTextViewMarca.setText(vehicleBrand);
		holder.mTextViewModelo.setText(vehicleModel);
		holder.mTextViewPlaca.setText(vehicleLicensePlate);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context);

		// Log.d(TAG, "newView(): ");

		// Create a new view
		View v = inflater.inflate(R.layout.vehicle_list_item, parent, false);

		// create a new ViewHolder
		VehicleListViewHolder holder = new VehicleListViewHolder();

		holder.mTextViewNome = (TextView) v.findViewById(R.id.text_veiculo_nome);
		holder.mTextViewMarca = (TextView) v.findViewById(R.id.text_veiculo_marca);		
		holder.mTextViewModelo = (TextView) v.findViewById(R.id.text_veiculo_modelo);	
		holder.mTextViewPlaca = (TextView) v.findViewById(R.id.text_veiculo_placa);

		holder.mButtonRemoveItem = (ImageButton) v.findViewById(R.id.button_abastecimento_remove);
		holder.mButtonRemoveItem.setOnClickListener(mBtnClickListener);

		holder.mButtonEditItem = (ImageButton) v.findViewById(R.id.button_abastecimento_edit);
		holder.mButtonEditItem.setOnClickListener(mBtnClickListener);

		// Log.d(TAG, "newView() holder: " + holder);

		// Store a holder in a view
		v.setTag(holder);

		// return the created view
		return v;

	}

	/**
	 * ViewHolder class
	 * 
	 * This class is responsible for mapping data fields to screen fields
	 * 
	 */
	public class VehicleListViewHolder {
		TextView mTextViewNome;
		TextView mTextViewMarca;
		TextView mTextViewModelo;
		TextView mTextViewPlaca;
		ImageButton mButtonRemoveItem;
		ImageButton mButtonEditItem;
	}

}
