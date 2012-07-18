package br.com.mltech;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.com.mltech.modelo.Evento;
import br.com.mltech.modelo.Participacao;
import br.com.mltech.modelo.Participante;

/**
 * RelatorioActivity<br>
 * 
 * Essa activity � respons�vel pela gera��o de relat�rios de uso do sistema.<br>
 * 
 * Implementa a exporta��o de todos os emails enviados com sucesso e quais foram
 * as prefer�ncias dos usu�rios quanto ao formato e tipo de efeito aplicado a
 * foto.
 * 
 * @author maurocl
 * 
 */
public class RelatorioActivity extends Activity implements Constantes {

	private static final String TAG = "RelatorioActivity";

	private static final String SEP = ";";
	private static final String DEM = "\"";

	private static Evento mEvento;

	private List<Participacao> lista = null;

	// nome do arquivo onde ser� gerado o arquivo .csv
	private static final String CSVFILE = "/mnt/sdcard/Pictures/fotoevento/lista.csv";

	private int numFotosPolaroid=0;
	private int numFotosCabine=0;
	private int numFotosCores=0;
	private int numFotosPB=0;

	/**
	 * onCreate()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
				
		if (savedInstanceState != null) {
			numFotosPolaroid = savedInstanceState.getInt("numFotosPolaroid");
			numFotosPB = savedInstanceState.getInt("numFotosPB");
			numFotosCabine = savedInstanceState.getInt("numFotosCabine");
			numFotosCores = savedInstanceState.getInt("numFotosCores");
		}

		setContentView(R.layout.relatorio);

		Log.d(TAG, "*** onCreate() ***");

		String num = null;

		// --------------------------------------------
		// Obt�m informa��es sobre a Intent usada para chamar essa activity
		// --------------------------------------------
		Intent intent = getIntent();

		if (intent != null) {

			// obtem os par�metros da activity
			Bundle params = intent.getExtras();

			// recupera o n� de participantes
			if (params != null) {
				// obt�m o n� de participantes do evento
				if (params.getString("numParticipantes") != null) {
					num = params.getString("numParticipantes");
				}
			}

			// recupera a lista de emails enviados
			if (params.containsKey("br.com.mltech.lista")) {
				lista = (List<Participacao>) params.getSerializable("br.com.mltech.lista");

				calculaEstatisticas(lista);

			} else {
				Log.w(TAG, "onCreate() - lista de participantes est� vazia");
			}

			// recupera as informa��es sobre o evento
			if (params.containsKey(Constantes.EVENTO)) {
				mEvento = (Evento) params.getSerializable(Constantes.EVENTO);
			} else {
				Log.w(TAG, "onCreate() - n�o h� informa��es sobre o evento");
			}

		}

		// ---------------------------------------------

		EditText numParticipantes = (EditText) findViewById(R.id.numParticipantes);

		Button btnExportExcel = (Button) findViewById(R.id.btn1);

		btnExportExcel.setText("Exportar arquivo para Excel");

		// TODO Obter o n�mero de participantes
		numParticipantes.setText(num);

		// Tratamento do evento do bot�o 1
		btnExportExcel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Log.d(TAG, "onCreate() - 'Exportar arquivo para Excel' foi pressionado");

				// Grava a lista no arquivo CSV
				try {

					gravarArquivoCSV(CSVFILE, lista);

					Log.d(TAG, "onCreate() - Lista exportada com sucesso para o arquivo: " + CSVFILE);

				} catch (IllegalArgumentException e) {

					Log.w(TAG, "gravarArquivoCSV()", e);

				} catch (FileNotFoundException e) {

					Log.d(TAG, "gravarArquivoCSV() - Arquivo " + CSVFILE + " n�o foi encontrado");
					// Toast.makeText(this, "Arquivo " + CSVFILE + " n�o foi encontrado.",
					// Toast.LENGTH_SHORT).show();

				} catch (Exception e) {

					Log.d(TAG, "gravarArquivoCSV() - Exce��o encontrada", e);
					// Toast.makeText(this, "Erro na grava��o do arquivo " + CSVFILE +
					// ".", Toast.LENGTH_SHORT).show();

				}

			}
		});

		// Tratamento do evento do bot�o 2
		/*
		 * btn2.setOnClickListener(new OnClickListener() {
		 * 
		 * public void onClick(View v) { // TODO Auto-generated method stub
		 * Log.d(TAG, "Bot�o 2 foi pressionado"); }
		 * 
		 * });
		 */

	}

	/**
	 * Grava um arquivo formato .csv com a rela��o dos emails enviados.<br>
	 * 
	 * O arquivo ser� armazenado filename.
	 * 
	 * @param filename
	 *          nome completo do arquivo
	 * 
	 * @param lista
	 *          Lista de participantes do evento
	 * 
	 * @throws IOException
	 */
	private void gravarArquivoCSV(String filename, List<Participacao> lista) throws IOException {

		if (filename == null) {
			throw new IllegalArgumentException("Nome do arquivo � nulo");
		}

		if (lista == null) {
			throw new IllegalArgumentException("Lista de Participantes est� vazia");
		}

		// abre o arquivo para grava��o no modo grava��o (escrita)
		FileOutputStream fos = new FileOutputStream(filename, false);

		DataOutputStream dos = new DataOutputStream(fos);

		// TODO aqui falta tratar os campos adicionais (se houverem)

		Log.d(TAG, "gravarArquivoCSV() - Processando a lista de participantes ...");
		Toast.makeText(this, "Processando lista de participantes", Toast.LENGTH_SHORT).show();

		Log.d(TAG, "gravarArquivoCSV() - Gravando arquivo: " + filename);
		Toast.makeText(this, "Gravando arquivo: " + filename, Toast.LENGTH_SHORT).show();

		// Cria o cabe�alho do arquivo .csv
		String[] campos = { "nome", "email", "telefone", "TipoFoto", "EfeitoFoto", "Arquivo" };

		String nomeCampos = formataLinha(campos);
		// Escreve o cabe�alho do arquivo
		dos.write(nomeCampos.getBytes());

		// n� de participantes
		int numParticipantes = 0;

		// um participante
		Participante participante = null;

		for (Participacao p : lista) {

			// TODO falta inserir os par�metros adicionais
			// obter os par�metros adicionais a partir de mEvento

			if (p == null) {
				// formata um participante e grava no arquivo
				Log.w(TAG, "gravarArquivoCSV() - participacao � nula");
				return;
			}

			participante = p.getParticipante();

			if (participante == null) {
				Log.w(TAG, "gravarArquivoCSV() - participante � nulo");
				continue;
			}

			String[] linha = { participante.getNome(), participante.getEmail(), p.getStrTipoFoto(), p.getStrEfeitoFoto(),
					p.getNomeArqFoto() };

			String s = formataLinha(linha);
			dos.write(s.getBytes());

			numParticipantes++;

			Log.d(TAG, numParticipantes + ") " + s);

		}

		dos.close();

		Log.d(TAG, "gravarArquivoCSV() - N� total de participantes: " + numParticipantes);
		Toast.makeText(this, numParticipantes + " participantes foram exportados", Toast.LENGTH_SHORT).show();

	}

	/**
	 * Formata uma linha do arquivo .csv<br>
	 * 
	 * Os campos s�o delimitados pelo delimitador (DEM) e pelo separador (SEP).<br>
	 * O default para o delimitador DEM �: " (aspa dupla)<br>
	 * . O default para o separador SEP �: ; (ponto e v�rgula)<br>
	 * 
	 * @param campos
	 *          Cont�m os valores de cada campo exportado.
	 * 
	 * @return Um string formatada com os dados de uma linha do arquivo ou null
	 *         caso o array fornecido seja vazio.
	 */
	private String formataLinha(String[] campos) {

		if (campos == null) {
			return null;
		}

		// obt�m o tamanho do array
		int size = campos.length;

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < (size - 1); i++) {
			sb.append(DEM).append(campos[i]).append(DEM).append(SEP);
		}
		sb.append(DEM).append(campos[size - 1]).append(DEM).append("\n");

		return sb.toString();

	}

	/**
	 * Calcula estat�sticas de uso da aplica��o.<br>
	 * 
	 * @param lista
	 *          Lista de participantes do evento.
	 * 
	 */
	private void calculaEstatisticas(List<Participacao> lista) {

		if (lista == null) {
			throw new IllegalArgumentException("Lista de Participantes est� vazia");
		}

		// n� de participantes
		int numParticipantes = 0;

		for (Participacao p : lista) {

			if (p == null) {
				// formata um participante e grava no arquivo
				Log.w(TAG, "gravarArquivoCSV() - participacao � nula");
				return;
			}

			// Contabiliza o formato da foto
			switch (p.getTipoFoto()) {
			case 1:
				numFotosPolaroid++;
				break;
			case 2:
				numFotosCabine++;
				break;
			default:
				break;
			}

			// Contabiliza o tipo do efeito da foto
			switch (p.getTipoFoto()) {
			case 11:
				numFotosCores++;
				break;
			case 12:
				numFotosPB++;
				break;
			default:
				break;
			}

			numParticipantes++;

		}

		Log.d(TAG, "calculaEstatisticas() - N� total de participantes: " + numParticipantes);
		Log.d(TAG, "calculaEstatisticas() - N� total de fotos Polaroid: " + numFotosPolaroid);
		Log.d(TAG, "calculaEstatisticas() - N� total de fotos Cabine: " + numFotosCabine);
		Log.d(TAG, "calculaEstatisticas() - N� total de fotos coloridas: " + numFotosCores);
		Log.d(TAG, "calculaEstatisticas() - N� total de fotos P&B: " + numFotosPB);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		outState.putInt("numFotosPolaroid", numFotosPolaroid);
		outState.putInt("numFotosPB", numFotosPB);
		outState.putInt("numFotosCabine", numFotosCabine);
		outState.putInt("numFotosCores", numFotosCores);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);

		if (savedInstanceState != null) {
			numFotosPolaroid = savedInstanceState.getInt("numFotosPolaroid");
			numFotosPB = savedInstanceState.getInt("numFotosPB");
			numFotosCabine = savedInstanceState.getInt("numFotosCabine");
			numFotosCores = savedInstanceState.getInt("numFotosCores");
		}

	}

}
