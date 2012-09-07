
package br.com.mltech.modelo;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Definição do nome dos parâmetros adicionais do evento que serão preenchidos
 * pelos participantes.
 * 
 * @author maurocl
 * 
 */
public class Parametros implements Serializable {

  /**
   * define o nº de parâmetros opcionais
   */
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
   * Construtor
   * 
   * Cria um objeto parâmetro com NUM_PARAM posições.
   * 
   */
  public Parametros() {

    this.parametros = new String[NUM_PARAM];
  }

  /**
   * 
   * Cria e inicia um objeto Parametro com um array de parâmetros
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
   * Obtém o nome dos parâmetros.
   * 
   * @return Um array de strings contendo o nome dos parâmetros.
   * 
   */
  public String[] getParametros() {

    return parametros;
  }

  /**
   * Obtem o nome de um parâmetro específico dado seu índice.
   * 
   * @param index
   *          index Índice do array
   * 
   * @return Retorna o nome do parâmetros
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
   * Atualiza o nome no índice indicado
   * 
   * @param index
   *          index Índice do array
   * 
   * @param value
   *          Atualiza o elemento da posição índice
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
   * Obtem o nº de parâmetros preenchidos.
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
   * Verifica se o índice é válido em um array
   * 
   * @param index
   *          Índice do array
   * 
   * @return Retorna true se o índice do array for válido ou false caso
   *         contrário
   */
  private boolean isValidIndex(int index) {

    return ((index >= 0) && (index < NUM_PARAM));

  }

}
