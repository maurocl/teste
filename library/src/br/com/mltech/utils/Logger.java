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
   * indica o modo de grava��o
   */
  private boolean append;

  /**
   * habilita/desabilita o log.
   */
  private boolean enabled;

  /**
   * BufferWriter
   */
  private BufferedWriter bw;

  /**
   * Construtor
   * 
   * @param filename
   *          Nome do arquivo onde ser� gerado o log.
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
   *          Nome do arquivo onde ser� gerado o log.
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

    bw = new BufferedWriter(new FileWriter(filename, append));

    Log.d(TAG, "Logger() - inicio do log");

  }

  /**
   * Loga a mensagem (se o log estiver habilitado)
   * 
   * @param str
   *          Mensagem
   * 
   * @throws IOException
   */
  public void print(String str) throws IOException {

    if (enabled) {
      bw.append(str);
    }

  }

  /**
   * Loga a mensagem e pula uma linha (se o log estiver habilitado)
   * 
   * @param str
   *          Mensagem
   * 
   * @throws IOException
   */
  public void println(String str) throws IOException {

    if (enabled) {
      StringBuilder sb = new StringBuilder();

      sb.append(getDataHora()).append(" ").append(str);

      print(sb.toString());
      bw.append("\n");
    }

  }

  /**
   * 
   * @throws IOException
   */
  public void flush() throws IOException {

    bw.flush();
  }

  /**
   * Fecha o arquivo de log.
   * 
   * @throws IOException
   */
  public void close() {

    try {
      Log.d(TAG, "Logger() - fim do log");
      bw.close();
    } catch (Exception e) {
      Log.w(TAG, "close() - erro ao fechar o arquivo", e);
    }

  }

  /**
   * Obt�m o nome do arquivo de log.
   * 
   * @return O nome do arquivo de log
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
   * Obt�m a data e a hora formatada como dd/MM/yyyy HH:mm:ss
   * 
   * @return A data e a hora formatada.
   */
  private static String getDataHora() {

    return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());

  }

  /**
   * Retorna o modo de grava��o do arquivo de log.
   * 
   * @return true indica adi��o de informa��o ao arquivo e false indica a
   *         cria��o de um novo arquivo.
   */
  public boolean isAppend() {

    return append;
  }

  /**
   * Indica se o sistema de log est� funcionando.
   * 
   * @return
   */
  public boolean isEnabled() {

    return enabled;
  }

  /**
   * Altera o modo de log
   * 
   * @param enabled
   */
  public void setEnabled(boolean enabled) {

    this.enabled = enabled;
  }

}
