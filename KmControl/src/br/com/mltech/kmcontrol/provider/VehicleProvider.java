package br.com.mltech.kmcontrol.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import br.com.mltech.kmcontrol.data.AbastecimentoData;
import br.com.mltech.kmcontrol.data.VeiculoData;
import br.com.mltech.kmcontrol.db.DBHelper;

/**
 * It is a ContentProvider for ...
 * 
 * 
 */
public class VehicleProvider extends ContentProvider {

	private static final String TAG = VehicleProvider.class.getSimpleName();

	public static final String AUTHORITY = "br.com.mltech";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

	
	private static final int ABASTECIMENTO = 1;
	private static final int ABASTECIMENTO_ID = 2;
	private static final int VEHICLE = 3;
	private static final int VEHICLE_ID = 4;

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {
		sURIMatcher.addURI(AUTHORITY, AbastecimentoData.URI_TYPE, ABASTECIMENTO);
		sURIMatcher.addURI(AUTHORITY, AbastecimentoData.URI_TYPE + "/#", ABASTECIMENTO_ID);
		sURIMatcher.addURI(AUTHORITY, VeiculoData.URI_TYPE, VEHICLE);
		sURIMatcher.addURI(AUTHORITY, VeiculoData.URI_TYPE + "/#", VEHICLE_ID);

	}

	/**
     * 
     */
	private DBHelper mDBHelper;

	@Override
	public boolean onCreate() {
		Log.d(TAG, "FuelSpentProvide.onCreate(): ");
		mDBHelper = new DBHelper(getContext());
		return false;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		// Log.d(TAG, "FuelSpentProvide.insert(): " + uri + ", values: " +
		// values);

		String tableName = null;

		int match = sURIMatcher.match(uri);

		switch (match) {

		case VEHICLE: {
			tableName = VeiculoData.TABLE_NAME;
			break;
		}
		
		case ABASTECIMENTO: {
			tableName = AbastecimentoData.TABLE_NAME;
			break;
		}

		}

		SQLiteDatabase db = mDBHelper.getWritableDatabase();

		try {

			long id = db.insertOrThrow(tableName, null, values);

			// the row ID of the newly inserted row, or -1 if an error occurred 
			if (id == -1) {
				throw new RuntimeException(
						String.format(
								"%s: Failed to insert [%s] to [%s] for unknown reasons.",
								TAG, values, uri)); //
			} else {
				return ContentUris.withAppendedId(uri, id);
			}

		} finally {
			db.close();
		}

	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		String tableName = null;

		// Log.d(TAG, "FuelSpentProvide.update(): " + uri);

		int match = sURIMatcher.match(uri);

		switch (match) {

		case VEHICLE: {
			tableName = VeiculoData.TABLE_NAME;
			break;
		}
		case VEHICLE_ID: {
			tableName = VeiculoData.TABLE_NAME;
			long id = this.getId(uri);
			if (id > 0) {
				selection = VeiculoData.VeiculoColumns._ID + "=" + id;
				selectionArgs = null;
			}
			break;
		}

		case ABASTECIMENTO: {
			tableName = AbastecimentoData.TABLE_NAME;
			break;
		}
		case ABASTECIMENTO_ID: {
			tableName = AbastecimentoData.TABLE_NAME;
			long id = this.getId(uri);
			if (id > 0) {
				selection = AbastecimentoData.AbastecimentoColumns._ID + "=" + id;
				selectionArgs = null;
			}
			break;
		}
		
		}

		SQLiteDatabase db = mDBHelper.getWritableDatabase();

		try {
			return db.update(tableName, values, selection, selectionArgs);
		} finally {
			db.close();
		}

	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		// Log.d(TAG, "FuelSpentProvide.delete(): " + uri);

		String tableName = null;

		int match = sURIMatcher.match(uri);

		switch (match) {

		case VEHICLE: {
			tableName = VeiculoData.TABLE_NAME;
			break;
		}
		case VEHICLE_ID: {
			tableName = VeiculoData.TABLE_NAME;
			long id = this.getId(uri);
			if (id > 0) {
				selection = VeiculoData.VeiculoColumns._ID + "=" + id;
				selectionArgs = null;
			}
			break;
		}
		
		case ABASTECIMENTO: {
			tableName = AbastecimentoData.TABLE_NAME;
			break;
		}
		case ABASTECIMENTO_ID: {
			tableName = AbastecimentoData.TABLE_NAME;
			long id = this.getId(uri);
			if (id > 0) {
				selection = AbastecimentoData.AbastecimentoColumns._ID + "=" + id;
				selectionArgs = null;
			}
			break;
		}


		}

		// Create or open a database
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		try {
			Log.d(TAG, "FuelSpentProvider.delete(): " + tableName + "/"
					+ selection + "/" + selectionArgs);
			return db.delete(tableName, selection, selectionArgs);
		} finally {
			db.close();
		}

	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		String tableName = null;

		int match = sURIMatcher.match(uri);

		// Log.d(TAG, "FuelSpentProvide.query(): " + uri+", match="+match);

		switch (match) {

		case VEHICLE: {
			tableName = VeiculoData.TABLE_NAME;
			long id = this.getId(uri);
			if (id > 0) {
				selection = VeiculoData.VeiculoColumns._ID + "=" + id;
				selectionArgs = null;
			}
			break;
		}

		case ABASTECIMENTO: {
			tableName = AbastecimentoData.TABLE_NAME;
			long id = this.getId(uri);
			if (id > 0) {
				selection = AbastecimentoData.AbastecimentoColumns._ID + "=" + id;
				selectionArgs = null;
			}
			break;
		}

		
		default: {
			Log.w(TAG, "FuelSpentProvide.query(): ERROR");
			return null;
		}

		}

		SQLiteDatabase db = mDBHelper.getReadableDatabase();

		return db.query(tableName, projection, selection, selectionArgs, null,
				null, sortOrder);

	}

	@Override
	public String getType(Uri uri) {

		int match = sURIMatcher.match(uri);

		// Log.d(TAG, "FuelSpentProvide.getType(): " + uri + ", match: " +
		// match);

		switch (match) {

		case VEHICLE:
			return VeiculoData.CONTENT_TYPE;
		case VEHICLE_ID:
			return VeiculoData.CONTENT_ITEM_TYPE;

		case ABASTECIMENTO:
			return AbastecimentoData.CONTENT_TYPE;
		case ABASTECIMENTO_ID:
			return AbastecimentoData.CONTENT_ITEM_TYPE;
			
			
		default:
			return null;
		}

	}

	/**
	 * Get id.
	 * 
	 * @param uri
	 *            Uri
	 * 
	 * @return -1 if error or id
	 * 
	 */
	private long getId(Uri uri) {

		// Log.d(TAG, "FuelSpentProvide.getUri(): " + uri);

		String lastPathSegment = uri.getLastPathSegment();

		if (lastPathSegment != null) {

			try {
				return Long.parseLong(lastPathSegment);
			} catch (NumberFormatException e) {
				Log.w(TAG, "FuelSpentProvider NumberFormatException() ["
						+ lastPathSegment + "]");
			}

		}

		return -1;

	}

}
