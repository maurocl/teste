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
 * RelatorioActivity
 * 
 * Essa activity é responsável pela geração de relatórios de uso do sistema.
 * 
 * @author maurocl
 * 
 */
public class RelatorioActivity extends Activity {

  private static final String TAG = "RelatorioActivity";

  private static final String SEP = ";";
  private static final String DEM = "\"";

  private static Evento mEvento;

  private List<Participacao> lista = null;

  // nome do arquivo onde será gerado o arquivo .csv
  private static final String CSVFILE = "lista.csv";

  /**
   * onCreate()
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.relatorio);

    Log.d(TAG, "*** onCreate() ***");

    String num = null;

    // --------------------------------------------
    // Obtém informações sobre a Intent usada para chamar essa activity
    // --------------------------------------------
    Intent intent = getIntent();

    if (intent != null) {

      // obtem os parâmetros da activity
      Bundle params = intent.getExtras();

      // recupera o nº de participantes
      if (params != null) {
        // obtém o nº de participantes do evento
        if (params.getString("numParticipantes") != null) {
          num = params.getString("numParticipantes");
        }
      }

      // recupera a lista de emails enviados
      if (params.getSerializable("br.com.mltech.lista") != null) {
        lista = (List<Participacao>) params.getSerializable("br.com.mltech.lista");
      } else {
        Log.w(TAG, "onCreate() - lista de participantes está vazia");
      }

      // recupera as informações sobre o evento
      if (params.getSerializable("br.com.mltech.evento") != null) {
        mEvento = (Evento) params.getSerializable("br.com.mltech.evento");
      } else {
        Log.w(TAG, "onCreate() - não há informações sobre o evento");
      }

    }

    // ---------------------------------------------

    EditText numParticipantes = (EditText) findViewById(R.id.numParticipantes);

    Button btn1 = (Button) findViewById(R.id.btn1);
    // Button btn2 = (Button) findViewById(R.id.btn2);

    // Button btn3 = (Button) findViewById(R.id.btn3);
    // Button btn4 = (Button) findViewById(R.id.btn4);

    btn1.setText("Exportar arquivo para Excel");

    // TODO Obter o número de participantes
    numParticipantes.setText(num);

    // Tratamento do evento do botão 1
    btn1.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Log.d(TAG, "onCreate() - Botão 1 foi pressionado");

        boolean gravou = gravarArquivoCSV(CSVFILE, lista);

        if (gravou) {
          Log.d(TAG, "onCreate() - Lista exportada com sucesso");
        } else {
          Log.d(TAG, "onCreate() - Falha na exportação da lista");
        }

      }
    });

    // Tratamento do evento do botão 2
    /*
     * btn2.setOnClickListener(new OnClickListener() {
     * 
     * public void onClick(View v) { // TODO Auto-generated method stub
     * Log.d(TAG, "Botão 2 foi pressionado"); }
     * 
     * });
     */

    // Tratamento do evento do botão 3
    /*
     * btn3.setOnClickListener(new OnClickListener() {
     * 
     * public void onClick(View v) { // TODO Auto-generated method stub
     * Log.d(TAG, "Botão 3 foi pressionado"); } });
     */

    // Tratamento do evento do botão 4
    /*
     * btn4.setOnClickListener(new OnClickListener() {
     * 
     * public void onClick(View v) { // TODO Auto-generated method stub
     * Log.d(TAG, "Botão 4 foi pressionado"); } });
     */

  }

  /**
   * gravarArquivoCSV(File f, List<Participacao> lista)
   * 
   * O arquivo será armazenado em /data/data/<nomeAplicacao>/files/<nomeArquivo>
   * 
   * @param filename
   *          nome do arquivo (sem separadores de path)
   * @param lista
   *          Lista de participantes do evento
   * 
   * @return true se o arquivo foi gravado com sucesso ou false, caso haja algum
   *         problema
   */
  private boolean gravarArquivoCSV(String filename, List<Participacao> lista) {

    boolean b = false;

    StringBuilder sb = new StringBuilder();

    // um participante
    Participante participante = null;

    // nº de participantes
    int numParticipantes = 0;

    if (filename == null) {
      Log.w(TAG, "gravarArquivoCSV() - Nenhum arquivo fornecido !");
      Toast.makeText(this, "Operação abortada - nome do arquivo é nulo", Toast.LENGTH_SHORT).show();
      return b;
    }

    if (lista == null) {
      // lista de participantes está vazia
      Log.w(TAG, "gravarArquivoCSV() - Lista de participantes está vazia !");
      Toast.makeText(this, "Lista de Participantes está vazia - arquivo não foi gerado", Toast.LENGTH_SHORT).show();
      return false;
    }

    Context myContext = this.getApplicationContext();

    if (myContext == null) {
      Log.w(TAG, "gravarArquivoCSV() - não foi possível obter o contexto da aplicação !");
      return false;
    }

    FileOutputStream fos = null;
    DataOutputStream dos = null;

    try {

      fos = myContext.openFileOutput(filename, MODE_PRIVATE);

      if (fos == null) {
        Log.w(TAG, "gravarArquivoCSV() - não foi possível abrir o arquivo " + filename + " para escrita!");
        Toast.makeText(this, "Não foi possível abrir o arquivo " + filename + " para escrita!", Toast.LENGTH_SHORT).show();
        return false;
      }

      dos = new DataOutputStream(fos);

      // abre o arquivo para gravação no modo gravação (escrita)

      Log.d(TAG, "gravarArquivoCSV() - Processando a lista de participantes ...");
      Toast.makeText(this, "Processando lista de participantes", Toast.LENGTH_SHORT).show();

      Log.d(TAG, "gravarArquivoCSV() - Gravando arquivo: " + filename);
      Toast.makeText(this, "Gravando arquivo: " + filename, Toast.LENGTH_SHORT).show();

      // Cria o cabeçalho do arquivo .csv
      sb = new StringBuilder();

      sb.append(formatItem("nome"));
      sb.append(formatItem("email"));
      sb.append(formatItem("telefone"));
      sb.append(formatItem("TipoFoto"));
      sb.append(formatItem("EfeitoFoto"));
      sb.append(formatItem("Arquivo"));
      sb.append("\n");

      // Escreve o cabeçalho do arquivo
      dos.write(sb.toString().getBytes());

      // TODO aqui falta tratar os campos adicionais (se houverem)

      for (Participacao p : lista) {

        sb = new StringBuilder();

        // TODO falta inserir os parâmetros adicionais
        // obter os parâmetros adicionais a partir de mEvento

        if (p == null) {
          // formata um participante e grava no arquivo
          Log.w(TAG, "gravarArquivoCSV() - participacao é nula");          
          return false;
        }

        participante = p.getParticipante();
        
        if (participante == null) {
          Log.w(TAG, "gravarArquivoCSV() - participante é nulo");
          return false;
        }

        sb.append(participante.getNome()).append(SEP);
        sb.append(participante.getEmail()).append(SEP);
        sb.append(participante.getTelefone()).append(SEP);

        sb.append("" + p.getTipoFoto()).append(SEP);
        sb.append("" + p.getEfeitoFoto()).append(SEP);
        sb.append(p.getNomeArqFoto());
        sb.append("\n");

        numParticipantes++;

        Log.d(TAG, numParticipantes + ") " + sb.toString());

        dos.write(sb.toString().getBytes());

      }
    } catch (FileNotFoundException e) {

      Log.d(TAG, "gravarArquivoCSV() - Arquivo " + filename + " não foi encontrado");
      Toast.makeText(this, "Arquivo " + filename + " não foi encontrado.", Toast.LENGTH_SHORT).show();

    } catch (Exception e) {

      Log.d(TAG, "gravarArquivoCSV() - Exceção encontrada", e);
      Toast.makeText(this, "Erro na gravação do arquivo " + filename + ".", Toast.LENGTH_SHORT).show();

    } finally {
      try {
        // fecha o arquivo de gravação
        dos.close();
      } catch (IOException e) {
        Log.w(TAG, "gravarArquivoCSV() - IOException encontrada", e);
      }
    }

    Log.d(TAG, "gravarArquivoCSV() - Nº total de participantes: " + numParticipantes);
    Toast.makeText(this, numParticipantes + " participantes foram exportados", Toast.LENGTH_SHORT).show();

    b = true;

    return b;

  }

  /**
   * formatItem(String s, boolean incluiSEP)
   * 
   * @param s
   * @param incluiSEP
   * @return
   */
  private String formatItem(String s, boolean incluiSEP) {

    StringBuilder sb = new StringBuilder();

    if (incluiSEP) {
      sb.append(DEM).append(s).append(DEM).append(SEP);
    } else {
      sb.append(DEM).append(s).append(DEM);
    }

    return sb.toString();

  }

  /**
   * formatItem(String s)
   * 
   * @param s
   * 
   * @return
   */
  private String formatItem(String s) {

    return formatItem(s, true);

  }

}
