
package br.com.mltech.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import br.com.mltech.modelo.Usuario;

/**
 * 
 * @author maurocl
 * 
 */
public class UsuarioDAO extends SQLiteOpenHelper {

  private static final String TAG = "UsuarioDAO";

  private static final String TABELA = "Usuario";

  private static final int VERSAO = 1; // a versao tem que iniciar com 1

  /**
   * UsuarioDAO
   * 
   * @param ctx
   *          Contexto da aplica��o
   * 
   */
  public UsuarioDAO(Context ctx) {

    // contexto, nome da base de dados, fabrica de cursores, vers�o da base de dados
    super(ctx, TABELA, null, VERSAO);

  }

  //---------------------------------------------------
  // esse m�todo � executado quando a tabela for criada
  //---------------------------------------------------
  @Override
  /** 
   * esse m�todo � executado quando a tabela for criada
   */
  public void onCreate(SQLiteDatabase db) {

    String ddl = "Create table " + TABELA + " (_id integer primary key, "
        + " username text unique not null, password text);";

    // executa a query
    db.execSQL(ddl);

  }

  @Override
  /**
   *  esse m�todo � chamado quando houver uma altera��o da vers�o da tabela
   *  por exemplo, quando alteramos a estrutura de uma tabela de uma vers�o
   *  para outra da aplica��o
   *  
   */
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    String sql = "DROP TABLE IF EXISTS " + TABELA;

    // Executa o comando para apagar a tabela
    db.execSQL(sql);

    // Cria-se a tabela novamente
    onCreate(db);

  }

  /**
   * Insere um novo usu�rio
   * 
   * @param usuario
   *          Usuario
   */
  public void inserir(Usuario usuario) {

    ContentValues valores = toContentValues(usuario);

    // nome da tabela, valor inserido quando o valor nao foi fornecido, valores
    getWritableDatabase().insert(TABELA, null, valores);

  }

  /**
   * Remove um usu�rio
   * 
   * @param usuario
   *          Usuario
   * 
   */
  public void deletar(Usuario usuario) {

    // tabela, where clause, ...

    int numLinhasRemovidas = getWritableDatabase().delete(TABELA, "id=?",
        new String[] { usuario.getId().toString() });

    Log.i(TAG, numLinhasRemovidas + " foram removidas");

  }

  /**
   * Remove o usu�rio dado seu identificador
   * 
   * @param id
   *          Identificador do usu�rio
   * 
   */
  public void deletar(Long id) {

    // tabela, where clause, ...

    int numLinhasRemovidas = getWritableDatabase().delete(TABELA, "_id=?",
        new String[] { String.valueOf(id) });

    Log.i(TAG, numLinhasRemovidas + " foram removidas");

  }

  /**
   * Altera os dados de um usu�rio
   * 
   * @param usuario
   *          Usuario
   * 
   */
  public void alterar(Usuario usuario) {

    ContentValues values = toContentValues(usuario);

    getWritableDatabase().update(TABELA, values, "id=?",
        new String[] { usuario.getId().toString() });

  }

  /**
   * Obt�m a lista de usu�rios
   * 
   * @return A lista de usu�rios ou null caso a tabela esteja vazia
   * 
   */
  public List<Usuario> getLista() {

    List<Usuario> usuarios = new ArrayList<Usuario>();

    // TABELA, null, selection (clausula), selectionArgs, groupBy, having, orderBy
    Cursor c = getWritableDatabase().query(TABELA, null, null, null, null, null, "username");

    while (c.moveToNext()) {

      Usuario usuario = new Usuario(c.getLong(0), c.getString(1), c.getString(2));

      // adiciona o usu�rio a lista
      usuarios.add(usuario);

    }

    // Fecha o cursor
    c.close();

    // retorn a lista de usu�rios
    return usuarios;

  }

  /**
   * Salva ou altera um us�rio
   * 
   * @param usuario
   *          Usuario
   * 
   */
  public void saveOrUpdate(Usuario usuario) {

    if (usuario.getId() != null) {
      this.alterar(usuario);
    } else {
      this.inserir(usuario);
    }

  }

  /**
   * Preenche um ContentValues com informa��es de um usu�rio
   * 
   * @param usuario
   *          Usuario
   * 
   * @return Uma inst�ncia de ContentValues preenchida com as informa��es do
   *         usu�rio
   */
  public ContentValues toContentValues(Usuario usuario) {

    ContentValues values = new ContentValues();

    values.put("username", usuario.getUsername());
    values.put("password", usuario.getPassword());

    return values;

  }

}
