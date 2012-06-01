package br.com.mltech.modelo;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Parametros.java
 * 
 * Defini��o do nome dos par�metros adicionais do evento que ser�o preenchidos
 * pelos participantes
 * 
 * @author maurocl
 * 
 */
public class Parametros implements Serializable {

  public static final int NUM_PARAM = 5;

  /**
	 * 
	 */
  private static final long serialVersionUID = 3128681269609677314L;

  /**
   * Array de String
   */
  private String[] parametros;

  // private Map<String, String> pp = new HashMap<String, String>();

  /**
   * Parametros()
   * 
   * Cria um objeto par�metro com NUM_PARAM posi��es.
   * 
   */
  public Parametros() {
    this.parametros = new String[NUM_PARAM];
  }

  /**
   * Parametros(String[] parametros)
   * 
   * Cria e inicia um objeto Parametro com um array de par�metros
   * 
   * @param parametros
   *          String[]
   * 
   */
  public Parametros(String[] parametros) {
    if (parametros == null) {
      this.parametros = new String[NUM_PARAM];
    } else {
      this.parametros = parametros;
    }
  }

  /**
   * getParametros()
   * 
   * @return
   */
  public String[] getParametros() {
    return parametros;
  }

  /**
   * getParametro(int index)
   * 
   * @param index
   *          index �ndice do array
   * 
   * @return Retorna o conte�do do �ndice
   * 
   */
  public String getParametro(int index) {

    String s = null;

    if (isValidIndex(index)) {
      s = parametros[index];
    }
    return s;

  }

  /**
   * setParametro(int index, String value)
   * 
   * @param index
   *          index �ndice do array
   * 
   * @param value
   *          Atualiza o elemento da posi��o �ndice
   * 
   */
  public void setParametro(int index, String value) {

    if (isValidIndex(index)) {
      parametros[index] = value;
    }

  }

  /**
   * toString()
   */
  @Override
  public String toString() {
    return "Parametros [parametros=" + Arrays.toString(parametros) + "]";
  }

  /**
   * numParametrosPreenchidos()
   * 
   * @return o numero de elementos preenchidos
   */
  public int numParametrosPreenchidos() {

    int num = 0;

    for (int i = 0; i < parametros.length; i++) {
      if (parametros[i] != null) {
        num++;
      }

    }

    return num;

  }

  /**
   * isValidIndex(int index)
   * 
   * Verifica se um �ndice � v�lido em um array
   * 
   * @param index
   *          �ndice do array
   * 
   * @return Retorna true se o �ndice do array for v�lido ou false caso
   *         contr�rio
   */
  private boolean isValidIndex(int index) {
    return ((index >= 0) && (index < NUM_PARAM));
  }

}
