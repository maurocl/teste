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
	private static final String NOME_BANCO = "fotoevento";

	// Nome da tabela
	private static final String NOME_TABELA = "participacao";

	public static String[] colunas = new String[] { Participacao._ID, Participante.NOME, Participante.EMAIL, Participante.TELEFONE,
			Participacao.TIPO, Participacao.EFEITO, Participante.PARAM1, Participante.PARAM2, Participante.PARAM3, Participante.PARAM4,
			Participante.PARAM5 };

	protected SQLiteDatabase db;

	/**
	 * 
	 * @param context
	 */
	public RepositorioParticipacao(Context context) {
		// Abre o banco de dados já existente
		db = context.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
	}

	/**
	 * 
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
	 * @return
	 */
	public long salvar(Participacao participacao) {

		long id = participacao.getId();

		if (id != 0) {
			atualizar(participacao);
		} else {
			// insere uma nova participacao
			id = inserir(participacao);

		}
		return id;
	}

	/**
	 * Insere uma nova participacao
	 * 
	 * @param p
	 *          Instância de Participacao
	 * 
	 * @return O id da inserção ou ... ?
	 */
	public long inserir(Participacao p) {

		ContentValues values = new ContentValues();

		if(p==null) {
			Log.w(TAG,"Participacao é nula");
			return -1;
		}
		
		Participante p1 = p.getParticipante();
		
		if(p1==null) {
			Log.w(TAG,"Participante é nulo");
			return -2;
		}
		
		values.put(Participante.NOME, p1.getNome());
		values.put(Participante.EMAIL, p1.getEmail());
		values.put(Participante.TELEFONE, p1.getTelefone());

		// TODO verificar a construção abaixo
		if (p1.getParametros() == null) {
			// cria um nova instância de Parametros
			Parametros pp = new Parametros();
			for (int j = 0; j < 5; j++) {
				pp.setParametro(j, null);
			}
			p1.setParametros(pp);
		}
		
		values.put(Participacao.TIPO, p.getTipoFoto());
		values.put(Participacao.EFEITO, p.getEfeitoFoto());

		// TODO verificar a construção abaixo
		if (p1.getParametros() == null) {
			// cria um nova instância de Parametros
			Parametros pp = new Parametros();
			for (int j = 0; j < 5; j++) {
				pp.setParametro(j, null);
			}
			p.getParticipante().setParametros(pp);
		}

		/*
		values.put(Participante.PARAM1, p.getParticipante().getParametros().getParametro(0));
		values.put(Participante.PARAM2, p.getParticipante().getParametros().getParametro(1));
		values.put(Participante.PARAM3, p.getParticipante().getParametros().getParametro(2));
		values.put(Participante.PARAM4, p.getParticipante().getParametros().getParametro(3));
		values.put(Participante.PARAM5, p.getParticipante().getParametros().getParametro(4));
*/
		
		Log.d(TAG, "inserir() - antes de chamar inserir !!!");
		
		long id = inserir(values);

		Log.d(TAG, "inserir() - Inserção " + id);

		return id;

	}

	/**
	 * Insere uma nova participacao
	 * 
	 * @param ContentValues
	 * 
	 * @return O id da inserção ou ... ?
	 */
	public long inserir(ContentValues contentValues) {

		Log.d(TAG, "inserir() - ContentValues: " + contentValues);
		
		long id = db.insert(NOME_TABELA, "", contentValues);

		Log.d(TAG, "inserir() - Inserção " + id);

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

		return -1;
	}

	/**
	 * 
	 * @param cv
	 * @param where
	 * @param whereArgs
	 * @return
	 */
	public int atualizar(ContentValues cv, String where, String[] whereArgs) {
		Log.d(TAG, "atualizar() -");
		return -1;

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

		return -1;
	}

	/**
	 * Deleta registros de acordo com uma condição fornecida
	 * 
	 * @param where
	 *          Cláusula where
	 * @param whereArgs
	 *          Argumentos da cláusula where
	 * 
	 * @return
	 * 
	 */
	public int deletar(String where, String[] whereArgs) {

		Log.d(TAG, "deletar() - where: " + where);

		return -1;

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

		Log.d(TAG,"buscarParticipacao() - id: "+id);
		
		Cursor c = db.query(true, NOME_TABELA, colunas, "_ID=" + id, null, null, null, null, null);

		if (c.getCount() > 0) {
			
			String[] nomeColunas = c.getColumnNames();
			
			for(String s: nomeColunas) {
				Log.d(TAG,"nomeColuna: "+s);
			}
			
			
			
			Log.d(TAG,"buscarParticipacao() - getCount(): "+c.getCount());
			
			// posiciona no primeiro elemento do cursor
			c.moveToFirst();

			Participacao participacao = new Participacao();

			// TODO preencher corretamente as informações do participante
			
			// cria um novo participante
			Participante participante = new Participante();

			participante.setNome(c.getString(c.getColumnIndex(Participante.NOME)));
			participante.setEmail(c.getString(c.getColumnIndex(Participante.EMAIL)));
			participante.setTelefone(c.getString(c.getColumnIndex(Participante.TELEFONE)));
			
			Parametros pp = new Parametros();
			
			String[] valores = new String[5];
			valores[0] = c.getString(c.getColumnIndex(Participante.PARAM1));
			valores[1] = c.getString(c.getColumnIndex(Participante.PARAM2));
			valores[2] = c.getString(c.getColumnIndex(Participante.PARAM3));
			valores[3] = c.getString(c.getColumnIndex(Participante.PARAM4));
			valores[4] = c.getString(c.getColumnIndex(Participante.PARAM5));
			
			participante.setParametros(pp);
			


			
			int tipoFoto = -1;
			int efeitoFoto = -1;
			

			// Lê os dados
			participacao.setId(c.getLong(c.getColumnIndex(Participacao._ID)));
						
			participacao.setParticipante(participante);
			
			participacao.setTipoFoto(tipoFoto);
			participacao.setEfeitoFoto(efeitoFoto);
			participacao.setNomeArqFoto(c.getString(c.getColumnIndex(Participacao.ARQUIVO)));

			// retorna as informações da participação.
			return participacao;

		}

		// id não encontrado. Retorna null.
		return null;
		
	}

	/**
	 * Obtem um cursor
	 * 
	 * @return o resultado de uma query
	 */
	public Cursor getCursor() {

		Log.d(TAG, "getCursor() ");

		return db.query(NOME_TABELA, colunas, null, null, null, null, null);

	}

	/**
	 * Obtem a lista de participações do evento
	 * 
	 * @return a lista de participações
	 * 
	 */
	public List<Participacao> listarParticipacoes() {

		Log.d(TAG, "listarParticipacoes()");

		Cursor c = getCursor();

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

			do {

				// Cria um novo participante
				Participante participante = new Participante(c.getString(idxNome), c.getString(idxEmail), c.getString(idxTelefone));

				String[] pp = new String[] { c.getString(idxParam1), c.getString(idxParam2), c.getString(idxParam3),
						c.getString(idxParam4), c.getString(idxParam5) };

				Parametros pr = new Parametros(pp);

				participante.setParametros(pr);

				int tipoFoto = c.getInt(idxTipo);

				int efeitoFoto = c.getInt(idxEfeito);

				String nomeArqFoto = c.getString(idxArquivo);

				Participacao p = new Participacao(participante, tipoFoto, efeitoFoto, nomeArqFoto);

				p.setId(c.getLong(idxId));

				// adiciona a nova participacao na lista
				participacoes.add(p);

			} while (c.moveToNext());

		} else {
			Log.d(TAG, "listarParticipacoes() - nenhuma participação foi encontrada");
		}

		// a lista de participacoes
		return participacoes;

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

		return null;
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

}
