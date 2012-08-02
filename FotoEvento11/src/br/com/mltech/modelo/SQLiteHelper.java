
package br.com.mltech.modelo;

import android.content.Context;
import android.database.SQLException;
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
   *          Contexto da aplicação
   *          
   * @param nomeBanco
   *          Nome do banco de dados
   *          
   * @param versaoBanco
   *          Versão do banco de dados
   *          
   * @param scriptSQLCreate
   *          Array de comandos SQL usado na criação da tabela
   *          
   * @param scriptSQLDelete
   *          Comando SQL usado para recriar a tabela
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

      Log.d(TAG, "onCreate() - executando o comando(" + i + "): " + sql);

      try {
        // Cria o banco de dados executando o script de criação
        db.execSQL(sql);
      } catch (SQLException e) {
        Log.w(TAG, "onCreate() - Falha na execução do comando: " + sql, e);
      }

    }

  }

  @Override
  /**
   * Ação executada quando houver uma alteração da versão da base de dados<br>
   * 
   * Mudou a versão ...
   */
  public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int novaVersao) {

    Log.w(TAG, "Atualizando da versão: " + versaoAntiga + " para " + novaVersao + ". Todos os registros serão apagados");
   
    try {
      
      // Deleta as tabelas ...
      db.execSQL(scriptSQLDelete);
      Log.d(TAG, "Executando comando: " + scriptSQLDelete);

    } catch (SQLException e) {
      
      Log.w(TAG, "onUpgrade() - Falha na execução do comando: " + scriptSQLDelete, e);
      
    }

    // Cria novamente...
    onCreate(db);

  }
  
  @Override
  /**
   * 
   */
  public void onOpen(SQLiteDatabase db) {
    super.onOpen(db);
    Log.d(TAG,"Database versão: "+db.getVersion()+" aberta com sucesso.");
  }

}
