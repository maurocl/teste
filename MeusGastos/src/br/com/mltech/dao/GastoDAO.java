
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

  private static final String TAG = "GastoDAO";

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

    StringBuilder sb = new StringBuilder();

    Log.d(TAG, "criando a tabela: " + TABELA);

    sb.append("Create table ").append(TABELA);

    sb.append("(");

    sb.append("id integer primary key").append(",");
    sb.append("data text not null").append(",");
    sb.append("descricao text not null").append(",");
    sb.append("valor double").append(",");
    sb.append("id_categoria integer");

    sb.append(")");

    //String ddl = "Create table " + TABELA + " (id integer primary key, " + " descricao text unique not null);";

    // Executa comando SQL para criação da tabela
    db.execSQL(sb.toString());

    Log.d(TAG, TABELA + " criada com sucesso");

  }

  /**
   * esse método é chamado quando houver uma alteração da versão da tabela por
   * exemplo, quando alteramos a estrutura de uma tabela de uma versão para
   * outra da aplicação
   */
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    String sql = "DROP TABLE IF EXISTS " + TABELA;

    Log.d(TAG, "Removendo a tabela: " + TABELA + " - versão: " + oldVersion + " - nova versão: " + newVersion);

    // Executa comando SQL para remover a tabela
    db.execSQL(sql);

    Log.d(TAG, "Tabela " + TABELA + " removida com sucesso");

    // Cria-se a tabela novamente
    onCreate(db);

  }

  /**
   * Insere um novo gasto
   * 
   * @param gasto
   *          gasto
   * 
   */
  public void inserir(Gasto gasto) {

    ContentValues valores = toContentValues(gasto);

    // nome da tabela, valor inserido quando o valor não for fornecido, valores
    long rowId = getWritableDatabase().insert(TABELA, null, valores);

    gasto.setId(rowId);

    if (rowId != -1) {
      Log.d(TAG, gasto + " inserido com sucesso");
    }
    else {
      Log.w(TAG, "Falha na inserção do gasto");
    }

  }

  /**
   * deletar
   * 
   * @param g
   *          Gasto
   */
  public void deletar(Gasto g) {

    // tabela, where clause, ...

    int numLinhasRemovidas = getWritableDatabase().delete(TABELA, "id=?", new String[] { g.getId().toString() });

    Log.i(TAG, numLinhasRemovidas + " foram removidas da tabela: " + TABELA);

  }

  /**
   * Remove o gasto dado seu identificador
   * 
   * @param id
   *          Identificador do gasto
   * 
   */
  public void deletar(Long id) {

    // tabela, where clause, ...

    int numLinhasRemovidas = getWritableDatabase().delete(TABELA, "id=?", new String[] { String.valueOf(id) });

    Log.i(TAG, numLinhasRemovidas + " foram removidas");

  }

  /**
   * Altera o gasto
   * 
   * @param g
   *          Gasto
   * 
   */
  public void alterar(Gasto g) {

    ContentValues valores = toContentValues(g);

    getWritableDatabase().update(TABELA, valores, "id=?", new String[] { g.getId().toString() });

    Log.i(TAG, "tabela alterada com sucesso");

  }

  /**
   * Transforma um gasto em um ContentValues
   * 
   * @param gasto
   *          Um gastos
   * 
   * @return um ContentValues
   */
  private ContentValues toContentValues(Gasto gasto) {

    ContentValues valores = new ContentValues();

    valores.put("descricao", gasto.getDescricao());
    valores.put("data", gasto.getData());
    valores.put("valor", gasto.getValor());
    valores.put("id_categoria", gasto.getCategoria().getId());

    return valores;

  }

  /**
   * Obtém a lista de todos os gastos
   * 
   * @return Retorna uma lista com todos os Gastos
   */
  public List<Gasto> getLista() {

    List<Gasto> Gastos = new ArrayList<Gasto>();

    // TABELA, null, selection (cláusula), selectionArgs, groupBy, having, orderBy
    Cursor cursor = getWritableDatabase().query(TABELA, null, null, null, null, null, "data");

    while (cursor.moveToNext()) {

      // Gasto(Long codGasto, String data, String descricao, double valor, Categoria categoria) 

      // aqui é necessário a criação de uma instância da classe Categoria
      Categoria c = new Categoria(0L, String.valueOf(cursor.getInt(4)));

      Gasto gasto = new Gasto(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3), c);

      // adiciona um novo Gasto a lista
      Gastos.add(gasto);

    }

    cursor.close();

    return Gastos;

  }

  /**
   * Obtém a lista de todos os gastos
   * 
   * @return Retorna uma lista com os Gastos compreendidos entre a data inicial
   *         e data final
   * 
   */
  public List<Gasto> getListaPeriodo(String data1, String data2) {

    List<Gasto> Gastos = new ArrayList<Gasto>();

    // TABELA, null, selection (cláusula), selectionArgs, groupBy, having, orderBy
    Cursor cursor = getWritableDatabase().query(TABELA, null, null, null, null, null, "data");

    while (cursor.moveToNext()) {

      // Gasto(Long codGasto, String data, String descricao, double valor, Categoria categoria) 

      // aqui é necessário a criação de uma instância da classe Categoria
      Categoria c = new Categoria(0L, String.valueOf(cursor.getInt(4)));

      Gasto gasto = new Gasto(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3), c);

      // adiciona um novo Gasto a lista
      Gastos.add(gasto);

    }

    cursor.close();

    return Gastos;

  }

  /**
   * saveOrUpdate
   * 
   * @param gasto
   */
  public void saveOrUpdate(Gasto gasto) {

    if (gasto.getId() != null) {
      this.alterar(gasto);
    } else {
      this.inserir(gasto);
    }

  }

}
