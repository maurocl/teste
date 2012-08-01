
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

  private static final String TAG = "CategoriaDAO";

  private static final String TABELA = "Categoria";

  private static final int VERSAO = 4; // a versao tem que iniciar com 1

  /**
   * CategoriaDAO
   * 
   * @param ctx
   *          Contexto da aplicação
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

    String ddl = "Create table " + TABELA + " (id integer primary key, "
        + " descricao text unique not null, xxx text);";

    Log.d(TAG, "criando a tabela: " + TABELA);

    db.execSQL(ddl);

    Log.d(TAG, TABELA + " criada com sucesso");

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

    Log.d(TAG, "Removendo a tabela: " + TABELA + " - versão: " + oldVersion + " - nova versão: " + newVersion);

    db.execSQL(sql);

    Log.d(TAG, "Tabela " + TABELA + " removida com sucesso");

    // Cria-se a tabela novamente
    onCreate(db);

  }

  @Override
  public void onOpen(SQLiteDatabase db) {

    super.onOpen(db);

    Log.d(TAG, "onOpen() path(): " + db.getPath());
    Log.d(TAG, "onOpen() version(): " + db.getVersion());

  }

  /**
   * Insere uma categoria
   * 
   * @param c
   *          Categoria
   */
  public void inserir(Categoria c) {

    ContentValues valores = toContentValues(c);

    // nome da tabela, valor inserido quando o valor não foi fornecido,
    // valores
    long id = getWritableDatabase().insert(TABELA, null, valores);

    Log.d(TAG, "inserir() - id: " + id);

    c.setId(id);

  }

  /**
   * @param c
   * @return
   */
  private ContentValues toContentValues(Categoria c) {

    ContentValues valores = new ContentValues();

    //valores.put("id", c.getCodCategoria());
    valores.put("descricao", c.getDescricao());

    return valores;

  }

  /**
   * Remove uma categoria
   * 
   * @param c
   *          Categoria
   */
  public void deletar(Categoria c) {

    // tabela, where clause, ...

    int numLinhasRemovidas = getWritableDatabase().delete(TABELA, "id=?",
        new String[] { c.getId().toString() });

    Log.i("Categoria", numLinhasRemovidas + " foram removidas");

  }

  /**
   * Atualiza Categotria
   * 
   * @param c
   *          Categoria
   */
  public void alterar(Categoria c) {

    ContentValues values = toContentValues(c);

    getWritableDatabase().update(TABELA, values, "id=?",
        new String[] { c.getId().toString() });

  }

  /**
   * Retorna a lista de Categorias
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
   * Salva ou atualiza uma categoria
   * 
   * @param c
   *          Categoria
   * 
   */
  public void saveOrUpdate(Categoria c) {

    if (c.getId() != null) {
      this.alterar(c);
    } else {
      this.inserir(c);
    }

  }

  /**
   * 
   * @return
   */
  public SQLiteDatabase getDb() {

    return getWritableDatabase();

  }

}
