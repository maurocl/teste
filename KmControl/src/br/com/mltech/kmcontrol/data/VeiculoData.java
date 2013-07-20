package br.com.mltech.kmcontrol.data;

import android.net.Uri;
import br.com.mltech.kmcontrol.provider.VehicleProvider;

/**
 * Defines information about Vehicle.
 * 
 * 
 */
public class VeiculoData {

	public static final String URI_TYPE = "veiculos";
	public static final Uri CONTENT_URI = Uri.parse(VehicleProvider.CONTENT_URI + "/" + URI_TYPE);

	public static final String CONTENT_TYPE      = "vnd.android.cursor.dir/veiculos";
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/veiculos";

	public static String TABLE_NAME = "veiculos";

	/**
	 * Static class with Vehicle Columns.
	 * 
	 * 
	 */
	public static class VeiculoColumns {
		public static final String _ID = "_id";
		public static final String NOME = "nome";
		public static final String MARCA = "marca";
		public static final String MODELO = "modelo";
		public static final String PLACA = "placa";

		public static final int _ID_IDX = 0;
		public static final int NOME_IDX = 1;
		public static final int MARCA_IDX = 2;
		public static final int MODELO_IDX = 3;
		public static final int PLACA_IDX = 4;
	}

	/**
	 * Create an SQL table (vehicles)
	 * 
	 * @return A string with SQL command to create a table.
	 * 
	 */
	public static String getCreateVehicleTableSQL() {
		return "CREATE TABLE " + TABLE_NAME + " ( " + 
	      VeiculoColumns._ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
		  VeiculoColumns.NOME	+ " TEXT, " + 
	      VeiculoColumns.MARCA  + " TEXT, "	+ 
		  VeiculoColumns.MODELO + " TEXT," + 
	      VeiculoColumns.PLACA	+ " TEXT"  + 
		" ) ";
	}

}
