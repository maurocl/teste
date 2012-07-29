package br.com.mltech.modelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * 
 *
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String TAG = "SQLiteHelper";

	/**
	 * Define um array de comandos SQL que serão executados
	 */
	private String[] scriptSQLCreate;

	/**
	 * Define o script SQL usado para apagar a tabela
	 */
	private String scriptSQLDelete;

	/**
	 * Constroi um novo SQLiteHelper
	 * 
	 * @param context
	 * @param nomeBanco
	 * @param versaoBanco
	 * @param scriptSQLCreate
	 * @param scriptSQLDelete
	 * 
	 */
	public SQLiteHelper(Context context, String nomeBanco, int versaoBanco, String[] scriptSQLCreate, String scriptSQLDelete) {

		super(context, nomeBanco, null, versaoBanco);

		this.scriptSQLCreate = scriptSQLCreate;
		
		this.scriptSQLDelete = scriptSQLDelete;

	}

	@Override
	/**
	 * Cria novo banco ...
	 */
	public void onCreate(SQLiteDatabase db) {

		Log.i(TAG, "Criando banco com SQL ...");

		int qtdeScripts = scriptSQLCreate.length;

		// executa cada sql passado como parâmetro
		for (int i = 0; i < qtdeScripts; i++) {
			String sql = scriptSQLCreate[i];
			Log.i(TAG, sql);
			// Cria o banco de dados executando o script de criação
			db.execSQL(sql);
		}

	}

	@Override
	/**
	 * Mudou a versão ...
	 */
	public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int novaVersao) {

		Log.w(TAG, "Atualizando da versão: " + versaoAntiga + " para " + novaVersao + ". Todos os registros serão apagados");

		Log.i(TAG, scriptSQLDelete);

		// Deleta as tabelas ...
		db.execSQL(scriptSQLDelete);

		// Cria novamente...
		onCreate(db);

	}

}
