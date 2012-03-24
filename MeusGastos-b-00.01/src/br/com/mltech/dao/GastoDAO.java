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
import br.com.mltech.modelo.Gasto;

/**
 * GastoDAO.java
 * 
 * @author maurocl
 * 
 */
public class GastoDAO extends SQLiteOpenHelper {

	private static final String TABELA = "Gasto";
	
	private static final int VERSAO = 1; // a versao tem que iniciar com 1

	/**
	 * GastoDAO
	 * 
	 * @param ctx
	 *          Contexto da aplicação
	 * 
	 */
	public GastoDAO(Context ctx) {
		// contexto, nome da base de dados, fabrica de cursores, versão da base
		// de dados
		super(ctx, TABELA, null, VERSAO);

	}

	// ---------------------------------------------------
	// esse método é executado quando a tabela for criada
	// ---------------------------------------------------
	@Override
	/**
	 * onCreate() 
	 */
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		StringBuilder sb = new StringBuilder();
		
		sb.append("Create table ").append(TABELA);
		
		sb.append("(");
		
		sb.append("id integer primary key").append(",");
		sb.append("data text not null").append(",");
		sb.append("descricao text not null").append(",");
		sb.append("valor double").append(",");
		sb.append("id_categoria integer");
		
		sb.append(")");
		
		//String ddl = "Create table " + TABELA + " (id integer primary key, " + " descricao text unique not null);";

		db.execSQL(sb.toString());

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
	 * @param g
	 */
	public void inserir(Gasto g) {

		ContentValues valores = new ContentValues();

		valores.put("id",           g.getCodGasto());
		valores.put("data",         g.getData());		
		valores.put("descricao",    g.getDescricao());
		valores.put("valor",        g.getValor());
		valores.put("id_categoria", g.getCategoria().getCodCategoria());

		// nome da tabela, valor inserido quando o valor não for fornecido, valores
		getWritableDatabase().insert(TABELA, null, valores);
		
		Log.i("GastoDAO",g.getData());
		Log.i("GastoDAO",g.toString());

	}

	/**
	 * deletar
	 * 
	 * @param g
	 */
	public void deletar(Gasto g) {
		// tabela, where clause, ...

		int numLinhasRemovidas = getWritableDatabase().delete(TABELA, "id=?", new String[] { g.getCodGasto().toString() });

		Log.i("Gasto", numLinhasRemovidas + " foram removidas");

	}

	/**
	 * alterar(Gasto g)
	 * 
	 * @param g
	 */
	public void alterar(Gasto g) {

		ContentValues valores = new ContentValues();

		valores.put("id",           g.getCodGasto());
		valores.put("descricao",    g.getDescricao());
		valores.put("data",         g.getData());
		valores.put("valor",        g.getValor());
		valores.put("id_categoria", g.getCategoria().getCodCategoria());
		
		getWritableDatabase().update(TABELA, valores, "id=?", new String[] { g.getCodGasto().toString() });

	}

	/**
	 * getLista()
	 * 
	 * Retorna uma lista com todos os Gastos
	 * 
	 * @return
	 */
	public List<Gasto> getLista() {

		List<Gasto> Gastos = new ArrayList<Gasto>();

		// TABELA, null, selection (cláusula), selectionArgs, groupBy, having, orderBy
		Cursor cursor = getWritableDatabase().query(TABELA, null, null, null, null, null, "data");

		while (cursor.moveToNext()) {

			// Gasto(Long codGasto, String data, String descricao, double valor, Categoria categoria) 
			
			
			// aqui é necessário a criação de uma instância da classe Categoria
			Categoria c = new Categoria(0L, (cursor.getInt(4))+"");
			
			Gasto gasto = new Gasto(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3), c);

			Gastos.add(gasto);

		}

		cursor.close();

		return Gastos;

	}

	/**
	 * saveOrUpdate
	 * 
	 * @param g
	 */
	public void saveOrUpdate(Gasto g) {

		if (g.getCodGasto() != null) {
			this.alterar(g);
		} else {
			this.inserir(g);
		}

	}

}
