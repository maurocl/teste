package br.com.mltech.kmcontrol.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import br.com.mltech.kmcontrol.data.AbastecimentoData;
import br.com.mltech.kmcontrol.data.VeiculoData;

/**
 * Helper classes for SQLiteOpenHelper
 * 
 * This class is responsible for create and update SQL tables inside database.
 * 
 *
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();
    
    private static final String DB_NAME = "km_control.db";
    
    private static final int DB_VERSION = 1;

   /**
    * Constructor 
    * 
    * @param context Application's context
    * 
    */
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        
        Log.d(TAG, "Creating Database "+DB_NAME+ " version "+DB_VERSION);

        // create Vehicle table
        Log.d(TAG, "Creating table "+VeiculoData.TABLE_NAME+" ...");
        Log.d(TAG, ""+VeiculoData.getCreateVehicleTableSQL());
        db.execSQL(VeiculoData.getCreateVehicleTableSQL());
        
        
        // create Abastecimento table
        Log.d(TAG, "Creating table "+AbastecimentoData.TABLE_NAME+" ...");
        Log.d(TAG, ""+AbastecimentoData.getCreateVehicleTableSQL());
        db.execSQL(AbastecimentoData.getCreateVehicleTableSQL());

        
    }   

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    }
    
}