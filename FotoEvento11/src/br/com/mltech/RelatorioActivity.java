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
import br.com.mltech.modelo.Evento;
import br.com.mltech.modelo.Participacao;
import br.com.mltech.modelo.Participante;

/**
 * RelatorioActivity
 * 
 * Essa activity � respons�vel pela gera��o de relat�rios de uso do sistema.
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

  private static final String CSVFILE = "lista.csv";

  /**
   * onCreate()
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.relatorio);

    String num = null;

    // --------------------------------------------
    // Obt�m informa��es sobre a Intent usada para chamar essa activity
    // --------------------------------------------
    Intent intent = getIntent();

    if (intent != null) {

      // verifica se existem par�metros existentes
      Bundle params = intent.getExtras();

      // recupera o n� de participantes
      if (params != null) {
        // obt�m o n� de participantes do evento
        if (params.getString("numParticipantes") != null) {
          num = params.getString("numParticipantes");
        }
      }

      // recupera a lista de emails enviados
      if (params.getSerializable("br.com.mltech.lista") != null) {
        lista = (List<Participacao>) params.getSerializable("br.com.mltech.lista");
      }

      // recupera as informa��es sobre o evento
      if (params.getSerializable("br.com.mltech.evento") != null) {
        mEvento = (Evento) params.getSerializable("br.com.mltech.evento");
      }

    }

    // ---------------------------------------------

    EditText numParticipantes = (EditText) findViewById(R.id.numParticipantes);

    Button btn1 = (Button) findViewById(R.id.btn1);
    Button btn2 = (Button) findViewById(R.id.btn2);
    /*
     * Button btn3 = (Button) findViewById(R.id.btn3); Button btn4 = (Button)
     * findViewById(R.id.btn4);
     */

    btn1.setText("Exportar arquivo para Excel");

    // TODO Obter o n�mero de participantes
    numParticipantes.setText(num);

    // Tratamento do evento do bot�o 1
    btn1.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Log.d(TAG, "onCreate() - Bot�o 1 foi pressionado");

        boolean b = gravarArquivoCSV(CSVFILE, lista);

        if (b) {
          Log.d(TAG, "onCreate() - Lista exportada com sucesso");
        } else {
          Log.d(TAG, "onCreate() - Falha na exporta��o da lista");
        }

      }
    });

    // Tratamento do evento do bot�o 2
    btn2.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {
        // TODO Auto-generated method stub
        Log.d(TAG, "Bot�o 2 foi pressionado");
      }
    });

    // Tratamento do evento do bot�o 3
    /*
     * btn3.setOnClickListener(new OnClickListener() {
     * 
     * public void onClick(View v) { // TODO Auto-generated method stub
     * Log.d(TAG, "Bot�o 3 foi pressionado"); } });
     */

    // Tratamento do evento do bot�o 4
    /*
     * btn4.setOnClickListener(new OnClickListener() {
     * 
     * public void onClick(View v) { // TODO Auto-generated method stub
     * Log.d(TAG, "Bot�o 4 foi pressionado"); } });
     */

  }

  /**
   * gravarArquivoCSV(File f, List<Participacao> lista)
   * 
   * O arquivo ser� armazenado em /data/data/<nomeAplicacao>/files/<nomeArquivo>
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

    // n� de participantes
    int numParticipantes = 0;

    if (filename == null) {
      Log.w(TAG, "gravarArquivoCSV() - Nenhum arquivo fornecido !");
      return b;
    }

    if (lista == null) {
      // lista de participantes est� vazia
      Log.d(TAG, "gravarArquivoCSV() - Lista de participantes est� vazia !");
      return false;
    }

    Context myContext = this.getApplicationContext();

    FileOutputStream fos = null;
    DataOutputStream dos = null;

    try {

      fos = myContext.openFileOutput(filename, MODE_PRIVATE);
      dos = new DataOutputStream(fos);

      // abre o arquivo para grava��o no modo grava��o (escrita)

      Log.d(TAG, "gravarArquivoCSV() - Processando a lista de participantes ...");

      Log.d(TAG, "gravarArquivoCSV() - Gravando arquivo: " + filename);

      int contador = 0;

      // Cria o cabe�alho do arquivo .csv
      sb = new StringBuilder();
      sb.append(formatItem("nome"));
      sb.append(formatItem("email"));
      sb.append(formatItem("telefone"));
      sb.append(formatItem("TipoFoto"));
      sb.append(formatItem("EfeitoFoto"));
      sb.append(formatItem("Arquivo"));
      sb.append("\n");
      dos.write(sb.toString().getBytes());

      // TODO aqui falta tratar os campos adicionais (se houverem)
      
      for (Participacao p : lista) {

        sb = new StringBuilder();

        contador++;

        // TODO falta inserir os par�metros adicionais
        // obter os par�metros adicionais a partir de mEvento

        // formata um participante e grava no arquivo
        participante = p.getParticipante();

        sb.append(participante.getNome()).append(SEP);
        sb.append(participante.getEmail()).append(SEP);
        sb.append(participante.getTelefone()).append(SEP);
        sb.append("" + p.getTipoFoto()).append(SEP);
        sb.append("" + p.getEfeitoFoto()).append(SEP);
        sb.append(p.getNomeArqFoto());
        sb.append("\n");

        numParticipantes++;

        Log.d(TAG, contador + ") " + sb.toString());

        dos.write(sb.toString().getBytes());

      }
    } catch (FileNotFoundException e) {
      // TODO: handle exception
      Log.d(TAG, "gravarArquivoCSV() - Arquivo " + filename + " n�o foi encontrado");

    } catch (Exception e) {
      // TODO: handle exception
      Log.d(TAG, "gravarArquivoCSV() - Exce��o encontrada", e);

    } finally {
      try {
        // fecha o arquivo de grava��o
        dos.close();
      } catch (IOException e) {
        Log.w(TAG, "gravarArquivoCSV() - IOException encontrada", e);
      }
    }

    Log.d(TAG, "gravarArquivoCSV() - N� total de participantes: " + numParticipantes);

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
