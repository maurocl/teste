package br.com.mltech;

import java.util.Arrays;


/**
 * 
 * @author maurocl
 *
 */
public class Memory {
  
  /**
   * Tamanho da mem�ria
   */
  private int size;
  
  /**
   * Array de posi��es
   */
  private int[] pos;

  /**
   * Construtor - Cria uma mem�ria com 100 posi��es 
   */
  public Memory() {
    this(100);    
  }

  
  /**
   * Construtor - Cria uma mem�ria com n posi��es 
   * @param size tamanho da mem�ria
   * 
   */
  public Memory(int size) {

    super();
    this.size = size;
    pos = new int[this.size];
    
  }
  
  /**
   * L� posi��o p da mem�ria
   * 
   * @param p Posi��o da mem�ria
   * 
   * @return O conte�do da mem�ria na posi��o p
   * 
   */
  int read(int p) {
    return pos[p];
  }
  
  /**
   * Escreve um valor na posi��o de mem�ria
   * 
   * @param p posi��o de mem�ria
   * @param value valor
   */
  void write(int p, int value) {
    pos[p]=value;
  }

  /**
   * Limpa a mem�ria (escreve o valor 0 em todas as posi��es)
   * 
   */
  void clear() {
    for(int i=0;i<this.size;i++) {
      pos[i]=0;
    }
  }
  
  @Override
  public String toString() {

    return "Memory [pos=" + Arrays.toString(pos) + "]";
  }


  /**
   * Obtem o tamanho da mem�ria
   * @return
   */
  public int getSize() {
  
    return size;
  }
  
}
