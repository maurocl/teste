package br.com.mltech.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

/**
 * Logger
 * 
 * Implementa um sistema de log bem rudimentar.
 * 
 * @author maurocl
 * 
 */
public class Logger {

  private static final String TAG = "Logger";

  /**
   * nome do arquivo
   */
  private String filename;

  /**
   * indica o modo de gravação
   */
  private boolean append;

  /**
   * habilita/desabilita o log.
   */
  private boolean enabled;

  /**
   * BufferWriter
   */
  private BufferedWriter bufferedWriter;

  /**
   * Construtor
   * 
   * @param filename
   *          Nome do arquivo onde será gerado o log.
   * 
   * @throws IOException
   */
  public Logger(String filename) throws IOException {

    this(filename, true);

  }

  /**
   * Construtor
   * 
   * @param filename
   *          Nome do arquivo onde será gerado o log.
   *          
   * @param append
   *          Indica o modo de abertura do arquivo
   * 
   * @throws IOException
   * 
   */
  public Logger(String filename, boolean append) throws IOException {

    this.filename = filename;

    this.enabled = true;

    this.append = append;

    bufferedWriter = new BufferedWriter(new FileWriter(filename, append));

    Log.d(TAG, "Logger() - inicio do log");

  }

  /**
   * Loga a mensagem (se o log estiver habilitado)
   * 
   * @param str
   *          Mensagem
   * 
   */
  public void print(String str) {

    if (enabled) {
      try {
        bufferedWriter.append(str);
      } catch (IOException e) {
        Log.w(TAG, "print() - falha no log", e);
      }
    }

  }

  /**
   * Loga a mensagem e pula uma linha (se o log estiver habilitado)
   * 
   * @param str
   *          Mensagem
   * 
   */
  public void println(String str) {

    if (enabled) {
    	
      StringBuilder sb = new StringBuilder();

      // monta a string de log: data e hora + mensagem + fim de linha
      sb.append(getDataHora()).append(" ").append(str).append("\n");

      print(sb.toString());

    }

  }

  /**
   * Loga uma linha em branco (se o log estiver habilitado)
   * 
   * 
   */
  public void println() {

    if (enabled) {
      StringBuilder sb = new StringBuilder();

      sb.append(getDataHora()).append(" ").append("\n");

      print(sb.toString());

    }

  }

  /**
   * Loga a mensagem e pula uma linha (se o log estiver habilitado)
   * 
   * @param str
   *          Mensagem
   * @param tr
   * 
   */
  public void println(String str, Throwable tr) {

    if (enabled) {
      StringBuilder sb = new StringBuilder();

      sb.append(getDataHora()).append(" ").append(str).append(tr.getMessage()).append("\n");

      print(sb.toString());

    }

  }

  /**
   * Executa um esvaziamento do buffer de escrita para garantir que toda mensagem tenha sido enviada do buffer.
   * 
   * @throws IOException Exceção lançada se houver algum problema no esvaziamento do buffer.
   */
  public void flush() throws IOException {

    bufferedWriter.flush();
  }

  /**
   * Fecha o arquivo de log.
   * 
   * @throws IOException
   */
  public void close() {

    try {
      Log.d(TAG, "Logger() - fim do log");
      bufferedWriter.close();
    } catch (Exception e) {
      Log.w(TAG, "close() - erro ao fechar o arquivo", e);
    }

  }

  /**
   * Obtém o nome do arquivo de log.
   * 
   * @return O nome do arquivo de log.
   * 
   */
  public String getFilename() {

    return filename;
  }

  /**
   * Atualiza o nome do arquivo de log.
   * 
   * @param filename
   *          Nome do arquivo de log
   * 
   */
  public void setFilename(String filename) {

    this.filename = filename;
  }

  /**
   * Obtém a data e a hora formatada como dd/MM/yyyy HH:mm:ss
   * 
   * @return A data e a hora formatada.
   */
  private static String getDataHora() {

    return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());

  }

  /**
   * Retorna o modo de gravação do arquivo de log.
   * 
   * @return true indica adição de informação ao arquivo e false indica a
   *         criação de um novo arquivo.
   */
  public boolean isAppend() {

    return append;
    
  }

  /**
   * Indica se o sistema de log está habilitado ou não.
   * 
   * @return true caso esteja habilitado ou false caso contrário
   * 
   */
  public boolean isEnabled() {

    return enabled;
  }

  /**
   * Altera o modo de log
   * 
   * @param enabled Habilita ou desabilita o log.
   * 
   */
  public void setEnabled(boolean enabled) {

    this.enabled = enabled;
  }

}
