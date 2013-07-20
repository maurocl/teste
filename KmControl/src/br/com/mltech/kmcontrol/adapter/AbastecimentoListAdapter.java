package br.com.mltech.kmcontrol.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import br.com.mltech.kmcontrol.R;
import br.com.mltech.kmcontrol.data.AbastecimentoData;

/**
 * Adapter para lista de abastecimentos
 * 
 */
public class AbastecimentoListAdapter extends CursorAdapter {

	public static final String TAG = "AbastecimentoListAdapter";

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
	public AbastecimentoListAdapter(Context context, Cursor cursor,boolean autoRequery, OnClickListener btnClickListener) {

		super(context, cursor, autoRequery);

		Log.d(TAG, "AbastecimentoListAdapter() constructor");

		// Update the code that will execute when the button were pressed.
		mBtnClickListener = btnClickListener;

	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		AbastecimentoListViewHolder holder = (AbastecimentoListViewHolder) view.getTag();

		Log.d(TAG, "bindView(): " + holder);

		// get information from cursor
		String data = cursor.getString(AbastecimentoData.AbastecimentoColumns.DATA_IDX);
		int kmTotal = cursor.getInt(AbastecimentoData.AbastecimentoColumns.KMTOTAL_IDX);
		double valor= cursor.getDouble(AbastecimentoData.AbastecimentoColumns.VALOR_IDX);
		double litros= cursor.getDouble(AbastecimentoData.AbastecimentoColumns.LITROS_IDX);
		//int kmParcial= cursor.getInt(AbastecimentoData.AbastecimentoColumns.KMPARCIAL_IDX);
		//double media= cursor.getDouble(AbastecimentoData.AbastecimentoColumns.MEDIA_IDX);

		Log.d(TAG,"data="+data);
		Log.d(TAG,"KmTotal="+kmTotal);
		Log.d(TAG,"valor="+valor);
		Log.d(TAG,"litros="+litros);
		
		// update ViewHolder with cursor information
		//updateViewHolder(holder, data, kmTotal, valor, litros, kmParcial, media);
		updateViewHolder(holder, data, kmTotal, valor, litros);

		view.setId(cursor.getInt(AbastecimentoData.AbastecimentoColumns._ID_IDX));

	}
	
	/**
	 * 
	 * @param holder
	 * @param data
	 * @param kmTotal
	 * @param valor
	 * @param litros
	 * @param kmParcial
	 * @param media
	 */
	private void updateViewHolder(AbastecimentoListViewHolder holder, String data, int kmTotal, double valor, double litros, int kmParcial, double media) {
		holder.mTextViewData.setText(data);
		holder.mTextViewKmTotal.setText(kmTotal);
		holder.mTextViewValor.setText(String.valueOf(valor));
		holder.mTextViewLitros.setText(String.valueOf(litros));
		//holder.mTextViewKmParcial.setText(kmParcial);
		//holder.mTextViewMedia.setText(String.valueOf(media));
	}

	/**
	 * 
	 * @param holder
	 * @param data
	 * @param kmTotal
	 * @param valor
	 * @param litros
	 */
	private void updateViewHolder(AbastecimentoListViewHolder holder, String data, int kmTotal, double valor, double litros) {
		holder.mTextViewData.setText(data);
		holder.mTextViewKmTotal.setText(String.valueOf(kmTotal));
		holder.mTextViewValor.setText(String.valueOf(valor));
		holder.mTextViewLitros.setText(String.valueOf(litros));
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context);

		Log.d(TAG, "newView(): ");

		// Create a new view
		View v = inflater.inflate(R.layout.abastecimento_list_item, parent, false);

		// create a new ViewHolder
		AbastecimentoListViewHolder holder = new AbastecimentoListViewHolder();

		holder.mTextViewData = (TextView) v.findViewById(R.id.text_data);
		holder.mTextViewKmTotal = (TextView) v.findViewById(R.id.text_km_total);		
		holder.mTextViewValor = (TextView) v.findViewById(R.id.text_valor);	
		holder.mTextViewLitros = (TextView) v.findViewById(R.id.text_litros);
		//holder.mTextViewKmParcial = (TextView) v.findViewById(R.id.text_km_parcial);
		//holder.mTextViewMedia = (TextView) v.findViewById(R.id.text_media);

		holder.mButtonRemoveItem = (ImageButton) v.findViewById(R.id.button_abastecimento_remove);
		holder.mButtonRemoveItem.setOnClickListener(mBtnClickListener);

		holder.mButtonEditItem = (ImageButton) v.findViewById(R.id.button_abastecimento_edit);
		holder.mButtonEditItem.setOnClickListener(mBtnClickListener);

		//Log.d(TAG, "newView() holder: " + holder);

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
	public class AbastecimentoListViewHolder {
		TextView mTextViewData;
		TextView mTextViewKmTotal;
		TextView mTextViewValor;
		TextView mTextViewLitros;
		//TextView mTextViewKmParcial;
		//TextView mTextViewMedia;
		
		ImageButton mButtonRemoveItem;
		ImageButton mButtonEditItem;
	}

}
