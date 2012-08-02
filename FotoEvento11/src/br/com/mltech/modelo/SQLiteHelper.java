
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
   * Define um array de comandos SQL que ser�o executados
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
   *          Contexto da aplica��o
   *          
   * @param nomeBanco
   *          Nome do banco de dados
   *          
   * @param versaoBanco
   *          Vers�o do banco de dados
   *          
   * @param scriptSQLCreate
   *          Array de comandos SQL usado na cria��o da tabela
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

    // executa cada sql passado como par�metro
    for (int i = 0; i < qtdeScripts; i++) {

      String sql = scriptSQLCreate[i];

      Log.i(TAG, sql);

      Log.d(TAG, "onCreate() - executando o comando(" + i + "): " + sql);

      try {
        // Cria o banco de dados executando o script de cria��o
        db.execSQL(sql);
      } catch (SQLException e) {
        Log.w(TAG, "onCreate() - Falha na execu��o do comando: " + sql, e);
      }

    }

  }

  @Override
  /**
   * A��o executada quando houver uma altera��o da vers�o da base de dados<br>
   * 
   * Mudou a vers�o ...
   */
  public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int novaVersao) {

    Log.w(TAG, "Atualizando da vers�o: " + versaoAntiga + " para " + novaVersao + ". Todos os registros ser�o apagados");
   
    try {
      
      // Deleta as tabelas ...
      db.execSQL(scriptSQLDelete);
      Log.d(TAG, "Executando comando: " + scriptSQLDelete);

    } catch (SQLException e) {
      
      Log.w(TAG, "onUpgrade() - Falha na execu��o do comando: " + scriptSQLDelete, e);
      
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
    Log.d(TAG,"Database vers�o: "+db.getVersion()+" aberta com sucesso.");
  }

}
