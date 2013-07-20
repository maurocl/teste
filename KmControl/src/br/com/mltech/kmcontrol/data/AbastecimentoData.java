package br.com.mltech.kmcontrol.data;

import android.net.Uri;
import br.com.mltech.kmcontrol.provider.VehicleProvider;

public class AbastecimentoData {

	public static final String URI_TYPE = "abastecimento";
	public static final Uri CONTENT_URI = Uri.parse(VehicleProvider.CONTENT_URI + "/" + URI_TYPE);

	public static final String CONTENT_TYPE      = "vnd.android.cursor.dir/abastecimento";
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/abastecimento";

	public static String TABLE_NAME = "abastecimento";

	/**
	 * Static class with Vehicle Columns.
	 * 
	 * 
	 */
	public static class AbastecimentoColumns {
		public static final String _ID = "_id";
		public static final String DATA = "data";
		public static final String KMTOTAL = "km_total";
		public static final String VALOR = "valor";
		public static final String LITROS = "litros";
		public static final String KMPARCIAL = "km_parcial";
		public static final String MEDIA = "media";

		public static final int _ID_IDX = 0;
		public static final int DATA_IDX = 1;
		public static final int KMTOTAL_IDX = 2;
		public static final int VALOR_IDX = 3;
		public static final int LITROS_IDX = 4;
		public static final int KMPARCIAL_IDX = 5;
		public static final int MEDIA_IDX = 6;
		
	}

	/**
	 * Create an SQL table (vehicles)
	 * 
	 * @return A string with SQL command to create a table.
	 * 
	 */
	public static String getCreateVehicleTableSQL() {
		return "CREATE TABLE " + TABLE_NAME + " ( " + 
				AbastecimentoColumns._ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				AbastecimentoColumns.DATA	+ " TEXT, " + 
				AbastecimentoColumns.KMTOTAL  + " INTEGER, "	+ 
				AbastecimentoColumns.VALOR + " REAL," + 
				AbastecimentoColumns.LITROS	+ " REAL, "  + 
				AbastecimentoColumns.KMPARCIAL	+ " INTEGER, "  +
				AbastecimentoColumns.MEDIA	+ " REAL"  +
		" ) ";
	}

	
}
