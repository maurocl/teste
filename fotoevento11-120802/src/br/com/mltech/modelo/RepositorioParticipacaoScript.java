
package br.com.mltech.modelo;

import android.content.Context;

/**
 * 
 * 
 *
 */
public class RepositorioParticipacaoScript extends RepositorioParticipacao {

  // Script para execução de um drop na tabela
  private static final String SCRIPT_DATABASE_DELETE = "DROP TABLE IF EXISTS "+NOME_TABELA;

  // Script de comandos SQL
  private static final String[] SCRIPT_DATABASE_CREATE = new String[] {

      "CREATE TABLE participacao ( _id INTEGER PRIMARY KEY, nome text, email text, telefone text, tipoFoto text, tipoEfeito text, nomeArqFoto text, param1 TEXT, param2 TEXT, param3 TEXT, param4 TEXT, param5 TEXT);"

  };

  // controle de versão
  private static final int VERSAO_BANCO = 6;

  // Classe utilitária para abrir, criar, e atualizar o banco de dados
  private SQLiteHelper dbHelper;

 
  /**
   * Cria o banco de dados com um script SQL
   * 
   * @param ctx Contexto da aplicação
   * 
   */
  public RepositorioParticipacaoScript(Context ctx) {

    // Criar usando um script SQL
    dbHelper = new SQLiteHelper(ctx, NOME_BANCO, VERSAO_BANCO, SCRIPT_DATABASE_CREATE, SCRIPT_DATABASE_DELETE);

    // Abre o banco no modo escrita para poder ler e alterá-lo
    db = dbHelper.getWritableDatabase();

  }
 
  /**
   * Fecha o banco
   */
  @Override
  public void fechar() {

    super.fechar();

    if (dbHelper != null) {
      dbHelper.close();
    }

  }

}
