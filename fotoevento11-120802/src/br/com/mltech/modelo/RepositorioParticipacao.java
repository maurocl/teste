
package br.com.mltech.modelo;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

/**
 * RepositorioParticipacao
 * 
 * @author maurocl
 * 
 */
public class RepositorioParticipacao {

  //
  private static final String TAG = "RepositorioParticipacao";

  // Nome do banco de dados
  protected static final String NOME_BANCO = "fotoevento";

  // Nome da tabela
  protected static final String NOME_TABELA = "participacao";

  public static String[] colunas = new String[] {
      Participacao._ID, Participante.NOME, Participante.EMAIL, Participante.TELEFONE,
      Participacao.TIPO, Participacao.EFEITO, Participacao.ARQUIVO,
      Participante.PARAM1, Participante.PARAM2, Participante.PARAM3, Participante.PARAM4, Participante.PARAM5
  };

  /**
   * Base de dados
   */
  protected SQLiteDatabase db;

  /**
   * Cria o repositório
   * 
   * @param context
   *          Contexto da aplicação
   * 
   */
  public RepositorioParticipacao(Context context) {

    // Abre o banco de dados já existente
    db = context.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);

    if (db != null) {
      Log.d(TAG, "RepositorioParticipacao() - db.getVersion(): " + db.getVersion());
      Log.d(TAG, "RepositorioParticipacao() - db.isOpen(): " + db.isOpen());
    }

  }

  /**
   * Cria o repositório
   */
  public RepositorioParticipacao() {

    // apenas para criar uma subclasse

  }

  /**
   * Salva a participacao, insere um novo ou atualiza
   * 
   * @param participacao
   *          Participacao
   * 
   * @return O indetificador da participação ou -1 em caso de erro
   */
  public long salvar(Participacao participacao) {

    long id = participacao.getId();

    if (id != 0) {

      // atualiza a participação
      atualizar(participacao);

    } else {

      // insere uma nova participacao
      id = inserir(participacao);

    }

    // retorna o identificador da participação
    return id;

  }

  /**
   * Insere uma nova participacao
   * 
   * @param participacao
   *          Instância de Participacao
   * 
   * @return O id da nova linha inserida ou -1 em caso de erro
   * 
   */
  public long inserir(Participacao participacao) {

    Log.d(TAG, "inserir(Participacao) - participacao: " + participacao);

    ContentValues values = toContentValues(participacao);

    Log.d(TAG, "inserir(Participacao) - antes de chamar inserir !!!");

    // the row ID of the newly inserted row, or -1 if an error occurred 
    long id = inserir(values);

    if (id == -1) {
      Log.w(TAG, "inserir(Participacao) - falha na inserção do participante " + id);
    }
    else {
      participacao.setId(id);
      Log.d(TAG, "inserir(Participacao) - participante inserido com sucesso: " + id);
    }

    return id;

  }

  /**
   * Insere uma nova participacao
   * 
   * @param ContentValues
   * 
   * @return O id da nova linha inserida ou -1 em caso de erro
   */
  public long inserir(ContentValues contentValues) {

    Log.d(TAG, "inserir(ContentValues) - ContentValues: " + contentValues);

    // the row ID of the newly inserted row, or -1 if an error occurred 
    long id = db.insert(NOME_TABELA, "", contentValues);

    Log.d(TAG, "inserir(ContentValues) - Inserção: " + id);

    return id;

  }

  /**
   * Atualiza os dados de uma participação
   * 
   * @param p
   *          Instância de uma participação
   * 
   * @return
   */
  public int atualizar(Participacao p) {

    Log.d(TAG, "atualizar() -  ");

    ContentValues values = toContentValues(p);

    String _id = String.valueOf(p.getId());

    String where = Participacao._ID + "=?";
    String[] whereArgs = new String[] { _id };
    int count = atualizar(values, where, whereArgs);

    return count;
  }

  /**
   * Atualiza os valores
   * 
   * @param cv
   * @param where
   * @param whereArgs
   * 
   * @return
   * 
   */
  public int atualizar(ContentValues cv, String where, String[] whereArgs) {

    Log.d(TAG, "atualizar() -");

    int count = db.update(NOME_TABELA, cv, where, whereArgs);

    Log.i(TAG, "Atualizou [" + count + "] participantes");

    return count;

  }

  /**
   * Deleta uma participação a partir de seu identificador
   * 
   * @param id
   *          Identificador da participação
   * 
   * @return
   * 
   */
  public int deletar(long id) {

    Log.d(TAG, "deletar() - id: " + id);

    String where = Participacao._ID + "=?";

    String _id = String.valueOf(id);

    String[] whereArgs = new String[] { _id };

    int count = deletar(where, whereArgs);

    return count;
  }

  /**
   * Deleta registros de acordo com uma condição fornecida
   * 
   * @param where
   *          Cláusula where
   * @param whereArgs
   *          Argumentos da cláusula where
   * 
   * @return o nº de registros removidos
   * 
   */
  public int deletar(String where, String[] whereArgs) {

    Log.d(TAG, "deletar() - where: " + where);

    int count = db.delete(NOME_TABELA, where, whereArgs);

    Log.i(TAG, "Deletou [" + count + "] registros");

    return count;

  }

  /**
   * Busca informações sobre a Participação dado seu identificador
   * 
   * @param id
   *          Identificador da Participação
   * 
   * @return Uma instância de participação ou null caso não seja encontrada
   * 
   */
  public Participacao buscarParticipacao(long id) {

    Log.d(TAG, "buscarParticipacao() - id: " + id);

    Cursor c = db.query(true, NOME_TABELA, colunas, "_ID=" + id, null, null, null, null, null);

    if (c.getCount() > 0) {

      String[] nomeColunas = c.getColumnNames();

      for (String s : nomeColunas) {
        Log.d(TAG, "nomeColuna: " + s);
      }

      Log.d(TAG, "buscarParticipacao() - getCount(): " + c.getCount());

      // posiciona no primeiro elemento do cursor
      c.moveToFirst();

      Participacao participacao = new Participacao();

      // TODO preencher corretamente as informações do participante

      // cria um novo participante
      Participante participante = new Participante();

      participante.setNome(c.getString(c.getColumnIndex(Participante.NOME)));
      participante.setEmail(c.getString(c.getColumnIndex(Participante.EMAIL)));
      participante.setTelefone(c.getString(c.getColumnIndex(Participante.TELEFONE)));

      // cria uma instância de Parametros
      Parametros pp = new Parametros();

      // cria um array de string para conter cada valor
      String[] valores = new String[5];

      valores[0] = c.getString(c.getColumnIndex(Participante.PARAM1));
      valores[1] = c.getString(c.getColumnIndex(Participante.PARAM2));
      valores[2] = c.getString(c.getColumnIndex(Participante.PARAM3));
      valores[3] = c.getString(c.getColumnIndex(Participante.PARAM4));
      valores[4] = c.getString(c.getColumnIndex(Participante.PARAM5));

      // Associa os parâmetros ao Participante
      participante.setParametros(pp);

      int tipoFoto = -1;
      int efeitoFoto = -1;

      // Lê os dados
      participacao.setId(c.getLong(c.getColumnIndex(Participacao._ID)));

      //
      participacao.setParticipante(participante);

      participacao.setTipoFoto(tipoFoto);
      participacao.setEfeitoFoto(efeitoFoto);

      participacao.setNomeArqFoto(c.getString(c.getColumnIndex(Participacao.ARQUIVO)));

      // retorna instância da participação encontrada
      return participacao;

    }

    // id não encontrado. Retorna null.
    return null;

  }

  /**
   * Obtem um cursor
   * 
   * @return o resultado de uma query
   * 
   */
  public Cursor getCursor() {

    Log.d(TAG, "getCursor() ");

    if (db == null) {
      Log.w(TAG, "getCursor()- base de dados está nula");
      return null;
    }

    Log.d(TAG, "getCursor() - executando consulta na tabela: " + NOME_TABELA);
    Log.d(TAG, "getCursor() - colunas: " + colunas);

    // public Cursor query (
    //   String table, 
    //   String[] columns, 
    //   String selection, 
    //   String[] selectionArgs, 

    //   String groupBy, 
    //   String having, 
    //   String orderBy) 

    // table, columns, selection, selectionArgs, groupBy, having, orderBy 
    return db.query(NOME_TABELA, colunas, null, null, null, null, null);

  }

  /**
   * Obtem a lista de participações do evento
   * 
   * @return a lista de participações ou null em caso de algum erro ou lista
   *         vazia
   * 
   */
  public List<Participacao> listarParticipacoes() {

    Log.d(TAG, "listarParticipacoes()");

    Cursor c = getCursor();

    if (c == null) {
      Log.w(TAG, "listarParticipacoes() - cursor vazio");
      return null;
    }

    // exibe as informações do cursor retornado
    exibeInfoCursor(c);

    List<Participacao> participacoes = new ArrayList<Participacao>();

    if (c.moveToFirst()) {

      // Recupera os índices das colunas
      int idxId = c.getColumnIndex(Participacao._ID);

      int idxNome = c.getColumnIndex(Participante.NOME);
      int idxEmail = c.getColumnIndex(Participante.EMAIL);
      int idxTelefone = c.getColumnIndex(Participante.TELEFONE);

      int idxParam1 = c.getColumnIndex(Participante.PARAM1);
      int idxParam2 = c.getColumnIndex(Participante.PARAM2);
      int idxParam3 = c.getColumnIndex(Participante.PARAM3);
      int idxParam4 = c.getColumnIndex(Participante.PARAM4);
      int idxParam5 = c.getColumnIndex(Participante.PARAM5);

      int idxTipo = c.getColumnIndex(Participacao.TIPO);
      int idxEfeito = c.getColumnIndex(Participacao.EFEITO);
      int idxArquivo = c.getColumnIndex(Participacao.ARQUIVO);

      while (c.moveToNext()) {

        Participacao p = new Participacao(c.getLong(idxId), c.getString(idxNome), c.getString(idxEmail), c.getString(idxTelefone),
            c.getInt(idxTipo), c.getInt(idxEfeito), c.getString(idxArquivo), c.getString(idxParam1), c.getString(idxParam2),
            c.getString(idxParam3),
            c.getString(idxParam4), c.getString(idxParam5));

        // adiciona a nova participacao na lista
        participacoes.add(p);

      }

    } else {
      Log.d(TAG, "listarParticipacoes() - nenhuma participação foi encontrada");
    }

    // a lista de participacoes
    return participacoes;

  }

  /**
   * Exibe informações sobre a consulta realizada
   * 
   * @param c
   *          Cursor
   */
  public void exibeInfoCursor(Cursor c) {

    if (c == null) {
      return;
    }

    // nº de colunas do cursor
    int numColunas = c.getColumnCount();

    Log.d(TAG, "Nº de colunas: " + numColunas);

    // Obtém o nome das colunas
    String[] nomeColunas = c.getColumnNames();

    int i = 0;
    // exibe o nome das colunas    
    for (String colName : nomeColunas) {
      Log.v(TAG, "coluna(" + i + "):" + colName);
      i++;
    }

  }

  /**
   * Busca uma participação dado o nome do participante
   * 
   * @param nome
   *          Nome do participante
   * 
   * @return As informações do participante ou null caso ele não seja
   *         encontrado.
   * 
   */
  public Participacao buscarParticipacaoPorNome(String nome) {

    Log.d(TAG, "listarParticipacoes() - buscando pelo nome: " + nome);

    Participacao participacao = null;

    Cursor c = db.query(NOME_TABELA, colunas, Participante.NOME + "='" + nome + "'", null, null, null, null);

    if (c.moveToNext()) {
      // encontrou um nome

      // Recupera os índices das colunas
      int idxId = c.getColumnIndex(Participacao._ID);

      int idxNome = c.getColumnIndex(Participante.NOME);
      int idxEmail = c.getColumnIndex(Participante.EMAIL);
      int idxTelefone = c.getColumnIndex(Participante.TELEFONE);

      int idxParam1 = c.getColumnIndex(Participante.PARAM1);
      int idxParam2 = c.getColumnIndex(Participante.PARAM2);
      int idxParam3 = c.getColumnIndex(Participante.PARAM3);
      int idxParam4 = c.getColumnIndex(Participante.PARAM4);
      int idxParam5 = c.getColumnIndex(Participante.PARAM5);

      int idxTipo = c.getColumnIndex(Participacao.TIPO);
      int idxEfeito = c.getColumnIndex(Participacao.EFEITO);
      int idxArquivo = c.getColumnIndex(Participacao.ARQUIVO);

      participacao = new Participacao(c.getLong(idxId), 
          c.getString(idxNome), c.getString(idxEmail), c.getString(idxTelefone),
          c.getInt(idxTipo), c.getInt(idxEfeito), c.getString(idxArquivo), 
          c.getString(idxParam1), c.getString(idxParam2), c.getString(idxParam3),
          c.getString(idxParam4), c.getString(idxParam5));

    }

    return participacao;
    
  }

  /**
   * Busca um participante utilizando as configurações definidas no
   * SQLiteQueryBuilder
   */
  public Cursor query(SQLiteQueryBuilder queryBuilder, String[] projection, String selection, String[] selectionArgs,
      String groupBy, String having, String orderBy) {

    Cursor c = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, orderBy);

    return c;
  }

  /**
   * Fecha o banco de dados
   * 
   */
  public void fechar() {

    Log.d(TAG, "fechar()");

    // fecha o banco de dados caso esteja aberto
    if (db != null) {
      db.close();
    }

  }

  /**
   * Cria um ContentValues contendo todos os atributos de uma participacao
   * 
   * @param participacao
   *          Instância de uma Participacao
   * 
   * @return um object ContentValues ou null em caso de algum problema
   * 
   */
  public ContentValues toContentValues(Participacao participacao) {

    if (participacao == null) {
      Log.w(TAG, "Participacao é nula");
      return null;
    }

    ContentValues values = new ContentValues();

    // Obtem informações sobre o participante do evento
    Participante p1 = participacao.getParticipante();

    if (p1 == null) {
      // participante é nulo
      Log.w(TAG, "Participante é nulo");
      return null;
    }

    // armazena as informações do participantes
    values.put(Participante.NOME, p1.getNome());
    values.put(Participante.EMAIL, p1.getEmail());
    values.put(Participante.TELEFONE, p1.getTelefone());

    // TODO verificar a construção abaixo
    if (p1.getParametros() == null) {

      values.put(Participante.PARAM1, (String) null);
      values.put(Participante.PARAM2, (String) null);
      values.put(Participante.PARAM3, (String) null);
      values.put(Participante.PARAM4, (String) null);
      values.put(Participante.PARAM5, (String) null);

    }
    else {

      values.put(Participante.PARAM1, p1.getParametros().getParametro(0));
      values.put(Participante.PARAM2, p1.getParametros().getParametro(1));
      values.put(Participante.PARAM3, p1.getParametros().getParametro(2));
      values.put(Participante.PARAM4, p1.getParametros().getParametro(3));
      values.put(Participante.PARAM5, p1.getParametros().getParametro(4));

    }

    values.put(Participacao.TIPO, participacao.getTipoFoto());
    values.put(Participacao.EFEITO, participacao.getEfeitoFoto());
    values.put(Participacao.ARQUIVO, participacao.getNomeArqFoto());

    Log.d(TAG, "inserir() - antes de chamar inserir !!!");

    return values;

  }

  /**
   * 
   * @return
   */
  public SQLiteDatabase getSQLiteDatabase() {

    return db;
  }

  /**
   * Obtem o caminho de onde está armazenado o banco de dados
   * 
   * @return Retorna o caminho de onde está armazenado o banco de dados
   * 
   */
  public String getDbPath() {

    return db.getPath();
  }

  /**
   * Verifica se a base de dados está aberta
   * 
   * @return true se estiver aberta ou false caso contrário
   * 
   */
  public boolean isDbOpen() {

    return db.isOpen();
  }

  /**
   * obtém a versão atual da base de dados
   * 
   * @return a versão atual da base de dados
   */
  public int getDbVersion() {

    return db.getVersion();
  }

  /**
   * Atualiza a versão da base de dados
   * 
   * @param version
   *          Nova versão da base de dados
   * 
   */
  public void setDbVersion(int version) {

    db.setVersion(version);
  }

  /**
   * Incremente a versão atual do banco de dados
   * 
   * @return A nova versão da base de dados
   * 
   */
  public int incrementDbVersion() {

    int novaVersao = db.getVersion() + 1;

    db.setVersion(novaVersao);

    return novaVersao;

  }

}
