package br.com.mltech.modelo;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Parametros.java
 * 
 * Definição do nome dos parâmetros adicionais do evento que serão preenchidos
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

  private String[] parametros = new String[NUM_PARAM];

  // private Map<String, String> pp = new HashMap<String, String>();

  /**
   * 
   * @param parametros
   */
  public Parametros(String[] parametros) {
    this.parametros = parametros;
  }

  /**
   * 
   * @return
   */
  public String[] getParametros() {
    return parametros;
  }

  /**
   * 
   * @param index
   * @return
   */
  public String getParametro(int index) {

    String s = null;

    if ((index >= 0) && (index < NUM_PARAM)) {
      s = parametros[index];
    }
    return s;

  }

  /**
   * 
   * @param index
   * @param value
   */
  public void setParametro(int index, String value) {

    if ((index >= 0) && (index < NUM_PARAM)) {
      parametros[index] = value;
    }

  }

  /**
   * 
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

}
