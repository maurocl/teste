package br.com.mltech.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import br.com.mltech.modelo.Categoria;

/**
 * 
 * @author maurocl
 * 
 */
public class CategoriaDAO extends SQLiteOpenHelper {

	private static final String TABELA = "Categoria";
	private static final int VERSAO = 1; // a versao tem que iniciar com 1

	/**
	 * CategoriaDAO
	 * 
	 * @param ctx
	 *            Contexto da aplicaÃ§Ã£o
	 * 
	 */
	public CategoriaDAO(Context ctx) {
		// contexto, nome da base de dados, fabrica de cursores, versÃ£o da base
		// de dados
		super(ctx, TABELA, null, VERSAO);

	}

	//---------------------------------------------------
	// esse método é executado quando a tabela for criada
	//---------------------------------------------------
	@Override
	/**
	 * 
	 */
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		String ddl = "Create table " + TABELA + " (id integer primary key, "
				+ " descricao text unique not null);";

		db.execSQL(ddl);

	}

	// esse método é chamado quando houver uma alteração da versão da tabela
	// por exemplo, quando alteramos a estrutura de uma tabela de uma versão
	// para outra da aplicação
	@Override
	/**
	 * 
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		String sql = "DROP TABLE IF EXISTS " + TABELA;
		db.execSQL(sql);
		// Cria-se a tabela novamente
		onCreate(db);

	}

	/**
	 * inserir
	 * 
	 * @param c
	 */
	public void inserir(Categoria c) {

		ContentValues valores = new ContentValues();

		valores.put("id", c.getCodCategoria());
		valores.put("descricao", c.getDescricao());

		// nome da tabela, valor inserido quando o valor nÃ£o foi fornecido,
		// valores
		getWritableDatabase().insert(TABELA, null, valores);

	}

	/**
	 * deletar
	 * 
	 * @param c
	 */
	public void deletar(Categoria c) {
		// tabela, where clause, ...

		int numLinhasRemovidas = getWritableDatabase().delete(TABELA, "id=?",
				new String[] { c.getCodCategoria().toString() });

		 Log.i("Categoria", numLinhasRemovidas + " foram removidas");

	}

	/**
	 * 
	 * @param c
	 */
	public void alterar(Categoria c) {

		ContentValues values = new ContentValues();

		values.put("id", c.getCodCategoria());
		values.put("descricao", c.getDescricao());

		getWritableDatabase().update(TABELA, values, "id=?",
				new String[] { c.getCodCategoria().toString() });

	}

	/**
	 * getLista()
	 * 
	 * @return
	 */
	public List<Categoria> getLista() {

		List<Categoria> categorias = new ArrayList<Categoria>();

		// TABELA, null, selection (clÃ¡usula), selectionArgs, groupBy, having,
		// orderBy
		Cursor c = getWritableDatabase().query(TABELA, null, null, null, null,
				null, "descricao");

		while (c.moveToNext()) {

			Categoria cat = new Categoria(c.getLong(0), c.getString(1));

			categorias.add(cat);

		}

		c.close();
		
		return categorias;

	}

	/**
	 * 
	 * @param c
	 */
	public void saveOrUpdate(Categoria c) {

		if (c.getCodCategoria() != null) {
			this.alterar(c);
		} else {
			this.inserir(c);
		}

	}

}
