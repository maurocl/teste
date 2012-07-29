package br.com.mltech.modelo;

import android.content.Context;

public class RepositorioParticipacaoScript extends RepositorioParticipacao {

	private static final String SCRIPT_DATABASE_DELETE = "DROP TABLE IF EXISTS participacao";

	private static final String[] SCRIPT_DATABASE_CREATE = new String[] {

	"CREATE TABLE participacao ( _id INTEGER PRIMARY KEY, nome text, email text, telefone text, tipoFoto text, tipoEfeito text, nomeArqFoto text, param1 TEXT, param2 TEXT, param3 TEXT, param4 TEXT, param5 TEXT);"

	};

	// nome do banco
	private static final String NOME_BANCO = "fotoevento";

	// controle de versão
	private static final int VERSAO_BANCO = 5;

	// nome da tabela
	// private static final String TABELA_PARTICIPACAO = "participacao";

	// Classe utilitária para abrir, criar, e atualizar o banco de dados
	private SQLiteHelper dbHelper;

	// Cria o banco de dados com um script SQL
	public RepositorioParticipacaoScript(Context ctx) {

		// Criar usando um script SQL
		dbHelper = new SQLiteHelper(ctx, RepositorioParticipacaoScript.NOME_BANCO, RepositorioParticipacaoScript.VERSAO_BANCO,
				RepositorioParticipacaoScript.SCRIPT_DATABASE_CREATE, RepositorioParticipacaoScript.SCRIPT_DATABASE_DELETE);

		// Abre o banco no modo escrita para poder alterar também
		db = dbHelper.getWritableDatabase();

	}

	// Fecha o banco
	@Override
	public void fechar() {

		super.fechar();

		if (dbHelper != null) {
			dbHelper.close();
		}

	}

}
