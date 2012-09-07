
package br.com.mltech;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.com.mltech.modelo.Evento;
import br.com.mltech.modelo.Parametros;
import br.com.mltech.modelo.Participacao;
import br.com.mltech.modelo.Participante;
import br.com.mltech.modelo.RepositorioParticipacao;

/**
 * Essa activity é responsável pela geração de relatórios de uso do sistema.<br>
 * 
 * Implementa a exportação de todos os emails enviados com sucesso e quais foram
 * as preferências dos usuários quanto ao formato e tipo de efeito aplicado a
 * foto.
 * 
 * @author maurocl
 * 
 */
public class RelatorioActivity extends Activity implements Constantes {

  private static final String TAG = "RelatorioActivity";

  private static final String SEP = ";";

  private static final String DEM = "\"";

  // Evento
  private static Evento mEvento;

  // Lista de participacao
  private List<Participacao> lista = null;

  // Repositorio de Participacao
  private RepositorioParticipacao repositorio;

  // nome do arquivo onde será gerado o arquivo .csv
  private static final String CSVFILE = "/mnt/sdcard/Pictures/fotoevento/lista.csv";

  private int numFotosPolaroid = 0;

  private int numFotosCabine = 0;

  private int numFotosCores = 0;

  private int numFotosPB = 0;

  private int num = -1;

  /**
   * onCreate()
   */
  @SuppressWarnings("unchecked")
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    if (savedInstanceState != null) {
      num = savedInstanceState.getInt("num");
      numFotosPolaroid = savedInstanceState.getInt("numFotosPolaroid");
      numFotosCabine = savedInstanceState.getInt("numFotosCabine");
      numFotosCores = savedInstanceState.getInt("numFotosCores");
      numFotosPB = savedInstanceState.getInt("numFotosPB");
    }

    setContentView(R.layout.relatorio);

    Log.d(TAG, "*** onCreate() ***");

    // --------------------------------------------
    // Obtém informações sobre a Intent usada para chamar essa activity
    // --------------------------------------------
    Intent intent = getIntent();

    if (intent != null) {

      // obtem os parâmetros da activity
      Bundle params = intent.getExtras();

      if (params != null) {

        // recupera as informações sobre o evento
        if (params.containsKey(Constantes.EVENTO)) {

          mEvento = (Evento) params.getSerializable(Constantes.EVENTO);

        } else {

          Log.w(TAG, "onCreate() - não há informações sobre o evento");

        }

      }

      // Obtém a lista de participações no evento
      lista = getListParticipacoes();

      if (lista != null) {
        // obtém o nº de participantes do evento
        num = lista.size();
      }

      // Calcula as estatísticas de uso da aplicação
      calculaEstatisticas(lista);

    }

    // ---------------------------------------------

    EditText numParticipantes = (EditText) findViewById(R.id.numParticipantes);
    EditText editNumFotosPolaroid = (EditText) findViewById(R.id.numFotosPolaroid);
    EditText editNumFotosCabine = (EditText) findViewById(R.id.numFotosCabine);
    EditText editNumFotosColoridas = (EditText) findViewById(R.id.numFotosColoridas);
    EditText editNumFotosPB = (EditText) findViewById(R.id.numFotosPB);

    numParticipantes.setText(String.valueOf(num));
    
    editNumFotosPolaroid.setText(String.valueOf(numFotosPolaroid));
    editNumFotosCabine.setText(String.valueOf(numFotosCabine));
    editNumFotosColoridas.setText(String.valueOf(numFotosCores));
    editNumFotosPB.setText(String.valueOf(numFotosPB));

    Button btnExportExcel = (Button) findViewById(R.id.btn1);
    btnExportExcel.setText("Exportar arquivo para Excel");

    Button btnExibe = (Button) findViewById(R.id.btn2);
    btnExibe.setText("Exibe participantes");

    // Tratamento do evento do botão 1
    btnExportExcel.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Log.d(TAG, "onCreate() - 'Exportar arquivo para Excel' foi pressionado");

        // Grava a lista no arquivo CSV
        try {

          List<Participacao> lista = getListParticipacoes();

          //gravarArquivoCSV(CSVFILE, lista);
          gravarArquivoCSV(CSVFILE);

          Log.d(TAG, "onCreate() - Lista exportada com sucesso para o arquivo: " + CSVFILE);

        } catch (IllegalArgumentException e) {

          Log.w(TAG, "gravarArquivoCSV()", e);

        } catch (FileNotFoundException e) {

          Log.d(TAG, "gravarArquivoCSV() - Arquivo " + CSVFILE + " não foi encontrado");
          // Toast.makeText(this, "Arquivo " + CSVFILE + " não foi encontrado.",
          // Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

          Log.d(TAG, "gravarArquivoCSV() - Exceção encontrada", e);
          // Toast.makeText(this, "Erro na gravação do arquivo " + CSVFILE +
          // ".", Toast.LENGTH_SHORT).show();

        }

      }
    });

    // Tratamento do evento do botão 2
    btnExibe.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        List<Participacao> participacoes = getListParticipacoes();

        logListaParticipacao(participacoes);

      }

    });

  }

  /**
   * Obtem a lista de participacao no evento
   * 
   * @return A lista de participação no evento
   * 
   */
  public List<Participacao> getListParticipacoes() {

    List<Participacao> participacoes = null;

    repositorio = new RepositorioParticipacao(this);

    if (repositorio != null) {

      try {

        participacoes = repositorio.listarParticipacoes();

      } catch (Exception e) {

        Log.w(TAG, "Exceção:", e);

      } finally {

        repositorio.fechar();

      }

    }

    // a lista de participações
    return participacoes;

  }

  /**
   * Exibe a lista de participação no arquivo de log
   * 
   */
  public void logListaParticipacao(List<Participacao> participacoes) {

    if ((participacoes == null)) {
      Log.d(TAG, "Repositório está vazio");
      return;
    }

    Toast.makeText(this, "Número de participações: " + participacoes.size(), Toast.LENGTH_SHORT).show();
    
    Log.d(TAG, "Número de participações: " + participacoes.size());

    int i = 0;

    for (Participacao p : participacoes) {

      i++;

      Log.d(TAG, i + ": participacao ==> " + p.toString());

    }

  }

  /**
   * Grava um arquivo formato .csv com a relação dos emails enviados.<br>
   * 
   * O arquivo será armazenado filename.
   * 
   * @param filename
   *          nome completo onde será salvo o arquivo
   * 
   * @param listaDeParticipacao
   *          Lista de participações no evento
   * 
   * @throws IOException
   */
  private void gravarArquivoCSV(String filename, List<Participacao> listaDeParticipacao) throws IOException {

    if (filename == null) {
      throw new IllegalArgumentException("Nome do arquivo é nulo");
    }

    if (listaDeParticipacao == null) {
      throw new IllegalArgumentException("Lista de Participantes está vazia");
    }

    // abre o arquivo para gravação no modo gravação (escrita)
    FileOutputStream fos = new FileOutputStream(filename, false);

    DataOutputStream dos = new DataOutputStream(fos);

    // verifica a existencia de parâmetros adicionais
    if (mEvento != null) {

      Parametros p = mEvento.getParametros();

      if (p != null) {
        // evento possui parâmetros adicionais associados

        // Obtém o nº de parâmetros preenchidos
        int numParametros = p.numParametrosPreenchidos();

        Log.d(TAG, "Nome dos campos adicionais:");
        for (int i = 0; i < numParametros; i++) {
          Log.d(TAG, "  Parâmetro: " + i + " = " + p.getParametro(i));
        }

      }
      else {
        // evento não possui parâmetros adicionais associados
      }

    }

    Log.d(TAG, "gravarArquivoCSV() - Processando a lista de participantes ...");
    Toast.makeText(this, "Processando lista de participantes", Toast.LENGTH_SHORT).show();

    Log.d(TAG, "gravarArquivoCSV() - Gravando arquivo: " + filename);
    Toast.makeText(this, "Gravando arquivo: " + filename, Toast.LENGTH_SHORT).show();

    // TODO alterar para incluir os campos opcionais

    // Cria o cabeçalho do arquivo .csv
    String[] linhaCabecalho = { "nome", "email", "telefone", "TipoFoto", "EfeitoFoto", "Arquivo", "Param1", "Param2", "Param3",
        "Param4",
        "Param5" };

    // Escreve a linha de cabeçalho
    String nomeCampos = formataLinha(linhaCabecalho);

    // Escreve o cabeçalho do arquivo
    dos.write(nomeCampos.getBytes());

    // Escreve a linha de cabeçalho no arquivo de log
    Log.d(TAG, nomeCampos);

    // nº de participantes
    int numParticipantes = 0;

    // um participante
    Participante participante = null;

    for (Participacao participacao : listaDeParticipacao) {

      if (participacao == null) {
        // formata um participante e grava no arquivo
        Log.w(TAG, "gravarArquivoCSV() - participacao é nula");
        return;
      }

      participante = participacao.getParticipante();

      if (participante == null) {
        Log.w(TAG, "gravarArquivoCSV() - participante é nulo");
        continue;
      }

      String p1 = "", p2 = "", p3 = "", p4 = "", p5 = "";
      Parametros ppp = participante.getParametros();
      if (ppp != null) {
        p1 = ppp.getParametro(0);
        p2 = ppp.getParametro(1);
        p3 = ppp.getParametro(2);
        p4 = ppp.getParametro(3);
        p5 = ppp.getParametro(4);
      }

      String[] linha = { participante.getNome(), participante.getEmail(), participacao.getStrTipoFoto(),
          participacao.getStrEfeitoFoto(),
          participacao.getNomeArqFoto(), p1, p2, p3, p4, p5 };

      // Formata uma linhsa do arquivo
      String s = formataLinha(linha);

      // Escreve a linha formatada no arquivo
      dos.write(s.getBytes());

      // incrementa o nº de participantes
      numParticipantes++;

      // Exibe o linha no arquivo de log
      Log.d(TAG, numParticipantes + ") " + s);

    }

    // Fecha o arquivo
    dos.close();

    Log.d(TAG, "gravarArquivoCSV() - Nº total de participantes: " + numParticipantes);
    Toast.makeText(RelatorioActivity.this, numParticipantes + " participantes foram exportados", Toast.LENGTH_LONG).show();

  }

  /**
   * Grava um arquivo formato .csv com a relação dos emails enviados.<br>
   * 
   * @param filename
   *          Nome do arquivo onde será gravado a lista de participantes
   * 
   * @throws IOException
   */
  private void gravarArquivoCSV(String filename) throws IOException {

    if (filename == null) {
      throw new IllegalArgumentException("Nome do arquivo é nulo");
    }

    // Obtem a lista de participação no evento
    List<Participacao> listaDeParticipacao = getListParticipacoes();

    // Grava um arquivo contendo informações sobre os participantes do evento
    gravarArquivoCSV(filename, listaDeParticipacao);

  }

  /**
   * Formata uma linha do arquivo .csv<br>
   * 
   * Os campos são delimitados pelo delimitador (DEM) e pelo separador (SEP).<br>
   * O default para o delimitador DEM é: " (aspa dupla)<br>
   * . O default para o separador SEP é: ; (ponto e vírgula)<br>
   * 
   * @param campos
   *          Contém os valores de cada campo exportado.
   * 
   * @return Um string formatada com os dados de uma linha do arquivo ou null
   *         caso o array fornecido seja vazio.
   */
  private String formataLinha(String[] campos) {

    if (campos == null) {
      return null;
    }

    // obtém o tamanho do array
    int size = campos.length;

    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < (size - 1); i++) {
      sb.append(DEM).append(campos[i]).append(DEM).append(SEP);
    }

    sb.append(DEM).append(campos[size - 1]).append(DEM).append("\n");

    return sb.toString();

  }

  /**
   * Calcula estatísticas de uso da aplicação.<br>
   * 
   * @param listaDeParticipantes
   *          Lista de participantes do evento.
   * 
   */
  private void calculaEstatisticas(List<Participacao> listaDeParticipantes) {

    if (listaDeParticipantes == null) {
      throw new IllegalArgumentException("Lista de Participantes está vazia");
    }

    // nº de participantes
    int numParticipantes = 0;

    for (Participacao participacao : listaDeParticipantes) {

      if (participacao == null) {
        // formata um participante e grava no arquivo
        Log.w(TAG, "gravarArquivoCSV() - participacao é nula");
        return;
      }

      // Contabiliza o formato da foto
      switch (participacao.getTipoFoto()) {
        case Constantes.TIPO_FOTO_POLAROID:
          numFotosPolaroid++;
          break;
        case Constantes.TIPO_FOTO_CABINE:
          numFotosCabine++;
          break;
        default:
          break;
      }

      // Contabiliza o tipo do efeito da foto
      switch (participacao.getEfeitoFoto()) {
        case Constantes.CORES:
          numFotosCores++;
          break;
        case Constantes.PB:
          numFotosPB++;
          break;
        default:
          break;
      }

      numParticipantes++;

    }

    Log.d(TAG, "calculaEstatisticas() - Nº total de participantes: " + numParticipantes);
    Log.d(TAG, "calculaEstatisticas() - Nº total de fotos Polaroid: " + numFotosPolaroid);
    Log.d(TAG, "calculaEstatisticas() - Nº total de fotos Cabine: " + numFotosCabine);
    Log.d(TAG, "calculaEstatisticas() - Nº total de fotos coloridas: " + numFotosCores);
    Log.d(TAG, "calculaEstatisticas() - Nº total de fotos P&B: " + numFotosPB);

    // Exibe o nº total de registros
    Toast.makeText(this, "Nº total de participantes: " + numParticipantes, Toast.LENGTH_LONG).show();

  }

  @Override
  /**
   * 
   */
  protected void onSaveInstanceState(Bundle outState) {

    super.onSaveInstanceState(outState);

    outState.putInt("num", num);
    outState.putInt("numFotosPolaroid", numFotosPolaroid);
    outState.putInt("numFotosPB", numFotosPB);
    outState.putInt("numFotosCabine", numFotosCabine);
    outState.putInt("numFotosCores", numFotosCores);

  }

  @Override
  /**
   * 
   */
  protected void onRestoreInstanceState(Bundle savedInstanceState) {

    super.onRestoreInstanceState(savedInstanceState);

    if (savedInstanceState != null) {
      numFotosPolaroid = savedInstanceState.getInt("numFotosPolaroid");
      numFotosPB = savedInstanceState.getInt("numFotosPB");
      numFotosCabine = savedInstanceState.getInt("numFotosCabine");
      numFotosCores = savedInstanceState.getInt("numFotosCores");
      num = savedInstanceState.getInt("num");
    }

  }

  /**
   * 
   * @param c
   */
  private void zzz(Cursor c) {

    int numColunas = c.getColumnCount();

    Log.d(TAG, "Nº de colunas: " + numColunas);

    // Obtém o nome das colunas
    String[] nomeColunas = c.getColumnNames();

    int i = 0;
    // exibe o nome das colunas    
    for (String colName : nomeColunas) {
      Log.d(TAG, "coluna(" + i + "):" + colName);
      i++;
    }

  }

}
